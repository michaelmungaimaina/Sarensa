package mich.gwan.sarensa.adapters;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import mich.gwan.sarensa.R;
import mich.gwan.sarensa.activities.SaleCategoryViewActivity;
import mich.gwan.sarensa.model.Receipt;
import mich.gwan.sarensa.model.Sales;

public class ReceiptRecyclerAdapter extends RecyclerView.Adapter<ReceiptRecyclerAdapter.ViewHolder>{
    private final List<Receipt> list;
    private int index = RecyclerView.NO_POSITION;
    private int count = 0;

    public ReceiptRecyclerAdapter(List<Receipt> list) {
        this.list = list;
    }

    @Override
    public ReceiptRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflating recycler item view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.receipt_recycler, parent, false);

        return new ReceiptRecyclerAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReceiptRecyclerAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        char firstChar = list.get(position).getReceiptTransType().charAt(0);
        char secondChar = list.get(position).getReceiptTransType().charAt(1);
        holder.date.setText(list.get(position).getReceiptDate());
        holder.time.setText(list.get(position).getReceiptTime());
        holder.type.setText(String.valueOf(firstChar + secondChar));
        holder.categoryName.setText(list.get(position).getItemCategory());
        holder.itemName.setText(list.get(position).getItemName());
        holder.quantity.setText(String.valueOf(list.get(position).getItemQuantity()));
        holder.unitPrice.setText(String.valueOf(list.get(position).getSellingPrice()));
        holder.total.setText(String.valueOf(list.get(position).getTotal()));
        holder.customerName.setText(String.valueOf(list.get(position).getCustomerName()));

        holder.itemView.setSelected(index == position);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                index = holder.getLayoutPosition();
                notifyItemChanged(index);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.v(ReceiptRecyclerAdapter.class.getSimpleName(),""+list.size());
        return list.size();
    }


    /**
     * ViewHolder class
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView date;
        public TextView time;
        public TextView type;
        public TextView categoryName;
        public TextView customerName;
        public TextView itemName;
        public TextView quantity;
        public TextView unitPrice;
        public TextView total;

        public ViewHolder(View view) {
            super(view);
            date = view.findViewById(R.id.txtDate);
            time = view.findViewById(R.id.txtTime);
            type = view.findViewById(R.id.txtSaleType);
            categoryName = view.findViewById(R.id.txtCategory);
            itemName = view.findViewById(R.id.txtItemName);
            quantity = view.findViewById(R.id.txtQuantity);
            unitPrice = view.findViewById(R.id.textUnitPrice);
            total = view.findViewById(R.id.textTotal);
            customerName = view.findViewById(R.id.txtCustomer);
        }
    }
}
