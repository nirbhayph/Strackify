package com.sportstracking.strackify.adapter;

/**
 * strackify: upcoming events adapter
 * populates the events data in for upcoming events
 * uses the event_view layout file
 *
 * @author Nirbhay Ashok Pherwani
 * email: np5318@rit.edu
 * profile: https://nirbhay.me
 */

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.sportstracking.strackify.R;
import com.sportstracking.strackify.model.UpcomingEvent;
import com.sportstracking.strackify.utility.VolleyService;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UpcomingEventsAdapter extends RecyclerView.Adapter<UpcomingEventsAdapter.MyViewHolder> implements Filterable {
    private ArrayList<UpcomingEvent> upcomingEventsData;
    private ArrayList<UpcomingEvent> upcomingEventsDataComplete;
    private Activity activity;
    private VolleyService volleyService;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView eventNameView;
        public ImageView eventThumbView;
        public TextView eventLeagueView;
        public TextView eventDateView;
        public TextView scoreDetailsView;

        public MyViewHolder(View v) {
            super(v);
            eventNameView = v.findViewById(R.id.eventName);
            eventThumbView = v.findViewById(R.id.eventThumb);
            eventLeagueView = v.findViewById(R.id.eventLeague);
            eventDateView = v.findViewById(R.id.eventDate);
            scoreDetailsView = v.findViewById(R.id.scoreDetails);
        }
    }

    /**
     * parametrized constructor to setup the adapter
     *
     * @param activity           activity set from
     * @param upcomingEventsData upcoming events data array list
     * @param volleyService      volley service reference
     */
    public UpcomingEventsAdapter(Activity activity, ArrayList<UpcomingEvent> upcomingEventsData, VolleyService volleyService) {
        this.activity = activity;
        this.upcomingEventsData = upcomingEventsData;
        this.upcomingEventsDataComplete = new ArrayList<>(upcomingEventsData);
        this.volleyService = volleyService;
    }

    /**
     * inflates custom event_view
     *
     * @param parent   parent view group
     * @param viewType view type
     * @return adapter view holder
     */
    @Override
    public UpcomingEventsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_view, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    /**
     * displays the event details for an event
     *
     * @param holder   reference to view
     * @param position position in the recycler view
     */
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.eventNameView.setText(upcomingEventsData.get(position).getEventName());

        if (!upcomingEventsData.get(position).getEventThumbnail().isEmpty() && !upcomingEventsData.get(position).getEventThumbnail().equals("null")) {
            volleyService.makeImageRequest(upcomingEventsData.get(position).getEventThumbnail(), holder.eventThumbView);
        }

        holder.eventLeagueView.setText(upcomingEventsData.get(position).getEventLeague());

        if (upcomingEventsData.get(position).getAwayScore() != null && !upcomingEventsData.get(position).getAwayScore().isEmpty() && !upcomingEventsData.get(position).getAwayScore().equals("null")) {
            holder.scoreDetailsView.setText(upcomingEventsData.get(position).getHomeTeam() + " [" + upcomingEventsData.get(position).getHomeScore() + " - " + upcomingEventsData.get(position).getAwayScore() + "] " + upcomingEventsData.get(position).getAwayTeam());
        } else {
            holder.scoreDetailsView.setVisibility(View.GONE);
        }

        String dateTime = "";
        String time = upcomingEventsData.get(position).getEventTime();
        String format = "", pattern = "";
        if (!time.equals("null") && !time.equals(null) && !time.isEmpty()) {
            dateTime = upcomingEventsData.get(position).getEventDate() + " " + upcomingEventsData.get(position).getEventTime();
            format = "yyyy-MM-dd HH:mm:ss";
            pattern = "dd MMMM, yyyy @ hh:mm a";
        } else {
            dateTime = upcomingEventsData.get(position).getEventDate();
            format = "yyyy-MM-dd";
            pattern = "dd MMMM, yyyy";
        }
        try {
            String dateTimeStr = dateTime;
            Date date = new SimpleDateFormat(format).parse(dateTimeStr);
            String formatedDate = new SimpleDateFormat(pattern).format(date);
            holder.eventDateView.setText(formatedDate);
        } catch (Exception e) {
            holder.eventDateView.setVisibility(View.GONE);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ...
            }
        });
    }

    /**
     * number of items in the array list
     *
     * @return number of items in the events list
     */
    @Override
    public int getItemCount() {
        return upcomingEventsData.size();
    }

    /**
     * search filter for events
     *
     * @return reference to filter for events
     */
    @Override
    public Filter getFilter() {
        return filter;
    }

    /**
     * filters event data on the basis of search query
     */
    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<UpcomingEvent> filteredUpcomingEventsData = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredUpcomingEventsData.addAll(upcomingEventsDataComplete);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (UpcomingEvent event : upcomingEventsDataComplete) {
                    if (event.getEventName().toLowerCase().contains(filterPattern)) {
                        filteredUpcomingEventsData.add(event);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredUpcomingEventsData;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            upcomingEventsData.clear();
            upcomingEventsData.addAll((ArrayList<UpcomingEvent>) results.values);
            notifyDataSetChanged();
        }
    };
}