package com.example.maouusama.midtermexam.Model;

/**
 * Created by Maouusama on 2/13/2017.
 */

public class Image {

    String imgUrl;
    String size;

    public Image(String imgUrl, String size) {
        this.imgUrl = imgUrl;
        this.size = size;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

}
