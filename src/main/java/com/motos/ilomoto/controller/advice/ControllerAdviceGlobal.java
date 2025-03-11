package com.motos.ilomoto.controller.advice;

import com.motos.ilomoto.common.exception.*;
import jakarta.servlet.ServletException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ControllerAdviceGlobal {
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        Map<String,String> response = new HashMap<>();
        response.put("message", "El valor proporcionado no es válido.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception ex) {
        // Si la excepción es un error 404 (NoHandlerFoundException) o una excepción de Servlet, no la manejamos aquí
        if (ex instanceof ServletException) {
            // Envuelve la excepción en una RuntimeException para re-lanzarla
            throw new RuntimeException(ex);
        }

        // Manejo de errores 500
        Map<String, String> response = new HashMap<>();
        response.put("message", "Ocurrió un error inesperado en el servidor.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(BrandException.class)
    public ResponseEntity<Map<String,String>> handleBrandException(BrandException ex) {
        Map<String,String> response = new HashMap<>();
        response.put("message", ex.getErrorMessage());  /* { message: ? } */
        return ResponseEntity.status(ex.getErrorStatus()).body(response);
    }

    @ExceptionHandler(EmployeeException.class)
    public ResponseEntity<Map<String,String>> handleEmployeeException(EmployeeException ex) {
        Map<String,String> response = new HashMap<>();
        response.put("message", ex.getErrorMessage());  /* { message: ? } */
        return ResponseEntity.status(ex.getErrorStatus()).body(response);
    }

    @ExceptionHandler(DocumentTypeException.class)
    public ResponseEntity<Map<String,String>> handleDocumentTypeException(DocumentTypeException ex) {
        Map<String,String> response = new HashMap<>();
        response.put("message", ex.getErrorMessage());  /* { message: ? } */
        return ResponseEntity.status(ex.getErrorStatus()).body(response);
    }

    @ExceptionHandler(JobPositionException.class)
    public ResponseEntity<Map<String,String>> handleJobPositionException(JobPositionException ex) {
        Map<String,String> response = new HashMap<>();
        response.put("message", ex.getErrorMessage());  /* { message: ? } */
        return ResponseEntity.status(ex.getErrorStatus()).body(response);
    }

    @ExceptionHandler(BackupException.class)
    public ResponseEntity<Map<String,String>> handleBackupException(BackupException ex) {
        Map<String,String> response = new HashMap<>();
        response.put("message", ex.getErrorMessage());  /* { message: ? } */
        return ResponseEntity.status(ex.getErrorStatus()).body(response);
    }
}
