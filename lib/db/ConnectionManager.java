package lib.db;

import java.sql.*;
import java.util.Map;

public class ConnectionManager {
    private static Connection connection = null;
    private static String url;
    private static String user;
    private static String pass;

    public static void setConfig(String url, String user, String pass) {
        ConnectionManager.url = url;
        ConnectionManager.user = user;
        ConnectionManager.pass = pass;
    }

    public ConnectionManager() {
        System.out.println("using db config: " + url + ", " + user + ", " + pass);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Retry execute(String query, Map<String, String> data) {
        for (Map.Entry<String, String> entry : data.entrySet()) {
            query = query.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                long id = rs.getLong(1);
                return new Retry((int) id, Integer.parseInt(data.get("uid")), Status.valueOf(data.get("status")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}