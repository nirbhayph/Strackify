package com.sportstracking.strackify.ui;

/**
 * strackify: team selection
 * uses the sport and country selected to populate
 * the teams in the recyceler view
 * maintains another favorite recycler view to manage user's favorites
 *
 * @author Nirbhay Ashok Pherwani
 * email: np5318@rit.edu
 * profile: https://nirbhay.me
 */

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.sportstracking.strackify.R;
import com.sportstracking.strackify.adapter.TeamSelectionAdapter;
import com.sportstracking.strackify.adapter.TeamsSelectedAdapter;
import com.sportstracking.strackify.model.Team;
import com.sportstracking.strackify.utility.Values;
import com.sportstracking.strackify.utility.VolleyService;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class TeamSelection extends AppCompatActivity {

    private RecyclerView teamSelectionRecyclerView, teamSelectedRecyclerView; // for showing searched teams and already selected favorites;
    private TeamSelectionAdapter teamSelectionAdapter;
    private TeamsSelectedAdapter teamSelectedAdapter;
    private VolleyService volleyService;
    private String selectedSport;
    private String selectedCountry;
    private ArrayList<Team> selectedTeamsData;
    private ArrayList<String> selectedTeamIds;
    private FloatingActionButton moveNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreviousUserSelections();
        setContentView(R.layout.activity_team_selection);
        setupFAB();
        setupService();
        setupDataView();
        setupSelectedTeams();
        getTeams();
    }

    /**
     * Sets the country and sport selected by the user
     */
    private void getPreviousUserSelections() {
        if (getIntent().hasExtra(Values.SPORTS_SELECTION)) {
            selectedSport = getIntent().getStringExtra(Values.SPORTS_SELECTION);
        }
        if (getIntent().hasExtra(Values.COUNTRIES_SELECTION)) {
            selectedCountry = getIntent().getStringExtra(Values.COUNTRIES_SELECTION);
        }
    }

    /**
     * instantiates the teams and teams selected recycler view
     * sets up the layout manager for both
     */
    private void setupDataView() {
        // For displaying teams to select
        teamSelectionRecyclerView = findViewById(R.id.team_selection_recycler_view);
        teamSelectionRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        teamSelectionRecyclerView.setLayoutManager(layoutManager);


        // For displaying teams that are already selected
        teamSelectedRecyclerView = findViewById(R.id.team_selected_recycler_view);
        teamSelectedRecyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        teamSelectedRecyclerView.setLayoutManager(linearLayoutManager);
    }

    /**
     * sets up the volley service instance
     */
    private void setupService() {
        volleyService = new VolleyService(this, Values.TEAMS_SELECTION);
    }

    /**
     * uses shared preferences to retrieve previously stores
     * favorite teams from the string set. uses gson to get the object for the team
     * sets the adpater with that data
     */
    private void setupSelectedTeams() {
        selectedTeamsData = new ArrayList<>();
        selectedTeamIds = new ArrayList<>();
        SharedPreferences sharedPreferencesFavorite = getSharedPreferences(Values.FAV_TEAMS, Context.MODE_PRIVATE);
        Set<String> selectedTeams = new LinkedHashSet<>(sharedPreferencesFavorite.getStringSet(Values.FAV_TEAMS, new LinkedHashSet<String>()));
        Gson gson = new Gson();

        for (String teamJson : selectedTeams) {
            Team team = gson.fromJson(teamJson, Team.class);
            selectedTeamsData.add(team);
            selectedTeamIds.add(team.getTeamId());
        }

        Collections.reverse(selectedTeamsData);
        teamSelectedAdapter = new TeamsSelectedAdapter(this, selectedTeamsData, volleyService);
        teamSelectedRecyclerView.setAdapter(teamSelectedAdapter);

    }

    /**
     * updates the favorites bar with new selections
     * @param selectedTeams string set of new favorites to populate
     */
    public void updateSelectedTeams(Set<String> selectedTeams) {
        Gson gson = new Gson();
        selectedTeamsData.clear();
        List<String> teamsJson = new ArrayList<>(selectedTeams);
        Collections.reverse(teamsJson);

        for (String teamJson : teamsJson) {
            Team team = gson.fromJson(teamJson, Team.class);
            selectedTeamsData.add(team);
        }
        teamSelectedAdapter.updateTeamsData(selectedTeamsData);
        teamSelectedAdapter.notifyDataSetChanged();

    }

    /**
     * volley service request to get the teams for options choosen by user
     */
    private void getTeams() {
        volleyService.makeRequest(Values.TEAMS + Values.SPORT_IDENTIFIER + selectedSport + Values.COUNTRY_IDENTIFIER + selectedCountry);
    }

    /**
     * updates the ui with the response obtained from the api
     * populates it with teams user can select
     * covers a range of properties
     * sets the adapter. displays a message if no results found
     * @param response
     */
    public void updateUI(JSONObject response) {

        ArrayList<Team> teams = new ArrayList<>();
        try {
            JSONArray teamsData = (JSONArray) response.get("teams");
            TextView notFoundTextView = findViewById(R.id.notFoundMessage);
            notFoundTextView.setVisibility(View.INVISIBLE);
            for (int counter = 0; counter < teamsData.length(); counter++) {
                JSONObject teamItem = (JSONObject) teamsData.get(counter);
                if (!selectedTeamIds.contains(teamItem.getString("idTeam"))) {
                    Team team = new Team();
                    team.setTeamName(teamItem.getString("strTeam"));
                    team.setTeamBadge(teamItem.getString("strTeamBadge"));
                    team.setTeamBanner(teamItem.getString("strTeamBanner"));
                    team.setTeamDescription(teamItem.getString("strDescriptionEN"));
                    team.setTeamCountry(teamItem.getString("strCountry"));
                    team.setSportName(teamItem.getString("strSport"));
                    team.setTeamId(teamItem.getString("idTeam"));
                    team.setYoutube(teamItem.getString("strYoutube"));
                    team.setInstagram(teamItem.getString("strInstagram"));
                    team.setFacebook(teamItem.getString("strFacebook"));
                    team.setTwitter(teamItem.getString("strTwitter"));
                    team.setFanArt(teamItem.getString("strTeamFanart1"));
                    team.setWebsite(teamItem.getString("strWebsite"));
                    team.setFormedYear(teamItem.getString("intFormedYear"));
                    team.setGender(teamItem.getString("strGender"));
                    team.setLeagueName(teamItem.getString("strLeague"));
                    team.setStadium(teamItem.getString("strStadium"));
                    team.setTeamLogo(teamItem.getString("strTeamLogo"));
                    teams.add(team);
                }
            }
            teamSelectionAdapter = new TeamSelectionAdapter(this, teams, volleyService);
            teamSelectionRecyclerView.setAdapter(teamSelectionAdapter);
        } catch (Exception e) {
            TextView notFoundTextView = findViewById(R.id.notFoundMessage);
            notFoundTextView.setVisibility(View.VISIBLE);
            ImageView oopsImage = findViewById(R.id.notFoundImage);
            oopsImage.setVisibility(View.VISIBLE);
            Toast.makeText(this, "No teams found! Try another combination!", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * adjusts both the recycler views if needed if a favorite is removed
     * @param team
     */
    public void adjustRemovedFavorite(Team team) {
        if (selectedCountry.equals(team.getTeamCountry()) && selectedSport.equals(team.getSportName())) {
            teamSelectionAdapter.adjustRemovedFavorite(team);
            teamSelectionAdapter.notifyDataSetChanged();
        }
    }

    /**
     * hides the move forward button if there is no favorite selected. shows it if there is atleast one.
     */
    public void updateFAB() {
        SharedPreferences sharedPreferencesFavorite = getSharedPreferences(Values.FAV_TEAMS, Context.MODE_PRIVATE);
        Set<String> selectedTeams = new LinkedHashSet<>(sharedPreferencesFavorite.getStringSet(Values.FAV_TEAMS, new LinkedHashSet<String>()));
        if (selectedTeams.size() > 0) {
            moveNext.show();
        } else {
            moveNext.hide();
        }

    }

    /**
     * sets the floating action button to move to home activity if clicked
     */
    public void setupFAB() {
        moveNext = findViewById(R.id.moveNextFAB);
        moveNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeamSelection.this, Home.class);
                startActivity(intent);
            }
        });
        updateFAB();
    }

    /**
     * for searching from teams populated for the particular sport and country selected
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
                try {
                    teamSelectionAdapter.getFilter().filter(newText);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Nothing to search!", Toast.LENGTH_SHORT);
                }
                return false;
            }
        });

        return true;
    }
}
