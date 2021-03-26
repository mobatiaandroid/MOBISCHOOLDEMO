package com.mobatia.naisapp.fragments.parents_evening.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.calender.model.CalendarEventsModel;

import java.util.ArrayList;

/**
 * Created by gayatri on 22/3/17.
 */
public class ParentsEveningRoomListAdapter extends  RecyclerView.Adapter<ParentsEveningRoomListAdapter.MyViewHolder> {
    ArrayList<CalendarEventsModel> mSocialMediaModels;
    Context mContext;
    Drawable iconImage;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgIcon;
        TextView listTxtView,listTxtClass;

        public MyViewHolder(View view) {
            super(view);

          imgIcon= (ImageView) view.findViewById(R.id.imagicon);
            listTxtView= (TextView) view.findViewById(R.id.listTxtTitle);
            listTxtClass= (TextView) view.findViewById(R.id.listTxtClass);


        }
    }
    public ParentsEveningRoomListAdapter(Context mContext, ArrayList<CalendarEventsModel> mSocialMediaModels) {
        this.mContext = mContext;
      this.mSocialMediaModels=mSocialMediaModels;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_room_list_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

    holder.listTxtView.setText(mSocialMediaModels.get(position).getStartTime()+" - "+mSocialMediaModels.get(position).getEndTime());
    holder.listTxtClass.setText("Room:"+mSocialMediaModels.get(position).getRoom());


    }


    @Override
    public int getItemCount() {

        return mSocialMediaModels.size();
    }
}
