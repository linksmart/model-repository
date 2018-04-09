package eu.linksmart.services.mr;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

//import eu.linksmart.lc;
//import eu.linksmart.lc.sc.client;

public class MRApplication {

	private final Logger LOG = LoggerFactory.getLogger(MRApplication.class);

	private String host;

	private int port;

	private String pathContext = "model-repository";

	private int ttl;

	private final String PROPERTY_FILE = "/model-repository.properties";

	private RepositoryWebContainer container = null;

	private String SERVICE_FILE = "/service.json";

	private String SC_BASE_URL = "http://localhost:8082/sc";

	private String serviceID = "example-MR";

	private boolean scRegistered = false;

	private boolean deregisterOnExit = true;

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


		try {
			String jsonService = new String(Files.readAllBytes(Paths.get(getClass().getResource(SERVICE_FILE).toURI())), Charset.defaultCharset());

			if (SC_BASE_URL != null && !SC_BASE_URL.isEmpty()) {
				Thread.sleep(1000);
				scRegistered = createRegistrationInSC(jsonService);
			}
		} catch (Exception e) {
			LOG.error("unable to read service defenition file [" + SERVICE_FILE + "] " + e.getMessage());
			System.exit(1);
		}


		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				shutdown();
			}
		});

		if (ttl > 0) {
			new Thread() {
				public void run() {
					LOG.info("model repository wait on TTL: " + ttl);
					try { Thread.sleep(ttl);  } catch (InterruptedException e) { }
					shutdown();
					LOG.info("model repository exiting on TTL: " + ttl);
				}
			}.start();
		}

/*		try {
			if (ttl > 0) {
				Thread.sleep(ttl);
				LOG.info("model repository exiting on ttl: " + ttl);
				shutdown();
				}
			else
				Runtime.getRuntime().addShutdownHook(new Thread() {
				public void run() {
					shutdown();
				}
			});

		} catch (Exception ie) {
			LOG.error("exception by Thread.sleep(ttl) :" + ie.getMessage());
			System.exit(1);
		}
*/
	}

	protected void shutdown() {
		LOG.info("------------------------------------------------------------------------------------");
		LOG.info("shutting down the model repository");
		LOG.info("------------------------------------------------------------------------------------");

		if (scRegistered && deregisterOnExit) deleteRegistrationInSC(serviceID);
		//ModelRepository.getInstance().closePersistence();
		container.stopContainer();
		container.interrupt();
		try { Thread.sleep(1000); } catch (InterruptedException e) { }
		LOG.info("model repository server terminated");
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
		this.ttl = Integer.parseInt(prop.getProperty("model.repository.ttl"));

		this.serviceID = prop.getProperty("model.repository.serviceID");
		this.SERVICE_FILE = prop.getProperty("model.repository.scFile");
		this.SC_BASE_URL = prop.getProperty("model.repository.scURL");
		this.deregisterOnExit = Boolean.parseBoolean(prop.getProperty("model.repository.deregisterOnExit"));

		LOG.info("using host: " + this.host);
		LOG.info("using port: " + this.port);
		LOG.info("using path: " + this.pathContext);
		LOG.info("using ttl: " + this.ttl);
		LOG.info("using serviceID: " + this.serviceID);
		LOG.info("using scFile: " + this.SERVICE_FILE);
		LOG.info("using scURL: " + this.SC_BASE_URL);
		LOG.info("using deregisterOnExit: " + this.deregisterOnExit);

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

	public boolean createRegistrationInSC(String serviceJson) {
		LOG.info("------------------------------------------------------------------------------------");
		LOG.info("registering in service catalog");
		LOG.info("------------------------------------------------------------------------------------");
		Client client = Client.create();
		try {

			String interface_option = "/" + serviceID;
			String scAddress = SC_BASE_URL + interface_option;

			WebResource webResourceClient = client.resource(scAddress);

			ClientResponse response = webResourceClient.type(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).put(ClientResponse.class, serviceJson);

			boolean success = (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL);

			if (success) {
				return success;
			} else {
				String error = response.getStatusInfo().getReasonPhrase();
				throw new Exception("failed to add a service : status code: " + response.getStatusInfo().getStatusCode() + " - reason: " + error);
			}

		} catch (Exception e) {
			System.err.println("service catalog error: reason: " + e.getMessage());
			return false;
		}
	}

	public boolean deleteRegistrationInSC(String serviceID) {
		LOG.info("------------------------------------------------------------------------------------");
		LOG.info("deleting from service catalog");
		LOG.info("------------------------------------------------------------------------------------");
		Client client = Client.create();
		try {

			String interface_option = "/" + serviceID;
			String scAddress = SC_BASE_URL + interface_option;
			LOG.info("dele address: " + scAddress);

			WebResource webResourceClient = client.resource(scAddress);

			ClientResponse response = webResourceClient.accept(MediaType.TEXT_PLAIN).delete(ClientResponse.class);

			boolean success = (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL);

			if (success) {
				return success;
			} else {
				String error = response.getStatusInfo().getReasonPhrase();
				throw new Exception("failed to remove a service : status code: " + response.getStatusInfo().getStatusCode() + " - reason: " + error);
			}

		} catch (Exception e) {
			System.err.println("service catalog error: reason: " + e.getMessage());
			return false;
		}
	}
}
