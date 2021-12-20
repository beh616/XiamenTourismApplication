package com.example.xiamentourismapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class NavigationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_nav);
        bottomNavigation.setOnItemSelectedListener(navListener);

        Fragment defaultFragment = new Home();
        getSupportFragmentManager().beginTransaction().replace(R.id.content, defaultFragment).commit();

    }

    private final NavigationBarView.OnItemSelectedListener navListener = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.menu_home:
                    selectedFragment = new Home();
                    break;
                case R.id.menu_info:
                    selectedFragment = new TravelInfo();
                    break;
                case R.id.menu_map:
                    selectedFragment = new Map();
                    break;
                case R.id.menu_article:
                    selectedFragment = new Blog();
                    break;
                case R.id.menu_profile:
                    selectedFragment = new Profile();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.content, selectedFragment).commit();
            return true;
        }
    };
}