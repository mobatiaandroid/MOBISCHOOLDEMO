package com.mobatia.naisapp.activities.canteen_new.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.canteen_new.CartActivity;
import com.mobatia.naisapp.activities.canteen_new.model.CartItemDateModel;
import com.mobatia.naisapp.activities.canteen_new.model.CartItemDetailModel;
import com.mobatia.naisapp.activities.canteen_new.model.DateModel;
import com.mobatia.naisapp.constants.IntentPassValueConstants;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NameValueConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CartDetailRecyclerAdapter extends RecyclerView.Adapter<CartDetailRecyclerAdapter.MyViewHolder> implements URLConstants, StatusConstants, JSONConstants, IntentPassValueConstants, NameValueConstants {

    private Context mContext;
    private ArrayList<CartItemDetailModel> mCartDetailArrayList;
    String dept;
    String canteen_cart_id="";
    String delivery_date="";
    String ordered_user_type="";
    String student_id="";
    String parent_id="";
    String staff_id="";
    String item_id="";
    String quantity="";
    String price="";

    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView cartitemname,cartitemprice,monthTxt;
        ImageView cartitemimage,remove_itemcart;
        RelativeLayout bgLinear;
        ElegantNumberButton cartitemcount;
        Button save;
        public MyViewHolder(View view) {
            super(view);

            cartitemname = (TextView) view.findViewById(R.id.cartitemname);
            cartitemprice = (TextView) view.findViewById(R.id.cartitemprice);
            cartitemimage = (ImageView) view.findViewById(R.id.cartitemimage);
            remove_itemcart = (ImageView) view.findViewById(R.id.remove_itemcart);
            cartitemcount = view.findViewById(R.id.cartitemcount);
            save = view.findViewById(R.id.save);

        }
    }


    public CartDetailRecyclerAdapter(Context mContext, ArrayList<CartItemDetailModel> mCartDetailArrayList,String delivery_date,String ordered_user_type,String student_id,String parent_id,String staff_id) {
        this.mContext = mContext;
        this.mCartDetailArrayList = mCartDetailArrayList;
        this.delivery_date = delivery_date;
        this.ordered_user_type = ordered_user_type;
        this.student_id = student_id;
        this.parent_id = parent_id;
        this.staff_id = staff_id;

    }

    @Override
    public CartDetailRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_cart_item_recycler, parent, false);

        return new CartDetailRecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CartDetailRecyclerAdapter.MyViewHolder holder, int position) {
        holder.save.setVisibility(View.INVISIBLE);
        holder.cartitemprice.setText(mCartDetailArrayList.get(position).getPrice()+" AED");
        holder.cartitemname.setText(mCartDetailArrayList.get(position).getItem_name());
        if (!mCartDetailArrayList.get(position).getItem_image().equals("")) {

            Picasso.with(mContext).load(AppUtils.replace(mCartDetailArrayList.get(position).getItem_image().toString())).placeholder(R.drawable.canteendefaultitem).fit().into(holder.cartitemimage);
        }
        else
        {

            holder.cartitemimage.setImageResource(R.drawable.canteendefaultitem);
        }
        holder.cartitemcount.setNumber(mCartDetailArrayList.get(position).getQuantity().trim());


        holder.cartitemcount.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {

                System.out.println("New Value"+newValue);
                System.out.println("New Value clicked position"+position);
                canteen_cart_id=mCartDetailArrayList.get(position).getId();

                quantity=String.valueOf(newValue);
                item_id=mCartDetailArrayList.get(position).getItem_id();
                price=mCartDetailArrayList.get(position).getPrice();
                System.out.println("New Value canteen_cart_id"+canteen_cart_id);
                int cartQuantity=Integer.valueOf(mCartDetailArrayList.get(position).getAvailable_quantity());
                holder.cartitemcount.setRange(1,cartQuantity);
                holder.save.setVisibility(View.INVISIBLE);
                if (AppUtils.isNetworkConnected(mContext)) {
                    getCartUpdate(URL_CANTEEN_CART_UPDATE,canteen_cart_id,delivery_date,ordered_user_type,item_id,quantity,price);
                } else {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", mContext.getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                }
            }
        });
        holder.remove_itemcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getCartCancel(URL_CANTEEN_REMOVE_CART_ITEM,mCartDetailArrayList.get(position).getId());
            }
        });
    }


    @Override
    public int getItemCount() {
        return mCartDetailArrayList.size();
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

                                        CartActivity.getCartDetail(URL_CANTEEN_CART_DETAIL);
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

                                        CartActivity.getCartDetail(URL_CANTEEN_CART_DETAIL);
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
