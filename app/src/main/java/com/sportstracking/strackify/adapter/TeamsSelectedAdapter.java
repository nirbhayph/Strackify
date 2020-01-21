package com.sportstracking.strackify.adapter;

/**
 * strackify: team selected adapter (favorites bar in team selection)
 * populates the favorite teams data
 * in the favorites recycler view.
 * uses the selected_team_view layout file
 *
 * @author Nirbhay Ashok Pherwani
 * email: np5318@rit.edu
 * profile: https://nirbhay.me
 */

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.sportstracking.strackify.R;
import com.sportstracking.strackify.model.Team;
import com.sportstracking.strackify.ui.TeamSelection;
import com.sportstracking.strackify.utility.Values;
import com.sportstracking.strackify.utility.VolleyService;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import de.hdodenhof.circleimageview.CircleImageView;
import static android.content.Context.MODE_PRIVATE;
import static com.sportstracking.strackify.utility.Values.FAV_CHECKER;
import static com.sportstracking.strackify.utility.Values.LATEST_FAV_TEAM;
import static com.sportstracking.strackify.utility.Values.LATEST_FAV_TEAM_NAME;

public class TeamsSelectedAdapter extends RecyclerView.Adapter<TeamsSelectedAdapter.MyViewHolder> {
    private ArrayList<Team> teamsData;
    private Activity activity;
    private VolleyService volleyService;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView teamNameView;
        public CircleImageView teamThumbView;

        public MyViewHolder(View v) {
            super(v);
            teamNameView = v.findViewById(R.id.teamName);
            teamThumbView = v.findViewById(R.id.teamThumb);
        }
    }

    /**
     * Paremtrized constructor to setup the adapter
     *
     * @param activity      activity set from
     * @param teamsData     teams data data array list
     * @param volleyService volley service reference
     */
    public TeamsSelectedAdapter(Activity activity, ArrayList<Team> teamsData, VolleyService volleyService) {
        this.activity = activity;
        this.teamsData = teamsData;
        this.volleyService = volleyService;
    }

    /**
     * inflates custom selected_team_view
     *
     * @param parent   parent view group
     * @param viewType view type
     * @return adapter view holder
     */
    @Override
    public TeamsSelectedAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.selected_team_view, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    /**
     * displays the team name and photograph
     * has an option to remove the favorite by clicking on minus icon
     * on removal updates the dataset and notifies the change
     * uses shared preferences to manage favorites
     *
     * @param holder   reference to view
     * @param position position in the recycler view
     */
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        String teamNameText = teamsData.get(position).getTeamName();
        if (teamNameText.length() > 10) {
            teamNameText = teamNameText.substring(0, 7) + "..";
        }
        holder.teamNameView.setText(teamNameText);
        volleyService.makeImageRequest(teamsData.get(position).getTeamBadge(), holder.teamThumbView);

        holder.teamNameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String teamName = teamsData.get(position).getTeamName();

                SharedPreferences preferences = activity.getSharedPreferences(Values.FAV_TEAMS, MODE_PRIVATE);
                Set<String> selectedTeams = new LinkedHashSet<>(preferences.getStringSet(Values.FAV_TEAMS, new LinkedHashSet<String>()));

                Gson gson = new Gson();

                for (String teamJson : selectedTeams) {
                    Team team = gson.fromJson(teamJson, Team.class);
                    if (team.getTeamId().equals(teamsData.get(position).getTeamId())) {
                        selectedTeams.remove(teamJson);
                        break;
                    }
                }

                SharedPreferences.Editor editor = preferences.edit();
                editor.putStringSet(Values.FAV_TEAMS, selectedTeams);
                editor.commit();


                Toast.makeText(activity, teamName + " removed from favorites!", Toast.LENGTH_SHORT).show();

                TeamSelection reference = (TeamSelection) activity;
                reference.adjustRemovedFavorite(teamsData.get(position));
                reference.updateFAB();

                teamsData.remove(position);
                notifyDataSetChanged();

                if (getItemCount() == 0) {
                    SharedPreferences favTeamPreferences = activity.getSharedPreferences(LATEST_FAV_TEAM, MODE_PRIVATE);


                    SharedPreferences.Editor favEditor = favTeamPreferences.edit();

                    favEditor.putString(LATEST_FAV_TEAM, FAV_CHECKER);
                    favEditor.putString(LATEST_FAV_TEAM_NAME, FAV_CHECKER);

                    favEditor.commit();

                    favTeamPreferences.getString(LATEST_FAV_TEAM, Values.DEFAULT);
                }
            }
        });
    }

    /**
     * number of items in the array list
     *
     * @return number of items in the favorite teams list
     */
    @Override
    public int getItemCount() {
        return teamsData.size();
    }

    /**
     * sets the updated team data
     *
     * @param teamsData updated teams data for the favorite bar
     */
    public void updateTeamsData(ArrayList<Team> teamsData) {
        this.teamsData = teamsData;
    }

}