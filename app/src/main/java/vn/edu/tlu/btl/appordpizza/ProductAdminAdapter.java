package vn.edu.tlu.btl.appordpizza;

import android.content.Context;
import android.content.Intent;
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

public class ProductAdminAdapter extends RecyclerView.Adapter<ProductAdminAdapter.ViewHolder> {

    private List<ProductModel> productList;
    private Context context;

    public ProductAdminAdapter(Context context, List<ProductModel> productList) {
        this.context = context;
        this.productList = productList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct, btnEdit, btnDelete;
        TextView tvName, tvPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvProductPrice);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_admin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductModel product = productList.get(position);

        // ✅ Không dùng Glide → dùng trực tiếp resource ID
        holder.imgProduct.setImageResource(product.getImageResId());

        holder.tvName.setText(product.getName());
        holder.tvPrice.setText(product.getPrice());

        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditAdmin.class);
            intent.putExtra("product_id", product.getId());
            intent.putExtra("name", product.getName());
            intent.putExtra("price", product.getPrice());
            intent.putExtra("category", product.getCategory());
            intent.putExtra("imageResId", product.getImageResId());
            intent.putExtra("description", product.getDescription());
            context.startActivity(intent);
        });

        holder.btnDelete.setOnClickListener(v -> {
            new androidx.appcompat.app.AlertDialog.Builder(context)
                    .setTitle("Xác nhận xoá")
                    .setMessage("Bạn có chắc chắn muốn xoá sản phẩm này?")
                    .setPositiveButton("Xoá", (dialog, which) -> {
                        DatabaseHelper dbHelper = new DatabaseHelper(context);
                        SQLiteDatabase db = dbHelper.getWritableDatabase();

                        int rows = db.delete("Products", "id=?", new String[]{String.valueOf(product.getId())});
                        db.close();

                        if (rows > 0) {
                            Toast.makeText(context, "Đã xoá sản phẩm", Toast.LENGTH_SHORT).show();
                            productList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, productList.size());
                        } else {
                            Toast.makeText(context, "Xoá thất bại", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Huỷ", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void setFilteredList(List<ProductModel> filteredList) {
        this.productList = filteredList;
        notifyDataSetChanged();
    }
}
