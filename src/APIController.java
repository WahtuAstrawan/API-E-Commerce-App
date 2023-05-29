import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.SQLException;

public class APIController {

    public static class UserHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                OutputStream outputStream = exchange.getResponseBody();
                exchange.getResponseHeaders().set("Content-Type", "application/json");

                try {
                    DatabaseManager.showData("users", "", outputStream, Main.connection, exchange);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

            }
            if ("POST".equals(exchange.getRequestMethod())) {
                OutputStream outputStream = exchange.getResponseBody();
                exchange.getResponseHeaders().set("Content-Type", "application/json");

                try {
                    DatabaseManager.addData("users", outputStream, Main.connection, exchange);

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static class UserByIdHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("DELETE".equals(exchange.getRequestMethod())) {
                OutputStream outputStream = exchange.getResponseBody();
                exchange.getResponseHeaders().set("Content-Type", "application/json");

                String requestURI = exchange.getRequestURI().toString();
                int id = getIdURI(requestURI);
                try {
                    DatabaseManager.deleteData("users", id, outputStream, Main.connection, exchange);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if("PUT".equals(exchange.getRequestMethod())){
                OutputStream outputStream = exchange.getResponseBody();
                exchange.getResponseHeaders().set("Content-Type", "application/json");

                String requestURI = exchange.getRequestURI().toString();
                int id = getIdURI(requestURI);
                try {
                    DatabaseManager.updateData("users", id, outputStream, Main.connection, exchange);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if("GET".equals(exchange.getRequestMethod())){
                OutputStream outputStream = exchange.getResponseBody();
                exchange.getResponseHeaders().set("Content-Type", "application/json");

            }
        }
    }

    public static class UserProductsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Logika untuk GET /users/{id}/products
        }
    }

    public static class UserOrdersHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Logika untuk GET /users/{id}/orders
        }
    }

    public static class UserReviewsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Logika untuk GET /users/{id}/reviews
        }
    }

    public static class OrderHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Logika untuk GET /orders/{id}
        }
    }

    public static class ProductHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Logika untuk GET /products
        }
    }

    public static class ProductDetailHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Logika untuk GET /products/{id}
        }
    }

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
