package pt.iscte.row_timer.events;

public class Alignment {
	String event_id;
	Integer lane;
	Crew crew;
	Integer RaceNo; 
	public String getEvent_id() {
		return event_id;
	}
	public void setEvent_id(String event_id) {
		this.event_id = event_id;
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
}
