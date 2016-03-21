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
import eu.linksmart.services.mr.storage.StorageEntryJson;
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
	// add XMI Domain Model, user provide the model name, and the version of the given model is computed by the service
    // generated Id of the XmiModel is composed of model name and its version ("name:version")
	//
	public String addXmiModel(String modelName, String xmiModelDoc) throws RepositoryException {
		
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
    	
    	String identifier = entry.getModelName() + SEPERATOR + entry.getCounter();
    	
    	//
    	// generate Json representation for this Xmi Document
    	//
    	String jsonModelDoc = null;
    	try {
			jsonModelDoc = generateJsonModel(entry.getModelName(), entry.getCounter(), xmiModelDoc);
        } catch (Exception e) {
        	throw new RepositoryException("[addXmiModel] unable to generate Json document for model [" + modelName + "] not storing the Xmi document", e);
        }
    	
    	StorageEntryXmi addXmiEntry = new StorageEntryXmi(identifier, entry.getModelName(), entry.getCounter(), xmiModelDoc);
    	
    	DBStorageEntry addJsonEntry = new DBStorageEntry(addXmiEntry.getKey(), addXmiEntry.getName(), addXmiEntry.getVersion(), jsonModelDoc);
    	
        if (!storage.addXmi(addXmiEntry, addJsonEntry)) {
            throw new RepositoryException("[add] domain model Xmi document could not be stored for model [" + modelName + "]");
        }
        
        LOG.info("domain model Xmi document added successfully with identifier [" + identifier + "]");
        
        return identifier;
	}

	//
	// here modelIdentifier is actually model name and version ("name:version")
	//
	public String getXmiModel(String modelIdentifier) throws ResourceNotFound, Exception {
		
		LOG.debug("getting domain model Xmi document for identifier [" + modelIdentifier + "]");
    	
    	StorageEntryXmi entry = storage.getXmi(modelIdentifier);
        
        if (entry == null)
        	throw new ResourceNotFound("[get] domain model Xmi document could not be found for identifier - " + modelIdentifier);
        	
        LOG.info("domain model Xmi document retrieved successfully for identifier [" + entry.getKey() + "]");
        
        return entry.getValue();
    }
	
	public String getXmiModelJson(String modelIdentifier) throws ResourceNotFound, Exception {
		
		LOG.debug("getting XmiModelJson document with identifier [" + modelIdentifier + "]");
    	
    	DBStorageEntry entry = storage.getXmiJson(modelIdentifier);
        
        if (entry == null)
        	throw new ResourceNotFound("[getXmiModelJson] document could not be found for identifier: " + modelIdentifier);
        
        LOG.info("XmiModelJson document retrieved successfully for identifier [" + modelIdentifier + "]");
        
        return entry.getValue();  
    }

	public String updateXmiModel(String modelIdentifier, String xmiModelDoc) throws ResourceNotFound, RepositoryException {
		
		LOG.debug("updating domain model Xmi document for identifier [" + modelIdentifier + "]");
    	
    	String[] identifierTokens = modelIdentifier.split(SEPERATOR);
		String modelName = identifierTokens[0];
		int version = Integer.parseInt(identifierTokens[1]);
		
    	String jsonModelDoc = null;
    	try {
			jsonModelDoc = generateJsonModel(modelName, version, xmiModelDoc);
        } catch (Exception e) {
        	throw new RepositoryException("[add] unable to generate Json document for model [" + modelName + "] not storing the Xmi document", e);
        }
    	
    	StorageEntryXmi updateXmiEntry = new StorageEntryXmi(modelIdentifier, modelName, version, xmiModelDoc);
    	
    	DBStorageEntry updateJsonEntry = new DBStorageEntry(modelIdentifier, modelName, version, jsonModelDoc);
        
        if (!storage.updateXmi(updateXmiEntry, updateJsonEntry))
        	throw new ResourceNotFound("[update] domain model Xmi document could not be updated for identifier [" + modelIdentifier + "] probably identifier doesn't already exist");
       
        LOG.info("domain model Xmi document updated successfully for identifier [" + modelIdentifier + "]");
        
        return updateXmiEntry.getKey();
	}
	
	public boolean deleteXmiModel(String modelIdentifier) throws ResourceNotFound {
		
		LOG.debug( "removing domain model Xmi document for identifier [" + modelIdentifier + "]");
    	
    	if (!storage.deleteXmi(modelIdentifier))
            throw new ResourceNotFound("[delete] domain model Xmi document with identifier [" + modelIdentifier + "] does not exist.");
               
        LOG.info("domain model Xmi document removed successfully for identifier [" + modelIdentifier + "]");
        
        return true;
	}
	
	public List<String> listXmi() throws Exception {
		
		Vector<String> result = new Vector<String>();
		
		List<StorageEntryXmi> entries = storage.listXmi();
        Iterator<StorageEntryXmi> it = entries.iterator();
        
        while (it.hasNext()) {
            result.add(it.next().getKey());
        }
               
        return result;
	}
	
	public List<String> listXmi(String modelName) throws Exception {
		
		Vector<String> result = new Vector<String>();
		
		List<StorageEntryXmi> entries = storage.listXmi(modelName);
        Iterator<StorageEntryXmi> it = entries.iterator();
        
        while (it.hasNext()) {
            result.add(it.next().getKey());
        }
               
        return result;
	}
	
	public String getLatestXmi(String modelName) throws ResourceNotFound, Exception {
		
		LOG.debug("getting latest domain model Xmi document for model: " + modelName);
    	
    	String modelIdentifier = null;
    	
    	CounterEntry versionEntry = storage.getCounterEntry(modelName);
    	
    	if(versionEntry == null) {
    		throw new ResourceNotFound("[getLatestXmi] no such domain model Xmi document found for model [" + modelName + "]");
    	} else {
    		modelIdentifier = modelName + SEPERATOR + versionEntry.getCounter();
    	}
    	
    	StorageEntryXmi entry = storage.getXmi(modelIdentifier);
        
        if (entry == null)
        	throw new ResourceNotFound("[getLatestXmi] domain model Xmi document could not be found for identifier - " + modelIdentifier);
        	
        LOG.info("domain model Xmi document (latest) retrieved successfully for identifier [" + entry.getKey() + "]");
        
        return entry.getValue();
	}
	
	public String getLatestXmiJson(String modelName) throws ResourceNotFound, Exception {
		
		LOG.debug("getting latest Xmi-Json document for model: " + modelName);
    	
    	String modelIdentifier = null;
    	
    	CounterEntry versionEntry = storage.getCounterEntry(modelName);
    	
    	if(versionEntry == null) {
    		throw new ResourceNotFound("[getLatestXmiJson] no such Xmi-Json document found for model [" + modelName + "]");
    	} else {
    		modelIdentifier = modelName + SEPERATOR + versionEntry.getCounter();
    	}
    	
    	DBStorageEntry entry = storage.getXmiJson(modelIdentifier);
        
        if (entry == null)
        	throw new ResourceNotFound("[getLatestXmiJson] domain model Xmi document could not be found for identifier - " + modelIdentifier);
        	
        LOG.info("domain model Json document (latest) retrieved successfully for identifier [" + entry.getKey() + "]");
        
        return entry.getValue();
	}
	
	public List<String> listXmiJson() throws Exception {
		
		Vector<String> result = new Vector<String>();
		
		List<DBStorageEntry> entries = storage.listXmiJson();
        Iterator<DBStorageEntry> it = entries.iterator();
        
        while (it.hasNext()) {
            result.add(it.next().getKey());
        }
               
        return result;
	}
	
	public List<String> listXmiJson(String modelName) throws Exception {
		
		Vector<String> result = new Vector<String>();
		
		List<DBStorageEntry> entries = storage.listXmiJson(modelName);
        Iterator<DBStorageEntry> it = entries.iterator();
        
        while (it.hasNext()) {
            result.add(it.next().getKey());
        }
               
        return result;
	}
	
	//
	//
	//
	public String addJsonModel(String modelIdentifier, String jsonModelDoc) throws ResourceTypeUnknown, ResourceInvalid, RepositoryException {
		
		LOG.debug("adding domain model Json document with identifier [" + modelIdentifier + "]");
    	
    	JsonObject modelJsonString = null;
    	
    	try {
    		modelJsonString = new JsonParser().parse(jsonModelDoc).getAsJsonObject();
    	} catch(Exception e) {
    		throw new ResourceTypeUnknown("resource type is unknown, should be parsable json ", e);
    	}
    	
    	String modelName = null;
    	int version = 0;
    	String docIdentifier = null;
    	
    	try {
    		modelName = modelJsonString.get("name").getAsString();
    		version = Integer.parseInt(modelJsonString.get("version").getAsString());
    		docIdentifier = modelName + SEPERATOR + version;
    	} catch(Exception e) {
    		throw new ResourceInvalid("resource document is invalid: unable to read name and version from given json document",e);
    	}
    	
    	if(!(modelIdentifier.equals(docIdentifier)))
			throw new ResourceInvalid("unable to add Json document because provided identifier [" + modelIdentifier + "] is different from document's identifier [" + docIdentifier + "]");
    	   
    	StorageEntryJson addEntry = new StorageEntryJson(modelIdentifier, modelName, version, jsonModelDoc);
        
        if (!storage.addJson(addEntry))
        	throw new RepositoryException("[add] domain model Json document could not be stored for identifier [" + modelIdentifier + "] perhaps a document with same identifier already exist");
        
        LOG.info("domain model Json document added successfully with identifier [" + modelIdentifier + "]");
        
        return addEntry.getKey();
	}

	public String getJsonModel(String modelIdentifier) throws ResourceNotFound, Exception {
	
		LOG.debug("getting domain model Json document with identifier [" + modelIdentifier + "]");
    	
    	StorageEntryJson entry = storage.getJson(modelIdentifier);
        
        if (entry == null)
        	throw new ResourceNotFound("[get] domain model Json document could not be found for identifier: " + modelIdentifier);
        
        LOG.info("domain model Json document retrieved successfully for identifier [" + modelIdentifier + "]");
        
        return entry.getValue();
    }

	public String updateJsonModel(String modelIdentifier, String jsonModelDoc) throws ResourceNotFound, ResourceTypeUnknown, ResourceInvalid, Exception {
		
		LOG.debug("updating domain model Json document with identifier [" + modelIdentifier + "]");
    	
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
    		String docIdentifier = modelName + SEPERATOR + version;
    		if(!(modelIdentifier.equals(docIdentifier)))
    			throw new ResourceInvalid("provided identifier [" + modelIdentifier + "] is different from document's identifier [" + docIdentifier + "]");
    	} catch(Exception e) {
    		throw new ResourceInvalid("resource document is invalid: unable to read name and version from given json document",e);
    	}
    	
    	StorageEntryJson updateEntry = new StorageEntryJson(modelIdentifier, modelName, version, jsonModelDoc);
        
        if (!storage.updateJson(updateEntry)) {
            throw new ResourceNotFound("[update] domain model Json document could not be updated for identifier [" + modelIdentifier + "] probably identifier doesn't already exist");
        }

        LOG.info("domain model Json document updated successfully for identifier [" + modelIdentifier + "]");
        
        return updateEntry.getKey();
	}
	
	public boolean deleteJsonModel(String modelIdentifier) throws ResourceNotFound {
		
		LOG.debug("removing domain model Json document with identifier [" + modelIdentifier + "]");
    	
    	if (!storage.deleteJson(modelIdentifier))
    		throw new ResourceNotFound("[update] domain model Json document with identifier [" + modelIdentifier + "] does not exist.");
               
        LOG.info("domain model Json document removed successfully for identifier [" + modelIdentifier + "]");
        
        return true;
	}
	
	public List<String> listJson(String modelName) throws Exception {
		
		Vector<String> result = new Vector<String>();
		
		List<StorageEntryJson> entries = storage.listJson(modelName);
        Iterator<StorageEntryJson> it = entries.iterator();
        
        while (it.hasNext()) {
            result.add(it.next().getKey());
        }
               
        return result;
	}
	
	public List<String> listJson() throws Exception {
		
		Vector<String> result = new Vector<String>();
		
		List<StorageEntryJson> entries = storage.listJson();
        Iterator<StorageEntryJson> it = entries.iterator();
        
        while (it.hasNext()) {
            result.add(it.next().getKey());
        }
               
        return result;
	}
	
	private String generateJsonModel(String modelName,int version, String xmiDoc) throws Exception {
		return XmiToJsonConverter.toJson(modelName, version, xmiDoc);
	}
}
