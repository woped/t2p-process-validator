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

class ValidationEventServiceTest {

    @InjectMocks
    private ValidationEventService validationEventService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testValidateBpmnEvents_withValidEvents() throws IOException {
        File tempFile = createTempBpmnFile("<bpmn:definitions xmlns:bpmn=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "  <bpmn:process id=\"Process_1\" isExecutable=\"true\">\n" +
                "    <bpmn:startEvent id=\"StartEvent_1\" name=\"Start\" />\n" +
                "    <bpmn:endEvent id=\"EndEvent_1\" name=\"End\" />\n" +
                "  </bpmn:process>\n" +
                "</bpmn:definitions>");

        ValidationResult result = validationEventService.validateBpmnEvents(tempFile.getAbsolutePath());
        assertEquals("completed", result.getValidationStatus());
        assertTrue(result.getErrors().isEmpty());
        assertTrue(result.getWarnings().isEmpty());

        tempFile.delete();
    }

    @Test
    void testValidateBpmnEvents_withMissingStartEvent() throws IOException {
        File tempFile = createTempBpmnFile("<bpmn:definitions xmlns:bpmn=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "  <bpmn:process id=\"Process_1\" isExecutable=\"true\">\n" +
                "    <bpmn:endEvent id=\"EndEvent_1\" name=\"End\" />\n" +
                "  </bpmn:process>\n" +
                "</bpmn:definitions>");

        ValidationResult result = validationEventService.validateBpmnEvents(tempFile.getAbsolutePath());
        assertEquals("failed", result.getValidationStatus());
        assertFalse(result.getErrors().isEmpty());
        assertTrue(result.getErrors().stream().anyMatch(e -> e.toString().contains("No Start Event found")));

        tempFile.delete();
    }

    @Test
    void testValidateBpmnEvents_withMissingEndEvent() throws IOException {
        File tempFile = createTempBpmnFile("<bpmn:definitions xmlns:bpmn=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "  <bpmn:process id=\"Process_1\" isExecutable=\"true\">\n" +
                "    <bpmn:startEvent id=\"StartEvent_1\" name=\"Start\" />\n" +
                "  </bpmn:process>\n" +
                "</bpmn:definitions>");

        ValidationResult result = validationEventService.validateBpmnEvents(tempFile.getAbsolutePath());
        assertEquals("failed", result.getValidationStatus());
        assertFalse(result.getErrors().isEmpty());
        assertTrue(result.getErrors().stream().anyMatch(e -> e.toString().contains("No End Event found")));

        tempFile.delete();
    }

    @Test
    void testValidateBpmnEvents_withUnnamedEvents() throws IOException {
        File tempFile = createTempBpmnFile("<bpmn:definitions xmlns:bpmn=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "  <bpmn:process id=\"Process_1\" isExecutable=\"true\">\n" +
                "    <bpmn:startEvent id=\"StartEvent_1\" />\n" +
                "    <bpmn:endEvent id=\"EndEvent_1\" />\n" +
                "  </bpmn:process>\n" +
                "</bpmn:definitions>");

        ValidationResult result = validationEventService.validateBpmnEvents(tempFile.getAbsolutePath());
        assertEquals("failed", result.getValidationStatus());
        assertFalse(result.getErrors().isEmpty());
        assertTrue(result.getErrors().stream().anyMatch(e -> e.toString().contains("has no name")));

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
