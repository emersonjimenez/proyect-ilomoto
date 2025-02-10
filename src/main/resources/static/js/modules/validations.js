// validations.js
import { ICONS, ERROR_TITLES } from './constants.js';
import { modalAlert } from './alerts.js';

// Verifica si el identificador es un número entero positivo
export const isNumeric = (id) => {
    const regexNumInt = /^\d+$/;
    if (!regexNumInt.test(id) || Number(id) <= 0) {
        modalAlert(ICONS.WARNING, ERROR_TITLES.CLIENT, "El identificador no es válido.");
        return false;
    }
    return true;
};
