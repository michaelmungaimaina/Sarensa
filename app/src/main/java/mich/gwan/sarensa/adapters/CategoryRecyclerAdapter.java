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
import mich.gwan.sarensa.interfaces.AdapterCallback;
import mich.gwan.sarensa.model.Category;

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.CategoryViewHolder> {
    private final List<Category> list;
    private int index = RecyclerView.NO_POSITION;
    private AdapterCallback listener;


    public CategoryRecyclerAdapter(List<Category> list, AdapterCallback listener) {
        this.list = list;
        this.listener = listener;
    }


    @Override
    public CategoryRecyclerAdapter.CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflating recycler item view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_recycler, parent, false);

        return new CategoryViewHolder(itemView);
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
                index = holder.getLayoutPosition();
                notifyItemChanged(index);
                return true;
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String catName = list.get(position).getCategoryName();
                System.out.println("PRESSED!");
                listener.onMethodCallback(catName);

                holder.saleActivity.myCategory = list.get(position).getCategoryName();
                System.out.println(holder.saleActivity.myCategory);
                index = holder.getLayoutPosition();
                notifyItemChanged(index);
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
        public CardView inner;
        public CardView outer;
        public AppCompatCheckBox checkBox;
        public LinearLayout parentLayout;
        private final SaleCategoryViewActivity saleActivity;

        public CategoryViewHolder(View view){
            super(view);
            catName = view.findViewById(R.id.catName);
            count = view.findViewById(R.id.textCount);
            textInitial = view.findViewById(R.id.textInitial);
            inner = view.findViewById(R.id.cardInitialInner);
            outer = view.findViewById(R.id.cardInitialOuter);
            checkBox = view.findViewById(R.id.categoryCheckBox);
            parentLayout = view.findViewById(R.id.parentLayout);
            saleActivity = new SaleCategoryViewActivity();
        }

    }
}
