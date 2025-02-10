// Muestra un message emergente y redirige después de 3 segundos
export const notify = (icon, title, text, callback) => {
    Swal.fire({ icon, title, text }).then(() => callback());
};

// Muestra un message de alerta
export const modalAlert = (icon, title, text) => {
    Swal.fire({ icon, title, text });
};
