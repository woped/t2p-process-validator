package com.example.t2pvalidation.service;
        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;
        import org.springframework.stereotype.Service;
        import org.xml.sax.SAXException;

        import javax.xml.XMLConstants;
        import javax.xml.transform.stream.StreamSource;
        import javax.xml.validation.Schema;
        import javax.xml.validation.SchemaFactory;
        import javax.xml.validation.Validator;
        import java.io.File;
        import java.io.IOException;

@Service
public class XMLValidationService {

    Logger logger = LoggerFactory.getLogger(XMLValidationService.class);
    public boolean validateXml(String xmlFilePath, String xsdFilePath) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(xsdFilePath));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(xmlFilePath)));
            return true;
        } catch (SAXException | IOException e) {
            logger.error("Validation error: ", e);
            e.printStackTrace();

            return false;
        }
    }
}

