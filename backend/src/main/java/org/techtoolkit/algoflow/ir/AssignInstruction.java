package org.techtoolkit.algoflow.ir;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AssignInstruction implements IrInstruction {

    private final String variable;
    private final String expression;

    @Override
    public String getType() {
        return "ASSIGN";
    }
}

