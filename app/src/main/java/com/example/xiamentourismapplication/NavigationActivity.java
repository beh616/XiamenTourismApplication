package com.example.xiamentourismapplication;

import static com.gun0912.tedpermission.TedPermissionUtil.isGranted;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.util.List;

public class NavigationActivity extends AppCompatActivity {

    GlobalClass globalVariable;
    //    Variable used to retrieve user location
    private FusedLocationProviderClient fusedLocationProviderClient;
    private double userLatitude = 0, userLongitude = 0;

    PermissionListener permissionListener;

    @Override
    protected void onStart() {
        super.onStart();
        globalVariable = (GlobalClass) getApplicationContext();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(NavigationActivity.this);
        if (isGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            if (userLatitude == 0 && userLongitude == 0) {
                getLastLocation();
            }
        }
        else{
            askForPermission();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_nav);
        bottomNavigation.setOnItemSelectedListener(navListener);

        Fragment defaultFragment = new Home();
        getSupportFragmentManager().beginTransaction().replace(R.id.content, defaultFragment).commit();

        permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(NavigationActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(NavigationActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }


        };


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
                    selectedFragment = new TravelTip();

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

    //    get user current location
    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        fusedLocationProviderClient.getLocationAvailability().addOnSuccessListener(new OnSuccessListener<LocationAvailability>() {
            @Override
            public void onSuccess(@NonNull LocationAvailability locationAvailability) {
                if(locationAvailability.isLocationAvailable()){
                    Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
                    locationTask.addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            Location location = task.getResult();
                            userLatitude = location.getLatitude();
                            userLongitude = location.getLongitude();
                            globalVariable.setLatitude(userLatitude);
                            globalVariable.setLongitude(userLongitude);
                        }
                    });
                }
            }
        });
    }

    private void askForPermission(){
        TedPermission.create()
                .setPermissionListener(permissionListener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .check();
    }
}