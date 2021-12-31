package com.example.xiamentourismapplication;

import static com.gun0912.tedpermission.TedPermissionUtil.isGranted;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.admin.FactoryResetProtectionPolicy;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.ByteArrayInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class DestinationAdapter extends RecyclerView.Adapter<DestinationAdapter.DestinationViewHolder> {

    Context context;
    Activity activity;
    ArrayList<Destination> destinations;
    GlobalClass globalVariable;



    public DestinationAdapter(Context context, Activity activity, ArrayList<Destination> destinations){
        this.context = context;
        this.activity = activity;
        this.destinations = destinations;
    }

    @NonNull
    @Override
    public DestinationAdapter.DestinationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.destination_card_layout, parent, false);
        globalVariable = (GlobalClass) activity.getApplicationContext();

        return new DestinationViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull DestinationAdapter.DestinationViewHolder holder, int position) {

        Destination destination = destinations.get(position);
        byte[] image = destination.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);


        final DecimalFormat df = new DecimalFormat("0.00");
        float[] results = new float[1];
        String distance;


        Location.distanceBetween(globalVariable.getLatitude(), globalVariable.longitude, destination.getLatitude(), destination.getLongitude(), results);
        double distanceInKm = results[0]/1000;
        distance = df.format(distanceInKm) + " km";



        holder.name.setText(destination.getName());
        holder.image.setImageBitmap(bitmap);
        holder.distance.setText(distance);

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment allDestination = activity.getSupportFragmentManager().findFragmentByTag("AllDestination");
                if (allDestination != null && allDestination.isVisible()){
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.content, DestinationDescription.newInstance(destination), "DestinationDescription").addToBackStack("AllDestination").commit();
                }
                else{
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.content, DestinationDescription.newInstance(destination), "DestinationDescription").addToBackStack(null).commit();
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
