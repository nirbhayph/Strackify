package com.sportstracking.strackify.ui.aboutteam;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.sportstracking.strackify.model.Team;
import com.sportstracking.strackify.utility.Constants;
import com.sportstracking.strackify.utility.VolleyService;


import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class AboutTeamViewModel extends AndroidViewModel {

    //private VolleyService volleyService;
    private MutableLiveData<ArrayList<Team>> favorites = new MutableLiveData<>();


    public AboutTeamViewModel(Application application) {
        super(application);
        updateFavorites();
    }


    public LiveData<ArrayList<Team>> getFavorites(){
        return favorites;
    }


    public void updateFavorites(){
        SharedPreferences sharedPreferencesFavorite = getApplication().getSharedPreferences(Constants.FAV_TEAMS, Context.MODE_PRIVATE);
        Set<String> selectedTeams = new LinkedHashSet<>(sharedPreferencesFavorite.getStringSet(Constants.FAV_TEAMS, new LinkedHashSet<String>()));
        Gson gson = new Gson();

        ArrayList<Team> favoritesArrayList = new ArrayList<>();

        for(String teamJson : selectedTeams){
            Team team = gson.fromJson(teamJson, Team.class);
            favoritesArrayList.add(team);
        }

        favorites.setValue(favoritesArrayList);
    }

}