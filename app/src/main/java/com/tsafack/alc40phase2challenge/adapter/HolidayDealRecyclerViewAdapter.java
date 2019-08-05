package com.tsafack.alc40phase2challenge.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tsafack.alc40phase2challenge.R;
import com.tsafack.alc40phase2challenge.entities.HolidayDeal;

import java.util.ArrayList;

public class HolidayDealRecyclerViewAdapter extends
        RecyclerView.Adapter<HolidayDealRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private ArrayList<HolidayDeal> listOfHolidayDeal;

    public HolidayDealRecyclerViewAdapter(Context context, ArrayList<HolidayDeal> listOfHolidayDeal) {
        this.context = context;
        this.listOfHolidayDeal = listOfHolidayDeal;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final HolidayDeal holidayDeal = listOfHolidayDeal.get(position);
        holder.name.setText(holidayDeal.getName());
        holder.description.setText(holidayDeal.getDescription());
        holder.price.setText(String.valueOf(holidayDeal.getPrice()));
    }

    @Override
    public int getItemCount() {
        return listOfHolidayDeal.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView name;
        public TextView description;
        public TextView price;

        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.id_image);
            name = (TextView) view.findViewById(R.id.list_title);
            description = view.findViewById(R.id.list_desc);
            price = view.findViewById(R.id.list_price);
        }
    }
}
