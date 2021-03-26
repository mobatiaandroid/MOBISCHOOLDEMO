package com.mobatia.naisapp.fragments.report.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.mobatia.naisapp.R;
import com.mobatia.naisapp.fragments.report.model.ReportModel;
import com.mobatia.naisapp.manager.AppUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by krishnaraj on 18/07/18.
 */

public class StudentSpinnerAdapter extends RecyclerView.Adapter<StudentSpinnerAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<ReportModel> mStudentList;
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


    public StudentSpinnerAdapter(Context mContext, ArrayList<ReportModel> mStudentList) {
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
        holder.mTitleTxt.setText(mStudentList.get(position).getmName());
        holder.imgIcon.setVisibility(View.VISIBLE);
        holder.listTxtClass.setText(mStudentList.get(position).getmClass());

       /* if(mStudentList.get(position).getAlumini().equalsIgnoreCase("1"))
        {
            holder.listTxtClass.setTextColor(mContext.getResources().getColor(R.color.grey));
            holder.listTxtClass.setText("Alumini");
        }
        else
        {

        }*/
        if (!mStudentList.get(position).getmPhoto().equals("")) {

            Picasso.with(mContext).load(AppUtils.replace(mStudentList.get(position).getmPhoto().toString())).placeholder(R.drawable.boy).fit().into(holder.imgIcon);
        }
        else

        {

            holder.imgIcon.setImageResource(R.drawable.boy);
        }
    }


    @Override
    public int getItemCount() {
        return mStudentList.size();
    }
}
