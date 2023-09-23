package Handlers;

import DAO.DataAccessException;
import Model.Event;
import Result.ErrorResult;
import Result.EventArrayResult;
import Result.EventResult;
import Services.EventArrayService;
import Services.EventService;
import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.HttpURLConnection;

public class EventHandler extends Handler {

    /**
     * Handles requests for /event and /event[eventID] API and
     * executes the corresponding EventService.
     *
     * @param httpExchange the exchange with a request
     * @throws IOException
     */
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        boolean success = false;
        try {
            System.out.println("Event handler is running");

            // check to see if request is get
            if (httpExchange.getRequestMethod().equalsIgnoreCase("get")) {

                // parse username and event
                Headers headers = httpExchange.getRequestHeaders();
                String token = headers.getFirst("Authorization");
                String username = Handler.authenticate(token);

                String path = httpExchange.getRequestURI().getPath();
                System.out.println(path);
                String[] uri = path.split("/");
                String eventID = null;

                try {
                    eventID = uri[uri.length - 1];
                }
                catch (Exception e) {
                    throw new DataAccessException("'Event' failed.");
                }

                System.out.println("EventID " + eventID);

                // run respective event service
                String resultJson = null;
                if (eventID.equalsIgnoreCase("event")) { // EventArrayService

                    EventArrayResult successResult = null;
                    successResult = EventArrayService.getEvents(username);
                    resultJson = successResult.serialize();
                }
                else { // EventService

                    EventResult successResult = EventService.getEvent(eventID);
                    if (!username.equals(successResult.getAssociatedUsername())) {
                        throw new DataAccessException("This event does not belong to " + username);
                    }

                    Gson gson = new Gson();
                    resultJson = gson.toJson(successResult, EventResult.class);
                }

                // send successful response and OK key
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                OutputStream resultBody = httpExchange.getResponseBody();
                Handler.writeString(resultJson, resultBody);

                resultBody.close();
                success = true;
            }

            if (!success) {
                throw new DataAccessException("Event failed");
            }
        }
        catch (Exception e) {

            // send BAD REQUEST key and ErrorResult
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            ErrorResult errorResult = new ErrorResult(e.getMessage());
            String errorJson = errorResult.serialize();
            OutputStream responseBody = httpExchange.getResponseBody();
            Handler.writeString(errorJson, responseBody);

            responseBody.close();
        }
    }

}
