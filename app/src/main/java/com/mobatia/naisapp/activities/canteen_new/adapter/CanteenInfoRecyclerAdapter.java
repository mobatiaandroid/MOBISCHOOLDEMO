package com.mobatia.naisapp.activities.canteen_new.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.canteen_new.model.CanteenInfoModel;
import com.mobatia.naisapp.fragments.secondary.model.SecondaryModel;

import java.util.ArrayList;

public class CanteenInfoRecyclerAdapter extends RecyclerView.Adapter<CanteenInfoRecyclerAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<CanteenInfoModel> mnNewsLetterModelArrayList;
    String dept;
    private RelativeLayout statusLayout;
    private TextView status;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageIcon;
        TextView pdfTitle;
        private RelativeLayout statusLayout;
        private TextView status;
        public MyViewHolder(View view) {
            super(view);
            imageIcon = (ImageView) view.findViewById(R.id.imageIcon);
            pdfTitle = (TextView) view.findViewById(R.id.pdfTitle);

            status = (TextView) view.findViewById(R.id.status);
            statusLayout = (RelativeLayout) view.findViewById(R.id.statusLayout);

        }
    }


    public CanteenInfoRecyclerAdapter(Context mContext, ArrayList<CanteenInfoModel> mnNewsLetterModelArrayList) {
        this.mContext = mContext;
        this.mnNewsLetterModelArrayList = mnNewsLetterModelArrayList;

    }

    @Override
    public CanteenInfoRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_pdf_adapter_row_new, parent, false);

        return new CanteenInfoRecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CanteenInfoRecyclerAdapter.MyViewHolder holder, int position) {
//        holder.submenu.setText(mnNewsLetterModelArrayList.get(position).getSubmenu());
        holder.pdfTitle.setText(mnNewsLetterModelArrayList.get(position).getTitle());
        holder.imageIcon.setVisibility(View.GONE);
/*
        if (mnNewsLetterModelArrayList.get(position).getmFile().endsWith(".pdf")) {
            holder.imageIcon.setBackgroundResource(R.drawable.pdfdownloadbutton);
        }
        else
        {
            holder.imageIcon.setBackgroundResource(R.drawable.webcontentviewbutton);

        }*/
//        if(mnNewsLetterModelArrayList.get(position).getStatus().equalsIgnoreCase("0"))
//        {
//            holder.statusLayout.setVisibility(View.VISIBLE);
//            holder.status.setBackgroundResource(R.drawable.rectangle_red);
//            holder.status.setText("New");
//        }
//        else if(mnNewsLetterModelArrayList.get(position).getStatus().equalsIgnoreCase("1")||mnNewsLetterModelArrayList.get(position).getStatus().equalsIgnoreCase(""))
//        {
//            holder.status.setVisibility(View.INVISIBLE);
//
//        }
//        else if(mnNewsLetterModelArrayList.get(position).getStatus().equalsIgnoreCase("2"))
//        {
//            holder.statusLayout.setVisibility(View.VISIBLE);
//            holder.status.setBackgroundResource(R.drawable.rectangle_orange);
//            holder.status.setText("Updated");
//        }
//        else
//        {
//            holder.status.setVisibility(View.GONE);
//
//        }


    }


    @Override
    public int getItemCount() {
        return mnNewsLetterModelArrayList.size();
    }

}

