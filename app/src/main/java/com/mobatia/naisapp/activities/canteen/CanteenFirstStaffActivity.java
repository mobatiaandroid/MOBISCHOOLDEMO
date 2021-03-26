package com.mobatia.naisapp.activities.canteen;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.canteen_new.ConfirmedOrderActivity;
import com.mobatia.naisapp.activities.canteen_new.MyOrderActivity;
import com.mobatia.naisapp.activities.canteen_new.OrderHistoryActivity;
import com.mobatia.naisapp.activities.canteen_new.PreOrderActivity;
import com.mobatia.naisapp.activities.canteen_new.PreOrderStaffActivity;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.activities.payment_history.PaymentHistoryNew;
import com.mobatia.naisapp.activities.payment_history.PaymentStaffHistoryNew;
import com.mobatia.naisapp.constants.IntentPassValueConstants;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NameValueConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.absence.model.LeavesModel;
import com.mobatia.naisapp.fragments.canteen.CanteenActviity;
import com.mobatia.naisapp.fragments.parents_evening.model.StudentModel;
import com.mobatia.naisapp.fragments.sports.adapter.StrudentSpinnerAdapter;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.HeaderManager;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.recyclerviewmanager.DividerItemDecoration;
import com.mobatia.naisapp.recyclerviewmanager.RecyclerItemListener;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.MediaType;
import payment.sdk.android.PaymentClient;
import payment.sdk.android.cardpayment.CardPaymentData;
import payment.sdk.android.cardpayment.CardPaymentRequest;

public class CanteenFirstStaffActivity extends Activity implements URLConstants, StatusConstants, JSONConstants,IntentPassValueConstants, NameValueConstants {
    Context mContext=this;
    Activity mActivity = this;
    String tab_type;
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back,btn_history,home;
    Bundle extras;
    private String mTitle;
    private String mTabId;
    private EditText amount;
    TextView mTitleTextView, walletbalance;
    static TextView studentName;
    private Button addToWallet;
    LinearLayout mStudentSpinner;
    static ImageView studImg;
    public final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/octet-stream");

    String ordered_user_type="";
    String student_id="";
    String staff_id="";
    String parent_id="";
    String PaymentToken = "",OrderRef = "",PayUrl = "", AuthUrl = "";
    String isFrom;
    String payAmount="";
    String merchantOrderReference="";
    String staffName="";
    String canteen_response="";
    String Error="";
    String staff_photo="";
    String topup_limit="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.canteen_first_staff_activity);
        mContext = this;
        initialiseUI();

