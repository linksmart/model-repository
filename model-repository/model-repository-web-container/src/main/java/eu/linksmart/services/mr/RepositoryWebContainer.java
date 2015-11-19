package eu.linksmart.services.mr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.sun.jersey.spi.container.servlet.ServletContainer;

public class RepositoryWebContainer extends Thread {
	
	private final Logger LOG = LoggerFactory.getLogger(RepositoryWebContainer.class);
	
	private MRApplication dm = null;
	
	private Server server = null;
	
	public RepositoryWebContainer(MRApplication dm) {
		this.dm = dm;
	}
	
	public void run() {
		
		try {
			
			// logging
			//System.setProperty("org.eclipse.jetty.util.log.class", "org.eclipse.jetty.util.log.Slf4jLog");
			org.eclipse.jetty.util.log.Log.setLog(new org.eclipse.jetty.util.log.Slf4jLog());
			
			server = new Server(this.dm.getPort());
			
			ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
	        context.setContextPath("/");
	        
	        server.setHandler(context);
	        
	        ServletHolder jerseyServlet = context.addServlet(ServletContainer.class, "/" + this.dm.getPathContext() + "/*");
	        jerseyServlet.setInitOrder(1);
	        jerseyServlet.setInitParameter("com.sun.jersey.config.property.packages","eu.linksmart.services.mr.service");

	        ServletHolder staticServlet = context.addServlet(DefaultServlet.class,"/*");
	        staticServlet.setInitParameter("resourceBase","webapp");
	        staticServlet.setInitParameter("pathInfoOnly","true");
	        
	        LOG.info("starting jetty web container");
	        
			server.start();
			server.join();
			
		} catch (Exception e) {
			LOG.error("unable to start the jetty web container." + e.getMessage());
			this.dm.shutdown();
		}
	}
	
	protected void stopContainer() {
		try {
			if(server != null) {
				server.stop();
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("unable to properly stop the jetty web container." + e.getMessage());
		}
		
	}
}
