package com.mobatia.naisapp.activities.bus_routes.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.bus_routes.Location;
import com.mobatia.naisapp.fragments.sports.model.BusModel;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.recyclerviewmanager.DividerItemDecoration;
import com.mobatia.naisapp.recyclerviewmanager.RecyclerItemListener;

import java.util.ArrayList;

/**
 * Created by Rijo on 17/1/17.
 */
public class BusRouteListRecyclerviewAdapter extends RecyclerView.Adapter<BusRouteListRecyclerviewAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<BusModel> mPhotosModelArrayList;
    String photo_id = "-1";
    RecyclerView.LayoutManager recyclerViewLayoutManager;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView busName;
        RecyclerView recycler_view_participant;
        ImageView location, callIconBusRoute;

        public MyViewHolder(View view) {
            super(view);

            busName = (TextView) view.findViewById(R.id.houseNameTextView);
            recycler_view_participant = (RecyclerView) view.findViewById(R.id.recycler_view_participant);
            location = (ImageView) view.findViewById(R.id.busLocation);
            callIconBusRoute = (ImageView) view.findViewById(R.id.callIconBusRoute);
        }
    }


    public BusRouteListRecyclerviewAdapter(Context mContext, ArrayList<BusModel> mPhotosList) {
        this.mContext = mContext;
        this.mPhotosModelArrayList = mPhotosList;
    }

    public BusRouteListRecyclerviewAdapter(Context mContext, ArrayList<BusModel> mPhotosList, String photo_id) {
        this.mContext = mContext;
        this.mPhotosModelArrayList = mPhotosList;
        this.photo_id = photo_id;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_adapter_bus_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.recycler_view_participant.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        holder.recycler_view_participant.setLayoutManager(llm);
        holder.recycler_view_participant.addItemDecoration(new DividerItemDecoration(mContext.getResources().getDrawable(R.drawable.list_divider)));
        BusRouteSubListRecyclerViewAdapter busRouteSubListRecyclerViewAdapter = new BusRouteSubListRecyclerViewAdapter(mContext, mPhotosModelArrayList.get(position).getmBusRoute());
        holder.recycler_view_participant.setAdapter(busRouteSubListRecyclerViewAdapter);

        holder.busName.setText(mPhotosModelArrayList.get(position).getBus_name() + " (" + mPhotosModelArrayList.get(position).getBus_no() + ")");
        holder.recycler_view_participant.addOnItemTouchListener(new RecyclerItemListener(mContext, holder.recycler_view_participant,
                new RecyclerItemListener.RecyclerTouchListener() {
                    public void onClickItem(View v, int pos) {
//                        showDetail(pos,position);

                    }

                    public void onLongClickItem(View v, int pos) {
//                        showDetail(pos,position);
                    }
                }));

        holder.location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPhotosModelArrayList.size() > 0) {
                    Intent intent = new Intent(mContext, Location.class);
                    intent.putExtra("busModelArray", mPhotosModelArrayList.get(position).getmBusRoute());
                    mContext.startActivity(intent);
                } else {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, mContext.getString(R.string.alert_heading), mContext.getString(R.string.no_details_available), R.drawable.infoicon, R.drawable.round);

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mPhotosModelArrayList.size();
    }

}
