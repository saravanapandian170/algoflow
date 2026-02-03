package org.techtoolkit.algoflow.ir;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JumpInstruction implements IrInstruction{
    private int index;
    @Override
    public String getType() {
        return "JUMP";
    }

    @Override
    public String getMessage() {
        return "";
    }
}
