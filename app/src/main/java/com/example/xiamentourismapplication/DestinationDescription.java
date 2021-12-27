package com.example.xiamentourismapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class DestinationDescription extends Fragment {
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

    private TextView des_name, des_description, des_address, des_phone, des_hours, des_website;
    private ImageView des_image;

    public DestinationDescription() {
    }

    public static DestinationDescription newInstance(int id, String name, String address, String description, String operation_hours, String phone, String website, double latitude, double longitude, byte[] image, int type_id) {
        DestinationDescription fragment = new DestinationDescription();
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        bundle.putString("name", name);
        bundle.putString("address", address);
        bundle.putString("description", description);
        bundle.putString("operation_hours", operation_hours);
        bundle.putString("phone", phone);
        bundle.putString("website", website);
        bundle.putDouble("latitude", latitude);
        bundle.putDouble("longitude", longitude);
        bundle.putByteArray("image", image);
        bundle.putInt("type_id", type_id);
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_destination_description, container, false);
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

        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        des_image.setImageBitmap(bitmap);

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



    }
}