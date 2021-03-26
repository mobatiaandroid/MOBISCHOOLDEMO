package com.mobatia.naisapp.activities.canteen_new;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.canteen.CanteenFirstActivity;
import com.mobatia.naisapp.activities.canteen.CanteenFirstStaffActivity;
import com.mobatia.naisapp.activities.canteen_new.adapter.BasketRecyclerAdapter;
import com.mobatia.naisapp.activities.canteen_new.adapter.MyOrderRecyclerAdapter;
import com.mobatia.naisapp.activities.canteen_new.model.BasketDetailModel;
import com.mobatia.naisapp.activities.canteen_new.model.BasketModel;
import com.mobatia.naisapp.activities.canteen_new.model.DateModel;
import com.mobatia.naisapp.activities.canteen_new.model.ItemsModel;
import com.mobatia.naisapp.activities.canteen_new.model.MyOrderDetailModel;
import com.mobatia.naisapp.activities.canteen_new.model.MyOrderModel;
import com.mobatia.naisapp.activities.canteen_new.model.OrdersModel;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.activities.login.LoginActivity;
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

import org.bouncycastle.i18n.filter.HTMLFilter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class BasketListActivity extends Activity implements URLConstants, StatusConstants, JSONConstants, IntentPassValueConstants, NameValueConstants {
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
    static int cart_totoal=0;
    static int total_items_in_cart=0;
    ArrayList<DateModel> mDateArrayList;
    static ArrayList<BasketModel> mMyBasketListArrayList;
    static ArrayList<BasketDetailModel>mMyBasketDetailArrayList;
    static RecyclerView dateRecyclerView;
    static ImageView noItemTxt;
    static LinearLayout itemLinear;
    static TextView amountTxt;
    static TextView itemTxt;
    ArrayList<ItemsModel>itemArray;
    ArrayList<OrdersModel>orderArray;
    static ArrayList<String>homeBannerUrlImageArray;
    String walletAmountString="0";
    int WalletAmount=0;
    static int CartTotalAmount=0;
    int BalanceWalletAmount=0;
    int BalanceConfirmWalletAmount=0;
    static   int TotalOrderedAmount=0;

    String isFrom="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
        mContext = this;
        initialiseUI();
        if (AppUtils.isNetworkConnected(mContext)) {
            AppController.basketPosition=0;
            getMyBasketList(URL_CANTEEN_CART_DETAIL,ordered_user_type,student_id,parent_id,staff_id);
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
            isFrom = extras.getString("isFrom");
            if (isFrom.equalsIgnoreCase("Items"))
            {
                mDateArrayList = (ArrayList<DateModel>) getIntent().getSerializableExtra("dateArray");
            }

        }
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        headermanager = new HeaderManager(BasketListActivity.this, tab_type);
        headermanager.getHeader(relativeHeader, 6);
        back = headermanager.getLeftButton();
        btn_history = headermanager.getRightHistoryImage();
        btn_history.setVisibility(View.INVISIBLE);
        headermanager.setButtonLeftSelector(R.drawable.back,
                R.drawable.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFrom.equalsIgnoreCase("Items"))
                {
                    Intent intent=new Intent(mContext,ItemListActivityNew.class);
                    intent.putExtra("dateArray",mDateArrayList);
                    intent.putExtra("tab_type","Pre-Order");
                    intent.putExtra("ordered_user_type",ordered_user_type);
                    intent.putExtra("student_id",student_id);
                    intent.putExtra("parent_id",parent_id);
                    intent.putExtra("staff_id",staff_id);
                    startActivity(intent);
                }
                else if(isFrom.equalsIgnoreCase("MyOrder"))
                {
                    Intent intent=new Intent(BasketListActivity.this,MyOrderActivity.class);
                    intent.putExtra("ordered_user_type",ordered_user_type);
                    intent.putExtra("tab_type","My Order");
                    intent.putExtra("student_id",student_id);
                    intent.putExtra("parent_id",PreferenceManager.getUserId(mContext));
                    intent.putExtra("staff_id",staff_id);
                    startActivity(intent);
                }
                else if(isFrom.equalsIgnoreCase("Order"))
                {
                    Intent intent=new Intent(BasketListActivity.this,OrderHistoryActivity.class);
                    intent.putExtra("ordered_user_type",ordered_user_type);
                    intent.putExtra("tab_type","Order History");
                    intent.putExtra("student_id",student_id);
                    intent.putExtra("parent_id",PreferenceManager.getUserId(mContext));
                    intent.putExtra("staff_id",staff_id);
                    startActivity(intent);
                }
                else
                {
                    AppUtils.hideKeyBoard(mContext);
                    finish();
                }
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
        itemLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            boolean isNoItemAvailableFound=false;
            int basketPos=0;
            int basketDetailPos=0;
           for (int j=0;j<mMyBasketListArrayList.size();j++)
           {
               for (int m=0;m<mMyBasketListArrayList.get(j).getmBasketDetailArrayList().size();m++)
               {
                   if (mMyBasketListArrayList.get(j).getmBasketDetailArrayList().get(m).getAvailable_quantity().equalsIgnoreCase("0"))
                   {
                       isNoItemAvailableFound=true;
                       basketPos=j;
                       basketDetailPos=m;
                   }
               }
           }
              if (!isNoItemAvailableFound)
              {
                  BalanceWalletAmount=WalletAmount-TotalOrderedAmount;
                  if (BalanceWalletAmount>0)
                  {
                      BalanceConfirmWalletAmount=BalanceWalletAmount-CartTotalAmount;
                      if (BalanceConfirmWalletAmount>=0)
                      {
                          itemArray=new ArrayList<>();
                          for (int i=0;i<mMyBasketListArrayList.size();i++)
                          {
                              ItemsModel mModel=new ItemsModel();
                              mModel.setDelivery_date(mMyBasketListArrayList.get(i).getDelivery_date());
                              orderArray=new ArrayList<>();
                              for (int j=0;j<mMyBasketListArrayList.get(i).getmBasketDetailArrayList().size();j++)
                              {
                                  OrdersModel nModel=new OrdersModel();
                                  nModel.setItem_id(mMyBasketListArrayList.get(i).getmBasketDetailArrayList().get(j).getItem_id());
                                  nModel.setQuantity(mMyBasketListArrayList.get(i).getmBasketDetailArrayList().get(j).getQuantity());
                                  orderArray.add(nModel);
                              }
                              mModel.setItems(orderArray);
                              itemArray.add(mModel);
                          }
                          Gson gson = new Gson();
                          String Data = gson.toJson(itemArray);

                          String JSON = "{\"student_id\":\""+student_id+"\"," +
                                  "\"ordered_user_type\":\""+ordered_user_type+"\"," +
                                  "\"parent_id\":\""+parent_id+"\"," +
                                  "\"staff_id\":\""+staff_id+"\","+
                                  "\"orders\":"+Data+"}";

                          Log.d("JSON:",JSON);
                          getPreOrder(URL_CANTEEN_PREORDER,JSON);
                      }
                      else
                      {
                          showInsufficientBal(mContext, "Alert", "Insufficient balance please top up wallet", R.drawable.exclamationicon, R.drawable.round);

                      }

                  }
                  else
                  {
                      showInsufficientBal(mContext, "Alert", "Insufficient balance please top up wallet", R.drawable.exclamationicon, R.drawable.round);

                     // AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert","You don't have enough balance in your wallet, Please topup your wallet.", R.drawable.exclamationicon, R.drawable.round);

                  }


              }
              else
              {
                  String itemNameNotAvailable=mMyBasketListArrayList.get(basketPos).getmBasketDetailArrayList().get(basketDetailPos).getItem_name();
                  String itemDate=mMyBasketListArrayList.get(basketPos).getDelivery_date();
                  AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert","The "+itemNameNotAvailable+" on "+AppUtils.dateConversionY(itemDate)+" is not available right now. Please remove it and place your order", R.drawable.exclamationicon, R.drawable.round);

              }


            }
        });

        /* String JSON = "{\"student_id\":\"3189\"," +
                    "\"ordered_user_type\":\"1\"," +
                    "\"parent_id\":\"21\"," +
                    "\"staff_id\":\"\"," +
                    "\"orders\":"+Data+"}";*/

    }

    public static void getMyBasketList(final String URL,String ordered_user_type, String student_id, String parent_id, String staff_id)
    {
        mMyBasketListArrayList=new ArrayList<>();
        try {

            final VolleyWrapper manager = new VolleyWrapper(URL);
            String[] name = {JTAG_ACCESSTOKEN,"ordered_user_type","student_id","parent_id","staff_id","portal"};
            String[] value = {PreferenceManager.getAccessToken(mContext),ordered_user_type,student_id,parent_id,staff_id,"1"};
            System.out.println("Values ::::" + PreferenceManager.getAccessToken(mContext) + "hfhgfhdfghd::::" + PreferenceManager.getStaffId(mContext)+"Orderd user type "+ordered_user_type+" student_id "+student_id+" parent_id "+parent_id);
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
                                        cart_totoal=respObject.optInt("cart_totoal");
                                        TotalOrderedAmount=respObject.optInt("previous_orders_total");
                                        total_items_in_cart=respObject.optInt("total_items_in_cart");
                                        CartTotalAmount=cart_totoal;
                                        JSONArray dataArray=respObject.getJSONArray("data");
                                        if (dataArray.length()>0)
                                        {
                                            for (int i=0;i<dataArray.length();i++)
                                            {
                                                JSONObject dataObject = dataArray.optJSONObject(i);
                                                BasketModel mModel=new BasketModel();
                                                mModel.setDelivery_date(dataObject.optString("delivery_date"));
                                                mModel.setTotal_amount(dataObject.optInt("total_amount"));
                                                mMyBasketDetailArrayList=new ArrayList<>();
                                                JSONArray itemsArray=dataObject.getJSONArray("items");
                                                if (itemsArray.length()>0) {
                                                    for (int j = 0; j < itemsArray.length(); j++)
                                                    {
                                                        JSONObject detailObject = itemsArray.optJSONObject(j);
                                                        BasketDetailModel nModel=new BasketDetailModel();
                                                        nModel.setId(detailObject.optString("id"));
                                                        nModel.setItem_id(detailObject.optString("item_id"));
                                                        nModel.setQuantity(detailObject.optString("quantity"));
                                                        nModel.setItem_name(detailObject.optString("item_name"));
                                                        nModel.setPrice(detailObject.optString("price"));
                                                        nModel.setPortal(detailObject.optString("portal"));
                                                        nModel.setAvailable_quantity(detailObject.optString("available_quantity"));
                                                        nModel.setItem_description(detailObject.optString("item_description"));
                                                        homeBannerUrlImageArray = new ArrayList<>();
                                                        JSONArray imageArray = detailObject.getJSONArray("item_image");
                                                        if (imageArray.length() > 0) {
                                                            for (int m = 0; m < imageArray.length(); m++) {
                                                                homeBannerUrlImageArray.add(imageArray.optString(m));
                                                            }
                                                        }
                                                        nModel.setImageBanner(homeBannerUrlImageArray);
                                                        mMyBasketDetailArrayList.add(nModel);
                                                    }
                                                }
                                                mModel.setmBasketDetailArrayList(mMyBasketDetailArrayList);
                                                mMyBasketListArrayList.add(mModel);


                                            }
                                            dateRecyclerView.setVisibility(View.VISIBLE);
                                            itemLinear.setVisibility(View.VISIBLE);
                                            noItemTxt.setVisibility(View.GONE);
                                            itemTxt.setText(String.valueOf(total_items_in_cart)+" items");
                                            amountTxt.setText(String.valueOf(cart_totoal)+" AED");
                                            System.out.println("Basket Position"+String.valueOf(AppController.basketPosition));
                                            BasketRecyclerAdapter mAdapter=new BasketRecyclerAdapter(mContext,mMyBasketListArrayList,ordered_user_type,student_id,parent_id,staff_id);
                                            dateRecyclerView.setAdapter(mAdapter);

                                        }
                                        else
                                        {
                                            dateRecyclerView.setVisibility(View.GONE);
                                            itemLinear.setVisibility(View.GONE);
                                            noItemTxt.setVisibility(View.VISIBLE);
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
                                    getMyBasketList(URL,ordered_user_type,student_id,parent_id,staff_id);

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

    public void getPreOrder(final String URL,String data)
    {
        try
        {

            final VolleyWrapper manager = new VolleyWrapper(URL);
            String[] name = {JTAG_ACCESSTOKEN,"data","portal"};
            String[] value = {PreferenceManager.getAccessToken(mContext),data,"1"};
            System.out.println("Values ::::" + PreferenceManager.getAccessToken(mContext) + "hfhgfhdfghd::::" + data);
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
                                        showDialogAlertDismiss((Activity) mContext, "Thank You!", "Your order has been successfully placed", R.drawable.tick, R.drawable.round);

                                      //  Toast.makeText(mContext,"Your order has been successfully placed",Toast.LENGTH_SHORT).show();
                                        dateRecyclerView.setVisibility(View.GONE);
                                        itemLinear.setVisibility(View.GONE);
                                        noItemTxt.setVisibility(View.VISIBLE);
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
                                    getMyBasketList(URL,ordered_user_type,student_id,parent_id,staff_id);

                                } else if (responsCode.equals(RESPONSE_ERROR)) {
//								CustomStatusDialog(RESPONSE_FAILURE);
                                    //Toast.makeText(mContext,"Failure",Toast.LENGTH_SHORT).show();


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
    public void showDialogAlertDismiss(final Activity activity, String msgHead, String msg, int ico,int bgIcon)
    {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_dialogue_ok_layout);
        ImageView icon = (ImageView) dialog.findViewById(R.id.iconImageView);
        icon.setBackgroundResource(bgIcon);
        icon.setImageResource(ico);
        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        TextView textHead = (TextView) dialog.findViewById(R.id.alertHead);
        text.setText(msg);
         String message= "Cost will be deducted"+ "<b>" + " only " + "</b> " + " when item delivered";
        text.setText(Html.fromHtml(message));
     //   textHead.setText(Html.fromHtml(mContext.getResources().getString(R.string.order_success)));
        textHead.setText(msgHead);

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_Ok);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();

            }
        });
//		Button dialogButtonCancel = (Button) dialog.findViewById(R.id.btn_Cancel);
//		dialogButtonCancel.setVisibility(View.GONE);
//		dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				dialog.dismiss();
//			}
//		});
        dialog.show();

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
            String[] name = {JTAG_ACCESSTOKEN, "staff_id"};
            String[] value = {PreferenceManager.getAccessToken(mContext), staffID};
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
                    finish();
                    startActivity(intent);
                }
                else
                {
                    Intent intent=new Intent(mContext, CanteenFirstActivity.class);
                    intent.putExtra("tab_type","Canteen");
                    intent.putExtra("email","");
                    intent.putExtra("isFrom","Preorder");
                    dialog.dismiss();
                    finish();
                    startActivity(intent);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (isFrom.equalsIgnoreCase("Items"))
        {
            Intent intent=new Intent(mContext,ItemListActivityNew.class);
            intent.putExtra("dateArray",mDateArrayList);
            intent.putExtra("tab_type","Pre-Order");
            intent.putExtra("ordered_user_type",ordered_user_type);
            intent.putExtra("student_id",student_id);
            intent.putExtra("parent_id",parent_id);
            intent.putExtra("staff_id",staff_id);
            startActivity(intent);
        }
        else if(isFrom.equalsIgnoreCase("MyOrder"))
        {
            Intent intent=new Intent(BasketListActivity.this,MyOrderActivity.class);
            intent.putExtra("ordered_user_type",ordered_user_type);
            intent.putExtra("tab_type","My Order");
            intent.putExtra("student_id",student_id);
            intent.putExtra("parent_id",PreferenceManager.getUserId(mContext));
            intent.putExtra("staff_id",staff_id);
            startActivity(intent);
        }
        else if(isFrom.equalsIgnoreCase("Order"))
        {
            Intent intent=new Intent(BasketListActivity.this,OrderHistoryActivity.class);
            intent.putExtra("ordered_user_type",ordered_user_type);
            intent.putExtra("tab_type","Order History");
            intent.putExtra("student_id",student_id);
            intent.putExtra("parent_id",PreferenceManager.getUserId(mContext));
            intent.putExtra("staff_id",staff_id);
            startActivity(intent);
        }
        else
        {
            AppUtils.hideKeyBoard(mContext);
            finish();
        }
        AppUtils.hideKeyBoard(mContext);
        finish();
    }


}
