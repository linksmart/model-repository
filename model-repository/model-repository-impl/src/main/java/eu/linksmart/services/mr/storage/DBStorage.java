package eu.linksmart.services.mr.storage;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConfigurationFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.linksmart.services.mr.ModelRepository;

/**
 *
 * @author hrasheed
 */
public class DBStorage {
	
	private final Logger LOG = LoggerFactory.getLogger(ModelRepository.class);

    private EntityManagerFactory emf = null;
    
    private static Configuration config ;

    public DBStorage() {
        init();
        String pUnit = config.getString("dbstorage.persistenceUnit") ;
        emf = Persistence.createEntityManagerFactory(pUnit);
        LOG.info("persistence engine is initialized");
    }

    private void init() {
        try {
            ConfigurationFactory factory = new ConfigurationFactory("config.xml");
            config = factory.getConfiguration();
        } catch (ConfigurationException ex) {
            ex.printStackTrace();
        }
    }
    
    public void close() {
    	if(emf != null) {
    		emf.close();
    		LOG.info("persistence engine is terminated");
    	}
    }
    
    @SuppressWarnings("finally")
	public CounterEntry getCounterEntry(String identifier) {
    	
        EntityManager em = emf.createEntityManager();
        
        //
    	// check if document with same identifier already exist
    	//
        CounterEntry entry = null;
        try {
            em.getTransaction().begin();
            entry = (CounterEntry) em.find(CounterEntry.class, identifier);
            em.getTransaction().commit();
    	} catch (Exception e) {
    		entry = null;
        } finally {
            em.close();
            return entry;
        }
    }
    
    @SuppressWarnings("finally")
	public boolean addCounterEntry(CounterEntry entry) {
    	
    	boolean result = true;
        
        EntityManager em = emf.createEntityManager();
        	
        try {
            em.getTransaction().begin();
            em.persist(entry);
            em.getTransaction().commit();
        } catch (Exception e) {
            result = false;
        } finally {
            em.close();
            return result;
        }
    }
    
    @SuppressWarnings("finally")
	public boolean updateCounterEntry(CounterEntry entry) {
    	
    	boolean result = true;
        
        EntityManager em = emf.createEntityManager();
        	
        try {
            em.getTransaction().begin();
            CounterEntry update = em.find(CounterEntry.class, entry.getModelName());
            update.setCounter(entry.getCounter());
            em.getTransaction().commit();
        } catch (Exception e) {
            result = false;
        } finally {
            em.close();
            return result;
        }
    }
    
    //
    // XMI document
    //
    @SuppressWarnings("finally")
	public boolean addXmi(StorageEntryXmi xmiEntry, DBStorageEntry jsonEntry) {
    	
        boolean result = true;
        
        EntityManager em = emf.createEntityManager();
        
        try {
            em.getTransaction().begin();
            em.persist(xmiEntry);
            em.persist(jsonEntry);
            em.getTransaction().commit();
        } catch (Exception e) {
        	e.printStackTrace();
            result = false;
        } finally {
            em.close();
            return result;
        }
    }
    
    @SuppressWarnings("finally")
	public StorageEntryXmi getXmi(String resourceKey) {
    	
    	StorageEntryXmi result = null;
    	EntityManager em = emf.createEntityManager();
    	
    	try {
            em.getTransaction().begin();
            result = (StorageEntryXmi) em.find(StorageEntryXmi.class, resourceKey);
            em.getTransaction().commit();
    	} catch (Exception e) {
    		result = null;
        } finally {
            em.close();
            return result;
        }
    }
    
    @SuppressWarnings("finally")
	public DBStorageEntry getXmiJson(String resourceKey) {
    	
    	DBStorageEntry result = null;
    	EntityManager em = emf.createEntityManager();
    	
    	try {
            em.getTransaction().begin();
            result = (DBStorageEntry) em.find(DBStorageEntry.class, resourceKey);
            em.getTransaction().commit();
    	} catch (Exception e) {
    		result = null;
        } finally {
            em.close();
            return result;
        }
    }
    
