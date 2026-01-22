package org.techtoolkit.algoflow.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.techtoolkit.algoflow.dto.CodeValidationRequest;
import org.techtoolkit.algoflow.dto.CodeValidationResponse;
import org.techtoolkit.algoflow.service.LinearSearchAstValidator;

@RestController
@AllArgsConstructor
public class ValidationController {
    private final LinearSearchAstValidator linearSearchAstValidator;

    @PostMapping("/validate")
    public ResponseEntity<CodeValidationResponse> validate(@RequestBody CodeValidationRequest codeValidationRequest) {
        linearSearchAstValidator.validate(codeValidationRequest.getCode());

        return ResponseEntity.ok(
                new CodeValidationResponse(true, "Valid Linear Search code")
        );
    }
}
