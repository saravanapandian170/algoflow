package org.techtoolkit.algoflow.service;

import com.github.javaparser.ast.body.MethodDeclaration;
import org.techtoolkit.algoflow.ir.IrInstruction;

import java.util.List;

public interface LinearSearchAstToIrConverter {
    List<IrInstruction> convert(String code);
    List<IrInstruction> convert(
            MethodDeclaration method,
            String arrayName,
            String indexVar,
            String targetVar);
}
