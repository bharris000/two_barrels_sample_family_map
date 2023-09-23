package Handlers;

import DAO.DataAccessException;
import Result.ClearResult;
import Result.ErrorResult;
import Services.ClearService;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.HttpURLConnection;

public class ClearHandler extends Handler{

    /**
     * Handles a request for a /clear API and executes the ClearService.
     *
     * @param httpExchange the exchange with a request
     * @throws IOException
     */
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        boolean success = false;
        try {
            System.out.println("Clear handler running");

            // check to see if request is post
            if (httpExchange.getRequestMethod().toLowerCase().equals("post")) {

                // execute clear and send successful response and OK key
                ClearService.clear();
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                OutputStream resultBody = httpExchange.getResponseBody();
                ClearResult successResult = new ClearResult("Clear succeeded.");
                String resultJson = successResult.serialize();
                Handler.writeString(resultJson, resultBody);

                resultBody.close();
                success = true;
            }

            if (!success) {
                throw new DataAccessException("Clear failed");
            }
        }
        catch (Exception e) {

            // send BAD REQUEST key and ErrorResult
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            ErrorResult errorResult = new ErrorResult(e.getMessage());
            String errorJson = errorResult.serialize();
            OutputStream resultBody = httpExchange.getResponseBody();
            Handler.writeString(errorJson, resultBody);

            resultBody.close();
        }
    }

}
