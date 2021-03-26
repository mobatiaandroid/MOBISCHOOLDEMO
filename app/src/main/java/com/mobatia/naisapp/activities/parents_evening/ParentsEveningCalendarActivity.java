package com.mobatia.naisapp.activities.parents_evening;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.home.HomeListActivity;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.calender.Day;
import com.mobatia.naisapp.calender.ExtendedCalendarView;
import com.mobatia.naisapp.calender.model.CalendarEventsModel;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.constants.ResultConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.HeaderManager;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by gayatri on 23/3/17.
 */
public class ParentsEveningCalendarActivity extends Activity implements JSONConstants, URLConstants, ResultConstants, StatusConstants, NaisClassNameConstants {
    ExtendedCalendarView calendarView;
    Context mContext = this;
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back,continueImageView,home;
    ArrayList<CalendarEventsModel> mListViewArray;
    ArrayList<CalendarEventsModel> calendarEventsModelFilteredList;
    Bundle extras;
    Calendar cal;
    TextView appointmentDateTV;
    private String mStaffId="";
    private String selectedDate="";
    private String mStudentId="";
    private String mStudentName="";
    private String mStaffName="";
    private String mClass="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parents_evening_calendar);
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
            mStaffId = extras.getString(JTAG_STAFF_ID);
            mStudentId = extras.getString(JTAG_STUDENT_ID);
            mStudentName = extras.getString("studentName");
            mStaffName = extras.getString("staffName");
            mClass = extras.getString("studentClass");
        }
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        appointmentDateTV = (TextView) findViewById(R.id.appointmentDateTV);
        continueImageView = (ImageView) findViewById(R.id.continueImageView);
        calendarView = (ExtendedCalendarView) findViewById(R.id.calendarView);
        headermanager = new HeaderManager(ParentsEveningCalendarActivity.this, PARENT_EVENING);
        headermanager.getHeader(relativeHeader, 0);
        back = headermanager.getLeftButton();
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
        continueImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent =new Intent(mContext, ParentsEveningTimeSlotActivity.class);
                mIntent.putExtra(JTAG_STAFF_ID,mStaffId);
                mIntent.putExtra(JTAG_STUDENT_ID,mStudentId);
                mIntent.putExtra("studentName",mStudentName);
                mIntent.putExtra("staffName",mStaffName);
                mIntent.putExtra("studentClass", mClass);
                mIntent.putExtra("selectedDate", selectedDate);
                startActivity(mIntent);
                if(!PreferenceManager.getIBackParent(mContext)){
                    finish();
                }
            }
        });
        calendarView.setOnDayClickListener(new ExtendedCalendarView.OnDayClickListener() {

            @Override
            public void onDayClicked(AdapterView<?> adapter, View view,
                                     int position, long id, Day day) {
                // TODO Auto-generated method stub
                if (mListViewArray.size() > 0) {
                    calendarEventsModelFilteredList = new ArrayList<CalendarEventsModel>();
                    for (int i = 0; i < mListViewArray.size(); i++) {
                        if (day.getYear() == mListViewArray
                                .get(i).getYear()
                                && day.getMonth() == mListViewArray
                                .get(i).getMonth()
                                && day.getDay() == mListViewArray
                                .get(i).getDay()) {

                            calendarEventsModelFilteredList
                                    .add(mListViewArray.get(i));
                            break;

                        }
                    }
                    if (calendarEventsModelFilteredList.size() > 0) {
                        continueImageView.setVisibility(View.INVISIBLE);
                        selectedDate=calendarEventsModelFilteredList.get(0).geteventdate();
//                        appointmentDateTV.setText("You've selected appointment date\n" + calendarEventsModelFilteredList.get(0).getDay() + " " + calendarEventsModelFilteredList.get(0).getMonthString() + " " + calendarEventsModelFilteredList.get(0).getYear());
                        Intent mIntent =new Intent(mContext, ParentsEveningTimeSlotActivity.class);
                        mIntent.putExtra(JTAG_STAFF_ID,mStaffId);
                        mIntent.putExtra(JTAG_STUDENT_ID,mStudentId);
                        mIntent.putExtra("studentName",mStudentName);
                        mIntent.putExtra("staffName",mStaffName);
                        mIntent.putExtra("studentClass", mClass);
                        mIntent.putExtra("selectedDate", selectedDate);
                        PreferenceManager.setBackParent(mContext, true);

                        startActivity(mIntent);
                      /*  if(PreferenceManager.getIBackParent(mContext)){
                           finish();
                        }*/
                    } else {
                        appointmentDateTV.setText("");
                        continueImageView.setVisibility(View.INVISIBLE);

                    }

                }
            /*	mMonthDay.setText(getMonth(day.getMonth()) + " "
						+ String.valueOf(day.getDay()));
				mYear.setText(String.valueOf(day.getYear()));*/
            }
        });

    }

    public void getPtaAllotedDateList() {

        try {
            mListViewArray = new ArrayList<CalendarEventsModel>();
            final VolleyWrapper manager = new VolleyWrapper(URL_GET_PTA_ALLOTTED_LIST);
            String[] name = new String[]{JTAG_ACCESSTOKEN, JTAG_STAFF_ID,JTAG_STUDENT_ID};
            String[] value = new String[]{PreferenceManager.getAccessToken(mContext), mStaffId,mStudentId};
            System.out.println("Staff ID"+mStaffId+ "student Id "+mStudentId);
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
                                            calendarView.setMonthPrevNextVisibility(2);

                                            for (int i = 0; i < dataArray.length(); i++) {
                                                String dataDate = dataArray.optString(i);
                                                mListViewArray.add(getSearchValues(dataDate));
                                            }
                                            calendarView.datesForDays(mListViewArray);

                                        } else {
                                            //CustomStatusDialog();
                                            calendarView.setMonthPrevNextVisibility(1);
                                            Toast.makeText(mContext, "No Dates Available", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
//										CustomStatusDialog(RESPONSE_FAILURE);
                                        Toast.makeText(mContext, "No Data Found", Toast.LENGTH_SHORT).show();
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
                                else if (responsCode.equals(RESPONSE_ERROR)) {
                                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                                }
                            } else {
//								CustomStatusDialog(RESPONSE_FAILURE);
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

    private CalendarEventsModel getSearchValues(String eventdate)
            throws JSONException {
        CalendarEventsModel mCalendarEventsModel = new CalendarEventsModel();
        mCalendarEventsModel.seteventdate(eventdate);
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

            } catch (java.text.ParseException e) {
                e.getLocalizedMessage();
            }
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

    @Override
    protected void onRestart() {
        super.onRestart();
        if(PreferenceManager.getIBackParent(mContext)){
            finish();
        }
    }
}
