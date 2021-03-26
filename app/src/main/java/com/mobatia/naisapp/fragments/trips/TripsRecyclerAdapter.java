package com.mobatia.naisapp.fragments.trips;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.manager.AppUtils;

import java.util.ArrayList;

public class TripsRecyclerAdapter extends RecyclerView.Adapter<TripsRecyclerAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<TripListModel> mnNewsLetterModelArrayList;
    String dept;


    public class MyViewHolder extends RecyclerView.ViewHolder {
      //  ImageView imageIcon;

        TextView pdfTitle;
        TextView tripsDateTxt;
        TextView tripsAmountTxt;
        RelativeLayout mainRelative;
        TextView status;
        RelativeLayout statusLayout;

        public MyViewHolder(View view) {
            super(view);

          //  imageIcon = (ImageView) view.findViewById(R.id.imageIcon);
            pdfTitle = (TextView) view.findViewById(R.id.pdfTitle);
            tripsDateTxt = (TextView) view.findViewById(R.id.tripsDateTxt);
            tripsAmountTxt = (TextView) view.findViewById(R.id.tripsAmountTxt);
            mainRelative = (RelativeLayout) view.findViewById(R.id.mainRelative);
            status = view.findViewById(R.id.status);
            statusLayout = view.findViewById(R.id.statusLayout);

        }
    }


    public TripsRecyclerAdapter(Context mContext, ArrayList<TripListModel> mnNewsLetterModelArrayList) {
        this.mContext = mContext;
        this.mnNewsLetterModelArrayList = mnNewsLetterModelArrayList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trips_recycler_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.pdfTitle.setText(mnNewsLetterModelArrayList.get(position).getTitle());
        // holder.imageIcon.setVisibility(View.GONE);
        holder.tripsDateTxt.setText(AppUtils.dateParsingTodd_MMM_yyyy(mnNewsLetterModelArrayList.get(position).getTrip_date()));
        holder.tripsAmountTxt.setText(mnNewsLetterModelArrayList.get(position).getAmount() + " " + "AED");
      /*  if (mnNewsLetterModelArrayList.get(position).getCompleted_date().equalsIgnoreCase("") && mnNewsLetterModelArrayList.get(position).getLast_payment_status().equalsIgnoreCase("0")) {
            holder.mainRelative.setBackgroundColor(mContext.getResources().getColor(R.color.term_button_bg));
        } else if (mnNewsLetterModelArrayList.get(position).getCompleted_date().equalsIgnoreCase("") && mnNewsLetterModelArrayList.get(position).getLast_payment_status().equalsIgnoreCase("1")) {
            holder.mainRelative.setBackgroundColor(mContext.getResources().getColor(R.color.rel_nine));
        } else if (!(mnNewsLetterModelArrayList.get(position).getCompleted_date().equalsIgnoreCase("")) && mnNewsLetterModelArrayList.get(position).getLast_payment_status().equalsIgnoreCase("0")) {
            holder.mainRelative.setBackgroundColor(mContext.getResources().getColor(R.color.trip_green));
        } else if (!(mnNewsLetterModelArrayList.get(position).getCompleted_date().equalsIgnoreCase("")) && mnNewsLetterModelArrayList.get(position).getLast_payment_status().equalsIgnoreCase("1")) {
            holder.mainRelative.setBackgroundColor(mContext.getResources().getColor(R.color.trip_green));
        } else {
            holder.mainRelative.setBackgroundColor(mContext.getResources().getColor(R.color.term_button_bg));
        }*/
      if (mnNewsLetterModelArrayList.get(position).getPayment_date_status().equalsIgnoreCase("0"))
      {
          holder.mainRelative.setBackgroundColor(mContext.getResources().getColor(R.color.list_bg));

      }
      else {
          if (mnNewsLetterModelArrayList.get(position).getPayment_status().equalsIgnoreCase("0"))
          {
              holder.mainRelative.setBackgroundColor(mContext.getResources().getColor(R.color.rel_nine));
          }
          else {
              holder.mainRelative.setBackgroundColor(mContext.getResources().getColor(R.color.list_bg));

          }

      }
        if (mnNewsLetterModelArrayList.get(position).getStatus().equalsIgnoreCase("0")) {
           // holder.imageIcon.setVisibility(View.VISIBLE);
            //holder.imageIcon.setBackgroundResource(R.drawable.shape_circle_red);
            holder.statusLayout.setVisibility(View.VISIBLE);
            holder.status.setBackgroundResource(R.drawable.rectangle_red);
            holder.status.setText("New");
        } else if (mnNewsLetterModelArrayList.get(position).getStatus().equalsIgnoreCase("1") || mnNewsLetterModelArrayList.get(position).getStatus().equalsIgnoreCase("")) {
            //holder.imageIcon.setVisibility(View.GONE);
            holder.statusLayout.setVisibility(View.GONE);

        } else if (mnNewsLetterModelArrayList.get(position).getStatus().equalsIgnoreCase("2")) {
           // holder.imageIcon.setVisibility(View.VISIBLE);
           // holder.imageIcon.setBackgroundResource(R.drawable.shape_circle_navy);
            holder.statusLayout.setVisibility(View.VISIBLE);
            holder.status.setBackgroundResource(R.drawable.rectangle_orange);
            holder.status.setText("Updated");
        }

    }
    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        return mnNewsLetterModelArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
