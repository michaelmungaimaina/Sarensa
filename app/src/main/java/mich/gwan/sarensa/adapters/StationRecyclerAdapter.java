package mich.gwan.sarensa.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import mich.gwan.sarensa.R;
import mich.gwan.sarensa.activities.SaleCategoryViewActivity;
import mich.gwan.sarensa.model.Station;
import mich.gwan.sarensa.ui.home.HomeFragment;

public class StationRecyclerAdapter extends RecyclerView.Adapter<StationRecyclerAdapter.StationViewHolder>{
        private final List<Station> list;
        private int index = RecyclerView.NO_POSITION;

    public StationRecyclerAdapter(List<Station> list) {
        this.list = list;
    }

        @Override
        public StationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) throws InflateException {
        // inflating recycler item view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.station_recycler, parent, false);

        return new StationViewHolder(itemView);
    }

        @Override
        public void onBindViewHolder(StationViewHolder holder, int position) {
        String station = list.get(position).getName();
        holder.location.setText(list.get(position).getLocation());
        holder.staionName.setText(list.get(position).getName());
        holder.sarensaDetail.setText(station);

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
                notifyItemChanged(index);
                index = holder.getLayoutPosition();
                notifyItemChanged(index);

                Context context = view.getContext();
                Intent intent = new Intent(context, SaleCategoryViewActivity.class);
                intent.putExtra("myStationName", station);
                context.startActivity(intent);
            }
        });
    }

        @Override
        public int getItemCount() {
        Log.v(StationRecyclerAdapter.class.getSimpleName(),""+list.size());
        return list.size();
    }


        /**
         * ViewHolder class
         */
        public class StationViewHolder extends RecyclerView.ViewHolder {

            public TextView staionName;
            public TextView location;
            public TextView sarensaDetail;
            private final HomeFragment homeActivity;

            public StationViewHolder(View view) {
                super(view);
                staionName = view.findViewById(R.id.stationName);
                location = view.findViewById(R.id.location);
                sarensaDetail = view.findViewById(R.id.textSarensa);
                homeActivity = new HomeFragment();
            }
        }
}
