package vn.edu.tlu.btl.appordpizza;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.NumberFormat;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView tvTotal;
    private Button btnOrder;
    private CartAdapter adapter;
    private ImageView btnBack;
    private List<CartItemModel> cartList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.recyclerCart);
        tvTotal = findViewById(R.id.tvTotal);
        btnOrder = findViewById(R.id.btnOrder);
        btnBack = findViewById(R.id.btnBack);

        try {
            cartList = CartManager.getCart();

            adapter = new CartAdapter(cartList, new CartAdapter.OnCartChangeListener() {
                @Override
                public void onCartUpdated() {
                    updateTotal();
                }

                @Override
                public void onItemRemoved(int position) {
                    cartList.remove(position);
                    adapter.notifyItemRemoved(position);
                    updateTotal();
                }
            });

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);

            updateTotal();
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi giỏ hàng: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        btnOrder.setOnClickListener(v -> {
            if (cartList.isEmpty()) {
                Toast.makeText(this, "Giỏ hàng đang trống!", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();
            CartManager.clearCart();
            adapter.notifyDataSetChanged();
            updateTotal();
        });

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, UserHome.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
                startActivity(intent);
            }
        });
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_profile) {
                Intent intent = new Intent(CartActivity.this, ProfileActivity.class);
                startActivity(intent);
                return true;
            }
            if (itemId == R.id.nav_home) {
                Intent intent = new Intent(CartActivity.this, UserHome.class);
                startActivity(intent);
                return true;
            }
            if (itemId == R.id.nav_chat) {
                Intent intent = new Intent(CartActivity.this, ChatUser.class);
                startActivity(intent);
                return true;
            }
            // Xử lý nút khác nếu cần
            return true;
        });
    }

    private void updateTotal() {
        int total = 0;
        for (CartItemModel item : cartList) {
            try {
                int price = Integer.parseInt(item.getPrice().replace(".", "").replace("đ", "").replace(",", ""));
                total += price * item.getQuantity();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        tvTotal.setText(NumberFormat.getInstance().format(total) + " VND");
    }
}
