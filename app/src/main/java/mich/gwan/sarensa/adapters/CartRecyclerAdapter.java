package mich.gwan.sarensa.adapters;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import mich.gwan.sarensa.R;
import mich.gwan.sarensa.activities.SaleCategoryViewActivity;
import mich.gwan.sarensa.model.Cart;
import mich.gwan.sarensa.sql.DatabaseHelper;

public class CartRecyclerAdapter extends RecyclerView.Adapter<CartRecyclerAdapter.ViewHolder>{
    private final List<Cart> list;
    private int index = RecyclerView.NO_POSITION;
    private int count = 0;
    int newQnty = 0;
    int counter = 0;
    private OnItemClickListener onItemClickListener;


    public void setOnItemClickListener (OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public interface  OnItemClickListener{
        void onClick(int position);
    }

    public CartRecyclerAdapter(List<Cart> list) {
        this.list = list;
    }

    @Override
    public CartRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflating recycler item view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_recycler, parent, false);

        return new CartRecyclerAdapter.ViewHolder(itemView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(CartRecyclerAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        char firstChar = list.get(position).getItemName().charAt(0);
        int total = list.get(position).getItemQnty() * list.get(position).getSellPrice();
        holder.itemName.setText(list.get(position).getItemName());
        holder.totalPrice.setText(String.valueOf(total));
        holder.sellPrice.setText(String.valueOf(list.get(position).getSellPrice()));
        // declare and initialize databasehelper object
        DatabaseHelper databaseHelper = new DatabaseHelper(holder.itemView.getContext());

        /*holder.count.setText(String.valueOf(list.get(position).getItemQnty() - databaseHelper.getCartQnty(list.get(position).getStationName(),
                list.get(position).getCategoryName(), list.get(position).getItemName())));*/

        holder.quantity.setText(String.valueOf(databaseHelper.getCartQnty(list.get(position).getStationName(),
                list.get(position).getCategoryName(), list.get(position).getItemName())));

        holder.itemView.setSelected(index == position);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                index = holder.getLayoutPosition();
                notifyItemChanged(index);
                notifyDataSetChanged();
                return true;
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index = holder.getLayoutPosition();
                notifyItemChanged(index);
                notifyDataSetChanged();
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
        //public TextView count;
        public TextView quantity;
        public TextView sellPrice;
        public TextView totalPrice;
        public CardView cardRemove;

        public ViewHolder(View view, OnItemClickListener onItemClickListener) {
            super(view);
            itemName = view.findViewById(R.id.itemName);
            quantity = view.findViewById(R.id.textItemQuantity);
            totalPrice = view.findViewById(R.id.textTotalPrice);
            sellPrice = view.findViewById(R.id.textUnitPrice);
            cardRemove = view.findViewById(R.id.cardRemove);
            //count = view.findViewById(R.id.textCount);

            cardRemove.setOnClickListener(v -> onItemClickListener.onClick(getAdapterPosition()));
        }
    }
}
