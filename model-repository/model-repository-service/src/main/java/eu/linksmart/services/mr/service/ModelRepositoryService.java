package eu.linksmart.services.mr.service;

import java.net.URI;
import java.util.List;

import org.apache.log4j.Logger;

import com.sun.jersey.core.util.MultivaluedMapImpl;

import eu.linksmart.services.mr.ModelRepository;
import eu.linksmart.services.mr.exceptions.ResourceInvalid;
import eu.linksmart.services.mr.exceptions.ResourceNotFound;
import eu.linksmart.services.mr.exceptions.ResourceTypeUnknown;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author hrasheed
 * 
 */
@XmlRootElement
@Path("/modelrepo")
public class ModelRepositoryService {

    private static final Logger LOG = Logger.getLogger(ModelRepositoryService.class);
    
    public ModelRepositoryService() {
        LOG.info("initialized model repository service");
    }
    
    @POST
	@Consumes(MediaType.APPLICATION_JSON)           
	@Produces(MediaType.TEXT_PLAIN)
	public Response add(String jsonModelDoc, @Context UriInfo uriInfo) {
    	
		if(jsonModelDoc == null)
			return Response.status(Response.Status.BAD_REQUEST).entity("jsonModelDoc is null").build();
		
		if(jsonModelDoc.length() == 0)
			return Response.status(Response.Status.NO_CONTENT).entity("jsonModelDoc is empty").build();
		
		String identifier = null;
		try {
			identifier = ModelRepository.getInstance().addModel(jsonModelDoc);
			URI modelUri = uriInfo.getAbsolutePathBuilder().path("json/" + identifier).build();
			return Response.status(Response.Status.OK).entity(modelUri.toString()).build();
		} catch (ResourceTypeUnknown e) {
			LOG.error(e.getMessage(), e);
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		} catch (ResourceInvalid e) {
			LOG.error(e.getMessage(), e);
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}
    
    @PUT
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	public Response generateJsonModel(String modelIdentifier) {
    	
		if(modelIdentifier == null)
			return Response.status(Response.Status.BAD_REQUEST).entity("modelIdentifier is null").build();
		
		if(modelIdentifier.length() == 0)
			return Response.status(Response.Status.NO_CONTENT).entity("modelIdentifier is empty").build();
		
		try {
			String jsonModelDoc = ModelRepository.getInstance().generateJsonModel(modelIdentifier);
			return Response.status(Response.Status.OK).entity(jsonModelDoc).build();
		} catch (ResourceNotFound e) {
			LOG.error(e.getMessage(), e);
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}
    
    @GET
    @Path("list/json")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_FORM_URLENCODED)
    public Response listJsonModels(@Context UriInfo uriInfo) {
    	
    	try {
    		
    		List<String> entries = ModelRepository.getInstance().listJson();
    		
            MultivaluedMap<String, String> entryParams = new MultivaluedMapImpl();
            
            for (String entry : entries) {
    			URI userUri = uriInfo.getAbsolutePathBuilder().path(entry).build();
                entryParams.add("json", userUri.toString());
    		}
             
            return Response.status(Response.Status.OK).entity(entryParams).build();
            
    	} catch( Exception e) {
    		LOG.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
    	} 
    }
    
    @GET
    @Path("list/xmi")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_FORM_URLENCODED)
    public Response listXmiModels(@Context UriInfo uriInfo) {
    	
    	try {
    		
    		List<String> entries = ModelRepository.getInstance().listXmi();
    		
    		MultivaluedMap<String, String> entryParams = new MultivaluedMapImpl();
    		
    		for (String entry : entries) {
    			URI userUri = uriInfo.getAbsolutePathBuilder().path(entry).build();
                entryParams.add("xmi", userUri.toString());
    		}
            
            return Response.status(Response.Status.OK).entity(entryParams).build();
            
    	} catch( Exception e) {
    		LOG.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
    	} 
    }
}
