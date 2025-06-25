package vn.edu.tlu.btl.appordpizza;

public class ProductModel {
    private int id; // ✅ Thêm id
    private String name;
    private String price;
    private int imageResId;
    private String category;
    private String description;
    private boolean isFavorite;

    // Constructor đầy đủ
    public ProductModel(int id, String name, String price, int imageResId, String category, String description) {
        this.id = id; // ✅ Gán id
        this.name = name;
        this.price = price;
        this.imageResId = imageResId;
        this.category = category;
        this.description = description;
        this.isFavorite = false; // ✅ Mặc định chưa yêu thích
    }

    // ✅ Getter & Setter cho id
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    // Các getter & setter còn lại giữ nguyên
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }

    public int getImageResId() { return imageResId; }
    public void setImageResId(int imageResId) { this.imageResId = imageResId; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }
}
