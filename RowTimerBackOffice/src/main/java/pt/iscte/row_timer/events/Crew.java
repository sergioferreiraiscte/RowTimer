package pt.iscte.row_timer.events;

import java.util.List;

public class Crew {
	String id;
	Competitor competitorId;
	String description;
	List<CrewMember> crewMembers;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Competitor getCompetitorId() {
		return competitorId;
	}
	public void setCompetitorId(Competitor competitorId) {
		this.competitorId = competitorId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<CrewMember> getCrewMembers() {
		return crewMembers;
	}
	public void setCrewMembers(List<CrewMember> crewMembers) {
		this.crewMembers = crewMembers;
	}

}
