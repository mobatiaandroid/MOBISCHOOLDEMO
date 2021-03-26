package com.mobatia.naisapp.fragments.sports.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.fragments.parents_evening.model.StudentModel;
import com.mobatia.naisapp.manager.AppUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class StudentRecyclerListSportsAdapter extends RecyclerView.Adapter<StudentRecyclerListSportsAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<StudentModel> mStudentList;
    String dept;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgIcon;

        public MyViewHolder(View view) {
            super(view);

            imgIcon=(ImageView) view.findViewById(R.id.imagicon);
        }
    }


    public StudentRecyclerListSportsAdapter(Context mContext, ArrayList<StudentModel> mStudentList) {
        this.mContext = mContext;
        this.mStudentList = mStudentList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_student_list_sports_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.imgIcon.setVisibility(View.GONE);
        if (!mStudentList.get(position).getmPhoto().equals("")) {

            Picasso.with(mContext).load(AppUtils.replace(mStudentList.get(position).getmPhoto().toString())).placeholder(R.drawable.student).fit().into(holder.imgIcon);
        }
        else

        {

            holder.imgIcon.setImageResource(R.drawable.student);
        }
    }


    @Override
    public int getItemCount() {
        return mStudentList.size();
    }
}
