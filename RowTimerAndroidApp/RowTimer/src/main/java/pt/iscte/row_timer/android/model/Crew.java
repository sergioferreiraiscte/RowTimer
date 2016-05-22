package pt.iscte.row_timer.android.model;


import java.util.List;

public class Crew {
    String id;
    Competitor competitor;
    String description;
    List<CrewMember> crewMembers;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Competitor getCompetitor() {
        return competitor;
    }
    public void setCompetitor(Competitor competitor) {
        this.competitor = competitor;
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