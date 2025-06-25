package vn.edu.tlu.btl.appordpizza;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    private EditText edtPhone, edtPassword;
    private Button btnLogin;
    private ImageView imgBack;
    private TextView tvRegister;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Ánh xạ view
        edtPhone = findViewById(R.id.edtPhone);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        imgBack = findViewById(R.id.imgBack);
        tvRegister = findViewById(R.id.tvRegister);

        dbHelper = new DatabaseHelper(this);

        // Xử lý chuyển sang màn hình đăng ký
        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, Signup.class);
            startActivity(intent);
        });

        // Xử lý nút quay lại
        imgBack.setOnClickListener(v -> {
            startActivity(new Intent(Login.this, MainActivity.class));
            finish();
        });

        // Xử lý nút đăng nhập
        btnLogin.setOnClickListener(v -> {
            String phone = edtPhone.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            if (phone.isEmpty() || password.isEmpty()) {
                Toast.makeText(Login.this, "Vui lòng nhập đầy đủ SĐT và mật khẩu", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kiểm tra đăng nhập từ SQLite
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT VaiTro FROM Users WHERE Phone = ? AND Pass = ?", new String[]{phone, password});

            if (cursor.moveToFirst()) {
                String role = cursor.getString(0); // Lấy VaiTro

                // ✅ Lưu số điện thoại để dùng sau (ví dụ: lưu yêu thích)
                SharedPreferences.Editor editor = getSharedPreferences("USER", MODE_PRIVATE).edit();
                editor.putString("phone", phone);
                editor.apply();

                // ✅ Điều hướng theo vai trò
                if (role.equalsIgnoreCase("Admin")) {
                    startActivity(new Intent(Login.this, AdminHome.class));
                } else {
                    startActivity(new Intent(Login.this, UserHome.class));
                }

                finish(); // kết thúc login activity
            } else {
                Toast.makeText(Login.this, "Sai SĐT hoặc mật khẩu", Toast.LENGTH_SHORT).show();
            }

            cursor.close();
            db.close();
        });
    }
}
