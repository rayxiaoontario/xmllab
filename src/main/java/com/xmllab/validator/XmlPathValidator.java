package com.xmllab.validator;

import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XmlPathValidator {
  public static void main(String[] args) {
    if (args.length != 1) {
      System.err.println("Usage: XmlPathValidator <dir_path>");
      System.exit(1);
    }

    File directory = new File(args[0]);

    if (!directory.exists() || !directory.isDirectory()) {
      System.err.println("Invalid directory path: " + args[0]);
      System.exit(1);
    }

    processDirectory(directory);
  }

  private static void processDirectory(File directory) {
    for (File file : directory.listFiles()) {
      if (file.isDirectory()) {
        processDirectory(file);
      } else if (file.getName().endsWith(".xml")) {
        validateXmlFile(file);
      }
    }
  }

  private static void validateXmlFile(File xmlFile) {
    System.out.println("Processing file: " + xmlFile.getAbsolutePath());

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
        public void warning(SAXParseException e) {
          errors.add(e);
        }

        @Override
        public void error(SAXParseException e) {
          errors.add(e);
        }

        @Override
        public void fatalError(SAXParseException e) {
          errors.add(e);
        }

        @Override
        public void endDocument() {
          if (!errors.isEmpty()) {
            System.out.println("Validation failed with " + errors.size() + " error(s):");
            errors.forEach(e -> System.out.println(e.getMessage()));
          } else {
            System.out.println("Validation successful!");
          }
        }
      };

      // Parse the document to validate
      saxParser.parse(xmlFile, handler);

    } catch (Exception e) {
      System.out.println("An unexpected error occurred: " + e.getMessage());
    }
  }
}
