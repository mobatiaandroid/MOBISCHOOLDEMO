package com.mobatia.naisapp.activities.canteen_new;

import android.app.Activity;
import android.app.AppComponentFactory;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.canteen_new.adapter.MyOrderRecyclerAdapter;
import com.mobatia.naisapp.activities.canteen_new.adapter.OrderHistoryRecyclerAdapter;
import com.mobatia.naisapp.activities.canteen_new.model.MyOrderDetailModel;
import com.mobatia.naisapp.activities.canteen_new.model.MyOrderModel;
import com.mobatia.naisapp.activities.canteen_new.model.OrderHistoryDateModel;
import com.mobatia.naisapp.activities.canteen_new.model.OrderHistoryDetailModel;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.appcontroller.AppController;
import com.mobatia.naisapp.constants.IntentPassValueConstants;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NameValueConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.HeaderManager;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.recyclerviewmanager.ItemOffsetDecoration;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyOrderActivity extends Activity implements URLConstants, StatusConstants, JSONConstants, IntentPassValueConstants, NameValueConstants {
    static Context mContext;
    String tab_type;
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back,btn_history,home;
    Bundle extras;
    static String ordered_user_type="";
    static String student_id="";
    static String parent_id="";
    static String staff_id="";
    static String totalAmount="";

    static int totalItems=0;
    static ArrayList<MyOrderModel>mMyOrderArrayList;
    static ArrayList<MyOrderDetailModel>mMyOrderDetailArrayList;
    static RecyclerView dateRecyclerView;
    static ImageView noItemTxt;
    static LinearLayout bottomLinear;
    static TextView amountTxt;
    static TextView itemTxt;
    static LinearLayout itemLinear;
   static ArrayList<String>homeBannerUrlImageArray;
    String walletAmountString="0";
   static int WalletAmount=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        mContext = this;
        initialiseUI();
        if (AppUtils.isNetworkConnected(mContext)) {
            AppController.myHistoryPosition=0;
            getMyOrder(URL_CANTEEN_CONFIRMED_ORDER_ITEM,ordered_user_type,student_id,parent_id,staff_id);
            if(PreferenceManager.getUserId(mContext).equalsIgnoreCase(""))
            {
                getWalletStaff(URL_CANTEEN_STAFF_WALLET,staff_id);
            }
            else
            {
                getWallet(URL_CANTEEN_WALLET, student_id);
            }
        } else {
            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

        }
    }

    private void initialiseUI() {
        extras = getIntent().getExtras();
        if (extras != null) {
            tab_type = extras.getString("tab_type");
            ordered_user_type = extras.getString("ordered_user_type");
            student_id = extras.getString("student_id");
            parent_id = extras.getString("parent_id");
            staff_id = extras.getString("staff_id");
        }

        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        headermanager = new HeaderManager(MyOrderActivity.this, tab_type);
        headermanager.getHeader(relativeHeader, 7);
        back = headermanager.getLeftButton();
        btn_history = headermanager.getRightHistoryImage();
        headermanager.setButtonLeftSelector(R.drawable.back,
                R.drawable.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.hideKeyBoard(mContext);
                finish();
            }
        });

        home = headermanager.getLogoButton();
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(mContext, HomeListAppCompatActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
            }
        });

        dateRecyclerView=findViewById(R.id.dateRecyclerView);
        noItemTxt=findViewById(R.id.noItemTxt);
        bottomLinear=findViewById(R.id.bottomLinear);
        amountTxt=findViewById(R.id.amountTxt);
        itemTxt=findViewById(R.id.itemTxt);
        itemLinear=findViewById(R.id.itemLinear);
        dateRecyclerView.setHasFixedSize(true);
        LinearLayoutManager lli = new LinearLayoutManager(mContext);
        lli.setOrientation(LinearLayoutManager.VERTICAL);
        int spacingItem = 5; // 50px
        ItemOffsetDecoration itemDecorationItem = new ItemOffsetDecoration(mContext,spacingItem);
        dateRecyclerView.addItemDecoration(itemDecorationItem);
        dateRecyclerView.setLayoutManager(lli);
        btn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(MyOrderActivity.this,BasketListActivity.class);
                intent.putExtra("ordered_user_type",ordered_user_type);
                intent.putExtra("tab_type","Basket");
                intent.putExtra("student_id",student_id);
                intent.putExtra("isFrom","My Orders");
                intent.putExtra("dateArray","");
                intent.putExtra("parent_id",PreferenceManager.getUserId(mContext));
                intent.putExtra("staff_id",PreferenceManager.getStaffId(mContext));
                finish();
                startActivity(intent);
            }
        });
    }

    public static void getMyOrder(final String URL,String ordered_user_type, String student_id, String parent_id, String staff_id)
    {
        mMyOrderArrayList=new ArrayList<>();

        try {

            final VolleyWrapper manager = new VolleyWrapper(URL);
            String[] name = {JTAG_ACCESSTOKEN,"ordered_user_type","student_id","parent_id","staff_id","portal"};
            String[] value = {PreferenceManager.getAccessToken(mContext),ordered_user_type,student_id,parent_id,staff_id,"1"};
            System.out.println("Values ::::" + PreferenceManager.getAccessToken(mContext) + "hfhgfhdfghd::::" + PreferenceManager.getStaffId(mContext));
            manager.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {

                @Override
                public void responseSuccess(String successResponse) {
                    Log.e("responseSuccess cItem", successResponse);
                    System.out.println("response order history"+successResponse);
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
                                        totalAmount=respObject.optString("whole_total");
                                        System.out.println("Total Amount my order"+String.valueOf(totalAmount));

                                        JSONArray dataArray=respObject.getJSONArray("data");
                                        if (dataArray.length()>0)
                                        {
                                            for (int i=0;i<dataArray.length();i++)
                                            {
                                                JSONObject dataObject = dataArray.optJSONObject(i);
                                                MyOrderModel mModel=new MyOrderModel();
                                                mModel.setId(dataObject.optString("id"));
                                                mModel.setDelivery_date(dataObject.optString("delivery_date"));
                                                mModel.setTotal_amount(dataObject.optString("total_amount"));
                                                mModel.setStatus(dataObject.optString("status"));
                                                mModel.setPickup_location(dataObject.optString("pickup_location"));
                                                mModel.setCancellation_time_exceed(dataObject.optString("cancellation_time_exceed"));
                                                mMyOrderDetailArrayList=new ArrayList<>();
                                                JSONArray detailArray=dataObject.getJSONArray("canteen_preordered_items");
                                                if (detailArray.length()>0)
                                                {
                                                    for (int j=0;j<detailArray.length();j++)
                                                    {
                                                        JSONObject detailObject = detailArray.optJSONObject(j);
                                                        MyOrderDetailModel nModel=new MyOrderDetailModel();
                                                        nModel.setId(detailObject.optString("id"));
                                                        nModel.setItem_id(detailObject.optString("item_id"));
                                                        nModel.setQuantity(detailObject.optString("quantity"));
                                                        nModel.setItem_status(detailObject.optString("item_status"));
                                                        nModel.setItem_name(detailObject.optString("item_name"));
                                                        nModel.setPrice(detailObject.optString("price"));
                                                        nModel.setItem_total(detailObject.optInt("item_total"));
                                                        nModel.setPortal(detailObject.optString("portal"));
                                                        nModel.setAvailable_quantity(detailObject.optString("available_quantity"));
                                                        nModel.setItem_description(detailObject.optString("item_description"));
                                                        nModel.setCancellation_time_exceed(detailObject.optString("cancellation_time_exceed"));
                                                        homeBannerUrlImageArray = new ArrayList<>();
                                                        JSONArray imageArray = detailObject.getJSONArray("item_image");
                                                        if (imageArray.length() > 0) {
                                                            for (int m = 0; m < imageArray.length(); m++) {
                                                                homeBannerUrlImageArray.add(imageArray.optString(m));
                                                            }
                                                        }
                                                        nModel.setImageBanner(homeBannerUrlImageArray);
                                                        mMyOrderDetailArrayList.add(nModel);


                                                    }
                                                }
                                                mModel.setmMyOrderDetailArrayList(mMyOrderDetailArrayList);
                                                if (mMyOrderArrayList.size()==0)
                                                {
                                                    mMyOrderArrayList.add(mModel);
                                                }
                                                else
                                                {
                                                    String insertID=dataObject.optString("id");
                                                    boolean isFoundItem=false;
                                                    for (int k=0;k<mMyOrderArrayList.size();k++)
                                                    {
                                                        if (mMyOrderArrayList.get(k).getId().equalsIgnoreCase(insertID))
                                                        {
                                                            isFoundItem=true;
                                                        }
                                                    }

                                                    if (!isFoundItem)
                                                    {
                                                        mMyOrderArrayList.add(mModel);
                                                    }
                                                }

                                            }
                                            dateRecyclerView.setVisibility(View.VISIBLE);
                                            bottomLinear.setVisibility(View.VISIBLE);
                                            noItemTxt.setVisibility(View.GONE);
                                            totalItems=0;
                                            for(int m=0;m<mMyOrderArrayList.size();m++)
                                            {
                                                for (int n=0;n<mMyOrderArrayList.get(m).getmMyOrderDetailArrayList().size();n++)
                                                {
                                                    if (!mMyOrderArrayList.get(m).getmMyOrderDetailArrayList().get(n).getItem_status().equalsIgnoreCase("0"))
                                                    {
                                                        totalItems=totalItems+Integer.parseInt(mMyOrderArrayList.get(m).getmMyOrderDetailArrayList().get(n).getQuantity());
                                                    }
                                                }

                                            }
                                            if (totalItems==0)
                                            {
                                                itemLinear.setVisibility(View.GONE);
                                            }
                                            else
                                            {
                                                itemLinear.setVisibility(View.VISIBLE);
                                            }
                                            amountTxt.setText(totalAmount+"AED ");
                                            itemTxt.setText(String.valueOf(totalItems)+" items");
                                            MyOrderRecyclerAdapter mAdapter=new MyOrderRecyclerAdapter(mContext,mMyOrderArrayList,ordered_user_type,student_id,parent_id,staff_id,totalAmount,WalletAmount);
                                            dateRecyclerView.setAdapter(mAdapter);
                                        }
                                        else
                                        {
                                            noItemTxt.setVisibility(View.VISIBLE);
                                            dateRecyclerView.setVisibility(View.GONE);
                                            bottomLinear.setVisibility(View.GONE);
                                        }


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
                                    getMyOrder(URL,ordered_user_type,student_id,parent_id,staff_id);

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

    @Override
    protected void onResume() {
        super.onResume();
        if (AppUtils.isNetworkConnected(mContext)) {
            if (PreferenceManager.getUserId(mContext).equalsIgnoreCase(""))
            {
                ordered_user_type="2";
                student_id="";
                parent_id="";
                staff_id=PreferenceManager.getStaffId(mContext);
            }
            else
            {
                ordered_user_type="1";
                student_id=PreferenceManager.getCanteenStudentId(mContext);
                parent_id=PreferenceManager.getUserId(mContext);
                staff_id="";
            }
            getMyOrder(URL_CANTEEN_CONFIRMED_ORDER_ITEM,ordered_user_type,student_id,parent_id,staff_id);
        } else {
            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

        }
    }

    public void getWallet(final String URL, final String student_id)
    {

        try {
            System.out.println("Student id api click" + student_id);
            final VolleyWrapper manager = new VolleyWrapper(URL);
//            stud_id = studentsModelArrayList.get(0).getmId();
            String[] name = {JTAG_ACCESSTOKEN, "student_id","portal"};
            String[] value = {PreferenceManager.getAccessToken(mContext), student_id,"1"};
            System.out.println("Values ::::" + PreferenceManager.getAccessToken(mContext) + "hfhgfhdfghd::::" + student_id);
            manager.getResponsePOST(mContext, 14, name, value, new VolleyWrapper.ResponseListener() {

                @Override
                public void responseSuccess(String successResponse) {
                    Log.e("responseSuccess: ", successResponse);
                    String responsCode = "";
                    if (successResponse != null) {
                        try {
                            JSONObject rootObject = new JSONObject(successResponse);
                            if (rootObject.optString(JTAG_RESPONSE) != null) {
                                responsCode = rootObject.optString(JTAG_RESPONSECODE);
                                if (responsCode.equals(RESPONSE_SUCCESS)) {
                                    JSONObject respObject = rootObject.getJSONObject(JTAG_RESPONSE);
                                    String statusCode = respObject.optString(JTAG_STATUSCODE);
                                    if (statusCode.equals(STATUS_SUCCESS)) {
                                        String walletAmount = respObject.optString("wallet_amount");
                                        if (walletAmount.equalsIgnoreCase("null")|| walletAmount.equalsIgnoreCase(""))
                                        {
                                            walletAmountString="0";
                                        }
                                        else
                                        {
                                            walletAmountString=walletAmount;
                                        }
                                        WalletAmount=Integer.parseInt(walletAmountString);

                                    } else {
                                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                                    }

                                } else if (responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) ||
                                        responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                                        responsCode.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
                                    AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                                        @Override
                                        public void getAccessToken() {
                                        }
                                    });
                                    getWallet(URL, student_id);

                                } else if (responsCode.equals(RESPONSE_ERROR)) {
//								CustomStatusDialog(RESPONSE_FAILURE);
                                    //Toast.makeText(mContext,"Failure",Toast.LENGTH_SHORT).show();
                                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                                }
                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                    }
                }

                @Override
                public void responseFailure(String failureResponse) {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    public void getWalletStaff( String URL,  String staffID)
    {

        try {
            System.out.println("Student id api click" + staffID);
            final VolleyWrapper manager = new VolleyWrapper(URL);
//            stud_id = studentsModelArrayList.get(0).getmId();
            String[] name = {JTAG_ACCESSTOKEN, "staff_id","portal"};
            String[] value = {PreferenceManager.getAccessToken(mContext), staffID,"1"};
            System.out.println("Values ::::" + PreferenceManager.getAccessToken(mContext) + "hfhgfhdfghd::::" + staffID);
            manager.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {

                @Override
                public void responseSuccess(String successResponse) {
                    Log.e("responseSuccess: ", successResponse);
                    String responsCode = "";
                    if (successResponse != null) {
                        try {
                            JSONObject rootObject = new JSONObject(successResponse);
                            if (rootObject.optString(JTAG_RESPONSE) != null) {
                                responsCode = rootObject.optString(JTAG_RESPONSECODE);
                                if (responsCode.equals(RESPONSE_SUCCESS)) {
                                    JSONObject respObject = rootObject.getJSONObject(JTAG_RESPONSE);
                                    String statusCode = respObject.optString(JTAG_STATUSCODE);
                                    if (statusCode.equals(STATUS_SUCCESS)) {
                                        String walletAmount = respObject.optString("wallet_amount");
                                        if (walletAmount.equalsIgnoreCase("null")|| walletAmount.equalsIgnoreCase(""))
                                        {
                                            walletAmountString="0";
                                        }
                                        else
                                        {
                                            walletAmountString=walletAmount;
                                        }
                                        WalletAmount=Integer.parseInt(walletAmountString);

                                    } else {
                                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                                    }

                                } else if (responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) ||
                                        responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                                        responsCode.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
                                    AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                                        @Override
                                        public void getAccessToken() {
                                        }
                                    });
                                    getWallet(URL, staffID);

                                } else if (responsCode.equals(RESPONSE_ERROR)) {
//								CustomStatusDialog(RESPONSE_FAILURE);
                                    //Toast.makeText(mContext,"Failure",Toast.LENGTH_SHORT).show();
                                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                                }
                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                    }
                }

                @Override
                public void responseFailure(String failureResponse) {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
