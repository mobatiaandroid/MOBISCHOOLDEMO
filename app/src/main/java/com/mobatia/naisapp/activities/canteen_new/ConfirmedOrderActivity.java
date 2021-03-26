package com.mobatia.naisapp.activities.canteen_new;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
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
import com.mobatia.naisapp.activities.canteen_new.adapter.CartDateRecyclerAdapter;
import com.mobatia.naisapp.activities.canteen_new.adapter.ConfirmedDateRecyclerAdapter;
import com.mobatia.naisapp.activities.canteen_new.adapter.DateRecyclerAdapter;
import com.mobatia.naisapp.activities.canteen_new.model.CartItemDateModel;
import com.mobatia.naisapp.activities.canteen_new.model.CartItemDetailModel;
import com.mobatia.naisapp.activities.canteen_new.model.CategoryModel;
import com.mobatia.naisapp.activities.canteen_new.model.ConfirmedDataModel;
import com.mobatia.naisapp.activities.canteen_new.model.ConfirmedDetailModel;
import com.mobatia.naisapp.activities.canteen_new.model.DateModel;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
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

public class ConfirmedOrderActivity extends Activity implements URLConstants, StatusConstants, JSONConstants, IntentPassValueConstants, NameValueConstants {
    static Context mContext;
    String tab_type;
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back, btn_history, home;
    Bundle extras;
    static   String ordered_user_type="";
    static  String student_id="";
    static  String parent_id="";
    static  String staff_id="";
    static  RecyclerView dateRecyclerView;
    static ArrayList<ConfirmedDataModel> mDateArrayList;
    static ArrayList<ConfirmedDetailModel> mCartItemArrayList;
    static ImageView noItemTxt;
        static TextView amountTxt,itemTxt;
     static    LinearLayout bottomLinear,itemLinear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmed_order);
        mContext = this;
        initialiseUI();
        if (AppUtils.isNetworkConnected(mContext)) {
            getConfirmedOrderList(URL_CANTEEN_CONFIRMED_ORDER_ITEM,ordered_user_type,student_id,parent_id,staff_id);
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
        headermanager = new HeaderManager(ConfirmedOrderActivity.this, tab_type);
        headermanager.getHeader(relativeHeader, 6);
        back = headermanager.getLeftButton();
        btn_history = headermanager.getRightHistoryImage();
        btn_history.setVisibility(View.INVISIBLE);
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
        noItemTxt = (ImageView) findViewById(R.id.noItemTxt);
        dateRecyclerView = findViewById(R.id.dateRecyclerView);
        amountTxt = findViewById(R.id.amountTxt);
        bottomLinear = findViewById(R.id.bottomLinear);
        itemLinear = findViewById(R.id.itemLinear);
        itemTxt = findViewById(R.id.itemTxt);
        dateRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        int spacing = 5; // 50px
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext,spacing);
        dateRecyclerView.addItemDecoration(itemDecoration);
        dateRecyclerView.setLayoutManager(llm);

    }

    public static void getConfirmedOrderList(final String URL,final String ordered_user_type,final String student_id,final String parent_id,final String staff_id)
    {
        try {
            mDateArrayList = new ArrayList<>();

            final VolleyWrapper manager = new VolleyWrapper(URL);
//            stud_id = studentsModelArrayList.get(0).getmId();
            String[] name = {JTAG_ACCESSTOKEN,"ordered_user_type","student_id","parent_id","staff_id","portal"};
            String[] value = {PreferenceManager.getAccessToken(mContext),ordered_user_type,student_id,parent_id,staff_id,"1"};
            System.out.println("Values ::::" + PreferenceManager.getAccessToken(mContext) + "hfhgfhdfghd::::" + PreferenceManager.getStaffId(mContext));
            manager.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {

                @Override
                public void responseSuccess(String successResponse) {
                    Log.e("responseSuccess cItem", successResponse);
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
                                            noItemTxt.setVisibility(View.GONE);
                                            bottomLinear.setVisibility(View.VISIBLE);
                                            for (int i=0;i<dataArray.length();i++)
                                            {
                                                JSONObject dataObject = dataArray.optJSONObject(i);
                                                ConfirmedDataModel mModel=new ConfirmedDataModel();
                                                mModel.setDelivery_date(dataObject.optString("delivery_date"));
                                                mModel.setId(dataObject.optString("id"));
                                                mModel.setTotal_amount(dataObject.optString("total_amount"));
                                                mModel.setStatus(dataObject.optString("status"));
                                                mModel.setCancellation_time_exceed(dataObject.optInt("cancellation_time_exceed"));
                                                mCartItemArrayList = new ArrayList<>();
                                                JSONArray itemsArray=dataObject.getJSONArray("canteen_preordered_items");
                                                System.out.println("It works outer");
                                                if (itemsArray.length()>0)
                                                {
                                                    for (int j=0;j<itemsArray.length();j++)
                                                    {
                                                        System.out.println("It works inner");
                                                        JSONObject itemObject=itemsArray.optJSONObject(j);
                                                        ConfirmedDetailModel model=new ConfirmedDetailModel();
                                                        model.setId(itemObject.optString("id"));
                                                        model.setItem_id(itemObject.optString("item_id"));
                                                        model.setQuantity(itemObject.optString("quantity"));
                                                        model.setPrice(itemObject.optString("price"));
                                                        model.setItem_name(itemObject.optString("item_name"));
                                                        model.setItem_image(itemObject.optString("item_image"));
                                                        model.setItem_status(itemObject.optString("item_status"));
                                                        model.setAvailable_quantity(itemObject.optString("available_quantity"));
                                                        model.setItem_total(itemObject.optInt("item_total"));
                                                        mCartItemArrayList.add(model);

                                                    }

                                                }
                                                mModel.setmConfirmedDetailArrayList(mCartItemArrayList);
                                                mDateArrayList.add(mModel);
                                            }
                                            boolean isActiveFound=false;
                                            for (int m=0;m<mDateArrayList.size();m++)
                                            {
                                                if (mDateArrayList.get(m).getStatus().equalsIgnoreCase("1"))
                                                {
                                                    isActiveFound=true;
                                                }
                                            }
                                            if (isActiveFound)
                                            {
                                                itemLinear.setVisibility(View.VISIBLE);
                                            }
                                            else
                                            {
                                                itemLinear.setVisibility(View.GONE);
                                            }
                                            int totalAmt=0;
                                            int totalItems=0;
                                            for (int i=0;i<mDateArrayList.size();i++)
                                            {
                                                if (mDateArrayList.get(i).getStatus().equalsIgnoreCase("1"))
                                                {
                                                    totalAmt=totalAmt+Integer.parseInt(mDateArrayList.get(i).getTotal_amount());
                                                }

                                                for (int j=0;j<mDateArrayList.get(i).getmConfirmedDetailArrayList().size();j++)
                                                {
                                                    if (mDateArrayList.get(i).getmConfirmedDetailArrayList().get(j).getItem_status().equalsIgnoreCase("1"))
                                                    {
                                                        totalItems=totalItems+Integer.parseInt(mDateArrayList.get(i).getmConfirmedDetailArrayList().get(j).getQuantity());
                                                    }

                                                }

                                            }

                                            amountTxt.setText(String.valueOf(totalAmt)+" AED");
                                            itemTxt.setText(String.valueOf(totalItems)+" Items");
                                            System.out.println("Array Size"+mDateArrayList.get(0).getmConfirmedDetailArrayList().size());
                                            dateRecyclerView.setVisibility(View.VISIBLE);
                                            ConfirmedDateRecyclerAdapter adapter = new ConfirmedDateRecyclerAdapter(mContext,mDateArrayList,ordered_user_type,student_id,parent_id,staff_id);
                                            dateRecyclerView.setAdapter(adapter);
                                        }
                                        else {
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
                                    getConfirmedOrderList(URL,ordered_user_type,student_id,parent_id,staff_id);

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

