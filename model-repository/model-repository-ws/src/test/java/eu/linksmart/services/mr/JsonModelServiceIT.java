package eu.linksmart.services.mr;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.linksmart.services.mr.client.ModelRepositoryClient;

public class JsonModelServiceIT extends AbstractIT {

	private final Logger LOG = LoggerFactory.getLogger(JsonModelServiceIT.class);
	
    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
    	super.tearDown();
    }
	
	public void testJson() throws Exception {
    	try {
    		
        	ModelRepositoryClient.initialize(URL);
        	
        	String jsonModelString = new String(Files.readAllBytes(Paths.get(getClass().getResource("/model.json").toURI())), Charset.defaultCharset());
        	
        	String modelURI = ModelRepositoryClient.addJson("e3:123", jsonModelString);
        	assertNotNull(modelURI);
        	LOG.info("Json-URI: " + modelURI);
        	
        	assertEquals(jsonModelString, ModelRepositoryClient.getJson(modelURI));
        	
        	String jsonModelUpdate = new String(Files.readAllBytes(Paths.get(getClass().getResource("/model_update.json").toURI())), Charset.defaultCharset());
        	
        	assertEquals(modelURI, ModelRepositoryClient.updateJson(modelURI, jsonModelUpdate));
        	
        	assertEquals(jsonModelUpdate, ModelRepositoryClient.getJson(modelURI));
        	
        	assertNotNull(ModelRepositoryClient.deleteJson(modelURI));
        	
        	LOG.info("testJson successfully completed");
        	
        } catch (Exception e) {
        	LOG.error(e.getMessage());
        	fail("testJson failed: " + e.getMessage());
		}
    }

}
