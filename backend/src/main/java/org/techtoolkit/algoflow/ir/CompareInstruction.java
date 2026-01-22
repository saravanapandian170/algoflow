package org.techtoolkit.algoflow.ir;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.classfile.Instruction;

@Getter
@AllArgsConstructor
public class CompareInstruction implements IrInstruction {
    private final String left;
    private final String operator;
    private final String right;

    @Override
    public String getType() {
        return "COMPARE";
    }
}
