package com.motos.ilomoto.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class DocumentTypeException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private HttpStatus errorStatus;
    private String errorMessage;
}
