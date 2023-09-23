package DAO;

import Model.Authtoken;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Data Access object used to access an authentication token in connection with the database.
 */
public class AuthtokenDAO {
    Connection conn;

    public AuthtokenDAO(Connection conn)    {
        this.conn = conn;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    /**
     * Inserts a new auth token into the database.
     *
     * @param tokenInput the Authtoken object to be inserted.
     */
    public void insert(Authtoken tokenInput) throws DataAccessException {
        String sql = "INSERT INTO authtoken (token, associatedUsername) VALUES(?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tokenInput.getToken());
            stmt.setString(2, tokenInput.getUsername());

            stmt.executeUpdate();
        }
        catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /**
     * Finds a token with a given token ID.
     *
     * @param tokenInput the auth token ID to be searched for.
     * @return the auth token that is found or null if not found.
     */
    public Authtoken find(String tokenInput) throws DataAccessException {
        Authtoken token;
        ResultSet rs = null;
        String sql = "SELECT * FROM authtoken WHERE token = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tokenInput);
            rs = stmt.executeQuery();
            if (rs.next()) {
                token = new Authtoken(rs.getString("token"), rs.getString("associatedUsername"));
                return token;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding event");
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
     * Deletes an authtoken from the database.
     *
     * @param tokenInput the token to be deleted.
     * @throws DataAccessException
     * @throws SQLException
     */
    public void delete(Authtoken tokenInput) throws DataAccessException, SQLException {
        String sql = "DELETE FROM authtoken WHERE token = " + tokenInput.getToken();
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
     * Clears all authtokens from the database.
     *
     * @throws DataAccessException
     * @throws SQLException
     */
    public void clear() throws DataAccessException, SQLException {
        String sql = "DELETE FROM authToken";
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
     * Checks that a token is valid when calling an API that requires one.
     *
     * @param authToken
     * @return
     * @throws DataAccessException
     */
    public String authenticate(String authToken) throws DataAccessException {
        String username = null;
        try {
            Authtoken token = this.find(authToken);
            if (token == null) {
                throw new DataAccessException("Invalid auth token");
            }
            username = token.getUsername();
            return username;
        }
        catch (DataAccessException e) {
            throw e;
        }
    }
}
