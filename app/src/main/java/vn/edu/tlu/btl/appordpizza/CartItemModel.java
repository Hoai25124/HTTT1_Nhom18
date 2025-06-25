package vn.edu.tlu.btl.appordpizza;

public class CartItemModel {
    private String name;
    private String price;
    private int quantity;
    private int imageResId;

    public CartItemModel(String name, String price, int quantity, int imageResId) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageResId = imageResId;
    }

    public String getName() { return name; }
    public String getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public int getImageResId() { return imageResId; }

    public void setQuantity(int quantity) { this.quantity = quantity; }
}
