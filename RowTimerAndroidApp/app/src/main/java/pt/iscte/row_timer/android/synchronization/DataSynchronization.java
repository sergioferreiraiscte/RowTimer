package pt.iscte.row_timer.android.synchronization;

/**
 * Behaviour related to remote data synchronization with current
 * Transfer of local films only occurr with Wifi connectivity
 */
public class DataSynchronization {

    /**
     * Get events list and store them in a local table to be shown
     *
     * It get only events that where changed since last pull
     *
     * TODO: Implement it
     */
    public void getEventsList() {
        // Get last pull time from the database
        // Get events basic info changed since last pull
    }

    /**
     * Get all info about an event if it does not ecist and it was not changed
     * Store the event info on the database and make it available to the Application object
     *
     * TODO : Implement it
     *
     * @param eventID
     */
    public void getEventDetails(String eventID) {
        // Get last event change time on local database
        // Ask the server for the event passing the date of last pull (null don't have it)
    }

    /**
     * Send results thar still did not been sent to the srevice
     * TODO : Implement it
     */
    public void sendResults() {
        // Send start times for races
        // Send finish times for races

    }

    /**
     * Send movies, only if there is wifi connectivity
     * TODO : Implement it
     */
    public void sendMovies() {

    }

    /**
     * Check if there is connectivity to the server.
     * Start to check if there is wifi, then GSM/3G/4G
     *
     * TODO : Implement this behaviour
     *
     * @return
     */
    private boolean hasServiceConnectivity() {
        return true;
    }
}
