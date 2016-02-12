package eu.linksmart.services.mr.service;

import java.net.URI;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.core.util.MultivaluedMapImpl;

import eu.linksmart.services.mr.ModelRepository;
import eu.linksmart.services.mr.exceptions.RepositoryException;
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
@Path("/mr")
public class ModelRepositoryService {

    private final Logger LOG = LoggerFactory.getLogger(ModelRepositoryService.class);
    
    protected static final String XMI_PATH = "mr/xmi/";
    
    protected static final String JSON_PATH = "mr/json/";
    
    public ModelRepositoryService() {
        LOG.debug("initialized model repository service");
    }
    
    @POST
    @Path("{modelName}")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.TEXT_PLAIN)
    public Response addXmiModel(@PathParam("modelName") String modelName, String xmiModelDoc, @Context UriInfo uriInfo) {
		
		if(xmiModelDoc == null)
			return Response.status(Response.Status.BAD_REQUEST).entity("xmiModelDoc is null").build();
		
		if(xmiModelDoc.length() == 0)
			return Response.status(Response.Status.NO_CONTENT).entity("xmiModelDoc is empty").build();
		
		if(modelName == null)
			return Response.status(Response.Status.BAD_REQUEST).entity("modelName is null").build();
		
		String modelIdentifier = null;
		try {
			modelIdentifier = ModelRepository.getInstance().addXmiModel(modelName, xmiModelDoc);
			URI modelUri = uriInfo.getBaseUriBuilder().path(XMI_PATH + modelIdentifier).build();
			return Response.status(Response.Status.OK).entity(modelUri.toString()).build();
		} catch (RepositoryException e) {
			LOG.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
    }
    
    @GET
    @Path("{modelIdentifier}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getXmiModelJson(@PathParam("modelIdentifier") String modelIdentifier) {
    	if(modelIdentifier == null)
			return Response.status(Response.Status.BAD_REQUEST).entity("modelIdentifier is null").build();
    	if(modelIdentifier.length() == 0)
			return Response.status(Response.Status.NO_CONTENT).entity("modelIdentifier is empty").build();
    	try {
			String modelJson = ModelRepository.getInstance().getXmiModelJson(modelIdentifier);
			return Response.status(Response.Status.OK).entity(modelJson).build();
		} catch (ResourceNotFound e) {
			LOG.error(e.getMessage(), e);
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}	
    }
    
    @GET
    @Path("latest/xmi/{modelName}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_XML)
    public Response getLatestXmiModel(@PathParam("modelName") String modelName) {
    	try {
    		String modelXmi = ModelRepository.getInstance().getLatestXmi(modelName);
    		return Response.status(Response.Status.OK).entity(modelXmi).build();    
    	} catch (ResourceNotFound e) {
			LOG.error(e.getMessage(), e);
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}	
    }
    
    @GET
    @Path("latest/json/{modelName}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLatestJsonModel(@PathParam("modelName") String modelName) {
    	try {
    		String modelJson = ModelRepository.getInstance().getLatestXmiJson(modelName);
    		return Response.status(Response.Status.OK).entity(modelJson).build();    
    	} catch (ResourceNotFound e) {
			LOG.error(e.getMessage(), e);
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		} catch (Exception e) {
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
    		
    		List<String> identifiers = ModelRepository.getInstance().listXmi();
    	
    		MultivaluedMap<String, String> entryParams = new MultivaluedMapImpl();
    		
    		for (String identifier : identifiers) {
    			URI modelUri = uriInfo.getBaseUriBuilder().path(XMI_PATH + identifier).build();
                entryParams.add(identifier, modelUri.toString());
    		}
            
            return Response.status(Response.Status.OK).entity(entryParams).build();
            
    	} catch( Exception e) {
    		LOG.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
    	} 
    }
    
    @GET
    @Path("list/xmi/{modelName}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_FORM_URLENCODED)
    public Response listXmiModels(@PathParam("modelName") String modelName, @Context UriInfo uriInfo) {
    	
    	try {
    		
    		List<String> identifiers = ModelRepository.getInstance().listXmi(modelName);
    		
    		MultivaluedMap<String, String> entryParams = new MultivaluedMapImpl();
    		
    		for (String identifier : identifiers) {
    			URI modelUri = uriInfo.getBaseUriBuilder().path(XMI_PATH + identifier).build();
                entryParams.add(identifier, modelUri.toString());
    		}
    		
            return Response.status(Response.Status.OK).entity(entryParams).build();
            
    	} catch( Exception e) {
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
    		
    		List<String> identifiers = ModelRepository.getInstance().listJson();
    		
            MultivaluedMap<String, String> entryParams = new MultivaluedMapImpl();
            
            for (String identifier : identifiers) {
    			URI userUri = uriInfo.getBaseUriBuilder().path(JSON_PATH + identifier).build();
                entryParams.add(identifier, userUri.toString());
    		}
            
            return Response.status(Response.Status.OK).entity(entryParams).build();
            
    	} catch( Exception e) {
    		LOG.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
    	} 
    }
    
    @GET
    @Path("list/json/{modelName}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_FORM_URLENCODED)
    public Response listJsonModels(@PathParam("modelName") String modelName, @Context UriInfo uriInfo) {
    	
    	try {
    		
    		List<String> identifiers = ModelRepository.getInstance().listJson(modelName);
    		
            MultivaluedMap<String, String> entryParams = new MultivaluedMapImpl();
            
            for (String identifier : identifiers) {
    			URI userUri = uriInfo.getBaseUriBuilder().path(JSON_PATH + identifier).build();
                entryParams.add(identifier, userUri.toString());
    		}
            
            return Response.status(Response.Status.OK).entity(entryParams).build();
            
    	} catch( Exception e) {
    		LOG.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
    	} 
    }    
}
