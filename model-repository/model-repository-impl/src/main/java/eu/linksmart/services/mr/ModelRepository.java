package eu.linksmart.services.mr;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import eu.linksmart.services.mr.exceptions.RepositoryException;
import eu.linksmart.services.mr.exceptions.ResourceInvalid;
import eu.linksmart.services.mr.exceptions.ResourceNotFound;
import eu.linksmart.services.mr.exceptions.ResourceTypeUnknown;
import eu.linksmart.services.mr.storage.CounterEntry;
import eu.linksmart.services.mr.storage.DBStorage;
import eu.linksmart.services.mr.storage.DBStorageEntry;
import eu.linksmart.services.mr.storage.StorageEntryXmi;

/**
 * Model Repository
 * 
 * @author hrasheed
 * 
 */
public class ModelRepository {

	private final Logger LOG = LoggerFactory.getLogger(ModelRepository.class);
	
	private static ModelRepository repositoryInstance = null;
	
	private DBStorage storage = null;
	
	public static final int DOMAIN_MODEL_JSON = 1;
	public static final int DOMAIN_MODEL_XMI = 2;
	
	private final String SEPERATOR = ":";
    
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
    
    public void closePersistence() {
    	if(storage != null)
    		storage.close();
    }
    
    //
	// XMI Domain Model
	//
	public String addXmiModel(String modelName, String xmiModelDoc) throws ResourceTypeUnknown, ResourceInvalid, RepositoryException {
		
		try {
        	
        	LOG.debug("adding domain model Xmi document for model: " + modelName);
        	
        	CounterEntry entry = storage.getCounterEntry(modelName);
        	
        	if(entry == null) {
        		entry = new CounterEntry(modelName, 1);
        		if (!storage.addCounterEntry(entry)) {
                    throw new RepositoryException("[add] domain model Xmi document could not be stored for model [" + modelName + "] unable to manage version");
                }
        	} else {
        		entry.setCounter(entry.getCounter() + 1);
        		if (!storage.updateCounterEntry(entry)) {
                    throw new RepositoryException("[add] domain model Xmi document could not be stored for model [" + modelName + "] unable to manage version");
                }
        	}
        	
        	String key = entry.getIdentifier() + SEPERATOR + entry.getCounter();
        	
        	StorageEntryXmi addEntry = new StorageEntryXmi(key, entry.getIdentifier(), entry.getCounter(), xmiModelDoc);
            
            if (!storage.addXmi(addEntry)) {
                throw new RepositoryException("[add] domain model Xmi document could not be stored for model [" + modelName + "]");
            }
 
            LOG.info("domain model Xmi document added successfully with identifier [" + key + "]");
            
            return key;
            
        } catch (Exception e) {
        	throw new RepositoryException(e);
        }
	}

	public String getXmiModel(String modelIdentifier) throws ResourceNotFound, RepositoryException {
		
        try {
        	
        	LOG.debug("getting domain model Xmi document for identifier [" + modelIdentifier + "]");
        	
        	StorageEntryXmi entry = storage.getXmi(modelIdentifier);
            
            if (entry == null)
            	throw new ResourceNotFound("[get] domain model Xmi document could not be found for identifier - " + modelIdentifier);
            	
            LOG.info( "domain model Xmi document retrieved successfully for identifier [" + entry.getKey() + "]");
            
            return entry.getValue();
            
        } catch (Exception e) {
        	throw new RepositoryException(e);
        }
    }

	public String updateXmiModel(String identifier, String xmiModelDoc) throws ResourceNotFound, ResourceTypeUnknown, ResourceInvalid, RepositoryException {
		
		try {
        	
        	LOG.debug("updating domain model Xmi document for identifier [" + identifier + "]");
        	
        	String[] identifierTokens = identifier.split(SEPERATOR);
			String modelName = identifierTokens[0];
			int version = Integer.parseInt(identifierTokens[1]);
			
        	StorageEntryXmi updateEntry = new StorageEntryXmi(identifier, modelName, version, xmiModelDoc);
            
            if (!storage.updateXmi(updateEntry))
            	throw new ResourceNotFound("[update] domain model Xmi document could not be updated for identifier [" + identifier + "] probably identifier doesn't already exist");
           
            LOG.info( "domain model Xmi document updated successfully for identifier [" + identifier + "]");
            
            return updateEntry.getKey();
            
        } catch (Exception e) {
        	throw new RepositoryException(e);
        }
	}
	
	public boolean deleteXmiModel(String modelIdentifier) throws ResourceNotFound, RepositoryException {
		
		try {
        	
        	LOG.debug( "removing domain model Xmi document for identifier [" + modelIdentifier + "]");
        	
        	if (!storage.deleteXmi(modelIdentifier))
                throw new ResourceNotFound("[delete] domain model Xmi document with identifier [" + modelIdentifier + "] does not exist.");
                   
            LOG.info( "domain model Xmi document removed successfully with identifier [" + modelIdentifier + "]");
            
            return true;
            
        } catch (Exception e) {
        	throw new RepositoryException(e);
        }
	}
	
	public List<String> listXmi(String modelName) throws RepositoryException {
		
		try {
        	
			Vector<String> result = new Vector<String>();
			
			List<StorageEntryXmi> entries = storage.listXmi(modelName);
            Iterator<StorageEntryXmi> it = entries.iterator();
            
            while (it.hasNext()) {
                result.add(it.next().getKey());
            }
                   
            return result;
            
        } catch (Exception e) {
        	throw new RepositoryException(e);
        }
	}
	
