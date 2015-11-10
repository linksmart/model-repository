package eu.linksmart.services.mr;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.List;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import eu.linksmart.services.mr.client.ModelRepositoryClient;

/**
 * @author hrasheed
 * 
 */
public class ModelRepositoryServiceIT extends TestCase {

    private static final Logger LOG = Logger.getLogger(ModelRepositoryServiceIT.class);
    
    private String URL = "http://localhost:9090/model-repository/modelrepo";
	
    @Override
    protected void setUp() {
        if(LOG.isDebugEnabled()) {
        	LOG.debug("================================================================================");
        	LOG.debug("Entering integration test: " + this.getName());
        	LOG.debug("--------------------------------------------------------------------------------");
        }
        
        LOG.info( MessageFormat.format( "model repository URL at {0}", new Object[] { this.URL } ) );
    }

    @Override
    protected void tearDown() {
        if(LOG.isDebugEnabled()) {
        	LOG.debug("--------------------------------------------------------------------------------");
        	LOG.debug("Leaving integration test: " + this.getName());
        	LOG.debug("================================================================================");
        }
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
    
    public void testJsonList() throws Exception {
    	try {
    		
        	ModelRepositoryClient.initialize(URL);
        	
        	String jsonModelString = new String(Files.readAllBytes(Paths.get(getClass().getResource("/model.json").toURI())), Charset.defaultCharset());
        	
        	String modelURI = ModelRepositoryClient.add(jsonModelString);
        	assertNotNull(modelURI);
        	System.out.println("URI: " + modelURI);
        	
        	
        	String jsonModel2 = new String(Files.readAllBytes(Paths.get(getClass().getResource("/model_2.json").toURI())), Charset.defaultCharset());
        	
        	String modelURI2 = ModelRepositoryClient.add(jsonModel2);
        	assertNotNull(modelURI2);
        	System.out.println("URI2: " + modelURI2);
        	
        	List<String> listJson = ModelRepositoryClient.listJsonModels();
        	assertNotNull(listJson);
        	System.out.println("listJson: " + listJson.size());
        	
        	String status = ModelRepositoryClient.delete(modelURI);
        	assertNotNull(status);
        	
        	String status2 = ModelRepositoryClient.delete(modelURI2);
        	assertNotNull(status2);
        	
        } catch (Exception e) {
        	LOG.error(e);
        	fail("testJsonList failed: " + e.getMessage());
		}
    }
    
}
