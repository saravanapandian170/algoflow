package org.techtoolkit.algoflow.ir;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IncrementInstruction implements IrInstruction {
    private final String variable;

    @Override
    public String getType() {
        return "INCREMENT";
    }
}
