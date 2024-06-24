package com.example.t2pvalidation.syntax.service;

import com.example.t2pvalidation.utils.ValidationResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ValidationFlowServiceTest {

    @Test
    public void testValidateNoUnboundFlows() {
        ValidationFlowService validationFlowService = new ValidationFlowService();

        String validBpmnPath = "src/main/resources/test/bpmn/GPT4o_Case_1_Short.bpmn"; // Replace with your test BPMN file path
        String invalidBpmnPath = "src/main/resources/test/bpmn/BadFlows_TestCase.bpmn"; // Replace with your test BPMN file path

        ValidationResult validResult = validationFlowService.validateNoUnboundFlows(validBpmnPath);
        assertEquals("completed", validResult.getValidationStatus(), "The BPMN diagram should have no unbound flows.");
        assertTrue(validResult.getErrors().isEmpty(), "There should be no errors for a valid BPMN diagram.");

        ValidationResult invalidResult = validationFlowService.validateNoUnboundFlows(invalidBpmnPath);
        assertEquals("failed", invalidResult.getValidationStatus(), "The BPMN diagram should have unbound flows.");
        assertFalse(invalidResult.getErrors().isEmpty(), "There should be errors for an invalid BPMN diagram.");
    }
}