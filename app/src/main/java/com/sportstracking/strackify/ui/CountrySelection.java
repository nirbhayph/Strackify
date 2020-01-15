package com.sportstracking.strackify.ui;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sportstracking.strackify.R;
import com.sportstracking.strackify.adapter.CountrySelectionAdapter;
import com.sportstracking.strackify.model.Country;
import com.sportstracking.strackify.utility.Constants;
import com.sportstracking.strackify.utility.VolleyService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class CountrySelection extends AppCompatActivity {

    private RecyclerView countrySelectionRecyclerView;
    private CountrySelectionAdapter countrySelectionAdapter;
    private VolleyService volleyService;
    private String selectedSport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSelectedSport();
        setContentView(R.layout.activity_country_selection);
        setupDataView();
        getCountries();
    }

    private void getSelectedSport(){
        if(getIntent().hasExtra(Constants.SPORTS_SELECTION)){
            selectedSport = getIntent().getStringExtra(Constants.SPORTS_SELECTION);
        }
    }

    private void setupDataView(){
        countrySelectionRecyclerView = findViewById(R.id.country_selection_recycler_view);
        countrySelectionRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        countrySelectionRecyclerView.setLayoutManager(layoutManager);
    }

    private void getCountries(){
        volleyService = new VolleyService(this, Constants.COUNTRIES_SELECTION);
        volleyService.makeRequest(Constants.COUNTRIES);
    }

    public void updateUI(JSONObject response){

        ArrayList<Country> countries = new ArrayList<Country>();
        Iterator<String> iterator = response.keys();
        try {
            while (iterator.hasNext()) {
                String countryCode = iterator.next();
                String countryName = response.get(countryCode).toString();
                Country country = new Country();
                country.setCountryCode(countryCode);
                country.setCountryName(countryName);
                countries.add(country);
            }
            countrySelectionAdapter = new CountrySelectionAdapter(this, countries, selectedSport);
            countrySelectionRecyclerView.setAdapter(countrySelectionAdapter);
        }
        catch (JSONException e){

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(countrySelectionAdapter != null){
                    countrySelectionAdapter.getFilter().filter(newText);
                }
                return false;
            }
        });

        return true;
    }
}
