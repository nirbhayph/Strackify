package com.sportstracking.strackify.ui.changefavorite;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sportstracking.strackify.utility.Constants;
import com.sportstracking.strackify.utility.VolleyService;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsViewModel extends AndroidViewModel {


    public SettingsViewModel(Application application) {
        super(application);
    }
}