package com.mobatia.naisapp.activities.parents_evening;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.activities.parents_evening.adapter.ParentsEveningTimeSlotRecyclerviewAdapter;
import com.mobatia.naisapp.activities.review_appointments.ReviewAppointmentsRecyclerViewActivity;
import com.mobatia.naisapp.calender.model.CalendarEventsModel;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.constants.ResultConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.parents_evening.adapter.ParentsEveningRoomListAdapter;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.HeaderManager;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.recyclerviewmanager.DividerItemDecoration;
import com.mobatia.naisapp.recyclerviewmanager.ItemOffsetDecoration;
import com.mobatia.naisapp.recyclerviewmanager.RecyclerItemListener;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by gayatri on 23/3/17.
 */
public class ParentsEveningTimeSlotActivity extends Activity implements JSONConstants, URLConstants, ResultConstants, StatusConstants, NaisClassNameConstants {
    Context mContext = this;
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back, continueImageView,parentshomeImg,infoImg, home,infoRoomImg;
    ArrayList<CalendarEventsModel> mListViewArray=new ArrayList<>();
    ArrayList<CalendarEventsModel> mListViewArrayPost;
    Bundle extras;
    String mStaffid = "";
    Calendar cal;
    TextView studentNameTV;
    TextView studentClassTV;
    TextView cancelTextView;
    TextView reviewConfirmTextView;
    TextView dateTextView;
    TextView staffTV;
    RecyclerView.LayoutManager recyclerViewLayoutManager;
    RecyclerView recycler_view_alloted_time;
    private String mStudentId = "";
    private String mStudentName = "";
    private String mStaffName = "";
    private String mClass = "";
    private int mDay;
    private int mMonth;
    private String mMonthString = "";
    private int mYear;
    private String selectedDate = "";
    boolean alreadyslotBookedByUser = false;
    boolean confirmedslotBookedByUser = false;
    String confirmedLink="";
    String dateTextValue;
    Button vpmlBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parents_evening_time_slot);
        mContext = this;
        initUI();
        if (AppUtils.isNetworkConnected(mContext)) {
            getPtaAllotedDateList();
        } else {
            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

        }

    }

    private void initUI() {
        cal = Calendar.getInstance();
        extras = getIntent().getExtras();
        if (extras != null) {
            mStaffid = extras.getString(JTAG_STAFF_ID);
            mStudentId = extras.getString(JTAG_STUDENT_ID);
            mStudentName = extras.getString("studentName");
            mStaffName = extras.getString("staffName");
            mClass = extras.getString("studentClass");
            selectedDate = extras.getString("selectedDate");
            if (!selectedDate.equalsIgnoreCase("")) {

                SimpleDateFormat format = new SimpleDateFormat(
                        "yyyy-MM-dd", Locale.ENGLISH);
                try {
                    Date date = format.parse(selectedDate);
                    Calendar cal1 = Calendar.getInstance();
                    cal1.setTime(date);
                    mDay = cal1.get(Calendar.DAY_OF_MONTH);
                    mYear = cal1.get(Calendar.YEAR);
                    mMonth = cal1.get(Calendar.MONTH);
                    mMonthString = getMonth(cal1.get(Calendar.MONTH));

                } catch (ParseException e) {
                    e.getLocalizedMessage();
                }
            }
        }
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        studentNameTV = (TextView) findViewById(R.id.studentNameTV);
        studentClassTV = (TextView) findViewById(R.id.studentClassTV);
        staffTV = (TextView) findViewById(R.id.staffTV);
        cancelTextView = (TextView) findViewById(R.id.cancelTextView);
        reviewConfirmTextView = (TextView) findViewById(R.id.reviewConfirmTextView);
        dateTextView = (TextView) findViewById(R.id.dateTextView);
        vpmlBtn = (Button) findViewById(R.id.vpmlBtn);
        infoRoomImg = (ImageView) findViewById(R.id.infoRoomImg);
        recycler_view_alloted_time = (RecyclerView) findViewById(R.id.recycler_view_alloted_time);
        staffTV.setText(mStaffName);
        studentClassTV.setText(mClass);
        studentNameTV.setText(mStudentName);
        dateTextValue = mDay + " " + mMonthString + " " + mYear;
        dateTextView.setText(dateTextValue);
        headermanager = new HeaderManager(ParentsEveningTimeSlotActivity.this, PARENT_EVENING);
        headermanager.getHeader(relativeHeader, 0);
//        headermanager.getHeader(relativeHeader, 3);
        back = headermanager.getLeftButton();
        parentshomeImg=headermanager.getRightButton();
  /*      infoImg=headermanager.getRightInfoImage();
        infoImg.setImageResource(R.drawable.infoptaicon);
        infoImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mContext, ParentsEveninginfoActivity.class);

                startActivity(mIntent);
            }
        });*/
        headermanager.setButtonRightSelector(R.drawable.parents_select, R.drawable.parents_select);
        headermanager.setButtonLeftSelector(R.drawable.back,
                R.drawable.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceManager.setBackParent(mContext, false);
                AppUtils.hideKeyBoard(mContext);
                finish();
            }
        });
        reviewConfirmTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogAlertReview((Activity) mContext, "Alert", "Reserved Only - Please review and confirm booking.", R.drawable.exclamationicon, R.drawable.round);


            }
        });
        vpmlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("ConfirmedLink"+confirmedLink);
                if (confirmedLink.equalsIgnoreCase(""))
                {

                }
                else
                {
                    Intent viewIntent =
                            new Intent("android.intent.action.VIEW",
                                    Uri.parse(confirmedLink));
                    startActivity(viewIntent);
                }

            }
        });

        PreferenceManager.setBackParent(mContext, false);

      parentshomeImg.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              PreferenceManager.setBackParent(mContext, true);
              finish();
          }
      });
        infoRoomImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppUtils.isNetworkConnected(mContext)) {

                    showRoomList();
                }
                else
                {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                }
            }
        });
      /*  if(PreferenceManager.getIsFirstTimeInPE(mContext)){
            PreferenceManager.setIsFirstTimeInPE(mContext,false);
            Intent mintent = new Intent(mContext, ParentsEveninginfoActivity.class);
            mintent.putExtra("type", 1);
            startActivity(mintent);
        }*/
        home = headermanager.getLogoButton();
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(mContext, HomeListAppCompatActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
            }
        });
        cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mListViewArrayPost.get(0).getBooking_open().equalsIgnoreCase("y")) {
                    showDialogAlertDoubeBtn((Activity) mContext, "Confirm", "Do you want to cancel this appointment?", R.drawable.questionmark_icon, R.drawable.round);
                } else {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.datexpirecontact), R.drawable.exclamationicon, R.drawable.round);

                }
            }
        });
