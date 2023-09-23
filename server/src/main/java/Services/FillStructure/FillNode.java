package Services.FillStructure;

import DAO.*;
import Model.*;

import java.sql.Connection;
import java.util.ArrayList;

/**
 * A node to hold info while filling people/events to a user.
 */
public class FillNode {
    private Person person;
    private ArrayList<Event> events;
    private FillNode fatherNode;
    private FillNode motherNode;
    private FillNode spouseNode;
    private int birthday;

    public FillNode(Person person) {
        this.person = person;
        this.events = new ArrayList<>();
        this.fatherNode = null;
        this.motherNode = null;
        this.spouseNode = null;
    }

    public void insert(Connection conn) throws DataAccessException {
        PersonDAO pDao = new PersonDAO(conn);
        pDao.insert(person);
        EventDAO eDao = new EventDAO(conn);
        for (Event event : events) {
            eDao.insert(event);
        }
    }

    public int getBirthday() {
        return birthday;
    }

    public void setBirthday(int birthday) {
        this.birthday = birthday;
    }

    public FillNode getFatherNode() {
        return fatherNode;
    }

    public void setFatherNode(FillNode fatherNode) {
        this.fatherNode = fatherNode;
    }

    public FillNode getMotherNode() {
        return motherNode;
    }


    public String getLastName() {
        return person.getLastName();
    }

    public void setMotherNode(FillNode motherNode) {
        this.motherNode = motherNode;
    }

    public FillNode getSpouseNode() {
        return spouseNode;
    }

    public void setSpouseNode(FillNode spouseNode) {
        this.spouseNode = spouseNode;
    }

    public void addEvent(Event event) {
        this.events.add(event);
    }

    public String getFatherID() {
        return this.person.getFatherID();
    }

    public String getMotherID() {
        return this.person.getMotherID();
    }

    public String getSpouseID() {
        return this.person.getSpouseID();
    }

    public String getPersonID() {
        return this.person.getPersonID();
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }
}
