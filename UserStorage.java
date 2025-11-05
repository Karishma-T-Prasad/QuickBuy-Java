package quickbuy;

import java.io.*;
import java.util.*;

public class UserStorage {
    private static final String USER_FILE = "users.txt";

    // Save new user details
    public static void saveUser(User user) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USER_FILE, true))) {
            bw.write(user.getName() + "," + user.getPassword() + "," +
                     user.getPhone() + "," + user.getAddress());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Validate login credentials
    public static User validateUser(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4 && parts[0].equals(username) && parts[1].equals(password)) {
                    return new User(parts[0], parts[1], parts[2], parts[3]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; // no match found
    }
}
