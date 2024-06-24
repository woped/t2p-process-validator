package com.example.t2pvalidation.syntax.controller;

import com.example.t2pvalidation.syntax.service.ValidationEventService;
import com.example.t2pvalidation.utils.ValidationResult;
import com.example.t2pvalidation.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ValidationEventController {

    @Autowired
    private ValidationEventService validationEventService;

    @GetMapping("/validate-bpmn-start-event")
    public Map<String, Object> validateBpmn(@RequestParam String filePath) {
        ValidationResult validationResult = validationEventService.validateBpmnStartPoint(filePath);
        return ValidationUtils.mapValidationResult(validationResult);
    }
}
