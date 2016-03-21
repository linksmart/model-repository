package eu.linksmart.services.mr;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;

import static org.junit.Assert.*;
import eu.linksmart.services.mr.exceptions.RepositoryException;
import eu.linksmart.services.mr.exceptions.ResourceInvalid;
import eu.linksmart.services.mr.exceptions.ResourceNotFound;
import eu.linksmart.services.mr.exceptions.ResourceTypeUnknown;

public class XmiPersistenceTest {
	
	@Test
	public void testXmiPersistence() throws Exception {
		
		try {
			
			byte[] encoded = Files.readAllBytes(Paths.get(getClass().getResource("/model.xmi").toURI()));
			String xmiModelDoc = new String(encoded, Charset.defaultCharset());
			
			String modelName = "sample";
			
			// adding first version
			String identifier = ModelRepository.getInstance().addXmiModel(modelName, xmiModelDoc);
			
			assertNotNull(ModelRepository.getInstance().getXmiModel(identifier));
			assertNotNull(ModelRepository.getInstance().getXmiModelJson(identifier));
			
			// adding second version
			String identifier2 = ModelRepository.getInstance().addXmiModel(modelName, xmiModelDoc);
			
			assertNotNull(ModelRepository.getInstance().getXmiModel(identifier2));
			assertNotNull(ModelRepository.getInstance().getXmiModelJson(identifier2));
			
			String xmiModelUpdate = new String(Files.readAllBytes(Paths.get(getClass().getResource("/model.xmi").toURI())), Charset.defaultCharset());
			
			assertNotNull(ModelRepository.getInstance().updateXmiModel(identifier, xmiModelUpdate));
			
			assertNotNull(ModelRepository.getInstance().getXmiModel(identifier));
			assertNotNull(ModelRepository.getInstance().getXmiModelJson(identifier));
			
			assertEquals(2, ModelRepository.getInstance().listXmi().size());
			assertEquals(2, ModelRepository.getInstance().listXmi(modelName).size());
			
			assertEquals(2, ModelRepository.getInstance().listXmiJson().size());
			assertEquals(2, ModelRepository.getInstance().listXmiJson(modelName).size());
			
			// deleting first version
			assertNotNull(ModelRepository.getInstance().deleteXmiModel(identifier));
			
			try {
				ModelRepository.getInstance().getXmiModelJson(identifier);
			} catch (Exception e) {
				assertSame(ResourceNotFound.class, e.getClass());
			}
			
			assertEquals(1, ModelRepository.getInstance().listXmi(modelName).size());
			assertEquals(1, ModelRepository.getInstance().listXmiJson(modelName).size());
			
			// deleting second version
			ModelRepository.getInstance().deleteXmiModel(identifier2);
			
			assertEquals(0, ModelRepository.getInstance().listXmi().size());
			assertEquals(0, ModelRepository.getInstance().listXmiJson().size());
			
		} catch (ResourceTypeUnknown | ResourceInvalid | RepositoryException | ResourceNotFound e) {
			e.printStackTrace();
		}		
	}

}
