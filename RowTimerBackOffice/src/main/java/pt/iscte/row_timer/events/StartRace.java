package pt.iscte.row_timer.events;

import java.util.Date;

/**
 * Information about the start of a race.
 * 
 * @author Sergio Ferreira
 *
 */
public class StartRace {
	private String eventId;
	private Integer raceno;
	private Date startMoment;
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	public Integer getRaceno() {
		return raceno;
	}
	public void setRaceno(Integer raceno) {
		this.raceno = raceno;
	}
	public Date getStartMoment() {
		return startMoment;
	}
	public void setStartMoment(Date startMoment) {
		this.startMoment = startMoment;
	}
}
