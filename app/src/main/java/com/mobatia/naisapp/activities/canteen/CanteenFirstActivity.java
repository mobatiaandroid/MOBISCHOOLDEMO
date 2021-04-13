package com.mobatia.naisapp.activities.canteen;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.mobatia.naisapp.BuildConfig;
import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.canteen_new.PreOrderActivity;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.activities.payment_history.PaymentHistoryNew;
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

public class CanteenFirstActivity extends Activity implements URLConstants, StatusConstants, JSONConstants,IntentPassValueConstants, NameValueConstants {
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
    private RelativeLayout card_walletbalance;
    private RelativeLayout belowViewRelative;
    private Button addToWallet;
    private RelativeLayout paymentRelative;
    static ArrayList<StudentModel> studentsModelArrayList = new ArrayList<>();
    private ArrayList<LeavesModel> mAbsenceListViewArray;
    LinearLayout mStudentSpinner;
    String stud_id;
    String studClass = "";
    String orderId = "";
    static String stud_img = "";
    static ImageView studImg;
    public final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/octet-stream");
    ArrayList<String> studentList = new ArrayList<>();
    boolean firstVisit;
    public String merchantpassword = "16496a68b8ac0fb9b6fde61274272457";
    String PaymentToken = "",OrderRef = "",PayUrl = "", AuthUrl = "";
    String isFrom;
    String payAmount="";
    String merchantOrderReference="";
    String canteen_response="";
    String Error="";
    String topup_limit="";
    String order_id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_canteen);
        mContext = this;
        initialiseUI();

        if (AppUtils.checkInternet(mContext)) {
            getStudentsListAPI(URL_GET_STUDENT_LIST);
        } else {
            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

        }
    }

    private void initialiseUI() {
        extras = getIntent().getExtras();
        if (extras != null) {
            tab_type = extras.getString("tab_type");
            isFrom = extras.getString("isFrom");
        }
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        headermanager = new HeaderManager(CanteenFirstActivity.this, tab_type);
        headermanager.getHeader(relativeHeader, 6);
        back = headermanager.getLeftButton();
        btn_history=headermanager.getRightHistoryImage();
        headermanager.setButtonLeftSelector(R.drawable.back,
                R.drawable.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.hideKeyBoard(mContext);
                if (isFrom.equalsIgnoreCase("Preorder"))
                {
                    Intent intent = new Intent(CanteenFirstActivity.this, PreOrderActivity.class);
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

        //getPaymentToken();

        btn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("history click");
                Intent in = new Intent(mContext, PaymentHistoryNew.class);
                in.putExtra("studentName", studentName.getText().toString());
                in.putExtra("studentImage", stud_img);
                in.putExtra("studentId", PreferenceManager.getCanteenStudentId(mContext));
                in.putExtra("StudentModelArray", studentsModelArrayList);
                startActivity(in);
            }
        });
        paymentRelative = findViewById(R.id.paymentRelative);
        mStudentSpinner = findViewById(R.id.studentSpinner);

        amount = findViewById(R.id.et_amount);
        addToWallet = findViewById(R.id.addToWallet);
        walletbalance = findViewById(R.id.walletbalance);
        card_walletbalance = findViewById(R.id.card_walletbalance);
        belowViewRelative = findViewById(R.id.belowViewRelative);
        paymentRelative.setVisibility(View.GONE);
        belowViewRelative.setVisibility(View.VISIBLE);
        studentName = findViewById(R.id.studentName);
        studImg = findViewById(R.id.imagicon);
        mStudentSpinner.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if (studentsModelArrayList.size() > 0) {
                    showSocialmediaList(studentsModelArrayList);
                } else
                    {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.student_not_available), R.drawable.exclamationicon, R.drawable.round);

                }
            }
        });
        addToWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
               // String paymentAmounts = amount.getText().toString();

