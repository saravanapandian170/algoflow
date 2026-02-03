package org.techtoolkit.algoflow.ir;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoopCheckInstruction implements IrInstruction {
    private final String left;
    private final String operator;
    private final String right;
    private final int exitInstructionIndex;

    @Override
    public String getType() {
        return "LOOP_CHECK";
    }

    @Override
    public String getMessage() {
        return "Executing LOOP_CHECK " + left + " " + operator + " " + right;
    }
}
