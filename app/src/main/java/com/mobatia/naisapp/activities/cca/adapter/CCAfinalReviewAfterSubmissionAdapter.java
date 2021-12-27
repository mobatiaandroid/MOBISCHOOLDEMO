package com.mobatia.naisapp.activities.cca.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.cca.CCAsReviewAfterSubmissionActivity;
import com.mobatia.naisapp.activities.cca.model.CCAReviewAfterSubmissionModel;
import com.mobatia.naisapp.manager.AppUtils;

import java.util.ArrayList;

/**
 * Created by gayatri on 22/3/17.
 */
public class CCAfinalReviewAfterSubmissionAdapter extends RecyclerView.Adapter<CCAfinalReviewAfterSubmissionAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<CCAReviewAfterSubmissionModel> mCCADetailModelArrayList;
    Dialog dialog;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCCADay;
        TextView textViewCCAChoice1;
        TextView textViewCCAChoice2;
        ImageView attendanceListIcon;
        ImageView deleteChoice1;
        ImageView deleteChoice2;
        LinearLayout linearChoice1, linearChoice2;
        TextView textViewCCAaDateItemChoice1;
        TextView textViewCCAaDateItemChoice2;
        TextView locationTxt;
        TextView descriptionTxt;
        TextView location2Txt;
        TextView description2Txt;
        TextView readMore,readMore1;
        public MyViewHolder(View view) {
            super(view);
            textViewCCAaDateItemChoice1 = (TextView) view.findViewById(R.id.textViewCCAaDateItemChoice1);
            textViewCCAaDateItemChoice2 = (TextView) view.findViewById(R.id.textViewCCAaDateItemChoice2);
            textViewCCADay = (TextView) view.findViewById(R.id.textViewCCADay);
            textViewCCAChoice1 = (TextView) view.findViewById(R.id.textViewCCAChoice1);
            textViewCCAChoice2 = (TextView) view.findViewById(R.id.textViewCCAChoice2);
            attendanceListIcon = (ImageView) view.findViewById(R.id.attendanceListIcon);
            deleteChoice1 = (ImageView) view.findViewById(R.id.deleteChoice1);
            deleteChoice2 = (ImageView) view.findViewById(R.id.deleteChoice2);
            linearChoice1 = (LinearLayout) view.findViewById(R.id.linearChoice1);
            linearChoice2 = (LinearLayout) view.findViewById(R.id.linearChoice2);
            locationTxt = (TextView) view.findViewById(R.id.locationTxt);
            descriptionTxt = (TextView) view.findViewById(R.id.descriptionTxt);
            description2Txt = (TextView) view.findViewById(R.id.description2Txt);
            location2Txt = (TextView) view.findViewById(R.id.location2Txt);
            readMore = (TextView) view.findViewById(R.id.readMore);
            readMore1 = (TextView) view.findViewById(R.id.readMore1);
            }
    }
    public CCAfinalReviewAfterSubmissionAdapter(Context mContext, ArrayList<CCAReviewAfterSubmissionModel> mCCADetailModelArrayList) {
        this.mContext = mContext;
        this.mCCADetailModelArrayList = mCCADetailModelArrayList;
        dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_attendance_list);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_cca_review_after_submit, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.textViewCCADay.setText(mCCADetailModelArrayList.get(position).getDay());
        if(mCCADetailModelArrayList.get(position).getCca_item_description().length()>40)
        {
            holder.readMore1.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.readMore1.setVisibility(View.GONE);
        }
         holder.attendanceListIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((!(mCCADetailModelArrayList.get(position).getChoice1().equalsIgnoreCase("0")) || !(mCCADetailModelArrayList.get(position).getChoice1().equalsIgnoreCase("-1"))) || (!(mCCADetailModelArrayList.get(position).getChoice2().equalsIgnoreCase("0")) || !(mCCADetailModelArrayList.get(position).getChoice2().equalsIgnoreCase("-1")))) {
                    showAttendanceList(position);
                    }
                    }
        });
        holder.readMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                AppUtils.showDialogAlertDismiss((Activity) mContext, "Description", mCCADetailModelArrayList.get(position).getCca_item_description_2(), R.drawable.exclamationicon, R.drawable.round);

            }
        });
        holder.description2Txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppUtils.showDialogAlertDismiss((Activity) mContext, "Description", mCCADetailModelArrayList.get(position).getCca_item_description_2(), R.drawable.exclamationicon, R.drawable.round);

            }
        });

        holder.readMore1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppUtils.showDialogAlertDismiss((Activity) mContext, "Description", mCCADetailModelArrayList.get(position).getCca_item_description(), R.drawable.exclamationicon, R.drawable.round);

            }
        });
        holder.descriptionTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppUtils.showDialogAlertDismiss((Activity) mContext, "Description", mCCADetailModelArrayList.get(position).getCca_item_description(), R.drawable.exclamationicon, R.drawable.round);

            }
        });

        if (mCCADetailModelArrayList.get(position).getChoice1().equalsIgnoreCase("0")) {
            holder.linearChoice1.setVisibility(View.GONE);
            holder.textViewCCAChoice1.setText("Choice 1 : None");
            } else if (mCCADetailModelArrayList.get(position).getChoice1().equalsIgnoreCase("-1")) {
            holder.linearChoice1.setVisibility(View.GONE);
            holder.textViewCCAChoice1.setText("Choice 1 : Nil");
            } else {
            holder.linearChoice1.setVisibility(View.VISIBLE);
            holder.textViewCCAChoice1.setText(mCCADetailModelArrayList.get(position).getChoice1());
             if (mCCADetailModelArrayList.get(position).getVenue().equalsIgnoreCase("0") || mCCADetailModelArrayList.get(position).getVenue().equalsIgnoreCase(""))
            {
                holder.locationTxt.setVisibility(View.GONE);
            }
            else
            {
                holder.locationTxt.setVisibility(View.VISIBLE);
                holder.locationTxt.setText("Location            : "+mCCADetailModelArrayList.get(position).getVenue());

            }
            System.out.println("DESC EDIT"+mCCADetailModelArrayList.get(position).getCca_item_description());
            if (mCCADetailModelArrayList.get(position).getCca_item_description().equalsIgnoreCase("0") || mCCADetailModelArrayList.get(position).getCca_item_description().equalsIgnoreCase(""))
            {
                holder.descriptionTxt.setVisibility(View.GONE);
                holder.readMore1.setVisibility(View.GONE);
            }
            else
            {
                holder.descriptionTxt.setVisibility(View.VISIBLE);
                holder.readMore1.setVisibility(View.VISIBLE);
                holder.descriptionTxt.setText("Description      : "+mCCADetailModelArrayList.get(position).getCca_item_description());

            }
            if (mCCADetailModelArrayList.get(position).getCca_item_start_time() != null && mCCADetailModelArrayList.get(position).getCca_item_end_time() != null) {
                holder.textViewCCAaDateItemChoice1.setVisibility(View.VISIBLE);
                holder.textViewCCAaDateItemChoice1.setText("(" + AppUtils.convertTimeToAMPM(mCCADetailModelArrayList.get(position).getCca_item_start_time()) + " - " + AppUtils.convertTimeToAMPM(mCCADetailModelArrayList.get(position).getCca_item_end_time()) + ")");
                } else if (mCCADetailModelArrayList.get(position).getCca_item_start_time() != null) {
                holder.textViewCCAaDateItemChoice1.setVisibility(View.VISIBLE);
                holder.textViewCCAaDateItemChoice1.setText("(" + AppUtils.convertTimeToAMPM(mCCADetailModelArrayList.get(position).getCca_item_start_time()) + ")"); } else if (mCCADetailModelArrayList.get(position).getCca_item_end_time() != null) {
                holder.textViewCCAaDateItemChoice1.setVisibility(View.VISIBLE);
                holder.textViewCCAaDateItemChoice1.setText("(" + AppUtils.convertTimeToAMPM(mCCADetailModelArrayList.get(position).getCca_item_end_time()) + ")");
            } else {
                holder.textViewCCAaDateItemChoice1.setVisibility(View.GONE);
                }
            if (mCCADetailModelArrayList.get(position).getAttending_status().equalsIgnoreCase("1")) {
                holder.deleteChoice1.setImageResource(R.drawable.delete_red);
            } else if (mCCADetailModelArrayList.get(position).getAttending_status().equalsIgnoreCase("3")) {
                holder.deleteChoice1.setImageResource(R.drawable.delete_disabled);
                holder.textViewCCAaDateItemChoice1.setTextColor(mContext.getResources().getColor(R.color.light_grey));
                holder.textViewCCAChoice1.setTextColor(mContext.getResources().getColor(R.color.light_grey));
                } else {
                holder.deleteChoice2.setImageResource(R.drawable.delete_disabled);
                holder.textViewCCAaDateItemChoice2.setTextColor(mContext.getResources().getColor(R.color.light_grey));
                holder.textViewCCAChoice2.setTextColor(mContext.getResources().getColor(R.color.light_grey));
                }
                }
        if (mCCADetailModelArrayList.get(position).getChoice2().equalsIgnoreCase("0")) {
            holder.linearChoice2.setVisibility(View.GONE);
            holder.textViewCCAChoice2.setText("Choice 2 : None");
            } else if (mCCADetailModelArrayList.get(position).getChoice2().equalsIgnoreCase("-1")) {
            holder.linearChoice2.setVisibility(View.GONE);
            holder.textViewCCAChoice2.setText("Choice 2 : Nil");
            } else {
            holder.linearChoice2.setVisibility(View.VISIBLE);
            holder.textViewCCAChoice2.setText(mCCADetailModelArrayList.get(position).getChoice2());
             if (mCCADetailModelArrayList.get(position).getVenue2().equalsIgnoreCase("0") || mCCADetailModelArrayList.get(position).getVenue2().equalsIgnoreCase(""))
            {
                holder.location2Txt.setVisibility(View.GONE);
                holder.readMore.setVisibility(View.GONE);
            }
            else
            {
                holder.location2Txt.setVisibility(View.VISIBLE);
                holder.readMore.setVisibility(View.VISIBLE);
                holder.location2Txt.setText("Location            : "+mCCADetailModelArrayList.get(position).getVenue2());

            }


            if (mCCADetailModelArrayList.get(position).getCca_item_description_2().equalsIgnoreCase("0") || mCCADetailModelArrayList.get(position).getCca_item_description_2().equalsIgnoreCase(""))
            {
                holder.description2Txt.setVisibility(View.GONE);
            }
            else
            {
                holder.description2Txt.setVisibility(View.VISIBLE);
                holder.description2Txt.setText("Description      : "+mCCADetailModelArrayList.get(position).getCca_item_description_2());

            }
            if (mCCADetailModelArrayList.get(position).getCca_item_start_time() != null && mCCADetailModelArrayList.get(position).getCca_item_end_time() != null) {
                holder.textViewCCAaDateItemChoice2.setVisibility(View.VISIBLE);
                holder.textViewCCAaDateItemChoice2.setText("(" + AppUtils.convertTimeToAMPM(mCCADetailModelArrayList.get(position).getCca_item_start_time()) + " - " + AppUtils.convertTimeToAMPM(mCCADetailModelArrayList.get(position).getCca_item_end_time()) + ")");
                } else if (mCCADetailModelArrayList.get(position).getCca_item_start_time() != null) {
                holder.textViewCCAaDateItemChoice2.setVisibility(View.VISIBLE);
                holder.textViewCCAaDateItemChoice2.setText("(" + AppUtils.convertTimeToAMPM(mCCADetailModelArrayList.get(position).getCca_item_start_time()) + ")");
            } else if (mCCADetailModelArrayList.get(position).getCca_item_end_time() != null) {
                holder.textViewCCAaDateItemChoice2.setVisibility(View.VISIBLE);
                holder.textViewCCAaDateItemChoice2.setText("(" + AppUtils.convertTimeToAMPM(mCCADetailModelArrayList.get(position).getCca_item_end_time()) + ")");
            } else {
                holder.textViewCCAaDateItemChoice2.setVisibility(View.GONE);
                }
            if (mCCADetailModelArrayList.get(position).getAttending_status2().equalsIgnoreCase("1")) {
                holder.deleteChoice2.setImageResource(R.drawable.delete_red);
                } else if (mCCADetailModelArrayList.get(position).getAttending_status2().equalsIgnoreCase("3")) {
                holder.deleteChoice2.setImageResource(R.drawable.delete_disabled);
                holder.textViewCCAaDateItemChoice2.setTextColor(mContext.getResources().getColor(R.color.light_grey));
                holder.textViewCCAChoice2.setTextColor(mContext.getResources().getColor(R.color.light_grey));
                } else {
                holder.deleteChoice2.setImageResource(R.drawable.delete_disabled);
                holder.textViewCCAaDateItemChoice2.setTextColor(mContext.getResources().getColor(R.color.light_grey));
                holder.textViewCCAChoice2.setTextColor(mContext.getResources().getColor(R.color.light_grey));
                }
                }
        if (((mCCADetailModelArrayList.get(position).getChoice1().equalsIgnoreCase("0")) || (mCCADetailModelArrayList.get(position).getChoice1().equalsIgnoreCase("-1"))) && ((mCCADetailModelArrayList.get(position).getChoice2().equalsIgnoreCase("0")) || (mCCADetailModelArrayList.get(position).getChoice2().equalsIgnoreCase("-1")))) {
            holder.attendanceListIcon.setVisibility(View.INVISIBLE);
        } else {
            holder.attendanceListIcon.setVisibility(View.VISIBLE);
            }
            holder.deleteChoice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCCADetailModelArrayList.get(position).getAttending_status().equalsIgnoreCase("1")) {
                    showDialogAlertDelete((Activity) mContext, "Alert", mContext.getResources().getString(R.string.deltechoicealertques), R.drawable.questionmark_icon, R.drawable.round, position, mCCADetailModelArrayList.get(position).getCca_details_id());
                } else {

                }
            }
        });
        holder.deleteChoice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCCADetailModelArrayList.get(position).getAttending_status2().equalsIgnoreCase("1")) {
                    showDialogAlertDelete((Activity) mContext, "Alert", mContext.getResources().getString(R.string.deltechoicealertques), R.drawable.questionmark_icon, R.drawable.round, position, mCCADetailModelArrayList.get(position).getCca_details_id2());
                } else {

                }
            }
        });
        }
        @Override
    public int getItemCount() {
        return mCCADetailModelArrayList.size();
    }
    public void showAttendanceList(int mPosition) {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
        Button dialogDismiss = (Button) dialog.findViewById(R.id.btn_dismiss);
        LinearLayout linearChoice3 = (LinearLayout) dialog.findViewById(R.id.linearChoice1);
        LinearLayout linearChoice4 = (LinearLayout) dialog.findViewById(R.id.linearChoice2);
        TextView alertHead = (TextView) dialog.findViewById(R.id.alertHead);
        TextView textViewCCAChoiceFirst = (TextView) dialog.findViewById(R.id.textViewCCAChoice1);
        TextView textViewCCAChoiceSecond = (TextView) dialog.findViewById(R.id.textViewCCAChoice2);
        ScrollView scrollViewMain = (ScrollView) dialog.findViewById(R.id.scrollViewMain);
        RecyclerView socialMediaList = (RecyclerView) dialog.findViewById(R.id.recycler_view_social_media);
        RecyclerView recycler_view_social_mediaChoice2 = (RecyclerView) dialog.findViewById(R.id.recycler_view_social_mediaChoice2);
        alertHead.setText("Attendance report of " + mCCADetailModelArrayList.get(mPosition).getDay());
        int showdialog=1;
//        scrollViewMain.pageScroll(View.FOCUS_DOWN);
        scrollViewMain.smoothScrollTo(0, 0);
        if (!(mCCADetailModelArrayList.get(mPosition).getChoice1().equalsIgnoreCase("0")) && !(mCCADetailModelArrayList.get(mPosition).getChoice1().equalsIgnoreCase("-1"))) {
            linearChoice3.setVisibility(View.VISIBLE);
            socialMediaList.setVisibility(View.VISIBLE);
            socialMediaList.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(mContext);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            socialMediaList.setLayoutManager(llm);
            if (mCCADetailModelArrayList.get(mPosition).getCalendarDaysChoice1().size()<=0)
            {
                textViewCCAChoiceFirst.setVisibility(View.GONE);
                showdialog=0;
            }
            else
            {
                textViewCCAChoiceFirst.setText(mCCADetailModelArrayList.get(mPosition).getChoice1());
                textViewCCAChoiceFirst.setVisibility(View.VISIBLE);
                showdialog=1;

            }
            CCAAttendenceListAdapter socialMediaAdapter = new CCAAttendenceListAdapter(mContext, mCCADetailModelArrayList.get(mPosition).getCalendarDaysChoice1());
            socialMediaList.setAdapter(socialMediaAdapter);
        } else {
            linearChoice3.setVisibility(View.GONE);
            socialMediaList.setVisibility(View.GONE);

        }


        if (!(mCCADetailModelArrayList.get(mPosition).getChoice2().equalsIgnoreCase("0")) && !(mCCADetailModelArrayList.get(mPosition).getChoice2().equalsIgnoreCase("-1"))) {
            if (mCCADetailModelArrayList.get(mPosition).getCalendarDaysChoice2().size()<=0)
            {
                textViewCCAChoiceSecond.setVisibility(View.GONE);
                showdialog=0;
            }
            else
            {
                textViewCCAChoiceSecond.setText(mCCADetailModelArrayList.get(mPosition).getChoice2());
                textViewCCAChoiceSecond.setVisibility(View.VISIBLE);
                showdialog=1;
            }
            linearChoice4.setVisibility(View.VISIBLE);
            recycler_view_social_mediaChoice2.setVisibility(View.VISIBLE);
            recycler_view_social_mediaChoice2.setHasFixedSize(true);
            LinearLayoutManager llmrecycler_view_social_mediaChoice2 = new LinearLayoutManager(mContext);
            llmrecycler_view_social_mediaChoice2.setOrientation(LinearLayoutManager.VERTICAL);
            recycler_view_social_mediaChoice2.setLayoutManager(llmrecycler_view_social_mediaChoice2);
            CCAAttendenceListAdapter socialMediaAdapterChoice2 = new CCAAttendenceListAdapter(mContext, mCCADetailModelArrayList.get(mPosition).getCalendarDaysChoice2());
            recycler_view_social_mediaChoice2.setAdapter(socialMediaAdapterChoice2);
        } else {
            linearChoice4.setVisibility(View.GONE);
            recycler_view_social_mediaChoice2.setVisibility(View.GONE);

        }
        dialogDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        if (showdialog==1) {
            dialog.show();
        }
        else
        {
            Toast.makeText(mContext, "No attendance details available", Toast.LENGTH_SHORT).show();
        }
    }

    public static void showDialogAlertDelete(final Activity activity, String msgHead, String msg, int ico, int bgIcon, final int position, final String ccaDetailsId) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_dialogue_layout);
        ImageView icon = (ImageView) dialog.findViewById(R.id.iconImageView);
        icon.setBackgroundResource(bgIcon);
        icon.setImageResource(ico);
        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        TextView textHead = (TextView) dialog.findViewById(R.id.alertHead);
        text.setText(msg);
        textHead.setText(msgHead);

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_Ok);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                CCAsReviewAfterSubmissionActivity.ccaDeleteAPI(ccaDetailsId);
            }
        });
        Button dialogButtonCancel = (Button) dialog.findViewById(R.id.btn_Cancel);
        dialogButtonCancel.setVisibility(View.VISIBLE);
        dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

}
