package com.luttu.tjatt;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.luttu.tjatt.BusinessLogic.TjattModel;
import com.luttu.tjatt.BusinessLogic.Decal;
import com.luttu.tjatt.UserInterfaceUtils.GridSpacingItemDecoration;

import java.util.List;

public class DecalDialogFragment extends DialogFragment {

    public static final String EXTRA_DECAL_ID = "com.luttu.tjatt.decalID";

    private RecyclerView mDecalsRecyclerView;
    private DecalsAdapter mAdapter;
    private Dialog mDialog;
    private TextView mDecalsTextView;
    private static final String ARG_DECAL_ID = "decalID";


    public static DecalDialogFragment newInstance() {
        Bundle args = new Bundle();

        DecalDialogFragment fragment = new DecalDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MyDialogFragmentStyle);
    }

    private void sendResult(int resultCode, int decalID){
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_DECAL_ID, decalID);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle(R.string.choose_decal);
        View v = inflater.inflate(R.layout.fragment_decals, container, false);
        mDecalsRecyclerView = (RecyclerView) v.findViewById(R.id.decals_recycler_view);
        mDecalsRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mDecalsRecyclerView.addItemDecoration(new GridSpacingItemDecoration(3, 25, true));
        updateUI();


/*
        mDialog = new AlertDialog.Builder(getActivity())
                .setTitle("Decals")
                .setPositiveButton(android.R.string.ok, null)
                .setView(v)
                .create();
*/
        return v;
    }

    private void updateUI() {
        TjattModel model = ((TjattApp) getActivity().getApplication()).getModel();
        List<Decal> mDecalsList = model.getDecals();
        mAdapter = new DecalsAdapter(mDecalsList);
        mDecalsRecyclerView.setAdapter(mAdapter);

    }

    private class DecalHolder extends RecyclerView.ViewHolder {

        private Decal mDecal;
        private ImageView mDecalImageView;

        public DecalHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_decal, parent, false));

            mDecalImageView = (ImageView) itemView.findViewById(R.id.decal_image_view);
            mDecalImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int mDecalID = mDecal.getID();
                    Log.d("DECAL ID", " " + mDecalID);
                    sendResult(Activity.RESULT_OK, mDecalID);
                    dismiss();
                }
            });

        }

        public void bind(Decal decal) {
            mDecal = decal;
            mDecalImageView.setImageBitmap(mDecal.getImgBinary());

        }
    }

    private class DecalsAdapter extends RecyclerView.Adapter<DecalHolder> {
        private List<Decal> mDecals;

        public DecalsAdapter(List<Decal> decals) {
            mDecals = decals;
        }

        @Override
        public DecalHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new DecalHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(DecalHolder holder, int position) {
            Decal decal = mDecals.get(position);
            holder.bind(decal);
        }

        @Override
        public int getItemCount() {
            return mDecals.size();
        }
    }


}
