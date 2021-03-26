package com.mobatia.naisapp.activities.canteen_new.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.mobatia.naisapp.BuildConfig;
import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.canteen.CanteenFirstActivity;
import com.mobatia.naisapp.activities.canteen.CanteenFirstStaffActivity;
import com.mobatia.naisapp.activities.canteen_new.ConfirmedOrderActivity;
import com.mobatia.naisapp.activities.canteen_new.MyOrderActivity;
import com.mobatia.naisapp.activities.canteen_new.model.MyOrderDetailModel;
import com.mobatia.naisapp.activities.canteen_new.model.OrderHistoryDetailModel;
import com.mobatia.naisapp.appcontroller.AppController;
import com.mobatia.naisapp.constants.IntentPassValueConstants;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NameValueConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.home.adapter.ImagePagerDrawableAdapter;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MyOrderDetailRecyclerAdapter extends RecyclerView.Adapter<MyOrderDetailRecyclerAdapter.MyViewHolder> implements URLConstants, StatusConstants, JSONConstants, IntentPassValueConstants, NameValueConstants {

    private Context mContext;
    private ArrayList<MyOrderDetailModel> mMyOrderDetailArrayList;
    String ordered_user_type="";
    String student_id="";
    String parent_id="";
    String staff_id="";
    String quantity="";
    String canteen_cart_id="";
    ArrayList<String> homeBannerUrlImageArray;
    int currentPage = 0;
    String totalAmount="";
    int WalletAmount =0;
    float BalanceWalletAmount=0;
    float BalanceConfirmWalletAmount=0;
    float CartTotalAmount=0;
    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView itemNameTxt,itemDescription,amountTxt,status,amountExceedTxt,itemsCount,statusExceed,portalTxt;
        ElegantNumberButton cartitemcount;
        LinearLayout multiLinear;
        LinearLayout linearlayout,exceedLinear;
        ViewPager bannerImagePager;
        public MyViewHolder(View view) {
            super(view);
            itemNameTxt = view.findViewById(R.id.itemNameTxt);
            itemDescription = view.findViewById(R.id.itemDescription);
            cartitemcount = view.findViewById(R.id.cartitemcount);
            multiLinear = view.findViewById(R.id.multiLinear);
            status = view.findViewById(R.id.status);
            linearlayout = view.findViewById(R.id.linearlayout);
            amountExceedTxt = view.findViewById(R.id.amountExceedTxt);
            itemsCount = view.findViewById(R.id.itemsCount);
            statusExceed = view.findViewById(R.id.statusExceed);
            exceedLinear = view.findViewById(R.id.exceedLinear);
            portalTxt = view.findViewById(R.id.portalTxt);
            bannerImagePager = (ViewPager) view.findViewById(R.id.bannerImagePager);

        }
    }


    public MyOrderDetailRecyclerAdapter(Context mContext, ArrayList<MyOrderDetailModel> mMyOrderDetailArrayList, String ordered_user_type, String student_id, String parent_id, String staff_id,String totalAmount,int WalletAmount) {
        this.mContext = mContext;
        this.mMyOrderDetailArrayList = mMyOrderDetailArrayList;
        this.ordered_user_type=ordered_user_type;
        this.student_id=student_id;
        this.parent_id=parent_id;
        this.staff_id=staff_id;
        this.totalAmount=totalAmount;
        this.WalletAmount=WalletAmount;
        //,totalAmount,WalletAmount
    }

    @Override
    public MyOrderDetailRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_my_order_detail, parent, false);

        return new MyOrderDetailRecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyOrderDetailRecyclerAdapter.MyViewHolder holder, int position) {
        holder.bannerImagePager.setVisibility(View.GONE);
        holder.itemNameTxt.setText(mMyOrderDetailArrayList.get(position).getItem_name());
        holder.itemDescription.setText(mMyOrderDetailArrayList.get(position).getItem_description());
        holder.amountExceedTxt.setText(mMyOrderDetailArrayList.get(position).getPrice()+" AED ");
        holder.cartitemcount.setNumber(mMyOrderDetailArrayList.get(position).getQuantity());
        holder.cartitemcount.setRange(0,Integer.valueOf(mMyOrderDetailArrayList.get(position).getAvailable_quantity()));
        if (mMyOrderDetailArrayList.get(position).getCancellation_time_exceed().equalsIgnoreCase("0"))
        {
            if (mMyOrderDetailArrayList.get(position).getQuantity().equalsIgnoreCase("1"))
            {
                holder.itemsCount.setText(mMyOrderDetailArrayList.get(position).getQuantity()+" item");
            }
            else
            {
                holder.itemsCount.setText(mMyOrderDetailArrayList.get(position).getQuantity()+" items");
            }

            if (mMyOrderDetailArrayList.get(position).getItem_status().equalsIgnoreCase("0"))
            {
                holder.statusExceed.setText("Cancelled");
                holder.statusExceed.setTextColor(mContext.getResources().getColor(R.color.red));
                holder.itemsCount.setVisibility(View.VISIBLE);
                holder.multiLinear.setVisibility(View.GONE);
                holder.linearlayout.setBackgroundColor(mContext.getResources().getColor(R.color.light_grey));
            }
            else if (mMyOrderDetailArrayList.get(position).getItem_status().equalsIgnoreCase("1"))
            {
                holder.statusExceed.setText("Active");
                holder.statusExceed.setTextColor(mContext.getResources().getColor(R.color.orange_circle));
                holder.itemsCount.setVisibility(View.GONE);
                holder.multiLinear.setVisibility(View.VISIBLE);
                holder.linearlayout.setBackgroundColor(mContext.getResources().getColor(R.color.canteen_item_bg));
            }
            else
            {
                holder.statusExceed.setText("Delivered");
                holder.statusExceed.setTextColor(mContext.getResources().getColor(R.color.green));
                holder.itemsCount.setVisibility(View.VISIBLE);
                holder.multiLinear.setVisibility(View.GONE);
                holder.linearlayout.setBackgroundColor(mContext.getResources().getColor(R.color.canteen_item_bg));
            }

        }
        else
        {
            holder.itemsCount.setVisibility(View.VISIBLE);
            holder.multiLinear.setVisibility(View.GONE);
            if (mMyOrderDetailArrayList.get(position).getQuantity().equalsIgnoreCase("1"))
            {
                holder.itemsCount.setText(mMyOrderDetailArrayList.get(position).getQuantity()+" Item");
            }
            else
            {
                holder.itemsCount.setText(mMyOrderDetailArrayList.get(position).getQuantity()+" Items");
            }
            if (mMyOrderDetailArrayList.get(position).getItem_status().equalsIgnoreCase("0"))
            {
                holder.statusExceed.setText("Cancelled");
                holder.statusExceed.setTextColor(mContext.getResources().getColor(R.color.red));
            }
            else if (mMyOrderDetailArrayList.get(position).getItem_status().equalsIgnoreCase("1"))
            {

                holder.statusExceed.setText("Active");
                holder.statusExceed.setTextColor(mContext.getResources().getColor(R.color.orange_circle));
            }
            else
            {

                holder.statusExceed.setText("Delivered");
                holder.statusExceed.setTextColor(mContext.getResources().getColor(R.color.green));
            }


        }
        if (mMyOrderDetailArrayList.get(position).getPortal().equalsIgnoreCase("2"))
        {
            holder.portalTxt.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.portalTxt.setVisibility(View.GONE);
        }

        holder.cartitemcount.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                canteen_cart_id=mMyOrderDetailArrayList.get(position).getId();
                quantity=String.valueOf(newValue);
                AppController.myHistoryPosition=position;
                if (AppUtils.isNetworkConnected(mContext)) {
                    if (newValue!=0)
                    {
                        if (newValue==Integer.valueOf(mMyOrderDetailArrayList.get(position).getAvailable_quantity()))
                        {
                            Toast.makeText(mContext, "You have reached the Pre-Order limit for this item", Toast.LENGTH_SHORT).show();
                        }
                        if (newValue>oldValue)
                        {
                            float totalAmt=Float.valueOf(totalAmount);
                            CartTotalAmount=totalAmt+Float.parseFloat(mMyOrderDetailArrayList.get(position).getPrice());
                            BalanceWalletAmount=WalletAmount-totalAmt;
                            if (BalanceWalletAmount>0)
                            {
                                BalanceConfirmWalletAmount=BalanceWalletAmount-CartTotalAmount;
                                if (BalanceConfirmWalletAmount>0)
                                {
                                    getCartUpdate(URL_CANTEEN_CONFIRMED_ORDER_EDIT,canteen_cart_id,quantity);
                                }
                                else
                                {
                                    holder.cartitemcount.setNumber(String.valueOf(oldValue));
                                    showInsufficientBal(mContext, "Alert", "Insufficient balance please top up wallet", R.drawable.exclamationicon, R.drawable.round);

                                }
                            }
                            else
                            {
                                holder.cartitemcount.setNumber(String.valueOf(oldValue));
                                showInsufficientBal(mContext, "Alert", "Insufficient balance please top up wallet", R.drawable.exclamationicon, R.drawable.round);

                            }
                        }
                        else
                        {
                            getCartUpdate(URL_CANTEEN_CONFIRMED_ORDER_EDIT,canteen_cart_id,quantity);
                        }


                    }
                    else
                    {

                        getCartCancel(URL_CANTEEN_CONFIRMED_ORDER_ITEM_CELL_CANCEL,canteen_cart_id);
                    }

                } else {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", mContext.getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                }
            }
        });
        homeBannerUrlImageArray=mMyOrderDetailArrayList.get(position).getImageBanner();
        if (homeBannerUrlImageArray != null)
        {

            final Handler handler = new Handler();
            final Runnable Update = new Runnable() {
                public void run() {
                    if (currentPage == homeBannerUrlImageArray.size()) {
                        currentPage = 0;
                        holder.bannerImagePager.setCurrentItem(currentPage,
                                false);
                    } else {

                        holder.bannerImagePager
                                .setCurrentItem(currentPage++, true);
                    }

                }
            };
            final Timer swipeTimer = new Timer();
            swipeTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(Update);
                }
            }, 100, 3000);
            holder.bannerImagePager.setAdapter(new ImagePagerDrawableAdapter(homeBannerUrlImageArray, mContext));
        }
        else
        {
            holder.bannerImagePager.setBackgroundResource(R.drawable.noitemscanteen);
        }

    }


    @Override
    public int getItemCount() {
        return mMyOrderDetailArrayList.size();
    }

    public  void getCartUpdate(final String URL,String canteen_cart_id,String quantity)
    {
        try {
            String deviceBrand = android.os.Build.MANUFACTURER;
            String deviceModel = Build.MODEL;
            String osVersion = android.os.Build.VERSION.RELEASE;
            String devicename=deviceBrand+" "+deviceModel+" "+osVersion;
            String version= BuildConfig.VERSION_NAME;
            final VolleyWrapper manager = new VolleyWrapper(URL);
//            stud_id = studentsModelArrayList.get(0).getmId();
            String[] name = {JTAG_ACCESSTOKEN,"canteen_preorder_item_id","quantity","portal","device_type","device_name","app_version","user_type","parent_id","student_id"};
            String[] value = {PreferenceManager.getAccessToken(mContext),canteen_cart_id,quantity,"1","2",devicename,version,"1",PreferenceManager.getUserId(mContext),""};
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
                                    if (statusCode.equals(STATUS_SUCCESS))
                                    {
                                       Toast.makeText(mContext,"Item Updated",Toast.LENGTH_SHORT).show();
                                        MyOrderActivity.getMyOrder(URL_CANTEEN_CONFIRMED_ORDER_ITEM,ordered_user_type,student_id,parent_id,staff_id);

                                    }
                                    else {
                                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "Cannot continue. Please try again later", R.drawable.exclamationicon, R.drawable.round);

                                    }

                                } else if (responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) ||
                                        responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                                        responsCode.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
                                    AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                                        @Override
                                        public void getAccessToken() {
                                        }
                                    });
                                    getCartUpdate(URL,canteen_cart_id,quantity);

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
            String[] name = {JTAG_ACCESSTOKEN,"canteen_preorder_item_id","portal","device_type","device_name","app_version","user_type","parent_id","student_id"};
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
                                    Toast.makeText(mContext,"Item Updated",Toast.LENGTH_SHORT).show();
                                    MyOrderActivity.getMyOrder(URL_CANTEEN_CONFIRMED_ORDER_ITEM,ordered_user_type,student_id,parent_id,staff_id);

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
    public void showInsufficientBal(final Context activity, String msgHead, String msg, int ico,int bgIcon)
    {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_insufficient_popup);
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
                if (PreferenceManager.getUserId(mContext).equalsIgnoreCase(""))
                {
                    Intent intent=new Intent(mContext, CanteenFirstStaffActivity.class);
                    intent.putExtra("tab_type","Canteen");
                    intent.putExtra("email","");
                    intent.putExtra("isFrom","Preorder");
                    dialog.dismiss();
                    mContext.startActivity(intent);
                }
                else
                {
                    Intent intent=new Intent(mContext, CanteenFirstActivity.class);
                    intent.putExtra("tab_type","Canteen");
                    intent.putExtra("email","");
                    intent.putExtra("isFrom","Preorder");
                    dialog.dismiss();
                    mContext.startActivity(intent);
                }
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
}
