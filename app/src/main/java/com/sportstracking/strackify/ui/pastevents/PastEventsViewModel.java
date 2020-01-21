package com.sportstracking.strackify.ui.pastevents;

/**
 * strackify: past events fragment view model
 * uses live data for view to create observables
 * retrieves data to populate past events
 * retrieved data from shared preferences to populate favorites
 * uses live data for storage which can then be used by fragment to observe
 *
 * @author Nirbhay Ashok Pherwani
 * email: np5318@rit.edu
 * profile: https://nirbhay.me
 */

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.gson.Gson;
import com.sportstracking.strackify.model.PastEvent;
import com.sportstracking.strackify.model.Team;
import com.sportstracking.strackify.utility.Values;
import com.sportstracking.strackify.utility.VolleyService;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class PastEventsViewModel extends AndroidViewModel {

    private VolleyService volleyService;
    private MutableLiveData<String> teamName;
    private MutableLiveData<Boolean> notFoundStatus;
    private MutableLiveData<ArrayList<PastEvent>> pastEvents = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Team>> favorites = new MutableLiveData<>();


    public PastEventsViewModel(Application application) {
        super(application);
        teamName = new MutableLiveData<>();
        notFoundStatus = new MutableLiveData<>();
        updateFavorites();
        makeDataRequest();
    }

    public LiveData<ArrayList<PastEvent>> getPastEvents() {
        return pastEvents;
    }

    public LiveData<ArrayList<Team>> getFavorites() {
        return favorites;
    }

    public LiveData<String> getTeamName() {
        return teamName;
    }

    public LiveData<Boolean> getNotFoundStatus() {
        return notFoundStatus;
    }

    /**
     * called by volley service after data retrieved from api
     * mutable live data is updated from here which is then reflected in fragment because of
     * observable on getters above
     * @param response JSONObject response obtained from api
     */
    public void updatePastEvents(JSONObject response) {

        ArrayList<PastEvent> pastEventsArrayList = new ArrayList<>();
        try {
            JSONArray pastEventsData = (JSONArray) response.get("results");
            for (int counter = 0; counter < pastEventsData.length(); counter++) {
                JSONObject pastEventItem = (JSONObject) pastEventsData.get(counter);
                PastEvent pastEvent = new PastEvent();
                pastEvent.setEventId(pastEventItem.get("idEvent").toString());
                pastEvent.setEventName(pastEventItem.get("strEvent").toString());
                pastEvent.setEventDate(pastEventItem.get("dateEvent").toString());
                pastEvent.setEventLeague(pastEventItem.get("strLeague").toString());
                pastEvent.setEventTime(pastEventItem.get("strTime").toString());
                pastEvent.setAwayScore(pastEventItem.get("intAwayScore").toString());
                pastEvent.setAwayTeam(pastEventItem.get("strAwayTeam").toString());
                pastEvent.setHomeScore(pastEventItem.get("intHomeScore").toString());
                pastEvent.setHomeTeam(pastEventItem.get("strHomeTeam").toString());

                if (!pastEventItem.get("strThumb").toString().isEmpty() && !pastEventItem.get("strThumb").toString().equals("null")) {
                    pastEvent.setEventThumbnail(pastEventItem.get("strThumb").toString());
                } else {
                    pastEvent.setEventThumbnail("https://source.unsplash.com/850x400/?soccer,basketball,"+pastEvent.getEventName().split(" vs")[0]);
                }
                pastEventsArrayList.add(pastEvent);
            }
            notFoundStatus.setValue(false);
            pastEvents.setValue(pastEventsArrayList);
        } catch (Exception e) {
            e.printStackTrace();
            notFoundStatus.setValue(true);
            Toast.makeText(getApplication(), "No past events found!", Toast.LENGTH_LONG).show();
            pastEvents.setValue(new ArrayList<PastEvent>());
        }
    }

    /**
     * to retrieve favorites data from shared preferences
     * uses gson to convert string set of json to team objects
     * updates mutable live data which is then reflected in
     * fragment ui because of observable on getters above.
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

    /**
     * volley reference used to request data for past events for
     * favorite team selected in past events fragment
     */
    public void makeDataRequest() {
        SharedPreferences sharedPref = getApplication().getApplicationContext().getSharedPreferences(Values.LATEST_FAV_TEAM, Context.MODE_PRIVATE);
        String favoriteTeamId = sharedPref.getString(Values.LATEST_FAV_TEAM, Values.FAV_CHECKER);
        teamName.setValue(sharedPref.getString(Values.LATEST_FAV_TEAM_NAME, ""));
        volleyService = new VolleyService(this, Values.PAST_EVENTS_DISPLAY, getApplication().getApplicationContext());
        volleyService.makeRequest(Values.PAST_EVENTS + Values.TEAM_ID_IDENTIFIER + favoriteTeamId);
    }

}