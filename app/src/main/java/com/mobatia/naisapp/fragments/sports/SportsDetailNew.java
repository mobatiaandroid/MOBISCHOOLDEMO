package com.mobatia.naisapp.fragments.sports;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.bus_routes.BusRoutesRecyclerViewActivity;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.activities.participants.ParticipantRecyclerViewActivity;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.URLConstants;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SportsDetailNew extends Activity implements URLConstants, JSONConstants {
    Bundle extras;
    private Context mContext = this;
    RelativeLayout relativeHeader;
    RelativeLayout relativePickupdelivery;
    RelativeLayout relativeParticipants;
    HeaderManager headermanager;
    ImageView back;
    ImageView home;
    String event_id;
    ArrayList<StudentModel> studentsModelArrayList = new ArrayList<>();
    ArrayList<SportsModel> sportsModelArrayList = new ArrayList<>();
    ArrayList<String> studentList = new ArrayList<>();
    LinearLayout mStudentSpinner, statusButtonslayout;
    String mDate = null;
    String mTime = null;
    Button mGoingButton;
    Button mNotGoingButton;
    Button mNotSurebutton;
    String stud_id = "";
    ImageView addToCalendar, sendMessage, callButton, locateMe;
    TextView decisionText, start_date, end_date, description, venueTextViewValue, studentName, start_time, end_time, eventTitle;
    String status;
    ScrollView mScrollView;
    EditText text_dialog, text_content;
    boolean attending=false,notAttending=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sports_event_detail);
        initUI();
        if (PreferenceManager.getUserId(mContext).equals("")) {
            if(AppUtils.isNetworkConnected(mContext)) {
                callEventDetailsApi(URL_GET_SPORTS_EVENTS_LISTDETAILS, "");
            }else{
                AppUtils.showDialogAlertDismiss((Activity)mContext,"Network Error",getString(R.string.no_internet),R.drawable.nonetworkicon,R.drawable.roundred);

            }
            mGoingButton.setVisibility(View.GONE);
            mNotGoingButton.setVisibility(View.GONE);
            mNotSurebutton.setVisibility(View.GONE);
            decisionText.setVisibility(View.GONE);
            mStudentSpinner.setVisibility(View.GONE);
            statusButtonslayout.setVisibility(View.GONE);
        } else {
            if(AppUtils.isNetworkConnected(mContext)) {
                getStudentsListAPI(URL_GET_STUDENT_LIST);
            }else{
                AppUtils.showDialogAlertDismiss((Activity)mContext,"Network Error",getString(R.string.no_internet),R.drawable.nonetworkicon,R.drawable.roundred);

            }
        }
    }

    private void initUI() {
        extras = getIntent().getExtras();
        if (extras != null) {
            event_id = extras.getString("event_id");
        }

        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        mScrollView = (ScrollView) findViewById(R.id.mScrollView);
        relativeParticipants = (RelativeLayout) findViewById(R.id.relativeParticipants);
        relativePickupdelivery = (RelativeLayout) findViewById(R.id.relativePickupdelivery);
        mStudentSpinner = (LinearLayout) findViewById(R.id.studentSpinner);
        mGoingButton = (Button) findViewById(R.id.goingBtn);
        mNotGoingButton = (Button) findViewById(R.id.notGoingBtn);
        mNotSurebutton = (Button) findViewById(R.id.notSureBtn);
        decisionText = (TextView) findViewById(R.id.decisionText);
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
        headermanager = new HeaderManager(SportsDetailNew.this, getString(R.string.sports_title));
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
        //studentList.add("Select a child");
        mStudentSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSocialmediaList(studentsModelArrayList);
            }
        });
        locateMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sportsModelArrayList.get(0).getSports_lat().equals("")||
                        sportsModelArrayList.get(0).getSports_lng().equals("")) {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.no_details_available), R.drawable.exclamationicon, R.drawable.round);

                }else{
                    Intent intent = new Intent(SportsDetailNew.this, LocateMe.class);
                    intent.putExtra("latitude", sportsModelArrayList.get(0).getSports_lat());
                    intent.putExtra("longitude", sportsModelArrayList.get(0).getSports_lng());
                    startActivity(intent);
                }
            }
        });
