package com.mobatia.naisapp.fragments.sports.adapter;

/**
 * Created by gayatri on 28/3/17.
 */

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.mobatia.naisapp.R;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.ResultConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.sports.SportsActivity;
import com.mobatia.naisapp.fragments.sports.SportsDetailActivity;
import com.mobatia.naisapp.fragments.sports.model.SportsModel;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by gayathri on 28th MAr 2017.
 */
public class SportsEventListAdapter extends RecyclerView.Adapter<SportsEventListAdapter.MyViewHolder> implements JSONConstants, URLConstants, ResultConstants, StatusConstants {

    private Context mContext;
    private ArrayList<SportsModel> mEventList;
    SportsEventListAdapter mSportsEventListAdapter;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView start_date, event, end_date, status;
        ImageView imageView11;
        ImageView imageView9;
        ImageView imageView10;
        RelativeLayout statusLayout;
        RecyclerView studentList;

        public MyViewHolder(View view) {
            super(view);

            start_date = (TextView) view.findViewById(R.id.start_date);
            end_date = (TextView) view.findViewById(R.id.end_date);
            event = (TextView) view.findViewById(R.id.event);
            status = (TextView) view.findViewById(R.id.status);
            imageView11 = (ImageView) view.findViewById(R.id.imageView11);
            imageView10 = (ImageView) view.findViewById(R.id.imageView10);
            imageView9 = (ImageView) view.findViewById(R.id.imageView9);
            statusLayout = (RelativeLayout) view.findViewById(R.id.statusLayout);
            studentList = (RecyclerView) view.findViewById(R.id.studentList);
            studentList.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(mContext);
            llm.setOrientation(LinearLayoutManager.HORIZONTAL);
            studentList.setLayoutManager(llm);

        }
    }


    public SportsEventListAdapter(Context mContext, ArrayList<SportsModel> eventList) {
        this.mContext = mContext;
        this.mEventList = eventList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_sports_event_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.start_date.setText(separateDate(mEventList.get(position).getSports_start_date()));
        holder.end_date.setText(AppUtils.convertTimeToAMPM(mEventList.get(position).getStart_time())+" - "+AppUtils.convertTimeToAMPM(mEventList.get(position).getEnd_time()));
        holder.imageView11.setImageResource(R.drawable.bubble);
        holder.imageView9.setImageResource(R.drawable.evnticon);
        holder.imageView10.setImageResource(R.color.event_list_bg);
        holder.event.setText(mEventList.get(position).getSports_name());
        StudentRecyclerListSportsAdapter mStudentRecyclerListSportsAdapter=new StudentRecyclerListSportsAdapter(mContext,mEventList.get(position).getStudentModelSportsList());
        holder.studentList.setAdapter(mStudentRecyclerListSportsAdapter);
        holder.imageView11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEventList.get(position).getStatus().equalsIgnoreCase("0") || mEventList.get(position).getStatus().equalsIgnoreCase("2")) {
                    callStatusChangeApi(URL_GET_STATUS_CHANGE_API, mEventList.get(position).getSports_id(), position, mEventList.get(position).getStatus());

                } else {
                    int studentSize=0;
                    int studPos=0;
                    String stude_id="";
                    for (int k=0;k<SportsActivity.mFilterModelList.size();k++)
                    {
                        if (SportsActivity.mFilterModelList.get(k).getCheckedStudent())
                        {
                            System.out.println("studentSize1"+studentSize);
                            studentSize=studentSize+1;
                            studPos=k;
                            System.out.println("studpos!!!"+studPos);
                        }
                    }
                    if (studentSize==1)
                    {
                        System.out.println("studpos"+studPos);
                        stude_id=SportsActivity.mFilterModelList.get(studPos).getStudentId();
                    }
                    System.out.println("stude_id"+stude_id);
                    Intent sIntent = new Intent(mContext, SportsDetailActivity.class);
                    sIntent.putExtra("event_id", mEventList.get(position).getSports_id());
                    sIntent.putExtra("stude_id", stude_id);
                    mContext.startActivity(sIntent);
                }
