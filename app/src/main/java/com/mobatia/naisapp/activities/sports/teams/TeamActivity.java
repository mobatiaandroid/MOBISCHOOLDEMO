package com.mobatia.naisapp.activities.sports.teams;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.activities.sports.teams.adapter.StudentSpinnerAdapter;
import com.mobatia.naisapp.activities.sports.teams.adapter.TeamEventRecyclerClickAdapter;
import com.mobatia.naisapp.activities.sports.teams.adapter.TeamRecyclerAdapter;
import com.mobatia.naisapp.activities.sports.teams.model.TeamDatesModel;
import com.mobatia.naisapp.activities.sports.teams.model.TeamEventClickModel;
import com.mobatia.naisapp.activities.sports.teams.model.TeamEventModel;
import com.mobatia.naisapp.activities.sports.teams.model.TeamModel;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.constants.ResultConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
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

public class TeamActivity extends Activity
        implements JSONConstants,URLConstants,ResultConstants,StatusConstants,NaisClassNameConstants {
    private static Context mContext;
    Bundle extras;
    String tab_type;
    RelativeLayout relativeHeader;
    LinearLayout mStudentSpinner;
    ImageView studImg;
    TextView studName;
    RecyclerView mnewsLetterListView;
    ArrayList<TeamModel> studentsModelArrayList;
    TextView textViewYear;
    static String stud_id = "";
    String stud_class = "";
    String stud_name = "";
    String stud_img="";
    String section="";
    ArrayList<TeamEventModel> newsLetterModelArrayList=new ArrayList<>();
    ArrayList<TeamEventClickModel> teamEventClickArrayList=new ArrayList<>();
    ArrayList<TeamDatesModel> teamDatesModelArrayList=new ArrayList<>();
    HeaderManager headermanager;
    ImageView back;
    ImageView home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);
        mContext=this;
        initUI();
        mStudentSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSocialmediaList(studentsModelArrayList);
            }
        });
        if (AppUtils.isNetworkConnected(mContext)) {
            getStudentsListAPI(URL_GET_STUDENT_LIST);
        } else {
            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

        }

    }

   private void initUI()
   {
       extras = getIntent().getExtras();
       if (extras != null) {
           tab_type=extras.getString("tab_type");
       }
       relativeHeader=(RelativeLayout)findViewById(R.id.relativeHeader);
       mStudentSpinner=(LinearLayout) findViewById(R.id.studentSpinner);
       studImg=(ImageView) findViewById(R.id.imagicon);
       studName=(TextView) findViewById(R.id.studentName);
       textViewYear=(TextView) findViewById(R.id.textViewYear);
       mnewsLetterListView=(RecyclerView) findViewById(R.id.mnewsLetterListView);
       mnewsLetterListView.setHasFixedSize(true);
       mnewsLetterListView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.list_divider)));
       headermanager = new HeaderManager(TeamActivity.this, tab_type);
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
       LinearLayoutManager llm = new LinearLayoutManager(this);
       llm.setOrientation(LinearLayoutManager.VERTICAL);
       mnewsLetterListView.setLayoutManager(llm);
       mnewsLetterListView.addOnItemTouchListener(new RecyclerItemListener(getApplicationContext(), mnewsLetterListView,
               new RecyclerItemListener.RecyclerTouchListener() {
                   public void onClickItem(View v, int position) {
///srk
                            getDatePopUpListAPI(URL_GET_SPORTS_TEAMS_CLICK_LIST,position,newsLetterModelArrayList.get(position).getTeamName());
                            /////
                       }

                   public void onLongClickItem(View v, int position) {
                       System.out.println("On Long Click Item interface");
                   }
               }));
   }
    private TeamModel addStudentDetails(JSONObject dataObject) {
        TeamModel studentModel = new TeamModel();
        studentModel.setmId(dataObject.optString(JTAG_ID));
        studentModel.setmName(dataObject.optString(JTAG_TAB_NAME));
        studentModel.setmClass(dataObject.optString(JTAG_TAB_CLASS));
        studentModel.setmSection(dataObject.optString(JTAG_TAB_SECTION));
        studentModel.setmHouse(dataObject.optString("house"));
        studentModel.setmPhoto(dataObject.optString("photo"));

        return studentModel;
    }
    private void getStudentsListAPI(final String URLHEAD) {
        VolleyWrapper volleyWrapper = new VolleyWrapper(URLHEAD);
        String[] name = {"access_token", "users_id"};
        String[] value = {PreferenceManager.getAccessToken(mContext), PreferenceManager.getUserId(mContext)};
        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("The response is student list" + successResponse);
                try {
                    studentsModelArrayList = new ArrayList<>();
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
                                    studentsModelArrayList.add(addStudentDetails(dataObject));
                                }
                                if (PreferenceManager.getStudIdForCCA(mContext).equalsIgnoreCase("")) {
                                    studName.setText(studentsModelArrayList.get(0).getmName());
                                    stud_id = studentsModelArrayList.get(0).getmId();
                                    stud_name = studentsModelArrayList.get(0).getmName();
                                    stud_class = studentsModelArrayList.get(0).getmClass();
                                    stud_img= studentsModelArrayList.get(0).getmPhoto();
                                    section=studentsModelArrayList.get(0).getmSection();
                                    if (!(stud_img.equals(""))) {

                                        Picasso.with(mContext).load(AppUtils.replace(stud_img)).placeholder(R.drawable.boy).fit().into(studImg);
                                    }
                                    else

                                    {

                                        studImg.setImageResource(R.drawable.boy);
                                    }


                                    textViewYear.setText("Class : " + studentsModelArrayList.get(0).getmClass());

                                    PreferenceManager.setCCAStudentIdPosition(mContext, "0");

                                }
                                else
                                {

                                    int studentSelectPosition=Integer.valueOf(PreferenceManager.getCCAStudentIdPosition(mContext));

                                    studName.setText(studentsModelArrayList.get(studentSelectPosition).getmName());
                                    stud_id = studentsModelArrayList.get(studentSelectPosition).getmId();
                                    stud_name = studentsModelArrayList.get(studentSelectPosition).getmName();
                                    stud_class = studentsModelArrayList.get(studentSelectPosition).getmClass();
                                    stud_img= studentsModelArrayList.get(studentSelectPosition).getmPhoto();
                                    System.out.println("selected student image"+studentsModelArrayList.get(studentSelectPosition).getmPhoto());
                                    if (!(stud_img.equals(""))) {

                                        Picasso.with(mContext).load(AppUtils.replace(stud_img)).placeholder(R.drawable.boy).fit().into(studImg);
                                    }
                                    else

                                    {

                                        studImg.setImageResource(R.drawable.boy);
                                    }
                                    textViewYear.setText("Class : " + studentsModelArrayList.get(studentSelectPosition).getmClass());


                                }
                                if (AppUtils.isNetworkConnected(mContext)) {
                                    getReportListAPI(URL_GET_SPORTS_TEAMS_LIST);
                                } else {
                                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                                }


                            } else {
//                                mRecycleView.setVisibility(View.GONE);
                                Toast.makeText(mContext, "No data found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                        //AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

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
                        //AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    }
                } catch (Exception ex) {
                    System.out.println("The Exception in edit profile is" + ex.toString());
                }

            }

            @Override
            public void responseFailure(String failureResponse) {


            }
        });


    }
    private  void getReportListAPI(final String URLHEAD)
    {
        newsLetterModelArrayList.clear();

        VolleyWrapper volleyWrapper = new VolleyWrapper(URLHEAD);
        String[] name = {"access_token","student_id"};
        String[] value = {PreferenceManager.getAccessToken(mContext), stud_id};
        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("The response is student list progress" + successResponse);
                try {

                    JSONObject obj = new JSONObject(successResponse);
                    String response_code = obj.getString(JTAG_RESPONSECODE);
                    if (response_code.equalsIgnoreCase("200")) {
                        JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                        String status_code = secobj.getString(JTAG_STATUSCODE);
                        if (status_code.equalsIgnoreCase("303")) {
                            JSONArray data = secobj.getJSONArray("details");
                            newsLetterModelArrayList= new ArrayList<>();

                            if (data.length() > 0) {

                                mnewsLetterListView.setVisibility(View.VISIBLE);
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject dataObject = data.getJSONObject(i);

                                    newsLetterModelArrayList.add(addEventsDetails(dataObject));
                                }

                                TeamRecyclerAdapter newsLetterAdapter = new TeamRecyclerAdapter(mContext, newsLetterModelArrayList);
                                mnewsLetterListView.setAdapter(newsLetterAdapter);
                            } else {
                                mnewsLetterListView.setVisibility(View.GONE);

                                Toast.makeText(mContext, "No data found.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                        // AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        getReportListAPI(URLHEAD);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        getReportListAPI(URLHEAD);

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        getReportListAPI(URLHEAD);

                    } else {


                    }
                } catch (Exception ex) {
                    System.out.println("The Exception in edit profile is" + ex.toString());
                }

            }

            @Override
            public void responseFailure(String failureResponse) {

            }
        });


    }
    private TeamEventModel addEventsDetails(JSONObject obj) {
        TeamEventModel model = new TeamEventModel();
        try {
            model.setId(obj.optString(JTAG_ID));
           model.setTeamName(obj.optString(JTAG_TAB_NAME));

        } catch (Exception ex) {
            System.out.println("Exception is" + ex);
        }

        return model;
    }
 /*   private TeamEventClickModel addEventsPopUpDetails(JSONObject obj) {
        TeamEventClickModel model = new TeamEventClickModel();
        try {
            //model.setId(obj.optString(JTAG_ID));
           // model.setTeamName(obj.optString(JTAG_TAB_NAME));

        } catch (Exception ex) {
            System.out.println("Exception is" + ex);
        }

        return model;
    }
*/
    public void showSocialmediaList(final ArrayList<TeamModel> mStudentArray) {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_student_list);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button dialogDismiss = dialog.findViewById(R.id.btn_dismiss);
        ImageView iconImageView = dialog.findViewById(R.id.iconImageView);
        iconImageView.setImageResource(R.drawable.boy);
        RecyclerView socialMediaList = dialog.findViewById(R.id.recycler_view_social_media);
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

        StudentSpinnerAdapter studentAdapter = new StudentSpinnerAdapter(mContext, mStudentArray);
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
                        studName.setText(mStudentArray.get(position).getmName());
                        stud_id = mStudentArray.get(position).getmId();
                        stud_name = mStudentArray.get(position).getmName();
                        stud_class = mStudentArray.get(position).getmClass();
                        stud_img=mStudentArray.get(position).getmPhoto();
                        section=mStudentArray.get(position).getmSection();
                        textViewYear.setText("Class : " + mStudentArray.get(position).getmClass());
                        if (!(stud_img.equals(""))) {

                            Picasso.with(mContext).load(AppUtils.replace(stud_img)).placeholder(R.drawable.boy).fit().into(studImg);
                        }
                        else

                        {

                            studImg.setImageResource(R.drawable.boy);
                        }
                        if (AppUtils.isNetworkConnected(mContext)) {
                            System.out.println("test working");
                            getReportListAPI(URL_GET_SPORTS_TEAMS_LIST);
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


    /////////////////////////////sharookh(Training Dates)

    private  void getDatePopUpListAPI(final String URLHEAD, final int position, final String teamName)
    {
        VolleyWrapper volleyWrapper = new VolleyWrapper(URLHEAD);
        String[] name = {"access_token","group_id"};
        String[] value = {PreferenceManager.getAccessToken(mContext),newsLetterModelArrayList.get(position).getId()};
        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("The response is team event click" + successResponse);
                try {
//                    studentsModelArrayList = new ArrayList<>();//wrong
                    ArrayList<TeamEventClickModel> mStudentModel=new ArrayList<>();

                    JSONObject obj = new JSONObject(successResponse);
                    String response_code = obj.getString(JTAG_RESPONSECODE);
                    if (response_code.equalsIgnoreCase("200")) {
                        JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                        String status_code = secobj.getString(JTAG_STATUSCODE);
                        if (status_code.equalsIgnoreCase("303")) {
                            JSONArray data = secobj.getJSONArray("details");

                            if (data.length() > 0) {
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject dataObject = data.getJSONObject(i);
                                    TeamEventClickModel model = new TeamEventClickModel();
                                    model.setDay(dataObject.optString("day"));
                                    JSONArray list =dataObject.getJSONArray("dates");
                                    ArrayList<TeamDatesModel> mDatamodel=new ArrayList<TeamDatesModel>();
                                    if(list.length()>0)
                                    {
                                        for(int x=0;x<list.length();x++)
                                        {
                                            JSONObject listObject= list.getJSONObject(x);
                                            TeamDatesModel xmodel=new TeamDatesModel();
                                            xmodel.setDate(listObject.optString("date"));
                                            String str = AppUtils.dateParsingToEEEEyyyyMMdd(listObject.optString("date"));
                                            String[] splitStr = str.trim().split("\\s+");
                                            xmodel.setDayStringDate(splitStr[0]);
                                            xmodel.setDayDate(splitStr[1]);
                                            xmodel.setMonthDate(splitStr[2]);
                                            xmodel.setYearDate(splitStr[3]);
                                            xmodel.setTime(listObject.optString("time"));
                                            xmodel.setEvent(listObject.optString("group_name"));
                                            xmodel.setId(listObject.optString("group_id"));
                                            xmodel.setVenue(listObject.optString("venue"));
                                            xmodel.setStart_time(listObject.optString("start_time"));
                                            xmodel.setEnd_time(listObject.optString("end_time"));
                                            mDatamodel.add(xmodel);


                                        }
                                    }

                                    model.setTeamDatesModel(mDatamodel);
                                    mStudentModel.add(model);


                                }
                                showSocialmediaListPopUp(mStudentModel,teamName);

                            }
                            else {
                                 AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "No Data Found", R.drawable.exclamationicon, R.drawable.round);


                            }
                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                        // AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        getReportListAPI(URLHEAD);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        getReportListAPI(URLHEAD);

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        getReportListAPI(URLHEAD);

                    } else {


                    }
                } catch (Exception ex) {
                    System.out.println("The Exception in edit profile is" + ex.toString());
                }

            }

            @Override
            public void responseFailure(String failureResponse) {

            }
        });


    }

