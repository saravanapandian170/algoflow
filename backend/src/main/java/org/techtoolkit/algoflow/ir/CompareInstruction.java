package org.techtoolkit.algoflow.ir;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CompareInstruction implements IrInstruction {
    private final String left;
    private final String operator;
    private final String right;
    private final int jumpIfFalse;

    @Override
    public String getType() {
        return "COMPARE";
    }

    @Override
    public String getMessage() {
        return "Executing COMPARE " + left + " " + operator + " " + right;
    }
}
