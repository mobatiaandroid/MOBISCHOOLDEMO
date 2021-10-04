package com.mobatia.naisapp.fragments.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.fragments.home.module.SurveyModel;
import com.mobatia.naisapp.fragments.secondary.model.SecondaryModel;

import java.util.ArrayList;

public class SurveyListAdapter extends RecyclerView.Adapter<SurveyListAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<SurveyModel> mSurveyArrayList;
    String dept;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageIcon;
        TextView pdfTitle;
        public MyViewHolder(View view) {
            super(view);
            imageIcon = (ImageView) view.findViewById(R.id.imageIcon);
            pdfTitle = (TextView) view.findViewById(R.id.pdfTitle);



        }
    }


    public SurveyListAdapter(Context mContext, ArrayList<SurveyModel> mSurveyArrayList) {
        this.mContext = mContext;
        this.mSurveyArrayList = mSurveyArrayList;

    }

    @Override
    public SurveyListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_pdf_adapter_row, parent, false);

        return new SurveyListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SurveyListAdapter.MyViewHolder holder, int position) {

        holder.pdfTitle.setText(mSurveyArrayList.get(position).getSurvey_name());
    }


    @Override
    public int getItemCount() {
        return mSurveyArrayList.size();
    }

}
