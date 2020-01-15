package com.sportstracking.strackify.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sportstracking.strackify.ui.Home;
import com.sportstracking.strackify.R;
import com.sportstracking.strackify.model.Team;
import com.sportstracking.strackify.utility.Constants;
import com.sportstracking.strackify.utility.VolleyService;

import java.util.ArrayList;

public class TeamSelectionAdapter extends RecyclerView.Adapter<TeamSelectionAdapter.MyViewHolder> implements Filterable {
    private ArrayList<Team> teamsData;
    private ArrayList<Team> teamsDataComplete;
    private Activity activity;
    private VolleyService volleyService;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView teamNameView;
        public ImageView teamThumbView;
        public MyViewHolder(View v) {
            super(v);
            teamNameView = v.findViewById(R.id.teamName);
            teamThumbView = v.findViewById(R.id.teamThumb);
        }
    }

    public TeamSelectionAdapter(Activity activity, ArrayList<Team> teamsData, VolleyService volleyService) {
        this.activity = activity;
        this.teamsData = teamsData;
        this.teamsDataComplete = new ArrayList<>(teamsData);
        this.volleyService = volleyService;
    }

    @Override
    public TeamSelectionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.team_view, parent, false);
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
                SharedPreferences sharedPref = activity.getSharedPreferences(Constants.FAV_TEAM, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(Constants.FAV_TEAM, teamsData.get(position).getTeamName());
                editor.commit();
                Intent intent = new Intent(activity, Home.class);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return teamsData.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Team> filteredTeamsData = new ArrayList<>();
            if(constraint == null || constraint.length()==0){
                filteredTeamsData.addAll(teamsDataComplete);
            }
            else{
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(Team team : teamsDataComplete){
                    if(team.getTeamName().toLowerCase().contains(filterPattern)){
                        filteredTeamsData.add(team);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredTeamsData;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            teamsData.clear();
            teamsData.addAll((ArrayList<Team>) results.values);
            notifyDataSetChanged();
        }
    };
}