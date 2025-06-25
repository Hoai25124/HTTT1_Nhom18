package vn.edu.tlu.btl.appordpizza;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProductDetailActivity2 extends AppCompatActivity {

    private ImageView imgProductDetail;
    private TextView tvProductName, tvProductPrice, tvProductDescription, tvQuantity;
    private ImageButton btnBack;
    private Button btnDecrease, btnIncrease, btnAddToCart;

    private int quantity = 1;

    private String name, price, description;
    private int imageResId;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail2);

        // Ánh xạ view
        imgProductDetail = findViewById(R.id.imgProductDetail);
        tvProductName = findViewById(R.id.tvProductNameDetail);
        tvProductPrice = findViewById(R.id.tvProductPriceDetail);
        tvProductDescription = findViewById(R.id.tvProductDescriptionDetail);
        tvQuantity = findViewById(R.id.tvQuantity);
        btnBack = findViewById(R.id.btnBack);
        btnDecrease = findViewById(R.id.btnDecrease);
        btnIncrease = findViewById(R.id.btnIncrease);
        btnAddToCart = findViewById(R.id.btnAddToCart);

        sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);

        // Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        price = intent.getStringExtra("price");
        description = intent.getStringExtra("description");
        imageResId = intent.getIntExtra("imageResId", R.drawable.pizza1);

        // Hiển thị dữ liệu
        imgProductDetail.setImageResource(imageResId);
        tvProductName.setText(name);
        tvProductPrice.setText(price);
        tvProductDescription.setText(description);
        tvQuantity.setText(String.valueOf(quantity));

        // Tăng số lượng
        btnIncrease.setOnClickListener(v -> {
            quantity++;
            tvQuantity.setText(String.valueOf(quantity));
        });

        // Giảm số lượng
        btnDecrease.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                tvQuantity.setText(String.valueOf(quantity));
            }
        });

        // Nút quay lại
        btnBack.setOnClickListener(v -> finish());

        // Nút thêm vào giỏ hàng
        btnAddToCart.setOnClickListener(v -> {
            // Kiểm tra đăng nhập
            boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

            if (!isLoggedIn) {
                Toast.makeText(this, "Vui lòng đăng nhập để thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
