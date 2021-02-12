package com.example.linuxgames.igdb;

public class jsonData {
    String type;
    String data;
    String id;
    String name;
    String url;
    String cloudinary;
    String release_year;
    String released;

    public jsonData(String type, String data, String id, String name, String url, String cloudinary, String release_year, String released) {
        this.type = type;
        this.data = data;
        this.id = id;
        this.name = name;
        this.url = url;
        this.cloudinary = cloudinary;
        this.release_year = release_year;
        this.released = released;
    }

    public jsonData() {

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCloudinary() {
        return cloudinary;
    }

    public void setCloudinary(String cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String getRelease_year() {
        return release_year;
    }

    public void setRelease_year(String release_year) {
        this.release_year = release_year;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }
}