//                Intent sIntent=new Intent(mContext, SportsDetailActivity.class);
//                sIntent.putExtra("event_id",mEventList.get(position).getSports_id());
//                mContext.startActivity(sIntent);
            }
        });
        if (mEventList.get(position).getStatus().equalsIgnoreCase("0")) {
            holder.statusLayout.setVisibility(View.VISIBLE);
            holder.status.setBackgroundResource(R.drawable.rectangle_red);
            holder.status.setText("New");
        } else if (mEventList.get(position).getStatus().equalsIgnoreCase("1") || mEventList.get(position).getStatus().equalsIgnoreCase("")) {
            holder.statusLayout.setVisibility(View.GONE);

        } else if (mEventList.get(position).getStatus().equalsIgnoreCase("2")) {
            holder.statusLayout.setVisibility(View.VISIBLE);
            holder.status.setBackgroundResource(R.drawable.rectangle_orange);
            holder.status.setText("Updated");
        }


    }


    @Override
    public int getItemCount() {
        return mEventList.size();
    }

    private String separateDate(String inputDate) {
        String mDate = "";

        try {
            Log.d("Time here ", "InputDate--" + inputDate);
            Date date;
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            date = formatter.parse(inputDate);
            //Subtracting 6 hours from selected time
            long time = date.getTime();

            SimpleDateFormat formatterFullDate = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
            mDate = formatterFullDate.format(time);
            Log.e("Date ", mDate);


        } catch (Exception e) {
            Log.d("Exception", "" + e);
        }
        return mDate;
    }

    private String separateTime(String inputDate) {
        String mTime = "";
        try {
            Log.d("Time here ", "InputDate--" + inputDate);
            Date date;
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            date = formatter.parse(inputDate);
            //Subtracting 6 hours from selected time
            long time = date.getTime();

            SimpleDateFormat formatterTime = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
            mTime = formatterTime.format(time);
            if (mTime.contains("a.m.")) {
                mTime = AppUtils.replaceAmdot(mTime);


            } else if (mTime.contains("p.m.")) {
                mTime = AppUtils.replacePmdot(mTime);
            }


            Log.e("Time ", mTime);

        } catch (Exception e) {
            Log.d("Exception", "" + e);
        }
        return mTime;
    }

    private void callStatusChangeApi(String URL, final String id, final int eventPosition, final String status) {
        VolleyWrapper volleyWrapper = new VolleyWrapper(URL);
        String[] name = {"access_token", "users_id", "id", "type"};
        String[] value = {PreferenceManager.getAccessToken(mContext), PreferenceManager.getUserId(mContext), id, "sports"};
        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("The response is status change" + successResponse);
                try {
                    JSONObject obj = new JSONObject(successResponse);
                    String response_code = obj.getString(JTAG_RESPONSECODE);
                    if (response_code.equalsIgnoreCase("200")) {
                        JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                        String status_code = secobj.getString(JTAG_STATUSCODE);
                        if (status_code.equalsIgnoreCase("303")) {
                            if (status.equalsIgnoreCase("0") || status.equalsIgnoreCase("2")) {
                                mEventList.get(eventPosition).setStatus("1");
                                notifyDataSetChanged();
                                int studentSize=SportsActivity.mFilterModelList.size();
                                String stude_id="";
                                for (int k=0;k<SportsActivity.mFilterModelList.size();k++)
                                {
                                    if (SportsActivity.mFilterModelList.get(k).getCheckedStudent())
                                    {

                                    }
                                    else
                                    {
                                        studentSize=studentSize-1;

                                    }
                                }
                                if (studentSize==1)
                                {
                                    stude_id=SportsActivity.mFilterModelList.get(studentSize-1).getStudentId();
                                }
                                System.out.println("stude_id"+stude_id);
                                Intent sIntent = new Intent(mContext, SportsDetailActivity.class);
                                sIntent.putExtra("event_id", mEventList.get(eventPosition).getSports_id());
                                sIntent.putExtra("stude_id", stude_id);


                                mContext.startActivity(sIntent);
                            }
                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                        // AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    } else if (response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) ||
                            response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                            response_code.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
                        AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                            @Override
                            public void getAccessToken() {

                            }
                        });

                    } else {

                    }
                } catch (Exception ex) {
                    System.out.println("The Exception in edit profile is" + ex.toString());
                }

            }

            @Override
            public void responseFailure(String failureResponse) {
                //AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

            }
        });


    }
}
