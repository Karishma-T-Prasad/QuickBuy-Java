package quickbuy;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class PaymentProcessingUI extends JFrame {

    private final User currentUser;
    private final Cart cart;

    public PaymentProcessingUI(User user, Cart cart) {
        this.currentUser = user;
        this.cart = cart;

        setTitle("💳 QuickBuy Payment");
        setSize(420, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        // --- Title ---
        JLabel title = new JLabel("Payment Processing", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setBounds(0, 20, 420, 30);
        add(title);

        // --- Animated Slider ---
        AnimatedSliderPanel animationPanel = new AnimatedSliderPanel();
        animationPanel.setBounds(30, 70, 360, 50);
        add(animationPanel);
        animationPanel.startAnimation();

        // --- QR Code Label ---
        JLabel qrLabel = new JLabel();
        qrLabel.setBounds(120, 140, 180, 180);
        add(qrLabel);

        // --- Amount Label ---
        JLabel amountLbl = new JLabel("Total: ₹" + String.format("%.2f", cart.getTotal()), SwingConstants.CENTER);
        amountLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        amountLbl.setForeground(new Color(0, 128, 0));
        amountLbl.setBounds(0, 330, 420, 30);
        add(amountLbl);

        // --- Payment Button ---
        JButton payBtn = styledButton("Complete Payment", new Color(0, 150, 136));
        payBtn.setBounds(110, 370, 200, 40);
        add(payBtn);

        // --- Button Action ---
        payBtn.addActionListener(e -> processPayment(qrLabel));

        setVisible(true);

        // --- Automatically generate QR and process payment (simulate) ---
        SwingUtilities.invokeLater(() -> processPayment(qrLabel));
    }

    // --- Styled Button Helper ---
    private JButton styledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // --- Payment Process ---
    private void processPayment(JLabel qrLabel) {
        try {
            // Generate receipt ID for display
            String receiptId = "RCP" + new Random().nextInt(100000);

            // Generate QR code (simulate payment)
            String qrPath = "payment_qr.png";
            String qrData = "User: " + currentUser.getName() + "\nTotal: " + cart.getTotal();
            QRCodeGenerator.generateQRCode(qrData, qrPath);
            // Load the QR image
            ImageIcon icon = new ImageIcon(qrPath);
            Image scaled = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            qrLabel.setIcon(new ImageIcon(scaled));

            // Generate receipt file
            ReceiptGenerator.generateReceipt(cart.getItems(), cart.getTotal(), "receipt.txt", currentUser);

            // Save order history BEFORE clearing cart
            OrderHistory.saveOrder(currentUser, new ArrayList<>(cart.getItems()), cart.getTotal());

            // Show confirmation dialog
            JOptionPane.showMessageDialog(this,
                    "<html><b>✅ Payment Successful!</b><br>Receipt ID: " + receiptId +
                            "<br>Saved as receipt.txt</html>",
                    "Payment Complete", JOptionPane.INFORMATION_MESSAGE);

            cart.clearCart(); // clear cart after saving history

            // --- Instead of closing, go to Home Page ---
            this.dispose();
            new HomeUI(currentUser); // assuming HomePage constructor takes User

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Error: " + ex.getMessage(),
                    "Payment Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- Animated Slider Panel ---
    static class AnimatedSliderPanel extends JPanel {
        private int pos = 0;

        public AnimatedSliderPanel() {
            setBackground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.LIGHT_GRAY);
            g.fillRoundRect(0, 10, 360, 20, 20, 20);
            g.setColor(new Color(63, 81, 181));
            g.fillRoundRect(0, 10, pos, 20, 20, 20);
        }

        public void startAnimation() {
            new Timer(15, e -> {
                int width = 360;
                pos += 4;
                repaint();
                if (pos >= width) ((Timer) e.getSource()).stop();
            }).start();
        }
    }
}
