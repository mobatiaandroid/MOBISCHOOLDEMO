package com.mobatia.naisapp.fragments.canteen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.activities.payment_history.PaymentHistoryActivity;
import com.mobatia.naisapp.activities.payment_history.PaymentHistoryNew;
import com.mobatia.naisapp.activities.trips.database.DatabaseClient;
import com.mobatia.naisapp.activities.trips.database.Trip;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.canteen.database.Canteen;
import com.mobatia.naisapp.fragments.trips.TripListModel;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.HeaderManager;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CanteenActviity extends Activity implements URLConstants, JSONConstants, StatusConstants {
    Bundle extras;
    String orderId;
    String tab_type = "";
    Context mContext;
    public String merchantpassword = "16496a68b8ac0fb9b6fde61274272457";
    public String postUrl;
    public String postBody = "";
    String fullHtml;
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back;
    ImageView home;
    String sessionId = "";
    String sessionVersion = "";
    WebView paymentWeb;
    private RelativeLayout mProgressRelLayout;
    private ArrayList<TripListModel> mListViewArray;
    RotateAnimation anim;
    public static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/octet-stream");
    String tripDetails = "";
    String stud_id = "";
    String amount = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.canteen_payment_detail);
        mContext = this;
        extras = getIntent().getExtras();
        if (extras != null)
        {
            orderId = extras.getString("orderId");
            stud_id = extras.getString("stud_id");
            amount = extras.getString("amount");
            //  Log.e( "order_id: ",orderId );
            //  Log.e( "stud_id: ",stud_id );
        }
           relativeHeader =  findViewById(R.id.relativeHeader);
        mProgressRelLayout = findViewById(R.id.progressDialog);
        paymentWeb = findViewById(R.id.paymentWeb);
        mProgressRelLayout.setVisibility(View.GONE);
        headermanager = new HeaderManager(this, "Canteen");
        headermanager.getHeader(relativeHeader, 4);
        back = headermanager.getLeftButton();
        headermanager.setButtonLeftSelector(R.drawable.back,
                R.drawable.back);
        LinearLayout back1 = headermanager.getLinearRight();
        back1.setVisibility(View.GONE);

        home = headermanager.getLogoButton();
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(mContext, HomeListAppCompatActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
//              proceed to pay
            }
        });
        postBody = "{\n" +
                "\"apiOperation\":\"CREATE_CHECKOUT_SESSION\",\n" +
                "\"order\":{\n" +
                "\"currency\":\"AED\",\n" +
                "\"id\":\"" + orderId + "\"\n" +
                "}\n" +
                "}";
        if (AppUtils.isNetworkConnected(mContext)) {

            try {
                postRequest(PreferenceManager.getSessionUrl(mContext), postBody, merchantpassword);
                Log.e("session_url: ", PreferenceManager.getSessionUrl(mContext));
                Log.e("post_body: ", postBody);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

        }


    }

    //    @SuppressLint("SetJavaScriptEnabled")
    private void setWebViewSettings() {
        mProgressRelLayout.setVisibility(View.VISIBLE);
        anim = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(mContext, android.R.interpolator.linear);
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(1000);
        mProgressRelLayout.setAnimation(anim);
        mProgressRelLayout.startAnimation(anim);
        paymentWeb.getSettings().setJavaScriptEnabled(true);
        paymentWeb.clearCache(true);
        paymentWeb.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        paymentWeb.getSettings().setSupportMultipleWindows(true);
        paymentWeb.setWebViewClient(new CanteenActviity.MyWebViewClient());
        paymentWeb.setWebChromeClient(new CanteenActviity.MyWebChromeClient());
    }


    void loadWebViewWithData(String title, String description) {
        StringBuffer sb = new StringBuffer();
        String eachLine = "";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(getAssets().open("paymentnew.html")));
            sb = new StringBuffer();
            eachLine = br.readLine();
            while (eachLine != null) {
                sb.append(eachLine);
                sb.append("\n");
                eachLine = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        fullHtml = sb.toString();
        if (fullHtml.length() > 0) {
            String merchant_name = PreferenceManager.getMerchantName(mContext);
            String payment_url = PreferenceManager.getPaymentUrl(mContext);
            String merchant_id = PreferenceManager.getMerchantId(mContext);
            fullHtml = fullHtml.replace("####amount####", amount);
            fullHtml = fullHtml.replace("######orderId#####", orderId);
            fullHtml = fullHtml.replace("##merchantId##", merchant_id);
            fullHtml = fullHtml.replace("######session_id#####", sessionId);
            fullHtml = fullHtml.replace("###payment_url###", payment_url);
            fullHtml = fullHtml.replace("###merchant_name###", merchant_name);
            fullHtml = fullHtml.replace("#####title#####", "NAS DUBAI");
            paymentWeb.loadDataWithBaseURL("", fullHtml, "text/html", "UTF-8", "");

        }

    }


    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            //Calling a javascript function in html page
