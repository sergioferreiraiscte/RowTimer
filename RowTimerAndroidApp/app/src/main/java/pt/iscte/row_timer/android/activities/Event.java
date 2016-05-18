package pt.iscte.row_timer.android.activities;

public class Event {
    private final int id;
    private final String title;
    private final String description;

    /*
     * Event Constructor - Instantiates a Event Object with a given id, name, and description
     */
    public Event(int id, String name, String description){
        this.id = id;
        this.title = name;
        this.description = description;
    }


    /**
     * General getters and setters
     */

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
