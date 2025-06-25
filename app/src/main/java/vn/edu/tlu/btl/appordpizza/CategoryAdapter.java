package vn.edu.tlu.btl.appordpizza;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private final List<String> categories;
    private int selectedPosition = 0; // Mặc định chọn "Tất cả"
    private final OnCategoryClickListener listener;

    public interface OnCategoryClickListener {
        void onCategorySelected(String category);
    }

    public CategoryAdapter(List<String> categories, OnCategoryClickListener listener) {
        this.categories = categories;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.tvCategoryName);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String category = categories.get(position);
        holder.tvCategory.setText(category);

        // Thay đổi giao diện khi được chọn
        if (position == selectedPosition) {
            holder.itemView.setBackgroundResource(R.drawable.bg_category_selected);
            holder.tvCategory.setTextColor(Color.WHITE);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.bg_category_unselected);
            holder.tvCategory.setTextColor(Color.BLACK);
        }

        holder.itemView.setOnClickListener(v -> {
            int previousPosition = selectedPosition;
            selectedPosition = holder.getAdapterPosition(); // Cập nhật vị trí mới

            // Cập nhật UI hai vị trí: cũ và mới
            notifyItemChanged(previousPosition);
            notifyItemChanged(selectedPosition);

            // Gửi danh mục được chọn về Activity
            if (listener != null) {
                listener.onCategorySelected(category);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