//                if (!paymentAmounts.equals("")) {
//                    int amountInt=Integer.parseInt(amount.getText().toString())*100;
//                    String payment_amount=String.valueOf(amountInt);
//                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "Payment Gateway under construction", R.drawable.exclamationicon, R.drawable.round);
//                }
//                else {
//                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "Please enter amount", R.drawable.exclamationicon, R.drawable.round);
//
//                }
                if (!amount.getText().toString().equalsIgnoreCase(""))
                {
                    String paymentAmounts = amount.getText().toString();
                    payAmount=amount.getText().toString();
                    int amountInt=Integer.parseInt(amount.getText().toString())*100;
                    String payment_amount=String.valueOf(amountInt);
                    if (!payment_amount.equals("")){
                        int paymentAmount=Integer.parseInt(payment_amount);;
                        if (paymentAmount>0)
                        {
                            int topAmt=Integer.parseInt(topup_limit)*100;
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
                                char [] newArray = Arrays.copyOfRange(array, firstNonZeroAt,arrayLength);
                                String resultString = new String(newArray);
                                System.out.println("amount removed zero"+resultString);
                                long unixTime = System.currentTimeMillis() / 1000L;
                                orderId="NASDUBAI"+stud_id+"C"+unixTime;
                                System.out.println("Unix Time:::"+unixTime+"Order ID"+orderId);
                                Intent intent =new Intent(mContext,CanteenActviity.class);
                                intent.putExtra("orderId",orderId);
                                intent.putExtra("stud_id",stud_id);
                                intent.putExtra("amount",resultString);
                                amount.getText().clear();
//                        startActivity(intent);
                                order_id="";
                                merchantOrderReference="";
                              //  CallForPayment(payment_amount);
                                CallForPayment(payAmount,stud_id);
                            }

                        }
                        else
                        {
                            Toast.makeText(mContext, "Please enter amount", Toast.LENGTH_SHORT).show();
                        }

                    }else{
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

    public void showSocialmediaList(final ArrayList<StudentModel> mStudentArray) {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_student_media_list);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button dialogDismiss = (Button) dialog.findViewById(R.id.btn_dismiss);
        ImageView iconImageView = (ImageView) dialog.findViewById(R.id.iconImageView);
        iconImageView.setImageResource(R.drawable.boy);
        RecyclerView socialMediaList = (RecyclerView) dialog.findViewById(R.id.recycler_view_social_media);
        //if(mSocialMediaArray.get())
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            dialogDismiss.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.button));

        } else {
            dialogDismiss.setBackground(mContext.getResources().getDrawable(R.drawable.button));

        }
        socialMediaList.addItemDecoration(new DividerItemDecoration(mContext.getResources().getDrawable(R.drawable.list_divider_teal)));

        socialMediaList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        socialMediaList.setLayoutManager(llm);

        StrudentSpinnerAdapter studentAdapter = new StrudentSpinnerAdapter(mContext, mStudentArray);
        socialMediaList.setAdapter(studentAdapter);
        dialogDismiss.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                dialog.dismiss();

            }

        });

        socialMediaList.addOnItemTouchListener(new RecyclerItemListener(mContext, socialMediaList,
                new RecyclerItemListener.RecyclerTouchListener() {
                    public void onClickItem(View v, int position) {
                        dialog.dismiss();
                        studentName.setText(mStudentArray.get(position).getmName());
                        stud_id = mStudentArray.get(position).getmId();
                        studClass = mStudentArray.get(position).getmClass();
                        stud_img = mStudentArray.get(position).getmPhoto();
                        if (!(stud_img.equals(""))) {

                            Picasso.with(mContext).load(AppUtils.replace(stud_img)).placeholder(R.drawable.boy).fit().into(studImg);
                        } else {

                            studImg.setImageResource(R.drawable.boy);
                        }
                        PreferenceManager.setCanteenStudentId(mContext,mStudentArray.get(position).getmId());
                        PreferenceManager.setCanteenStudentImage(mContext,mStudentArray.get(position).getmPhoto());
                        PreferenceManager.setCanteenStudentId(mContext,mStudentArray.get(position).getmId());
                        PreferenceManager.setCanteenStudentName(mContext,mStudentArray.get(position).getmName());
                        if (AppUtils.isNetworkConnected(mContext)) {
                            getWallet(URL_CANTEEN_WALLET, stud_id);
                            System.out.println("Student id social click" + stud_id);
                        } else {

                            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                        }
                    }

                    public void onLongClickItem(View v, int position) {
                        System.out.println("On Long Click Item interface");
                    }
                }));
        dialog.show();
    }

    public void getWallet(final String URL, final String student_id)
    {

        try {
            System.out.println("Student id api click" + student_id);
            mAbsenceListViewArray = new ArrayList<>();
            final VolleyWrapper manager = new VolleyWrapper(URL);
//            stud_id = studentsModelArrayList.get(0).getmId();
            String[] name = {JTAG_ACCESSTOKEN, "student_id"};
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
                                        topup_limit= respObject.optString("topup_limit");
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

    private void getStudentsListAPI(final String URLHEAD) {
        VolleyWrapper volleyWrapper = new VolleyWrapper(URLHEAD);
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
                            JSONArray data = secobj.getJSONArray("data");
                            studentsModelArrayList.clear();
                            studentList.clear();
                            if (data.length() > 0) {
                                //studentsModelArrayList.add(0,);
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject dataObject = data.getJSONObject(i);
                                    studentsModelArrayList.add(addStudentDetails(dataObject));
                                    studentList.add(studentsModelArrayList.get(i).getmName());
                                }
                                studentName.setText(studentsModelArrayList.get(0).getmName());
                                stud_id = studentsModelArrayList.get(0).getmId();
                                //  PreferenceManager.setLeaveStudentId(mContext, stud_id);
                                //    PreferenceManager.setLeaveStudentName(mContext, studentsModelArrayList.get(0).getmName());
                                studClass = studentsModelArrayList.get(0).getmClass();
                                stud_img = studentsModelArrayList.get(0).getmPhoto();
                                PreferenceManager.setCanteenStudentId(mContext,studentsModelArrayList.get(0).getmId());
                                PreferenceManager.setCanteenStudentImage(mContext,studentsModelArrayList.get(0).getmPhoto());
                                PreferenceManager.setCanteenStudentId(mContext,studentsModelArrayList.get(0).getmId());

                                if (!(stud_img.equals(""))) {

                                    Picasso.with(mContext).load(AppUtils.replace(stud_img)).placeholder(R.drawable.boy).fit().into(studImg);
                                } else {

                                    studImg.setImageResource(R.drawable.boy);
                                }

                                belowViewRelative.setVisibility(View.VISIBLE);
//                                newRequest.setVisibility(View.VISIBLE);
                                if (AppUtils.isNetworkConnected(mContext)) {
                                    getWallet(URL_CANTEEN_WALLET, stud_id);
                                } else {
                                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                                }

                                // studentList.add("Select a child");

                                /*CustomSpinnerAdapter dataAdapter = new CustomSpinnerAdapter(mContext,
                                        R.layout.spinnertextwithoutbg, studentList,-1);
                                mStudentSpinner.setAdapter(dataAdapter);*/

                            } else {
                                belowViewRelative.setVisibility(View.INVISIBLE);
//                                newRequest.setVisibility(View.INVISIBLE);

                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.student_not_available), R.drawable.exclamationicon, R.drawable.round);
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
                        getStudentsListAPI(URLHEAD);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        getStudentsListAPI(URLHEAD);

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        getStudentsListAPI(URLHEAD);

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

    private StudentModel addStudentDetails(JSONObject dataObject) {
        StudentModel studentModel = new StudentModel();
        studentModel.setmId(dataObject.optString(JTAG_ID));
        studentModel.setmName(dataObject.optString(JTAG_TAB_NAME));
        studentModel.setmClass(dataObject.optString(JTAG_TAB_CLASS));
        studentModel.setmSection(dataObject.optString(JTAG_TAB_SECTION));
        studentModel.setmHouse(dataObject.optString("house"));
        studentModel.setmPhoto(dataObject.optString("photo"));
        return studentModel;
    }

    @Override
    public void onResume() {
        super.onResume();
        //other stuff
        if (AppUtils.isNetworkConnected(mContext))
        {
            studentName.setText(PreferenceManager.getCanteenStudentName(mContext));
            if (!(PreferenceManager.getCanteenStudentImage(mContext).equals(""))) {

                Picasso.with(mContext).load(AppUtils.replace(PreferenceManager.getCanteenStudentImage(mContext))).placeholder(R.drawable.boy).fit().into(studImg);
            } else {

                studImg.setImageResource(R.drawable.boy);
            }
            getWallet(URL_CANTEEN_WALLET, PreferenceManager.getCanteenStudentId(mContext));
        } else {
            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

        }
    }
    public class RemoveLeadingZeroes {
        public  void main(String[] args) {

            //System.out.println(resultString);
        }
    }

    private void getPaymentToken()
    {
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


    private void CallForPayment(String amount,String studentID)
    {
        String deviceBrand = android.os.Build.MANUFACTURER;
        String deviceModel = Build.MODEL;
        String osVersion = android.os.Build.VERSION.RELEASE;
        String devicename=deviceBrand+" "+deviceModel+" "+osVersion;
        String version= BuildConfig.VERSION_NAME;
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();

        VolleyWrapper volleyWrapper = new VolleyWrapper(URLConstants.URL_CREATE_PAYMENT);
        String[] name = {"access_token","users_id","student_id","amount","device_type","device_name","app_version"};
        String[] value = {PreferenceManager.getAccessToken(mContext),PreferenceManager.getUserId(mContext),studentID,amount,"2",devicename,version};
        Log.e("LOG CANTEEN PAY",studentID+"  "+amount);
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

                            order_id = secobj.getString("order_id");
                            OrderRef = secobj.getString("order_reference");
                            merchantOrderReference = secobj.getString("merchantOrderReference");
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
                        CallForPayment(amount,studentID);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        CallForPayment(amount,studentID);

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        CallForPayment(amount,studentID);

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
        }
        else {
            if (requestCode == 0) {
                CardPaymentData cardPaymentData = CardPaymentData.getFromIntent(data);
                Log.d("PAYMM",String.valueOf(cardPaymentData.getCode()));
                Log.d("PAYMM",String.valueOf(cardPaymentData.getReason()));
                if (cardPaymentData.getCode() == 2)
                {
                    String JSONData = "{\"details\":[{"+"\"student_id\":\""+PreferenceManager.getCanteenStudentId(mContext)+"\"," +
                            "\"users_id\":\""+PreferenceManager.getUserId(mContext)+"\"," +
                            "\"amount\":\""+payAmount+"\"," +
                            "\"keycode\":\""+merchantOrderReference+"\""+"}]}";
                    System.out.println("JSON DATA URL"+JSONData);
                    CallWalletSubmission(JSONData);



                }
                else {

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
            Intent intent = new Intent(CanteenFirstActivity.this, PreOrderActivity.class);
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
        String deviceBrand = android.os.Build.MANUFACTURER;
        String deviceModel = Build.MODEL;
        String osVersion = android.os.Build.VERSION.RELEASE;
        String devicename=deviceBrand+" "+deviceModel+" "+osVersion;
        //  int versionCode= BuildConfig.VERSION_NAME;
        String version= BuildConfig.VERSION_NAME;
        VolleyWrapper volleyWrapper = new VolleyWrapper(URL_CANTEEN_WALLET_SUBMISSION);
        String[] name = {"access_token","users_id","student_id","order_id","device_type","device_name","app_version"};
        String[] value = {PreferenceManager.getAccessToken(mContext),PreferenceManager.getUserId(mContext),stud_id,order_id,"2",devicename,version};
        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("The response is" + successResponse);
                try {
                    JSONObject obj = new JSONObject(successResponse);
                    String response_code = obj.getString(JTAG_RESPONSECODE);
                    if (response_code.equalsIgnoreCase("200"))
                    {
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
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Payment Successful","Thank You!", R.drawable.tick, R.drawable.round);

                              //  Toast.makeText(mContext, "Transaction successfully completed", Toast.LENGTH_SHORT).show();
                                if (AppUtils.isNetworkConnected(mContext)) {
                                    getWallet(URL_CANTEEN_WALLET, stud_id);
                                    System.out.println("Student id social click" + stud_id);
                                } else {

                                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                                }
                            }
                            else
                            {
                                String errorMessage=Error;
                                Toast.makeText(mContext, errorMessage, Toast.LENGTH_SHORT).show();
//                                if (canteen_response.equalsIgnoreCase("Error"))
//                                {
//                                    String errorMessage=Error;
//                                    Toast.makeText(mContext, errorMessage, Toast.LENGTH_SHORT).show();
//                                }
//                                else
//                                {
//                                    Toast.makeText(mContext, "Cannot continue please try again later", Toast.LENGTH_SHORT).show();
//                                }

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
