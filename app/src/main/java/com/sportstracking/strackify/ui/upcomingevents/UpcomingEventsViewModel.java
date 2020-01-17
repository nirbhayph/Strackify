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
    private MutableLiveData<ArrayList<UpcomingEvent>> upcomingEvents = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Team>> favorites = new MutableLiveData<>();


    public UpcomingEventsViewModel(Application application) {
        super(application);
        updateFavorites();
        makeDataRequest();
    }

    public LiveData<ArrayList<UpcomingEvent>> getUpcomingEvent(){
        return upcomingEvents;
    }
    public LiveData<ArrayList<Team>> getFavorites(){
        return favorites;
    }


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

                if(!upcomingEventItem.get("strThumb").toString().isEmpty() && !upcomingEventItem.get("strThumb").toString().equals("null") ){
                    upcomingEvent.setEventThumbnail("strThumb");
                }
                else{
                    upcomingEvent.setEventThumbnail("https://images.unsplash.com/photo-1431324155629-1a6deb1dec8d?ixlib=rb-1.2.1&auto=format&fit=crop&w=1050&q=80");
                }
                upcomingEventsArrayList.add(upcomingEvent);
            }
            upcomingEvents.setValue(upcomingEventsArrayList);
        }
        catch (Exception e){
            e.printStackTrace();
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
        String favoriteTeamId = sharedPref.getString(Constants.LATEST_FAV_TEAM, "FAV_TEAM");
        volleyService = new VolleyService(this, Constants.UPCOMING_EVENTS_DISPLAY, getApplication().getApplicationContext());
        volleyService.makeRequest(Constants.UPCOMING_EVENTS + Constants.TEAM_ID_IDENTIFIER + favoriteTeamId);
        Log.d("URL_UPCOMING", Constants.UPCOMING_EVENTS + Constants.TEAM_ID_IDENTIFIER + favoriteTeamId);

    }

}