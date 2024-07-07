package com.example.t2pvalidation.syntax.controller;

import com.example.t2pvalidation.syntax.service.ValidationTaskService;
import com.example.t2pvalidation.utils.ValidationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ValidationTaskController {

    @Autowired
    private ValidationTaskService validationTaskService;

    @GetMapping("/validate/tasks")
    public ValidationResult validateTasks(@RequestParam String filePath) {
        return validationTaskService.validateTasks(filePath);
    }
}
