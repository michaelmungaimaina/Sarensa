package mich.gwan.sarensa.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mich.gwan.sarensa.R;
import mich.gwan.sarensa.model.Cart;
import mich.gwan.sarensa.model.Item;
import mich.gwan.sarensa.sql.DatabaseHelper;

public class ItemListAdapter extends ArrayAdapter<Item>{
    Context mContext;
    int mResource;
    int count = 0;
    List<Item> list;
    HashMap<String,Integer> itemPosition = new HashMap<String,Integer>();
    public ItemListAdapter(@NonNull Context context, int resource, @NonNull List<Item> objects) {
        super(context, resource, objects);
        this.mContext=context;
        this.mResource=resource;
        this.list = objects;
    }

    public static class ViewHolder {
        public TextView itemName;
        public TextView count;
        public TextView quantity;
        public TextView buyPrice;
        public TextView sellPrice;
        public TextView textInitial;
        public TextView textWarning;
        public CardView inner;
        public CardView outer;
        public CardView cardSell;
        public AppCompatCheckBox checkBox;
        public String position;
    }


    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            LayoutInflater inflater=LayoutInflater.from(mContext);
            view = inflater.inflate(mResource,parent,false);
            holder = new ViewHolder();

            holder.itemName = view.findViewById(R.id.itemName);
            holder.quantity = view.findViewById(R.id.textItemQuantity);
            holder.buyPrice = view.findViewById(R.id.textBuyPrice);
            holder.sellPrice = view.findViewById(R.id.textSellPrice);
            holder.textInitial = view.findViewById(R.id.textInitial);
            holder.textWarning = view.findViewById(R.id.textWarning);
            holder.cardSell = view.findViewById(R.id.cardSell);
            holder.count = view.findViewById(R.id.textCount);
            holder.position = String.valueOf(position);

            holder.cardSell.setTag(position);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
            //holder=(ViewHolder)view.getTag();


            char firstChar = list.get(position).getItemName().charAt(0);
            holder.itemName.setText(list.get(position).getItemName());
            holder.quantity.setText(String.valueOf(list.get(position).getItemQnty()));
            holder.buyPrice.setText(String.valueOf(list.get(position).getBuyPrice()));
            holder.sellPrice.setText(String.valueOf(list.get(position).getSellPrice()));
            holder.textInitial.setText(String.valueOf(firstChar));

            //DatabaseHelper databaseHelper = new DatabaseHelper(holder.itemView.getContext());
            DatabaseHelper databaseHelper = new DatabaseHelper(mContext);

            //check if the item exists in cart and set its count value
            if (databaseHelper.checkCart(list.get(position).getStationName(),
                    list.get(position).getCategoryName(),list.get(position).getItemName())){
                holder.count.setText(String.valueOf(databaseHelper.getCartQnty(
                        list.get(position).getStationName(),list.get(position).getCategoryName(),
                        list.get(position).getItemName())));
                // assign count
                count = databaseHelper.getItemQnty(list.get(position).getStationName(),list.get(position).getCategoryName(),
                        list.get(position).getItemName()) + 1 - databaseHelper.getCartQnty(list.get(position).getStationName(),
                        list.get(position).getCategoryName(), list.get(position).getItemName());
                //holder.count.setText(String.valueOf(counter));
            }else {
                holder.count.setText(String.valueOf(0));
                count = databaseHelper.getItemQnty(list.get(position).getStationName(),list.get(position).getCategoryName(),
                        list.get(position).getItemName()) + 1;
            }
            //change background color if remaining qnty is < 5
            if (databaseHelper.getItemQnty(list.get(position).getStationName(),list.get(position).getCategoryName(),
                    list.get(position).getItemName()) <= 5){
                //holder.inner.setCardBackgroundColor(ContextCompat.getColor(context,R.color.less_item));
                holder.outer.setCardBackgroundColor(Color.RED);
                holder.textInitial.setTextColor(Color.RED);
                holder.itemName.setTextColor(ContextCompat.getColor(mContext,R.color.less_item));
            } else {
                holder.outer.setCardBackgroundColor(Color.BLUE);
                holder.itemName.setTextColor(Color.BLACK);
                holder.textInitial.setTextColor(Color.BLACK);
            }

            //holder.cardSell.setTag(position);
            holder.cardSell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = (Integer) view.getTag();
                    count--;
                    if (count >= 1) {
                        Cart par = new Cart();
                        par.setStationName(list.get(position).getStationName());
                        par.setCategoryName(list.get(position).getCategoryName());
                        par.setItemName(list.get(position).getItemName());
                        par.setBuyPrice(list.get(position).getBuyPrice());
                        par.setSellPrice(list.get(position).getSellPrice());
                        if (!databaseHelper.checkCart(list.get(position).getStationName(), list.get(position).getCategoryName(),
                                list.get(position).getItemName())) {
                            //add 1 quantity
                            par.setItemQnty(1);
                            databaseHelper.addCart(par);
                            int qnty = databaseHelper.getCartQnty(list.get(position).getStationName(),
                                   list.get(position).getCategoryName(), list.get(position).getItemName());
                            holder.count.setText(String.valueOf(qnty));
                            //create data
                            //cart.clear();
                            //cart.addAll(databaseHelper.getAllCat(list.get(position).getStationName()));
                            //listener.onMethodCall(cart);

                        } else {
                            // get existing cart quantity
                            int qnty = databaseHelper.getCartQnty(list.get(position).getStationName(),
                                    list.get(position).getCategoryName(), list.get(position).getItemName());
                            //set new qnty value
                            par.setItemQnty(qnty + 1);
                            // update quantity instead
                            databaseHelper.updateQuantity(par);
                            //update count
                            holder.count.setText(String.valueOf(databaseHelper.getCartQnty(
                                    list.get(position).getStationName(), list.get(position).getCategoryName(),
                                    list.get(position).getItemName())));
                        }
                    } else {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                holder.textWarning.setText("");
                                holder.textWarning.setTextColor(Color.TRANSPARENT);
                                holder.textWarning.setBackgroundColor(Color.TRANSPARENT);
                            }
                        }, 3000);
                        holder.textWarning.setText("Stock is depleted, Kindly add stock!");
                        holder.textWarning.setTextColor(Color.WHITE);
                        holder.textWarning.setBackgroundColor(Color.RED);
                    }
                    itemPosition.put(holder.position, count);
                    notifyDataSetChanged();
                }
            });

        return view;
    }

    public Map<String,Integer> getItemPosition()
    {
        return itemPosition;
    }


    @Override
    public long getItemId(int i) {
        return list.get(i).getItemId();
    }
    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Item getItem(int position) {
        return this.list.get(position);
    }

}
