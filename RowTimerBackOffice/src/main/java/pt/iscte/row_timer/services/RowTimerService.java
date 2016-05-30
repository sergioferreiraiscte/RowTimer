package pt.iscte.row_timer.services;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
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

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import pt.iscte.row_timer.beans.DBService;
import pt.iscte.row_timer.beans.RowTimerException;
import pt.iscte.row_timer.events.Login;
import pt.iscte.row_timer.events.Result;
import pt.iscte.row_timer.events.RowingEvent;
import pt.iscte.row_timer.events.StartRace;

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
	public Response event(@PathParam("id") String eventId, @QueryParam("pulled") Long lastPullDate) {
		if ( logger.isDebugEnabled()) {
			if ( lastPullDate != null )
				logger.debug("/api/event/id - Get event by ID " + eventId + " and filter (pulled date:" + new Date(lastPullDate) + ") called " );
			else
				logger.debug("/api/event/id - Get event by ID " + eventId + " called " );
		}

		RowingEvent rowingEvent = null;
		try {
			String filter = null;
			if ( lastPullDate != null ) { 
				SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss.S");
				String strDate = sdf.format(new Date(lastPullDate)); 
				filter = " change_moment > '" + strDate + "'";
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
	 * Store the start times related top an event. This will be executed with the information of 
	 * the start referee.
	 * 
	 * @return
	 */
	@POST
	@Path("/event/{id}/start/times")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response storeStartTimes(@PathParam("id") String eventId, List<StartRace> startTimes) {
		if ( logger.isDebugEnabled()) {
			logger.debug("/api/event/id - Get event by ID called");
		}

		RowingEvent rowingEvent = null;
		try {
			dbService.updateStartTimes(eventId,startTimes);
		} catch (Exception e) {
			logger.error("Error saving start times : ",e);
			ResponseBuilder response = Response.serverError();
			return response.build();
		}

		ResponseBuilder response = Response.ok((Object) rowingEvent );
		return response.build();
	}

	/**
	 * Store the results related to an event. This will be executed with the information of 
	 * the finish referee.
	 * 	
	 * @return
	 */
	@POST
	@Path("/event/{id}/arrival/times")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response storeResults(@PathParam("id") String eventId, List<Result> results) {
		if ( logger.isDebugEnabled()) {
			logger.debug("/api/event/id/finish/times - Get event by ID called");
		}

		RowingEvent rowingEvent = null;
		try {
			dbService.updateResults(eventId,results);
		} catch (Exception e) {
			logger.error("Error saving start times : ",e);
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
	@Consumes(MediaType.APPLICATION_OCTET_STREAM)
	public Response storeArrivalMovie(@PathParam("id") String eventId, InputStream is ) {
		if ( logger.isDebugEnabled()) {
			logger.debug("/api/event/{id}/arrival/movie - Store the arrival movie related to a race");
		}

		FileOutputStream out = null;
		try {
			out = new FileOutputStream("/tmp/xpto");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			IOUtils.copy(is, out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ResponseBuilder response = Response.ok((Object) "OK" );
		return response.build();
	}

	/**
	 * Store the movie related to an race
	 * TODO : Implement it 
	 * @return
	 */
	@POST
	@Path("/login/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response login(@PathParam("username") String username, String encriptedPassword) {
		if ( logger.isDebugEnabled()) {
			logger.debug("/api/login/{username} - Login with username and encripted password : " + username + "/" + encriptedPassword);
		}

		Login login = new Login();
		login.setUsername(username);
		login.setPassword(encriptedPassword);
		
		try {
			//login = dbService.login(login);
		} catch (Exception e) {
			logger.error("Error logging in : ",e);
			ResponseBuilder response = Response.serverError();
			return response.build();
		}
		
		ResponseBuilder response = Response.ok((Object) login );
		return response.build();
	}

	
}
