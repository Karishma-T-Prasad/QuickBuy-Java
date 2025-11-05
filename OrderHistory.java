package quickbuy;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class OrderHistory {
    private static final String FILE = "order_history.txt";

    public static void saveOrder(User user, List<Product> items, double total) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String date = sdf.format(new Date());

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE, true))) {
            bw.write("User: " + user.getName());
            bw.newLine();
            bw.write("Date: " + date);
            bw.newLine();
            for (Product p : items) {
                bw.write(p.getName() + " - ₹" + p.getPrice());
                bw.newLine();
            }
            bw.write("Total: ₹" + total);
            bw.newLine();
            bw.write("----------------------------------------");
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
