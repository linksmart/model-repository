package eu.linksmart.services.mr;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.linksmart.services.mr.client.ModelRepositoryClient;

/**
 * @author hrasheed
 * 
 */
public class ModelRepositoryServiceIT extends AbstractIT {

	private final Logger LOG = LoggerFactory.getLogger(ModelRepositoryServiceIT.class);
	
    protected void setUp() {
        super.setUp();
    }

    protected void tearDown() {
    	super.tearDown();
    }
    
    public void testJsonList() throws Exception {
    	try {
    		
        	ModelRepositoryClient.initialize(URL);
        	
        	String jsonModelString = new String(Files.readAllBytes(Paths.get(getClass().getResource("/model.json").toURI())), Charset.defaultCharset());
        	
        	String modelURI = ModelRepositoryClient.add(jsonModelString);
        	assertNotNull(modelURI);
        	LOG.info("URI: " + modelURI);
        	
        	
        	String jsonModel2 = new String(Files.readAllBytes(Paths.get(getClass().getResource("/model_2.json").toURI())), Charset.defaultCharset());
        	
        	String modelURI2 = ModelRepositoryClient.add(jsonModel2);
        	assertNotNull(modelURI2);
        	LOG.info("URI2: " + modelURI2);
        	
        	List<String> listJson = ModelRepositoryClient.listJsonModels();
        	assertNotNull(listJson);
        	LOG.info("listJson: " + listJson.size());
        	
        	String status = ModelRepositoryClient.delete(modelURI);
        	assertNotNull(status);
        	
        	String status2 = ModelRepositoryClient.delete(modelURI2);
        	assertNotNull(status2);
        	
        } catch (Exception e) {
        	LOG.error(e.getMessage());
        	fail("testJsonList failed: " + e.getMessage());
		}
    }
    
    public void testXmiList() throws Exception {
    	try {
    		
    		ModelRepositoryClient.initialize(URL);
    		
			String xmiModelString = new String(Files.readAllBytes(Paths.get(getClass().getResource("/model.xmi").toURI())), Charset.defaultCharset());
			String modelURI = ModelRepositoryClient.addXmi("sample-xmi-id-1", xmiModelString);
        	assertNotNull(modelURI);
        	LOG.info("URI: " + modelURI);
        	
    		List<String> listXmi = ModelRepositoryClient.listXmiModels();
        	assertNotNull(listXmi);
        	LOG.info("listXmi: " + listXmi.size());
        	
        	String status = ModelRepositoryClient.deleteXmi(modelURI);
        	assertNotNull(status);
        } catch (Exception e) {
        	LOG.error(e.getMessage());
        	fail("testXmiList failed: " + e.getMessage());
		}
    }
    
    public void testGenerateJson() throws Exception {
    	try {
    		
    		ModelRepositoryClient.initialize(URL);
    		
    		String xmiModelString = new String(Files.readAllBytes(Paths.get(getClass().getResource("/model.xmi").toURI())), Charset.defaultCharset());
        	
        	String modelURI = ModelRepositoryClient.addXmi("sample-xmi-id", xmiModelString);
        	assertNotNull(modelURI);
        	LOG.info("URI: " + modelURI);
        	
        	String jsonModelDoc = ModelRepositoryClient.generateJsonModel("sample-xmi-id");
        	assertNotNull(jsonModelDoc);
        	LOG.info("JsonDoc: " + jsonModelDoc);
        	
        	String status = ModelRepositoryClient.deleteXmi(modelURI);
        	assertNotNull(status);
        	
        } catch (Exception e) {
        	LOG.error(e.getMessage());
        	fail("testGenerateJson failed: " + e.getMessage());
		}
    }
    
}
