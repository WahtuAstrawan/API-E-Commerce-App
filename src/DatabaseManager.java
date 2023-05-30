import java.io.IOException;
import java.io.OutputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import org.json.JSONArray;
import org.json.JSONObject;

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

    public static void showData(String tabel, String kondisi, Integer idRoute, OutputStream outputStream, Connection connection, HttpExchange exchange) throws SQLException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        String sqlSemua = "SELECT * FROM " + tabel;
        if (tabel.equals("users")) {
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sqlSemua)) {

                ArrayList<User> userList = new ArrayList<>();
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
        if (tabel.equals("products")) {
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sqlSemua)) {

                ArrayList<Product> productList = new ArrayList<>();
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    int idSeller = resultSet.getInt("id_seller");
                    String title = resultSet.getString("title");
                    String description = resultSet.getString("description");
                    String price = resultSet.getString("price");
                    int stock = resultSet.getInt("stock");
                    Product product = new Product(id, idSeller, title, description, price, stock);
                    productList.add(product);
                }
                String json = objectMapper.writeValueAsString(productList);
                exchange.sendResponseHeaders(200, json.getBytes().length);
                outputStream.write(json.getBytes());
            }
        }
        if (tabel.equals("users, addresses")) {
            HashMap<Integer, String> validasiHash = QueryUtil.getColumnData("users", "id, type", "");
            boolean idValid = validasiHash.containsKey(idRoute);
            if (idValid) {
                ArrayList<String> arrayTabel = new ArrayList<>();
                ArrayList<String> arrayForeignKey = new ArrayList<>();
                arrayTabel.add("users");
                arrayTabel.add("addresses");
                arrayForeignKey.add("id");
                arrayForeignKey.add("id_user");
                String sql = QueryUtil.sqlJoinStringGenerator(arrayTabel, arrayForeignKey, idRoute);
                try (Statement statement = connection.createStatement();
                     ResultSet resultSet = statement.executeQuery(sql)) {

                    int id = resultSet.getInt("id");
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    String email = resultSet.getString("email");
                    String phoneNumber = resultSet.getString("phone_number");
                    String typeU = resultSet.getString("type");
                    User user = new User(id, firstName, lastName, email, phoneNumber, typeU);

                    while (resultSet.next()) {
                        int idUser = resultSet.getInt("id_user");
                        String type = resultSet.getString("type");
                        String line1 = resultSet.getString("line1");
                        String line2 = resultSet.getString("line2");
                        String city = resultSet.getString("city");
                        String province = resultSet.getString("province");
                        String postcode = resultSet.getString("postcode");
                        Address address = new Address(idUser, type, line1, line2, city, province, postcode);
                        if (idUser != 0) {
                            user.addAddress(address);
                        }
                    }
                    String json = objectMapper.writeValueAsString(user);
                    exchange.sendResponseHeaders(200, json.getBytes().length);
                    outputStream.write(json.getBytes());
                }
            } else {
                String response = "Tidak terdapat data user dengan ID : " + idRoute;
                ServerUtil.sendResponse(exchange, 400, outputStream, response);
            }
        }
        if (tabel.equals("products, users")) {
            HashMap<Integer, String> validasiHash = QueryUtil.getColumnData("products", "id_seller, id", "");
            boolean idValid = validasiHash.containsKey(idRoute);
            if (idValid) {
                ArrayList<String> arrayTabel = new ArrayList<>();
                ArrayList<String> arrayForeignKey = new ArrayList<>();
                arrayTabel.add("products");
                arrayTabel.add("users");
                arrayForeignKey.add("id_seller");
                arrayForeignKey.add("id");
                System.out.println("Halo product id");

                String sql = QueryUtil.sqlJoinStringGenerator(arrayTabel, arrayForeignKey, idRoute);
                try (Statement statement = connection.createStatement();
                     ResultSet resultSet = statement.executeQuery(sql)) {

                    int idS = resultSet.getInt("id");
                    int idSeller = resultSet.getInt("id_seller");
                    String title = resultSet.getString("title");
                    String description = resultSet.getString("description");
                    String price = resultSet.getString("price");
                    int stock = resultSet.getInt("stock");
                    Product product = new Product(idS, idSeller, title, description, price, stock);

                    int id = resultSet.getInt("id");
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");
                    String email = resultSet.getString("email");
                    String phoneNumber = resultSet.getString("phone_number");
                    String typeU = resultSet.getString("type");
                    User user = new User(id, firstName, lastName, email, phoneNumber, typeU);

                    String json = objectMapper.writeValueAsString(product);
                    String json2 = objectMapper.writeValueAsString(user);
                    String gabungan = json + json2;

                    exchange.sendResponseHeaders(200, gabungan.getBytes().length);
                    outputStream.write(gabungan.getBytes());
                }
            } else {
                String response = "Tidak terdapat data product dengan ID : " + idRoute;
                ServerUtil.sendResponse(exchange, 400, outputStream, response);
            }
        }
        if (tabel.equals("products")) {
            sqlSemua += " WHERE id_seller = " + idRoute;
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sqlSemua)) {

                ArrayList<Product> productList = new ArrayList<>();
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    int idSeller = resultSet.getInt("id_seller");
                    String title = resultSet.getString("title");
                    String description = resultSet.getString("description");
                    String price = resultSet.getString("price");
                    int stock = resultSet.getInt("stock");
                    Product product = new Product(id, idSeller, title, description, price, stock);
                    productList.add(product);
                }
                String json = objectMapper.writeValueAsString(productList);
                exchange.sendResponseHeaders(200, json.getBytes().length);
                outputStream.write(json.getBytes());
            }
        }
        if (tabel.equals("orders")) {
            sqlSemua += " WHERE id_buyer = " + idRoute;
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sqlSemua)) {

                ArrayList<Order> orderList = new ArrayList<>();
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    int idBuyer = resultSet.getInt("id_buyer");
                    int note = resultSet.getInt("note");
                    int total = resultSet.getInt("total");
                    int discount = resultSet.getInt("discount");
                    boolean isPaid = resultSet.getBoolean("is_paid");
                    Order order = new Order(id, idBuyer, note, total, discount, isPaid);
                    orderList.add(order);
                }
                String json = objectMapper.writeValueAsString(orderList);
                exchange.sendResponseHeaders(200, json.getBytes().length);
                outputStream.write(json.getBytes());
            }
        }
        if (tabel.equals("reviews")) {
            sqlSemua += " WHERE id_order = " + idRoute;
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sqlSemua)) {

                int idOrder = resultSet.getInt("id_order");
                int star = resultSet.getInt("star");
                String description = resultSet.getString("description");
                Review review = new Review(idOrder, star, description);

                String json = objectMapper.writeValueAsString(review);
                exchange.sendResponseHeaders(200, json.getBytes().length);
                outputStream.write(json.getBytes());
            }
        }
        if (tabel.equals("orders, users, order_details, reviews, products")) {
            HashMap<Integer, String> validasiHash = QueryUtil.getColumnData("orders", "id, id_buyer", "");
            boolean idValid = validasiHash.containsKey(idRoute);
            JSONArray jsonArray = new JSONArray();
            String queryJoin1 = "SELECT * FROM orders INNER JOIN users ON orders.id_buyer=users.id WHERE orders.id =" + idRoute;
            String queryJoin2 = "SELECT * FROM order_details INNER JOIN products ON products.id=order_details.id_product WHERE order_details.id_order =" + idRoute;
            String queryJoin3 = "SELECT * FROM reviews WHERE reviews.id_order =" + idRoute;
            if (idValid) {
                try {
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(queryJoin1);
                    while (resultSet.next()) {
                        JSONObject jsonOrder = new JSONObject();
                        jsonOrder.put("id", resultSet.getInt("id"));
                        jsonOrder.put("id_buyer", resultSet.getInt("id_buyer"));
                        jsonOrder.put("note", resultSet.getInt("note"));
                        jsonOrder.put("total", resultSet.getInt("total"));
                        jsonOrder.put("discount", resultSet.getInt("discount"));
                        jsonOrder.put("is_paid", resultSet.getString("is_paid"));
                        JSONArray jsonDetail = new JSONArray();
                        try {
                            Statement statementJoin2 = connection.createStatement();
                            ResultSet resultSetJoin2 = statementJoin2.executeQuery(queryJoin2);
                            while (resultSetJoin2.next()) {
                                JSONObject jsonOrderDetail = new JSONObject();
                                jsonOrderDetail.put("title", resultSetJoin2.getString("title"));
                                jsonOrderDetail.put("quantity", resultSetJoin2.getInt("quantity"));
                                jsonOrderDetail.put("price", resultSetJoin2.getInt("price"));
                                jsonDetail.put(jsonOrderDetail);
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        jsonOrder.put("order_detail", jsonDetail);
                        jsonArray.put(jsonOrder);
                        JSONArray jsonReviewArray = new JSONArray();
                        try {
                            Statement statementJoin3 = connection.createStatement();
                            ResultSet resultSetJoin3 = statementJoin3.executeQuery(queryJoin3);
                            while (resultSetJoin3.next()) {
                                JSONObject jsonReview = new JSONObject();
                                jsonReview.put("star", resultSetJoin3.getInt("star"));
                                jsonReview.put("description", resultSetJoin3.getString("description"));
                                jsonReviewArray.put(jsonReview);
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        jsonOrder.put("reviews", jsonReviewArray);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                String json = jsonArray.toString();
                exchange.sendResponseHeaders(200, json.getBytes().length);
                outputStream.write(json.getBytes());
            }
            String response = "Tidak terdapat data order dengan ID : " + idRoute;
            ServerUtil.sendResponse(exchange, 400, outputStream, response);
        }
        exchange.getResponseBody().flush();
        outputStream.flush();
        outputStream.close();
    }


    public static void addData(String tabel, OutputStream outputStream, Connection connection, HttpExchange exchange) throws SQLException, IOException {
        String requestBody = ServerUtil.getRequestBody(exchange);
        Gson gson = new Gson();

        if (tabel.equals("users")) {
            try {
                HashMap<Integer, String> validasiHash = QueryUtil.getColumnData(tabel, "id, type", "");
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
                        ServerUtil.sendResponse(exchange, 200, outputStream, response);
                    } else if (id == null) {
                        String response = "Tidak terdapat id dari request body untuk membuat entitas users";
                        ServerUtil.sendResponse(exchange, 400, outputStream, response);
                    } else if (id <= 0) {
                        String response = "Data id yang diberikan tidak boleh bernilai kurang atau sama dengan 0";
                        ServerUtil.sendResponse(exchange, 400, outputStream, response);
                    } else if (first_name.isEmpty()) {
                        String response = "Tidak terdapat first_name request body untuk membuat dari entitas users";
                        ServerUtil.sendResponse(exchange, 400, outputStream, response);
                    } else if (last_name.isEmpty()) {
                        String response = "Tidak terdapat last_name request body untuk membuat dari entitas users";
                        ServerUtil.sendResponse(exchange, 400, outputStream, response);
                    } else if (email.isEmpty()) {
                        String response = "Tidak terdapat email dari request body untuk membuat entitas users";
                        ServerUtil.sendResponse(exchange, 400, outputStream, response);
                    } else if (phone_number.isEmpty()) {
                        String response = "Tidak terdapat phone_number dari request body untuk membuat entitas users";
                        ServerUtil.sendResponse(exchange, 400, outputStream, response);
                    } else if (type.isEmpty()) {
                        String response = "Tidak terdapat type dari request body untuk membuat entitas users";
                        ServerUtil.sendResponse(exchange, 400, outputStream, response);
                    } else if (!idValid) {
                        String response = "Sudah terdapat data dengan id yang sama dari entitas users";
                        ServerUtil.sendResponse(exchange, 400, outputStream, response);
                    } else {
                        String response = "Terdapat lebih dari 1 atribut yang bernilai null/kosong untuk membuat entitas users";
                        ServerUtil.sendResponse(exchange, 400, outputStream, response);
                    }
                } else {
                    String response = "Data yang ada berikan tidak memiliki semua atribut atau melebihi atribut untuk membuat entitas users";
                    ServerUtil.sendResponse(exchange, 400, outputStream, response);
                }

            } catch (Exception e) {
                String response = "Terjadi kesalahan yang tidak terduga: " + e.getMessage();
                ServerUtil.sendResponse(exchange, 500, outputStream, response);
            }
        }
        if (tabel.equals("addresses")) {
            try {
                HashMap<Integer, String> validasiHash = QueryUtil.getColumnData("users", "id, type", "");
                HashMap cekAtribut = gson.fromJson(requestBody, HashMap.class);

                if (cekAtribut.size() == 7) {
                    Address address = gson.fromJson(requestBody, Address.class);
                    boolean idValid = (validasiHash.containsKey(address.getId_user()));
                    boolean typeValid = (validasiHash.get(address.getId_user()).equals(address.getType()));
                    Integer id_user = address.getId_user();
                    String type = address.getType();
                    String line1 = address.getLine1();
                    String line2 = address.getLine2();
                    String city = address.getCity();
                    String province = address.getProvince();
                    String postcode = address.getPostcode();
                    if (typeValid && id_user != null && id_user > 0 && idValid && !(line1.isEmpty()) && !(line2.isEmpty()) && !(city.isEmpty()) && !(province.isEmpty()) && !(type.isEmpty()) && !(postcode.isEmpty())) {
                        String sql = "INSERT INTO " + tabel + " (id_user, type, line1, line2, city, province, postcode) VALUES (?, ?, ?, ?, ?, ?, ?)";
                        PreparedStatement statement = connection.prepareStatement(sql);
                        statement.setInt(1, address.getId_user());
                        statement.setString(2, type);
                        statement.setString(3, line1);
                        statement.setString(4, line2);
                        statement.setString(5, city);
                        statement.setString(6, province);
                        statement.setString(7, postcode);
                        statement.executeUpdate();

                        String response = "Data berhasil ditambahkan ke database";
                        ServerUtil.sendResponse(exchange, 200, outputStream, response);
                    } else if (id_user == null) {
                        String response = "Tidak terdapat id_user dari request body untuk membuat entitas addresses";
                        ServerUtil.sendResponse(exchange, 400, outputStream, response);
                    } else if (id_user <= 0) {
                        String response = "Data id_user yang diberikan tidak boleh bernilai kurang atau sama dengan 0";
                        ServerUtil.sendResponse(exchange, 400, outputStream, response);
                    } else if (line1.isEmpty()) {
                        String response = "line1 pada request body tidak boleh kosong untuk membuat entitas dari addresses";
                        ServerUtil.sendResponse(exchange, 400, outputStream, response);
                    } else if (line2.isEmpty()) {
                        String response = "line2 pada request body tidak boleh kosong untuk membuat entitas dari addresses";
                        ServerUtil.sendResponse(exchange, 400, outputStream, response);
                    } else if (city.isEmpty()) {
                        String response = "city pada request body tidak boleh kosong untuk membuat entitas dari addresses";
                        ServerUtil.sendResponse(exchange, 400, outputStream, response);
                    } else if (province.isEmpty()) {
                        String response = "province pada request body tidak boleh kosong untuk membuat entitas dari addresses";
                        ServerUtil.sendResponse(exchange, 400, outputStream, response);
                    } else if (type.isEmpty()) {
                        String response = "type pada request body tidak boleh kosong untuk membuat entitas dari addresses";
                        ServerUtil.sendResponse(exchange, 400, outputStream, response);
                    } else if (!idValid) {
                        String response = "id_user yang anda masukkan tidak ada pada data id di tabel users";
                        ServerUtil.sendResponse(exchange, 400, outputStream, response);
                    } else if (!typeValid) {
                        String response = "type yang anda masukkan tidak sama pada data type di tabel users dengan id_user yang anda masukkan";
                        ServerUtil.sendResponse(exchange, 400, outputStream, response);
                    } else {
                        String response = "Terdapat lebih dari 1 atribut yang bernilai null untuk membuat entitas addresses";
                        ServerUtil.sendResponse(exchange, 400, outputStream, response);
                    }
                } else {
                    String response = "Data yang ada berikan tidak memiliki semua atribut atau melebihi atribut untuk membuat entitas addresses";
                    ServerUtil.sendResponse(exchange, 400, outputStream, response);
                }

            } catch (Exception e) {
                String response = "Terjadi kesalahan yang tidak terduga: " + e.getMessage();
                ServerUtil.sendResponse(exchange, 500, outputStream, response);
            }
        }
        exchange.getResponseBody().flush();
        outputStream.flush();
        outputStream.close();
    }

    public static void deleteData(String tabel, int idRoute, String namaKolom, String namaKolomId, String kondisi, OutputStream outputStream, Connection connection, HttpExchange exchange) throws SQLException, IOException {
        HashMap<Integer, String> validasiHash = QueryUtil.getColumnData(tabel, namaKolom, kondisi);
        boolean idValid = validasiHash.containsKey(Integer.valueOf(idRoute));
        try {
            if (idValid) {
                String sql = "DELETE FROM " + tabel + " WHERE " + namaKolomId + " = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setInt(1, idRoute);
                    statement.executeUpdate();
                }
                String response = "Berhasil menghapus data " + tabel + " dengan ID : " + idRoute;
                ServerUtil.sendResponse(exchange, 200, outputStream, response);
            } else {
                String response = "Tidak terdapat data " + tabel + " dengan ID : " + idRoute;
                ServerUtil.sendResponse(exchange, 400, outputStream, response);
            }
        } catch (Exception e) {
            String response = "Terjadi kesalahan yang tidak terduga : " + e.getMessage();
            ServerUtil.sendResponse(exchange, 500, outputStream, response);
        }
    }

    public static void updateData(String tabel, int id, OutputStream outputStream, Connection connection, HttpExchange exchange) throws SQLException, IOException {
        String requestBody = ServerUtil.getRequestBody(exchange);
        Gson gson = new Gson();
        boolean idValid = false;
        try {
            if (tabel.equals("users")) {
                HashMap<Integer, String> validasiHash = QueryUtil.getColumnData(tabel, "id, type", "");
                idValid = validasiHash.containsKey(Integer.valueOf(id));
            } else if (tabel.equals("addresses")) {
                HashMap<Integer, String> validasiHash = QueryUtil.getColumnData(tabel, "id_user, type", "");
                idValid = validasiHash.containsKey(Integer.valueOf(id));
            }
            HashMap requestHash = gson.fromJson(requestBody, HashMap.class);
            if (idValid) {
                if (requestHash.size() > 0) {
                    String sql = QueryUtil.sqlUpdateStringGenerator(requestHash, id, tabel);
                    try (PreparedStatement statement = connection.prepareStatement(sql)) {
                        statement.executeUpdate();
                    }
                    String response = "Berhasil mengupdate data " + tabel + " dengan ID : " + id;
                    ServerUtil.sendResponse(exchange, 200, outputStream, response);
                } else {
                    String response = "Request body tidak boleh kosong";
                    ServerUtil.sendResponse(exchange, 400, outputStream, response);
                }
            } else {
                String response = "Tidak terdapat data " + tabel + " dengan ID : " + id;
                ServerUtil.sendResponse(exchange, 400, outputStream, response);
            }

        } catch (Exception e) {
            String response = "Terjadi kesalahan yang tidak terduga : " + e.getMessage();
            ServerUtil.sendResponse(exchange, 500, outputStream, response);
        }
    }
}