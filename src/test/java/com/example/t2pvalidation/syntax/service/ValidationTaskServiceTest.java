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

class ValidationTaskServiceTest {

    @InjectMocks
    private ValidationTaskService validationTaskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testValidateTasks_withValidTasks() throws IOException {
        File tempFile = createTempBpmnFile("<bpmn:definitions xmlns:bpmn=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "  <bpmn:process id=\"Process_1\" isExecutable=\"true\">\n" +
                "    <bpmn:userTask id=\"UserTask_1\" name=\"User Task 1\" camunda:assignee=\"user1\" />\n" +
                "    <bpmn:serviceTask id=\"ServiceTask_1\" name=\"Service Task 1\" camunda:type=\"delegateExpression\" />\n" +
                "  </bpmn:process>\n" +
                "</bpmn:definitions>");

        ValidationResult result = validationTaskService.validateTasks(tempFile.getAbsolutePath());
        assertEquals("completed", result.getValidationStatus());
        assertTrue(result.getErrors().isEmpty());
        assertTrue(result.getWarnings().isEmpty());

        tempFile.delete();
    }

    @Test
    void testValidateTasks_withInvalidUserTask() throws IOException {
        File tempFile = createTempBpmnFile("<bpmn:definitions xmlns:bpmn=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "  <bpmn:process id=\"Process_1\" isExecutable=\"true\">\n" +
                "    <bpmn:userTask id=\"UserTask_1\" />\n" +
                "  </bpmn:process>\n" +
                "</bpmn:definitions>");

        ValidationResult result = validationTaskService.validateTasks(tempFile.getAbsolutePath());
        assertEquals("failed", result.getValidationStatus());
        assertFalse(result.getErrors().isEmpty());
        assertTrue(result.getErrors().stream().anyMatch(e -> e.toString().contains("has no name")));
        assertTrue(result.getErrors().stream().anyMatch(e -> e.toString().contains("has no assignee")));

        tempFile.delete();
    }

    @Test
    void testValidateTasks_withInvalidServiceTask() throws IOException {
        File tempFile = createTempBpmnFile("<bpmn:definitions xmlns:bpmn=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "  <bpmn:process id=\"Process_1\" isExecutable=\"true\">\n" +
                "    <bpmn:serviceTask id=\"ServiceTask_1\" />\n" +
                "  </bpmn:process>\n" +
                "</bpmn:definitions>");

        ValidationResult result = validationTaskService.validateTasks(tempFile.getAbsolutePath());
        assertEquals("failed", result.getValidationStatus());
        assertFalse(result.getErrors().isEmpty());
        assertTrue(result.getErrors().stream().anyMatch(e -> e.toString().contains("has no name")));
        assertTrue(result.getErrors().stream().anyMatch(e -> e.toString().contains("has no implementation type")));

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
