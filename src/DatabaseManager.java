import java.io.IOException;
import java.io.OutputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

public class DatabaseManager {
    private Connection connection;

    public Connection connect() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\Asus Tuf\\OneDrive\\Desktop\\Code\\Java\\API-E-Commerce-App\\Database\\ecommerce.db");
        } catch (SQLException e) {
            System.out.println("Error untuk connect ke database : ");
            e.printStackTrace();
        }
        return connection;
    }

    public void stopConnect(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("Koneksi database ditutup");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void showData(String tabel, String kondisi, OutputStream outputStream, Connection connection, HttpExchange exchange) throws SQLException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        String sql = "SELECT * FROM " + tabel + " " + kondisi;
        if (tabel.equals("users")) {
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                List<User> userList = new ArrayList<>();
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    String email = resultSet.getString("email");
                    String phoneNumber = resultSet.getString("phone_number");
                    String type = resultSet.getString("type");
                    User user = new User(id, firstName, lastName, email, phoneNumber, type);
                    userList.add(user);
                }
                String json = objectMapper.writeValueAsString(userList);
                exchange.sendResponseHeaders(200, json.getBytes().length);
                outputStream.write(json.getBytes());
            }
        }
        exchange.getResponseBody().flush();
        outputStream.flush();
        outputStream.close();
    }

    public static void addData(String tabel, OutputStream outputStream, Connection connection, HttpExchange exchange) throws SQLException, IOException {
        String requestBody = APIController.getRequestBody(exchange);
        Gson gson = new Gson();

        if (tabel.equals("users")) {
            try {
                HashMap<Integer, String> validasiHash = getColumnData(tabel, "id, type", "");
                HashMap cekAtribut = gson.fromJson(requestBody, HashMap.class);

                if (cekAtribut.size() == 6) {
                    User user = gson.fromJson(requestBody, User.class);
                    boolean idValid = !validasiHash.containsKey(Integer.valueOf(user.getId()));
                    Integer id = user.getId();
                    String first_name = user.getFirst_name();
                    String last_name = user.getLast_name();
                    String email = user.getEmail();
                    String phone_number = user.getPhone_number();
                    String type = user.getType();
                    if (id != null && id > 0 && idValid && !(first_name.isEmpty()) && !(last_name.isEmpty()) && !(email.isEmpty()) && !(phone_number.isEmpty()) && !(type.isEmpty())) {
                        String sql = "INSERT INTO " + tabel + " (id, first_name, last_name, email, phone_number, type) VALUES (?, ?, ?, ?, ?, ?)";
                        PreparedStatement statement = connection.prepareStatement(sql);
                        statement.setInt(1, user.getId());
                        statement.setString(2, first_name);
                        statement.setString(3, last_name);
                        statement.setString(4, email);
                        statement.setString(5, phone_number);
                        statement.setString(6, type);
                        statement.executeUpdate();

                        String response = "Data berhasil ditambahkan ke database";
                        APIController.sendResponse(exchange, 200, outputStream, response);
                    } else if (id == null) {
                        String response = "Tidak terdapat id dari request body untuk membuat entitas users";
                        APIController.sendResponse(exchange, 400, outputStream, response);
                    } else if (id <= 0) {
                        String response = "Data id yang diberikan tidak boleh bernilai kurang atau sama dengan 0";
                        APIController.sendResponse(exchange, 400, outputStream, response);
                    } else if (first_name.isEmpty()) {
                        String response = "Tidak terdapat first_name request body untuk membuat dari entitas users";
                        APIController.sendResponse(exchange, 400, outputStream, response);
                    } else if (last_name.isEmpty()) {
                        String response = "Tidak terdapat last_name request body untuk membuat dari entitas users";
                        APIController.sendResponse(exchange, 400, outputStream, response);
                    } else if (email.isEmpty()) {
                        String response = "Tidak terdapat email dari request body untuk membuat entitas users";
                        APIController.sendResponse(exchange, 400, outputStream, response);
                    } else if (phone_number.isEmpty()) {
                        String response = "Tidak terdapat phone_number dari request body untuk membuat entitas users";
                        APIController.sendResponse(exchange, 400, outputStream, response);
                    } else if (type.isEmpty()) {
                        String response = "Tidak terdapat type dari request body untuk membuat entitas users";
                        APIController.sendResponse(exchange, 400, outputStream, response);
                    } else if (!idValid) {
                        String response = "Sudah terdapat data dengan id yang sama dari entitas users";
                        APIController.sendResponse(exchange, 400, outputStream, response);
                    } else {
                        String response = "Terdapat lebih dari 1 atribut yang bernilai null/kosong untuk membuat entitas users";
                        APIController.sendResponse(exchange, 400, outputStream, response);
                    }
                } else {
                    String response = "Data yang ada berikan tidak memiliki semua atribut atau melebihi atribut untuk membuat entitas users";
                    APIController.sendResponse(exchange, 400, outputStream, response);
                }

            } catch (Exception e) {
                String response = "Terjadi kesalahan yang tidak terduga: " + e.getMessage();
                APIController.sendResponse(exchange, 500, outputStream, response);
            }
        }
        exchange.getResponseBody().flush();
        outputStream.flush();
        outputStream.close();
    }

    public static void deleteData(String tabel, int id, OutputStream outputStream, Connection connection, HttpExchange exchange) throws SQLException, IOException {
        HashMap<Integer, String> validasiHash = getColumnData(tabel, "id, type", "");
        boolean idValid = validasiHash.containsKey(Integer.valueOf(id));
        if (idValid) {
            String sql = "DELETE FROM " + tabel + " WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);
                statement.executeUpdate();
            }
            String response = "Berhasil menghapus data users dengan ID : " + id;
            APIController.sendResponse(exchange, 200, outputStream, response);
        } else {
            String response = "Tidak terdapat data user dengan ID : " + id;
            APIController.sendResponse(exchange, 400, outputStream, response);
        }
    }

    public static void updateData(String tabel, int id, OutputStream outputStream, Connection connection, HttpExchange exchange) throws SQLException, IOException {
        String requestBody = APIController.getRequestBody(exchange);
        Gson gson = new Gson();
        HashMap<Integer, String> validasiHash = getColumnData(tabel, "id, type", "");
        boolean idValid = validasiHash.containsKey(Integer.valueOf(id));
        HashMap requestHash = gson.fromJson(requestBody, HashMap.class);

        try {
            if (idValid) {
                if (requestHash.size() > 0) {
                    String sql = sqlUpdateStringGenerator(requestHash, id, tabel);
                    try (PreparedStatement statement = connection.prepareStatement(sql)) {
                        statement.executeUpdate();
                    }
                    String response = "Berhasil mengupdate data users dengan ID : " + id;
                    APIController.sendResponse(exchange, 200, outputStream, response);
                } else {
                    String response = "Request body tidak boleh kosong";
                    APIController.sendResponse(exchange, 400, outputStream, response);
                }
            } else {
                String response = "Tidak terdapat data user dengan ID : " + id;
                APIController.sendResponse(exchange, 400, outputStream, response);
            }

        } catch (Exception e) {
            String response = "Terjadi kesalahan yang tidak terduga : " + e.getMessage();
            APIController.sendResponse(exchange, 500, outputStream, response);
        }
    }

    public static HashMap<Integer, String> getColumnData(String tabel, String kolom, String kondisi) throws SQLException {
        HashMap<Integer, String> dataList = new HashMap<>();

        String sql = "SELECT " + kolom + " FROM " + tabel + " " + kondisi;
        try (Statement statement = Main.connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                if (tabel.equals("users")) {
                    int nilaiId = resultSet.getInt("id");
                    Integer id = nilaiId;
                    String type = resultSet.getString("type");
                    dataList.put(id, type);
                }
            }
        }
        return dataList;
    }

    public static String sqlUpdateStringGenerator(HashMap requestBody, int id, String tabel) {
        String sql = null;
        if (tabel.equals("users")) {
            String setUpdate = "SET ";
            int banyakAtribut = 0;
            boolean[] arrayAtribut = {false, false, false, false, false, false};
            if (requestBody.containsKey("id")) arrayAtribut[0] = true;
            if (requestBody.containsKey("first_name")) arrayAtribut[1] = true;
            if (requestBody.containsKey("last_name")) arrayAtribut[2] = true;
            if (requestBody.containsKey("email")) arrayAtribut[3] = true;
            if (requestBody.containsKey("phone_number")) arrayAtribut[4] = true;
            if (requestBody.containsKey("type")) arrayAtribut[5] = true;

            for (int i = 0; i <= 5; i++) {
                if (arrayAtribut[i] && i == 0) {
                    setUpdate += String.format("id = %.0f", requestBody.get("id"));
                    banyakAtribut++;
                }
                if (arrayAtribut[i] && i == 1) {
                    if (banyakAtribut > 0) {
                        setUpdate += String.format(",first_name = \"%s\"", requestBody.get("first_name"));
                        banyakAtribut++;
                    } else if (banyakAtribut == 0) {
                        setUpdate += String.format("first_name = \"%s\"", requestBody.get("first_name"));
                        banyakAtribut++;
                    }
                }
                if (arrayAtribut[i] && i == 2) {
                    if (banyakAtribut > 0) {
                        setUpdate += String.format(",last_name = \"%s\"", requestBody.get("last_name"));
                        banyakAtribut++;
                    } else if (banyakAtribut == 0) {
                        setUpdate += String.format("last_name = \"%s\"", requestBody.get("last_name"));
                        banyakAtribut++;
                    }
                }
                if (arrayAtribut[i] && i == 3) {
                    if (banyakAtribut > 0) {
                        setUpdate += String.format(",email = \"%s\"", requestBody.get("email"));
                        banyakAtribut++;
                    } else if (banyakAtribut == 0) {
                        setUpdate += String.format("email = \"%s\"", requestBody.get("email"));
                        banyakAtribut++;
                    }
                }
                if (arrayAtribut[i] && i == 4) {
                    if (banyakAtribut > 0) {
                        setUpdate += String.format(",phone_number = \"%s\"", requestBody.get("phone_number"));
                        banyakAtribut++;
                    } else if (banyakAtribut == 0) {
                        setUpdate += String.format("phone_number = \"%s\"", requestBody.get("phone_number"));
                        banyakAtribut++;
                    }
                }
                if (arrayAtribut[i] && i == 5) {
                    if (banyakAtribut > 0) {
                        setUpdate += String.format(",type = \"%s\"", requestBody.get("type"));
                        banyakAtribut++;
                    } else if (banyakAtribut == 0) {
                        setUpdate += String.format("type = \"%s\"", requestBody.get("type"));
                        banyakAtribut++;
                    }
                }
            }
            sql = "UPDATE " + tabel + " " + setUpdate + " WHERE id = " + id;
        }
        return sql;
    }

    public static String sqlInnerJoinStringGenerator(HashMap<String, String> tabelKolom) {
        String sql = "";
        return "";
    }
}
