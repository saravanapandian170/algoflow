package org.techtoolkit.algoflow.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CodeValidationResponse {
    private boolean valid;
    private String message;
}
