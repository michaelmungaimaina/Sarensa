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
import mich.gwan.sarensa.model.Item;
import mich.gwan.sarensa.model.Sales;

public class StockRecyclerAdapter extends RecyclerView.Adapter<StockRecyclerAdapter.ViewHolder>{
    private final List<Item> list;
    private int index = RecyclerView.NO_POSITION;
    private int count = 0;

    public StockRecyclerAdapter(List<Item> list) {
        this.list = list;
    }

    @Override
    public StockRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflating recycler item view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stock_recycler, parent, false);

        return new StockRecyclerAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StockRecyclerAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.station.setText(list.get(position).getStationName());
        holder.category.setText(list.get(position).getCategoryName());
        holder.item.setText(String.valueOf(list.get(position).getItemName()));
        holder.quantity.setText(String.valueOf(list.get(position).getItemQnty()));

        holder.itemView.setSelected(index == position);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public boolean onLongClick(View view) {
                index = holder.getLayoutPosition();
                notifyDataSetChanged();
                return true;
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                index = holder.getLayoutPosition();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.v(StockRecyclerAdapter.class.getSimpleName(),""+list.size());
        return list.size();
    }


    /**
     * ViewHolder class
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView station;
        public TextView category;
        public TextView item;
        public TextView quantity;

        public ViewHolder(View view) {
            super(view);
            station = view.findViewById(R.id.textStation);
            category = view.findViewById(R.id.textCategory);
            item = view.findViewById(R.id.textItem);
            quantity = view.findViewById(R.id.textQuantity);
        }
    }
}
