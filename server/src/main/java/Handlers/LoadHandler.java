package Handlers;

import DAO.DataAccessException;
import Request.LoadRequest;
import Result.ErrorResult;
import Result.LoadResult;
import Result.Serializer;
import Services.LoadService;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.HttpURLConnection;

public class LoadHandler extends Handler{

    /**
     * Handles a request for a /load API and executes the LoadService.
     *
     * @param httpExchange the exchange with a request
     * @throws IOException
     */
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        boolean success = false;
        try {
            System.out.println("Load handler is running");

            // check to see if request is post
            if (httpExchange.getRequestMethod().toLowerCase().equals("post")) {

                System.out.println("method is POST");

                // create LoadRequest
                Headers requestHeaders = httpExchange.getRequestHeaders();
                InputStream requestBody = httpExchange.getRequestBody();
                String jsonInput = Handler.readString(requestBody);
                LoadRequest loadRequest = null;
                loadRequest = (LoadRequest) Serializer.deserialize(jsonInput, LoadRequest.class);

                // execute load and send successful result and OK key
                LoadResult successfulResult = LoadService.load(loadRequest);
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                String resultJson = successfulResult.serialize();
                OutputStream resultBody = httpExchange.getResponseBody();
                Handler.writeString(resultJson, resultBody);

                resultBody.close();

                success = true;
            }

            if (!success) {
                throw new DataAccessException("Load failed.");
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
