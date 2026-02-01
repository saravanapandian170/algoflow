package org.techtoolkit.algoflow.algorithm.template;

import org.techtoolkit.algoflow.ir.*;

import java.util.ArrayList;
import java.util.List;

public class FirstAndLastOccurrenceTemplate {
    public List<IrInstruction> buildCombinedIr() {

        List<IrInstruction> ir = new ArrayList<>();

        ir.add(new InitInstruction("low", 0));                          // 0
        ir.add(new InitInstruction("high", -1));                        // 1
        ir.add(new InitInstruction("mid", 0));                          // 2
        ir.add(new InitInstruction("firstIndex", -1));                  // 3
        ir.add(new InitInstruction("lastIndex", -1));                   // 4

        ir.add(new AssignInstruction("high", "array.length - 1"));      // 5

        // =====================================================
        // PHASE 1: FIRST OCCURRENCE
        // =====================================================

        ir.add(new LoopCheckInstruction("low", "<=", "high", 17));       // 6

        ir.add(new AssignInstruction("mid", "(low + high) / 2"));        // 7

        ir.add(new CompareInstruction("array[mid]", "==", "target", 11));// 8
        ir.add(new CompareInstruction("array[mid]", "<", "target", 14));// 9

        ir.add(new AssignInstruction("high", "mid - 1"));               //10
        ir.add(new JumpInstruction(6));                                  //11

        ir.add(new AssignInstruction("firstIndex", "mid"));             //12
        ir.add(new AssignInstruction("high", "mid - 1"));               //13
        ir.add(new JumpInstruction(6));                                  //14

        ir.add(new AssignInstruction("low", "mid + 1"));                //15
        ir.add(new JumpInstruction(6));                                  //16

        // =====================================================
        // RESET POINTERS FOR LAST OCCURRENCE
        // =====================================================

        ir.add(new AssignInstruction("low", "0"));                       //17
        ir.add(new AssignInstruction("high", "array.length - 1"));      //18

        // =====================================================
        // PHASE 2: LAST OCCURRENCE
        // =====================================================

        ir.add(new LoopCheckInstruction("low", "<=", "high", 29));       //19

        ir.add(new AssignInstruction("mid", "(low + high) / 2"));        //20

        ir.add(new CompareInstruction("array[mid]", "==", "target", 23));//21
        ir.add(new CompareInstruction("array[mid]", "<", "target", 26));//22

        ir.add(new AssignInstruction("high", "mid - 1"));               //23
        ir.add(new JumpInstruction(19));                                 //24

        ir.add(new AssignInstruction("lastIndex", "mid"));              //25
        ir.add(new AssignInstruction("low", "mid + 1"));                //26
        ir.add(new JumpInstruction(19));                                 //27

        ir.add(new AssignInstruction("low", "mid + 1"));                //28
        ir.add(new JumpInstruction(19));                                 //29

        ir.add(new ReturnInstruction("lastIndex"));                      //30

        return ir;
    }
}
