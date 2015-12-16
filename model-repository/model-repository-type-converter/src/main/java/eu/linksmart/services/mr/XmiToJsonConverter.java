package eu.linksmart.services.mr;

import com.google.gson.JsonObject;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.StringReader;

public class XmiToJsonConverter {

	private static final Logger LOG = Logger.getLogger(XmiToJsonConverter.class);

	public static String toJson(String modelIdentifier, Integer modelVersion, String xmiDoc) {

		UMLModel model = new UMLModel();
		model.setName( modelIdentifier);
		model.setVersion( modelVersion.toString());

		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = builderFactory.newDocumentBuilder();
			InputSource is= new InputSource(new StringReader(xmiDoc));
			Document doc = builder.parse( is);

			model.parseDom( doc);

		} catch (ParserConfigurationException
				| org.xml.sax.SAXException
				| javax.xml.xpath.XPathExpressionException
				| java.io.IOException e) {
			LOG.error(e.getMessage(), e);
		}

		return model.toStringJSON();
	}

}
