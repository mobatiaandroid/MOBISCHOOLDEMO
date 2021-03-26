package com.mobatia.naisapp.activities.canteen_new;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
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

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.canteen_new.adapter.CartDateRecyclerAdapter;
import com.mobatia.naisapp.activities.canteen_new.model.CartItemDateModel;
import com.mobatia.naisapp.activities.canteen_new.model.CartItemDetailModel;
import com.mobatia.naisapp.activities.canteen_new.model.DateModel;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.activities.payment_history.PaymentHistoryNew;
import com.mobatia.naisapp.constants.IntentPassValueConstants;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NameValueConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.absence.model.LeavesModel;
import com.mobatia.naisapp.fragments.canteen.CanteenActviity;
import com.mobatia.naisapp.fragments.canteen_new.CanteenFragmentNew;
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
import java.util.Calendar;
import java.util.GregorianCalendar;

import okhttp3.MediaType;

public class PreOrderActivity extends Activity implements URLConstants, StatusConstants, JSONConstants, IntentPassValueConstants, NameValueConstants {
    Context mContext=this;
    String tab_type;
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back,btn_history,home;
    Bundle extras;
    ImageView nextBtn;
    LinearLayout mStudentSpinner;
    static ArrayList<StudentModel> studentsModelArrayList = new ArrayList<>();
    static TextView studentName;
    String stud_id;
    String studClass = "";
    String orderId = "";
    static String stud_img = "";
    static ImageView studImg;
    ArrayList<String> studentList = new ArrayList<>();
     String ordered_user_type="";
     String student_id="";
     String staff_id="";
     String parent_id="";
     RelativeLayout addOrderRelative,orderHistoryRelative,myOrderRelative;
     Button cartBtn;
    String time_exceed="";

