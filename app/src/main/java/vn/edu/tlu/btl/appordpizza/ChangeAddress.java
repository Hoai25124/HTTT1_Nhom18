package vn.edu.tlu.btl.appordpizza;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ChangeAddress extends AppCompatActivity {

    EditText edtName, edtPhone, edtAddress;
    Button btnConfirm;
    ImageView btnBack;

    DatabaseHelper dbHelper;
    SQLiteDatabase db;
    String currentPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_address);

        // Ánh xạ view
        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        edtAddress = findViewById(R.id.edtAddress);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnBack = findViewById(R.id.btnBack);

        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getReadableDatabase();

        // Lấy số điện thoại từ SharedPreferences
        currentPhone = getSharedPreferences("USER", MODE_PRIVATE).getString("phone", "");

        // Load họ tên, sđt từ SQL
        loadUserData();

        // Nút xác nhận
        btnConfirm.setOnClickListener(v -> {
            String address = edtAddress.getText().toString().trim();
            if (address.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập địa chỉ", Toast.LENGTH_SHORT).show();
                return;
            }

            // Trả địa chỉ về CheckoutActivity
            Intent resultIntent = new Intent();
            resultIntent.putExtra("address", address);
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        // Nút quay lại
        btnBack.setOnClickListener(v -> finish());
    }

    private void loadUserData() {
        Cursor cursor = db.query("Users", null, "Phone=?", new String[]{currentPhone}, null, null, null);
        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow("Name"));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow("Phone"));

            edtName.setText(name);
            edtPhone.setText(phone);

            edtName.setEnabled(false);   // Không cho sửa
            edtPhone.setEnabled(false);  // Không cho sửa
        }
        cursor.close();
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }
}
