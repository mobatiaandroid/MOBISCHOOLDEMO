package com.mobatia.naisapp.fragments.report.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.pdf.PdfReaderActivity;
import com.mobatia.naisapp.activities.web_view.LoadUrlWebViewActivity;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.report.model.DataModel;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by krishnaraj on 23/07/18.
 */

public class RecyclerViewSubAdapter extends RecyclerView.Adapter<RecyclerViewSubAdapter.MyViewHolder> implements JSONConstants,URLConstants,StatusConstants {

    private Context mContext;
    private ArrayList<DataModel> mDataModel;
    String dept;



    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView termName;
        LinearLayout clickLinear;
        RelativeLayout statusLayout;
        TextView status;

        public MyViewHolder(View view) {
            super(view);
            termName = view.findViewById(R.id.termname);
            clickLinear = view.findViewById(R.id.clickLinear);
            ArrayList<DataModel> mDataModel= new ArrayList<>();
            statusLayout=view.findViewById(R.id.statusLayout);
            status=view.findViewById(R.id.status);



        }
    }


    public RecyclerViewSubAdapter(Context mContext, ArrayList<DataModel> mDataModel) {
        this.mContext = mContext;
        this.mDataModel = mDataModel;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_adapter_sub, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
       holder.termName.setText(mDataModel.get(position).getReporting_cycle());
        if (mDataModel.get(position).getStatus().equalsIgnoreCase("0"))
        {
            holder.statusLayout.setVisibility(View.VISIBLE);
            holder.status.setBackgroundResource(R.drawable.rectangle_red);
            holder.status.setText("New");
        }
        else if (mDataModel.get(position).getStatus().equalsIgnoreCase("2"))
        {
            holder.statusLayout.setVisibility(View.VISIBLE);
            holder.status.setBackgroundResource(R.drawable.rectangle_orange);
            holder.status.setText("Updated");
        }
        else if (mDataModel.get(position).getStatus().equalsIgnoreCase(""))
        {
            holder.statusLayout.setVisibility(View.GONE);

        }
        else if (mDataModel.get(position).getStatus().equalsIgnoreCase("1"))
        {
            holder.statusLayout.setVisibility(View.GONE);

        }
        holder.clickLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDataModel.get(position).getStatus().equalsIgnoreCase("0")||mDataModel.get(position).getStatus().equalsIgnoreCase("2"))
                {
                   callStatusChangeApi(URL_GET_STATUS_CHANGE_API,mDataModel.get(position).getId(),position,mDataModel.get(position).getStatus());

                }

               else
                {
                    if (mDataModel.get(position).getFile().endsWith(".pdf")) {
                        Intent intent = new Intent(mContext, PdfReaderActivity.class);
                        intent.putExtra("pdf_url", mDataModel.get(position).getFile());
                        mContext.startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(mContext, LoadUrlWebViewActivity.class);
                        intent.putExtra("url",mDataModel.get(position).getFile());
                        mContext.startActivity(intent);
                    }
                }

            }
        });


    }



    @Override
    public int getItemCount() {
        return mDataModel.size();
    }
    public  void callStatusChangeApi(final String URL, final String id,final int position,final String status) {
        VolleyWrapper volleyWrapper = new VolleyWrapper(URL);
        String[] name = {"access_token","users_id","id","type"};
        String[] value = {PreferenceManager.getAccessToken(mContext),PreferenceManager.getUserId(mContext),id,"report"};
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
                            mDataModel.get(position).setStatus("1");
                            notifyDataSetChanged();
                            if (mDataModel.get(position).getFile().endsWith(".pdf")) {
                                Intent intent = new Intent(mContext, PdfReaderActivity.class);
                                intent.putExtra("pdf_url", mDataModel.get(position).getFile());
                                mContext.startActivity(intent);
                            }
                            else
                            {
                                Intent intent = new Intent(mContext, LoadUrlWebViewActivity.class);
                                intent.putExtra("url",mDataModel.get(position).getFile());
                                mContext.startActivity(intent);
                            }
                            //mRecyclerViewMainAdapter.notifyDataSetChanged();
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
                        callStatusChangeApi(URL,id,position,status);

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