    int cartCount=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preorder_new);
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
        }
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        orderHistoryRelative = (RelativeLayout) findViewById(R.id.orderHistoryRelative);
        headermanager = new HeaderManager(PreOrderActivity.this, tab_type);
        headermanager.getHeader(relativeHeader, 6);
        back = headermanager.getLeftButton();
        btn_history = headermanager.getRightHistoryImage();
        btn_history.setVisibility(View.INVISIBLE);
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
        ordered_user_type="1";
        parent_id=PreferenceManager.getUserId(mContext);
        staff_id="";
        addOrderRelative=findViewById(R.id.addOrderRelative);
        myOrderRelative=findViewById(R.id.myOrderRelative);
        cartBtn=findViewById(R.id.cartBtn);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(mContext, HomeListAppCompatActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
            }
        });
     //   nextBtn = findViewById(R.id.nextBtn);
        orderHistoryRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(PreOrderActivity.this,OrderHistoryActivity.class);
                intent.putExtra("ordered_user_type","1");
                intent.putExtra("tab_type","Order History");
                intent.putExtra("student_id",stud_id);
                intent.putExtra("parent_id",PreferenceManager.getUserId(mContext));
                intent.putExtra("staff_id","");
                startActivity(intent);
            }
        });
        myOrderRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(PreOrderActivity.this,MyOrderActivity.class);
                intent.putExtra("ordered_user_type","1");
                intent.putExtra("tab_type","My Orders");
                intent.putExtra("student_id",stud_id);
                intent.putExtra("parent_id",PreferenceManager.getUserId(mContext));
                intent.putExtra("staff_id","");
                startActivity(intent);
            }
        });
        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PreOrderActivity.this,BasketListActivity.class);
                intent.putExtra("ordered_user_type","1");
                intent.putExtra("tab_type","My Order");
                intent.putExtra("student_id",stud_id);
                intent.putExtra("parent_id",PreferenceManager.getUserId(mContext));
                intent.putExtra("staff_id","");
                startActivity(intent);
            }
        });
        btn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PreOrderActivity.this,ConfirmedOrderActivity.class);
                intent.putExtra("ordered_user_type","1");
                intent.putExtra("tab_type","History");
                intent.putExtra("student_id",stud_id);
                intent.putExtra("parent_id",PreferenceManager.getUserId(mContext));
                intent.putExtra("staff_id","");
                startActivity(intent);
            }
        });
        addOrderRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PreferenceManager.getUserId(mContext).equals(""))
                {
                    if (!PreferenceManager.getStaffId(mContext).equals(""))
                    {
                         ordered_user_type="2";
                         student_id="";
                         staff_id=PreferenceManager.getStaffId(mContext);
                         parent_id="";
                    }
                }
                else
                {
                    ordered_user_type="1";
                    student_id=stud_id;
                    staff_id="";
                    parent_id=PreferenceManager.getUserId(mContext);
                }
                callTimeExceed(ordered_user_type,student_id,staff_id,parent_id);

            }
        });
        mStudentSpinner = findViewById(R.id.studentSpinner);
        studentName = findViewById(R.id.studentName);
        studentName = findViewById(R.id.studentName);
        studImg = findViewById(R.id.imagicon);

        mStudentSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (studentsModelArrayList.size() > 0) {
                    showSocialmediaList(studentsModelArrayList);
                } else {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.student_not_available), R.drawable.exclamationicon, R.drawable.round);

                }
            }
        });

     //   nextBtn.setVisibility(View.VISIBLE);

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
                        PreferenceManager.setCanteenStudentName(mContext,studentsModelArrayList.get(position).getmName());

                    }

                    public void onLongClickItem(View v, int position) {
                        System.out.println("On Long Click Item interface");
                    }
                }));
        dialog.show();
    }
    private void getStudentsListAPI(final String URLHEAD)
    {
        VolleyWrapper volleyWrapper = new VolleyWrapper(URLHEAD);
        String[] name = {"access_token", "users_id","portal"};
        String[] value = {PreferenceManager.getAccessToken(mContext), PreferenceManager.getUserId(mContext),"1"};
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
//                                  PreferenceManager.setLeaveStudentId(mContext, stud_id);
//                                  PreferenceManager.setLeaveStudentName(mContext, studentsModelArrayList.get(0).getmName());
                                studClass = studentsModelArrayList.get(0).getmClass();
                                stud_img = studentsModelArrayList.get(0).getmPhoto();
                                PreferenceManager.setCanteenStudentId(mContext,studentsModelArrayList.get(0).getmId());
                                PreferenceManager.setCanteenStudentImage(mContext,studentsModelArrayList.get(0).getmPhoto());
                                PreferenceManager.setCanteenStudentName(mContext,studentsModelArrayList.get(0).getmName());

                                if (!(stud_img.equals(""))) {

                                    Picasso.with(mContext).load(AppUtils.replace(stud_img)).placeholder(R.drawable.boy).fit().into(studImg);
                                } else {

                                    studImg.setImageResource(R.drawable.boy);
                                }



                            } else {

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
    private void callTimeExceed(String ordered_user_type,String student_id,String staff_id,String parent_id)
    {
        VolleyWrapper volleyWrapper = new VolleyWrapper(URL_GET_TIME_EXCEED);
        String[] name = {"access_token"};
        String[] value = {PreferenceManager.getAccessToken(mContext)};
        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("The response is time exceed" + successResponse);
                try {
                    JSONObject obj = new JSONObject(successResponse);
                    String response_code = obj.getString(JTAG_RESPONSECODE);
                    if (response_code.equalsIgnoreCase("200")) {
                        JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                        String status_code = secobj.getString(JTAG_STATUSCODE);
                        if (status_code.equalsIgnoreCase("303")) {

                            String current_date_time=secobj.optString("current_date_time");
                            String time_exceed=secobj.optString("time_exceed");
                            showCalendarPopup(PreOrderActivity.this,ordered_user_type,student_id,staff_id,parent_id,time_exceed,current_date_time);

                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                      callTimeExceed(ordered_user_type,student_id,staff_id,parent_id);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        callTimeExceed(ordered_user_type,student_id,staff_id,parent_id);

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        callTimeExceed(ordered_user_type,student_id,staff_id,parent_id);

                    } else {

                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    }
                } catch (Exception ex) {
                    System.out.println("The Exception in edit profile is" + ex.toString());
                }

            }

            @Override
            public void responseFailure(String failureResponse)
                {
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


    public  void showCalendarPopup(Activity activity,String ordered_user_type,String student_id,String staff_id,String parent_id,String time_exceed,String current_date_time)
    {
        ArrayList<DateModel>mDateArrayList=new ArrayList<>();
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.show_calendar_popup);
        Button GetDate = (Button) dialog.findViewById(R.id.GetDate);
        ImageView closeImg=(ImageView)dialog.findViewById(R.id.closeImg);
        ImageView dummyClose=(ImageView)dialog.findViewById(R.id.dummyClose);
        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDateArrayList.clear();
                dialog.dismiss();
            }
        });
        dummyClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDateArrayList.clear();
                dialog.dismiss();
            }
        });

        final CalendarView calendarView = dialog.findViewById(R.id.MCalendar);

        if (time_exceed.equalsIgnoreCase("1"))
        {
            Calendar c = Calendar.getInstance();
            calendarView.setMinimumDate(c);
        }
        else
        {
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE, -1);
            calendarView.setMinimumDate(c);
        }


