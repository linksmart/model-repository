package eu.linksmart.services.mr;

import java.text.MessageFormat;

import junit.framework.TestCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractIT extends TestCase {

    final Logger LOG = LoggerFactory.getLogger(AbstractIT.class);
    
    public final String URL = "http://localhost:9090/model-repository/mr";
	
    @Override
    protected void setUp() {
    	LOG.info("================================================================================");
    	LOG.info("Entering integration test: " + this.getName());
    	LOG.info("--------------------------------------------------------------------------------");
        LOG.info( MessageFormat.format( "model repository URL at {0}", new Object[] { this.URL } ) );
    }

    @Override
    protected void tearDown() {
    	LOG.info("--------------------------------------------------------------------------------");
    	LOG.info("Leaving integration test: " + this.getName());
    	LOG.info("================================================================================");
    }

}
