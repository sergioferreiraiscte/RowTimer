package pt.iscte.row_timer.events;

import java.time.LocalDate;
import java.util.List;

/**
 * Represent a rowing event : Example Campeonato Nacional de Velocidade
 * @author sergio
 *
 */
public class RowingEvent {
	String id;
	String name;
	LocalDate event_date;
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
	public LocalDate getEvent_date() {
		return event_date;
	}
	public void setEvent_date(LocalDate event_date) {
		this.event_date = event_date;
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
}
