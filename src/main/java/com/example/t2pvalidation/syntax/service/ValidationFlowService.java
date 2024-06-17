package com.example.t2pvalidation.syntax.service;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.StartEvent;
import org.camunda.bpm.model.bpmn.instance.EndEvent;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class ValidationFlowService {

    public boolean validateNoUnboundFlows(String filePath) {

        Logger logger = LoggerFactory.getLogger(ValidationFlowService.class);

        try {
            BpmnModelInstance modelInstance = Bpmn.readModelFromFile(new File(filePath));

            for (ModelElementInstance elementInstance : modelInstance.getModelElementsByType(FlowNode.class)) {
                if (elementInstance instanceof FlowNode) {
                    FlowNode flowNode = (FlowNode) elementInstance;

                    // Check if the node is not a StartEvent and has no incoming flows
                    if (!(flowNode instanceof StartEvent) && flowNode.getIncoming().isEmpty()) {
                        return false;
                    }

                    // Check if the node is not an EndEvent and has no outgoing flows
                    if (!(flowNode instanceof EndEvent) && flowNode.getOutgoing().isEmpty()) {
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Validation error: ", e);
        }
        return true;
    }
}