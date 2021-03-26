package com.mobatia.naisapp.activities.participants.adapter;

import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.fragments.sports.model.SportsModel;
import com.mobatia.naisapp.recyclerviewmanager.ItemOffsetDecoration;

import java.util.ArrayList;

/**
 * Created by Rijo on 17/1/17.
 */
public class ParticipantRecyclerviewAdapter extends RecyclerView.Adapter<ParticipantRecyclerviewAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<SportsModel> mPhotosModelArrayList;
    String photo_id="-1";
    RecyclerView.LayoutManager recyclerViewLayoutManager;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView houseNameTextView;
        RecyclerView recycler_view_participant;
        public MyViewHolder(View view) {
            super(view);

            houseNameTextView = (TextView) view.findViewById(R.id.houseNameTextView);
            recycler_view_participant = (RecyclerView) view.findViewById(R.id.recycler_view_participant);

        }
    }


    public ParticipantRecyclerviewAdapter(Context mContext, ArrayList<SportsModel> mPhotosList) {
        this.mContext = mContext;
        this.mPhotosModelArrayList = mPhotosList;
    }
    public ParticipantRecyclerviewAdapter(Context mContext, ArrayList<SportsModel> mPhotosList, String photo_id) {
        this.mContext = mContext;
        this.mPhotosModelArrayList = mPhotosList;
        this.photo_id = photo_id;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_adapter_participation, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
//        if (!mPhotosModelArrayList.get(position).getPhotoUrl().equalsIgnoreCase("")) {
//            Picasso.with(mContext).load(AppUtils.replace(mPhotosModelArrayList.get(position).getPhotoUrl())).fit()
//                    .into(holder.photoImageView, new com.squareup.picasso.Callback() {
//                        @Override
//                        public void onSuccess() {
//
//                        }
//
//                        @Override
//                        public void onError() {
//
//                        }
//                    });
//        }
//        if (!photo_id.equalsIgnoreCase("-1")) {
//            if (photo_id.equalsIgnoreCase(mPhotosModelArrayList.get(position).getPhotoId())) {
//                didTapButton(holder.photoImageView);
//            }
//        }
        holder. recycler_view_participant.setHasFixedSize(true);
        recyclerViewLayoutManager = new GridLayoutManager(mContext, 4);
        int spacing = 5; // 50px
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext,spacing);
        holder.recycler_view_participant.addItemDecoration(itemDecoration);
        holder.recycler_view_participant.setLayoutManager(recyclerViewLayoutManager);
        holder.recycler_view_participant.setAdapter(new ParticipantHouseWisetListRecyclerviewAdapter(mContext, mPhotosModelArrayList.get(position).getSportsModelParticipantsArrayList(),mPhotosModelArrayList.get(position).getSports_name()));
//        holder.recycler_view_participant.setAdapter(new ParticipantHouseWisetListRecyclerviewAdapter(mContext, mPhotosModelArrayList.get(position).getSportsModelParticipantsArrayList()));
        holder.houseNameTextView.setText(mPhotosModelArrayList.get(position).getSports_name());

//                }));
    }

    @Override
    public int getItemCount() {
        return mPhotosModelArrayList.size();
    }

}
