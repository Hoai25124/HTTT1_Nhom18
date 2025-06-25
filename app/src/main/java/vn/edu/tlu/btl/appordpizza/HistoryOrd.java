package vn.edu.tlu.btl.appordpizza;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HistoryOrd extends AppCompatActivity {

    private RecyclerView recyclerOrderHistory;
    private OrderHistoryAdapter adapter;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private String currentPhone;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_order);

        // DB
        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getReadableDatabase();

        // Lấy số điện thoại người dùng từ SharedPreferences
        currentPhone = getSharedPreferences("USER", MODE_PRIVATE).getString("phone", "");

        // Ánh xạ view
        recyclerOrderHistory = findViewById(R.id.recyclerOrderHistory);
        recyclerOrderHistory.setLayoutManager(new LinearLayoutManager(this));
        btnBack = findViewById(R.id.btnBack);

        // Load danh sách đơn hàng
        List<OrderHistoryModel> orderList = getOrderListFromDatabase();
        if (orderList.isEmpty()) {
            Toast.makeText(this, "Bạn chưa có đơn hàng nào!", Toast.LENGTH_SHORT).show();
        }

        adapter = new OrderHistoryAdapter(orderList);
        recyclerOrderHistory.setAdapter(adapter);

        // Nút quay lại
        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(HistoryOrd.this, ProfileActivity.class));
            finish();
        });
    }

    private List<OrderHistoryModel> getOrderListFromDatabase() {
        List<OrderHistoryModel> list = new ArrayList<>();

        Cursor cursor = db.query(
                "Orders",
                null,
                "Phone = ?",
                new String[]{currentPhone},
                null,
                null,
                "OrderTime DESC"
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int orderId = cursor.getInt(cursor.getColumnIndexOrThrow("OrderId")); // ✅ Sửa: giữ kiểu int
                String name = cursor.getString(cursor.getColumnIndexOrThrow("FullName"));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow("Phone"));
                String address = cursor.getString(cursor.getColumnIndexOrThrow("Address"));
                String orderTime = cursor.getString(cursor.getColumnIndexOrThrow("OrderTime"));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("Quantity"));
                int totalPrice = cursor.getInt(cursor.getColumnIndexOrThrow("TotalAmount"));
                String payment = cursor.getString(cursor.getColumnIndexOrThrow("PaymentMethod"));
                String foodDetail = cursor.getString(cursor.getColumnIndexOrThrow("FoodDetail"));

                OrderHistoryModel order = new OrderHistoryModel(
                        orderId, // ✅ Sửa: truyền int
                        name,
                        phone,
                        address,
                        orderTime,
                        foodDetail != null ? foodDetail : "",
                        quantity,
                        totalPrice,
                        payment
                );

                list.add(order);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return list;
    }

    @Override
    protected void onDestroy() {
        if (db != null && db.isOpen()) db.close();
        super.onDestroy();
    }
}
