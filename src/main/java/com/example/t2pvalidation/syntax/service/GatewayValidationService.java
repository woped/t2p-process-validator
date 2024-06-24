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
import org.camunda.bpm.model.xml.ModelParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
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

        BpmnModelInstance modelInstance = null;
        try {
            // Attempt to parse the BPMN model
            modelInstance = Bpmn.readModelFromFile(new File(bpmnFilePath));
        } catch (ModelParseException e) {
            // Handle the parsing exception, log it, and continue
            logger.warn("Handled BPMN parsing issue: " + e.getMessage());
            warnings.add("BPMN parsing issues detected: " + e.getMessage());
            validationResult.setWarnings(warnings);
        }

        if (modelInstance != null) {
            try {
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
                            errors.add("Invalid gateway configuration for gateway ID " + gateway.getId() + ": Parallel Gateway must have one incoming and multiple outgoing flows or vice versa.");
                        }
                    } else {
                        // Default rule for other types of gateways if any
                        if (!(incoming.size() == 1 && outgoing.size() >= 1)) {
                            errors.add("Invalid gateway configuration for gateway ID " + gateway.getId() + ": Other Gateway types must have one incoming and multiple outgoing flows.");
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("Validation error while processing gateways: ", e);
                errors.add("Validation error while processing gateways: " + e.getMessage());
            }

            validationResult.setErrors(errors);
            validationResult.setWarnings(warnings);
            validationResult.setValidationStatus(errors.isEmpty() ? "completed" : "failed");

        } else {
            validationResult.setValidationStatus("failed");
            validationResult.setErrorMessage("Model instance could not be parsed.");
        }

        return validationResult;
    }
}
