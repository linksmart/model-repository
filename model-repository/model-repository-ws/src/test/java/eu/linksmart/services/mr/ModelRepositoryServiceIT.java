package eu.linksmart.services.mr;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map.Entry;

import javax.ws.rs.core.MultivaluedMap;

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
    
    public void testXmiList() throws Exception {
    	try {
    		
    		ModelRepositoryClient.initialize(URL);
    		
    		String modelName = "sampleXmi";

			int initialSize = 0;
			MultivaluedMap<String, String> listXmi = ModelRepositoryClient.listXmiModels();
			if( listXmi != null)
				initialSize = listXmi.size();

			String xmiModelString = new String(Files.readAllBytes(Paths.get(getClass().getResource("/model.xmi").toURI())), Charset.defaultCharset());
			
			String modelURI = ModelRepositoryClient.addXmi(modelName, xmiModelString);
        	assertNotNull(modelURI);
        	LOG.info("URI-1: " + modelURI);
        	
			String modelURI2 = ModelRepositoryClient.addXmi(modelName, xmiModelString);
        	assertNotNull(modelURI2);
        	LOG.info("URI-2: " + modelURI2);
        	
        	listXmi = ModelRepositoryClient.listXmiModels();
        	assertNotNull(listXmi);
        	assertEquals((initialSize + 2), listXmi.size());
        	LOG.info("listXmi - expecting " + (initialSize + 2) + " entries: " + listXmi.size());
        	for (Entry<String, List<String>> entry : listXmi.entrySet()) {
        		LOG.info("[listXmi] " + entry.getKey() + " - " + entry.getValue().get(0));
            }
        	
        	MultivaluedMap<String, String> listXmiModel = ModelRepositoryClient.listXmiModels(modelName);
        	assertNotNull(listXmiModel);
        	assertEquals(2, listXmiModel.size());
        	LOG.info("listXmi-modelName [" + modelName + "] = expecting 2 entries: " + listXmiModel.size());
        	for (Entry<String, List<String>> entry : listXmiModel.entrySet()) {
        		LOG.info("[listXmi-modelName] " + entry.getKey() + " - " + entry.getValue().get(0));
            }
        	
        	assertEquals(ModelRepositoryClient.getXmi(modelURI2), ModelRepositoryClient.getLatestXmiModel(modelName));
        	
        	assertNotNull(ModelRepositoryClient.deleteXmi(modelURI));
        	
        	assertNotNull(ModelRepositoryClient.deleteXmi(modelURI2));
        	
        	MultivaluedMap<String, String> listXmiModelFinal = ModelRepositoryClient.listXmiModels(modelName);
        	assertNotNull(listXmiModelFinal);
        	assertEquals(0, listXmiModelFinal.size());
        	
        	MultivaluedMap<String, String> listXmiFinal = ModelRepositoryClient.listXmiModels();
        	assertNotNull(listXmiFinal);
        	assertEquals(initialSize, listXmiFinal.size());
        	
        	LOG.info("testXmiList successfully completed");
        	
        } catch (Exception e) {
        	LOG.error(e.getMessage());
        	fail("testXmiList failed: " + e.getMessage());
		}
    }
    
    public void testJsonList() throws Exception {
    	try {
    		
        	ModelRepositoryClient.initialize(URL);
        	
        	String modelName = "e3";
        	
        	String jsonModelString = new String(Files.readAllBytes(Paths.get(getClass().getResource("/model.json").toURI())), Charset.defaultCharset());
        	
        	String modelURI = ModelRepositoryClient.addJson("e3:123", jsonModelString);
        	assertNotNull(modelURI);
        	LOG.info("URI: " + modelURI);
        	
        	
        	String jsonModel2 = new String(Files.readAllBytes(Paths.get(getClass().getResource("/model_2.json").toURI())), Charset.defaultCharset());
        	
        	String modelName2 = "e3-t1";
        	
        	String modelURI2 = ModelRepositoryClient.addJson("e3-t1:456", jsonModel2);
        	assertNotNull(modelURI2);
        	LOG.info("URI2: " + modelURI2);
        	
        	MultivaluedMap<String, String> listJson = ModelRepositoryClient.listJsonModels();
        	assertNotNull(listJson);
        	assertEquals(2, listJson.size());
        	LOG.info("listJson - expecting 2 entries: " + listJson.size());
        	for (Entry<String, List<String>> entry : listJson.entrySet()) {
        		LOG.info("[listJson] " + entry.getKey() + " - " + entry.getValue().get(0));
            }
        	
        	MultivaluedMap<String, String> listJsonModels = ModelRepositoryClient.listJsonModels(modelName);
        	assertNotNull(listJsonModels);
        	assertEquals(1, listJsonModels.size());
        	LOG.info("listJson-modelName [" + modelName + "] = expecting entries: " + listJsonModels.size());
        	for (Entry<String, List<String>> entry : listJsonModels.entrySet()) {
        		LOG.info("[listJson-modelName] " + entry.getKey() + " - " + entry.getValue().get(0));
            }
        	
        	assertNotNull(ModelRepositoryClient.deleteJson(modelURI));
        	
        	assertNotNull(ModelRepositoryClient.deleteJson(modelURI2));
        	
        	MultivaluedMap<String, String> listJsonModelsFinal = ModelRepositoryClient.listJsonModels(modelName);
        	assertNotNull(listJsonModelsFinal);
        	assertEquals(0, listJsonModelsFinal.size());
        	
        	MultivaluedMap<String, String> listJsonFinal = ModelRepositoryClient.listJsonModels();
        	assertNotNull(listJsonFinal);
        	assertEquals(0, listJsonFinal.size());
        	
        	LOG.info("testJsonList successfully completed");
        	
        } catch (Exception e) {
        	LOG.error(e.getMessage());
        	fail("testJsonList failed: " + e.getMessage());
		}
    }
}
