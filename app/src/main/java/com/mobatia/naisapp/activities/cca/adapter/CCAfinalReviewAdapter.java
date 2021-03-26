package com.mobatia.naisapp.activities.cca.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.cca.model.CCADetailModel;
import com.mobatia.naisapp.manager.AppUtils;

import java.util.ArrayList;

/**
 * Created by gayatri on 22/3/17.
 */
public class CCAfinalReviewAdapter extends  RecyclerView.Adapter<CCAfinalReviewAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<CCADetailModel>mCCADetailModelArrayList;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCCAaDateItemChoice1;
        TextView textViewCCAaDateItemChoice2;

        TextView textViewCCADay;
        TextView textViewCCAChoice1;
        TextView textViewCCAChoice2;
        LinearLayout linearChoice1,linearChoice2;
        public MyViewHolder(View view) {
            super(view);

            textViewCCAaDateItemChoice1= (TextView) view.findViewById(R.id.textViewCCAaDateItemChoice1);
            textViewCCAaDateItemChoice2= (TextView) view.findViewById(R.id.textViewCCAaDateItemChoice2);
            textViewCCADay= (TextView) view.findViewById(R.id.textViewCCADay);
            textViewCCAChoice1= (TextView) view.findViewById(R.id.textViewCCAChoice1);
            textViewCCAChoice2= (TextView) view.findViewById(R.id.textViewCCAChoice2);
            linearChoice1= (LinearLayout) view.findViewById(R.id.linearChoice1);
            linearChoice2= (LinearLayout) view.findViewById(R.id.linearChoice2);


        }
    }

//    public CCAfinalReviewAdapter(Context mContext) {
//        this.mContext = mContext;
//    }
//    public CCAfinalReviewAdapter(Context mContext,ArrayList<CCADetailModel>mCCADetailModelArrayList) {
//        this.mContext = mContext;
//        this.mCCADetailModelArrayList = mCCADetailModelArrayList;
//    }
public CCAfinalReviewAdapter(Context mContext,ArrayList<CCADetailModel>mCCADetailModelArrayList) {
    this.mContext = mContext;
    this.mCCADetailModelArrayList = mCCADetailModelArrayList;
}
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_cca_final_review, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {




    holder.textViewCCADay.setText(mCCADetailModelArrayList.get(position).getDay());
        if (mCCADetailModelArrayList.get(position).getChoice1()==null)
        {
            holder.linearChoice1.setVisibility(View.GONE);
            holder.textViewCCAChoice1.setText("Choice 1 : Nil");

        }else
        {
            holder.linearChoice1.setVisibility(View.VISIBLE);

            holder.textViewCCAChoice1.setText("Choice 1 : "+mCCADetailModelArrayList.get(position).getChoice1());


            if (mCCADetailModelArrayList.get(position).getCca_item_start_timechoice1()!=null && mCCADetailModelArrayList.get(position).getCca_item_end_timechoice1()!=null)
            {
                holder.textViewCCAaDateItemChoice1.setVisibility(View.VISIBLE);

                holder.textViewCCAaDateItemChoice1.setText("("+AppUtils.convertTimeToAMPM(mCCADetailModelArrayList.get(position).getCca_item_start_timechoice1())+" - "+AppUtils.convertTimeToAMPM(mCCADetailModelArrayList.get(position).getCca_item_end_timechoice1())+")");

            }else if (mCCADetailModelArrayList.get(position).getCca_item_start_timechoice1()!=null)
            {
                holder.textViewCCAaDateItemChoice1.setVisibility(View.VISIBLE);

                holder.textViewCCAaDateItemChoice1.setText("("+AppUtils.convertTimeToAMPM(mCCADetailModelArrayList.get(position).getCca_item_start_timechoice1())+")");
            }else if (mCCADetailModelArrayList.get(position).getCca_item_end_timechoice1()!=null)
            {
                holder.textViewCCAaDateItemChoice1.setVisibility(View.VISIBLE);

                holder.textViewCCAaDateItemChoice1.setText("("+AppUtils.convertTimeToAMPM(mCCADetailModelArrayList.get(position).getCca_item_end_timechoice1())+")");
            }
            else
            {
                holder.textViewCCAaDateItemChoice1.setVisibility(View.GONE);

            }

        }
        if (mCCADetailModelArrayList.get(position).getChoice2()==null)
        {
            holder.linearChoice2.setVisibility(View.GONE);
            holder.textViewCCAChoice2.setText("Choice 2 : Nil");

        }else
        {
            holder.linearChoice2.setVisibility(View.VISIBLE);

            holder.textViewCCAChoice2.setText("Choice 2 : "+mCCADetailModelArrayList.get(position).getChoice2());
            if (mCCADetailModelArrayList.get(position).getCca_item_start_timechoice2()!=null && mCCADetailModelArrayList.get(position).getCca_item_end_timechoice2()!=null)
            {
                holder.textViewCCAaDateItemChoice2.setVisibility(View.VISIBLE);

                holder.textViewCCAaDateItemChoice2.setText("("+AppUtils.convertTimeToAMPM(mCCADetailModelArrayList.get(position).getCca_item_start_timechoice2())+" - "+AppUtils.convertTimeToAMPM(mCCADetailModelArrayList.get(position).getCca_item_end_timechoice2())+")");

            }else if (mCCADetailModelArrayList.get(position).getCca_item_start_timechoice2()!=null)
            {
                holder.textViewCCAaDateItemChoice2.setVisibility(View.VISIBLE);

                holder.textViewCCAaDateItemChoice2.setText("("+AppUtils.convertTimeToAMPM(mCCADetailModelArrayList.get(position).getCca_item_start_timechoice2())+")");
            }else if (mCCADetailModelArrayList.get(position).getCca_item_end_timechoice2()!=null)
            {
                holder.textViewCCAaDateItemChoice2.setVisibility(View.VISIBLE);

                holder.textViewCCAaDateItemChoice2.setText("("+AppUtils.convertTimeToAMPM(mCCADetailModelArrayList.get(position).getCca_item_end_timechoice2())+")");
            }
            else
            {
                holder.textViewCCAaDateItemChoice2.setVisibility(View.GONE);

            }
        }



    }
    @Override
    public int getItemCount() {
        return mCCADetailModelArrayList.size();
    }
}
