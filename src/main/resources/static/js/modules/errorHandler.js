import { ICONS, ERROR_TITLES, ERROR_MESSAGES } from './constants.js';
import { modalAlert } from "./alerts.js";
import { toPage500 } from "./redirections.js";

// Manejar todos los tipos de errores del catch
export const errorHandler = (ex) => {
    // Se realizó la solicitud y el servidor respondió
    if (ex.response) {
        const { status, data } = ex.response;

        // Error interno del servidor
        if (status === 500) {
            toPage500();
            return;
        }
        // Acceso denegado
        if (status === 403) {
            modalAlert(ICONS.WARNING, ERROR_TITLES.ACCESS_DENIED, data?.message || ERROR_MESSAGES.ACCESS_DENIED);
            return;
        }

        // Mostrar un mensaje de error específico
        const statusIcon = status >= 400 && status < 500 ? ICONS.WARNING : ICONS.ERROR;
        const statusText = status >= 400 && status < 500 ? ERROR_TITLES.CLIENT : ERROR_TITLES.SERVER;
        modalAlert(statusIcon, statusText, data?.message || ERROR_MESSAGES.UNEXPECTED);
    } else if (ex.request) { // Se realizó la solicitud, pero no se recibió respuesta
        modalAlert(ICONS.ERROR, ERROR_TITLES.NETWORK, ERROR_MESSAGES.NETWORK);
    } else { // Algo sucedió al configurar la solicitud
        modalAlert(ICONS.ERROR, ERROR_TITLES.UNEXPECTED, ERROR_MESSAGES.PROCESSING);
    }
};

// Función de errores del servidor de listar
export const handleTableError = (xhr) => {
    const { status } = xhr; // XMLHttpRequest (XHR)
    if (status === 500) {
        toPage500();
        return;
    }
    const statusIcon = status >= 400 && status < 500 ? ICONS.WARNING : ICONS.ERROR;
    const statusText = status >= 400 && status < 500 ? ERROR_TITLES.CLIENT : ERROR_TITLES.SERVER;
    const statusMessage = status >= 400 && status < 500 ? ERROR_MESSAGES.PROCESSING : ERROR_MESSAGES.NETWORK;
    modalAlert(statusIcon, statusText, statusMessage);
};
