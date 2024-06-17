package com.example.t2pvalidation.syntax.service;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.*;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Collection;

@Service
public class GatewayValidationService {

    public boolean validateGateways(String bpmnFilePath) {

        BpmnModelInstance modelInstance = Bpmn.readModelFromFile(new File(bpmnFilePath));
        Collection<Gateway> gateways = modelInstance.getModelElementsByType(Gateway.class);

        for (Gateway gateway : gateways) {
            Collection<SequenceFlow> incoming = gateway.getIncoming();
            Collection<SequenceFlow> outgoing = gateway.getOutgoing();

            if (gateway instanceof ExclusiveGateway || gateway instanceof InclusiveGateway || gateway instanceof ComplexGateway) {
                if (!((incoming.size() == 1 && outgoing.size() >= 1) || (incoming.size() >= 1 && outgoing.size() == 1))) {
                    return false;
                }
            } else if (gateway instanceof ParallelGateway) {
                if (!(incoming.size() == 1 || outgoing.size() == 1)) {
                    return false;
                }
            } else {
                // Default rule for other types of gateways if any
                if (!(incoming.size() == 1 && outgoing.size() >= 1)) {
                    return false;
                }
            }
        }
        return true;
    }
}