package vn.edu.tlu.btl.appordpizza;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class EditAdmin extends AppCompatActivity {
    EditText edtName, edtPrice, edtDescription;
    Spinner spinnerCategory;
    ImageView imgBack, imgPreview;
    Button btnUpdate, btnChonAnh;

    Uri selectedImageUri = null;
    DatabaseHelper dbHelper;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private BottomNavigationView bottomnavigation;
    private int productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_admin);

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
        btnUpdate = findViewById(R.id.btnUpdate);
        imgBack = findViewById(R.id.imgBack);
        bottomnavigation = findViewById(R.id.bottomnavigation);
        dbHelper = new DatabaseHelper(this);

        // Quay lại
        imgBack.setOnClickListener(v -> {
            startActivity(new Intent(EditAdmin.this, AdminHome.class));
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

        // Bottom nav
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

        // Nhận dữ liệu từ Intent
        productId = getIntent().getIntExtra("product_id", -1);
        Toast.makeText(this, "Received ID: " + productId, Toast.LENGTH_SHORT).show();
        String name = getIntent().getStringExtra("name");
        String price = getIntent().getStringExtra("price");
        String category = getIntent().getStringExtra("category");
        String description = getIntent().getStringExtra("description");
        String imageUriStr = getIntent().getStringExtra("imageResId");

        // Gán dữ liệu vào form
        edtName.setText(name);
        edtPrice.setText(price);
        edtDescription.setText(description);
        //    if (imageUriStr != null && imageUriStr.startsWith("content://")) {
        //        selectedImageUri = Uri.parse(imageUriStr);
        //        imgPreview.setImageURI(selectedImageUri);}

        // Thiết lập Spinner (đã dùng entries trong XML)
        if (category != null) {
            String[] categoryArray = getResources().getStringArray(R.array.loai);
            for (int i = 0; i < categoryArray.length; i++) {
                if (categoryArray[i].equalsIgnoreCase(category)) {
                    spinnerCategory.setSelection(i);
                    break;
                }
            }
        }

        // Cập nhật dữ liệu
        btnUpdate.setOnClickListener(view -> {
            String newName = edtName.getText().toString().trim();
            String newPrice = edtPrice.getText().toString().trim();
            String newDescription = edtDescription.getText().toString().trim();
            String newCategory = spinnerCategory.getSelectedItem().toString();

            if (newName.isEmpty() || newPrice.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ tên và giá", Toast.LENGTH_SHORT).show();
                return;
            }

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("Name", newName);
            values.put("Price", newPrice);
            values.put("Category", newCategory);
            values.put("Description", newDescription);

            if (selectedImageUri != null) {
                values.put("imageResId", selectedImageUri.toString());
            }

            int rows = db.update("Products", values, "Id=?", new String[]{String.valueOf(productId)});
            if (rows > 0) {
                Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
