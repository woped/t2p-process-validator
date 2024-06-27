package com.example.t2pvalidation.syntax.service;

import com.example.t2pvalidation.utils.ValidationResult;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.EndEvent;
import org.camunda.bpm.model.bpmn.instance.StartEvent;
import org.camunda.bpm.model.xml.ModelParseException;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class ValidationEventService {

    private static final Logger logger = LoggerFactory.getLogger(ValidationEventService.class);

    public ValidationResult validateBpmnEvents(String filePath) {
        ValidationResult validationResult = new ValidationResult();
        List<Object> errors = new ArrayList<>();
        List<Object> warnings = new ArrayList<>();

        BpmnModelInstance modelInstance = null;
        try {
            modelInstance = Bpmn.readModelFromFile(new File(filePath));
        } catch (ModelParseException e) {
            logger.warn("Handled BPMN parsing issue: " + e.getMessage());
            warnings.add("BPMN parsing issues detected: " + e.getMessage());
            validationResult.setWarnings(warnings);
        }

        if (modelInstance != null) {
            try {
                boolean startEventFound = false;
                boolean endEventFound = false;

                for (ModelElementInstance elementInstance : modelInstance.getModelElementsByType(StartEvent.class)) {
                    if (elementInstance instanceof StartEvent) {
                        startEventFound = true;
                        break;
                    }
                }

                for (ModelElementInstance elementInstance : modelInstance.getModelElementsByType(EndEvent.class)) {
                    if (elementInstance instanceof EndEvent) {
                        endEventFound = true;
                        break;
                    }
                }

                if (!startEventFound) {
                    errors.add("No Start Event found in the BPMN model.");
                }

                if (!endEventFound) {
                    errors.add("No End Event found in the BPMN model.");
                }

            } catch (Exception e) {
                logger.error("Validation error while processing events: ", e);
                errors.add("Validation error while processing events: " + e.getMessage());
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
