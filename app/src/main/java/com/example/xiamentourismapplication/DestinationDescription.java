package com.example.xiamentourismapplication;

import static com.gun0912.tedpermission.TedPermissionUtil.isGranted;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.HashMap;

public class DestinationDescription extends Fragment implements OnMapReadyCallback, BottomSheetDialog.FragmentListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "id";
    private static final String ARG_PARAM2 = "name";
    private static final String ARG_PARAM3 = "address";
    private static final String ARG_PARAM4 = "description";
    private static final String ARG_PARAM5 = "operation_hours";
    private static final String ARG_PARAM6 = "phone";
    private static final String ARG_PARAM7 = "website";
    private static final String ARG_PARAM8 = "latitude";
    private static final String ARG_PARAM9 = "longitude";
    private static final String ARG_PARAM10 = "image";
    private static final String ARG_PARAM11 = "type_id";

    // parameters declaration
    private int id;
    private String name;
    private String address;
    private String description;
    private String operation_hours;
    private String phone;
    private String website;
    private double latitude;
    private double longitude;
    private byte[] image;
    private int type_id;

    MapView mapView;
    private TextView des_name, des_description, des_address, des_phone, des_hours, des_website;
    private ImageView des_image, btn_back;
    private Button viewMap, direction;
    private Bitmap bitmap;

    private boolean isAddedToWishList = false;
    WishlistDatabaseHelper databaseHelper;
    private String userId;
    ImageView favourite;
    BottomSheetDialog bottomSheetDialogFragment;

