package com.mobatia.naisapp.activities.canteen_new;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener;
import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.canteen_new.model.CartItemDateModel;
import com.mobatia.naisapp.activities.canteen_new.model.CartItemDetailModel;
import com.mobatia.naisapp.activities.canteen_new.model.DateModel;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.constants.IntentPassValueConstants;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NameValueConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.parents_evening.model.StudentModel;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.HeaderManager;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class PreOrderStaffActivity extends Activity implements URLConstants, StatusConstants, JSONConstants, IntentPassValueConstants, NameValueConstants {
    Context mContext = this;
    String tab_type;
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back, btn_history, home;
    Bundle extras;
    ImageView nextBtn;
    LinearLayout mStudentSpinner;
    static ArrayList<StudentModel> studentsModelArrayList = new ArrayList<>();
    static TextView studentName;
    TextView emailValueTxt,pickUpLocationValueTxt,classRoomValueTxt;
    String stud_id;
    String studClass = "";
    String orderId = "";
    static String stud_img = "";
    static ImageView studImg;
    ArrayList<String> studentList = new ArrayList<>();
    ImageView line1Img;
    ImageView cartBgImg,OrderClickImg;
    String ordered_user_type="";
    String student_id="";
    String staff_id="";
    String parent_id="";

    ImageView orderImg;
    TextView orderTxt,cartCountImg;
    ArrayList<CartItemDateModel> mDateArrayList;
    ArrayList<CartItemDetailModel> mCartItemArrayList;

    RelativeLayout addOrderRelative,orderHistoryRelative,myOrderRelative;
    int cartCount=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preorder_staff);
        mContext = this;
        initialiseUI();
        if (AppUtils.isNetworkConnected(mContext)) {
            getStaffInfo(URL_STAFF_INFO_FOR_CANTEEN);
        } else {
            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

        }
    }

    private void initialiseUI() {
        extras = getIntent().getExtras();
        if (extras != null) {
            tab_type = extras.getString("tab_type");
        }
        ordered_user_type="2";
        parent_id="";
        student_id="";
        staff_id=PreferenceManager.getStaffId(mContext);
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        headermanager = new HeaderManager(PreOrderStaffActivity.this, tab_type);
        headermanager.getHeader(relativeHeader, 6);
        back = headermanager.getLeftButton();
        btn_history = headermanager.getRightHistoryImage();
        btn_history.setVisibility(View.INVISIBLE);
        addOrderRelative=findViewById(R.id.addOrderRelative);
        myOrderRelative=findViewById(R.id.myOrderRelative);
        orderHistoryRelative = (RelativeLayout) findViewById(R.id.orderHistoryRelative);
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

        mStudentSpinner = findViewById(R.id.studentSpinner);
        studentName = findViewById(R.id.studentName);
        studentName = findViewById(R.id.studentName);
        studImg = findViewById(R.id.imagicon);

        //   nextBtn = findViewById(R.id.nextBtn);

//        cartBgImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent=new Intent(PreOrderStaffActivity.this,CartActivity.class);
//                intent.putExtra("ordered_user_type","2");
//                intent.putExtra("tab_type","Cart");
//                intent.putExtra("student_id","");
//                intent.putExtra("parent_id","");
//                intent.putExtra("staff_id",PreferenceManager.getStaffId(mContext));
//                startActivity(intent);
//            }
//        });

        btn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(PreOrderStaffActivity.this,ConfirmedOrderActivity.class);
                intent.putExtra("ordered_user_type","2");
                intent.putExtra("tab_type","History");
                intent.putExtra("student_id","");
                intent.putExtra("parent_id","");
                intent.putExtra("staff_id",PreferenceManager.getStaffId(mContext));
                startActivity(intent);
            }
        });

        addOrderRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ordered_user_type="2";
                student_id="";
                staff_id=PreferenceManager.getStaffId(mContext);
                parent_id="";
                showCalendarPopup(PreOrderStaffActivity.this,ordered_user_type,student_id,staff_id,parent_id);
            }
        });
        orderHistoryRelative.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(PreOrderStaffActivity.this,OrderHistoryActivity.class);
                intent.putExtra("ordered_user_type","2");
                intent.putExtra("tab_type","Order History");
                intent.putExtra("student_id","");
                intent.putExtra("parent_id","");
                intent.putExtra("staff_id",PreferenceManager.getStaffId(mContext));
                startActivity(intent);
            }
        });
        myOrderRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(PreOrderStaffActivity.this,MyOrderActivity.class);
                intent.putExtra("ordered_user_type","2");
                intent.putExtra("tab_type","My Orders");
                intent.putExtra("student_id","");
                intent.putExtra("parent_id","");
                intent.putExtra("staff_id",PreferenceManager.getStaffId(mContext));
                startActivity(intent);
            }
        });
    }
    public  void showCalendarPopup(Activity activity,String ordered_user_type,String student_id,String staff_id,String parent_id)
    {
        ArrayList<DateModel>mDateArrayList=new ArrayList<>();
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.show_calendar_popup);
        Button GetDate = (Button) dialog.findViewById(R.id.GetDate);
        ImageView closeImg=(ImageView)dialog.findViewById(R.id.imgClose);
        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDateArrayList.clear();
                dialog.dismiss();
            }
        });

        final CalendarView calendarView = dialog.findViewById(R.id.MCalendar);
        Calendar c = Calendar.getInstance();
        calendarView.setMinimumDate(c);

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
                for (Calendar calendar : calendarView.getSelectedDates()) {
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
                    dialog.dismiss();
                    Intent intent=new Intent(mContext,ItemListActivityNew.class);
                    intent.putExtra("dateArray",mDateArrayList);
                    intent.putExtra("ordered_user_type","2");
                    intent.putExtra("tab_type","Pre-Order");
                    intent.putExtra("student_id","");
                    intent.putExtra("parent_id","");
                    intent.putExtra("staff_id",PreferenceManager.getStaffId(mContext));
                    startActivity(intent);
                }
                System.out.println("ArrayList size date"+mDateArrayList.size());


            }
        });

        dialog.show();

    }

    public void getStaffInfo(final String URL)
    {

        try {

            //   mAbsenceListViewArray = new ArrayList<>();
            final VolleyWrapper manager = new VolleyWrapper(URL);
//            stud_id = studentsModelArrayList.get(0).getmId();
            String[] name = {JTAG_ACCESSTOKEN, "staff_id","portal"};
            String[] value = {PreferenceManager.getAccessToken(mContext), PreferenceManager.getStaffId(mContext),"1"};
            System.out.println("Values ::::" + PreferenceManager.getAccessToken(mContext) + "hfhgfhdfghd::::" + PreferenceManager.getStaffId(mContext));
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
                                        JSONObject dataObject=respObject.getJSONObject("data");
                                        String id=dataObject.optString("id");
                                        String name=dataObject.optString("name");
                                        String email=dataObject.optString("email");
                                        String pickup_location=dataObject.optString("pickup_location");
                                        String staff_photo=dataObject.optString("staff_photo");
                                        studentName.setText(name);

                                        if (!(staff_photo.equals(""))) {

                                            Picasso.with(mContext).load(AppUtils.replace(staff_photo)).placeholder(R.drawable.boy).fit().into(studImg);
                                        } else {

                                            studImg.setImageResource(R.drawable.boy);
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
                                    getStaffInfo(URL);

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
                  //  AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }



    @Override
    protected void onResume() {
        super.onResume();
        if (AppUtils.isNetworkConnected(mContext)) {
            getStaffInfo(URL_STAFF_INFO_FOR_CANTEEN);
        } else {
            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

        }
    }
}










