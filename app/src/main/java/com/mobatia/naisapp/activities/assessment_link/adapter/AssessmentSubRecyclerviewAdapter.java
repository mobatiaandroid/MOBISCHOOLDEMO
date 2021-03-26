package com.mobatia.naisapp.activities.assessment_link.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.assessment_link.AssementModel;

import java.util.ArrayList;

/**
 * Created by Rijo on 17/1/17.
 */
public class AssessmentSubRecyclerviewAdapter extends RecyclerView.Adapter<AssessmentSubRecyclerviewAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<AssementModel> mAssementModelArrayList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView listTxtTitle;

        public MyViewHolder(View view) {
            super(view);
            listTxtTitle = (TextView) view.findViewById(R.id.listTxtTitle);

        }
    }


    public AssessmentSubRecyclerviewAdapter(Context mContext, ArrayList<AssementModel> mAssementModelList) {
        this.mContext = mContext;
        this.mAssementModelArrayList = mAssementModelList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_aboutus_list_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.listTxtTitle.setText(mAssementModelArrayList.get(position).getTitle());

    }



    @Override
    public int getItemCount() {
        return mAssementModelArrayList.size();
    }

    @Override
    public void onViewDetachedFromWindow(MyViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }
}
