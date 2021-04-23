package com.mobatia.naisapp.activities.universityguidance.guidanceessential.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.universityguidance.guidanceessential.model.GuidanceEssentialModel;
import com.mobatia.naisapp.activities.universityguidance.information.adapter.GuidanceInformationRecyclerAdapter;
import com.mobatia.naisapp.activities.universityguidance.information.model.InformationModel;

import java.util.ArrayList;

public class GuidanceEssentialAdapter  extends RecyclerView.Adapter<GuidanceEssentialAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<GuidanceEssentialModel> informationModelArrayList;
    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView listTxtTitle;
        private RelativeLayout statusLayout;
        private TextView status;
        public MyViewHolder(View view) {
            super(view);
            listTxtTitle = (TextView) view.findViewById(R.id.listTxtTitle);

        }
    }


    public GuidanceEssentialAdapter(Context mContext, ArrayList<GuidanceEssentialModel> informationModelArrayList) {
        this.mContext = mContext;
        this.informationModelArrayList = informationModelArrayList;

    }

    @Override
    public GuidanceEssentialAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_guidance_essentials, parent, false);

        return new GuidanceEssentialAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final GuidanceEssentialAdapter.MyViewHolder holder, int position) {

        holder.listTxtTitle.setText(informationModelArrayList.get(position).getName());

    }


    @Override
    public int getItemCount() {
        return informationModelArrayList.size();
    }

}
