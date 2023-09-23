package Handlers;

import DAO.*;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.sql.Connection;

/**
 * An abstract class to house universal functions.
 */
public abstract class Handler implements HttpHandler {

    public Connection conn;

    /**
     * Reads an input stream.
     *
     * @param is InputStream input.
     * @return built String.
     * @throws IOException
     */
    public static String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buffer = new char[1024];
        int length;
        while ((length = sr.read(buffer)) > 0) {
            sb.append(buffer, 0, length);
        }
        return sb.toString();
    }

    /**
     * Writes a string to an output stream.
     *
     * @param str the String input.
     * @param os the OutputStream to write to.
     * @throws IOException
     */
    public static void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter streamWriter = new OutputStreamWriter(os);
        streamWriter.write(str);
        streamWriter.flush();
    }

    /**
     * Checks that an authtoken is valid.
     *
     * @param authToken authtoken to be validated.
     * @return associatedUsername of token.
     */
    public static String authenticate(String authToken) throws DataAccessException {
        Database db = new Database();
        Connection conn = db.openConnection();
        String username = null;

        try {
            AuthtokenDAO aDao = new AuthtokenDAO(conn);
            username = aDao.authenticate(authToken);
        }
        finally {
            db.closeConnection(false);
        }
        return username;
    }

}
