package com.mobatia.naisapp.activities.sports.teams.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.sports.teams.model.TeamEventModel;

import java.util.ArrayList;

public class TeamRecyclerAdapter extends RecyclerView.Adapter<TeamRecyclerAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<TeamEventModel> mStaffList;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mTitleTxt;
        ImageView iconImg;
        RelativeLayout relSub;

        public MyViewHolder(View view) {
            super(view);

            mTitleTxt = (TextView) view.findViewById(R.id.listTxtTitle);
            iconImg = (ImageView) view.findViewById(R.id.iconImg);
            relSub = (RelativeLayout) view.findViewById(R.id.relSub);

        }
    }


    public TeamRecyclerAdapter(Context mContext,ArrayList<TeamEventModel> mStaffList) {
        this.mContext = mContext;
        this.mStaffList = mStaffList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.team_recycler_adapter_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.iconImg.setVisibility(View.GONE);
        holder.mTitleTxt.setText(mStaffList.get(position).getTeamName());




    }


    @Override
    public int getItemCount() {
        return mStaffList.size();
    }
}
