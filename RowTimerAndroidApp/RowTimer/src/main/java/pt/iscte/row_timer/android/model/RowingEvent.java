package pt.iscte.row_timer.android.model;


import java.util.Date;
import java.util.List;

/**
 * Represent a rowing event : Example Campeonato Nacional de Velocidade
 *
 * @author sergio
 */
public class RowingEvent {
    String id;
    String name;
    Date eventDate;
    Date changeMoment;
    String location;
    List<Race> eventRaces;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date event_date) {
        this.eventDate = event_date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Race> getEventRaces() {
        return eventRaces;
    }

    public void setEventRaces(List<Race> eventRaces) {
        this.eventRaces = eventRaces;
    }

    public Date getChangeMoment() {
        return changeMoment;
    }

    public void setChangeMoment(Date changeMoment) {
        this.changeMoment = changeMoment;
    }
};

