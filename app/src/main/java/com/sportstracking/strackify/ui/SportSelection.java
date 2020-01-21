package com.sportstracking.strackify.ui;

/**
 * strackify: sport selection
 * populates the sports a user can choose
 * from in the sports recyceler view.
 *
 * @author Nirbhay Ashok Pherwani
 * email: np5318@rit.edu
 * profile: https://nirbhay.me
 */

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.sportstracking.strackify.R;
import com.sportstracking.strackify.adapter.SportSelectionAdapter;
import com.sportstracking.strackify.model.Sport;
import com.sportstracking.strackify.utility.Values;
import com.sportstracking.strackify.utility.VolleyService;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import static com.sportstracking.strackify.utility.Values.FAV_CHECKER;
import static com.sportstracking.strackify.utility.Values.LATEST_FAV_TEAM;

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

    /**
     * instantiates the sports recycler view
     * sets up the layout manager for it
     */
    private void setupDataView() {
        sportSelectionRecyclerView = findViewById(R.id.sports_selection_recycler_view);
        sportSelectionRecyclerView.setHasFixedSize(true);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        sportSelectionRecyclerView.setLayoutManager(gridLayoutManager);
    }

    /**
     * sets up the volley servies and makes the request for retrieving the sports data
     */
    private void getSports() {
        volleyService = new VolleyService(this, Values.SPORTS_SELECTION);
        volleyService.makeRequest(Values.ALL_SPORTS);
    }


    /**
     * update method to populate the result obtained
     * called by the volley service
     * @param response JSONObject response of data obtained from api
     */
    public void updateUI(JSONObject response) {
        ArrayList<Sport> sports = new ArrayList<Sport>();
        try {
            JSONArray sportsData = (JSONArray) response.get("sports");
            for (int counter = 0; counter < sportsData.length(); counter++) {
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
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Unable to retrieve sports data!", Toast.LENGTH_SHORT);
        }

    }

    /**
     * on pressing back closes the app if there is no favorite present
     * otherwise takes it back into the app
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SharedPreferences preferences = getSharedPreferences(LATEST_FAV_TEAM, MODE_PRIVATE);
        String latestFavTeam = preferences.getString(LATEST_FAV_TEAM, FAV_CHECKER);
        if (latestFavTeam.equals(FAV_CHECKER)) {
            this.moveTaskToBack(true);
            this.finishAffinity();
        }
    }
}
