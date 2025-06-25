package vn.edu.tlu.btl.appordpizza;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdminOrder extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OrderHistoryAdapter orderAdapter;
    private List<OrderHistoryModel> orderList;
    private List<OrderHistoryModel> filteredList;
    private ImageView imgBack, btnSearch;
    private EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order);

        imgBack = findViewById(R.id.imgBack);
        recyclerView = findViewById(R.id.recyclerOrderHistory);
        etSearch = findViewById(R.id.etSearch);
        btnSearch = findViewById(R.id.btnSearch);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        orderList = loadAllOrders(); // Lấy tất cả đơn hàng từ SQLite
        filteredList = new ArrayList<>(orderList); // Danh sách lọc

        orderAdapter = new OrderHistoryAdapter(filteredList);
        recyclerView.setAdapter(orderAdapter);

        imgBack.setOnClickListener(v -> finish());

        btnSearch.setOnClickListener(v -> performSearch());

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                performSearch(); // Tự động tìm khi gõ
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    private List<OrderHistoryModel> loadAllOrders() {
        DatabaseHelper db = new DatabaseHelper(this);
        return db.getAllOrders();
    }

    private void performSearch() {
        String keyword = etSearch.getText().toString().trim().toLowerCase();
        filteredList.clear();

        if (keyword.isEmpty()) {
            filteredList.addAll(orderList);
        } else {
            for (OrderHistoryModel order : orderList) {
                if (order.getName().toLowerCase().contains(keyword) ||
                        order.getTime().toLowerCase().contains(keyword)) {
                    filteredList.add(order);
                }
            }
        }
        orderAdapter.notifyDataSetChanged();
    }
}
