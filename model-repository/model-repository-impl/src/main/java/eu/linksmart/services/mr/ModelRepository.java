package eu.linksmart.services.mr;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import eu.linksmart.services.mr.exceptions.RepositoryException;
import eu.linksmart.services.mr.exceptions.ResourceInvalid;
import eu.linksmart.services.mr.exceptions.ResourceNotFound;
import eu.linksmart.services.mr.exceptions.ResourceTypeUnknown;
import eu.linksmart.services.mr.storage.DBStorage;
import eu.linksmart.services.mr.storage.DBStorageEntry;

/**
 * Model Repository
 * 
 * @author hrasheed
 * 
 */
public class ModelRepository {

	private static Logger LOG = Logger.getLogger(ModelRepository.class.getName());
	
	private static ModelRepository repositoryInstance = null;
	
	private DBStorage storage = null;
	
	public static final int DOMAIN_MODEL_JSON = 1;
	public static final int DOMAIN_MODEL_XMI = 2;
    
    private synchronized void initialize() {
        storage = new DBStorage();
    }
    
    private ModelRepository() {
    	initialize();
    }
    
    public static synchronized ModelRepository getInstance() {
        if (repositoryInstance == null ) {
        	repositoryInstance = new ModelRepository();
        }
        return repositoryInstance;
    }

	public String addModel(String jsonModelDoc) throws ResourceTypeUnknown, ResourceInvalid, RepositoryException {
		
		try {
        	
        	LOG.debug("adding domain model Json document");
        	
        	JsonObject modelJsonString = null;
        	
        	try {
        		modelJsonString = new JsonParser().parse(jsonModelDoc).getAsJsonObject();
        	} catch(Exception e) {
        		throw new ResourceTypeUnknown("resource type is unknown", e);
        	}
        	
        	String identifier = null;
        	
        	try {
        		identifier = modelJsonString.get("name").getAsString() + "." + modelJsonString.get("version").getAsString();
        	} catch(Exception e) {
        		throw new ResourceInvalid("resource document is invalid",e);
        	}
        	    
            DBStorageEntry addEntry = new DBStorageEntry(identifier, DOMAIN_MODEL_JSON, jsonModelDoc);
            
            if (!storage.add(addEntry))
            	throw new RepositoryException("[add] domain model Json document could not be stored for identifier: " + identifier);
            
            LOG.info("domain model Json document added successfully with identifier: " + identifier);
            
            return addEntry.getKey();
            
        } catch (Exception e) {
        	throw new RepositoryException(e);
        }
	}

	public String getModel(String identifier) throws ResourceNotFound, RepositoryException {
	
        try {
        	
        	LOG.debug( "getting domain model Json document" );
        	
        	DBStorageEntry entry = storage.get(identifier);
            
            if (entry == null)
            	throw new ResourceNotFound("[get] domain model Json document could not be found for identifier: " + identifier);
            
            if(entry.getType() != DOMAIN_MODEL_JSON)
            	throw new ResourceTypeUnknown("[get] retrieved domain model is not Json for identifier: " + identifier);

            LOG.info( "domain model Json document retrieved successfully for identifier: " + identifier);
            
            return entry.getValue();
            
        } catch (Exception e) {
        	throw new RepositoryException(e);
        }
    }

	public String updateModel(String identifier, String jsonModelDoc) throws ResourceNotFound, ResourceTypeUnknown, ResourceInvalid, RepositoryException {
		
		try {
        	
        	LOG.debug( "updating domain model Json document" );
        	
        	JsonObject modelJsonString = null;
        	
        	try {
        		modelJsonString = new JsonParser().parse(jsonModelDoc).getAsJsonObject();
        	} catch(Exception e) {
        		throw new ResourceTypeUnknown("resource type is unknown", e);
        	}
        	
        	try {
        		String docIdentifier = modelJsonString.get("name").getAsString() + "." + modelJsonString.get("version").getAsString();
        		if(!(identifier.equals(docIdentifier)))
        			throw new ResourceInvalid("given identifier [" + identifier + "] is different from document identifier [" + docIdentifier + "]");
        	} catch(Exception e) {
        		throw new ResourceInvalid("resource document is invalid",e);
        	}
        	
            DBStorageEntry updateEntry = new DBStorageEntry(identifier, DOMAIN_MODEL_JSON, jsonModelDoc);
            
            if (!storage.update(updateEntry)) {
                throw new ResourceNotFound("[update] domain model Json document could not be updated for identifier: " + identifier);
            }

            LOG.info( "domain model Json document updated successfully for identifier: " + identifier);
            
            return updateEntry.getKey();
            
        } catch (Exception e) {
        	throw new RepositoryException(e);
        }
	}
	
