package Services;

import DAO.DataAccessException;
import DAO.Database;
import DAO.PersonDAO;
import DAO.UserDAO;
import Model.Person;
import Model.User;
import Request.LoginRequest;
import Request.RegisterRequest;
import Result.LoadResult;
import Result.LoginResult;

import java.sql.Connection;

public class RegisterService {

    /**
     *  Creates a new user account, generates 4 generations
     *  of ancestor data for the new user, logs the user in,
     *  and returns an auth token.
     *
     * @param request RegisterRequest with login and personal info on user registering.
     * @return RegisterResult with authToken and username and success status.
     */
    public static LoginResult register(RegisterRequest request) throws DataAccessException {

        boolean commit = false;
        Database db = new Database();
        LoginResult result;
        User user;
        Person userPerson;

        try {
            Connection conn = db.openConnection();

            // new person for the user
            userPerson = new Person(
                    request.getFirstName(),
                    request.getLastName(),
                    request.getGender(),
                    "",
                    "",
                    "",
                    request.getUsername()
            );

            // new user
            user = new User(
                    request.getUsername(),
                    request.getPassword(),
                    request.getEmail(),
                    request.getFirstName(),
                    request.getLastName(),
                    request.getGender(),
                    userPerson.getPersonID()
            );


            UserDAO uDao = new UserDAO(conn);
            PersonDAO pDao = new PersonDAO(conn);
            pDao.insert(userPerson);
            uDao.insert(user);

            commit = true;
        }
        finally {
            db.closeConnection(commit);
        }

        LoginRequest loginRequest = new LoginRequest(
                user.getUsername(),
                user.getPassword()
        );

        // result body is the same as a login, so execute login
        result = LoginService.login(loginRequest);
        return result;
    }

}
