package com.mobatia.naisapp.fragments.sports;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.activities.sports.filter.FilterModel;
import com.mobatia.naisapp.activities.sports.filter.FilterStudentListRecyclerAdapter;
import com.mobatia.naisapp.constants.CacheDIRConstants;
import com.mobatia.naisapp.constants.IntentPassValueConstants;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.constants.NaisTabConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.parents_evening.model.StudentModel;
import com.mobatia.naisapp.fragments.sports.adapter.SportsEventListAdapter;
import com.mobatia.naisapp.fragments.sports.model.SportsModel;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.HeaderManager;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;


public class SportsActivityNew extends Activity implements
        NaisTabConstants, CacheDIRConstants, URLConstants,
        IntentPassValueConstants, NaisClassNameConstants, JSONConstants {

    private Context mContext;
    RecyclerView mSportListView;
    EditText searchEditText;
    ImageView btnImgsearch;
    ArrayList<SportsModel> filtered;
    LinearLayout searchLinear;
    ArrayList<SportsModel> mSportsModelArrayList = new ArrayList<>();
    ArrayList<SportsModel> mSportsModelArrayListRe = new ArrayList<>();
    HeaderManager headermanager;
    RelativeLayout relativeHeader;
    ImageView back;
    ImageView filter;
    ImageView home;
    String tab_type = "Sports";
    Bundle extras;
    public static ArrayList<FilterModel> mFilterModelList;
    public static ArrayList<FilterModel> mFilterModelListCopy;
    String searchString = "";
    SportsEventListAdapter sportsEventListAdapter;
    FilterStudentListRecyclerAdapter mFilterCarePlanRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sports_event_list);
        mContext = this;
        filtered = new ArrayList<SportsModel>();
        mFilterModelList = new ArrayList<>();
        mFilterModelListCopy = new ArrayList<>();

        extras = getIntent().getExtras();
        if (extras != null) {
            tab_type = extras.getString("tab_type");
        }
        initialiseUI();
        btnImgsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.hideKeyBoard(mContext);


            }
        });
        searchEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                // TODO Auto-generated method stub
                if (mSportsModelArrayList.size() > 0) {
                    filtered = new ArrayList<SportsModel>();
                    for (int i = 0; i < mSportsModelArrayList.size(); i++) {
                        if (mSportsModelArrayList.get(i).getSports_name()
                                .toLowerCase().contains(s.toString().toLowerCase())) {
                            if (!filtered.contains(mSportsModelArrayList.get(i))) {
                                filtered.add(mSportsModelArrayList.get(i));
                            }
                        }
                    }
                    sportsEventListAdapter = new SportsEventListAdapter(mContext, filtered);
                    mSportListView.setAdapter(sportsEventListAdapter);

                    if (searchEditText.getText().toString()
                            .equalsIgnoreCase("")) {
                        sportsEventListAdapter = new SportsEventListAdapter(mContext, mSportsModelArrayList);
                        mSportListView.setAdapter(sportsEventListAdapter);
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });
        if (AppUtils.isNetworkConnected(mContext)) {
            getStudentsListAPI();
        } else {
            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

        }

    }

    private void initialiseUI() {
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        headermanager = new HeaderManager(SportsActivityNew.this, tab_type);
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
        searchLinear = (LinearLayout) findViewById(R.id.searchLinear);
        searchEditText = (EditText) findViewById(R.id.searchEditText);
        btnImgsearch = (ImageView) findViewById(R.id.btnImgsearch);
        filter = (ImageView) findViewById(R.id.filter);

        mSportListView = (RecyclerView) findViewById(R.id.mSporsEventListView);
        //mSportListView.addItemDecoration(new DividerItemDecoration(getDrawable(R.drawable.list_divider)));
        mSportListView.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mSportListView.setLayoutManager(llm);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*if (mFilterModelList.size()>0) {
Intent mIntent = new Intent(mContext, FilterStudentListActivity.class);
mIntent.putExtra(FILTERS_ARRAY, mFilterModelList);
startActivity(mIntent);
}*/
                showDialogAlertFilter(mContext);

            }
        });
    }


    private void callSportsEventListAPI(String URL) {
        mSportsModelArrayList = new ArrayList<>();
        VolleyWrapper volleyWrapper = new VolleyWrapper(URL);
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

                            if (data.length() > 0) {
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject dataObject = data.getJSONObject(i);
                                    mSportsModelArrayList.add(addSportsListDetails(dataObject));
                                }
                                mSportsModelArrayListRe = (ArrayList<SportsModel>) mSportsModelArrayList.clone();
                                sportsEventListAdapter = new SportsEventListAdapter(mContext, mSportsModelArrayList);
                                mSportListView.setAdapter(sportsEventListAdapter);
                            } else {
                                Toast.makeText(mContext, "No data found", Toast.LENGTH_SHORT).show();
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
                        callSportsEventListAPI(URL_GET_SPORTSEVENT_LIST);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        callSportsEventListAPI(URL_GET_SPORTSEVENT_LIST);

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        callSportsEventListAPI(URL_GET_SPORTSEVENT_LIST);

                    } else {

                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    }
                } catch (Exception ex) {
                    //System.out.println("The Exception in edit profile is" + ex.toString());
                }

            }

            @Override
            public void responseFailure(String failureResponse) {

                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

            }
        });


    }

    public SportsModel addSportsListDetails(JSONObject obj) {
        SportsModel sportsModel = new SportsModel();
        sportsModel.setSports_id(obj.optString(JTAG_ID));
        sportsModel.setSports_name(obj.optString(JTAG_TAB_NAME));
        sportsModel.setSports_start_date(obj.optString(JTAG_EVENT_STARTDATE));
        sportsModel.setSports_end_date(obj.optString(JTAG_EVENT_ENDDATE));
        sportsModel.setStudentIds(obj.optString(JTAG_STUDENT_IDS));
        sportsModel.setStatus(obj.optString("status"));
        ArrayList<String> items = (ArrayList<String>) Arrays.asList(obj.optString("studentIds").split("\\s*,\\s*"));

        ArrayList<StudentModel> studentModelSportsList = new ArrayList<>();
        for (int j = 0; j < items.size(); j++) {
            for (int i = 0; i < mFilterModelList.size(); i++) {
                if (items.get(j).equalsIgnoreCase(mFilterModelList.get(i).getStudentId())) {
                    StudentModel studentModel = new StudentModel();
                    studentModel.setmId((mFilterModelList.get(i).getStudentId()));
                    studentModel.setmPhoto((mFilterModelList.get(i).getmPhoto()));
                    studentModelSportsList.add(studentModel);

                }
            }
        }
        sportsModel.setStudentModelSportsList(studentModelSportsList);
        return sportsModel;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (AppUtils.isNetworkConnected(mContext)) {
            callSportsEventListAPI(URL_GET_SPORTSEVENT_LIST);
        } else {
            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

        }

    }


    private void getStudentsListAPI() {
        VolleyWrapper volleyWrapper = new VolleyWrapper(URL_GET_STUDENT_LIST);
        String[] name = {"access_token", "users_id"};
        String[] value = {PreferenceManager.getAccessToken(mContext), PreferenceManager.getUserId(mContext)};
        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("The response is student list" + successResponse);
                try {
                    mFilterModelList = new ArrayList<>();
                    mFilterModelListCopy = new ArrayList<>();
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
                                    mFilterModelList.add(addStudentDetails(dataObject));
                                    mFilterModelListCopy.add(addStudentDetails(dataObject));
                                }
                                callSportsEventListAPI(URL_GET_SPORTSEVENT_LIST);

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
                        getStudentsListAPI();

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        getStudentsListAPI();

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        getStudentsListAPI();

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

    private FilterModel addStudentDetails(JSONObject dataObject) {
        FilterModel studentModel = new FilterModel();
        studentModel.setStudentId(dataObject.optString(JTAG_ID));
        studentModel.setmName(dataObject.optString(JTAG_TAB_NAME));
        studentModel.setmClass(dataObject.optString(JTAG_TAB_CLASS));
        studentModel.setmSection(dataObject.optString(JTAG_TAB_SECTION));
        studentModel.setmHouse(dataObject.optString("house"));
        studentModel.setmPhoto(dataObject.optString("photo"));
        studentModel.setChecked(true);
        studentModel.setCheckedStudent(true);

        return studentModel;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!(PreferenceManager.getSportsFixtureBadge(mContext).equalsIgnoreCase("0"))) {
            SportsMainScreenFragment.fixtureDot.setText(PreferenceManager.getSportsFixtureBadge(mContext));

        } else if (!(PreferenceManager.getSportsEditedFixtureBadge(mContext).equalsIgnoreCase("0"))) {
            SportsMainScreenFragment.fixtureDot.setText(PreferenceManager.getSportsEditedBadge(mContext));
        } else {
            SportsMainScreenFragment.fixtureDot.setVisibility(View.GONE);
        }
        if (!(PreferenceManager.getSportsCalendarBadge(mContext).equalsIgnoreCase("0"))) {
            SportsMainScreenFragment.fixtureDot.setText(PreferenceManager.getSportsCalendarBadge(mContext));

        } else if (!(PreferenceManager.getSportsEditedCalendarBadge(mContext).equalsIgnoreCase("0"))) {
            SportsMainScreenFragment.fixtureDot.setText(PreferenceManager.getSportsEditedCalendarBadge(mContext));
        } else {
            SportsMainScreenFragment.fixtureDot.setVisibility(View.GONE);
        }
    }


    public void showDialogAlertFilter(final Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_filter);
        final RecyclerView filterRecyclerView = (RecyclerView) dialog.findViewById(R.id.filterRecyclerView);
        Button dialogButtonApply = (Button) dialog.findViewById(R.id.btn_Ok);
        Button dialogButtonDismiss = (Button) dialog.findViewById(R.id.btn_Cancel);
        TextView clearButton = (TextView) dialog.findViewById(R.id.clearButton);
        searchString = searchEditText.getText().toString();
        GridLayoutManager recyclerViewLayoutManager = new GridLayoutManager(mContext, 1);
        filterRecyclerView.setLayoutManager(recyclerViewLayoutManager);
        filterRecyclerView.setHasFixedSize(true);
        mFilterCarePlanRecyclerAdapter = new FilterStudentListRecyclerAdapter(mContext, mFilterModelList);
        filterRecyclerView.setAdapter(mFilterCarePlanRecyclerAdapter);
        dialogButtonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i = 0; i < mFilterModelList.size(); i++) {
                    if (mFilterModelList.get(i).getCheckedStudent()) {
                        mFilterModelListCopy.get(i).setCheckedStudent(true);
                    } else {
                        mFilterModelListCopy.get(i).setCheckedStudent(false);

                    }
                }

                mFilterCarePlanRecyclerAdapter.notifyDataSetChanged();
                mSportsModelArrayList.clear();
                for (int i = 0; i < mFilterModelList.size(); i++) {
                    if (mFilterModelList.get(i).getCheckedStudent()) {
                        for (int j = 0; j < mSportsModelArrayListRe.size(); j++) {
                            if (mSportsModelArrayListRe.get(j).getStudentIds().contains(mFilterModelList.get(i).getStudentId())) {
                                mSportsModelArrayList.add(mSportsModelArrayListRe.get(j));
                            }

                        }
                    }

                }
                sportsEventListAdapter.notifyDataSetChanged();
                dialog.dismiss();
                searchEditText.setText(searchString);

            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < mFilterModelList.size(); i++) {
                    mFilterModelList.get(i).setCheckedStudent(true);
                }
                mFilterCarePlanRecyclerAdapter.notifyDataSetChanged();
            }
        });
        dialogButtonDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < mFilterModelList.size(); i++) {
                    if (mFilterModelListCopy.get(i).getCheckedStudent()) {
                        mFilterModelList.get(i).setCheckedStudent(true);
                    } else {
                        mFilterModelList.get(i).setCheckedStudent(false);

                    }
                }

                dialog.dismiss();
            }
        });
        dialog.show();

    }

//    DatePickerDialog.OnDateSetListener endDate = new DatePickerDialog.OnDateSetListener() {
//        @Override
//        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//            toDate = String.valueOf(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
//            pickUpVenue.setText(AppUtils.dateConversionY(toDate));
//
//        }
//
//
//    };

}
