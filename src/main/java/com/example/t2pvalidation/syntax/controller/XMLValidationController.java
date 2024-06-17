package com.example.t2pvalidation.syntax.controller;

import com.example.t2pvalidation.syntax.service.XMLValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class XMLValidationController {

    @Autowired
    private XMLValidationService xmlValidationService;

    @GetMapping("/xml_validate")
    public Map<String, Object> validateXml() {
        String xmlFilePath = "src/main/resources/test/bpmn/GPT4o_Case_1_Short.bpmn";
        String xsdFilePath = "src/main/resources/BPMN20.xsd";
        return xmlValidationService.validateXml(xmlFilePath, xsdFilePath);
    }
}

