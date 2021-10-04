package com.mobatia.naisapp.activities.review_appointments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.activities.review_appointments.adapter.ReviewAppointmentsRecyclerviewAdapter;
import com.mobatia.naisapp.activities.review_appointments.model.ReviewModel;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.constants.NameValueConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.home.adapter.SurveyPagerAdapter;
import com.mobatia.naisapp.fragments.home.module.AnswerSubmitModel;
import com.mobatia.naisapp.fragments.home.module.SurveyAnswersModel;
import com.mobatia.naisapp.fragments.home.module.SurveyModel;
import com.mobatia.naisapp.fragments.home.module.SurveyQuestionsModel;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.HeaderManager;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.recyclerviewmanager.DividerItemDecoration;
import com.mobatia.naisapp.recyclerviewmanager.ItemOffsetDecoration;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Rijo on 25/1/17.
 */
public class ReviewAppointmentsRecyclerViewActivity extends Activity implements URLConstants, StatusConstants, JSONConstants, NameValueConstants, NaisClassNameConstants {
    Context mContext;
    private RecyclerView recycler_review;
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back;
    ImageView home;
    TextView confirmTV;
    ArrayList<ReviewModel> mVideoModelArrayList;
    ReviewAppointmentsRecyclerviewAdapter mVideoRecyclerviewAdapter;
    RecyclerView.LayoutManager recyclerViewLayoutManager;
    String confirmJsonArrayId = "";
    boolean confirmVisibility = false;
    String confirmJsonArrayIds = "";
    String confirmJsonArrayIdSingle = "";
    boolean confirmBySingle=false;
    ArrayList<String> list;
    ArrayList<String> lists;
    int survey_satisfation_status=0;
    int currentPage = 0;
    int currentPageSurvey = 0;
    private int surveySize=0;
    int pos=-1;
    ArrayList<SurveyModel> surveyArrayList;
    ArrayList<SurveyQuestionsModel> surveyQuestionArrayList;
    ArrayList<SurveyAnswersModel> surveyAnswersArrayList;
    ArrayList<AnswerSubmitModel>mAnswerList;
    TextView text_content;
    TextView text_dialog;
    private String surveyEmail="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_appontment_recycler_activity);
        mContext = this;
        confirmBySingle=false;
        initUI();
        if (AppUtils.isNetworkConnected(mContext)) {
            photosListApiCall();
        } else {
            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
        }
    }

    private void initUI() {
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        confirmTV = (TextView) findViewById(R.id.confirmTV);
        headermanager = new HeaderManager(ReviewAppointmentsRecyclerViewActivity.this, "Review Appointments");
        headermanager.getHeader(relativeHeader, 1);
        back = headermanager.getLeftButton();
        headermanager.setButtonLeftSelector(R.drawable.back,
                R.drawable.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        recycler_review = (RecyclerView) findViewById(R.id.recycler_review);
//        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
//        mSwipeRefreshLayout.setRefreshing(false);
        recycler_review.setHasFixedSize(true);
        recyclerViewLayoutManager = new GridLayoutManager(mContext, 1);
        int spacing = 10; // 50px
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext, spacing);

        //or
        recycler_review.addItemDecoration(
                new DividerItemDecoration(mContext.getResources().getDrawable(R.drawable.list_divider_teal)));
        recycler_review.addItemDecoration(itemDecoration);
        recycler_review.setLayoutManager(recyclerViewLayoutManager);
        confirmTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppUtils.isNetworkConnected(mContext)) {
                    confirmApiCall();
                } else {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
                }
            }
        });
    }



    private void photosListApiCall() {
        try {
            mVideoModelArrayList = new ArrayList<ReviewModel>();
            String[] name = {NAME_ACCESS_TOKEN, JTAG_USERS_ID,};
            String[] values = {PreferenceManager.getAccessToken(mContext), PreferenceManager.getUserId(mContext)};
            final VolleyWrapper manager = new VolleyWrapper(URL_GET_PTA_REVIEW_LIST);
            manager.getResponsePOST(mContext, 11, name, values,
                    new VolleyWrapper.ResponseListener() {

                        @Override
                        public void responseSuccess(String successResponse) {
                            if (successResponse != null) {
                                try {
                                    System.out.println("successResponse::" + successResponse);
                                    JSONObject rootObject = new JSONObject(successResponse);
                                    String responseCode = rootObject.getString(JTAG_RESPONSECODE);
                                    if (responseCode.equalsIgnoreCase(RESPONSE_SUCCESS)) {
                                        JSONObject responseObject = rootObject.optJSONObject(JTAG_RESPONSE);
                                        String statusCode = responseObject.getString(JTAG_STATUSCODE);
                                        if (statusCode.equalsIgnoreCase(STATUS_SUCCESS)) {

                                            JSONArray data = responseObject.optJSONArray(JTAG_RESPONSE_DATA_ARRAY);
                                            list = new ArrayList<String>();
                                            if (data.length() > 0) {
                                                for (int i = 0; i < data.length(); i++) {
                                                    JSONObject imageDetail = data.optJSONObject(i);
                                                    ReviewModel mPhotosModel = new ReviewModel();
                                                    mPhotosModel.setId(imageDetail.optString("id"));
                                                    mPhotosModel.setStudentId(imageDetail.optString("student_id"));
                                                    mPhotosModel.setStudentName(imageDetail.optString("student"));
                                                    mPhotosModel.setStudentPhoto(imageDetail.optString("student_photo"));
                                                    mPhotosModel.setStaffId(imageDetail.optString("staff_id"));
                                                    mPhotosModel.setStaffName(imageDetail.optString("staff"));
                                                    mPhotosModel.setDateAppointment(imageDetail.optString("date"));
                                                    mPhotosModel.setStartTime(imageDetail.optString("start_time"));
                                                    mPhotosModel.setEndTime(imageDetail.optString("end_time"));
                                                    mPhotosModel.setClassName(imageDetail.optString("class"));
                                                    mPhotosModel.setStatus(imageDetail.optString("status"));
                                                    mPhotosModel.setVpml(imageDetail.optString("vpml"));
                                                    mPhotosModel.setRoom(imageDetail.optString("room"));
                                                    mPhotosModel.setBook_end_date(imageDetail.optString("book_end_date"));
                                                    mPhotosModel.setBooking_open(imageDetail.optString("booking_open"));
                                                    try {
                                                        Date dateStart,dateEnd;
                                                        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                                                        dateStart = formatter.parse(imageDetail.optString("date")+" "+imageDetail.optString("start_time"));
                                                        dateEnd = formatter.parse(imageDetail.optString("date")+" "+imageDetail.optString("end_time"));
                                                        //Subtracting 6 hours from selected time
                                                        long timeStart = dateStart.getTime();
                                                        long timeEnd = dateEnd.getTime();

                                                        SimpleDateFormat formatterTime = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
                                                       String mStartTime = formatterTime.format(timeStart);
                                                       String mEndTime = formatterTime.format(timeEnd);
                                                        mPhotosModel.setStartTimeFormated(mStartTime);
                                                        mPhotosModel.setEndTimeFormated(mEndTime);
                                                        Log.e("StartTime ", mStartTime);
                                                        Log.e("EndTime ", mEndTime);

                                                    } catch (Exception e) {
                                                        Log.d("Exception", "" + e);
                                                    }
                                                    if (imageDetail.optString("status").equalsIgnoreCase("2") && imageDetail.optString("booking_open").equalsIgnoreCase("y")) {
                                                        list.add(imageDetail.optString(JTAG_ID));

                                                    }
                                                    mVideoModelArrayList.add(mPhotosModel);
                                                    if (list.size() > 0) {
                                                        JSONArray jsArray = new JSONArray(list);
                                                        confirmJsonArrayId = jsArray.toString();
                                                    }

                                                }
                                            } else {
//                                                AppUtils.showDialogAlertSingleBtnFinish((Activity) mContext, "Alert", "No Appointments Available", R.drawable.exclamationicon, R.drawable.round);
                                                AppUtils.showDialogAlertDismissFinish((Activity) mContext, "Alert", "No Appointments Available.", R.drawable.exclamationicon, R.drawable.round);

                                            }

                                            for (int j = 0; j < mVideoModelArrayList.size(); j++) {
                                                if (mVideoModelArrayList.get(j).getStatus().equalsIgnoreCase("2") && mVideoModelArrayList.get(j).getBooking_open().equalsIgnoreCase("y")) {
                                                    confirmVisibility = true;
                                                    break;
                                                } else {
                                                    confirmVisibility = false;
                                                }
                                            }
//                                            mVideoRecyclerviewAdapter = new ReviewAppointmentsRecyclerviewAdapter(mContext, mVideoModelArrayList);
                                            mVideoRecyclerviewAdapter = new ReviewAppointmentsRecyclerviewAdapter(mContext, mVideoModelArrayList, new ReviewAppointmentsRecyclerviewAdapter.CancelAppointment() {
                                                @Override
                                                public void cancelAppointmentPTA(int position) {
                                                    if (mVideoModelArrayList.get(position).getBooking_open().equalsIgnoreCase("y")) {

                                                        showDialogAlertDoubeBtn((Activity) mContext, "Alert", "Do you want to cancel this appointment?", R.drawable.questionmark_icon, R.drawable.round, "1", position);
                                                    }
                                                    else
                                                    {
                                                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.datexpirecontact), R.drawable.exclamationicon, R.drawable.round);

                                                    }
                                                }
                                            }, new ReviewAppointmentsRecyclerviewAdapter.ConfirmAppointment() {
                                                @Override
                                                public void confirmAppointmentPTA(int position) {
                                                    if (mVideoModelArrayList.get(position).getBooking_open().equalsIgnoreCase("y")) {

                                                        confirmBySingle = true;
                                                        lists = new ArrayList<String>();

                                                        for (int i = 0; i < mVideoModelArrayList.size(); i++) {
                                                            if (mVideoModelArrayList.get(i).getId().equalsIgnoreCase(mVideoModelArrayList.get(position).getId())) {
                                                                lists.add(mVideoModelArrayList.get(position).getId());
                                                                JSONArray jsArray = new JSONArray(lists);
                                                                confirmJsonArrayIdSingle = jsArray.toString();
                                                                break;
                                                            }
                                                        }
                                                        showDialogAlertDoubeBtn((Activity) mContext, "Alert", "Do you want to confirm this appointment?", R.drawable.questionmark_icon, R.drawable.round, "2", position);
                                                    }  else
                                                    {
                                                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.datexpirecontact), R.drawable.exclamationicon, R.drawable.round);

                                                    }
                                                }
                                            });
                                            recycler_review.setAdapter(mVideoRecyclerviewAdapter);
                                            if (confirmVisibility) {
                                                confirmTV.setVisibility(View.VISIBLE);

                                            } else {
                                                confirmTV.setVisibility(View.GONE);

                                            }
                                        } else if (statusCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                                                statusCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING)) {
                                            AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                                                @Override
                                                public void getAccessToken() {
                                                }
                                            });
                                            photosListApiCall();

                                        } else {
//                                            Toast.makeText(ReviewAppointmentsRecyclerViewActivity.this, "Status Failure:" + rootObject.getString("Status"), Toast.LENGTH_SHORT).show();
                                            AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                                        }

                                    } else if (responseCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) ||
                                            responseCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                                            responseCode.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
                                        AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                                            @Override
                                            public void getAccessToken() {
                                            }
                                        });
                                        photosListApiCall();

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
            // CustomStatusDialog(RESPONSE_FAILURE);
            e.printStackTrace();
        }
    }

    private void confirmApiCall() {
        try {
            if (confirmBySingle)
            {
                confirmJsonArrayIds=confirmJsonArrayIdSingle;
            }
            else
            {
                confirmJsonArrayIds=confirmJsonArrayId;
            }
            String[] name = {NAME_ACCESS_TOKEN, "ids"};

            String[] values = {PreferenceManager.getAccessToken(mContext), confirmJsonArrayIds};
            final VolleyWrapper manager = new VolleyWrapper(URL_PTA_CONFIRMATION);
            manager.getResponsePOST(mContext, 11, name, values,
                    new VolleyWrapper.ResponseListener() {

                        @Override
                        public void responseSuccess(String successResponse) {
                            if (successResponse != null) {
                                try {
                                    System.out.println("successResponse::" + successResponse);
                                    JSONObject rootObject = new JSONObject(successResponse);
                                    String responseCode = rootObject.getString(JTAG_RESPONSECODE);
                                    confirmBySingle=false;

                                    if (responseCode.equalsIgnoreCase(RESPONSE_SUCCESS)) {
                                        JSONObject responseObject = rootObject.optJSONObject(JTAG_RESPONSE);
                                        String statusCode = responseObject.getString(JTAG_STATUSCODE);
                                        if (statusCode.equalsIgnoreCase(STATUS_SUCCESS)) {
                                            int survey= responseObject.optInt("survey");
                                            showDialogAlertSingleBtn((Activity) mContext, "Alert", "Successfully confirmed appointment.", R.drawable.tick, R.drawable.round,survey);

//                                            Toast.makeText(mContext,"Successfully Confirmed.",Toast.LENGTH_SHORT).show();//rijo
                                            photosListApiCall();
                                        } else if (statusCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                                                statusCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING)) {
                                            AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                                                @Override
                                                public void getAccessToken() {
                                                }
                                            });
                                            confirmApiCall();

                                        } else if (statusCode.equals(STATUS_SLOT_NOT_FOUND)) {

                                            showDialogAlertSingleBtn((Activity) mContext, "Alert", "Slot not found.", R.drawable.exclamationicon, R.drawable.round,0);
                                        }else if (statusCode.equals(STATUS_BOOKING_DATE_EXPIRED)) {

                                            showDialogAlertSingleBtn((Activity) mContext, "Alert",getString(R.string.datexpirecontact), R.drawable.exclamationicon, R.drawable.round,0);
                                        }else {
//                                            Toast.makeText(ReviewAppointmentsRecyclerViewActivity.this, "Status Failure:" + rootObject.getString("Status"), Toast.LENGTH_SHORT).show();
                                            AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                                        }

                                    } else if (responseCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) ||
                                            responseCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                                            responseCode.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
                                        AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                                            @Override
                                            public void getAccessToken() {
                                            }
                                        });
                                        confirmApiCall();

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
            // CustomStatusDialog(RESPONSE_FAILURE);
            e.printStackTrace();
        }
    }
    public void showDialogAlertDoubeBtn(final Activity activity, String msgHead, String msg, int ico, int bgIcon, final String cancel, final int mPosition) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_dialogue_layout);
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
                if (cancel.equalsIgnoreCase("1")) {
                    postSelectedSlot(mPosition);
                }
                else
                {
                    confirmApiCall();
                }
                dialog.dismiss();
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

    public void postSelectedSlot(final int position) {

        try {
            final VolleyWrapper manager = new VolleyWrapper(URL_BOOK_PTA_INSERT_TIME_SLOT);
            String[] name = new String[]{JTAG_ACCESSTOKEN, JTAG_USERS_ID, JTAG_STAFF_ID, JTAG_STUDENT_ID, JTAG_TAB_DATE, "start_time", "end_time","vpml","room"};
            String[] value = new String[]{PreferenceManager.getAccessToken(mContext), PreferenceManager.getUserId(mContext), mVideoModelArrayList.get(position).getStaffId(), mVideoModelArrayList.get(position).getStudentId(),  mVideoModelArrayList.get(position).getDateAppointment(), mVideoModelArrayList.get(position).getStartTime(), mVideoModelArrayList.get(position).getEndTime(),mVideoModelArrayList.get(position).getVpml(),mVideoModelArrayList.get(position).getRoom()};
            manager.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {

                @Override
                public void responseSuccess(String successResponse) {
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
                                        showDialogAlertSingleBtn((Activity) mContext, "Alert", "Reserved Only – Please review and confirm bookingReserved Only – Please review and confirm booking", R.drawable.tick, R.drawable.round,0);

                                        photosListApiCall();
                                    } else if (statusCode.equals(STATUS_CANCEL)) {

                                        showDialogAlertSingleBtn((Activity) mContext, "Alert", "Request cancelled successfully.", R.drawable.tick, R.drawable.round,0);
                                        photosListApiCall();
                                    } else if (statusCode.equals(STATUS_BOOKED_BY_USER)) {

                                        showDialogAlertSingleBtn((Activity) mContext, "Alert", "Slot is already booked by an another user.", R.drawable.exclamationicon, R.drawable.round,0);
                                    }else if (statusCode.equals(STATUS_SLOT_NOT_FOUND)) {

                                        showDialogAlertSingleBtn((Activity) mContext, "Alert", "Slot not found.", R.drawable.exclamationicon, R.drawable.round,0);
                                    }else if (statusCode.equals(STATUS_BOOKING_DATE_EXPIRED)) {

                                        showDialogAlertSingleBtn((Activity) mContext, "Alert", getString(R.string.datexpirecontact), R.drawable.exclamationicon, R.drawable.round,0);
                                    } else {
                                        Toast.makeText(mContext, "Failure", Toast.LENGTH_SHORT).show();
                                    }
                                } else if (responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) ||
                                        responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                                        responsCode.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
                                    AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                                        @Override
                                        public void getAccessToken() {
                                        }
                                    });
                                    postSelectedSlot(position);

                                }
                            } else if (responsCode.equals(RESPONSE_ERROR)) {
//								CustomStatusDialog(RESPONSE_FAILURE);
                                Toast.makeText(mContext, "Failure", Toast.LENGTH_SHORT).show();

                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }

                @Override
                public void responseFailure(String failureResponse) {
                    // CustomStatusDialog(RESPONSE_FAILURE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    public void showDialogAlertSingleBtn(final Activity activity, String msgHead, String msg, int ico, int bgIcon,int survey) {
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
        textHead.setText(msgHead);

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_Ok);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (survey==1)
                {
                    callSurveyApi();
                }
                dialog.dismiss();
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


    /**********************************SURVEY API***************************************/
    public void callSurveyApi() {
        surveyArrayList=new ArrayList<>();


        try {
            final VolleyWrapper manager = new VolleyWrapper(URL_GET_USER_SURVEY);
            String[] name = new String[]{JTAG_ACCESSTOKEN,"users_id","module"};
            String[] value = new String[]{PreferenceManager.getAccessToken(mContext),PreferenceManager.getUserId(mContext),"3"};
            manager.getResponsePOST(mContext, 14, name, value, new VolleyWrapper.ResponseListener() {

                @Override
                public void responseSuccess(String successResponse) {
                    String responsCode = "";
                    if (successResponse != null) {
                        try {
                            Log.e("SURVEY VALUE",successResponse);
                            JSONObject rootObject = new JSONObject(successResponse);
                            if (rootObject.optString(JTAG_RESPONSE) != null) {
                                responsCode = rootObject.optString(JTAG_RESPONSECODE);
                                if (responsCode.equals(RESPONSE_SUCCESS)) {
                                    JSONObject respObject = rootObject.getJSONObject(JTAG_RESPONSE);
                                    String statusCode = respObject.optString(JTAG_STATUSCODE);
                                    if (statusCode.equals(STATUS_SUCCESS)) {

                                        PreferenceManager.setIsSurveyHomeVisible(mContext,true);
                                        surveyEmail=respObject.optString("contact_email");
                                        JSONArray dataArray=respObject.getJSONArray("data");
                                        if (dataArray.length()>0)
                                        {
                                            surveySize=dataArray.length();
                                            for (int i=0;i<dataArray.length();i++)
                                            {
                                                JSONObject dataObject = dataArray.getJSONObject(i);
                                                SurveyModel model=new SurveyModel();
                                                model.setId(dataObject.optString("id"));
                                                model.setSurvey_name(dataObject.optString("survey_name"));
                                                model.setImage(dataObject.optString("image"));
                                                model.setTitle(dataObject.optString("title"));
                                                model.setDescription(dataObject.optString("description"));
                                                model.setCreated_at(dataObject.optString("created_at"));
                                                model.setUpdated_at(dataObject.optString("updated_at"));
                                                surveyQuestionArrayList=new ArrayList<>();
                                                JSONArray questionsArray=dataObject.getJSONArray("questions");
                                                if (questionsArray.length()>0)
                                                {
                                                    for (int j=0;j<questionsArray.length();j++)
                                                    {
                                                        JSONObject questionsObject = questionsArray.getJSONObject(j);
                                                        SurveyQuestionsModel mModel=new SurveyQuestionsModel();
                                                        mModel.setId(questionsObject.optString("id"));
                                                        mModel.setQuestion(questionsObject.optString("question"));
                                                        mModel.setAnswer_type(questionsObject.optString("answer_type"));
                                                        mModel.setAnswer("");
                                                        surveyAnswersArrayList=new ArrayList<>();
                                                        JSONArray answerArray=questionsObject.getJSONArray("offered_answers");
                                                        if (answerArray.length()>0)
                                                        {
                                                            for (int k=0;k<answerArray.length();k++)
                                                            {
                                                                JSONObject answerObject = answerArray.getJSONObject(k);
                                                                SurveyAnswersModel nModel=new SurveyAnswersModel();
                                                                nModel.setId(answerObject.optString("id"));
                                                                nModel.setAnswer(answerObject.optString("answer"));
                                                                nModel.setClicked(false);
                                                                nModel.setClicked0(false);

                                                                surveyAnswersArrayList.add(nModel);
                                                            }
                                                        }
                                                        mModel.setSurveyAnswersrrayList(surveyAnswersArrayList);
                                                        surveyQuestionArrayList.add(mModel);
                                                    }
                                                }
                                                model.setSurveyQuestionsArrayList(surveyQuestionArrayList);
                                                surveyArrayList.add(model);
                                            }
                                            //showSurvey(getActivity(),surveyArrayList);
                                            showSurveyWelcomeDialogue((Activity) mContext,surveyArrayList,false);
                                        }
                                        else {

                                        }

                                    }
                                }
                                else if (responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) ||
                                        responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                                        responsCode.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
                                    AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                                        @Override
                                        public void getAccessToken() {
                                        }
                                    });
                                    callSurveyApi();

                                }
                            } else if (responsCode.equals(RESPONSE_ERROR)) {
//								CustomStatusDialog(RESPONSE_FAILURE);

                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }

                @Override
                public void responseFailure(String failureResponse) {
                    // CustomStatusDialog(RESPONSE_FAILURE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    /**********************************SURVEY DIALOGUES***************************************/
    public void showSurveyWelcomeDialogue(final Activity activity, final ArrayList<SurveyModel> surveyArrayList,final Boolean isThankyou)
    {
//        final Dialog dialog = new Dialog(activity,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
//        dialog.requestWindowFeature(R.style.full_screen_dialog);
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_survey_welcome_screen);
        Button startNowBtn = (Button) dialog.findViewById(R.id.startNowBtn);

        ImageView imgClose = (ImageView) dialog.findViewById(R.id.closeImg);
        TextView headingTxt = (TextView) dialog.findViewById(R.id.titleTxt);
        TextView descriptionTxt = (TextView) dialog.findViewById(R.id.descriptionTxt);
//        if (isThankyou)
//		{
//			thankyouTxt.setVisibility(View.VISIBLE);
//			thankyouTxt.setText("Thank you For Submitting your Survey");
//		}
//        else {
//			thankyouTxt.setVisibility(View.GONE);
//		}

        headingTxt.setText(surveyArrayList.get(pos+1).getTitle());
        descriptionTxt.setText(surveyArrayList.get(pos+1).getDescription());

        ImageView bannerImg = (ImageView) dialog.findViewById(R.id.bannerImg);
        if (!surveyArrayList.get(pos+1).getImage().equalsIgnoreCase(""))
        {
            Picasso.with(mContext).load(AppUtils.replace(surveyArrayList.get(pos+1).getImage())).placeholder(R.drawable.survey).fit().into(bannerImg);
        }
        else
        {
            bannerImg.setImageResource(R.drawable.survey);
        }
        startNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (surveyArrayList.size()>0)
                {
                    pos=pos+1;
                    if (pos<surveyArrayList.size())
                    {
                        showSurveyQuestionAnswerDialog(activity,surveyArrayList.get(pos).getSurveyQuestionsArrayList(),surveyArrayList.get(pos).getSurvey_name(),surveyArrayList.get(pos).getId(),surveyArrayList.get(pos).getContact_email());
                    }
                }

            }
        });
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showCloseSurveyDialog(activity,dialog);
            }
        });
        Button skipBtn = (Button) dialog.findViewById(R.id.skipBtn);
        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                //showCloseSurveyDialog(activity,dialog);
            }
        });
        dialog.show();

    }


    public void showCloseSurveyDialog(final Activity activity, final Dialog dialogCLose)
    {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_close_survey);
        TextView text_dialog = (TextView) dialog.findViewById(R.id.text_dialog);
        text_dialog.setText("Are you sure you want to close this survey.");

        Button btn_Ok = (Button) dialog.findViewById(R.id.btn_Ok);
        btn_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set Preference to true
                dialogCLose.dismiss();
                dialog.dismiss();


            }
        });

        Button btn_Cancel = (Button) dialog.findViewById(R.id.btn_Cancel);
        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });
        dialog.show();

    }


    private void sendEmailToStaff(String URL,String email,Dialog dialogThank,Dialog dialog) {
        VolleyWrapper volleyWrapper=new VolleyWrapper(URL);
        String[] name={"access_token","email","users_id","title","message"};
        String[] value={PreferenceManager.getAccessToken(mContext),email,PreferenceManager.getUserId(mContext),text_dialog.getText().toString(),text_content.getText().toString()};//contactEmail

        //String[] value={PreferenceManager.getAccessToken(mContext),mStaffList.get(pos).getStaffEmail(),JTAG_USERS_ID_VALUE,text_dialog.getText().toString(),text_content.getText().toString()};
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
                            Toast toast = Toast.makeText(mContext, "Successfully sent email to staff", Toast.LENGTH_SHORT);
                            toast.show();
                            dialogThank.dismiss();
                            dialog.dismiss();
                        } else {
                            Toast toast = Toast.makeText(mContext, "Email not sent", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        sendEmailToStaff(URL_SEND_EMAIL_TO_STAFF,email,dialogThank,dialog);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        sendEmailToStaff(URL_SEND_EMAIL_TO_STAFF,email,dialogThank,dialog);


                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        sendEmailToStaff(URL_SEND_EMAIL_TO_STAFF,email,dialogThank,dialog);

                    } else {
						/*CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
								, getResources().getString(R.string.ok));
						dialog.show();*/
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    }
                } catch (Exception ex)
                {
                    System.out.println("The Exception in edit profile is" + ex.toString());
                }

            }

            @Override
            public void responseFailure(String failureResponse) {
				/*CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
						, getResources().getString(R.string.ok));
				dialog.show();*/
                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

            }
        });


    }


    public void showSurveyThankYouDialog(final Activity activity, final ArrayList<SurveyModel> surveyArrayList,final Boolean isThankyou)
    {
        final Dialog dialogThank = new Dialog(activity);
        dialogThank.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogThank.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogThank.setCancelable(false);
        dialogThank.setContentView(R.layout.dialog_survey_thank_you);
        survey_satisfation_status=0;
        //callSurveySubmitApi(URL_SURVEY_SUBMIT,surveyId,jsonData,getActivity(),surveyArrayList,isThankyou,survey_satisfation_status,dialog);

        Button btn_Ok = (Button) dialogThank.findViewById(R.id.btn_Ok);
        ImageView emailImg = (ImageView) dialogThank.findViewById(R.id.emailImg);
        if (surveyEmail.equalsIgnoreCase(""))
        {
            emailImg.setVisibility(View.GONE);
        }
        else
        {
            emailImg.setVisibility(View.VISIBLE);
        }

        emailImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(mContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.alert_send_email_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Button dialogCancelButton = (Button) dialog.findViewById(R.id.cancelButton);
                Button submitButton = (Button) dialog.findViewById(R.id.submitButton);
                text_dialog = (EditText) dialog.findViewById(R.id.text_dialog);
                text_content = (EditText) dialog.findViewById(R.id.text_content);


                dialogCancelButton.setOnClickListener(new View.OnClickListener() {

                    @Override

                    public void onClick(View v) {
                        //   AppUtils.hideKeyBoard(mContext);
                        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(text_dialog.getWindowToken(), 0);
                        imm.hideSoftInputFromWindow(text_content.getWindowToken(), 0);
                        dialog.dismiss();

                    }

                });

                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendEmailToStaff(URL_SEND_EMAIL_TO_STAFF,surveyEmail,dialogThank,dialog);
                    }
                });


                dialog.show();
            }
        });

        btn_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (isThankyou)
                {
                    Log.e("SURVEY SIZE","Thank");
                    showSurveyWelcomeDialogue(activity,surveyArrayList,true);

                }
                else
                {

                }
                dialogThank.dismiss();
            }
        });
        dialogThank.show();

    }

    private void callSurveySubmitApi(String URL,String survey_id,String answer,Activity activity,ArrayList<SurveyModel> surveyArrayList, Boolean isThankyou,int survey_satisfation_status) {
        VolleyWrapper volleyWrapper=new VolleyWrapper(URL);
        String[] name={"access_token","users_id","survey_id","answers","survey_satisfation_status"};
        Log.e("STATUs",String.valueOf(survey_satisfation_status));
        String[] value={PreferenceManager.getAccessToken(mContext),PreferenceManager.getUserId(mContext),survey_id,answer,String.valueOf(survey_satisfation_status)};//contactEmail
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

                            showSurveyThankYouDialog((Activity) mContext,surveyArrayList,isThankyou);


                        } else {


                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        callSurveySubmitApi(URL_SURVEY_SUBMIT,survey_id,answer,activity,surveyArrayList,isThankyou,survey_satisfation_status);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        callSurveySubmitApi(URL_SURVEY_SUBMIT,survey_id,answer,activity,surveyArrayList,isThankyou,survey_satisfation_status);


                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        callSurveySubmitApi(URL_SURVEY_SUBMIT,survey_id,answer,activity,surveyArrayList,isThankyou,survey_satisfation_status);

                    } else {
						/*CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
								, getResources().getString(R.string.ok));
						dialog.show();*/
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    }
                } catch (Exception ex)
                {
                    System.out.println("The Exception in edit profile is" + ex.toString());
                }

            }

            @Override
            public void responseFailure(String failureResponse) {
				/*CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
						, getResources().getString(R.string.ok));
				dialog.show();*/
                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

            }
        });


    }



    public void showSurveyQuestionAnswerDialog(final Activity activity, final ArrayList<SurveyQuestionsModel> surveyQuestionArrayList,final String surveyname,String surveyID,String contactEmail)
    {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_question_choice_survey);
        ViewPager surveyPager = (ViewPager) dialog.findViewById(R.id.surveyPager);
        TextView questionCount = (TextView) dialog.findViewById(R.id.questionCount);
        TextView nxtQnt = (TextView) dialog.findViewById(R.id.nxtQnt);
        TextView currentQntTxt = (TextView) dialog.findViewById(R.id.currentQntTxt);
        TextView surveyName = (TextView) dialog.findViewById(R.id.surveyName);
        Button skipBtn = (Button) dialog.findViewById(R.id.skipBtn);
        ImageView previousBtn = (ImageView) dialog.findViewById(R.id.previousBtn);
        ImageView nextQuestionBtn = (ImageView) dialog.findViewById(R.id.nextQuestionBtn);
        ImageView emailImg = (ImageView) dialog.findViewById(R.id.emailImg);
        ImageView closeImg = (ImageView) dialog.findViewById(R.id.closeImg);
        ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.progressBar);
        progressBar.setMax(surveyQuestionArrayList.size());
        progressBar.getProgressDrawable().setColorFilter(mContext.getResources().getColor(R.color.rel_one), android.graphics.PorterDuff.Mode.SRC_IN);
        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showCloseSurveyDialog(activity,dialog);
            }
        });
        if (surveyQuestionArrayList.size()>9)
        {
            currentQntTxt.setText("01");
            questionCount.setText("/"+String.valueOf(surveyQuestionArrayList.size()));
        }
        else {
            currentQntTxt.setText("01");
            questionCount.setText("/0"+String.valueOf(surveyQuestionArrayList.size()));
        }
        surveyName.setText(surveyname);
