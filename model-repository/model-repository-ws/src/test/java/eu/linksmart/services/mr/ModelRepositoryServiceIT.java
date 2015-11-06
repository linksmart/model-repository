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
        	LOG.debug("Entering unit test: " + this.getName());
        	LOG.debug("--------------------------------------------------------------------------------");
        }
        
        LOG.info( MessageFormat.format( "model repository URL at {0}", new Object[] { this.URL } ) );
    }

    @Override
    protected void tearDown() {
        if(LOG.isDebugEnabled()) {
        	LOG.debug("--------------------------------------------------------------------------------");
        	LOG.debug("Leaving unit test: " + this.getName());
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
        	fail("testAdd failed: " + e.getMessage());
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
        	
        	
        	List<String> listXmi = ModelRepositoryClient.listXmiModels();
        	assertNotNull(listXmi);
        	System.out.println("listXmi: " + listXmi.size());
            
        } catch (Exception e) {
        	LOG.error(e);
        	fail("test failed: " + e.getMessage());
		}
    }
    
    public void testXmi() throws Exception {	
    }
    
    public void testXmiList() throws Exception {
    	try {
    		
        	//List<String> listXmi = ModelRepositoryClient.listXmiModels();
        	//assertNotNull(listXmi);
        	//System.out.println("listXmi: " + listXmi.size());
            
        } catch (Exception e) {
        	LOG.error(e);
        	fail("test failed: " + e.getMessage());
		}
    }
    
//    public void testAdd() throws Exception {
//        try {
//        	ModelRepositoryClient.initialize(URL);
//        	String modelString = getModel();
//        	assertNotNull(ModelRepositoryClient.add(modelString));
//            LOG.info("add is successful");
//        } catch (Exception e) {
//        	LOG.error(e);
//        	fail("testAdd failed: " + e.getMessage());
//		}
//    }
//    
//   public void testGet() throws Exception {
//        try {
//        	ModelRepositoryClient.initialize(URL);
//        	String outcome = ModelRepositoryClient.get("e3.123");
//        	assertNotNull(outcome);
//        	LOG.info("get is successful");
//        } catch (Exception e) {
//        	LOG.error(e);
//        	fail("testGet failed: " + e.getMessage());
//		}
//    }
//    
//   public void testUpdate() throws Exception {
//        try {
//        } catch (Exception e) {
//        	LOG.error(e);
//        	fail("testUpdate failed: " + e.getMessage());
//		}
//   }
//   
//   public void testRemove() throws Exception {
//       try {
//       } catch (Exception e) {
//       	LOG.error(e);
//       	fail("testRemoveManifest failed: " + e.getMessage());
//		}
//   }
//   
//   public void testGetAllResourcesOfType() throws Exception {
//       try {   
//       } catch (Exception e) {
//       	LOG.error(e);
//       	fail("testGetAllResourcesOfType failed: " + e.getMessage());
//       }
//   }
   
   private String getModel() throws Exception {
	   byte[] encoded = Files.readAllBytes(Paths.get(getClass().getResource("/model.json").toURI()));
	   return new String(encoded, Charset.defaultCharset());
   }

}
