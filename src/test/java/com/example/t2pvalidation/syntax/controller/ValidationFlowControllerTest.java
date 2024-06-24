package com.example.t2pvalidation.syntax.controller;

import com.example.t2pvalidation.syntax.service.ValidationFlowService;
import com.example.t2pvalidation.utils.ValidationResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class ValidationFlowControllerTest {

    @MockBean
    private ValidationFlowService validationFlowService;

    @InjectMocks
    private ValidationFlowController validationFlowController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(validationFlowController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testValidateFlows_noUnboundFlows() throws Exception {
        String filePath = "src/test/resources/test.bpmn";

        ValidationResult validationResult = new ValidationResult();
        validationResult.setValidationStatus("completed");
        validationResult.setErrors(new ArrayList<>());
        validationResult.setWarnings(new ArrayList<>());

        when(validationFlowService.validateNoUnboundFlows(filePath)).thenReturn(validationResult);

        String expectedResponse = objectMapper.writeValueAsString(validationResult);

        mockMvc.perform(get("/validate-flows")
                        .param("filePath", filePath))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    public void testValidateFlows_hasUnboundFlows() throws Exception {
        String filePath = "src/test/resources/test.bpmn";

        ValidationResult validationResult = new ValidationResult();
        validationResult.setValidationStatus("failed");
        ArrayList<Object> errors = new ArrayList<>();
        errors.add("FlowNode with ID node1 has no incoming flows.");
        validationResult.setErrors(errors);
        validationResult.setWarnings(new ArrayList<>());

        when(validationFlowService.validateNoUnboundFlows(filePath)).thenReturn(validationResult);

        String expectedResponse = objectMapper.writeValueAsString(validationResult);

        mockMvc.perform(get("/validate-flows")
                        .param("filePath", filePath))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }
}
