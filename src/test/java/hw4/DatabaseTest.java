package hw4;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTest extends AbstractTest {

    @Test
    @Order(1)
    void testGetProductForPrice() {
        String request = "SELECT * FROM products WHERE price>1000";
        try {
            Statement stmt = getConnection().createStatement();
            int count = 0;
            ResultSet result = stmt.executeQuery(request);
            while (result.next()) {
                count++;
            }
            assertEquals(2, count);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    @Order(2)
    void testGetCustomerByName() throws SQLException {
        String request = "SELECT * FROM customers WHERE last_name LIKE 'Smith'";
        Statement stmt = getConnection().createStatement();
        int count = 0;
        ResultSet result = stmt.executeQuery(request);
        while (result.next()) {
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    @Order(3)
    void testCreateTable() throws SQLException {
        String create = "CREATE TABLE IF NOT EXISTS homeworks\n" +
                "(id integer PRIMARY KEY,\n" +
                "first_name text NOT NULL,\n" +
                "last_name text NOT NULL,\n" +
                "subject text NOT NULL,\n" +
                "grade text NOT NULL);";
        Statement stmt = getConnection().createStatement();
        stmt.execute(create);
        assertNotNull(stmt);
    }

    @Test
    @Order(4)
    void testInsertDate() throws SQLException {
        insertData(1, "Tina", "Sumilova", "Autotest", "Good");
        insertData(2, "Dana", "Sumilova", "Autotest", "Great");
        String request = "SELECT * FROM homeworks";
        Statement test = getConnection().createStatement();
        int count = 0;
        ResultSet result = test.executeQuery(request);
        while (result.next()) {
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    @Order(5)
    void delete() throws SQLException {
        String sqlDeleteFromHomeworks = "DELETE FROM homeworks WHERE id = 1";
        Statement stmt = getConnection().createStatement();
        stmt.execute(sqlDeleteFromHomeworks);
        String request = "SELECT * FROM homeworks";
        Statement test = getConnection().createStatement();
        int count = 0;
        ResultSet result = test.executeQuery(request);
        while (result.next()) {
            count++;
        }
        assertEquals(1, count);
    }

    public void insertData(Integer id, String firstName, String lastName, String subject, String grade) throws SQLException {
        String insert = "INSERT INTO homeworks(id, first_name, last_name, subject, grade) VALUES(?,?,?,?,?)";
        PreparedStatement pstmt = getConnection().prepareStatement(insert);
        pstmt.setInt(1, id);
        pstmt.setString(2, firstName);
        pstmt.setString(3, lastName);
        pstmt.setString(4, subject);
        pstmt.setString(5, grade);
        pstmt.executeUpdate();
    }
}
