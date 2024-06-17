package com.example.t2pvalidation.utils;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CustomDomErrorHandler implements ErrorHandler {
    private static final Logger LOGGER = Logger.getLogger(CustomDomErrorHandler.class.getName());
    private final List<SAXParseException> errors = new ArrayList<>();
    private final List<SAXParseException> warnings = new ArrayList<>();

    @Override
    public void warning(SAXParseException spe) {
        LOGGER.warning(getParseExceptionInfo(spe));
        warnings.add(spe);
    }

    @Override
    public void error(SAXParseException spe) {
        // Log the error but do not throw an exception to allow parsing to continue
        LOGGER.severe("Error: " + getParseExceptionInfo(spe));
        errors.add(spe);
    }

    @Override
    public void fatalError(SAXParseException spe) {
        // Log the fatal error but do not throw an exception to allow parsing to continue
        LOGGER.severe("Fatal Error: " + getParseExceptionInfo(spe));
        errors.add(spe);
    }

    private String getParseExceptionInfo(SAXParseException spe) {
        return "URI=" + spe.getSystemId() + " Line=" + spe.getLineNumber() + ": " + spe.getMessage();
    }

    public List<SAXParseException> getErrors() {
        return errors;
    }

    public List<SAXParseException> getWarnings() {
        return warnings;
    }
}
