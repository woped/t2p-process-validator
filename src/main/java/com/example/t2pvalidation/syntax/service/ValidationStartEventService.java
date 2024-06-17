package com.example.t2pvalidation.syntax.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.StartEvent;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import java.io.File;

@Service
public class ValidationStartEventService {

    public static boolean validateBpmnStartPoint(String filePath) {

        Logger logger = LoggerFactory.getLogger(XMLValidationService.class);

        try {
            BpmnModelInstance modelInstance = Bpmn.readModelFromFile(new File(filePath));

            for (ModelElementInstance elementInstance : modelInstance.getModelElementsByType(StartEvent.class)) {
                if (elementInstance instanceof StartEvent) {
                    return true;
                }
            }
        }
        catch (Exception e) {
            logger.error("Validation error: ", e);
        }
        return false;
    }
}