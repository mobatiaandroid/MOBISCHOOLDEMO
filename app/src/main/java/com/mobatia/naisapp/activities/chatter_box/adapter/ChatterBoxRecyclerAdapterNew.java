package com.mobatia.naisapp.activities.chatter_box.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.term_calendar.model.TermsCalendarModel;

import java.util.ArrayList;

/**
 * Created by gayatri on 23/3/17.
 */
public class ChatterBoxRecyclerAdapterNew extends RecyclerView.Adapter<ChatterBoxRecyclerAdapterNew.MyViewHolder> {

    private Context mContext;
    private ArrayList<TermsCalendarModel> mnNewsLetterModelArrayList;
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


    public ChatterBoxRecyclerAdapterNew(Context mContext, ArrayList<TermsCalendarModel> mnNewsLetterModelArrayList) {
        this.mContext = mContext;
        this.mnNewsLetterModelArrayList = mnNewsLetterModelArrayList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_pdf_adapter_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.pdfTitle.setText(mnNewsLetterModelArrayList.get(position).getmTitle());
        holder.imageIcon.setVisibility(View.GONE);


    }


    @Override
    public int getItemCount() {
        return mnNewsLetterModelArrayList.size();
    }

}
