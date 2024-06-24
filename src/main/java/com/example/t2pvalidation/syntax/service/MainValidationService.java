package com.example.t2pvalidation.syntax.service;
import com.example.t2pvalidation.utils.ValidationResult;
import com.example.t2pvalidation.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class MainValidationService {

    @Autowired
    private XMLValidationService xmlValidationService;

    @Autowired
    private ValidationFlowService validationFlowService;

    @Autowired
    private GatewayValidationService gatewayValidationService;

    @Autowired
    private ValidationEventService validationEventService;

    private static final String XSD_FILE_PATH = "src/main/resources/BPMN20.xsd";

    public Map<String, Object> validateAll(String xmlFilePath) {
        Map<String, Object> result = new HashMap<>();

        // Call XML validation
        ValidationResult xmlValidationResult = xmlValidationService.validateXml(xmlFilePath, XSD_FILE_PATH);
        result.put("xmlValidation", ValidationUtils.mapValidationResult(xmlValidationResult));

        // Call Gateway validation
        ValidationResult gatewayValidationResult = gatewayValidationService.validateGateways(xmlFilePath);
        result.put("gatewayValidation", ValidationUtils.mapValidationResult(gatewayValidationResult));

        ValidationResult eventValidationResult = validationEventService.validateBpmnStartPoint(xmlFilePath);
        result.put("eventValidation", ValidationUtils.mapValidationResult(eventValidationResult));

        // Call Flow validation
        ValidationResult flowValidationResult = validationFlowService.validateNoUnboundFlows(xmlFilePath);
        result.put("flowValidation", ValidationUtils.mapValidationResult(flowValidationResult));

        // Call other validation services
        return result;
    }
}