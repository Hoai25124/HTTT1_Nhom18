package vn.edu.tlu.btl.appordpizza;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CheckoutActivity extends AppCompatActivity {

    private static final int REQUEST_CHANGE_ADDRESS = 1;

    private ImageButton btnBack;
    private TextView tvChangeAddress, tvPhone, tvAddress;
    private TextView tvTotalItems, tvShippingFee, tvTotalAmount;
    private RadioButton rbCOD, rbBank;
    private Button btnPlaceOrder;
    private RadioGroup radioDelivery;
    private RadioButton rbStandard, rbSchedule;
    private LinearLayout layoutTimeSelect;
    private TextView tvSelectedTime;

    private static final int SHIPPING_FEE = 15000;

    private String currentPhone;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        btnBack = findViewById(R.id.btnBack);
        tvChangeAddress = findViewById(R.id.tvChangeAddress);
        tvPhone = findViewById(R.id.tvPhone);
        tvAddress = findViewById(R.id.tvAddress);
        tvTotalItems = findViewById(R.id.tvTotalItems);
        tvShippingFee = findViewById(R.id.tvShippingFee);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        rbCOD = findViewById(R.id.rbCOD);
        rbBank = findViewById(R.id.rbBank);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        radioDelivery = findViewById(R.id.radioDelivery);
        rbStandard = findViewById(R.id.rbStandard);
        rbSchedule = findViewById(R.id.rbSchedule);
        layoutTimeSelect = findViewById(R.id.layoutTimeSelect);
        tvSelectedTime = findViewById(R.id.tvSelectedTime);

        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        currentPhone = getSharedPreferences("USER", MODE_PRIVATE).getString("phone", "");

        loadUserInfo();
        updateTotal();

        btnBack.setOnClickListener(v -> finish());

        tvChangeAddress.setOnClickListener(v -> {
            Intent intent = new Intent(CheckoutActivity.this, ChangeAddress.class);
            startActivityForResult(intent, REQUEST_CHANGE_ADDRESS);
        });

        radioDelivery.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbSchedule) {
                layoutTimeSelect.setVisibility(View.VISIBLE);
            } else {
                layoutTimeSelect.setVisibility(View.GONE);
            }
        });

        tvSelectedTime.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePicker = new TimePickerDialog(this, (view, hourOfDay, minute1) -> {
                String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute1);
                tvSelectedTime.setText(selectedTime);
            }, hour, minute, true);

            timePicker.setTitle("Chọn giờ giao hàng");
            timePicker.show();
        });

        btnPlaceOrder.setOnClickListener(v -> {
            String phone = tvPhone.getText().toString();
            String address = tvAddress.getText().toString();

            if (address.isEmpty() || address.contains("Chưa có địa chỉ")) {
                Toast.makeText(this, "Vui lòng nhập địa chỉ trước khi đặt hàng!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Lấy tên người dùng từ database
            Cursor cursor = db.query("Users", null, "Phone=?", new String[]{currentPhone}, null, null, null);
            String fullName = "";
            if (cursor.moveToFirst()) {
                fullName = cursor.getString(cursor.getColumnIndexOrThrow("Name"));
            }
            cursor.close();

            List<CartItemModel> cartList = CartManager.getCart();
            if (cartList.isEmpty()) {
                Toast.makeText(this, "Giỏ hàng trống!", Toast.LENGTH_SHORT).show();
                return;
            }

            int total = 0;
            int totalQuantity = 0;
            StringBuilder foodDetailBuilder = new StringBuilder();

            for (CartItemModel item : cartList) {
                try {
                    int price = Integer.parseInt(item.getPrice().replaceAll("[^0-9]", ""));
                    int quantity = item.getQuantity();
                    total += price * quantity;
                    totalQuantity += quantity;
                    foodDetailBuilder.append(item.getName()).append(" (x").append(quantity).append("), ");
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            if (foodDetailBuilder.length() > 0) {
                foodDetailBuilder.setLength(foodDetailBuilder.length() - 2); // Xóa dấu ", " cuối
            }

            total += SHIPPING_FEE;

            String payment = rbCOD.isChecked() ? "Tiền mặt" : "Chuyển khoản";

            // Gọi hàm insertOrder
            dbHelper.insertOrder(fullName, phone, address, foodDetailBuilder.toString(), totalQuantity, total, payment);

            Toast.makeText(this, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();
            CartManager.clearCart();

            Intent intent = new Intent(CheckoutActivity.this, OrderSuccessful.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void loadUserInfo() {
        Cursor cursor = db.query("Users", null, "Phone=?", new String[]{currentPhone}, null, null, null);
        if (cursor.moveToFirst()) {
            String phone = cursor.getString(cursor.getColumnIndexOrThrow("Phone"));
            tvPhone.setText(phone);
            tvAddress.setText("Chưa có địa chỉ. Nhấn \"Thay đổi\" để nhập.");
        }
        cursor.close();
    }

    private void updateTotal() {
        List<CartItemModel> cartList = CartManager.getCart();
        int total = 0;

        for (CartItemModel item : cartList) {
            try {
                int price = Integer.parseInt(item.getPrice().replaceAll("[^0-9]", ""));
                total += price * item.getQuantity();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        NumberFormat formatter = NumberFormat.getInstance();
        tvTotalItems.setText("Tổng tiền hàng: " + formatter.format(total) + " VND");
        tvShippingFee.setText("Phí vận chuyển: " + formatter.format(SHIPPING_FEE) + " VND");
        tvTotalAmount.setText("Tổng thanh toán: " + formatter.format(total + SHIPPING_FEE) + " VND");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHANGE_ADDRESS && resultCode == RESULT_OK && data != null) {
            String address = data.getStringExtra("address");
            if (address != null && !address.isEmpty()) {
                tvAddress.setText(address);
            }
        }
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }
}
