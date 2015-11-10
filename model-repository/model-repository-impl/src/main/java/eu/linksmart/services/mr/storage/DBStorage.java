package eu.linksmart.services.mr.storage;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConfigurationFactory;
import org.apache.log4j.Logger;

import eu.linksmart.services.mr.ModelRepository;

/**
 *
 * @author hrasheed
 */
public class DBStorage {
	
	private static Logger LOG = Logger.getLogger(ModelRepository.class.getName());

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

    @SuppressWarnings("finally")
	public boolean add(DBStorageEntry entry) {
    	
        boolean result = true;
        
        EntityManager em = emf.createEntityManager();
        
        //
    	// check if resource with same identifier already exist
    	//
    	DBStorageEntry exist = em.find(DBStorageEntry.class, entry.getKey());
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
	public boolean update(DBStorageEntry entry) {
    	
        boolean result = true;
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();
            DBStorageEntry update = em.find(DBStorageEntry.class, entry.getKey());
            update.setType(entry.getType());
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
	public DBStorageEntry get(String resourceKey) {
    	
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
	public boolean delete(String resourceKey) {
    	
    	boolean result = true;
    	EntityManager em = emf.createEntityManager();
    	
    	try {
            String jpql = "delete from DBStorageEntry se where se.key=\"" + resourceKey + "\"";
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
	public List<DBStorageEntry> filterByType(int type) {
    	
    	List<DBStorageEntry> queryList = null;
    	EntityManager em = emf.createEntityManager();
    	
    	try {
    		em.getTransaction().begin();
            String jpql = "select se from DBStorageEntry se where se.type=" + type;
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