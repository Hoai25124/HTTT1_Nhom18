package vn.edu.tlu.btl.appordpizza;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.List;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "AppPizza.db";
    private static final int DATABASE_VERSION = 9;

    public static final String TABLE_USERS = "Users";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Users (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Phone TEXT NOT NULL, " +
                "Pass TEXT NOT NULL, " +
                "VaiTro TEXT NOT NULL, " +
                "Name TEXT NOT NULL)");

        db.execSQL("CREATE TABLE IF NOT EXISTS Orders (" +
                "OrderId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "FullName TEXT, " +
                "Phone TEXT, " +
                "Address TEXT, " +
                "OrderTime TEXT, " +
                "FoodDetail TEXT, " +
                "Quantity INTEGER, " +
                "TotalAmount INTEGER, " +
                "PaymentMethod TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS Favorites (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "phone TEXT, " +
                "productName TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS Products (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "price TEXT, " +
                "imageResId INTEGER, " +  // lưu đúng kiểu int cho image
                "category TEXT, " +
                "description TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS Messages (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "content TEXT, " +
                "isUser INTEGER, " +
                "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "senderPhone TEXT)");

        insertSampleUsers(db);
        insertSampleProducts(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Users");
        db.execSQL("DROP TABLE IF EXISTS Orders");
        db.execSQL("DROP TABLE IF EXISTS Favorites");
        db.execSQL("DROP TABLE IF EXISTS Products");
        db.execSQL("DROP TABLE IF EXISTS Messages");
        onCreate(db);
    }

    // ------------------- Insert -------------------

    private void insertSampleUsers(SQLiteDatabase db) {
        db.execSQL("INSERT INTO Users (Phone, Pass, VaiTro, Name) VALUES " +
                "('0991234567', '123456', 'Admin', 'Dr. Nguyễn Văn A')," +
                "('0997654321', '654321', 'Admin', 'Dr. Trần Thị B')," +
                "('0987654321', '456789', 'User', 'Tạ Thị P')," +
                "('0912345678', '345678', 'User', 'Lê Thị H')," +
                "('0992004100', '234567', 'User', 'Vũ Thị M')");
    }

    private void insertSampleProducts(SQLiteDatabase db) {
        insertProduct(db, "Pizza Gà Phô Mai", "129.000đ", R.drawable.pizza1, "Pizza", "Pizza gà phô mai béo ngậy, thơm ngon.");
        insertProduct(db, "Pizza Hawaiian", "129.000đ", R.drawable.pizza2, "Pizza", "Pizza Hawaiian vị dứa và jambon đặc trưng.");
        insertProduct(db, "Pizza Tôm Sốt Bơ Tỏi", "149.000đ", R.drawable.pizza3, "Pizza", "Pizza tôm sốt bơ tỏi thơm phức, hấp dẫn.");
        insertProduct(db, "Pizza Lava", "169.000đ", R.drawable.pizza4, "Pizza", "Pizza Lava sốt phô mai tan chảy tuyệt vời.");
        insertProduct(db, "Pizza Thập Cẩm", "269.000đ", R.drawable.pizza5, "Pizza", "Pizza thập cẩm với đầy đủ topping.");
        insertProduct(db, "Pizza Italy", "199.000đ", R.drawable.pizza8, "Pizza", "Pizza Italy truyền thống chuẩn vị.");
        insertProduct(db, "Pizza Xúc Xích", "129.000đ", R.drawable.pizza7, "Pizza", "Pizza xúc xích hấp dẫn, đậm đà.");
        insertProduct(db, "Pizza Kiểu Nhật", "159.000đ", R.drawable.pizza8, "Pizza", "Pizza kiểu Nhật với rong biển và sốt Teriyaki.");
        insertProduct(db, "Khoai Tây Chiên", "39.000đ", R.drawable.ic_load, "Món khai vị", "Khoai tây chiên giòn, thơm ngon.");
        insertProduct(db, "Gà Rán", "39.000đ", R.drawable.garan, "Món khai vị", "Cánh gà chiên nước mắm đậm đà.");
        insertProduct(db, "Coca Cola", "19.000đ", R.drawable.coca, "Thức uống", "Nước ngọt Coca Cola mát lạnh.");
        insertProduct(db, "Trà Chanh", "15.000đ", R.drawable.trachanh, "Thức uống", "Trà chanh thanh mát, giải khát.");
        insertProduct(db, "Nước cam", "25.000đ", R.drawable.nuoccam, "Thức uống", "Nước cam tươi mát, giải khát.");
        insertProduct(db, "Mỳ Ý Bò Bằm", "69.000đ", R.drawable.miy, "Món khai vị", "Mì Ý bò bằm sốt cà chua.");
    }

    private void insertProduct(SQLiteDatabase db, String name, String price, int imageResId, String category, String description) {
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("price", price);
        values.put("imageResId", imageResId);
        values.put("category", category);
        values.put("description", description);
        db.insert("Products", null, values);
    }

    public void insertOrder(String fullName, String phone, String address, String foodDetail, int quantity, int totalAmount, String paymentMethod) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("FullName", fullName);
        values.put("Phone", phone);
        values.put("Address", address);

        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        Log.d("ORDER", "Insert OrderTime: " + currentTime);
        values.put("OrderTime", currentTime);

        values.put("FoodDetail", foodDetail);
        values.put("Quantity", quantity);
        values.put("TotalAmount", totalAmount);
        values.put("PaymentMethod", paymentMethod);

        db.insert("Orders", null, values);
        db.close();
    }

    // ------------------- Revenue -------------------

    public int getRevenueByDay(int day, int month, int year) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT SUM(TotalAmount) FROM Orders WHERE strftime('%d', OrderTime) = ? AND strftime('%m', OrderTime) = ? AND strftime('%Y', OrderTime) = ?";
        Cursor cursor = db.rawQuery(query, new String[]{
                String.format("%02d", day),
                String.format("%02d", month),
                String.valueOf(year)
        });

        int total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getInt(0);
        }
        cursor.close();
        Log.d("REVENUE", "Doanh thu ngày: " + total);
        return total;
    }

    public int getRevenueByMonth(int month, int year) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT SUM(TotalAmount) FROM Orders WHERE strftime('%m', OrderTime) = ? AND strftime('%Y', OrderTime) = ?";
        Cursor cursor = db.rawQuery(query, new String[]{
                String.format("%02d", month),
                String.valueOf(year)
        });

        int total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getInt(0);
        }
        cursor.close();
        Log.d("REVENUE", "Doanh thu tháng: " + total);
        return total;
    }

    public int getRevenueByYear(int year) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT SUM(TotalAmount) FROM Orders WHERE strftime('%Y', OrderTime) = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(year)});

        int total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getInt(0);
        }
        cursor.close();
        Log.d("REVENUE", "Doanh thu năm: " + total);
        return total;
    }

    public List<OrderHistoryModel> getAllOrders() {
        List<OrderHistoryModel> orderList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Orders", null);

        if (cursor.moveToFirst()) {
            do {
                int orderId = cursor.getInt(cursor.getColumnIndexOrThrow("OrderId"));
                String fullName = cursor.getString(cursor.getColumnIndexOrThrow("FullName"));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow("Phone"));
                String address = cursor.getString(cursor.getColumnIndexOrThrow("Address"));
                String orderTime = cursor.getString(cursor.getColumnIndexOrThrow("OrderTime"));
                String foodDetail = cursor.getString(cursor.getColumnIndexOrThrow("FoodDetail"));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("Quantity"));
                int totalAmount = cursor.getInt(cursor.getColumnIndexOrThrow("TotalAmount"));
                String paymentMethod = cursor.getString(cursor.getColumnIndexOrThrow("PaymentMethod"));

                // Tạo đối tượng Order và thêm vào danh sách
                OrderHistoryModel order = new OrderHistoryModel(orderId, fullName, phone, address, orderTime, foodDetail, quantity, totalAmount, paymentMethod);
                orderList.add(order);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return orderList;
    }

    public void insertMonAn(String name, String price, String imageResId, String category, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "INSERT INTO Products (name, price, imageResId, category, description) VALUES (?, ?, ?, ?, ?)";
        db.execSQL(query, new Object[]{name, price, imageResId, category, description});
    }
    public String getUserName(String phone) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("Users", new String[]{"Name"}, "Phone=?", new String[]{phone}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("Name"));
            cursor.close();
            return name;
        }
        return null;
    }

    public boolean checkLogin(String phone, String pass) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, "Phone=? AND Pass=?", new String[]{phone, pass}, null, null, null);
        boolean exists = (cursor != null && cursor.moveToFirst());
        if (cursor != null) cursor.close();
        return exists;
    }

    public String getUserRole(String phone) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{"VaiTro"}, "Phone=?", new String[]{phone}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String role = cursor.getString(cursor.getColumnIndexOrThrow("VaiTro"));
            cursor.close();
            return role;
        }
        return null;
    }


    // ✅ Đã thêm ID
    public List<ProductModel> getAllProducts() {
        List<ProductModel> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("Products", null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String price = cursor.getString(cursor.getColumnIndexOrThrow("price"));
                int imageResId = cursor.getInt(cursor.getColumnIndexOrThrow("imageResId"));
                String category = cursor.getString(cursor.getColumnIndexOrThrow("category"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                list.add(new ProductModel(id, name, price, imageResId, category, description));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    // ✅ Đã thêm ID
    public List<ProductModel> getFavoriteProducts(String phone) {
        List<ProductModel> favList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT p.* FROM Products p " +
                "INNER JOIN Favorites f ON p.name = f.productName " +
                "WHERE f.phone = ?";
        Cursor cursor = db.rawQuery(query, new String[]{phone});
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String price = cursor.getString(cursor.getColumnIndexOrThrow("price"));
                int imageResId = cursor.getInt(cursor.getColumnIndexOrThrow("imageResId"));
                String category = cursor.getString(cursor.getColumnIndexOrThrow("category"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                ProductModel product = new ProductModel(id, name, price, imageResId, category, description);
                product.setFavorite(true);
                favList.add(product);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return favList;
    }

    public void insertMessage(String content, boolean isUser, String senderPhone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("content", content);
        values.put("isUser", isUser ? 1 : 0);
        values.put("senderPhone", senderPhone);
        db.insert("Messages", null, values);
    }

    public List<Message> getMessagesByUser(String senderPhone) {
        List<Message> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Messages WHERE senderPhone = ? ORDER BY timestamp ASC", new String[]{senderPhone});
        if (cursor.moveToFirst()) {
            do {
                String content = cursor.getString(cursor.getColumnIndexOrThrow("content"));
                boolean isUser = cursor.getInt(cursor.getColumnIndexOrThrow("isUser")) == 1;
                list.add(new Message(content, isUser));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public List<UserChat> getAllUsersChatted() {
        List<UserChat> userList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT senderPhone FROM Messages WHERE isUser = 1", null);
        if (cursor.moveToFirst()) {
            do {
                String phone = cursor.getString(0);
                String name = getUserNameByPhone(phone);
                String lastMsg = getLastMessageByUser(phone);
                userList.add(new UserChat(phone, name, lastMsg));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return userList;
    }

    public String getUserNameByPhone(String phone) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Name FROM Users WHERE Phone = ?", new String[]{phone});
        String name = phone;
        if (cursor.moveToFirst()) {
            name = cursor.getString(0);
        }
        cursor.close();
        return name;
    }

    private String getLastMessageByUser(String phone) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT content FROM Messages WHERE senderPhone = ? ORDER BY id DESC LIMIT 1", new String[]{phone});
        String lastMessage = "";
        if (cursor.moveToFirst()) {
            lastMessage = cursor.getString(0);
        }
        cursor.close();
        return lastMessage;
    }
}
