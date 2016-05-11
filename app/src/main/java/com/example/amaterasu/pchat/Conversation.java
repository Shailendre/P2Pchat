package com.example.amaterasu.pchat;

import android.graphics.Bitmap;



/**
 * Created by dell on 2/3/16.
 */
public class Conversation {

    int id;
    String name;
    Bitmap thumb;
    String number;
    boolean groupflag = false;


    public  void setId(int id){this.id=id;}

    public int getId(){return id;}

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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setGroupflag(boolean groupflag){
        this.groupflag = true;
    }

    public boolean getGroupflag(){ return groupflag;}
}
