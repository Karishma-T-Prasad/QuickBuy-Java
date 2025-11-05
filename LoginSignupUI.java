package quickbuy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class LoginSignupUI extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private ArrayList<User> users = new ArrayList<>();

    public LoginSignupUI() {
        setTitle("QuickBuy Stationery - Login / Signup");
        setSize(450, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        loadUsersFromFile(); // 🔹 Load saved users at start
        createReceiptTableIfNotExists(); // 🔹 Ensure receipts table exists in DB

        // ---------- LOGIN PANEL ----------
        JPanel loginPanel = new JPanel(null);
        loginPanel.setBackground(new Color(240, 248, 255));

        JLabel title = new JLabel("QuickBuy Stationery", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setBounds(70, 30, 300, 40);
        loginPanel.add(title);

        JLabel loginLabel = new JLabel("LOGIN", SwingConstants.CENTER);
        loginLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        loginLabel.setBounds(0, 80, 450, 30);
        loginPanel.add(loginLabel);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(80, 140, 100, 25);
        loginPanel.add(userLabel);

        JTextField usernameField = new JTextField();
        usernameField.setBounds(180, 140, 180, 25);
        loginPanel.add(usernameField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(80, 180, 100, 25);
        loginPanel.add(passLabel);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(180, 180, 180, 25);
        loginPanel.add(passwordField);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBackground(new Color(34, 139, 34));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setBounds(230, 230, 90, 35);
        loginPanel.add(loginBtn);

        JLabel signUpText = new JLabel("Don't have an account?");
        signUpText.setBounds(205, 290, 200, 25);
        loginPanel.add(signUpText);

        JButton goSignupBtn = new JButton("Sign Up");
        goSignupBtn.setBackground(new Color(100, 149, 237));
        goSignupBtn.setForeground(Color.WHITE);
        goSignupBtn.setBounds(230, 320, 90, 35);
        loginPanel.add(goSignupBtn);

        // ---------- SIGNUP PANEL ----------
        JPanel signupPanel = new JPanel(null);
        signupPanel.setBackground(new Color(245, 255, 250));

        JLabel signupLabel = new JLabel("Create New Account", SwingConstants.CENTER);
        signupLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        signupLabel.setBounds(0, 40, 450, 30);
        signupPanel.add(signupLabel);

        JLabel nameLabel = new JLabel("Username:");
        nameLabel.setBounds(70, 100, 100, 25);
        signupPanel.add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setBounds(170, 100, 200, 25);
        signupPanel.add(nameField);

        JLabel pass1Label = new JLabel("Password:");
        pass1Label.setBounds(70, 140, 100, 25);
        signupPanel.add(pass1Label);

        JPasswordField passField = new JPasswordField();
        passField.setBounds(170, 140, 200, 25);
        signupPanel.add(passField);

        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setBounds(70, 180, 100, 25);
        signupPanel.add(phoneLabel);

        JTextField phoneField = new JTextField();
        phoneField.setBounds(170, 180, 200, 25);
        signupPanel.add(phoneField);

        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setBounds(70, 220, 100, 25);
        signupPanel.add(addressLabel);

        JTextField addressField = new JTextField();
        addressField.setBounds(170, 220, 200, 25);
        signupPanel.add(addressField);

        JButton signupBtn = new JButton("Sign Up");
        signupBtn.setBackground(new Color(72, 61, 139));
        signupBtn.setForeground(Color.WHITE);
        signupBtn.setBounds(170, 270, 120, 35);
        signupPanel.add(signupBtn);

        JButton backBtn = new JButton("Back to Login");
        backBtn.setBounds(150, 330, 150, 30);
        signupPanel.add(backBtn);

        // Add panels to main
        mainPanel.add(loginPanel, "login");
        mainPanel.add(signupPanel, "signup");
        add(mainPanel);
        cardLayout.show(mainPanel, "login");

        // ---------- BUTTON ACTIONS ----------
        goSignupBtn.addActionListener(e -> cardLayout.show(mainPanel, "signup"));
        backBtn.addActionListener(e -> cardLayout.show(mainPanel, "login"));

        // SIGN UP BUTTON
        signupBtn.addActionListener((ActionEvent e) -> {
            String name1 = nameField.getText().trim();
            String pass = new String(passField.getPassword());
            String phone = phoneField.getText().trim();
            String address = addressField.getText().trim();

            if (name1.isEmpty() || pass.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                JOptionPane.showMessageDialog(LoginSignupUI.this, "Please fill all fields!");
                return;
            }
            if (findUser(name1) != null) {
                JOptionPane.showMessageDialog(LoginSignupUI.this, "Username already exists!");
                return;
            }

            User newUser = new User(name1, pass, phone, address);
            users.add(newUser);

            // Save once only
            UserStorage.saveUser(newUser);

            JOptionPane.showMessageDialog(LoginSignupUI.this, "Signup successful! Welcome, " + name1 + " 🎉");
            dispose();
            new HomeUI(newUser).setVisible(true); // go directly to home
        });


        // LOGIN BUTTON
        loginBtn.addActionListener(e -> {
        String name = usernameField.getText().trim();
        String pass = new String(passwordField.getPassword());

        // Load user from saved file
        User u = UserStorage.validateUser(name, pass);

        if (u != null) {
            JOptionPane.showMessageDialog(this, "Welcome back, " + name + "!");
            dispose();
            new HomeUI(u).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password!");
        }
    });

    }

    // ---------- File Storage Helpers ----------
    private void loadUsersFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    users.add(new User(parts[0], parts[1], parts[2], parts[3]));
                }
            }
        } catch (IOException ex) {
            System.out.println("No previous users found yet.");
        }
    }

    private void saveUserToFile(User u) {
        try (FileWriter fw = new FileWriter("users.txt", true)) {
            fw.write(u.getName() + "," + u.getPassword() + "," + u.getPhone() + "," + u.getAddress() + "\n");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private User findUser(String username) {
        for (User u : users) {
            if (u.getName().equalsIgnoreCase(username)) return u;
        }
        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginSignupUI().setVisible(true));
    }
    // --- Create table if not exists ---
    private void createReceiptTableIfNotExists() {
        String sql = """
            CREATE TABLE IF NOT EXISTS receipts (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT NOT NULL,
                details TEXT NOT NULL,
                total REAL,
                date_time TEXT
            );
            """;

        try (var conn = DatabaseConnection.getConnection();
             var stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("✅ Receipts table ready!");
        } catch (Exception e) {
            e.printStackTrace();
        }
}


}
