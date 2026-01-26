package org.techtoolkit.algoflow.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.techtoolkit.algoflow.dto.LinearSearchExecutionRequest;
import org.techtoolkit.algoflow.engine.ExecutionInput;
import org.techtoolkit.algoflow.engine.ExecutionStep;
import org.techtoolkit.algoflow.engine.IrExecutionEngine;
import org.techtoolkit.algoflow.ir.IrInstruction;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/execute")
@AllArgsConstructor
public class ExecutionController {
    private final LinearSearchAstValidator validator;
    private final LinearSearchAstToIrConverter irConverter;
    private final IrExecutionEngine executionEngine;

    @PostMapping("/linear-search")
    public List<ExecutionStep> executeLinearSearch(
            @RequestBody LinearSearchExecutionRequest request
    ) {

        List<IrInstruction> instructions =
                irConverter.convert(request.getCode());

        ExecutionInput input = new ExecutionInput(
                request.getArray(),
                request.getTarget()
        );

        return executionEngine.execute(instructions, input);
    }
}
