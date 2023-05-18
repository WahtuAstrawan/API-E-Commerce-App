import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\Asus Tuf\\OneDrive\\Desktop\\Code\\Java\\API-E-Commerce-App\\Database\\ecommerce.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        int port = 8071;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("HTTP Server berjalan pada port " + port);
            System.out.println("Kunjungi pada : http://localhost:8071/ ");


            while (true) {
                Socket clientSocket = serverSocket.accept();
                Thread thread = new Thread(() -> handleRequest(clientSocket));
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleRequest(Socket clientSocket) {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                OutputStream out = clientSocket.getOutputStream()
        ) {
            StringBuilder requestBuilder = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null && !line.isEmpty()) {
                requestBuilder.append(line).append("\r\n");
            }

            String request = requestBuilder.toString();

            String response = "HTTP/1.1 200 OK\r\n"
                    + "Content-Type: text/plain\r\n"
                    + "\r\n"
                    + "Hello, Bro!";

            out.write(response.getBytes());
            out.flush();

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}