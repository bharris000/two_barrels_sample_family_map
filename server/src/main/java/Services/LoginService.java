package Services;

import DAO.AuthtokenDAO;
import DAO.DataAccessException;
import DAO.Database;
import DAO.UserDAO;
import Model.Authtoken;
import Model.User;
import Request.LoginRequest;
import Result.LoginResult;

import java.sql.Connection;

public class LoginService {

    /**
     * Logs in the user and returns an auth token.
     *
     * @param request LoginRequest with a username and password.
     * @return LoginResult with token and user info and success status.
     */
    public static LoginResult login(LoginRequest request) throws DataAccessException {

        Database db = new Database();
        Connection conn;
        boolean commit = false;
        LoginResult result;

        try {
            // get user
            conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);
            User user = uDao.find(request.getUsername());
            if (user == null) {
                throw new DataAccessException("User not found");
            }
            if (!user.getPassword().equals(request.getPassword())) {
                throw new DataAccessException("Incorrect password");
            }

            Authtoken token = new Authtoken(user.getUsername());
            AuthtokenDAO aDao = new AuthtokenDAO(conn);
            aDao.insert(token);

            result = new LoginResult(
                    token.getToken(),
                    user.getUsername(),
                    user.getPersonID());

            commit = true;
        }
        finally {
            db.closeConnection(commit);
        }
        return result;
    }
}

