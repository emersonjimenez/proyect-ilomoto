package com.motos.ilomoto.common.util.constant;

public class JobPositionConstant {
    // Mensajes relacionados con la operación LEER (Read)
    public static final String JOB_POSITION_LIST_SUCCESS = "Cargos de empleado listados correctamente.";
    public static final String JOB_POSITION_EMPTY_LIST_ERROR = "No se encontraron cargos de empleado disponibles para listar.";
    public static final String JOB_POSITION_RETRIEVAL_SUCCESS = "El cargo de empleado con el identificador %s fue recuperado correctamente.";
    public static final String JOB_POSITION_NOT_FOUND_ERROR = "No se encontró el cargo de empleado con el identificador %s.";

    // Mensajes relacionados con la operación CREAR (Create)
    public static final String JOB_POSITION_CREATION_SUCCESS = "El cargo de empleado %s se creó correctamente.";
    public static final String JOB_POSITION_CREATION_CONFLICT = "No se pudo crear el cargo de empleado con el nombre %s porque ya existe.";

    // Mensajes relacionados con la operación ACTUALIZAR (Update)
    public static final String JOB_POSITION_UPDATE_SUCCESS = "El cargo de empleado %s se actualizó correctamente.";
    public static final String JOB_POSITION_UPDATE_CONFLICT = "No se actualizo el cargo de empleado con el nombre %s porque ya existe.";

    // Mensajes relacionados con la operación ELIMINAR (Delete)
    public static final String JOB_POSITION_DELETE_SUCCESS = "El cargo de empleado %s se eliminó correctamente.";
    public static final String JOB_POSITION_DELETE_CONFLICT = "No se puede eliminar el cargo de empleado %s porque está relacionado con otros elementos.";

    // Mensajes relacionados con errores generales y validaciones
}
