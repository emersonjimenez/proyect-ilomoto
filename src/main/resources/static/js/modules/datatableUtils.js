// Función para renderizar los btn acciones
export const renderActions = (id) => {
    return `<div class="text-center">
                <button class="btn btn-sm btn-outline-warning btn-update" data-id="${id}">
                    <i class="ri-pen-nib-line"></i> Editar
                </button>
                <button class="btn btn-sm btn-outline-danger btn-delete" data-id="${id}">
                    <i class="ri-delete-bin-2-line"></i> Eliminar
                </button>
            </div>`;
};

// Función para estilizar los botones de paginación
export const stylePagBtns = () => {
    const paginationContainer = document.querySelector(".dataTables_paginate > .pagination");
    paginationContainer ? paginationContainer.classList.add("pagination-rounded") : null;
};

// Función para agregar margen inferior al menú de longitud
export const marginLenMenu = (table) => { //tableBrand_length
    const idTable = table.replace("#", "").concat("_length");
    const selectElement = document.querySelector(`select[name='${idTable}']`);
    selectElement ? selectElement.classList.add("mb-1") : null;
};
