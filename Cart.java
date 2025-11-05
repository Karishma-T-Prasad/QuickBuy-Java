package quickbuy;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Cart implements Serializable {
    private List<Product> items = new ArrayList<>();

    public void addItem(Product p) { items.add(p); }
    public void removeItem(Product p) { items.remove(p); }
    public List<Product> getItems() { return items; }
    public double getTotal() { return items.stream().mapToDouble(Product::getPrice).sum(); }
    public void clearCart() { items.clear(); }

    // Save cart for user
    public void saveCart(String username) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("cart_" + username + ".dat"))) {
            oos.writeObject(items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load saved cart for user
    @SuppressWarnings("unchecked")
    public void loadCart(String username) {
        File f = new File("cart_" + username + ".dat");
        if (f.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
                items = (List<Product>) ois.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
