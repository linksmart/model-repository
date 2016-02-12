package eu.linksmart.services.mr.service;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.linksmart.services.mr.ModelRepository;
import eu.linksmart.services.mr.exceptions.ResourceInvalid;
import eu.linksmart.services.mr.exceptions.ResourceNotFound;
import eu.linksmart.services.mr.exceptions.ResourceTypeUnknown;

/**
 * @author hrasheed
 * 
 */
@Path("/mr/json/{modelIdentifier}")
public class DomainModel {
	
	private final Logger LOG = LoggerFactory.getLogger(DomainModel.class);
	
	protected static final String PATH = "mr/json/";
	
	public DomainModel() {
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)           
	@Produces(MediaType.TEXT_PLAIN)
	public Response addJsonDoc(@PathParam("modelIdentifier") String modelIdentifier, String jsonModelDoc, @Context UriInfo uriInfo) {
  	
		if(jsonModelDoc == null)
			return Response.status(Response.Status.BAD_REQUEST).entity("jsonModelDoc is null").build();
		
		if(jsonModelDoc.length() == 0)
			return Response.status(Response.Status.NO_CONTENT).entity("jsonModelDoc is empty").build();
		
		try {
			modelIdentifier = ModelRepository.getInstance().addJsonModel(modelIdentifier, jsonModelDoc);
			URI modelUri = uriInfo.getBaseUriBuilder().path(PATH + modelIdentifier).build();
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
	
	@GET
    @Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("modelIdentifier") String modelIdentifier) {
		try {
			String modelJson = ModelRepository.getInstance().getJsonModel(modelIdentifier);
			return Response.status(Response.Status.OK).entity(modelJson).build();
		} catch (ResourceNotFound e) {
			LOG.error(e.getMessage(), e);
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}	
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response update(@PathParam("modelIdentifier") String modelIdentifier, String jsonModelDoc, @Context UriInfo uriInfo) {
		try {
			ModelRepository.getInstance().updateJsonModel(modelIdentifier, jsonModelDoc);
			URI modelUri = uriInfo.getAbsolutePathBuilder().build();
			return Response.status(Response.Status.OK).entity(modelUri.toString()).build();
		} catch (ResourceNotFound e) {
			LOG.error(e.getMessage(), e);
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
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
		
	@DELETE
	@Consumes( MediaType.TEXT_PLAIN )
	@Produces( MediaType.TEXT_PLAIN )
	public Response delete(@PathParam("modelIdentifier") String modelIdentifier) {
		try {
			boolean status = ModelRepository.getInstance().deleteJsonModel(modelIdentifier);
			return Response.status(Response.Status.OK).entity(Boolean.toString(status)).build();
		} catch (ResourceNotFound e) {
			LOG.error(e.getMessage(), e);
			return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}
}
