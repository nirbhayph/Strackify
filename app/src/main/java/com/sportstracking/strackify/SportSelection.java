package com.sportstracking.strackify;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sportstracking.strackify.adapter.SportSelectionAdapter;
import com.sportstracking.strackify.model.Sport;
import com.sportstracking.strackify.utility.Constants;
import com.sportstracking.strackify.utility.VolleyService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SportSelection extends AppCompatActivity {

    private RecyclerView sportSelectionRecyclerView;
    private SportSelectionAdapter sportSelectionAdapter;
    private VolleyService volleyService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sport_selection);
        setupDataView();
        getSports();
    }

    private void setupDataView(){
        sportSelectionRecyclerView = findViewById(R.id.sports_selection_recycler_view);
        sportSelectionRecyclerView.setHasFixedSize(true);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        sportSelectionRecyclerView.setLayoutManager(layoutManager);
    }

    private void getSports(){
        volleyService = new VolleyService(this, Constants.SPORTS_SELECTION);
        volleyService.makeRequest(Constants.ALL_SPORTS);
        //getImage(volleyService);
    }

    private void getImage(VolleyService volleyService){
        ImageView imageView = findViewById(R.id.imageToGet);
        volleyService.makeImageRequest("https://images.unsplash.com/photo-1578730171162-7242d0e2f2e9?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1350&q=80", imageView);
    }

    public void updateUI(JSONObject response){
        ArrayList<Sport> sports = new ArrayList<Sport>();
        try{
            JSONArray sportsData = (JSONArray) response.get("sports");
            for(int counter = 0; counter<sportsData.length(); counter++){
               JSONObject sportItem = (JSONObject) sportsData.get(counter);
               Sport sport = new Sport();
               sport.setSportName(sportItem.getString("strSport"));
               sport.setSportDescription(sportItem.getString("strSportDescription"));
               sport.setSportFormat(sportItem.getString("strFormat"));
               sport.setSportThumbnail(sportItem.getString("strSportThumb"));
               sport.setSportId(sportItem.getString("idSport"));
               sports.add(sport);
            }
            sportSelectionAdapter = new SportSelectionAdapter(this, sports, volleyService);
            sportSelectionRecyclerView.setAdapter(sportSelectionAdapter);
        }
        catch (Exception e){

        }

    }
}
