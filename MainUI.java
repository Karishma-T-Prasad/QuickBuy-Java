package quickbuy;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainUI extends JFrame {
    private final Cart cart;
    private final User currentUser;
    private final List<Product> products = new ArrayList<>();
    private final List<JCheckBox> productCheckboxes = new ArrayList<>();
    private final DefaultListModel<String> cartModel = new DefaultListModel<>();
    private final JList<String> cartList = new JList<>(cartModel);
    private JLabel totalLabel = new JLabel("Total: ₹0.0");
    

    public MainUI(User user) {
        this.currentUser = user;
        this.cart = new Cart(); // initialize empty cart first

        // Load user's saved cart items (from file)
        List<Product> savedItems = CartStorage.loadCart(user.getName());
        for (Product p : savedItems) {
            cart.addItem(p);              // add each saved product into cart
            cartModel.addElement(p.toString()); // show in UI list
        }

        // Update total
        totalLabel = new JLabel("Total: ₹" + cart.getTotal());
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 14));


        setTitle("🛒 QuickBuy Stationery Store - Welcome " + user.getName());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(850, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        // --- Header ---
        JLabel title = new JLabel("🛍️ QuickBuy Stationery Store", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(new Color(33, 97, 140));
        add(title, BorderLayout.NORTH);

        // --- Product Panel with Checkboxes ---
        JPanel productPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        productPanel.setBorder(BorderFactory.createTitledBorder("Available Products"));
        productPanel.setBackground(new Color(250, 250, 255));

        addProduct(new Product("Notebook (200 pages)", 60), productPanel);
        addProduct(new Product("Pen (Blue)", 10), productPanel);
        addProduct(new Product("Pencil (Apsara)", 5), productPanel);
        addProduct(new Product("Ruler (30 cm)", 15), productPanel);
        addProduct(new Product("Eraser", 5), productPanel);
        addProduct(new Product("Sharpener", 5), productPanel);
        addProduct(new Product("Geometry Box", 120), productPanel);
        addProduct(new Product("Procircle", 25), productPanel);
        addProduct(new Product("Set Square", 20), productPanel);
        addProduct(new Product("Fevicol (50 ml)", 25), productPanel);
        addProduct(new Product("Sketch Pen Set", 90), productPanel);
        addProduct(new Product("Highlighter", 35), productPanel);
        addProduct(new Product("File Folder", 50), productPanel);

        JScrollPane productScroll = new JScrollPane(productPanel);
        productScroll.setBorder(BorderFactory.createEmptyBorder());

        // --- Cart Panel ---
        JPanel cartPanel = new JPanel(new BorderLayout(10, 10));
        cartPanel.setBorder(BorderFactory.createTitledBorder("Your Cart"));
        cartPanel.setBackground(new Color(245, 255, 245));
        cartList.setFont(new Font("SansSerif", Font.PLAIN, 14));
        cartPanel.add(new JScrollPane(cartList), BorderLayout.CENTER);

        // --- Center Layout ---
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 15, 15));
        centerPanel.add(productScroll);
        centerPanel.add(cartPanel);
        add(centerPanel, BorderLayout.CENTER);

        // --- Buttons ---
        JButton addBtn = new JButton("Add to Cart");
        JButton removeBtn = new JButton("Remove item from cart");
        JButton checkoutBtn = new JButton("Checkout");
        JButton logoutBtn = new JButton("Logout");

        addBtn.setBackground(new Color(46, 204, 113));
        addBtn.setForeground(Color.WHITE);
        removeBtn.setBackground(new Color(230, 126, 34));
        removeBtn.setForeground(Color.WHITE);
        checkoutBtn.setBackground(new Color(52, 152, 219));
        checkoutBtn.setForeground(Color.WHITE);
        logoutBtn.setBackground(new Color(192, 57, 43));
        logoutBtn.setForeground(Color.WHITE);

        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        totalLabel.setForeground(new Color(0, 102, 0));

        JPanel southPanel = new JPanel();
        southPanel.add(addBtn);
        southPanel.add(removeBtn);
        southPanel.add(checkoutBtn);
        southPanel.add(totalLabel);
        southPanel.add(logoutBtn);
        add(southPanel, BorderLayout.SOUTH);
        JButton homeBtn = new JButton("🏠 Home");
        homeBtn.setBounds(500, 20, 30, 25);
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(63, 81, 181)); // blue header
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("🏠 QuickBuy");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);

        topPanel.add(titleLabel);
        add(topPanel, BorderLayout.NORTH);

        homeBtn.addActionListener(e -> {
            new HomeUI(currentUser).setVisible(true);
            dispose();
        });
        topPanel.add(homeBtn);


        // --- Event Listeners ---
        addBtn.addActionListener(e -> {
            boolean addedAny = false;
            for (int i = 0; i < products.size(); i++) {
                if (productCheckboxes.get(i).isSelected()) {
                    Product p = products.get(i);
                    cart.addItem(p);
                    cart.saveCart(currentUser.getName());

                    cartModel.addElement(p.toString());
                    productCheckboxes.get(i).setSelected(false);
                    addedAny = true;
                }
            }
            if (addedAny) {
                totalLabel.setText("Total: ₹" + cart.getTotal());
                JOptionPane.showMessageDialog(this, "Items added to cart!", "Added ✅", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Please select at least one product.", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });

        removeBtn.addActionListener(e -> {
            int index = cartList.getSelectedIndex();
            if (index >= 0) {
                cart.removeItem(cart.getItems().get(index));
                cart.saveCart(currentUser.getName());

                cartModel.remove(index);
                totalLabel.setText("Total: ₹" + cart.getTotal());
            } else {
                JOptionPane.showMessageDialog(this, "Select an item to remove!", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });

        checkoutBtn.addActionListener(e -> {
        if (cart.getItems().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Your cart is empty!");
            return;
        }

        // Save current cart to file before moving to payment
        CartStorage.saveCart(currentUser.getName(), cart.getItems());

        // ✅ Pass the actual product list, not the cart object
        new PaymentProcessingUI(currentUser, cart).setVisible(true);

        dispose();
    });




        logoutBtn.addActionListener(e -> {
            CartStorage.saveCart(currentUser.getName(), cart.getItems());

            dispose();
            new LoginSignupUI().setVisible(true);
        });
    }

    private void addProduct(Product p, JPanel panel) {
        products.add(p);
        JCheckBox box = new JCheckBox(p.getName() + " - ₹" + p.getPrice());
        box.setFont(new Font("SansSerif", Font.PLAIN, 14));
        box.setBackground(new Color(250, 250, 255));
        productCheckboxes.add(box);
        panel.add(box);
    }

    private void checkout(User user) {
        try {
            double total = cart.getTotal();
            if (total == 0) {
                JOptionPane.showMessageDialog(this, "Your cart is empty!");
                return;
            }

            // Prepare order summary
            StringBuilder data = new StringBuilder();
            data.append("Customer Details:\n");
            data.append("Name: ").append(currentUser.getName()).append("\n");
            data.append("Phone: ").append(currentUser.getPhone()).append("\n");
            data.append("Address: ").append(currentUser.getAddress()).append("\n\n");
            data.append("Items:\n");
            for (Product p : cart.getItems()) {
                data.append(p.getName()).append(" - ₹").append(p.getPrice()).append("\n");
            }
            data.append("\nGrand Total: ₹").append(total);
            data.append("\n\nThank you for shopping with us!");

            // Generate QR and Receipt
            String qrPath = "payment_qr.png";
            QRCodeGenerator.generateQRCode(data.toString(), qrPath);

            JOptionPane.showMessageDialog(this, new ImageIcon(qrPath), "Scan to Pay", JOptionPane.INFORMATION_MESSAGE);
            JOptionPane.showMessageDialog(this, "<html><font color='green'><b>Payment Successful ✅</b></font></html>", "Success", JOptionPane.INFORMATION_MESSAGE);

            ReceiptGenerator.generateReceipt(cart.getItems(), cart.getTotal(), "receipt.txt", user);

            JOptionPane.showMessageDialog(this, "Receipt saved as receipt.txt");

            cart.clearCart();
            cartModel.clear();
            totalLabel.setText("Total: Rs. 0.0");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error during checkout: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
            new MainUI(new User("Guest", "guest123", "0000000000", "No Address")).setVisible(true)
        );
    }
}
