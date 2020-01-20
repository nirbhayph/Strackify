package com.sportstracking.strackify.ui.pastevents;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
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
import com.sportstracking.strackify.adapter.PastEventsAdapter;
import com.sportstracking.strackify.model.PastEvent;
import com.sportstracking.strackify.model.Team;
import com.sportstracking.strackify.ui.SportSelection;
import com.sportstracking.strackify.utility.Constants;
import com.sportstracking.strackify.utility.VolleyService;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PastEventsFragment extends Fragment {

    private PastEventsViewModel pastEventsViewModel;
    private ArrayList<PastEvent> pastEvents;
    private ArrayList<Team> favorites;

    private ImageView addTeam;

    private RecyclerView pastEventsRecyclerView, favoriteTeamsRecyclerView;
    private PastEventsAdapter pastEventsAdapter;
    private FavoriteTeamsAdapter favoriteTeamsAdapter;
    private VolleyService volleyService;

    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        pastEventsViewModel =
                ViewModelProviders.of(this).get(PastEventsViewModel.class);
        if(getView()!=null){
            root = getView();
        }
        else{
            root = inflater.inflate(R.layout.fragment_past_events, container, false);
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

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("  Past Events");

        pastEventsRecyclerView = root.findViewById(R.id.past_events_recycler_view);
        pastEventsRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        pastEventsRecyclerView.setLayoutManager(layoutManager);

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

    public void makeNewRequest(String teamId, String teamName){
        SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences(Constants.LATEST_FAV_TEAM, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constants.LATEST_FAV_TEAM, teamId);
        editor.putString(Constants.LATEST_FAV_TEAM_NAME, teamName);
        editor.commit();

        pastEventsViewModel.makeDataRequest();
    }

    public void setupDataObserver(){

        pastEventsViewModel.getPastEvents().observe(this, new Observer<ArrayList<PastEvent>>() {
            @Override
            public void onChanged(@Nullable ArrayList<PastEvent> pastEvents ) {
                updatePastEvents(pastEvents);
            }
        });

        pastEventsViewModel.getFavorites().observe(this, new Observer<ArrayList<Team>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Team> teams ) {
                updateFavorites(teams);
            }
        });

        pastEventsViewModel.getTeamName().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String team ) {
                TextView title = root.findViewById(R.id.teamsHeaderText);
                title.setText(team);
            }
        });

        pastEventsViewModel.getNotFoundStatus().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean status ) {
                TextView notFoundMessage = root.findViewById(R.id.notFoundMessage);
                ImageView notFoundImage = root.findViewById(R.id.notFoundImage);

                if(status){
                    notFoundMessage.setVisibility(View.VISIBLE);
                    notFoundImage.setVisibility(View.VISIBLE);
                }
                else{
                    notFoundImage.setVisibility(View.GONE);
                    notFoundMessage.setVisibility(View.GONE);
                }
            }
        });
    }

    public void updateFavorites(ArrayList<Team> favorites){
        this.favorites = favorites;
        updateFavoritesBar();
    }


    public void updatePastEvents(ArrayList<PastEvent> pastEvents){
        this.pastEvents = pastEvents;
        updateUI();
    }

    public void updateUI(){
        volleyService = new VolleyService(getActivity(), Constants.PAST_EVENTS_DISPLAY);

        pastEventsAdapter = new PastEventsAdapter(getActivity(), pastEvents, volleyService);
        pastEventsRecyclerView.setAdapter(pastEventsAdapter);

    }

    public void updateFavoritesBar(){
        favoriteTeamsAdapter = new FavoriteTeamsAdapter(getActivity(), favorites, new VolleyService(getActivity(), Constants.FAV_TEAMS), this, Constants.PAST_EVENTS);
        favoriteTeamsRecyclerView.setAdapter(favoriteTeamsAdapter);
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
                try {
                    pastEventsAdapter.getFilter().filter(newText);
                }
                catch (Exception e){
                    return false;
                }
                return false;
            }
        });
    }

}