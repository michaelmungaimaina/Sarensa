package mich.gwan.sarensa.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mich.gwan.sarensa.R;
import mich.gwan.sarensa.activities.SaleCategoryViewActivity;
import mich.gwan.sarensa.interfaces.AdapterCallback;
import mich.gwan.sarensa.interfaces.CartAdapterCallback;
import mich.gwan.sarensa.model.Cart;
import mich.gwan.sarensa.model.Item;
import mich.gwan.sarensa.model.Sales;
import mich.gwan.sarensa.sql.DatabaseHelper;

public class ItemRecyclerAdapter extends RecyclerView.Adapter<ItemRecyclerAdapter.ItemViewHolder>{
    private List<Item> list;
    private int index = RecyclerView.NO_POSITION;
    private CartAdapterCallback listener;
    private Context context;
    private OnItemClickListener onItemClickListener;
    private int count;

    private final static int FADE_DURATION = 1000; //FADE_DURATION in milliseconds

    public ItemRecyclerAdapter( List<Item> list) {
        this.list = list;
    }

    public void setOnItemClickListener (OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public interface  OnItemClickListener{
        void onClick(int position);
    }

    @NonNull
    @Override
    public ItemRecyclerAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflating recycler item view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recycler, parent, false);
        context = parent.getContext();
        return new ItemViewHolder(itemView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(ItemRecyclerAdapter.ItemViewHolder holder, @SuppressLint("RecyclerView") int position) {
        char firstChar = list.get(position).getItemName().charAt(0);
        holder.itemName.setText(list.get(position).getItemName());
        holder.buyPrice.setText(String.valueOf(list.get(position).getBuyPrice()));
        holder.sellPrice.setText(String.valueOf(list.get(position).getSellPrice()));
        holder.textWarning.setText("");
        holder.textWarning.setTextColor(Color.TRANSPARENT);
        holder.textWarning.setBackgroundColor(Color.TRANSPARENT);
        // declare and initialize databashelper object
        //DatabaseHelper databaseHelper = new DatabaseHelper(holder.itemView.getContext());
        DatabaseHelper databaseHelper = new DatabaseHelper(context);

        //check if the item exists in cart and set its count value
        if (databaseHelper.checkCart(list.get(position).getStationName(),
                list.get(position).getCategoryName(),list.get(position).getItemName())){
            holder.count.setText(String.valueOf(databaseHelper.getCartQnty(
                    list.get(position).getStationName(),list.get(position).getCategoryName(),
                    list.get(position).getItemName())));
            holder.quantity.setText(String.valueOf(databaseHelper.getItemQnty(list.get(position).getStationName(),list.get(position).getCategoryName(),
                    list.get(position).getItemName())));
            // assign count
            count = databaseHelper.getItemQnty(list.get(position).getStationName(),list.get(position).getCategoryName(),
                    list.get(position).getItemName()) + 1 - databaseHelper.getCartQnty(list.get(position).getStationName(),
                    list.get(position).getCategoryName(), list.get(position).getItemName());
            //holder.count.setText(String.valueOf(counter));
        }else {
            holder.count.setText(String.valueOf(0));
            holder.quantity.setText(String.valueOf(list.get(position).getItemQnty()));
            count = databaseHelper.getItemQnty(list.get(position).getStationName(),list.get(position).getCategoryName(),
                    list.get(position).getItemName()) + 1;
        }
        //change background color if remaining qnty is < 5
        if (databaseHelper.getItemQnty(list.get(position).getStationName(),list.get(position).getCategoryName(),
                list.get(position).getItemName()) <= 5){
            //holder.inner.setCardBackgroundColor(ContextCompat.getColor(context,R.color.less_item));
            holder.itemName.setTextColor(ContextCompat.getColor(context,R.color.less_item));
        } else {
            holder.itemName.setTextColor(Color.BLACK);
        }

        holder.itemView.setSelected(index == position);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public boolean onLongClick(View view) {
                index = holder.getLayoutPosition();
                //notifyItemChanged(index);
                notifyDataSetChanged();
                return true;
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                index = holder.getLayoutPosition();
                //notifyItemChanged(index);
                notifyDataSetChanged();
            }
        });

        setScaleAnimation(holder.itemView);

    }

    @Override
    public int getItemCount() {
        Log.v(ItemRecyclerAdapter.class.getSimpleName(),""+list.size());
        return list.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(List<Item> listItem){
        list = listItem;
        notifyDataSetChanged();
    }

    private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }
    /**
     * ViewHolder class
     */
    public static class ItemViewHolder extends RecyclerView.ViewHolder{

        public TextView itemName;
        public TextView count;
        public TextView quantity;
        public TextView buyPrice;
        public TextView sellPrice;
        public TextView textWarning;
        public CardView cardSell;

        public ItemViewHolder(View view, OnItemClickListener onItemClickListener) {
            super(view);
            itemName = view.findViewById(R.id.itemName);
            quantity = view.findViewById(R.id.textItemQuantity);
            buyPrice = view.findViewById(R.id.textBuyPrice);
            sellPrice = view.findViewById(R.id.textSellPrice);
            textWarning = view.findViewById(R.id.textWarning);
            cardSell = view.findViewById(R.id.cardSell);
            count = view.findViewById(R.id.textCount);

            cardSell.setOnClickListener(v -> onItemClickListener.onClick(getAdapterPosition()));
        }

        public interface OnFirstItemVisibilityCallBack{

            void onChange(boolean isVisible);

        }
    }
}
