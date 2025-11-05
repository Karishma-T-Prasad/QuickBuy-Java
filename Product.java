package quickbuy;

import java.io.Serializable;

public class Product implements Serializable {
    private static final long serialVersionUID = 1L; // optional but recommended

    private final String name;
    private final double price;
   

    // Constructor
    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    // Getters & Setters
    public String getName() { return name; }
    public double getPrice() { return price; }

    @Override
    public String toString() {
         return name + " - Rs. " + price;
    }
}
