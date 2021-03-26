package com.mobatia.naisapp.activities.trips;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.activities.trips.database.DatabaseClient;
import com.mobatia.naisapp.activities.trips.database.Trip;
import com.mobatia.naisapp.activities.trips.model.FullPaymentListModel;
import com.mobatia.naisapp.activities.trips.model.InstallmentListModel;
import com.mobatia.naisapp.appcontroller.AppController;
import com.mobatia.naisapp.constants.CacheDIRConstants;
import com.mobatia.naisapp.constants.IntentPassValueConstants;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.constants.NaisTabConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.trips.TripListModel;
import com.mobatia.naisapp.fragments.trips.TripsRecyclerAdapter;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.HeaderManager;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.recyclerviewmanager.DividerItemDecoration;
import com.mobatia.naisapp.recyclerviewmanager.RecyclerItemListener;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TripListActivity extends Activity implements AdapterView.OnItemClickListener,
        NaisTabConstants, CacheDIRConstants, URLConstants,
        IntentPassValueConstants, NaisClassNameConstants, JSONConstants, StatusConstants {
    RelativeLayout relativeHeader;
    Context mContext;
    HeaderManager headermanager;
    ImageView back;
    ImageView home;
    Bundle extras;
    String tab_type;
    String categoryId;
    String email;
    String stud_id;
    String merchant_id;
    String auth_id;
    String session_url;
    String payment_url;
    String merchant_name;
    int adapterSize;
    private String mTitle;
    private String mTabId;
    private RelativeLayout relMain;
    private ImageView mBannerImage;
    private RecyclerView mRecycleView;
    private TextView alertText;
    private ArrayList<TripListModel> mListViewArray;
    String tripDetails = "";
   public static int installPos=0;
    private static ArrayList<InstallmentListModel> installmentArrayList;
    private static ArrayList<FullPaymentListModel> fullpaymentArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_trips);
        mContext = this;
        initUI();
