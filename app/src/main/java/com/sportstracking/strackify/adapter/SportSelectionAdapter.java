package com.sportstracking.strackify.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sportstracking.strackify.CountrySelection;
import com.sportstracking.strackify.R;
import com.sportstracking.strackify.model.Sport;
import com.sportstracking.strackify.utility.VolleyService;

import java.util.ArrayList;

public class SportSelectionAdapter extends RecyclerView.Adapter<SportSelectionAdapter.MyViewHolder>{
    private ArrayList<Sport> sportsData;
    private VolleyService volleyService;
    private Activity activity;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView sportNameView;
        public ImageView sportThumbView;
        public MyViewHolder(View v) {
            super(v);
            sportNameView = v.findViewById(R.id.sportName);
            sportThumbView = v.findViewById(R.id.sportThumb);
        }
    }

    public SportSelectionAdapter(Activity activity, ArrayList<Sport> sportsData, VolleyService volleyService) {
        this.sportsData = sportsData;
        this.volleyService = volleyService;
        this.activity = activity;
    }

    @Override
    public SportSelectionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sport_view, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.sportNameView.setText(sportsData.get(position).getSportName());
        volleyService.makeImageRequest(sportsData.get(position).getSportThumbnail(), holder.sportThumbView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, CountrySelection.class);
                activity.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return sportsData.size();
    }

}