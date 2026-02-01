package org.techtoolkit.algoflow.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.techtoolkit.algoflow.algorithm.template.BinarySearchTemplate;
import org.techtoolkit.algoflow.algorithm.template.FirstAndLastOccurrenceTemplate;
import org.techtoolkit.algoflow.algorithm.template.LinearSearchTemplate;
import org.techtoolkit.algoflow.dto.BinarySearchExecutionRequest;
import org.techtoolkit.algoflow.dto.FirstAndLastOccurrenceExecutionRequest;
import org.techtoolkit.algoflow.dto.LinearSearchExecutionRequest;
import org.techtoolkit.algoflow.engine.ExecutionInput;
import org.techtoolkit.algoflow.engine.ExecutionResult;
import org.techtoolkit.algoflow.engine.ExecutionStep;
import org.techtoolkit.algoflow.engine.IrExecutionEngine;
import org.techtoolkit.algoflow.ir.IrInstruction;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/execute")
@AllArgsConstructor
public class ExecutionController {
    private final LinearSearchTemplate linearSearchTemplate;
    private final BinarySearchTemplate  binarySearchTemplate;
    private final FirstAndLastOccurrenceTemplate firstAndLastOccurrenceTemplate;
    private final IrExecutionEngine executionEngine;

    @PostMapping("/linear-search")
    public ExecutionResult executeLinearSearch(
            @RequestBody LinearSearchExecutionRequest request
    ) {

        List<IrInstruction> instructions = linearSearchTemplate.buildIr();

        ExecutionInput input = new ExecutionInput(
                request.getArray(),
                request.getTarget()
        );
        List<ExecutionStep> steps =
                executionEngine.execute(instructions, input);

        return new ExecutionResult(request.getArray(), steps);
    }

    @PostMapping("/binary-search")
    public ExecutionResult executeBinarySearch(@RequestBody BinarySearchExecutionRequest request) {
        List<IrInstruction> instructions = binarySearchTemplate.buildIr();

        ExecutionInput input = new ExecutionInput(
                request.getArray(),
                request.getTarget()
        );
        List<ExecutionStep> steps =
                executionEngine.execute(instructions, input);

        return new ExecutionResult(request.getArray(), steps);
    }

    @PostMapping("/first-and-last-occurrence")
    public ExecutionResult executeFirstAndLastOccurrence(
            @RequestBody FirstAndLastOccurrenceExecutionRequest request
    ) {
        List<IrInstruction> instructions = firstAndLastOccurrenceTemplate.buildCombinedIr();
        ExecutionInput input = new ExecutionInput(
                request.getArray(),
                request.getTarget()
        );
        List<ExecutionStep> steps =
                executionEngine.execute(instructions, input);

        return new ExecutionResult(request.getArray(), steps);
    }
}
