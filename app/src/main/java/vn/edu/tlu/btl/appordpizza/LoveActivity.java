package vn.edu.tlu.btl.appordpizza;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LoveActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private DatabaseHelper dbHelper;
    private List<ProductModel> favoriteProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love);

        // Ánh xạ view
        recyclerView = findViewById(R.id.recyclerFavorites);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // Khởi tạo DB
        dbHelper = new DatabaseHelper(this);

        // Lấy số điện thoại từ SharedPreferences
        SharedPreferences prefs = getSharedPreferences("USER", Context.MODE_PRIVATE);
        String phone = prefs.getString("phone", "");

        // Lấy danh sách sản phẩm yêu thích từ SQLite
        favoriteProducts = dbHelper.getFavoriteProducts(phone);

        // Hiển thị lên RecyclerView
        productAdapter = new ProductAdapter(favoriteProducts);
        recyclerView.setAdapter(productAdapter);
    }
}
