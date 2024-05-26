package com.example.t2pvalidation.controller;

import com.example.t2pvalidation.service.XMLValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class XMLValidationController {

    @Autowired
    private XMLValidationService xmlValidationService;

    @GetMapping("/xml_validate")
    public String validateXml() {
        String xmlFilePath = "src/main/resources/test/bpmn/redhat_example.bpmn";
        String xsdFilePath = "src/main/resources/BPMN20.xsd";
        boolean isValid = xmlValidationService.validateXml(xmlFilePath, xsdFilePath);
        return isValid ? "XML is valid" : "XML is not valid";
    }
}

