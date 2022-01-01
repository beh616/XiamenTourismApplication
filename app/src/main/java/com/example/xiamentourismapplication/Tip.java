package com.example.xiamentourismapplication;

public class Tip {
    private int id;
    private String title, url, type;
    private byte[] image;
    private String author;

    public Tip(int id, String title, String url, String type, byte[] image, String author) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.type = type;
        this.image = image;
        this.author = author;
    }

    public Tip() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
