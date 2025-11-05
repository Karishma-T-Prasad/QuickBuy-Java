package quickbuy;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReceiptGenerator {

    public static void generateReceipt(List<Product> items, double total, String filename, User user) {
        // Split declaration and assignment
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String date = sdf.format(new Date());

        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("===== PAYMENT RECEIPT =====\n");
            writer.write("Date: " + date + "\n");
            writer.write("User: " + user.getName() + "\n\n");

            for (Product item : items) {
                writer.write(item.getName() + " - ₹" + item.getPrice() + "\n");
            }

            writer.write("\n----------------------------\n");
            writer.write("TOTAL: ₹" + total + "\n");
            writer.write("============================\n");

            // ✅ Save into database
            ReceiptDatabase.saveReceipt(user.getName(), buildItemString(items), total, date);

            System.out.println("Receipt generated successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper method to join product list into readable text
    private static String buildItemString(List<Product> items) {
        StringBuilder sb = new StringBuilder();
        for (Product p : items) {
            sb.append("• ")
              .append(p.getName())
              .append(" - ₹")
              .append(p.getPrice())
              .append("\n");
        }
        return sb.toString().trim(); // remove last newline for neatness
    }

}