//        callButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(!sportsModelArrayList.get(0).getSports_phone_number().equals("")) {
//                    Intent intent = new Intent(Intent.ACTION_CALL);
//                    intent.setData(Uri.parse("tel:" + sportsModelArrayList.get(0).getSports_phone_number()));
//                    startActivity(intent);
//                }else{
//                    AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.no_contact_no), R.drawable.exclamationicon, R.drawable.round);
//
//                }
//
//                /*if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                    // TODO: Consider calling
//                    //    ActivityCompat#requestPermissions
//                    // here to request the missing permissions, and then overriding
//                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                    //                                          int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for ActivityCompat#requestPermissions for more details.
//                    return;
//                }*/
//            }
//        });

        /*CustomSpinnerAdapter dataAdapter = new CustomSpinnerAdapter(mContext,
                R.layout.spinnertextwithoutbg, studentList,-1);
        mStudentSpinner.setAdapter(dataAdapter);*/

        mGoingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* mGoingButton.setBackgroundColor(mContext.getResources().getColor(R.color.ed));
                mNotGoingButton.setBackgroundColor(mContext.getResources().getColor(R.color.grey));
                mNotSurebutton.setBackgroundColor(mContext.getResources().getColor(R.color.grey));*/

                if (!attending) {
                    final int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        mGoingButton.setBackgroundDrawable( mContext.getResources().getDrawable(R.drawable.edittext_bg) );
                        mNotGoingButton.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.event_status_selectbg));
                        mNotSurebutton.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.event_status_selectbg));
                    } else {
                        mGoingButton.setBackground( mContext.getResources().getDrawable(R.drawable.edittext_bg));
                        mNotGoingButton.setBackground(mContext.getResources().getDrawable(R.drawable.event_status_selectbg));
                        mNotSurebutton.setBackground(mContext.getResources().getDrawable(R.drawable.event_status_selectbg));
                    }
                    status = "1";
                    decisionText.setText("ATTENDING");
                    if (AppUtils.isNetworkConnected(mContext)) {
                        UpdateStudentEventGoingStatus(URL_UPDATE_EVENTGOINGSTATUS, stud_id);
                        attending = true;
                        notAttending = false;

                    } else {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                    }
                }
                else
                {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "You have already submitted your participation status.", R.drawable.exclamationicon, R.drawable.round);

                }

                //callEventDetailsApi(URL_GET_SPORTS_EVENTS_LISTDETAILS, stud_id);
            }
        });

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

                Intent mIntent=new Intent(mContext,ParticipantRecyclerViewActivity.class);
                mIntent.putExtra("tab_type","Participants");
                mIntent.putExtra("participant_sports_array",sportsModelArrayList.get(0).getmSportsModelHouseArrayList());
                startActivity(mIntent);
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
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sportsModelArrayList.get(0).getSports_email().equals("")) {
                    final Dialog dialog = new Dialog(mContext);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.alert_send_email_dialog);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    Button dialogCancelButton = (Button) dialog.findViewById(R.id.cancelButton);
                    Button submitButton= (Button) dialog.findViewById(R.id.submitButton);
                    text_dialog= (EditText) dialog.findViewById(R.id.text_dialog);
                    text_content= (EditText) dialog.findViewById(R.id.text_content);


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

