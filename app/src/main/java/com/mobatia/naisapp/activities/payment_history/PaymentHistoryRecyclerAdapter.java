package com.mobatia.naisapp.activities.payment_history;

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

public class PaymentHistoryRecyclerAdapter extends RecyclerView.Adapter<PaymentHistoryRecyclerAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<PaymentWalletHistoryModel> mnNewsLetterModelArrayList;
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


    public PaymentHistoryRecyclerAdapter(Context mContext, ArrayList<PaymentWalletHistoryModel> mnNewsLetterModelArrayList) {
        this.mContext = mContext;
        this.mnNewsLetterModelArrayList = mnNewsLetterModelArrayList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.payment_wallet_history_recycler_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.pdfTitle.setText("Paid by " + mnNewsLetterModelArrayList.get(position).getName());
        // holder.imageIcon.setVisibility(View.GONE);
        holder.tripsDateTxt.setText(AppUtils.dateConversionYY(mnNewsLetterModelArrayList.get(position).getDate_time()));
        holder.tripsAmountTxt.setText("Credit "+mnNewsLetterModelArrayList.get(position).getAmount() + " " + "AED");
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
        if (mnNewsLetterModelArrayList.get(position).getStatus().equalsIgnoreCase("0")) {
            holder.status.setText("(Pending)");

        } else {
            holder.status.setText(" ");
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
