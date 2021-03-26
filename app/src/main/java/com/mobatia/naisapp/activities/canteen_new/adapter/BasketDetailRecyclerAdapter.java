package com.mobatia.naisapp.activities.canteen_new.adapter;

import android.app.Activity;
import android.app.AppComponentFactory;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.canteen_new.BasketListActivity;
import com.mobatia.naisapp.activities.canteen_new.CartActivity;
import com.mobatia.naisapp.activities.canteen_new.MyOrderActivity;
import com.mobatia.naisapp.activities.canteen_new.model.BasketDetailModel;
import com.mobatia.naisapp.activities.canteen_new.model.MyOrderDetailModel;
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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class BasketDetailRecyclerAdapter extends RecyclerView.Adapter<BasketDetailRecyclerAdapter.MyViewHolder> implements URLConstants, StatusConstants, JSONConstants, IntentPassValueConstants, NameValueConstants {

    private Context mContext;
    private ArrayList<BasketDetailModel> mMyOrderDetailArrayList;
    String ordered_user_type="";
    String student_id="";
    String parent_id="";
    String staff_id="";
    String quantity="";
    String canteen_cart_id="";
    String delivery_date="";
    String item_id="";
    String price="";
    ArrayList<String> homeBannerUrlImageArray;
    int currentPage = 0;
    public class MyViewHolder extends RecyclerView.ViewHolder {


        ImageView itemImage;
        TextView itemNameTxt,itemDescription,amountTxt,notAvailableTxt,removeTxt,portalTxt;
        ElegantNumberButton cartitemcount;
        LinearLayout multiLinear;
        LinearLayout linearlayout;
        ViewPager bannerImagePager;
        public MyViewHolder(View view) {
            super(view);
            itemImage = view.findViewById(R.id.itemImage);
            itemNameTxt = view.findViewById(R.id.itemNameTxt);
            itemDescription = view.findViewById(R.id.itemDescription);
            amountTxt = view.findViewById(R.id.amountTxt);
            cartitemcount = view.findViewById(R.id.cartitemcount);
            multiLinear = view.findViewById(R.id.multiLinear);
            linearlayout = view.findViewById(R.id.linearlayout);
            notAvailableTxt = view.findViewById(R.id.notAvailableTxt);
            removeTxt = view.findViewById(R.id.removeTxt);
            portalTxt = view.findViewById(R.id.portalTxt);
            bannerImagePager = (ViewPager) view.findViewById(R.id.bannerImagePager);

        }
    }


    public BasketDetailRecyclerAdapter(Context mContext, ArrayList<BasketDetailModel> mMyOrderDetailArrayList, String ordered_user_type, String student_id, String parent_id, String staff_id,String delivery_date) {

        this.mContext = mContext;
        this.mMyOrderDetailArrayList = mMyOrderDetailArrayList;
        this.ordered_user_type=ordered_user_type;
        this.student_id=student_id;
        this.parent_id=parent_id;
        this.staff_id=staff_id;
        this.delivery_date=delivery_date;


    }

    @Override
    public BasketDetailRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_my_order_detail_recycler, parent, false);

        return new BasketDetailRecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final BasketDetailRecyclerAdapter.MyViewHolder holder, int position) {
        holder.itemNameTxt.setText(mMyOrderDetailArrayList.get(position).getItem_name());
        holder.itemDescription.setText(mMyOrderDetailArrayList.get(position).getItem_description());
        holder.amountTxt.setText(mMyOrderDetailArrayList.get(position).getPrice()+"AED");
        if (mMyOrderDetailArrayList.get(position).getAvailable_quantity().equalsIgnoreCase("0"))
        {
            holder.notAvailableTxt.setVisibility(View.VISIBLE);
            holder.removeTxt.setVisibility(View.VISIBLE);
            holder.multiLinear.setVisibility(View.GONE);
        }
        else
        {
            if (mMyOrderDetailArrayList.get(position).getPortal().equalsIgnoreCase("1"))
            {
                holder.portalTxt.setVisibility(View.GONE);
            }
            else
            {
                holder.portalTxt.setVisibility(View.VISIBLE);
            }
            holder.notAvailableTxt.setVisibility(View.GONE);
            holder.removeTxt.setVisibility(View.GONE);
            holder.multiLinear.setVisibility(View.VISIBLE);
            holder.cartitemcount.setNumber(mMyOrderDetailArrayList.get(position).getQuantity());
            holder.cartitemcount.setRange(0,Integer.valueOf(mMyOrderDetailArrayList.get(position).getAvailable_quantity()));
        }

        holder.removeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getCartCancel(URL_CANTEEN_REMOVE_CART_ITEM,mMyOrderDetailArrayList.get(position).getId());
            }
        });

        holder.cartitemcount.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                canteen_cart_id=mMyOrderDetailArrayList.get(position).getId();
                quantity=String.valueOf(newValue);
                item_id=mMyOrderDetailArrayList.get(position).getItem_id();
                price=mMyOrderDetailArrayList.get(position).getPrice();
                if (AppUtils.isNetworkConnected(mContext)) {
                    AppController.basketPosition=position;
                    if (newValue!=0)
                    {
                        if (newValue==Integer.valueOf(mMyOrderDetailArrayList.get(position).getAvailable_quantity()))
                        {
                            Toast.makeText(mContext, "You have reached the Pre-Order limit for this item", Toast.LENGTH_SHORT).show();
                        }
                     //   getCartUpdate(URL_CANTEEN_CONFIRMED_ORDER_EDIT,canteen_cart_id,quantity);
                        getCartUpdate(URL_CANTEEN_CART_UPDATE,canteen_cart_id,delivery_date,ordered_user_type,item_id,quantity,price);

                    }
                    else
                    {
                        //getCartCancel(URL_CANTEEN_CONFIRMED_ORDER_ITEM_CELL_CANCEL,canteen_cart_id);
                        getCartCancel(URL_CANTEEN_REMOVE_CART_ITEM,mMyOrderDetailArrayList.get(position).getId());
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

    public  void getCartUpdate(final String URL,String canteen_cart_id,String delivery_date,String ordered_user_type,String item_id,String quantity,String price)
    {
        try {
            final VolleyWrapper manager = new VolleyWrapper(URL);
//            stud_id = studentsModelArrayList.get(0).getmId();
            String[] name = {JTAG_ACCESSTOKEN,"canteen_cart_id","delivery_date","ordered_user_type","student_id","parent_id","staff_id","item_id","quantity","price","portal"};
            String[] value = {PreferenceManager.getAccessToken(mContext),canteen_cart_id,delivery_date,ordered_user_type,student_id,parent_id,staff_id,item_id,quantity,price,"1"};
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
                                        BasketListActivity. getMyBasketList(URL_CANTEEN_CART_DETAIL,ordered_user_type,student_id,parent_id,staff_id);
                                    } else {
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
                                    getCartUpdate(URL,canteen_cart_id,delivery_date,ordered_user_type,item_id,quantity,price);

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
            final VolleyWrapper manager = new VolleyWrapper(URL);
//            stud_id = studentsModelArrayList.get(0).getmId();
            String[] name = {JTAG_ACCESSTOKEN,"canteen_cart_id","portal"};
            String[] value = {PreferenceManager.getAccessToken(mContext),canteen_cart_id,"1"};
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

                                        BasketListActivity. getMyBasketList(URL_CANTEEN_CART_DETAIL,ordered_user_type,student_id,parent_id,staff_id);
                                    } else {
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