//                    Intent email = new Intent(Intent.ACTION_SEND);
//                    email.putExtra(Intent.EXTRA_EMAIL, new String[]{sportsModelArrayList.get(0).getSports_email()});
//                    email.setType("message/rfc822");
//                    startActivity(Intent.createChooser(email, "Choose an Email client :"));
                } else {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.no_contact_email), R.drawable.exclamationicon, R.drawable.round);
                }

            }
        });

        mNotGoingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*mNotGoingButton.setBackgroundColor(mContext.getResources().getColor(R.color.split_bg));
                mGoingButton.setBackgroundColor(mContext.getResources().getColor(R.color.grey));
                mNotSurebutton.setBackgroundColor(mContext.getResources().getColor(R.color.grey));*/

                if (!notAttending) {
                    final int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        mGoingButton.setBackgroundDrawable( mContext.getResources().getDrawable(R.drawable.event_status_selectbg) );
                        mNotGoingButton.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.edittext_bg));
                        mNotSurebutton.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.event_status_selectbg));
                    } else {
                        mGoingButton.setBackground( mContext.getResources().getDrawable(R.drawable.event_status_selectbg));
                        mNotGoingButton.setBackground(mContext.getResources().getDrawable(R.drawable.edittext_bg));
                        mNotSurebutton.setBackground(mContext.getResources().getDrawable(R.drawable.event_status_selectbg));
                    }
                    status = "0";
                    decisionText.setText("NOT ATTENDING");

                    if (AppUtils.isNetworkConnected(mContext)) {
                        UpdateStudentEventGoingStatus(URL_UPDATE_EVENTGOINGSTATUS, stud_id);
                        attending = false;
                        notAttending = true;
                    } else {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                    }
                }
                else
                {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "You have already submitted your participation status.", R.drawable.exclamationicon, R.drawable.round);

                }
                //callEventDetailsApi(URL_GET_SPORTS_EVENTS_LISTDETAILS, stud_id);
            }
        });

        mNotSurebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* mNotGoingButton.setBackgroundColor(mContext.getResources().getColor(R.color.grey));
                mGoingButton.setBackgroundColor(mContext.getResources().getColor(R.color.grey));
                mNotSurebutton.setBackgroundColor(mContext.getResources().getColor(R.color.split_bg));*/
                final int sdk = android.os.Build.VERSION.SDK_INT;
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    mGoingButton.setBackgroundDrawable( mContext.getResources().getDrawable(R.drawable.event_status_selectbg) );
                    mNotGoingButton.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.event_status_selectbg));
                    mNotSurebutton.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.edittext_bg));
                } else {
                    mGoingButton.setBackground( mContext.getResources().getDrawable(R.drawable.event_status_selectbg));
                    mNotGoingButton.setBackground(mContext.getResources().getDrawable(R.drawable.event_status_selectbg));
                    mNotSurebutton.setBackground(mContext.getResources().getDrawable(R.drawable.edittext_bg));
                }
                status = "2";
                if(AppUtils.isNetworkConnected(mContext)) {
                    UpdateStudentEventGoingStatus(URL_UPDATE_EVENTGOINGSTATUS, stud_id);
                }else{
                    AppUtils.showDialogAlertDismiss((Activity)mContext,"Network Error",getString(R.string.no_internet),R.drawable.nonetworkicon,R.drawable.roundred);

                }
                decisionText.setText("NOT SURE");
                //callEventDetailsApi(URL_GET_SPORTS_EVENTS_LISTDETAILS, stud_id);
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
                            start_date.setText(separateDate(sportsModelArrayList.get(0).getSports_start_date()));
                            start_time.setText(separateTime(sportsModelArrayList.get(0).getSports_start_date()));
                            end_time.setText(separateTime(sportsModelArrayList.get(0).getSports_end_date()));
                            end_date.setText(separateDate(sportsModelArrayList.get(0).getSports_end_date()));
                            description.setText(sportsModelArrayList.get(0).getSports_description());
                            venueTextViewValue.setText(sportsModelArrayList.get(0).getSports_venue());
                            eventTitle.setText(sportsModelArrayList.get(0).getSports_name());
                            mScrollView.setVisibility(View.VISIBLE);
                            System.out.println("Going status---" + sportsModelArrayList.get(0).getGoing_status());
                            if(sportsModelArrayList.get(0).getPartipipate_status().equals("1")){
                                statusButtonslayout.setVisibility(View.VISIBLE);
                                decisionText.setVisibility(View.VISIBLE);
                                relativeParticipants.setVisibility(View.VISIBLE);
                            }else{
                                statusButtonslayout.setVisibility(View.GONE);
                                decisionText.setVisibility(View.GONE);
                                relativeParticipants.setVisibility(View.GONE);
                            }
                            if (dataObj.optString(JTAG_EVENT_GOINGSTATUS).equals("0")) {
                               /* mNotGoingButton.setBackgroundColor(mContext.getResources().getColor(R.color.split_bg));
                                mGoingButton.setBackgroundColor(mContext.getResources().getColor(R.color.grey));
                                mNotSurebutton.setBackgroundColor(mContext.getResources().getColor(R.color.grey));*/
                                final int sdk = android.os.Build.VERSION.SDK_INT;
                                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                    mGoingButton.setBackgroundDrawable( mContext.getResources().getDrawable(R.drawable.event_status_selectbg) );
                                    mNotGoingButton.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.edittext_bg));
                                    mNotSurebutton.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.event_status_selectbg));
                                } else {
                                    mGoingButton.setBackground( mContext.getResources().getDrawable(R.drawable.event_status_selectbg));
                                    mNotGoingButton.setBackground(mContext.getResources().getDrawable(R.drawable.edittext_bg));
                                    mNotSurebutton.setBackground(mContext.getResources().getDrawable(R.drawable.event_status_selectbg));
                                }
