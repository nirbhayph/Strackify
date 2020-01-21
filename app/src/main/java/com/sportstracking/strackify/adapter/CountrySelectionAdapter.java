package com.sportstracking.strackify.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.recyclerview.widget.RecyclerView;

import com.sportstracking.strackify.R;
import com.sportstracking.strackify.ui.TeamSelection;
import com.sportstracking.strackify.model.Country;
import com.sportstracking.strackify.utility.Values;
import com.vanniktech.emoji.EmojiTextView;

import java.util.ArrayList;

public class CountrySelectionAdapter extends RecyclerView.Adapter<CountrySelectionAdapter.MyViewHolder> implements Filterable {
    private ArrayList<Country> countriesData;
    private ArrayList<Country> countriesDataComplete;
    private Activity activity;
    private String selectedSport;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public EmojiTextView countryNameView;
        public MyViewHolder(View v) {
            super(v);
            countryNameView = v.findViewById(R.id.countryName);
        }
    }

    public CountrySelectionAdapter(Activity activity, ArrayList<Country> countriesData, String selectedSport) {
        this.activity = activity;
        this.countriesData = countriesData;
        this.countriesDataComplete = new ArrayList<>(countriesData);
        this.selectedSport = selectedSport;
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
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.countryNameView.setText(" " + countriesData.get(position).getCountryEmoji() +"  "+ countriesData.get(position).getCountryName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, TeamSelection.class);
                intent.putExtra(Values.SPORTS_SELECTION, selectedSport);
                intent.putExtra(Values.COUNTRIES_SELECTION, countriesData.get(position).getCountryName());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return countriesData.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Country> filteredCountriesData = new ArrayList<>();
            if(constraint == null || constraint.length()==0){
                filteredCountriesData.addAll(countriesDataComplete);
            }
            else{
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(Country country : countriesDataComplete){
                    if(country.getCountryName().toLowerCase().contains(filterPattern) || country.getCountryCode().toLowerCase().contains(filterPattern)){
                        filteredCountriesData.add(country);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredCountriesData;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            countriesData.clear();
            countriesData.addAll((ArrayList<Country>) results.values);
            notifyDataSetChanged();
        }
    };
}