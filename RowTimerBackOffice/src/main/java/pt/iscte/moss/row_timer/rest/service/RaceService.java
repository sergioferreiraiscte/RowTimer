package pt.iscte.moss.row_timer.rest.service;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import pt.iscte.moss.row_timer.dao.Event;
import pt.iscte.moss.row_timer.dao.EventDAO;

/**
 * Services to interact with Race information.
 * A Race is one serie where boats do the race
 * 
 * @author Sergio Ferreira
 */

@Path("/")
@Service
@Scope("prototype")
@Produces(MediaType.APPLICATION_JSON)
public class RaceService {
	@Autowired
	private EventDAO eventDAO;
	
	/**
	 * Get all races available
	 */
	@Path("/race")
	@GET	
	@Produces({ MediaType.APPLICATION_JSON})
	public Response getRaces() {
		ResponseBuilder response = Response.ok((Object) "Hello world" );
		return response.build();
	}
	
	/**
	 * Get all events available
	 */
	@Path("/event")
	@GET	
	@Produces({ MediaType.APPLICATION_JSON})
	public Response getEvents() {
//		List<Event> le = eventDAO.getEvents(null);
		eventDAO.createEvent();
		ResponseBuilder response = Response.ok("OK");
		return response.build();
	}
}
