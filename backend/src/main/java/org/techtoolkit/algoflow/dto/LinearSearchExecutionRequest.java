package org.techtoolkit.algoflow.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LinearSearchExecutionRequest {
    private String code;
    private int[] array;
    private int target;
}
