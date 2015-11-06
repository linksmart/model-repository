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
	
	//@Test
	public void testStorage() throws IOException, URISyntaxException {
		
		try {
			
			byte[] encoded = Files.readAllBytes(Paths.get(getClass().getResource("/model.json").toURI()));
			String jsonModelDoc = new String(encoded, Charset.defaultCharset());
			
			String identifier = ModelRepository.getInstance().addModel(jsonModelDoc);
		
			System.out.println("get: " + ModelRepository.getInstance().getModel(identifier));
			
			String jsonModelUpdate = new String(Files.readAllBytes(Paths.get(getClass().getResource("/model_update.json").toURI())), Charset.defaultCharset());
			
			String identifierUpdate = ModelRepository.getInstance().addModel(jsonModelUpdate);
			
			System.out.println("get-updated: " + ModelRepository.getInstance().getModel(identifierUpdate));
			
			ModelRepository.getInstance().deleteModel(identifierUpdate);
			
		} catch (ResourceTypeUnknown | ResourceInvalid | RepositoryException | ResourceNotFound e) {
			e.printStackTrace();
		}	
	}
	
	@Test
	public void testAdd() throws IOException, URISyntaxException {
		
		try {
			
			byte[] encoded = Files.readAllBytes(Paths.get(getClass().getResource("/model.json").toURI()));
			String jsonModelDoc = new String(encoded, Charset.defaultCharset());
			
			ModelRepository.getInstance().addModel(jsonModelDoc);
			
			// should fail
			ModelRepository.getInstance().addModel(jsonModelDoc);
			
		} catch (ResourceTypeUnknown | ResourceInvalid | RepositoryException e) {
			assertSame(RepositoryException.class, e.getClass());
		}	
	}

}