//        Calendar max = Calendar.getInstance();
//        max.add(Calendar.MONTH, 1);
//        calendarView.setMaximumDate(max);

        calendarView.setPreviousButtonImage(mContext.getResources().getDrawable(R.drawable.arrow_list_back));
        calendarView.setForwardButtonImage(mContext.getResources().getDrawable(R.drawable.arrow_list));

        calendarView.setOnForwardPageChangeListener(new OnCalendarPageChangeListener() {
            @Override
            public void onChange() {
                calendarView.setPreviousButtonImage(mContext.getResources().getDrawable(R.drawable.arrow_list_back));
                calendarView.setForwardButtonImage(mContext.getResources().getDrawable(R.drawable.arrow_list));
            }
        });

        calendarView.setOnPreviousPageChangeListener(new OnCalendarPageChangeListener() {
            @Override
            public void onChange() {
                calendarView.setPreviousButtonImage(mContext.getResources().getDrawable(R.drawable.arrow_list_back));
                calendarView.setForwardButtonImage(mContext.getResources().getDrawable(R.drawable.arrow_list));
            }
        });
        GetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               for (Calendar calendar : calendarView.getSelectedDates())
                 {
                     System.out.println("GetDate: "+calendar.getTime().toString());
                     System.out.println("GetDate: "+AppUtils.dateParsingToyymmdd(calendar.getTime().toString()));
                     System.out.println("GetDate Month: "+AppUtils.dateParsingTomm(calendar.getTime().toString()));
                     System.out.println("GetDate Day: "+AppUtils.dateParsingToEEE(calendar.getTime().toString()));
                     System.out.println("GetDate Year: "+AppUtils.dateParsingToyyy(calendar.getTime().toString()));
                     System.out.println("GetDate Date: "+AppUtils.dateParsingTodd(calendar.getTime().toString()));
                     System.out.println("GetDate: "+calendar.getTimeInMillis());
                     String date=AppUtils.dateParsingToyymmdd(calendar.getTime().toString());
                     String Month=AppUtils.dateParsingTomm(calendar.getTime().toString());
                     String Day=AppUtils.dateParsingToEEE(calendar.getTime().toString());
                     String Year=AppUtils.dateParsingToyyy(calendar.getTime().toString());
                     String numberDate=AppUtils.dateParsingTodd(calendar.getTime().toString());

                     DateModel model=new DateModel();
                     model.setDate(date);
                     model.setDay(Day);
                     model.setMonth(Month);
                     model.setYear(Year);
                     model.setNumberDate(numberDate);
                     model.setDateSelected(false);
                     model.setItemSelected(false);
                     mDateArrayList.add(model);


             }


                 if (mDateArrayList.size()==0)
                 {
                     AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "Please select any date", R.drawable.exclamationicon, R.drawable.round);

                 }
                 else
                 {
                     boolean isFound=false;
                     int foundPosition=-1;
                     for (int i=0;i<mDateArrayList.size();i++)
                     {
                         if (AppUtils.dateParsingTocurrent(current_date_time).equalsIgnoreCase(mDateArrayList.get(i).getDate()))
                         {
                             String timeExceed=callTimeExceedCheck();
                             if (timeExceed.equalsIgnoreCase("1"))
                             {
                                 isFound=true;
                                 foundPosition=i;
                             }

                         }
                     }
                     if (isFound)
                     {
                         AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "Sorry pre-ordering for the day closes at 7.30am .Please remove todays date", R.drawable.exclamationicon, R.drawable.round);

                     }
                     else {
                         dialog.dismiss();
                         Intent intent=new Intent(mContext,ItemListActivityNew.class);
                         intent.putExtra("dateArray",mDateArrayList);
                         intent.putExtra("tab_type","Pre-Order");
                         intent.putExtra("ordered_user_type",ordered_user_type);
                         intent.putExtra("student_id",student_id);
                         intent.putExtra("parent_id",parent_id);
                         intent.putExtra("staff_id",staff_id);
                         startActivity(intent);
                     }

                 }
                System.out.println("ArrayList size date"+mDateArrayList.size());


            }
        });

        dialog.show();

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
        } else {
            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

        }
    }
    private String callTimeExceedCheck()
    {

        VolleyWrapper volleyWrapper = new VolleyWrapper(URL_GET_TIME_EXCEED);
        String[] name = {"access_token"};
        String[] value = {PreferenceManager.getAccessToken(mContext)};
        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("The response is time exceed" + successResponse);
                try {
                    JSONObject obj = new JSONObject(successResponse);
                    String response_code = obj.getString(JTAG_RESPONSECODE);
                    if (response_code.equalsIgnoreCase("200")) {
                        JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                        String status_code = secobj.getString(JTAG_STATUSCODE);
                        if (status_code.equalsIgnoreCase("303")) {

                            String current_date_time=secobj.optString("current_date_time");
                         time_exceed=secobj.optString("time_exceed");


                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        callTimeExceedCheck();

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        callTimeExceedCheck();

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        callTimeExceedCheck();

                    } else {

                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    }
                } catch (Exception ex) {
                    System.out.println("The Exception in edit profile is" + ex.toString());
                }

            }

            @Override
            public void responseFailure(String failureResponse)
            {
                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

            }
        });

      return time_exceed;
    }
}
