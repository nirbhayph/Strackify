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

import com.sportstracking.strackify.model.PastEvent;
import com.sportstracking.strackify.R;
import com.sportstracking.strackify.utility.VolleyService;

import java.util.ArrayList;

public class PastEventsAdapter extends RecyclerView.Adapter<PastEventsAdapter.MyViewHolder> implements Filterable {
    private ArrayList<PastEvent> pastEventsData;
    private ArrayList<PastEvent> pastEventsDataComplete;
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

    public PastEventsAdapter(Activity activity, ArrayList<PastEvent> pastEventsData, VolleyService volleyService) {
        this.activity = activity;
        this.pastEventsData = pastEventsData;
        this.pastEventsDataComplete = new ArrayList<>(pastEventsData);
        this.volleyService = volleyService;
    }

    @Override
    public PastEventsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_view, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.eventNameView.setText(pastEventsData.get(position).getEventName());

        if(!pastEventsData.get(position).getEventThumbnail().isEmpty() && !pastEventsData.get(position).getEventThumbnail().equals("null")){
            volleyService.makeImageRequest(pastEventsData.get(position).getEventThumbnail(), holder.eventThumbView);
        }

        holder.eventLeagueView.setText(pastEventsData.get(position).getEventLeague());
        holder.eventDateView.setText(pastEventsData.get(position).getEventDate());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ...
            }
        });
    }

    @Override
    public int getItemCount() {
        return pastEventsData.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<PastEvent> filteredPastEventsData = new ArrayList<>();
            if(constraint == null || constraint.length()==0){
                filteredPastEventsData.addAll(pastEventsDataComplete);
            }
            else{
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(PastEvent event : pastEventsDataComplete){
                    if(event.getEventName().toLowerCase().contains(filterPattern)){
                        filteredPastEventsData.add(event);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredPastEventsData;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            pastEventsData.clear();
            pastEventsData.addAll((ArrayList<PastEvent>) results.values);
            notifyDataSetChanged();
        }
    };
}