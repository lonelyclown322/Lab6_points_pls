package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnector {

    private String url = "jdbc:postgresql://localhost:5432/Lab7DB";
    private String user = "postgres";
    private String password = "admin";


    public Connection getNewConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("db driver not found");
            return null;
        }
        return DriverManager.getConnection(url, user, password);
    }
}
