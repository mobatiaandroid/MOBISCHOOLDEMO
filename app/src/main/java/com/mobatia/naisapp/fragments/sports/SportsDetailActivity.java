package com.mobatia.naisapp.fragments.sports;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.bus_routes.BusRoutesRecyclerViewActivity;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.activities.participants.ParticipantRecyclerViewActivity;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.home.adapter.SurveyPagerAdapter;
import com.mobatia.naisapp.fragments.home.module.AnswerSubmitModel;
import com.mobatia.naisapp.fragments.home.module.SurveyAnswersModel;
import com.mobatia.naisapp.fragments.home.module.SurveyModel;
import com.mobatia.naisapp.fragments.home.module.SurveyQuestionsModel;
import com.mobatia.naisapp.fragments.parents_evening.model.StudentModel;
import com.mobatia.naisapp.fragments.sports.adapter.StrudentSpinnerAdapter;
import com.mobatia.naisapp.fragments.sports.model.BusModel;
import com.mobatia.naisapp.fragments.sports.model.SportsModel;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.HeaderManager;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.recyclerviewmanager.DividerItemDecoration;
import com.mobatia.naisapp.recyclerviewmanager.RecyclerItemListener;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by gayatri on 28/3/17.
 */
public class SportsDetailActivity extends Activity implements URLConstants, JSONConstants, StatusConstants {
    Bundle extras;
    private Context mContext = this;
    RelativeLayout relativeHeader;
    RelativeLayout relativePickupdelivery;
    RelativeLayout relativeParticipants;
    HeaderManager headermanager;
    ImageView back;
    ImageView home;
    ImageView studImg;
    String stud_class = "";
    String stud_name = "";
    String stud_img = "";
    String section = "";
    static String event_id;
    ArrayList<StudentModel> studentsModelArrayList = new ArrayList<>();
    ArrayList<SportsModel> sportsModelArrayList = new ArrayList<>();
    ArrayList<String> studentList = new ArrayList<>();
    LinearLayout mStudentSpinner, statusButtonslayout;
    String mDate = null;
    String mTime = null;
    Button mGoingButton;
    Button mNotGoingButton;
    Button mNotSurebutton;
    static String stud_id = "";
    ImageView addToCalendar, sendMessage, callButton, locateMe;
    TextView start_date, end_date, description, venueTextViewValue, studentName, start_time, end_time, eventTitle;
    static String status;
    ScrollView mScrollView;
    EditText text_dialog, text_content;
    boolean attending = false, notAttending = false;
    /************* Created by Aparna**********/
    TextView startDate;
    TextView venueTxt;
    TextView meetPointTxt;
    TextView meetTimeTxt;
    TextView fixtureStartTxt;
    TextView fixtureEndTxt;
    TextView schoolArrivalTxt;
    Button pickUpAtVenue;
    Button pickUpSchool;
    LinearLayout meetPointLinear;
    LinearLayout meetTimeLinear;
    LinearLayout fixtureStartLinear;
    LinearLayout fixtureEndLinear;
    LinearLayout schoolArrivalLinear;
    LinearLayout pickUpLinear;
    LinearLayout notesLinear;
    static String pickUpStatus = "0";
    static Boolean isClick = false;
    String stude_id="";

