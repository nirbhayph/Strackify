package com.sportstracking.strackify.ui.upcomingevents;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.sportstracking.strackify.model.PastEvent;
import com.sportstracking.strackify.model.Team;
import com.sportstracking.strackify.model.UpcomingEvent;
import com.sportstracking.strackify.utility.Constants;
import com.sportstracking.strackify.utility.VolleyService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class UpcomingEventsViewModel extends AndroidViewModel {

    private VolleyService volleyService;
    private MutableLiveData<String> teamName;
    private MutableLiveData<Boolean> notFoundStatus;
    private MutableLiveData<ArrayList<UpcomingEvent>> upcomingEvents = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Team>> favorites = new MutableLiveData<>();


    public UpcomingEventsViewModel(Application application) {
        super(application);
        teamName = new MutableLiveData<>();
        notFoundStatus = new MutableLiveData<>();
        updateFavorites();
        makeDataRequest();
    }

    public LiveData<ArrayList<UpcomingEvent>> getUpcomingEvent(){
        return upcomingEvents;
    }
    public LiveData<ArrayList<Team>> getFavorites(){
        return favorites;
    }
    public LiveData<String> getTeamName() {
        return teamName;
    }
    public LiveData<Boolean> getNotFoundStatus() {return notFoundStatus; }




    public void updateUpcomingEvents(JSONObject response){

        ArrayList<UpcomingEvent> upcomingEventsArrayList = new ArrayList<>();
        try{
            JSONArray upcomingEventsData = (JSONArray) response.get("events");
            for(int counter = 0; counter<upcomingEventsData.length(); counter++){
                JSONObject upcomingEventItem = (JSONObject) upcomingEventsData.get(counter);
                UpcomingEvent upcomingEvent = new UpcomingEvent();
                upcomingEvent.setEventId(upcomingEventItem.get("idEvent").toString());
                upcomingEvent.setEventName(upcomingEventItem.get("strEvent").toString());
                upcomingEvent.setEventDate(upcomingEventItem.get("dateEvent").toString());
                upcomingEvent.setEventLeague(upcomingEventItem.get("strLeague").toString());

                upcomingEvent.setEventTime(upcomingEventItem.get("strTime").toString());
                upcomingEvent.setAwayScore(upcomingEventItem.get("intAwayScore").toString());
                upcomingEvent.setAwayTeam(upcomingEventItem.get("strAwayTeam").toString());
                upcomingEvent.setHomeScore(upcomingEventItem.get("intHomeScore").toString());
                upcomingEvent.setHomeTeam(upcomingEventItem.get("strHomeTeam").toString());

                if(!upcomingEventItem.get("strThumb").toString().isEmpty() && !upcomingEventItem.get("strThumb").toString().equals("null") ){
                    upcomingEvent.setEventThumbnail(upcomingEventItem.get("strThumb").toString());

                }
                else{
                    upcomingEvent.setEventThumbnail("https://source.unsplash.com/1500x300/?"+upcomingEvent.getHomeTeam()+","+upcomingEvent.getAwayTeam()+","+upcomingEvent.getEventLeague());
                }
                upcomingEventsArrayList.add(upcomingEvent);
            }
            notFoundStatus.setValue(false);
            upcomingEvents.setValue(upcomingEventsArrayList);
        }
        catch (Exception e){
            e.printStackTrace();
            notFoundStatus.setValue(true);

            upcomingEvents.setValue(new ArrayList<UpcomingEvent>());

            Toast.makeText(getApplication(), "No upcoming events found!", Toast.LENGTH_LONG).show();
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
        String favoriteTeamId = sharedPref.getString(Constants.LATEST_FAV_TEAM, Constants.FAV_CHECKER);
        teamName.setValue(sharedPref.getString(Constants.LATEST_FAV_TEAM_NAME, ""));
        volleyService = new VolleyService(this, Constants.UPCOMING_EVENTS_DISPLAY, getApplication().getApplicationContext());
        volleyService.makeRequest(Constants.UPCOMING_EVENTS + Constants.TEAM_ID_IDENTIFIER + favoriteTeamId);
    }

}