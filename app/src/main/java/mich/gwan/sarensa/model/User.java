package mich.gwan.sarensa.model;

public class User {
    int userId;
    String userType;
    String userName;
    String userEmail;
    String userPhone;
    String userPassword;

    public void setUserId(int userId) {
        this.userId = userId;
    }
    public void setUserType(String userType) {
        this.userType = userType;
    }
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public int getUserId() {
        return userId;
    }
    public String getUserType() {
        return userType;
    }
    public String getUserName() {
        return userName;
    }
    public String getUserEmail() {
        return userEmail;
    }
    public String getUserPhone() {
        return userPhone;
    }
    public String getUserPassword() {
        return userPassword;
    }
}
