package eu.linksmart.services.mr;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

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
        	
        	String xmiModelString = new String(Files.readAllBytes(Paths.get(getClass().getResource("/model.xmi").toURI())), Charset.defaultCharset());
        	
        	String modelURI = ModelRepositoryClient.addXmi(modelName, xmiModelString);
        	assertNotNull(modelURI);
        	LOG.info("URI: " + modelURI);
        	
        	String modelURI2 = ModelRepositoryClient.addXmi(modelName, xmiModelString);
        	assertNotNull(modelURI2);
        	LOG.info("URI2: " + modelURI2);
        	
        	assertNotNull(ModelRepositoryClient.getXmi(modelURI));
        	
        	List<String> listXmiModel = ModelRepositoryClient.listXmiModels(modelName);
        	assertNotNull(listXmiModel);
        	LOG.info("listXmi-modelName [" + modelName + "] = " + listXmiModel.size());
        	        	
        	assertNotNull(ModelRepositoryClient.deleteXmi(modelURI));
        	
        	assertNotNull(ModelRepositoryClient.deleteXmi(modelURI2));
        	
        	List<String> listXmiFinal = ModelRepositoryClient.listXmiModels();
        	assertNotNull(listXmiFinal);
        	LOG.info("listXmi: " + listXmiFinal.size());
            
        } catch (Exception e) {
        	LOG.error(e.getMessage());
        	fail("testXmi failed: " + e.getMessage());
		}
    }
    
}
