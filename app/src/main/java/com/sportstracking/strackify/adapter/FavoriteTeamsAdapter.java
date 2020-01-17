package com.sportstracking.strackify.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.sportstracking.strackify.R;
import com.sportstracking.strackify.model.Team;
import com.sportstracking.strackify.ui.pastevents.PastEventsFragment;
import com.sportstracking.strackify.ui.upcomingevents.UpcomingEventsFragment;
import com.sportstracking.strackify.utility.Constants;
import com.sportstracking.strackify.utility.VolleyService;


import java.util.ArrayList;

public class FavoriteTeamsAdapter extends RecyclerView.Adapter<FavoriteTeamsAdapter.MyViewHolder> {
    private ArrayList<Team> teamsData;
    private Activity activity;
    private VolleyService volleyService;
    private Object reference;
    private String callFrom;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView teamNameView;
        public ImageView teamThumbView;

        public MyViewHolder(View v) {
            super(v);
            teamNameView = v.findViewById(R.id.teamName);
            teamThumbView = v.findViewById(R.id.teamThumb);
        }
    }

    public FavoriteTeamsAdapter(Activity activity, ArrayList<Team> teamsData, VolleyService volleyService, Object reference, String callFrom) {
        this.activity = activity;
        this.teamsData = teamsData;
        this.volleyService = volleyService;
        this.reference = reference;
        this.callFrom = callFrom;
    }

    @Override
    public FavoriteTeamsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favorite_team_view, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.teamNameView.setText(teamsData.get(position).getTeamName());
        volleyService.makeImageRequest(teamsData.get(position).getTeamBadge(), holder.teamThumbView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String teamName = teamsData.get(position).getTeamName();
                String teamId = teamsData.get(position).getTeamId();

                switch(callFrom){
                    case Constants
                            .PAST_EVENTS: {
                        PastEventsFragment pastEventsFragment = (PastEventsFragment) reference;
                        pastEventsFragment.makeNewRequest(teamId, teamName);
                        break;
                    }
                    case Constants
                            .UPCOMING_EVENTS: {
                        UpcomingEventsFragment upcomingEventsFragment = (UpcomingEventsFragment) reference;
                        upcomingEventsFragment.makeNewRequest(teamId, teamName);
                        break;
                    }
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return teamsData.size();
    }


}