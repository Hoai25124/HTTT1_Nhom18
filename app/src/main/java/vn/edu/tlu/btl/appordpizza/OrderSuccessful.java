package vn.edu.tlu.btl.appordpizza;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class OrderSuccessful extends AppCompatActivity {

    private ImageButton btnBack;
    private Button btnContinueShopping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_successful);

        // Ánh xạ
        btnBack = findViewById(R.id.btnBack);
        btnContinueShopping = findViewById(R.id.btnContinueShopping);

        // Xử lý cả 2 nút về UserHome
        btnBack.setOnClickListener(v -> navigateToHome());
        btnContinueShopping.setOnClickListener(v -> navigateToHome());
    }

    private void navigateToHome() {
        Intent intent = new Intent(OrderSuccessful.this, UserHome.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
