package com.mobatia.naisapp.activities.staff_directory.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.staff_directory.model.StaffModel;

import java.util.ArrayList;

/**
 * Created by Sona NR on 20/1/17.
 */
public class CustomRecyclerListStaffDirAdapter extends RecyclerView.Adapter<CustomRecyclerListStaffDirAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<StaffModel> mStaffList;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mTitleTxt;

        public MyViewHolder(View view) {
            super(view);

            mTitleTxt = (TextView) view.findViewById(R.id.listTxtTitle);


        }
    }


    public CustomRecyclerListStaffDirAdapter(Context mContext,ArrayList<StaffModel> mStaffList) {
        this.mContext = mContext;
        this.mStaffList = mStaffList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_aboutus_list_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        holder.mTitleTxt.setText(mStaffList.get(position).getStaffCategoryName());


    }


    @Override
    public int getItemCount() {
        return mStaffList.size();
    }
}
