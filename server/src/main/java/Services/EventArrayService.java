package Services;

import DAO.*;
import Model.Event;
import Result.EventArrayResult;

import java.sql.Connection;
import java.util.ArrayList;

public class EventArrayService {

    /**
     * Returns ALL events for ALL family members of the current user.
     * The current user is determined from the provided auth token.
     *
     * @param username will search for all events with this associatedUsername.
     * @return an EventArrayResult with an array of events and success status.
     */
    public static EventArrayResult getEvents(String username)  throws DataAccessException {

        Database db = new Database();
        Connection conn = db.openConnection();
        ArrayList<Event> eventArray = null;
        try {
            EventDAO eDao = new EventDAO(conn);
            eventArray = eDao.findAll(username);

            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
            throw e;
        }
        return new EventArrayResult(eventArray);
    }
}
