import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class QueryUtil {
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
                if (tabel.equals("addresses")) {
                    int nilaiId = resultSet.getInt("id_user");
                    Integer id = nilaiId;
                    String type = resultSet.getString("type");
                    dataList.put(id, type);
                }
                if (tabel.equals("orders")) {
                    int nilaiId = resultSet.getInt("id");
                    Integer id = nilaiId;
                    String idBuyer = resultSet.getString("id_buyer");
                    dataList.put(id, idBuyer);
                }
                if (tabel.equals("products")) {
                    int idSeller = resultSet.getInt("id_seller");
                    Integer id = idSeller;
                    String nilaiId = resultSet.getString("id");
                    dataList.put(id, nilaiId);
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
        if (tabel.equals("addresses")) {
            String setUpdate = "SET ";
            int banyakAtribut = 0;
            boolean[] arrayAtribut = {false, false, false, false, false, false, false};
            if (requestBody.containsKey("id_user")) arrayAtribut[0] = true;
            if (requestBody.containsKey("type")) arrayAtribut[1] = true;
            if (requestBody.containsKey("line1")) arrayAtribut[2] = true;
            if (requestBody.containsKey("line2")) arrayAtribut[3] = true;
            if (requestBody.containsKey("city")) arrayAtribut[4] = true;
            if (requestBody.containsKey("province")) arrayAtribut[5] = true;
            if (requestBody.containsKey("postcode")) arrayAtribut[6] = true;

            for (int i = 0; i <= 6; i++) {
                if (arrayAtribut[i] && i == 0) {
                    setUpdate += String.format("id_user = %.0f", requestBody.get("id_user"));
                    banyakAtribut++;
                }
                if (arrayAtribut[i] && i == 1) {
                    if (banyakAtribut > 0) {
                        setUpdate += String.format(",type = \"%s\"", requestBody.get("type"));
                        banyakAtribut++;
                    } else if (banyakAtribut == 0) {
                        setUpdate += String.format("type = \"%s\"", requestBody.get("type"));
                        banyakAtribut++;
                    }
                }
                if (arrayAtribut[i] && i == 2) {
                    if (banyakAtribut > 0) {
                        setUpdate += String.format(",line1 = \"%s\"", requestBody.get("line1"));
                        banyakAtribut++;
                    } else if (banyakAtribut == 0) {
                        setUpdate += String.format("line1 = \"%s\"", requestBody.get("line1"));
                        banyakAtribut++;
                    }
                }
                if (arrayAtribut[i] && i == 3) {
                    if (banyakAtribut > 0) {
                        setUpdate += String.format(",line2 = \"%s\"", requestBody.get("line2"));
                        banyakAtribut++;
                    } else if (banyakAtribut == 0) {
                        setUpdate += String.format("line2 = \"%s\"", requestBody.get("line2"));
                        banyakAtribut++;
                    }
                }
                if (arrayAtribut[i] && i == 4) {
                    if (banyakAtribut > 0) {
                        setUpdate += String.format(",city = \"%s\"", requestBody.get("city"));
                        banyakAtribut++;
                    } else if (banyakAtribut == 0) {
                        setUpdate += String.format("city = \"%s\"", requestBody.get("city"));
                        banyakAtribut++;
                    }
                }
                if (arrayAtribut[i] && i == 5) {
                    if (banyakAtribut > 0) {
                        setUpdate += String.format(",province = \"%s\"", requestBody.get("province"));
                        banyakAtribut++;
                    } else if (banyakAtribut == 0) {
                        setUpdate += String.format("province = \"%s\"", requestBody.get("province"));
                        banyakAtribut++;
                    }
                }
                if (arrayAtribut[i] && i == 6) {
                    if (banyakAtribut > 0) {
                        setUpdate += String.format(",postcode = \"%s\"", requestBody.get("postcode"));
                        banyakAtribut++;
                    } else if (banyakAtribut == 0) {
                        setUpdate += String.format("postcode = \"%s\"", requestBody.get("postcode"));
                        banyakAtribut++;
                    }
                }
            }
            sql = "UPDATE " + tabel + " " + setUpdate + " WHERE id_user = " + id;
        }
        return sql;
    }

    public static String sqlJoinStringGenerator(ArrayList<String> tabel, ArrayList<String> foreignKey,
                                                Integer kondisiId) {
        StringBuilder sql = new StringBuilder("SELECT * FROM ");
        for (int i = 0; i < tabel.size(); i++) {
            if (i == 0) {
                sql.append(tabel.get(i));
            }
            if (i > 0) {
                sql.append(" LEFT JOIN ");
                sql.append(tabel.get(i));
                sql.append(" ON ");
                sql.append(tabel.get(i - 1));
                sql.append(".");
                sql.append(foreignKey.get(i - 1));
                sql.append(" = ");
                sql.append(tabel.get(i));
                sql.append(".");
                sql.append(foreignKey.get(i));
            }
        }
        sql.append(" WHERE ");
        sql.append(tabel.get(0));
        sql.append(".");
        sql.append(foreignKey.get(0));
        sql.append(" = ");
        sql.append(kondisiId);

        return sql.toString();
    }
}

