package mich.gwan.sarensa.adapters;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import mich.gwan.sarensa.R;
import mich.gwan.sarensa.activities.SaleCategoryViewActivity;
import mich.gwan.sarensa.model.Cart;
import mich.gwan.sarensa.model.Sales;
import mich.gwan.sarensa.sql.DatabaseHelper;

public class SalesRecyclerAdapter extends RecyclerView.Adapter<SalesRecyclerAdapter.ViewHolder>{
    private final List<Sales> list;
    private int index = RecyclerView.NO_POSITION;
    private int count = 0;

    public SalesRecyclerAdapter(List<Sales> list) {
        this.list = list;
    }

    @Override
    public SalesRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflating recycler item view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sales_recycler, parent, false);

        return new SalesRecyclerAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SalesRecyclerAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        char firstChar = list.get(position).getSaleType().charAt(0);
        holder.date.setText(list.get(position).getDate());
        holder.time.setText(list.get(position).getTime());
        holder.type.setText(String.valueOf(firstChar));
        holder.categoryName.setText(list.get(position).getItemCategory());
        holder.itemName.setText(list.get(position).getItemName());
        holder.quantity.setText(String.valueOf(list.get(position).getItemQnty()));
        holder.unitPrice.setText(String.valueOf(list.get(position).getSellPrice()));
        holder.total.setText(String.valueOf(list.get(position).getTotal()));
        holder.profit.setText(String.valueOf(list.get(position).getProfit()));
        holder.itemView.setSelected(index == position);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                index = holder.getLayoutPosition();
                notifyItemChanged(index);
                return true;
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index = holder.getLayoutPosition();
                notifyItemChanged(index);
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.v(SalesRecyclerAdapter.class.getSimpleName(),""+list.size());
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
        public TextView itemName;
        public TextView quantity;
        public TextView unitPrice;
        public TextView total;
        public TextView profit;
        private final SaleCategoryViewActivity saleActivity;

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
            profit = view.findViewById(R.id.textProfit);
            saleActivity = new SaleCategoryViewActivity();
        }
    }
}
