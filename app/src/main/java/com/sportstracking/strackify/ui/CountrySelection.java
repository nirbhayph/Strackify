package com.sportstracking.strackify.ui;

/**
 * strackify: country selection
 * populates the countries from a custom json created
 * in the countries recyceler view. also displays the flags for each country
 *
 * @author Nirbhay Ashok Pherwani
 * email: np5318@rit.edu
 * profile: https://nirbhay.me
 */


import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.sportstracking.strackify.R;
import com.sportstracking.strackify.adapter.CountrySelectionAdapter;
import com.sportstracking.strackify.model.Country;
import com.sportstracking.strackify.utility.Values;
import com.sportstracking.strackify.utility.VolleyService;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.twitter.TwitterEmojiProvider;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CountrySelection extends AppCompatActivity {

    private RecyclerView countrySelectionRecyclerView;
    private CountrySelectionAdapter countrySelectionAdapter;
    private VolleyService volleyService;
    private String selectedSport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EmojiManager.install(new TwitterEmojiProvider());
        getSelectedSport();
        setContentView(R.layout.activity_country_selection);
        setupDataView();
        getCountries();
    }

    /**
     * sets the selected sport by the user
     */
    private void getSelectedSport() {
        if (getIntent().hasExtra(Values.SPORTS_SELECTION)) {
            selectedSport = getIntent().getStringExtra(Values.SPORTS_SELECTION);
        } else {
            selectedSport = "Soccer";
        }
    }

    /**
     * sets up the header text, recycler view and its layout manager
     */
    private void setupDataView() {
        TextView headerTitle = findViewById(R.id.countryHeaderText);
        headerTitle.setText("Select a country your " + selectedSport + "\nteam belongs to");
        countrySelectionRecyclerView = findViewById(R.id.country_selection_recycler_view);
        countrySelectionRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        countrySelectionRecyclerView.setLayoutManager(layoutManager);
    }

    /**
     * uses the volley service to get the countries
     */
    private void getCountries() {
        volleyService = new VolleyService(this, Values.COUNTRIES_SELECTION);
        volleyService.makeRequest(Values.COUNTRIES);
    }

    /**
     * update method to populate the countries data obtained
     * called by the volley service
     *
     * @param response JSONObject response of data obtained from api
     */
    public void updateUI(JSONObject response) {
        ArrayList<Country> countries = new ArrayList<Country>();
        try {
            JSONArray countriesList = (JSONArray) response.get("countries");
            for (int i = 0; i < countriesList.length(); i++) {
                JSONObject iCountry = countriesList.getJSONObject(i);
                String name = iCountry.getString("name");
                String code = iCountry.getString("code");
                String emoji = iCountry.getString("emoji");
                String unicode = iCountry.getString("unicode");
                Country country = new Country();
                country.setCountryCode(code);
                country.setCountryName(name);
                country.setCountryUnicode(unicode);
                country.setCountryEmoji(emoji);
                countries.add(country);
            }
            countrySelectionAdapter = new CountrySelectionAdapter(this, countries, selectedSport);
            countrySelectionRecyclerView.setAdapter(countrySelectionAdapter);
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Unable to retrieve countries data!", Toast.LENGTH_SHORT);
        }

    }

    /**
     * for searching from countires populated for using the custom json url
     *
     * @param menu menu on action bar
     * @return return true or false after menu inflated
     */
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
                if (countrySelectionAdapter != null) {
                    countrySelectionAdapter.getFilter().filter(newText);
                }
                return false;
            }
        });

        return true;
    }
}
