package com.mobatia.naisapp.activities.cca.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.cca.CCASelectionActivity;
import com.mobatia.naisapp.activities.cca.model.CCAchoiceModel;
import com.mobatia.naisapp.activities.cca.model.WeekListModel;
import com.mobatia.naisapp.appcontroller.AppController;
import com.mobatia.naisapp.manager.AppUtils;

import java.util.ArrayList;

/**
 * Created by gayatri on 22/3/17.
 */
public class CCAsChoiceListActivityAdapter extends RecyclerView.Adapter<CCAsChoiceListActivityAdapter.MyViewHolder> {
    //    ArrayList<String> mSocialMediaModels;
    ArrayList<CCAchoiceModel> mCCAmodelArrayList;
    Context mContext;
    int dayPosition = 0;
    int choicePosition = 0;
    ArrayList<WeekListModel> weekList;
    RecyclerView recyclerWeek;
    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView listTxtView;
        TextView textViewCCAaDateItem;
        ImageView confirmationImageview;
        TextView textViewCCAVenue;
        TextView descriptionTxt;
        TextView readMoreTxt;
        RelativeLayout descriptionRel;

        public MyViewHolder(View view) {
            super(view);

            textViewCCAaDateItem = (TextView) view.findViewById(R.id.textViewCCAaDateItem);
            listTxtView = (TextView) view.findViewById(R.id.textViewCCAaItem);
            textViewCCAVenue = (TextView) view.findViewById(R.id.textViewCCAVenue);
            descriptionTxt = (TextView) view.findViewById(R.id.descriptionTxt);
            readMoreTxt = (TextView) view.findViewById(R.id.readMoreTxt);
            confirmationImageview = (ImageView) view.findViewById(R.id.confirmationImageview);
            descriptionRel = (RelativeLayout) view.findViewById(R.id.descriptionRel);


        }
    }

    public CCAsChoiceListActivityAdapter(Context mContext, ArrayList<CCAchoiceModel> mCCAmodelArrayList) {
        this.mContext = mContext;
        this.mCCAmodelArrayList = mCCAmodelArrayList;
    }

    public CCAsChoiceListActivityAdapter(Context mContext, ArrayList<CCAchoiceModel> mCCAmodelArrayList, int mdayPosition, ArrayList<WeekListModel> mWeekList) {
        this.mContext = mContext;
        this.mCCAmodelArrayList = mCCAmodelArrayList;
        this.dayPosition = mdayPosition;
        this.weekList = mWeekList;

    }

    public CCAsChoiceListActivityAdapter(Context mContext, ArrayList<CCAchoiceModel> mCCAmodelArrayList, int mdayPosition, ArrayList<WeekListModel> mWeekList, int mChoicePosition, RecyclerView recyclerWeek) {
        this.mContext = mContext;
        this.mCCAmodelArrayList = mCCAmodelArrayList;
        this.dayPosition = mdayPosition;
        this.weekList = mWeekList;
        this.choicePosition = mChoicePosition;
        this.recyclerWeek = recyclerWeek;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_ccalist_activity_new, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, @SuppressLint("RecyclerView") int position) {


        holder.confirmationImageview.setVisibility(View.VISIBLE);
        if (mCCAmodelArrayList.get(position).getVenue().equalsIgnoreCase("0")|| (mCCAmodelArrayList.get(position).getVenue().equalsIgnoreCase("")))
        {
            holder.textViewCCAVenue.setVisibility(View.GONE);

        }
        else
        {

            holder.textViewCCAVenue.setText(mCCAmodelArrayList.get(position).getVenue());
            holder.textViewCCAVenue.setVisibility(View.VISIBLE);

        }
        System.out.println("DESC TEST"+mCCAmodelArrayList.get(position).getDescription());

     //   Log.e("DESC ADA",mCCAmodelArrayList.get(position).getDescription());
        if(mCCAmodelArrayList.get(position).getDescription().equalsIgnoreCase("0")|| mCCAmodelArrayList.get(position).getDescription().equalsIgnoreCase(""))
        {
            holder.descriptionRel.setVisibility(View.GONE);
        }
        else
        {
            holder.descriptionRel.setVisibility(View.VISIBLE);
            holder.descriptionTxt.setText("Description : "+mCCAmodelArrayList.get(position).getDescription());
        }
        Integer count=holder.descriptionTxt.getLineCount();
        Log.e("LINE COUNT", String.valueOf(count));

        if(mCCAmodelArrayList.get(position).getDescription().length()>22)
        {
            holder.readMoreTxt.setVisibility(View.GONE);
        }
        else
        {
            holder.readMoreTxt.setVisibility(View.GONE);
        }

        holder.readMoreTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppUtils.showDialogAlertDismiss((Activity) mContext, "Description", mCCAmodelArrayList.get(position).getDescription(), R.drawable.exclamationicon, R.drawable.round);

            }
        });
        if (choicePosition == 0) {

            if (mCCAmodelArrayList.get(position).getDisableCccaiem()) {
                holder.confirmationImageview.setBackgroundResource(R.drawable.disablecrossicon);
                holder.listTxtView.setTextColor(mContext.getResources().getColor(R.color.grey));

            }else if (mCCAmodelArrayList.get(position).getStatus().equalsIgnoreCase("0")) {
                holder.confirmationImageview.setBackgroundResource(R.drawable.close_icon_with_white);
                holder.listTxtView.setTextColor(mContext.getResources().getColor(R.color.black));


            } else {
                holder.confirmationImageview.setBackgroundResource(R.drawable.participatingsmallicon_new);
                holder.listTxtView.setTextColor(mContext.getResources().getColor(R.color.black));
                AppController.weekList.get(dayPosition).setChoiceStatus("1");
                CCASelectionActivity.CCADetailModelArrayList.get(CCASelectionActivity.ccaDetailpos).setChoice1(mCCAmodelArrayList.get(position).getCca_item_name());
                CCASelectionActivity.CCADetailModelArrayList.get(CCASelectionActivity.ccaDetailpos).setChoice1Id(mCCAmodelArrayList.get(position).getCca_details_id());
                CCAsWeekListAdapter mCCAsWeekListAdapter = new CCAsWeekListAdapter(mContext, AppController.weekList, dayPosition);
                mCCAsWeekListAdapter.notifyDataSetChanged();
                recyclerWeek.setAdapter(mCCAsWeekListAdapter);

            }
        } else {

            System.out.println("disable2::"+mCCAmodelArrayList.get(position).getDisableCccaiem()+" @ "+position);
            System.out.println("disable2::"+mCCAmodelArrayList.get(position).getDisableCccaiem()+" @dayPosition: "+dayPosition);
            if (mCCAmodelArrayList.get(position).getDisableCccaiem()) {
                holder.confirmationImageview.setBackgroundResource(R.drawable.disablecrossicon);
                holder.listTxtView.setTextColor(mContext.getResources().getColor(R.color.grey));

            }else if (mCCAmodelArrayList.get(position).getStatus().equalsIgnoreCase("0")) {
                holder.confirmationImageview.setBackgroundResource(R.drawable.close_icon_with_white);
                holder.listTxtView.setTextColor(mContext.getResources().getColor(R.color.black));
            }
            else {
                holder.confirmationImageview.setBackgroundResource(R.drawable.participatingsmallicon_new);
                AppController.weekList.get(dayPosition).setChoiceStatus1("1");
                CCASelectionActivity.CCADetailModelArrayList.get(CCASelectionActivity.ccaDetailpos).setChoice2(mCCAmodelArrayList.get(position).getCca_item_name());
                CCASelectionActivity.CCADetailModelArrayList.get(CCASelectionActivity.ccaDetailpos).setChoice2Id(mCCAmodelArrayList.get(position).getCca_details_id());
                CCAsWeekListAdapter mCCAsWeekListAdapter = new CCAsWeekListAdapter(mContext, AppController.weekList, dayPosition);
                mCCAsWeekListAdapter.notifyDataSetChanged();
                recyclerWeek.setAdapter(mCCAsWeekListAdapter);

            }
        }

        for (int j = 0; j < AppController.weekList.size(); j++) {
            if (AppController.weekList.get(j).getChoiceStatus().equalsIgnoreCase("0") || AppController.weekList.get(j).getChoiceStatus1().equalsIgnoreCase("0")) {
                CCASelectionActivity.filled = false;
                break;
            } else {
                CCASelectionActivity.filled = true;
            }
            if (!( CCASelectionActivity.filled))
            {
                break;

            }

        }
        if (CCASelectionActivity.filled) {
            CCASelectionActivity.submitBtn.getBackground().setAlpha(255);
            CCASelectionActivity.submitBtn.setVisibility(View.VISIBLE);
            CCASelectionActivity.nextBtn.getBackground().setAlpha(255);
            CCASelectionActivity.nextBtn.setVisibility(View.GONE);

        } else {
            CCASelectionActivity.submitBtn.getBackground().setAlpha(150);
            CCASelectionActivity.submitBtn.setVisibility(View.INVISIBLE);
            CCASelectionActivity.nextBtn.getBackground().setAlpha(255);
            CCASelectionActivity.nextBtn.setVisibility(View.VISIBLE);

        }
        holder.listTxtView.setText(mCCAmodelArrayList.get(position).getCca_item_name());
        if (mCCAmodelArrayList.get(position).getCca_item_start_time()!=null && mCCAmodelArrayList.get(position).getCca_item_end_time()!=null)
        {
            holder.textViewCCAaDateItem.setVisibility(View.VISIBLE);

            holder.textViewCCAaDateItem.setText("("+AppUtils.convertTimeToAMPM(mCCAmodelArrayList.get(position).getCca_item_start_time())+" - "+AppUtils.convertTimeToAMPM(mCCAmodelArrayList.get(position).getCca_item_end_time())+")");

        }else if (mCCAmodelArrayList.get(position).getCca_item_start_time()!=null)
        {
            holder.textViewCCAaDateItem.setVisibility(View.VISIBLE);

            holder.textViewCCAaDateItem.setText("("+AppUtils.convertTimeToAMPM(mCCAmodelArrayList.get(position).getCca_item_start_time())+")");
        }else if (mCCAmodelArrayList.get(position).getCca_item_end_time()!=null)
        {
            holder.textViewCCAaDateItem.setVisibility(View.VISIBLE);

            holder.textViewCCAaDateItem.setText("("+AppUtils.convertTimeToAMPM(mCCAmodelArrayList.get(position).getCca_item_end_time())+")");
        }
        else
        {
            holder.textViewCCAaDateItem.setVisibility(View.GONE);

        }


    }


    @Override
    public int getItemCount() {

        return mCCAmodelArrayList.size();
    }
}
