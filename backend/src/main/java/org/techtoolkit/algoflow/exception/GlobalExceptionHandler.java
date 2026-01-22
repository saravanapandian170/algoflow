package org.techtoolkit.algoflow.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.techtoolkit.algoflow.dto.CodeValidationResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<CodeValidationResponse> handleValidationException(
            ValidationException ex) {

        return ResponseEntity.badRequest()
                .body(new CodeValidationResponse(false, ex.getMessage()));
    }
}

