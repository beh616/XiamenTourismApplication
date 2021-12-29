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
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class FilteredDestination extends Fragment {

    private static final String ARG_PARAM1 = "type";

    private String type;

    TextView title;
    Button btn_back;
    DestinationDatabaseHelper destinationDatabaseHelper;
    ArrayList<Destination> destinations;
    RecyclerView recyclerView;
    DestinationAdapter adapter;


    public FilteredDestination() {
        // Required empty public constructor
    }


    public static FilteredDestination newInstance(String type) {
        FilteredDestination fragment = new FilteredDestination();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filtered_destination, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        title = view.findViewById(R.id.tv_title);
        recyclerView = view.findViewById(R.id.FilteredRecyclerView);
        destinations = new ArrayList<>();
        destinationDatabaseHelper = new DestinationDatabaseHelper(getContext());
        String fragmentTitle = capitalizedTitle();
        title.setText(fragmentTitle);

        getFilteredDestination();

        recyclerView.setHasFixedSize(true);
        adapter = new DestinationAdapter(getContext(), getActivity(), destinations);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter);
    }

    private String capitalizedTitle() {
        return type.substring(0, 1).toUpperCase() + type.substring(1);
    }

    private void getFilteredDestination() {
        Cursor cursor = destinationDatabaseHelper.getFilteredDestination(type);
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