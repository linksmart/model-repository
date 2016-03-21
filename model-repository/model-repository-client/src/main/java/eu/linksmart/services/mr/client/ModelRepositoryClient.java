package eu.linksmart.services.mr.client;

import javax.ws.rs.core.MultivaluedMap;

import org.apache.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 * @author hrasheed
 * 
 */
public class ModelRepositoryClient {
	
	private static final Logger LOG = Logger.getLogger(ModelRepositoryClient.class);
	
	private static String BASE_URL = "http://localhost:9090";
	
    public ModelRepositoryClient() {
    }
   
    public static void initialize(String url) {
    	BASE_URL = url;
    }
    
    //
    // XMI Model
    //
    public static String addXmi(String modelName, String xmiModelDoc) {
    	
    	try {
    	
    		Client client = Client.create();
            WebResource webResourceClient = client.resource(BASE_URL + "/" + modelName);

            String outcome = webResourceClient.type("application/xml; charset=utf-8").accept("text/plain; charset=utf-8").post(String.class, xmiModelDoc);
            
            return outcome;
            
        } catch (Exception e) {
			LOG.error(e);
			return null;
		}	
    }
    
    public static String getXmi(String modelURI) {
    	
    	try {
    	
    		Client client = Client.create();
            WebResource webResourceClient = client.resource(modelURI);

            String outcome = webResourceClient.type("text/plain; charset=utf-8").accept("application/xml; charset=utf-8").get(String.class);
            
            return outcome;
            
        } catch (Exception e) {
			LOG.error(e);
			return null;
		}	
    }
    
    public static String getXmiJson(String modelIdentifier) {
    	
    	try {
    	
    		Client client = Client.create();
            WebResource webResourceClient = client.resource(BASE_URL + "/" + modelIdentifier);

            String outcome = webResourceClient.type("text/plain; charset=utf-8").accept("application/json; charset=utf-8").get(String.class);
            
            return outcome;
            
        } catch (Exception e) {
			LOG.error(e);
			return null;
		}	
    }
    
    public static String updateXmi(String modelURI, String xmiModelDoc) {
    	
    	try {
    	
    		Client client = Client.create();
            WebResource webResourceClient = client.resource(modelURI);

            String outcome = webResourceClient.type("application/xml; charset=utf-8").accept("text/plain; charset=utf-8").put(String.class, xmiModelDoc);
            
            return outcome;
            
        } catch (Exception e) {
			LOG.error(e);
			return null;
		}	
    }
    
    public static String deleteXmi(String modelURI) {
    	
    	try {
    	
    		Client client = Client.create();
            WebResource webResourceClient = client.resource(modelURI);

            String outcome = webResourceClient.type("text/plain; charset=utf-8").accept("text/plain; charset=utf-8").delete(String.class);
            
            return outcome;
            
        } catch (Exception e) {
			LOG.error(e);
			return null;
		}	
    }
    
    public static MultivaluedMap<String, String> listXmiModels() {
    	
    	try {
    	
    		Client client = Client.create();
            WebResource webResourceClient = client.resource(BASE_URL + "/" + "list/xmi");
            
            ClientResponse response = webResourceClient.type("text/plain; charset=utf-8").accept("application/x-www-form-urlencoded; charset=utf-8").get(ClientResponse.class);
    		
            MultivaluedMap<String, String> entries = response.getEntity(MultivaluedMap.class);
			         
    		return entries;
    		
        } catch (Exception e) {
        	e.printStackTrace();
			LOG.error(e);
			return null;
		}	
    }
    
    public static MultivaluedMap<String, String> listXmiModels(String modelName) {
    	
    	try {
    	
    		Client client = Client.create();
            WebResource webResourceClient = client.resource(BASE_URL + "/" + "list/xmi/" + modelName);
            
            ClientResponse response = webResourceClient.type("text/plain; charset=utf-8").accept("application/x-www-form-urlencoded; charset=utf-8").get(ClientResponse.class);
    		
            MultivaluedMap<String, String> entries = response.getEntity(MultivaluedMap.class);
			
    		return entries;
    		
        } catch (Exception e) {
			LOG.error(e);
			return null;
		}	
    }
    
