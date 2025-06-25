package vn.edu.tlu.btl.appordpizza;

public class UserChat {
    private String phone;
    private String userName;
    private String lastMessage;

    // Constructor mới nhận 3 tham số
    public UserChat(String phone, String userName, String lastMessage) {
        this.phone = phone;
        this.userName = userName;
        this.lastMessage = lastMessage;
    }

    public String getPhone() {
        return phone;
    }

    public String getUserName() {
        return userName;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    // Nếu cần setter
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
