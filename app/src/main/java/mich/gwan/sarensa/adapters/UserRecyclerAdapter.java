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
import mich.gwan.sarensa.model.User;
import mich.gwan.sarensa.sql.DatabaseHelper;

public class UserRecyclerAdapter extends RecyclerView.Adapter<UserRecyclerAdapter.ViewHolder>{
    private final List<User> list;
    private int index = RecyclerView.NO_POSITION;

    public UserRecyclerAdapter(List<User> list) {
        this.list = list;
    }

    @Override
    public UserRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflating recycler item view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_recycler, parent, false);

        return new UserRecyclerAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserRecyclerAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.name.setText(list.get(position).getUserName());
        holder.userType.setText(list.get(position).getUserType());
        holder.email.setText(String.valueOf(list.get(position).getUserEmail()));
        holder.phone.setText(String.valueOf(list.get(position).getUserPhone()));
        holder.password.setText(String.valueOf(list.get(position).getUserPassword()));

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
        Log.v(UserRecyclerAdapter.class.getSimpleName(),""+list.size());
        return list.size();
    }


    /**
     * ViewHolder class
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView userType;
        public TextView email;
        public TextView phone;
        public TextView password;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.textViewName);
            userType = view.findViewById(R.id.textViewUserType);
            email = view.findViewById(R.id.textViewEmail);
            phone = view.findViewById(R.id.textViewPhone);
            password = view.findViewById(R.id.textViewPassword);
        }
    }
}
