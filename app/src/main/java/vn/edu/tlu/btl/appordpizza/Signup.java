package vn.edu.tlu.btl.appordpizza;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Signup extends AppCompatActivity {
    TextView tvLogin;
    EditText edtName, edtPhone, edtPassword, edtRePassword;
    Button btnRegister;
    ImageView imgBack;
    DatabaseHelper dbHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        // Ánh xạ
        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        edtPassword = findViewById(R.id.edtPassword);
        edtRePassword = findViewById(R.id.edtRePassword);
        btnRegister = findViewById(R.id.btnRegister);
        imgBack = findViewById(R.id.imgBack);
        dbHelper = new DatabaseHelper(this);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Quay về MainActivity
                Intent intent = new Intent(Signup.this, MainActivity.class);
                startActivity(intent);
                finish(); // optional: đóng Login để không quay lại bằng nút back
            }
        });

        tvLogin  = findViewById(R.id.tvLogin);
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Signup.this, Login.class);
                startActivity(intent);
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edtName.getText().toString().trim();
                String phone = edtPhone.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                String rePassword = edtRePassword.getText().toString().trim();

                if (name.isEmpty() || phone.isEmpty() || password.isEmpty() || rePassword.isEmpty()) {
                    Toast.makeText(Signup.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(rePassword)) {
                    Toast.makeText(Signup.this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Kiểm tra trùng SĐT
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE Phone = ?", new String[]{phone});
                if (cursor.moveToFirst()) {
                    Toast.makeText(Signup.this, "SĐT đã được sử dụng", Toast.LENGTH_SHORT).show();
                    cursor.close();
                    return;
                }
                cursor.close();

                // Thêm user mới
                SQLiteDatabase writeDb = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("Phone", phone);
                values.put("Pass", password);
                values.put("VaiTro", "User"); // mặc định là người dùng
                values.put("Name", name);

                long result = writeDb.insert("Users", null, values);
                if (result != -1) {
                    Toast.makeText(Signup.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Signup.this, Login.class));
                    finish();
                } else {
                    Toast.makeText(Signup.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

}