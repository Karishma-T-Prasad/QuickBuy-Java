package quickbuy;

import java.io.*;
import java.util.*;

public class CartStorage {
    private static final String CART_FILE = "cart_data.txt";

    public static void saveCart(String username, List<Product> cartItems) {
        removeExistingUserCart(username);  // only removes this user's old entry
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CART_FILE, true))) {
            bw.write(username + ":");
            for (Product p : cartItems) {
                bw.write(p.getName() + "," + p.getPrice() + ";");
            }
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Product> loadCart(String username) {
        List<Product> items = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(CART_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith(username + ":")) {
                    String[] parts = line.substring(username.length() + 1).split(";");
                    for (String part : parts) {
                        if (!part.isEmpty()) {
                            String[] prod = part.split(",");
                            items.add(new Product(prod[0], Double.parseDouble(prod[1])));
                        }
                    }
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return items;
    }

    private static void removeExistingUserCart(String username) {
        try {
            File inputFile = new File(CART_FILE);
            File tempFile = new File("cart_temp.txt");

            if (!inputFile.exists()) return;

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith(username + ":")) {
                    writer.write(line + System.lineSeparator());
                }
            }

            writer.close();
            reader.close();

            inputFile.delete();
            tempFile.renameTo(inputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