//            view.loadUrl("javascript:alert(showVersion('called by Android'))");
            mProgressRelLayout.clearAnimation();
            mProgressRelLayout.setVisibility(View.GONE);
            view.loadUrl("javascript:Checkout.showLightbox()");
            Log.d("WebView", "your current url when webpage finished." + url);
            if (url.startsWith("https://network.gateway.mastercard.com/checkout/receipt/")) {
                goToNextView();
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            Log.d("WebView", "your current url when webpage loading.." + url);
//            if (url.startsWith("https://network.gateway.mastercard.com/checkout/receipt/"))
//            {
//                saveTask();
//            }
        }
    }


    private class MyPrintWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            //Calling a javascript function in html page

//            view.loadUrl(url);

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

            Log.d("WebView", "print webpage loading.." + url);

        }
    }

    /*https://network.gateway.mastercard.com/checkout/receipt/SESSION0002030093378I80793691G9?resultIndicator=3c9926449c9f42cf&sessionVersion=f7b077bf08*/
    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {

            Log.d("LogTag", message);
            result.confirm();
            return true;
        }

        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            if (consoleMessage.message().contains("completeCallback")) {
                System.out.println("completeCallback");
                saveTask();

            } else if (consoleMessage.message().contains("cancelCallback")) {
                finish();
            } else if (consoleMessage.message().contains("errorCallback")) {
                System.out.println("errorCallback");
            }


            return super.onConsoleMessage(consoleMessage);
        }
    }


    void postRequest(final String postUrl, final String postBody, String merchantPassword) throws IOException {

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON_MEDIA_TYPE, postBody);
        final Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .addHeader("authorization", PreferenceManager.getAuthId(mContext))
                .addHeader("postman-token", "0bc67956-9357-f12e-65d9-1da59f8c74d5")
                .addHeader("cache-control", "no-cache")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                System.out.println("postUrl::"+response);
