package com.mobatia.naisapp.activities.parentsassociation.adapter;

import android.content.Context;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.parentsassociation.model.ParentAssociationEventsModel;

import java.util.ArrayList;

/**
 * Created by Rijo on 17/1/17.
 */
public class ParentsAssociationItemNameRecyclerviewAdapter extends RecyclerView.Adapter<ParentsAssociationItemNameRecyclerviewAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<ParentAssociationEventsModel> mParentAssociationEventsModelArrayList;
    String photo_id = "-1";
    String startTime = "";
    boolean startTimeAm = true;
    boolean endTimeAm = true;
    String endTime = "";
    private SparseBooleanArray selectedItems;
    int pos=0;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        TextView eventDate;
        RelativeLayout gridClickRelative;
         CardView card_view;
        public MyViewHolder(View view) {
            super(view);

//            photoImageView = (ImageView) view.findViewById(R.id.imgView);
            itemName = (TextView) view.findViewById(R.id.itemName);
            gridClickRelative = (RelativeLayout) view.findViewById(R.id.gridClickRelative);
            card_view = (CardView) view.findViewById(R.id.card_view);

        }
    }


    public ParentsAssociationItemNameRecyclerviewAdapter(Context mContext, ArrayList<ParentAssociationEventsModel> mParentAssociationEventsModelArrayList) {
        this.mContext = mContext;
        this.mParentAssociationEventsModelArrayList = mParentAssociationEventsModelArrayList;
        this.pos=0;

    }
    public ParentsAssociationItemNameRecyclerviewAdapter(Context mContext, ArrayList<ParentAssociationEventsModel> mParentAssociationEventsModelArrayList,int mPos) {
        this.mContext = mContext;
        this.mParentAssociationEventsModelArrayList = mParentAssociationEventsModelArrayList;
        this.pos=mPos;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.parents_association_itemname_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
//        holder.phototakenDate.setText(mPhotosModelArrayList.get(position).getMonth() + " " + mPhotosModelArrayList.get(position).getDay() + "," + mPhotosModelArrayList.get(position).getYear());

holder.itemName.setText(mParentAssociationEventsModelArrayList.get(position).getItemName());



//        if (position==pos)

//        if (mParentAssociationEventsModelArrayList.get(position).isItemSelected())
//        {
//            holder.gridClickRelative.setBackgroundResource(R.drawable.curve_filled_pta_selected);
//        }
//        else
//        {
//            holder.gridClickRelative.setBackgroundResource(R.drawable.curved_filled_layout_parents_association);
//        }
        if (position == pos)
        {
            holder.gridClickRelative.setBackgroundResource(R.drawable.curve_filled_pta_selected);
//            holder.gridClickRelative.getBackground().setAlpha(150);
        }
        else
        {
            holder.gridClickRelative.setBackgroundResource(R.drawable.curved_filled_layout_parents_association);
        }

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
