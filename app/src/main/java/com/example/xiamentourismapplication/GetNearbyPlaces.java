package com.example.xiamentourismapplication;

import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class GetNearbyPlaces extends AsyncTask<Object, String, String> {
    private String googlePlaceData, url;
    private GoogleMap map;

    @Override
    protected void onPostExecute(String s) {
        List<HashMap<String, String>> nearbyPlacesList = null;
        JsonDataParser dataParser = new JsonDataParser();
        nearbyPlacesList = dataParser.parse(s);

        DisplayNearbyPlaces(nearbyPlacesList);
    }

    @Override
    protected String doInBackground(Object... objects) {
        map = (GoogleMap) objects[0];
        url = (String) objects[1];

        DownloadUrl downloadUrl = new DownloadUrl();
        try {
            googlePlaceData = downloadUrl.ReadUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return googlePlaceData;
    }

    private void DisplayNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList)
    {
        for (int i = 0; i < nearbyPlacesList.size(); i++)
        {
            MarkerOptions markerOptions = new MarkerOptions();

            HashMap<String, String> nearbyPlace = nearbyPlacesList.get(i);
            String nameOfPlace = nearbyPlace.get("place_name");
            String vicinity = nearbyPlace.get("vicinity");
            double latitude = Double.parseDouble(nearbyPlace.get("lat"));
            double longitude = Double.parseDouble(nearbyPlace.get("lng"));
            String compoundCode = nearbyPlace.get("compound_code");

//            filter nearby place that is not in Xiamen
            if(compoundCode.contains("Xiamen")){
                LatLng place = new LatLng(latitude, longitude);
                markerOptions.position(place);
                markerOptions.title(nameOfPlace + ":" + vicinity);
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                map.addMarker(markerOptions);
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(place, 10));
            }

        }
    }
}
