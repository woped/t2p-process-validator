package com.example.t2pvalidation.syntax.service;

import com.example.t2pvalidation.utils.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class GatewayValidationServiceTest {

    @InjectMocks
    private GatewayValidationService gatewayValidationService;

    @BeforeEach
    void setUp() {
        // Keine Notwendigkeit, die modelInstance zu mocken
    }

    @Test
    void testValidateGatewaysCorrectGateway() {
        String correctGatewayPath = "src/main/resources/test/bpmn/perfect_model.bpmn";

        ValidationResult result = gatewayValidationService.validateGateways(correctGatewayPath);

        assertNotNull(result);
        assertEquals("completed", result.getValidationStatus());
        assertTrue(result.getErrors().isEmpty());
        assertTrue(result.getWarnings().isEmpty());
    }

    @Test
    void testValidateGatewaysFalseGateway() {
        String falseGatewayPath = "src/main/resources/test/bpmn/gateway/false_gateway.bpmn";

        ValidationResult result = gatewayValidationService.validateGateways(falseGatewayPath);

        assertNotNull(result);
        assertEquals("failed", result.getValidationStatus());
        assertFalse(result.getErrors().isEmpty());
    }
}
