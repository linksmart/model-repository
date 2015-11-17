package eu.linksmart.services.mr;

import java.io.InputStream;
import java.util.Properties;

public class MRApplication {
	
	private String host;
	private int port;
	private String pathContext = "model-repository";
	
	private final String PROPERTY_FILE = "/model-repository.properties";
	
	private RepositoryWebContainer container = null;

	public static void main(String[] args) {
		new MRApplication();
	}
	
	public MRApplication() {
		
		System.out.println("------------------------------------------------------------------------------------");
		System.out.println("staring the model repository");
		System.out.println("------------------------------------------------------------------------------------");
		
		try {
			readDefaults();
		} catch (Exception e) {
			System.err.println("unable to read configuration file: " + e.getMessage());
			System.exit(1);
		}
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
		    public void run() {
		    	shutdown();
		    }
		});
		
		try {
			container = new RepositoryWebContainer(this.host, this.port, this.pathContext);
			container.start();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("unable to start the jetty web container, exiting...");
			try {
				container.stop();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			System.exit(1);
		} 
	}
	
	public void shutdown() {
		try {
			System.out.println("------------------------------------------------------------------------------------");
			System.out.println("shutting down the model repository");
			System.out.println("------------------------------------------------------------------------------------");
			container.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}	
		System.exit(0);
	}
	
	private void readDefaults() throws Exception {
		
		Properties prop = new Properties();
		
		InputStream inputStream = getClass().getResourceAsStream(PROPERTY_FILE);
		prop.load(inputStream);
		
		this.host = prop.getProperty("model.repository.host");
		this.port = Integer.parseInt(prop.getProperty("model.repository.port"));
		this.pathContext = prop.getProperty("model.repository.path");
		
		System.out.println("using host: " + this.host);
		System.out.println("using port: " + this.port);
		System.out.println("using path: " + this.pathContext);
		
        inputStream.close();
	}

}
