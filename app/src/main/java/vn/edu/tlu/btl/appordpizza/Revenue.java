package vn.edu.tlu.btl.appordpizza;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat;
import java.util.Calendar;

public class Revenue extends AppCompatActivity {

    private Spinner spinner;
    private TextView txtDate, txtTongDoanhThu;
    private DatabaseHelper dbHelper;
    private String selectedType = "Ngày";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revenue);

        spinner = findViewById(R.id.spinnerType);
        txtDate = findViewById(R.id.txtDate);
        txtTongDoanhThu = findViewById(R.id.txtTongDoanhThu);
        ImageView imgBack = findViewById(R.id.imgBack);

        dbHelper = new DatabaseHelper(this);

        // Set Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"Ngày", "Tháng", "Năm"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Sự kiện chọn loại thống kê
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedType = parent.getItemAtPosition(position).toString();
                txtDate.setText("Chọn " + selectedType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Chọn ngày/tháng/năm
        txtDate.setOnClickListener(v -> showDatePicker());

        // Nút quay lại
        imgBack.setOnClickListener(v -> finish());
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    selectedMonth += 1; // Tháng bắt đầu từ 0
                    String dateDisplay = "";

                    int totalRevenue = 0;

                    if (selectedType.equals("Ngày")) {
                        dateDisplay = selectedDay + "/" + selectedMonth + "/" + selectedYear;
                        totalRevenue = dbHelper.getRevenueByDay(selectedDay, selectedMonth, selectedYear);
                    } else if (selectedType.equals("Tháng")) {
                        dateDisplay = "Tháng " + selectedMonth + "/" + selectedYear;
                        totalRevenue = dbHelper.getRevenueByMonth(selectedMonth, selectedYear);
                    } else if (selectedType.equals("Năm")) {
                        dateDisplay = "Năm " + selectedYear;
                        totalRevenue = dbHelper.getRevenueByYear(selectedYear);
                    }

                    txtDate.setText(dateDisplay);

                    // Định dạng số có dấu phẩy
                    NumberFormat formatter = NumberFormat.getInstance();
                    String formattedRevenue = formatter.format(totalRevenue);

                    txtTongDoanhThu.setText("Tổng doanh thu: " + formattedRevenue + " VNĐ");
                }, year, month, day);
        datePickerDialog.show();
    }
}
