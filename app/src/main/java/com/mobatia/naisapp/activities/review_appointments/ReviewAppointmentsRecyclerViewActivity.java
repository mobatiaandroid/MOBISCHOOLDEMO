package com.mobatia.naisapp.activities.review_appointments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.activities.review_appointments.adapter.ReviewAppointmentsRecyclerviewAdapter;
import com.mobatia.naisapp.activities.review_appointments.model.ReviewModel;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.constants.NameValueConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.HeaderManager;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.recyclerviewmanager.DividerItemDecoration;
import com.mobatia.naisapp.recyclerviewmanager.ItemOffsetDecoration;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;

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
                                            showDialogAlertSingleBtn((Activity) mContext, "Alert", "Successfully confirmed appointment.", R.drawable.tick, R.drawable.round);

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

                                            showDialogAlertSingleBtn((Activity) mContext, "Alert", "Slot not found.", R.drawable.exclamationicon, R.drawable.round);
                                        }else if (statusCode.equals(STATUS_BOOKING_DATE_EXPIRED)) {

                                            showDialogAlertSingleBtn((Activity) mContext, "Alert",getString(R.string.datexpirecontact), R.drawable.exclamationicon, R.drawable.round);
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
                                        showDialogAlertSingleBtn((Activity) mContext, "Alert", "Reserved Only – Please review and confirm bookingReserved Only – Please review and confirm booking", R.drawable.tick, R.drawable.round);

                                        photosListApiCall();
                                    } else if (statusCode.equals(STATUS_CANCEL)) {

                                        showDialogAlertSingleBtn((Activity) mContext, "Alert", "Request cancelled successfully.", R.drawable.tick, R.drawable.round);
                                        photosListApiCall();
                                    } else if (statusCode.equals(STATUS_BOOKED_BY_USER)) {

                                        showDialogAlertSingleBtn((Activity) mContext, "Alert", "Slot is already booked by an another user.", R.drawable.exclamationicon, R.drawable.round);
                                    }else if (statusCode.equals(STATUS_SLOT_NOT_FOUND)) {

                                        showDialogAlertSingleBtn((Activity) mContext, "Alert", "Slot not found.", R.drawable.exclamationicon, R.drawable.round);
                                    }else if (statusCode.equals(STATUS_BOOKING_DATE_EXPIRED)) {

                                        showDialogAlertSingleBtn((Activity) mContext, "Alert", getString(R.string.datexpirecontact), R.drawable.exclamationicon, R.drawable.round);
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
    public void showDialogAlertSingleBtn(final Activity activity, String msgHead, String msg, int ico, int bgIcon) {
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

}
