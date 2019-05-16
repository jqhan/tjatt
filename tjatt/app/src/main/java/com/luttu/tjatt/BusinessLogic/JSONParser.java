package com.luttu.tjatt.BusinessLogic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSONParser {

    public static final String DEBUG_TAG = "JSONParser";

    public static List<Room> parseRoomList(JSONObject jsonObject) {

        List<Room> roomList = new ArrayList<>();
        try {
            JSONArray jsonRoomList = jsonObject.getJSONArray("list");
            for (int i = 0; i < jsonRoomList.length(); i++) {
                JSONObject jsonRoom = jsonRoomList.getJSONObject(i);

                Room room = new Room();
                room.setID(Integer.parseInt(jsonRoom.getString("id")));
                room.setRoomName(jsonRoom.getString("name"));
                room.setCapacity(Integer.parseInt(jsonRoom.getString("capacity")));
                roomList.add(room);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return roomList;
    }

    public static List<Decal> parseDecalList(JSONObject jsonObject) {

        List<Decal> decalList = new ArrayList<>();
        try {
            JSONArray jsonDecalList = jsonObject.getJSONArray("list");
            for (int i = 0; i < jsonDecalList.length(); i++) {
                JSONObject jsonDecal = jsonDecalList.getJSONObject(i);

                Decal decal = new Decal();
                decal.setId(Integer.parseInt(jsonDecal.getString("id")));
                decal.setFormat(jsonDecal.getString("format"));
                decal.setAddedBy(Integer.parseInt(jsonDecal.getString("added_by")));
                decalList.add(decal);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return decalList;
    }

    public static List<Message> parseMessageList(JSONObject jsonObject) {

        List<Message> messageList = new ArrayList<>();
        try {
            JSONArray jsonMsgList = jsonObject.getJSONArray("list");
            for (int i = 0; i < jsonMsgList.length(); i++) {
                JSONObject jsonMsg = jsonMsgList.getJSONObject(i);

                Message msg = new Message();
                msg.setID(Integer.parseInt(jsonMsg.getString("id")));
                msg.setRoomID(Integer.parseInt(jsonMsg.getString("roomID")));
                msg.setPostedBy(jsonMsg.getString("postedBy"));
                msg.setPostedByID(Integer.parseInt(jsonMsg.getString("postedByID")));
                msg.setDate(jsonMsg.getString("date"));
                msg.setType(jsonMsg.getString("type"));
                msg.setPayload(jsonMsg.getString("payload"));
                messageList.add(msg);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return messageList;
    }

    public static User parseLoginValidationData(JSONArray jsonArray) {
        try {
            // jsonArray = [ "OK/BAD", userID, userName ]
            if (jsonArray.get(0).equals("OK")) {
                int id = (int) jsonArray.get(1);
                String name = (String) jsonArray.get(2);
                return new User(id, name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Integer> parseActiveUsersList(JSONObject jsonObject) {
        List<Integer> activeUsers = new ArrayList<>();
        JSONArray jsonArray = null;
        try {
            jsonArray = jsonObject.getJSONArray("list");
            for (int i=0; i<jsonArray.length(); i++) {
                try {
                    activeUsers.add(jsonArray.getInt(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return activeUsers;
    }
}
