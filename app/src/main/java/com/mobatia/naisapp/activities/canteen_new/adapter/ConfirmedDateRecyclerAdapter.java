package com.mobatia.naisapp.activities.canteen_new.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobatia.naisapp.BuildConfig;
import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.canteen_new.ConfirmedOrderActivity;
import com.mobatia.naisapp.activities.canteen_new.model.ConfirmedDataModel;
import com.mobatia.naisapp.constants.IntentPassValueConstants;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NameValueConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.recyclerviewmanager.ItemOffsetDecoration;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;

import org.json.JSONObject;

import java.util.ArrayList;

public class ConfirmedDateRecyclerAdapter extends RecyclerView.Adapter<ConfirmedDateRecyclerAdapter.MyViewHolder> implements URLConstants, StatusConstants, JSONConstants, IntentPassValueConstants, NameValueConstants {

    private Context mContext;
    private ArrayList<ConfirmedDataModel> mCartDateArrayList;
    String dept;
    ConfirmedDetailRecyclerAdapter mAdapter;
    String ordered_user_type;
    String student_id;
    String parent_id;
    String staff_id;

    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView itemDateTxt,amountTxt;
        ImageView closeImg;
        RelativeLayout amountRelative;

        RecyclerView cartItemRecycler;
        public MyViewHolder(View view) {
            super(view);


            cartItemRecycler=view.findViewById(R.id.cartItemRecycler);
            itemDateTxt=view.findViewById(R.id.itemDateTxt);
            amountTxt=view.findViewById(R.id.amountTxt);
            closeImg=view.findViewById(R.id.imgClose);
            amountRelative=view.findViewById(R.id.amountRelative);

        }
    }


    public ConfirmedDateRecyclerAdapter(Context mContext, ArrayList<ConfirmedDataModel> mCartDateArrayList, String ordered_user_type, String student_id, String parent_id, String staff_id) {
        this.mContext = mContext;
        this.mCartDateArrayList = mCartDateArrayList;
        this.ordered_user_type = ordered_user_type;
        this.student_id = student_id;
        this.parent_id = parent_id;
        this.staff_id = staff_id;

    }

    @Override
    public ConfirmedDateRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_confirmed_date_recycler, parent, false);

        return new ConfirmedDateRecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ConfirmedDateRecyclerAdapter.MyViewHolder holder, int position) {
        holder.cartItemRecycler.setHasFixedSize(true);
        if (mCartDateArrayList.get(position).getStatus().equalsIgnoreCase("0"))
        {
            holder.closeImg.setVisibility(View.INVISIBLE);
            holder.amountRelative.setVisibility(View.GONE);
        }
        else if (mCartDateArrayList.get(position).getStatus().equalsIgnoreCase("1"))
        {
            holder.closeImg.setVisibility(View.VISIBLE);
            holder.amountRelative.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.closeImg.setVisibility(View.GONE);
            holder.amountRelative.setVisibility(View.VISIBLE);
        }
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        int spacing = 5; // 50px
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext,spacing);
        holder.cartItemRecycler.addItemDecoration(itemDecoration);
        holder.cartItemRecycler.setLayoutManager(llm);
        holder.itemDateTxt.setText(AppUtils.dateConversionY(mCartDateArrayList.get(position).getDelivery_date()));
        holder.amountTxt.setText(mCartDateArrayList.get(position).getTotal_amount()+" AED");
        mAdapter=new ConfirmedDetailRecyclerAdapter(mContext,mCartDateArrayList.get(position).getmConfirmedDetailArrayList(),mCartDateArrayList.get(position).getDelivery_date(),ordered_user_type,student_id,parent_id,staff_id);
        holder.cartItemRecycler.setAdapter(mAdapter);
        holder.closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialogAlertLogout(mContext, "Confirm", "Do you want to cancel the order?", R.drawable.questionmark_icon, R.drawable.round,mCartDateArrayList.get(position).getId());

            }
        });
    }


    @Override
    public int getItemCount() {
        return mCartDateArrayList.size();
    }
    public  void showDialogAlertLogout( Context activity, String msgHead, String msg, int ico,int bgIcon,String id)
    {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
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
                getCartCancel(URL_CANTEEN_CONFIRMED_ORDER_DATE_CELL_CANCEL,id);
                dialog.dismiss();
            }
        });
        Button dialogButtonCancel = (Button) dialog.findViewById(R.id.btn_Cancel);
        dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }
    public  void getCartCancel(final String URL,String canteen_cart_id)
    {
        try {
            String deviceBrand = android.os.Build.MANUFACTURER;
            String deviceModel = Build.MODEL;
            String osVersion = android.os.Build.VERSION.RELEASE;
            String devicename=deviceBrand+" "+deviceModel+" "+osVersion;
            String version= BuildConfig.VERSION_NAME;
            final VolleyWrapper manager = new VolleyWrapper(URL);
//            stud_id = studentsModelArrayList.get(0).getmId();
            String[] name = {JTAG_ACCESSTOKEN,"canteen_preorder_id","portal","device_type","device_name","app_version","user_type","parent_id","student_id"};
            String[] value = {PreferenceManager.getAccessToken(mContext),canteen_cart_id,"1","2",devicename,version,"1",PreferenceManager.getUserId(mContext),""};
            System.out.println("Values ::::" + PreferenceManager.getAccessToken(mContext) + "hfhgfhdfghd::::" + PreferenceManager.getStaffId(mContext));
            manager.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {

                @Override
                public void responseSuccess(String successResponse) {
                    Log.e("responseSuccess uItem", successResponse);
                    String responsCode = "";
                    if (successResponse != null) {
                        try {
                            JSONObject rootObject = new JSONObject(successResponse);
                            if (rootObject.optString(JTAG_RESPONSE) != null) {
                                responsCode = rootObject.optString(JTAG_RESPONSECODE);
                                if (responsCode.equals(RESPONSE_SUCCESS)) {
                                    JSONObject respObject = rootObject.getJSONObject(JTAG_RESPONSE);
                                    String statusCode = respObject.optString(JTAG_STATUSCODE);

                                    ConfirmedOrderActivity.getConfirmedOrderList(URL_CANTEEN_CONFIRMED_ORDER_ITEM,ordered_user_type,student_id,parent_id,staff_id);

                                } else if (responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) ||
                                        responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                                        responsCode.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
                                    AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                                        @Override
                                        public void getAccessToken() {
                                        }
                                    });
                                    getCartCancel(URL,canteen_cart_id);

                                } else if (responsCode.equals(RESPONSE_ERROR)) {
//								CustomStatusDialog(RESPONSE_FAILURE);
                                    //Toast.makeText(mContext,"Failure",Toast.LENGTH_SHORT).show();
                                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "Cannot continue. Please try again later", R.drawable.exclamationicon, R.drawable.round);

                                }
                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "Cannot continue. Please try again later", R.drawable.exclamationicon, R.drawable.round);

                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                    }
                }

                @Override
                public void responseFailure(String failureResponse) {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "Cannot continue. Please try again later", R.drawable.exclamationicon, R.drawable.round);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
