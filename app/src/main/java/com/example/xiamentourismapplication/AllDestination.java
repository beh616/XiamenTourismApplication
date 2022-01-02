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

import java.util.ArrayList;
import java.util.Locale;


public class AllDestination extends Fragment {

    public static final String ARG = "query";

    String query;
    Bundle bundle;

    DestinationDatabaseHelper destinationDatabaseHelper;
    ArrayList<Destination> destinations;
    ArrayList<Destination> filteredList;
    RecyclerView recyclerView;
    DestinationAdapter adapter;

    public AllDestination() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bundle = this.getArguments();
        if(bundle != null){
            query = bundle.getString(ARG);
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_destination, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        recyclerView = view.findViewById(R.id.AllRecyclerView);
        destinationDatabaseHelper = new DestinationDatabaseHelper(getContext());
        destinations = new ArrayList<>();
        filteredList = new ArrayList<>();

        getAllDestination();
        recyclerView.setHasFixedSize(true);
        TextView title = view.findViewById(R.id.tv_destination);
        TextView result = view.findViewById(R.id.tv_search_result);
        if(bundle != null){
            title.setText("Search Result");
            filter(query);
            adapter = new DestinationAdapter(getContext(), getActivity(), filteredList);
            if(filteredList.isEmpty()){
                result.setText("No destination found!");
            }
        }
        else{
            adapter = new DestinationAdapter(getContext(), getActivity(), destinations);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter);



    }

    private void filter(String query) {
        filteredList.clear();
        for(Destination destination:destinations){
            if(destination.getName().toLowerCase().contains(query.toLowerCase())){
                filteredList.add(destination);
            }
        }
    }

    private void getAllDestination() {
        Cursor cursor = destinationDatabaseHelper.getAllDestination();
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