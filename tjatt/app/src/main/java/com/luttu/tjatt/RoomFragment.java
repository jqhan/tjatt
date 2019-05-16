package com.luttu.tjatt;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Ack;
import com.github.nkzawa.socketio.client.Socket;
import com.luttu.tjatt.BusinessLogic.FetchMessagesTask;
import com.luttu.tjatt.BusinessLogic.Message;
import com.luttu.tjatt.BusinessLogic.Room;
import com.luttu.tjatt.BusinessLogic.TjattModel;
import com.luttu.tjatt.BusinessLogic.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static android.graphics.Color.WHITE;

public class RoomFragment extends Fragment {

    public static final String ARG_ROOM_NAME = "room_name";
    public static final String ARG_USER_NAME = "user_name";
    private static final int REQUEST_DECAL = 0;
    private static final String DIALOG = "dialog";
    private static final String DEBUG_TAG = "RoomFragment";
    private RecyclerView mChatRecyclerView;
    private MessageAdapter mAdapter;
    private Button mSendButton;
    private EditText mEditText;
    private TextView mRoomNameTextView;
    private ImageButton mDecalsButton;
    private Room mRoom;
    private User mUser;
    private RoomFragment mInstance;

    private TjattModel mModel;


    private Socket mSocket;

    public static RoomFragment newInstance(Room room, User user) {
        Bundle args = new Bundle();
        RoomFragment fragment = new RoomFragment();
        fragment.mRoom = room;
        fragment.mUser = user;
        fragment.setArguments(args);
        return fragment;
    }

