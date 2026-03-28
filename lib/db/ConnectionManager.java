package lib.db;

import java.sql.*;
import java.util.Map;
import java.util.Stack;

public class ConnectionManager {
    private static final int MAX_CONNECTIONS = 10;
    private static Stack<Connection> connectionPool = new Stack<>();
    private static String url;
    private static String user;
    private static String pass;

    public static void setConfig(String url, String user, String pass) {
        ConnectionManager.url = url;
        ConnectionManager.user = user;
        ConnectionManager.pass = pass;
        System.out.println("using db config: " + url + ", " + user + ", " + pass);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            for(int i = 0; i < MAX_CONNECTIONS; i++) {
                connectionPool.push(DriverManager.getConnection(url, user, pass));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static ConnectionManager instance;
    public static synchronized ConnectionManager getInstance() {
        if(instance == null) instance = new ConnectionManager();
        return instance;
    }

    public synchronized Connection getConnection() {
        while(connectionPool.isEmpty()) {
            try {
                wait(); // properly wait until a connection is returned
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Thread interrupted while waiting for connection", e);
            }
        }
        return connectionPool.pop();
    }

    private synchronized void returnConnection(Connection connection) {
        connectionPool.push(connection);
        notifyAll();
    }

    public Retry execute(String query, Map<String, String> data) {
        for (Map.Entry<String, String> entry : data.entrySet()) {
            query = query.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        
        Retry retry = null;
        Connection connection = getConnection();
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                long id = rs.getLong(1);
                retry = new Retry(
                    (int) id,
                    Integer.parseInt(data.get("uid")),
                    Integer.parseInt(data.getOrDefault("count", "0")),
                    Status.valueOf(data.get("status")),
                    data.get("data")
                );
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            returnConnection(connection);
        }
        return retry;
    }

    public void close() {
        try {
            for (Connection connection : connectionPool) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}