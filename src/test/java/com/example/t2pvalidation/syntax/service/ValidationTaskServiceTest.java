package com.example.t2pvalidation.syntax.service;

import com.example.t2pvalidation.utils.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ValidationTaskServiceTest {

    @InjectMocks
    private ValidationTaskService validationTaskService;

    @BeforeEach
    void setUp() {
        // Initialisiere Mockito-Annotationen
    }

    @Test
    void testValidateTasks_withValidTasks() {
        String validBpmnPath = "src/main/resources/test/bpmn/perfect_model.bpmn";

        ValidationResult result = validationTaskService.validateTasks(validBpmnPath);
        assertEquals("completed", result.getValidationStatus());
        assertTrue(result.getErrors().isEmpty());
        assertTrue(result.getWarnings().isEmpty());
    }

    @Test
    void testValidateTasks_withInvalidUserTask() {
        String invalidBpmnPath = "src/main/resources/test/bpmn/task/false_event_invalid_user_task.bpmn";

        ValidationResult result = validationTaskService.validateTasks(invalidBpmnPath);
        assertEquals("failed", result.getValidationStatus());
        assertFalse(result.getErrors().isEmpty());
        assertTrue(result.getErrors().stream().anyMatch(e -> e.toString().contains("has no name")));
        assertTrue(result.getErrors().stream().anyMatch(e -> e.toString().contains("has no assignee")));
    }

    @Test
    void testValidateTasks_withInvalidServiceTask() {
        String invalidBpmnPath = "src/main/resources/test/bpmn/task/false_event_invalid_service_task.bpmn";

        ValidationResult result = validationTaskService.validateTasks(invalidBpmnPath);
        assertEquals("failed", result.getValidationStatus());
        assertFalse(result.getErrors().isEmpty());
        assertTrue(result.getErrors().stream().anyMatch(e -> e.toString().contains("has no name")));
        assertTrue(result.getErrors().stream().anyMatch(e -> e.toString().contains("has no implementation type")));
    }
}
