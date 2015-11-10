package eu.linksmart.services.mr;

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

import org.apache.log4j.Logger;

/**
 * @author hrasheed
 * 
 */
@Path("/modelrepo/xmi/{modelIdentifier}")
public class XmiDomainModel {
	
	private static final Logger LOG = Logger.getLogger(XmiDomainModel.class);
	
	public XmiDomainModel() {
	}
	
	@POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.TEXT_PLAIN)
    public Response addXmiModel(@PathParam("modelIdentifier") String modelIdentifier, String xmiModelDoc, @Context UriInfo uriInfo) {
		
		if(xmiModelDoc == null)
			return Response.status(Response.Status.BAD_REQUEST).entity("xmiModelDoc is null").build();
		
		if(xmiModelDoc.length() == 0)
			return Response.status(Response.Status.NO_CONTENT).entity("xmiModelDoc is empty").build();
		
		try {
			ModelRepository.getInstance().addXmiModel(modelIdentifier, xmiModelDoc);
			URI modelUri = uriInfo.getAbsolutePathBuilder().build();
			return Response.status(Response.Status.OK).entity(modelUri.toString()).build();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
    }
	
	@GET
    @Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_XML)
	public Response get(@PathParam("modelIdentifier") String modelIdentifier) {
		try {
			String modelXmi = ModelRepository.getInstance().getXmiModel(modelIdentifier);
			return Response.status(Response.Status.OK).entity(modelXmi).build();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}	
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.TEXT_PLAIN)
	public Response update(@PathParam("modelIdentifier") String modelIdentifier, String xmiModelDoc, @Context UriInfo uriInfo) {
		try {
			ModelRepository.getInstance().updateXmiModel(modelIdentifier, xmiModelDoc);
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
			boolean status = ModelRepository.getInstance().deleteXmiModel(modelIdentifier);
			return Response.status(Response.Status.OK).entity(Boolean.toString(status)).build();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}
}