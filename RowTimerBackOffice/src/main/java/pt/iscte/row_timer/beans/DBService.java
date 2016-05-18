package pt.iscte.row_timer.beans;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;

import pt.iscte.row_timer.events.Alignment;
import pt.iscte.row_timer.events.BoatType;
import pt.iscte.row_timer.events.Category;
import pt.iscte.row_timer.events.Race;
import pt.iscte.row_timer.events.RowingEvent;

/**
 * Database access to information about the devices and controllers
 * 
 * @author Sergio Ferreira
 *
 */
public class DBService {
	static final Logger logger = Logger.getLogger(DBService.class);

	BasicDataSource dataSource;

	public void setDataSource(BasicDataSource dataSource) {
		this.dataSource = dataSource;
	}


	public List<RowingEvent> selectEvents(String filter) {
		List<RowingEvent> events = new ArrayList<RowingEvent>();
		return events;
	}

	/**
	 * Select the controlled devices from the mySQL database, instantiate the
	 * specific object and return the list of them.
	 * 
	 * @param where String to add to where clause in SELECT 
	 * @return A list of devices
	 */
	public RowingEvent selectEvent(String eventId) throws RowTimerException {
		if ( logger.isDebugEnabled()) {
			logger.debug("selectEvent() called from event with ID " + eventId);
		}

		Connection c = null;
		PreparedStatement s = null;
		RowingEvent rowingEvent = new RowingEvent();
		rowingEvent.setId("CNV");
		rowingEvent.setName("Campeonato Nacional de Velocidade");


		/*
		try {
			c = dataSource.getConnection();

			StringBuffer strSql = new StringBuffer("select * from controlable_device ");
			if ( where != null ) {
				strSql.append(" WHERE ");
				strSql.append(where);
			}
			strSql.append(" order by dev_order");
			s = c.prepareStatement(strSql.toString());

			ResultSet rs = s.executeQuery();
			while (rs.next() ) {
				String deviceType = rs.getString("device_type");
				ControledDevice d = ControledDeviceFactory.getControledDevice(deviceType);		
				d.setId(rs.getString("id"));
				d.setName(rs.getString("name"));
				d.setControlerID(rs.getString("controler_id"));
				d.setDescription(rs.getString("description"));
				d.setDeviceType(deviceType);
				d.setJSONConfigurations(rs.getString("configuration"));			
				devices.add(d);
			}
			rs.close();
		} catch (Exception e) {
			logger.error("Error selecting devices",e);
			throw new LightsException(e);
		} finally {

			try {
				s.close();
				c.close();
			} catch (SQLException sqle) {
				logger.error("Error closing SQL statement and connection ",sqle);
			}
		}*/
		return rowingEvent;

	}

	private List<Race> selectRacesOfEvent(String eventId) {
		List<Race> races = new ArrayList<Race>();
		return races;
	}

	private BoatType selectBoatType(String boatTypeId) {

	}

	private Category selectCategory(String categoryId) {

	}

	private List<Alignment> selectCrewAlignment(String eventId,Integer raceNo) {

	}

	private Crew selectCrew(String crewId) {

	}
	
	private List<CrewMember> selectCrewMembers(String crewId) {
		
	}
	
	private Competitor selectCompetitor(competitorId) {
		
	}
	
	private Person selectPerson(String personId) {
		
	}

}
