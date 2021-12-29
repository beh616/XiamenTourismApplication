package com.example.xiamentourismapplication;

import android.app.Application;

import java.util.ArrayList;

public class GlobalClass extends Application {
    public ArrayList<Integer> number_list = new ArrayList<>();
    public double latitude = 0, longitude = 0;

    public GlobalClass() {

    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public ArrayList<Integer> getNumber_list() {
        return number_list;
    }

    public void setNumber_list(ArrayList<Integer> number_list) {
        this.number_list = number_list;
    }
}
