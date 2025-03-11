package com.motos.ilomoto.common.util.constant;

public class EmployeeConstant {
    // Mensajes relacionados con la operación LEER (Read)
    public static final String EMPLOYEE_LIST_SUCCESS = "Empleados listados correctamente.";
    public static final String EMPLOYEE_EMPTY_LIST_ERROR = "No se encontraron empleados disponibles para listar.";
    public static final String EMPLOYEE_RETRIEVAL_SUCCESS = "El empleado con el identificador %s fue recuperado correctamente.";
    public static final String EMPLOYEE_NOT_FOUND_ERROR = "No se encontró el empleado con el identificador %s.";

    // Mensajes relacionados con la operación CREAR (Create)
    public static final String EMPLOYEE_CREATION_SUCCESS = "El empleado %s se creó correctamente.";
    public static final String EMPLOYEE_CREATION_CONFLICT = "No se pudo crear el empleado con el documento único %s porque ya existe.";

    // Mensajes relacionados con la operación ACTUALIZAR (Update)
    public static final String EMPLOYEE_UPDATE_SUCCESS = "El empleado %s se actualizó correctamente.";
    public static final String EMPLOYEE_UPDATE_CONFLICT = "No se actualizo el empleado con el documento único %s porque ya existe.";

    // Mensajes relacionados con la operación ELIMINAR (Delete)
    public static final String EMPLOYEE_DELETE_SUCCESS = "El empleado %s se eliminó correctamente.";
    public static final String EMPLOYEE_DELETE_CONFLICT = "No se puede eliminar el empleado %s porque está relacionado con otros elementos.";

    // Mensajes relacionados con errores generales y validaciones
}
