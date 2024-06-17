package com.example.t2pvalidation.syntax.service;

import com.example.t2pvalidation.utils.ValidationResult;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.Gateway;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;
import org.camunda.bpm.model.bpmn.instance.ExclusiveGateway;
import org.camunda.bpm.model.bpmn.instance.InclusiveGateway;
import org.camunda.bpm.model.bpmn.instance.ComplexGateway;
import org.camunda.bpm.model.bpmn.instance.ParallelGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class GatewayValidationService {

    private static final Logger logger = LoggerFactory.getLogger(GatewayValidationService.class);

    public ValidationResult validateGateways(String bpmnFilePath) {
        ValidationResult validationResult = new ValidationResult();
        List<Object> errors = new ArrayList<>();
        List<Object> warnings = new ArrayList<>();

        try {
            BpmnModelInstance modelInstance = Bpmn.readModelFromFile(new File(bpmnFilePath));
            Collection<Gateway> gateways = modelInstance.getModelElementsByType(Gateway.class);

            for (Gateway gateway : gateways) {
                Collection<SequenceFlow> incoming = gateway.getIncoming();
                Collection<SequenceFlow> outgoing = gateway.getOutgoing();

                if (gateway instanceof ExclusiveGateway || gateway instanceof InclusiveGateway || gateway instanceof ComplexGateway) {
                    if (!((incoming.size() == 1 && outgoing.size() >= 1) || (incoming.size() >= 1 && outgoing.size() == 1))) {
                        errors.add("Invalid gateway configuration for gateway ID " + gateway.getId() + ": Exclusive, Inclusive, or Complex Gateway must have one incoming and multiple outgoing flows or vice versa.");
                    }
                } else if (gateway instanceof ParallelGateway) {
                    if (!(incoming.size() == 1 || outgoing.size() == 1)) {
                        if (!(incoming.size() == 1 || outgoing.size() == 1)) {
                        }
                    } else {
                        // Default rule for other types of gateways if any
                        if (!(incoming.size() == 1 && outgoing.size() >= 1)) {
                            errors.add("Invalid gateway configuration for gateway ID " + gateway.getId() + ": Other Gateway types must have one incoming and multiple outgoing flows.");
                        }
                    }
                }
            }
            validationResult.setErrors(errors);
            validationResult.setWarnings(warnings);
            validationResult.setValidationStatus(errors.isEmpty() ? "completed" : "failed");

        } catch (Exception e) {
            logger.error("Validation error: ", e);
            validationResult.setValidationStatus("failed");
            validationResult.setErrorMessage(e.getMessage());
        }

        return validationResult;
    }
}
