package com.example.t2pvalidation.syntax.service;

import com.example.t2pvalidation.utils.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ValidationFlowServiceTest {

    @InjectMocks
    private ValidationFlowService validationFlowService;

    @BeforeEach
    void setUp() {
        // Initialisiere Mockito-Annotationen
    }

    @Test
    public void testValidateNoUnboundFlows_withValidFlows() {
        String validBpmnPath = "src/main/resources/test/bpmn/GPT4o_Case_1_Short.bpmn";

        ValidationResult validResult = validationFlowService.validateNoUnboundFlows(validBpmnPath);
        assertEquals("completed", validResult.getValidationStatus(), "The BPMN diagram should have no unbound flows.");
        assertTrue(validResult.getErrors().isEmpty(), "There should be no errors for a valid BPMN diagram.");
    }

    @Test
    public void testValidateNoUnboundFlows_withInvalidFlows() {
        String invalidBpmnPath = "src/main/resources/test/bpmn/flows/BadFlows_TestCase.bpmn";

        ValidationResult invalidResult = validationFlowService.validateNoUnboundFlows(invalidBpmnPath);
        assertEquals("failed", invalidResult.getValidationStatus(), "The BPMN diagram should have unbound flows.");
        assertFalse(invalidResult.getErrors().isEmpty(), "There should be errors for an invalid BPMN diagram.");
    }
}
