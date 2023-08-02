package com.xmllab.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XmlValidator {
  private static final ObjectMapper objectMapper = new ObjectMapper();

  public static void main(String[] args) {
    if (args.length != 1) {
      System.err.println("Usage: XmlValidator <dir_path>");
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
    List<String> errors = new ArrayList<>();
    try {
      // Enable validation
      SAXParserFactory spf = SAXParserFactory.newInstance();
      spf.setValidating(true);

      // Get a new instance of parser
      SAXParser saxParser = spf.newSAXParser();

      // Define an error handler to catch any problems during parsing
      DefaultHandler handler = new DefaultHandler() {
        @Override
        public void warning(SAXParseException e) {
          errors.add(e.getMessage());
        }

        @Override
        public void error(SAXParseException e) {
          errors.add(e.getMessage());
        }

        @Override
        public void fatalError(SAXParseException e) {
          errors.add(e.getMessage());
        }
      };

      // Parse the document to validate
      saxParser.parse(xmlFile, handler);

    } catch (SAXException | IOException | ParserConfigurationException e) {
      errors.add(e.getMessage());
    }

    ObjectNode result = objectMapper.createObjectNode();
    result.put("name", xmlFile.getAbsolutePath());

    if (errors.isEmpty()) {
      result.put("status", "success");
    } else {
      result.put("status", "failed");
      ArrayNode errorsNode = objectMapper.createArrayNode();
      errors.forEach(errorsNode::add);
      result.set("errors", errorsNode);
    }

    try {
      String jsonOutput = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);
      System.out.println(jsonOutput);
    } catch (Exception e) {
      System.out.println("Error creating JSON output: " + e.getMessage());
    }
  }
}
