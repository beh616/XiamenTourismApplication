package com.example.xiamentourismapplication;

import android.app.admin.FactoryResetProtectionPolicy;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayInputStream;
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
        byte[] image = destination.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);

        holder.name.setText(destination.getName());
        holder.image.setImageBitmap(bitmap);

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                int id = destination.getDestination_id();
                String name = destination.getName();
                String address = destination.getAddress();
                String description = destination.getDescription();
                String operation_hours = destination.getOperation_hours();
                String phone = destination.getPhone();
                String website = destination.getWebsite();
                double latitude = destination.getLatitude();
                double longitude = destination.getLongitude();
                byte[] image = destination.getImage();
                int type_id = destination.getType_id();

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment allDestination = activity.getSupportFragmentManager().findFragmentByTag("AllDestination");
                if (allDestination != null && allDestination.isVisible()){
                    Fragment nextFragment = new DestinationDescription();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.content, DestinationDescription.newInstance(id, name, address, description, operation_hours, phone, website, latitude, longitude, image, type_id), "DestinationDescription").addToBackStack("AllDestination").commit();
                }
                else{
                    Fragment nextFragment = new DestinationDescription();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.content, DestinationDescription.newInstance(id, name, address, description, operation_hours, phone, website, latitude, longitude, image, type_id), "DestinationDescription").addToBackStack(null).commit();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return destinations.size();
    }

    public class DestinationViewHolder extends RecyclerView.ViewHolder{
        ConstraintLayout card;
        TextView name, distance;
        ImageView image;
        public DestinationViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_destName);
            distance = itemView.findViewById(R.id.tv_distance);
            image = itemView.findViewById(R.id.dest_image);
            card = itemView.findViewById(R.id.dest_card);

        }
    }
}
