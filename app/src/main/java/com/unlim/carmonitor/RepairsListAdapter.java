package com.unlim.carmonitor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RepairsListAdapter extends RecyclerView.Adapter<RepairsListAdapter.RepairsListViewHolder>  {
    private List<Repair> repairList = new ArrayList<>();
    private RepairsListAdapter.OnRecyclerItemClickListener onRecyclerItemClickListener;

    public interface OnRecyclerItemClickListener {
        void onItemClick(Repair repair);
    }

    class RepairsListViewHolder extends RecyclerView.ViewHolder {
        private TextView tvRepairName, tvRepairDate, tvRepairOdometer;

        public RepairsListViewHolder(View itemView) {
            super(itemView);
            tvRepairName = itemView.findViewById(R.id.repair_name);
            tvRepairDate = itemView.findViewById(R.id.repair_date);
            tvRepairOdometer = itemView.findViewById(R.id.repair_odometer_value);
        }

        public void bind(final Repair repair, final RepairsListAdapter.OnRecyclerItemClickListener onRecyclerItemClickListener) {
            tvRepairName.setText(repair.getName());
            tvRepairDate.setText(repair.getDateStr());
            tvRepairOdometer.setText(Const.getDecimalSpacedString(String.valueOf(repair.getOdometer())));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRecyclerItemClickListener.onItemClick(repair);
                }
            });
        }
    }

    public void setItems(Collection<Repair> repairs) {
        repairList.clear();
        repairList.addAll(repairs);
        notifyDataSetChanged();
    }

    public void setOnRecyclerItemClickListener(RepairsListAdapter.OnRecyclerItemClickListener onRecyclerItemClickListener) {
        this.onRecyclerItemClickListener = onRecyclerItemClickListener;
    }

    public void clearItems() {
        repairList.clear();
        notifyDataSetChanged();
    }

    @Override
    public RepairsListAdapter.RepairsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.repair_list_item, parent, false);
        return new RepairsListAdapter.RepairsListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RepairsListAdapter.RepairsListViewHolder holder, int position) {
        holder.bind(repairList.get(position), onRecyclerItemClickListener);
    }


    @Override
    public int getItemCount() {
        return repairList.size();
    }
}
