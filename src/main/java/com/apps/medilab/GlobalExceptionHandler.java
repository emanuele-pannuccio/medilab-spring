package com.apps.medilab;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.apps.medilab.response.ApiResponse;

import jakarta.persistence.EntityNotFoundException;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 9. GLOBAL EXCEPTION HANDLER
 * Gestisce centralmente gli errori (es. 404, 422)
 * e li formatta usando la tua classe ApiResponse.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Gestisce 404 (EntityNotFoundException)
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse(404, Map.of("message", ex.getMessage())));
    }

    /**
     * Gestisce 422 (Errori di Validazione DTO)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        // Estrae gli errori di validazione
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        fieldError -> fieldError.getField(),
                        fieldError -> fieldError.getDefaultMessage()
                ));
        
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY) // 422
                .body(new ApiResponse(422, Map.of("message", "Validation Failed", "errors", errors)));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse> handleValidationErrors(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY) // 422
                .body(new ApiResponse(422, Map.of("message", "Validation Failed", "errors", ex.getMessage())));
    }

}
