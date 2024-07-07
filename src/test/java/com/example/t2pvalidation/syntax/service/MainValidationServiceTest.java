package com.example.t2pvalidation.syntax.service;

import com.example.t2pvalidation.utils.ValidationResult;
import com.example.t2pvalidation.utils.ValidationUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MainValidationServiceTest {

    @InjectMocks
    private MainValidationService mainValidationService;

    @Mock
    private XMLValidationService xmlValidationService;

    @Mock
    private GatewayValidationService gatewayValidationService;

    @Mock
    private ValidationEventService validationEventService;

    @Mock
    private ValidationFlowService validationFlowService;

    @Mock
    private ValidationTaskService validationTaskService;

    @Test
    void testValidateAll_withValidModel() throws IOException {
        File validBpmnFile = new ClassPathResource("test/bpmn/perfect_model.bpmn").getFile();
        ValidationResult mockResult = new ValidationResult();
        mockResult.setValidationStatus("completed");

        when(xmlValidationService.validateXml(validBpmnFile.getAbsolutePath(), "src/main/resources/BPMN20.xsd")).thenReturn(mockResult);
        when(gatewayValidationService.validateGateways(validBpmnFile.getAbsolutePath())).thenReturn(mockResult);
        when(validationEventService.validateBpmnEvents(validBpmnFile.getAbsolutePath())).thenReturn(mockResult);
        when(validationFlowService.validateNoUnboundFlows(validBpmnFile.getAbsolutePath())).thenReturn(mockResult);
        when(validationTaskService.validateTasks(validBpmnFile.getAbsolutePath())).thenReturn(mockResult);

        Map<String, Object> result = mainValidationService.validateAll(validBpmnFile.getAbsolutePath());

        assertNotNull(result);
        assertEquals("completed", ((Map<String, Object>) result.get("xmlValidation")).get("validationStatus"));
        assertEquals("completed", ((Map<String, Object>) result.get("gatewayValidation")).get("validationStatus"));
        assertEquals("completed", ((Map<String, Object>) result.get("eventValidation")).get("validationStatus"));
        assertEquals("completed", ((Map<String, Object>) result.get("flowValidation")).get("validationStatus"));
        assertEquals("completed", ((Map<String, Object>) result.get("taskValidation")).get("validationStatus"));
    }

    @Test
    void testValidateAll_withInvalidModel() throws IOException {
        File invalidBpmnFile = new ClassPathResource("test/bpmn/false_model.bpmn").getFile();
        ValidationResult mockResult = new ValidationResult();
        mockResult.setValidationStatus("failed");

        when(xmlValidationService.validateXml(invalidBpmnFile.getAbsolutePath(), "src/main/resources/BPMN20.xsd")).thenReturn(mockResult);
        when(gatewayValidationService.validateGateways(invalidBpmnFile.getAbsolutePath())).thenReturn(mockResult);
        when(validationEventService.validateBpmnEvents(invalidBpmnFile.getAbsolutePath())).thenReturn(mockResult);
        when(validationFlowService.validateNoUnboundFlows(invalidBpmnFile.getAbsolutePath())).thenReturn(mockResult);
        when(validationTaskService.validateTasks(invalidBpmnFile.getAbsolutePath())).thenReturn(mockResult);

        Map<String, Object> result = mainValidationService.validateAll(invalidBpmnFile.getAbsolutePath());

        assertNotNull(result);
        assertEquals("failed", ((Map<String, Object>) result.get("xmlValidation")).get("validationStatus"));
        assertEquals("failed", ((Map<String, Object>) result.get("gatewayValidation")).get("validationStatus"));
        assertEquals("failed", ((Map<String, Object>) result.get("eventValidation")).get("validationStatus"));
        assertEquals("failed", ((Map<String, Object>) result.get("flowValidation")).get("validationStatus"));
        assertEquals("failed", ((Map<String, Object>) result.get("taskValidation")).get("validationStatus"));
    }
}
