// ==================== IMPORTACIONES ====================
import { ICONS, ERROR_TITLES, ERROR_MESSAGES } from '../modules/constants.js';
import { renderActions, stylePagBtns, marginLenMenu } from "../modules/datatableUtils.js";
import { errorHandler, handleTableError } from "../modules/errorHandler.js";
import { notify, modalAlert } from "../modules/alerts.js";
import { isNumeric } from "../modules/validations.js";

// ==================== CONSTANTES ====================
/** API y rutas para “brand”  */
const API_EMPLOYEE = "http://localhost:8080/ilomoto/employee";
const API_DOCUMENT_TYPE = "http://localhost:8080/ilomoto/document-type";
const API_JOB_POSITION = "http://localhost:8080/ilomoto/job-position";

/** Selectores DOM */
const SELECTORS = {
    formAdd: "#formAdd",
    formUpdate: "#formUpdate",
    tableEmployee: "#tableEmployee"
};

// ==================== EVENTOS ====================
document.addEventListener("DOMContentLoaded", async () => {
    const bodyId = document.body.id;
    if (bodyId === "employeesPage") await initEmployeesPage();
    else if (bodyId === "newEmployeePage") await initNewEmployeePage();
    else if (bodyId === "updateEmployeePage") await initUpdateEmployeePage();
});

// ==================== FUNCIONES ====================

/**
 * Inicializa la página de listado de empleados.
 */
const initEmployeesPage = async () => {
    await loadTable();
    document.addEventListener("click", async evt => {
        const btn = evt.target.closest("button"); // Detecta el botón más cercano
        if (!btn) return; // Si no es un botón, sale

        evt.preventDefault();
        const id = btn.dataset.id; // Obtiene el ID del Empleado

        // Si es botón de actualizar o eliminar, ejecuta la acción correspondiente
        if (btn.classList.contains("btn-update")) toUpdate(id);
        else if (btn.classList.contains("btn-delete")) await confirmDelete(id);
    });
};

/**
 * Inicializa la página para agregar un Empleado.
 */
const initNewEmployeePage = async () => {
    // Carga selects del formulario en paralelo
    await Promise.all([
        loadDocumentType(null),
        loadEmployeePosition(null),
    ]);

    document.querySelector(SELECTORS.formAdd).addEventListener("submit", async evt => {
        evt.preventDefault();
        const data = {
            idPosition: document.querySelector("#idPosition").value,
            idDocumentType: document.querySelector("#idDocumentType").value,
            firstName: document.querySelector("#firstName").value,
            lastName: document.querySelector("#lastName").value,
            documentNumber: document.querySelector("#documentNumber").value,
            //image: document.querySelector("#image").value,
            salary: document.querySelector("#salary").value
        };

        try {
            const res = await axios.post(`${API_EMPLOYEE}`, data, {
                headers: {
                    "Accept": "application/json",
                    "Content-Type": "application/json"
                }
            });
            const { statusCode, message } = res.data;
            if (statusCode === 201) { // CREATED
                document.querySelector(SELECTORS.formAdd).reset();
                notify(ICONS.SUCCESS, "¡Empleado registrado!", message, toList);
            }
        } catch (ex) {
            errorHandler(ex);
        }
    });
};

/**
 * Inicializa la página para actualizar un Empleado.
 */
const initUpdateEmployeePage = async () => {
    // Carga datos de el formulario
    await loadDataForm();

    document.querySelector(SELECTORS.formUpdate).addEventListener("submit", async evt => {
        evt.preventDefault();
        const id = document.querySelector("#idEmployee").value;
        const data = {
            idPosition: document.querySelector("#idPosition").value,
            idDocumentType: document.querySelector("#idDocumentType").value,
            firstName: document.querySelector("#firstName").value,
            lastName: document.querySelector("#lastName").value,
            documentNumber: document.querySelector("#documentNumber").value,
            //image: document.querySelector("#image").value,
            salary: document.querySelector("#salary").value
        };

        if (!isNumeric(id)) return;
        try {
            const res = await axios.put(`${API_EMPLOYEE}/${id}`, data, {
                headers: {
                    "Accept": "application/json",
                    "Content-Type": "application/json"
                }
            });
            const { statusCode, message } = res.data;
            if (statusCode === 200) { // OK
                document.querySelector(SELECTORS.formUpdate).reset();
                notify(ICONS.SUCCESS, "¡Empleado actualizado!", message, toList);
            }
        } catch (ex) {
            errorHandler(ex);
        }
    });
};

/**
 * Carga los datos de un Empleado en el formulario de actualización.
 */
const loadDataForm = async () => {
    try {
        // Obtener el id en la URL
        const id = window.location.pathname.split("/").pop();
        if (!isNumeric(id)) return;

        const res = await axios.get(`${API_EMPLOYEE}/${id}`);
        const { statusCode, data } = res.data;
        if (statusCode === 200) {
            document.getElementById("idEmployee").value = data.idEmployee;
            document.getElementById("firstName").value = data.firstName;
            document.getElementById("lastName").value = data.lastName;
            document.getElementById("documentNumber").value = data.documentNumber;
            document.getElementById("salary").value = data.documentNumber;

            await Promise.all([
                loadDocumentType(data.idEmployee),
                loadEmployeePosition(data.idEmployee),
            ]);
        }
    } catch (ex) {
        errorHandler(ex);
    }
};

