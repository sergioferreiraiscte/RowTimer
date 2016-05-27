package pt.iscte.row_timer.android.model;


import java.util.Date;
import java.util.List;

public class Race {
    String eventId;
    Integer seqno;
    Date hour;
    BoatType boatType;
    Category category;
    Date startTime;
    List<Alignment> crewAlignment;
    public String getEventId() {
        return eventId;
    }
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
    public Integer getSeqno() {
        return seqno;
    }
    public void setSeqno(Integer seqno) {
        this.seqno = seqno;
    }
    public Date getHour() {
        return hour;
    }
    public void setHour(Date hour) {
        this.hour = hour;
    }
    public BoatType getBoatType() {
        return boatType;
    }
    public void setBoatType(BoatType boatType) {
        this.boatType = boatType;
    }
    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }
    public List<Alignment> getCrewAlignment() {
        return crewAlignment;
    }
    public void setCrewAlignment(List<Alignment> crewAlignment) {
        this.crewAlignment = crewAlignment;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
}
