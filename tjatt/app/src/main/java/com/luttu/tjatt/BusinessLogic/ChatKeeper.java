/*package com.luttu.tjatt.BusinessLogic.Model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class ChatKeeper {

    private static ChatKeeper sChatKeeper;
    private List<Message> mMessages;

    public static ChatKeeper get(Context context) {
        if (sChatKeeper == null) {
            sChatKeeper = new ChatKeeper(context);
        }
        return sChatKeeper;
    }

    public List<Message> getChats() {
        return mMessages;
    }

    public void addChat(String msg, String postedBy, String room) {
        mMessages.add(new Message(msg, postedBy, room));
    }

    private ChatKeeper(Context context) {
        mMessages = new ArrayList<>();
        mMessages.add(new Message("Hej", "User", "Room1"));
        mMessages.add(new Message("Halo", "User", "Room2"));
    }
}
*/