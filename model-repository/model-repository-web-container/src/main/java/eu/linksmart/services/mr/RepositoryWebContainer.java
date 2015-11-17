package eu.linksmart.services.mr;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.sun.jersey.spi.container.servlet.ServletContainer;

public class RepositoryWebContainer {
	
	private static Logger LOG = Logger.getLogger(RepositoryWebContainer.class.getName());
	
	private Server server = null;
	
	private String host;
	private int port;
	private String pathContext = "/model-repository/*";
	
	public RepositoryWebContainer() {
	}
	
	public RepositoryWebContainer(String host, int port, String pathContext) {
		this.host = host;
		this.port = port;
		this.pathContext = "/" + pathContext + "/*";
	}
	
	protected void start() throws Exception {
		
		server = new Server(this.port);
		
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        context.setContextPath("/");
        
        server.setHandler(context);
        
        ServletHolder jerseyServlet = context.addServlet(ServletContainer.class, this.pathContext);
        jerseyServlet.setInitOrder(1);
        jerseyServlet.setInitParameter("com.sun.jersey.config.property.packages","eu.linksmart.services.mr.service");

        ServletHolder staticServlet = context.addServlet(DefaultServlet.class,"/*");
        staticServlet.setInitParameter("resourceBase","webapp");
        staticServlet.setInitParameter("pathInfoOnly","true");
		
		server.start();
		server.join();
	}
	
	protected void stop() throws Exception {
		server.destroy();
	}
}
