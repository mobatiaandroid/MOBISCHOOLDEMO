package com.mobatia.naisapp.fragments.report;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.mobatia.naisapp.R;
import com.mobatia.naisapp.constants.CacheDIRConstants;
import com.mobatia.naisapp.constants.IntentPassValueConstants;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.constants.NaisTabConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.report.adapter.RecyclerViewMainAdapter;
import com.mobatia.naisapp.fragments.report.adapter.StudentSpinnerAdapter;
import com.mobatia.naisapp.fragments.report.model.DataModel;
import com.mobatia.naisapp.fragments.report.model.ReportModel;
import com.mobatia.naisapp.fragments.report.model.StudentInfoModel;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.recyclerviewmanager.DividerItemDecoration;
import com.mobatia.naisapp.recyclerviewmanager.RecyclerItemListener;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReportFragment extends Fragment implements AdapterView.OnItemClickListener,
        NaisTabConstants,CacheDIRConstants, URLConstants,
        IntentPassValueConstants,NaisClassNameConstants,JSONConstants,StatusConstants {
    RelativeLayout mtitleRel;
    RelativeLayout alertTxtRelative;
    RelativeLayout CCAFRegisterRel;
    ArrayList<ReportModel> studentsModelArrayList;
    ListView studentDetail;
    TextView studentName;
    TextView textViewYear;
    static String stud_id = "";
    String stud_class = "";
    String stud_name = "";
    String stud_img="";
    String section="";
    TextView noDataTxt;
    ImageView noDataImg;
    LinearLayout mStudentSpinner;
    ImageView back;
    ImageView home;
    ImageView studImg;
    Bundle extras;
    private View mRootView;
    private static Context mContext;
    private String mTitle;
    private String mTabId;
    private RelativeLayout relMain;
    private ImageView mBannerImage;
    private static RecyclerView mRecycleView;
    private TextView alertText;
    ArrayList<String> studentList = new ArrayList<>();
    static RecyclerViewMainAdapter mRecyclerViewMainAdapter;
    public ReportFragment() {

    }

    public ReportFragment(String title, String tabId) {
        this.mTitle = title;
        this.mTabId = tabId;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.progress_report, container,
                false);
        mContext = getActivity();
      TextView  mTitleTextView= (TextView) mRootView.findViewById(R.id.titleTextView);
        mTitleTextView.setText(REPORT);
        initialiseUI();
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


        return mRootView;
    }

    private void initialiseUI() {
        if (extras != null) {
        }
//
        mStudentSpinner = mRootView.findViewById(R.id.studentSpinner);
        studentName = mRootView.findViewById(R.id.studentName);
        studImg = mRootView.findViewById(R.id.imagicon);
        mRecycleView = mRootView.findViewById(R.id.recycler_view_list);
        relMain = mRootView.findViewById(R.id.relMain);
        textViewYear = mRootView.findViewById(R.id.textViewYear);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleView.setLayoutManager(llm);
        relMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
private ReportModel addStudentDetails(JSONObject dataObject) {
    ReportModel studentModel = new ReportModel();
        studentModel.setmId(dataObject.optString(JTAG_ID));
        studentModel.setmName(dataObject.optString(JTAG_TAB_NAME));
        studentModel.setmClass(dataObject.optString(JTAG_TAB_CLASS));
        studentModel.setmSection(dataObject.optString(JTAG_TAB_SECTION));
        studentModel.setmHouse(dataObject.optString("house"));
        studentModel.setmPhoto(dataObject.optString("photo"));

        return studentModel;
    }
    private void getStudentsListAPI(final String URLHEAD)
    {
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
                            studentList.clear();

                            if (data.length() > 0) {
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject dataObject = data.getJSONObject(i);
                                    studentsModelArrayList.add(addStudentDetails(dataObject));
                                    studentList.add(studentsModelArrayList.get(i).getmName());
                                }
                                if (PreferenceManager.getStudIdForCCA(mContext).equalsIgnoreCase("")) {
                                    studentName.setText(studentsModelArrayList.get(0).getmName());
                                    stud_id = studentsModelArrayList.get(0).getmId();
                                    stud_name = studentsModelArrayList.get(0).getmName();
                                    stud_class = studentsModelArrayList.get(0).getmClass();

                                    section=studentsModelArrayList.get(0).getmSection();
                                    stud_img= studentsModelArrayList.get(0).getmPhoto();
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

                                    studentName.setText(studentsModelArrayList.get(studentSelectPosition).getmName());
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
                                    System.out.println("test working");
                                    getReportListAPI(URL_GET_STUDENT_REPORT);
                                } else {
                                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                                }


                            } else {
//                                mRecycleView.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
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
    private static void getReportListAPI(final String URLHEAD)
    {
        VolleyWrapper volleyWrapper = new VolleyWrapper(URLHEAD);
        String[] name = {"access_token","users_id","student_id"};
        String[] value = {PreferenceManager.getAccessToken(mContext),PreferenceManager.getUserId(mContext), stud_id};
        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("The response is student list progress" + successResponse);
                try {
//                    studentsModelArrayList = new ArrayList<>();//wrong
                    ArrayList<StudentInfoModel> mStudentModel=new ArrayList<>();

                    JSONObject obj = new JSONObject(successResponse);
                    String response_code = obj.getString(JTAG_RESPONSECODE);
                    if (response_code.equalsIgnoreCase("200")) {
                        JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                        String status_code = secobj.getString(JTAG_STATUSCODE);
                        if (status_code.equalsIgnoreCase("303")) {
                            JSONArray data = secobj.getJSONArray("responseArray");

                            if (data.length() > 0) {
                                mRecycleView.setVisibility(View.VISIBLE);
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject dataObject = data.getJSONObject(i);
                                    StudentInfoModel model = new StudentInfoModel();
                                    model.setAcyear(dataObject.optString("Acyear"));
                                    JSONArray list =dataObject.getJSONArray("data");
                                    ArrayList<DataModel> mDatamodel=new ArrayList<DataModel>();
                                    if(list.length()>0)
                                    {
                                        for(int x=0;x<list.length();x++)
                                        {
                                            JSONObject listObject= list.getJSONObject(x);
                                            DataModel xmodel=new DataModel();
                                            xmodel.setId(listObject.optString("id"));
                                            xmodel.setReporting_cycle(listObject.optString("reporting_cycle"));
                                            xmodel.setAcademic_year(listObject.optString("academic_year"));
                                            xmodel.setUpdated_on(listObject.optString("updated_on"));
                                            xmodel.setFile(listObject.optString("file"));
                                            xmodel.setStatus(listObject.optString("status"));
                                            mDatamodel.add(xmodel);


                                        }
                                    }

                                    model.setmDataModel(mDatamodel);
                                    mStudentModel.add(model);
                                    mRecycleView.setVisibility(View.VISIBLE);
                                    RecyclerViewMainAdapter mRecyclerViewMainAdapter=new RecyclerViewMainAdapter(mContext,mStudentModel);
                                    mRecycleView.setAdapter(mRecyclerViewMainAdapter);
                                }


                            } else {
                                RecyclerViewMainAdapter mRecyclerViewMainAdapter=new RecyclerViewMainAdapter(mContext,mStudentModel);
                                mRecycleView.setAdapter(mRecyclerViewMainAdapter);

                              Toast.makeText(mContext, "No data available", Toast.LENGTH_SHORT).show();
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

    public void showSocialmediaList(final ArrayList<ReportModel> mStudentArray) {
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
                        studentName.setText(mStudentArray.get(position).getmName());
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

                            mRecycleView.setVisibility(View.VISIBLE);
                            if (AppUtils.isNetworkConnected(mContext)) {
                                System.out.println("test working");
                                getReportListAPI(URL_GET_STUDENT_REPORT);
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




    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {


    }

    @Override
    public void onResume() {
        super.onResume();

    }

}

