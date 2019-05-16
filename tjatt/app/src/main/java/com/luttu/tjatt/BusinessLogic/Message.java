package com.luttu.tjatt.BusinessLogic;

import java.util.Date;
import java.util.UUID;

public class Message {

    private int mID;
    private Object mPayload;
    private String mDate;
    private String mPostedBy;
    private int mPostedByID;
    private int mRoomID;
    private int mMessageID;
    private String mType;
    private boolean mUserIsOnline;

    public Message() {
        mUserIsOnline = false;
    };

    public void setPayload(Object payload) {
        mPayload = payload;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public Message(int messageID, int roomID, String postedBy, int postedByID, String date, String type, Object payload) {
        mMessageID = messageID;
        mPayload = payload;
        mPostedBy = postedBy;
        mRoomID = roomID;
        mDate = date;
        mPostedByID = postedByID;
        mUserIsOnline = true;
        mType = type;

    }

    public int getID() {
        return mID;
    }

    public void setID(int id) {
        mID = id;
    }

    public int getUserId() {
        return mPostedByID;
    }

    public void setUserId(int userId) {
        mPostedByID = userId;
    }

    public Object getPayload() {
        return mPayload;
    }

    public void setMsg(Object payload) {
        mPayload = payload;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public int getPostedByID() {
        return mPostedByID;
    }

    public void setPostedByID(int postedByID) {
        mPostedByID = postedByID;
    }

    public String getPostedBy() {
        return mPostedBy;
    }

    public void setPostedBy(String postedBy) {
        mPostedBy = postedBy;
    }

    public boolean isUserIsOnline() {
        return mUserIsOnline;
    }

    public void setUserIsOnline(boolean userIsOnline) {
        mUserIsOnline = userIsOnline;
    }

    public int getUserID() {
        return mPostedByID;
    }

    public void setUserID(int userId) {
        mPostedByID = userId;
    }

    public int getRoomID() {
        return mRoomID;
    }

    public void setRoomID(int roomID) {
        mRoomID = roomID;
    }
}
