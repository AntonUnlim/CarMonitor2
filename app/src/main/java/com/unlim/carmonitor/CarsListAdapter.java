package com.unlim.carmonitor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CarsListAdapter extends RecyclerView.Adapter<CarsListAdapter.CarsListViewHolder> {

    private List<Car> carList = new ArrayList<>();
    private OnRecyclerItemClickListener onRecyclerItemClickListener;

    public interface OnRecyclerItemClickListener {
        void onItemClick(Car car);
    }

    class CarsListViewHolder extends RecyclerView.ViewHolder {
        private TextView tvBrandModel;
        private TextView tvOdometer;

        public CarsListViewHolder(View itemView) {
            super(itemView);
            tvBrandModel = itemView.findViewById(R.id.tv_car_brand_model);
            tvOdometer = itemView.findViewById(R.id.tv_car_odometer_in_item);
        }

        public void bind(final Car car, final OnRecyclerItemClickListener onRecyclerItemClickListener) {
            tvBrandModel.setText(car.getBrand() + " " + car.getModel());
            tvOdometer.setText(String.valueOf(Const.getDecimalSpacedString(String.valueOf(car.getOdometer()))));
            tvOdometer.setCompoundDrawablesWithIntrinsicBounds(R.drawable.outline_speed_black_18dp, 0, 0, 0);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRecyclerItemClickListener.onItemClick(car);
                }
            });
        }
    }

    public void setItems(Collection<Car> cars) {
        carList.clear();
        carList.addAll(cars);
        notifyDataSetChanged();
    }

    public void setOnRecyclerItemClickListener(OnRecyclerItemClickListener onRecyclerItemClickListener) {
        this.onRecyclerItemClickListener = onRecyclerItemClickListener;
    }

    public void clearItems() {
        carList.clear();
        notifyDataSetChanged();
    }

    @Override
    public CarsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.car_list_item, parent, false);
        return new CarsListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CarsListViewHolder holder, int position) {
        holder.bind(carList.get(position), onRecyclerItemClickListener);
    }


    @Override
    public int getItemCount() {
        return carList.size();
    }
}
