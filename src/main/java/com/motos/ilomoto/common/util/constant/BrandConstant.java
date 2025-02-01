package com.motos.ilomoto.common.util.constant;

public class BrandConstant {
    // Mensajes relacionados con la operación LEER (Read)
    public static final String BRAND_LIST_SUCCESS = "Marcas listadas correctamente.";
    public static final String BRAND_EMPTY_LIST_ERROR = "No se encontraron marcas disponibles para listar.";
    public static final String BRAND_RETRIEVAL_SUCCESS = "La marca con el identificador %s fue recuperado correctamente.";
    public static final String BRAND_NOT_FOUND_ERROR = "No se encontró la marca con el identificador %s.";

    // Mensajes relacionados con la operación CREAR (Create)
    public static final String BRAND_CREATION_SUCCESS = "La marca %s se creó correctamente.";
    public static final String BRAND_CREATION_CONFLICT = "No se pudo crear la marca con el nombre %s porque ya existe.";

    // Mensajes relacionados con la operación ACTUALIZAR (Update)
    public static final String BRAND_UPDATE_SUCCESS = "La marca con el identificador %s se actualizó correctamente.";
    public static final String BRAND_UPDATE_CONFLICT = "No se pudo actualizar la marca con el identificador %s debido a un conflicto de datos.";

    // Mensajes relacionados con la operación ELIMINAR (Delete)
    public static final String BRAND_DELETE_SUCCESS = "La marca con el identificador %s se eliminó correctamente.";
    public static final String BRAND_DELETE_CONFLICT = "No se puede eliminar la marca con el identificador %s porque está relacionado con otros elementos.";

    // Mensajes relacionados con errores generales y validaciones
}