//                System.out.println("orderId::"+orderId);

                if (response.isSuccessful()) {

                    try {
                        JSONObject obj = new JSONObject(response.body().string());
                        String result = obj.getString("result");
                        if (result.equalsIgnoreCase("SUCCESS")) {
                            final JSONObject sessionObj = obj.getJSONObject("session");
                            sessionId = sessionObj.getString("id");
                            sessionVersion = sessionObj.getString("version");
                            Log.e("sessionid: ", sessionId);
                            Log.e("sessionver: ", sessionVersion);
//                            System.out.println("session Id " + sessionId);
//                            System.out.println("session version" + sessionVersion);


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() { // Stuff that updates the UI } });
                                    if (sessionId.equalsIgnoreCase("")) {
                                        try {
                                            postRequest(PreferenceManager.getSessionUrl(mContext), postBody, merchantpassword);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    } else {

                                        setWebViewSettings();
                                        loadWebViewWithData("NAS DUBAI", "NAS DUBAI");
                                        Log.d("LOG3", fullHtml);
                                    }


                                }
                            });
                        }
                        System.out.println("result" + result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }
    public void tripSubmiotApi(final String URLHEAD, final String studentId) {

        CanteenActviity.TripSubmit mTripSubmit = new CanteenActviity.TripSubmit(URLHEAD, studentId);
        mTripSubmit.execute();
    }

    class TripSubmit extends AsyncTask<Void, Void, Void> {
        List<Canteen> mTripList;
        String url;
        String tripId;
        String studentId;

        TripSubmit(String urlHead, String mStudentId) {
            this.url = urlHead;
            this.studentId = mStudentId;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.e("doInBackground: ", studentId);
            Log.e("doInBackground: ", PreferenceManager.getUserId(mContext));
            mTripList = DatabaseClient.getInstance(mContext).getAppDatabase()
                    .canteenDao().getTripUnsyncWithId("0", studentId, PreferenceManager.getUserId(mContext), amount, orderId);//trip with status zero
            Gson gson = new Gson();
//                gson.toJson(mTripList);
//                Log.v("gson to json:", " " +  gson.toJson(mTripList));
            tripDetails = "{\"details\":" + gson.toJson(mTripList) + "}";
            Log.e("tripdetails: ", tripDetails);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (mTripList.size() > 0) {
                tripSync(url, tripId, studentId);
            }

        }

    }
    void tripSync(final String URLHEAD, final String tripId, final String studentId) {
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
                            JSONArray mJsonDataArray = secobj.optJSONArray("data");
                            Intent history = new Intent(CanteenActviity.this, PaymentHistoryNew.class);
                            startActivity(history);
                            finish();
                            //    CanteenActviity.DeleteQueryAsyncTask mDeleteQueryAsyncTask = new CanteenActviity.DeleteQueryAsyncTask(orderId, studentId, mJsonDataArray);
                            //    mDeleteQueryAsyncTask.execute();
                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                        // AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        tripSync(URLHEAD, tripId, studentId);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {

                            }
                        });
                        tripSync(URLHEAD, tripId, studentId);

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        tripSync(URLHEAD, tripId, studentId);

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

    public class DeleteQueryAsyncTask extends AsyncTask<Void, Void, Void> {

        String tripId = "";
        String studentId = "";
        JSONArray orderDataArray;

        DeleteQueryAsyncTask(String mTripId, String mStudentId, JSONArray mOrderDataArray) {
            this.tripId = mTripId;
            this.studentId = mStudentId;
            this.orderDataArray = mOrderDataArray;
        }

        @Override
        protected Void doInBackground(Void... voids) {
//            Trip tripWithId = DatabaseClient.getInstance(mContext).getAppDatabase()
//                    .tripDao().getTripWithId(tripId, studentId, PreferenceManager.getUserId(mContext));
            for (int i = 0; i < orderDataArray.length(); i++) {
                JSONObject mOrderData = orderDataArray.optJSONObject(i);

                if (mOrderData.optString("status").equalsIgnoreCase("success")) {
                    if (mOrderData.optString("orderId").equalsIgnoreCase(orderId)) {
                        JSONObject mDetail = mOrderData.optJSONObject("details");
                        // completed_dated=mDetail.optString("completed_dated");
                        // invoiceNote=mDetail.optString("invoiceNote");
                    }
                    DatabaseClient.getInstance(mContext).getAppDatabase()
                            .tripDao().deleteTripWithOrderId(mOrderData.optString("orderId"), PreferenceManager.getUserId(mContext));
                }
            }

//            DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
//                    .tripDao().delete(tripWithId);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void saveTask() {
        //    final String tTripId = trip_id;
        final String tStatus = "0";
        final String tStudentId = stud_id;

        class SaveTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                //creating a task
                Canteen task = new Canteen();
                // task.setTrip_id(tTripId);
                task.setStatus(tStatus);
                task.setStudent_id(tStudentId);
                task.setKeycode(orderId);
                task.setAmount(amount);
                task.setUsers_id(PreferenceManager.getUserId(mContext));

                //adding to database
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .canteenDao()
                        .insert(task);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (AppUtils.isNetworkConnected(mContext)) {
                    tripSubmiotApi(URL_CANTEEN_WALLET_SUBMISSION, tStudentId);
                } else {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                }

            }

        }

        SaveTask st = new SaveTask();
        st.execute();
    }


    private void goToNextView() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
//                startService(new Intent(getBaseContext(), OnClearFromRecentService.class));
                saveTask();

            }
        }, 10000);
    }


}