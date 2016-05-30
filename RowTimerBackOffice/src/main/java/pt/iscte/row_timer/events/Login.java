package pt.iscte.row_timer.events;

/**
 * Created by sergio on 29-05-2016.
 */
public class Login {
    private Boolean logged;
    private String username;
    private String password;

    public Boolean getLogged() {
        return logged;
    }

    public void setLogged(Boolean logged) {
        this.logged = logged;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
