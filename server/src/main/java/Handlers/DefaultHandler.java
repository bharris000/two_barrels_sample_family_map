package Handlers;

import Result.ErrorResult;
import com.sun.net.httpserver.HttpExchange;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DefaultHandler extends Handler {

    /**
     * A default handler, checks for 404 errors and checks for /.
     *
     * @param httpExchange exchange
     * @throws IOException
     */
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        try {
            String uri = httpExchange.getRequestURI().toString();
            Path filePath;

            // default (when it's just '/')
            if (uri.equals("/")) {
                String filePathStr = "web/index.html";
                String path = Paths.get("").toAbsolutePath().toString() + filePathStr;
                filePath = FileSystems.getDefault().getPath(filePathStr);

                // send OK key
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                OutputStream httpResponseBody = httpExchange.getResponseBody();
                Files.copy(filePath, httpResponseBody);
                httpResponseBody.close();
            }
            else {
                // file cannot easily be found
                filePath = FileSystems.getDefault().getPath("web" + uri);

                // try to find
                String path = filePath.toString();
                System.out.println(path);
                File temp = new File(path);

                System.out.println("File " + path + " exists: " + temp.exists());

                if (!temp.exists()) {
                    //does not exist, give 404 page
                    String notFoundFile = "web/HTML/404.html";
                    filePath = FileSystems.getDefault().getPath(notFoundFile);

                    // send NOT FOUND key
                    httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
                    OutputStream httpResponseBody = httpExchange.getResponseBody();
                    Files.copy(filePath, httpResponseBody);
                    httpResponseBody.close();
                }
                else {
                    // send OK key
                    httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    OutputStream httpResponseBody = httpExchange.getResponseBody();
                    Files.copy(filePath, httpResponseBody);
                    httpResponseBody.close();
                }
            }
        }
        catch (Exception e) {

            // send NOT FOUND key and ErrorResult
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
            ErrorResult errorResult = new ErrorResult(e.getMessage());
            String errorJson = errorResult.serialize();
            OutputStream resultBody = httpExchange.getResponseBody();
            Handler.writeString(errorJson, resultBody);

            resultBody.close();
        }
    }

}
