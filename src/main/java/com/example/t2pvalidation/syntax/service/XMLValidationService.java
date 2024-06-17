package com.example.t2pvalidation.syntax.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
public class XMLValidationService {


    Logger logger = LoggerFactory.getLogger(XMLValidationService.class);

    public Map<String, Object> validateXml(String xmlFilePath, String xsdFilePath) {
        CustomErrorHandler errorHandler = new CustomErrorHandler();

        Map<String, Object> result = new HashMap<>();
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(xsdFilePath));
            Validator validator = schema.newValidator();
            validator.setErrorHandler(errorHandler);
            validator.validate(new StreamSource(new File(xmlFilePath)));

            List<SAXParseException> errors = errorHandler.getErrors();
            List<SAXParseException> warnings = errorHandler.getWarnings();

            if (!errors.isEmpty() || !warnings.isEmpty()) {
                result.put("validationStatus", "completed");
                result.put("errors", categorizeErrors(errors));
                result.put("warnings", categorizeWarnings(warnings));
                result.put("summary", createSummary(errors, warnings));
                return result;
            }

            result.put("validationStatus", "completed");
            result.put("errors", new ArrayList<>());
            result.put("warnings", new ArrayList<>());
            result.put("summary", createSummary(errors, warnings));
            return result;
        } catch (SAXException | IOException e) {
            logger.error("Validation error: ", e);
            result.put("validationStatus", "failed");
            result.put("errorMessage", e.getMessage());
            return result;
        }
    }

    private List<Map<String, String>> categorizeErrors(List<SAXParseException> errors) {
        List<Map<String, String>> categorizedErrors = new ArrayList<>();
        for (SAXParseException error : errors) {
            Map<String, String> errorDetails = new HashMap<>();
            errorDetails.put("type", "syntax");
            errorDetails.put("line", String.valueOf(error.getLineNumber()));
            errorDetails.put("elementId", "unknown");  // You can update this to fetch the actual element ID
            errorDetails.put("message", error.getMessage());
            errorDetails.put("suggestion", "Please fix the syntax error.");
            categorizedErrors.add(errorDetails);
        }
        return categorizedErrors;
    }

    private List<Map<String, String>> categorizeWarnings(List<SAXParseException> warnings) {
        List<Map<String, String>> categorizedWarnings = new ArrayList<>();
        for (SAXParseException warning : warnings) {
            Map<String, String> warningDetails = new HashMap<>();
            warningDetails.put("type", "complexity");
            warningDetails.put("elementId", "unknown");  // You can update this to fetch the actual element ID
            warningDetails.put("message", warning.getMessage());
            warningDetails.put("suggestion", "Consider reviewing the complexity.");
            categorizedWarnings.add(warningDetails);
        }
        return categorizedWarnings;
    }

    private Map<String, Integer> createSummary(List<SAXParseException> errors, List<SAXParseException> warnings) {
        Map<String, Integer> summary = new HashMap<>();
        summary.put("totalErrors", errors.size());
        summary.put("totalWarnings", warnings.size());
        return summary;
    }

    private static class CustomErrorHandler implements ErrorHandler {

        private final List<SAXParseException> syntaxErrors = new ArrayList<>();
        private final List<SAXParseException> syntaxWarnings = new ArrayList<>();

        @Override
        public void warning(SAXParseException exception) throws SAXException {
            syntaxWarnings.add(exception);
        }

        @Override
        public void error(SAXParseException exception) throws SAXException {
            syntaxErrors.add(exception);
        }

        @Override
        public void fatalError(SAXParseException exception) throws SAXException {
            syntaxErrors.add(exception);
        }

        public List<SAXParseException> getErrors() {
            return syntaxErrors;
        }

        public List<SAXParseException> getWarnings() {
            return syntaxWarnings;
        }
    }
}
