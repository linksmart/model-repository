package eu.linksmart.services.mr;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.List;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.junit.Ignore;

import eu.linksmart.services.mr.client.ModelRepositoryClient;

public class XmiModelServiceIT extends TestCase {

    private static final Logger LOG = Logger.getLogger(XmiModelServiceIT.class);
    
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
    
    public void testXmiList() throws Exception {
    	try {
    		
    		ModelRepositoryClient.initialize(URL);
    		
			String xmiModelString = new String(Files.readAllBytes(Paths.get(getClass().getResource("/model.xmi").toURI())), Charset.defaultCharset());
			String modelURI = ModelRepositoryClient.addXmi("sample-xmi-id-1", xmiModelString);
        	assertNotNull(modelURI);
        	System.out.println("URI: " + modelURI);
        	
    		List<String> listXmi = ModelRepositoryClient.listXmiModels();
        	assertNotNull(listXmi);
        	System.out.println("listXmi: " + listXmi.size());
        	
        	String status = ModelRepositoryClient.deleteXmi(modelURI);
        	assertNotNull(status);
        } catch (Exception e) {
        	LOG.error(e);
        	fail("testXmiList failed: " + e.getMessage());
		}
    }

}
