package eu.linksmart.services.mr;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;

import org.junit.Test;

import eu.linksmart.services.mr.exceptions.RepositoryException;
import eu.linksmart.services.mr.exceptions.ResourceInvalid;
import eu.linksmart.services.mr.exceptions.ResourceNotFound;
import eu.linksmart.services.mr.exceptions.ResourceTypeUnknown;

public class PersistenceTest {
	
	public PersistenceTest() {
		
	}
	
	@Test
	public void testJsonPersistence() throws Exception {
		
		try {
			
			byte[] encoded = Files.readAllBytes(Paths.get(getClass().getResource("/model.json").toURI()));
			String jsonModelDoc = new String(encoded, Charset.defaultCharset());
			
			String identifier = ModelRepository.getInstance().addJsonModel("e3:123", jsonModelDoc);
			
			assertNotNull(ModelRepository.getInstance().getJsonModel(identifier));
		
			String jsonModelUpdate = new String(Files.readAllBytes(Paths.get(getClass().getResource("/model_update.json").toURI())), Charset.defaultCharset());
			
			assertNotNull(ModelRepository.getInstance().updateJsonModel(identifier, jsonModelUpdate));
			
			assertNotNull(ModelRepository.getInstance().getJsonModel(identifier));
			
			System.out.println("list-json: " + ModelRepository.getInstance().listJson("sample").size());

			ModelRepository.getInstance().deleteJsonModel(identifier);
			
			System.out.println("list-json: " + ModelRepository.getInstance().listJson().size());
			
		} catch (ResourceTypeUnknown | ResourceInvalid | RepositoryException | ResourceNotFound e) {
			fail("testJsonPersistence failed: " + e.getMessage());
		}	
	}
	
	@Test
	public void testDoubleAdd() throws Exception {
		
		String identifier = null;
		try {
			
			byte[] encoded = Files.readAllBytes(Paths.get(getClass().getResource("/model.json").toURI()));
			String jsonModelDoc = new String(encoded, Charset.defaultCharset());
			
			identifier = ModelRepository.getInstance().addJsonModel("e3:123", jsonModelDoc);
			
			// should fail
			ModelRepository.getInstance().addJsonModel("e3:123", jsonModelDoc);
			
		} catch (ResourceTypeUnknown | ResourceInvalid | RepositoryException e) {
			assertSame(RepositoryException.class, e.getClass());
			
		}
		
		try {
			ModelRepository.getInstance().deleteJsonModel(identifier);
		} catch (Exception e) {
			fail("not deleteing json document");
		}
	}
	
}