	public boolean deleteModel(String modelIdentifier) throws ResourceNotFound, RepositoryException {
		
		try {
        	
        	LOG.debug( "removing domain model Json document" );
        	
        	if (!storage.delete(modelIdentifier))
        		throw new ResourceNotFound("[update] domain model Json document with identifier [" + modelIdentifier + "] does not exist.");
                   
            LOG.info( "domain model Json document removed successfully with identifier: " + modelIdentifier);
            
            return true;
            
        } catch (Exception e) {
        	throw new RepositoryException(e);
        }
	}
	
	//
	// XMI Domain Model
	//
	public String addXmiModel(String modelIdentifier, String xmiModelDoc) throws ResourceTypeUnknown, ResourceInvalid, RepositoryException {
		
		try {
        	
        	LOG.debug("adding domain model Xmi document");
        	    
            DBStorageEntry addEntry = new DBStorageEntry(modelIdentifier, DOMAIN_MODEL_XMI, xmiModelDoc);
            
            if (!storage.add(addEntry)) {
                throw new RepositoryException("[add] domain model Xmi document could not be stored for identifier: " + modelIdentifier);
            }

            LOG.info("domain model Xmi document added successfully with identifier: " + modelIdentifier);
            
            return addEntry.getKey();
            
        } catch (Exception e) {
        	throw new RepositoryException(e);
        }
	}

	public String getXmiModel(String identifier) throws ResourceNotFound, RepositoryException {
		
        try {
        	
        	LOG.debug( "getting domain model Xmi document" );
        	
        	DBStorageEntry entry = storage.get(identifier);
            
            if (entry == null)
            	throw new ResourceNotFound("[get] domain model Xmi document could not be found for identifier: " + identifier);
            
            if(entry.getType() != DOMAIN_MODEL_XMI)
            	throw new ResourceTypeUnknown("[get] retrieved domain model is not Xmi for identifier: " + identifier);
            	
            LOG.info( "domain model Xmi document retrieved successfully for identifier: " + identifier);
            
            return entry.getValue();
            
        } catch (Exception e) {
        	throw new RepositoryException(e);
        }
    }

	public String updateXmiModel(String identifier, String xmiModelDoc) throws ResourceNotFound, ResourceTypeUnknown, ResourceInvalid, RepositoryException {
		
		try {
        	
        	LOG.debug( "updating domain model Xmi document" );
        	
            DBStorageEntry updateEntry = new DBStorageEntry(identifier, DOMAIN_MODEL_XMI, xmiModelDoc);
            
            if (!storage.update(updateEntry))
            	throw new ResourceNotFound("[update] domain model Xmi document could not be updated for identifier: " + identifier);
           
            LOG.info( "domain model Xmi document updated successfully for identifier: " + identifier);
            
            return updateEntry.getKey();
            
        } catch (Exception e) {
        	throw new RepositoryException(e);
        }
	}
	
	public boolean deleteXmiModel(String modelIdentifier) throws ResourceNotFound, RepositoryException {
		
		try {
        	
        	LOG.debug( "removing domain model Xmi document" );
        	
        	if (!storage.delete(modelIdentifier))
                throw new ResourceNotFound("[update] domain model Xmi document with identifier [" + modelIdentifier + "] does not exist.");
                   
            LOG.info( "domain model Xmi document removed successfully with identifier: " + modelIdentifier);
            
            return true;
            
        } catch (Exception e) {
        	throw new RepositoryException(e);
        }
	}
	
	public List<String> listAllModels(int type) throws RepositoryException {
		
		try {
        	
			List<DBStorageEntry> entries = storage.filterByType(type);
			
			Vector<String> result = new Vector<String>();
			
            Iterator<DBStorageEntry> it = entries.iterator();
            
            while (it.hasNext()) {
                result.add(it.next().getKey());
            }
                   
            return result;
            
        } catch (Exception e) {
        	throw new RepositoryException(e);
        }
	}
	
}
