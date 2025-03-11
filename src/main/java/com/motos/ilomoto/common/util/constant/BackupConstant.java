package com.motos.ilomoto.common.util.constant;

public class BackupConstant {
    // Mensajes relacionados con la operación LEER (Read)
    public static final String BACKUP_LIST_SUCCESS = "Respaldos listados correctamente.";
    public static final String BACKUP_EMPTY_LIST_ERROR = "No se encontraron respaldos disponibles para listar.";

    // Mensajes relacionados con la operación CREAR (Create)
    public static final String BACKUP_CREATION_SUCCESS = "¡Se generó el respaldo exitosamente!";
    public static final String BACKUP_CREATION_ERROR = "Error al generar el respaldo: %s.";

    // Mensajes relacionados con la operación RESTAURAR (Restore)
    public static final String BACKUP_RESTORE_SUCCESS = "¡Restauración completada exitosamente!";
    public static final String BACKUP_RESTORE_ERROR = "Error al restaurar la base de datos: %s.";
    public static final String BACKUP_RESTORE_FILE_NOT_FOUND = "El archivo especificado no existe en la ruta %s.";
    public static final String BACKUP_RESTORE_INVALID_SQL = "El archivo ZIP no contiene un archivo SQL válido.";
    public static final String BACKUP_RESTORE_INVALID_DB_NAME = "El nombre de la base de datos no es válido.";

    // Mensajes relacionados con errores generales y validaciones
    public static final String BACKUP_GENERAL_ERROR = "Error en la operación de respaldo: %s.";
}