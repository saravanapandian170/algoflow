package org.techtoolkit.algoflow.service.impl;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.ForStmt;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.techtoolkit.algoflow.exception.ValidationException;
import org.techtoolkit.algoflow.ir.*;
import org.techtoolkit.algoflow.service.LinearSearchAstToIrConverter;

import java.util.ArrayList;
import java.util.List;
@Service
@AllArgsConstructor
public class LinearSearchAstToIrConverterImpl implements LinearSearchAstToIrConverter {
    private final JavaParser javaParser;

    @Override
    public List<IrInstruction> convert(String code) {
        CompilationUnit cu = javaParser.parse(code)
                .getResult()
                .orElseThrow(() -> new ValidationException("Invalid Java code"));

        MethodDeclaration method = cu.findFirst(MethodDeclaration.class)
                .orElseThrow(() -> new ValidationException("Method not found"));

        Parameter arrayParam = method.getParameters().stream()
                .filter(p -> p.getType().isArrayType())
                .findFirst()
                .orElseThrow();

        String arrayName = arrayParam.getNameAsString();

        Parameter targetParam = method.getParameters().stream()
                .filter(p -> p.getType().isPrimitiveType())
                .findFirst()
                .orElseThrow();

        String targetVar = targetParam.getNameAsString();

        ForStmt forStmt = method.findFirst(ForStmt.class)
                .orElseThrow();

        VariableDeclarationExpr init =
                (VariableDeclarationExpr) forStmt.getInitialization().get(0);

        String indexVar = init.getVariables().get(0).getNameAsString();

        return convert(method, arrayName, indexVar, targetVar);
    }


    @Override
    public List<IrInstruction> convert(MethodDeclaration method,
                                       String arrayName,
                                       String indexVar,
                                       String targetVar) {
        List<IrInstruction> instructions = new ArrayList<>();

        instructions.add(new InitInstruction(indexVar, 0));

        instructions.add(new LoopCheckInstruction(
                indexVar,
                "<",
                arrayName + ".length",
                6
        ));

        instructions.add(new CompareInstruction(
                arrayName + "[" + indexVar + "]",
                "==",
                targetVar
        ));

        instructions.add(new ReturnInstruction(indexVar));

        instructions.add(new IncrementInstruction(indexVar));

        instructions.add(new LoopCheckInstruction(
                indexVar,
                "<",
                arrayName + ".length",
                6
        ));

        instructions.add(new ReturnInstruction(-1));

        return instructions;
    }
}
