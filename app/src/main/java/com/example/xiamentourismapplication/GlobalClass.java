package com.example.xiamentourismapplication;

import android.app.Application;

import java.util.ArrayList;

public class GlobalClass extends Application {
    public ArrayList<Integer> number_list = new ArrayList<>();

    public GlobalClass() {

    }

    public ArrayList<Integer> getNumber_list() {
        return number_list;
    }

    public void setNumber_list(ArrayList<Integer> number_list) {
        this.number_list = number_list;
    }
}
