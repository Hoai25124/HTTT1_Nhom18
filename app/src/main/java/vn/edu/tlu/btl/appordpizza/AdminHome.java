package vn.edu.tlu.btl.appordpizza;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class AdminHome extends AppCompatActivity {
    private RecyclerView recyclerA, recyclerB;
    private ProductAdminAdapter productAdapter;
    private List<ProductModel> fullProductList = new ArrayList<>();

    private BottomNavigationView bottomnavigation;
    private DatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        recyclerA = findViewById(R.id.recyclerA);
        recyclerB = findViewById(R.id.recyclerB);
        bottomnavigation = findViewById(R.id.bottomnavigation);
        dbHelper = new DatabaseHelper(this);

        // Lấy danh sách sản phẩm từ SQLite
        fullProductList = dbHelper.getAllProducts();


        // Adapter loại
        List<String> categoryList = new ArrayList<>();
        categoryList.add("Tất cả");
        categoryList.add("Pizza");
        categoryList.add("Món khai vị");
        categoryList.add("Thức uống");

        CategoryAdapter categoryAdapter = new CategoryAdapter(categoryList, selectedCategory -> {
            if (selectedCategory.equals("Tất cả")) {
                productAdapter.setFilteredList(fullProductList);
            } else {
                List<ProductModel> filtered = new ArrayList<>();
                for (ProductModel product : fullProductList) {
                    if (product.getCategory().equalsIgnoreCase(selectedCategory)) {
                        filtered.add(product);
                    }
                }
                productAdapter.setFilteredList(filtered);
            }
        });

        recyclerA.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerA.setAdapter(categoryAdapter);

        // Adapter sản phẩm
        productAdapter = new ProductAdminAdapter(this, fullProductList);
        recyclerB.setLayoutManager(new GridLayoutManager(this, 2)); // 2 cột
        recyclerB.setAdapter(productAdapter);

        // Bottom navigation
        bottomnavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(AdminHome.this, AdminHome.class));
                return true;
            }
            if (id == R.id.nav_chat) {
                startActivity(new Intent(AdminHome.this, ChatAdminList.class));
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(AdminHome.this, AccountAdmin.class));
                return true;
            }

            return false;
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        // Load lại danh sách từ SQLite
        fullProductList = dbHelper.getAllProducts();
        productAdapter.setFilteredList(fullProductList);
    }

}