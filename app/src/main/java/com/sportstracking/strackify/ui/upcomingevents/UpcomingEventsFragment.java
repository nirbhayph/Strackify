package com.sportstracking.strackify.ui.upcomingevents;

/**
 * strackify: upcoming events fragment
 * has observable attached to retrieve data updates from the view model
 * populates upcoming events in a recycler view
 * populates favorites in a story like recycler view for the user to choose from
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.sportstracking.strackify.R;
import com.sportstracking.strackify.adapter.FavoriteTeamsAdapter;
import com.sportstracking.strackify.adapter.UpcomingEventsAdapter;
import com.sportstracking.strackify.model.Team;
import com.sportstracking.strackify.model.UpcomingEvent;
import com.sportstracking.strackify.ui.SportSelection;
import com.sportstracking.strackify.utility.Values;
import com.sportstracking.strackify.utility.VolleyService;
import java.util.ArrayList;

public class UpcomingEventsFragment extends Fragment {

    private UpcomingEventsViewModel upcomingEventsViewModel;
    private ArrayList<UpcomingEvent> upcomingEvents;
    private ArrayList<Team> favorites;

    private ImageView addTeam;

    private RecyclerView upcomingEventsRecyclerView, favoriteTeamsRecyclerView;
    private UpcomingEventsAdapter upcomingEventsAdpater;
    private FavoriteTeamsAdapter favoriteTeamsAdapter;
    private VolleyService volleyService;

    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        upcomingEventsViewModel =
                ViewModelProviders.of(this).get(UpcomingEventsViewModel.class);
        if (getView() != null) {
            root = getView();
        } else {
            root = inflater.inflate(R.layout.fragment_upcoming_events, container, false);
            setup();
        }
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        setup();
    }

    /**
     * makes calls to data observer and setting the data view function
     */
    public void setup() {
        setHasOptionsMenu(true);
        setupDataView();
        setupDataObserver();
    }

    /**
     * sets up both the recycler views
     * sets the layout for both
     * on pressing add user is taken through the process of adding another favorite
     */
    private void setupDataView() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("  Upcoming Events");

        upcomingEventsRecyclerView = root.findViewById(R.id.upcoming_events_recycler_view);
        upcomingEventsRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        upcomingEventsRecyclerView.setLayoutManager(layoutManager);

        favoriteTeamsRecyclerView = root.findViewById(R.id.favorite_teams_recycler_view);
        favoriteTeamsRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManagerFavorites = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        favoriteTeamsRecyclerView.setLayoutManager(layoutManagerFavorites);

        addTeam = root.findViewById(R.id.addTeam);

        addTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SportSelection.class);
                getActivity().startActivity(intent);
            }
        });
    }

    /**
     * makes a request to the view model for obtaining data for a
     * particular favorite team clicked by the user
     *
     * @param teamId   team id which events are required
     * @param teamName team name for which events are required
     */
    public void makeNewRequest(String teamId, String teamName) {
        SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences(Values.LATEST_FAV_TEAM, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Values.LATEST_FAV_TEAM, teamId);
        editor.putString(Values.LATEST_FAV_TEAM_NAME, teamName);
        editor.commit();

        upcomingEventsViewModel.makeDataRequest();
    }

    /**
     * sets up the data observer for getting the upcoming events, favorites and team name to set the header
     * also sets an observer for no data found for an event. changes ui accordingly
     */
    public void setupDataObserver() {

        upcomingEventsViewModel.getUpcomingEvent().observe(this, new Observer<ArrayList<UpcomingEvent>>() {
            @Override
            public void onChanged(@Nullable ArrayList<UpcomingEvent> upcomingEvents) {
                updateUpcomingEvents(upcomingEvents);
            }
        });

        upcomingEventsViewModel.getFavorites().observe(this, new Observer<ArrayList<Team>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Team> teams) {
                updateFavorites(teams);
            }
        });

        upcomingEventsViewModel.getTeamName().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String team) {
                TextView title = root.findViewById(R.id.teamsHeaderText);
                title.setText(team);
            }
        });

        upcomingEventsViewModel.getNotFoundStatus().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean status) {
                TextView notFoundMessage = root.findViewById(R.id.notFoundMessage);
                ImageView notFoundImage = root.findViewById(R.id.notFoundImage);

                if (status) {
                    notFoundMessage.setVisibility(View.VISIBLE);
                    notFoundImage.setVisibility(View.VISIBLE);
                } else {
                    notFoundImage.setVisibility(View.GONE);
                    notFoundMessage.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * stores the favorites
     * makes a call to update the favorites bar
     *
     * @param favorites list of favorites selected by user
     */
    public void updateFavorites(ArrayList<Team> favorites) {
        this.favorites = favorites;
        updateFavoritesBar();
    }

    /**
     * sets the adapter for the recycler view with the favorites data
     */
    public void updateFavoritesBar() {
        favoriteTeamsAdapter = new FavoriteTeamsAdapter(getActivity(), favorites, new VolleyService(getActivity(), Values.FAV_TEAMS), this, Values.UPCOMING_EVENTS);
        favoriteTeamsRecyclerView.setAdapter(favoriteTeamsAdapter);
    }

    /**
     * stores the upcoming events
     * makes a call to update the ui with the upcoming events stores
     *
     * @param upcomingEvents upcoming events data
     */
    public void updateUpcomingEvents(ArrayList<UpcomingEvent> upcomingEvents) {
        this.upcomingEvents = upcomingEvents;
        updateUI();
    }

    /**
     * sets the adapter for the recycler view with the upcoming events data
     */
    public void updateUI() {
        volleyService = new VolleyService(getActivity(), Values.UPCOMING_EVENTS_DISPLAY);
        upcomingEventsAdpater = new UpcomingEventsAdapter(getActivity(), upcomingEvents, volleyService);
        upcomingEventsRecyclerView.setAdapter(upcomingEventsAdpater);
    }

    /**
     * for searching from events populated for the selected favorite
     *
     * @param menu menu on action bar
     * @return return true or false after menu inflated
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.options_menu, menu);

        SearchManager searchManager =
                (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getActivity().getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try {
                    upcomingEventsAdpater.getFilter().filter(newText);
                } catch (Exception e) {
                    return false;
                }
                return false;
            }
        });
    }

}