package Services;

import DAO.*;
import Model.Event;
import Model.Person;
import Model.User;
import Request.LoadRequest;
import Result.LoadResult;

import java.sql.Connection;
import java.util.ArrayList;

public class LoadService {

    /**
     *  Clears all data from the database (just like the /clear API),
     *  and then loads the posted user, person, and event data into the database.
     *
     * @param request a LoadRequest arrays of Users, Persons, and Events.
     * @return a LoadResult with a message and success status.
     */
    public static LoadResult load(LoadRequest request) throws DataAccessException {

        ClearService.clear();
        ArrayList<User> users = request.getUsers();
        ArrayList<Person> people = request.getPersons();
        ArrayList<Event> events = request.getEvents();
        Database db = new Database();

        try {
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);
            PersonDAO pDao = new PersonDAO(conn);
            EventDAO eDao = new EventDAO(conn);

            //use insertArray in DAOs to insert the arrays from request
            uDao.insertArray(users);
            pDao.insertArray(people);
            eDao.insertArray(events);

            db.closeConnection(true);
        }
        catch (Exception e) {
            db.closeConnection(false);
            throw e;
        }

        return new LoadResult(
                "Successfully added " + users.size() + " users, " +
                        people.size() + " persons, and " +
                        events.size() + " events to the database."
        );
    }
}
