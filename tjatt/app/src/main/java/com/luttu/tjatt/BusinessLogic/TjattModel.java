package com.luttu.tjatt.BusinessLogic;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class TjattModel {

    private List<Room> mRooms = new ArrayList<>();
    private User mUser;
    private String TJATT_MODEL_TAG = "TjattModel";
    private SSLHelper mSSLHelper;

    public void setDecals(List<Decal> mDecals) {
        this.mDecals = mDecals;
    }

    private List<Decal> mDecals = new ArrayList<>();
    private Context mInstance;

    public void setContext(Context instance) {
        mInstance = instance;
    }

    public List<Room> getRooms() {
        return mRooms;
    }

    public Room getRoom(int id) {
        for (Room room : mRooms) {
            if (room.getID() == id) {
                return room;
            }
        }
        Log.e(TJATT_MODEL_TAG, "Room with ID:" + id + " couldn't be found.");
        return null;
    }

    public Decal getDecal(int decalID) {
        for (Decal decal : mDecals) {
            if (decal.getID() == decalID) {
                return decal;
            }
        }
        Log.e(TJATT_MODEL_TAG, "Decal with ID:" + decalID + " couldn't be found.");
        return null;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

    // Ska starta fetch etc
    public void update(){
        /*
        for(int i = 0; i < 20; i++){
            mDecals.add(new Decal(i+1));
        }*/

        //mRooms.add(new Room("room1", 10, "1"   ));
        //mRooms.add(new Room("room2", 10, "2"));
        Log.i("FetchRoomTask", "model update called, trying to fetch rooms");
        new FetchRoomsTask(this).execute();
        new FetchDecalsMetaDataTask(this).execute();
    }
    public void setRooms(List<Room> roomList) {
        mRooms = roomList;
    }

    public SSLHelper getSSLHelper() {
        return mSSLHelper;
    }

    public void setSSLHelper(SSLHelper mSSLHelper) {
        this.mSSLHelper = mSSLHelper;
    }

    public List<Decal> getDecals() {
        return mDecals;
    }
}
