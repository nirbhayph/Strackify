package com.sportstracking.strackify;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_selection);
        setupContentView();
        setupDataView();
        getCountries();
    }

    private void setupContentView(){
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
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
            countrySelectionAdapter = new CountrySelectionAdapter(countries);
            countrySelectionRecyclerView.setAdapter(countrySelectionAdapter);
        }
        catch (JSONException e){

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
