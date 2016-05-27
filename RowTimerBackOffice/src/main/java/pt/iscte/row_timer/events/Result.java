package pt.iscte.row_timer.events;

import java.util.Date;

/**
 * Information of a result of a race, used to be transfered between the app and the server
 * in order to be stored in the database.
 * 
 * @author sergio
 *
 */
public class Result {
	private String event_id;
	private Integer raceno;
	private Date finishTime;
	private Integer lane;
	private String crewId;
	public String getEvent_id() {
		return event_id;
	}
	public void setEvent_id(String event_id) {
		this.event_id = event_id;
	}
	public Integer getRaceno() {
		return raceno;
	}
	public void setRaceno(Integer raceno) {
		this.raceno = raceno;
	}
	public Date getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}
	public Integer getLane() {
		return lane;
	}
	public void setLane(Integer lane) {
		this.lane = lane;
	}
	public String getCrewId() {
		return crewId;
	}
	public void setCrewId(String crewId) {
		this.crewId = crewId;
	}
}
