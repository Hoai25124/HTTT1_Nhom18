package vn.edu.tlu.btl.appordpizza;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserChatAdapter extends RecyclerView.Adapter<UserChatAdapter.ViewHolder> {
    public interface OnUserClickListener {
        void onUserClick(UserChat userChat);
    }

    private Context context;
    private List<UserChat> userChatList;
    private OnUserClickListener listener;

    // Constructor nhận Context, danh sách và listener
    public UserChatAdapter(Context context, List<UserChat> userChatList, OnUserClickListener listener) {
        this.context = context;
        this.userChatList = userChatList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user_chat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserChat userChat = userChatList.get(position);
        holder.tvUserName.setText(userChat.getUserName());
        holder.tvLastMessage.setText(userChat.getLastMessage());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onUserClick(userChat);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userChatList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName, tvLastMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName    = itemView.findViewById(R.id.tvUserName);
            tvLastMessage = itemView.findViewById(R.id.tvLastMessage);
        }
    }
}
