package org.techtoolkit.algoflow.engine;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class ExecutionState {
    private Map<String, Integer> variables;
    private int[] array;
    private int target;
    private int instructionPointer;
    private boolean finished = false;

    public ExecutionState(int[] array, int target) {
        this.array = array;
        this.target = target;
    }

    public void incrementInstructionPointer() {
        this.instructionPointer++;
    }

    public void markFinished() {
        this.finished = true;
    }
}
