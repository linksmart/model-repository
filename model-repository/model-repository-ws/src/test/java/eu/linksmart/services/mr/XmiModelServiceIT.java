package eu.linksmart.services.mr;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.linksmart.services.mr.client.ModelRepositoryClient;

public class XmiModelServiceIT extends AbstractIT {
	
	private final Logger LOG = LoggerFactory.getLogger(XmiModelServiceIT.class);
	
    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
    	super.tearDown();
    }
	
	public void testXmi() throws Exception {
    	try {
    		
        	ModelRepositoryClient.initialize(URL);
        	
        	String modelName = "sampleXmi";
        	
        	String xmiModelString = new String(Files.readAllBytes(Paths.get(getClass().getResource("/simple_deployment.xmi").toURI())), Charset.defaultCharset());
        	
        	String modelURI = ModelRepositoryClient.addXmi(modelName, xmiModelString);
        	assertNotNull(modelURI);
        	LOG.info("URI-1: " + modelURI);
        	
        	String modelURI2 = ModelRepositoryClient.addXmi(modelName, xmiModelString);
        	assertNotNull(modelURI2);
        	LOG.info("URI-2: " + modelURI2);
        	
        	assertEquals(xmiModelString, ModelRepositoryClient.getXmi(modelURI));
        	
        	//
        	// if it runs standalone then postfix is correct, otherwise when running with other unit tests then
        	// version number could be different, hence assertion would fail
        	//
        	String modelIdentifier1 = modelName + ":" + 1;
        	String jsonModel = ModelRepositoryClient.getXmiJson(modelIdentifier1);
        	System.out.println("json:" + jsonModel);
        	assertNotNull(jsonModel);
        	
        	String modelIdentifier2 = modelName + ":" + 2;
        	assertEquals(ModelRepositoryClient.getLatestJsonModel(modelName), ModelRepositoryClient.getXmiJson(modelIdentifier2));
        	
        	assertEquals(modelURI, ModelRepositoryClient.updateXmi(modelURI, xmiModelString));
        	
        	assertEquals(xmiModelString, ModelRepositoryClient.getXmi(modelURI));
        	
        	assertNotNull(ModelRepositoryClient.deleteXmi(modelURI));
        	
        	assertNotNull(ModelRepositoryClient.deleteXmi(modelURI2));
        	
        	LOG.info("testXmi successfully completed");
        	
        } catch (Exception e) {
        	LOG.error(e.getMessage());
        	fail("testXmi failed: " + e.getMessage());
		}
    }
    
}
