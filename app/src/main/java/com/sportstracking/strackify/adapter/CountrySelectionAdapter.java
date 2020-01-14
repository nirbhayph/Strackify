package com.sportstracking.strackify.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sportstracking.strackify.R;
import com.sportstracking.strackify.model.Country;

import java.util.ArrayList;

public class CountrySelectionAdapter extends RecyclerView.Adapter<CountrySelectionAdapter.MyViewHolder> {
    private ArrayList<Country> countriesData;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView countryNameView;
        public MyViewHolder(View v) {
            super(v);
            countryNameView = v.findViewById(R.id.countryName);
        }
    }

    public CountrySelectionAdapter(ArrayList<Country> countriesData) {
        this.countriesData = countriesData;
    }

    @Override
    public CountrySelectionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.country_view, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.countryNameView.setText(countriesData.get(position).getCountryName());
    }

    @Override
    public int getItemCount() {
        return countriesData.size();
    }
}