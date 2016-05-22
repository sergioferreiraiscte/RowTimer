package pt.iscte.row_timer.android.model;

public class CrewMember {
    String crewId;
    Integer position;
    Person person;
    public String getCrewId() {
        return crewId;
    }
    public void setCrewId(String crewId) {
        this.crewId = crewId;
    }
    public Person getPerson() {
        return person;
    }
    public void setPerson(Person personId) {
        this.person = personId;
    }
    public Integer getPosition() {
        return position;
    }
    public void setPosition(Integer position) {
        this.position = position;
    }


}
