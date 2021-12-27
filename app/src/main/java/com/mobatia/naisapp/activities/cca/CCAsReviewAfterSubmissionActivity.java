package com.mobatia.naisapp.activities.cca;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.cca.adapter.CCAfinalReviewAfterSubmissionAdapter;
import com.mobatia.naisapp.activities.cca.model.CCAAttendanceModel;
import com.mobatia.naisapp.activities.cca.model.CCAReviewAfterSubmissionModel;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.HeaderManager;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CCAsReviewAfterSubmissionActivity extends Activity implements URLConstants, JSONConstants {
    GridLayoutManager recyclerViewLayoutManager;
    static RecyclerView recycler_review;
    HeaderManager headermanager;
    RelativeLayout relativeHeader;
    ImageView back;
    ImageView home;
    ImageView attendanceListIcon;
    String tab_type = "CCAs";
    Bundle extras;
    static Context mContext;
    public static ArrayList<CCAReviewAfterSubmissionModel> mCCADetailModelArrayList;
    TextView textViewCCAaItem;
    RelativeLayout messageTxt;
    RelativeLayout editCcca;
    static ArrayList<String> weekList;
    static ArrayList<String> absentDaysChoice2Array;
    static ArrayList<String> presentDaysChoice2Array;
    static ArrayList<String> upcomingDaysChoice2Array;
    static ArrayList<String> absentDaysChoice1Array;
    static ArrayList<String> presentDaysChoice1Array;
    static ArrayList<String> upcomingDaysChoice1Array;
    static ArrayList<CCAAttendanceModel> datestringChoice1;
    static ArrayList<CCAAttendanceModel> datestringChoice2;
    String submissiondateover = "-1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cca_review_after_submit);
        mContext = this;
        extras = getIntent().getExtras();
        if (extras != null) {
            tab_type = extras.getString("tab_type");
            submissiondateover = extras.getString("submissiondateover", "-1");
        }
        weekList = new ArrayList<>();
        weekList.add("Sunday");
        weekList.add("Monday");
        weekList.add("Tuesday");
        weekList.add("Wednesday");
        weekList.add("Thursday");
        weekList.add("Friday");
        weekList.add("Saturday");
        absentDaysChoice2Array = new ArrayList<>();
        presentDaysChoice2Array = new ArrayList<>();
        upcomingDaysChoice2Array = new ArrayList<>();
        absentDaysChoice1Array = new ArrayList<>();
        presentDaysChoice1Array = new ArrayList<>();
        upcomingDaysChoice1Array = new ArrayList<>();
        datestringChoice1 = new ArrayList<>();
        datestringChoice2 = new ArrayList<>();
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        recycler_review = (RecyclerView) findViewById(R.id.recycler_view_cca);
        textViewCCAaItem = (TextView) findViewById(R.id.textViewCCAaItem);
        messageTxt = (RelativeLayout) findViewById(R.id.messageTxt);
        editCcca = (RelativeLayout) findViewById(R.id.editCcca);
        editCcca.setVisibility(View.GONE);
        messageTxt.setVisibility(View.GONE);
        headermanager = new HeaderManager(CCAsReviewAfterSubmissionActivity.this, tab_type);
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
        recycler_review.setHasFixedSize(true);
        recyclerViewLayoutManager = new GridLayoutManager(mContext, 1);
        recycler_review.setLayoutManager(recyclerViewLayoutManager);
        mCCADetailModelArrayList = new ArrayList<>();
        if (PreferenceManager.getStudClassForCCA(mContext).equalsIgnoreCase("")) {
            textViewCCAaItem.setText(Html.fromHtml(PreferenceManager.getCCATitle(mContext) + "<br/>" + PreferenceManager.getStudNameForCCA(mContext)));
        } else {
            textViewCCAaItem.setText(Html.fromHtml(PreferenceManager.getCCATitle(mContext) + "<br/>" + PreferenceManager.getStudNameForCCA(mContext) + "<br/>Year Group : " + PreferenceManager.getStudClassForCCA(mContext)));

        }
        if (AppUtils.isNetworkConnected(mContext)) {
            ccaReviewListAPI();
        } else {
            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

        }


    }

    private static void ccaReviewListAPI() {
        VolleyWrapper volleyWrapper = new VolleyWrapper(URL_CCA_REVIEWS);
        String[] name = {"access_token", "student_id", "cca_days_id"};
        String[] value = {PreferenceManager.getAccessToken(mContext), PreferenceManager.getStudIdForCCA(mContext), PreferenceManager.getCCAItemId(mContext)};
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
                            JSONArray data = secobj.optJSONArray("data");
                            mCCADetailModelArrayList = new ArrayList<>();

                            if (data.length() > 0) {
                                for (int j = 0; j < weekList.size(); j++) {
                                    for (int i = 0; i < data.length(); i++) {
                                        JSONObject dataObject = data.getJSONObject(i);
                                        if (dataObject.optString("day").equalsIgnoreCase(weekList.get(j).toString())) {
                                            addCCAReviewlist(dataObject);
                                        }
                                    }
                                }
                                CCAfinalReviewAfterSubmissionAdapter mCCAsActivityAdapter = new CCAfinalReviewAfterSubmissionAdapter(mContext, mCCADetailModelArrayList);
                                recycler_review.setAdapter(mCCAsActivityAdapter);

                            } else {
                                CCAfinalReviewAfterSubmissionAdapter mCCAsActivityAdapter = new CCAfinalReviewAfterSubmissionAdapter(mContext, mCCADetailModelArrayList);
                                recycler_review.setAdapter(mCCAsActivityAdapter);
                                Toast.makeText(mContext, "No CCA available", Toast.LENGTH_SHORT).show();
                            }

                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert",  mContext.getResources().getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        ccaReviewListAPI();

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        ccaReviewListAPI();

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        ccaReviewListAPI();

                    } else {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert",  mContext.getResources().getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    }
                } catch (Exception ex) {
                }

            }

            @Override
            public void responseFailure(String failureResponse) {
                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert",  mContext.getResources().getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

            }
        });


    }

    private static void addCCAReviewlist(JSONObject dataObject) {
        CCAReviewAfterSubmissionModel mCCAModel = new CCAReviewAfterSubmissionModel();
        mCCAModel.setDay(dataObject.optString("day"));
        datestringChoice1 = new ArrayList<>();
        datestringChoice2 = new ArrayList<>();
        int ch=0;
        if (dataObject.has("choice1")) {

            JSONObject choice1 = dataObject.optJSONObject("choice1");

            if (choice1 != null) {
                if (choice1.has("cca_item_name")) {
                    mCCAModel.setChoice1(choice1.optString("cca_item_name"));
                    mCCAModel.setCca_details_id(choice1.optString("cca_details_id"));
                    mCCAModel.setCca_item_start_time(choice1.optString("cca_item_start_time"));
                    mCCAModel.setCca_item_end_time(choice1.optString("cca_item_end_time"));
                    mCCAModel.setVenue(choice1.optString("venue"));
                    mCCAModel.setCca_item_description(choice1.optString("cca_item_description"));
                    mCCAModel.setAttending_status(choice1.optString("attending_status"));
                    JSONArray absentDaysChoice1 = choice1.optJSONArray("absentDays");
                    absentDaysChoice1Array = new ArrayList<>();
                    presentDaysChoice1Array = new ArrayList<>();
                    upcomingDaysChoice1Array = new ArrayList<>();
                    if ((choice1.optString("attending_status").equalsIgnoreCase("1"))) {
                        if (choice1.has("absentDays")) {
                            ch=1;

                            for (int i = 0; i < absentDaysChoice1.length(); i++) {
                                absentDaysChoice1Array.add(absentDaysChoice1.optString(i));
                            }
                        }
                        if (choice1.has("presentDays")) {
                            ch=1;

                            JSONArray presentDaysChoice1 = choice1.optJSONArray("presentDays");
                            for (int i = 0; i < presentDaysChoice1.length(); i++) {
                                presentDaysChoice1Array.add(presentDaysChoice1.optString(i));
                            }
                        }
                        if (choice1.has("upcomingDays")) {
                            ch=1;

                            JSONArray upcomingDaysChoice1 = choice1.optJSONArray("upcomingDays");
                            for (int i = 0; i < upcomingDaysChoice1.length(); i++) {
                                upcomingDaysChoice1Array.add(upcomingDaysChoice1.optString(i));
                            }
                        }
                    }else  if ((choice1.optString("attending_status").equalsIgnoreCase("3"))){
                        if (choice1.has("absentDays")) {

                            for (int i = 0; i < absentDaysChoice1.length(); i++) {
                                absentDaysChoice1Array.add(absentDaysChoice1.optString(i));
                            }
                            ch=1;

                        }
                        if (choice1.has("presentDays")) {

                            JSONArray presentDaysChoice1 = choice1.optJSONArray("presentDays");
                            for (int i = 0; i < presentDaysChoice1.length(); i++) {
                                presentDaysChoice1Array.add(presentDaysChoice1.optString(i));
                            }
                            ch=1;

                        }

                    }
                    else{
                        mCCAModel.setChoice1("0");

                    }
                } else {
                    mCCAModel.setChoice1("0");

                }
            } else {
                mCCAModel.setChoice1("0");
            }

        } else {
            mCCAModel.setChoice1("-1");
        }
        if (dataObject.has("choice2")) {
            JSONObject choice2 = dataObject.optJSONObject("choice2");
            if (choice2 != null) {
                if (choice2.has("cca_item_name")) {

                    mCCAModel.setChoice2(choice2.optString("cca_item_name"));
                    mCCAModel.setCca_item_start_time(choice2.optString("cca_item_start_time"));
                    mCCAModel.setCca_item_end_time(choice2.optString("cca_item_end_time"));
                    mCCAModel.setCca_details_id2(choice2.optString("cca_details_id"));
                    mCCAModel.setCca_item_description_2(choice2.optString("cca_item_description"));
                    mCCAModel.setVenue2(choice2.optString("venue"));
                    mCCAModel.setAttending_status2(choice2.optString("attending_status"));

                    JSONArray absentDaysChoice2 = choice2.optJSONArray("absentDays");
                    absentDaysChoice2Array = new ArrayList<>();
                    presentDaysChoice2Array = new ArrayList<>();
                    upcomingDaysChoice2Array = new ArrayList<>();
                    if ((choice2.optString("attending_status").equalsIgnoreCase("1"))) {

                        if (choice2.has("absentDays")) {
                            ch=1;

                            for (int i = 0; i < absentDaysChoice2.length(); i++) {
                                absentDaysChoice2Array.add(absentDaysChoice2.optString(i));
                            }
                        }

                        JSONArray presentDaysChoice2 = choice2.optJSONArray("presentDays");
                        if (choice2.has("presentDays")) {
                            ch=1;

                            for (int i = 0; i < presentDaysChoice2.length(); i++) {
                                presentDaysChoice2Array.add(presentDaysChoice2.optString(i));
                            }
                        }

                        JSONArray upcomingDaysChoice2 = choice2.optJSONArray("upcomingDays");
                        if (choice2.has("upcomingDays")) {
                            ch=1;

                            for (int i = 0; i < upcomingDaysChoice2.length(); i++) {
                                upcomingDaysChoice2Array.add(upcomingDaysChoice2.optString(i));
                            }
                        }
                    }else  if ((choice2.optString("attending_status").equalsIgnoreCase("3"))){
                        if (choice2.has("absentDays")) {

                            for (int i = 0; i < absentDaysChoice2.length(); i++) {
                                absentDaysChoice2Array.add(absentDaysChoice2.optString(i));
                            }
                            ch=1;

                        }
                        if (choice2.has("presentDays")) {

                            JSONArray presentDaysChoice2 = choice2.optJSONArray("presentDays");
                            for (int i = 0; i < presentDaysChoice2.length(); i++) {
                                presentDaysChoice2Array.add(presentDaysChoice2.optString(i));
                            }
                            ch=1;

                        }

                    }
                    else {
                        mCCAModel.setChoice2("0");

                    }
                } else {
                    mCCAModel.setChoice2("0");

                }
            } else {
                mCCAModel.setChoice2("0");
            }

        } else {
            mCCAModel.setChoice2("-1");
        }

        if (absentDaysChoice1Array.size() > 0) {
            for (int i = 0; i < absentDaysChoice1Array.size(); i++) {
                CCAAttendanceModel mCCAAttendanceModel = new CCAAttendanceModel();
                mCCAAttendanceModel.setDateAttend(absentDaysChoice1Array.get(i).toString());
                mCCAAttendanceModel.setStatusCCA("a");
                datestringChoice1.add(mCCAAttendanceModel);
            }
        }

        if (upcomingDaysChoice1Array.size() > 0) {
            for (int i = 0; i < upcomingDaysChoice1Array.size(); i++)
//                datestringChoice1.add(upcomingDaysChoice1Array.get(i).toString());
            {
                CCAAttendanceModel mCCAAttendanceModel = new CCAAttendanceModel();
                mCCAAttendanceModel.setDateAttend(upcomingDaysChoice1Array.get(i).toString());
                mCCAAttendanceModel.setStatusCCA("u");
                datestringChoice1.add(mCCAAttendanceModel);
            }
        }

        if (presentDaysChoice1Array.size() > 0) {
            for (int i = 0; i < presentDaysChoice1Array.size(); i++)
//                datestringChoice1.add(presentDaysChoice1Array.get(i).toString());
            {
                CCAAttendanceModel mCCAAttendanceModel = new CCAAttendanceModel();
                mCCAAttendanceModel.setDateAttend(presentDaysChoice1Array.get(i).toString());
                mCCAAttendanceModel.setStatusCCA("p");
                datestringChoice1.add(mCCAAttendanceModel);
            }
        }
        if (absentDaysChoice2Array.size() > 0) {
            for (int i = 0; i < absentDaysChoice2Array.size(); i++)
            {
                CCAAttendanceModel mCCAAttendanceModel = new CCAAttendanceModel();
                mCCAAttendanceModel.setDateAttend(absentDaysChoice2Array.get(i).toString());
                mCCAAttendanceModel.setStatusCCA("a");
                datestringChoice2.add(mCCAAttendanceModel);
            }
        }
        if (upcomingDaysChoice2Array.size() > 0) {
            for (int i = 0; i < upcomingDaysChoice2Array.size(); i++)
            {
                CCAAttendanceModel mCCAAttendanceModel = new CCAAttendanceModel();
                mCCAAttendanceModel.setDateAttend(upcomingDaysChoice2Array.get(i).toString());
                mCCAAttendanceModel.setStatusCCA("u");
                datestringChoice2.add(mCCAAttendanceModel);
            }
        }
        if (presentDaysChoice2Array.size() > 0) {
            for (int i = 0; i < presentDaysChoice2Array.size(); i++)
            {
                CCAAttendanceModel mCCAAttendanceModel = new CCAAttendanceModel();
                mCCAAttendanceModel.setDateAttend(presentDaysChoice2Array.get(i).toString());
                mCCAAttendanceModel.setStatusCCA("p");
                datestringChoice2.add(mCCAAttendanceModel);
            }
        }
        if (datestringChoice1.size() > 0) {

            Collections.sort(datestringChoice1, new Comparator<CCAAttendanceModel>() {
                public int compare(CCAAttendanceModel s1, CCAAttendanceModel s2) {
                    return s1.getDateAttend().compareToIgnoreCase(s2.getDateAttend());
                }
            });
        }
        if (datestringChoice2.size() > 0) {

            Collections.sort(datestringChoice1, new Comparator<CCAAttendanceModel>() {
                public int compare(CCAAttendanceModel s1, CCAAttendanceModel s2) {
                    return s1.getDateAttend().compareToIgnoreCase(s2.getDateAttend());
                }
            });
        }
        mCCAModel.setCalendarDaysChoice1(datestringChoice1);
        mCCAModel.setCalendarDaysChoice2(datestringChoice2);
        System.out.println("ch:::"+ch);

        if (ch==1) {
            mCCADetailModelArrayList.add(mCCAModel);
        }
    }
    public static void ccaDeleteAPI(final String cca_days_details_ids) {
        VolleyWrapper volleyWrapper = new VolleyWrapper(URL_CCA_DELETE);
        String[] name = {"access_token", "users_id", "student_id", "cca_days_details_ids"};
        String[] value = {PreferenceManager.getAccessToken(mContext),PreferenceManager.getUserId(mContext), PreferenceManager.getStudIdForCCA(mContext), cca_days_details_ids};
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
                            ccaReviewListAPI();
                            AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getResources().getString(R.string.deltechoicealertsuccess), R.drawable.exclamationicon, R.drawable.round);
                        }
                        else
                        {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getResources().getString(R.string.deltechoicealert), R.drawable.exclamationicon, R.drawable.round);

                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getResources().getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        ccaDeleteAPI(cca_days_details_ids);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        ccaDeleteAPI(cca_days_details_ids);

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        ccaDeleteAPI(cca_days_details_ids);

                    } else {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert",  mContext.getResources().getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    }
                } catch (Exception ex) {
                }

            }

            @Override
            public void responseFailure(String failureResponse) {

            }
        });


    }

}
