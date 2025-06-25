package vn.edu.tlu.btl.appordpizza;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ViewOut extends AppCompatActivity {

    private RecyclerView recyclerCategory, recyclerProduct;
    private ProductAdapter2 productAdapter;
    private List<ProductModel> fullProductList;         // Danh sách tất cả sản phẩm
    private List<ProductModel> currentFilteredList;     // Danh sách sau khi lọc
    private EditText edtSearch;
    private TextView tvLogin;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_out); // Layout phải có đủ recyclerCategory, recyclerProduct, etSearch, tvLogin

        // Ánh xạ view
        recyclerCategory = findViewById(R.id.recyclerCategory);
        recyclerProduct = findViewById(R.id.recyclerProduct);
        edtSearch = findViewById(R.id.etSearch);
        tvLogin = findViewById(R.id.tvLogin);

        dbHelper = new DatabaseHelper(this);

        // 1. Load dữ liệu sản phẩm từ SQLite
        fullProductList = dbHelper.getAllProducts();
        currentFilteredList = new ArrayList<>(fullProductList);

        // 2. Danh mục sản phẩm
        List<String> categoryList = new ArrayList<>();
        categoryList.add("Tất cả");
        categoryList.add("Pizza");
        categoryList.add("Món khai vị");
        categoryList.add("Thức uống");

        CategoryAdapter categoryAdapter = new CategoryAdapter(categoryList, this::filterProductsByCategory);
        recyclerCategory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerCategory.setAdapter(categoryAdapter);

        // 3. Hiển thị sản phẩm
        productAdapter = new ProductAdapter2(currentFilteredList);
        recyclerProduct.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerProduct.setAdapter(productAdapter);

        // 4. Tìm kiếm sản phẩm theo tên
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                String keyword = s.toString().toLowerCase().trim();
                filterProductsByName(keyword);
            }
        });

        // 5. Xử lý nút đăng nhập
        tvLogin.setOnClickListener(v -> {
            Intent intent = new Intent(ViewOut.this, Login.class);
            startActivity(intent);
        });
    }

    // Lọc sản phẩm theo danh mục
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
        String currentKeyword = edtSearch.getText().toString().toLowerCase().trim();
        filterProductsByName(currentKeyword);
    }

    // Lọc sản phẩm theo từ khóa tìm kiếm
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
            productAdapter.notifyDataSetChanged(); // Làm mới giao diện nếu có thay đổi
        }
    }
}
