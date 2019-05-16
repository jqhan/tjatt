package com.luttu.tjatt.BusinessLogic;

import android.util.Log;

import com.luttu.tjatt.RoomFragment;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private String mRoomName;
    private int mCapacity;
    private List<Message> mMessages;
    private int mID;
    private List<Integer> mActiveUsers = new ArrayList<>();

    public List<Integer> getActiveUsers() {
        return mActiveUsers;
    }

    public void setActiveUsers(List<Integer> mActiveUsers) {
        this.mActiveUsers = mActiveUsers;
    }

    public Room(){
        mMessages = new ArrayList<>();
        //mMessages.add(new Message("Hej", 44,"User", "Room1"));
        //mMessages.add(new Message("Halo", 44,"User", "Room2"));
    }

    public Room(String roomName, int capacity, int id) {
        mRoomName = roomName;
        mCapacity = capacity;
        mID = id;
        mMessages = new ArrayList<>();
        //mMessages.add(new Message("Hej", 44,"User", "Room1"));
        //mMessages.add(new Message("Halo", 44,"User", "Room2"));
    }

    public String getRoomName() {
        return mRoomName;
    }

    public int getID() {
        return mID;
    }

    public void setID(int mID) {
        this.mID = mID;
    }

    public void setRoomName(String roomName) {
        mRoomName = roomName;
    }

    public int getCapacity() {
        return mCapacity;
    }

    public void setCapacity(int capacity) {
        mCapacity = capacity;
    }

    public List<Message> getMessages() {
        return mMessages;
    }

    public void updateOnlinePresence(RoomFragment fragment) {
        new FetchActiveUsersTask(fragment).execute();
    }

    public void setMessages(List<Message> messages) {
        mMessages = messages;
    }

    public void addMessage(Message message) {
        mMessages.add(message);
    }
}
