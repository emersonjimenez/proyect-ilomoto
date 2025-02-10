document.addEventListener("DOMContentLoaded", function () {
    const paginaActual = document.getElementById("paginaActual").value;

    // Mapa de las páginas y sus grupos correspondientes
    const menuMap = {
        // Dashboard
        /*   Key         value   */
        "dashboard": "dashboards",
        "general-inventory": "dashboards",
        "quarterly-inventory": "dashboards",

        // Usuarios
        "users": "users",
        "new-user": "users",

        // Roles
        "roles": "roles",
        "new-role": "roles",

        // Clientes
        "customers": "customers",
        "new-customer": "customers",

        // Proveedores
        "suppliers": "suppliers",
        "new-supplier": "suppliers",

        // Empleados
        "employees": "employees",
        "new-employee": "employees",

        // Cargo empleado
        "position": "position",
        "new-position": "position",

        // Productos
        "products": "products",
        "new-product": "products",

        // Marca
        "brands": "brands",
        "new-brand": "brands",

        // Categoria
        "categories": "categories",
        "new-categorie": "categories",

        // Tipo de documentos
        "document-types": "document-types",
        "new-document-type": "document-types",

        // Compras
        "new-purchase": "purchases",
        "purchase-details": "purchases",

        // Ventas
        "sale-new": "sales",
        "sale-details": "sales"
    };

    // Obtener el grupo correspondiente según la página actual
    const grupo = menuMap[paginaActual];

    if (grupo) {
        activarMenu(grupo, paginaActual);
    }
});

const activarMenu = (grupo, item) => {
    const grupoElemento = document.getElementById(grupo); /* Ej. dashboards */
    const itemElemento = document.getElementById("item-" + item); /* Ej. item-dashboard */

    if (grupoElemento) {
        grupoElemento.classList.add("show"); /* Desplegamos el Grupo dashboards */
        grupoElemento.parentElement.classList.add("menuitem-active"); /* Activamos el apartado en que nos encontremos*/
    }

    if (itemElemento) {
        itemElemento.parentElement.classList.add("menuitem-active"); /* Activamos el apartado en que nos encontremos*/
    }
};
