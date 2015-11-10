package eu.linksmart.services.mr;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;

/**
 * @author hrasheed
 * 
 */
@Path("/modelrepo/json/{modelIdentifier}")
public class DomainModel {
	
	private static final Logger LOG = Logger.getLogger(DomainModel.class);
	
	public DomainModel() {
	}
	
	@GET
    @Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@PathParam("modelIdentifier") String modelIdentifier) {
		try {
			String modelJson = ModelRepository.getInstance().getModel(modelIdentifier);
			return Response.status(Response.Status.OK).entity(modelJson).build();
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
			ModelRepository.getInstance().updateModel(modelIdentifier, jsonModelDoc);
			URI modelUri = uriInfo.getAbsolutePathBuilder().build();
			return Response.status(Response.Status.OK).entity(modelUri.toString()).build();
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
			boolean status = ModelRepository.getInstance().deleteModel(modelIdentifier);
			return Response.status(Response.Status.OK).entity(Boolean.toString(status)).build();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}
}