/**
 * Carga los datos de un documento en el formulario de actualización.
 * */
const loadDocumentType = async (idDocumentType) => {
    try {
        const res = await axios.get(`${API_DOCUMENT_TYPE}/list`);
        const { statusCode, data } = res.data;

        if (statusCode === 200 && Array.isArray(data)) {
            const selectElement = document.getElementById("idDocumentType");
            selectElement.innerHTML = "<option disabled selected value=''>Seleccione una opción</option>";

            data.forEach(documentType => { // Recorre el array devuelto por la API
                const option = document.createElement("option");
                option.value = documentType.idDocumentType;
                option.textContent = documentType.name;
                selectElement.appendChild(option);
            });

            // Establecer el valor del select a idDocumentType
            if (idDocumentType !== null) {
                selectElement.value = idDocumentType;
            }
        }
    } catch (ex) {
        errorHandler(ex);
    }
}

/**
 * Carga los datos de un documento en el formulario de actualización.
 * */
const loadEmployeePosition = async (idPosition) => {
    try {
        const res = await axios.get(`${API_JOB_POSITION}/list`);
        const { statusCode, data } = res.data;

        if (statusCode === 200 && Array.isArray(data)) {
            const selectElement = document.getElementById("idPosition");
            selectElement.innerHTML = "<option disabled selected value=''>Seleccione una opción</option>";

            data.forEach(jobPosition => { // Recorre el array devuelto por la API
                const option = document.createElement("option");
                option.value = jobPosition.idPosition;
                option.textContent = jobPosition.name;
                selectElement.appendChild(option);
            });

            // Establecer el valor del select a idPosition
            if (idPosition !== null) {
                selectElement.value = idPosition;
            }
        }
    } catch (ex) {
        errorHandler(ex);
    }
}

/**
 * Inicializa la tabla de empleados.
 */
const loadTable = async () => {
    try {
        // Destruye DataTable si ya existe
        if ($.fn.DataTable.isDataTable(SELECTORS.tableEmployee)) {
            $(SELECTORS.tableEmployee).DataTable().destroy();
        }

        $(SELECTORS.tableEmployee).DataTable({
            serverSide: true,
            ajax: {
                url: `${API_EMPLOYEE}`,
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
                { data: "idEmployee" },
                { data: "firstName" },
                { data: "lastName" },
                {
                    data: null,
                    render: (data) => {
                        if (data.idDocumentType && data.idDocumentType.name) {
                            return `${data.idDocumentType.name}`;
                        } else {
                            return ``;
                        }
                    }
                },
                { data: "documentNumber" },
                { data: "idPosition.name" },
                {
                    data: "salary",
                    render: (data) => {
                        return `$${parseFloat(data).toFixed(2)}`;
                    }
                },
                {
                    data: "image",
                    render: (data) => {
                        if (data) {
                            return `<div style="display: flex; justify-content: center; align-items: center; height: 100%;">
                                    <img src="data:image/jpeg;base64,${data}" alt="Imagen" style="max-width: 100px; max-height: 100px; width: auto; height: auto;"/></div>`;
                        } else {
                            return `<span>Sin imagen</span>`;
                        }
                    }
                },
                {
                    data: "status",
                    render: (data) => {
                        return data ? '<span class="badge text-bg-success">Activo</span>'
                                    : '<span class="badge text-bg-warning">Inactivo</span>';
                    }
                },
                {
                    data: null,
                    orderable: false,
                    render: ({ idEmployee }) => {
                        return renderActions(idEmployee);
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
                { responsivePriority: 1, targets: 0 },
                { responsivePriority: 2, targets: -1 }
            ],
            drawCallback: () => stylePagBtns(), // Estiliza paginación
            initComplete: () => marginLenMenu(SELECTORS.tableEmployee) // Ajusta margen del menú
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
    await deleteEmployee(id);
};

/**
 * Elimina un Empleado.
 */
const deleteEmployee = async (id) => {
    try {
        if (!isNumeric(id)) return;
        const res = await axios.delete(`${API_EMPLOYEE}/${id}`, {
            headers: { "Accept": "application/json" }
        });
        const { statusCode, message } = res.data; // res.data.data
        if (statusCode === 200) { // OK
            notify(ICONS.SUCCESS, "¡Empleado eliminado!", message, toList);
        }
    } catch (ex) {
        errorHandler(ex);
    }
};

/**
 * Redirige a la página de actualización.
 */
const toUpdate = (id) => {
    window.location.href = `/ilomoto/update-employee/${id}`;
};

/**
 * Redirige a la lista de empleados.
 */
const toList = () => {
    window.location.href = `/ilomoto/employees`;
};
