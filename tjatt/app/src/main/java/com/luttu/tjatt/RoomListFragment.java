package com.luttu.tjatt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.luttu.tjatt.BusinessLogic.Room;
import com.luttu.tjatt.BusinessLogic.TjattModel;

import java.util.ArrayList;
import java.util.List;

public class RoomListFragment extends Fragment {

    private RecyclerView mRoomRecyclerView;
    private RecyclerView.Adapter mRoomAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Room> mRooms = new ArrayList<>();
    private TjattModel mModel;

    public static RoomListFragment newInstance(TjattModel model) {
        Bundle args = new Bundle();

        RoomListFragment fragment = new RoomListFragment();
        fragment.mModel = model;
        fragment.mRooms = model.getRooms();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_room_list, container, false);
        mRoomRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_room_list);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRoomRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRoomRecyclerView.setLayoutManager(mLayoutManager);
        mRoomRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_room_list);
        mRoomRecyclerView.setLayoutManager(mLayoutManager);

        setupAdapter();

        return v;
    }

    private void setupAdapter() {
        if (isAdded()) {
            mRoomAdapter = new RoomAdapter(mRooms);
            mRoomRecyclerView.setAdapter(mRoomAdapter);
        }
    }

    // En vy-model för en rad i listan
    private class RoomHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mRoomNameTextView;
        private TextView mCapacityTextView;

        public RoomHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mRoomNameTextView = (TextView) itemView.findViewById(R.id.textViewRoomName);
            mCapacityTextView = (TextView) itemView.findViewById(R.id.textViewCapacity);
        }

        public void bindRoomName(String roomName) {
            mRoomNameTextView.setText(roomName);
        }

        public void bindCapacity(String capacity) {
            mCapacityTextView.setText(capacity);
        }

        @Override
        public void onClick(View itemView) {
            Room room = mRooms.get(getAdapterPosition());
//            Toast.makeText(getActivity(), "clicked on " + room.getRoomName(), Toast.LENGTH_SHORT).show();
            //Intent intent = new Intent(getActivity(), RoomActivity.class);
//            Intent intent = RoomActivity.newIntent(getActivity(), mRooms.get(getAdapterPosition()).getRoomName());
            Intent intent = RoomListActivity.createEnterRoomIntent(getActivity(), room.getID());
            startActivity(intent);
        }
    }

    // En bro mellan data och list-vyn. Adaptern populate:ar listan.
    private class RoomAdapter extends RecyclerView.Adapter<RoomHolder> {
        private List<Room> mRooms;

        public RoomAdapter(List<Room> rooms) {
            mRooms = rooms;
        }

        @Override
        public RoomHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.list_item_room, viewGroup, false);
            return new RoomHolder(view);
        }

        @Override
        public void onBindViewHolder(RoomHolder roomHolder, int position) {
            Room room = mRooms.get(position);

            // knyt rumnamn från datan till vyns textview för rumnamn
            roomHolder.bindRoomName(room.getRoomName());

            // knyt kapacitet från datan till vyns textview för kapacitet
            roomHolder.bindCapacity(Integer.toString(room.getCapacity()));


        }

        @Override
        public int getItemCount() {
            return mRooms.size();
        }
    }
}
