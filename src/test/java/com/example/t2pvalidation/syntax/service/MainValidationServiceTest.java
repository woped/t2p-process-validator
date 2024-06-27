package com.example.t2pvalidation.syntax.service;

import com.example.t2pvalidation.utils.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    private static final String XSD_FILE_PATH = "src/main/resources/BPMN20.xsd";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testValidateAll_withValidFile() throws IOException {
        File tempFile = createTempBpmnFile("<bpmn:definitions xmlns:bpmn=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "  <bpmn:process id=\"Process_1\" isExecutable=\"true\">\n" +
                "    <bpmn:startEvent id=\"StartEvent_1\" />\n" +
                "    <bpmn:endEvent id=\"EndEvent_1\" />\n" +
                "  </bpmn:process>\n" +
                "</bpmn:definitions>");

        ValidationResult validResult = new ValidationResult();
        validResult.setValidationStatus("completed");

        when(xmlValidationService.validateXml(anyString(), eq(XSD_FILE_PATH))).thenReturn(validResult);
        when(gatewayValidationService.validateGateways(anyString())).thenReturn(validResult);
        when(validationEventService.validateBpmnEvents(anyString())).thenReturn(validResult);
        when(validationFlowService.validateNoUnboundFlows(anyString())).thenReturn(validResult);

        Map<String, Object> result = mainValidationService.validateAll(tempFile.getAbsolutePath());
        assertNotNull(result);
        assertEquals("completed", ((Map)result.get("xmlValidation")).get("validationStatus"));
        assertEquals("completed", ((Map)result.get("gatewayValidation")).get("validationStatus"));
        assertEquals("completed", ((Map)result.get("eventValidation")).get("validationStatus"));
        assertEquals("completed", ((Map)result.get("flowValidation")).get("validationStatus"));

        tempFile.delete();
    }

    @Test
    void testValidateAll_withInvalidXmlFile() throws IOException {
        File tempFile = createTempBpmnFile("<invalidXml>");

        ValidationResult invalidResult = new ValidationResult();
        invalidResult.setValidationStatus("failed");
        invalidResult.setErrorMessage("Invalid XML");

        when(xmlValidationService.validateXml(anyString(), eq(XSD_FILE_PATH))).thenReturn(invalidResult);

        Map<String, Object> result = mainValidationService.validateAll(tempFile.getAbsolutePath());
        assertNotNull(result);
        assertEquals("failed", ((Map)result.get("xmlValidation")).get("validationStatus"));
        assertEquals("Invalid XML", ((Map)result.get("xmlValidation")).get("errorMessage"));

        tempFile.delete();
    }

    private File createTempBpmnFile(String bpmnContent) throws IOException {
        File tempFile = File.createTempFile("test-", ".bpmn");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(bpmnContent);
        }
        return tempFile;
    }
}
