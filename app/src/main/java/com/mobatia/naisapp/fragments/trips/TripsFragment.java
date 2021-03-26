package com.mobatia.naisapp.fragments.trips;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
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

import com.google.gson.Gson;
import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.trips.PaymentDetailActivity;
import com.mobatia.naisapp.activities.trips.TripsDetailActivity;
import com.mobatia.naisapp.activities.trips.database.DatabaseClient;
import com.mobatia.naisapp.activities.trips.database.Trip;
import com.mobatia.naisapp.constants.CacheDIRConstants;
import com.mobatia.naisapp.constants.IntentPassValueConstants;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.constants.NaisTabConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.report.adapter.StudentSpinnerAdapter;
import com.mobatia.naisapp.fragments.report.model.ReportModel;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.recyclerviewmanager.DividerItemDecoration;
import com.mobatia.naisapp.recyclerviewmanager.RecyclerItemListener;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TripsFragment extends Fragment implements AdapterView.OnItemClickListener,
        NaisTabConstants, CacheDIRConstants, URLConstants,
        IntentPassValueConstants, NaisClassNameConstants, JSONConstants, StatusConstants {
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
    String stud_img = "";
    String section = "";
    TextView noDataTxt;
    ImageView noDataImg;
    LinearLayout mStudentSpinner;
    ImageView back;
    ImageView home;
    ImageView studImg;
    Bundle extras;
    String merchant_id;
    String auth_id;
    String session_url;
    String payment_url;
    String merchant_name;
    private View mRootView;
    private static Context mContext;
    private String mTitle;
    private String mTabId;
    private RelativeLayout relMain;
    private ImageView mBannerImage;
    private RecyclerView mRecycleView;
    private TextView alertText;
    private ArrayList<TripListModel> mListViewArray;
    String tripDetails = "";

    public TripsFragment() {

    }

    public TripsFragment(String title, String tabId) {
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
        mRootView = inflater.inflate(R.layout.fragment_trips, container,
                false);
        mContext = getActivity();
        TextView mTitleTextView = (TextView) mRootView.findViewById(R.id.titleTextView);
        mTitleTextView.setText("Payments");
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
        mRecycleView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.list_divider)));
        mRecycleView.setLayoutManager(llm);
        relMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mRecycleView.addOnItemTouchListener(new RecyclerItemListener(getActivity(), mRecycleView,
                new RecyclerItemListener.RecyclerTouchListener() {
                    public void onClickItem(View v, int position) {
                        Intent intent = new Intent(mContext, PaymentDetailActivity.class);
                        intent.putExtra("title", mListViewArray.get(position).getTitle());
                        intent.putExtra("trip_id", mListViewArray.get(position).getId());
                        intent.putExtra("description", mListViewArray.get(position).getDescription());
                        intent.putExtra("trip_date", mListViewArray.get(position).getTrip_date());
                        intent.putExtra("amount", mListViewArray.get(position).getAmount());
                        intent.putExtra("completed_dated", mListViewArray.get(position).getCompleted_date());
                        intent.putExtra("last_payment_date", mListViewArray.get(position).getLast_payment_date());
                        intent.putExtra("last_payment_status", mListViewArray.get(position).getLast_payment_status());
                        intent.putExtra("trip_date_staus", mListViewArray.get(position).getTrip_date_staus());
                        intent.putExtra("orderId", "NAST" + mListViewArray.get(position).getId() + "S" + stud_id);
                        intent.putExtra("merchant_id", merchant_id);
                        intent.putExtra("auth_id", auth_id);
                        intent.putExtra("session_url", session_url);
                        intent.putExtra("payment_url", payment_url);
                        intent.putExtra("stud_id", stud_id);
                        intent.putExtra("merchant_name", merchant_name);
                        System.out.println("payment_url" + payment_url);
                        mContext.startActivity(intent);

                    }

                    public void onLongClickItem(View v, int position) {
                        System.out.println("On Long Click Item interface");
                    }
                }));
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
                                    studentName.setText(studentsModelArrayList.get(0).getmName());
                                    stud_name = studentsModelArrayList.get(0).getmName();
                                    stud_class = studentsModelArrayList.get(0).getmClass();
                                    stud_img = studentsModelArrayList.get(0).getmPhoto();

                                    section = studentsModelArrayList.get(0).getmSection();
                                    if (!(stud_img.equals(""))) {

                                        Picasso.with(mContext).load(AppUtils.replace(stud_img)).placeholder(R.drawable.boy).fit().into(studImg);
                                    } else

                                    {

                                        studImg.setImageResource(R.drawable.boy);
                                    }


                                    textViewYear.setText("Class : " + studentsModelArrayList.get(0).getmClass());

                                    PreferenceManager.setCCAStudentIdPosition(mContext, "0");

                                } else {

                                    int studentSelectPosition = Integer.valueOf(PreferenceManager.getCCAStudentIdPosition(mContext));

                                    studentName.setText(studentsModelArrayList.get(studentSelectPosition).getmName());
                                    stud_id = studentsModelArrayList.get(studentSelectPosition).getmId();
                                    stud_name = studentsModelArrayList.get(studentSelectPosition).getmName();
                                    stud_class = studentsModelArrayList.get(studentSelectPosition).getmClass();
                                    stud_img = studentsModelArrayList.get(studentSelectPosition).getmPhoto();
                                    System.out.println("selected student image" + studentsModelArrayList.get(studentSelectPosition).getmPhoto());
                                    if (!(stud_img.equals(""))) {

                                        Picasso.with(mContext).load(AppUtils.replace(stud_img)).placeholder(R.drawable.boy).fit().into(studImg);
                                    } else

                                    {

                                        studImg.setImageResource(R.drawable.boy);
                                    }
                                    textViewYear.setText("Class : " + studentsModelArrayList.get(studentSelectPosition).getmClass());


                                }
                                if (AppUtils.isNetworkConnected(mContext)) {
                                    System.out.println("test working");
                                    getReportListAPI(URL_TRIP_LIST);
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

    private void getReportListAPI(final String URLHEAD) {
        mListViewArray = new ArrayList<>();

        VolleyWrapper volleyWrapper = new VolleyWrapper(URLHEAD);
        String[] name = {"access_token", "student_id", "users_id"};
        String[] value = {PreferenceManager.getAccessToken(mContext), stud_id, PreferenceManager.getUserId(mContext)};
        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("The response is student list progress" + successResponse);
                try {
//                    studentsModelArrayList = new ArrayList<>();//wrong

                    JSONObject obj = new JSONObject(successResponse);
                    String response_code = obj.getString(JTAG_RESPONSECODE);
                    if (response_code.equalsIgnoreCase("200")) {
                        JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                        String status_code = secobj.getString(JTAG_STATUSCODE);
                        if (status_code.equalsIgnoreCase("303")) {
                            JSONArray dataArray = secobj.getJSONArray(JTAG_RESPONSE_DATA_ARRAY);
                            if (dataArray.length() > 0) {
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject dataObject = dataArray.getJSONObject(i);
                                    mListViewArray.add(getSearchValues(dataObject));


                                }
//											mListView.setAdapter(new CustomSecondaryAdapter(getActivity(), mListViewArray));
                                mRecycleView.setAdapter(new TripsRecyclerAdapter(mContext, mListViewArray));

                            } else {

                                Toast.makeText(mContext, "No Trips available", Toast.LENGTH_SHORT).show();
                            }
                            JSONObject paymentObj = secobj.getJSONObject("payment_info");
                            merchant_id = paymentObj.getString("merchant_id");
                            auth_id = paymentObj.getString("auth_id");
                            session_url = paymentObj.getString("session_url");
                            payment_url = paymentObj.getString("payment_url");
                            merchant_name = paymentObj.getString("merchant_name");
                            tripSubmiotApi(URL_TRIP_SUBMISSION);
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

    private static TripListModel getSearchValues(JSONObject Object)
            throws JSONException {
        TripListModel mTripListModel = new TripListModel();
        mTripListModel.setId(Object.getString(JTAG_ID));
        mTripListModel.setTitle(Object.getString("title"));
        mTripListModel.setDescription(Object.getString("description"));
        mTripListModel.setTrip_date(Object.getString("trip_date"));
        mTripListModel.setAmount(Object.getString("amount"));
        mTripListModel.setLast_payment_date(Object.getString("last_payment_date"));
        mTripListModel.setCompleted_date(Object.getString("completed_dated"));
        mTripListModel.setLast_payment_status(Object.getString("last_payment_status"));
        mTripListModel.setTrip_date_staus(Object.getString("trip_date_staus"));
        return mTripListModel;
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
                        stud_img = mStudentArray.get(position).getmPhoto();
                        section = mStudentArray.get(position).getmSection();
                        textViewYear.setText("Class : " + mStudentArray.get(position).getmClass());
                        if (!(stud_img.equals(""))) {

                            Picasso.with(mContext).load(AppUtils.replace(stud_img)).placeholder(R.drawable.boy).fit().into(studImg);
                        } else

                        {

                            studImg.setImageResource(R.drawable.boy);
                        }
                        if (AppUtils.isNetworkConnected(mContext)) {
                            System.out.println("test working");
                            getReportListAPI(URL_TRIP_LIST);
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


        Intent intent = new Intent(mContext, TripsDetailActivity.class);
        intent.putExtra("title", mListViewArray.get(position).getTitle());
        intent.putExtra("description", mListViewArray.get(position).getDescription());
        intent.putExtra("trip_date", mListViewArray.get(position).getTrip_date());
        intent.putExtra("amount", mListViewArray.get(position).getAmount());
        intent.putExtra("completed_dated", mListViewArray.get(position).getCompleted_date());
        mContext.startActivity(intent);


    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void tripSubmiotApi(final String URLHEAD) {

        class TripSubmit extends AsyncTask<Void, Void, Void> {
            List<Trip> mTripList;

            @Override
            protected Void doInBackground(Void... voids) {

                mTripList = DatabaseClient.getInstance(mContext).getAppDatabase()
                        .tripDao().getTripUnsync("0", PreferenceManager.getUserId(mContext));//trip with status zero
                Gson gson = new Gson();
//                gson.toJson(mTripList);
//                Log.v("gson to json:", " " +  gson.toJson(mTripList));
                tripDetails = "{\"details\":" + gson.toJson(mTripList) + "}";

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (mTripList.size() > 0) {
                    tripSync(URLHEAD);
                }
            }


        }

        TripSubmit mTripSubmit = new TripSubmit();
        mTripSubmit.execute();


    }
//rijo commented below aparna code
//    @Override
//    public void onStart() {
//        super.onStart();
//        if (AppController.isAPISuccess)
//        {
//            AppController.isAPISuccess=false;
//            if (AppUtils.isNetworkConnected(mContext)) {
//                getReportListAPI(URL_TRIP_LIST);
//            } else {
//                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
//
//            }
//        }
//        System.out.println("start function called");
//    }

    void tripSync(final String URLHEAD) {
        {
            VolleyWrapper volleyWrapper = new VolleyWrapper(URLHEAD);
            String[] name = {"access_token", "data"};
            String[] value = {PreferenceManager.getAccessToken(mContext), tripDetails};
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


                                List<Trip> tripWithId = DatabaseClient.getInstance(mContext).getAppDatabase()
                                        .tripDao().getTripUnsync("0", PreferenceManager.getUserId(mContext));
                                for (int i = 0; i < tripWithId.size(); i++) {
//                                    tripWithId.get(i).setStatus("1");
//                                    DatabaseClient.getInstance(mContext).getAppDatabase()
//                                            .tripDao().update(tripWithId.get(i));
                                    DatabaseClient.getInstance(mContext).getAppDatabase()
                                            .tripDao().delete(tripWithId.get(i));
                                }
                                getReportListAPI(URL_TRIP_LIST);

                            }
                        } else if (response_code.equalsIgnoreCase("500")) {
                            // AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                        } else if (response_code.equalsIgnoreCase("400")) {
                            AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                                @Override
                                public void tokenrenewed() {
                                }
                            });
                            tripSync(URLHEAD);

                        } else if (response_code.equalsIgnoreCase("401")) {
                            AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                                @Override
                                public void tokenrenewed() {
                                }
                            });
                            tripSync(URLHEAD);

                        } else if (response_code.equalsIgnoreCase("402")) {
                            AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                                @Override
                                public void tokenrenewed() {
                                }
                            });
                            tripSync(URLHEAD);

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
    }
}