    int currentPage = 0;
    int currentPageSurvey = 0;
    private int surveySize=0;
    int pos=-1;
    ArrayList<SurveyModel> surveyArrayList;
    ArrayList<SurveyQuestionsModel> surveyQuestionArrayList;
    ArrayList<SurveyAnswersModel> surveyAnswersArrayList;
    ArrayList<AnswerSubmitModel>mAnswerList;
    String surveyEmail = "";
    int survey_satisfation_status=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports_event_detail_new);
        initUI();
        if (PreferenceManager.getUserId(mContext).equals("")) {
            if (AppUtils.isNetworkConnected(mContext)) {
                callEventDetailsApi(URL_GET_SPORTS_EVENTS_LISTDETAILS, "");
            } else {
                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

            }
        } else {
            if (AppUtils.isNetworkConnected(mContext)) {
                getStudentsListAPI(URL_GET_STUDENT_LIST);
            } else {
                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

            }
        }
    }

    private void initUI() {
        extras = getIntent().getExtras();
        if (extras != null) {
            event_id = extras.getString("event_id");
            stude_id = extras.getString("stude_id");
        }
        studImg = findViewById(R.id.imagicon);

        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        mScrollView = (ScrollView) findViewById(R.id.mScrollView);
        relativeParticipants = (RelativeLayout) findViewById(R.id.relativeParticipants);
        relativePickupdelivery = (RelativeLayout) findViewById(R.id.relativePickupdelivery);
        mStudentSpinner = (LinearLayout) findViewById(R.id.studentSpinner);
        mGoingButton = (Button) findViewById(R.id.goingBtn);
        mNotGoingButton = (Button) findViewById(R.id.notGoingBtn);
        mNotSurebutton = (Button) findViewById(R.id.notSureBtn);
        pickUpAtVenue = (Button) findViewById(R.id.pickUpAtVenue);
        pickUpSchool = (Button) findViewById(R.id.pickUpSchool);
        start_date = (TextView) findViewById(R.id.start_date);
        end_date = (TextView) findViewById(R.id.end_date);
        start_time = (TextView) findViewById(R.id.start_time);
        end_time = (TextView) findViewById(R.id.end_time);
        description = (TextView) findViewById(R.id.description);
        venueTextViewValue = (TextView) findViewById(R.id.venueTextViewValue);
        studentName = (TextView) findViewById(R.id.studentName);
        eventTitle = (TextView) findViewById(R.id.eventTitle);
        addToCalendar = (ImageView) findViewById(R.id.addTocalendar);
        sendMessage = (ImageView) findViewById(R.id.sendMessage);
        callButton = (ImageView) findViewById(R.id.callBtn);
        locateMe = (ImageView) findViewById(R.id.goToLocation);
        statusButtonslayout = (LinearLayout) findViewById(R.id.statusButtonslayout);
        /************ created by aparna*********/
        startDate = (TextView) findViewById(R.id.startDate);
        venueTxt = (TextView) findViewById(R.id.venueTxt);
        meetPointTxt = (TextView) findViewById(R.id.meetPointTxt);
        meetTimeTxt = (TextView) findViewById(R.id.meetTimeTxt);
        fixtureStartTxt = (TextView) findViewById(R.id.fixtureStartTxt);
        fixtureEndTxt = (TextView) findViewById(R.id.fixtureEndTxt);
        schoolArrivalTxt = (TextView) findViewById(R.id.schoolArrivalTxt);
        meetPointLinear = (LinearLayout) findViewById(R.id.meetPointLinear);
        meetTimeLinear = (LinearLayout) findViewById(R.id.meetTimeLinear);
        fixtureStartLinear = (LinearLayout) findViewById(R.id.fixtureStartLinear);
        fixtureEndLinear = (LinearLayout) findViewById(R.id.fixtureEndLinear);
        schoolArrivalLinear = (LinearLayout) findViewById(R.id.schoolArrivalLinear);
        notesLinear = (LinearLayout) findViewById(R.id.notesLinear);
        pickUpLinear = (LinearLayout) findViewById(R.id.pickUpLinear);
        pickUpAtVenue = (Button) findViewById(R.id.pickUpAtVenue);
        pickUpSchool = (Button) findViewById(R.id.pickUpSchool);
        description = (TextView) findViewById(R.id.description);

        headermanager = new HeaderManager(SportsDetailActivity.this, "Sports Fixture");

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
        mStudentSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSocialmediaList(studentsModelArrayList);
            }
        });
        locateMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sportsModelArrayList.get(0).getSports_lat().equals("") ||
                        sportsModelArrayList.get(0).getSports_lng().equals("")) {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.no_details_available), R.drawable.exclamationicon, R.drawable.round);

                } else {
                    Intent intent = new Intent(SportsDetailActivity.this, LocateMe.class);
                    intent.putExtra("latitude", sportsModelArrayList.get(0).getSports_lat());
                    intent.putExtra("longitude", sportsModelArrayList.get(0).getSports_lng());
                    startActivity(intent);
                }
            }
        });
        relativePickupdelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "Work In Progress", R.drawable.exclamationicon, R.drawable.round);

                Intent mIntent=new Intent(mContext,BusRoutesRecyclerViewActivity.class);
                mIntent.putExtra("tab_type","Pick Up And Drop Off");
                mIntent.putExtra("bus_route_array",sportsModelArrayList.get(0).getSportsModelBusRoutesArrayList());
                startActivity(mIntent);
            }
        });
        pickUpAtVenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (attending) {
                    pickUpStatus = "1";
                    status = "1";
                    System.out.println("pick up status attending " + pickUpStatus);

                    pickUpAtVenue.setBackgroundResource(R.drawable.curve_filled_split_bg_sports);
                    pickUpSchool.setBackgroundResource(R.drawable.curve_filled_light_grey_sports);
                    if (AppUtils.isNetworkConnected(mContext)) {
                        UpdateStudentEventGoingStatus(URL_UPDATE_EVENTGOINGSTATUS, stud_id, pickUpStatus);

                    } else {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                    }
                } else if (notAttending) {
                    pickUpStatus = "0";
                    status = "0";

                    pickUpAtVenue.setBackgroundResource(R.drawable.curve_filled_light_grey_sports);
                    pickUpSchool.setBackgroundResource(R.drawable.curve_filled_light_grey_sports);

                } else if (pickUpStatus.equalsIgnoreCase("2")) {
                    showDialogAlertPickUp((Activity) mContext, "Confirm your Pick Up Point");
                } else {
                    pickUpStatus = "1";
                    pickUpAtVenue.setBackgroundResource(R.drawable.curve_filled_split_bg_sports);
                    pickUpSchool.setBackgroundResource(R.drawable.curve_filled_light_grey_sports);
                }


            }
        });
        pickUpSchool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("pick up status School" + pickUpStatus);
                if (notAttending) {
                    pickUpStatus = "0";
                    status = "0";
                    pickUpAtVenue.setBackgroundResource(R.drawable.curve_filled_light_grey_sports);
                    pickUpSchool.setBackgroundResource(R.drawable.curve_filled_light_grey_sports);


                } else if (attending) {
                    pickUpStatus = "2";
                    status = "1";
                    System.out.println("pick up status notattending " + pickUpStatus);

                    pickUpSchool.setBackgroundResource(R.drawable.curve_filled_split_bg_sports);
                    pickUpAtVenue.setBackgroundResource(R.drawable.curve_filled_light_grey_sports);
                    if (AppUtils.isNetworkConnected(mContext)) {
                        UpdateStudentEventGoingStatus(URL_UPDATE_EVENTGOINGSTATUS, stud_id, pickUpStatus);


                    } else {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                    }
                } else if (pickUpStatus.equalsIgnoreCase("1")) {
                    showDialogAlertPickUp((Activity) mContext, "Confirm your Pick Up Point");

                } else {
                    pickUpStatus = "2";
                    pickUpSchool.setBackgroundResource(R.drawable.curve_filled_split_bg_sports);
                    pickUpAtVenue.setBackgroundResource(R.drawable.curve_filled_light_grey_sports);
                }


            }
        });


        mGoingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!attending) {

//                    decisionText.setText("ATTENDING");
                    if (pickUpStatus.equalsIgnoreCase("0")) {
                        if (sportsModelArrayList.get(0).getIsAway().equalsIgnoreCase("1")) {
                            showDialogAlertPickUp((Activity) mContext, "Choose a Pick Up Point");

                        } else {
                            final int sdk = android.os.Build.VERSION.SDK_INT;
                            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                mGoingButton.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.edittext_bg));
                                mNotGoingButton.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.event_status_selectbg));
                                mNotSurebutton.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.event_status_selectbg));
                            } else {
                                mGoingButton.setBackground(mContext.getResources().getDrawable(R.drawable.edittext_bg));
                                mNotGoingButton.setBackground(mContext.getResources().getDrawable(R.drawable.event_status_selectbg));
                                mNotSurebutton.setBackground(mContext.getResources().getDrawable(R.drawable.event_status_selectbg));
                            }
                            status = "1";
                            if (AppUtils.isNetworkConnected(mContext)) {
                                UpdateStudentEventGoingStatus(URL_UPDATE_EVENTGOINGSTATUS, stud_id, pickUpStatus);
                                attending = true;
                                notAttending = false;

                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                            }

                        }
                    }
                    /*if (AppUtils.isNetworkConnected(mContext)) {
                        UpdateStudentEventGoingStatus(URL_UPDATE_EVENTGOINGSTATUS, stud_id,pickUpStatus);
                        attending = true;
                        notAttending = false;

                    } else {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                    }*/
                } else {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "You have already submitted your participation status.", R.drawable.exclamationicon, R.drawable.round);

                }

            }
        });