    @SuppressWarnings("finally")
	public boolean updateXmi(StorageEntryXmi xmiEntry, DBStorageEntry jsonEntry) {
    	
        boolean result = true;
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            StorageEntryXmi updateXmi = em.find(StorageEntryXmi.class, xmiEntry.getKey());
            updateXmi.setValue(xmiEntry.getValue());
            DBStorageEntry updateJson = em.find(DBStorageEntry.class, jsonEntry.getKey());
            updateJson.setValue(jsonEntry.getValue());
            em.getTransaction().commit();
        } catch (Exception e) {
            result = false;
        } finally {
            em.close();
            return result;
        }
    }

    @SuppressWarnings("finally")
	public boolean deleteXmi(String resourceKey) {
    	
    	boolean result = true;
    	EntityManager em = emf.createEntityManager();
    	
    	try {
            String xmiJpql = "delete from StorageEntryXmi se where se.key=\"" + resourceKey + "\"";
            String jsonJpql = "delete from DBStorageEntry se where se.key=\"" + resourceKey + "\"";
            em.getTransaction().begin();
            Query xmiQuery = em.createQuery(xmiJpql);
            int xmiRow = xmiQuery.executeUpdate();
            Query jsonQuery = em.createQuery(jsonJpql);
            int jsonRow = jsonQuery.executeUpdate();
            em.getTransaction().commit();
            if (xmiRow == 0 || jsonRow == 0) {
            	result = false;
            } 
    	} catch (Exception e) {
    		result = false;
        } finally {
            em.close();
            return result;
        }
    }
    
    @SuppressWarnings("finally")
	public List<StorageEntryXmi> listXmi() {
    	
    	List<StorageEntryXmi> queryList = null;
    	EntityManager em = emf.createEntityManager();
    	
    	try {
    		em.getTransaction().begin();
            String jpql = "select se from StorageEntryXmi se";
            Query query = em.createQuery(jpql);
            queryList = query.getResultList();
            em.getTransaction().commit();
    	} catch (Exception e) {
    		queryList = null;
        } finally {
            em.close();
            return queryList;
        }
    }
    
    @SuppressWarnings("finally")
	public List<StorageEntryXmi> listXmi(String modelName) {
    	
    	List<StorageEntryXmi> queryList = null;
    	EntityManager em = emf.createEntityManager();
    	
    	try {
    		em.getTransaction().begin();
            String jpql = "select se from StorageEntryXmi se where se.name=\"" + modelName + "\"";
            Query query = em.createQuery(jpql);
            queryList = query.getResultList();
            em.getTransaction().commit();
    	} catch (Exception e) {
    		queryList = null;
        } finally {
            em.close();
            return queryList;
        }
    }
    
    @SuppressWarnings("finally")
	public List<DBStorageEntry> listXmiJson() {
    	
    	List<DBStorageEntry> queryList = null;
    	EntityManager em = emf.createEntityManager();
    	
    	try {
    		em.getTransaction().begin();
            String jpql = "select se from DBStorageEntry se";
            Query query = em.createQuery(jpql);
            queryList = query.getResultList();
            em.getTransaction().commit();
    	} catch (Exception e) {
    		queryList = null;
        } finally {
            em.close();
            return queryList;
        }
    }
    
    @SuppressWarnings("finally")
	public List<DBStorageEntry> listXmiJson(String modelName) {
    	
    	List<DBStorageEntry> queryList = null;
    	EntityManager em = emf.createEntityManager();
    	
    	try {
    		em.getTransaction().begin();
            String jpql = "select se from DBStorageEntry se where se.name=\"" + modelName + "\"";
            Query query = em.createQuery(jpql);
            queryList = query.getResultList();
            em.getTransaction().commit();
    	} catch (Exception e) {
    		queryList = null;
        } finally {
            em.close();
            return queryList;
        }
    }

    //
    // Json Documents
    //
    @SuppressWarnings("finally")
	public boolean addJson(StorageEntryJson entry) {
    	
        boolean result = true;
        
        EntityManager em = emf.createEntityManager();
        
        //
    	// check if resource with same identifier already exist
    	//
        StorageEntryJson exist = em.find(StorageEntryJson.class, entry.getKey());
        	
    	if(exist != null) {
    		em.close();
    		return false;
    	}
    		
        try {
            em.getTransaction().begin();
            em.persist(entry);
            em.getTransaction().commit();
        } catch (Exception e) {
            result = false;
        } finally {
            em.close();
            return result;
        }
    }
    
    @SuppressWarnings("finally")
	public boolean updateJson(StorageEntryJson entry) {
    	
        boolean result = true;
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            StorageEntryJson update = em.find(StorageEntryJson.class, entry.getKey());
            update.setValue(entry.getValue());
            em.getTransaction().commit();
        } catch (Exception e) {
            result = false;
        } finally {
            em.close();
            return result;
        }
    }

    @SuppressWarnings("finally")
	public StorageEntryJson getJson(String resourceKey) {
    	
    	StorageEntryJson result = null;
    	EntityManager em = emf.createEntityManager();
    	
    	try {
            em.getTransaction().begin();
            result = (StorageEntryJson) em.find(StorageEntryJson.class, resourceKey);
            em.getTransaction().commit();
    	} catch (Exception e) {
    		result = null;
        } finally {
            em.close();
            return result;
        }
    }

    @SuppressWarnings("finally")
	public boolean deleteJson(String resourceKey) {
    	
    	boolean result = true;
    	EntityManager em = emf.createEntityManager();
    	
    	try {
            String jpql = "delete from StorageEntryJson se where se.key=\"" + resourceKey + "\"";
            em.getTransaction().begin();
            Query query = em.createQuery(jpql);
            int row = query.executeUpdate();
            em.getTransaction().commit();
            if (row == 0) {
            	result = false;
            } 
    	} catch (Exception e) {
    		result = false;
        } finally {
            em.close();
            return result;
        }
    }
    
    @SuppressWarnings("finally")
	public List<StorageEntryJson> listJson() {
    	
    	List<StorageEntryJson> queryList = null;
    	EntityManager em = emf.createEntityManager();
    	
    	try {
    		em.getTransaction().begin();
            String jpql = "select se from StorageEntryJson se";
            Query query = em.createQuery(jpql);
            queryList = query.getResultList();
            em.getTransaction().commit();
    	} catch (Exception e) {
    		queryList = null;
        } finally {
            em.close();
            return queryList;
        }
    }
    
    @SuppressWarnings("finally")
	public List<StorageEntryJson> listJson(String modelName) {
    	
    	List<StorageEntryJson> queryList = null;
    	EntityManager em = emf.createEntityManager();
    	
    	try {
    		em.getTransaction().begin();
            String jpql = "select se from StorageEntryJson se where se.name=\"" + modelName + "\"";
            Query query = em.createQuery(jpql);
            queryList = query.getResultList();
            em.getTransaction().commit();
    	} catch (Exception e) {
    		queryList = null;
        } finally {
            em.close();
            return queryList;
        }
    }
}
