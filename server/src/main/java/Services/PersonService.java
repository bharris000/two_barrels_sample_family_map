package Services;

import DAO.*;
import Model.Person;
import Result.PersonResult;

import java.sql.Connection;

public class PersonService {

    /**
     * Returns the single Person object with the specified ID.
     *
     * @return PersonResult with info on person and success status.
     */
    public static PersonResult getPerson(String personID) throws DataAccessException {

        Database db = new Database();
        Connection conn = db.openConnection();
        Person person;
        try {
            PersonDAO pDao = new PersonDAO(conn);
            person = pDao.findByID(personID);

            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            e.printStackTrace();
            db.closeConnection(false);
            throw e;
        }

        return new PersonResult(person);
    }
}
