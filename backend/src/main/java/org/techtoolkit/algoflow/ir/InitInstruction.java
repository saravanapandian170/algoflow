package org.techtoolkit.algoflow.ir;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class InitInstruction implements IrInstruction {
    private final String variable;
    private final int value;

    @Override
    public String getType() {
        return "INIT";
    }

    @Override
    public String getMessage() {
        return "Executing INIT " + variable + " " + value;
    }
}
