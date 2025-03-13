package com.motos.ilomoto.common.util.constant;

public class CategoryConstant {
    // Mensajes relacionados con la operación LEER (Read)
    public static final String CATEGORY_LIST_SUCCESS = "Categorías listadas correctamente.";
    public static final String CATEGORY_EMPTY_LIST_ERROR = "No se encontraron categorías disponibles para listar.";
    public static final String CATEGORY_RETRIEVAL_SUCCESS = "La categoría con el identificador %s fue recuperado correctamente.";
    public static final String CATEGORY_NOT_FOUND_ERROR = "No se encontró la categoría con el identificador %s.";

    // Mensajes relacionados con la operación CREAR (Create)
    public static final String CATEGORY_CREATION_SUCCESS = "La categoría %s se creó correctamente.";
    public static final String CATEGORY_CREATION_CONFLICT = "No se pudo crear la categoría con el nombre %s porque ya existe.";

    // Mensajes relacionados con la operación ACTUALIZAR (Update)
    public static final String CATEGORY_UPDATE_SUCCESS = "La categoría %s se actualizó correctamente.";
    public static final String CATEGORY_UPDATE_CONFLICT = "No se actualizo la categoría con el nombre %s porque ya existe.";

    // Mensajes relacionados con la operación ELIMINAR (Delete)
    public static final String CATEGORY_DELETE_SUCCESS = "La categoría %s se eliminó correctamente.";
    public static final String CATEGORY_DELETE_CONFLICT = "No se puede eliminar la categoría %s porque está relacionado con otros elementos.";
}
