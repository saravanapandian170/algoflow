package org.techtoolkit.algoflow.engine;

import org.techtoolkit.algoflow.ir.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IrExecutionEngine {

    public List<ExecutionStep> execute(
            List<IrInstruction> instructions,
            ExecutionInput input
    ) {
        ExecutionState state = new ExecutionState(
                input.getArray(),
                input.getTarget()
        );

        List<ExecutionStep> steps = new ArrayList<>();
        int stepCounter = 0;

        while (!state.isFinished()
                && state.getInstructionPointer() < instructions.size()) {
            int ipBefore = state.getInstructionPointer();
            IrInstruction instruction =
                    instructions.get(state.getInstructionPointer());

            executeInstruction(instruction, state, instructions);

            if(!state.isFinished() && state.getInstructionPointer() == ipBefore) {
                state.incrementInstructionPointer();
            }

            ExecutionStep step = new ExecutionStep(
                    stepCounter++,
                    copyArray(state.getArray()),
                    copyPointers(state),
                    List.of(),
                    "Executing " + instruction.getType(),
                    state.isFinished()
            );

            steps.add(step);
        }

        return steps;
    }

    private void executeInstruction(
            IrInstruction instruction,
            ExecutionState state,
            List<IrInstruction> instructions
    ) {

        if (instruction instanceof InitInstruction) {
            executeInit((InitInstruction) instruction, state);
            return;
        }

        if (instruction instanceof LoopCheckInstruction) {
            executeLoopCheck((LoopCheckInstruction) instruction, state, instructions);
            return;
        }

        if (instruction instanceof CompareInstruction) {
            executeCompare((CompareInstruction) instruction, state, instructions);
            return;
        }

        if (instruction instanceof IncrementInstruction) {
            executeIncrement((IncrementInstruction) instruction, state);
            return;
        }

        if (instruction instanceof ReturnInstruction) {
            executeReturn((ReturnInstruction) instruction, state);
            return;
        }

        throw new UnsupportedOperationException(
                "Unsupported instruction: " + instruction.getType()
        );
    }

    private void executeInit(
            InitInstruction instruction,
            ExecutionState state
    ) {
        state.getVariables()
                .put(instruction.getVariable(), instruction.getValue());
    }

    private void executeLoopCheck(
            LoopCheckInstruction instruction,
            ExecutionState state,
            List<IrInstruction> instructions
    ) {
        String indexVar = instruction.getLeft();

        int indexValue = state.getVariables().get(indexVar);
        int arrayLength = state.getArray().length;

        boolean conditionTrue = indexValue < arrayLength;

        if (!conditionTrue) {
            state.setInstructionPointer(instruction.getExitInstructionIndex());
        }
    }

    private void executeCompare(
            CompareInstruction instruction,
            ExecutionState state,
            List<IrInstruction> instructions
    ) {
        String left = instruction.getLeft();
        String indexVar = left.substring(left.indexOf('[') + 1, left.indexOf(']'));

        int indexValue = state.getVariables().get(indexVar);
        int elementValue = state.getArray()[indexValue];
        int targetValue = state.getTarget();

        boolean match = elementValue == targetValue;

        if (!match) {
            state.incrementInstructionPointer();
        }
    }

    private void executeIncrement(
            IncrementInstruction instruction,
            ExecutionState state
    ) {
        String var = instruction.getVariable();

        state.getVariables().compute(var, (k, currentValue) -> currentValue + 1);
    }

    private void executeReturn(
            ReturnInstruction instruction,
            ExecutionState state
    ) {
        Object value = instruction.getValue();

        int returnValue;

        if (value instanceof String) {
            returnValue = state.getVariables().get(value);
        } else {
            returnValue = (int) value;
        }

        state.markFinished();
    }

    private int[] copyArray(int[] array) {
        return array == null ? null : array.clone();
    }

    private Map<String, Integer> copyPointers(ExecutionState state) {
        return new HashMap<>(state.getVariables());
    }
}
