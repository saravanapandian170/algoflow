package org.techtoolkit.algoflow.ir;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReturnInstruction implements IrInstruction {
    private final Object value;

    @Override
    public String getType() {
        return "RETURN";
    }

    @Override
    public String getMessage() {
        return "Executing RETURN " + value;
    }
}
