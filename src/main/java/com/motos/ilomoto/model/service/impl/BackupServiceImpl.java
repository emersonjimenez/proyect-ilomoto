package com.motos.ilomoto.model.service.impl;

import com.motos.ilomoto.common.exception.BackupException;
import com.motos.ilomoto.common.util.constant.BackupConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.motos.ilomoto.model.service.abstraction.IBackupService;
import com.motos.ilomoto.model.dto.request.BackupRequest;
import com.motos.ilomoto.model.dto.dto.APIResponse;
import com.smattme.MysqlImportService;
import com.smattme.MysqlExportService;
import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class BackupServiceImpl implements IBackupService {
    @Value("${backup.db.name}")
    private String dbName;

    @Value("${backup.db.username}")
    private String dbUsername;

    @Value("${backup.db.password}")
    private String dbPassword;

    @Value("${backup.db.host}")
    private String dbHost;

    @Value("${backup.db.port}")
    private String dbPort;

    @Value("${backup.directory.path}")
    private String backupDirectoryPath;

    @Value("${temp.directory.path}")
    private String tempDirectoryPath;

    // Constantes de Data Base
    private static final String DROP_DATABASE = " DROP DATABASE IF EXISTS ";
    private static final String CREATE_DATABASE = " CREATE DATABASE IF NOT EXISTS ";

    @Override
    public APIResponse<List<String>> getAllBackups() {
        try {
            // Obtener la lista de nombres de archivos de backup
            List<String> backups = getBackupFileNames();

            if (backups.isEmpty()) {
                throw new BackupException(
                        HttpStatus.NO_CONTENT,
                        BackupConstant.BACKUP_EMPTY_LIST_ERROR
                );
            }

            // Respuesta exitosa
            return new APIResponse<>(
                    backups,
                    BackupConstant.BACKUP_LIST_SUCCESS,
                    HttpStatus.OK
            );
        } catch (Exception e) {
            throw new BackupException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    String.format(BackupConstant.BACKUP_GENERAL_ERROR, e.getMessage())
            );
        }
    }

    /**
     * Obtiene la lista de nombres de archivos de backup en el directorio especificado.
     */
    private List<String> getBackupFileNames() {
        // Directorio donde se almacenan los backups
        File backupDir = new File(backupDirectoryPath);

        // Verificar si el directorio existe y es válido
        if (!backupDir.exists() || !backupDir.isDirectory()) {
            return Collections.emptyList();
        }

        // Lista de archivos en el directorio
        return Arrays.stream(Objects.requireNonNull(backupDir.listFiles()))
                .filter(File::isFile) // Filtrar solo archivos (no directorios)
                .map(File::getName)   // Obtener solo los nombres de archivo
                .collect(Collectors.toList());
    }

    @Override
    public APIResponse<Void> backup() {
        try {
            // Configurar propiedades para el servicio de exportación
            Properties properties = configureExportProperties();

            // Crear el directorio de backups si no existe
            createBackupDirectory();

            // Inicializar y ejecutar el servicio de exportación
            MysqlExportService mysqlExportService = new MysqlExportService(properties);
            mysqlExportService.export();

            // Respuesta exitosa
            return new APIResponse<>(
                    null, BackupConstant.BACKUP_CREATION_SUCCESS,
                    HttpStatus.OK
            );
        } catch (IOException | SQLException | ClassNotFoundException e) {
            throw new BackupException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    String.format(BackupConstant.BACKUP_CREATION_ERROR, e.getMessage())
            );
        }
    }

    /**
     * Configura las propiedades para el servicio de exportación.
     */
    private Properties configureExportProperties() {
        Properties properties = new Properties();
        properties.setProperty(MysqlExportService.DB_NAME, dbName);
        properties.setProperty(MysqlExportService.DB_USERNAME, dbUsername);
        properties.setProperty(MysqlExportService.DB_PASSWORD, dbPassword);
        properties.setProperty(MysqlExportService.DB_HOST, dbHost);
        properties.setProperty(MysqlExportService.DB_PORT, dbPort);

        // Configurar la carpeta temporal para almacenar los archivos generados
        File tempDir = new File(backupDirectoryPath);
        properties.setProperty(MysqlExportService.TEMP_DIR, tempDir.getAbsolutePath());
        properties.setProperty(MysqlExportService.PRESERVE_GENERATED_ZIP, "true");

        return properties;
    }

    /**
     * Crea el directorio de backups si no existe.
     */
    private void createBackupDirectory() {
        File tempDir = new File(backupDirectoryPath);
        if (!tempDir.exists()) {
            tempDir.mkdirs(); // Crea el directorio si no existe
        }
    }

    @Override
    public APIResponse<Void> restore(BackupRequest backupRequest) {
        try {
            // Validar y extraer el archivo SQL
            String extractedSqlFilePath = extractSqlFileFromZip(backupRequest.getPathname());

            // Leer el contenido del archivo SQL
            String sql = readSqlFile(extractedSqlFilePath);

            // Eliminar y recrear la base de datos
            recreateDatabase();

            // Restaurar la base de datos usando el archivo SQL
            boolean result = restoreDatabase(sql);

            // Limpiar el archivo extraído
            Files.deleteIfExists(Paths.get(extractedSqlFilePath));

            if (result) {
                return new APIResponse<>(
                        null, BackupConstant.BACKUP_RESTORE_SUCCESS,
                        HttpStatus.OK
                );
            } else {
                throw new BackupException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        String.format(BackupConstant.BACKUP_RESTORE_ERROR, backupRequest.getPathname())
                );
            }
        } catch (BackupException be) {
            throw be;
        } catch (IOException | SQLException | ClassNotFoundException e) {
            throw new BackupException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    String.format(BackupConstant.BACKUP_RESTORE_ERROR, e.getMessage())
            );
        }
    }

    /**
     * Extrae el archivo SQL del archivo ZIP.
     */
    private String extractSqlFileFromZip(String zipFileName) throws IOException {
        String zipPath = backupDirectoryPath + "/" + zipFileName;

        // Validar si el archivo ZIP existe
        File zipFile = new File(zipPath);
        if (!zipFile.exists()) {
            throw new BackupException(
                    HttpStatus.BAD_REQUEST,
                    String.format(BackupConstant.BACKUP_RESTORE_FILE_NOT_FOUND, zipFileName)
            );
        }

        // Ruta temporal para extraer el archivo SQL
        File tempDirFile = new File(tempDirectoryPath);
        if (!tempDirFile.exists()) {
            tempDirFile.mkdirs();
        }

        // Descomprimir el archivo ZIP y extraer el archivo SQL
        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipPath))) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                if (entry.getName().endsWith(".sql")) {
                    String extractedSqlFilePath = tempDirectoryPath + File.separator + entry.getName();
                    try (FileOutputStream fileOutputStream = new FileOutputStream(extractedSqlFilePath)) {
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = zipInputStream.read(buffer)) > 0) {
                            fileOutputStream.write(buffer, 0, len);
                        }
                    }
                    return extractedSqlFilePath;
                }
            }
        }

        throw new BackupException(
                HttpStatus.BAD_REQUEST,
                BackupConstant.BACKUP_RESTORE_INVALID_SQL
        );
    }

    /**
     * Lee el contenido del archivo SQL.
     */
    private String readSqlFile(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }

    /**
     * Elimina y recrea la base de datos.
     */
    private void recreateDatabase() throws SQLException {
        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://" + dbHost + ":" + dbPort + "/", dbUsername, dbPassword);
             Statement statement = connection.createStatement()) {

            // Validar que el nombre de la base de datos sea seguro
            if (!isValidDatabaseName(dbName)) {
                throw new BackupException(
                        HttpStatus.BAD_REQUEST,
                        BackupConstant.BACKUP_RESTORE_INVALID_DB_NAME
                );
            }

            // Eliminar la base de datos si existe
            statement.executeUpdate(DROP_DATABASE + dbName);

            // Crear la base de datos
            statement.executeUpdate(CREATE_DATABASE + dbName);
        }
    }

    /**
     * Restaura la base de datos usando el archivo SQL.
     */
    private boolean restoreDatabase(String sql) throws SQLException, ClassNotFoundException {
        return MysqlImportService.builder()
                .setDatabase(dbName)
                .setSqlString(sql)
                .setUsername(dbUsername)
                .setPassword(dbPassword)
                .setHost(dbHost)
                .setPort(dbPort)
                .importDatabase();
    }

    /**
     * Valida que el nombre de la base de datos sea seguro.
     */
    private boolean isValidDatabaseName(String dbName) {
        // Solo permite letras, números y guiones bajos
        return dbName.matches("^[a-zA-Z0-9_]+$");
    }
}
