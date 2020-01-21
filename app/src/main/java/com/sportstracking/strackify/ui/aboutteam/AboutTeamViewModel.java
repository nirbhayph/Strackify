package com.sportstracking.strackify.ui.aboutteam;

/**
 * strackify: about team fragment view model
 * uses live data for view to create observables
 * retrieved data from shared preferences to populate favorites teams
 * uses live data for storage which can then be used by fragment to observe
 *
 * @author Nirbhay Ashok Pherwani
 * email: np5318@rit.edu
 * profile: https://nirbhay.me
 */

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.gson.Gson;
import com.sportstracking.strackify.model.Team;
import com.sportstracking.strackify.utility.Values;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class AboutTeamViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<Team>> favorites = new MutableLiveData<>();

    public AboutTeamViewModel(Application application) {
        super(application);
        updateFavorites();
    }

    public LiveData<ArrayList<Team>> getFavorites() {
        return favorites;
    }

    /**
     * to retrieve favorites data from shared preferences
     * uses gson to convert string set of json to team objects
     * updates mutable live data which is then reflected in
     * fragment ui because of observable on getter above.
     */
    public void updateFavorites() {
        SharedPreferences sharedPreferencesFavorite = getApplication().getSharedPreferences(Values.FAV_TEAMS, Context.MODE_PRIVATE);
        Set<String> selectedTeams = new LinkedHashSet<>(sharedPreferencesFavorite.getStringSet(Values.FAV_TEAMS, new LinkedHashSet<String>()));
        Gson gson = new Gson();

        ArrayList<Team> favoritesArrayList = new ArrayList<>();

        for (String teamJson : selectedTeams) {
            Team team = gson.fromJson(teamJson, Team.class);
            favoritesArrayList.add(team);
        }

        favorites.setValue(favoritesArrayList);
    }

}