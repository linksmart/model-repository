package eu.linksmart.services.mr.client;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RepoClientApplication {
	
	public static void main(String[] args) {

		if (args.length == 0) {
			System.out.println("using command line arguments as follows:");
			System.out.println("[model-repo-url] [xmidocument name] [xmidocument location]");
    		System.exit(0);
		}
		
		if (args.length == 1) {
			if(args[0].equals("-h")) {
				System.out.println("using command line arguments as follows:");
				System.out.println("[model-repo-url] [xmidocument name] [xmidocument location]");
	    		System.exit(0);
	    	}
		}
		
		if (args.length == 3) {
			String endpoint = args[0];
	    	String modelName = args[1];
	    	String docPath = args[2];
	    	new RepoClientApplication(endpoint, modelName, docPath);
		}
		
		System.exit(0);
	}
	
	public RepoClientApplication(String endpoint, String modelName, String docPath) {
		
		try {
	    	
			ModelRepositoryClient.initialize(endpoint);
			
			String xmiModelString = new String(Files.readAllBytes(Paths.get(getClass().getResource(modelName).toURI())), Charset.defaultCharset());
			
			String modelURI = ModelRepositoryClient.addXmi(modelName, xmiModelString);
     
			System.out.println("URI-1: " + modelURI);
        			    	
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	System.err.println("unable to add deployment model onto model repository - reason: " + e.getMessage());
	    }
	}
	
}
