package com.sportstracking.strackify.adapter;

/**
 * strackify: country selection adapter
 * populates the countries data
 * in the countries recycler view. also displays the flags for each country
 * uses the country_view layout file
 * has a filter to search for the countries
 *
 * @author Nirbhay Ashok Pherwani
 * email: np5318@rit.edu
 * profile: https://nirbhay.me
 */

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

    /**
     * Paremtrized constructor to setup the adapter
     *
     * @param activity      activity set from
     * @param countriesData countries data array list
     * @param selectedSport sport selected by the user
     */
    public CountrySelectionAdapter(Activity activity, ArrayList<Country> countriesData, String selectedSport) {
        this.activity = activity;
        this.countriesData = countriesData;
        this.countriesDataComplete = new ArrayList<>(countriesData);
        this.selectedSport = selectedSport;
    }

    /**
     * inflates custom country_view
     *
     * @param parent   parent view group
     * @param viewType view type
     * @return adapter view holder
     */
    @Override
    public CountrySelectionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.country_view, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    /**
     * displays the country name with the flag
     * on clicking adds extras to the intent and moves to the next task
     *
     * @param holder   reference to view
     * @param position position in the recycler view
     */
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.countryNameView.setText(" " + countriesData.get(position).getCountryEmoji() + "  " + countriesData.get(position).getCountryName());
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

    /**
     * number of items in the array list
     *
     * @return number of items in the countries list
     */
    @Override
    public int getItemCount() {
        return countriesData.size();
    }

    /**
     * search filter for countries
     *
     * @return reference to filter for countries
     */
    @Override
    public Filter getFilter() {
        return filter;
    }

    /**
     * filters country data on the basis of search query
     */
    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Country> filteredCountriesData = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredCountriesData.addAll(countriesDataComplete);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Country country : countriesDataComplete) {
                    if (country.getCountryName().toLowerCase().contains(filterPattern) || country.getCountryCode().toLowerCase().contains(filterPattern)) {
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