//        if (pickUpStatus.equalsIgnoreCase("0"))
//        {
//            pickUpAtVenue.setBackgroundColor(mContext.getResources().getColor(R.color.light_grey));
//            pickUpSchool.setBackgroundColor(mContext.getResources().getColor(R.color.light_grey));
//
//        }
//        else if (pickUpStatus.equalsIgnoreCase("1"))
//        {
//            pickUpAtVenue.setBackgroundColor(mContext.getResources().getColor(R.color.split_bg));
//            pickUpSchool.setBackgroundColor(mContext.getResources().getColor(R.color.light_grey));
//        }
//        else if (pickUpStatus.equalsIgnoreCase("2"))
//        {
//            pickUpAtVenue.setBackgroundColor(mContext.getResources().getColor(R.color.light_grey));
//            pickUpSchool.setBackgroundColor(mContext.getResources().getColor(R.color.split_bg));
//        }

        addToCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long startTime = 0, endTime = 0;

                try {
                    Date dateStart = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(sportsModelArrayList.get(0).getSports_start_date());
                    startTime = dateStart.getTime();
                    Date dateEnd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(sportsModelArrayList.get(0).getSports_end_date());
                    endTime = dateEnd.getTime();
                } catch (Exception e) {
                }

                Calendar cal = Calendar.getInstance();
                Intent intent = new Intent(Intent.ACTION_EDIT);
                intent.setType("vnd.android.cursor.item/event");
                intent.putExtra("beginTime", startTime);
                intent.putExtra("allDay", true);
                intent.putExtra("rrule", "FREQ=YEARLY");
                intent.putExtra("endTime", endTime);
                intent.putExtra("title", sportsModelArrayList.get(0).getSports_name());
                startActivity(intent);
            }
        });
        relativeParticipants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "Work In Progress", R.drawable.exclamationicon, R.drawable.round);

                Intent mIntent = new Intent(mContext, ParticipantRecyclerViewActivity.class);
                mIntent.putExtra("tab_type", "Participants");
                mIntent.putExtra("participant_sports_array", sportsModelArrayList.get(0).getmSportsModelHouseArrayList());
                startActivity(mIntent);
            }
        });

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sportsModelArrayList.get(0).getSports_email().equals("")) {
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

                            dialog.dismiss();

                        }

                    });

                    submitButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sendEmailToStaff(URL_SEND_EMAIL_TO_STAFF);
                        }
                    });


                    dialog.show();

                } else {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.no_contact_email), R.drawable.exclamationicon, R.drawable.round);
                }

            }
        });

        mNotGoingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!notAttending) {
                    final int sdk = android.os.Build.VERSION.SDK_INT;
                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {

                        mGoingButton.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.event_status_selectbg));
                        mNotGoingButton.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.edittext_bg));
                        mNotSurebutton.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.event_status_selectbg));
                        pickUpSchool.setBackgroundResource(R.drawable.curve_filled_light_grey_sports);
                        pickUpAtVenue.setBackgroundResource(R.drawable.curve_filled_light_grey_sports);

                    } else {
                        mGoingButton.setBackground(mContext.getResources().getDrawable(R.drawable.event_status_selectbg));
                        mNotGoingButton.setBackground(mContext.getResources().getDrawable(R.drawable.edittext_bg));
                        mNotSurebutton.setBackground(mContext.getResources().getDrawable(R.drawable.event_status_selectbg));
                        pickUpSchool.setBackgroundResource(R.drawable.curve_filled_light_grey_sports);
                        pickUpAtVenue.setBackgroundResource(R.drawable.curve_filled_light_grey_sports);

                    }
                    status = "0";

