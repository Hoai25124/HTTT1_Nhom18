package vn.edu.tlu.btl.appordpizza;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView imgProductDetail;
    private TextView tvProductNameDetail, tvProductPriceDetail, tvProductDescriptionDetail, tvQuantity;
    private Button btnDecrease, btnIncrease, btnAddToCart;
    private ImageButton btnBack;

    private int quantity = 1;
    private String name, price, description;
    private int imageResId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Ánh xạ view
        imgProductDetail = findViewById(R.id.imgProductDetail);
        tvProductNameDetail = findViewById(R.id.tvProductNameDetail);
        tvProductPriceDetail = findViewById(R.id.tvProductPriceDetail);
        tvProductDescriptionDetail = findViewById(R.id.tvProductDescriptionDetail);
        tvQuantity = findViewById(R.id.tvQuantity);
        btnDecrease = findViewById(R.id.btnDecrease);
        btnIncrease = findViewById(R.id.btnIncrease);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnBack = findViewById(R.id.btnBack);

        // Nhận dữ liệu từ Intent
        name = getIntent().getStringExtra("name");
        price = getIntent().getStringExtra("price");
        imageResId = getIntent().getIntExtra("imageResId", 0);
        description = getIntent().getStringExtra("description");

        // Hiển thị dữ liệu
        imgProductDetail.setImageResource(imageResId);
        tvProductNameDetail.setText(name);
        tvProductPriceDetail.setText(price);
        tvProductDescriptionDetail.setText(description);
        tvQuantity.setText(String.valueOf(quantity));

        // Tăng giảm số lượng
        btnIncrease.setOnClickListener(v -> {
            quantity++;
            tvQuantity.setText(String.valueOf(quantity));
        });

        btnDecrease.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                tvQuantity.setText(String.valueOf(quantity));
            }
        });

        // Thêm vào giỏ hàng
        btnAddToCart.setOnClickListener(v -> {
            CartItemModel cartItem = new CartItemModel(name, price, quantity, imageResId);
            CartManager.addToCart(cartItem, quantity);
            Toast.makeText(this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
        });

        // Quay lại
        btnBack.setOnClickListener(v -> finish());

        // Điều hướng giỏ hàng
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_cart) {
                startActivity(new Intent(ProductDetailActivity.this, CartActivity.class));
                return true;
            }
            if (item.getItemId() == R.id.nav_home) {
                startActivity(new Intent(ProductDetailActivity.this, UserHome.class));
                return true;
            }
            if (item.getItemId() == R.id.nav_chat) {
                startActivity(new Intent(ProductDetailActivity.this, ChatUser.class));
                return true;
            }
            return false;
        });
    }
}
