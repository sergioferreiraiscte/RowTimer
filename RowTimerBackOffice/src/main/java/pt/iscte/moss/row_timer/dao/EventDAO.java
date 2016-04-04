package pt.iscte.moss.row_timer.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * Data Access Object to Rowing Events
 * @author Sergio Ferreira
 *
 */

public class EventDAO {
	@PersistenceContext //(unitName="rowTimerDB")
	private EntityManager entityManager;

	/**
	 * Get all events from database
	 * 
	 * @param orderByDate
	 * @return
	 */
	public List<Event> getEvents(String orderByDate) {
		String sqlString = null;
		if(orderByDate != null){
			sqlString = "SELECT e FROM Event e" + " ORDER BY p.startDate " + orderByDate;
		} else {
			sqlString = "SELECT e FROM Event e";
		}		 
		TypedQuery<Event> query = entityManager.createQuery(sqlString, Event.class);		

		return query.getResultList();
	}
}