//                    decisionText.setText("NOT ATTENDING");
                    pickUpStatus = "0";

                    if (AppUtils.isNetworkConnected(mContext)) {
                        UpdateStudentEventGoingStatus(URL_UPDATE_EVENTGOINGSTATUS, stud_id, pickUpStatus);

                        attending = false;
                        notAttending = true;
                    } else {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                    }
                } else {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "You have already submitted your participation status.", R.drawable.exclamationicon, R.drawable.round);

                }
            }
        });

        mNotSurebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final int sdk = android.os.Build.VERSION.SDK_INT;
                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    mGoingButton.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.event_status_selectbg));
                    mNotGoingButton.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.event_status_selectbg));
                    mNotSurebutton.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.edittext_bg));
                } else {
                    mGoingButton.setBackground(mContext.getResources().getDrawable(R.drawable.event_status_selectbg));
                    mNotGoingButton.setBackground(mContext.getResources().getDrawable(R.drawable.event_status_selectbg));
                    mNotSurebutton.setBackground(mContext.getResources().getDrawable(R.drawable.edittext_bg));
                }
                status = "2";
                if (AppUtils.isNetworkConnected(mContext)) {
                    UpdateStudentEventGoingStatus(URL_UPDATE_EVENTGOINGSTATUS, stud_id, pickUpStatus);
                } else {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                }
            }
        });
    }

    private void callEventDetailsApi(String URL, String student_id) {
        VolleyWrapper volleyWrapper = new VolleyWrapper(URL);
        String[] name = {"access_token", "event_id", "users_id", "student_id"};
        String[] value = {PreferenceManager.getAccessToken(mContext), event_id, PreferenceManager.getUserId(mContext), student_id};
        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("The response is1" + successResponse);
                try {
                    JSONObject obj = new JSONObject(successResponse);
                    String response_code = obj.getString(JTAG_RESPONSECODE);
                    if (response_code.equalsIgnoreCase("200")) {
                        JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                        String status_code = secobj.getString(JTAG_STATUSCODE);
                        sportsModelArrayList.clear();
                        if (status_code.equalsIgnoreCase("303")) {
                            JSONObject dataObj = secobj.getJSONObject("data");

                            sportsModelArrayList.add(addSportsEventDetails(dataObj));
                            startDate.setText(separateStartDate(sportsModelArrayList.get(0).getSports_start_date()));

                            venueTxt.setText(sportsModelArrayList.get(0).getSports_venue());
                            if (sportsModelArrayList.get(0).getSports_description().equalsIgnoreCase("")) {
                                notesLinear.setVisibility(View.GONE);
                            } else {
                                notesLinear.setVisibility(View.VISIBLE);
                                description.setText(sportsModelArrayList.get(0).getSports_description());

                            }
                            if (sportsModelArrayList.get(0).getSports_name().equalsIgnoreCase("")) {
                                eventTitle.setVisibility(View.GONE);
                            } else {
                                eventTitle.setVisibility(View.VISIBLE);
                                eventTitle.setText(sportsModelArrayList.get(0).getSports_name());

                            }
                            if (sportsModelArrayList.get(0).getMeeting_point().equalsIgnoreCase("")) {
                                meetPointLinear.setVisibility(View.GONE);

                            } else {
                                meetPointLinear.setVisibility(View.VISIBLE);

                                meetPointTxt.setText(sportsModelArrayList.get(0).getMeeting_point());

                            }
                            if (sportsModelArrayList.get(0).getMeeting_time().equalsIgnoreCase("")) {
                                meetTimeLinear.setVisibility(View.GONE);

                            } else {
                                meetTimeLinear.setVisibility(View.VISIBLE);

                                meetTimeTxt.setText(AppUtils.convertTimeToAMPM(sportsModelArrayList.get(0).getMeeting_time()) + " (Approx)");

                            }
                            if (sportsModelArrayList.get(0).getFixture_start().equalsIgnoreCase("")) {
                                fixtureStartLinear.setVisibility(View.GONE);

                            } else {
                                fixtureStartLinear.setVisibility(View.VISIBLE);

                                fixtureStartTxt.setText(AppUtils.convertTimeToAMPM(sportsModelArrayList.get(0).getFixture_start()) + " (Approx)");

                            }
                            if (sportsModelArrayList.get(0).getFixture_end().equalsIgnoreCase("")) {
                                fixtureEndLinear.setVisibility(View.GONE);

                            } else {
                                fixtureEndLinear.setVisibility(View.VISIBLE);

                                fixtureEndTxt.setText(AppUtils.convertTimeToAMPM(sportsModelArrayList.get(0).getFixture_end()) + " (Approx)");

                            }

                            if (!(sportsModelArrayList.get(0).getIsAway().equals("0")) && (sportsModelArrayList.get(0).getPartipipate_status().equalsIgnoreCase("1"))) {
                                schoolArrivalLinear.setVisibility(View.VISIBLE);
                                pickUpLinear.setVisibility(View.VISIBLE);
                                schoolArrivalTxt.setText(AppUtils.convertTimeToAMPM(sportsModelArrayList.get(0).getSchool_arrival()) + " (Approx)");

                            }
                            else  if (!(sportsModelArrayList.get(0).getIsAway().equals("0")) && (sportsModelArrayList.get(0).getPartipipate_status().equalsIgnoreCase("0"))) {
                                schoolArrivalLinear.setVisibility(View.VISIBLE);
                                schoolArrivalTxt.setText(AppUtils.convertTimeToAMPM(sportsModelArrayList.get(0).getSchool_arrival()) + " (Approx)");
                                pickUpLinear.setVisibility(View.GONE);
                            }
                            else {

                                schoolArrivalLinear.setVisibility(View.GONE);
                                pickUpLinear.setVisibility(View.GONE);

                            }

                            mScrollView.setVisibility(View.VISIBLE);
                            System.out.println("Going status---" + sportsModelArrayList.get(0).getGoing_status());
                            if (sportsModelArrayList.get(0).getPartipipate_status().equals("1")) {
                                statusButtonslayout.setVisibility(View.VISIBLE);
                                relativeParticipants.setVisibility(View.VISIBLE);
                            } else {
                                statusButtonslayout.setVisibility(View.GONE);
                                relativeParticipants.setVisibility(View.INVISIBLE);
                            }
                            if (dataObj.optString(JTAG_EVENT_GOINGSTATUS).equals("0")) {

                                final int sdk = android.os.Build.VERSION.SDK_INT;
                                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                    mGoingButton.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.event_status_selectbg));
                                    mNotGoingButton.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.edittext_bg));
                                    mNotSurebutton.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.event_status_selectbg));

                                } else {
                                    mGoingButton.setBackground(mContext.getResources().getDrawable(R.drawable.event_status_selectbg));
                                    mNotGoingButton.setBackground(mContext.getResources().getDrawable(R.drawable.edittext_bg));
                                    mNotSurebutton.setBackground(mContext.getResources().getDrawable(R.drawable.event_status_selectbg));
                                }
                                attending = false;
                                notAttending = true;
                            } else if (dataObj.optString(JTAG_EVENT_GOINGSTATUS).equals("1")) {

                                final int sdk = android.os.Build.VERSION.SDK_INT;
                                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                    mGoingButton.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.edittext_bg));
                                    mNotGoingButton.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.event_status_selectbg));
                                    mNotSurebutton.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.event_status_selectbg));
                                } else {
                                    mGoingButton.setBackground(mContext.getResources().getDrawable(R.drawable.edittext_bg));
                                    mNotGoingButton.setBackground(mContext.getResources().getDrawable(R.drawable.event_status_selectbg));
                                    mNotSurebutton.setBackground(mContext.getResources().getDrawable(R.drawable.event_status_selectbg));
                                }
                                attending = true;
                                notAttending = false;
                            } else if (dataObj.optString(JTAG_EVENT_GOINGSTATUS).equals("2")) {

                                final int sdk = android.os.Build.VERSION.SDK_INT;
                                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                    mGoingButton.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.event_status_selectbg));
                                    mNotGoingButton.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.event_status_selectbg));
                                    mNotSurebutton.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.edittext_bg));
                                } else {
                                    mGoingButton.setBackground(mContext.getResources().getDrawable(R.drawable.event_status_selectbg));
                                    mNotGoingButton.setBackground(mContext.getResources().getDrawable(R.drawable.event_status_selectbg));
                                    mNotSurebutton.setBackground(mContext.getResources().getDrawable(R.drawable.edittext_bg));
                                }
                                attending = false;
                                notAttending = true;
                            } else {
                                final int sdk = android.os.Build.VERSION.SDK_INT;
                                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                    mGoingButton.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.event_status_selectbg));
                                    mNotGoingButton.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.event_status_selectbg));
                                    mNotSurebutton.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.event_status_selectbg));
                                } else {
                                    mGoingButton.setBackground(mContext.getResources().getDrawable(R.drawable.event_status_selectbg));
                                    mNotGoingButton.setBackground(mContext.getResources().getDrawable(R.drawable.event_status_selectbg));
                                    mNotSurebutton.setBackground(mContext.getResources().getDrawable(R.drawable.event_status_selectbg));
                                }
                                attending = false;
                                notAttending = false;
