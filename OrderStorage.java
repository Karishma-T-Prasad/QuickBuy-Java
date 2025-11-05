package quickbuy;

import java.io.*;
import java.time.LocalDateTime;
import java.util.List;

public class OrderStorage {
    private static final String ORDER_FILE = "orders.txt";

    public static void saveOrder(String username, List<Product> items, double total) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ORDER_FILE, true))) {
            bw.write("Username: " + username + "\n");
            bw.write("Date: " + LocalDateTime.now() + "\n");
            bw.write("Items:\n");
            for (Product p : items) {
                bw.write("- " + p.getName() + " (₹" + p.getPrice() + ")\n");
            }
            bw.write("Total: ₹" + total + "\n");
            bw.write("--------------------------------------------\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
