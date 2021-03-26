package com.mobatia.naisapp.activities.staff_directory.adapter;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.staff_directory.model.StaffModel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by gayatri on 22/3/17.
 */
public class StaffAdapterAdapterNew extends  RecyclerView.Adapter<StaffAdapterAdapterNew.MyViewHolder> {
    ArrayList<StaffModel> mStaffModels;
    ArrayList<String> deptArrayList;
    Context mContext;
    HashMap<String, ArrayList<StaffModel>> hashmap;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout deptLayout;
        View separator;
        TextView deptName;
       RecyclerView mStaffDeptListView;

        public MyViewHolder(View view) {
            super(view);

           deptLayout= (LinearLayout) view.findViewById(R.id.deptLayout);
            mStaffDeptListView= (RecyclerView) view.findViewById(R.id.mStaffDepListView);
            deptName= (TextView) view.findViewById(R.id.departmentName);
            separator=view.findViewById(R.id.separator);
            //mStaffDeptListView = (RecyclerView) view.findViewById(R.id.mStaffListView);
            mStaffDeptListView.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(mContext);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            mStaffDeptListView.setLayoutManager(llm);


        }
    }
    public StaffAdapterAdapterNew(Context mContext,ArrayList<String> deptArrayList,ArrayList<StaffModel> mStaffList) {
        this.mContext = mContext;
        this.mStaffModels = mStaffList;
        this.deptArrayList=deptArrayList;
    }
    public StaffAdapterAdapterNew(Context mContext,ArrayList<String> deptArrayList,HashMap<String, ArrayList<StaffModel>> hashmap) {
        this.mContext = mContext;
        this.hashmap = hashmap;
        this.deptArrayList=deptArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customadapter_staffdept_listitem, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        System.out.println("Adapter---" + position + "--" + hashmap.get(deptArrayList.get(position)).size());
       if(hashmap.get(deptArrayList.get(position)).size()>0) {
           holder.deptName.setText(deptArrayList.get(position));
       }/*else if(hashmap.get(deptArrayList.get(position)).size()>0&&deptArrayList.size()==1){
           holder.deptLayout.setVisibility(View.GONE);
           //Toast.makeText(mContext)
           //AppUtils.showDialogAlertFinish();
           AppUtils.showDialogAlertFinish((Activity) mContext, mContext.getString(R.string.alert_heading), mContext.getString(R.string.no_details_available), R.drawable.exclamationicon, R.drawable.round);

       }*/else{
           holder.deptLayout.setVisibility(View.GONE);

       }

            holder.separator.setVisibility(View.GONE);
CustomStaffDeptRecyclerAdapter customStaffDeptRecyclerAdapter=new CustomStaffDeptRecyclerAdapter(mContext,hashmap.get(deptArrayList.get(position)),"list");
holder.mStaffDeptListView.setAdapter(customStaffDeptRecyclerAdapter);

    }


    @Override
    public int getItemCount() {
        System.out.println("Adapter---size" + deptArrayList.size());

        return deptArrayList.size();
    }
}
