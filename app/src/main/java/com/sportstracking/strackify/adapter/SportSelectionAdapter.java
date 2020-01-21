package com.sportstracking.strackify.adapter;

/**
 * strackify: sport selection adapter
 * populates the sports data
 * in the sports recycler view.
 * uses the sport_view layout file
 *
 * @author Nirbhay Ashok Pherwani
 * email: np5318@rit.edu
 * profile: https://nirbhay.me
 */

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.sportstracking.strackify.ui.CountrySelection;
import com.sportstracking.strackify.R;
import com.sportstracking.strackify.model.Sport;
import com.sportstracking.strackify.utility.Values;
import com.sportstracking.strackify.utility.VolleyService;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

public class SportSelectionAdapter extends RecyclerView.Adapter<SportSelectionAdapter.MyViewHolder> {
    private ArrayList<Sport> sportsData;
    private VolleyService volleyService;
    private Activity activity;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView sportNameView;
        public CircleImageView sportThumbView;

        public MyViewHolder(View v) {
            super(v);
            sportNameView = v.findViewById(R.id.sportName);
            sportThumbView = v.findViewById(R.id.sportThumb);
        }
    }

    /**
     * Paremtrized constructor to setup the adapter
     *
     * @param activity      activity set from
     * @param sportsData sports data array list
     * @param volleyService instance for volley service
     */
    public SportSelectionAdapter(Activity activity, ArrayList<Sport> sportsData, VolleyService volleyService) {
        this.sportsData = sportsData;
        this.volleyService = volleyService;
        this.activity = activity;
    }

    /**
     * inflates custom sport_view
     *
     * @param parent   parent view group
     * @param viewType view type
     * @return adapter view holder
     */
    @Override
    public SportSelectionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sport_view, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    /**
     * displays the sport name with the icon
     * on clicking adds extras to the intent and moves to the next task
     *
     * @param holder   reference to view
     * @param position position in the recycler view
     */
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.sportNameView.setText(sportsData.get(position).getSportName());
        volleyService.makeImageRequest(sportsData.get(position).getSportThumbnail(), holder.sportThumbView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, CountrySelection.class);
                intent.putExtra(Values.SPORTS_SELECTION, sportsData.get(position).getSportName());
                activity.startActivity(intent);
            }
        });

    }

    /**
     * number of items in the array list
     *
     * @return number of items in the sports data list
     */
    @Override
    public int getItemCount() {
        return sportsData.size();
    }

}