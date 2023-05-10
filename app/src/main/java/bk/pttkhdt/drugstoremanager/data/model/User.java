package bk.pttkhdt.drugstoremanager.data.model;

public class User {

    private String phoneNumber;
    private String password;
    private String name;
    private String birthday;
    private String email;
    private String address;

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public User(String phoneNumber, String password, String name, String birthday, String email, String address) {
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.name = name;
        this.birthday = birthday;
        this.email = email;
        this.address = address;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
