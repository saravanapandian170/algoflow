package org.techtoolkit.algoflow.engine;

import org.springframework.stereotype.Component;
import org.techtoolkit.algoflow.ir.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
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
                    copyPointers(state),
                    List.of(),
                    "Executing " + instruction.getType(),
                    state.isFinished(),
                    state.getReturnValue()
            );
            steps.add(step);
            if (stepCounter > 5000) {
                throw new IllegalStateException("Execution step limit exceeded");
            }

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

        if (instruction instanceof JumpInstruction) {
            executeJump((JumpInstruction) instruction, state);
            return;
        }

        if (instruction instanceof ReturnInstruction) {
            executeReturn((ReturnInstruction) instruction, state);
            return;
        }

        if (instruction instanceof AssignInstruction) {
            executeAssign((AssignInstruction) instruction, state);
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
        int leftValue = resolveValue(instruction.getLeft(), state);
        int rightValue = resolveValue(instruction.getRight(), state);

        boolean conditionTrue;

        switch (instruction.getOperator()) {
            case "<":
                conditionTrue = leftValue < rightValue;
                break;
            case "<=":
                conditionTrue = leftValue <= rightValue;
                break;
            case ">":
                conditionTrue = leftValue > rightValue;
                break;
            case ">=":
                conditionTrue = leftValue >= rightValue;
                break;
            default:
                throw new UnsupportedOperationException(
                        "Unsupported operator: " + instruction.getOperator()
                );
        }

        if (!conditionTrue) {
            state.setInstructionPointer(instruction.getExitInstructionIndex());
        }
    }

    private void executeCompare(
            CompareInstruction instruction,
            ExecutionState state,
            List<IrInstruction> instructions
    ) {
        int leftValue = resolveCompareLeft(instruction.getLeft(), state);
        int rightValue = resolveValue(instruction.getRight(), state);

        boolean conditionTrue;

        switch (instruction.getOperator()) {
            case "==":
                conditionTrue = leftValue == rightValue;
                break;
            case "<":
                conditionTrue = leftValue < rightValue;
                break;
            case ">":
                conditionTrue = leftValue > rightValue;
                break;
            default:
                throw new UnsupportedOperationException(
                        "Unsupported compare operator: " + instruction.getOperator()
                );
        }

        if (!conditionTrue) {
            state.setInstructionPointer(instruction.getJumpIfFalse());
        }
    }

    private void executeIncrement(
            IncrementInstruction instruction,
            ExecutionState state
    ) {
        String var = instruction.getVariable();

        state.getVariables().compute(var, (k, currentValue) -> currentValue + 1);
    }

    private void executeJump(
            JumpInstruction instruction,
            ExecutionState state
    ) {
        state.setInstructionPointer(instruction.getIndex());
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
        state.setReturnValue(returnValue);
        state.markFinished();
    }

    private void executeAssign(
            AssignInstruction instruction,
            ExecutionState state
    ) {
        String variable = instruction.getVariable();
        String expression = instruction.getExpression();

        int value = evaluateExpression(expression, state);

        state.getVariables().put(variable, value);
    }

    private int evaluateExpression(
            String expr,
            ExecutionState state
    ) {
        expr = expr.replaceAll("\\s+", "");

        // array.length
        if (expr.equals("array.length")) {
            return state.getArray().length;
        }

        // array.length - 1
        if (expr.equals("array.length-1")) {
            return state.getArray().length - 1;
        }

        // (low+high)/2
        if (expr.matches("\\(.*\\)/2")) {
            String inside = expr.substring(1, expr.indexOf(")"));
            String[] parts = inside.split("\\+");
            int left = state.getVariables().get(parts[0]);
            int right = state.getVariables().get(parts[1]);
            return (left + right) / 2;
        }

        // mid+1 or mid-1
        if (expr.contains("+")) {
            String[] parts = expr.split("\\+");
            return state.getVariables().get(parts[0]) + Integer.parseInt(parts[1]);
        }

        if (expr.contains("-")) {
            String[] parts = expr.split("-");
            return state.getVariables().get(parts[0]) - Integer.parseInt(parts[1]);
        }

        // single variable
        if (state.getVariables().containsKey(expr)) {
            return state.getVariables().get(expr);
        }

        // literal number
        return Integer.parseInt(expr);
    }

    private int resolveCompareLeft(
            String left,
            ExecutionState state
    ) {
        // array[index]
        if (left.startsWith("array[")) {
            String indexVar =
                    left.substring(left.indexOf('[') + 1, left.indexOf(']'));

            int index = state.getVariables().get(indexVar);
            return state.getArray()[index];
        }

        else if(left instanceof String) {
            return state.getVariables().get(left);
        }

        throw new IllegalArgumentException("Invalid compare left: " + left);
    }

    private int resolveValue(String token, ExecutionState state) {

        // array.length
        if (token.equals("array.length")) {
            return state.getArray().length;
        }

        // target input
        if (token.equals("target")) {
            return state.getTarget();
        }

        // numeric literal
        if (Character.isDigit(token.charAt(0)) || token.charAt(0) == '-') {
            return Integer.parseInt(token);
        }


        // algorithm variable (low, high, mid, i)
        if (state.getVariables().containsKey(token)) {
            return state.getVariables().get(token);
        }

        throw new IllegalArgumentException("Unknown token: " + token);
    }


    private int[] copyArray(int[] array) {
        return array == null ? null : array.clone();
    }

    private Map<String, Integer> copyPointers(ExecutionState state) {
        return new HashMap<>(state.getVariables());
    }
}
