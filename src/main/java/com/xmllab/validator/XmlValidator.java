package com.xmllab.validator;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.IOException;

public class XmlValidator {
  public static void main(String[] args) {
    if (args.length != 2) {
      System.err.println("Usage: XmlValidator <xml_file> <dtd_file>");
      System.exit(1);
    }

    String xmlFile = args[0];
    String dtdFile = args[1];

    try {
      // Create a new XMLReader
      XMLReader reader = new org.apache.xerces.parsers.SAXParser();

      // Activate validation
      reader.setFeature("http://xml.org/sax/features/validation", true);

      // Load external DTD
      reader.setProperty("http://apache.org/xml/properties/schema/external-noNamespaceSchemaLocation", "file://" + dtdFile);

      // Error handler to report errors and warnings
      DefaultHandler errorHandler = new DefaultHandler() {
        public void error(SAXException e) throws SAXException {
          throw e;
        }
      };
      reader.setErrorHandler(errorHandler);

      // Parse the document
      reader.parse(new InputSource(xmlFile));

      System.out.println("Validation successful!");

    } catch (SAXException e) {
      System.out.println("Validation failed: " + e.getMessage());
      System.exit(1);
    } catch (IOException e) {
      System.out.println("IOException: " + e.getMessage());
      System.exit(1);
    }
  }
}
