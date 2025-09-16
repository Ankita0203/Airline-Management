import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static Connection getConnection() throws SQLException {
        // ✅ Update these values before running locally
        String url = "jdbc:mysql://localhost:3306/airline?useSSL=false&serverTimezone=UTC";
        String user = "your-username";     // <-- Replace with your MySQL username
        String password = "your-password"; // <-- Replace with your MySQL password

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found.", e);
        }
        return DriverManager.getConnection(url, user, password);
    }
}
