package com.mobatia.naisapp.activities.trips.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.parentsassociation.model.ParentAssociationEventsModel;

import java.util.ArrayList;

public class PaymentInformationAdapter extends RecyclerView.Adapter<PaymentInformationAdapter.MyViewHolder> {

    private Context mContext;
    String dept;
    private RelativeLayout statusLayout;
    private TextView status;
   private ArrayList<ParentAssociationEventsModel> mListViewArray;
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


    public PaymentInformationAdapter (Context mContext, ArrayList<ParentAssociationEventsModel> mListViewArray) {
        this.mContext = mContext;
        this.mListViewArray = mListViewArray;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_pdf_adapter_row_new, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
//        holder.submenu.setText(mnNewsLetterModelArrayList.get(position).getSubmenu());
        holder.pdfTitle.setText(mListViewArray.get(position).getPdfTitle());
        holder.imageIcon.setVisibility(View.GONE);
/*
        if (mnNewsLetterModelArrayList.get(position).getmFile().endsWith(".pdf")) {
            holder.imageIcon.setBackgroundResource(R.drawable.pdfdownloadbutton);
        }
        else
        {
            holder.imageIcon.setBackgroundResource(R.drawable.webcontentviewbutton);

        }*/



    }


    @Override
    public int getItemCount() {
        return mListViewArray.size();
    }

}
