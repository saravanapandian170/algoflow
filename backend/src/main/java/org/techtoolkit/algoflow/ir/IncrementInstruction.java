package org.techtoolkit.algoflow.ir;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IncrementInstruction implements IrInstruction {
    private final String variable;
    private final int jumpIndex = 1;

    @Override
    public String getType() {
        return "INCREMENT";
    }

    @Override
    public String getMessage() {
        return "Executing INCREMENT " + variable;
    }
}
