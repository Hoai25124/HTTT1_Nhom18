package vn.edu.tlu.btl.appordpizza;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class ChatAdminList extends AppCompatActivity
        implements UserChatAdapter.OnUserClickListener {

    private RecyclerView recyclerUserList;
    private ImageView imgBack;
    private BottomNavigationView bottomNavigation;
    private UserChatAdapter adapter;
    private List<UserChat> userChats;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_admin_list);

        // Ánh xạ
        recyclerUserList = findViewById(R.id.recyclerUserList);
        imgBack          = findViewById(R.id.imgBack);
        bottomNavigation = findViewById(R.id.bottomNavigation);

        // Nút back
        imgBack.setOnClickListener(v -> finish());

        // Load từ SQLite
        db = new DatabaseHelper(this);
        userChats = db.getAllUsersChatted();  // List<UserChat> đã chứa phone, name, lastMessage

        // Adapter
        adapter = new UserChatAdapter(this, userChats, this);
        recyclerUserList.setLayoutManager(new LinearLayoutManager(this));
        recyclerUserList.setAdapter(adapter);

        // Bottom nav
        bottomNavigation.setSelectedItemId(R.id.nav_chat);
        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, AdminHome.class));
                finish();
                return true;
            }
            if (id == R.id.nav_profile) {
                startActivity(new Intent(this, AccountAdmin.class));
                finish();
                return true;
            }
            if (id == R.id.nav_chat) {
                return true; // đang ở list chat
            }
            return false;
        });
    }
    // Khi admin chọn 1 user trong list
    @Override
    public void onUserClick(UserChat userChat) {
        Intent intent = new Intent(this, ChatAdmin.class);
        intent.putExtra("userPhone", userChat.getPhone());
        startActivity(intent);
    }
}
