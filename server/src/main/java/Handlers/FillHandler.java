package Handlers;

import DAO.DataAccessException;
import Request.FillRequest;
import Result.ErrorResult;
import Result.FillResult;
import Services.FillService;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.HttpURLConnection;

public class FillHandler extends Handler {

    /**
     * Handles requests for /fill/[username] and /fill/[username]/{generations}
     * API and executes the FillService.
     *
     * @param httpExchange the exchange with a request
     * @throws IOException
     */
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        boolean success = false;
        try {

            System.out.println("Fill handler is running");

            // check to see if request is post
            if (httpExchange.getRequestMethod().toLowerCase().equals("post")) {

                // parse desired generations
                String path = httpExchange.getRequestURI().getPath();
                System.out.println(path);
                String[] uri = path.split("/");
                int generations = 4;
                try {
                    //check if a number is entered
                    generations = Integer.parseInt(uri[uri.length - 1]);
                }
                catch (Exception e) {
                    // leave at default 4
                }

                //parse username
                String username = uri[2];

                System.out.println("Username: " + username);
                System.out.println("Generations: " + generations);

                // send successful result and OK key
                FillRequest request = new FillRequest(username, generations);
                FillResult successfulResult = FillService.fill(request);
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                String resultJson = successfulResult.serialize();
                OutputStream resultBody = httpExchange.getResponseBody();
                Handler.writeString(resultJson, resultBody);

                resultBody.close();

                success = true;
            }

            if (!success) {
                throw new DataAccessException("Fill failed");
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