	public List<String> listXmi() throws RepositoryException {
		
		try {
        	
			Vector<String> result = new Vector<String>();
			
			List<StorageEntryXmi> entries = storage.listXmi();
            Iterator<StorageEntryXmi> it = entries.iterator();
            
            while (it.hasNext()) {
                result.add(it.next().getKey());
            }
                   
            return result;
            
        } catch (Exception e) {
        	throw new RepositoryException(e);
        }
	}

	public String addModel(String jsonModelDoc) throws ResourceTypeUnknown, ResourceInvalid, RepositoryException {
		
		try {
        	
        	LOG.debug("adding domain model Json document");
        	
        	JsonObject modelJsonString = null;
        	
        	try {
        		modelJsonString = new JsonParser().parse(jsonModelDoc).getAsJsonObject();
        	} catch(Exception e) {
        		throw new ResourceTypeUnknown("resource type is unknown, should be parsable json ", e);
        	}
        	
        	String identifier = null;
        	String modelName = null;
        	int version = 0;
        	
        	try {
        		modelName = modelJsonString.get("name").getAsString();
        		version = Integer.parseInt(modelJsonString.get("version").getAsString());
        		identifier =  modelName + SEPERATOR + version;
        	} catch(Exception e) {
        		throw new ResourceInvalid("resource document is invalid: unable to read name and version from given json document",e);
        	}
        	    
            DBStorageEntry addEntry = new DBStorageEntry(identifier, modelName, version, jsonModelDoc);
            
            if (!storage.add(addEntry))
            	throw new RepositoryException("[add] domain model Json document could not be stored for identifier [" + identifier + "] perhaps a document with same identifier already exist");
            
            LOG.info("domain model Json document added successfully with identifier [" + identifier + "]");
            
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
            
            LOG.info( "domain model Json document retrieved successfully for identifier [" + identifier + "]");
            
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
        	
        	String modelName = null;
        	int version = 0;
        	
        	try {
        		modelName = modelJsonString.get("name").getAsString();
        		version = Integer.parseInt(modelJsonString.get("version").getAsString());
        		String docIdentifier = modelName + SEPERATOR + version;;
        		if(!(identifier.equals(docIdentifier)))
        			throw new ResourceInvalid("provided identifier [" + identifier + "] is different from document's identifier [" + docIdentifier + "]");
        	} catch(Exception e) {
        		throw new ResourceInvalid("resource document is invalid: unable to read name and version from given json document",e);
        	}
        	
            DBStorageEntry updateEntry = new DBStorageEntry(identifier, modelName, version, jsonModelDoc);
            
            if (!storage.update(updateEntry)) {
                throw new ResourceNotFound("[update] domain model Json document could not be updated for identifier [" + identifier + "] probably identifier doesn't already exist");
            }

            LOG.info( "domain model Json document updated successfully for identifier [" + identifier + "]");
            
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
                   
            LOG.info( "domain model Json document removed successfully with identifier [" + modelIdentifier + "]");
            
            return true;
            
        } catch (Exception e) {
        	throw new RepositoryException(e);
        }
	}
	
	public String generateJsonModel(String modelIdentifier) throws ResourceNotFound, RepositoryException {
		
		try {
			
			StorageEntryXmi entry = storage.getXmi(modelIdentifier);
            
            if (entry == null)
            	throw new ResourceNotFound("[generateJsonModel] domain model Xmi document could not be found for identifier: " + modelIdentifier);
            
            String xmiDoc = entry.getValue();
            
        	return XmiToJsonConverter.toJson(modelIdentifier, xmiDoc);
        	
        } catch (Exception e) {
        	throw new RepositoryException(e);
        }
	}
	
	public List<String> listJson(String modelName) throws RepositoryException {
		
		try {
        	
			Vector<String> result = new Vector<String>();
			
			List<DBStorageEntry> entries = storage.listJson(modelName);
            Iterator<DBStorageEntry> it = entries.iterator();
            
            while (it.hasNext()) {
                result.add(it.next().getKey());
            }
                   
            return result;
            
        } catch (Exception e) {
        	throw new RepositoryException(e);
        }
	}
	
	public List<String> listJson() throws RepositoryException {
		
		try {
        	
			Vector<String> result = new Vector<String>();
			
			List<DBStorageEntry> entries = storage.listJson();
            Iterator<DBStorageEntry> it = entries.iterator();
            
            while (it.hasNext()) {
                result.add(it.next().getKey());
            }
                   
            return result;
            
        } catch (Exception e) {
        	throw new RepositoryException(e);
        }
	}
	
<<<<<<< HEAD
	public String generateJsonModel(String modelIdentifier) throws ResourceNotFound, RepositoryException {
		
		try {
			
			StorageEntryXmi entry = storage.getXmi(modelIdentifier);
            
            if (entry == null)
            	throw new ResourceNotFound("[generateJsonModel] domain model Xmi document could not be found for identifier: " + modelIdentifier);
            
            String xmiDoc = entry.getValue();

			int version = 1;
        	return XmiToJsonConverter.toJson(modelIdentifier, version, xmiDoc);
        	
        } catch (Exception e) {
        	throw new RepositoryException(e);
        }
	}
	
=======
>>>>>>> interface change, Identifier combine with version impl
}
