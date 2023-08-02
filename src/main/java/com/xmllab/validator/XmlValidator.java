package com.xmllab.validator;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;

public class XmlValidator {
  public static void main(String[] args) {
    if (args.length != 2) {
      System.err.println("Usage: XmlValidator <xml_file> <dtd_file>");
      System.exit(1);
    }

    String xmlFile = args[0];
    String dtdFile = args[1];

    try {
      // Enable validation
      SAXParserFactory spf = SAXParserFactory.newInstance();
      spf.setValidating(true);

      // Get a new instance of parser
      SAXParser saxParser = spf.newSAXParser();

      // Set the XMLReader's DTD handling properties
      XMLReader xmlReader = saxParser.getXMLReader();
      xmlReader.setEntityResolver(new EntityResolver() {
        @Override
        public InputSource resolveEntity(String publicId, String systemId) {
          if (systemId.endsWith(".dtd")) {
            return new InputSource(dtdFile);
          } else {
            return null;
          }
        }
      });

      // Define an error handler to catch any problems during parsing
      xmlReader.setErrorHandler(new ErrorHandler() {
        @Override
        public void warning(SAXParseException e) throws SAXException {
          throw e; // Throwing exception on warnings to make validation strict
        }

        @Override
        public void error(SAXParseException e) throws SAXException {
          throw e;
        }

        @Override
        public void fatalError(SAXParseException e) throws SAXException {
          throw e;
        }
      });

      // Parse the document to validate
      xmlReader.parse(new InputSource(xmlFile));

      System.out.println("Validation successful!");

    } catch (Exception e) {
      System.out.println("Validation failed: " + e.getMessage());
      System.exit(1);
    }
  }
}
