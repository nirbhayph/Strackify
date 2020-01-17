package com.sportstracking.strackify.ui.upcomingevents;

import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
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
import com.sportstracking.strackify.utility.Constants;
import com.sportstracking.strackify.utility.VolleyService;

import java.util.ArrayList;

public class UpcomingEventsFragment extends Fragment {

    private UpcomingEventsViewModel upcomingEventsViewModel;
    private ArrayList<UpcomingEvent> upcomingEvents;
    private ArrayList<Team> favorites;


    private RecyclerView upcomingEventsRecyclerView, favoriteTeamsRecyclerView;
    private UpcomingEventsAdapter upcomingEventsAdpater;
    private FavoriteTeamsAdapter favoriteTeamsAdapter;
    private VolleyService volleyService;

    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        upcomingEventsViewModel =
                ViewModelProviders.of(this).get(UpcomingEventsViewModel.class);
        if(getView()!=null){
            root = getView();
        }
        else{
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

    public void setup(){
        setHasOptionsMenu(true);
        setupDataView();
        setupDataObserver();
    }

    private void setupDataView(){
        upcomingEventsRecyclerView = root.findViewById(R.id.upcoming_events_recycler_view);
        upcomingEventsRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        upcomingEventsRecyclerView.setLayoutManager(layoutManager);

        favoriteTeamsRecyclerView = root.findViewById(R.id.favorite_teams_recycler_view);
        favoriteTeamsRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManagerFavorites = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        favoriteTeamsRecyclerView.setLayoutManager(layoutManagerFavorites);
    }

    public void makeNewRequest(String teamId, String teamName){
        SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences(Constants.LATEST_FAV_TEAM, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constants.LATEST_FAV_TEAM, teamId);
        editor.putString(Constants.LATEST_FAV_TEAM_NAME, teamName);
        editor.commit();

        upcomingEventsViewModel.makeDataRequest();
    }

    public void setupDataObserver(){

        upcomingEventsViewModel.getUpcomingEvent().observe(this, new Observer<ArrayList<UpcomingEvent>>() {
            @Override
            public void onChanged(@Nullable ArrayList<UpcomingEvent> upcomingEvents ) {
                updateUpcomingEvents(upcomingEvents);
            }
        });

        upcomingEventsViewModel.getFavorites().observe(this, new Observer<ArrayList<Team>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Team> teams ) {
                updateFavorites(teams);
            }
        });
    }

    public void updateFavorites(ArrayList<Team> favorites){
        this.favorites = favorites;
        updateFavoritesBar();
    }

    public void updateFavoritesBar(){
        favoriteTeamsAdapter = new FavoriteTeamsAdapter(getActivity(), favorites, new VolleyService(getActivity(), Constants.FAV_TEAMS), this, Constants.UPCOMING_EVENTS);
        favoriteTeamsRecyclerView.setAdapter(favoriteTeamsAdapter);
    }

    public void updateUpcomingEvents(ArrayList<UpcomingEvent> upcomingEvents){
        this.upcomingEvents = upcomingEvents;
        updateUI();
    }

    public void updateUI(){
        volleyService = new VolleyService(getActivity(), Constants.UPCOMING_EVENTS_DISPLAY);

        upcomingEventsAdpater = new UpcomingEventsAdapter(getActivity(), upcomingEvents, volleyService);
        upcomingEventsRecyclerView.setAdapter(upcomingEventsAdpater);
    }

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
                try{
                    upcomingEventsAdpater.getFilter().filter(newText);
                }catch (Exception e){
                    return false;
                }
                return false;
            }
        });
    }

}