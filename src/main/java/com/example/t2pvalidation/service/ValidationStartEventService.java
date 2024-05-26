package com.example.t2pvalidation.service;
import org.springframework.stereotype.Service;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.StartEvent;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;

import java.io.File;

@Service
public class ValidationStartEventService {

    public static boolean validateBpmnStartPoint(String filePath) {
        BpmnModelInstance modelInstance = Bpmn.readModelFromFile(new File(filePath));

        for (ModelElementInstance elementInstance : modelInstance.getModelElementsByType(StartEvent.class)) {
            if (elementInstance instanceof StartEvent) {
                return true;
            }
        }

        return false;
    }
}
