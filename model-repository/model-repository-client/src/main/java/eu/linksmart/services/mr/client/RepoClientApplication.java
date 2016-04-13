package eu.linksmart.services.mr.client;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RepoClientApplication {
	
	private static final Logger LOG = LoggerFactory.getLogger(RepoClientApplication.class);

	public static void main(String[] args) {

		if (args.length == 1) {
			if(args[0].equals("-h")) {
	    		LOG.info("using command line arguments as follows:");
	    		LOG.info("[model-repo-url] [xmidocument name] [xmidocument location]");
	    		System.exit(0);
	    	}
		}
		
		if (args.length > 0) {
		    try {
		    	String endpoint = args[0];
		    	String modelName = args[1];
		    	String docPath = args[2];
		    	
				ModelRepositoryClient.initialize(endpoint);
				
				//String xmiModelString = new String(Files.readAllBytes(Paths.get(getClass().getResource("/simple_deployment.xmi").toURI())), Charset.defaultCharset());
				
				//String modelURI = ModelRepositoryClient.addXmi(modelName, xmiModelString);
	     
	        	//LOG.info("URI-1: " + modelURI);
	        			    	
		    } catch (Exception e) {
		    	LOG.error("argument [" + args[0] + "] must be an integer.", e);
		    	LOG.error("unable to read command line arguments");
		    	System.exit(1);
		    }
		}
		
		System.exit(0);
	}
	
}
