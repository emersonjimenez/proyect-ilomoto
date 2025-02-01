package com.motos.ilomoto.controller.advice;

import com.motos.ilomoto.common.exception.BrandException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Ocurrió un error inesperado en el servidor.");
        response.put("messageError", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(response);
    }

    @ExceptionHandler(BrandException.class)
    public ResponseEntity<Map<String,String>> handleBrandException(BrandException ex) {
        Map<String,String> response = new HashMap<>();
        response.put("message", ex.getErrorMessage());  /* { message: ? } */
        return ResponseEntity.status(ex.getErrorStatus().value()).body(response);
    }
}
