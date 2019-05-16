package com.luttu.tjatt;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;

import com.luttu.tjatt.BusinessLogic.Room;
import com.luttu.tjatt.BusinessLogic.TjattModel;

public class RoomActivity extends AppCompatActivity {
    private String ROOM_ACTIVITY_TAG = "RoomActivity";
    private static final String EXTRA_ROOM_ID =
            "com.luttu.tjatt.room_id";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        int roomID = (int) getIntent().getIntExtra(EXTRA_ROOM_ID,0);
        //Log.d(ROOM_ACTIVITY_TAG, roomID);

        TjattModel model = ((TjattApp) this.getApplication()).getModel();
        Room room = model.getRoom(roomID);

        this.setTitle(Html.fromHtml("<font color=\"black\">" + room.getRoomName() + "</font>", 0));
        this.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.niceYellow))); // #f2ef57 = nice yellow


        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);


        if (fragment == null) {
            fragment = RoomFragment.newInstance(room, model.getUser());
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }
    }
}