//                                decisionText.setText("");
                            }
                            System.out.println("pickup status" + dataObj.optString("pick_up_status"));
                            pickUpStatus = dataObj.optString("pick_up_status");
                            if (pickUpStatus.equalsIgnoreCase("1")) {
                                pickUpAtVenue.setBackgroundResource(R.drawable.curve_filled_split_bg_sports);
                                pickUpSchool.setBackgroundResource(R.drawable.curve_filled_light_grey_sports);


                            } else if (pickUpStatus.equalsIgnoreCase("2"))
                            {
                                pickUpAtVenue.setBackgroundResource(R.drawable.curve_filled_light_grey_sports);
                                pickUpSchool.setBackgroundResource(R.drawable.curve_filled_split_bg_sports);
                            } else {
                                pickUpAtVenue.setBackgroundResource(R.drawable.curve_filled_light_grey_sports);
                                pickUpSchool.setBackgroundResource(R.drawable.curve_filled_light_grey_sports);


                            }

                        }

                    } else if (response_code.equalsIgnoreCase("500")) {
                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        callEventDetailsApi(URL_GET_SPORTS_EVENTS_LISTDETAILS, stud_id);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        callEventDetailsApi(URL_GET_SPORTS_EVENTS_LISTDETAILS, stud_id);

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        callEventDetailsApi(URL_GET_SPORTS_EVENTS_LISTDETAILS, stud_id);

                    } else {

                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    }
                } catch (Exception ex) {
                    System.out.println("The Exception in edit profile is" + ex.toString());
                }

            }

            @Override
            public void responseFailure(String failureResponse) {
				/*CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
						, getResources().getString(R.string.ok));
				dialog.show();*/
                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

            }
        });


    }

    private SportsModel addSportsEventDetails(JSONObject dataObj) {
        SportsModel sportsModel = new SportsModel();
        sportsModel.setSports_name(dataObj.optString(JTAG_TAB_NAME));
        sportsModel.setSports_start_date(dataObj.optString(JTAG_EVENT_STARTDATE));
        sportsModel.setSports_end_date(dataObj.optString(JTAG_EVENT_ENDDATE));
        sportsModel.setSports_description(dataObj.optString(JTAG_DESCRIPTION));
        sportsModel.setSports_lat(dataObj.optString(JTAG_EVENT_LATITUDE));
        sportsModel.setSports_lng(dataObj.optString(JTAG_EVENT_LONGITUDE));
        sportsModel.setSports_venue(dataObj.optString(JTAG_EVENT_VENUE));
        sportsModel.setMeeting_point(dataObj.optString("meeting_point"));
        sportsModel.setMeeting_time(dataObj.optString("meeting_time"));
        sportsModel.setFixture_start(dataObj.optString("fixture_start"));
        sportsModel.setFixture_end(dataObj.optString("fixture_end"));
        sportsModel.setSchool_arrival(dataObj.optString("school_arrival"));
        sportsModel.setIsAway(dataObj.optString("isAway"));
        sportsModel.setPartipipate_status(dataObj.optString(JTAG_ISPARTICIPANT));
        sportsModel.setSports_email(dataObj.optString(JTAG_EMAIL));
        sportsModel.setGoing_status(dataObj.optString(JTAG_EVENT_GOINGSTATUS));
        System.out.println("Going status---1--" + dataObj.optString(JTAG_EVENT_GOINGSTATUS));

        try {
            JSONArray participantDetailsArray = dataObj.getJSONArray(JTAG_EVENT_PARTICIPANTS_DETAILS);
            if (participantDetailsArray.length() > 0) {
                relativeParticipants.setVisibility(View.VISIBLE);
            } else {
                relativeParticipants.setVisibility(View.GONE);

            }
            ArrayList<SportsModel> mSportsModelHouseArrayList = new ArrayList<>();
            for (int m = 0; m < participantDetailsArray.length(); m++) {
                JSONObject participantDetailsObj = participantDetailsArray.getJSONObject(m);
                SportsModel sportsModels = new SportsModel();
                sportsModels.setSports_name(participantDetailsObj.optString("house_name"));
                JSONArray participantsArray = participantDetailsObj.getJSONArray(JTAG_EVENT_PARTICIPANTS);
                ArrayList<StudentModel> participantArray = new ArrayList<>();
                for (int j = 0; j < participantsArray.length(); j++) {
                    StudentModel sModel = new StudentModel();
                    JSONObject pObj = participantsArray.getJSONObject(j);
                    sModel.setmId(pObj.optString(JTAG_ID));
                    sModel.setmName(pObj.optString(JTAG_TAB_NAME));
                    sModel.setmClass(pObj.optString(JTAG_TAB_CLASS));
                    sModel.setmSection(pObj.optString(JTAG_TAB_SECTION));
                    sModel.setmHouse(pObj.optString("house"));
                    sModel.setmPhoto(pObj.optString("photo"));
                    sModel.setGoingStatus(pObj.optString(JTAG_EVENT_GOINGSTATUS));
                    participantArray.add(sModel);
                    //sModel.se
                }
                sportsModels.setSportsModelParticipantsArrayList(participantArray);
                mSportsModelHouseArrayList.add(sportsModels);
            }
            JSONArray busArray = dataObj.getJSONArray(JTAG_EVENT_BUSROUTES);
            if (busArray.length()>0)
            {
                relativePickupdelivery.setVisibility(View.GONE);
            }
            else
            {
                relativePickupdelivery.setVisibility(View.GONE);

            }
            ArrayList<BusModel> busMiodelArray = new ArrayList<>();

            for (int k = 0; k < busArray.length(); k++) {
                JSONObject bObj = busArray.getJSONObject(k);
                BusModel busModel = new BusModel();
                busModel.setBus_name(bObj.optString(JTAG_EVENT_BUSNAME));
                busModel.setBus_no(bObj.optString(JTAG_EVENT_BUS_NO));
                busModel.setContact_no(bObj.optString(JTAG_EVENT_BUSCONTACT_NO));
                busModel.setContact_person(bObj.optString(JTAG_EVENT_BUSCONTACT_PESRON));
                ArrayList<BusModel> mBusRouteArrayList = new ArrayList<>();
                JSONArray mBusRouteArray = bObj.optJSONArray("bus_routes");
                for (int m = 0; m < mBusRouteArray.length(); m++) {
                    JSONObject bObjs = mBusRouteArray.getJSONObject(m);
                    BusModel busModels = new BusModel();
                    busModels.setRoute_name(bObjs.optString("route_name"));
                    busModels.setTime(bObjs.optString("time"));
                    busModels.setLatitude(bObjs.optString("latitude"));
                    busModels.setLongitude(bObjs.optString("longitude"));
                    mBusRouteArrayList.add(busModels);
                }
                busModel.setmBusRoute(mBusRouteArrayList);
                busMiodelArray.add(busModel);
            }
            sportsModel.setSportsModelBusRoutesArrayList(busMiodelArray);
            sportsModel.setmSportsModelHouseArrayList(mSportsModelHouseArrayList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sportsModel;
    }

    private String separateDate(String inputDate) {
        try {
            Log.d("Time here ", "InputDate--" + inputDate);
            Date date;
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            date = formatter.parse(inputDate);
            //Subtracting 6 hours from selected time
            long time = date.getTime();

            SimpleDateFormat formatterFullDate = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
            mDate = formatterFullDate.format(time);
            Log.e("Date ", mDate);


        } catch (Exception e) {
            Log.d("Exception", "" + e);
        }
        return mDate;
    }

    private String separateStartDate(String inputDate) {
        try {
            Log.d("Time here ", "InputDate--" + inputDate);
            Date date;
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            date = formatter.parse(inputDate);
            //Subtracting 6 hours from selected time
            long time = date.getTime();

            SimpleDateFormat formatterFullDate = new SimpleDateFormat("EEEE dd MMMM", Locale.ENGLISH);
            mDate = formatterFullDate.format(time);
            Log.e("Date ", mDate);


        } catch (Exception e) {
            Log.d("Exception", "" + e);
        }
        return mDate;
    }

    private String separateTime(String inputDate) {
        try {
            Log.d("Time here ", "InputDate--" + inputDate);
            Date date;
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
            date = formatter.parse(inputDate);
            //Subtracting 6 hours from selected time
            long time = date.getTime();

            SimpleDateFormat formatterTime = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
            mTime = formatterTime.format(time);
            Log.e("Time ", mTime);

        } catch (Exception e) {
            Log.d("Exception", "" + e);
        }
        return mTime;
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
                                for (int k=0;k<studentsModelArrayList.size();k++)
                                {
                                    if (studentsModelArrayList.get(k).getmId().equalsIgnoreCase(stude_id))
                                    {
                                        System.out.println("studid"+studentsModelArrayList.get(k).getmId());
                                        studentName.setText(studentsModelArrayList.get(k).getmName());
                                        stud_id = studentsModelArrayList.get(k).getmId();
                                        stud_img = studentsModelArrayList.get(k).getmPhoto();
                                        break;
                                    }
                                }
                               if (stude_id.equalsIgnoreCase(""))
                               {
                                   studentName.setText(studentsModelArrayList.get(0).getmName());
                                   stud_id = studentsModelArrayList.get(0).getmId();
                                   stud_img = studentsModelArrayList.get(0).getmPhoto();
                               }
                                if (!(stud_img.equals(""))) {

                                    Picasso.with(mContext).load(AppUtils.replace(stud_img)).placeholder(R.drawable.boy).fit().into(studImg);
                                } else

                                {

                                    studImg.setImageResource(R.drawable.boy);
                                }

                                System.out.println("size--" + studentList.size());
                                if (AppUtils.isNetworkConnected(mContext)) {
                                    callEventDetailsApi(URL_GET_SPORTS_EVENTS_LISTDETAILS, stud_id);
                                } else {
                                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                                }


                            } else {
                                Toast.makeText(SportsDetailActivity.this, "No data found", Toast.LENGTH_SHORT).show();
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
                        stud_img=mStudentArray.get(position).getmPhoto();
                        System.out.println("Selected student id---" + stud_id);
                        System.out.println("Selected student name--" + studentName.getText().toString());
                        if (!(stud_img.equals(""))) {

                            Picasso.with(mContext).load(AppUtils.replace(stud_img)).placeholder(R.drawable.boy).fit().into(studImg);
                        }
                        else

                        {

                            studImg.setImageResource(R.drawable.boy);
                        }
                        if (AppUtils.isNetworkConnected(mContext)) {
                            callEventDetailsApi(URL_GET_SPORTS_EVENTS_LISTDETAILS, mStudentArray.get(position).getmId());
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

    private void UpdateStudentEventGoingStatus(String URL, final String student_id, final String pickUpStatus) {
        VolleyWrapper volleyWrapper = new VolleyWrapper(URL);
        String[] name = {"access_token", "event_id", "users_id", "student_id", "status", "pick_up_status"};
        String[] value = {PreferenceManager.getAccessToken(mContext), event_id, PreferenceManager.getUserId(mContext), student_id, status, pickUpStatus};
        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("The response is 123" + successResponse);
                try {
                    JSONObject obj = new JSONObject(successResponse);
                    String response_code = obj.getString(JTAG_RESPONSECODE);
                    if (response_code.equalsIgnoreCase("200")) {

                        JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                        String status_code = secobj.getString(JTAG_STATUSCODE);
                        if (status_code.equalsIgnoreCase("303")) {

                            int survey=secobj.getInt("survey");
                            if (survey==1)
                            {
                                callSurveyApi();
                            }
                            if (AppUtils.isNetworkConnected(mContext)) {
                                callEventDetailsApi(URL_GET_SPORTS_EVENTS_LISTDETAILS, stud_id);
                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                            }

                        }

                    } else if (response_code.equalsIgnoreCase("500")) {
                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        UpdateStudentEventGoingStatus(URL_UPDATE_EVENTGOINGSTATUS, stud_id, pickUpStatus);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        UpdateStudentEventGoingStatus(URL_UPDATE_EVENTGOINGSTATUS, stud_id, pickUpStatus);

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        UpdateStudentEventGoingStatus(URL_UPDATE_EVENTGOINGSTATUS, stud_id, pickUpStatus);

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
				/*CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
						, getResources().getString(R.string.ok));
				dialog.show();*/

            }
        });


    }

    private void sendEmailToStaff(String URL) {
        VolleyWrapper volleyWrapper = new VolleyWrapper(URL);
        String[] name = {"access_token", "email", "users_id", "title", "message"};
        String[] value = {PreferenceManager.getAccessToken(mContext), sportsModelArrayList.get(0).getSports_email(), PreferenceManager.getUserId(mContext), text_dialog.getText().toString(), text_content.getText().toString()};//contactEmail

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
                        } else {
                            Toast toast = Toast.makeText(mContext, "Email not sent", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        sendEmailToStaff(URL_SEND_EMAIL_TO_STAFF);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        sendEmailToStaff(URL_SEND_EMAIL_TO_STAFF);

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        sendEmailToStaff(URL_SEND_EMAIL_TO_STAFF);

                    } else {
						/*CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
								, getResources().getString(R.string.ok));
						dialog.show();*/
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    }
                } catch (Exception ex) {
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

    public void showDialogAlertPickUp(final Activity activity, String msgHead) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_dialogue_pickup_select);
        ImageView icon = (ImageView) dialog.findViewById(R.id.iconImageView);
        TextView textHead = (TextView) dialog.findViewById(R.id.alertHead);
        textHead.setText(msgHead);
        final Button pickUpVenue = (Button) dialog.findViewById(R.id.pickUpVenue);
        final Button pickUpAtSchool = (Button) dialog.findViewById(R.id.pickUpAtSchool);
        isClick = false;
       final String pickUpStatusCurrent=pickUpStatus;
        Button dialogButton = (Button) dialog.findViewById(R.id.btn_Ok);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClick) {
                    if (AppUtils.isNetworkConnected(mContext)) {
                        dialog.dismiss();
                        final int sdk = android.os.Build.VERSION.SDK_INT;
                        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                            mGoingButton.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.edittext_bg));
                            mNotGoingButton.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.event_status_selectbg));
                            mNotSurebutton.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.event_status_selectbg));
                        } else {
                            mGoingButton.setBackground(mContext.getResources().getDrawable(R.drawable.edittext_bg));
                            mNotGoingButton.setBackground(mContext.getResources().getDrawable(R.drawable.event_status_selectbg));
                            mNotSurebutton.setBackground(mContext.getResources().getDrawable(R.drawable.event_status_selectbg));
                        }
                        status = "1";
                        UpdateStudentEventGoingStatus(URL_UPDATE_EVENTGOINGSTATUS, stud_id, pickUpStatus);
                        attending = true;
                        notAttending = false;

                    }else {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                    }
                } else {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "Select any Pick Up Point", R.drawable.exclamationicon, R.drawable.roundred);


                }
            }
        });
        pickUpAtSchool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pickUpStatus = "2";
                isClick = true;
                pickUpVenue.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.curve_filled_light_grey_sports));
                pickUpAtSchool.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.curve_filled_split_bg_sports));
