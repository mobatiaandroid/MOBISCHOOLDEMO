package com.mobatia.naisapp.fragments.canteen;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CanteenPaymentActivity extends Activity implements URLConstants, JSONConstants, StatusConstants {
    Context mContext;
    String postBody;
    String orderId="";
    String postUrl="";
    String sessionId="";
    String sessionVersion="";
    String fullHtml;
    String merchantpassword="16496a68b8ac0fb9b6fde61274272457";
    WebView paymentWeb;
    private RelativeLayout mProgressRelLayout;
    RotateAnimation anim;
    Bundle extras;
    public final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/octet-stream");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.canteen_payment_detail);
        mContext = this;
        initUI();
        //"NASDUBAI" + mListViewArray.get(position).getId() + "S" + stud_id
    }
    public void initUI()
    {
        extras = getIntent().getExtras();
        if (extras != null) {
            orderId=extras.getString("orderId");
        }
        mProgressRelLayout = (RelativeLayout) findViewById(R.id.progressDialog);
        paymentWeb=findViewById(R.id.paymentWeb);
        postBody = "{\n" +
                "\"apiOperation\":\"CREATE_CHECKOUT_SESSION\",\n" +
                "\"order\":{\n" +
                "\"currency\":\"AED\",\n" +
                "\"id\":\"" + orderId + "\"\n" +
                "}\n" +
                "}";
        try {
            postRequest(PreferenceManager.getSessionUrl(mContext), postBody, merchantpassword,orderId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    void postRequest(final String postUrl, final String postBody, String merchantPassword, final String orderId) throws IOException {

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
//                            System.out.println("session Id " + sessionId);
//                            System.out.println("session version" + sessionVersion);


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() { // Stuff that updates the UI } });
                                    // closedImg.setVisibility(View.GONE);
                                    if (sessionId.equalsIgnoreCase("")) {
                                        try {
                                            postRequest(postUrl, postBody, merchantpassword,orderId);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    } else {

                                        setWebViewSettings();
                                        loadWebViewWithData();
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
        paymentWeb.setWebViewClient(new CanteenPaymentActivity.MyWebViewClient());
        paymentWeb.setWebChromeClient(new CanteenPaymentActivity.MyWebChromeClient());
    }
    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            //Calling a javascript function in html page
//            view.loadUrl("javascript:alert(showVersion('called by Android'))");
            mProgressRelLayout.clearAnimation();
            mProgressRelLayout.setVisibility(View.GONE);
            view.loadUrl("javascript:Checkout.showLightbox()");
            //  Log.d("WebView", "your current url when webpage finished." + url);
            if (url.startsWith("https://network.gateway.mastercard.com/checkout/receipt/")) {
                goToNextView();
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            // Log.d("WebView", "your current url when webpage loading.." + url);
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

            //   Log.d("WebView", "print webpage loading.." + url);

        }
    }

    /*https://network.gateway.mastercard.com/checkout/receipt/SESSION0002030093378I80793691G9?resultIndicator=3c9926449c9f42cf&sessionVersion=f7b077bf08*/
    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {

            //   Log.d("LogTag", message);
            result.confirm();
            return true;
        }

        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            if (consoleMessage.message().contains("completeCallback")) {
                //System.out.println("completeCallback");
                //saveTask();
                if (AppUtils.isNetworkConnected(mContext)) {
                   // tripSubmiotApi(URL_TRIP_SUBMISSION);
                } else {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                }
            } else if (consoleMessage.message().contains("cancelCallback")) {
                System.out.println("cancelCallback");
                finish();
            } else if (consoleMessage.message().contains("errorCallback")) {
                System.out.println("errorCallback");
            }


            return super.onConsoleMessage(consoleMessage);
        }
    }
    void loadWebViewWithData() {
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
            fullHtml = fullHtml.replace("####amount####", "100");
            fullHtml = fullHtml.replace("######orderId#####", orderId);
            fullHtml = fullHtml.replace("##merchantId##", PreferenceManager.getMerchantId(mContext));
            fullHtml = fullHtml.replace("######session_id#####", sessionId);
            fullHtml = fullHtml.replace("###payment_url###", PreferenceManager.getPaymentUrl(mContext));
            fullHtml = fullHtml.replace("###merchant_name###", PreferenceManager.getMerchantName(mContext));
            fullHtml = fullHtml.replace("#####title#####", "NAS DUBAI");

//            Log.d("LOG3","fullHtml = "+fullHtml);
            paymentWeb.loadDataWithBaseURL("", fullHtml, "text/html", "UTF-8", "");
//            paymentWeb.loadUrl("javascript:Checkout.showLightbox();");

        }

    }
    private void goToNextView() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
//                startService(new Intent(getBaseContext(), OnClearFromRecentService.class));
              //  saveTask();

            }
        }, 10000);
    }
}
