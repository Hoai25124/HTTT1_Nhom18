package vn.edu.tlu.btl.appordpizza;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.provider.MediaStore;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class InsertAdmin extends AppCompatActivity {

    EditText edtName, edtPrice, edtDescription;
    Spinner spinnerCategory;
    ImageView imgBack, imgPreview;
    Button btnInsert, btnChonAnh;

    Uri selectedImageUri = null;
    DatabaseHelper dbHelper;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private BottomNavigationView bottomnavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_insert_admin);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ view
        edtName = findViewById(R.id.edtName);
        edtPrice = findViewById(R.id.edtPrice);
        edtDescription = findViewById(R.id.edtDescription);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        imgPreview = findViewById(R.id.imgPreview);
        btnChonAnh = findViewById(R.id.btnChonAnh);
        btnInsert = findViewById(R.id.btnInsert);
        imgBack = findViewById(R.id.imgBack);
        bottomnavigation = findViewById(R.id.bottomnavigation);
        dbHelper = new DatabaseHelper(this);

        // Quay lại
        imgBack.setOnClickListener(v -> {
            startActivity(new Intent(InsertAdmin.this, AccountAdmin.class));
            finish();
        });

        // Đăng ký launcher chọn ảnh
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        imgPreview.setImageURI(selectedImageUri);
                    }
                }
        );

        // Mở thư viện ảnh
        btnChonAnh.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

        // Xử lý thêm món
        btnInsert.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String price = edtPrice.getText().toString().trim();
            String description = edtDescription.getText().toString().trim();
            String category = spinnerCategory.getSelectedItem().toString();

            if (name.isEmpty() || price.isEmpty() || selectedImageUri == null) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Lưu vào database
            dbHelper.insertMonAn(name, price, selectedImageUri.toString(), category, description);
            Toast.makeText(this, "Thêm món ăn thành công!", Toast.LENGTH_SHORT).show();

            // Reset lại form
            edtName.setText("");
            edtPrice.setText("");
            edtDescription.setText("");
            imgPreview.setImageResource(0);
            spinnerCategory.setSelection(0);
            selectedImageUri = null;
        });
        bottomnavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, AdminHome.class));
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, AccountAdmin.class));
                return true;
            }
            return false;
        });
    }
}
