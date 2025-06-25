package vn.edu.tlu.btl.appordpizza;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvUserName, tvUserPhone;

    private LinearLayout layoutOrderHistory, layoutFavorites, layoutResetPassword, layoutLogout;

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Ánh xạ
        tvUserName = findViewById(R.id.tvUserName);
        tvUserPhone = findViewById(R.id.tvUserPhone);

        layoutOrderHistory = findViewById(R.id.layoutOrderHistory);
        layoutFavorites = findViewById(R.id.layoutFavorites);
        layoutResetPassword = findViewById(R.id.layoutResetPassword);
        layoutLogout = findViewById(R.id.layoutLogout);

        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getReadableDatabase();

        // Lấy số điện thoại từ SharedPreferences
        SharedPreferences preferences = getSharedPreferences("USER", MODE_PRIVATE);
        String phone = preferences.getString("phone", "");

        if (!phone.isEmpty()) {
            loadUserInfo(phone);
        } else {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
        }

        // Xử lý chuyển trang
        layoutOrderHistory.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, HistoryOrd.class));
        });

        layoutFavorites.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, LoveActivity.class));
        });

        layoutResetPassword.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, PassWord.class));
        });

        layoutLogout.setOnClickListener(v -> {
            new androidx.appcompat.app.AlertDialog.Builder(ProfileActivity.this)
                    .setTitle("Xác nhận đăng xuất")
                    .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                    .setPositiveButton("Đăng xuất", (dialog, which) -> {
                        // Xóa thông tin đăng nhập
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.apply();

                        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_cart) {
                Intent intent = new Intent(ProfileActivity.this, CartActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                return true;
            }

            if (itemId == R.id.nav_home) {
                Intent intent = new Intent(ProfileActivity.this, UserHome.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                return true;
            }

            return true;
        });



    }

    private void loadUserInfo(String phone) {
        Cursor cursor = db.query("Users", null, "Phone = ?", new String[]{phone}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("Name"));
            String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow("Phone"));

            tvUserName.setText("Tên: " + name);
            tvUserPhone.setText("SĐT: " + phoneNumber);

            cursor.close();
        } else {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        if (db != null && db.isOpen()) db.close();
        super.onDestroy();
    }
}
