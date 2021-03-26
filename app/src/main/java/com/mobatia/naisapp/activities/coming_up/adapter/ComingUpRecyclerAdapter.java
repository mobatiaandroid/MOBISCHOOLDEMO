package com.mobatia.naisapp.activities.coming_up.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.coming_up.model.ComingUpModel;
import com.mobatia.naisapp.manager.AppUtils;

import java.util.ArrayList;

public class ComingUpRecyclerAdapter extends RecyclerView.Adapter<ComingUpRecyclerAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<ComingUpModel> mStaffList;
    LayoutInflater inflater;





    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView listTxtTitle;
        TextView listTxtDate;
        ImageView iconImg;
        RelativeLayout statusLayout;
        TextView status;

        public MyViewHolder(View view) {
            super(view);
            listTxtTitle = view.findViewById(R.id.listTxtTitle);
            listTxtDate= view.findViewById(R.id.listTxtDate);
            iconImg= view.findViewById(R.id.iconImg);
            statusLayout= view.findViewById(R.id.statusLayout);
            status= view.findViewById(R.id.status);




        }
    }


    public ComingUpRecyclerAdapter(Context context,
                           ArrayList<ComingUpModel> arrList) {
        this.mContext = context;
        this.mStaffList = arrList;

    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_coming_up_list_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.listTxtTitle.setText(mStaffList.get(position).getTitle().trim());
        holder.listTxtDate.setText(AppUtils.dateParsingTodd_MMM_yyyy(mStaffList.get(position).getDate()));
        if (mStaffList.get(position).getStatus().equalsIgnoreCase("0")) {
            holder.statusLayout.setVisibility(View.VISIBLE);
            holder.status.setBackgroundResource(R.drawable.rectangle_red);
            holder.status.setText("New");
        } else if (mStaffList.get(position).getStatus().equalsIgnoreCase("1") || mStaffList.get(position).getStatus().equalsIgnoreCase("")) {
            holder.status.setVisibility(View.INVISIBLE);
        } else if (mStaffList.get(position).getStatus().equalsIgnoreCase("2")) {
            holder.statusLayout.setVisibility(View.VISIBLE);
            holder.status.setBackgroundResource(R.drawable.rectangle_orange);
            holder.status.setText("Updated");
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public int getItemCount() {
        return mStaffList.size();
    }

}
