package org.techtoolkit.algoflow.algorithm.template;

import org.springframework.stereotype.Component;
import org.techtoolkit.algoflow.ir.*;

import java.util.ArrayList;
import java.util.List;

@Component
public class BinarySearchTemplate {
    public List<IrInstruction> buildIr() {

        List<IrInstruction> ir = new ArrayList<>();

        ir.add(new InitInstruction("low", 0));
        ir.add(new AssignInstruction("high", "array.length - 1"));

        ir.add(new LoopCheckInstruction(
                "low", "<=", "high", 11
        ));

        ir.add(new AssignInstruction("mid", "(low + high) / 2"));

        ir.add(new CompareInstruction(
                "array[mid]", "==", "target", 6
        ));

        ir.add(new ReturnInstruction("mid"));

        ir.add(new CompareInstruction(
                "array[mid]", "<", "target", 9
        ));

        ir.add(new AssignInstruction("low", "mid + 1"));
        ir.add(new JumpInstruction(2));

        ir.add(new AssignInstruction("high", "mid - 1"));
        ir.add(new JumpInstruction(2));

        ir.add(new ReturnInstruction(-1));

        return ir;
    }
}

