package Handlers;

import DAO.DataAccessException;
import Model.Person;
import Result.ErrorResult;
import Result.PersonArrayResult;
import Result.PersonResult;
import Services.PersonArrayService;
import Services.PersonService;
import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class PersonHandler extends Handler{

    /**
     * Handles requests for /person and /person[personID] API and
     * executes the corresponding PersonService.
     *
     * @param httpExchange the exchange with a request
     * @throws IOException
     */
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        boolean success = false;
        try {

            System.out.println("Person handler is running");

            // check to see if request is get
            if (httpExchange.getRequestMethod().toLowerCase().equals("get")) {

                // authenticate
                Headers headers = httpExchange.getRequestHeaders();
                String token = headers.getFirst("Authorization");
                String username = Handler.authenticate(token);

                // parse input path
                String path = httpExchange.getRequestURI().getPath();
                System.out.println(path);
                String[] uri = path.split("/");
                String personID = null;

                try {
                    personID = uri[uri.length - 1];
                }
                catch (Exception e) {
                    throw new DataAccessException("'Person' failed.");
                }

                System.out.println("PersonID" + personID);

                // run respective person service
                String resultJson = null;
                if (personID.toLowerCase().equals("person")) { // PersonArrayService

                    PersonArrayResult successfulResult = null;
                    successfulResult = PersonArrayService.getPeople(username);
                    resultJson = successfulResult.serialize();

                }
                else { // PersonService

                    // get person and check authtoken
                    PersonResult personResponse = PersonService.getPerson(personID);
                    if (!username.equals(personResponse.getAssociatedUsername())) {
                        throw new DataAccessException("Person not associated with " + username);
                    }

                    Gson gson = new Gson();
                    resultJson = gson.toJson(personResponse, PersonResult.class);
                }

                // send successful response and OK key
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                OutputStream resultBody = httpExchange.getResponseBody();
                Handler.writeString(resultJson, resultBody);

                resultBody.close();

                success = true;
            }

            if (!success) {
                throw new DataAccessException("Person request failed");
            }
        }
        catch (Exception e) {

            // send BAD REQUEST key and ErrorResult
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            ErrorResult errorResponse = new ErrorResult(e.getMessage());
            String errorJson = errorResponse.serialize();
            OutputStream responseBody = httpExchange.getResponseBody();
            Handler.writeString(errorJson, responseBody);

            responseBody.close();
        }
    }

}
