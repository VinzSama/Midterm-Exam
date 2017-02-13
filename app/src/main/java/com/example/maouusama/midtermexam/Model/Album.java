package com.example.maouusama.midtermexam.Model;

import java.util.ArrayList;

/**
 * Created by Maouusama on 2/13/2017.
 */

public class Album {

    String name;
    String artist;
    String url;
    ArrayList<Image> imageList;
    int streamable;
    String mbId;

    public Album(String name, String artist, String url, ArrayList<Image> imageList, int streamable, String mbId) {
        this.name = name;
        this.artist = artist;
        this.url = url;
        this.imageList = imageList;
        this.streamable = streamable;
        this.mbId = mbId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<Image> getImageList() {
        return imageList;
    }

    public void setImageList(ArrayList<Image> imageList) {
        this.imageList = imageList;
    }

    public int getStreamable() {
        return streamable;
    }

    public void setStreamable(int streamable) {
        this.streamable = streamable;
    }

    public String getMbId() {
        return mbId;
    }

    public void setMbId(String mbId) {
        this.mbId = mbId;
    }
}
