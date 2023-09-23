package Handlers;

import DAO.DataAccessException;
import Request.FillRequest;
import Request.RegisterRequest;
import Result.ErrorResult;
import Result.LoginResult;
import Result.Serializer;
import Services.FillService;
import Services.RegisterService;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class RegisterHandler extends Handler {

    /**
     * Handles a request for a /user/register API and executes the RegisterService.
     *
     * @param httpExchange the exchange with a request
     * @throws IOException
     */
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        boolean success = false;
        try {
            System.out.println("Register handler is running");

            // check to see if request is post
            if (httpExchange.getRequestMethod().equalsIgnoreCase("post")) {

                System.out.println("method is POST");

                // execute RegisterService
                Headers requestHeaders = httpExchange.getRequestHeaders();
                InputStream requestBody = httpExchange.getRequestBody();
                String jsonInput = Handler.readString(requestBody);
                RegisterRequest registerRequest = null;
                registerRequest = (RegisterRequest) Serializer.deserialize(jsonInput, RegisterRequest.class);
                LoginResult registerResponse = RegisterService.register(registerRequest);

                // execute FillService
                String username = registerRequest.getUsername();
                FillRequest fillRequest = new FillRequest(username, 4);
                FillService.fill(fillRequest);

                // send successful response and OK key
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                String loginJson = registerResponse.serialize();
                OutputStream responseBody = httpExchange.getResponseBody();
                Handler.writeString(loginJson, responseBody);

                responseBody.close();

                success = true;
            }

            if (!success) {
                throw new DataAccessException("Register failed.");
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