//    Variable used to retrieve user location
    private FusedLocationProviderClient fusedLocationProviderClient;
    private double userLatitude, userLongitude;

    public DestinationDescription() {
    }

    public static DestinationDescription newInstance(Destination destination) {
        DestinationDescription fragment = new DestinationDescription();
        Bundle bundle = new Bundle();
        bundle.putInt("id", destination.getDestination_id());
        bundle.putString("name", destination.getName());
        bundle.putString("address", destination.getAddress());
        bundle.putString("description", destination.getDescription());
        bundle.putString("operation_hours", destination.getOperation_hours());
        bundle.putString("phone", destination.getPhone());
        bundle.putString("website", destination.getWebsite());
        bundle.putDouble("latitude", destination.getLatitude());
        bundle.putDouble("longitude", destination.getLongitude());
        bundle.putByteArray("image", destination.getImage());
        bundle.putInt("type_id", destination.getType_id());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            id = getArguments().getInt(ARG_PARAM1);
            name = getArguments().getString(ARG_PARAM2);
            address = getArguments().getString(ARG_PARAM3);
            description = getArguments().getString(ARG_PARAM4);
            operation_hours = getArguments().getString(ARG_PARAM5);
            phone = getArguments().getString(ARG_PARAM6);
            website = getArguments().getString(ARG_PARAM7);
            latitude = getArguments().getDouble(ARG_PARAM8);
            longitude = getArguments().getDouble(ARG_PARAM9);
            image = getArguments().getByteArray(ARG_PARAM10);
            type_id = getArguments().getInt(ARG_PARAM11);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_destination_description, container, false);
        mapView = (MapView) view.findViewById(R.id.des_location);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        SessionManager manager = new SessionManager(getContext());

        HashMap<String, String> userData = manager.getUserDetailFromSession();
        userId = userData.get(manager.KEY_USERID);

        DestinationDatabaseHelper helper = new DestinationDatabaseHelper(getContext());
        databaseHelper = new WishlistDatabaseHelper(getContext());
        isAddedToWishList = helper.checkFavouriteList(Integer.parseInt(userId), id);
        bottomSheetDialogFragment = BottomSheetDialog.newInstance(this);
        // Inflate the layout for this fragment
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        des_image = view.findViewById(R.id.des_image);
        des_name = view.findViewById(R.id.des_name);
        des_description = view.findViewById(R.id.des_description);
        des_phone = view.findViewById(R.id.des_phone);
        des_hours = view.findViewById(R.id.des_hours);
        des_website = view.findViewById(R.id.des_website);
        des_address = view.findViewById(R.id.des_address);
        viewMap = view.findViewById(R.id.btn_viewMap);
        direction = view.findViewById(R.id.btn_direction);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        btn_back = view.findViewById(R.id.btn_back);
        favourite = view.findViewById(R.id.iv_favourite);

        bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        des_image.setImageBitmap(bitmap);

        if(isAddedToWishList){
            favourite.setImageResource(R.drawable.ic_baseline_favorite_24);
        }
        else{
            favourite.setImageResource(R.drawable.ic_baseline_favorite_border_24);
        }

        des_name.setText(name);
        des_description.setText(description);
        des_hours.setText(operation_hours);
        if (phone != null)
        {
            des_phone.setText(phone);
        }
        else{
            des_phone.setText("No phone number listed");
        }
        if(website != null){
            des_website.setText(website);
        }
        else{
            des_website.setText("No website listed");
        }
        des_address.setText(address);

        viewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Implicit intent to google map
                String geo = "geo:" + String.valueOf(latitude) + "," + String.valueOf(longitude);
                Uri uri = Uri.parse(geo);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
            }
        });

        direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isGranted(Manifest.permission.ACCESS_FINE_LOCATION)){
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://maps.google.com/maps?saddr=" + userLatitude + "," + userLongitude + "&daddr=" + latitude + "," + longitude));
                    intent.setPackage("com.google.android.apps.maps");
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getContext(), "Permission is not granted", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0)
                {
                    getActivity().getSupportFragmentManager().popBackStack();

                }
                else {
                    Fragment backFragment = new Home();
                    getParentFragmentManager().beginTransaction().replace(R.id.content, backFragment).commit();
                }
            }
        });

        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isAddedToWishList){

                    bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), bottomSheetDialogFragment.getTag());

                }
                else{
                    boolean result = databaseHelper.removeList(id, Integer.parseInt(userId));  //remove record from favourite table
                    if(result){
                        isAddedToWishList = false;
                        favourite.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                        Toast.makeText(getContext(), "Removed from wishlist", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
//        set google map style
        UiSettings uiSettings = googleMap.getUiSettings();

        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.style_json));

        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
        if(isGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            getLastLocation();
        }
        googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(name).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude), 15));
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();

    }
//    Used fusedLocationProviderClient object to retrieve user current location
    @SuppressLint("MissingPermission")
    private void getLastLocation(){

        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(@NonNull Location location) {
                if (location != null){
                    userLatitude = location.getLatitude();
                    userLongitude = location.getLongitude();
                }

            }
        });
    }

    @Override
    public void getView(View view) {
        ((RoundedImageView)view.findViewById(R.id.destinationImage)).setImageBitmap(bitmap);
        ((TextView)view.findViewById(R.id.tv_destinationName)).setText(name);
        ((TextView)view.findViewById(R.id.tv_destinationAddress)).setText(address);
        ((EditText)view.findViewById(R.id.et_comment)).setText("");
        ((Button)view.findViewById(R.id.btn_add)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                String txt_comment = ((EditText)view.findViewById(R.id.et_comment)).getText().toString();
                if(txt_comment.equals("")){
                    Toast.makeText(getContext(), "Enter your comment", Toast.LENGTH_SHORT);
                }
                else{
                    boolean result = databaseHelper.insertWishlist(id, Integer.parseInt(userId), txt_comment);
                    if(result) {
                        Toast.makeText(getActivity(), "Added to wishlist", Toast.LENGTH_SHORT).show();
                        isAddedToWishList = true;
                        favourite.setImageResource(R.drawable.ic_baseline_favorite_24);
                        bottomSheetDialogFragment.dismiss();
                    }
                }
            }


            });
    }
}