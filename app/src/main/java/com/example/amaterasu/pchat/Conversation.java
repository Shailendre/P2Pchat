package com.example.amaterasu.pchat;

import android.graphics.Bitmap;

import java.util.Date;

/**
 * Created by dell on 2/3/16.
 */
public class Conversation {

    String name;
    Bitmap thumb;
    String date;

    public Bitmap getThumb() {
        return thumb;
    }

    public void setThumb(Bitmap thumb) {
        this.thumb = thumb;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
