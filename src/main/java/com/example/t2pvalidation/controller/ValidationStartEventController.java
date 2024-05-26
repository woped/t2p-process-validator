package com.example.t2pvalidation.controller;
import com.example.t2pvalidation.service.ValidationStartEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ValidationStartEventController {
    String xmlFilePath = "src/main/resources/redhat_example.bpmn";

    @Autowired
    private ValidationStartEventService validationStartEventService;

    @GetMapping("/validate-bpmn")
    public String validateBpmn(@RequestParam String filePath) {
        boolean isValid = ValidationStartEventService.validateBpmnStartPoint(xmlFilePath);

        if (isValid) {
            return "The BPMN diagram has a valid start event.";
        } else {
            return "The BPMN diagram does not have a valid start event.";
        }
    }
}
