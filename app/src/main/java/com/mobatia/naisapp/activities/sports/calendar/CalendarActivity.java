package com.mobatia.naisapp.activities.sports.calendar;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import androidx.core.app.ActivityCompat;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.constants.IntentPassValueConstants;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.constants.ResultConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.calendar.adapter.CalendarFragmentListSportsAdapter;
import com.mobatia.naisapp.fragments.calendar.adapter.CustomSpinnerAdapter;
import com.mobatia.naisapp.fragments.calendar.model.CalendarModel;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.HeaderManager;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.volleywrappermanager.CustomDialog;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class CalendarActivity extends Activity
        implements JSONConstants,URLConstants,ResultConstants,StatusConstants,NaisClassNameConstants,IntentPassValueConstants,View.OnClickListener {
    private Context mContext;
    Bundle extras;
    String tab_type;
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back;
    ImageView home;

    private ListView mCalendarList;
    //	private ArrayList<CalendarFeedModel> mCalendarArrayList;
//	private CalendarAdapter mListAdapter;
    private String mTitle;
    int mnthId;
    private String mTabId;
    private RelativeLayout mProgressRelLayout;
    private RelativeLayout mDateRelLayout;
    private TextView mDateTxt;
    private TextView mMnthTxt;
    private TextView mYearTxt;
    //	private TextView clearData;
    private ImageView clearData;
    private ImageView infoImg;
    private Calendar mCalendar;
    private int month;
    private String mLoadUrl;
    private int mPosition;
    private String mCatName;
    private ImageView mAddAllEvents;
    private ImageView mDelAllEvents;
    private TextView monthSpinner;
    private TextView yearSpinner;
    private TextView daySpinner;
    private boolean monthSpinSelect = true;
    private boolean yearSpinSelect = true;
    private boolean daySpinSelect = true;
    private String selectedMonth;
    private int selectedMonthId;
    private String selectedYear;
    private RelativeLayout relMain;
    private RelativeLayout commonRelList;
    boolean isSelectedSpinner;
    TextView mTitleTextView;
    List<String> yearValues = new ArrayList<String>();
    ArrayList<CalendarModel> eventDateListArray = new ArrayList<CalendarModel>();
    ArrayList<CalendarModel> tempArrayList = new ArrayList<CalendarModel>();
    ArrayList<CalendarModel> eventModels;
    CalendarFragmentListSportsAdapter calendarFragmentListAdapter;
    JSONArray details;
    HashMap<String, ArrayList<CalendarModel>> hashmap =
            new HashMap<String, ArrayList<CalendarModel>>();
    ListView dayListView;
    ListView monthListView;
    ListView yearListView;
    Activity activity;
    List<String> monthValues = new ArrayList<String>();
    List<String> dayValues = new ArrayList<String>();
    int mPosMonth = 0;
    int mPosYear = 0;
    int dayPosition = -1;
    CustomSpinnerAdapter daydataAdapter;
    CustomSpinnerAdapter monthdataAdapter;
    CustomSpinnerAdapter dataAdapter;
    String selection;
    String[] selectionArgs = new String[]{Integer.toString(3)};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_calendar_sports_list);
        mContext = this;
        isSelectedSpinner = false;
//		setHasOptionsMenu(true);
//	 Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(mContext));
        initialiseUI();

//	 GetCalendarDetailsAsyncTask calListAsynTask = new GetCalendarDetailsAsyncTask(
//	 mLoadUrl, mContext, CALENDAR_DATA_DIR + mCatName, 0, 1, mTabId);
//	 calListAsynTask.execute();
        if (AppUtils.isNetworkConnected(mContext)) {
            callCalendarListAPI(URL_GET_SPORTS_CALENDAR_LIST);
        } else {
//            CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.no_internet)
//                    , getResources().getString(R.string.ok));
//            dialog.show();
            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

        }
        /*if (PreferenceManager.getIsFirstTimeInCalendar(mContext)) {
            PreferenceManager.setIsFirstTimeInCalendar(mContext, false);
            Intent mintent = new Intent(mContext, CalendarinfoActivity.class);
            mintent.putExtra("type", 1);
            mContext.startActivity(mintent);
        }*/

    }

    private void initialiseUI() {
        extras = getIntent().getExtras();
        if (extras != null) {
            tab_type=extras.getString("tab_type");
        }
        relativeHeader=(RelativeLayout)findViewById(R.id.relativeHeader);
//	 calRelMain = (RelativeLayout) mRootView.findViewById(R.id.calRelMain);
//	 calRelMain.setOnClickListener(new View.OnClickListener() {
//
//	 @Override
//	 public void onClick(View v) {
//	 // TODO Auto-generated method stub
//
//	 }
//	 });
        headermanager = new HeaderManager(CalendarActivity.this, tab_type);
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
        commonRelList = (RelativeLayout)findViewById(R.id.commonRelList);
        dayListView = (ListView)findViewById(R.id.dayListView);
        monthListView = (ListView)findViewById(R.id.monthListView);
        yearListView = (ListView)findViewById(R.id.yearListView);
       // infoImg = (ImageView) findViewById(R.id.infoImg);
        relMain = (RelativeLayout)findViewById(R.id.relMain);
        relMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mCalendarList = (ListView)findViewById(R.id.calList);
//	 mProgressRelLayout = (RelativeLayout) mRootView
//	 .findViewById(R.id.progressDialog);
     //   mTitleTextView = (TextView)findViewById(R.id.titleTextView);
        clearData = (ImageView) findViewById(R.id.clearData);
       // mTermsCalendar = (TextView) findViewById(R.id.moreImage);
      //   mTermsCalendar.setOnClickListener(this);
        clearData.setOnClickListener(this);
      //  infoImg.setOnClickListener(this);
       // mTitleTextView.setText(CALENDAR);
        mDateRelLayout = (RelativeLayout)findViewById(R.id.dateRel);
        mDateTxt = (TextView)findViewById(R.id.dateText);
        mMnthTxt = (TextView)findViewById(R.id.mnthTxt);
        mYearTxt = (TextView)findViewById(R.id.yearTxt);
        mAddAllEvents = (ImageView)findViewById(R.id.addAllBtn);
        monthSpinner = (TextView)findViewById(R.id.monthSpinner);
        yearSpinner = (TextView)findViewById(R.id.yearSpinner);
        daySpinner = (TextView)findViewById(R.id.daySpinner);
        selectedMonth = mContext.getResources().getString(R.string.month);
        selectedYear = mContext.getResources().getString(R.string.year);
//		selectedDay = mContext.getResources().getString(R.string.day);
        populateMonthSpinner();
//		monthSpinner.setSelection(0, false);
//		monthSpinner.getChildAt(0).setBackgroundResource(R.color.transparent);
//		monthSpinner.setOnItemSelectedListener(this);

        populateYearSpinner();
        populateDaySpinner();
        dayListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                daySpinSelect = true;
                commonRelList.setVisibility(View.GONE);
                dayListView.setVisibility(View.GONE);
                dayPosition = position;
                isSelectedSpinner = true;
                daySpinner.setText(dayValues.get(position).toString());
                if (!daySpinner.getText().toString().equalsIgnoreCase("DAY") && !monthSpinner.getText().toString().equalsIgnoreCase("MONTH") && !yearSpinner.getText().toString().equalsIgnoreCase("YEAR")) {
                    tempArrayList = new ArrayList<CalendarModel>();
                    for (int i = 0; i < eventDateListArray.size(); i++) {
                        if ((eventDateListArray.get(i).getDayDate().equalsIgnoreCase(daySpinner.getText().toString()) || eventDateListArray.get(i).getDayDate().equalsIgnoreCase("0" + daySpinner.getText().toString())) && eventDateListArray.get(i).getYearDate().equalsIgnoreCase(yearSpinner.getText().toString()) && eventDateListArray.get(i).getYearDate().equalsIgnoreCase(yearSpinner.getText().toString()) && eventDateListArray.get(i).getMonthDate().toLowerCase().contains(monthSpinner.getText().toString().toLowerCase())) {
                            tempArrayList.add(eventDateListArray.get(i));
                        }
                    }
                    CalendarFragmentListSportsAdapter calendarFragmentListAdapter = new CalendarFragmentListSportsAdapter(mContext, tempArrayList);
                    calendarFragmentListAdapter.notifyDataSetChanged();
                    mCalendarList.setAdapter(calendarFragmentListAdapter);
                } else if (!monthSpinner.getText().toString().equalsIgnoreCase("MONTH") && !yearSpinner.getText().toString().equalsIgnoreCase("YEAR")) {
                    tempArrayList = new ArrayList<CalendarModel>();
                    for (int i = 0; i < eventDateListArray.size(); i++) {
                        if (eventDateListArray.get(i).getMonthDate().toLowerCase().contains(monthSpinner.getText().toString().toLowerCase()) && eventDateListArray.get(i).getYearDate().equalsIgnoreCase(yearSpinner.getText().toString())) {
                            tempArrayList.add(eventDateListArray.get(i));
                        }
                    }
                    CalendarFragmentListSportsAdapter calendarFragmentListAdapter = new CalendarFragmentListSportsAdapter(mContext, tempArrayList);
                    calendarFragmentListAdapter.notifyDataSetChanged();
                    mCalendarList.setAdapter(calendarFragmentListAdapter);
                } else if (!monthSpinner.getText().toString().equalsIgnoreCase("MONTH") && !daySpinner.getText().toString().equalsIgnoreCase("DAY")) {
                    tempArrayList = new ArrayList<CalendarModel>();
                    for (int i = 0; i < eventDateListArray.size(); i++) {
                        if (eventDateListArray.get(i).getMonthDate().toLowerCase().contains(monthSpinner.getText().toString().toLowerCase()) && (eventDateListArray.get(i).getDayDate().equalsIgnoreCase(daySpinner.getText().toString()) || eventDateListArray.get(i).getDayDate().equalsIgnoreCase("0" + daySpinner.getText().toString()))) {
                            tempArrayList.add(eventDateListArray.get(i));
                        }
                    }
                    CalendarFragmentListSportsAdapter calendarFragmentListAdapter = new CalendarFragmentListSportsAdapter(mContext, tempArrayList);
                    calendarFragmentListAdapter.notifyDataSetChanged();
                    mCalendarList.setAdapter(calendarFragmentListAdapter);
                } else if (!monthSpinner.getText().toString().equalsIgnoreCase("MONTH")) {
                    tempArrayList = new ArrayList<CalendarModel>();
                    for (int i = 0; i < eventDateListArray.size(); i++) {
                        if (eventDateListArray.get(i).getMonthDate().toLowerCase().contains(monthSpinner.getText().toString().toLowerCase())) {
                            tempArrayList.add(eventDateListArray.get(i));
                        }
                    }
                    CalendarFragmentListSportsAdapter calendarFragmentListAdapter = new CalendarFragmentListSportsAdapter(mContext, tempArrayList);
                    calendarFragmentListAdapter.notifyDataSetChanged();
                    mCalendarList.setAdapter(calendarFragmentListAdapter);
                } else if (!yearSpinner.getText().toString().equalsIgnoreCase("YEAR")) {
                    tempArrayList = new ArrayList<CalendarModel>();
                    for (int i = 0; i < eventDateListArray.size(); i++) {
                        if (eventDateListArray.get(i).getYearDate().equalsIgnoreCase(yearSpinner.getText().toString())) {
                            tempArrayList.add(eventDateListArray.get(i));
                        }
                    }
                    CalendarFragmentListSportsAdapter calendarFragmentListAdapter = new CalendarFragmentListSportsAdapter(mContext, tempArrayList);
                    calendarFragmentListAdapter.notifyDataSetChanged();
                    mCalendarList.setAdapter(calendarFragmentListAdapter);
                } else if (!daySpinner.getText().toString().equalsIgnoreCase("DAY")) {
                    tempArrayList = new ArrayList<CalendarModel>();
                    for (int i = 0; i < eventDateListArray.size(); i++) {
                        if (eventDateListArray.get(i).getDayDate().equalsIgnoreCase(daySpinner.getText().toString()) || eventDateListArray.get(i).getDayDate().equalsIgnoreCase("0" + daySpinner.getText().toString())) {
                            tempArrayList.add(eventDateListArray.get(i));
                        }
                    }
                    CalendarFragmentListSportsAdapter calendarFragmentListAdapter = new CalendarFragmentListSportsAdapter(mContext, tempArrayList);
                    calendarFragmentListAdapter.notifyDataSetChanged();
                    mCalendarList.setAdapter(calendarFragmentListAdapter);
                }


            }
        });
        monthListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                monthSpinSelect = true;
                isSelectedSpinner = true;
                commonRelList.setVisibility(View.GONE);
                monthListView.setVisibility(View.GONE);
                monthListView.setVisibility(View.GONE);
                selectedMonth = monthValues.get(position).toString();
                monthSpinner.setText(monthValues.get(position).toString());
                mPosMonth = position;
