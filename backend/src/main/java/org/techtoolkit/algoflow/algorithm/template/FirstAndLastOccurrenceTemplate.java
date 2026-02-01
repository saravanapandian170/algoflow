package org.techtoolkit.algoflow.algorithm.template;

import org.springframework.stereotype.Component;
import org.techtoolkit.algoflow.ir.*;

import java.util.ArrayList;
import java.util.List;

@Component
public class FirstAndLastOccurrenceTemplate {
    public List<IrInstruction> buildCombinedIr() {

        List<IrInstruction> ir = new ArrayList<>();

        ir.add(new InitInstruction("low", 0));                                                      // 0
        ir.add(new InitInstruction("high", -1));                                                    // 1
        ir.add(new InitInstruction("mid", 0));                                                      // 2
        ir.add(new InitInstruction("firstIndex", -1));                                              // 3
        ir.add(new InitInstruction("lastIndex", -1));                                               // 4

        ir.add(new AssignInstruction("high", "array.length - 1"));                              // 5

        // =====================================================
        // PHASE 1: FIRST OCCURRENCE
        // =====================================================

        ir.add(new LoopCheckInstruction("low", "<=", "high", 17));              // 6

        ir.add(new AssignInstruction("mid", "(low + high) / 2"));                               // 7

        ir.add(new CompareInstruction("array[mid]", "==", "target", 12));             // 8
        ir.add(new AssignInstruction("firstIndex", "mid"));                                     // 9
        ir.add(new AssignInstruction("high", "mid - 1"));                                       // 10
        ir.add(new JumpInstruction(6));                                                                     // 11

        ir.add(new CompareInstruction("array[mid]", "<", "target", 15));              // 12
        ir.add(new AssignInstruction("low", "mid + 1"));                                        // 13
        ir.add(new JumpInstruction(6));                                                                     // 14

        ir.add(new AssignInstruction("high", "mid - 1"));                                       // 15
        ir.add(new JumpInstruction(6));                                                                     // 16

        // =====================================================
        // RESET POINTERS FOR LAST OCCURRENCE
        // =====================================================
        ir.add(new CompareInstruction("firstIndex", "==", "-1", 19));                 // 17
        ir.add(new JumpInstruction(32));                                                                    // 18

        ir.add(new AssignInstruction("low", "0"));                                              // 19
        ir.add(new AssignInstruction("high", "array.length - 1"));                              // 20

        // =====================================================
        // PHASE 2: LAST OCCURRENCE
        // =====================================================

        ir.add(new LoopCheckInstruction("low", "<=", "high", 32));              // 21

        ir.add(new AssignInstruction("mid", "(low + high) / 2"));                               // 22

        ir.add(new CompareInstruction("array[mid]", "==", "target", 27));             // 23
        ir.add(new AssignInstruction("lastIndex", "mid"));                                      // 24
        ir.add(new AssignInstruction("low", "mid + 1"));                                        // 25
        ir.add(new JumpInstruction(21));                                                                    // 26

        ir.add(new CompareInstruction("array[mid]", "<", "target", 30));              // 27
        ir.add(new AssignInstruction("low", "mid + 1"));                                        // 28
        ir.add(new JumpInstruction(21));                                                                    // 29

        ir.add(new AssignInstruction("high", "mid - 1"));                                       // 30
        ir.add(new JumpInstruction(21));                                                                    // 31

        ir.add(new ReturnInstruction("lastIndex"));                                                         // 32

        return ir;
    }
}
