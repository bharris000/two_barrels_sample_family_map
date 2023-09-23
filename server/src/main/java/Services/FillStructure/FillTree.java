package Services.FillStructure;

import DAO.DataAccessException;
import DAO.Database;
import Model.Event;
import Model.FileUser;
import Model.Location;
import Model.Person;

import java.sql.Connection;
import java.util.ArrayList;

/**
 * A structure to hold info for generated people and events.
 */
public class FillTree {
    private static final int PARENT_AGE_AT_CHILDBIRTH = 25;
    private static final int MARRIAGE_AGE = 20;
    private static final int DEATH_AGE = 80;
    private static final int USER_BIRTHDAY = 2000;

    private String username;
    private int numNodes;
    private int numEvents;

    private FillNode node;

    public FillTree(Person person) {

        this.username = person.getAssociatedUsername();
        this.numNodes = 1;
        this.numEvents = 0;

        node = new FillNode(person);
        Location rootBirthplace = FileUser.getRandomLocation();
        Event birth = new Event(
                person.getAssociatedUsername(),
                person.getPersonID(),
                rootBirthplace.getLatitude(),
                rootBirthplace.getLongitude(),
                rootBirthplace.getCountry(),
                rootBirthplace.getCity(),
                "birth",
                USER_BIRTHDAY

        );
        node.addEvent(birth);
        this.numEvents++;
        node.setBirthday(USER_BIRTHDAY);

    }

    public int getNumNodes() {
        return numNodes;
    }

    public int getNumEvents() {
        return numEvents;
    }

    /**
     * Adds two parent nodes and adds events for them.
     *
     * @param rootNode the child.
     * @return ArrayList of nodes
     * @throws DataAccessException
     */
    public ArrayList<FillNode> createParents(FillNode rootNode) throws DataAccessException {

        numNodes += 2;

        // Add a mother and father to the fill tree
        // father has the same last name, mother has random
        Person father = new Person(
                FileUser.getRandomMaleName(),
                rootNode.getLastName(),
                "m",
                "",
                "",
                "",
                this.username
        );

        Person mother = new Person(
                FileUser.getRandomFemaleName(),
                FileUser.getRandomLastName(),
                "f",
                "",
                "",
                "",
                this.username
                );

        father.setSpouseID(mother.getPersonID());
        mother.setSpouseID(father.getPersonID());

        // create three events for mom and pop,
        // namely: birth, marriage, death
        int parentBirthday = rootNode.getBirthday() - PARENT_AGE_AT_CHILDBIRTH;
        Event fBorn = Event.createBirthEvent(parentBirthday, father);
        Event mBorn = Event.createBirthEvent(parentBirthday, mother);

        int yearOfDeath = parentBirthday + DEATH_AGE;
        Event fDies = Event.createDeathEvent(yearOfDeath, father);
        Event mDies = Event.createDeathEvent(yearOfDeath, mother);

        int yearOfMarriage = parentBirthday + MARRIAGE_AGE;
        Location marriageLocation = FileUser.getRandomLocation();
        Event fMarried = new Event(
                this.username,
                father.getPersonID(),
                marriageLocation.getLatitude(),
                marriageLocation.getLongitude(),
                marriageLocation.getCountry(),
                marriageLocation.getCity(),
                "marriage",
                yearOfMarriage
        );

        Event mMarried = new Event(
                this.username,
                mother.getPersonID(),
                marriageLocation.getLatitude(),
                marriageLocation.getLongitude(),
                marriageLocation.getCountry(),
                marriageLocation.getCity(),
                "marriage",
                yearOfMarriage
        );

        FillNode fNode = new FillNode(father);
        FillNode mNode = new FillNode(mother);
        fNode.addEvent(fBorn);
        fNode.setBirthday(parentBirthday);
        fNode.addEvent(fMarried);
        fNode.addEvent(fDies);
        mNode.addEvent(mBorn);
        mNode.setBirthday(parentBirthday);
        mNode.addEvent(mMarried);
        mNode.addEvent(mDies);
        numEvents += 6;

        // associate with child and get arrays of nodes
        Person root = rootNode.getPerson();
        root.setFatherID(father.getPersonID());
        root.setMotherID(mother.getPersonID());
        rootNode.setFatherNode(fNode);
        rootNode.setMotherNode(mNode);

        ArrayList<FillNode> parentNodes = new ArrayList<>();
        parentNodes.add(fNode);
        parentNodes.add(mNode);
        return parentNodes;
    }

    /**
     * Creates a given number of parent generations.
     *
     * @param generations the amount of parent generations to be created.
     * @throws DataAccessException
     */
    public void addParents(int generations) throws DataAccessException {

        if (generations == 0) {
            return;
        }

        ArrayList<FillNode> currentGen;
        currentGen = createParents(node);

        for (int i = 0; i < generations - 1; i++) {
            ArrayList<FillNode> nextGen = new ArrayList<>();
            for (FillNode temp : currentGen) {
                nextGen.addAll(createParents(temp));
            }
            currentGen = nextGen;
        }

    }

    /**
     * Adds a FillTree to a database.
     *
     * @throws DataAccessException
     */
    public void addTree() throws DataAccessException {

        ArrayList<FillNode> resultList = new ArrayList<>();
        viewAncestors(resultList, node);

        Database db = new Database();
        Connection conn = db.openConnection();

        try {
            for (FillNode temp : resultList) {
                temp.insert(conn);
            }
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
            throw e;
        }
    }

    /**
     * Recursively traverses a tree of ancestor data
     * and adds them to a list to return.
     *
     * @param resultList the list to be added to.
     * @param root the node to be added.
     */
    private void viewAncestors(ArrayList<FillNode> resultList, FillNode root) {

        if (root == null) {
            return;
        }
        resultList.add(root);
        viewAncestors(resultList, root.getFatherNode());
        viewAncestors(resultList, root.getMotherNode());
    }
}
