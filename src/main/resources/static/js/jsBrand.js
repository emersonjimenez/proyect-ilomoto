const API_C = "/ilomoto/brand";

const URLS = {
    add: `${API_C}/add`,
    update: (id) => `${API_C}/update/${id}`,
    list: `${API_C}/list`,
    findById: (id) => `${API_C}/findById/${id}`,
    delete: (id) => `${API_C}/delete/${id}`
};

const METHODS = {GET: "GET", POST: "POST", PUT: "PUT", PATCH: "PATCH", DELETE: "DELETE"};
const SELECTORS = {formAdd: "#formAdd", formUpdate: "#formUpdate", tableBrand: "#tableBrand"};

document.addEventListener("DOMContentLoaded", async () => {
    const bodyId = document.body.id;
    if (bodyId === "brandsPage") await initBrandsPage();
    else if (bodyId === "newBrandPage") await initNewBrandPage();
    else if (bodyId === "updateBrandPage") await initUpdateBrandPage();
});

const initBrandsPage = async () => {
    await loadTable();

    document.addEventListener("click", async (evt) => {
        const target = evt.target.closest("button"); // Busca el botón más cercano al clic
        if (!target) return; // Si no es un botón, termina

        evt.preventDefault();
        const idBrand = target.dataset.id; // Obtiene el id desde el atributo `data-id`

        // Verifica si el botón es de actualizar
        if (target.classList.contains("btn-update")) redirectPageUpdate(idBrand);
        // Verifica si el botón es de eliminar
        else if (target.classList.contains("btn-delete")) await confirmDelete(idBrand);
    });
};

