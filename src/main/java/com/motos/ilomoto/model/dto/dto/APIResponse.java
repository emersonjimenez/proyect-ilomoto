package com.motos.ilomoto.model.dto.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class APIResponse <T>{ /* Clase genérica */
    private T data;
    private String message;
    private HttpStatus httpStatus;
    private int statusCode;

    public APIResponse(T data, String message, HttpStatus status) {
        this.data = data;
        this.message = message;
        this.httpStatus = status;
        this.statusCode = status.value();
    }
}
