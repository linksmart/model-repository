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
        	
        	String modelURI = ModelRepositoryClient.add(jsonModelString);
        	assertNotNull(modelURI);
        	LOG.info("URI: " + modelURI);
        	
        	String jsonModel = ModelRepositoryClient.get(modelURI);
        	assertNotNull(jsonModel);
        	LOG.info("get: " + jsonModel);
        	
        	String jsonModelUpdate = new String(Files.readAllBytes(Paths.get(getClass().getResource("/model_update.json").toURI())), Charset.defaultCharset());
        	
        	String modelURIUpdated = ModelRepositoryClient.update(modelURI, jsonModelUpdate);
        	assertNotNull(modelURIUpdated);
        	LOG.info("URI-update: " + modelURIUpdated);
        	
        	String jsonModelUpdated = ModelRepositoryClient.get(modelURIUpdated);
        	assertNotNull(jsonModelUpdated);
        	LOG.info("get-update: " + jsonModelUpdated);
        	
        	String status = ModelRepositoryClient.delete(modelURIUpdated);
        	assertNotNull(status);
        	LOG.info("delete-status: " + status);
            
        } catch (Exception e) {
        	LOG.error(e.getMessage());
        	fail("testJson failed: " + e.getMessage());
		}
    }

}
