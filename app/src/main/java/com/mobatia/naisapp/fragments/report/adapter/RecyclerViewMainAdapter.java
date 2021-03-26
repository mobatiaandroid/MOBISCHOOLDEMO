package com.mobatia.naisapp.fragments.report.adapter;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.fragments.report.model.DataModel;
import com.mobatia.naisapp.fragments.report.model.StudentInfoModel;

import java.util.ArrayList;

/**
 * Created by krishnaraj on 18/07/18.
 */

public class RecyclerViewMainAdapter extends RecyclerView.Adapter<RecyclerViewMainAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<StudentInfoModel> mStudentArrayList;
    private ArrayList<DataModel> mDataModel;
    String dept;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView accYr;
        RecyclerView dataRecycle;

        public MyViewHolder(View view) {
            super(view);
            accYr = view.findViewById(R.id.accYr);
            dataRecycle= view.findViewById(R.id.recycler_view_list);
            LinearLayoutManager llm = new LinearLayoutManager(mContext);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            dataRecycle.setLayoutManager(llm);




        }
    }


    public RecyclerViewMainAdapter(Context mContext, ArrayList<StudentInfoModel> mStudentInfoModel) {
        this.mContext = mContext;
        this.mStudentArrayList = mStudentInfoModel;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_main_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.accYr.setText(mStudentArrayList.get(position).getAcyear());
        RecyclerViewSubAdapter mRecyclerViewSubAdapter = new RecyclerViewSubAdapter(mContext,mStudentArrayList.get(position).getmDataModel());
        holder.dataRecycle.setAdapter(mRecyclerViewSubAdapter);

    }


    @Override
    public int getItemCount() {
        return mStudentArrayList.size();
    }

}
