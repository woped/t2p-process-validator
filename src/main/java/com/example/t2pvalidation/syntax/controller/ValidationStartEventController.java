package com.example.t2pvalidation.syntax.controller;
import com.example.t2pvalidation.syntax.service.ValidationStartEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ValidationStartEventController {
    String xmlFilePath = "src/main/resources/test/bpmn/redhat_example.bpmn";

    @Autowired
    private ValidationStartEventService validationStartEventService;

    @GetMapping("/validate-bpmn")
    public String validateBpmn(String filePath) {
        boolean isValid = ValidationStartEventService.validateBpmnStartPoint(xmlFilePath);

        if (isValid) {
            return "The BPMN diagram has a valid start event.";
        } else {
            return "The BPMN diagram does not have a valid start event.";
        }
    }
}