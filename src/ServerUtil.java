import com.sun.net.httpserver.HttpExchange;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class ServerUtil {
    public static String getRequestBody(HttpExchange exchange) throws IOException {
        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody());
        BufferedReader br = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        isr.close();
        return sb.toString();
    }

    public static String parseParamsTable(String requestURI) {
        String[] pathParts = requestURI.split("/");
        if (pathParts.length >= 3) {
            return pathParts[3];
        }
        return "";
    }

    public static void sendResponse(HttpExchange exchange, int statusCode, OutputStream outputStream, String Response) throws IOException {
        JSONObject json = new JSONObject();
        json.put("message", Response);
        String jsonString = json.toString();
        exchange.sendResponseHeaders(statusCode, jsonString.getBytes().length);
        outputStream.write(jsonString.getBytes());
        outputStream.close();
    }

    public static int getIdURI(String requestURI) {
        String[] pathParts = requestURI.split("/");
        String idString = "";

        for (String part : pathParts) {
            if (part.matches("\\d+")) {
                idString += part;
            }
        }

        int id = Integer.parseInt(idString);
        return id;
    }
}
