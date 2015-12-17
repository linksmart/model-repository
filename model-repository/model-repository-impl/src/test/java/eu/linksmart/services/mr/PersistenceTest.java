package eu.linksmart.services.mr;

import java.io.IOException;
import java.net.URISyntaxException;
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
	public void testStorage() throws IOException, URISyntaxException {
		
		try {
			
			byte[] encoded = Files.readAllBytes(Paths.get(getClass().getResource("/model.json").toURI()));
			String jsonModelDoc = new String(encoded, Charset.defaultCharset());
			
			String identifier = ModelRepository.getInstance().addModel(jsonModelDoc);
			assertNotNull(ModelRepository.getInstance().getModel(identifier));
		
			String jsonModelUpdate = new String(Files.readAllBytes(Paths.get(getClass().getResource("/model_update.json").toURI())), Charset.defaultCharset());
			assertNotNull(ModelRepository.getInstance().updateModel(identifier, jsonModelUpdate));
			assertNotNull(ModelRepository.getInstance().getModel(identifier));
			
			System.out.println("list-json: " + ModelRepository.getInstance().listJson("sample").size());

			ModelRepository.getInstance().deleteModel(identifier);
			
			System.out.println("list-json: " + ModelRepository.getInstance().listJson().size());
			
		} catch (ResourceTypeUnknown | ResourceInvalid | RepositoryException | ResourceNotFound e) {
			e.printStackTrace();
		}	
	}
	
	@Test
	public void testDoubleAdd() throws IOException, URISyntaxException {
		
		String identifier = null;
		try {
			
			byte[] encoded = Files.readAllBytes(Paths.get(getClass().getResource("/model.json").toURI()));
			String jsonModelDoc = new String(encoded, Charset.defaultCharset());
			
			identifier = ModelRepository.getInstance().addModel(jsonModelDoc);
			
			// should fail
			ModelRepository.getInstance().addModel(jsonModelDoc);
			
		} catch (ResourceTypeUnknown | ResourceInvalid | RepositoryException e) {
			assertSame(RepositoryException.class, e.getClass());
			
		}
		
		try {
			ModelRepository.getInstance().deleteModel(identifier);
		} catch (RepositoryException | ResourceNotFound e) {
			fail("not deleteing json document");
		}
	}
	
}
