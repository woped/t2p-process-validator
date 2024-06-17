package com.example.t2pvalidation.syntax.controller;

import com.example.t2pvalidation.syntax.service.ValidationFlowService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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

    @Test
    public void testValidateFlows() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(validationFlowController).build();

        String filePath = "src/test/resources/test.bpmn";

        when(validationFlowService.validateNoUnboundFlows(filePath)).thenReturn(true);

        mockMvc.perform(get("/validate-flows")
                        .param("filePath", filePath))
                .andExpect(status().isOk())
                .andExpect(content().string("The BPMN diagram has no unbound flows."));

        when(validationFlowService.validateNoUnboundFlows(filePath)).thenReturn(false);

        mockMvc.perform(get("/validate-flows")
                        .param("filePath", filePath))
                .andExpect(status().isOk())
                .andExpect(content().string("The BPMN diagram has unbound flows."));
    }
}
