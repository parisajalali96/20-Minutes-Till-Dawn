package models;

public class User {
    private String username;
    private String password;
    private String SQ;
    private String SA;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getSA() {
        return SA;
    }
    public void setSA(String SA) {
        this.SA = SA;
    }
    public String getSQ() {
        return SQ;
    }
    public void setSQ(String SQ) {
        this.SQ = SQ;
    }

}
