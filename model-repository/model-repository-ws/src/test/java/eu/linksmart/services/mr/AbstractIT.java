package eu.linksmart.services.mr;

import java.text.MessageFormat;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

public abstract class AbstractIT extends TestCase {

    private static final Logger LOG = Logger.getLogger(AbstractIT.class);
    
    public final String URL = "http://localhost:9090/model-repository/modelrepo";
	
    @Override
    protected void setUp() {
        if(LOG.isDebugEnabled()) {
        	LOG.debug("================================================================================");
        	LOG.debug("Entering integration test: " + this.getName());
        	LOG.debug("--------------------------------------------------------------------------------");
        }
        
        LOG.info( MessageFormat.format( "model repository URL at {0}", new Object[] { this.URL } ) );
    }

    @Override
    protected void tearDown() {
        if(LOG.isDebugEnabled()) {
        	LOG.debug("--------------------------------------------------------------------------------");
        	LOG.debug("Leaving integration test: " + this.getName());
        	LOG.debug("================================================================================");
        }
    }

}
