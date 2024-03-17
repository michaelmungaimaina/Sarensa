package mich.gwan.sarensa.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import mich.gwan.sarensa.R;
import mich.gwan.sarensa.activities.SaleCategoryViewActivity;
import mich.gwan.sarensa.model.Category;
import mich.gwan.sarensa.sql.DatabaseHelper;

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.CategoryViewHolder> {
    private final List<Category> list;
    private int index = RecyclerView.NO_POSITION;
    Context context;


    public CategoryRecyclerAdapter(List<Category> list) {
        this.list = list;
    }


    @Override
    public CategoryRecyclerAdapter.CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflating recycler item view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_recycler, parent, false);
        context = parent.getContext();

        return new CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, @SuppressLint("RecyclerView") int position) {
        char firstChar = list.get(position).getCategoryName().charAt(0);
        holder.catName.setText(list.get(position).getCategoryName());
        holder.textInitial.setText(String.valueOf(firstChar));
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        holder.count.setText(String.valueOf(databaseHelper.getItemTypesCount(list.get(position).getStationName(),
                list.get(position).getCategoryName())));



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
        Log.v(CategoryRecyclerAdapter.class.getSimpleName(),""+list.size());
        return list.size();
    }


    /**
     * ViewHolder class
     */
    public static class CategoryViewHolder extends RecyclerView.ViewHolder{

        public TextView catName;
        public TextView count;
        public TextView textInitial;
        public LinearLayout parentLayout;
        private final SaleCategoryViewActivity saleActivity;

        public CategoryViewHolder(View view){
            super(view);
            catName = view.findViewById(R.id.catName);
            count = view.findViewById(R.id.textCount);
            textInitial = view.findViewById(R.id.textInitial);
            parentLayout = view.findViewById(R.id.parentLayout);
            saleActivity = new SaleCategoryViewActivity();
        }

    }
}
