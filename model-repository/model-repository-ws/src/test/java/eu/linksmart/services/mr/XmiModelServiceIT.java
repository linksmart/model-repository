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
        	
        	String xmiModelString = new String(Files.readAllBytes(Paths.get(getClass().getResource("/model.xmi").toURI())), Charset.defaultCharset());
        	
        	String modelURI = ModelRepositoryClient.addXmi("sample-xmi-id", xmiModelString);
        	assertNotNull(modelURI);
        	LOG.info("URI: " + modelURI);
        	
        	String xmiModel = ModelRepositoryClient.getXmi(modelURI);
        	assertNotNull(xmiModel);
        	//System.out.println("get-xmi: " + xmiModel);
        	
        	String xmiModelUpdate = new String(Files.readAllBytes(Paths.get(getClass().getResource("/model.xmi").toURI())), Charset.defaultCharset());
        	
        	String modelURIUpdated = ModelRepositoryClient.updateXmi(modelURI, xmiModelUpdate);
        	assertNotNull(modelURIUpdated);
        	LOG.info("URI-xmi-update: " + modelURIUpdated);
        	
        	String xmiModelUpdated = ModelRepositoryClient.getXmi(modelURIUpdated);
        	assertNotNull(xmiModelUpdated);
        	        	
        	String status = ModelRepositoryClient.deleteXmi(modelURIUpdated);
        	assertNotNull(status);
        	LOG.info("delete-xmi-status: " + status);
            
        } catch (Exception e) {
        	LOG.error(e.getMessage());
        	fail("testXmi failed: " + e.getMessage());
		}
    }
    
}
