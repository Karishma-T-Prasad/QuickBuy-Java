package quickbuy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * ReceiptDatabase utility: creates the receipts table (if needed)
 * and provides a method to save receipts to the SQLite DB.
 */
public class ReceiptDatabase {

    // create table if not exists
    public static void createTableIfNotExists() {
        String sql = """
            CREATE TABLE IF NOT EXISTS receipts (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT NOT NULL,
                details TEXT NOT NULL,
                total REAL,
                date TEXT
            );
            """;
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Save a receipt record (this signature matches your call)
    public static void saveReceipt(String username, String details, double total, String date) {
        String sql = "INSERT INTO receipts (username, details, total, date) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, details);
            ps.setDouble(3, total);
            ps.setString(4, date);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
