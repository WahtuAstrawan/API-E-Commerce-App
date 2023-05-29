import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.util.concurrent.Executors;

public class Main {
    public static Connection connection;

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 8071), 0);
        DatabaseManager databaseManager = new DatabaseManager();
        connection = databaseManager.connect();
        System.out.println("Berhasil connect ke database");

        server.createContext("/users", new APIController.UserHandler());
        server.createContext("/users/", new APIController.UserByIdHandler());
//        server.createContext("/users/{id}/products", new APIController.userProductsHandler());
//        server.createContext("/users/{id}/orders", new APIController.userOrdersHandler());
//        server.createContext("/users/{id}/reviews", new APIController.userReviewsHandler());
//        server.createContext("/orders/{id}", new APIController.orderHandler());
//        server.createContext("/products", new APIController.productHandler());
//        server.createContext("/products/{id}", new APIController.productDetailHandler());
        server.setExecutor(Executors.newFixedThreadPool(10));
        server.start();
        System.out.println("Server berjalan di : http://localhost:8071/");
    }
}