//				selectedMonth = monthListView.getItemAtPosition(position).toString();
                if (selectedMonth.equalsIgnoreCase(mContext.getResources().getString(
                        R.string.jan_short))) {
                    selectedMonthId = 1;
                } else if (selectedMonth.equalsIgnoreCase(mContext.getResources()
                        .getString(R.string.feb_short))) {
                    selectedMonthId = 2;
                } else if (selectedMonth.equalsIgnoreCase(mContext.getResources()
                        .getString(R.string.mar_short))) {
                    selectedMonthId = 3;
                } else if (selectedMonth.equalsIgnoreCase(mContext.getResources()
                        .getString(R.string.apr_short))) {
                    selectedMonthId = 4;
                } else if (selectedMonth.equalsIgnoreCase(mContext.getResources()
                        .getString(R.string.may_short))) {
                    selectedMonthId = 5;
                } else if (selectedMonth.equalsIgnoreCase(mContext.getResources()
                        .getString(R.string.jun_short))) {
                    selectedMonthId = 6;
                } else if (selectedMonth.equalsIgnoreCase(mContext.getResources()
                        .getString(R.string.jul_short))) {
                    selectedMonthId = 7;
                } else if (selectedMonth.equalsIgnoreCase(mContext.getResources()
                        .getString(R.string.aug_short))) {
                    selectedMonthId = 8;
                } else if (selectedMonth.equalsIgnoreCase(mContext.getResources()
                        .getString(R.string.sep_short))) {
                    selectedMonthId = 9;
                } else if (selectedMonth.equalsIgnoreCase(mContext.getResources()
                        .getString(R.string.oct_short))) {
                    selectedMonthId = 10;
                } else if (selectedMonth.equalsIgnoreCase(mContext.getResources()
                        .getString(R.string.nov_short))) {
                    selectedMonthId = 11;
                } else if (selectedMonth.equalsIgnoreCase(mContext.getResources()
                        .getString(R.string.dec_short))) {
                    selectedMonthId = 12;
                }
                selectedYear = yearValues.get(mPosYear).toString();
                System.out.println("Selected year----" + selectedYear);

                if ((!selectedMonth.equalsIgnoreCase(mContext.getResources().getString(
                        R.string.month)) && (!selectedYear.equalsIgnoreCase(mContext
                        .getResources().getString(R.string.year))))) {
                    setSearchCalendarResult();
                    if (selectedYear.equals("YEAR")) {
                        selectedYear = yearValues.get(0).toString();
                    }
                    populateDaySpinner(selectedMonthId - 1, Integer.valueOf(selectedYear));
                    Log.e("Position1:", position + "");

                } else if ((!selectedMonth.equalsIgnoreCase(mContext.getResources().getString(
                        R.string.month)))) {
                    populateDaySpinner(selectedMonthId - 1, Calendar.YEAR - 1);
                    Log.e("Position2:", position + "");

                } else if ((!selectedYear.equalsIgnoreCase(mContext.getResources().getString(
                        R.string.year)))) {

                    populateDaySpinner(Calendar.MONTH, Integer.valueOf(selectedYear));
                    Log.e("Position3:", position + "");

                } else {
                    populateDaySpinner(Calendar.MONTH, Calendar.YEAR - 1);
                    Log.e("Position4:", position + "");

                }
            }
        });
        yearListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                yearSpinSelect = true;
                isSelectedSpinner = true;
                commonRelList.setVisibility(View.GONE);
                yearListView.setVisibility(View.GONE);
                mPosYear = position;
                yearSpinner.setText(yearValues.get(position).toString());

                selectedYear = yearValues.get(position).toString();

                if ((!selectedMonth.equalsIgnoreCase(mContext.getResources().getString(
                        R.string.month)) && (!selectedYear.equalsIgnoreCase(mContext
                        .getResources().getString(R.string.year))))) {
                    setSearchCalendarResult();
                    if (selectedYear.equals("YEAR")) {
                        selectedYear = yearValues.get(0).toString();
                    }
                    populateDaySpinner(selectedMonthId - 1, Integer.valueOf(selectedYear));
                    Log.e("Position1:", position + "");

                } else if ((!selectedMonth.equalsIgnoreCase(mContext.getResources().getString(
                        R.string.month)))) {
                    populateDaySpinner(selectedMonthId - 1, Calendar.YEAR - 1);
                    Log.e("Position2:", position + "");

                } else if ((!selectedYear.equalsIgnoreCase(mContext.getResources().getString(
                        R.string.year)))) {
                    if (selectedYear.equals("YEAR")) {
                        selectedYear = yearValues.get(0).toString();
                    }
                    populateDaySpinner(Calendar.MONTH, Integer.valueOf(selectedYear));
                    Log.e("Position3:", Integer.valueOf(selectedYear) + "::" + selectedYear);


                } else {
                    populateDaySpinner(Calendar.MONTH, Calendar.YEAR - 1);
                    Log.e("Position4:", position + "");

                }
            }
        });

        selectedYear = mContext.getResources().getString(R.string.year);
