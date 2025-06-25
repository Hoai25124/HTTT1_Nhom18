package vn.edu.tlu.btl.appordpizza;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class UserHome extends AppCompatActivity {

    private RecyclerView recyclerCategory, recyclerProduct;
    private ProductAdapter productAdapter;
    private List<ProductModel> fullProductList;         // Tất cả sản phẩm từ SQLite
    private List<ProductModel> currentFilteredList;     // Danh sách hiện tại sau lọc/tìm kiếm
    private EditText edtSearch;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        recyclerCategory = findViewById(R.id.recyclerCategory);
        recyclerProduct = findViewById(R.id.recyclerProduct);
        edtSearch = findViewById(R.id.etSearch);

        dbHelper = new DatabaseHelper(this);

        // 1. Load danh sách sản phẩm từ SQLite
        fullProductList = dbHelper.getAllProducts();
        currentFilteredList = new ArrayList<>(fullProductList);

        // 2. Danh sách danh mục
        List<String> categoryList = new ArrayList<>();
        categoryList.add("Tất cả");
        categoryList.add("Pizza");
        categoryList.add("Món khai vị");
        categoryList.add("Thức uống");

        CategoryAdapter categoryAdapter = new CategoryAdapter(categoryList, this::filterProductsByCategory);
        recyclerCategory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerCategory.setAdapter(categoryAdapter);

        // 3. Hiển thị sản phẩm
        productAdapter = new ProductAdapter(currentFilteredList);
        recyclerProduct.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerProduct.setAdapter(productAdapter);

        // 4. Tìm kiếm
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                String keyword = s.toString().toLowerCase().trim();
                filterProductsByName(keyword);
            }
        });

        // 5. Điều hướng dưới cùng
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                startActivity(new Intent(this, UserHome.class));
                return true;
            }
            if (itemId == R.id.nav_cart) {
                startActivity(new Intent(UserHome.this, CartActivity.class));
                return true;
            }
            if (itemId == R.id.nav_profile) {
                startActivity(new Intent(UserHome.this, ProfileActivity.class));
                return true;
            }

            if (itemId == R.id.nav_chat) {
                startActivity(new Intent(UserHome.this, ChatUser.class));
                return true;
            }
            return true;
        });
    }

    // ✅ Lọc sản phẩm theo danh mục
    private void filterProductsByCategory(String category) {
        if (category.equalsIgnoreCase("Tất cả")) {
            currentFilteredList = new ArrayList<>(fullProductList);
        } else {
            currentFilteredList = new ArrayList<>();
            for (ProductModel product : fullProductList) {
                if (product.getCategory().equalsIgnoreCase(category)) {
                    currentFilteredList.add(product);
                }
            }
        }
        // Lọc theo từ khóa tìm kiếm hiện tại
        String currentKeyword = edtSearch.getText().toString().toLowerCase().trim();
        filterProductsByName(currentKeyword);
    }

    // ✅ Lọc theo từ khóa tìm kiếm
    private void filterProductsByName(String keyword) {
        List<ProductModel> result = new ArrayList<>();
        for (ProductModel product : currentFilteredList) {
            if (product.getName().toLowerCase().contains(keyword)) {
                result.add(product);
            }
        }
        productAdapter.setFilteredList(result);
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (productAdapter != null) {
            productAdapter.notifyDataSetChanged(); // Làm mới giao diện
        }
    }

}
