package Services;

import DAO.*;
import Model.Person;
import Model.User;
import Request.FillRequest;
import Result.FillResult;
import Services.FillStructure.FillTree;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FillService {

    /**
     * Populates the server's database with generated data for the specified user name.
     *
     * @param request FillRequest with the username and number of generations.
     * @return a FillResult with a notification on how many people/events generated and
     * success status.
     * @throws DataAccessException
     * @throws SQLException
     */
    public static FillResult fill(FillRequest request) throws DataAccessException, SQLException {
        String username = request.getUsername();
        int generations = request.getGenerations();

        // start by deleting every event and person
        // associated with the user
        deleteAssociations(username);

        Database db = new Database();
        Connection conn = db.openConnection();
        UserDAO uDao = new UserDAO(conn);
        User user = uDao.find(username);
        db.closeConnection(true);

        if (user == null) {
            throw new DataAccessException(username + " is not a registered user");
        }

        Person person = Person.generateUserPerson(user);
        user.setPersonID(person.getPersonID());

        // use tree to generate random events/people
        FillTree tree = new FillTree(person);
        tree.addParents(generations);
        tree.addTree();

        return new FillResult("Successfully added " + tree.getNumNodes() +
                " persons and " + tree.getNumEvents() + " events to the database.");
    }

    /**
     * Deletes all events and people that have the given associatedUsername.
     *
     * @param username the associatedUsername.
     * @throws DataAccessException
     */
    private static void deleteAssociations(String username) throws DataAccessException {

        Database db = new Database();
        Connection conn = db.openConnection();
        try {
            String sql = "DELETE FROM person WHERE associatedUsername = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.executeUpdate();
            stmt.close();

            sql = "DELETE FROM event WHERE associatedUsername = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.executeUpdate();

            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
            throw e;
        }
        catch (SQLException e) {
            db.closeConnection(false);
            throw new DataAccessException(e.getMessage());
        }
    }

}
