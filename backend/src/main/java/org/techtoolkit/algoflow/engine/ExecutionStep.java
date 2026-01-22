package org.techtoolkit.algoflow.engine;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class ExecutionStep {

    private final int stepNumber;
    private final int[] arraySnapshot;
    private final Map<String, Integer> pointers;
    private final List<Integer> highlightedIndices;
    private final String message;
    private final boolean finished;

}

