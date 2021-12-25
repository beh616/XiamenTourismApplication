package com.example.xiamentourismapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DestinationAdapter extends RecyclerView.Adapter<DestinationAdapter.DestinationViewHolder> {

    Context context;
    ArrayList<Destination> destinations;

    public DestinationAdapter(Context context, ArrayList<Destination> destinations){
        this.context = context;
        this.destinations = destinations;
    }

    @NonNull
    @Override
    public DestinationAdapter.DestinationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.destination_card_layout, parent, false);
        return new DestinationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DestinationAdapter.DestinationViewHolder holder, int position) {

        Destination destination = destinations.get(position);

        holder.name.setText(destination.getName());
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class DestinationViewHolder extends RecyclerView.ViewHolder{

        TextView name, distance;
        ImageView image;
        public DestinationViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_destName);
            distance = itemView.findViewById(R.id.tv_distance);
            image = itemView.findViewById(R.id.dest_image);

        }
    }
}
