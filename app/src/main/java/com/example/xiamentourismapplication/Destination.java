package com.example.xiamentourismapplication;

import java.sql.Blob;

public class Destination {

    int destination_id;
    String name, address, description, operation_hours;
    int phone;
    String website;
    double latitude, longitude;
    byte[] image;
    int type_id;

    public Destination() {
    }

    public Destination(int destination_id, String name, String address, String description, String operation_hours, int phone, String website, double latitude, double longitude, byte[] image, int type_id) {
        this.destination_id = destination_id;
        this.name = name;
        this.address = address;
        this.description = description;
        this.operation_hours = operation_hours;
        this.phone = phone;
        this.website = website;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image = image;
        this.type_id = type_id;
    }


    public int getDestination_id() {
        return destination_id;
    }

    public void setDestination_id(int destination_id) {
        this.destination_id = destination_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOperation_hours() {
        return operation_hours;
    }

    public void setOperation_hours(String operation_hours) {
        this.operation_hours = operation_hours;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }
}
