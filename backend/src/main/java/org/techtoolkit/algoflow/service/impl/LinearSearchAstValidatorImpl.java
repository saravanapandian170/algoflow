package org.techtoolkit.algoflow.service.impl;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.UnaryExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.techtoolkit.algoflow.exception.ValidationException;
import org.techtoolkit.algoflow.service.LinearSearchAstValidator;

import java.util.List;

@Service
@AllArgsConstructor
public class LinearSearchAstValidatorImpl implements LinearSearchAstValidator {
    private final JavaParser javaParser;

    @Override
    public void validate(String code) {
        ParseResult<CompilationUnit> result = javaParser.parse(code);

        if (!result.isSuccessful() || result.getResult().isEmpty()) {
            result.getProblems().forEach(System.out::println);
            throw new ValidationException("Invalid Linear Search code");
        }

        CompilationUnit compilationUnit = result.getResult().get();

        List<MethodDeclaration> methods = compilationUnit.findAll(MethodDeclaration.class);

        if(methods.size() != 1) {
            throw new ValidationException("Number of methods in Linear Search code is not 1.");
        }

        MethodDeclaration method = methods.get(0);
        if(!method.getType().asString().equals("int")) {
            throw new ValidationException("Return type must be int.");
        }

        List<Parameter> parameters = method.getParameters();

        if(parameters.size() != 2) {
            throw new ValidationException("Number of parameters in Linear Search code is not 2.");
        }
        boolean hasArray = parameters.stream()
                .anyMatch(p ->
                        p.getType().isArrayType() &&
                        p.getType().asArrayType()
                                .getComponentType()
                                .asString().equals("int"));

        boolean hasTarget = parameters.stream()
                .anyMatch(p ->
                        p.getType().isPrimitiveType() &&
                                p.getType().asPrimitiveType().getType()
                                        == com.github.javaparser.ast.type.PrimitiveType.Primitive.INT
                );

        if(!hasArray || !hasTarget) {
            throw new ValidationException("The method parameters must be int[] and int.");
        }

        List<ForStmt> forLoops = method.findAll(ForStmt.class);
        if(forLoops.size() != 1) {
            throw new ValidationException("Number of for-loops in Linear Search code must be exactly 1.");
        }

        ForStmt forLoop = forLoops.get(0);
        Expression init = forLoop.getInitialization().get(0);

        if(!(init instanceof VariableDeclarationExpr)) {
            throw new ValidationException("Loop must declare index variable");
        }

        Parameter arrayParam = parameters.stream()
                .filter(p -> p.getType().isArrayType())
                .findFirst()
                .orElseThrow(() -> new ValidationException("Array parameter not found"));

        String arrayName = arrayParam.getNameAsString();
        VariableDeclarationExpr varDecl = (VariableDeclarationExpr) init;
        VariableDeclarator var = varDecl.getVariables().get(0);
        String indexVar = var.getNameAsString();

        Expression condition = forLoop.getCompare()
                .orElseThrow(() -> new ValidationException("For-loop condition missing"));

        if (!(condition instanceof BinaryExpr)) {
            throw new ValidationException("Loop condition must be a comparison");
        }

        BinaryExpr binaryExpr = (BinaryExpr) condition;
        if (binaryExpr.getOperator() != BinaryExpr.Operator.LESS) {
            throw new ValidationException("Loop condition must use '<' operator");
        }

        Expression left = binaryExpr.getLeft();
        Expression right = binaryExpr.getRight();

        if (!left.isNameExpr() ||
                !left.asNameExpr().getNameAsString().equals(indexVar)) {
            throw new ValidationException("Left side must be loop index");
        }

        if (!right.isFieldAccessExpr()) {
            throw new ValidationException("Right side must be array.length");
        }

        var fieldAccess = right.asFieldAccessExpr();

        if (!fieldAccess.getNameAsString().equals("length") ||
                !fieldAccess.getScope().isNameExpr() ||
                !fieldAccess.getScope().asNameExpr().getNameAsString().equals(arrayName)) {
            throw new ValidationException("Loop condition must be index < array.length");
        }

        Expression update = forLoop.getUpdate().get(0);

        if(!(update instanceof UnaryExpr)) {
            throw new ValidationException("Loop must increment index using i++");
        }
        UnaryExpr unary = (UnaryExpr) update;

        if (unary.getOperator() != UnaryExpr.Operator.POSTFIX_INCREMENT ||
                !unary.getExpression().isNameExpr() ||
                !unary.getExpression().asNameExpr().getNameAsString().equals(indexVar)) {

            throw new ValidationException("Loop must increment index using i++");
        }

        List<IfStmt> ifStmts = forLoop.findAll(IfStmt.class);
        if(ifStmts.size() != 1) {
            throw new ValidationException("Exactly one if-statement inside loop is required");
        }

        IfStmt ifStatement = ifStmts.get(0);
        Expression ifCondition = ifStatement.getCondition();

        if (!(ifCondition instanceof BinaryExpr)) {
            throw new ValidationException("If condition must be a comparison");
        }

        BinaryExpr ifBinary = (BinaryExpr) ifCondition;

        if (ifBinary.getOperator() != BinaryExpr.Operator.EQUALS) {
            throw new ValidationException("If condition must use '==' operator");
        }

        List<ReturnStmt> loopReturns = ifStatement.findAll(ReturnStmt.class);
        if(loopReturns.size() != 1) {
            throw new ValidationException("Return i required inside if");
        }

        ReturnStmt returnStmt = loopReturns.get(0);

        if (!returnStmt.getExpression().isPresent() ||
                !returnStmt.getExpression().get().isNameExpr() ||
                !returnStmt.getExpression().get().asNameExpr().getNameAsString().equals(indexVar)) {

            throw new ValidationException("Return statement inside if must return index variable");
        }

        List<ReturnStmt> allReturns = method.findAll(ReturnStmt.class);

        /*System.out.println("=== ALL RETURN STATEMENTS ===");
        for (ReturnStmt r : allReturns) {
            System.out.println(
                    "Return: " + r +
                            " | line=" + r.getBegin().map(p -> p.line).orElse(-1)
            );
        }

        System.out.println("=== RETURN LOCATION CHECK ===");
        for (ReturnStmt r : allReturns) {
            boolean insideLoop = forLoop.isAncestorOf(r);
            System.out.println("Return: " + r + " | insideForLoop=" + insideLoop);
        }*/

        if (allReturns.size() != 2) {
            throw new ValidationException("Method must have exactly two return statements");
        }

        ReturnStmt finalReturn = allReturns.stream()
                .filter(r -> !forLoop.isAncestorOf(r))
                .findFirst()
                .orElseThrow(() -> new ValidationException("Final return statement missing"));

        Expression expr = finalReturn.getExpression()
                .orElseThrow(() -> new ValidationException("Final return must have an expression"));

        boolean isMinusOne =
                expr.isUnaryExpr()
                        && expr.asUnaryExpr().getOperator() == UnaryExpr.Operator.MINUS
                        && expr.asUnaryExpr().getExpression().isIntegerLiteralExpr()
                        && expr.asUnaryExpr().getExpression()
                        .asIntegerLiteralExpr().getValue().equals("1");

        if (!isMinusOne) {
            throw new ValidationException("Final return statement must be -1");
        }

    }
}
