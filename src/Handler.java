import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;

public class Handler {

    public static class UserHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                OutputStream outputStream = exchange.getResponseBody();
                exchange.getResponseHeaders().set("Content-Type", "application/json");

                try {
                    DatabaseManager.showData("users", "", null, outputStream, Main.connection, exchange);
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
            String requestURIA = exchange.getRequestURI().toString();
            String[] pathParts = requestURIA.split("/");
            String tabel = "";
            if (pathParts.length > 3) {
                tabel = ServerUtil.parseParamsTable(requestURIA);
            }

            if ("GET".equals(exchange.getRequestMethod()) && tabel.equals("products")) {
                OutputStream outputStream = exchange.getResponseBody();
                exchange.getResponseHeaders().set("Content-Type", "application/json");

                String requestURI = exchange.getRequestURI().toString();
                int id = ServerUtil.getIdURI(requestURI);
                try {
                    DatabaseManager.showData("products", "", id, outputStream, Main.connection, exchange);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if ("GET".equals(exchange.getRequestMethod()) && tabel.equals("orders")) {
                OutputStream outputStream = exchange.getResponseBody();
                exchange.getResponseHeaders().set("Content-Type", "application/json");

                String requestURI = exchange.getRequestURI().toString();
                int id = ServerUtil.getIdURI(requestURI);
                try {
                    DatabaseManager.showData("orders", "", id, outputStream, Main.connection, exchange);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if ("GET".equals(exchange.getRequestMethod()) && tabel.equals("reviews")) {
                OutputStream outputStream = exchange.getResponseBody();
                exchange.getResponseHeaders().set("Content-Type", "application/json");

                String requestURI = exchange.getRequestURI().toString();
                int id = ServerUtil.getIdURI(requestURI);
                try {
                    DatabaseManager.showData("reviews", "", id, outputStream, Main.connection, exchange);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if ("DELETE".equals(exchange.getRequestMethod()) && tabel.equals("")) {
                OutputStream outputStream = exchange.getResponseBody();
                exchange.getResponseHeaders().set("Content-Type", "application/json");

                String requestURI = exchange.getRequestURI().toString();
                int id = ServerUtil.getIdURI(requestURI);
                try {
                    DatabaseManager.deleteData("users", id, "id, type", "id", "", outputStream, Main.connection, exchange);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if ("PUT".equals(exchange.getRequestMethod()) && tabel.equals("")) {
                OutputStream outputStream = exchange.getResponseBody();
                exchange.getResponseHeaders().set("Content-Type", "application/json");

                String requestURI = exchange.getRequestURI().toString();
                int id = ServerUtil.getIdURI(requestURI);
                try {
                    DatabaseManager.updateData("users", id, outputStream, Main.connection, exchange);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if ("GET".equals(exchange.getRequestMethod()) && tabel.equals("")) {
                OutputStream outputStream = exchange.getResponseBody();
                exchange.getResponseHeaders().set("Content-Type", "application/json");

                String requestURI = exchange.getRequestURI().toString();
                int id = ServerUtil.getIdURI(requestURI);
                try {
                    DatabaseManager.showData("users, addresses", "", id, outputStream, Main.connection, exchange);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }

    public static class AddressesHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equals(exchange.getRequestMethod())) {
                OutputStream outputStream = exchange.getResponseBody();
                exchange.getResponseHeaders().set("Content-Type", "application/json");

                try {
                    DatabaseManager.addData("addresses", outputStream, Main.connection, exchange);

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if ("PUT".equals(exchange.getRequestMethod())) {
                OutputStream outputStream = exchange.getResponseBody();
                exchange.getResponseHeaders().set("Content-Type", "application/json");

                String requestURI = exchange.getRequestURI().toString();
                int id = ServerUtil.getIdURI(requestURI);
                try {
                    DatabaseManager.updateData("addresses", id, outputStream, Main.connection, exchange);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if ("DELETE".equals(exchange.getRequestMethod())) {
                OutputStream outputStream = exchange.getResponseBody();
                exchange.getResponseHeaders().set("Content-Type", "application/json");

                String requestURI = exchange.getRequestURI().toString();
                int id = ServerUtil.getIdURI(requestURI);
                try {
                    DatabaseManager.deleteData("addresses", id, "id_user, type", "id_user", "", outputStream, Main.connection, exchange);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static class OrderByIdHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                OutputStream outputStream = exchange.getResponseBody();
                exchange.getResponseHeaders().set("Content-Type", "application/json");

                String requestURI = exchange.getRequestURI().toString();
                int id = ServerUtil.getIdURI(requestURI);
                try {
                    DatabaseManager.showData("orders, users, order_details, reviews, products", "", id, outputStream, Main.connection, exchange);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static class ProductHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                OutputStream outputStream = exchange.getResponseBody();
                exchange.getResponseHeaders().set("Content-Type", "application/json");

                try {
                    DatabaseManager.showData("products", "", null, outputStream, Main.connection, exchange);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static class ProductByIdHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equals(exchange.getRequestMethod())) {
                OutputStream outputStream = exchange.getResponseBody();
                exchange.getResponseHeaders().set("Content-Type", "application/json");

                String requestURI = exchange.getRequestURI().toString();
                int id = ServerUtil.getIdURI(requestURI);
                try {
                    DatabaseManager.showData("products, users", "", id, outputStream, Main.connection, exchange);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
