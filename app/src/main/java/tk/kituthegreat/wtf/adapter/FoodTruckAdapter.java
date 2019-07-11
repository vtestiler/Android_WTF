package tk.kituthegreat.wtf.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import tk.kituthegreat.wtf.R;
import tk.kituthegreat.wtf.holder.FoodTruckHolder;
import tk.kituthegreat.wtf.model.FoodTruck;

public class FoodTruckAdapter extends RecyclerView.Adapter<FoodTruckHolder> {

    private ArrayList<FoodTruck> trucks;

    public FoodTruckAdapter(ArrayList<FoodTruck> trucks) {
        this.trucks = trucks;
    }

    @Override
    public void onBindViewHolder(FoodTruckHolder holder, int position) {

        final FoodTruck truck = trucks.get(position);
        holder.updateUI(truck);

    }

    @Override
    public int getItemCount() {
        return trucks.size();
    }

    @Override
    public FoodTruckHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View truckCard = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_foodtruck, parent, false);
        return new FoodTruckHolder(truckCard);
    }
}
