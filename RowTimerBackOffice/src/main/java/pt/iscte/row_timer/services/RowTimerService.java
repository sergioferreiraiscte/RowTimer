package pt.iscte.row_timer.services;

import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import pt.iscte.row_timer.beans.DBService;
import pt.iscte.row_timer.beans.RowTimerException;
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
	 * Get information of all events
	 * @return
	 */
	@GET
	@Path("/event")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response events() {
		if ( logger.isDebugEnabled()) {
			logger.debug("/api/devices - Get all events called");
		}

		List<RowingEvent> rowingEvents = null;
		try {
			rowingEvents = dbService.selectAllEvents();
		} catch (Exception e) {
			logger.error("Error getting events : ",e);
			ResponseBuilder response = Response.serverError();
			return response.build();
		}

		ResponseBuilder response = Response.ok((Object) rowingEvents );
		return response.build();
	}


	/**
	 * Get information of a rowing event available on the database
	 * 
	 * @TODO : Implement the pulled mechanism
	 * 
	 * @param eventId The primary key of the event
	 * @param pulled Time of last pull, to know if it should send the info
	 * where change_moment > pulled
	 * @return
	 */
	@GET
	@Path("/event/{id}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response event(@PathParam("id") String eventId, @QueryParam("pulled") String pulled) {
		if ( logger.isDebugEnabled()) {
			logger.debug("/api/event/id - Get event by ID " + eventId + " and filter (pulled date:" + pulled + ") called " );
		}

		RowingEvent rowingEvent = null;
		try {
			String filter = null;
			if ( pulled != null ) { 
			   filter = " change_moment > '" + pulled + "'";
			}
			rowingEvent = dbService.selectEvent(eventId,filter);
		} catch (RowTimerException rte) {
			if ( rte.getCode() == 404 ) {
				return Response.status(Status.NOT_FOUND).build();
			}
			logger.error("Error getting event : ",rte);
			ResponseBuilder response = Response.serverError();
			return response.build();
		}
		ResponseBuilder response = Response.ok((Object) rowingEvent );
		return response.build();
	}

	/**
	 * Store the results related to an event
	 * TODO : Implement the logic
	 * @return
	 */
	@POST
	@Path("/event/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response storeEvent(@PathParam("id") String eventId) {
		if ( logger.isDebugEnabled()) {
			logger.debug("/api/event/id - Get event by ID called");
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
	
	/**
	 * Store the movie related to an race
	 * TODO : Implement it - Should create a library to store the movies in folders (could be in alfresco)
	 * @return
	 */
	@POST
	@Path("/event/{id}/{race}/arrival/movie")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response storeArrivalMovie(@PathParam("id") String eventId) {
		if ( logger.isDebugEnabled()) {
			logger.debug("/api/event/{id}/arrival/movie - Store the arrival movie related to a race");
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
