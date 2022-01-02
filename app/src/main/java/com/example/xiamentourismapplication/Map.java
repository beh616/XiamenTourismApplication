package com.example.xiamentourismapplication;

import static com.gun0912.tedpermission.TedPermissionUtil.isGranted;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.IntentSender;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.util.List;

public class Map extends Fragment implements OnMapReadyCallback, View.OnClickListener {

    private ImageButton attraction, food, hotel, hospital, police, bus, railway;

    private double latitude, longitude;
    private GoogleMap map;
    private UiSettings uiSettings;
    private PermissionListener permissionListener;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            if(locationResult == null){
                return;
            }
            for(Location location: locationResult.getLocations()){
                if(userMarker != null){
                    userMarker.remove();
                }
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                userMarker = map.addMarker(new MarkerOptions().position(userLocation).title("Your current location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)).zIndex(1.0f));

            }
        }
    };

//    default location if permission is not granted
    LatLng Xiamen = new LatLng(24.480949192070508, 118.09009362798139);
//    private PlacesClient placesClient;
//    private GeoDataClient mGeoDataClient;
//    private PlaceDetectionClient mPlaceDetectionClient;
    private Marker userMarker;
    public static final int Request_User_Location_Code = 99;

    private int ProximityRadius = 100000; //100km = 100,000 m


    public Map() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        attraction = view.findViewById(R.id.btn_attraction);
        food = view.findViewById(R.id.btn_food);
        hotel = view.findViewById(R.id.btn_hotel);
        hospital = view.findViewById(R.id.btn_hospital);
        police = view.findViewById(R.id.btn_policeStation);
        bus = view.findViewById(R.id.btn_bus);
        railway = view.findViewById(R.id.btn_railway);

        attraction.setOnClickListener(this);
        food.setOnClickListener(this);
        hotel.setOnClickListener(this);
        hospital.setOnClickListener(this);
        police.setOnClickListener(this);
        bus.setOnClickListener(this);
        railway.setOnClickListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map_xiamen);
        mapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(3000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStop() {
        super.onStop();
        stopLocationUpdates();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(isGranted(Manifest.permission.ACCESS_FINE_LOCATION)){
            checkSettingsAndStartLocationUpdates();
        }
    }

    @SuppressLint("MissingPermission")

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        uiSettings = googleMap.getUiSettings();

        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.style_json));

        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        if(isGranted(Manifest.permission.ACCESS_FINE_LOCATION)){
            map.setMyLocationEnabled(true);
            getLastLocation();
        }
        else{
            askPermission();
//            default location marker in location(24.480949192070508, 118.09009362798139)
            map.addMarker(new MarkerOptions().position(Xiamen).title("Xiamen"));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(Xiamen, 15));
        }
    }


    @SuppressLint("MissingPermission")
    private void getLastLocation(){
        fusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(@NonNull Location location) {
                if (location != null){
                    LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    map.addMarker(new MarkerOptions().position(userLocation).title("Initial Point").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
                }
                else{
                    //            default location marker in location(24.480949192070508, 118.09009362798139)
                    map.addMarker(new MarkerOptions().position(Xiamen).title("Xiamen"));
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(Xiamen, 15));
                }
            }
        });
    }

    private void checkSettingsAndStartLocationUpdates(){
        LocationSettingsRequest request = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest).build();
        SettingsClient client = LocationServices.getSettingsClient(getActivity());
        Task<LocationSettingsResponse> locationSettingsResponseTask = client.checkLocationSettings(request);
        locationSettingsResponseTask.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(@NonNull LocationSettingsResponse locationSettingsResponse) {
                startLocationUpdates();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException){
                    ResolvableApiException exception = (ResolvableApiException) e;
                    try {
                        exception.startResolutionForResult(getActivity(), 1001);
                    } catch (IntentSender.SendIntentException sendIntentException) {
                        sendIntentException.printStackTrace();
                    }
                }
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates(){
        fusedLocationClient.requestLocationUpdates(locationRequest,locationCallback, Looper.getMainLooper());
    }

    private void stopLocationUpdates(){
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    private void askPermission() {
        permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(getActivity(), "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(getActivity(), "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }


        };
        TedPermission.create()
                .setPermissionListener(permissionListener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .check();
    }

    @Override
    public void onClick(View view) {
        String[] places = {"Attraction", "Restaurant","Hotel","Hospital", "Police Station", "Bus Station", "Railway Station"};
        String[] types = {"attraction", "restaurant","hotel","hospital", "police", "bus_station", "transit_station"};
        String type = null;
        Object transferData[] = new Object[2];
        String place = null;
        GetNearbyPlaces getNearbyPlaces = new GetNearbyPlaces();

        switch(view.getId()){
            case R.id.btn_attraction:
                map.clear();
                place = places[0];
                type = types[0];
                break;

            case R.id.btn_food:
                map.clear();
                place = places[1];
                type = types[1];
                break;

            case R.id.btn_hotel:
                map.clear();
                place = places[2];
                type = types[2];
                break;

            case R.id.btn_hospital:
                map.clear();
                place = places[3];
                type = types[3];
                break;
            case R.id.btn_policeStation:
                map.clear();
                place = places[4];
                type = types[4];
                break;
            case R.id.btn_bus:
                map.clear();
                place = places[5];
                type = types[5];
                break;
            case R.id.btn_railway:
                map.clear();
                place = places[6];
                type = types[6];
                break;

        }
        String url = getUrl(latitude, longitude, type);
        transferData[0] = map;
        transferData[1] = url;

        getNearbyPlaces.execute(transferData);
        Toast.makeText(getContext(), "Searching for Nearby " + place, Toast.LENGTH_SHORT).show();
        Toast.makeText(getContext(), "Displaying Nearby " + place, Toast.LENGTH_SHORT).show();
    }

    private String getUrl(double latitude, double longitude, String placeType){
        StringBuilder googleUrl;
        if(placeType.equals("attraction") || placeType.equals("hotel")){
            googleUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            googleUrl.append("location="+ latitude + "," + longitude);
            googleUrl.append("&radius=" + ProximityRadius);
            googleUrl.append("&keyword=" + placeType);
            googleUrl.append("&key=AIzaSyBOYe9FGKyu9xr_6lNPdyyxIdtZ2HwtE0M");
        }
        else{
            googleUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            googleUrl.append("location="+ latitude + "," + longitude);
            googleUrl.append("&radius=" + ProximityRadius);
            googleUrl.append("&types=" + placeType);
            googleUrl.append("&key=AIzaSyBOYe9FGKyu9xr_6lNPdyyxIdtZ2HwtE0M");
            System.out.println(googleUrl);
        }
        return googleUrl.toString();
    }
}