//        if (AppUtils.isNetworkConnected(mContext)) {
//            getReportListAPI(URL_TRIP_LIST);
//        } else {
//            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
//
//        }
        if (AppUtils.isNetworkConnected(mContext)) {
            getReportListAPI(URL_TRIP_LIST);
        } else {
            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

        }
        if (AppUtils.isNetworkConnected(mContext)) {
            tripSubmiotApi(URL_TRIP_SUBMISSION, stud_id);
        } else {
            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

        }
    }

    public void initUI() {
        extras = getIntent().getExtras();
        if (extras != null) {
            tab_type = extras.getString("tab_type");
            categoryId = extras.getString("categoryId");
            stud_id = extras.getString("studentId");
            email = extras.getString("email");

        }
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        headermanager = new HeaderManager(TripListActivity.this, tab_type);
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
        mRecycleView = findViewById(R.id.recycler_view_list);
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycleView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.list_divider)));
        mRecycleView.setLayoutManager(llm);
        mRecycleView.addOnItemTouchListener(new RecyclerItemListener(mContext, mRecycleView,
                new RecyclerItemListener.RecyclerTouchListener() {
                    public void onClickItem(View v, int position) {
                        Intent intent = new Intent(mContext, PaymentDetailActivityInstallNew.class);
                     /*   intent.putExtra("title", mListViewArray.get(position).getTitle());
                        intent.putExtra("trip_id", mListViewArray.get(position).getId());
                        intent.putExtra("description", mListViewArray.get(position).getDescription());
                        intent.putExtra("trip_date", mListViewArray.get(position).getTrip_date());
                        intent.putExtra("amount", mListViewArray.get(position).getAmount());
                        intent.putExtra("completed_dated", mListViewArray.get(position).getCompleted_date());
                        intent.putExtra("last_payment_date", mListViewArray.get(position).getLast_payment_date());
                        intent.putExtra("last_payment_status", mListViewArray.get(position).getLast_payment_status());
                        intent.putExtra("trip_date_staus", mListViewArray.get(position).getTrip_date_staus());
                        intent.putExtra("status", mListViewArray.get(position).getStatus());
                        if (mListViewArray.get(position).getOrder_id().equalsIgnoreCase("")) {
                            intent.putExtra("orderId", "NASDUBAI" + mListViewArray.get(position).getId() + "S" + stud_id);

                        } else {
                            intent.putExtra("orderId", mListViewArray.get(position).getOrder_id());
                        }
                        intent.putExtra("payment_type", mListViewArray.get(position).getPayment_type());
                        intent.putExtra("paidby", mListViewArray.get(position).getPaidby());
                        intent.putExtra("invoiceNote", mListViewArray.get(position).getInvoiceNote());
                        intent.putExtra("parentName", mListViewArray.get(position).getParentName());
                        intent.putExtra("studentName", mListViewArray.get(position).getStudentName());
                        intent.putExtra("merchant_id", merchant_id);
                        intent.putExtra("auth_id", auth_id);
                        intent.putExtra("session_url", session_url);
                        intent.putExtra("payment_url", payment_url);
                        intent.putExtra("stud_id", stud_id);
                        intent.putExtra("merchant_name", merchant_name);*/
                        AppController.Position=position;
                        AppController.stud_id=stud_id;
                        AppController.categoryId=categoryId;
                        System.out.println("Student id"+AppController.stud_id);
                        System.out.println("Student id"+stud_id);
                        if (mListViewArray.get(AppController.Position).getInstallment().equalsIgnoreCase("1"))
                        {
                            if (mListViewArray.get(AppController.Position).getInstallmentArrayList().size()==1)
                            {

                                adapterSize=1;
                                System.out.println("adapter"+adapterSize);
                            }
                            else {

                                adapterSize=mListViewArray.get(AppController.Position).getInstallmentArrayList().size()+1;
                                System.out.println("adapter 11"+adapterSize);
                            }
                        }
                        else {

                            adapterSize=1;
                            System.out.println("adapter 222"+adapterSize);

                        }
                        intent.putExtra("key",mListViewArray);
                        intent.putExtra("adapterSize",adapterSize);

                        intent.putExtra("pos",AppController.Position);
                      //  intent.putExtra("orderId", "NASDUBAI" + mListViewArray.get(position).getId() + "S" + stud_id);
                        //intent.putExtra("orderId", mListViewArray.get(position).getOrder_id());
                        intent.putExtra("merchant_id", merchant_id);
                        intent.putExtra("auth_id", auth_id);
                        intent.putExtra("session_url", session_url);
                        intent.putExtra("payment_url", payment_url);
                        intent.putExtra("merchant_name", merchant_name);
                        intent.putExtra("tab_type", "Payment Details");
                        intent.putExtra("stud_id", stud_id);
                        intent.putExtra("email", email);
                        intent.putExtra("status", mListViewArray.get(position).getStatus());

                        intent.putExtra("categoryId", categoryId);
                        startActivity(intent);

                    }

                    public void onLongClickItem(View v, int position) {
                        System.out.println("On Long Click Item interface");


                    }
                }));
    }
    private void getReportListAPI(final String URLHEAD) {
        mListViewArray = new ArrayList<>();
        installmentArrayList = new ArrayList<>();
        fullpaymentArrayList = new ArrayList<>();
        mListViewArray.clear();
        installmentArrayList.clear();
        fullpaymentArrayList.clear();
        VolleyWrapper volleyWrapper = new VolleyWrapper(URLHEAD);
        String[] name = {"access_token", "student_id", "users_id", "category_id"};
        String[] value = {PreferenceManager.getAccessToken(mContext), stud_id, PreferenceManager.getUserId(mContext), categoryId};
        System.out.println("Response paramete"+PreferenceManager.getAccessToken(mContext)+stud_id+PreferenceManager.getUserId(mContext)+categoryId);
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
                            JSONArray dataArray = secobj.getJSONArray(JTAG_RESPONSE_DATA_ARRAY);
                            if (dataArray.length() > 0) {
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject dataObject = dataArray.getJSONObject(i);
                                    installPos=i;
                                    mListViewArray.add(getSearchValues(dataObject));


//                                    JSONArray installmentArray = dataObject.getJSONArray(JTAG_RESPONSE_INSTALLMENT_ARRAY);
//                                    for (int j = 0; j < installmentArray.length(); j++) {
//                                        JSONObject instaObject = installmentArray.getJSONObject(j);
//                                        InstallmentArray.add(getInstallmentValues(instaObject));
//
//
//                                    }
//
//                                    JSONArray fullpaymentArray = dataObject.getJSONArray(JTAG_RESPONSE_FULLPAYMENT_ARRAY);
//                                    for (int k = 0; k < fullpaymentArray.length(); k++) {
//                                        JSONObject instaObject = fullpaymentArray.getJSONObject(k);
//                                        FullpaymentArray.add(getfullpaymentValues(instaObject));
//
//
//                                    }



                                }

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
//                            if (AppUtils.isNetworkConnected(mContext)) {
//                                tripSubmiotApi(URL_TRIP_SUBMISSION,stud_id);
//                            } else {
//                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
//
//                            }
                        }
                    } else if (response_code.equalsIgnoreCase("500")) {

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
        mTripListModel.setInstallment(Object.getString("installment"));
        mTripListModel.setPayment_option(Object.getString("payment_option"));
        mTripListModel.setRemaining_amount(Object.getString("remaining_amount"));
        mTripListModel.setDescription(Object.getString("description"));
        mTripListModel.setTrip_date(Object.getString("trip_date"));
        mTripListModel.setAmount(Object.getString("amount"));
        mTripListModel.setTrip_date_staus(Object.getString("trip_date_staus"));
        mTripListModel.setStatus(Object.getString("status"));
        mTripListModel.setStudentName(Object.getString("studentName"));
        mTripListModel.setParentName(Object.getString("parentName"));
        mTripListModel.setPayment_status(Object.getString("payment_status"));
        mTripListModel.setClosing_date(Object.getString("closing_date"));
        mTripListModel.setLast_paid_date(Object.getString("last_paid_date"));
        mTripListModel.setPayment_date_status(Object.getString("payment_date_status"));
        mTripListModel.setBillingCode(Object.getString("isams_no"));
        JSONArray installmentArray = Object.getJSONArray(JTAG_RESPONSE_INSTALLMENT_ARRAY);
        installmentArrayList = new ArrayList<>();
        if (installmentArray.length() > 0) {
            for (int j = 0; j < installmentArray.length(); j++) {
                JSONObject Objectsec = installmentArray.getJSONObject(j);
                InstallmentListModel nTripListModel = new InstallmentListModel();
                nTripListModel.setInstallment_id(Objectsec.getString("installment_id"));
                nTripListModel.setInvoiceNote(Objectsec.getString("invoice_note"));
                nTripListModel.setPaid_amount(Objectsec.getString("paid_amount"));
                nTripListModel.setInst_amount(Objectsec.getString("inst_amount"));
                nTripListModel.setPaid_status(Objectsec.getString("paid_status"));
                nTripListModel.setPayment_option(Objectsec.getString("payment_option"));
                nTripListModel.setLast_payment_date(Objectsec.getString("last_payment_date"));
                nTripListModel.setPaid_by(Objectsec.getString("paid_by"));
                nTripListModel.setPayment_type(Objectsec.getString("payment_type"));
                nTripListModel.setPaid_date(Objectsec.getString("paid_date"));
                nTripListModel.setOrder_id(Objectsec.getString("order_id"));
                nTripListModel.setInst_date_status(Objectsec.getString("inst_date_status"));
                installmentArrayList.add(nTripListModel);
                Log.e("ggggggg", String.valueOf(installmentArrayList));
            }


        }

        JSONArray fullpaymentArray = Object.getJSONArray(JTAG_RESPONSE_FULLPAYMENT_ARRAY);
        fullpaymentArrayList = new ArrayList<>();
        if (fullpaymentArray.length() > 0) {
            for (int k = 0; k < fullpaymentArray.length(); k++) {
                JSONObject Objects = fullpaymentArray.getJSONObject(k);
                FullPaymentListModel oTripListModel = new FullPaymentListModel();
                oTripListModel.setInvoiceNote(Objects.getString("invoice_note"));
                oTripListModel.setLast_payment_date(Objects.getString("last_payment_date"));
                oTripListModel.setPaid_amount(Objects.getString("paid_amount"));
                oTripListModel.setPaid_by(Objects.getString("paid_by"));
                oTripListModel.setPayment_type(Objects.getString("payment_type"));
                oTripListModel.setPaid_date(Objects.getString("paid_date"));
                oTripListModel.setOrder_id(Objects.getString("order_id"));
                fullpaymentArrayList.add(oTripListModel);
            }

        }


        mTripListModel.setInstallmentArrayList(installmentArrayList);
        mTripListModel.setFullpaymentArrayList(fullpaymentArrayList);

        return mTripListModel;
    }
    public void tripSubmiotApi(final String URLHEAD, final String studentId) {

        TripSubmit mTripSubmit = new TripSubmit(URLHEAD, studentId);
        mTripSubmit.execute();


    }

    class TripSubmit extends AsyncTask<Void, Void, Void> {
        List<Trip> mTripList;
        String url;
        String studentId;

        TripSubmit(String urlHead, String mStudentId) {
            this.url = urlHead;
            this.studentId = mStudentId;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            mTripList = DatabaseClient.getInstance(mContext).getAppDatabase()
                    .tripDao().getTripUnsyncWithId("0", studentId, PreferenceManager.getUserId(mContext));//trip with status zero
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
                tripSync(url);
            }


        }

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


                            /*    List<Trip> tripWithId = DatabaseClient.getInstance(mContext).getAppDatabase()
                                        .tripDao().getTripUnsyncWithId("0", stud_id
                                                , PreferenceManager.getUserId(mContext));
                                for (int i = 0; i < tripWithId.size(); i++) {
//                                    tripWithId.get(i).setStatus("1");
//                                    DatabaseClient.getInstance(mContext).getAppDatabase()
//                                            .tripDao().update(tripWithId.get(i));
                                    DatabaseClient.getInstance(mContext).getAppDatabase()
                                            .tripDao().delete(tripWithId.get(i));

                                }*/

                                DeleteQueryAsyncTask mDeleteQueryAsyncTask = new DeleteQueryAsyncTask(stud_id);
                                mDeleteQueryAsyncTask.execute();
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        if (AppUtils.isNetworkConnected(mContext)) {
//                                            getReportListAPI(URL_TRIP_LIST);
//                                        } else {
//                                            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
//
//                                        }
//                                    }
//                                });
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

                        } else if (AppUtils.isNetworkConnected(mContext)) {

                                    getReportListAPI(URL_TRIP_LIST);


                        } else {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                        }
                    } catch (Exception ex) {
                        System.out.println("The Exception in edit profile sssis" + ex.toString());
                    }

                }

                @Override
                public void responseFailure(String failureResponse) {

                }
            });
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        AppUtils.hideKeyBoard(mContext);
        if (AppUtils.isNetworkConnected(mContext)) {
            getReportListAPI(URL_TRIP_LIST);
        } else {
            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

        }
    }

    class DeleteQueryAsyncTask extends AsyncTask<Void, Void, Void> {

        String studentId = "";

        DeleteQueryAsyncTask(String mStudentId) {
            this.studentId = mStudentId;
        }

        @Override
        protected Void doInBackground(Void... voids) {
//            Trip tripWithId = DatabaseClient.getInstance(mContext).getAppDatabase()
//                    .tripDao().getTripWithId(tripId, studentId, PreferenceManager.getUserId(mContext));


//            DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
//                    .tripDao().delete(tripWithId);
            List<Trip> tripWithId = DatabaseClient.getInstance(mContext).getAppDatabase()
                    .tripDao().getTripUnsyncWithId("0", stud_id
                            , PreferenceManager.getUserId(mContext));
            for (int i = 0; i < tripWithId.size(); i++) {
//                                    tripWithId.get(i).setStatus("1");
//                                    DatabaseClient.getInstance(mContext).getAppDatabase()
//                                            .tripDao().update(tripWithId.get(i));
                DatabaseClient.getInstance(mContext).getAppDatabase()
                        .tripDao().delete(tripWithId.get(i));

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (AppUtils.isNetworkConnected(mContext)) {
                getReportListAPI(URL_TRIP_LIST);
            } else {
                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

            }
        }

    }

}