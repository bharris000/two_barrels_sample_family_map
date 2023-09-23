package Services;

import DAO.*;
import Model.Person;
import Result.PersonArrayResult;

import java.sql.Connection;
import java.util.ArrayList;

public class PersonArrayService {

    /**
     * Returns ALL family members of the current user.
     * The current user is determined from the provided auth token.
     *
     * @return PersonArrayResult with an array of all family members and success status.
     */
    public static PersonArrayResult getPeople(String username) throws DataAccessException {

        Database db = new Database();
        Connection conn = db.openConnection();
        ArrayList<Person> resultArray;
        try {
            PersonDAO pDao = new PersonDAO(conn);
            resultArray = pDao.findAll(username);

            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            e.printStackTrace();
            db.closeConnection(false);
            throw e;
        }

        return new PersonArrayResult(resultArray);
    }
}
