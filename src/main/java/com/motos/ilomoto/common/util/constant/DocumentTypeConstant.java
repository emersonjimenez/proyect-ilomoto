package com.motos.ilomoto.common.util.constant;

public class DocumentTypeConstant {
    // Mensajes relacionados con la operación LEER (Read)
    public static final String DOCUMENT_TYPE_LIST_SUCCESS = "Tipos de documento listados correctamente.";
    public static final String DOCUMENT_TYPE_EMPTY_LIST_ERROR = "No se encontraron tipos de documento disponibles para listar.";
    public static final String DOCUMENT_TYPE_RETRIEVAL_SUCCESS = "El tipo de documento con el identificador %s fue recuperado correctamente.";
    public static final String DOCUMENT_TYPE_NOT_FOUND_ERROR = "No se encontró el tipo de documento con el identificador %s.";

    // Mensajes relacionados con la operación CREAR (Create)
    public static final String DOCUMENT_TYPE_CREATION_SUCCESS = "El tipo de documento %s se creó correctamente.";
    public static final String DOCUMENT_TYPE_CREATION_CONFLICT = "No se pudo crear el tipo de documento con el nombre %s porque ya existe.";

    // Mensajes relacionados con la operación ACTUALIZAR (Update)
    public static final String DOCUMENT_TYPE_UPDATE_SUCCESS = "El tipo de documento %s se actualizó correctamente.";
    public static final String DOCUMENT_TYPE_UPDATE_CONFLICT = "No se actualizo el tipo de documento con el nombre %s porque ya existe.";

    // Mensajes relacionados con la operación ELIMINAR (Delete)
    public static final String DOCUMENT_TYPE_DELETE_SUCCESS = "El tipo de documento %s se eliminó correctamente.";
    public static final String DOCUMENT_TYPE_DELETE_CONFLICT = "No se puede eliminar el tipo de documento %s porque está relacionado con otros elementos.";

    // Mensajes relacionados con errores generales y validaciones
}