//                mGoingButton.setBackground(mContext.getResources().getDrawable(R.drawable.edittext_bg));
//                mNotGoingButton.setBackground(mContext.getResources().getDrawable(R.drawable.event_status_selectbg));
//                mNotSurebutton.setBackground(mContext.getResources().getDrawable(R.drawable.event_status_selectbg));

            }
        });
        pickUpVenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickUpStatus = "1";
                isClick = true;
                pickUpVenue.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.curve_filled_split_bg_sports));
                pickUpAtSchool.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.curve_filled_light_grey_sports));
//                mGoingButton.setBackground(mContext.getResources().getDrawable(R.drawable.edittext_bg));
//                mNotGoingButton.setBackground(mContext.getResources().getDrawable(R.drawable.event_status_selectbg));
//                mNotSurebutton.setBackground(mContext.getResources().getDrawable(R.drawable.event_status_selectbg));

            }
        });
        Button dialogButtonCancel = (Button) dialog.findViewById(R.id.btn_Cancel);
        dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickUpStatus=pickUpStatusCurrent;
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    /**********************************SURVEY ******************************************/

    /**********************************SURVEY API***************************************/
    public void callSurveyApi() {
        surveyArrayList=new ArrayList<>();


        try {
            final VolleyWrapper manager = new VolleyWrapper(URL_GET_USER_SURVEY);
            String[] name = new String[]{JTAG_ACCESSTOKEN,"users_id","module"};
            String[] value = new String[]{PreferenceManager.getAccessToken(mContext),PreferenceManager.getUserId(mContext),"8"};
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
                                                model.setContact_email(dataObject.optString("contact_email"));
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
                                                                nModel.setLabel(answerObject.optString("label"));
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
                                            showSurveyWelcomeDialogue((Activity)mContext ,surveyArrayList,false);
                                        }

                                    }
                                }
                                else if (responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) ||
                                        responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                                        responsCode.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
                                    AppUtils.postInitParam((Activity)mContext, new AppUtils.GetAccessTokenInterface() {
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
//        else
//        {
//			thankyouTxt.setVisibility(View.GONE);
//		}
        ImageView bannerImg = (ImageView) dialog.findViewById(R.id.bannerImg);
        if (!surveyArrayList.get(pos+1).getImage().equalsIgnoreCase(""))
        {
            Picasso.with(mContext).load(AppUtils.replace(surveyArrayList.get(pos+1).getImage())).placeholder(R.drawable.survey).fit().into(bannerImg);
        }
        else
        {
            bannerImg.setImageResource(R.drawable.survey);
        }
        headingTxt.setText(surveyArrayList.get(pos+1).getTitle());
        descriptionTxt.setText(surveyArrayList.get(pos+1).getDescription());


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
        Button skipBtn = (Button) dialog.findViewById(R.id.skipBtn);
        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCloseSurveyDialog(activity,dialog);


            }
        });
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCloseSurveyDialog(activity,dialog);
            }
        });
        dialog.show();

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
                        callSurveySubmitApi(URL_SURVEY_SUBMIT,surveyID,JSON_STRING,(Activity)mContext,surveyArrayList,false,1);
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
                        callSurveySubmitApi(URL_SURVEY_SUBMIT,surveyID,JSON_STRING,(Activity)mContext,surveyArrayList,true,1);
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
                        callSurveySubmitApi(URL_SURVEY_SUBMIT,surveyID,JSON_STRING,(Activity)mContext,surveyArrayList,false,1);
                    }
                    else {
                        callSurveySubmitApi(URL_SURVEY_SUBMIT,surveyID,JSON_STRING,(Activity)mContext,surveyArrayList,true,1);

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

        ImageView emailImg = (ImageView) dialogThank.findViewById(R.id.emailImg);
        if (surveyEmail.equalsIgnoreCase(""))
        {
            emailImg.setVisibility(View.GONE);
        }
        else {
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


        Button btn_Ok = (Button) dialogThank.findViewById(R.id.btn_Ok);
        btn_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (isThankyou)
                {
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

                            showSurveyThankYouDialog((Activity)mContext,surveyArrayList,isThankyou);

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

}
