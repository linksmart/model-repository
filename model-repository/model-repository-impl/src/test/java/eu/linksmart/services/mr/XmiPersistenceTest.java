package eu.linksmart.services.mr;

import java.io.IOException;
import java.net.URISyntaxException;
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
	public void testXmiPersistence() throws IOException, URISyntaxException {
		
		try {
			
			byte[] encoded = Files.readAllBytes(Paths.get(getClass().getResource("/model.xmi").toURI()));
			String xmiModelDoc = new String(encoded, Charset.defaultCharset());
			
			String identifier = ModelRepository.getInstance().addXmiModel("sample", xmiModelDoc);
			assertNotNull(ModelRepository.getInstance().getXmiModel(identifier));
			
			String identifier2 = ModelRepository.getInstance().addXmiModel("sample", xmiModelDoc);
			assertNotNull(ModelRepository.getInstance().getXmiModel(identifier2));
			
			String xmiModelUpdate = new String(Files.readAllBytes(Paths.get(getClass().getResource("/model.xmi").toURI())), Charset.defaultCharset());
			
			ModelRepository.getInstance().updateXmiModel(identifier, xmiModelUpdate);
			assertNotNull(ModelRepository.getInstance().getXmiModel(identifier));
			
			System.out.println("list-xmi-model: " + ModelRepository.getInstance().listXmi("sample").size());
			
			ModelRepository.getInstance().deleteXmiModel(identifier);
			System.out.println("list-xmi-model: " + ModelRepository.getInstance().listXmi("sample").size());
			
			ModelRepository.getInstance().deleteXmiModel(identifier2);
			System.out.println("list-xmi: " + ModelRepository.getInstance().listXmi().size());
			
		} catch (ResourceTypeUnknown | ResourceInvalid | RepositoryException | ResourceNotFound e) {
			e.printStackTrace();
		}		
	}

}
