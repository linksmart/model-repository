package eu.linksmart.services.mr;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.log4j.Logger;

import eu.linksmart.services.mr.client.ModelRepositoryClient;

public class JsonModelServiceIT extends AbstractIT {

	private static final Logger LOG = Logger.getLogger(JsonModelServiceIT.class);
	
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
        	System.out.println("URI: " + modelURI);
        	
        	String jsonModel = ModelRepositoryClient.get(modelURI);
        	assertNotNull(jsonModel);
        	System.out.println("get: " + jsonModel);
        	
        	String jsonModelUpdate = new String(Files.readAllBytes(Paths.get(getClass().getResource("/model_update.json").toURI())), Charset.defaultCharset());
        	
        	String modelURIUpdated = ModelRepositoryClient.update(modelURI, jsonModelUpdate);
        	assertNotNull(modelURIUpdated);
        	System.out.println("URI-update: " + modelURIUpdated);
        	
        	String jsonModelUpdated = ModelRepositoryClient.get(modelURIUpdated);
        	assertNotNull(jsonModelUpdated);
        	System.out.println("get-update: " + jsonModelUpdated);
        	
        	String status = ModelRepositoryClient.delete(modelURIUpdated);
        	assertNotNull(status);
        	System.out.println("delete-status: " + status);
            
        } catch (Exception e) {
        	LOG.error(e);
        	fail("testJson failed: " + e.getMessage());
		}
    }

}
