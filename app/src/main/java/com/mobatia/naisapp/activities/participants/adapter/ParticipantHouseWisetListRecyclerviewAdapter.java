package com.mobatia.naisapp.activities.participants.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.fragments.parents_evening.model.StudentModel;
import com.mobatia.naisapp.manager.AppUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Rijo on 17/1/17.
 */
public class ParticipantHouseWisetListRecyclerviewAdapter extends RecyclerView.Adapter<ParticipantHouseWisetListRecyclerviewAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<StudentModel> mCalendarEventsModelArrayList;

    String houseName = "";

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView studentNameTextView;
        ImageView studentInfoImg;
        ImageView studentImage;
        LinearLayout linearProfile;

        public MyViewHolder(View view) {
            super(view);

            studentNameTextView = (TextView) view.findViewById(R.id.studentNameTextView);
            studentInfoImg = (ImageView) view.findViewById(R.id.studentInfoImg);
            studentImage = (ImageView) view.findViewById(R.id.studentImage);
            linearProfile = (LinearLayout) view.findViewById(R.id.linearProfile);


        }
    }


    public ParticipantHouseWisetListRecyclerviewAdapter(Context mContext, ArrayList<StudentModel> mCalendarEventsModelArrayList) {
        this.mContext = mContext;
        this.mCalendarEventsModelArrayList = mCalendarEventsModelArrayList;
    }


    public ParticipantHouseWisetListRecyclerviewAdapter(Context mContext, ArrayList<StudentModel> mCalendarEventsModelArrayList, String houseName) {
        this.mContext = mContext;
        this.mCalendarEventsModelArrayList = mCalendarEventsModelArrayList;
        this.houseName = houseName;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.participnt_house_recycler_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
//        holder.phototakenDate.setText(mPhotosModelArrayList.get(position).getMonth() + " " + mPhotosModelArrayList.get(position).getDay() + "," + mPhotosModelArrayList.get(position).getYear());

        holder.studentNameTextView.setText(mCalendarEventsModelArrayList.get(position).getmName());

        if (mCalendarEventsModelArrayList.get(position).getGoingStatus().equalsIgnoreCase("0")) {
            holder.studentInfoImg.setImageResource(R.drawable.notparticipatingsmallicon);
            holder.studentInfoImg.setVisibility(View.VISIBLE);
        } else if (mCalendarEventsModelArrayList.get(position).getGoingStatus().equalsIgnoreCase("1")) {
            holder.studentInfoImg.setImageResource(R.drawable.participatingsmallicon);
            holder.studentInfoImg.setVisibility(View.VISIBLE);
        } else if (mCalendarEventsModelArrayList.get(position).getGoingStatus().equalsIgnoreCase("2")) {
            holder.studentInfoImg.setImageResource(R.drawable.doubtnparticipatingsmallicon);
            holder.studentInfoImg.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.studentInfoImg.setVisibility(View.GONE);

        }

        if (!mCalendarEventsModelArrayList.get(position).getmPhoto().equals("")) {

            Picasso.with(mContext).load(AppUtils.replace(mCalendarEventsModelArrayList.get(position).getmPhoto().toString())).error(R.drawable.student).placeholder(R.drawable.student).fit().into(holder.studentImage);//rijo edited
        } else

        {

            holder.studentImage.setImageResource(R.drawable.student);
        }
        holder.studentInfoImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCalendarEventsModelArrayList.get(position).getGoingStatus().equalsIgnoreCase("0")) {
                    Toast.makeText(mContext, "Not Going", Toast.LENGTH_SHORT).show();
                } else if (mCalendarEventsModelArrayList.get(position).getGoingStatus().equalsIgnoreCase("1")) {
                    Toast.makeText(mContext, "Going", Toast.LENGTH_SHORT).show();
                } else if (mCalendarEventsModelArrayList.get(position).getGoingStatus().equalsIgnoreCase("2")) {
                    Toast.makeText(mContext, "Not Sure", Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.linearProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(mContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialogue_participant_detail);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Button dialogDismiss = (Button) dialog.findViewById(R.id.btn_dismiss);
//        ImageView iconImageView = (ImageView) dialog.findViewById(R.id.iconImageView);
                TextView studentNameTV = (TextView) dialog.findViewById(R.id.studentNameTV);
                TextView studentClassTV = (TextView) dialog.findViewById(R.id.studentClassTV);
                TextView studentSectionTV = (TextView) dialog.findViewById(R.id.studentSectionTV);
                TextView houseNameTV = (TextView) dialog.findViewById(R.id.houseNameTV);
                ImageView studentInfo = (ImageView) dialog.findViewById(R.id.studentInfo);
                ImageView iconImageView = (ImageView) dialog.findViewById(R.id.iconImageView);
                if (mCalendarEventsModelArrayList.get(position).getGoingStatus().equalsIgnoreCase("0")) {
                    studentInfo.setImageResource(R.drawable.notparticipatingsmallicon);
                    studentInfo.setVisibility(View.VISIBLE);
                } else if (mCalendarEventsModelArrayList.get(position).getGoingStatus().equalsIgnoreCase("1")) {
                    studentInfo.setImageResource(R.drawable.participatingsmallicon);
                    studentInfo.setVisibility(View.VISIBLE);
                } else if (mCalendarEventsModelArrayList.get(position).getGoingStatus().equalsIgnoreCase("2")) {
                    studentInfo.setImageResource(R.drawable.doubtnparticipatingsmallicon);
                    studentInfo.setVisibility(View.VISIBLE);
                }
                else {
                    studentInfo.setVisibility(View.GONE);
                }
                if (!mCalendarEventsModelArrayList.get(position).getmPhoto().equals("")) {

                    Picasso.with(mContext).load(AppUtils.replace(mCalendarEventsModelArrayList.get(position).getmPhoto().toString())).error(R.drawable.student).placeholder(R.drawable.student).fit().into(iconImageView);//rijo edited
                } else

                {

                    iconImageView.setImageResource(R.drawable.student);
                }
                studentNameTV.setText(mCalendarEventsModelArrayList.get(position).getmName());
                studentClassTV.setText(mCalendarEventsModelArrayList.get(position).getmClass());
                studentSectionTV.setText(mCalendarEventsModelArrayList.get(position).getmSection());
                houseNameTV.setText(houseName);
                dialogDismiss.setOnClickListener(new View.OnClickListener() {

                    @Override

                    public void onClick(View v) {

                        dialog.dismiss();

                    }

                });


                dialog.show();


            }
        });

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
