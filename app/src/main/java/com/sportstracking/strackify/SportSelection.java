package com.sportstracking.strackify;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.sportstracking.strackify.utility.Constants;
import com.sportstracking.strackify.utility.VolleyService;

public class SportSelection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSports();
    }

    private void getSports(){
        VolleyService volleyService = new VolleyService(this.getApplicationContext());
        volleyService.makeRequest(Constants.ALL_SPORTS);
    }

}
