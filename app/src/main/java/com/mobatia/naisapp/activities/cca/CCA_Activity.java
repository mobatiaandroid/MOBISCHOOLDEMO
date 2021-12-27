package com.mobatia.naisapp.activities.cca;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.cca.adapter.CCAsListActivityAdapter;
import com.mobatia.naisapp.activities.cca.model.CCADetailModel;
import com.mobatia.naisapp.activities.cca.model.CCAModel;
import com.mobatia.naisapp.activities.cca.model.CCAchoiceModel;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.cca.CcaFragmentMain;
import com.mobatia.naisapp.fragments.parents_evening.model.StudentModel;
import com.mobatia.naisapp.fragments.sports.adapter.StrudentSpinnerAdapter;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.HeaderManager;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.recyclerviewmanager.DividerItemDecoration;
import com.mobatia.naisapp.recyclerviewmanager.ItemOffsetDecoration;
import com.mobatia.naisapp.recyclerviewmanager.RecyclerItemListener;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CCA_Activity extends Activity implements URLConstants, JSONConstants ,StatusConstants {

    Context mContext;
    ArrayList<StudentModel> studentsModelArrayList;
    ArrayList<CCAModel> mCCAmodelArrayList;
    ArrayList<CCADetailModel> CCADetailModelArrayList;
    ArrayList<CCAchoiceModel> CCAchoiceModelArrayList;
    ArrayList<CCAchoiceModel> CCAchoiceModelArrayList2;
    TextView studentName;
    TextView textViewYear;
    TextView enterTextView;
    String stud_id = "";
    String stud_class = "";
    String stud_name = "";
    LinearLayout mStudentSpinner;
    HeaderManager headermanager;
    RelativeLayout relativeHeader;
    ImageView back;
    ImageView studImg;
    String stud_img="";

    ImageView home;
    String tab_type = "EAP Options";
    Bundle extras;
    RecyclerView recycler_review;
    GridLayoutManager recyclerViewLayoutManager;
    CCAsListActivityAdapter mCCAsActivityAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cca);
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

    public void initUI() {
        mContext = this;
        extras = getIntent().getExtras();
        if (extras != null) {
            tab_type = extras.getString("tab_type");
        }
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        recycler_review = (RecyclerView) findViewById(R.id.recycler_view_cca);
        recycler_review.setHasFixedSize(true);
        recyclerViewLayoutManager = new GridLayoutManager(mContext, 1);
        int spacing = 5; // 50px
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext, spacing);
        recycler_review.addItemDecoration(
                new DividerItemDecoration(mContext.getResources().getDrawable(R.drawable.list_divider)));
        recycler_review.addItemDecoration(itemDecoration);
        recycler_review.setLayoutManager(recyclerViewLayoutManager);
        headermanager = new HeaderManager(CCA_Activity.this, tab_type);
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
        studImg = findViewById(R.id.imagicon);

        mStudentSpinner = (LinearLayout) findViewById(R.id.studentSpinner);
        studentName = (TextView) findViewById(R.id.studentName);
        enterTextView = (TextView) findViewById(R.id.enterTextView);
        textViewYear = (TextView) findViewById(R.id.textViewYear);
        recycler_review.addOnItemTouchListener(new RecyclerItemListener(getApplicationContext(), recycler_review,
                new RecyclerItemListener.RecyclerTouchListener() {
                    public void onClickItem(View v, int position) {
                        if(mCCAmodelArrayList.get(position).getStatus().equalsIgnoreCase("0")||mCCAmodelArrayList.get(position).getStatus().equalsIgnoreCase("2"))
                        {
                            callStatusChangeApi(URL_GET_STATUS_CHANGE_API,mCCAmodelArrayList.get(position).getCca_days_id(),position,mCCAmodelArrayList.get(position).getStatus());

                        }
                        if (mCCAmodelArrayList.get(position).getIsAttendee().equalsIgnoreCase("0")) {
                            if (mCCAmodelArrayList.get(position).getIsSubmissionDateOver().equalsIgnoreCase("0")) {
                                if (mCCAmodelArrayList.get(position).getDetails().size() > 0) {
                                    Intent intent = new Intent(mContext, CCASelectionActivity.class);
                                    intent.putExtra("CCA_Detail", mCCAmodelArrayList.get(position).getDetails());
                                    intent.putExtra("tab_type", tab_type);
                                    PreferenceManager.setStudIdForCCA(mContext, stud_id);
                                    PreferenceManager.setStudNameForCCA(mContext, stud_name);
                                    PreferenceManager.setStudClassForCCA(mContext, stud_class);
                                    PreferenceManager.setCCATitle(mContext, mCCAmodelArrayList.get(position).getTitle());
                                    PreferenceManager.setCCAItemId(mContext, mCCAmodelArrayList.get(position).getCca_days_id());
                                    startActivity(intent);
                                } else {
                                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "No data available", R.drawable.exclamationicon, R.drawable.round);

                                }
                            } else {

                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "CCA Sign-Up Closed", R.drawable.exclamationicon, R.drawable.round);

                            }
                        } else if (mCCAmodelArrayList.get(position).getIsAttendee().equalsIgnoreCase("2")) {
                            Intent intent = new Intent(mContext, CCAsReviewEditAfterSubmissionActivity.class);
                            intent.putExtra("tab_type", tab_type);
                            intent.putExtra("CCA_Detail", mCCAmodelArrayList.get(position).getDetails());

                            intent.putExtra("submissiondateover", mCCAmodelArrayList.get(position).getIsSubmissionDateOver());
                            PreferenceManager.setStudIdForCCA(mContext, stud_id);
                            PreferenceManager.setStudNameForCCA(mContext, stud_name);
                            PreferenceManager.setStudClassForCCA(mContext, stud_class);
                            PreferenceManager.setCCATitle(mContext, mCCAmodelArrayList.get(position).getTitle());
                            PreferenceManager.setCCAItemId(mContext, mCCAmodelArrayList.get(position).getCca_days_id());
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(mContext, CCAsReviewAfterSubmissionActivity.class);
                            intent.putExtra("tab_type", tab_type);
                            PreferenceManager.setStudIdForCCA(mContext, stud_id);
                            PreferenceManager.setStudNameForCCA(mContext, stud_name);
                            PreferenceManager.setStudClassForCCA(mContext, stud_class);
                            PreferenceManager.setCCATitle(mContext, mCCAmodelArrayList.get(position).getTitle());
                            PreferenceManager.setCCAItemId(mContext, mCCAmodelArrayList.get(position).getCca_days_id());
                            startActivity(intent);
                        }

                    }

                    public void onLongClickItem(View v, int position)
                    {
                           
                    }
                }));
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

    private void getStudentsListAPI(final String URLHEAD) {
        VolleyWrapper volleyWrapper = new VolleyWrapper(URLHEAD);
        String[] name = {"access_token", "users_id"};
        String[] value = {PreferenceManager.getAccessToken(mContext), PreferenceManager.getUserId(mContext)};
        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("The response is" + successResponse);
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
                                    studentName.setText(studentsModelArrayList.get(0).getmName());
                                    stud_id = studentsModelArrayList.get(0).getmId();
                                    stud_name = studentsModelArrayList.get(0).getmName();
                                    stud_class = studentsModelArrayList.get(0).getmClass();
                                    textViewYear.setText("Class : " + studentsModelArrayList.get(0).getmClass());
                                    stud_img= studentsModelArrayList.get(0).getmPhoto();

                                    if (!(stud_img.equals(""))) {

                                        Picasso.with(mContext).load(AppUtils.replace(stud_img)).placeholder(R.drawable.boy).fit().into(studImg);
                                    }
                                    else

                                    {

                                        studImg.setImageResource(R.drawable.boy);
                                    }

                                    PreferenceManager.setCCAStudentIdPosition(mContext, "0");
                                } else {
                                    int studentSelectPosition = Integer.valueOf(PreferenceManager.getCCAStudentIdPosition(mContext));

                                    studentName.setText(studentsModelArrayList.get(studentSelectPosition).getmName());
                                    stud_id = studentsModelArrayList.get(studentSelectPosition).getmId();
                                    stud_name = studentsModelArrayList.get(studentSelectPosition).getmName();
                                    stud_class = studentsModelArrayList.get(studentSelectPosition).getmClass();
                                    textViewYear.setText("Class : " + studentsModelArrayList.get(studentSelectPosition).getmClass());
                                    stud_img= studentsModelArrayList.get(studentSelectPosition).getmPhoto();

                                    if (!(stud_img.equals(""))) {

                                        Picasso.with(mContext).load(AppUtils.replace(stud_img)).placeholder(R.drawable.boy).fit().into(studImg);
                                    }
                                    else

                                    {

                                        studImg.setImageResource(R.drawable.boy);
                                    }


                                }
                                getCCAListAPI(stud_id);


                            } else {
                                Toast.makeText(CCA_Activity.this, "No Student found", Toast.LENGTH_SHORT).show();
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

    public void showSocialmediaList(final ArrayList<StudentModel> mStudentArray) {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogue_student_list);
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
                        stud_name = mStudentArray.get(position).getmName();
                        stud_class = mStudentArray.get(position).getmClass();
                        stud_img=mStudentArray.get(position).getmPhoto();
                        textViewYear.setText("Class : " + mStudentArray.get(position).getmClass());
                        if (!(stud_img.equals(""))) {

                            Picasso.with(mContext).load(AppUtils.replace(stud_img)).placeholder(R.drawable.boy).fit().into(studImg);
                        }
                        else

                        {

                            studImg.setImageResource(R.drawable.boy);
                        }
                        PreferenceManager.setCCAStudentIdPosition(mContext, position + "");
                        getCCAListAPI(stud_id);

                    }

                    public void onLongClickItem(View v, int position) {
                    }
                }));
        dialog.show();
    }

    private void getCCAListAPI(final String studentId) {
        VolleyWrapper volleyWrapper = new VolleyWrapper(URL_CCA_DETAILS);
        String[] name = {"access_token", "student_id", "users_id"};//user_id added in phase3
        String[] value = {PreferenceManager.getAccessToken(mContext), studentId, PreferenceManager.getUserId(mContext)};
        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("The response is" + successResponse);
                try {
                    mCCAmodelArrayList = new ArrayList<>();
                    JSONObject obj = new JSONObject(successResponse);
                    String response_code = obj.getString(JTAG_RESPONSECODE);
                    if (response_code.equalsIgnoreCase("200")) {
                        JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                        String status_code = secobj.getString(JTAG_STATUSCODE);
                        if (status_code.equalsIgnoreCase("303")) {
                            JSONArray data = secobj.optJSONArray("data");
                            if (data.length() > 0) {
                                for (int i = 0; i < data.length(); i++) {
                                    enterTextView.setVisibility(View.VISIBLE);
                                    JSONObject dataObject = data.getJSONObject(i);
                                    mCCAmodelArrayList.add(addCCAlist(dataObject));
                                }
                                if (mCCAmodelArrayList.size() > 0) {
                                     mCCAsActivityAdapter = new CCAsListActivityAdapter(CCA_Activity.this, mCCAmodelArrayList);
                                    recycler_review.setAdapter(mCCAsActivityAdapter);
                                }

                            } else {
                                mCCAsActivityAdapter = new CCAsListActivityAdapter(CCA_Activity.this, mCCAmodelArrayList);
                                recycler_review.setAdapter(mCCAsActivityAdapter);
                                enterTextView.setVisibility(View.GONE);
                                Toast.makeText(CCA_Activity.this, "No CCA available", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                                getCCAListAPI(studentId);
                            }
                        });
                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                                getCCAListAPI(studentId);
                            }
                        });
                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                                getCCAListAPI(studentId);
                            }
                        });
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

    private CCAModel addCCAlist(JSONObject dataObject) {
        CCAModel mCCAModel = new CCAModel();
        mCCAModel.setCca_days_id(dataObject.optString("cca_days_id"));
        mCCAModel.setTitle(dataObject.optString("title"));
        mCCAModel.setFrom_date(dataObject.optString("from_date"));
        mCCAModel.setTo_date(dataObject.optString("to_date"));
        mCCAModel.setIsAttendee(dataObject.optString("isAttendee"));
        mCCAModel.setStatus(dataObject.optString("status"));
        mCCAModel.setSubmission_dateTime(dataObject.optString("submission_dateTime"));
        mCCAModel.setIsSubmissionDateOver(dataObject.optString("isSubmissiondateOver"));
        JSONArray jsonCCADetailArray = dataObject.optJSONArray("details");
        CCADetailModelArrayList = new ArrayList<>();
        if (jsonCCADetailArray.length() > 0) {
            for (int i = 0; i < jsonCCADetailArray.length(); i++) {
                JSONObject objectCCA = jsonCCADetailArray.optJSONObject(i);
                CCADetailModel mCCADetailModel = new CCADetailModel();
                mCCADetailModel.setDay(objectCCA.optString("day"));
                JSONArray jsonCCAChoiceArray = objectCCA.optJSONArray("choice1");
                JSONArray jsonCCAChoiceArray2 = objectCCA.optJSONArray("choice2");

                CCAchoiceModelArrayList = new ArrayList<>();
                if (jsonCCAChoiceArray.length() > 0) {
                    int k = 0;
                    for (int j = 0; j <= jsonCCAChoiceArray.length(); j++) {
                        CCAchoiceModel mCCADetailModelchoice = new CCAchoiceModel();
                        if (jsonCCAChoiceArray.length() != j) {
                            JSONObject objectCCAchoice = jsonCCAChoiceArray.optJSONObject(j);
                            mCCADetailModelchoice.setCca_item_name(objectCCAchoice.optString("cca_item_name"));
                            mCCADetailModelchoice.setCca_details_id(objectCCAchoice.optString("cca_details_id"));
                            mCCADetailModelchoice.setIsattending(objectCCAchoice.optString("isAttendee"));
                            mCCADetailModelchoice.setCca_item_start_time(objectCCAchoice.optString("cca_item_start_time"));
                            mCCADetailModelchoice.setCca_item_end_time(objectCCAchoice.optString("cca_item_end_time"));
                            mCCADetailModelchoice.setVenue(objectCCAchoice.optString("venue"));
                            mCCADetailModelchoice.setDescription(objectCCAchoice.optString("description"));
                            Log.e("DESC 1",objectCCAchoice.optString("description"));
                            if (objectCCAchoice.optString("attending_status").equalsIgnoreCase("0")) {
                                if (dataObject.optString("isAttendee").equalsIgnoreCase("2")) {
                                    mCCADetailModelchoice.setStatus("1");
                                    mCCADetailModel.setChoice1(objectCCAchoice.optString("cca_item_name"));
                                    mCCADetailModel.setChoice1Id(objectCCAchoice.optString("cca_details_id"));
                                } else {
                                    mCCADetailModelchoice.setStatus("0");

                                }
                                k = k + 1;
                            } else {
                                mCCADetailModelchoice.setStatus("0");

                            }
                            mCCADetailModelchoice.setDisableCccaiem(false);

                            mCCADetailModelchoice.setDayChoice(objectCCAchoice.optString("day"));

                        } else {
                            mCCADetailModelchoice.setCca_item_name("None");
                            mCCADetailModelchoice.setCca_details_id("-541");
                            mCCADetailModelchoice.setVenue("0");
                            mCCADetailModelchoice.setDescription("0");
                            mCCADetailModelchoice.setIsattending("0");
                            if (k == 0) {
                                if (dataObject.optString("isAttendee").equalsIgnoreCase("2")) {
                                    mCCADetailModelchoice.setStatus("1");
                                    mCCADetailModel.setChoice1("None");
                                    mCCADetailModel.setChoice1Id("-541");
                                } else {
                                    mCCADetailModelchoice.setStatus("0");

                                }
                            } else {
                                mCCADetailModelchoice.setStatus("0");

                            }
                            mCCADetailModelchoice.setDisableCccaiem(false);
                            mCCADetailModelchoice.setDayChoice(objectCCA.optString("day"));

                        }
                        CCAchoiceModelArrayList.add(mCCADetailModelchoice);
                    }
                }
                mCCADetailModel.setCcaChoiceModel(CCAchoiceModelArrayList);
                CCAchoiceModelArrayList2 = new ArrayList<>();
                if (jsonCCAChoiceArray2.length() > 0) {
                    int k = 0;
                    for (int j = 0; j <= jsonCCAChoiceArray2.length(); j++) {
                        CCAchoiceModel mCCADetailModelchoice = new CCAchoiceModel();
                        if (jsonCCAChoiceArray2.length() != j) {
                            JSONObject objectCCAchoice = jsonCCAChoiceArray2.optJSONObject(j);
                            mCCADetailModelchoice.setCca_item_name(objectCCAchoice.optString("cca_item_name"));
                            mCCADetailModelchoice.setCca_details_id(objectCCAchoice.optString("cca_details_id"));
                            mCCADetailModelchoice.setIsattending(objectCCAchoice.optString("isAttendee"));
                            mCCADetailModelchoice.setCca_item_start_time(objectCCAchoice.optString("cca_item_start_time"));
                            mCCADetailModelchoice.setCca_item_end_time(objectCCAchoice.optString("cca_item_end_time"));
                            mCCADetailModelchoice.setVenue(objectCCAchoice.optString("venue"));
                            mCCADetailModelchoice.setDescription(objectCCAchoice.optString("description"));
                            Log.e("DESC 2",objectCCAchoice.optString("description"));
                            mCCADetailModelchoice.setDayChoice(objectCCAchoice.optString("day"));
                            if (objectCCAchoice.optString("attending_status").equalsIgnoreCase("0")) {
                                if (dataObject.optString("isAttendee").equalsIgnoreCase("2")) {
                                    mCCADetailModelchoice.setStatus("1");
                                    mCCADetailModel.setChoice2(objectCCAchoice.optString("cca_item_name"));
                                    mCCADetailModel.setChoice2Id(objectCCAchoice.optString("cca_details_id"));
                                } else {
                                    mCCADetailModelchoice.setStatus("0");

                                }
                                k = k + 1;

                            } else {
                                mCCADetailModelchoice.setStatus("0");

                            }
                            mCCADetailModelchoice.setDisableCccaiem(false);

                        } else {
                            mCCADetailModelchoice.setCca_item_name("None");
                            mCCADetailModelchoice.setCca_details_id("-541");
                            mCCADetailModelchoice.setIsattending("0");
                            mCCADetailModelchoice.setVenue("0");
                            mCCADetailModelchoice.setDescription("0");

                            if (k == 0) {
                                if (dataObject.optString("isAttendee").equalsIgnoreCase("2")) {
                                    mCCADetailModelchoice.setStatus("1");
                                    mCCADetailModel.setChoice2("None");
                                    mCCADetailModel.setChoice2Id("-541");
                                } else {
                                    mCCADetailModelchoice.setStatus("0");

                                }
                            } else {
                                mCCADetailModelchoice.setStatus("0");

                            }
                            mCCADetailModelchoice.setDayChoice(objectCCA.optString("day"));
                            mCCADetailModelchoice.setDisableCccaiem(false);

                        }
                        CCAchoiceModelArrayList2.add(mCCADetailModelchoice);
                    }


                }
                mCCADetailModel.setCcaChoiceModel2(CCAchoiceModelArrayList2);
                CCADetailModelArrayList.add(mCCADetailModel);
            }
        }
        mCCAModel.setDetails(CCADetailModelArrayList);
        return mCCAModel;
    }

    private void callStatusChangeApi(String URL, final String id, final int eventPosition, final String status) {
        VolleyWrapper volleyWrapper = new VolleyWrapper(URL);
        String[] name = {"access_token","users_id","id","type"};
        String[] value = {PreferenceManager.getAccessToken(mContext),PreferenceManager.getUserId(mContext),id,"cca"};
        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("The response is status change" + successResponse);
                try {
                    JSONObject obj = new JSONObject(successResponse);
                    String response_code = obj.getString(JTAG_RESPONSECODE);
                    if (response_code.equalsIgnoreCase("200")) {
                        JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                        String status_code = secobj.getString(JTAG_STATUSCODE);
                        if (status_code.equalsIgnoreCase("303")) {
                            if(status.equalsIgnoreCase("0")||status.equalsIgnoreCase("2"))
                            {
                                mCCAmodelArrayList.get(eventPosition).setStatus("1");
                                mCCAsActivityAdapter.notifyDataSetChanged();

                            }
                        }
                    } else if (response_code.equalsIgnoreCase("500")) {

                    } else if (response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) ||
                            response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                            response_code.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
                        AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                            @Override
                            public void getAccessToken() {

                            }
                        });

                    } else {

                    }
                } catch (Exception ex) {
                }

            }

            @Override
            public void responseFailure(String failureResponse) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(!(PreferenceManager.getCcaOptionBadge(mContext).equalsIgnoreCase("0")))
        {
            CcaFragmentMain.ccaDot.setText(PreferenceManager.getCcaOptionBadge(mContext));

        }
        else if (!(PreferenceManager.getCcaOptionEditedBadge(mContext).equalsIgnoreCase("0")))
        {
            CcaFragmentMain.ccaDot.setText(PreferenceManager.getCcaOptionEditedBadge(mContext));
        }
        else {
            CcaFragmentMain.ccaDot.setVisibility(View.GONE);
        }
    }
}
