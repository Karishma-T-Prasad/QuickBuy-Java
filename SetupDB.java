package quickbuy;

import java.sql.Connection;
import java.sql.Statement;

public class SetupDB {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            String createUsers = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT," +
                    "password TEXT," +
                    "phone TEXT," +
                    "address TEXT)";
            
            String createReceipts = "CREATE TABLE IF NOT EXISTS receipts (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT," +
                    "details TEXT," +
                    "amount REAL," +
                    "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)";
            
            stmt.execute(createUsers);
            stmt.execute(createReceipts);

            System.out.println("Tables created successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
