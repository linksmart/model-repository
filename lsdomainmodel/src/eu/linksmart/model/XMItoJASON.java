package eu.linksmart.model;

import java.io.File;
import java.io.FileWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;


public class XMItoJASON {

    public static void main(String[] args) {
	// write your code here
        UMLModel model = new UMLModel();
        // Check the arguments
        if (args.length != 1) {
            System.err.println("Usage: java XMItoJASON filename.xmi");
            System.exit (1);
        }

        String filename = args[0];
        File inputFile = new File(filename);
        if( !inputFile.isFile() ) {
            System.err.println("File " + filename + " does not exist!");
            System.exit (1);
        }

        String name = inputFile.getName();
        model.setName( name.split("\\.")[0]);
        model.setVersion( name.split("\\.")[1]);

        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = builderFactory.newDocumentBuilder();
            Document doc = builder.parse(inputFile);

            model.parseDom( doc);

            filename = filename.substring(0, filename.lastIndexOf(".")) + ".json";
            File outputFile = new File(filename);
            if( outputFile.exists())
                outputFile.delete();

            FileWriter fw = new FileWriter( outputFile);
            fw.write( model.toStringJSON());
            fw.close();

            } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (org.xml.sax.SAXException e) {
            e.printStackTrace();
        } catch (java.io.IOException e){
            e.printStackTrace();
        }catch (javax.xml.xpath.XPathExpressionException e){
            e.printStackTrace();
        }


        }

}


