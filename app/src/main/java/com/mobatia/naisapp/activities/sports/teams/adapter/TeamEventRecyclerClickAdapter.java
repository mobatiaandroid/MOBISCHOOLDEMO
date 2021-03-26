package com.mobatia.naisapp.activities.sports.teams.adapter;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.sports.teams.model.TeamEventClickModel;

import java.util.ArrayList;

public class TeamEventRecyclerClickAdapter extends RecyclerView.Adapter<TeamEventRecyclerClickAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<TeamEventClickModel> mStudentList;
    String dept;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView dayTxt,dateTxt,timeTxt,placeTxt;
        RecyclerView recycler_view_social_media;

        public MyViewHolder(View view) {
            super(view);

            dayTxt= view.findViewById(R.id.dayTxt);
            recycler_view_social_media= view.findViewById(R.id.recycler_view_social_media);

        }
    }


    public TeamEventRecyclerClickAdapter(Context mContext, ArrayList<TeamEventClickModel> mStudentList) {
        this.mContext = mContext;
        this.mStudentList = mStudentList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dailog_event_team_recycler, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.dayTxt.setText(mStudentList.get(position).getDay());

        holder.recycler_view_social_media.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        holder.recycler_view_social_media.setLayoutManager(llm);

        TeamEventRecyclerListDPVAdapter mTeamEventRecyclerAdapter= new TeamEventRecyclerListDPVAdapter(mContext,mStudentList.get(position).getTeamDatesModel());
        holder.recycler_view_social_media.setAdapter(mTeamEventRecyclerAdapter);

        }


    @Override
    public int getItemCount() {
        return mStudentList.size();
    }
}
