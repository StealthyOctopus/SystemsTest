package utils;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;

public class ConfigurationLoader
{
    //Very basic XML parsing, makes several assumptions on how we structure and use the configs
    //TODO: Look into using something like JAXB library to unmarshal xml to Objetcs: https://www.oracle.com/technetwork/articles/javase/index-140168.html
    public static NodeList LoadNodesFromXML(final String file, final String schemaPath)
    {
        try
        {
            File configFile = new File(file);

            //load our schema
            Schema schema = ConfigurationLoader.loadSchema(schemaPath);

            if(configFile == null || schema == null)
                return null;

            //create our document factor
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

            //Validation
            dbFactory.setValidating(true);
            dbFactory.setSchema(schema);

            //create the builder from the factor
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            //Validation error handling - just print errors for now
            dBuilder.setErrorHandler(new ErrorHandler()
            {
                @Override
                public void fatalError(SAXParseException exception) throws SAXException
                {
                    System.err.println("fatalError: " + exception);
                }

                @Override
                public void error(SAXParseException exception) throws SAXException
                {
                    System.err.println("error: " + exception);
                }

                @Override
                public void warning(SAXParseException exception) throws SAXException
                {
                    System.err.println("warning: " + exception);
                }
            });

            //attempt to parse the document
            Document doc = dBuilder.parse(configFile);
            //normalize entries in doc
            doc.getDocumentElement().normalize();

            //get node list to traverse
            return doc.getElementsByTagName("*");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    //attempts to load an xml schema
    public static Schema loadSchema(String name) {
        Schema schema = null;
        try
        {
            // getting the default implementation of XML Schema factory
            String language = XMLConstants.W3C_XML_SCHEMA_NS_URI;
            SchemaFactory factory = SchemaFactory.newInstance(language);

            // parsing the schema file
            schema = factory.newSchema(new File(name));

        }
        catch (Exception e)
        {
            // catching all exceptions
            System.out.println();
            System.out.println(e.toString());
        }
        return schema;
    }

    //TESTING
    public static void main(String[] args)
    {
        NodeList testNodes = ConfigurationLoader.LoadNodesFromXML("config/LifeSupportConfig.xml", "config/LifeSupportConfigSchema.xsd");

        //Success?
        if(testNodes != null)
        {
            for (int i = 1; i < testNodes.getLength(); ++i)
            {
                Node node = testNodes.item(i);

                if (node != null && node.getNodeType() == Node.ELEMENT_NODE)
                {
                    System.out.println("Found Node: " + node.getNodeName());
                }
            }
        }
        System.exit(0);
    }
}
