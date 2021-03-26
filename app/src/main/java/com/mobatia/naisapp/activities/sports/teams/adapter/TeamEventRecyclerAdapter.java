package com.mobatia.naisapp.activities.sports.teams.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.sports.teams.model.TeamEventModel;

import java.util.ArrayList;

public class TeamEventRecyclerAdapter extends RecyclerView.Adapter<TeamEventRecyclerAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<TeamEventModel> mStudentList;
    String dept;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mTitleTxt,listTxtClass;
        ImageView imgIcon;

        public MyViewHolder(View view) {
            super(view);

            mTitleTxt= view.findViewById(R.id.listTxtTitle);
            listTxtClass= view.findViewById(R.id.listTxtClass);
            imgIcon= view.findViewById(R.id.imagicon);
        }
    }


    public TeamEventRecyclerAdapter(Context mContext, ArrayList<TeamEventModel> mStudentList) {
        this.mContext = mContext;
        this.mStudentList = mStudentList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_student_list_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.mTitleTxt.setText(mStudentList.get(position).getTeamName());
        holder.imgIcon.setVisibility(View.VISIBLE);
        //holder.listTxtClass.setText(mStudentList.get(position).getmClass());

       /* if(mStudentList.get(position).getAlumini().equalsIgnoreCase("1"))
        {
            holder.listTxtClass.setTextColor(mContext.getResources().getColor(R.color.grey));
            holder.listTxtClass.setText("Alumini");
        }
        else
        {

        }*/

    }


    @Override
    public int getItemCount() {
        return mStudentList.size();
    }
}
