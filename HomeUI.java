package quickbuy;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HomeUI extends JFrame {
    private final User currentUser;

    public HomeUI(User user) {
        this.currentUser = user;

        setTitle("🏠 QuickBuy Home");
        setSize(300, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.BLUE);

        // --- Profile Section (Top) ---
        JPanel profilePanel = new JPanel(new BorderLayout());
        profilePanel.setBackground(Color.WHITE);
        profilePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        
        // --- Buttons Section ---
        // --- Buttons Section (replace your current GridLayout panel) ---
        JPanel buttonPanel = new JPanel(null); // use null layout for absolute positioning
        buttonPanel.setBackground(new Color(173, 216, 230));


        // Create buttons
        JButton itemsBtn = createStyledButton("🛍️", "View Items", new Color(63, 81, 181));
        JButton historyBtn = createStyledButton("📜", "Order History", new Color(0, 150, 136));
        JButton cartBtn = createStyledButton("🛒", "My Cart", new Color(255, 152, 0));
        JButton logoutBtn = createStyledButton("🚪", "<html> Logout </html>", new Color(244, 67, 54));

        // Horizontal layout
        int btnWidth = 100;
        int btnHeight = 80;
        int hGap = 30; // horizontal gap between buttons
        int vGap = 30; // vertical gap between rows
        int startX = 30; // left margin
        int startY = 50;

        // --- Row 1 ---
        itemsBtn.setBounds(startX, startY, btnWidth, btnHeight);
        historyBtn.setBounds(startX + btnWidth + hGap, startY, btnWidth, btnHeight);

        // --- Row 2 ---
        cartBtn.setBounds(startX, startY + btnHeight + vGap, btnWidth, btnHeight);
        logoutBtn.setBounds(startX + btnWidth + hGap, startY + btnHeight + vGap, btnWidth, btnHeight);

        // Add buttons to panel
        buttonPanel.add(itemsBtn);
        buttonPanel.add(historyBtn);
        buttonPanel.add(cartBtn);
        buttonPanel.add(logoutBtn);

        // Add button panel to frame
        add(buttonPanel, BorderLayout.CENTER);

        // --- Button Actions (keep as is) ---
        itemsBtn.addActionListener(e -> {
            new MainUI(currentUser).setVisible(true);
            dispose();
        });
        historyBtn.addActionListener(e -> showOrderHistory());
        cartBtn.addActionListener(e -> {
            new MainUI(currentUser).setVisible(true);
            dispose();
        });
        logoutBtn.addActionListener(e -> {
            new LoginSignupUI().setVisible(true);
            dispose();
        });

        setVisible(true);
    }
    private JButton createStyledButton(String iconText, String text, Color bgColor) {
        JButton btn = new JButton("<html><center>" + iconText + "<br>" + text + "</center></html>");
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bgColor);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Rounded corners
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void installUI(JComponent c) {
                super.installUI(c);
                c.setOpaque(false);
            }

            @Override
            public void paint(Graphics g, JComponent c) {
                AbstractButton b = (AbstractButton) c;
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(b.getBackground());
                g2.fillRoundRect(0, 0, b.getWidth(), b.getHeight(), 20, 20);
                super.paint(g2, c);
                g2.dispose();
            }
        });

        // Hover effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            Color original = bgColor;
            Color hover = original.darker();

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(hover);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(original);
            }
        });

        return btn;
    }

    // Make button style consistent
    private JButton styledButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setFont(new Font("SansSerif", Font.BOLD, 15));
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    // --- Make Circular Image ---
    private Image makeCircularImage(Image img) {
        int w = img.getWidth(null);
        int h = img.getHeight(null);

        // 🔒 Safety check — if image failed to load
        if (w <= 0 || h <= 0) {
            w = h = 100; // default placeholder size
            BufferedImage placeholder = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = placeholder.createGraphics();
            g2.setColor(Color.LIGHT_GRAY);
            g2.fillOval(0, 0, w, h);
            g2.setColor(Color.DARK_GRAY);
            g2.drawString("No Img", 25, 55);
            g2.dispose();
            return placeholder;
        }

        int size = Math.min(w, h);
        BufferedImage mask = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = mask.createGraphics();
        g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, size, size));
        g2.drawImage(img, 0, 0, size, size, null);
        g2.dispose();
        return mask;
    }


    // --- Order History Logic (same as before) ---
    private void showOrderHistory() {
        List<String> orders = readOrderHistory(currentUser.getName());
        if (orders.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No previous orders found!", "📜 Order History", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JTextArea area = new JTextArea(String.join("\n\n", orders));
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 13));

        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(350, 200));

        JOptionPane.showMessageDialog(this, scroll, "📜 Order History", JOptionPane.INFORMATION_MESSAGE);
    }

    private List<String> readOrderHistory(String username) {
        List<String> history = new ArrayList<>();
        File file = new File("order_history.txt");
        if (!file.exists()) return history;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            StringBuilder record = new StringBuilder();
            String line;
            boolean foundUser = false;

            while ((line = br.readLine()) != null) {
                if (line.startsWith("User: ")) {
                    if (foundUser && record.length() > 0) {
                        history.add(record.toString());
                        record.setLength(0);
                    }
                    foundUser = line.equals("User: " + username);
                }
                if (foundUser) record.append(line).append("\n");
            }

            if (foundUser && record.length() > 0) history.add(record.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return history;
    }
}
