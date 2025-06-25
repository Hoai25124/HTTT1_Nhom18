package vn.edu.tlu.btl.appordpizza;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class    ChatUser extends AppCompatActivity {

    private RecyclerView recyclerChat;
    private EditText edtMessage;
    private ImageView btnSend, imgBack;
    private BottomNavigationView bottomNavigation;

    private DatabaseHelper db;
    private String currentPhone;
    private List<Message> messageList;
    private MessageAdapter messageAdapter;

    // Flag để chỉ gửi greeting một lần
    private boolean isFirstUserMessage = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat_user);

        // --- 1. Đọc số điện thoại user từ SharedPreferences ---
        SharedPreferences prefs = getSharedPreferences("USER", MODE_PRIVATE);
        currentPhone = prefs.getString("phone", "");

        // --- 2. Ánh xạ view ---
        recyclerChat     = findViewById(R.id.recyclerChat);
        edtMessage       = findViewById(R.id.edtMessage);
        btnSend          = findViewById(R.id.btnSend);
        imgBack          = findViewById(R.id.imgBack);
        bottomNavigation = findViewById(R.id.bottomNavigation);

        // --- 3. Khởi tạo DB và load lịch sử chat ---
        db = new DatabaseHelper(this);
        messageList = db.getMessagesByUser(currentPhone);

        // --- 4. Gắn adapter ---
        messageAdapter = new MessageAdapter(messageList, true); // true = đang dùng app với tư cách User

        recyclerChat.setLayoutManager(new LinearLayoutManager(this));
        recyclerChat.setAdapter(messageAdapter);

        // --- 5. Nút back ---
        imgBack.setOnClickListener(v -> finish());

        // --- 6. Xử lý gửi tin nhắn ---
        btnSend.setOnClickListener(v -> {
            String content = edtMessage.getText().toString().trim();
            if (!content.isEmpty()) {
                // 6.1 Lưu tin nhắn user vào SQLite
                db.insertMessage(content, true, currentPhone);

                // 6.2 Hiển thị tin nhắn user
                Message userMsg = new Message(content, true);
                messageList.add(userMsg);
                messageAdapter.notifyItemInserted(messageList.size() - 1);
                recyclerChat.scrollToPosition(messageList.size() - 1);
                edtMessage.setText("");

                // 6.3 Nếu là tin nhắn đầu tiên, admin gửi greeting
                if (isFirstUserMessage) {
                    isFirstUserMessage = false;
                    recyclerChat.postDelayed(() -> {
                        String greeting = "Xin chào bạn! Mình có thể giúp gì được cho bạn?";
                        // Lưu greeting vào SQLite dưới dạng admin gửi (isUser=false)
                        db.insertMessage(greeting, false, currentPhone);
                        // Hiển thị greeting
                        Message adminMsg = new Message(greeting, false);
                        messageList.add(adminMsg);
                        messageAdapter.notifyItemInserted(messageList.size() - 1);
                        recyclerChat.scrollToPosition(messageList.size() - 1);
                    }, 800);
                }
            }
        });

        // --- 7. Cấu hình BottomNavigationView ---
        bottomNavigation.setSelectedItemId(R.id.nav_chat);
        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, UserHome.class));
                finish();
                return true;
            }
            if (id == R.id.nav_cart) {
                startActivity(new Intent(this, CartActivity.class));
                finish();
                return true;
            }
            if (id == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                finish();
                return true;
            }
            return id == R.id.nav_chat;
        });

        // --- 8. Xử lý Insets để tránh bị che khuất ---
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });
    }
}
