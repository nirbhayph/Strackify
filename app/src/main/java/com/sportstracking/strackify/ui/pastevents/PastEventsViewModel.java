package com.sportstracking.strackify.ui.pastevents;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.sportstracking.strackify.model.PastEvent;
import com.sportstracking.strackify.model.Team;
import com.sportstracking.strackify.utility.Constants;
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

    public LiveData<ArrayList<PastEvent>> getPastEvents(){
        return pastEvents;
    }

    public LiveData<ArrayList<Team>> getFavorites(){
        return favorites;
    }

    public LiveData<String> getTeamName() {
        return teamName;
    }

    public LiveData<Boolean> getNotFoundStatus() {return notFoundStatus; }

    public void updatePastEvents(JSONObject response){

        ArrayList<PastEvent> pastEventsArrayList = new ArrayList<>();
        try{
            JSONArray pastEventsData = (JSONArray) response.get("results");
            for(int counter = 0; counter<pastEventsData.length(); counter++){
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

                if(!pastEventItem.get("strThumb").toString().isEmpty() && !pastEventItem.get("strThumb").toString().equals("null") ){
                    pastEvent.setEventThumbnail(pastEventItem.get("strThumb").toString());
                }
                else{
                    pastEvent.setEventThumbnail("https://images.unsplash.com/photo-1563882757905-21bd5e0875fe?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=2089&q=80");
                }
                pastEventsArrayList.add(pastEvent);
            }
            notFoundStatus.setValue(false);
            pastEvents.setValue(pastEventsArrayList);
        }
        catch (Exception e){
            e.printStackTrace();
            notFoundStatus.setValue(true);
            Toast.makeText(getApplication(), "No past events found!", Toast.LENGTH_LONG).show();
            pastEvents.setValue(new ArrayList<PastEvent>());
        }
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

    public void makeDataRequest(){
        SharedPreferences sharedPref = getApplication().getApplicationContext().getSharedPreferences(Constants.LATEST_FAV_TEAM, Context.MODE_PRIVATE);
        String favoriteTeamId = sharedPref.getString(Constants.LATEST_FAV_TEAM, "FAV_TEAM");
        teamName.setValue(sharedPref.getString(Constants.LATEST_FAV_TEAM_NAME, ""));
        volleyService = new VolleyService(this, Constants.PAST_EVENTS_DISPLAY, getApplication().getApplicationContext());
        volleyService.makeRequest(Constants.PAST_EVENTS + Constants.TEAM_ID_IDENTIFIER + favoriteTeamId);
        Log.d("URL_PAST", Constants.PAST_EVENTS + Constants.TEAM_ID_IDENTIFIER + favoriteTeamId);

    }

}