//		yearSpinner.setOnItemSelectedListener(this);
        mAddAllEvents.setOnClickListener(this);
        daySpinner.setOnClickListener(this);
        monthSpinner.setOnClickListener(this);
        yearSpinner.setOnClickListener(this);
        mDelAllEvents = (ImageView) findViewById(R.id.delAllBtn);
        mDelAllEvents.setOnClickListener(this);
        mCalendar = Calendar.getInstance();
        mDateTxt.setText(Integer.toString(mCalendar.get(Calendar.DATE)));
        month = mCalendar.get(Calendar.MONTH);
        if (month == 0) {
            mMnthTxt.setText(mContext.getResources()
                    .getString(R.string.jan_short));
        } else if (month == 1) {
            mMnthTxt.setText(mContext.getResources().getString(
                    R.string.feb_short));
        } else if (month == 2) {
            mMnthTxt.setText(mContext.getResources().getString(R.string.mar_short));
        } else if (month == 3) {
            mMnthTxt.setText(mContext.getResources().getString(R.string.apr_short));
        } else if (month == 4) {
            mMnthTxt.setText(mContext.getResources().getString(R.string.may_short));
        } else if (month == 5) {
            mMnthTxt.setText(mContext.getResources().getString(R.string.jun_short));
        } else if (month == 6) {
            mMnthTxt.setText(mContext.getResources().getString(R.string.jul_short));
        } else if (month == 7) {
            mMnthTxt.setText(mContext.getResources().getString(R.string.aug_short));
        } else if (month == 8) {
            mMnthTxt.setText(mContext.getResources().getString(
                    R.string.sep_short));
        } else if (month == 9) {
            mMnthTxt.setText(mContext.getResources()
                    .getString(R.string.oct_short));
        } else if (month == 10) {
            mMnthTxt.setText(mContext.getResources().getString(
                    R.string.nov_short));
        } else if (month == 11) {
            mMnthTxt.setText(mContext.getResources().getString(
                    R.string.dec_short));
        }
        mYearTxt.setText(Integer.toString(mCalendar.get(Calendar.YEAR)));
    }


    private void callCalendarListAPI(String URL) {
        VolleyWrapper volleyWrapper = new VolleyWrapper(URL);
        String[] name = {"access_token","users_id"};
        String[] value = {PreferenceManager.getAccessToken(mContext),PreferenceManager.getUserId(mContext)};
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
                            if (data.length() > 0) {
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject dataObject = data.getJSONObject(i);
                                    CalendarModel calendarModel = new CalendarModel();
                                    calendarModel.setDateNTime(dataObject.optString("date"));
                                    String str = dataObject.optString("date");
                                    String[] splitStr = str.trim().split("\\s+");
                                    calendarModel.setDayStringDate(splitStr[0]);
                                    calendarModel.setDayDate(splitStr[1]);
                                    calendarModel.setMonthDate(splitStr[2]);
                                    calendarModel.setYearDate(splitStr[3]);

                                    details = dataObject.getJSONArray("details");
                                    eventModels = new ArrayList<CalendarModel>();
                                    for (int j = 0; j < details.length(); j++) {
                                        JSONObject detJsonObject = details.getJSONObject(j);
                                        CalendarModel model = new CalendarModel();
                                        model.setDayStringDate(splitStr[0]);
                                        model.setDayDate(splitStr[1]);
                                        model.setMonthDate(splitStr[2]);
                                        model.setYearDate(splitStr[3]);
                                        model.setId(detJsonObject.optString("id"));
                                        model.setStatus(detJsonObject.optString("status"));

                                    /*    model.setFromTime(detJsonObject.optString("starttime"));
                                        model.setToTime(detJsonObject.optString("endtime"));*/


                                        if (!(detJsonObject.optString("starttime").equalsIgnoreCase(""))) {


                                            model.setFromTime(detJsonObject.optString("starttime"));
                                        } else {
                                            model.setFromTime("");

                                        }
                                        if (!(detJsonObject.optString("endtime").equalsIgnoreCase(""))) {

                                            model.setToTime(detJsonObject.optString("endtime"));
                                        } else {
                                            model.setToTime("");

                                        }

                                        model.setIsAllDay(detJsonObject.optString("isAllday"));
                                        DateFormat format1 = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
                                        try {
                                            SimpleDateFormat format2 = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);

                                            /*Date dateStart = format1.parse(detJsonObject.optString("starttime"));
                                            SimpleDateFormat format2 = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
                                            String startTime = format2.format(dateStart);
                                            model.setStartTime(startTime);
                                            Date dateEndTime = format1.parse(detJsonObject.optString("endtime"));
                                            String endTime = format2.format(dateEndTime);
                                            model.setEndTime(endTime);*/
                                            if (!(detJsonObject.optString("starttime").equalsIgnoreCase(""))) {

                                                Date dateStart = format1.parse(detJsonObject.optString("starttime"));
                                                String startTime = format2.format(dateStart);
                                                model.setStartTime(startTime);
                                            } else {
                                                model.setStartTime("");

                                            }
                                            if (!(detJsonObject.optString("endtime").equalsIgnoreCase(""))) {
                                                Date dateEndTime = format1.parse(detJsonObject.optString("endtime"));
                                                String endTime = format2.format(dateEndTime);
                                                model.setEndTime(endTime);
                                            } else {
                                                model.setEndTime("");

                                            }
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        model.setEvent(detJsonObject.optString("title"));
                                        if (detJsonObject.has("vpml"))
                                        {
                                            Log.e("It ", "Works vpml");
                                            model.setVpml(detJsonObject.optString("vpml"));
                                        }
                                        else
                                        {
                                            model.setVpml("");
                                        }
                                        eventModels.add(model);
                                    }
                                    calendarModel.setEventModels(eventModels);
                                    eventDateListArray.add(calendarModel);

//                                    hashmap.put(eventDateListArray.get(i).getDateNTime(), eventModels);
/*CalendarFragmentListAdapter calendarFragmentListAdapter = new CalendarFragmentListAdapter(mContext, eventDateListArray,eventModels);
mCalendarList.setAdapter(calendarFragmentListAdapter);*/
                                }
                                CalendarFragmentListSportsAdapter calendarFragmentListAdapter = new CalendarFragmentListSportsAdapter(mContext, eventDateListArray);
                                mCalendarList.setAdapter(calendarFragmentListAdapter);
//                                calendarFragmentListAdapter = new CalendarFragmentListSportsAdapter(mContext, eventDateListArray, hashmap);
//                                mCalendarList.setAdapter(calendarFragmentListAdapter);

                            } else {
                                Toast.makeText(mContext, "No data found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    } else if (response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) ||
                            response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                            response_code.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
                        AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                            @Override
                            public void getAccessToken() {

                            }
                        });
                        callCalendarListAPI(URL_GET_SPORTS_CALENDAR_LIST);

                    } else {
                        CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
                                , getResources().getString(R.string.ok));
                        dialog.show();
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

    private void loadSubItems() {
        System.out.println("called sub items---");
//creating new items in the list
        CalendarModel calendarModel = new CalendarModel();
        calendarModel.setEvent("Mindful Parenting Workshop");
        calendarModel.setFromTime("07:00pm");
        calendarModel.setToTime("09:00pm");
        eventModels.add(calendarModel);
    }


    /*******************************************************
     * Method name : populateMonthSpinner Description : get month list for month
     * spinner Parameters : nil Return type : void Date : Jan 22, 2015 Author :
     * Rijo K Jose
     *****************************************************/
    private void populateMonthSpinner() {
        int[] months = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
        monthValues = new ArrayList<String>();
//		monthValues.add(mContext.getResources().getString(R.string.month));
        for (int i = 0; i < months.length; i++) {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat month_date = new SimpleDateFormat("MMMM", Locale.ENGLISH);
            cal.set(Calendar.DATE, 5);
            cal.set(Calendar.MONTH, months[i]);
            String month_name = month_date.format(cal.getTime());
            if (month_name.equalsIgnoreCase(mContext.getResources().getString(
                    R.string.january))) {
                month_name = mContext.getResources().getString(
                        R.string.jan_short);
            } else if (month_name.equalsIgnoreCase(mContext.getResources()
                    .getString(R.string.february))) {
                month_name = mContext.getResources().getString(
                        R.string.feb_short);
            } else if (month_name.equalsIgnoreCase(mContext.getResources()
                    .getString(R.string.march))) {
                month_name = mContext.getResources().getString(
                        R.string.mar_short);
            } else if (month_name.equalsIgnoreCase(mContext.getResources()
                    .getString(R.string.april))) {
                month_name = mContext.getResources().getString(
                        R.string.apr_short);
            } else if (month_name.equalsIgnoreCase(mContext.getResources()
                    .getString(R.string.may))) {
                month_name = mContext.getResources().getString(
                        R.string.may_short);
            } else if (month_name.equalsIgnoreCase(mContext.getResources()
                    .getString(R.string.june))) {
                month_name = mContext.getResources().getString(
                        R.string.jun_short);
            } else if (month_name.equalsIgnoreCase(mContext.getResources()
                    .getString(R.string.july))) {
                month_name = mContext.getResources().getString(
                        R.string.jul_short);
            } else if (month_name.equalsIgnoreCase(mContext.getResources()
                    .getString(R.string.august))) {
                month_name = mContext.getResources().getString(
                        R.string.aug_short);
            } else if (month_name.equalsIgnoreCase(mContext.getResources()
                    .getString(R.string.september))) {
                month_name = mContext.getResources().getString(
                        R.string.sep_short);
            } else if (month_name.equalsIgnoreCase(mContext.getResources()
                    .getString(R.string.october))) {
                month_name = mContext.getResources().getString(
                        R.string.oct_short);
            } else if (month_name.equalsIgnoreCase(mContext.getResources()
                    .getString(R.string.november))) {
                month_name = mContext.getResources().getString(
                        R.string.nov_short);
            } else if (month_name.equalsIgnoreCase(mContext.getResources()
                    .getString(R.string.december))) {
                month_name = mContext.getResources().getString(
                        R.string.dec_short);
            }
            monthValues.add(month_name);
        }

        monthdataAdapter = new CustomSpinnerAdapter(mContext,
                R.layout.spinner_textview_item, monthValues, -1);

        monthListView.setAdapter(monthdataAdapter);
    }


    private void populateDaySpinner(int month, int year) {
        Calendar selctedCalender = Calendar.getInstance();
        selctedCalender.set(Calendar.MONTH, month);
        selctedCalender.set(Calendar.YEAR, year);
        Log.e("Calendar.DAY_OF_MONTH", month + year + "");
        int noOfDays = selctedCalender.getActualMaximum(Calendar.DAY_OF_MONTH);
        Log.e("noofdays", noOfDays + "");
        dayValues = new ArrayList<String>();


        switch (noOfDays) {
            case 28:
                dayValues = new ArrayList<String>(Arrays.asList(mContext.getResources().getStringArray(R.array.month28)));
                break;
            case 29:
                dayValues = new ArrayList<String>(Arrays.asList(mContext.getResources().getStringArray(R.array.month29)));

                break;
            case 30:
                dayValues = new ArrayList<String>(Arrays.asList(mContext.getResources().getStringArray(R.array.month30)));

                break;
            case 31:
                dayValues = new ArrayList<String>(Arrays.asList(mContext.getResources().getStringArray(R.array.month31)));

                break;
            default:
                break;
        }

//		int pos = 0;
//		if (dayListView.getSelectedItemPosition() != 0) {
//			pos = dayListView.getSelectedItemPosition();
//		}
        dataAdapter = new CustomSpinnerAdapter(mContext,
                R.layout.spinner_textview_item, dayValues, -1);

        dayListView.setAdapter(dataAdapter);
        if (dayPosition >= 0 && !daySpinner.getText().toString().equalsIgnoreCase("DAY")) {
            if (dayPosition < noOfDays) {
                daySpinner.setText(dayValues.get(dayPosition).toString());
            } else {

                daySpinner.setText(dayValues.get(noOfDays - 1).toString());
            }
        } else {
            daySpinner.setText("DAY");

        }
        if (!daySpinner.getText().toString().equalsIgnoreCase("DAY") && !monthSpinner.getText().toString().equalsIgnoreCase("MONTH") && !yearSpinner.getText().toString().equalsIgnoreCase("YEAR")) {
            tempArrayList = new ArrayList<CalendarModel>();

            for (int i = 0; i < eventDateListArray.size(); i++) {
                if ((eventDateListArray.get(i).getDayDate().equalsIgnoreCase(daySpinner.getText().toString()) || eventDateListArray.get(i).getDayDate().equalsIgnoreCase("0" + daySpinner.getText().toString())) && eventDateListArray.get(i).getYearDate().equalsIgnoreCase(yearSpinner.getText().toString()) && eventDateListArray.get(i).getMonthDate().toLowerCase().contains(monthSpinner.getText().toString().toLowerCase())) {
                    tempArrayList.add(eventDateListArray.get(i));
                }
            }
            CalendarFragmentListSportsAdapter calendarFragmentListAdapter = new CalendarFragmentListSportsAdapter(mContext, tempArrayList);
            calendarFragmentListAdapter.notifyDataSetChanged();
            mCalendarList.setAdapter(calendarFragmentListAdapter);
        } else if (!monthSpinner.getText().toString().equalsIgnoreCase("MONTH") && !yearSpinner.getText().toString().equalsIgnoreCase("YEAR")) {
            tempArrayList = new ArrayList<CalendarModel>();
            for (int i = 0; i < eventDateListArray.size(); i++) {
                if (eventDateListArray.get(i).getMonthDate().toLowerCase().contains(monthSpinner.getText().toString().toLowerCase()) && eventDateListArray.get(i).getYearDate().equalsIgnoreCase(yearSpinner.getText().toString())) {
                    tempArrayList.add(eventDateListArray.get(i));
                }
            }
            CalendarFragmentListSportsAdapter calendarFragmentListAdapter = new CalendarFragmentListSportsAdapter(mContext, tempArrayList);
            calendarFragmentListAdapter.notifyDataSetChanged();
            mCalendarList.setAdapter(calendarFragmentListAdapter);
        } else if (!monthSpinner.getText().toString().equalsIgnoreCase("MONTH") && !daySpinner.getText().toString().equalsIgnoreCase("DAY")) {
            tempArrayList = new ArrayList<CalendarModel>();
            for (int i = 0; i < eventDateListArray.size(); i++) {
                if (eventDateListArray.get(i).getMonthDate().toLowerCase().contains(monthSpinner.getText().toString().toLowerCase()) && (eventDateListArray.get(i).getDayDate().equalsIgnoreCase(daySpinner.getText().toString()) || eventDateListArray.get(i).getDayDate().equalsIgnoreCase("0" + daySpinner.getText().toString()))) {
                    tempArrayList.add(eventDateListArray.get(i));
                }
            }
            CalendarFragmentListSportsAdapter calendarFragmentListAdapter = new CalendarFragmentListSportsAdapter(mContext, tempArrayList);
            calendarFragmentListAdapter.notifyDataSetChanged();
            mCalendarList.setAdapter(calendarFragmentListAdapter);
        } else if (!monthSpinner.getText().toString().equalsIgnoreCase("MONTH")) {
            tempArrayList = new ArrayList<CalendarModel>();
            for (int i = 0; i < eventDateListArray.size(); i++) {

                if (eventDateListArray.get(i).getMonthDate().toLowerCase().contains(monthSpinner.getText().toString().toLowerCase())) {
                    tempArrayList.add(eventDateListArray.get(i));
                }
            }
            CalendarFragmentListSportsAdapter calendarFragmentListAdapter = new CalendarFragmentListSportsAdapter(mContext, tempArrayList);
            calendarFragmentListAdapter.notifyDataSetChanged();
            mCalendarList.setAdapter(calendarFragmentListAdapter);
        } else if (!yearSpinner.getText().toString().equalsIgnoreCase("YEAR")) {
            tempArrayList = new ArrayList<CalendarModel>();
            for (int i = 0; i < eventDateListArray.size(); i++) {
                if (eventDateListArray.get(i).getYearDate().equalsIgnoreCase(yearSpinner.getText().toString())) {
                    tempArrayList.add(eventDateListArray.get(i));
                }
            }
            CalendarFragmentListSportsAdapter calendarFragmentListAdapter = new CalendarFragmentListSportsAdapter(mContext, tempArrayList);
            calendarFragmentListAdapter.notifyDataSetChanged();
            mCalendarList.setAdapter(calendarFragmentListAdapter);
        } else if (!daySpinner.getText().toString().equalsIgnoreCase("DAY")) {
            tempArrayList = new ArrayList<CalendarModel>();
            for (int i = 0; i < eventDateListArray.size(); i++) {
                if (eventDateListArray.get(i).getDayDate().equalsIgnoreCase(daySpinner.getText().toString()) || eventDateListArray.get(i).getDayDate().equalsIgnoreCase("0" + daySpinner.getText().toString())) {
                    tempArrayList.add(eventDateListArray.get(i));
                }
            }
            CalendarFragmentListSportsAdapter calendarFragmentListAdapter = new CalendarFragmentListSportsAdapter(mContext, tempArrayList);
            calendarFragmentListAdapter.notifyDataSetChanged();
            mCalendarList.setAdapter(calendarFragmentListAdapter);
        }
    }

    private void populateDaySpinner() {
        Calendar selctedCalender = Calendar.getInstance();
        int noOfDays = selctedCalender.getActualMaximum(Calendar.DAY_OF_MONTH);
        Log.e("noofdays", noOfDays + "");
        dayValues = new ArrayList<String>();
        switch (noOfDays) {
            case 28:
                dayValues = new ArrayList<String>(Arrays.asList(mContext.getResources().getStringArray(R.array.month28)));
                break;
            case 29:
                dayValues = new ArrayList<String>(Arrays.asList(mContext.getResources().getStringArray(R.array.month29)));

                break;
            case 30:
                dayValues = new ArrayList<String>(Arrays.asList(mContext.getResources().getStringArray(R.array.month30)));

                break;
            case 31:
                dayValues = new ArrayList<String>(Arrays.asList(mContext.getResources().getStringArray(R.array.month31)));

                break;
            default:
                break;
        }

        daydataAdapter = new CustomSpinnerAdapter(mContext,
                R.layout.spinner_textview_item, dayValues, -1);
//	 dataAdapter
// .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        dayListView.setAdapter(daydataAdapter);
//		daySpinner.setSelection(0);
    }

    /*******************************************************
     * Method name : populateYearSpinner Description : get year list for year
     * spinner Parameters : nil Return type : void Date : Jan 22, 2015 Author :
     * Rijo K Jose
     *****************************************************/
    private void populateYearSpinner() {
//List<String> yearValues = new ArrayList<String>();
//		yearValues.add(mContext.getResources().getString(R.string.year));
        int yearInt = Calendar.getInstance().get(Calendar.YEAR) - 1;
        System.out.println("YEAR::" + yearInt);
        yearValues.add(yearInt + "");
        yearValues.add(yearInt + 1 + "");
        yearValues.add(yearInt + 2 + "");
        yearValues.add(yearInt + 3 + "");
        yearValues.add(yearInt + 4 + "");
        yearValues.add(yearInt + 5 + "");
//	 yearValues.add(String.valueOf((Calendar.getInstance()
//	 .get(Calendar.YEAR) - 1)));
//	 yearValues.add(String.valueOf((Calendar.getInstance()
//	 .get(Calendar.YEAR))));
//	 yearValues.add(String.valueOf((Calendar.getInstance()
//	 .get(Calendar.YEAR) + 1)));
        dataAdapter = new CustomSpinnerAdapter(mContext,
                R.layout.spinner_textview_item, yearValues, -1);
//	 dataAdapter
// .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        yearListView.setAdapter(dataAdapter);
    }


    /*******************************************************
     * Method name : setSearchCalendarResult Description : set search result of
     * calendar Parameters : nil Return type : void Date : Jan 22, 2015 Author :
     * Rijo K Jose
     *****************************************************/
    private void setSearchCalendarResult() {
//	 ArrayList<CalendarFeedModel> tempCalArrayList = new ArrayList<CalendarFeedModel>();
//	 for (int i = 0; i < mCalendarArrayList.size(); i++) {
//	 String[] dateTime = getDateAndTimeDetails(i);
//	 if (((Integer.parseInt(dateTime[1])) == selectedMonthId)
//	 && (selectedYear.equalsIgnoreCase(dateTime[2]))) {
//	 tempCalArrayList.add(mCalendarArrayList.get(i));
//	 }
//	 }
//	 if (tempCalArrayList.size() == 0) {
//	 WissUtils.showAlert((Activity) mContext, mContext.getResources()
//	 .getString(R.string.no_item), "", mContext.getResources()
//	 .getString(R.string.ok_btn), false);
//	 } else {
//	 mListAdapter = new CalendarAdapter(mContext, tempCalArrayList,
//	 mCatName);
//	 mCalendarList.setAdapter(mListAdapter);
//	 }

    }


    @Override
    public void onClick(View v) {
       /* if (v == infoImg) {
            Intent mIntent = new Intent(mContext, CalendarinfoActivity.class);

            mContext.startActivity(mIntent);
        } else*/ /*if (v == mTermsCalendar) {
            monthListView.setVisibility(View.GONE);
            yearListView.setVisibility(View.GONE);
            dayListView.setVisibility(View.GONE);
            commonRelList.setVisibility(View.GONE);
            Intent intent = new Intent(mContext, TermCalendarActivity.class);
            mContext.startActivity(intent);
        } else */if (v == clearData) {
            monthListView.setVisibility(View.GONE);
            yearListView.setVisibility(View.GONE);
            dayListView.setVisibility(View.GONE);
            commonRelList.setVisibility(View.GONE);
            daySpinner.setText("DAY");
            monthSpinner.setText("MONTH");
            yearSpinner.setText("YEAR");
            isSelectedSpinner = false;
            selectedMonth = mContext.getResources().getString(R.string.month);
            selectedYear = mContext.getResources().getString(R.string.year);
            tempArrayList.clear();
            CalendarFragmentListSportsAdapter calendarFragmentListAdapter = new CalendarFragmentListSportsAdapter(mContext, eventDateListArray);
            calendarFragmentListAdapter.notifyDataSetChanged();
            mCalendarList.setAdapter(calendarFragmentListAdapter);
        } else if (v == mAddAllEvents) {
            boolean mEventAdded = false;
            System.out.println("temparrayList size9==" + tempArrayList.size());

            if (eventDateListArray.size() > 0) {
                if (isSelectedSpinner) {
                    if (tempArrayList.size() > 0) {
                        for (int k = 0; k < tempArrayList.size(); k++) {

                            for (int position = 0; position < tempArrayList.get(k).getEventModels().size(); position++) {
                                System.out.println("temparrayList size=" + tempArrayList.get(k).getEventModels().size());

                                int year = -1;
                                int month = -1;
                                int day = -1;
                                String[] timeString;
                                int hour = -1;
                                int min = -1;
                                String[] timeString1;
                                int hour1 = -1;
                                int min1 = -1;
                                String allDay = "0";
                                year = Integer.parseInt(tempArrayList.get(k).getEventModels().get(position).getYearDate());
                                month = getMonthDetails(mContext, tempArrayList.get(k).getEventModels().get(position).getMonthDate());
                                day = Integer.parseInt(tempArrayList.get(k).getEventModels().get(position).getDayDate());


                                if (!(tempArrayList.get(k).getEventModels().get(position).getFromTime().equalsIgnoreCase(""))) {
                                    timeString = tempArrayList.get(k).getEventModels().get(position).getFromTime().split(":");
                                    hour = Integer.parseInt(timeString[0]);
                                    min = Integer.parseInt(timeString[1]);
                                } else {
                                    hour = -1;
                                    min = -1;
                                }

                                allDay = tempArrayList.get(k).getEventModels().get(position).getIsAllDay();
                                if (!(tempArrayList.get(k).getEventModels().get(position).getToTime().equalsIgnoreCase(""))) {
                                    timeString1 = tempArrayList.get(k).getEventModels().get(position).getToTime().split(":");
                                    hour1 = Integer.parseInt(timeString1[0]);
                                    min1 = Integer.parseInt(timeString1[1]);
                                } else {
                                    hour1 = -1;
                                    min1 = -1;
                                }

                                if (hour1 == -1 && min1 == -1) {
                                    if (tempArrayList.get(k).getEventModels().get(position).isEventAddToCalendar()) {
                                        addReminder(year, month, day, hour, min, year, month,
                                                (day), hour, min,
                                                tempArrayList.get(k).getEventModels().get(position).getEvent(),
                                                tempArrayList.get(k).getEventModels().get(position).getEvent(), 0, position, allDay, k);
                                        mEventAdded = true;
                                        tempArrayList.get(k).getEventModels().get(position).setEventAddToCalendar(true);
                                    }
                                } else {
                                    if (tempArrayList.get(k).getEventModels().get(position).isEventAddToCalendar()) {

                                        addReminder(year, month, day, hour, min, year, month,
                                                (day), hour1, min1,
                                                tempArrayList.get(k).getEventModels().get(position).getEvent(),
                                                tempArrayList.get(k).getEventModels().get(position).getEvent(), 0, position, allDay, k);
                                        mEventAdded = true;
                                        tempArrayList.get(k).getEventModels().get(position).setEventAddToCalendar(true);
                                    }
                                }
                      /*          timeString1 = tempArrayList.get(k).getEventModels().get(position).getToTime().split(":");
                                hour1 = Integer.parseInt(timeString1[0]);
                                min1 = Integer.parseInt(timeString1[1]);*/
//                                boolean addToCalendar = true;
//                                String[] prefData = PreferenceManager
//                                        .getCalendarEventNames(mContext).split(",");
//                                for (int i = 0; i < prefData.length; i++) {
//                                    if (prefData[i].equalsIgnoreCase(tempArrayList.get(k).getEventModels().get(position).getEvent() + tempArrayList.get(k).getEventModels().get(position).getEvent())) {
//                                        addToCalendar = false;
//                                        break;
//                                    }
//                                }
//                                System.out.println("addToCalendar---" + addToCalendar);
//                                if (addToCalendar) {
//                                    if (year != -1 && month != -1 && day != -1 && hour != -1
//                                            && min != -1) {
//                                        if (hour1 == -1 && min1 == -1) {
//                                            addReminder(year, month, day, hour, min, year, month,
//                                                    (day), hour, min,
//                                                    tempArrayList.get(k).getEventModels().get(position).getEvent(),
//                                                    tempArrayList.get(k).getEventModels().get(position).getEvent(), 0, position, allDay, k);
//
//                                        } else {
//                                            addReminder(year, month, day, hour, min, year, month,
//                                                    (day), hour1, min1,
//                                                    tempArrayList.get(k).getEventModels().get(position).getEvent(),
//                                                    tempArrayList.get(k).getEventModels().get(position).getEvent(), 0, position, allDay, k);
//
//                                        }
//                                        mEventAdded = true;
//
//                                    } else {
//                                        mEventAdded = false;
//
////                                        Toast.makeText(mContext,
////                                                mContext.getResources().getString(
////                                                        R.string.no_evnt_details), Toast.LENGTH_SHORT).show();
//                                    }
//                                } else {
//                                    mEventAdded = true;
//
////                                    Toast.makeText(mContext,
////                                            mContext.getResources().getString(
////                                                    R.string.add_cal_success), Toast.LENGTH_SHORT).show();
//                                }
                            }
                        }
                    } else if (eventDateListArray.size() > 0) {
                        for (int k = 0; k < eventDateListArray.size(); k++) {

                            for (int position = 0; position < eventDateListArray.get(k).getEventModels().size(); position++) {
                                System.out.println("temparrayList size=" + eventDateListArray.get(k).getEventModels().size());

                                int year = -1;
                                int month = -1;
                                int day = -1;
                                String[] timeString;
                                int hour = -1;
                                int min = -1;
                                String[] timeString1;
                                int hour1 = -1;
                                int min1 = -1;
                                String allDay = "0";
                                year = Integer.parseInt(eventDateListArray.get(k).getEventModels().get(position).getYearDate());
                                month = getMonthDetails(mContext, eventDateListArray.get(k).getEventModels().get(position).getMonthDate());
                                day = Integer.parseInt(eventDateListArray.get(k).getEventModels().get(position).getDayDate());


                                if ((eventDateListArray.get(k).getEventModels().get(position).getFromTime().equalsIgnoreCase(""))) {
                                    timeString = eventDateListArray.get(k).getEventModels().get(position).getFromTime().split(":");
                                    hour = Integer.parseInt(timeString[0]);
                                    min = Integer.parseInt(timeString[1]);
                                } else {
                                    hour = -1;
                                    min = -1;
                                }

                                allDay = eventDateListArray.get(k).getEventModels().get(position).getIsAllDay();
                                if ((eventDateListArray.get(k).getEventModels().get(position).getToTime().equalsIgnoreCase(""))) {
                                    timeString1 = eventDateListArray.get(k).getEventModels().get(position).getToTime().split(":");
                                    hour1 = Integer.parseInt(timeString1[0]);
                                    min1 = Integer.parseInt(timeString1[1]);
                                } else {
                                    hour1 = -1;
                                    min1 = -1;
                                }

                                if (hour1 == -1 && min1 == -1) {
                                    if (eventDateListArray.get(k).getEventModels().get(position).isEventAddToCalendar()) {
                                        addReminder(year, month, day, hour, min, year, month,
                                                (day), hour, min,
                                                eventDateListArray.get(k).getEventModels().get(position).getEvent(),
                                                eventDateListArray.get(k).getEventModels().get(position).getEvent(), 0, position, allDay, k);
                                        mEventAdded = true;
                                        eventDateListArray.get(k).getEventModels().get(position).setEventAddToCalendar(true);
                                    }
                                } else {
                                    if (eventDateListArray.get(k).getEventModels().get(position).isEventAddToCalendar()) {

                                        addReminder(year, month, day, hour, min, year, month,
                                                (day), hour1, min1,
                                                eventDateListArray.get(k).getEventModels().get(position).getEvent(),
                                                eventDateListArray.get(k).getEventModels().get(position).getEvent(), 0, position, allDay, k);
                                        mEventAdded = true;
                                        eventDateListArray.get(k).getEventModels().get(position).setEventAddToCalendar(true);
                                    }
                                }
                      /*          timeString1 = tempArrayList.get(k).getEventModels().get(position).getToTime().split(":");
                                hour1 = Integer.parseInt(timeString1[0]);
                                min1 = Integer.parseInt(timeString1[1]);*/
//                                boolean addToCalendar = true;
//                                String[] prefData = PreferenceManager
//                                        .getCalendarEventNames(mContext).split(",");
//                                for (int i = 0; i < prefData.length; i++) {
//                                    System.out.println("dataCal11:"+prefData[i]);
//                                    System.out.println("dataCal22:"+eventDateListArray.get(k).getEventModels().get(position).getEvent());
//
//                                    if (prefData[i].equalsIgnoreCase(eventDateListArray.get(k).getEventModels().get(position).getEvent() + eventDateListArray.get(k).getEventModels().get(position).getEvent())) {
//                                        addToCalendar = false;
//                                        break;
//                                    }
//
//                                }
//                                if (addToCalendar) {
//                                    if (year != -1 && month != -1 && day != -1 && hour != -1
//                                            && min != -1) {
//                                        if (hour1 == -1 && min1 == -1) {
//                                            addReminder(year, month, day, hour, min, year, month,
//                                                    (day), hour, min,
//                                                    eventDateListArray.get(k).getEventModels().get(position).getEvent(),
//                                                    eventDateListArray.get(k).getEventModels().get(position).getEvent(), 0, position, allDay, k);
//                                            System.out.println("addToCalendar--eventDateListArray1-" + addToCalendar);
//
//                                        } else {
//                                            addReminder(year, month, day, hour, min, year, month,
//                                                    (day), hour1, min1,
//                                                    eventDateListArray.get(k).getEventModels().get(position).getEvent(),
//                                                    eventDateListArray.get(k).getEventModels().get(position).getEvent(), 0, position, allDay, k);
//
//                                        }
//                                        mEventAdded = true;
//
//                                    } else {
//                                        mEventAdded = false;
//
////                                        Toast.makeText(mContext,
////                                                mContext.getResources().getString(
////                                                        R.string.no_evnt_details), Toast.LENGTH_SHORT).show();
//                                    }
//                                } else {
//                                    mEventAdded = true;

//                                    Toast.makeText(mContext,
//                                            mContext.getResources().getString(
//                                                    R.string.add_cal_success), Toast.LENGTH_SHORT).show();
//                                }
                            }
                        }
                    } else {
                        mEventAdded = false;
//                        Toast.makeText(mContext,
//                                mContext.getResources().getString(
//                                        R.string.no_evnt_details), Toast.LENGTH_SHORT).show();
                    }
                    if (mEventAdded) {
                        Toast.makeText(mContext,
                                mContext.getResources().getString(
                                        R.string.add_cal_success), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext,
                                mContext.getResources().getString(
                                        R.string.no_evnt_details), Toast.LENGTH_SHORT).show();
                    }
//
                } else {
                    for (int k = 0; k < eventDateListArray.size(); k++) {
                        for (int position = 0; position < eventDateListArray.get(k).getEventModels().size(); position++) {
                            System.out.println("temparrayList size9k==" + k + " " + eventDateListArray.size());

                            int year = -1;
                            int month = -1;
                            int day = -1;
                            String[] timeString;
                            int hour = -1;
                            int min = -1;
                            String[] timeString1;
                            int hour1 = -1;
                            int min1 = -1;
                            String allDay = "0";
                            year = Integer.parseInt(eventDateListArray.get(k).getEventModels().get(position).getYearDate());
                            month = getMonthDetails(mContext, eventDateListArray.get(k).getEventModels().get(position).getMonthDate());
                            day = Integer.parseInt(eventDateListArray.get(k).getEventModels().get(position).getDayDate());
                            if (!(eventDateListArray.get(k).getEventModels().get(position).getFromTime().equalsIgnoreCase(""))) {
                                timeString = eventDateListArray.get(k).getEventModels().get(position).getFromTime().split(":");
                                hour = Integer.parseInt(timeString[0]);
                                min = Integer.parseInt(timeString[1]);
                            } else {
                                hour = -1;
                                min = -1;
                            }


                            allDay = eventDateListArray.get(k).getEventModels().get(position).getIsAllDay();

                            if (!(eventDateListArray.get(k).getEventModels().get(position).getToTime().equalsIgnoreCase(""))) {
                                timeString1 = eventDateListArray.get(k).getEventModels().get(position).getToTime().split(":");
                                hour1 = Integer.parseInt(timeString1[0]);
                                min1 = Integer.parseInt(timeString1[1]);
                            } else {
                                hour1 = -1;
                                min1 = -1;
                            }


                            if (hour1 == -1 && min1 == -1) {
                                if (!eventDateListArray.get(k).getEventModels().get(position).isEventAddToCalendar()) {
                                    addReminder(year, month, day, hour, min, year, month,
                                            (day), hour, min,
                                            eventDateListArray.get(k).getEventModels().get(position).getEvent(),
                                            eventDateListArray.get(k).getEventModels().get(position).getEvent(), 0, position, allDay, k);
                                    mEventAdded = true;
                                    eventDateListArray.get(k).getEventModels().get(position).setEventAddToCalendar(true);
                                }
                            } else {
                                if (!eventDateListArray.get(k).getEventModels().get(position).isEventAddToCalendar()) {

                                    addReminder(year, month, day, hour, min, year, month,
                                            (day), hour1, min1,
                                            eventDateListArray.get(k).getEventModels().get(position).getEvent(),
                                            eventDateListArray.get(k).getEventModels().get(position).getEvent(), 0, position, allDay, k);
                                    mEventAdded = true;
                                    eventDateListArray.get(k).getEventModels().get(position).setEventAddToCalendar(true);
                                }
                            }
//                            System.out.println("addToCalendar---" + addToCalendar);
//                            if (addToCalendar) {
//                                if (year != -1 && month != -1 && day != -1 && hour != -1
//                                        && min != -1) {
//                                    if (hour1 == -1 && min1 == -1) {
//                                        addReminder(year, month, day, hour, min, year, month,
//                                                (day), hour, min,
//                                                eventDateListArray.get(k).getEventModels().get(position).getEvent(),
//                                                eventDateListArray.get(k).getEventModels().get(position).getEvent(), 0, position, allDay, k);
//
//                                    } else {
//                                        addReminder(year, month, day, hour, min, year, month,
//                                                (day), hour1, min1,
//                                                eventDateListArray.get(k).getEventModels().get(position).getEvent(),
//                                                eventDateListArray.get(k).getEventModels().get(position).getEvent(), 0, position, allDay, k);
//
//                                    }
//                                    mEventAdded = true;
//
//                                } else {
//
//                                    mEventAdded = false;
////                                    Toast.makeText(mContext,
////                                            mContext.getResources().getString(
////                                                    R.string.no_evnt_details), Toast.LENGTH_SHORT).show();
//                                }
//                            } else {
//                                mEventAdded = true;
////                                Toast.makeText(mContext,
////                                        mContext.getResources().getString(
////                                                R.string.add_cal_success), Toast.LENGTH_SHORT).show();
//
//                            }
                        }
                    }
                }
                if (mEventAdded) {
                    Toast.makeText(mContext,
                            mContext.getResources().getString(
                                    R.string.add_cal_success), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext,
                            mContext.getResources().getString(
                                    R.string.no_evnt_details), Toast.LENGTH_SHORT).show();
                }
//
            } else {
                Toast.makeText(mContext,
                        mContext.getResources().getString(
                                R.string.no_evnt_details), Toast.LENGTH_SHORT).show();
            }
        } else if (v == mDelAllEvents) {
            if (eventDateListArray.size() > 0) {

             /*   String selection = "(" + CalendarContract.Events.CALENDAR_ID + " = ?)";
                String[] selectionArgs = new String[]{Integer.toString(1)};
                ContentResolver cr = mContext.getContentResolver();
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                int rows = cr.delete(CalendarContract.Events.CONTENT_URI, selection, selectionArgs);
                Log.d("deletion ", rows + " events deleted");*/

                String selection = "(" + CalendarContract.Events.CALENDAR_ID + " = ?)";
                if (Build.VERSION.SDK_INT >  Build.VERSION_CODES.M) {
                    // Marshmallow+
                    selectionArgs = new String[]{Integer.toString(3)};
                }else if (Build.VERSION.SDK_INT ==  Build.VERSION_CODES.LOLLIPOP || Build.VERSION.SDK_INT ==  Build.VERSION_CODES.LOLLIPOP_MR1) {
                    // lollipop
                    selectionArgs = new String[]{Integer.toString(3)};
                }else{
                    //below Marshmallow
                    selectionArgs = new String[]{Integer.toString(1)};
                }


                ContentResolver cr = mContext.getContentResolver();
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                int rows = cr.delete(CalendarContract.Events.CONTENT_URI, selection, selectionArgs);
                Log.d("deletion ", rows + " events deleted");

                PreferenceManager.setSportsCalendarEventNames(mContext, "");
                Toast.makeText(mContext,
                        mContext.getResources().getString(
                                R.string.del_cal_success), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext,
                        mContext.getResources().getString(
                                R.string.no_evnt_details), Toast.LENGTH_SHORT).show();
            }

        } else if (v == daySpinner) {
            if (daySpinSelect) {
                commonRelList.setVisibility(View.VISIBLE);
                dayListView.setVisibility(View.VISIBLE);
                yearListView.setVisibility(View.GONE);
                monthListView.setVisibility(View.GONE);

                daySpinSelect = false;
            } else {
                daySpinSelect = true;

                commonRelList.setVisibility(View.GONE);
                dayListView.setVisibility(View.GONE);
            }
        } else if (v == monthSpinner) {
            if (monthSpinSelect) {
                monthSpinSelect = false;
                dayListView.setVisibility(View.GONE);
                yearListView.setVisibility(View.GONE);

                commonRelList.setVisibility(View.VISIBLE);
                monthListView.setVisibility(View.VISIBLE);

            } else {
                monthSpinSelect = true;

                commonRelList.setVisibility(View.GONE);
                monthListView.setVisibility(View.GONE);
            }
        } else if (v == yearSpinner) {
            if (yearSpinSelect) {
                yearSpinSelect = false;
                monthListView.setVisibility(View.GONE);
                dayListView.setVisibility(View.GONE);

                commonRelList.setVisibility(View.VISIBLE);
                yearListView.setVisibility(View.VISIBLE);
            } else {
                yearSpinSelect = true;
                commonRelList.setVisibility(View.GONE);
                yearListView.setVisibility(View.GONE);
            }
        }
    }


    public void addReminder(int startYear, int startMonth, int startDay,
                            int startHour, int startMinute, int endYear, int endMonth,
                            int endDay, int endHour, int endMinutes, String name,
                            String description, int count, int position, String isAllday, int k) {
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(startYear, startMonth, startDay, startHour, startMinute);
        long startMillis = beginTime.getTimeInMillis();

        Calendar endTime = Calendar.getInstance();
        endTime.set(endYear, endMonth, endDay, endHour, endMinutes);
        long endMillis = endTime.getTimeInMillis();

        String eventUriString = "content://com.android.calendar/events";
        ContentValues eventValues = new ContentValues();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            // Marshmallow+
            eventValues.put(CalendarContract.Events.CALENDAR_ID, 3);//1

        }else if (Build.VERSION.SDK_INT ==  Build.VERSION_CODES.LOLLIPOP || Build.VERSION.SDK_INT ==  Build.VERSION_CODES.LOLLIPOP_MR1) {
            // lollipop
            eventValues.put(CalendarContract.Events.CALENDAR_ID, 3);//1

        }else{
            //below Marshmallow
            eventValues.put(CalendarContract.Events.CALENDAR_ID, 1);//1

        }

        eventValues.put(CalendarContract.Events.TITLE, name);
        eventValues.put(CalendarContract.Events.DESCRIPTION, description);
        eventValues.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.SHORT);
        eventValues.put(CalendarContract.Events.DTSTART, startMillis);
        eventValues.put(CalendarContract.Events.DTEND, endMillis);
        if (isAllday.equals("1")) {
            eventValues.put(CalendarContract.Events.ALL_DAY, true);
        } else {
            eventValues.put(CalendarContract.Events.ALL_DAY, false);
        }
        eventValues.put("eventStatus", 1);
        eventValues.put(CalendarContract.Events.HAS_ALARM, 1);
        Uri eventUri = mContext.getContentResolver().insert(
                Uri.parse(eventUriString), eventValues);
        long eventID = Long.parseLong(eventUri.getLastPathSegment());
        Log.d("TAG", "1----" + eventID);
