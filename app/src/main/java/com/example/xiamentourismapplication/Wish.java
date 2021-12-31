package com.example.xiamentourismapplication;

public class Wish {
    private int wish_id, destination_id, user_id;
    private String comment;

    public Wish(int wish_id, int destination_id, int user_id, String comment) {
        this.wish_id = wish_id;
        this.destination_id = destination_id;
        this.user_id = user_id;
        this.comment = comment;
    }

    public Wish() {
    }

    public int getWish_id() {
        return wish_id;
    }

    public void setWish_id(int wish_id) {
        this.wish_id = wish_id;
    }

    public int getDestination_id() {
        return destination_id;
    }

    public void setDestination_id(int destination_id) {
        this.destination_id = destination_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
