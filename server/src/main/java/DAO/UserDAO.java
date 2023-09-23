package DAO;

import Model.Event;
import Model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

/**
 * Data Access object used to access a user in connection with the database.
 */
public class UserDAO {
    Connection conn;

    public UserDAO(Connection conn) {
        this.conn = conn;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection userConnection) {
        this.conn = userConnection;
    }

    /**
     * Inserts a ner user into the database.
     *
     * @param userInput the new user to be inserted.
     * @throws DataAccessException
     */
    public void insert(User userInput) throws DataAccessException {

        String sql = "INSERT INTO user (username, password, email, firstName, lastName, " +
                "gender, personID) VALUES(?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userInput.getUsername());
            stmt.setString(2, userInput.getPassword());
            stmt.setString(3, userInput.getEmail());
            stmt.setString(4, userInput.getFirstName());
            stmt.setString(5, userInput.getLastName());
            stmt.setString(6, userInput.getGender());
            stmt.setString(7, userInput.getPersonID());

            stmt.executeUpdate();
        }
        catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /**
     * Inserts an array of user objects into the database.
     *
     * @param users the collection of users to be inserted.
     * @throws DataAccessException
     */
    public void insertArray(Collection<User> users) throws DataAccessException {
        if(!users.isEmpty())
        for (User user : users) {
            insert(user);
        }
    }

    /**
     * Finds a person in the database by username.
     *
     * @param username the username of the user to be searched for.
     * @return the found user or null if not found.
     */
    public User find(String username)throws DataAccessException {
        User user;
        ResultSet rs = null;
        String sql = "SELECT * FROM user WHERE username = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User(rs.getString("username"), rs.getString("password"),
                        rs.getString("email"), rs.getString("firstName"), rs.getString("lastName"),
                        rs.getString("gender"), rs.getString("personID"));
                return user;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding user");
        }
        finally {
            if(rs != null) {
                try {
                    rs.close();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }

    /**
     * Deletes a user from the database.
     *
     * @throws DataAccessException
     * @throws SQLException
     */
    public void delete(String userInput) throws DataAccessException, SQLException {
        String sql = "DELETE FROM user WHERE username = " + userInput;
        PreparedStatement stmt = null;
        try  {
            stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();
        }
        catch (SQLException e) {
            throw new DataAccessException("Error encountered while deleting from the database");
        }
        finally {
            assert stmt != null;
            stmt.close();
        }
    }

    /**
     * Clears all user objects from the database.
     *
     * @throws DataAccessException
     * @throws SQLException
     */
    public void clear() throws DataAccessException, SQLException {
        String sql = "DELETE FROM user";
        PreparedStatement stmt = null;
        try  {
            stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();
        }
        catch (SQLException e) {
            throw new DataAccessException("Error encountered while deleting from the database");
        }
        finally {
            assert stmt != null;
            stmt.close();
        }
    }
}

