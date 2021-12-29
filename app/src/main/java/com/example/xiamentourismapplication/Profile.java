package com.example.xiamentourismapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

public class Profile extends Fragment {


    public Profile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView profile = view.findViewById(R.id.tv_profile);
        Button signOut = view.findViewById(R.id.btn_signOut);

        SessionManager manager = new SessionManager(getContext());
        HashMap<String, String> userData = manager.getUserDetailFromSession();

        String username = userData.get(manager.KEY_USERNAME);
        String email = userData.get(manager.KEY_EMAIL);

        profile.setText("Username: " + username);

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manager.logoutUser();
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });
    }
}