//                                decisionText.setText("NO I CANNOT MAKE IT");
                                decisionText.setText("NOT ATTENDING");
                                attending = false;
                                notAttending = true;
                            } else if (dataObj.optString(JTAG_EVENT_GOINGSTATUS).equals("1")) {
                                /*mGoingButton.setBackgroundColor(mContext.getResources().getColor(R.color.split_bg));
                                mNotGoingButton.setBackgroundColor(mContext.getResources().getColor(R.color.grey));
                                mNotSurebutton.setBackgroundColor(mContext.getResources().getColor(R.color.grey));*/
                                final int sdk = android.os.Build.VERSION.SDK_INT;
                                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                    mGoingButton.setBackgroundDrawable( mContext.getResources().getDrawable(R.drawable.edittext_bg) );
                                    mNotGoingButton.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.event_status_selectbg));
                                    mNotSurebutton.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.event_status_selectbg));
                                } else {
                                    mGoingButton.setBackground( mContext.getResources().getDrawable(R.drawable.edittext_bg));
                                    mNotGoingButton.setBackground(mContext.getResources().getDrawable(R.drawable.event_status_selectbg));
                                    mNotSurebutton.setBackground(mContext.getResources().getDrawable(R.drawable.event_status_selectbg));
                                }
//                                decisionText.setText("YES,I'M IN");
                                decisionText.setText("ATTENDING");
                                attending = true;
                                notAttending = false;
                            } else if (dataObj.optString(JTAG_EVENT_GOINGSTATUS).equals("2")) {
                                /*mGoingButton.setBackgroundColor(mContext.getResources().getColor(R.color.grey));
                                mNotGoingButton.setBackgroundColor(mContext.getResources().getColor(R.color.grey));
                                mNotSurebutton.setBackgroundColor(mContext.getResources().getColor(R.color.split_bg));*/
                                final int sdk = android.os.Build.VERSION.SDK_INT;
                                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                    mGoingButton.setBackgroundDrawable( mContext.getResources().getDrawable(R.drawable.event_status_selectbg) );
                                    mNotGoingButton.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.event_status_selectbg));
                                    mNotSurebutton.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.edittext_bg));
                                } else {
                                    mGoingButton.setBackground( mContext.getResources().getDrawable(R.drawable.event_status_selectbg));
                                    mNotGoingButton.setBackground(mContext.getResources().getDrawable(R.drawable.event_status_selectbg));
                                    mNotSurebutton.setBackground(mContext.getResources().getDrawable(R.drawable.edittext_bg));
                                }
