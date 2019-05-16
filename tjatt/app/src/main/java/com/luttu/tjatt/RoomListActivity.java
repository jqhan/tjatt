package com.luttu.tjatt;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Html;

import com.luttu.tjatt.BusinessLogic.TjattModel;

public class RoomListActivity extends SingleFragmentActivity {

    private static final String EXTRA_ROOM_ID =
            "com.luttu.tjatt.room_id";

    public static Intent createEnterRoomIntent(Context packageContext, int roomID) {
        Intent intent = new Intent(packageContext, RoomActivity.class);
        intent.putExtra(EXTRA_ROOM_ID, roomID);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        TjattModel model = ((TjattApp) this.getApplication()).getModel();
        return RoomListFragment.newInstance(model);
    }
}
