package com.sportstracking.strackify.ui.changefavorite;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.sportstracking.strackify.R;
import com.sportstracking.strackify.ui.SportSelection;

public class ChangeFavoriteFragment extends Fragment {

    private ChangeFavoriteViewModel changeFavoriteViewModel;
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        changeFavoriteViewModel =
                ViewModelProviders.of(this).get(ChangeFavoriteViewModel.class);
        root = inflater.inflate(R.layout.fragment_change_favorite, container, false);
        final TextView textView = root.findViewById(R.id.changeFavoriteText);
        changeFavoriteViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        setup();
        return root;
    }

    public void setup(){
        Button change = root.findViewById(R.id.changeFavoriteButton);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SportSelection.class);
                startActivity(intent);
            }
        });
    }
}