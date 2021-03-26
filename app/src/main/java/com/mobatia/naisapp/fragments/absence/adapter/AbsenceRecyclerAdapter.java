package com.mobatia.naisapp.fragments.absence.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.fragments.absence.model.LeavesModel;
import com.mobatia.naisapp.manager.AppUtils;

import java.util.ArrayList;

/**
 * Created by gayatri on 23/3/17.
 */
public class AbsenceRecyclerAdapter extends RecyclerView.Adapter<AbsenceRecyclerAdapter.MyViewHolder> {

private Context mContext;
private ArrayList<LeavesModel> mLeavesList;
        String dept;


public class MyViewHolder extends RecyclerView.ViewHolder {
    TextView listDate,listStatus;
    ImageView imgIcon;
    View v;
    RelativeLayout listBackGround;

    public MyViewHolder(View view) {
        super(view);

        //listName= (TextView) view.findViewById(R.id.listName);
        listDate= (TextView) view.findViewById(R.id.listDate);
        listStatus= (TextView) view.findViewById(R.id.listStatus);


        listBackGround= (RelativeLayout) view.findViewById(R.id.listBackGround);
    }
}


    public AbsenceRecyclerAdapter(Context mContext,ArrayList<LeavesModel> timeTableList) {
        this.mContext = mContext;
        this.mLeavesList = timeTableList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.absence_recycler_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        if(mLeavesList.get(position).getTo_date().equals(mLeavesList.get(position).getFrom_date())){
            holder.listDate.setText(AppUtils.dateConversionddmmyyyynew(mLeavesList.get(position).getFrom_date()));
        }else{
            holder.listDate.setText(Html.fromHtml(AppUtils.dateConversionddmmyyyynew(mLeavesList.get(position).getFrom_date()) + " to " +
                    AppUtils.dateConversionddmmyyyynew(mLeavesList.get(position).getTo_date())));
        }
        //holder.listName.setText(AppPreferenceManager.getStudentName(mContext));
        if(mLeavesList.get(position).getStatus().equals("1")){
            holder.listStatus.setText("Approved");
            holder.listStatus.setTextColor(mContext.getResources().getColor(R.color.nas_green));

        }else if(mLeavesList.get(position).getStatus().equals("2")){
            holder.listStatus.setText("Pending");
            holder.listStatus.setTextColor(mContext.getResources().getColor(R.color.rel_six));


        }else if(mLeavesList.get(position).getStatus().equals("3")){
            holder.listStatus.setText("Rejected");
            holder.listStatus.setTextColor(mContext.getResources().getColor(R.color.rel_nine));


        }
    }


    @Override
    public int getItemCount() {
        return mLeavesList.size();
    }
}
