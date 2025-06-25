package vn.edu.tlu.btl.appordpizza;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_SENT = 1;
    private static final int TYPE_RECEIVED = 2;

    private List<Message> messageList;
    private boolean isCurrentUserUser; // true nếu đang đăng nhập bằng tài khoản người dùng

    public MessageAdapter(List<Message> messageList, boolean isCurrentUserUser) {
        this.messageList = messageList;
        this.isCurrentUserUser = isCurrentUserUser;
    }

    @Override
    public int getItemViewType(int position) {
        boolean isSentByUser = messageList.get(position).isSentByUser();
        // Nếu người hiện tại là User và tin nhắn do User gửi -> gửi
        // Nếu người hiện tại là Admin và tin nhắn do Admin gửi -> gửi
        // Ngược lại là nhận
        if (isSentByUser == isCurrentUserUser) {
            return TYPE_SENT;
        } else {
            return TYPE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_SENT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_sent, parent, false);
            return new SentMessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_received, parent, false);
            return new ReceivedMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message msg = messageList.get(position);
        if (holder.getItemViewType() == TYPE_SENT) {
            ((SentMessageViewHolder) holder).tvMessage.setText(msg.getContent());
        } else {
            ((ReceivedMessageViewHolder) holder).tvMessage.setText(msg.getContent());
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    // ViewHolder gửi
    public static class SentMessageViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage;

        public SentMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessage);
        }
    }

    // ViewHolder nhận
    public static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage;

        public ReceivedMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessage);
        }
    }
}

