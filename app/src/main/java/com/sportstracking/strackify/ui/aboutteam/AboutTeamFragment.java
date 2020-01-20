package com.sportstracking.strackify.ui.aboutteam;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sportstracking.strackify.R;
import com.sportstracking.strackify.adapter.FavoriteTeamsAdapter;
import com.sportstracking.strackify.model.Team;
import com.sportstracking.strackify.utility.Constants;
import com.sportstracking.strackify.utility.VolleyService;

import java.util.ArrayList;

public class AboutTeamFragment extends Fragment {

    private ImageButton switchToFavButton;
    private Boolean switchState=false;
    private LinearLayout layout;

    private AboutTeamViewModel aboutTeamViewModel;
    private ArrayList<Team> favorites;

    private RecyclerView favoriteTeamsRecyclerView;
    private FavoriteTeamsAdapter favoriteTeamsAdapter;
    private VolleyService volleyService;

    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        aboutTeamViewModel =
                ViewModelProviders.of(this).get(AboutTeamViewModel.class);
        if(getView()!=null){
            root = getView();
        }
        else{
            root = inflater.inflate(R.layout.fragment_about_team, container, false);
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
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("  About Team");
    }

    private void setupDataView(){
        favoriteTeamsRecyclerView = root.findViewById(R.id.favorites_recycler_view);
        favoriteTeamsRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManagerFavorites = new GridLayoutManager(getActivity().getApplicationContext(), 2);
        favoriteTeamsRecyclerView.setLayoutManager(layoutManagerFavorites);

        layout = root.findViewById(R.id.about_team);
        switchToFavButton = root.findViewById(R.id.switchToFav);
        switchToFavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switchState.equals(true)){
                    favoriteTeamsRecyclerView.setVisibility(View.VISIBLE);
                    layout.setVisibility(View.GONE);
                    switchState = false;
                    updateFavoritesView();
                }
                else{
                    favoriteTeamsRecyclerView.setVisibility(View.GONE);
                    layout.setVisibility(View.VISIBLE);
                    switchState = true;
                }
            }
        });
    }

    public void setupDataObserver(){
        aboutTeamViewModel.getFavorites().observe(this, new Observer<ArrayList<Team>>() {
            @Override
            public void onChanged(@Nullable ArrayList<Team> teams ) {
                updateFavorites(teams);
            }
        });
    }

    public void updateFavorites(ArrayList<Team> favorites){
        this.favorites = favorites;
        updateFavoritesView();
    }


    public void updateFavoritesView(){
        volleyService = new VolleyService(getActivity(), Constants.FAV_TEAMS);
        favoriteTeamsAdapter = new FavoriteTeamsAdapter(getActivity(), favorites, volleyService, this, Constants.ABOUT_TEAM);
        favoriteTeamsRecyclerView.setAdapter(favoriteTeamsAdapter);
    }

    public void showTeamDetails(Team team){
        favoriteTeamsRecyclerView.setVisibility(View.GONE);
        layout.setVisibility(View.VISIBLE);
        switchState=true;

        TextView teamName = root.findViewById(R.id.teamNameAbout);
        ImageView teamThumb = root.findViewById(R.id.teamThumbAbout);
        TextView teamDescription = root.findViewById(R.id.teamDescriptionAbout);

        teamName.setText(team.getTeamName());
        teamDescription.setText(team.getTeamDescription());
        volleyService.makeImageRequest(team.getTeamBadge(), teamThumb);
    }

}