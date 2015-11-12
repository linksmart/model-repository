package eu.linksmart.services.mr;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.log4j.Logger;

import eu.linksmart.services.mr.client.ModelRepositoryClient;

public class XmiModelServiceIT extends AbstractIT {
	
	private static final Logger LOG = Logger.getLogger(XmiModelServiceIT.class);
	
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
        	System.out.println("URI: " + modelURI);
        	
        	String xmiModel = ModelRepositoryClient.getXmi(modelURI);
        	assertNotNull(xmiModel);
        	//System.out.println("get-xmi: " + xmiModel);
        	
        	String xmiModelUpdate = new String(Files.readAllBytes(Paths.get(getClass().getResource("/model.xmi").toURI())), Charset.defaultCharset());
        	
        	String modelURIUpdated = ModelRepositoryClient.updateXmi(modelURI, xmiModelUpdate);
        	assertNotNull(modelURIUpdated);
        	System.out.println("URI-xmi-update: " + modelURIUpdated);
        	
        	String xmiModelUpdated = ModelRepositoryClient.getXmi(modelURIUpdated);
        	assertNotNull(xmiModelUpdated);
        	        	
        	String status = ModelRepositoryClient.deleteXmi(modelURIUpdated);
        	assertNotNull(status);
        	System.out.println("delete-xmi-status: " + status);
            
        } catch (Exception e) {
        	LOG.error(e);
        	fail("testXmi failed: " + e.getMessage());
		}
    }
    
}
