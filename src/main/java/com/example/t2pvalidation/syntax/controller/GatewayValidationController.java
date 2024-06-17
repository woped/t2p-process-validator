package com.example.t2pvalidation.syntax.controller;
import com.example.t2pvalidation.syntax.service.GatewayValidationService;
import com.example.t2pvalidation.syntax.service.ValidationStartEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GatewayValidationController {
    String xmlFilePath = "src/main/resources/test/bpmn/GPT4o_Case_1_Short.bpmn";

    @Autowired
    private GatewayValidationService gatewayValidationService;

    @GetMapping("/validate-bpmn-gateway")
    public String validateBpmn(String filePath) {
        boolean isValid = gatewayValidationService.validateGateways(xmlFilePath);

        if (isValid) {
            return "The BPMN diagram has correct gateway flows.";
        } else {
            return "The BPMN diagram has false gateway flows.";
        }
    }
}