package pt.iscte.row_timer.beans;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;

import pt.iscte.row_timer.events.Alignment;
import pt.iscte.row_timer.events.BoatType;
import pt.iscte.row_timer.events.Category;
import pt.iscte.row_timer.events.Competitor;
import pt.iscte.row_timer.events.Crew;
import pt.iscte.row_timer.events.CrewMember;
import pt.iscte.row_timer.events.Person;
import pt.iscte.row_timer.events.Race;
import pt.iscte.row_timer.events.Result;
import pt.iscte.row_timer.events.RowingEvent;
import pt.iscte.row_timer.events.StartRace;

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

	public List<RowingEvent> selectAllEvents() throws RowTimerException {
		return selectEvents(null);
	}

	/**
	 * Select the rowing events
	 * @param filter Where clause to add to the query
	 * @return
	 * @throws RowTimerException 
	 */
	public List<RowingEvent> selectEvents(String filter) throws RowTimerException {
		List<RowingEvent> events = new ArrayList<RowingEvent>();
		if ( logger.isDebugEnabled()) {
			logger.debug("selectEvents() called with filter: " + filter);
		}

		Connection c = null;
		PreparedStatement s = null;

		try {
			c = dataSource.getConnection();

			StringBuffer strSql = new StringBuffer("select * from rowing_event ");
			if ( filter != null ) {
				strSql.append(" WHERE ");
				strSql.append(filter);
			}
			strSql.append(" order by event_date");
			s = c.prepareStatement(strSql.toString());

			ResultSet rs = s.executeQuery();
			while (rs.next() ) {
				RowingEvent re = new RowingEvent();
				re.setId(rs.getString("id"));
				re.setName(rs.getString("name"));
				re.setEventDate(new Date(rs.getTimestamp("event_date").getTime()));
				re.setChangeMoment(new Date(rs.getTimestamp("change_moment").getTime()));
				re.setLocation(rs.getString("location"));
				events.add(re);
			}
			rs.close();
		} catch (Exception e) {
			logger.error("Error selecting devices",e);
			throw new RowTimerException(e);
		} finally {

			try {
				s.close();
				c.close();
			} catch (SQLException sqle) {
				logger.error("Error closing SQL statement and connection ",sqle);
			}
		}
		return events;
	}

	public RowingEvent selectEvent(String eventId) throws RowTimerException {
		return selectEvent(eventId, null);
	}

	/**
	 * Select the controlled devices from the mySQL database, instantiate the
	 * specific object and return the list of them.
	 * 
	 * @param where String to add to where clause in SELECT 
	 * @return A list of devices
	 */
	public RowingEvent selectEvent(String eventId,String filter) throws RowTimerException {
		if ( logger.isDebugEnabled()) {
			logger.debug("selectEvent() called with ID " + eventId + " and filter : " + filter);
		}

		Connection c = null;
		PreparedStatement s = null;
		RowingEvent re = new RowingEvent();

		try {
			c = dataSource.getConnection();

			StringBuffer strSql = new StringBuffer("select * from rowing_event ");
			strSql.append(" WHERE id='" + eventId + "'");
			if ( filter != null ) {
				strSql.append(" and ");
				strSql.append(filter);
			}
			if ( logger.isDebugEnabled()) {
				logger.debug("Query " + strSql.toString());
			}
			s = c.prepareStatement(strSql.toString());

			ResultSet rs = s.executeQuery();
			if ( rs.next() ) {
				re.setId(rs.getString("id"));
				re.setName(rs.getString("name"));
				re.setEventDate(new Date(rs.getTimestamp("event_date").getTime()));
				re.setChangeMoment((new Date(rs.getTimestamp("change_moment").getTime())));
				re.setLocation(rs.getString("location"));
				re.setEventRaces(selectRacesOfEvent(eventId));

			} else {
				logger.warn("Event with ID=" + eventId + " does not exist on the database ");
				throw new RowTimerException("Event with ID=" + eventId + " does not exist on the database ",404);
			}
			rs.close();
		} catch (RowTimerException rte) {
			throw rte; 
		} catch (Exception e) {
			logger.error("Error selecting rowing_event",e);
			throw new RowTimerException(e);
		} finally {

			try {
				s.close();
				c.close();
			} catch (SQLException sqle) {
				logger.error("Error closing SQL statement and connection ",sqle);
			}
		}
		return re;
	}


	/** 
	 * Select the races of an event
	 * @param eventId
	 * @return
	 * @throws RowTimerException 
	 */
	private List<Race> selectRacesOfEvent(String eventId) throws RowTimerException {
		List<Race> races = new ArrayList<Race>();
		String filter = "event_id='" + eventId + "'";
		if ( logger.isDebugEnabled()) {
			logger.debug("selectRacesOfEvent() called with filter: " + filter);
		}

		Connection c = null;
		PreparedStatement s = null;

		try {
			c = dataSource.getConnection();

			StringBuffer strSql = new StringBuffer("select * from race ");
			if ( filter != null ) {
				strSql.append(" WHERE ");
				strSql.append(filter);
			}
			strSql.append(" order by seqno");
			s = c.prepareStatement(strSql.toString());

			ResultSet rs = s.executeQuery();
			while (rs.next() ) {
				Race race = new Race();

				race.setEventId(rs.getString("event_id"));
				race.setSeqno(rs.getInt("seqno"));
				race.setHour(new Date(rs.getTimestamp("hour").getTime()));
				race.setBoatType(selectBoatType(rs.getString("boat_type")));
				race.setCategory(selectCategory(rs.getString("category")));
				race.setCrewAlignment(selectCrewAlignment(race.getEventId(),race.getSeqno()));
				races.add(race);
			}
			rs.close();
		} catch (Exception e) {
			logger.error("Error selecting races",e);
			throw new RowTimerException(e);
		} finally {

			try {
				s.close();
				c.close();
			} catch (SQLException sqle) {
				logger.error("Error closing SQL statement and connection ",sqle);
			}
		}
		return races;
	}

	/**
	 * Update start times registered by start referee
	 * 
	 * @param eventId
	 * @param startTimes
	 * @throws RowTimerException 
	 */
	public void updateStartTimes(String eventId,List<StartRace> startTimes) throws RowTimerException {
		if ( logger.isDebugEnabled()) {
			logger.debug("updateStartTimes() called ");
		}

		Connection c = null;
		PreparedStatement s = null;

		try {
			c = dataSource.getConnection();

			String updateTableSQL = "UPDATE race SET start_time = ? WHERE event_id = ? AND seqno = ?";
			if ( logger.isDebugEnabled()) {
				logger.debug("Update SQL : " + updateTableSQL);
			}
			s = c.prepareStatement(updateTableSQL);

			for (StartRace startRaceTime : startTimes) {
				s.setTimestamp(1, new Timestamp(startRaceTime.getStartMoment().getTime()));
				s.setString(2, eventId);
				s.setInt(3, startRaceTime.getRaceno());
				s.execute();
			}
		} catch (SQLException e) {
			logger.error("Error updating start times of a race",e);
			throw new RowTimerException(e);
		} finally {
			if (s != null) {
				try {
					s.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (c != null) {
				try {
					c.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Update the results of races that already finished
	 * 
	 * @param eventId
	 * @param results
	 * @throws RowTimerException 
	 */
	public void updateResults(String eventId,List<Result> results) throws RowTimerException {
		if ( logger.isDebugEnabled()) {
			logger.debug("updateStartTimes() called ");
		}

		Connection c = null;
		PreparedStatement s = null;

		try {
			c = dataSource.getConnection();

			String updateTableSQL = "UPDATE alignment SET end_time = ? " +
			  " WHERE event_id = ? AND race_no = ? AND lane = ?";
			if ( logger.isDebugEnabled()) {
				logger.debug("Update SQL : " + updateTableSQL);
			}
			s = c.prepareStatement(updateTableSQL);

			for (Result result : results) {
				s.setDate(1, new java.sql.Date(result.getFinishTime().getTime()));
				s.setString(2, eventId);
				s.setInt(3, result.getRaceno());
				s.setInt(4, result.getLane());
				s.execute();
			}
		} catch (SQLException e) {
			logger.error("Error updating arrival times of a race",e);
			throw new RowTimerException(e);
		} finally {
			if (s != null) {
				try {
					s.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (c != null) {
				try {
					c.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}


	private BoatType selectBoatType(String boatTypeId) throws RowTimerException {
		if ( logger.isDebugEnabled()) {
			logger.debug("selectBoatType() called with ID " + boatTypeId);
		}

		if ( boatTypeId == null || "".equals(boatTypeId.trim()) )
			return null;

		Connection c = null;
		PreparedStatement s = null;
		BoatType boatType = new BoatType();

		try {
			c = dataSource.getConnection();

			StringBuffer strSql = new StringBuffer("select * from boat_type ");
			strSql.append(" where boat_id='" + boatTypeId + "'");
			s = c.prepareStatement(strSql.toString());

			ResultSet rs = s.executeQuery();
			if ( rs.next() ) {
				boatType.setBoatId(rs.getString("boat_id"));
				boatType.setName(rs.getString("name"));

			} else {
				logger.warn("Boat Type with ID=" + boatTypeId + " does not exist on the database ");
				throw new RowTimerException("Crew with ID=" + boatTypeId + " does not exist on the database ");
			}
			rs.close();
		} catch (RowTimerException rte) {
			throw rte; 
		} catch (Exception e) {
			logger.error("Error selecting boat type",e);
			throw new RowTimerException(e);
		} finally {

			try {
				s.close();
				c.close();
			} catch (SQLException sqle) {
				logger.error("Error closing SQL statement and connection ",sqle);
			}
		}
		return boatType;
	}


	private Category selectCategory(String categoryId) throws RowTimerException {
		if ( logger.isDebugEnabled()) {
			logger.debug("selectCategory() called with ID " + categoryId);
		}

		if ( categoryId == null || "".equals(categoryId.trim()))
			return null;

		Connection c = null;
		PreparedStatement s = null;
		Category category = new Category();

		try {
			c = dataSource.getConnection();

			StringBuffer strSql = new StringBuffer("select * from category ");
			strSql.append(" where id='" + categoryId + "'");
			s = c.prepareStatement(strSql.toString());

			ResultSet rs = s.executeQuery();
			if ( rs.next() ) {
				category.setId(rs.getString("id"));
				category.setName(rs.getString("name"));
				category.setGender(rs.getString("gender"));

			} else {
				logger.warn("Boat Type with ID=" + categoryId + " does not exist on the database ");
				throw new RowTimerException("Crew with ID=" + categoryId + " does not exist on the database ");
			}
			rs.close();
		} catch (RowTimerException rte) {
			throw rte; 
		} catch (Exception e) {
			logger.error("Error selecting category",e);
			throw new RowTimerException(e);
		} finally {

			try {
				s.close();
				c.close();
			} catch (SQLException sqle) {
				logger.error("Error closing SQL statement and connection ",sqle);
			}
		}
		return category;
	}


	/**
	 * Select the crews that are aligned in a rec
	 * @param eventId
	 * @param raceNo
	 * @return
	 * @throws RowTimerException 
	 */
	private List<Alignment> selectCrewAlignment(String eventId,Integer raceNo) throws RowTimerException {
		List<Alignment> alignment = new ArrayList<Alignment>();
		String filter = "event_id='" + eventId + "' and race_no=" + raceNo;
		if ( logger.isDebugEnabled()) {
			logger.debug("selectRacesOfEvent() called with filter: " + filter);
		}

		Connection c = null;
		PreparedStatement s = null;

		try {
			c = dataSource.getConnection();

			StringBuffer strSql = new StringBuffer("select * from alignment ");
			if ( filter != null ) {
				strSql.append(" WHERE ");
				strSql.append(filter);
			}
			strSql.append(" order by lane");
			if ( logger.isDebugEnabled()) {
				logger.debug("Query: " + strSql.toString());
			}

			s = c.prepareStatement(strSql.toString());

			ResultSet rs = s.executeQuery();
			while (rs.next() ) {
				Alignment alignedCrew = new Alignment ();

				alignedCrew.setEventId(rs.getString("event_id"));
				alignedCrew.setRaceNo(rs.getInt("race_no"));
				alignedCrew.setLane(rs.getInt("lane"));
				alignedCrew.setCrew(selectCrew(rs.getString("crew")));
				alignment.add(alignedCrew);
			}
			rs.close();
		} catch (Exception e) {
			logger.error("Error selecting alignment",e);
			throw new RowTimerException(e);
		} finally {

			try {
				s.close();
				c.close();
			} catch (SQLException sqle) {
				logger.error("Error closing SQL statement and connection ",sqle);
			}
		}
		return alignment;
	}

	/**
	 * Select the crew by id 
	 * 
	 * @param crewId
	 * @return
	 * @throws RowTimerException 
	 */
	private Crew selectCrew(String crewId) throws RowTimerException {
		if ( logger.isDebugEnabled()) {
			logger.debug("selectCrew() called with ID " + crewId);
		}

		if ( crewId == null || "".equals(crewId.trim()) )
			return null;

		Connection c = null;
		PreparedStatement s = null;
		Crew crew = new Crew ();

		try {
			c = dataSource.getConnection();

			StringBuffer strSql = new StringBuffer("select * from crew ");
			strSql.append(" where id='" + crewId + "'");
			if ( logger.isDebugEnabled()) {
				logger.debug("Query " + strSql.toString());
			}
			s = c.prepareStatement(strSql.toString());

			ResultSet rs = s.executeQuery();
			if ( rs.next() ) {
				crew.setId(rs.getString("id"));
				crew.setCompetitor(selectCompetitor(rs.getString("competitor_id")));
				crew.setDescription(rs.getString("description"));
				crew.setCrewMembers(selectCrewMembers(rs.getString("id")));

			} else {
				logger.warn("Crew with ID=" + crewId + " does not exist on the database ");
				throw new RowTimerException("Crew with ID=" + crewId + " does not exist on the database ");
			}
			rs.close();
		} catch (RowTimerException rte) {
			throw rte; 
		} catch (Exception e) {
			logger.error("Error selecting crew",e);
			throw new RowTimerException(e);
		} finally {

			try {
				s.close();
				c.close();
			} catch (SQLException sqle) {
				logger.error("Error closing SQL statement and connection ",sqle);
			}
		}
		return crew;
	}


	private List<CrewMember> selectCrewMembers(String crewId) throws RowTimerException {
		List<CrewMember> alignment = new ArrayList<CrewMember>();
		if ( logger.isDebugEnabled()) {
			logger.debug("selectCrewMembers() called " );
		}

		Connection c = null;
		PreparedStatement s = null;
		if ( crewId == null || "".equals(crewId.trim()) )
			return null;

		try {
			c = dataSource.getConnection();

			StringBuffer strSql = new StringBuffer("select * from crew_member ");
			strSql.append(" where crew_id='" + crewId + "'");
			strSql.append(" order by position");
			s = c.prepareStatement(strSql.toString());

			ResultSet rs = s.executeQuery();
			while (rs.next() ) {
				CrewMember crewMember = new CrewMember();

				crewMember.setCrewId(rs.getString("crew_id"));
				crewMember.setPosition(rs.getInt("position"));
				crewMember.setPerson(selectPerson(rs.getString("person_id")));
				alignment.add(crewMember);
			}
			rs.close();
		} catch (Exception e) {
			logger.error("Error selecting alignment",e);
			throw new RowTimerException(e);
		} finally {

			try {
				s.close();
				c.close();
			} catch (SQLException sqle) {
				logger.error("Error closing SQL statement and connection ",sqle);
			}
		}
		return alignment;
	}

	private Competitor selectCompetitor(String competitorId) throws RowTimerException {
		if ( logger.isDebugEnabled()) {
			logger.debug("selectPerson() called with ID " + competitorId);
		}

		if ( competitorId == null || "".equals(competitorId.trim()) )
			return null;

		Connection c = null;
		PreparedStatement s = null;
		Competitor competitor = new Competitor();

		try {
			c = dataSource.getConnection();

			StringBuffer strSql = new StringBuffer("select * from competitor ");
			strSql.append(" where id='" + competitorId + "'");
			s = c.prepareStatement(strSql.toString());

			ResultSet rs = s.executeQuery();
			if ( rs.next() ) {
				competitor.setId(rs.getString("id"));
				competitor.setName(rs.getString("name"));
				competitor.setAcronym(rs.getString("acronym"));

			} else {
				logger.warn("Competitor with ID=" + competitorId + " does not exist on the database ");
				throw new RowTimerException("Crew with ID=" + competitorId + " does not exist on the database ");
			}
			rs.close();
		} catch (RowTimerException rte) {
			throw rte; 
		} catch (Exception e) {
			logger.error("Error selecting competitor",e);
			throw new RowTimerException(e);
		} finally {

			try {
				s.close();
				c.close();
			} catch (SQLException sqle) {
				logger.error("Error closing SQL statement and connection ",sqle);
			}
		}
		return competitor;
	}


	private Person selectPerson(String personId) throws RowTimerException {
		if ( logger.isDebugEnabled()) {
			logger.debug("selectPerson() called with ID " + personId);
		}

		if ( personId == null || "".equals(personId.trim()) )
			return null;

		Connection c = null;
		PreparedStatement s = null;
		Person person = new Person();

		try {
			c = dataSource.getConnection();

			StringBuffer strSql = new StringBuffer("select * from person ");
			strSql.append(" where id='" + personId + "'");
			s = c.prepareStatement(strSql.toString());

			ResultSet rs = s.executeQuery();
			if ( rs.next() ) {
				person.setId(rs.getString("id"));
				person.setName(rs.getString("name"));

			} else {
				logger.warn("Person with ID=" + personId + " does not exist on the database ");
				throw new RowTimerException("Crew with ID=" + personId + " does not exist on the database ");
			}
			rs.close();
		} catch (RowTimerException rte) {
			throw rte; 
		} catch (Exception e) {
			logger.error("Error selecting person",e);
			throw new RowTimerException(e);
		} finally {

			try {
				s.close();
				c.close();
			} catch (SQLException sqle) {
				logger.error("Error closing SQL statement and connection ",sqle);
			}
		}
		return person;
	}


}
