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
    
//    public void testJsonList() throws Exception {
//    	try {
//    		
//        	ModelRepositoryClient.initialize(URL);
//        	
//        	String jsonModelString = new String(Files.readAllBytes(Paths.get(getClass().getResource("/model.json").toURI())), Charset.defaultCharset());
//        	
//        	String modelURI = ModelRepositoryClient.add(jsonModelString);
//        	assertNotNull(modelURI);
//        	LOG.info("URI: " + modelURI);
//        	
//        	
//        	String jsonModel2 = new String(Files.readAllBytes(Paths.get(getClass().getResource("/model_2.json").toURI())), Charset.defaultCharset());
//        	
//        	String modelURI2 = ModelRepositoryClient.add(jsonModel2);
//        	assertNotNull(modelURI2);
//        	LOG.info("URI2: " + modelURI2);
//        	
//        	List<String> listJson = ModelRepositoryClient.listJsonModels();
//        	assertNotNull(listJson);
//        	LOG.info("listJson: " + listJson.size());
//        	
//        	String status = ModelRepositoryClient.delete(modelURI);
//        	assertNotNull(status);
//        	
//        	String status2 = ModelRepositoryClient.delete(modelURI2);
//        	assertNotNull(status2);
//        	
//        } catch (Exception e) {
//        	LOG.error(e.getMessage());
//        	fail("testJsonList failed: " + e.getMessage());
//		}
//    }
    
    public void testXmiList() throws Exception {
    	try {
    		
    		ModelRepositoryClient.initialize(URL);
    		
    		String modelName = "sampleXmi";
    		
			String xmiModelString = new String(Files.readAllBytes(Paths.get(getClass().getResource("/model.xmi").toURI())), Charset.defaultCharset());
			String modelURI = ModelRepositoryClient.addXmi(modelName, xmiModelString);
        	assertNotNull(modelURI);
        	LOG.info("URI1: " + modelURI);
        	
			String modelURI2 = ModelRepositoryClient.addXmi(modelName, xmiModelString);
        	assertNotNull(modelURI2);
        	LOG.info("URI2: " + modelURI2);
        	
        	List<String> listXmiModel = ModelRepositoryClient.listXmiModels(modelName);
        	assertNotNull(listXmiModel);
        	LOG.info("listXmi-modelName [" + modelName + "] = " + listXmiModel.size());
        	
    		List<String> listXmi = ModelRepositoryClient.listXmiModels();
        	assertNotNull(listXmi);
        	LOG.info("listXmi: " + listXmi.size());
        	
        	assertNotNull(ModelRepositoryClient.deleteXmi(modelURI));
        	
        	assertNotNull(ModelRepositoryClient.deleteXmi(modelURI2));
        	
        	List<String> listXmiFinal = ModelRepositoryClient.listXmiModels();
        	assertNotNull(listXmiFinal);
        	LOG.info("listXmiFinal: " + listXmiFinal.size());
        	
        } catch (Exception e) {
        	LOG.error(e.getMessage());
        	fail("testXmiList failed: " + e.getMessage());
		}
    }
    
}
