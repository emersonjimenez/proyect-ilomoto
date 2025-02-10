// ==================== IMPORTACIONES ====================
import { ICONS, ERROR_TITLES, ERROR_MESSAGES } from '../modules/constants.js';
import { renderActions, stylePagBtns, marginLenMenu } from "../modules/datatableUtils.js";
import { errorHandler, handleTableError } from "../modules/errorHandler.js";
import { notify, modalAlert } from "../modules/alerts.js";
import { isNumeric } from "../modules/validations.js";

// ==================== CONSTANTES ====================
/** API y rutas para “brand”  */
const API_BRAND = "http://localhost:8080/ilomoto/brand";

/** Selectores DOM */
const SELECTORS = {
    formAdd: "#formAdd",
    formUpdate: "#formUpdate",
    tableBrand: "#tableBrand"
};

// ==================== EVENTOS ====================
document.addEventListener("DOMContentLoaded", async () => {
    const bodyId = document.body.id;
    if (bodyId === "brandsPage") await initBrandsPage();
    else if (bodyId === "newBrandPage") await initNewBrandPage();
    else if (bodyId === "updateBrandPage") await initUpdateBrandPage();
});

// ==================== FUNCIONES ====================

/**
 * Inicializa la página de listado de marcas.
 */
const initBrandsPage = async () => {
    await loadTable();
    document.addEventListener("click", async evt => {
        const btn = evt.target.closest("button"); // Detecta el botón más cercano
        if (!btn) return; // Si no es un botón, sale

        evt.preventDefault();
        const id = btn.dataset.id; // Obtiene el ID de la marca

        // Si es botón de actualizar o eliminar, ejecuta la acción correspondiente
        if (btn.classList.contains("btn-update")) toUpdate(id);
        else if (btn.classList.contains("btn-delete")) await confirmDelete(id);
    });
};

/**
 * Inicializa la página para agregar una marca.
 */
const initNewBrandPage = async () => {
    document.querySelector(SELECTORS.formAdd).addEventListener("submit", async evt => {
        evt.preventDefault();
        const data = { name: document.querySelector("#name").value };

        try {
            const res = await axios.post(`${API_BRAND}`, data, {
                headers: {
                    "Accept": "application/json",
                    "Content-Type": "application/json"
                }
            });
            const { statusCode, message } = res.data;
            if (statusCode === 201) { // CREATED
                document.querySelector(SELECTORS.formAdd).reset();
                notify(ICONS.SUCCESS, "¡Marca registrada!", message, toList);
            }
        } catch (ex) {
            errorHandler(ex);
        }
    });
};

/**
 * Inicializa la página para actualizar una marca.
 */
const initUpdateBrandPage = async () => {
    await loadDataForm(); // Carga datos en el formulario
    document.querySelector(SELECTORS.formUpdate).addEventListener("submit", async evt => {
        evt.preventDefault();
        const id = document.querySelector("#idBrand").value;
        const data = { name: document.querySelector("#name").value };

        if (!isNumeric(id)) return;
        try {
            const res = await axios.put(`${API_BRAND}/${id}`, data, {
                headers: {
                    "Accept": "application/json",
                    "Content-Type": "application/json"
                }
            });
            const { statusCode, message } = res.data;
            if (statusCode === 200) { // OK
                document.querySelector(SELECTORS.formUpdate).reset();
                notify(ICONS.SUCCESS, "¡Marca actualizada!", message, toList);
            }
        } catch (ex) {
            errorHandler(ex);
        }
    });
};

/**
 * Carga los datos de una marca en el formulario de actualización.
 */
const loadDataForm = async () => {
    try {
        // Obtener el id en la URL
        const id = window.location.pathname.split("/").pop();
        if (!isNumeric(id)) return;

        const res = await axios.get(`${API_BRAND}/${id}`);
        const { statusCode, data } = res.data;
        if (statusCode === 200) {
            document.getElementById("idBrand").value = data.idBrand;
            document.getElementById("name").value = data.name;
        }
    } catch (ex) {
        errorHandler(ex);
    }
};

/**
 * Inicializa la tabla de marcas.
 */
const loadTable = async () => {
    try {
        // Destruye DataTable si ya existe
        if ($.fn.DataTable.isDataTable(SELECTORS.tableBrand)) {
            $(SELECTORS.tableBrand).DataTable().destroy();
        }

        $(SELECTORS.tableBrand).DataTable({
            serverSide: true,
            ajax: {
                url: `${API_BRAND}`,
                method: "GET",
                dataSrc: (json) => { // Reestructurar el JSON
                    json.draw = json.data.draw;
                    json.recordsTotal = json.data.recordsTotal;
                    json.recordsFiltered = json.data.recordsFiltered;
                    return json.data.data;
                },
                error: (xhr) => {
                    handleTableError(xhr)
                }
            },
            columns: [
                { data: "idBrand" }, // Columna ID
                { data: "name" },    // Columna Nombre
                {                    // Columna Acciones
                    data: null,
                    orderable: false,
                    render: ({ idBrand }) => {
                        return renderActions(idBrand);
                    }
                }
            ],
            keys: true,
            responsive: true,
            pageLength: 5,
            lengthMenu: [[5, 10, 20], [5, 10, 20]],
            language: {
                url: "https://cdn.datatables.net/plug-ins/1.12.1/i18n/es-ES.json"
            },
            columnDefs: [
                { width: "7%", targets: 0 },
                { width: "78%", targets: 1 },
                { width: "15%", targets: 2 },
                { responsivePriority: 1, targets: 0 },
                { responsivePriority: 2, targets: -1 }
            ],
            drawCallback: () => stylePagBtns(), // Estiliza paginación
            initComplete: () => marginLenMenu(SELECTORS.tableBrand) // Ajusta margen del menú
        });
    } catch (ex) {
        modalAlert(ICONS.ERROR, ERROR_TITLES.UNEXPECTED, ERROR_MESSAGES.UNEXPECTED);
    }
};

/**
 * Muestra un diálogo para confirmar eliminación y ejecuta la acción si se confirma.
 */
const confirmDelete = async (id) => {
    const result = await Swal.fire({
        title: "¿Estás seguro?",
        text: "Los datos serán eliminados completamente.",
        showDenyButton: true,
        denyButtonText: "No",
        confirmButtonText: "Sí"
    });
    if (!result.isConfirmed) return;
    await deleteBrand(id);
};

/**
 * Elimina una marca.
 */
const deleteBrand = async (id) => {
    try {
        if (!isNumeric(id)) return;
        const res = await axios.delete(`${API_BRAND}/${id}`, {
            headers: { "Accept": "application/json" }
        });
        const { statusCode, message } = res.data; // res.data.data
        if (statusCode === 200) { // OK
            notify(ICONS.SUCCESS, "¡Marca eliminada!", message, toList);
        }
    } catch (ex) {
        errorHandler(ex);
    }
};

/**
 * Redirige a la página de actualización.
 */
const toUpdate = (id) => {
    window.location.href = `/ilomoto/update-brand/${id}`;
};

/**
 * Redirige a la lista de marcas.
 */
const toList = () => {
    window.location.href = `/ilomoto/brands`;
};
