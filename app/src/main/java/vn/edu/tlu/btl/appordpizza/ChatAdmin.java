package vn.edu.tlu.btl.appordpizza;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdmin extends AppCompatActivity {

    private RecyclerView recyclerChat;
    private EditText edtMessage;
    private ImageView btnSend, imgBack;
    private TextView tvChatWith;

    private String userPhone;
    private DatabaseHelper db;
    private List<Message> messageList;
    private MessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat_admin);

        // Xử lý insets (không thay đổi)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ view
        recyclerChat = findViewById(R.id.recyclerChat);
        edtMessage = findViewById(R.id.edtMessage);
        btnSend = findViewById(R.id.btnSend);
        imgBack = findViewById(R.id.imgBack);
        tvChatWith = findViewById(R.id.tvChatWith);

        // Khởi tạo DB
        db = new DatabaseHelper(this);

        // Nhận số điện thoại user từ Intent
        Intent intent = getIntent();
        // ChatAdmin.java
        userPhone = getIntent().getStringExtra("userPhone");


        // Hiển thị tên người dùng
        String userName = db.getUserNameByPhone(userPhone);
        tvChatWith.setText("Chat với " + userName);

        // Load lịch sử chat từ DB
        messageList = db.getMessagesByUser(userPhone);
        adapter = new MessageAdapter(messageList, false); // false = đang dùng app với tư cách Admin

        recyclerChat.setLayoutManager(new LinearLayoutManager(this));
        recyclerChat.setAdapter(adapter);

        // Gửi tin nhắn
        btnSend.setOnClickListener(v -> {
            String content = edtMessage.getText().toString().trim();
            if (!content.isEmpty()) {
                // Lưu vào DB
                db.insertMessage(content, false, userPhone); // false = admin gửi
                // Cập nhật giao diện
                messageList.add(new Message(content, false));
                adapter.notifyItemInserted(messageList.size() - 1);
                recyclerChat.scrollToPosition(messageList.size() - 1);
                edtMessage.setText("");
            }
        });

        // Quay lại màn hình trước
        imgBack.setOnClickListener(v -> finish());
    }
}