/////////////////////////////////sharookh
    public static void showSocialmediaListPopUp(final ArrayList<TeamEventClickModel> mTeamArray,String teamname) {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_team_event_click);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button dialogDismiss = (Button) dialog.findViewById(R.id.btn_dismiss);
        ImageView iconImageView = (ImageView) dialog.findViewById(R.id.iconImageView);
        TextView alertHead = (TextView) dialog.findViewById(R.id.alertHead);
        iconImageView.setImageResource(R.drawable.teams_popup);
        alertHead.setText(teamname);
        RecyclerView socialMediaList = (RecyclerView) dialog.findViewById(R.id.recycler_view_social_media);
        //if(mSocialMediaArray.get())
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            dialogDismiss.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.button));

        } else {
            dialogDismiss.setBackground(mContext.getResources().getDrawable(R.drawable.button));

        }

        socialMediaList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        socialMediaList.setLayoutManager(llm);

        TeamEventRecyclerClickAdapter mTeamEventRecyclerAdapter= new TeamEventRecyclerClickAdapter(mContext,mTeamArray);
        socialMediaList.setAdapter(mTeamEventRecyclerAdapter);
        //TeamE studentAdapter = new StrudentSpinnerAdapter(mContext, mTeamArray);
       // socialMediaList.setAdapter(studentAdapter);
        dialogDismiss.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                dialog.dismiss();

            }

        });

        dialog.show();
    }
}
