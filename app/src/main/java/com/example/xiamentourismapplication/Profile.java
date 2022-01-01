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
import android.widget.LinearLayout;
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

        LinearLayout signOut = view.findViewById(R.id.btn_signOut);
        LinearLayout wishlist = view.findViewById(R.id.btn_wishlist);
        LinearLayout updateEmail = view.findViewById(R.id.btn_update);
        LinearLayout changePassword = view.findViewById(R.id.btn_change);

        SessionManager manager = new SessionManager(getContext());
        HashMap<String, String> userData = manager.getUserDetailFromSession();

        String userId = userData.get(manager.KEY_USERID);
        String username = userData.get(manager.KEY_USERNAME);
        String email = userData.get(manager.KEY_EMAIL);

//        profile.setText("Username: " + username);


        updateEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.content, UpdateEmail.newInstance(userId, email))
                        .addToBackStack(null)
                        .commit();
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.content, ChangePassword.newInstance(userId, email))
                        .addToBackStack(null)
                        .commit();
            }
        });

        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.content, Wishlist.newInstance(Integer.parseInt(userId)))
                        .addToBackStack(null)
                        .commit();
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manager.logoutUser();
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });


    }
}