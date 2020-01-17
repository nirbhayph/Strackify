package com.sportstracking.strackify.adapter;

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

import java.util.ArrayList;

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

        public MyViewHolder(View v) {
            super(v);
            eventNameView = v.findViewById(R.id.eventName);
            eventThumbView = v.findViewById(R.id.eventThumb);
            eventLeagueView = v.findViewById(R.id.eventLeague);
            eventDateView = v.findViewById(R.id.eventDate);
        }
    }

    public UpcomingEventsAdapter(Activity activity, ArrayList<UpcomingEvent> upcomingEventsData, VolleyService volleyService) {
        this.activity = activity;
        this.upcomingEventsData = upcomingEventsData;
        this.upcomingEventsDataComplete = new ArrayList<>(upcomingEventsData);
        this.volleyService = volleyService;
    }

    @Override
    public UpcomingEventsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_view, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.eventNameView.setText(upcomingEventsData.get(position).getEventName());

        if(!upcomingEventsData.get(position).getEventThumbnail().isEmpty() && !upcomingEventsData.get(position).getEventThumbnail().equals("null")){
            volleyService.makeImageRequest(upcomingEventsData.get(position).getEventThumbnail(), holder.eventThumbView);
        }

        holder.eventLeagueView.setText(upcomingEventsData.get(position).getEventLeague());
        holder.eventDateView.setText(upcomingEventsData.get(position).getEventDate());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ...
            }
        });
    }

    @Override
    public int getItemCount() {
        return upcomingEventsData.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<UpcomingEvent> filteredUpcomingEventsData = new ArrayList<>();
            if(constraint == null || constraint.length()==0){
                filteredUpcomingEventsData.addAll(upcomingEventsDataComplete);
            }
            else{
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(UpcomingEvent event : upcomingEventsDataComplete){
                    if(event.getEventName().toLowerCase().contains(filterPattern)){
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