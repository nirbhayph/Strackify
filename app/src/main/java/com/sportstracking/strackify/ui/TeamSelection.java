package com.sportstracking.strackify.ui;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sportstracking.strackify.R;
import com.sportstracking.strackify.adapter.TeamSelectionAdapter;
import com.sportstracking.strackify.model.Team;
import com.sportstracking.strackify.utility.Constants;
import com.sportstracking.strackify.utility.VolleyService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TeamSelection extends AppCompatActivity {

    private RecyclerView teamSelectionRecyclerView;
    private TeamSelectionAdapter teamSelectionAdapter;
    private VolleyService volleyService;
    private String selectedSport;
    private String selectedCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSelectedData();
        setContentView(R.layout.activity_team_selection);
        setupDataView();
        getTeams();
    }

    private void getSelectedData(){
        if(getIntent().hasExtra(Constants.SPORTS_SELECTION)){
            selectedSport = getIntent().getStringExtra(Constants.SPORTS_SELECTION);
        }
        if(getIntent().hasExtra(Constants.COUNTRIES_SELECTION)){
            selectedCountry = getIntent().getStringExtra(Constants.COUNTRIES_SELECTION);
        }
    }

    private void setupDataView(){
        teamSelectionRecyclerView = findViewById(R.id.team_selection_recycler_view);
        teamSelectionRecyclerView.setHasFixedSize(true);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        teamSelectionRecyclerView.setLayoutManager(layoutManager);
    }

    private void getTeams(){
        volleyService = new VolleyService(this, Constants.TEAMS_SELECTION);
        volleyService.makeRequest(Constants.TEAMS + Constants.SPORT_IDENTIFIER + selectedSport + Constants.COUNTRY_IDENTIFIER + selectedCountry);
    }

    public void updateUI(JSONObject response){

        ArrayList<Team> teams = new ArrayList<>();
        try{
            JSONArray teamsData = (JSONArray) response.get("teams");
            TextView notFoundTextView = findViewById(R.id.notFoundMessage);
            notFoundTextView.setVisibility(View.INVISIBLE);
            for(int counter = 0; counter<teamsData.length(); counter++){
                JSONObject teamItem = (JSONObject) teamsData.get(counter);
                Team team = new Team();
                team.setTeamName(teamItem.getString("strTeam"));
                team.setTeamBadge(teamItem.getString("strTeamBadge"));
                teams.add(team);
            }
            teamSelectionAdapter = new TeamSelectionAdapter(this, teams, volleyService);
            teamSelectionRecyclerView.setAdapter(teamSelectionAdapter);
        }
        catch (Exception e){
            TextView notFoundTextView = findViewById(R.id.notFoundMessage);
            notFoundTextView.setVisibility(View.VISIBLE);
            Toast.makeText(this, "No teams found! Try another combination!", Toast.LENGTH_LONG).show();
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
                teamSelectionAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }
}
