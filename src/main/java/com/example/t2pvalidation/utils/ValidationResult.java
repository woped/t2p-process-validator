package com.example.t2pvalidation.utils;

import java.util.List;

public class ValidationResult {
    private String validationStatus;
    private String errorMessage;
    private List<Object> errors;  // Generic type to hold both SAXParseException and custom string errors
    private List<Object> warnings;  // Generic type to hold both SAXParseException and custom string warnings

    // Getters and setters
    public String getValidationStatus() {
        return validationStatus;
    }

    public void setValidationStatus(String validationStatus) {
        this.validationStatus = validationStatus;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public List<Object> getErrors() {
        return errors;
    }

    public void setErrors(List<Object> errors) {
        this.errors = errors;
    }

    public List<Object> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<Object> warnings) {
        this.warnings = warnings;
    }
}