//        if (contactEmail.equalsIgnoreCase(""))
//		{
//			emailImg.setVisibility(View.GONE);
//		}
//        else {
//			emailImg.setVisibility(View.GONE);
//		}



        currentPageSurvey=1;
        surveyPager.setCurrentItem(currentPageSurvey-1);
        progressBar.setProgress(currentPageSurvey);
        surveyPager.setAdapter(new SurveyPagerAdapter(activity,surveyQuestionArrayList,nextQuestionBtn));
        if(currentPageSurvey==surveyQuestionArrayList.size())
        {
            previousBtn.setVisibility(View.INVISIBLE);
            nextQuestionBtn.setVisibility(View.INVISIBLE);
            nxtQnt.setVisibility(View.VISIBLE);
        }
        else
        {
            if (currentPageSurvey==1)
            {
                previousBtn.setVisibility(View.INVISIBLE);
                nextQuestionBtn.setVisibility(View.VISIBLE);
                nxtQnt.setVisibility(View.INVISIBLE);
            }
            else {
                previousBtn.setVisibility(View.INVISIBLE);
                nextQuestionBtn.setVisibility(View.VISIBLE);
                nxtQnt.setVisibility(View.INVISIBLE);
            }
        }

        nextQuestionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currentPageSurvey==surveyQuestionArrayList.size())
                {

                }
                else {
                    currentPageSurvey++;
                    progressBar.setProgress(currentPageSurvey);
                    surveyPager.setCurrentItem(currentPageSurvey-1);
                    if (currentPageSurvey==surveyQuestionArrayList.size())
                    {
                        nextQuestionBtn.setVisibility(View.INVISIBLE);
                        previousBtn.setVisibility(View.VISIBLE);
                        nxtQnt.setVisibility(View.VISIBLE);

                    }else {
                        nextQuestionBtn.setVisibility(View.VISIBLE);
                        previousBtn.setVisibility(View.VISIBLE);
                        nxtQnt.setVisibility(View.INVISIBLE);
                    }
                }

                if (surveyQuestionArrayList.size()>9)
                {
                    if (currentPageSurvey<9)
                    {
                        currentQntTxt.setText("0"+currentPageSurvey);
                        questionCount.setText("/"+String.valueOf(surveyQuestionArrayList.size()));
                    }
                    else {
                        currentQntTxt.setText(currentPageSurvey);
                        questionCount.setText("/"+String.valueOf(surveyQuestionArrayList.size()));
                    }

                }
                else {
                    if (currentPageSurvey<9)
                    {
                        currentQntTxt.setText("0"+currentPageSurvey);
                        questionCount.setText("/"+"0"+String.valueOf(surveyQuestionArrayList.size()));
                    }
                    else {
                        currentQntTxt.setText(currentPageSurvey);
                        questionCount.setText("/"+"0"+String.valueOf(surveyQuestionArrayList.size()));
                    }
                }

            }
        });
        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPageSurvey==1)
                {
                    previousBtn.setVisibility(View.INVISIBLE);
                    nxtQnt.setVisibility(View.INVISIBLE);
                    if (currentPageSurvey==surveyQuestionArrayList.size())
                    {
                        nxtQnt.setVisibility(View.VISIBLE);
                    }
                    else {
                        nxtQnt.setVisibility(View.INVISIBLE);
                    }
                }
                else {

                    currentPageSurvey--;
                    progressBar.setProgress(currentPageSurvey);
                    surveyPager.setCurrentItem(currentPageSurvey-1);
                    if (currentPageSurvey==surveyQuestionArrayList.size())
                    {
                        nextQuestionBtn.setVisibility(View.INVISIBLE);
                        previousBtn.setVisibility(View.VISIBLE);
                        nxtQnt.setVisibility(View.VISIBLE);

                    }else {
                        if (currentPageSurvey==1)
                        {
                            previousBtn.setVisibility(View.INVISIBLE);
                            nextQuestionBtn.setVisibility(View.VISIBLE);
                            nxtQnt.setVisibility(View.INVISIBLE);
                        }
                        else {
                            nextQuestionBtn.setVisibility(View.VISIBLE);
                            previousBtn.setVisibility(View.VISIBLE);
                            nxtQnt.setVisibility(View.INVISIBLE);
                        }

                    }
                }


                if (surveyQuestionArrayList.size()>9)
                {
                    if (currentPageSurvey<9)
                    {
                        currentQntTxt.setText("0"+currentPageSurvey);
                        questionCount.setText("/"+String.valueOf(surveyQuestionArrayList.size()));
                    }
                    else {
                        currentQntTxt.setText(currentPageSurvey);
                        questionCount.setText("/"+String.valueOf(surveyQuestionArrayList.size()));
                    }

                }
                else {
                    if (currentPageSurvey<9)
                    {
                        currentQntTxt.setText("0"+currentPageSurvey);
                        questionCount.setText("/"+"0"+String.valueOf(surveyQuestionArrayList.size()));
                    }
                    else {
                        currentQntTxt.setText(currentPageSurvey);
                        questionCount.setText("/"+"0"+String.valueOf(surveyQuestionArrayList.size()));
                    }
                }

            }
        });
        nxtQnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isFound=false;
                int pos=-1;
                int emptyvalue=0;
                for (int i=0;i<surveyQuestionArrayList.size();i++)
                {
                    if (surveyQuestionArrayList.get(i).getAnswer().equalsIgnoreCase(""))
                    {
                        emptyvalue=emptyvalue+1;
                        if (!isFound)
                        {
                            isFound=true;
                            pos=i;
                        }
                    }
                }
                if (isFound)
                {
                    mAnswerList=new ArrayList<>();
                    for (int k=0;k<surveyQuestionArrayList.size();k++)
                    {
                        AnswerSubmitModel model=new AnswerSubmitModel();
                        model.setQuestion_id(surveyQuestionArrayList.get(k).getId());
                        model.setAnswer_id(surveyQuestionArrayList.get(k).getAnswer());
                        mAnswerList.add(model);
                    }
                    Gson gson   = new Gson();
                    ArrayList<String> PassportArray = new ArrayList<>();
                    for (int i=0;i<mAnswerList.size();i++)
                    {
                        AnswerSubmitModel nmodel=new AnswerSubmitModel();
                        nmodel.setAnswer_id(mAnswerList.get(i).getAnswer_id());
                        nmodel.setQuestion_id(mAnswerList.get(i).getQuestion_id());
                        String json = gson.toJson(nmodel);
                        PassportArray.add(i,json);
                    }
                    String JSON_STRING=""+PassportArray+"";
                    Log.e("JSON",JSON_STRING);
                    if (emptyvalue==surveyQuestionArrayList.size())
                    {
                        boolean isEmpty=true;
                        showSurveyContinueDialog(activity,surveyID,JSON_STRING,surveyArrayList,progressBar,surveyPager,surveyQuestionArrayList,previousBtn,nextQuestionBtn,nxtQnt,currentQntTxt,questionCount,pos,dialog,isEmpty);

                    }
                    else {
                        boolean isEmpty=false;
                        showSurveyContinueDialog(activity,surveyID,JSON_STRING,surveyArrayList,progressBar,surveyPager,surveyQuestionArrayList,previousBtn,nextQuestionBtn,nxtQnt,currentQntTxt,questionCount,pos,dialog,isEmpty);

                    }


                }
                else
                {
                    surveySize=surveySize-1;
                    if (surveySize<=0)
                    {
                        mAnswerList=new ArrayList<>();
                        for (int k=0;k<surveyQuestionArrayList.size();k++)
                        {
                            AnswerSubmitModel model=new AnswerSubmitModel();
                            model.setQuestion_id(surveyQuestionArrayList.get(k).getId());
                            model.setAnswer_id(surveyQuestionArrayList.get(k).getAnswer());
                            mAnswerList.add(model);
                        }
                        Gson gson   = new Gson();
                        ArrayList<String> PassportArray = new ArrayList<>();
                        for (int i=0;i<mAnswerList.size();i++)
                        {
                            AnswerSubmitModel nmodel=new AnswerSubmitModel();
                            nmodel.setAnswer_id(mAnswerList.get(i).getAnswer_id());
                            nmodel.setQuestion_id(mAnswerList.get(i).getQuestion_id());
                            String json = gson.toJson(nmodel);
                            PassportArray.add(i,json);
                        }
                        String JSON_STRING=""+PassportArray+"";
                        Log.e("JSON",JSON_STRING);
                        dialog.dismiss();
                        callSurveySubmitApi(URL_SURVEY_SUBMIT,surveyID,JSON_STRING,(Activity) mContext,surveyArrayList,false,1);
                    }
                    else {
                        mAnswerList=new ArrayList<>();
                        for (int k=0;k<surveyQuestionArrayList.size();k++)
                        {
                            AnswerSubmitModel model=new AnswerSubmitModel();
                            model.setQuestion_id(surveyQuestionArrayList.get(k).getId());
                            model.setAnswer_id(surveyQuestionArrayList.get(k).getAnswer());
                            mAnswerList.add(model);
                        }
                        Gson gson   = new Gson();
                        ArrayList<String> PassportArray = new ArrayList<>();
                        for (int i=0;i<mAnswerList.size();i++)
                        {
                            AnswerSubmitModel nmodel=new AnswerSubmitModel();
                            nmodel.setAnswer_id(mAnswerList.get(i).getAnswer_id());
                            nmodel.setQuestion_id(mAnswerList.get(i).getQuestion_id());
                            String json = gson.toJson(nmodel);
                            PassportArray.add(i,json);
                        }
                        String JSON_STRING=""+PassportArray+"";
                        Log.e("JSON",JSON_STRING);
                        dialog.dismiss();
                        callSurveySubmitApi(URL_SURVEY_SUBMIT,surveyID,JSON_STRING,(Activity) mContext,surveyArrayList,true,1);
                    }


                }

                Log.e("POS",String.valueOf(pos));


            }

        });

        dialog.show();

    }



    public void showSurveyContinueDialog(final Activity activity,String surveyID,String JSON_STRING,ArrayList<SurveyModel> surveyArrayList,ProgressBar progressBar,ViewPager surveyPager,ArrayList<SurveyQuestionsModel>surveyQuestionArrayList,ImageView previousBtn,ImageView nextQuestionBtn,TextView nxtQnt,TextView currentQntTxt,TextView questionCount, int pos,Dialog nDialog,boolean isEmpty)
    {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_continue_layout);
        survey_satisfation_status=0;
        //callSurveySubmitApi(URL_SURVEY_SUBMIT,surveyId,jsonData,getActivity(),surveyArrayList,isThankyou,survey_satisfation_status,dialog);
        Button btn_Ok = (Button) dialog.findViewById(R.id.btn_Ok);
        Button submit = (Button) dialog.findViewById(R.id.submit);
        TextView thoughtsTxt = (TextView) dialog.findViewById(R.id.thoughtsTxt);

        if (isEmpty)
        {
            submit.setClickable(false);
            submit.setAlpha(0.5f);
            thoughtsTxt.setText("Appreciate atleast one feedback from you.");
        }
        else {
            submit.setClickable(true);
            submit.setAlpha(1.0f);
            thoughtsTxt.setText("There is an unanswered question on this survey. Would you like to continue?");
        }
        submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if (!isEmpty)
                {
                    nDialog.dismiss();
                    Log.e("SURVEY SIZE",String.valueOf(surveySize));
                    surveySize=surveySize-1;
                    if (surveySize<=0)
                    {
                        Log.e("SURVEY SIZE ",String.valueOf(surveySize));
                        callSurveySubmitApi(URL_SURVEY_SUBMIT,surveyID,JSON_STRING,(Activity) mContext,surveyArrayList,false,1);
                    }
                    else {
                        callSurveySubmitApi(URL_SURVEY_SUBMIT,surveyID,JSON_STRING,(Activity) mContext
                                ,surveyArrayList,true,1);

                    }
                    dialog.dismiss();
                }



            }
        });
        btn_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentPageSurvey=pos+1;
                progressBar.setProgress(currentPageSurvey);
                surveyPager.setCurrentItem(currentPageSurvey-1);

                Log.e("WORKING","SURVEY COUNT"+String.valueOf(currentPageSurvey));
                if(surveyQuestionArrayList.size()>1)
                {
                    if (currentPageSurvey!=surveyQuestionArrayList.size())
                    {
                        if(currentPageSurvey==1)
                        {
                            previousBtn.setVisibility(View.INVISIBLE);
                            nextQuestionBtn.setVisibility(View.VISIBLE);
                            nxtQnt.setVisibility(View.INVISIBLE);
                        }
                        else {
                            if(currentPageSurvey==1)
                            {
                                previousBtn.setVisibility(View.INVISIBLE);
                                nextQuestionBtn.setVisibility(View.VISIBLE);
                                nxtQnt.setVisibility(View.INVISIBLE);
                            }
                            else {
                                previousBtn.setVisibility(View.VISIBLE);
                                nextQuestionBtn.setVisibility(View.VISIBLE);
                                nxtQnt.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                    else {
                        previousBtn.setVisibility(View.VISIBLE);
                        nextQuestionBtn.setVisibility(View.INVISIBLE);
                        nxtQnt.setVisibility(View.VISIBLE);
                    }

                }
                else {
                    if (currentPageSurvey==1)
                    {
                        previousBtn.setVisibility(View.INVISIBLE);
                        nextQuestionBtn.setVisibility(View.INVISIBLE);
                        nxtQnt.setVisibility(View.VISIBLE);
                    }
                }
                if (surveyQuestionArrayList.size()>9)
                {
                    if (currentPageSurvey<9)
                    {
                        currentQntTxt.setText("0"+currentPageSurvey);
                        questionCount.setText("/"+String.valueOf(surveyQuestionArrayList.size()));
                    }
                    else {
                        currentQntTxt.setText(currentPageSurvey);
                        questionCount.setText("/"+String.valueOf(surveyQuestionArrayList.size()));
                    }

                }
                else {
                    if (currentPageSurvey<9)
                    {
                        currentQntTxt.setText("0"+currentPageSurvey);
                        questionCount.setText("/"+"0"+String.valueOf(surveyQuestionArrayList.size()));
                    }
                    else {
                        currentQntTxt.setText(currentPageSurvey);
                        questionCount.setText("/"+"0"+String.valueOf(surveyQuestionArrayList.size()));
                    }
                }

                dialog.dismiss();
            }
        });
        dialog.show();

    }



}