//        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
//        mSwipeRefreshLayout.setRefreshing(false);
        recycler_view_alloted_time.setHasFixedSize(true);
        recyclerViewLayoutManager = new GridLayoutManager(mContext, 4);
        int spacing = 5; // 50px
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext, spacing);
        recycler_view_alloted_time.addItemDecoration(itemDecoration);
        recycler_view_alloted_time.setLayoutManager(recyclerViewLayoutManager);

        recycler_view_alloted_time.addOnItemTouchListener(new RecyclerItemListener(mContext, recycler_view_alloted_time,
                new RecyclerItemListener.RecyclerTouchListener() {
                    public void onClickItem(View v, int position) {
                        if (mListViewArray.get(position).getStatus().equalsIgnoreCase("1")) {
                            showDialogAlertSingleBtn((Activity) mContext, "Alert", "This slot is not available.", R.drawable.exclamationicon, R.drawable.round);

                        } else if (confirmedslotBookedByUser) {
                            showDialogAlertSingleBtn((Activity) mContext, "Alert", "Your time slot is already confirmed.", R.drawable.exclamationicon, R.drawable.round);

                        } else if (!alreadyslotBookedByUser) {
                            mListViewArrayPost = new ArrayList<CalendarEventsModel>();
                            mListViewArrayPost.add(mListViewArray.get(position));
                            if (mListViewArrayPost.get(0).getBooking_open().equalsIgnoreCase("y")) {
                                showDialogAlertDoubeBtn((Activity) mContext, "Alert", "Do you want to reserve your appointment on " + dateTextValue + "," + mListViewArrayPost.get(0).getStartTime() + " - " + mListViewArrayPost.get(0).getEndTime() + "?", R.drawable.questionmark_icon, R.drawable.round);
                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.datexpirecontact), R.drawable.exclamationicon, R.drawable.round);

                            }
                        } else {
                            if (mListViewArray.get(position).getStatus().equalsIgnoreCase("2")) {
                                showDialogAlertSingleBtn((Activity) mContext, "Alert", "This slot is reserved by you for the Parents' Evening. Click 'Cancel' option to cancel this appointment", R.drawable.exclamationicon, R.drawable.round);
                            } else {
                                showDialogAlertSingleBtn((Activity) mContext, "Alert", "Another Slot is already booked by you. If you want to take appointment on this time, please cancel earlier appointment and try", R.drawable.exclamationicon, R.drawable.round);

                            }
                        }
                    }

                    public void onLongClickItem(View v, int position) {
                    }
                }));
    }

    public void getPtaAllotedDateList() {

        try {
            alreadyslotBookedByUser = false;
            mListViewArray = new ArrayList<CalendarEventsModel>();
            final VolleyWrapper manager = new VolleyWrapper(URL_GET_PTA_TIME_SLOT_LIST);
            String[] name = new String[]{JTAG_ACCESSTOKEN, JTAG_STAFF_ID, JTAG_STUDENT_ID, JTAG_TAB_DATE};
            String[] value = new String[]{PreferenceManager.getAccessToken(mContext), mStaffid, mStudentId, selectedDate};
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
                                        JSONArray dataArray = respObject.getJSONArray(JTAG_RESPONSE_DATA_ARRAY);
                                        if (dataArray.length() > 0) {
                                            for (int i = 0; i < dataArray.length(); i++) {
                                                JSONObject dataDate = dataArray.optJSONObject(i);
                                                mListViewArray.add(getSearchValues(dataDate));
                                            }
                                            ParentsEveningTimeSlotRecyclerviewAdapter mParentsEveningTimeSlotRecyclerviewAdapter = new ParentsEveningTimeSlotRecyclerviewAdapter(mContext, mListViewArray);
                                            recycler_view_alloted_time.setAdapter(mParentsEveningTimeSlotRecyclerviewAdapter);
                                            for (int j = 0; j < mListViewArray.size(); j++) {
                                                if (mListViewArray.get(j).getStatus().equalsIgnoreCase("2")) {
                                                    mListViewArrayPost = new ArrayList<CalendarEventsModel>();
                                                    mListViewArrayPost.add(mListViewArray.get(j));
                                                    alreadyslotBookedByUser = true;
                                                    break;
                                                }
                                            }
                                            for (int j = 0; j < mListViewArray.size(); j++) {
                                                if (mListViewArray.get(j).getStatus().equalsIgnoreCase("3")) {
                                                    confirmedslotBookedByUser = true;
                                                    confirmedLink=mListViewArray.get(j).getVpml();
                                                    break;
                                                }
                                            }
                                            if (confirmedslotBookedByUser) {
                                                if(confirmedLink.equalsIgnoreCase(""))
                                                {
                                                    vpmlBtn.setVisibility(View.GONE);
                                                }
                                                else
                                                {
                                                    vpmlBtn.setVisibility(View.VISIBLE);
                                                }

                                                cancelTextView.setVisibility(View.INVISIBLE);
                                                reviewConfirmTextView.setVisibility(View.INVISIBLE);

                                            }
                                            else if (alreadyslotBookedByUser) {
                                                cancelTextView.setVisibility(View.VISIBLE);
                                                reviewConfirmTextView.setVisibility(View.VISIBLE);
                                            } else {
                                                cancelTextView.setVisibility(View.INVISIBLE);
                                                reviewConfirmTextView.setVisibility(View.INVISIBLE);

                                            }

                                        } else {
                                            //CustomStatusDialog();
                                            Toast.makeText(mContext, "No Data Available", Toast.LENGTH_SHORT).show();
                                        }
                                    } else if (statusCode.equals(STATUS_BOOKED_BY_USER)) {

                                        showDialogAlertSingleBtn((Activity) mContext, "Alert", "Slot is already booked by an another user.", R.drawable.tick, R.drawable.round);

//                                    getPtaAllotedDateList();
                                    } else {
//										CustomStatusDialog(RESPONSE_FAILURE);
                                        Toast.makeText(mContext, "No Data Available", Toast.LENGTH_SHORT).show();
                                    }
                                } else if (responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) ||
                                        responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                                        responsCode.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
                                    AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                                        @Override
                                        public void getAccessToken() {
                                        }
                                    });
                                    getPtaAllotedDateList();

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

    public void postSelectedSlot() {

        try {
            final VolleyWrapper manager = new VolleyWrapper(URL_BOOK_PTA_INSERT_TIME_SLOT);
            String[] name = new String[]{JTAG_ACCESSTOKEN, JTAG_USERS_ID, JTAG_STAFF_ID, JTAG_STUDENT_ID, JTAG_TAB_DATE, "start_time", "end_time","vpml","room"};
            String[] value = new String[]{PreferenceManager.getAccessToken(mContext), PreferenceManager.getUserId(mContext), mStaffid, mStudentId, selectedDate, mListViewArrayPost.get(0).getEventStartDate(), mListViewArrayPost.get(0).getEventEndDate(),mListViewArrayPost.get(0).getVpml(),mListViewArrayPost.get(0).getRoom()};
            System.out.println("paramenters  ::::"+mListViewArrayPost.get(0).getEventStartDate()+mListViewArrayPost.get(0).getEventEndDate()+mListViewArrayPost.get(0).getVpml());
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
                                        showDialogAlertSingleBtn((Activity) mContext, "Alert", "Reserved Only – Please review and confirm booking", R.drawable.tick, R.drawable.round);
                                        getPtaAllotedDateList();
                                    } else if (statusCode.equals(STATUS_CANCEL)) {

                                        showDialogAlertSingleBtn((Activity) mContext, "Alert", "Request cancelled successfully", R.drawable.tick, R.drawable.round);
                                        getPtaAllotedDateList();
                                    } else if (statusCode.equals(STATUS_BOOKED_BY_USER)) {

                                        showDialogAlertSingleBtn((Activity) mContext, "Alert", "Slot is already booked by an another user", R.drawable.exclamationicon, R.drawable.round);
//                                    getPtaAllotedDateList();
                                    } else if (statusCode.equals(STATUS_SLOT_NOT_FOUND)) {

                                        showDialogAlertSingleBtn((Activity) mContext, "Alert", "Timeslot not found", R.drawable.exclamationicon, R.drawable.round);
                                    }else if (statusCode.equals(STATUS_BOOKING_DATE_EXPIRED)) {

                                        showDialogAlertSingleBtn((Activity) mContext, "Alert", getString(R.string.datexpirecontact), R.drawable.exclamationicon, R.drawable.round);
                                    }else {
//										CustomStatusDialog(RESPONSE_FAILURE);
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
                                    postSelectedSlot();

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

    private CalendarEventsModel getSearchValues(JSONObject eventdate)
            throws JSONException {
        CalendarEventsModel mCalendarEventsModel = new CalendarEventsModel();
        mCalendarEventsModel.seteventdate(eventdate.optString("date"));
        mCalendarEventsModel.setEventStartDate(eventdate.optString("start_time"));
        mCalendarEventsModel.setEventEndDate(eventdate.optString("end_time"));
        mCalendarEventsModel.setStatus(eventdate.optString("status"));
        mCalendarEventsModel.setBook_end_date(eventdate.optString("book_end_date"));
        mCalendarEventsModel.setBooking_open(eventdate.optString("booking_open"));
        mCalendarEventsModel.setRoom(eventdate.optString("room"));
        mCalendarEventsModel.setVpml(eventdate.optString("vpml"));
        String dateString = mCalendarEventsModel.geteventdate();
        System.out.println("dateString|" + dateString + "|");
        if (dateString != null) {

            SimpleDateFormat format = new SimpleDateFormat(
                    "yyyy-MM-dd", Locale.ENGLISH);
            try {
                Date date = format.parse(dateString);
                Calendar cal1 = Calendar.getInstance();
                cal1.setTime(date);
                System.out.println("date:|" + cal1.get(Calendar.YEAR) + "|"
                        + "Calender:|" + cal.get(Calendar.YEAR) + "|");
                mCalendarEventsModel.setDay(cal1.get(Calendar.DAY_OF_MONTH));
                mCalendarEventsModel.setYear(cal1.get(Calendar.YEAR));
                mCalendarEventsModel.setMonth(cal1.get(Calendar.MONTH));
                mCalendarEventsModel.setMonthString(getMonth(cal1.get(Calendar.MONTH)));

            } catch (ParseException e) {
                e.getLocalizedMessage();
            }
        }
        DateFormat format1 = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        try {
            Date dateStart = format1.parse(mCalendarEventsModel.getEventStartDate());
            SimpleDateFormat format2 = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
            String startTime = format2.format(dateStart);
            mCalendarEventsModel.setStartTime(startTime);
            Date dateEndTime = format1.parse(mCalendarEventsModel.getEventEndDate());
            String endTime = format2.format(dateEndTime);
            mCalendarEventsModel.setEndTime(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return mCalendarEventsModel;
    }

    public String getMonth(int month) {
        String monthOfYear = "";
        switch (month) {
            case 0:
                monthOfYear = mContext.getResources().getString(R.string.january);
                break;
            case 1:
                monthOfYear = mContext.getResources().getString(R.string.february);
                break;
            case 2:
                monthOfYear = mContext.getResources().getString(R.string.march);
                break;
            case 3:
                monthOfYear = mContext.getResources().getString(R.string.april);
                break;
            case 4:
                monthOfYear = mContext.getResources().getString(R.string.may);
                break;
            case 5:
                monthOfYear = mContext.getResources().getString(R.string.june);
                break;
            case 6:
                monthOfYear = mContext.getResources().getString(R.string.july);
                break;
            case 7:
                monthOfYear = mContext.getResources().getString(R.string.august);
                break;
            case 8:
                monthOfYear = mContext.getResources().getString(R.string.september);
                break;
            case 9:
                monthOfYear = mContext.getResources().getString(R.string.october);
                break;
            case 10:
                monthOfYear = mContext.getResources().getString(R.string.november);
                break;
            case 11:
                monthOfYear = mContext.getResources().getString(R.string.december);
                break;
            default:
                break;
        }
        return monthOfYear;

    }

    public void showDialogAlertDoubeBtn(final Activity activity, String msgHead, String msg, int ico, int bgIcon) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
                postSelectedSlot();
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

    public void showDialogAlertSingleBtn(final Activity activity, String msgHead, String msg, int ico, int bgIcon) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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

    @Override
    public void onBackPressed() {
        PreferenceManager.setBackParent(mContext, false);
        finish();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (AppUtils.isNetworkConnected(mContext)) {
            getPtaAllotedDateList();
        } else {
            AppUtils.showDialogAlertDismissFinish((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

        }
    }
    public void showRoomList() {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_room_slot_list);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button dialogDismiss = (Button) dialog.findViewById(R.id.btn_dismiss);
        ImageView iconImageView = (ImageView) dialog.findViewById(R.id.iconImageView);
        RecyclerView socialMediaList = (RecyclerView) dialog.findViewById(R.id.recycler_view_social_media);
        //if(mSocialMediaArray.get())


        socialMediaList.addItemDecoration(new DividerItemDecoration(mContext.getResources().getDrawable(R.drawable.list_divider_teal)));

        socialMediaList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        socialMediaList.setLayoutManager(llm);

        ParentsEveningRoomListAdapter socialMediaAdapter = new ParentsEveningRoomListAdapter(mContext, mListViewArray);
        socialMediaList.setAdapter(socialMediaAdapter);
        dialogDismiss.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                dialog.dismiss();

            }

        });

        dialog.show();
    }
    public void showDialogAlertReview(final Activity activity, String msgHead, String msg, int ico, int bgIcon) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_dialogue_reviewlayout);
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
                Intent mIntent = new Intent(ParentsEveningTimeSlotActivity.this, ReviewAppointmentsRecyclerViewActivity.class);
                startActivity(mIntent);
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

}
