package com.mobatia.naisapp.activities.parentsassociation.adapter;

import android.content.Context;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.parentsassociation.model.ParentAssociationEventItemsModel;
import com.mobatia.naisapp.manager.AppUtils;

import java.util.ArrayList;

/**
 * Created by Rijo on 17/1/17.
 */
public class ParentsAssociationTimeSlotRecyclerviewAdapter extends RecyclerView.Adapter<ParentsAssociationTimeSlotRecyclerviewAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<ParentAssociationEventItemsModel> mParentAssociationEventsModelArrayList;
    String photo_id = "-1";
    String startTime = "";
    boolean startTimeAm = true;
    boolean endTimeAm = true;
    String endTime = "";
    int pos;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        TextView timeFrom;
        TextView timeTo;
        TextView textViewTo;
        RelativeLayout gridClickRelative;
        LinearLayout mLinearLayout;
         CardView card_view;

        public MyViewHolder(View view) {
            super(view);

//            photoImageView = (ImageView) view.findViewById(R.id.imgView);
            userName = (TextView) view.findViewById(R.id.userName);
            timeFrom = (TextView) view.findViewById(R.id.timeFrom);
            timeTo = (TextView) view.findViewById(R.id.timeTo);
            textViewTo = (TextView) view.findViewById(R.id.textViewTo);
            gridClickRelative = (RelativeLayout) view.findViewById(R.id.gridClickRelative);
            mLinearLayout = (LinearLayout) view.findViewById(R.id.mLinearLayout);
            card_view = (CardView) view.findViewById(R.id.card_view);

        }
    }


    public ParentsAssociationTimeSlotRecyclerviewAdapter(Context mContext, ArrayList<ParentAssociationEventItemsModel> mParentAssociationEventsModelArrayList) {
        this.mContext = mContext;
        this.mParentAssociationEventsModelArrayList = mParentAssociationEventsModelArrayList;
    }
    public ParentsAssociationTimeSlotRecyclerviewAdapter(Context mContext, ArrayList<ParentAssociationEventItemsModel> mParentAssociationEventsModelArrayList, int pos) {
        this.mContext = mContext;
        this.mParentAssociationEventsModelArrayList = mParentAssociationEventsModelArrayList;
        this.pos = pos;
    }

    public ParentsAssociationTimeSlotRecyclerviewAdapter(Context mContext, ArrayList<ParentAssociationEventItemsModel> mParentAssociationEventsModelArrayList, String photo_id) {
        this.mContext = mContext;
        this.mParentAssociationEventsModelArrayList = mParentAssociationEventsModelArrayList;
        this.photo_id = photo_id;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.time_slot_parents_association_adapter, parent, false);
        itemView.setId(pos);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
//        holder.phototakenDate.setText(mPhotosModelArrayList.get(position).getMonth() + " " + mPhotosModelArrayList.get(position).getDay() + "," + mPhotosModelArrayList.get(position).getYear());

       System.out.println("Time:::"+mParentAssociationEventsModelArrayList.get(position).getTo_time());
        if (mParentAssociationEventsModelArrayList.get(position).getTo_time().contains("a.m.")) {
            endTime=AppUtils.replaceamdot(mParentAssociationEventsModelArrayList.get(position).getTo_time()).trim();
            endTime+=" am";

            endTimeAm=true;

        } else  if (mParentAssociationEventsModelArrayList.get(position).getTo_time().contains("p.m.")){
            endTime=AppUtils.replacepmdot(mParentAssociationEventsModelArrayList.get(position).getTo_time()).trim();
            endTime+=" pm";
            endTimeAm=false;

        }
        else
        {
            endTime=    mParentAssociationEventsModelArrayList.get(position).getTo_time();

        }
         if (mParentAssociationEventsModelArrayList.get(position).getFrom_time().contains("a.m.")) {
            startTime=AppUtils.replaceamdot(mParentAssociationEventsModelArrayList.get(position).getFrom_time()).trim();
            startTime+=" am";

            endTimeAm=true;

        } else  if (mParentAssociationEventsModelArrayList.get(position).getFrom_time().contains("p.m.")){
            startTime=AppUtils.replacepmdot(mParentAssociationEventsModelArrayList.get(position).getFrom_time()).trim();
            startTime+=" pm";
            endTimeAm=false;

        }
        else
         {
             startTime=   mParentAssociationEventsModelArrayList.get(position).getFrom_time();

         }
holder.userName.setText(mParentAssociationEventsModelArrayList.get(position).getUserName());
holder.timeTo.setText(endTime);
holder.timeFrom.setText(startTime);

        if (mParentAssociationEventsModelArrayList.get(position).getUserName().equalsIgnoreCase(""))
        {
            RelativeLayout.LayoutParams layoutParams =
                    (RelativeLayout.LayoutParams)holder.mLinearLayout.getLayoutParams();
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            holder.mLinearLayout.setLayoutParams(layoutParams);
        }
        else
        {
            RelativeLayout.LayoutParams layoutParams =
                    (RelativeLayout.LayoutParams)holder.mLinearLayout.getLayoutParams();
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
            holder.mLinearLayout.setLayoutParams(layoutParams);
        }

        if (mParentAssociationEventsModelArrayList.get(position).getStatus().equalsIgnoreCase("0"))
        {
            holder.gridClickRelative.setBackgroundResource(R.drawable.time_curved_rel_layout);
            holder.textViewTo.setTextColor(mContext.getResources().getColor(R.color.black));

//            holder.textViewTo.setTextColor(mContext.getResources().getColor(R.color.split_bg));
        }
        else if (mParentAssociationEventsModelArrayList.get(position).getStatus().equalsIgnoreCase("1"))
        {
            holder.timeFrom.setTextColor(mContext.getResources().getColor(R.color.white));
            holder.timeTo.setTextColor(mContext.getResources().getColor(R.color.white));
            holder.textViewTo.setTextColor(mContext.getResources().getColor(R.color.white));
            holder.userName.setTextColor(mContext.getResources().getColor(R.color.white));

            holder.gridClickRelative.setBackgroundResource(R.drawable.parent_slot_new);
//            holder.textViewTo.setTextColor(mContext.getResources().getColor(R.color.black));

        }
        else if (mParentAssociationEventsModelArrayList.get(position).getStatus().equalsIgnoreCase("2"))
        {
            holder.gridClickRelative.setBackgroundResource(R.drawable.slotbooked_curved_rel_layout);
            holder.textViewTo.setTextColor(mContext.getResources().getColor(R.color.black));

//            holder.textViewTo.setTextColor(mContext.getResources().getColor(R.color.split_bg));


        }


//        if (startTimeAm) {
//            holder.timeFrom.setText(startTime+"\nam");
//        }
//        else
//        {
//            holder.timeFrom.setText(startTime+"\npm");
//
//        }
//        if (endTimeAm) {
//
//            holder.timeTo.setText(endTime+"\nam");
//        }
//        else
//        {
//            holder.timeTo.setText(endTime+"\npm");
//
//        }

    }

    @Override
    public int getItemCount() {
        return mParentAssociationEventsModelArrayList.size();
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
