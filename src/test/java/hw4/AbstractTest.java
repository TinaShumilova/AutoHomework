package hw4;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AbstractTest {

    private static Connection connection;

    @BeforeAll
    static void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:homework.db");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ":" + e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
    }

    @AfterAll
    static void disconnect() throws SQLException {
        connection.close();
        System.out.println("Closed database successfully");
    }

    public static Connection getConnection() {
        return connection;
    }
}
