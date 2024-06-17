package com.example.t2pvalidation.syntax.controller;

import com.example.t2pvalidation.syntax.service.ValidationFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ValidationFlowController {
    String xmlFilePath = "src/main/resources/test/bpmn/GPT4o_Case_1_Short.bpmn";

    @Autowired
    private ValidationFlowService validationFlowService;

    @GetMapping("/validate-flows")
    public String validateFlows(String filePath) {
        boolean hasNoUnboundFlows = validationFlowService.validateNoUnboundFlows(xmlFilePath);

        if (hasNoUnboundFlows) {
            return "The BPMN diagram has no unbound flows.";
        } else {
            return "The BPMN diagram has unbound flows.";
        }
    }
}