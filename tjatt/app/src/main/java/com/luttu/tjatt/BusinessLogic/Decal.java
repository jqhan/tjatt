package com.luttu.tjatt.BusinessLogic;

import android.graphics.Bitmap;

import java.util.UUID;

public class Decal {
    private int mID;
    private String mFormat;
    private int mAddedBy;
    private Bitmap mImgBinary;

    public Decal () { }
    public Decal(int id){
        mID = id;
        mFormat = "bmp";
        mAddedBy = 1;
        mImgBinary = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
    }

    public int getID() {
        return mID;
    }

    public void setId(int id) {
        mID = id;
    }

    public String getFormat() {
        return mFormat;
    }

    public void setFormat(String format) {
        this.mFormat = format;
    }

    public Bitmap getImgBinary() {
        return mImgBinary;
    }

    public void setImgBinary(Bitmap imgBinary) {
        mImgBinary = imgBinary;
    }

    public int getAddedBy() {
        return mAddedBy;
    }

    public void setAddedBy(int addedBy) {
        this.mAddedBy = addedBy;
    }
}
