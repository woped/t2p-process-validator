package com.example.t2pvalidation.syntax.service;

import com.example.t2pvalidation.utils.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GatewayValidationServiceTest {

    @InjectMocks
    private GatewayValidationService gatewayValidationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testValidateGateways_withValidGateways() throws IOException {
        File tempFile = createTempBpmnFile("<bpmn:definitions xmlns:bpmn=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "  <bpmn:process id=\"Process_1\" isExecutable=\"true\">\n" +
                "    <bpmn:startEvent id=\"StartEvent_1\" />\n" +
                "    <bpmn:exclusiveGateway id=\"ExclusiveGateway_1\" />\n" +
                "    <bpmn:sequenceFlow id=\"SequenceFlow_1\" sourceRef=\"StartEvent_1\" targetRef=\"ExclusiveGateway_1\" />\n" +
                "    <bpmn:sequenceFlow id=\"SequenceFlow_2\" sourceRef=\"ExclusiveGateway_1\" targetRef=\"Task_1\">\n" +
                "      <bpmn:conditionExpression xsi:type=\"bpmn:tFormalExpression\">true</bpmn:conditionExpression>\n" +
                "    </bpmn:sequenceFlow>\n" +
                "    <bpmn:endEvent id=\"EndEvent_1\" />\n" +
                "    <bpmn:sequenceFlow id=\"SequenceFlow_3\" sourceRef=\"Task_1\" targetRef=\"EndEvent_1\" />\n" +
                "  </bpmn:process>\n" +
                "</bpmn:definitions>");

        ValidationResult result = gatewayValidationService.validateGateways(tempFile.getAbsolutePath());
        assertEquals("completed", result.getValidationStatus());
        assertTrue(result.getErrors().isEmpty());
        assertTrue(result.getWarnings().isEmpty());

        tempFile.delete();
    }

    @Test
    void testValidateGateways_withInvalidGatewayConfiguration() throws IOException {
        File tempFile = createTempBpmnFile("<bpmn:definitions xmlns:bpmn=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "  <bpmn:process id=\"Process_1\" isExecutable=\"true\">\n" +
                "    <bpmn:startEvent id=\"StartEvent_1\" />\n" +
                "    <bpmn:exclusiveGateway id=\"ExclusiveGateway_1\" />\n" +
                "    <bpmn:sequenceFlow id=\"SequenceFlow_1\" sourceRef=\"StartEvent_1\" targetRef=\"ExclusiveGateway_1\" />\n" +
                "    <bpmn:endEvent id=\"EndEvent_1\" />\n" +
                "    <bpmn:sequenceFlow id=\"SequenceFlow_3\" sourceRef=\"ExclusiveGateway_1\" targetRef=\"EndEvent_1\" />\n" +
                "  </bpmn:process>\n" +
                "</bpmn:definitions>");

        ValidationResult result = gatewayValidationService.validateGateways(tempFile.getAbsolutePath());
        assertEquals("failed", result.getValidationStatus());
        assertFalse(result.getErrors().isEmpty());
        assertTrue(result.getErrors().stream().anyMatch(e -> e.toString().contains("Invalid gateway configuration")));

        tempFile.delete();
    }

    @Test
    void testValidateGateways_withMissingDecisionText() throws IOException {
        File tempFile = createTempBpmnFile("<bpmn:definitions xmlns:bpmn=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "  <bpmn:process id=\"Process_1\" isExecutable=\"true\">\n" +
                "    <bpmn:startEvent id=\"StartEvent_1\" />\n" +
                "    <bpmn:exclusiveGateway id=\"ExclusiveGateway_1\" />\n" +
                "    <bpmn:sequenceFlow id=\"SequenceFlow_1\" sourceRef=\"StartEvent_1\" targetRef=\"ExclusiveGateway_1\" />\n" +
                "    <bpmn:sequenceFlow id=\"SequenceFlow_2\" sourceRef=\"ExclusiveGateway_1\" targetRef=\"Task_1\" />\n" +
                "    <bpmn:endEvent id=\"EndEvent_1\" />\n" +
                "    <bpmn:sequenceFlow id=\"SequenceFlow_3\" sourceRef=\"Task_1\" targetRef=\"EndEvent_1\" />\n" +
                "  </bpmn:process>\n" +
                "</bpmn:definitions>");

        ValidationResult result = gatewayValidationService.validateGateways(tempFile.getAbsolutePath());
        assertEquals("failed", result.getValidationStatus());
        assertFalse(result.getErrors().isEmpty());
        assertTrue(result.getErrors().stream().anyMatch(e -> e.toString().contains("Missing decision text")));

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
