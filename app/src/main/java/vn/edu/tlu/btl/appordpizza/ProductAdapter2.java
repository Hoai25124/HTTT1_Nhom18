package vn.edu.tlu.btl.appordpizza;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductAdapter2 extends RecyclerView.Adapter<ProductAdapter2.ViewHolder> {

    private List<ProductModel> productList;

    // Constructor
    public ProductAdapter2(List<ProductModel> productList) {
        this.productList = productList;
    }

    // Hàm cập nhật danh sách sau khi lọc
    public void setFilteredList(List<ProductModel> filteredList) {
        this.productList = filteredList;
        notifyDataSetChanged();
    }

    // ViewHolder quản lý các view trong item_product
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct, imgFavorite;
        TextView tvName, tvPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvProductPrice);
            imgFavorite = itemView.findViewById(R.id.imgFavorite);
        }
    }

    @NonNull
    @Override
    public ProductAdapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter2.ViewHolder holder, int position) {
        ProductModel product = productList.get(position);
        Context context = holder.itemView.getContext();

        // Lấy trạng thái yêu thích từ SharedPreferences
        SharedPreferences prefs = context.getSharedPreferences("Favorites", Context.MODE_PRIVATE);
        boolean isFavorite = prefs.getBoolean(product.getName(), false);
        product.setFavorite(isFavorite);

        // Cập nhật icon yêu thích
        holder.imgFavorite.setImageResource(isFavorite ? R.drawable.ic_redheart : R.drawable.ic_heart);

        // Xử lý click vào icon yêu thích
        holder.imgFavorite.setOnClickListener(v -> {
            boolean newFavState = !product.isFavorite();
            product.setFavorite(newFavState);

            // Cập nhật UI
            holder.imgFavorite.setImageResource(newFavState ? R.drawable.ic_redheart : R.drawable.ic_heart);

            // Lưu trạng thái mới vào SharedPreferences
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(product.getName(), newFavState);
            editor.apply();

            // Thông báo
            Toast.makeText(context,
                    newFavState ? "Đã thêm vào yêu thích" : "Đã bỏ khỏi yêu thích",
                    Toast.LENGTH_SHORT).show();
        });

        // Gán dữ liệu cho item
        holder.imgProduct.setImageResource(product.getImageResId());
        holder.tvName.setText(product.getName());
        holder.tvPrice.setText(product.getPrice());

        // Xử lý click vào sản phẩm -> mở ProductDetailActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailActivity2.class);
            intent.putExtra("name", product.getName());
            intent.putExtra("price", product.getPrice());
            intent.putExtra("imageResId", product.getImageResId());
            intent.putExtra("description", product.getDescription());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
