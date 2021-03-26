package com.mobatia.naisapp.activities.canteen_new;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener;
import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.canteen_new.adapter.ConfirmedDateRecyclerAdapter;
import com.mobatia.naisapp.activities.canteen_new.adapter.OrderHistoryRecyclerAdapter;
import com.mobatia.naisapp.activities.canteen_new.model.ConfirmedDataModel;
import com.mobatia.naisapp.activities.canteen_new.model.ConfirmedDetailModel;
import com.mobatia.naisapp.activities.canteen_new.model.DateModel;
import com.mobatia.naisapp.activities.canteen_new.model.OrderHistoryDateModel;
import com.mobatia.naisapp.activities.canteen_new.model.OrderHistoryDetailModel;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.constants.IntentPassValueConstants;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NameValueConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.parents_evening.model.StudentModel;
import com.mobatia.naisapp.fragments.sports.adapter.StrudentSpinnerAdapter;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.HeaderManager;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.recyclerviewmanager.DividerItemDecoration;
import com.mobatia.naisapp.recyclerviewmanager.ItemOffsetDecoration;
import com.mobatia.naisapp.recyclerviewmanager.RecyclerItemListener;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class OrderHistoryActivity extends Activity implements URLConstants, StatusConstants, JSONConstants, IntentPassValueConstants, NameValueConstants {
    Context mContext=this;
    String tab_type;
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back,btn_history,home;
    Bundle extras;
    String ordered_user_type="";
    String student_id="";
    String parent_id="";
    String staff_id="";
    ArrayList<OrderHistoryDateModel>mOrderHistoryArrayList;
    ArrayList<OrderHistoryDetailModel>mOrderHistoryDetailArrayList;
    RecyclerView dateRecyclerView;
    ArrayList<String>homeBannerUrlImageArray;
    ImageView noItemTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actiivity_order_history);
        mContext = this;
        initialiseUI();
        if (AppUtils.isNetworkConnected(mContext)) {
            getOrderHistory(URL_CANTEEN_PREORDER_HISTORY);
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
        headermanager = new HeaderManager(OrderHistoryActivity.this, tab_type);
        headermanager.getHeader(relativeHeader, 7);
        back = headermanager.getLeftButton();
        btn_history = headermanager.getRightHistoryImage();
        btn_history.setVisibility(View.VISIBLE);
        btn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(OrderHistoryActivity.this,BasketListActivity.class);
                intent.putExtra("ordered_user_type",ordered_user_type);
                intent.putExtra("tab_type","Basket");
                intent.putExtra("student_id",student_id);
                intent.putExtra("isFrom","Order");
                intent.putExtra("dateArray","");
                intent.putExtra("parent_id",PreferenceManager.getUserId(mContext));
                intent.putExtra("staff_id",PreferenceManager.getStaffId(mContext));
                finish();
                startActivity(intent);
            }
        });
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
        dateRecyclerView.setHasFixedSize(true);
        LinearLayoutManager lli = new LinearLayoutManager(mContext);
        lli.setOrientation(LinearLayoutManager.VERTICAL);
        int spacingItem = 5; // 50px
        ItemOffsetDecoration itemDecorationItem = new ItemOffsetDecoration(mContext,spacingItem);
        dateRecyclerView.addItemDecoration(itemDecorationItem);
        dateRecyclerView.setLayoutManager(lli);

    }

    public void getOrderHistory(final String URL)
    {
        mOrderHistoryArrayList=new ArrayList<>();

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
                                        JSONArray dataArray=respObject.getJSONArray("data");
                                        if (dataArray.length()>0)
                                        {
                                            for (int i = 0; i < dataArray.length(); i++)
                                            {
                                                JSONObject dataObject = dataArray.optJSONObject(i);
                                                OrderHistoryDateModel mModel=new OrderHistoryDateModel();
                                                mModel.setId(dataObject.optString("id"));
                                                mModel.setDelivery_date(dataObject.optString("delivery_date"));
                                                mModel.setStatus(dataObject.optString("status"));
                                                mOrderHistoryDetailArrayList=new ArrayList<>();
                                                JSONArray detailArray=dataObject.getJSONArray("canteen_preordered_items");
                                                if (detailArray.length()>0)
                                                {
                                                    for (int j=0;j<detailArray.length();j++)
                                                    {
                                                        JSONObject detailObject=detailArray.optJSONObject(j);
                                                        OrderHistoryDetailModel nModel=new OrderHistoryDetailModel();
                                                        nModel.setId(detailObject.optString("id"));
                                                        nModel.setItem_id(detailObject.optString("item_id"));
                                                        nModel.setQuantity(detailObject.optString("quantity"));
                                                        nModel.setItem_status(detailObject.optString("item_status"));
                                                        nModel.setItem_name(detailObject.optString("item_name"));
                                                        nModel.setItem_total(detailObject.optString("item_total"));
                                                        nModel.setPrice(detailObject.optString("price"));
                                                        nModel.setItem_description(detailObject.optString("item_description"));
                                                        homeBannerUrlImageArray = new ArrayList<>();
                                                        JSONArray imageArray = detailObject.getJSONArray("item_image");
                                                        if (imageArray.length() > 0) {
                                                            for (int m = 0; m < imageArray.length(); m++) {
                                                                homeBannerUrlImageArray.add(imageArray.optString(m));
                                                            }
                                                        }
                                                        nModel.setImageBanner(homeBannerUrlImageArray);
                                                        mOrderHistoryDetailArrayList.add(nModel);

                                                    }
                                                }
                                                mModel.setOrderHistoryDetailArrayList(mOrderHistoryDetailArrayList);
                                                mOrderHistoryArrayList.add(mModel);

                                            }

                                            OrderHistoryRecyclerAdapter adapter=new OrderHistoryRecyclerAdapter(mContext,mOrderHistoryArrayList);
                                            dateRecyclerView.setAdapter(adapter);
                                            noItemTxt.setVisibility(View.GONE);
                                            dateRecyclerView.setVisibility(View.VISIBLE);
                                        }
                                        else
                                        {
                                            noItemTxt.setVisibility(View.VISIBLE);
                                            dateRecyclerView.setVisibility(View.GONE);
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
                                    getOrderHistory(URL);

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
