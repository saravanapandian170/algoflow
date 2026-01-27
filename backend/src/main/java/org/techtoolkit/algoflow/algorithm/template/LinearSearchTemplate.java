package org.techtoolkit.algoflow.algorithm.template;

import org.springframework.stereotype.Component;
import org.techtoolkit.algoflow.ir.*;

import java.util.ArrayList;
import java.util.List;

@Component
public class LinearSearchTemplate {

    public List<IrInstruction> buildIr() {
        List<IrInstruction> instructions = new ArrayList<>();

        instructions.add(new InitInstruction("i", 0));

        instructions.add(new LoopCheckInstruction(
                "i",
                "<",
                "array.length",
                6
        ));

        instructions.add(new CompareInstruction(
                "array[i]",
                "==",
                "target",
                4
        ));

        instructions.add(new ReturnInstruction("i"));

        instructions.add(new IncrementInstruction("i"));

        instructions.add(new JumpInstruction(1));

        instructions.add(new ReturnInstruction(-1));

        return instructions;
    }
}

