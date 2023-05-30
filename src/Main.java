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

        server.createContext("/users", new Handler.UserHandler());
        server.createContext("/users/", new Handler.UserByIdHandler());
        server.createContext("/addresses", new Handler.AddressesHandler());
        server.createContext("/addresses/", new Handler.AddressesHandler());
        server.createContext("/orders/", new Handler.OrderByIdHandler());
        server.createContext("/products", new Handler.ProductHandler());
        server.createContext("/products/", new Handler.ProductByIdHandler());

        server.setExecutor(Executors.newFixedThreadPool(10));
        server.start();
        System.out.println("Server berjalan di : http://localhost:8071/");
    }
}
