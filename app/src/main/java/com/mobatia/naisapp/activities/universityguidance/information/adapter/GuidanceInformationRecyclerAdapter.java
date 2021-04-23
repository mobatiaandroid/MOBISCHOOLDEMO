package com.mobatia.naisapp.activities.universityguidance.information.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.sports.information.adapter.InformationRecyclerAdapter;
import com.mobatia.naisapp.activities.universityguidance.information.model.InformationModel;
import com.mobatia.naisapp.fragments.secondary.model.SecondaryModel;

import java.util.ArrayList;

public class GuidanceInformationRecyclerAdapter extends RecyclerView.Adapter<GuidanceInformationRecyclerAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<InformationModel> informationModelArrayList;
    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageIcon;
        TextView pdfTitle;
        private RelativeLayout statusLayout;
        private TextView status;
        public MyViewHolder(View view) {
            super(view);
            imageIcon = (ImageView) view.findViewById(R.id.imageIcon);
            pdfTitle = (TextView) view.findViewById(R.id.pdfTitle);

        }
    }


    public GuidanceInformationRecyclerAdapter(Context mContext, ArrayList<InformationModel> informationModelArrayList) {
        this.mContext = mContext;
        this.informationModelArrayList = informationModelArrayList;

    }

    @Override
    public GuidanceInformationRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_guidance_info_recycler, parent, false);

        return new GuidanceInformationRecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final GuidanceInformationRecyclerAdapter.MyViewHolder holder, int position) {

        holder.pdfTitle.setText(informationModelArrayList.get(position).getName());
        holder.imageIcon.setVisibility(View.GONE);

    }


    @Override
    public int getItemCount() {
        return informationModelArrayList.size();
    }

}
