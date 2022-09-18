package mich.gwan.sarensa.adapters;

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
import mich.gwan.sarensa.model.Item;

public class ItemRecyclerAdapter extends RecyclerView.Adapter<ItemRecyclerAdapter.ItemViewHolder>{
    private final List<Item> list;
    private int index = RecyclerView.NO_POSITION;
    private long mLastClickTime = System.currentTimeMillis();
    private static final long CLICK_TIME_INTERVAL = 300;

    public ItemRecyclerAdapter(List<Item> list) {
        this.list = list;
    }

    @Override
    public ItemRecyclerAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflating recycler item view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recycler, parent, false);

        return new ItemRecyclerAdapter.ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemRecyclerAdapter.ItemViewHolder holder, int position) {
        char firstChar = list.get(position).getItemName().charAt(0);
        holder.itemName.setText(list.get(position).getCategoryName());
        holder.quantity.setText(list.get(position).getItemQnty());
        holder.buyPrice.setText(list.get(position).getBuyPrice());
        holder.sellPrice.setText(list.get(position).getSellPrice());
        holder.textInitial.setText(firstChar);

        holder.itemView.setSelected(index == position);
        // perform sale
        holder.cardSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                notifyItemChanged(index);
                index = holder.getLayoutPosition();
                notifyItemChanged(index);
                return true;
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long now = System.currentTimeMillis();
                if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                    if (holder.checkBox.getVisibility() == View.GONE) {
                        holder.checkBox.setVisibility(View.VISIBLE);
                    } else {
                        holder.checkBox.setVisibility(View.GONE);
                    }
                }
                mLastClickTime = now;
                notifyItemChanged(index);
                index = holder.getLayoutPosition();
                notifyItemChanged(index);
            }
        });
        //holder.itemView.set
    }

    @Override
    public int getItemCount() {
        Log.v(ItemRecyclerAdapter.class.getSimpleName(),""+list.size());
        return list.size();
    }


    /**
     * ViewHolder class
     */
    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView itemName;
        public TextView quantity;
        public TextView buyPrice;
        public TextView sellPrice;
        public TextView textInitial;
        public CardView inner;
        public CardView outer;
        public CardView cardSell;
        public AppCompatCheckBox checkBox;
        private final SaleCategoryViewActivity saleActivity;

        public ItemViewHolder(View view) {
            super(view);
            itemName = view.findViewById(R.id.itemName);
            quantity = view.findViewById(R.id.textQuantity);
            buyPrice = view.findViewById(R.id.textBuyPrice);
            sellPrice = view.findViewById(R.id.textSellPrice);
            textInitial = view.findViewById(R.id.textInitial);
            inner = view.findViewById(R.id.cardInitialInner);
            cardSell = view.findViewById(R.id.cardSell);
            outer = view.findViewById(R.id.cardInitialOuter);
            checkBox = view.findViewById(R.id.categoryCheckBox);
            saleActivity = new SaleCategoryViewActivity();
        }
    }
}
