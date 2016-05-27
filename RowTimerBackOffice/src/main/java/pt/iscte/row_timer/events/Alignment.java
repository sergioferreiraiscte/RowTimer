package pt.iscte.row_timer.events;

import java.util.Date;

public class Alignment {
	String eventId;
	Integer lane;
	Crew crew;
	Integer RaceNo; 
	Date endTime;
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String event_id) {
		this.eventId = event_id;
	}
	public Integer getLane() {
		return lane;
	}
	public void setLane(Integer lane) {
		this.lane = lane;
	}
	public Crew getCrew() {
		return crew;
	}
	public void setCrew(Crew crew) {
		this.crew = crew;
	}
	public Integer getRaceNo() {
		return RaceNo;
	}
	public void setRaceNo(Integer raceNo) {
		RaceNo = raceNo;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
}
