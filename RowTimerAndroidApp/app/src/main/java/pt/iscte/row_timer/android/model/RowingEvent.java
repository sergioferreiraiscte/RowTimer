package pt.iscte.row_timer.android.model;

/**
 * Represent a rowing event : Example Campeonato Nacional de Velocidade
 *
 * @author sergio
 */
public class RowingEvent {
    String id;
    String name;

    public RowingEvent() {
       super();
    }

    public RowingEvent(String id,String name) {
        this.id = id;
        this.name = name;
    }

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
}

