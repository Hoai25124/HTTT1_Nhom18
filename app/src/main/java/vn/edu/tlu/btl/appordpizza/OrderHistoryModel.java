package vn.edu.tlu.btl.appordpizza;

public class OrderHistoryModel {
    private int orderId; // Đổi từ String sang int
    private String name, phone, address, orderTime, foodDetail, paymentMethod;
    private int quantity, totalPrice;

    public OrderHistoryModel(int orderId, String name, String phone, String address,
                             String orderTime, String foodDetail,
                             int quantity, int totalPrice, String paymentMethod) {
        this.orderId = orderId;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.orderTime = orderTime;
        this.foodDetail = foodDetail;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.paymentMethod = paymentMethod;
    }

    public int getOrderId() { return orderId; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public String getTime() { return orderTime; }
    public String getFoodDetail() { return foodDetail; }
    public int getQuantity() { return quantity; }
    public int getTotal() { return totalPrice; }
    public String getPayment() { return paymentMethod; }
}
