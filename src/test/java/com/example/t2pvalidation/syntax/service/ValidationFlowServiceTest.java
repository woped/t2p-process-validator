package com.example.t2pvalidation.syntax.service;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.StartEvent;
import org.camunda.bpm.model.bpmn.instance.EndEvent;
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

        assertTrue(validationFlowService.validateNoUnboundFlows(validBpmnPath), "The BPMN diagram should have no unbound flows.");
        assertFalse(validationFlowService.validateNoUnboundFlows(invalidBpmnPath), "The BPMN diagram should have unbound flows.");
    }
}
