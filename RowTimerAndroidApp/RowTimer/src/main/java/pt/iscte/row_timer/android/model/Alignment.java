package pt.iscte.row_timer.android.model;


public class Alignment {
    String eventId;
    Integer lane;
    Crew crew;
    Integer RaceNo;
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
}