        if (AppUtils.isNetworkConnected(mContext)) {
            getStaffInfo(URL_STAFF_INFO_FOR_CANTEEN);
        } else {
            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

        }
    }

    private void initialiseUI()
    {
        extras = getIntent().getExtras();
        if (extras != null) {
            tab_type = extras.getString("tab_type");
            isFrom = extras.getString("isFrom");
        }
        ordered_user_type="2";
        parent_id="";
        student_id="";
        staff_id=PreferenceManager.getStaffId(mContext);
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        headermanager = new HeaderManager(CanteenFirstStaffActivity.this, tab_type);
        headermanager.getHeader(relativeHeader, 6);
        back = headermanager.getLeftButton();
        btn_history = headermanager.getRightHistoryImage();
        btn_history.setVisibility(View.VISIBLE);
        headermanager.setButtonLeftSelector(R.drawable.back,
                R.drawable.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.hideKeyBoard(mContext);
                if (isFrom.equalsIgnoreCase("Preorder"))
                {
                    Intent intent = new Intent(CanteenFirstStaffActivity.this, PreOrderActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    finish();
                }
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
        amount = findViewById(R.id.et_amount);
        mStudentSpinner = findViewById(R.id.studentSpinner);
        studentName = findViewById(R.id.studentName);
        studentName = findViewById(R.id.studentName);
        studImg = findViewById(R.id.imagicon);
        addToWallet = findViewById(R.id.addToWallet);
        walletbalance = findViewById(R.id.walletbalance);
        btn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btn_history.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("history click");
                        Intent in = new Intent(mContext, PaymentStaffHistoryNew.class);
                        in.putExtra("studentName", studentName.getText().toString());
                        in.putExtra("studentImage",staff_photo);
                        in.putExtra("studentId", PreferenceManager.getStaffId(mContext));
                        startActivity(in);
                    }
                });
            }
        });
        getPaymentToken();
        addToWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if (!amount.getText().toString().equalsIgnoreCase(""))
               {
                   String paymentAmounts = amount.getText().toString();
                   payAmount=amount.getText().toString();
                   int amountInt=Integer.parseInt(amount.getText().toString())*100;
                   String payment_amount=String.valueOf(amountInt);
                   if (!payment_amount.equals(""))
                   {

                       int paymentAmount=Integer.parseInt(payment_amount);;
                       if (paymentAmount>0)
                       {
                           int topAmt=Integer.parseInt(topup_limit)*100;
                           System.out.println("Top Amount Limit"+topup_limit+"  TpAmt  "+topAmt+"Amount "+String.valueOf(paymentAmount));
                           if(paymentAmount>topAmt)
                           {
                               Toast.makeText(mContext, "Allow only upto "+topup_limit+"AED on a single transaction", Toast.LENGTH_SHORT).show();
                           }
                           else
                           {
                               String str = payment_amount;
                               int arrayLength = 0;
                               char[] array = str.toCharArray();
                               arrayLength = array.length;
                               int firstNonZeroAt = 0;
                               for(int i=0; i<array.length; i++) {
                                   if(!String.valueOf(array[i]).equalsIgnoreCase("0")) {
                                       firstNonZeroAt = i;
                                       break;
                                   }
                               }
                               System.out.println("first non zero digit at : " +firstNonZeroAt);
                               char [] newArray = Arrays.copyOfRange(array, firstNonZeroAt,arrayLength);
                               String resultString = new String(newArray);
                               System.out.println("amount removed zero"+resultString);
                               long unixTime = System.currentTimeMillis() / 1000L;
                               amount.getText().clear();
//                        startActivity(intent);
                               CallForPayment(payment_amount);
                           }

                       }
                       else
                       {
                           Toast.makeText(mContext, "Please enter amount", Toast.LENGTH_SHORT).show();
                       }

                   }
                   else{
                       Toast.makeText(mContext, "Please enter amount", Toast.LENGTH_SHORT).show();
                   }
               }
               else
               {
                   Toast.makeText(mContext, "Please enter amount", Toast.LENGTH_SHORT).show();
               }



            }
        });



    }




    public void getWallet(final String URL, final String student_id)
    {

        try {
            System.out.println("Student id api click" + student_id);
            final VolleyWrapper manager = new VolleyWrapper(URL);
//            stud_id = studentsModelArrayList.get(0).getmId();
            String[] name = {JTAG_ACCESSTOKEN, "staff_id"};
            String[] value = {PreferenceManager.getAccessToken(mContext), student_id};
            System.out.println("Values ::::" + PreferenceManager.getAccessToken(mContext) + "hfhgfhdfghd::::" + student_id);
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
                                         topup_limit = respObject.optString("topup_limit");
                                        walletbalance.setText(walletAmount);

//                                        String walletAmountn = respObject.optString("wallet_amount");
                                        if (walletAmount.equals("null")) {
                                            walletbalance.setText("0");
                                        }

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


    public void getStaffInfo(final String URL)
    {

        try {

            //   mAbsenceListViewArray = new ArrayList<>();
            final VolleyWrapper manager = new VolleyWrapper(URL);
//            stud_id = studentsModelArrayList.get(0).getmId();
            String[] name = {JTAG_ACCESSTOKEN, "staff_id"};
            String[] value = {PreferenceManager.getAccessToken(mContext), PreferenceManager.getStaffId(mContext)};
            System.out.println("Values ::::" + PreferenceManager.getAccessToken(mContext) + "hfhgfhdfghd::::" + PreferenceManager.getStaffId(mContext));
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
                                        JSONObject dataObject=respObject.getJSONObject("data");
                                        String id=dataObject.optString("id");
                                        String name=dataObject.optString("name");
                                         staffName=dataObject.optString("name");
                                        String email=dataObject.optString("email");
                                        String pickup_location=dataObject.optString("pickup_location");
                                        staff_photo=dataObject.optString("staff_photo");
                                        studentName.setText(name);

                                        if (!(staff_photo.equals(""))) {

                                            Picasso.with(mContext).load(AppUtils.replace(staff_photo)).placeholder(R.drawable.boy).fit().into(studImg);
                                        } else {

                                            studImg.setImageResource(R.drawable.boy);
                                        }

                                        if (AppUtils.isNetworkConnected(mContext)) {
                                            getWallet(URL_CANTEEN_STAFF_WALLET, staff_id);
                                        } else {
                                            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                                        }
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
                                    getStaffInfo(URL);

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
                    //  AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    @Override
    public void onResume() {
        super.onResume();
        if (AppUtils.isNetworkConnected(mContext)) {
            getStaffInfo(URL_STAFF_INFO_FOR_CANTEEN);
        } else {
            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

        }
        //other stuff
    }
    private void getPaymentToken() {
        VolleyWrapper volleyWrapper = new VolleyWrapper(URLConstants.URL_PAYMENT_TOKEN);
        String[] name = {"access_token", "users_id"};
        String[] value = {PreferenceManager.getAccessToken(mContext), PreferenceManager.getUserId(mContext)};
        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("The response is" + successResponse);
                try {
                    JSONObject obj = new JSONObject(successResponse);
                    String response_code = obj.getString(JTAG_RESPONSECODE);
                    if (response_code.equalsIgnoreCase("200")) {
                        JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                        String status_code = secobj.getString(JTAG_STATUSCODE);
                        if (status_code.equalsIgnoreCase("303")) {
                            PaymentToken = secobj.getString("access_token");
                            Log.d("PAYMM",PaymentToken);
                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        getPaymentToken();

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        getPaymentToken();

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        getPaymentToken();

                    } else {
                        /*CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
								, getResources().getString(R.string.ok));
						dialog.show();*/
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    }
                } catch (Exception ex) {
                    System.out.println("The Exception in edit profile is" + ex.toString());
                }

            }

            @Override
            public void responseFailure(String failureResponse) {
                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

            }
        });
    }

    private void CallForPayment(String amount) {
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        merchantOrderReference=PreferenceManager.getStaffId(mContext)+ts;
        VolleyWrapper volleyWrapper = new VolleyWrapper(URLConstants.URL_CREATE_PAYMENT);
        String[] name = {"access_token", "amount","merchantOrderReference","firstName","lastName","address1","city","countryCode","emailAddress"};
        String[] value = {PaymentToken, amount,merchantOrderReference,staffName,"","NAS DUBAI","DUBAI","UAE",PreferenceManager.getUserEmail(mContext)};
        System.out.println("payment Access_token"+PaymentToken);
        System.out.println("payment amount"+amount);
        System.out.println("payment merchantOrderReference"+merchantOrderReference);
        System.out.println("payment firstName"+staffName);
        System.out.println("payment emailAddress"+PreferenceManager.getUserEmail(mContext));
        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("The response is" + successResponse);
                try {
                    JSONObject obj = new JSONObject(successResponse);
                    String response_code = obj.getString(JTAG_RESPONSECODE);
                    if (response_code.equalsIgnoreCase("200")) {
                        JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                        String status_code = secobj.getString(JTAG_STATUSCODE);
                        if (status_code.equalsIgnoreCase("303"))
                        {

                            OrderRef = secobj.getString("order_reference");
                            PayUrl = secobj.getString("order_paypage_url");
                            AuthUrl = secobj.getString("authorization");
                            String Code = PayUrl.split("=")[1];
                            Log.d("PAYMM",OrderRef);
                            Log.d("PAYMM",PayUrl);
                            Log.d("PAYMM",Code);
                            Log.d("PAYMM",AuthUrl);

//                            String AuthURL = "https://api-gateway.sandbox.ngenius-payments.com/transactions/paymentAuthorization";

                            CardPaymentRequest request = new CardPaymentRequest(AuthUrl,Code);

                            PaymentClient paymentClient = new PaymentClient(mActivity);
                            paymentClient.launchCardPayment(request,0);

                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        CallForPayment(amount);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        CallForPayment(amount);

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        CallForPayment(amount);

                    } else {
                        /*CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
								, getResources().getString(R.string.ok));
						dialog.show();*/
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    }
                } catch (Exception ex) {
                    System.out.println("The Exception in edit profile is" + ex.toString());
                }

            }

            @Override
            public void responseFailure(String failureResponse) {
                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("request_code", String.valueOf(requestCode));
        Log.d("resultt_code", String.valueOf(resultCode));
        if (data == null) {

            Toast.makeText(mActivity, "transaction cancelled", Toast.LENGTH_SHORT).show();
        } else {
            if (requestCode == 0) {

//            Log.d("response",data.getStringExtra("jsonResponse"));
//            String jsonObject=data.getStringExtra("jsonResponse");
//            Log.v("jsonResponse",jsonObject);

                CardPaymentData cardPaymentData = CardPaymentData.getFromIntent(data);
                Log.d("PAYMM",String.valueOf(cardPaymentData.getCode()));
                Log.d("PAYMM",String.valueOf(cardPaymentData.getReason()));
                if (cardPaymentData.getCode() == 2) {
                    String JSONData = "{\"details\":[{"+"\"staff_id\":\""+PreferenceManager.getStaffId(mContext)+"\"," +
                            "\"amount\":\""+payAmount+"\"," +
                            "\"keycode\":\""+merchantOrderReference+"\""+"}]}";
                    System.out.println("JSON DATA URL"+JSONData);
                    CallWalletSubmission(JSONData);


//                Log.d("reason",cardPaymentData.getReason());

                } else {
                    Toast.makeText(mContext, "Transaction failed", Toast.LENGTH_SHORT).show();

                }

            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AppUtils.hideKeyBoard(mContext);
        if (isFrom.equalsIgnoreCase("Preorder"))
        {
            Intent intent = new Intent(CanteenFirstStaffActivity.this, PreOrderActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        else
        {
            finish();
        }

    }

    private void CallWalletSubmission(String data) {
        VolleyWrapper volleyWrapper = new VolleyWrapper(URL_GET_STAFF_WALLET_SUBMISSION);
        String[] name = {"access_token","data"};
        String[] value = {PreferenceManager.getAccessToken(mContext),data};
        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("The response is" + successResponse);
                try {
                    JSONObject obj = new JSONObject(successResponse);
                    String response_code = obj.getString(JTAG_RESPONSECODE);
                    if (response_code.equalsIgnoreCase("200")) {
                        JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                        String status_code = secobj.getString(JTAG_STATUSCODE);
                        if (status_code.equalsIgnoreCase("303"))
                        {
                            String BillSerialNo=secobj.optString("BillSerialNo");
                            String canteen_status_code=secobj.optString("canteen_status_code");
                            canteen_response=secobj.optString("canteen_response");
                            Error=secobj.optString("Error");
                            if (canteen_status_code.equalsIgnoreCase("1"))
                            {
                                Toast.makeText(mContext, "Transaction successfully completed", Toast.LENGTH_SHORT).show();
                                if (AppUtils.isNetworkConnected(mContext)) {
                                    getWallet(URL_CANTEEN_STAFF_WALLET, PreferenceManager.getStaffId(mContext));
                                    System.out.println("Student id social click" + PreferenceManager.getStaffId(mContext));
                                } else {

                                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                                }
                            }
                            else
                            {
                                if (canteen_response.equalsIgnoreCase("Error"))
                                {
                                    String errorMessage=Error;
                                    Toast.makeText(mContext, errorMessage, Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(mContext, "Cannot continue please try again later", Toast.LENGTH_SHORT).show();
                                }

                            }

                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        CallWalletSubmission(data);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        CallWalletSubmission(data);

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        CallWalletSubmission(data);

                    } else {
                        /*CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
								, getResources().getString(R.string.ok));
						dialog.show();*/
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    }
                } catch (Exception ex) {
                    System.out.println("The Exception in edit profile is" + ex.toString());
                }

            }

            @Override
            public void responseFailure(String failureResponse) {
                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

            }
        });

    }
}
