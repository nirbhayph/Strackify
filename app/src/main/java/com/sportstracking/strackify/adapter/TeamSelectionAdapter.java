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
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.sportstracking.strackify.ui.Home;
import com.sportstracking.strackify.R;
import com.sportstracking.strackify.model.Team;
import com.sportstracking.strackify.ui.TeamSelection;
import com.sportstracking.strackify.utility.Constants;
import com.sportstracking.strackify.utility.VolleyService;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class TeamSelectionAdapter extends RecyclerView.Adapter<TeamSelectionAdapter.MyViewHolder> implements Filterable {
    private ArrayList<Team> teamsData;
    private ArrayList<Team> teamsDataComplete;
    private Activity activity;
    private VolleyService volleyService;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView teamNameView;
        public ImageView teamThumbView;
        public TextView teamCountryView;
        public TextView teamDescriptionView;

        public MyViewHolder(View v) {
            super(v);
            teamNameView = v.findViewById(R.id.teamName);
            teamThumbView = v.findViewById(R.id.teamThumb);
            teamCountryView = v.findViewById(R.id.teamCountry);
            teamDescriptionView = v.findViewById(R.id.teamDescription);
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
            String teamDescription = teamsData.get(position).getTeamDescription();
            if (teamDescription.length() >= 300) {
                holder.teamDescriptionView.setText(teamDescription.substring(0, 299) + " ...");
            } else {
                if (!teamDescription.equals("null") && !teamDescription.isEmpty()) {
                    holder.teamDescriptionView.setText(teamDescription);
                }
            }
            holder.teamCountryView.setText(teamsData.get(position).getTeamCountry());


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences sharedPref = activity.getSharedPreferences(Constants.LATEST_FAV_TEAM, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(Constants.LATEST_FAV_TEAM, teamsData.get(position).getTeamId());
                    editor.putString(Constants.LATEST_FAV_TEAM_NAME, teamsData.get(position).getTeamName());
                    editor.commit();

                    // For storing multiple favorites

                    Gson gson = new Gson();
                    Team newFavorite = teamsData.get(position);
                    String teamJson = gson.toJson(newFavorite);


                    SharedPreferences sharedPreferencesFavorite = activity.getSharedPreferences(Constants.FAV_TEAMS, Context.MODE_PRIVATE);
                    SharedPreferences.Editor prefEditor = sharedPreferencesFavorite.edit();


                    Set<String> favTeams = new LinkedHashSet<>(sharedPreferencesFavorite.getStringSet(Constants.FAV_TEAMS, new LinkedHashSet<String>()));
                    favTeams.add(teamJson);

                    prefEditor.putStringSet(Constants.FAV_TEAMS, favTeams);
                    prefEditor.commit();

                    TeamSelection reference = (TeamSelection) activity;
                    reference.updateSelectedTeams(favTeams);

                    Toast.makeText(activity, newFavorite.getTeamName()+" added to favorites!", Toast.LENGTH_SHORT).show();


                    teamsData.remove(position);


                    Team teamToRemove = null;

                    for(Team team : teamsDataComplete){
                        if(team.getTeamId().equals(newFavorite.getTeamId())){
                            teamToRemove = team;
                            break;
                        }
                    }

                    teamsDataComplete.remove(teamToRemove);

                    notifyDataSetChanged();

                    reference.updateFAB();
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
            try {
                teamsData.clear();
                teamsData.addAll((ArrayList<Team>) results.values);
                notifyDataSetChanged();
            }
            catch(Exception e){
                Toast.makeText(activity.getApplicationContext(), "Nothing to search!", Toast.LENGTH_SHORT);
            }
        }
    };

    public void adjustRemovedFavorite(Team team){
        this.teamsData.add(0, team);
        teamsDataComplete.add(0, team);
    }
}