package eu.linksmart.services.mr.client;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class RepoClientApplication {
	
	public static void main(String[] args) {

		if (args.length == 0) {
			System.out.println("using command line arguments as follows:");
			System.out.println("[model-repo-url] [xmidocument name] [file-name]");
    		System.exit(0);
		}
		
		if (args.length == 1) {
			if(args[0].equals("-h")) {
				System.out.println("using command line arguments as follows:");
				System.out.println("[model-repo-url] [xmidocument name] [file-name]");
	    		System.exit(0);
	    	}
		}
		
		if (args.length == 3) {
			String endpoint = args[0];
	    	String modelName = args[1];
	    	String fileName = args[2];
	    	System.out.println("endpoint: " + endpoint);
	    	System.out.println("modelName: " + modelName);
	    	System.out.println("fileName: " + fileName);
	    	new RepoClientApplication(endpoint, modelName, fileName);
		}
		
		System.exit(0);
	}
	
	public RepoClientApplication(String endpoint, String modelName, String fileName) {
		
		String xmiModelString = null;
		
		try {
			xmiModelString = new String(Files.readAllBytes(Paths.get(getClass().getResource("/" + fileName).toURI())), Charset.defaultCharset());
			System.out.println("domain model is loaded successfully");
		} catch (Exception e) {
			e.printStackTrace();
	    	System.err.println("unable to load deployment model - reason: " + e.getMessage());
	    	System.exit(1);
		}
		
		try {
			System.out.println("invoking model repo client to add model: " + modelName);
			ModelRepositoryClient.initialize(endpoint);
			String modelURI = ModelRepositoryClient.addXmi(modelName, xmiModelString);
			System.out.println("URI-1: " + modelURI);
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	System.err.println("unable to add deployment model onto model repository - reason: " + e.getMessage());
	    	System.exit(1);
	    }
	}
	
}
