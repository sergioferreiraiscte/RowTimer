package pt.iscte.row_timer.services;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import pt.iscte.row_timer.beans.DBService;
import pt.iscte.row_timer.events.RowingEvent;

/**
 * REST Services to get information about events
 * 
 * @author Sergio Ferreira
 *
 */
@Path("/")
@Service
@Scope("prototype")
@Produces(MediaType.APPLICATION_JSON)
public class RowTimerService {
	static final Logger logger = Logger.getLogger(RowTimerService.class);
	
	@Context ServletContext context;
	@Autowired
	private DBService dbService;


	/**
	 * Get all devices available on the database
	 * @return
	 */
	@GET
	@Path("/event/{id}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response event(@PathParam("id") String eventId) {
		if ( logger.isDebugEnabled()) {
			logger.debug("/api/devices - Get event by ID called");
		}

		RowingEvent rowingEvent = null;
		try {
			rowingEvent = dbService.selectEvent(eventId);
		} catch (Exception e) {
			logger.error("Error getting event : ",e);
			ResponseBuilder response = Response.serverError();
			return response.build();
		}

		ResponseBuilder response = Response.ok((Object) rowingEvent );
		return response.build();
	}

}
