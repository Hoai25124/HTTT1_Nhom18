package vn.edu.tlu.btl.appordpizza;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PassWord extends AppCompatActivity {

    private EditText edtOldPassword, edtNewPassword, edtConfirmPassword;
    private Button btnChangePassword;
    private ImageButton btnBack;

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private String currentPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_word);

        // Ánh xạ
        edtOldPassword = findViewById(R.id.edtOldPassword);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnBack = findViewById(R.id.btnBack);

        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        // Lấy số điện thoại từ SharedPreferences
        SharedPreferences preferences = getSharedPreferences("USER", MODE_PRIVATE);
        currentPhone = preferences.getString("phone", "");

        // Sự kiện quay lại
        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(PassWord.this, ProfileActivity.class));
            finish();
        });

        // Sự kiện đổi mật khẩu
        btnChangePassword.setOnClickListener(v -> handleChangePassword());
    }

    private void handleChangePassword() {
        String oldPass = edtOldPassword.getText().toString().trim();
        String newPass = edtNewPassword.getText().toString().trim();
        String confirmPass = edtConfirmPassword.getText().toString().trim();

        // Kiểm tra nhập đủ
        if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        // 🔐 Kiểm tra độ dài mật khẩu mới
        if (newPass.length() < 6) {
            Toast.makeText(this, "Mật khẩu mới phải có ít nhất 6 ký tự!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra mật khẩu mới trùng khớp
        if (!newPass.equals(confirmPass)) {
            Toast.makeText(this, "Mật khẩu mới không khớp!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra mật khẩu cũ
        Cursor cursor = db.query("Users", new String[]{"Pass"}, "Phone = ?", new String[]{currentPhone}, null, null, null);
        if (cursor.moveToFirst()) {
            String currentPassword = cursor.getString(cursor.getColumnIndexOrThrow("Pass"));
            if (!currentPassword.equals(oldPass)) {
                Toast.makeText(this, "Mật khẩu cũ không đúng!", Toast.LENGTH_SHORT).show();
                cursor.close();
                return;
            }
        } else {
            Toast.makeText(this, "Không tìm thấy tài khoản!", Toast.LENGTH_SHORT).show();
            return;
        }
        cursor.close();

        // Cập nhật lại mật khẩu mới
        ContentValues values = new ContentValues();
        values.put("Pass", newPass);
        int rows = db.update("Users", values, "Phone = ?", new String[]{currentPhone});

        if (rows > 0) {
            Toast.makeText(this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(PassWord.this, ProfileActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Có lỗi xảy ra khi cập nhật!", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onDestroy() {
        if (db != null && db.isOpen()) db.close();
        super.onDestroy();
    }
}
