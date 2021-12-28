package com.example.xiamentourismapplication;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Home extends Fragment {

    DestinationDatabaseHelper destinationDatabaseHelper;
    ArrayList<Destination> destinations;
    RecyclerView recyclerView;
    DestinationAdapter adapter;
    Random random;

    public Home() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.randomRecyclerView);

        destinationDatabaseHelper = new DestinationDatabaseHelper(getContext());
        destinations = new ArrayList<>();
        random = new Random();

        int random_num = 0;
        ArrayList<Integer> number_list = new ArrayList<>();

        int limit = destinationDatabaseHelper.getNumberOfRecords();
        final GlobalClass globalVariable = (GlobalClass) getActivity().getApplicationContext();

        if(globalVariable.getNumber_list().isEmpty()){
            for (int i = 0;  i < 5; i++){
                do{
                    random_num = random.nextInt(limit) + 1;
                }while(number_list.contains(random_num));
                number_list.add(random_num);
            }
        }
        if(!number_list.isEmpty()){
            globalVariable.setNumber_list(number_list);
        }

        getRecommendedDestination(globalVariable.getNumber_list());
        recyclerView.setHasFixedSize(true);
        adapter = new DestinationAdapter(getContext(), destinations);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
    }

    public void getRecommendedDestination(ArrayList<Integer> number_list){
        Cursor cursor = destinationDatabaseHelper.getRecommendDestination(number_list);
        if(cursor.getCount() != 0){
            while (cursor.moveToNext()){
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String address = cursor.getString(2);
                String description = cursor.getString(3);
                String operation_hours = cursor.getString(4);
                String phone = cursor.getString(5);
                String website = cursor.getString(6);
                double latitude = cursor.getDouble(7);
                double longitude = cursor.getDouble(8);
                byte[] image = cursor.getBlob(9);
                int type_id = cursor.getInt(10);

                destinations.add(new Destination(id, name, address, description, operation_hours, phone, website, latitude, longitude, image, type_id));
            }
        }
    }
}