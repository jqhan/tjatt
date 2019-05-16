package com.luttu.tjatt.BusinessLogic;

public class User {

    private int mID;
    private String mUserName;

    public User(int mID, String mUserName) {
        this.mID = mID;
        this.mUserName = mUserName;
    }

    public int getID() {
        return mID;
    }

    public void setID(int mID) {
        this.mID = mID;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String mUserName) {
        this.mUserName = mUserName;
    }
    
}
