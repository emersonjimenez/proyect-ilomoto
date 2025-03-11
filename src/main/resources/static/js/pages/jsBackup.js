// ==================== IMPORTACIONES ====================
import { ICONS } from '../modules/constants.js';
import { errorHandler } from "../modules/errorHandler.js";
import {modalAlert, notify} from "../modules/alerts.js";

// ==================== CONSTANTES ====================
const API_BACKUP = "http://localhost:8080/ilomoto/backup";

// ==================== FUNCIONES ====================
document.addEventListener("DOMContentLoaded", async () => {
    const restoreBackupModal = new bootstrap.Modal(document.getElementById("restoreBackupModal"));
    const backupModal = new bootstrap.Modal(document.getElementById("backupModal"));

    // Escucha el clic en el botón de respaldo
    document.getElementById("btnBackup").addEventListener("click", async () => {
        try {
            // Realiza la petición GET para iniciar el respaldo
            const res = await axios.get(`${API_BACKUP}/backup`);
            const { statusCode, message } = res.data;

            // Verífica el estado de la respuesta
            if (statusCode === 200) {
                backupModal.hide();
                modalAlert(ICONS.SUCCESS, "¡Respaldo generado!", message);
            }
        } catch (ex) {
            // Manejo de errores
            errorHandler(ex);
        }
    });

    // Escucha el clic en el botón de restauración
    document.getElementById("btnRestoreBackup").addEventListener("click", async () => {
        try {
            const pathname = document.querySelector("#selectRestoreBackup").value;
            if (pathname === null || pathname === "") {
                modalAlert(ICONS.WARNING, "¡Respaldo no generado!", "Seleccione un backup.");
                return;
            }

            const data = { pathname: pathname }; // Nombre del backup

            // Realiza la petición POST para iniciar la restauración
            const res = await axios.post(`${API_BACKUP}/restore`, data, {
                headers: {
                    "Accept": "application/json",
                    "Content-Type": "application/json"
                }
            });
            const { statusCode, message } = res.data;

            // Verífica el estado de la respuesta
            if (statusCode === 200) {
                restoreBackupModal.hide();
                notify(ICONS.SUCCESS, "¡Restauración generada!", message, toLogin);
            }
        } catch (ex) {
            // Manejo de errores
            errorHandler(ex);
        }
    });

    // Escucha el evento de apertura del modal de restauración
    document.getElementById("restoreBackupModal").addEventListener("show.bs.modal", async () => {
        await getAllBackups(); // Cargamos todos los backups
    });
});

// Función que obtiene todos los backups desde el servidor
const getAllBackups = async () => {
    try {
        const res = await axios.get(API_BACKUP);
        const { statusCode, data } = res.data;

        if (statusCode === 200) {
            const select = document.querySelector("#selectRestoreBackup");
            select.innerHTML = '<option value="" disabled selected>Seleccione un backup</option>';

            // Agrega las opciones dinámicamente
            data.forEach(backup => {
                const option = document.createElement("option");
                option.value = backup; // Aquí va el valor real del backup
                option.textContent = backup; // Mostrar el nombre del backup
                select.appendChild(option);
            });
        }
    } catch (ex) {
        errorHandler(ex);
    }
};

const toLogin = () => {
    window.location.href = `/ilomoto/authentication/login`;
};