    public static String getLatestXmiModel(String modelName) {
    	
    	try {
    	
    		Client client = Client.create();
            WebResource webResourceClient = client.resource(BASE_URL + "/" + "latest/xmi/" + modelName);
            
            String outcome = webResourceClient.type("text/plain; charset=utf-8").accept("application/xml; charset=utf-8").get(String.class);
    		
    		return outcome;
    		
        } catch (Exception e) {
			LOG.error(e);
			return null;
		}	
    }
    
    public static String getLatestJsonModel(String modelName) {
    	
    	try {
    	
    		Client client = Client.create();
            WebResource webResourceClient = client.resource(BASE_URL + "/" + "latest/json/" + modelName);
            
            String outcome = webResourceClient.type("text/plain; charset=utf-8").accept("application/json; charset=utf-8").get(String.class);
    		
    		return outcome;
    		
        } catch (Exception e) {
			LOG.error(e);
			return null;
		}	
    }
    
    //
    // Json model
    //
    public static String addJson(String modelIdentifier, String jsonModelDoc) {
    	
    	try {
    	
    		Client client = Client.create();
            WebResource webResourceClient = client.resource(BASE_URL + "/json/" + modelIdentifier);

            String outcome = webResourceClient.type("application/json; charset=utf-8").accept("text/plain; charset=utf-8").post(String.class, jsonModelDoc);
            
            return outcome;
            
        } catch (Exception e) {
			LOG.error(e);
			return null;
		}	
    }
    
    public static String getJson(String modelURI) {
    	
    	try {
    	
    		Client client = Client.create();
            WebResource webResourceClient = client.resource(modelURI);

            String outcome = webResourceClient.type("text/plain; charset=utf-8").accept("application/json; charset=utf-8").get(String.class);
            
            return outcome;
            
        } catch (Exception e) {
			LOG.error(e);
			return null;
		}	
    }
    
    public static String updateJson(String modelURI, String jsonModelDoc) {
    	
    	try {
    	
    		Client client = Client.create();
            WebResource webResourceClient = client.resource(modelURI);

            String outcome = webResourceClient.type("application/json; charset=utf-8").accept("text/plain; charset=utf-8").put(String.class, jsonModelDoc);
            
            return outcome;
            
        } catch (Exception e) {
			LOG.error(e);
			return null;
		}	
    }
    
    public static String deleteJson(String modelURI) {
    	
    	try {
    	
    		Client client = Client.create();
            WebResource webResourceClient = client.resource(modelURI);

            String outcome = webResourceClient.type("text/plain; charset=utf-8").accept("text/plain; charset=utf-8").delete(String.class);
            
            return outcome;
            
        } catch (Exception e) {
			LOG.error(e);
			return null;
		}	
    }
    
    public static MultivaluedMap<String, String> listJsonModels() {
    	
    	try {
    	
    		Client client = Client.create();
            WebResource webResourceClient = client.resource(BASE_URL + "/" + "list/json");
            
            ClientResponse response = webResourceClient.type("text/plain; charset=utf-8").accept("application/x-www-form-urlencoded; charset=utf-8").get(ClientResponse.class);
    		
            MultivaluedMap<String, String> entries = response.getEntity(MultivaluedMap.class);
            
    		return entries;
    		
        } catch (Exception e) {
			LOG.error(e);
			return null;
		}	
    }
    
    public static MultivaluedMap<String, String> listJsonModels(String modelName) {
    	
    	try {
    	
    		Client client = Client.create();
            WebResource webResourceClient = client.resource(BASE_URL + "/" + "list/json/" + modelName);
            
            ClientResponse response = webResourceClient.type("text/plain; charset=utf-8").accept("application/x-www-form-urlencoded; charset=utf-8").get(ClientResponse.class);
    		
            MultivaluedMap<String, String> entries = response.getEntity(MultivaluedMap.class);
			
    		return entries;
    		
        } catch (Exception e) {
			LOG.error(e);
			return null;
		}	
    }
}
