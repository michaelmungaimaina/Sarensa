package mich.gwan.sarensa.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

import mich.gwan.sarensa.R;
import mich.gwan.sarensa.activities.SaleCategoryViewActivity;
import mich.gwan.sarensa.helpers.RecyclerTouchListener;
import mich.gwan.sarensa.model.Category;
import mich.gwan.sarensa.model.Item;
import mich.gwan.sarensa.model.ListItem;
import mich.gwan.sarensa.model.Station;
import mich.gwan.sarensa.sql.DatabaseHelper;
import mich.gwan.sarensa.ui.home.HomeFragment;

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.CategoryViewHolder>{
    private final List<Category> list;
    private int index = RecyclerView.NO_POSITION;
    private long mLastClickTime = System.currentTimeMillis();
    private static final long CLICK_TIME_INTERVAL = 300;
    private Context context;

    public CategoryRecyclerAdapter(List<Category> list) {
        this.list = list;
    }


    @Override
    public CategoryRecyclerAdapter.CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflating recycler item view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_recycler, parent, false);
        context = parent.getContext();

        return new CategoryRecyclerAdapter.CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CategoryRecyclerAdapter.CategoryViewHolder holder, @SuppressLint("RecyclerView") int position) {
        char firstChar = list.get(position).getCategoryName().charAt(0);
        holder.catName.setText(list.get(position).getCategoryName());
        holder.textInitial.setText(String.valueOf(firstChar));

        holder.itemView.setSelected(index == position);
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
                /*long now = System.currentTimeMillis();
                if (now - mLastClickTime < CLICK_TIME_INTERVAL) {
                    if (holder.checkBox.getVisibility() == View.GONE) {
                        holder.checkBox.setVisibility(View.VISIBLE);
                    } else {
                        holder.checkBox.setVisibility(View.GONE);
                    }
                }
                mLastClickTime = now;*/
                //
               //holder.saleActivity.showAddFAB();
                // assign a category value
                System.out.println(list.get(position).getCategoryName());
                System.out.println(list.get(position).getStationName());
                //holder.saleActivity.listItem.clear();
                //holder.saleActivity.listItem.addAll(holder.saleActivity.databaseHelper.
                // getAllItems(list.get(position).getStationName(), list.get(position).getCategoryName()));
                //holder.saleActivity.listItem = holder.itemmm;
                //holder.saleActivity.itemRecyclerAdapter = holder.adapter;
                holder.saleActivity.myCategory = list.get(position).getCategoryName();
                System.out.println(holder.saleActivity.myCategory);
                //holder.getItemsFromSQlite(holder.myList.getAdapter(), holder.myList.getDbHelper(),holder.myList.getItem(),list.get(position).getStationName(), list.get(position).getCategoryName());
                index = holder.getLayoutPosition();
                notifyItemChanged(index);
            }
        });
        //holder.itemView.set
    }

    @Override
    public int getItemCount() {
        Log.v(CategoryRecyclerAdapter.class.getSimpleName(),""+list.size());
        return list.size();
    }


    /**
     * ViewHolder class
     */
    public class CategoryViewHolder extends RecyclerView.ViewHolder{

        public TextView catName;
        public TextView count;
        public TextView textInitial;
        public CardView inner;
        public CardView outer;
        public AppCompatCheckBox checkBox;
        private final SaleCategoryViewActivity saleActivity;

        public CategoryViewHolder(View view){
            super(view);
            catName = view.findViewById(R.id.catName);
            count = view.findViewById(R.id.textCount);
            textInitial = view.findViewById(R.id.textInitial);
            inner = view.findViewById(R.id.cardInitialInner);
            outer = view.findViewById(R.id.cardInitialOuter);
            checkBox = view.findViewById(R.id.categoryCheckBox);
            saleActivity = new SaleCategoryViewActivity();
        }

    }
}
