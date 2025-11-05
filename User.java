package quickbuy;

public class User {
    private final String username;
    private final String password;
    private final String phoneNumber;
    private final String address;

    public User(String username, String password, String phoneNumber, String address) {
        this.username = username;
        this.password = password;
        this.phoneNumber= phoneNumber;
        this.address= address;
        
    }
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.phoneNumber = "N/A";
        this.address = "N/A";
    }


    public String getName() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    public String getPhone() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
}
}
