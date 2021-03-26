package com.mobatia.naisapp.fragments.canteen;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.payment_history.PaymentHistoryActivity;
import com.mobatia.naisapp.constants.CacheDIRConstants;
import com.mobatia.naisapp.constants.IntentPassValueConstants;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.constants.NaisTabConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.absence.model.LeavesModel;
import com.mobatia.naisapp.fragments.parents_evening.model.StudentModel;
import com.mobatia.naisapp.fragments.sports.adapter.StrudentSpinnerAdapter;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.recyclerviewmanager.DividerItemDecoration;
import com.mobatia.naisapp.recyclerviewmanager.RecyclerItemListener;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MediaType;

/**
 * A simple {@link Fragment} subclass.
 */
public class CanteenFragment extends Fragment implements
        NaisTabConstants, CacheDIRConstants, URLConstants,
        IntentPassValueConstants, NaisClassNameConstants, JSONConstants, StatusConstants {
    private View mRootView;
    private Context mContext;
    private String mTitle;
    private String mTabId;
    private EditText amount;
    TextView mTitleTextView, walletbalance;
    static TextView studentName;
    private CardView card_walletbalance;
    LinearLayout history;
    private RelativeLayout relMain;
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
    public String postBody = "";
    public String postUrl;
    String sessionId="";
    String sessionVersion="";
    public final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/octet-stream");
    ArrayList<String> studentList = new ArrayList<>();
    boolean firstVisit;
    public String merchantpassword = "16496a68b8ac0fb9b6fde61274272457";
    public CanteenFragment() {

    }

    @SuppressLint("ValidFragment")
    public CanteenFragment(String title, String tabId) {
        this.mTitle = title;
        this.mTabId = tabId;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * android.support.v4.app.Fragment#onCreateOptionsMenu(android.view.Menu,
     * android.view.MenuInflater)
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_canteen, container,
                false);
        setHasOptionsMenu(true);
        mContext = getActivity();
        firstVisit = true;
        initialiseUI();

        if (AppUtils.checkInternet(mContext)) {

            getStudentsListAPI(URL_GET_STUDENT_LIST);
        } else {
            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

        }
        return mRootView;
    }

    /*******************************************************
     * Method name : initialiseUI Description : initialise UI elements
     * Parameters : nil Return type : void Date : Jan 12, 2015 Author : Vandana
     * Surendranath
     *****************************************************/
    private void initialiseUI() {
        mTitleTextView = (TextView) mRootView.findViewById(R.id.titleTextView);
//        mAbsenceListView = (RecyclerView) mRootView.findViewById(R.id.mAbsenceListView);
        relMain = (RelativeLayout) mRootView.findViewById(R.id.relMain);
        paymentRelative = (RelativeLayout) mRootView.findViewById(R.id.paymentRelative);
        mStudentSpinner = (LinearLayout) mRootView.findViewById(R.id.studentSpinner);
        studImg = (ImageView) mRootView.findViewById(R.id.imagicon);
        history = mRootView.findViewById(R.id.linear);
        history.setVisibility(View.VISIBLE);
        amount = mRootView.findViewById(R.id.et_amount);
        addToWallet = (Button) mRootView.findViewById(R.id.addToWallet);
        walletbalance = mRootView.findViewById(R.id.walletbalance);
        card_walletbalance = mRootView.findViewById(R.id.card_walletbalance);
        belowViewRelative = (RelativeLayout) mRootView.findViewById(R.id.belowViewRelative);
        paymentRelative.setVisibility(View.GONE);
        belowViewRelative.setVisibility(View.VISIBLE);
        relMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        studentName = (TextView) mRootView.findViewById(R.id.studentName);
        mTitleTextView.setText(CANTEEN);
        mStudentSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (studentsModelArrayList.size() > 0) {
                    showSocialmediaList(studentsModelArrayList);
                    getWallet(URL_CANTEEN_WALLET, stud_id);
                    System.out.println("Student id spinner click" + stud_id);

                } else {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.student_not_available), R.drawable.exclamationicon, R.drawable.round);

                }
            }
        });
        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(mContext, PaymentHistoryActivity.class);
                startActivity(in);
            }
        });
        addToWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String payment_amount = amount.getText().toString();
                if (!payment_amount.equals("")){
                    long unixTime = System.currentTimeMillis() / 1000L;
                    orderId="NASDUBAI"+stud_id+"C"+unixTime;
                    System.out.println("Unix Time:::"+unixTime+"Order ID"+orderId);
                    Intent intent =new Intent(mContext,CanteenActviity.class);
                    intent.putExtra("orderId",orderId);
                    intent.putExtra("stud_id",stud_id);
                    intent.putExtra("amount",payment_amount);
                    startActivity(intent);
                }else{
                    Toast.makeText(mContext, "Please enter a valid amount", Toast.LENGTH_SHORT).show();
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

    public void getWallet(final String URL, final String student_id) {

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
                                    AppUtils.postInitParam(getActivity(), new AppUtils.GetAccessTokenInterface() {
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
    }

}
