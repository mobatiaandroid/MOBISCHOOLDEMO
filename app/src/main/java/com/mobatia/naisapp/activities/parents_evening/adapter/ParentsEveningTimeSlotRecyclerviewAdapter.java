package com.mobatia.naisapp.activities.parents_evening.adapter;

import android.content.Context;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.calender.model.CalendarEventsModel;
import com.mobatia.naisapp.manager.AppUtils;

import java.util.ArrayList;

/**
 * Created by Rijo on 17/1/17.
 */
public class ParentsEveningTimeSlotRecyclerviewAdapter extends RecyclerView.Adapter<ParentsEveningTimeSlotRecyclerviewAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<CalendarEventsModel> mCalendarEventsModelArrayList;
    String photo_id = "-1";
    String startTime = "";
    boolean startTimeAm = true;
    boolean endTimeAm = true;
    String endTime = "";

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView photoImageView;
        TextView timeTo;
        TextView timeFrom;
        TextView textViewTo;
        LinearLayout gridClickRelative;
         CardView card_view;
        public MyViewHolder(View view) {
            super(view);

//            photoImageView = (ImageView) view.findViewById(R.id.imgView);
            timeTo = (TextView) view.findViewById(R.id.timeTo);
            timeFrom = (TextView) view.findViewById(R.id.timeFrom);
            textViewTo = (TextView) view.findViewById(R.id.textViewTo);
            gridClickRelative = (LinearLayout) view.findViewById(R.id.gridClickRelative);
            card_view = (CardView) view.findViewById(R.id.card_view);

        }
    }


    public ParentsEveningTimeSlotRecyclerviewAdapter(Context mContext, ArrayList<CalendarEventsModel> mCalendarEventsModelArrayList) {
        this.mContext = mContext;
        this.mCalendarEventsModelArrayList = mCalendarEventsModelArrayList;
    }

    public ParentsEveningTimeSlotRecyclerviewAdapter(Context mContext, ArrayList<CalendarEventsModel> mCalendarEventsModelArrayList, String photo_id) {
        this.mContext = mContext;
        this.mCalendarEventsModelArrayList = mCalendarEventsModelArrayList;
        this.photo_id = photo_id;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.time_slot_parents_evening_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
//        holder.phototakenDate.setText(mPhotosModelArrayList.get(position).getMonth() + " " + mPhotosModelArrayList.get(position).getDay() + "," + mPhotosModelArrayList.get(position).getYear());
     System.out.println("time:"+mCalendarEventsModelArrayList.get(position).getStartTime());
        if (mCalendarEventsModelArrayList.get(position).getStartTime().contains("am")) {
            startTime=AppUtils.replaceam(mCalendarEventsModelArrayList.get(position).getStartTime()).trim();
            startTimeAm=true;
        } else if (mCalendarEventsModelArrayList.get(position).getStartTime().contains("pm"))  {
            startTime=AppUtils.replacepm(mCalendarEventsModelArrayList.get(position).getStartTime()).trim();
            startTimeAm=false;

        }
        else  if (mCalendarEventsModelArrayList.get(position).getStartTime().contains("AM")) {
            startTime=AppUtils.replaceAM(mCalendarEventsModelArrayList.get(position).getStartTime()).trim();
            startTimeAm=true;
        } else if (mCalendarEventsModelArrayList.get(position).getStartTime().contains("PM"))  {
            startTime=AppUtils.replacePM(mCalendarEventsModelArrayList.get(position).getStartTime()).trim();
            startTimeAm=false;

        }else  if (mCalendarEventsModelArrayList.get(position).getStartTime().contains("A.M.")) {
            startTime=AppUtils.replaceAMDot(mCalendarEventsModelArrayList.get(position).getStartTime()).trim();
            startTimeAm=true;

        } else  if (mCalendarEventsModelArrayList.get(position).getStartTime().contains("P.M.")){
            startTime=AppUtils.replacePMDot(mCalendarEventsModelArrayList.get(position).getStartTime()).trim();
            startTimeAm=false;

        }
        else if (mCalendarEventsModelArrayList.get(position).getStartTime().contains("a.m.")) {
            startTime=AppUtils.replaceamdot(mCalendarEventsModelArrayList.get(position).getStartTime()).trim();
            startTimeAm=true;

        } else  if (mCalendarEventsModelArrayList.get(position).getStartTime().contains("p.m.")){
            startTime=AppUtils.replacepmdot(mCalendarEventsModelArrayList.get(position).getStartTime()).trim();
            startTimeAm=false;

        }

        //===starttime
        if (mCalendarEventsModelArrayList.get(position).getEndTime().contains("am")) {
            endTime=AppUtils.replaceam(mCalendarEventsModelArrayList.get(position).getEndTime()).trim();
            endTimeAm=true;

        } else  if (mCalendarEventsModelArrayList.get(position).getEndTime().contains("pm")){
            endTime=AppUtils.replacepm(mCalendarEventsModelArrayList.get(position).getEndTime()).trim();
            endTimeAm=false;

        }
        else if (mCalendarEventsModelArrayList.get(position).getEndTime().contains("AM")) {
            endTime=AppUtils.replaceAM(mCalendarEventsModelArrayList.get(position).getEndTime()).trim();
            endTimeAm=true;

        } else  if (mCalendarEventsModelArrayList.get(position).getEndTime().contains("PM")){
            endTime=AppUtils.replacePM(mCalendarEventsModelArrayList.get(position).getEndTime()).trim();
            endTimeAm=false;

        }
        else if (mCalendarEventsModelArrayList.get(position).getEndTime().contains("A.M.")) {
            endTime=AppUtils.replaceAMDot(mCalendarEventsModelArrayList.get(position).getEndTime()).trim();
            endTimeAm=true;

        } else  if (mCalendarEventsModelArrayList.get(position).getEndTime().contains("P.M.")){
            endTime=AppUtils.replacePMDot(mCalendarEventsModelArrayList.get(position).getEndTime()).trim();
            endTimeAm=false;

        }
        else if (mCalendarEventsModelArrayList.get(position).getEndTime().contains("a.m.")) {
            endTime=AppUtils.replaceamdot(mCalendarEventsModelArrayList.get(position).getEndTime()).trim();
            endTimeAm=true;

        } else  if (mCalendarEventsModelArrayList.get(position).getEndTime().contains("p.m.")){
            endTime=AppUtils.replacepmdot(mCalendarEventsModelArrayList.get(position).getEndTime()).trim();
            endTimeAm=false;

        }

        if (mCalendarEventsModelArrayList.get(position).getStatus().equalsIgnoreCase("0"))
        {
            holder.gridClickRelative.setBackgroundResource(R.drawable.time_curved_rel_layout);
//            holder.textViewTo.setTextColor(mContext.getResources().getColor(R.color.split_bg));
            holder.textViewTo.setTextColor(mContext.getResources().getColor(R.color.black));
            holder.timeTo.setTextColor(mContext.getResources().getColor(R.color.black));
            holder.timeFrom.setTextColor(mContext.getResources().getColor(R.color.black));
        }
        else if (mCalendarEventsModelArrayList.get(position).getStatus().equalsIgnoreCase("1"))
        {
            holder.gridClickRelative.setBackgroundResource(R.drawable.slotbooked_curved_rel_layout);
//            holder.textViewTo.setTextColor(mContext.getResources().getColor(R.color.split_bg));
            holder.textViewTo.setTextColor(mContext.getResources().getColor(R.color.black));
            holder.timeTo.setTextColor(mContext.getResources().getColor(R.color.black));
            holder.timeFrom.setTextColor(mContext.getResources().getColor(R.color.black));
        }
        else if (mCalendarEventsModelArrayList.get(position).getStatus().equalsIgnoreCase("2"))
        {
            holder.gridClickRelative.setBackgroundResource(R.drawable.slotbookedbyuser_curved_rel_layout);
//            holder.textViewTo.setTextColor(mContext.getResources().getColor(R.color.black));
            holder.textViewTo.setTextColor(mContext.getResources().getColor(R.color.black));
            holder.timeTo.setTextColor(mContext.getResources().getColor(R.color.black));
            holder.timeFrom.setTextColor(mContext.getResources().getColor(R.color.black));

        }
        else if (mCalendarEventsModelArrayList.get(position).getStatus().equalsIgnoreCase("3"))
        {
            holder.gridClickRelative.setBackgroundResource(R.drawable.parent_slot_new);
            holder.textViewTo.setTextColor(mContext.getResources().getColor(R.color.white));
            holder.timeTo.setTextColor(mContext.getResources().getColor(R.color.white));
            holder.timeFrom.setTextColor(mContext.getResources().getColor(R.color.white));
        }


        if (startTimeAm) {
            holder.timeFrom.setText(startTime+"\nAM");
        }
        else
        {
            holder.timeFrom.setText(startTime+"\nPM");

        }
        if (endTimeAm) {

            holder.timeTo.setText(endTime+"\nAM");
        }
        else
        {
            holder.timeTo.setText(endTime+"\nPM");

        }


    }

    @Override
    public int getItemCount() {
        return mCalendarEventsModelArrayList.size();
    }
//    public void didTapButton(View view) {
////        ImageView button = (ImageView)view.findViewById(R.id.imgView);
////        final Animation myAnim = AnimationUtils.loadAnimation(mContext, R.anim.bounce);
////        button.startAnimation(myAnim);
//        final Animation myAnim = AnimationUtils.loadAnimation(mContext, R.anim.bounce);
//
//        // Use bounce interpolator with amplitude 0.2 and frequency 20
//        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.6, 20);
//        myAnim.setInterpolator(interpolator);
//
//        view.startAnimation(myAnim);
//    }
//    @Override
//    public void onViewDetachedFromWindow(MyViewHolder holder) {
//        super.onViewDetachedFromWindow(holder);
//        holder.photoImageView.clearAnimation();
//    }
}
