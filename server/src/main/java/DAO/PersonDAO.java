package DAO;

import Model.Person;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Data Access object used to access a person in connection with the database.
 */
public class PersonDAO {
    Connection conn;

    public PersonDAO(Connection conn) {
        this.conn = conn;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    /**
     * Inserts a new event into the database.
     *
     * @param personInput the new person to be inserted.
     * @throws DataAccessException
     */
    public void insert(Person personInput) throws DataAccessException {
        String sql = "INSERT INTO person (personID, firstName, lastName, gender, fatherID, " +
                "motherID, spouseID, associatedUsername) VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personInput.getPersonID());
            stmt.setString(2, personInput.getFirstName());
            stmt.setString(3, personInput.getLastName());
            stmt.setString(4, personInput.getGender());
            stmt.setString(5, personInput.getFatherID());
            stmt.setString(6, personInput.getMotherID());
            stmt.setString(7, personInput.getSpouseID());
            stmt.setString(8, personInput.getAssociatedUsername());

            stmt.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /**
     * Inserts an array of person objects into the database.
     *
     * @param persons the collection of persons to be inserted.
     * @throws DataAccessException
     */
    public void insertArray(Collection<Person> persons) throws DataAccessException {
        if(!persons.isEmpty())
        for (Person person : persons) {
            insert(person);
        }
    }

    /**
     * Finds a single person by a given personID. To be used with PersonService.
     *
     * @param personID the ID of the person to be searched for.
     * @return the found person or null if not found.
     * @throws DataAccessException
     */
    public Person findByID(String personID)throws DataAccessException {
        Person person;
        ResultSet rs = null;
        String sql = "SELECT * FROM person WHERE personID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                person = new Person(rs.getString("personID"), rs.getString("firstName"),
                        rs.getString("lastName"), rs.getString("gender"), rs.getString("fatherID"),
                        rs.getString("motherID"), rs.getString("spouseID"), rs.getString("associatedUsername"));
                return person;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding person");
        }
        finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }

    /**
     * Finds an array of all family members of the current person. To be used with the PersonArrayService.
     *
     * @param username the person to find all family members of.
     * @return an array of all people in the family.
     */
    public ArrayList<Person> findAll(String username) throws DataAccessException {
        ArrayList<Person> toReturn = new ArrayList<>();
        Person person = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM person WHERE associatedUsername = ?;";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            while (rs.next()) {
                person = new Person(
                        rs.getString("personID"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("gender"),
                        rs.getString("fatherID"),
                        rs.getString("motherID"),
                        rs.getString("spouseID"),
                        rs.getString("associatedUsername"));
                toReturn.add(person);
            }
            rs.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding person");
        }
        return toReturn;
    }

    /**
     * Deletes a person from the database.
     *
     * @param personInput the person to be deleted.
     */
    public void delete(Person personInput) {
        String sql = "DELETE FROM person WHERE personID = " + personInput.getPersonID();
    }

    /**
     * Clears all persons from the database.
     *
     * @throws DataAccessException
     * @throws SQLException
     */
    public void clear() throws DataAccessException, SQLException {
        String sql = "DELETE FROM person";
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
