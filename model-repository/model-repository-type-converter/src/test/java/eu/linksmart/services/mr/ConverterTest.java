package eu.linksmart.services.mr;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;

import org.junit.Test;

//import eu.linksmart.services.mr.exceptions.RepositoryException;
//import eu.linksmart.services.mr.exceptions.ResourceInvalid;
//import eu.linksmart.services.mr.exceptions.ResourceNotFound;
//import eu.linksmart.services.mr.exceptions.ResourceTypeUnknown;


public class ConverterTest {

	//private final Logger LOG = LoggerFactory.getLogger(ConverterTest.class);

	public ConverterTest() {
		
	}


	@Test
	public void testConvert() throws Exception {

//		byte[] encoded = Files.readAllBytes(Paths.get(getClass().getResource("/model.xmi").toURI()));
//		byte[] encoded = Files.readAllBytes(Paths.get(getClass().getResource("/simple_deployment5.xmi").toURI()));
//		byte[] encoded = Files.readAllBytes(Paths.get(getClass().getResource("/DeploymentWuerfel.xmi").toURI()));
		byte[] encoded = Files.readAllBytes(Paths.get(getClass().getResource("/DeploymentWuerfel_OPC.xmi").toURI()));
//		byte[] encoded = Files.readAllBytes(Paths.get(getClass().getResource("/Deployment-E3-IWU.xmi").toURI()));
//		byte[] encoded = Files.readAllBytes(Paths.get(getClass().getResource("/E3_domain_simplifiedCube_R.xmi").toURI()));
		String jsonModelDoc = new String(encoded, Charset.defaultCharset());
		String modelIdentifier = "1.0.1";
		Integer modelVersion = 5;

		XmiToJsonConverter.toJson( modelIdentifier, modelVersion, jsonModelDoc);
	}
	

}
