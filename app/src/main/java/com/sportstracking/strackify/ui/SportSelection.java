package com.sportstracking.strackify.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sportstracking.strackify.R;
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
