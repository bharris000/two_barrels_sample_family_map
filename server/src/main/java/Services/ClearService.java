package Services;

import DAO.DataAccessException;
import DAO.Database;
import Result.ClearResult;

public class ClearService {

    /**
     * Deletes ALL data from the database, including user accounts,
     * auth tokens, and generated person and event data.
     *
     * @return a ClearResult with a message and success status.
     */
    public static ClearResult clear() throws DataAccessException {

        Database db = new Database();
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
        return new ClearResult("Clear succeeded.");

    }
}
