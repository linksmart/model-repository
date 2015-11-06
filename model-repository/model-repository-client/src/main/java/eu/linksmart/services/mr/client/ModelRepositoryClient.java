package eu.linksmart.services.mr.client;

import java.util.ArrayList;
import java.util.List;

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
	
	private static String BASE_URL = "http://localhost:9090/";
	
    public ModelRepositoryClient() {
    }
   
    public static void initialize(String url) {
    	BASE_URL = url;
    }

    public static String add(String jsonModelDoc) {
    	
    	try {
    	
    		Client client = Client.create();
            WebResource webResourceClient = client.resource(BASE_URL);

            String outcome = webResourceClient.type("application/json; charset=utf-8").accept("text/plain; charset=utf-8").post(String.class, jsonModelDoc);
            
            return outcome;
            
        } catch (Exception e) {
			LOG.error(e);
			return null;
		}	
    }
    
    public static String get(String modelURI) {
    	
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
    
    public static String update(String modelURI, String jsonModelDoc) {
    	
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
    
    public static String delete(String modelURI) {
    	
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
    
    public static List<String> listJsonModels() {
    	
    	try {
    	
    		Client client = Client.create();
            WebResource webResourceClient = client.resource(BASE_URL + "/" + "list/json");
            
            ClientResponse response = webResourceClient.type("text/plain; charset=utf-8").accept("application/x-www-form-urlencoded; charset=utf-8").get(ClientResponse.class);
    		
            MultivaluedMap<String, String> entries = response.getEntity(MultivaluedMap.class);
			
            ArrayList<String> list = new ArrayList<String>();
            
            for (int i = 0; i <= entries.size(); i++) {
				String model = entries.get("json").get(i);
				list.add(model);
			}
			
    		return list;
    		
        } catch (Exception e) {
			LOG.error(e);
			return null;
		}	
    }
    
    public static List<String> listXmiModels() {
    	
    	try {
    	
    		Client client = Client.create();
            WebResource webResourceClient = client.resource(BASE_URL + "/" + "list/xmi");
            
            ClientResponse response = webResourceClient.type("text/plain; charset=utf-8").accept("application/x-www-form-urlencoded; charset=utf-8").get(ClientResponse.class);
    		
            MultivaluedMap<String, String> entries = response.getEntity(MultivaluedMap.class);
			
            ArrayList<String> list = new ArrayList<String>();
            
            for (int i = 0; i <= entries.size(); i++) {
				String model = entries.get("xmi").get(i);
				list.add(model);
			}
			
    		return list;
    		
        } catch (Exception e) {
			LOG.error(e);
			return null;
		}	
    }
    
    //
    // XMI Model
    //
    public static String addXmi(String modelIdentifier, String xmiModelDoc) {
    	
    	try {
    	
    		Client client = Client.create();
            WebResource webResourceClient = client.resource(BASE_URL + "/xmi/" + modelIdentifier);

            String outcome = webResourceClient.type("application/json; charset=utf-8").accept("text/plain; charset=utf-8").post(String.class, xmiModelDoc);
            
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

            String outcome = webResourceClient.type("application/json; charset=utf-8").accept("text/plain; charset=utf-8").put(String.class, xmiModelDoc);
            
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
}
