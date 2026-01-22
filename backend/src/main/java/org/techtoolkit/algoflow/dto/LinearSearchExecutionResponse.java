package org.techtoolkit.algoflow.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.techtoolkit.algoflow.engine.ExecutionStep;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LinearSearchExecutionResponse {
    private List<ExecutionStep> executionSteps;
}
