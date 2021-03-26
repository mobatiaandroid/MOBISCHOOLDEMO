package com.mobatia.naisapp.activities.canteen_new.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.vision.text.Line;
import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.canteen_new.model.DateModel;
import com.mobatia.naisapp.activities.term_calendar.model.TermsCalendarModel;

import java.util.ArrayList;

public class DateRecyclerAdapter extends RecyclerView.Adapter<DateRecyclerAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<DateModel> mDateModelArrayList;
    String dept;


    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView dayTxt,dateTxt,monthTxt;
        ImageView itemSelectedImg;
        RelativeLayout bgLinear;

        public MyViewHolder(View view) {
            super(view);

            dayTxt = (TextView) view.findViewById(R.id.dayTxt);
            dateTxt = (TextView) view.findViewById(R.id.dateTxt);
            monthTxt = (TextView) view.findViewById(R.id.monthTxt);
            itemSelectedImg = (ImageView) view.findViewById(R.id.itemSelectedImg);
            bgLinear = (RelativeLayout) view.findViewById(R.id.bgLinear);

        }
    }


    public DateRecyclerAdapter(Context mContext, ArrayList<DateModel> mDateModelArrayList) {
        this.mContext = mContext;
        this.mDateModelArrayList = mDateModelArrayList;

    }

    @Override
    public DateRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_date_recycler, parent, false);

        return new DateRecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final DateRecyclerAdapter.MyViewHolder holder, int position) {
        holder.dateTxt.setText(mDateModelArrayList.get(position).getNumberDate());
        holder.dayTxt.setText(mDateModelArrayList.get(position).getDay());
        holder.monthTxt.setText(mDateModelArrayList.get(position).getMonth());
        System.out.println("Selected Date in positions"+mDateModelArrayList.get(position).isDateSelected());
        if (mDateModelArrayList.get(position).isDateSelected())
        {
            holder.bgLinear.setBackgroundResource(R.drawable.date_selected);
            holder.dateTxt.setTextColor(mContext.getResources().getColor(R.color.canteen_date_orange));
            holder.dayTxt.setTextColor(mContext.getResources().getColor(R.color.canteen_date_orange));
            holder.monthTxt.setTextColor(mContext.getResources().getColor(R.color.canteen_date_orange));
        }
        else
        {
            holder.bgLinear.setBackgroundResource(R.drawable.date);
            holder.dateTxt.setTextColor(mContext.getResources().getColor(R.color.dark_grey1));
            holder.dayTxt.setTextColor(mContext.getResources().getColor(R.color.dark_grey1));
            holder.monthTxt.setTextColor(mContext.getResources().getColor(R.color.dark_grey1));
        }
        if (mDateModelArrayList.get(position).isItemSelected())
        {
            holder.itemSelectedImg.setVisibility(View.VISIBLE);
            holder.itemSelectedImg.setImageResource(R.drawable.close);
        }
        else
        {
            holder.itemSelectedImg.setVisibility(View.GONE);
        }


    }


    @Override
    public int getItemCount() {
        return mDateModelArrayList.size();
    }

}
