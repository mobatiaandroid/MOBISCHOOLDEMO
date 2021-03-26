package com.mobatia.naisapp.activities.canteen_new.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.room.util.StringUtil;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.canteen_new.CartActivity;
import com.mobatia.naisapp.activities.canteen_new.ItemListActivity;
import com.mobatia.naisapp.activities.canteen_new.PreOrderActivity;
import com.mobatia.naisapp.activities.canteen_new.model.CanteenDetailShowModel;
import com.mobatia.naisapp.activities.canteen_new.model.CartItemDetailModel;
import com.mobatia.naisapp.activities.canteen_new.model.CategoryModel;
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

import org.json.JSONObject;

import java.util.ArrayList;

public class CanteenItemRecyclerAdapter extends RecyclerView.Adapter<CanteenItemRecyclerAdapter.MyViewHolder> implements URLConstants, StatusConstants, JSONConstants, IntentPassValueConstants, NameValueConstants {

    private Context mContext;
    private ArrayList<CanteenDetailShowModel> mCartDetailArrayList;
    String delivery_date="";
    String ordered_user_type="";
    String student_id="";
    String parent_id="";
    String staff_id="";
    String selectedCategory="";
    String selectedCategoryID="";
    String selectedDate="";
    ArrayList<DateModel> mDateArrayList;
    ArrayList<CategoryModel> mCategoryArrayList;
    String dateList="";

    public class MyViewHolder extends RecyclerView.ViewHolder {

        Button cartStatusImg;
        TextView itemNameTxt,amountTxt,notAvailableTxt;
        ImageView itemImage;
        LinearLayout addLinear,multiLinear;
        ElegantNumberButton itemCount;
        public MyViewHolder(View view) {
            super(view);

//            cartStatusImg = (Button) view.findViewById(R.id.cartStatusImg);
            itemNameTxt = (TextView) view.findViewById(R.id.itemNameTxt);
            amountTxt = (TextView) view.findViewById(R.id.amountTxt);
            notAvailableTxt = (TextView) view.findViewById(R.id.notAvailableTxt);
            itemImage = (ImageView) view.findViewById(R.id.itemImage);
            addLinear = (LinearLayout) view.findViewById(R.id.addLinear);
            multiLinear = (LinearLayout) view.findViewById(R.id.multiLinear);
            itemCount=view.findViewById(R.id.itemCount);


        }
    }


    public CanteenItemRecyclerAdapter(Context mContext, ArrayList<CanteenDetailShowModel> mCartDetailArrayList, String delivery_date, String ordered_user_type, String student_id, String parent_id, String staff_id,String selectedCategory,String selectedCategoryID,String selectedDate,ArrayList<DateModel> mDateArrayList,ArrayList<CategoryModel> mCategoryArrayList,String dateList) {
        this.mContext = mContext;
        this.mCartDetailArrayList = mCartDetailArrayList;
        this.delivery_date = delivery_date;
        this.ordered_user_type = ordered_user_type;
        this.student_id = student_id;
        this.parent_id = parent_id;
        this.staff_id = staff_id;
        this.selectedCategory=selectedCategory;
        this.selectedCategoryID=selectedCategoryID;
        this.selectedDate=selectedDate;
        this.mDateArrayList=mDateArrayList;
        this.mCategoryArrayList=mCategoryArrayList;
        this.dateList=dateList;

    }

    @Override
    public CanteenItemRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_canteen_item_recycler_new, parent, false);

        return new CanteenItemRecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CanteenItemRecyclerAdapter.MyViewHolder holder, int position) {
        holder.amountTxt.setText(mCartDetailArrayList.get(position).getPrice()+" AED");
        if (mCartDetailArrayList.get(position).getAvailable_quantity().equalsIgnoreCase("0"))
        {
            holder.notAvailableTxt.setVisibility(View.VISIBLE);
       //     holder.cartStatusImg.setVisibility(View.INVISIBLE);
            holder.multiLinear.setVisibility(View.GONE);
            holder.addLinear.setVisibility(View.GONE);
        }
        else
        {
            holder.notAvailableTxt.setVisibility(View.GONE);
//            if (mCartDetailArrayList.get(position).getItemQuantity().equalsIgnoreCase("0"))
//            {
//                holder.addLinear.setVisibility(View.VISIBLE);
//                holder.multiLinear.setVisibility(View.GONE);
//            }
//            else
//            {
//                holder.addLinear.setVisibility(View.GONE);
//                holder.multiLinear.setVisibility(View.VISIBLE);
//            }


        }
        holder.addLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAddToCart(URL_CANTEEN_ADD_TO_CART,position,"1");
            }
        });
        holder.itemCount.setNumber(mCartDetailArrayList.get(position).getItemQuantity());

     holder.itemNameTxt.setText(mCartDetailArrayList.get(position).getItem_name());
        if (!mCartDetailArrayList.get(position).getItem_image().equalsIgnoreCase(""))
        {
            Picasso.with(mContext).load(AppUtils.replace(mCartDetailArrayList.get(position).getItem_image())).placeholder(R.drawable.canteendefaultitem).fit().into(holder.itemImage);
        }
        else
        {
                 holder.itemImage.setImageResource(R.drawable.canteendefaultitem);
        }

        holder.itemCount.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {

                System.out.println("New Value"+newValue);
                System.out.println("New Value clicked position"+position);
                if (newValue==0)
                {

                }
                else
                {
                    getAddToCart(URL_CANTEEN_ADD_TO_CART,position,String.valueOf(newValue));
                }

            }
        });

    }


    @Override
    public int getItemCount() {

        System.out.println("Item list size"+mCartDetailArrayList.size());
        return mCartDetailArrayList.size();
    }

    public  void getAddToCart(final String URL,int position,String quantity)
    {
        try {
            final VolleyWrapper manager = new VolleyWrapper(URL);
//            stud_id = studentsModelArrayList.get(0).getmId();
            String[] name = {JTAG_ACCESSTOKEN,"delivery_date","ordered_user_type","student_id","parent_id","staff_id","item_id","quantity","price","portal"};
            String[] value = {PreferenceManager.getAccessToken(mContext),delivery_date,ordered_user_type,student_id,parent_id,staff_id,mCartDetailArrayList.get(position).getId(),quantity,mCartDetailArrayList.get(position).getPrice(),"1"};
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
                                        Toast.makeText(mContext,"Item Successfully added to cart",Toast.LENGTH_SHORT).show();
                                        ItemListActivity.getItemList(URL_CANTEEN_ITEM_LIST,dateList,selectedCategory,selectedCategoryID,selectedDate,mDateArrayList,mCategoryArrayList);
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
                                    getAddToCart(URL,position,quantity);

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
