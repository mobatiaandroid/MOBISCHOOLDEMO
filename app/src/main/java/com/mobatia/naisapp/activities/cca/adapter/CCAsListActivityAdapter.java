package com.mobatia.naisapp.activities.cca.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.cca.model.CCAModel;
import com.mobatia.naisapp.manager.AppUtils;

import java.util.ArrayList;

/**
 * Created by gayatri on 22/3/17.
 */
public class CCAsListActivityAdapter extends RecyclerView.Adapter<CCAsListActivityAdapter.MyViewHolder> {
    ArrayList<CCAModel> mCCAmodelArrayList;
    Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView listTxtView;
        TextView listTxtViewDate;
        TextView status;
        ImageView statusImageView;
        RelativeLayout statusLayout;

        public MyViewHolder(View view) {
            super(view);

            listTxtView = (TextView) view.findViewById(R.id.textViewCCAaItem);
            listTxtViewDate = (TextView) view.findViewById(R.id.textViewCCAaDateItem);
            statusImageView = (ImageView) view.findViewById(R.id.statusImageView);
            statusLayout = (RelativeLayout) view.findViewById(R.id.statusLayout);
            status = (TextView) view.findViewById(R.id.status);


        }
    }
    public CCAsListActivityAdapter(Context mContext, ArrayList<CCAModel> mCCAmodelArrayList) {
        this.mContext = mContext;
        this.mCCAmodelArrayList = mCCAmodelArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_cca_first_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.listTxtView.setText(mCCAmodelArrayList.get(position).getTitle());
        holder.listTxtViewDate.setText(AppUtils.dateParsingTodd_MMM_yyyy(mCCAmodelArrayList.get(position).getFrom_date()) + " to " + AppUtils.dateParsingTodd_MMM_yyyy(mCCAmodelArrayList.get(position).getTo_date()));
        if (mCCAmodelArrayList.get(position).getIsAttendee().equalsIgnoreCase("0")) {
            if (mCCAmodelArrayList.get(position).getIsSubmissionDateOver().equalsIgnoreCase("1")) {
                //closed
                holder.statusImageView.setImageResource(R.drawable.closed);
            } else {
                holder.statusImageView.setImageResource(R.drawable.edit);//edit
            }
        } else if (mCCAmodelArrayList.get(position).getIsAttendee().equalsIgnoreCase("1")) {
            //approved
            holder.statusImageView.setImageResource(R.drawable.approve_new);

        } else if (mCCAmodelArrayList.get(position).getIsAttendee().equalsIgnoreCase("2")) {
            //pending
            holder.statusImageView.setImageResource(R.drawable.pending);

        }
        if (mCCAmodelArrayList.get(position).getStatus().equalsIgnoreCase("0")) {
            holder.statusLayout.setVisibility(View.VISIBLE);
            holder.status.setBackgroundResource(R.drawable.rectangle_red);
            holder.status.setText("New");
        } else if (mCCAmodelArrayList.get(position).getStatus().equalsIgnoreCase("1") || mCCAmodelArrayList.get(position).getStatus().equalsIgnoreCase("")) {
            holder.statusLayout.setVisibility(View.INVISIBLE);

        } else if (mCCAmodelArrayList.get(position).getStatus().equalsIgnoreCase("2")) {
            holder.statusLayout.setVisibility(View.VISIBLE);
            holder.status.setBackgroundResource(R.drawable.rectangle_orange);
            holder.status.setText("Updated");
        }

    }


    @Override
    public int getItemCount() {

        return mCCAmodelArrayList.size();
    }
}
