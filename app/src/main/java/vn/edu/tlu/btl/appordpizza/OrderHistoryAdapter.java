package vn.edu.tlu.btl.appordpizza;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder> {

    private final List<OrderHistoryModel> orderList;

    public OrderHistoryAdapter(List<OrderHistoryModel> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_history, parent, false);
        return new OrderViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderHistoryModel order = orderList.get(position);

        holder.tvOrderId.setText("Mã đơn hàng: " + order.getOrderId());
        holder.tvName.setText("Họ tên: " + order.getName());
        holder.tvPhone.setText("SĐT: " + order.getPhone());
        holder.tvAddress.setText("Địa chỉ nhận: " + order.getAddress());
        holder.tvOrderTime.setText("Ngày đặt hàng: " + order.getTime());
        holder.tvFoodDetail.setText("Thực đơn: " + order.getFoodDetail());
        holder.tvQuantity.setText("Số lượng: " + order.getQuantity());
        holder.tvTotal.setText("Tổng tiền: " + NumberFormat.getInstance().format(order.getTotal()) + " VND");
        holder.tvPayment.setText("Thanh toán: " + order.getPayment());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvName, tvPhone, tvAddress, tvOrderTime,
                tvFoodDetail, tvQuantity, tvTotal, tvPayment;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvName = itemView.findViewById(R.id.tvName);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvOrderTime = itemView.findViewById(R.id.tvOrderTime);
            tvFoodDetail = itemView.findViewById(R.id.tvFoodDetail); // ← thêm dòng này
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            tvPayment = itemView.findViewById(R.id.tvPayment);
        }
    }
}
