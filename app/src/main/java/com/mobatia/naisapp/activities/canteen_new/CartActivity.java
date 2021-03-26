package com.mobatia.naisapp.activities.canteen_new;

import android.app.Activity;
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
import com.mobatia.naisapp.activities.canteen_new.adapter.CartDateRecyclerAdapter;
import com.mobatia.naisapp.activities.canteen_new.adapter.CategoryRecyclerAdpter;
import com.mobatia.naisapp.activities.canteen_new.adapter.DateRecyclerAdapter;
import com.mobatia.naisapp.activities.canteen_new.model.CartItemDateModel;
import com.mobatia.naisapp.activities.canteen_new.model.CartItemDetailModel;
import com.mobatia.naisapp.activities.canteen_new.model.CategoryModel;
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
import com.mobatia.naisapp.recyclerviewmanager.RecyclerItemListener;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CartActivity extends Activity implements URLConstants, StatusConstants, JSONConstants, IntentPassValueConstants, NameValueConstants {
    static Context mContext;
    String tab_type;
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back, btn_history, home;
    Bundle extras;
    TextView singleDateViewTxt;
    static ArrayList<CartItemDateModel> mDateArrayList;
    static ArrayList<CartItemDetailModel> mCartItemArrayList;
    static  RecyclerView dateRecyclerView;
    static   String ordered_user_type="";
    static  String student_id="";
    static  String parent_id="";
    static  String staff_id="";
    static  TextView noItemTxt,amountTxt,itemTxt;
   static LinearLayout itemLinear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_detail);
        mContext = this;
        initialiseUI();
        if (AppUtils.isNetworkConnected(mContext)) {
            getCartDetail(URL_CANTEEN_CART_DETAIL);
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
        singleDateViewTxt = (TextView) findViewById(R.id.singleDateViewTxt);
        noItemTxt = (TextView) findViewById(R.id.noItemTxt);
        headermanager = new HeaderManager(CartActivity.this, tab_type);
        headermanager.getHeader(relativeHeader, 6);
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
        dateRecyclerView = findViewById(R.id.dateRecyclerView);
        itemTxt = findViewById(R.id.itemTxt);
        amountTxt = findViewById(R.id.amountTxt);
        itemLinear = findViewById(R.id.itemLinear);
        dateRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        int spacing = 5; // 50px
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext,spacing);
        dateRecyclerView.addItemDecoration(itemDecoration);
        dateRecyclerView.setLayoutManager(llm);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(mContext, HomeListAppCompatActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
            }
        });
        btn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CartActivity.this,ConfirmedOrderActivity.class);
                intent.putExtra("ordered_user_type",ordered_user_type);
                intent.putExtra("tab_type","History");
                intent.putExtra("student_id",student_id);
                intent.putExtra("parent_id",PreferenceManager.getUserId(mContext));
                intent.putExtra("staff_id",staff_id);
                startActivity(intent);
            }
        });

    }


    public static void getCartDetail(final String URL)
    {
        try {
            mDateArrayList = new ArrayList<>();
            mCartItemArrayList = new ArrayList<>();
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
                                            for (int i=0;i<dataArray.length();i++)
                                            {
                                                JSONObject dataObject = dataArray.optJSONObject(i);
                                                CartItemDateModel mModel=new CartItemDateModel();
                                                mModel.setDelivery_date(dataObject.optString("delivery_date"));
                                                mModel.setTotal_amount(dataObject.optInt("total_amount"));
                                                JSONArray itemsArray=dataObject.getJSONArray("items");
                                                for (int j=0;j<itemsArray.length();j++)
                                                {
                                                    JSONObject itemObject=itemsArray.optJSONObject(j);
                                                    CartItemDetailModel model=new CartItemDetailModel();
                                                    model.setId(itemObject.optString("id"));
                                                    model.setItem_id(itemObject.optString("item_id"));
                                                    model.setQuantity(itemObject.optString("quantity"));
                                                    model.setPrice(itemObject.optString("price"));
                                                    model.setItem_name(itemObject.optString("item_name"));
                                                    model.setItem_image(itemObject.optString("item_image"));
                                                    model.setAvailable_quantity(itemObject.optString("available_quantity"));
                                                    mCartItemArrayList.add(model);

                                                }
                                                mModel.setmCartItemDetailArrayList(mCartItemArrayList);
                                                mDateArrayList.add(mModel);
                                            }

                                            int totalItems=0;
                                            float totalAmount=0;
                                            for (int i=0;i<mDateArrayList.size();i++)
                                            {
                                                for (int j=0;j<mCartItemArrayList.size();j++)
                                                {
                                                    totalItems=totalItems+Integer.parseInt(mCartItemArrayList.get(j).getQuantity());
                                                }
                                                totalAmount=totalAmount+mDateArrayList.get(i).getTotal_amount();

                                            }
                                            itemTxt.setText(String.valueOf(totalItems+" Item"));
                                            amountTxt.setText(String.valueOf(totalAmount+" AED"));
//                                            for (int i = 0; i < dataArray.length(); i++) {
////                                                JSONObject dataObject = dataArray.optJSONObject(i);
////                                                mCategoryArrayList.add(addEventsDetails(dataObject));
////                                            }
////
////                                            CategoryRecyclerAdpter adapter=new CategoryRecyclerAdpter(mContext,mCategoryArrayList);
////                                            categoryRecyclerView.setAdapter(adapter);

                                            dateRecyclerView.setVisibility(View.VISIBLE);
                                            itemLinear.setVisibility(View.VISIBLE);
                                            CartDateRecyclerAdapter adapter = new CartDateRecyclerAdapter(mContext,mDateArrayList,ordered_user_type,student_id,parent_id,staff_id);
                                            dateRecyclerView.setAdapter(adapter);
                                        }
                                        else {
                                            noItemTxt.setVisibility(View.VISIBLE);
                                            dateRecyclerView.setVisibility(View.GONE);
                                            itemLinear.setVisibility(View.GONE);
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
                                    getCartDetail(URL);

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

