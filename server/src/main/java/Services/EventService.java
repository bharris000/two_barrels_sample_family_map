package Services;

import DAO.*;
import Model.Event;
import Result.EventResult;
import Result.PersonResult;

import java.sql.Connection;

public class EventService {

    /**
     * Returns the single Event object with the specified ID.
     *
     * @return an EventResult with info on the desired result and success status.
     */
    public static EventResult getEvent(String eventID) throws DataAccessException {

        Database db = new Database();
        Connection conn = db.openConnection();
        Event event;
        try {
            EventDAO eDao = new EventDAO(conn);
            event = eDao.findByID(eventID);
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            e.printStackTrace();
            db.closeConnection(false);
            throw e;
        }

        return new EventResult(event);
    }
}
