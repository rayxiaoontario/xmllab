package com.xmllab.validator;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class XmlValidator {
  public static void main(String[] args) {
    if (args.length != 1) {
      System.err.println("Usage: XmlValidator <xml_file>");
      System.exit(1);
    }

    String xmlFile = args[0];

    try {
      // Enable validation
      SAXParserFactory spf = SAXParserFactory.newInstance();
      spf.setValidating(true);

      // Get a new instance of parser
      SAXParser saxParser = spf.newSAXParser();

      // Define an error handler to catch any problems during parsing
      DefaultHandler handler = new DefaultHandler() {
        private List<SAXParseException> errors = new ArrayList<>();

        @Override
        public void warning(SAXParseException e) throws SAXException {
          errors.add(e);
        }

        @Override
        public void error(SAXParseException e) throws SAXException {
          errors.add(e);
        }

        @Override
        public void fatalError(SAXParseException e) throws SAXException {
          errors.add(e);
        }

        @Override
        public void endDocument() throws SAXException {
          if (!errors.isEmpty()) {
            System.out.println("Validation failed with " + errors.size() + " error(s):");
            errors.forEach(e -> System.out.println(e.getMessage()));
            System.exit(1);
          }
        }
      };

      // Parse the document to validate
      saxParser.parse(new File(xmlFile), handler);

      System.out.println("Validation successful!");

    } catch (Exception e) {
      System.out.println("An unexpected error occurred: " + e.getMessage());
      System.exit(1);
    }
  }
}
