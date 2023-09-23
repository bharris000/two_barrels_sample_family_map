package Server;

import DAO.DataAccessException;
import DAO.Database;
import Handlers.*;
import com.sun.net.httpserver.HttpServer;

import java.awt.event.TextEvent;
import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {
    private static HttpServer server;

    /** Initializes HTTP Server */
    /*private static void execute(String portNumber) {

        System.out.println("Initializing Family Map HTTP Server");

        try {
            server = HttpServer.create(
                    new InetSocketAddress(Integer.parseInt(portNumber)),
                    MAX_WAITING_CONNECTIONS);
        }
        catch (IOException e) {
            e.printStackTrace();
            return;
        }
        server.setExecutor(null);

        // contexts of paths and handlers
        System.out.println("Creating contexts");
        server.createContext("/clear", new ClearHandler());
        server.createContext("/load", new LoadHandler());
        server.createContext("/fill", new FillHandler());
        server.createContext("/user/login", new LoginHandler());
        server.createContext("/user/register", new RegisterHandler());
        server.createContext("/event", new EventHandler());
        server.createContext("/person", new PersonHandler());
        //server.createContext("/", new Handler());

        // start the server
        System.out.println("Starting server on port " + portNumber);
        server.start();
    } */

    private static void runServer(int portNumber) throws IOException {
        InetSocketAddress address = new InetSocketAddress(portNumber);
        server = HttpServer.create(address, 10);
        createHandlers(server);
        server.start();
        System.out.println("Starting server on port " + portNumber);
    }

    public static void createHandlers(HttpServer server) {
        System.out.println("Creating contexts");
        server.createContext("/clear", new ClearHandler());
        server.createContext("/load", new LoadHandler());
        server.createContext("/fill", new FillHandler());
        server.createContext("/user/login", new LoginHandler());
        server.createContext("/user/register", new RegisterHandler());
        server.createContext("/event", new EventHandler());
        server.createContext("/person", new PersonHandler());
        server.createContext("/", new DefaultHandler());
    }

    /** First argument is the port number on which to run the server. */
    public static void main(String[] args) throws IOException {
        runServer(8080); //fixme
    }
}
