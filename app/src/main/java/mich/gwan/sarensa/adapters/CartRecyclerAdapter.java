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
import mich.gwan.sarensa.model.Item;
import mich.gwan.sarensa.sql.DatabaseHelper;

public class CartRecyclerAdapter extends RecyclerView.Adapter<CartRecyclerAdapter.ViewHolder>{
    private final List<Cart> list;
    private int index = RecyclerView.NO_POSITION;
    private int count = 0;
    int newQnty = 0;
    int counter = 0;

    public CartRecyclerAdapter(List<Cart> list) {
        this.list = list;
    }

    @Override
    public CartRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflating recycler item view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sell_recycler, parent, false);

        return new CartRecyclerAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CartRecyclerAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        char firstChar = list.get(position).getItemName().charAt(0);
        int total = list.get(position).getItemQnty() * list.get(position).getSellPrice();
        holder.itemName.setText(list.get(position).getItemName());
        holder.quantity.setText(String.valueOf(list.get(position).getItemQnty()));
        holder.totalPrice.setText(String.valueOf(total));
        holder.sellPrice.setText(String.valueOf(list.get(position).getSellPrice()));
        holder.textInitial.setText(String.valueOf(firstChar));
        // declare and initialize databasehelper object
        DatabaseHelper databaseHelper = new DatabaseHelper(holder.itemView.getContext());
        SaleCategoryViewActivity saleCategoryViewActivity = new SaleCategoryViewActivity();

        holder.itemView.setSelected(index == position);
        holder.count.setText(String.valueOf(0));
        int currentQnty = list.get(position).getItemQnty();
        counter = list.get(position).getItemQnty() + 1;
        holder.cardRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
                counter--;
                if(counter >= 1) {
                    newQnty = currentQnty - count;
                    list.get(position).setItemQnty(newQnty);
                    holder.quantity.setText(String.valueOf(newQnty));
                    // display the new count value
                    holder.count.setText(String.valueOf(count));
                    count = 0;
                    counter = 0;
                } else {
                    list.remove(position);
                    notifyItemChanged(position);
                    count = 0;
                    counter = 0;
                }
            }
        });
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
        Log.v(CartRecyclerAdapter.class.getSimpleName(),""+list.size());
        return list.size();
    }


    /**
     * ViewHolder class
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView itemName;
        public TextView count;
        public TextView quantity;
        public TextView sellPrice;
        public TextView totalPrice;
        public TextView textInitial;
        public CardView inner;
        public CardView outer;
        public CardView cardRemove;
        public AppCompatCheckBox checkBox;
        private final SaleCategoryViewActivity saleActivity;

        public ViewHolder(View view) {
            super(view);
            itemName = view.findViewById(R.id.itemName);
            quantity = view.findViewById(R.id.textItemQuantity);
            totalPrice = view.findViewById(R.id.textTotalPrice);
            sellPrice = view.findViewById(R.id.textUnitPrice);
            textInitial = view.findViewById(R.id.textInitial);
            inner = view.findViewById(R.id.cardInitialInner);
            cardRemove = view.findViewById(R.id.cardRemove);
            count = view.findViewById(R.id.textCount);
            outer = view.findViewById(R.id.cardInitialOuter);
            checkBox = view.findViewById(R.id.categoryCheckBox);
            saleActivity = new SaleCategoryViewActivity();
        }
    }
}