//        eventModels.get(position).setId(String.valueOf(eventID));
        eventDateListArray.get(k).getEventModels().get(position).setId(String.valueOf(eventID));
        Log.d("TAG", "2----");
        System.out.println("ResponseSize3---");

        PreferenceManager.setSportsCalendarEventNames(mContext,
                PreferenceManager.getSportsCalendarEventNames(mContext) + name
                        + description + ",");
//        if (count == 0) {
//            Toast.makeText(mContext, mContext.getResources()
//                    .getString(R.string.add_cal_success), Toast.LENGTH_SHORT).show();
//        }
//        /***************** Event: Reminder(with alert) Adding reminder to event *******************/
//        String reminderUriString = "content://com.android.calendar/reminders";
//        ContentValues reminderValues = new ContentValues();
//        reminderValues.put(EVENT_ID, eventID);
//        reminderValues.put(MINUTES, 1440);
//        reminderValues.put(METHOD, 1);
//        Uri reminderUri = mContext.getContentResolver().insert(
//                Uri.parse(reminderUriString), reminderValues);
//        long eventIDlong = Long.parseLong(reminderUri.getLastPathSegment());
    }

    public int getMonthDetails(Context mContext, String descStringTime) {
        // january
        if (descStringTime.equalsIgnoreCase(mContext.getResources().getString(
                R.string.january))) {
//            if (type == 0) {
//                mnthTxt.setText(mContext.getResources().getString(
//                        R.string.jan_short));
//            }
            mnthId = 0;
        } // february
        if (descStringTime.equalsIgnoreCase(mContext.getResources().getString(
                R.string.february))) {
//            if (type == 0) {
//                mnthTxt.setText(mContext.getResources().getString(
//                        R.string.feb_short));
//            }
            mnthId = 1;
        } // march
        if (descStringTime.equalsIgnoreCase(mContext.getResources().getString(
                R.string.march))) {
//            if (type == 0) {
//                mnthTxt.setText(mContext.getResources().getString(
//                        R.string.mar_short));
//            }
            mnthId = 2;
        } // april
        if (descStringTime.equalsIgnoreCase(mContext.getResources().getString(
                R.string.april))) {
//            if (type == 0) {
//                mnthTxt.setText(mContext.getResources().getString(
//                        R.string.apr_short));
//            }
            mnthId = 3;
        }// may
        if (descStringTime.equalsIgnoreCase(mContext.getResources().getString(
                R.string.may))) {
//            if (type == 0) {
//                mnthTxt.setText(mContext.getResources().getString(
//                        R.string.may_short));
//            }
            mnthId = 4;
        } // june
        if (descStringTime.equalsIgnoreCase(mContext.getResources().getString(
                R.string.june))) {
//            if (type == 0) {
//                mnthTxt.setText(mContext.getResources().getString(
//                        R.string.jun_short));
//            }
            mnthId = 5;
        } // july
        if (descStringTime.equalsIgnoreCase(mContext.getResources().getString(
                R.string.july))) {
//            if (type == 0) {
//                mnthTxt.setText(mContext.getResources().getString(
//                        R.string.jul_short));
//            }
            mnthId = 6;
        } // august
        if (descStringTime.equalsIgnoreCase(mContext.getResources().getString(
                R.string.august))) {
//            if (type == 0) {
//                mnthTxt.setText(mContext.getResources().getString(
//                        R.string.aug_short));
//            }
            mnthId = 7;
        } // september
        if (descStringTime.equalsIgnoreCase(mContext.getResources().getString(
                R.string.september))) {
//            if (type == 0) {
//                mnthTxt.setText(mContext.getResources().getString(
//                        R.string.sep_short));
//            }
            mnthId = 8;
        } // october
        if (descStringTime.equalsIgnoreCase(mContext.getResources().getString(
                R.string.october))) {
//            if (type == 0) {
//                mnthTxt.setText(mContext.getResources().getString(
//                        R.string.oct_short));
//            }
            mnthId = 9;
        } // november
        if (descStringTime.equalsIgnoreCase(mContext.getResources().getString(
                R.string.november))) {
//            if (type == 0) {
//                mnthTxt.setText(mContext.getResources().getString(
//                        R.string.nov_short));
//            }
            mnthId = 10;
        } // december
        if (descStringTime.equalsIgnoreCase(mContext.getResources().getString(
                R.string.december))) {
//            if (type == 0) {
//                mnthTxt.setText(mContext.getResources().getString(
//                        R.string.dec_short));
//            }
            mnthId = 11;
        }
        return mnthId;
    }

}