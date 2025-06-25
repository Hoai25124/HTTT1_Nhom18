package vn.edu.tlu.btl.appordpizza;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItemModel> cartList;
    private OnCartChangeListener listener;

    public interface OnCartChangeListener {
        void onCartUpdated();
        void onItemRemoved(int position);
    }

    public CartAdapter(List<CartItemModel> cartList, OnCartChangeListener listener) {
        this.cartList = cartList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItemModel item = cartList.get(position);

        holder.tvName.setText(item.getName());
        holder.tvPrice.setText(item.getPrice());
        holder.tvQuantity.setText(String.valueOf(item.getQuantity()));
        holder.imgProduct.setImageResource(item.getImageResId());

        // Cộng số lượng
        holder.btnIncrease.setOnClickListener(v -> {
            item.setQuantity(item.getQuantity() + 1);
            notifyItemChanged(position);
            listener.onCartUpdated();
        });

        // Trừ số lượng
        holder.btnDecrease.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);
                notifyItemChanged(position);
                listener.onCartUpdated();
            }
        });

        // Xoá sản phẩm
        holder.btnDelete.setOnClickListener(v -> {
            listener.onItemRemoved(holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvName, tvPrice, tvQuantity;
        Button btnIncrease, btnDecrease;
        ImageView btnDelete;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgCart);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            btnIncrease = itemView.findViewById(R.id.btnPlus);
            btnDecrease = itemView.findViewById(R.id.btnMinus);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

}
