package com.example.xiamentourismapplication;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

public class Wishlist extends Fragment {

    private static final String ARG_PARAM1 = "userId";

    private int userId;

    WishlistDatabaseHelper wishlistDatabaseHelper;
    ArrayList<Destination> destinations;
    ArrayList<Wish> wishlist;
    RecyclerView recyclerView;
    WishlistAdapter adapter;
    ImageView btn_back;

    public Wishlist() {
        // Required empty public constructor
    }

    public static Wishlist newInstance(int userId) {
        Wishlist fragment = new Wishlist();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);
        recyclerView = view.findViewById(R.id.wish_recycler_view);
        btn_back = view.findViewById(R.id.btn_back_wishlist);
        wishlistDatabaseHelper = new WishlistDatabaseHelper(getContext());
        wishlist = new ArrayList<>();
        destinations = new ArrayList<>();

        getAllDestinationAdded();
        getAllWish();
        recyclerView.setHasFixedSize(true);
        adapter = new WishlistAdapter(getContext(), getActivity(), wishlist, destinations);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        return view;
    }

    private void getAllWish(){
        Cursor cursor = wishlistDatabaseHelper.getAllWish(userId);
        if(cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                int destination_id = cursor.getInt(1);
                int userId = cursor.getInt(2);
                String comment = cursor.getString(3);

                wishlist.add(new Wish(id, destination_id, userId, comment));
            }
        }
    }

    private void getAllDestinationAdded() {
        Cursor cursor = wishlistDatabaseHelper.getAllDestinationAdded(userId);
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