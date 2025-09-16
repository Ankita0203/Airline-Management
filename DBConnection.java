import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // ✅ Correct JDBC URL with required parameters
    private static final String URL =
        "jdbc:mysql://localhost:3306/airline?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
    private static final String USER = "root"; // your MySQL username
    private static final String PASSWORD = "MYSQL@2004"; // your MySQL password

    public static Connection getConnection() throws SQLException {
        try {
            // ✅ Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found.", e);
        }

        // ✅ Return a live DB connection
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
