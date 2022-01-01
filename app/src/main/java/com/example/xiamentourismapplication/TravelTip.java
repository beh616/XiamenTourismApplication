package com.example.xiamentourismapplication;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class TravelTip extends Fragment {

    TipDatabaseHelper tipDatabaseHelper;
    RecyclerView recyclerView;
    TipAdapter adapter;
    ArrayList<Tip> tips;

    public TravelTip() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_travel_tip, container, false);
        tipDatabaseHelper = new TipDatabaseHelper(getContext());
        tips = new ArrayList<>();
        recyclerView = view.findViewById(R.id.tipRecyclerView);
        getAllTips();
        adapter = new TipAdapter(getContext(), getActivity(), tips);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void getAllTips() {
        Cursor cursor = tipDatabaseHelper.getAllTips();
        if(cursor.getCount() != 0){
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String url = cursor.getString(2);
                String type = cursor.getString(3);
                byte[] image = cursor.getBlob(4);
                String author = cursor.getString(5);

                tips.add(new Tip(id, title, url, type, image, author));
            }
        }
    }
}