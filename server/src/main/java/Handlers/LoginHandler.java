package Handlers;

import DAO.DataAccessException;
import Request.LoginRequest;
import Result.ErrorResult;
import Result.LoginResult;
import Result.Serializer;
import Services.LoginService;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class LoginHandler extends Handler{

    /**
     * Handles a request for a /user/login API and executes the LoginService.
     *
     * @param httpExchange the exchange with a request
     * @throws IOException
     */
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        boolean success = false;
        try {
            System.out.println("Login handler is running");

            // check to see if request is post
            if (httpExchange.getRequestMethod().toLowerCase().equals("post")) {

                // create LoginRequest
                Headers requestHeaders = httpExchange.getRequestHeaders();
                InputStream requestBody = httpExchange.getRequestBody();
                String jsonInput = Handler.readString(requestBody);
                LoginRequest loginRequest = null;
                loginRequest = (LoginRequest) Serializer.deserialize(jsonInput, LoginRequest.class);

                // execute LoginService
                LoginResult loginResult = LoginService.login(loginRequest);

                // send successful result and OK key
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                String resultJson = loginResult.serialize();
                OutputStream resultBody = httpExchange.getResponseBody();
                Handler.writeString(resultJson, resultBody);

                resultBody.close();

                success = true;
            }

            if (!success) {
                throw new DataAccessException("Login failed.");
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