    public Room getRoom() {
        return mRoom;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DECAL) {
            int mDecalID = (int) data.getSerializableExtra(DecalDialogFragment.EXTRA_DECAL_ID);
            Log.d(DEBUG_TAG, "onActivityResult: " + mDecalID);


            mSocket.emit("newMessage", mRoom.getID(), mUser.getID(), "decal", mDecalID, new Ack() {
                @Override
                public void call(Object... args) {
                    Log.i(DEBUG_TAG, "Server received emit");
                }
            });
        }
    }

    public TjattModel getModel() {
        return mModel;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mInstance = this;

        Log.d(DEBUG_TAG, mRoom.getRoomName());

        TjattApp app = (TjattApp) getActivity().getApplication();
        mModel = app.getModel();
        new FetchMessagesTask(mModel, mRoom.getID(), this).execute();
        Log.d(DEBUG_TAG, app.toString());
        mSocket = app.getModel().getSSLHelper().getSocket();
        mSocket.connect();
        mRoom.updateOnlinePresence(this);

        Log.d(DEBUG_TAG, "is socket connected? " + mSocket.connected());
        mSocket.emit("join", mUser.getID(), mUser.getUserName(), mRoom.getID());


        mSocket.on("messageDeleted", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                if (getActivity() == null) {
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int messageID = (int) args[0];
                        for (int i = 0; i < mAdapter.getMessages().size(); i++) {
                            if (mAdapter.getMessages().get(i).getID() == messageID) {
                                Log.d(DEBUG_TAG, "Removing msg in client");
                                mRoom.getMessages().remove(mAdapter.getMessages().get(i));
                            }
                        }
                        updateUI();
                    }
                });
            }
        });

        mSocket.on("userJoinedChat", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                if (getActivity() == null) {
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int userID = (int) args[0];
                        String userName = (String) args[1];

                        int roomID = (int) args[2];
                        Log.d(DEBUG_TAG, "user id joined " + userID);

                        if (roomID != mRoom.getID()) {
                            return;
                        }

                        mRoom.updateOnlinePresence(mInstance); // calls updatesUI() when finished
                        if (userID != mUser.getID()) {
                            Toast.makeText(getActivity(), userName + " joined the chat", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        mSocket.on("message", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                if (getActivity() == null) {
                    return;
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(DEBUG_TAG, args[0].toString());
                        JSONObject data = (JSONObject) args[0];
                        Log.d(DEBUG_TAG, data.toString());
                        try {
                            int roomID = Integer.parseInt(data.getString("roomID"));
                            if (roomID != mRoom.getID()) {
                                return;
                            }
                            int messageID = Integer.parseInt(data.getString("id"));
                            String postedBy = data.getString("postedBy");
                            Log.d(DEBUG_TAG, "Got message from: " + postedBy);
                            int postedByID = Integer.parseInt(data.getString("postedByID"));
                            String date = data.getString("date");
                            String type = data.getString("type");
                            Object payload = data.getString("payload");

                            Message message = new Message(messageID, roomID, postedBy, postedByID, date, type, payload);
                            mRoom.addMessage(message);
                            updateUI();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        mSocket.on("userDisconnect", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                if (getActivity() == null) {
                    return;
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int userID = (int) args[0];
                        String userName = (String) args[1];

                        int roomID = (int) args[2];

                        if (roomID != mRoom.getID() || userID == mUser.getID()) {
                            return;
                        }

                        Toast.makeText(getActivity(), userName + " has left", Toast.LENGTH_SHORT).show();
                        mRoom.updateOnlinePresence(mInstance);
                        updateUI();
                    }
                });
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_chat_list, container, false);
        mChatRecyclerView = (RecyclerView) view
                .findViewById(R.id.chat_recycler_view);
        mChatRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // VERY BAD work-around to prevent incoming message to be right-aligned (happened sometimes)
        mChatRecyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);

        mSendButton = (Button) view.findViewById(R.id.button_send_msg);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = mEditText.getEditableText().toString();
                Log.d(DEBUG_TAG, "försöker skicka texten: " + content);
                mSocket.emit("newMessage", mRoom.getID(), mUser.getID(), "text", content, new Ack() {
                    @Override
                    public void call(Object... args) {
                        Log.i(DEBUG_TAG, "Server received emit");
                    }
                });
                mEditText.getText().clear();
            }
        });

        mEditText = (EditText) view.findViewById(R.id.edittext_chat);

        mDecalsButton = (ImageButton) view.findViewById(R.id.emoji);

        mDecalsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DecalDialogFragment dialog = DecalDialogFragment.newInstance();
                dialog.setTargetFragment(RoomFragment.this, REQUEST_DECAL);
                dialog.show(manager, DIALOG);
            }
        });

        updateUI();

        return view;
    }


    public MessageAdapter getAdapter() {
        return mAdapter;
    }

    public void updateUI() {
        List<Message> messages = mRoom.getMessages();


        if (mAdapter == null) {
            mAdapter = new MessageAdapter(messages);
            mChatRecyclerView.setAdapter(mAdapter);
        } else {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    Log.d(DEBUG_TAG, "Notifying adapter from UI thread");
                    mAdapter.notifyDataSetChanged();
                    mAdapter.setMessages(mAdapter.getMessages());
                    // scrollar automatiskt till nyaste meddelandet
                    mChatRecyclerView.scrollToPosition(mAdapter.getMessages().size() - 1);
                }
            });
        }


    }

    private class MessageHolder extends RecyclerView.ViewHolder {

        private Message mMessage;
        private TextView mUserTextView;
        private TextView mMsgTextView;
        private TextView mDateTextView;
        private ImageView mOnlineIndicator;
        private ImageView mDecalView;


        public MessageHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_chat, parent, false));

            mUserTextView = (TextView) itemView.findViewById(R.id.user);
            mMsgTextView = (TextView) itemView.findViewById(R.id.chat_msg);
            mDateTextView = (TextView) itemView.findViewById(R.id.chat_date);
            mOnlineIndicator = (ImageView) itemView.findViewById(R.id.green_dot);
            mDecalView = (ImageView) itemView.findViewById(R.id.decal_sent);

        }

        private void makeMessageViewAlignRight(ConstraintLayout view) {
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(view);
            // Removes chat_msg connection to the left (to parent)
            constraintSet.clear(R.id.chat_msg, ConstraintSet.START);
            // Adds connection between chat_msg to the right (to parent)
            constraintSet.connect(R.id.chat_msg, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 16);

            constraintSet.clear(R.id.chat_date, ConstraintSet.START);
            constraintSet.clear(R.id.user, ConstraintSet.START);
            constraintSet.clear(R.id.decal_sent, ConstraintSet.START);
            constraintSet.connect(R.id.decal_sent, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 8);
            constraintSet.connect(R.id.user, ConstraintSet.END, R.id.chat_date, ConstraintSet.START, 8);

            constraintSet.applyTo(view);
        }

        public void bind(Message message) {
            mMessage = message;

            mDateTextView.setText(mMessage.getDate());
            mUserTextView.setText(mMessage.getPostedBy());
            mOnlineIndicator.setVisibility(mMessage.isUserIsOnline() ? View.VISIBLE : View.GONE);

            if (mMessage.getPostedByID() == mUser.getID()) {
                mOnlineIndicator.setVisibility(View.GONE);
                mUserTextView.setText("You");
                makeMessageViewAlignRight((ConstraintLayout) itemView);
                if (mMessage.getType().equals("text")) {
                    mMsgTextView.setPadding(20, 15, 70, 15);
                    mMsgTextView.setBackgroundResource(R.drawable.shape_bg_outgoing_bubble);
                    mMsgTextView.setTextColor(WHITE);
                }
            } else {
                if (mMessage.getType().equals("text")) {
                    mMsgTextView.setBackgroundResource(R.drawable.shape_bg_incoming_bubble);
                }
            }

            if (mMessage.getType().equals("text")) {
                mMsgTextView.setVisibility(View.VISIBLE);
                mDecalView.setVisibility(View.GONE);
                mMsgTextView.setText(mMessage.getPayload().toString());
            } else {
                //Log.d(DEBUG_TAG, "payload " + mMessage.getPayload());
                mMsgTextView.setVisibility(View.GONE);
                int decalID = Integer.parseInt((mMessage.getPayload()).toString());
                TjattModel model = ((TjattApp) getActivity().getApplication()).getModel();

                /* ta bort det här när vi har decals i databasen
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_insert_emoticon);
                model.getDecal(decalID).setImgBinary(bitmap);
                */


                mDecalView.setVisibility(View.VISIBLE);
                mDecalView.setImageBitmap(model.getDecal(decalID).getImgBinary());
            }
        }
    }

    public class MessageAdapter extends RecyclerView.Adapter<MessageHolder> {
        private List<Message> mMessages;

        public MessageAdapter(List<Message> messages) {
            mMessages = messages;
        }

        @Override
        public MessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new MessageHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(MessageHolder holder, final int position) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mAdapter.getMessages().get(position).getPostedByID() == mUser.getID()) {
                        Log.d(DEBUG_TAG, "onlongclicked");
                        new AlertDialog.Builder(getContext())
                                .setTitle("Delete entry")
                                .setMessage("Are you sure you want to delete this entry?")

                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                // The dialog is automatically dismissed when a dialog button is clicked.
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        mSocket.emit("deleteMessage", mAdapter.getMessages().get(position).getID());
                                    }
                                })

                                // A null listener allows the button to dismiss the dialog and take no further action.
                                .setNegativeButton(android.R.string.no, null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                    return true;
                }
            });
            Message message = mMessages.get(position);
            holder.bind(message);
            if (message.getPostedByID() == mUser.getID()) {
//                Resources res = getResources();
//                try {
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (XmlPullParserException e) {
//                    e.printStackTrace();
//                }
            }
        }


        @Override
        public int getItemCount() {
            return mMessages.size();
        }

        public List<Message> getMessages() {
            return mMessages;
        }

        public void setMessages(List<Message> messages) {
            mMessages = messages;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mSocket.emit("join", mUser.getID(), mUser.getUserName(), mRoom.getID());
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
        mSocket.emit("join", mUser.getID(), mUser.getUserName(), mRoom.getID());
    }

    @Override
    public void onPause() {
        super.onPause();
        mSocket.emit("userDisconnect", mUser.getID(), mUser.getUserName(), mRoom.getID());
    }

    @Override
    public void onStop() {
        super.onStop();
        mSocket.emit("userDisconnect", mUser.getID(), mUser.getUserName(), mRoom.getID());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSocket.emit("userDisconnect", mUser.getID(), mUser.getUserName(), mRoom.getID());
        //mSocket.disconnect();
    }
}
