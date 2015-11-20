package eu.linksmart.services.mr;

import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MRApplication {
	
	private final Logger LOG = LoggerFactory.getLogger(MRApplication.class);
	
	private String host;
	
	private int port;
	
	private String pathContext = "model-repository";
	
	private final String PROPERTY_FILE = "/model-repository.properties";
	
	private RepositoryWebContainer container = null;

	public static void main(String[] args) {
		new MRApplication();
	}
	
	public MRApplication() {
		
		LOG.info("------------------------------------------------------------------------------------");
		LOG.info("staring the model repository");
		LOG.info("------------------------------------------------------------------------------------");
		
		try {
			readDefaults();
		} catch (Exception e) {
			LOG.error("unable to read configuration file [" + PROPERTY_FILE + "] " + e.getMessage());
			System.exit(1);
		}
		
		container = new RepositoryWebContainer(this);
		container.start();
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
		    public void run() {
		    	shutdown();
		    }
		});
	}
	
	protected void shutdown() {
		LOG.info("------------------------------------------------------------------------------------");
		LOG.info("shutting down the model repository");
		LOG.info("------------------------------------------------------------------------------------");
		container.stopContainer();
		container.interrupt();
		try { Thread.sleep(1000); } catch (InterruptedException e) { }
		LOG.info("model repository terminated successfully");
		Runtime.getRuntime().runFinalization();
		System.exit(0);
		//Runtime.getRuntime().halt(0); // this method should be used very carefully since it doesn't gracefully shutdown the virtual machine
	}
	
	private void readDefaults() throws Exception {
		
		Properties prop = new Properties();
		
		InputStream inputStream = getClass().getResourceAsStream(PROPERTY_FILE);
		prop.load(inputStream);
		
		this.host = prop.getProperty("model.repository.host");
		this.port = Integer.parseInt(prop.getProperty("model.repository.port"));
		this.pathContext = prop.getProperty("model.repository.path");
		
		LOG.info("using host: " + this.host);
		LOG.info("using port: " + this.port);
		LOG.info("using path: " + this.pathContext);
		
        inputStream.close();
	}
	
	protected final String getHost() {
		return host;
	}

	protected final int getPort() {
		return port;
	}

	protected final String getPathContext() {
		return pathContext;
	}

}
