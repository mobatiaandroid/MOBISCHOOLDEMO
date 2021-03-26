package com.mobatia.naisapp.activities.assessment_link;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.mobatia.naisapp.activities.assessment_link.adapter.AssessmentRecyclerviewAdapter;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.parents_evening.model.StudentModel;
import com.mobatia.naisapp.fragments.sports.adapter.StrudentSpinnerAdapter;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.HeaderManager;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.recyclerviewmanager.DividerItemDecoration;
import com.mobatia.naisapp.recyclerviewmanager.ItemOffsetDecoration;
import com.mobatia.naisapp.recyclerviewmanager.RecyclerItemListener;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by gayatri on 28/3/17.
 */
public class AssessmentLinkActivity extends Activity implements URLConstants, JSONConstants,NaisClassNameConstants {
    Bundle extras;
    private Context mContext = this;
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back;
    ImageView home;
    ArrayList<StudentModel> studentsModelArrayList = new ArrayList<>();
    ArrayList<AssementModel> mAssementModelArrayList = new ArrayList<>();
    ArrayList<String> studentList = new ArrayList<>();
    LinearLayout mStudentSpinner;
    String stud_id = "";
    TextView studentName;
    AssessmentRecyclerviewAdapter mAssessmentRecyclerviewAdapter;
    RecyclerView.LayoutManager recyclerViewLayoutManager;
    RecyclerView recycler_ListView;
    String tab_type="";
    String tabKey="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assentmentlink_activity);
        initUI();
        if(AppUtils.isNetworkConnected(mContext)) {
            getStudentsListAPI(URL_GET_STUDENT_LIST);
        }else{
            AppUtils.showDialogAlertDismiss((Activity)mContext,"Network Error",getString(R.string.no_internet),R.drawable.nonetworkicon,R.drawable.roundred);

        }
    }

    private void initUI() {
        extras = getIntent().getExtras();
        if (extras != null) {
            tab_type = extras.getString("tab_type");
        }
        if (tab_type!=null) {
            if (tab_type.equalsIgnoreCase(EARLY_YEARS)) {
                tabKey = "early";
            } else if (tab_type.equalsIgnoreCase(PRIMARY)) {
                tabKey = "primary";

            } else if (tab_type.equalsIgnoreCase(SECONDARY)) {
                tabKey = "secondary";

            } else if (tab_type.equalsIgnoreCase(IB_PROGRAMME)) {
                tabKey = "ib_programms";

            }
        }

        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
         mStudentSpinner = (LinearLayout) findViewById(R.id.studentSpinner);
       studentName = (TextView) findViewById(R.id.studentName);
       headermanager = new HeaderManager(AssessmentLinkActivity.this,tab_type);
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

    recycler_ListView = (RecyclerView) findViewById(R.id.mListView);
        recycler_ListView.setHasFixedSize(true);
    recyclerViewLayoutManager = new GridLayoutManager(mContext, 1);
    int spacing = 10; // 50px
    ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext,spacing);
     recycler_ListView.addItemDecoration(
            new DividerItemDecoration(mContext.getResources().getDrawable(R.drawable.list_divider)));
        recycler_ListView.addItemDecoration(itemDecoration);
        recycler_ListView.setLayoutManager(recyclerViewLayoutManager);

        recycler_ListView.addOnItemTouchListener(new RecyclerItemListener(mContext, recycler_ListView,
                                                                         new RecyclerItemListener.RecyclerTouchListener() {
        public void onClickItem(View v, int position) {
            if (mAssementModelArrayList.size()>0)
            {
                if (mAssementModelArrayList.get(position).getmAssementModelArrayList().size()>0)
                {
                    Intent intent = new Intent(mContext, AssessmentSubListActivity.class);
                    intent.putExtra("detail", mAssementModelArrayList.get(position).getmAssementModelArrayList());
                    intent.putExtra("tab_type",tab_type);
                    startActivity(intent);
                }else
                {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "No Data Available", R.drawable.exclamationicon, R.drawable.round);

                }

            }
            else
            {
                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "No Data Available", R.drawable.exclamationicon, R.drawable.round);

            }

        }

    public void onLongClickItem(View v, int position) {
    }
}));

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
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject dataObject = data.getJSONObject(i);
                                    studentsModelArrayList.add(addStudentDetails(dataObject));
                                    studentList.add(studentsModelArrayList.get(i).getmName());
                                }
                                studentName.setText(studentsModelArrayList.get(0).getmName());
                                stud_id = studentsModelArrayList.get(0).getmId();
                                getAssessmentListAPI();


                            } else {
                                Toast.makeText(AssessmentLinkActivity.this, "No student data found", Toast.LENGTH_SHORT).show();
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
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    }
                } catch (Exception ex) {

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
                        getAssessmentListAPI();
                    }

                    public void onLongClickItem(View v, int position) {
                    }
                }));
        dialog.show();
    }

    private void getAssessmentListAPI() {
        mAssementModelArrayList=new ArrayList<>();
        VolleyWrapper volleyWrapper = new VolleyWrapper(URL_ASSESSMENT_LINK_LIST);//student_id (ex:2)
        String[] name = {"access_token", "student_id","department"};
        String[] value = {PreferenceManager.getAccessToken(mContext), stud_id,tabKey};
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
                                //studentsModelArrayList.add(0,);
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject dataObject = data.getJSONObject(i);
                                    mAssementModelArrayList.add(addAssessment(dataObject));
                                }
                                 mAssessmentRecyclerviewAdapter=new AssessmentRecyclerviewAdapter(mContext,mAssementModelArrayList);
                                recycler_ListView.setAdapter(mAssessmentRecyclerviewAdapter);

                            } else {
                                mAssessmentRecyclerviewAdapter=new AssessmentRecyclerviewAdapter(mContext,mAssementModelArrayList);
                                recycler_ListView.setAdapter(mAssessmentRecyclerviewAdapter);
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "No Assessment Links available for "+studentName.getText().toString(), R.drawable.exclamationicon, R.drawable.round);


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
                        getAssessmentListAPI();

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        getAssessmentListAPI();

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        getAssessmentListAPI();

                    } else {

                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    }
                } catch (Exception ex) {
                }

            }

            @Override
            public void responseFailure(String failureResponse) {

                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

            }
        });


    }
    private AssementModel addAssessment(JSONObject dataObject) {
        AssementModel mAssementModel = new AssementModel();
        mAssementModel.setAssessmentDate(dataObject.optString("date"));
        JSONArray detailsArray=dataObject.optJSONArray("details");
        ArrayList<AssementModel>mAssessmentDetail=new ArrayList<>();
        for (int j=0;j<detailsArray.length();j++)
        {
            JSONObject detailObject=detailsArray.optJSONObject(j);
            AssementModel mAssementDetailModel = new AssementModel();
            mAssementDetailModel.setDescription(detailObject.optString("description"));
            mAssementDetailModel.setTitle(detailObject.optString("title"));
            mAssementDetailModel.setType(detailObject.optString("type"));
            mAssessmentDetail.add(mAssementDetailModel);
        }
        mAssementModel.setmAssementModelArrayList(mAssessmentDetail);

        return mAssementModel;
    }

}