const initNewBrandPage = async () => {
    document.querySelector(SELECTORS.formAdd).addEventListener("submit", async (evt) => {
        evt.preventDefault();
        const data = {name: document.querySelector('#name').value};

        try {
            const response = await fetch(URLS.add, {
                method: METHODS.POST,
                headers: {
                    "Accept": "application/json",
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(data)
            });
            const json = await response.json();

            if (json.statusCode === 201) {// CREATED = status (201)
                document.querySelector(SELECTORS.formAdd).reset();
                message("¡Marca registrada!", json.message, "success");
            } else {
                const statusText = response.status >= 400 && response.status < 500 ? "Advertencia" : "Error";
                const statusIcon = response.status >= 400 && response.status < 500 ? "warning" : "error";
                showModalAlert(statusIcon, statusText, json.message);
            }
        } catch (e) {
            showModalAlert("error", "Error", "Ocurrió un problema con la solicitud. Inténtelo más tarde.");
        }
    });
};

const initUpdateBrandPage = async () => {
    await loadDataForm(); //Cargar los Datos en el formulario

    document.querySelector(SELECTORS.formUpdate).addEventListener("submit", async (evt) => {
        evt.preventDefault();

        const idBrand = document.querySelector('#idBrand').value;
        const data = {name: document.querySelector('#name').value};

        try {
            const response = await fetch(URLS.update(idBrand), {
                method: METHODS.PUT,
                headers: {
                    "Accept": "application/json",
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(data)
            });
            const json = await response.json();

            if (json.statusCode === 200) {// OK = status (200)
                document.querySelector(SELECTORS.formUpdate).reset();
                message("¡Marca actualizada!", json.message, "success");
            } else {
                const statusText = response.status >= 400 && response.status < 500 ? "Advertencia" : "Error";
                const statusIcon = response.status >= 400 && response.status < 500 ? "warning" : "error";
                showModalAlert(statusIcon, statusText, json.message);
            }
        } catch (e) {
            showModalAlert("error", "Error", "Ocurrió un problema con la solicitud. Inténtelo más tarde.");
        }
    });
};

// Función para cargar los datos en el formulario de actualización
const loadDataForm = async () => {
    try {
        // Obtener el ID de la marca desde la URL - Dividiendo la cadena en un array
        const idBrand = window.location.pathname.split("/").pop(); // Con pop sacamos el último del array
        const response = await fetch(URLS.findById(idBrand));
        const json = await response.json();

        if (json.statusCode === 200) {// OK = status (200)
            document.getElementById("idBrand").value = json.data.idBrand;
            document.getElementById("name").value = json.data.name;
        } else {
            const statusText = response.status >= 400 && response.status < 500 ? "Advertencia" : "Error";
            const statusIcon = response.status >= 400 && response.status < 500 ? "warning" : "error";
            showModalAlert(statusIcon, statusText, json.message);
        }
    } catch (e) {
        showModalAlert("error", "Error", "Ocurrió un problema con la solicitud. Inténtelo más tarde.")
    }
};

// Función para llenar la tabla
const loadTable = async () => {
    try {
        // Destruir DataTable si ya está inicializado
        if ($.fn.DataTable.isDataTable(SELECTORS.tableBrand)) {
            $(SELECTORS.tableBrand).DataTable().destroy();
        }

        $(SELECTORS.tableBrand).DataTable({
            serverSide: true,
            ajax: {
                url: URLS.list,
                method: METHODS.POST
            },
            columns: [
                {data: "idBrand"}, // Columna 1: ID de la marca
                {data: "name"},    // Columna 2: Nombre de la marca
                {
                    data: null,      // Columna 3: Acciones
                    orderable: false,
                    searchTable: false,
                    render: (data) => ( // Renderizar las acciones
                        `<div class="text-center">
                            <button class="btn btn-sm btn-outline-warning btn-update" data-id="${data.idBrand}">
                                 <i class="mdi mdi-tag-text"></i>Editar
                            </button>
                            <button class="btn btn-sm btn-outline-danger btn-delete" data-id="${data.idBrand}">
                                <i class="mdi mdi-tag-remove"></i>Eliminar
                            </button>
                        </div>`
                    )
                }
            ],
            keys: !0,
            responsive: true,
            pageLength: 5,
            lengthMenu: [[5, 10, 20], [5, 10, 20]],
            language: {
                url: "https://cdn.datatables.net/plug-ins/1.12.1/i18n/es-ES.json",
                paginate: {
                    previous: "<i class='mdi mdi-chevron-left'>",
                    next: "<i class='mdi mdi-chevron-right'>"
                }
            },
            columnDefs: [
                {width: "7%", targets: 0}, // Ancho de la columna 1 (ID)
                {width: "78%", targets: 1}, // Ancho de la columna 2 (Nombre)
                {width: "15%", targets: 2}, // Ancho de la columna 3 (Acciones)
                {responsivePriority: 1, targets: 0}, // Prioridad responsiva para la columna 1
                {responsivePriority: 2, targets: -1} // Prioridad responsiva para la columna 3
            ],
            drawCallback: () => { // Callback predefinido de DataTables
                stylePaginationButtons(); // Llamada a la función personalizada
            },
            initComplete: () => { // Callback predefinido de DataTables
                addMarginToLengthMenu(); // Llamada a la función personalizada
            }
        });
    } catch (e) {
        showModalAlert("error", "Error", "Ocurrió un problema con la solicitud. Inténtelo más tarde.")
    }
};

// Función para estilizar los botones de paginación
const stylePaginationButtons = () => {
    const paginationContainer = document.querySelector(".dataTables_paginate > .pagination");
    if (paginationContainer) {
        paginationContainer.classList.add("pagination-rounded");
    }
};

// Función para agregar margen inferior al menú de longitud
const addMarginToLengthMenu = () => {
    const selectElement = document.querySelector('select[name="tableBrand_length"]');
    if (selectElement) {
        selectElement.classList.add('mb-1');
    }
};

// Función para confirmar la eliminación
const confirmDelete = async (idBrand) => {
    const result = await Swal.fire({
        title: "¿Estás seguro?",
        text: "Los datos serán eliminados completamente del sistema",
        showDenyButton: true,
        denyButtonText: "No",
        confirmButtonText: "Sí"
    });

    if (result.isConfirmed) await deleteBrand(idBrand);
    else showModalAlert("info", "Cancelado", "Opción cancelada por el usuario.");
};

// Función para eliminar
const deleteBrand = async (idBrand) => {
    try {
        const response = await fetch(URLS.delete(idBrand), {
            method: METHODS.DELETE,
            headers: {"Accept": "application/json"}
        });
        const json = await response.json();

        if (json.statusCode === 200) {// OK = status (200)
            await loadTable();
            message("¡Marca eliminada!", json.message, "success");
        } else if (response.status === 403) {// SC_FORBIDDEN = status (403)
            showModalAlert("warning", "Acceso Denegado", json.message);
        } else {
            const statusText = response.status >= 400 && response.status < 500 ? "Advertencia" : "Error";
            const statusIcon = response.status >= 400 && response.status < 500 ? "warning" : "error";
            showModalAlert(statusIcon, statusText, json.message);
        }
    } catch (e) {
        showModalAlert("error", "Error", "Ocurrió un problema con la solicitud. Inténtelo más tarde.")
    }
};

// Función para redireccionar al apartado actualizar
const redirectPageUpdate = (idBrand) => {
    window.location.href = `/ilomoto/update-brand/${idBrand}`;
};

// Función para redireccionar al apartado listado
const redirectPageList = () => {
    window.location.href = `/ilomoto/brands`;
};

// Muestra un message emergente y redirige después de 3 segundos
const message = (title, text, icon) => {
    Swal.fire({
        title: title,
        text: text,
        icon: icon
    }).then(() => {
        redirectPageList();
    });
};

const showModalAlert = (icon, title, text) => {
    Swal.fire({
        icon: icon,
        title: title,
        text: text
    });
};
