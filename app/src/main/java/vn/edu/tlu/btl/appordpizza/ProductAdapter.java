package vn.edu.tlu.btl.appordpizza;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<ProductModel> productList;

    public ProductAdapter(List<ProductModel> productList) {
        this.productList = productList;
    }

    public void setFilteredList(List<ProductModel> filteredList) {
        this.productList = filteredList;
        notifyDataSetChanged();
    }

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
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {
        ProductModel product = productList.get(position);
        Context context = holder.itemView.getContext();

        // Reset image trước khi set để tránh hiện ảnh sai do tái sử dụng ViewHolder
        holder.imgProduct.setImageDrawable(null);
        holder.imgProduct.setImageResource(product.getImageResId());

        // Set dữ liệu sản phẩm
        holder.tvName.setText(product.getName());
        holder.tvPrice.setText(product.getPrice());

        // Lấy số điện thoại từ SharedPreferences
        SharedPreferences pref = context.getSharedPreferences("USER", Context.MODE_PRIVATE);
        String phone = pref.getString("phone", "");

        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Kiểm tra sản phẩm có trong Favorites không
        boolean isFavorite = false;
        Cursor cursor = db.query("Favorites", null, "phone=? AND productName=?",
                new String[]{phone, product.getName()}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            isFavorite = true;
            cursor.close();
        }
        db.close();

        product.setFavorite(isFavorite);
        holder.imgFavorite.setImageResource(isFavorite ? R.drawable.ic_redheart : R.drawable.ic_heart);

        // Sự kiện click trái tim
        holder.imgFavorite.setOnClickListener(v -> {
            boolean newFavState = !product.isFavorite();
            product.setFavorite(newFavState);
            holder.imgFavorite.setImageResource(newFavState ? R.drawable.ic_redheart : R.drawable.ic_heart);

            SQLiteDatabase writableDb = dbHelper.getWritableDatabase();
            if (newFavState) {
                ContentValues values = new ContentValues();
                values.put("phone", phone);
                values.put("productName", product.getName());
                writableDb.insert("Favorites", null, values);
                Toast.makeText(context, "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
            } else {
                writableDb.delete("Favorites", "phone=? AND productName=?",
                        new String[]{phone, product.getName()});
                Toast.makeText(context, "Đã bỏ khỏi yêu thích", Toast.LENGTH_SHORT).show();
            }
            writableDb.close();
        });

        // Sự kiện click sản phẩm
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailActivity.class);
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
