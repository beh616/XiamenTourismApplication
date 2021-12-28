package com.example.xiamentourismapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.util.List;

public class NavigationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_nav);
        bottomNavigation.setOnItemSelectedListener(navListener);

        Fragment defaultFragment = new Home();
        getSupportFragmentManager().beginTransaction().replace(R.id.content, defaultFragment).commit();

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(NavigationActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(NavigationActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }


        };
        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .check();

    }

    private final NavigationBarView.OnItemSelectedListener navListener = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            String TAG = "";
            switch (item.getItemId()) {
                case R.id.menu_home:
                    selectedFragment = new Home();
                    TAG = "Home";
                    break;
                case R.id.menu_info:
                    selectedFragment = new AllDestination();
                    TAG = "AllDestination";
                    break;
                case R.id.menu_map:
                    selectedFragment = new Map();
                    TAG = "Map";
                    break;
                case R.id.menu_article:
                    selectedFragment = new Blog();

                    break;
                case R.id.menu_profile:
                    selectedFragment = new Profile();
                    TAG = "Profile";
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.content, selectedFragment, TAG).commit();
            return true;
        }
    };
}