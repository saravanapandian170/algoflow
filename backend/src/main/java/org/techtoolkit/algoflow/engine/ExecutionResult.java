package org.techtoolkit.algoflow.engine;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExecutionResult {
    private int[] array;
    private List<ExecutionStep> steps;
}
