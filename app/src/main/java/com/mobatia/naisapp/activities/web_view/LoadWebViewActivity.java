package com.mobatia.naisapp.activities.web_view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.home.HomeListActivity;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.activities.staff_directory.adapter.CustomStaffDirectoryAdapter;
import com.mobatia.naisapp.activities.staff_directory.model.StaffModel;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.about_us.model.AboutusModel;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.HeaderManager;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.volleywrappermanager.CustomDialog;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by gayatri on 20/3/17.
 */
public class LoadWebViewActivity extends Activity implements URLConstants,JSONConstants{
    Context mContext=this;
    RelativeLayout relativeHeader,mProgressRelLayout;
    HeaderManager headermanager;
    ImageView back;
    ImageView home;
    Bundle extras;
    String tab_type;
    ArrayList<AboutusModel> aboutusModelArrayList=new ArrayList<>();
    WebView web;
    ProgressBar progressBar;
    RotateAnimation anim;
    private WebSettings mwebSettings;
    private boolean loadingFlag = true;
    String mLoadUrl;
    private boolean mErrorFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_aboutus);
        initUI();
        if(AppUtils.isNetworkConnected(mContext)){
            callAboutUsApi(URL_ABOUTUS_LIST);
        }else{
            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.round);

        }
    }

    private void initUI() {
        extras=getIntent().getExtras();
        if(extras!=null){
            tab_type=extras.getString("tab_type");
        }
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        web = (WebView) findViewById(R.id.webView);
        mProgressRelLayout= (RelativeLayout) findViewById(R.id.progressDialog);
        //web.setWebViewClient(new myWebClient());
        web.getSettings().setJavaScriptEnabled(true);
        headermanager = new HeaderManager(LoadWebViewActivity.this, tab_type);
        headermanager.getHeader(relativeHeader, 0);
        back = headermanager.getLeftButton();
        headermanager.setButtonLeftSelector(R.drawable.back,
                R.drawable.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //AppUtils.hideKeyBoard(mContext);
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
    }

    private void callAboutUsApi(String URL) {
        VolleyWrapper volleyWrapper=new VolleyWrapper(URL);
        String[] name={"access_token"};
        String[] value={PreferenceManager.getAccessToken(mContext)};
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
                                    JSONObject datObject = data.getJSONObject(i);
                                    aboutusModelArrayList.add(addAoutUsDetails(datObject));
                                    if (tab_type.equals(aboutusModelArrayList.get(i).getTabType())) {
                                        System.out.println("Tab type---" + tab_type);
                                        mLoadUrl=aboutusModelArrayList.get(i).getUrl();
                                        getWebViewSettings();
                                    }
                                }
                            } else {
                                Toast.makeText(LoadWebViewActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        callAboutUsApi(URL_GET_ABOUTUS_LIST);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        callAboutUsApi(URL_GET_ABOUTUS_LIST);

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        callAboutUsApi(URL_GET_ABOUTUS_LIST);

                    } else {
                        CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
                                , getResources().getString(R.string.ok));
                        dialog.show();
                    }
                } catch (Exception ex) {
                    System.out.println("The Exception in edit profile is" + ex.toString());
                }

            }

            @Override
            public void responseFailure(String failureResponse) {
                CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
                        , getResources().getString(R.string.ok));
                dialog.show();
            }
        });


    }

    private AboutusModel addAoutUsDetails(JSONObject obj) {
        AboutusModel model = new AboutusModel();
        try {
            model.setId(obj.optString("id"));
            model.setTabType(obj.optString("tab_type"));
            model.setUrl(obj.optString("url"));
        } catch (Exception ex) {
            System.out.println("Exception is" + ex);
        }

        return model;
    }

    private void getWebViewSettings() {


        mProgressRelLayout.setVisibility(View.VISIBLE);
        anim = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(mContext, android.R.interpolator.linear);
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(1000);
        mProgressRelLayout.setAnimation(anim);
        mProgressRelLayout.startAnimation(anim);
        web.setFocusable(true);
        web.setFocusableInTouchMode(true);
        web.setBackgroundColor(0X00000000);
        web.setVerticalScrollBarEnabled(true);
        web.setHorizontalScrollBarEnabled(true);
        web.setWebChromeClient(new WebChromeClient());
        int sdk = Build.VERSION.SDK_INT;
        if (sdk >= Build.VERSION_CODES.JELLY_BEAN) {
            web.setBackgroundColor(Color.argb(1, 0, 0, 0));
        }
        mwebSettings = web.getSettings();
        mwebSettings.setSaveFormData(true);
        mwebSettings.setBuiltInZoomControls(false);
        mwebSettings.setSupportZoom(false);

        mwebSettings.setPluginState(WebSettings.PluginState.ON);
        mwebSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        mwebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        mwebSettings.setDomStorageEnabled(true);
        mwebSettings.setDatabaseEnabled(true);
        mwebSettings.setDefaultTextEncodingName("utf-8");
        mwebSettings.setLoadsImagesAutomatically(true);

        web.getSettings().setAppCacheMaxSize(10 * 1024 * 1024); // 5MB
        web.getSettings().setAppCachePath(
                mContext.getCacheDir().getAbsolutePath());
        web.getSettings().setAllowFileAccess(true);
        web.getSettings().setAppCacheEnabled(true);
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings()
                .setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);



        web.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                mProgressRelLayout.clearAnimation();
                mProgressRelLayout.setVisibility(View.GONE);
                if (AppUtils.checkInternet(mContext) && loadingFlag) {
                    view.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                   // view.loadUrl(url);
                    loadingFlag = false;
                } else if (!AppUtils.checkInternet(mContext) && loadingFlag) {
                    view.getSettings()
                            .setCacheMode(WebSettings.LOAD_CACHE_ONLY);
                   // view.loadUrl(url);
                    System.out.println("CACHE LOADING");
                    loadingFlag = false;
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }

            /*
             * (non-Javadoc)
             *
             * @see
             * android.webkit.WebViewClient#onReceivedError(android.webkit.WebView
             * , int, java.lang.String, java.lang.String)
             */
            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                mProgressRelLayout.clearAnimation();
                mProgressRelLayout.setVisibility(View.GONE);
                if (AppUtils.checkInternet(mContext)) {
                    AppUtils.showAlertFinish((Activity) mContext, getResources()
                                    .getString(R.string.common_error), "",
                            getResources().getString(R.string.ok), false);
                }

                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        });
        if (mLoadUrl.endsWith(".pdf")) {
            mLoadUrl = "http://docs.google.com/gview?embedded=true&url=" + mLoadUrl;
//             mLoadUrl="<iframe src="+"'http://docs.google.com/gview?embedded=true&url="+AppUtils.replace(mLoadUrl)+"'width='100%' height='100%'style='border: none;'></iframe>";

        }
        if (mLoadUrl.equals("")) {

            mErrorFlag = true;
        } else {
            mErrorFlag = false;
        }
        if (mLoadUrl != null && !mErrorFlag) {
            System.out.println("NAS load url " + mLoadUrl);
            web.loadUrl(mLoadUrl);
        } else {
            mProgressRelLayout.clearAnimation();
            mProgressRelLayout.setVisibility(View.GONE);
            AppUtils.showAlertFinish((Activity) mContext, getResources()
                            .getString(R.string.common_error_loading_pdf), "",
                    getResources().getString(R.string.ok), false);
        }
//		GetWebUrlAsyncTask getWebUrl = new GetWebUrlAsyncTask(WEB_CONTENT_URL
//				+ mType, WEB_CONTENT + "/" + mType, 1, mTAB_ID);
//		getWebUrl.execute();
    }
}
