package com.example.t2pvalidation.syntax.controller;

import com.example.t2pvalidation.syntax.service.MainValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/main/syntax")
public class MainValidationController {

    @Autowired
    private MainValidationService mainValidationService;
    String xmlFilePath = "src/main/resources/test/bpmn/GPT4o_Case_1_Short.bpmn";

    @GetMapping("/validate")
    public Map<String, Object> validateAll() {
        //For testing purposes
        return mainValidationService.validateAll(xmlFilePath);
    }
}