//                                decisionText.setText("I'M NOT SURE");
                                decisionText.setText("NOT SURE");

                            } else {
                                /*mGoingButton.setBackgroundColor(mContext.getResources().getColor(R.color.ev));
                                mNotGoingButton.setBackgroundColor(mContext.getResources().getColor(R.color.event_list_bg));
                                mNotSurebutton.setBackgroundColor(mContext.getResources().getColor(R.color.grey));*/
                                final int sdk = android.os.Build.VERSION.SDK_INT;
                                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                                    mGoingButton.setBackgroundDrawable( mContext.getResources().getDrawable(R.drawable.event_status_selectbg) );
                                    mNotGoingButton.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.edittext_bg));
                                    mNotSurebutton.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.event_status_selectbg));
                                } else {
                                    mGoingButton.setBackground( mContext.getResources().getDrawable(R.drawable.event_status_selectbg));
                                    mNotGoingButton.setBackground(mContext.getResources().getDrawable(R.drawable.edittext_bg));
                                    mNotSurebutton.setBackground(mContext.getResources().getDrawable(R.drawable.event_status_selectbg));
                                }
                                decisionText.setText("");
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
        sportsModel.setPartipipate_status(dataObj.optString(JTAG_ISPARTICIPANT));
        // sportsModel.setSports_phone_number(dataObj.optString(JTAG_EVENT_PHONE));
        sportsModel.setSports_email(dataObj.optString(JTAG_EMAIL));
        sportsModel.setGoing_status(dataObj.optString(JTAG_EVENT_GOINGSTATUS));
        System.out.println("Going status---1--" + dataObj.optString(JTAG_EVENT_GOINGSTATUS));

        try {
            JSONArray participantDetailsArray = dataObj.getJSONArray(JTAG_EVENT_PARTICIPANTS_DETAILS);
            if (participantDetailsArray.length()>0)
            {
                relativeParticipants.setVisibility(View.VISIBLE);
            }
            else
            {
                relativeParticipants.setVisibility(View.GONE);

            }
            ArrayList<SportsModel>mSportsModelHouseArrayList=new ArrayList<>();
            for (int m=0;m<participantDetailsArray.length();m++)
            {
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
                relativePickupdelivery.setVisibility(View.VISIBLE);
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
                ArrayList<BusModel>mBusRouteArrayList=new ArrayList<>();
                JSONArray mBusRouteArray=bObj.optJSONArray("bus_routes");
                for (int m=0;m<mBusRouteArray.length();m++)
                {
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
                                studentName.setText(studentsModelArrayList.get(0).getmName());
                                stud_id = studentsModelArrayList.get(0).getmId();
                                System.out.println("size--" + studentList.size());
                                if (AppUtils.isNetworkConnected(mContext)) {
                                    callEventDetailsApi(URL_GET_SPORTS_EVENTS_LISTDETAILS, stud_id);
                                } else {
                                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                                }

                                // studentList.add("Select a child");

                                /*CustomSpinnerAdapter dataAdapter = new CustomSpinnerAdapter(mContext,
                                        R.layout.spinnertextwithoutbg, studentList,-1);
                                mStudentSpinner.setAdapter(dataAdapter);*/

                            } else {
                                Toast.makeText(SportsDetailNew.this, "No data found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                        AppUtils.showDialogAlertDismiss((Activity)mContext,"Alert",getString(R.string.common_error),R.drawable.exclamationicon,R.drawable.round);

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
				/*CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
						, getResources().getString(R.string.ok));
				dialog.show();*/
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
                        System.out.println("Selected student id---" + stud_id);
                        System.out.println("Selected student name--" + studentName.getText().toString());
                        if(AppUtils.isNetworkConnected(mContext)) {
                            callEventDetailsApi(URL_GET_SPORTS_EVENTS_LISTDETAILS, mStudentArray.get(position).getmId());
                        }else{
                            AppUtils.showDialogAlertDismiss((Activity)mContext,"Network Error",getString(R.string.no_internet),R.drawable.nonetworkicon,R.drawable.roundred);

                        }
                    }

                    public void onLongClickItem(View v, int position) {
                        System.out.println("On Long Click Item interface");
                    }
                }));
        dialog.show();
    }

    private void UpdateStudentEventGoingStatus(String URL, final String student_id) {
        VolleyWrapper volleyWrapper = new VolleyWrapper(URL);
        String[] name = {"access_token", "event_id", "users_id", "student_id", "status"};
        String[] value = {PreferenceManager.getAccessToken(mContext), event_id, PreferenceManager.getUserId(mContext), student_id, status};
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
                            if(AppUtils.isNetworkConnected(mContext)) {
                                callEventDetailsApi(URL_GET_SPORTS_EVENTS_LISTDETAILS, stud_id);
                            }else{
                                AppUtils.showDialogAlertDismiss((Activity)mContext,"Network Error",getString(R.string.no_internet),R.drawable.nonetworkicon,R.drawable.roundred);

                            }
                        }

                    } else if (response_code.equalsIgnoreCase("500")) {
                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        UpdateStudentEventGoingStatus(URL_UPDATE_EVENTGOINGSTATUS, stud_id);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        UpdateStudentEventGoingStatus(URL_UPDATE_EVENTGOINGSTATUS, stud_id);

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        UpdateStudentEventGoingStatus(URL_UPDATE_EVENTGOINGSTATUS, stud_id);

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
                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

            }
        });


    }
    private void sendEmailToStaff(String URL) {
        VolleyWrapper volleyWrapper=new VolleyWrapper(URL);
        String[] name={"access_token","email","users_id","title","message"};
        String[] value={PreferenceManager.getAccessToken(mContext),sportsModelArrayList.get(0).getSports_email(),PreferenceManager.getUserId(mContext),text_dialog.getText().toString(),text_content.getText().toString()};//contactEmail

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


}
