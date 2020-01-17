package com.sportstracking.strackify.ui.changefavorite;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sportstracking.strackify.utility.Constants;

public class ChangeFavoriteViewModel extends AndroidViewModel {

    private MutableLiveData<String> teamName;

    public ChangeFavoriteViewModel(Application application) {
        super(application);
        teamName = new MutableLiveData<>();
        SharedPreferences sharedPref = getApplication().getApplicationContext().getSharedPreferences(Constants.LATEST_FAV_TEAM, Context.MODE_PRIVATE);
        teamName.setValue(sharedPref.getString(Constants.LATEST_FAV_TEAM_NAME, "Favorite Team"));
    }

    public LiveData<String> getText() {
        return teamName;
    }
}