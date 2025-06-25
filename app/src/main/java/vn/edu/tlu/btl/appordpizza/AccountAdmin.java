package vn.edu.tlu.btl.appordpizza;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AccountAdmin extends AppCompatActivity {
    ImageView imgback,imginsert,imgdt,imgchat,imgdx;
    EditText edtTen;
    BottomNavigationView bottomnavigation;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private LinearLayout layoutLogout, addFood, viewOrders, viewRevenue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account_admin);

        imgback = findViewById(R.id.imgBack);
        imginsert = findViewById(R.id.imginsert);
        imgdt = findViewById(R.id.imgdt);
        imgdx = findViewById(R.id.imgdx);
        edtTen = findViewById(R.id.edtTen);
        bottomnavigation = findViewById(R.id.bottomnavigation);
        viewOrders = findViewById(R.id.viewOrders);
        addFood = findViewById(R.id.addFood);
        viewRevenue = findViewById(R.id.viewRevenue);
        layoutLogout = findViewById(R.id.layoutLogout);
        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getReadableDatabase();

        SharedPreferences preferences = getSharedPreferences("USER", MODE_PRIVATE);
        String phone = preferences.getString("phone", "");

        String name = dbHelper.getUserName(phone);
        if (name != null) {
            edtTen.setText(name);
        }

        addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountAdmin.this, InsertAdmin.class);
                startActivity(intent);

            }
        });
        viewRevenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountAdmin.this, Revenue.class);
                startActivity(intent);

            }
        });
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountAdmin.this,AdminHome.class);
                startActivity(intent);

            }
        });
        viewOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountAdmin.this,AdminOrder.class);
                startActivity(intent);

            }
        });
        layoutLogout.setOnClickListener(v -> {
            new androidx.appcompat.app.AlertDialog.Builder(AccountAdmin.this)
                    .setTitle("Xác nhận đăng xuất")
                    .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                    .setPositiveButton("Đăng xuất", (dialog, which) -> {
                        // Xóa thông tin đăng nhập
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.apply();

                        Intent intent = new Intent(AccountAdmin.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });
        bottomnavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                Intent intent = new Intent(this, AdminHome.class);
                startActivity(intent);
                return true;
            } if (id == R.id.nav_chat) {
                Intent intent = new Intent(this, ChatAdminList.class);
                startActivity(intent);
                return true;
            }
            else if (id == R.id.nav_profile) {
                return true;
            }
            return false;
        });
    }
}