package DAO;

import Model.Event;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Data Access object used to access an event in connection with the database.
 */
public class EventDAO {
    Connection conn;

    public EventDAO(Connection conn)    {
        this.conn = conn;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection eventConnection) {
        this.conn = eventConnection;
    }

    /**
     * Inserts a new event into the database.
     *
     * @param eventInput the event to be inserted.
     * @throws DataAccessException
     */
    public void insert(Event eventInput) throws DataAccessException {
        String sql = "INSERT INTO event (eventID, associatedUsername, personID, latitude, longitude, " +
                "country, city, eventType, year) VALUES(?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eventInput.getEventID());
            stmt.setString(2, eventInput.getAssociatedUsername());
            stmt.setString(3, eventInput.getPersonID());
            stmt.setFloat(4, eventInput.getLatitude());
            stmt.setFloat(5, eventInput.getLongitude());
            stmt.setString(6, eventInput.getCountry());
            stmt.setString(7, eventInput.getCity());
            stmt.setString(8, eventInput.getEventType());
            stmt.setInt(9, eventInput.getYear());

            stmt.executeUpdate();
        }
        catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /**
     * Inserts an array of event objects into the database.
     *
     * @param events the collection of events to be inserted.
     * @throws DataAccessException
     */
    public void insertArray(Collection<Event> events) throws DataAccessException {
        if(!events.isEmpty())
        for (Event event : events) {
            insert(event);
        }
    }

    /**
     * Finds a single event by an ID. To be used with the EventService.
     *
     * @param eventID the event ID to be searched for.
     * @return the single event that is found or null if not found.
     * @throws DataAccessException
     */
    public Event findByID(String eventID) throws DataAccessException {
        Event event;
        ResultSet rs = null;
        String sql = "SELECT * FROM event WHERE eventID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eventID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                event = new Event(rs.getString("eventID"), rs.getString("associatedUsername"),
                        rs.getString("personID"), rs.getFloat("latitude"), rs.getFloat("longitude"),
                        rs.getString("country"), rs.getString("city"), rs.getString("eventType"),
                        rs.getInt("year"));
                return event;
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
     * Finds all events for a person and all family members. To be used with EventArrayService.
     *
     * @param username the user to find all events for.
     * @return an array of all events for the specified person and their family.
     * @throws DataAccessException
     */
    public ArrayList<Event> findAll(String username) throws DataAccessException {
        ArrayList<Event> eventArray = new ArrayList<>();

        Event event = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM event WHERE associatedUsername = ?;";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            while (rs.next()) {
                event = new Event(
                        rs.getString("eventID"),
                        rs.getString("associatedUsername"),
                        rs.getString("personID"),
                        rs.getFloat("latitude"),
                        rs.getFloat("longitude"),
                        rs.getString("country"),
                        rs.getString("city"),
                        rs.getString("eventType"),
                        rs.getInt("year"));
                eventArray.add(event);
            }
            rs.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding event");
        }
        return eventArray;
    }

    /**
     * Delets an event from the database.
     *
     * @param eventInput the event to be deleted.
     * @throws DataAccessException
     * @throws SQLException
     */
    public void delete(Event eventInput) throws DataAccessException, SQLException {
        String sql = "DELETE FROM event WHERE eventID = " + eventInput.getEventID();
        PreparedStatement stmt = null;
        try  {
            stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();
        }
        catch (SQLException e) {
            throw new DataAccessException("Error encountered while deleting from the database");
        }
        finally {
            stmt.close();
        }
    }

    /**
     * Deletes all events with a given associatedUsername.
     *
     * @param username events with this associatedUsername will be deleted.
     * @throws DataAccessException
     * @throws SQLException
     */
    public void deleteAll(String username) throws DataAccessException, SQLException {
        String sql = "DELETE FROM event WHERE associatedUsername = '" + username + "'";
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
     * Clears all events from the database.
     *
     * @throws DataAccessException
     * @throws SQLException
     */
    public void clear() throws DataAccessException, SQLException {
        String sql = "DELETE FROM event";
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
