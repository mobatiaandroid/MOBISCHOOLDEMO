package com.mobatia.naisapp.activities.terms_of_service;

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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.home.HomeListActivity;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
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
public class TermsOfServiceActivity extends Activity implements URLConstants,JSONConstants{
    Context mContext=this;
    RelativeLayout relativeHeader,mProgressRelLayout;
    HeaderManager headermanager;
    ImageView back;
    ImageView home;
    Bundle extras;
    String tab_type;
   WebView web;
    RotateAnimation anim;
    private WebSettings mwebSettings;
    private boolean loadingFlag = true;
    private boolean mErrorFlag = false;
    String termsTitle;
    String termsDescription;
    String mLoadData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_of_service);
        initUI();
        if(AppUtils.isNetworkConnected(mContext)){
            callAboutUsApi(URL_TERMS_OF_SERVICE);
        }else{
            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

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
        headermanager = new HeaderManager(TermsOfServiceActivity.this, tab_type);
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
        mProgressRelLayout.setVisibility(View.VISIBLE);
        anim = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(mContext, android.R.interpolator.linear);
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(1000);
        mProgressRelLayout.setAnimation(anim);
        mProgressRelLayout.startAnimation(anim);
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
                            JSONObject data = secobj.optJSONObject("data");
                            termsTitle=data.optString("title");
                            termsDescription=data.optString("description");
                             mLoadData= "<!DOCTYPE html>\n" +
                                    "<html>\n" +
                                    "<head>\n" +
                                    "<style>\n" +
                                    "\n" +
                                    "@font-face {\n" +
                                    "font-family: SourceSansPro-Semibold;"+
                                    "src: url(SourceSansPro-Semibold.otf);"+

                                    "font-family: SourceSansPro-Regular;"+
                                    "src: url(SourceSansPro-Regular.otf);"+
                                    "}"+
                                    ".title {"+
                                    "font-family: SourceSansPro-Regular;"+
                                    "font-size:16px;"+
                                    "text-align:left;"+
                                    "color:	#46C1D0;"+
                                    "text-align: ####TEXT_ALIGN####;"+
                                    "}"+

                                    ".description {"+
                                    "font-family: SourceSansPro-Light;"+
                                    "text-align:justify;"+
                                    "font-size:14px;"+
                                    "color: #000000;"+
                                    "text-align: ####TEXT_ALIGN####;"+
                                    "}"+
                                    "</style>\n"+"</head>"+
                                    "<body>"+
                                    "<p class='title'>"+termsTitle+"</p>"+"<p class='description'>"+termsDescription+"</p>"+
                                    "</body>\n</html>";
                            getWebViewSettings();

                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                        mProgressRelLayout.clearAnimation();
                        mProgressRelLayout.setVisibility(View.GONE);
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    } else if (response_code.equalsIgnoreCase("400")) {

                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        callAboutUsApi(URL_TERMS_OF_SERVICE);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        callAboutUsApi(URL_TERMS_OF_SERVICE);

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        callAboutUsApi(URL_TERMS_OF_SERVICE);

                    } else {
                        mProgressRelLayout.clearAnimation();
                        mProgressRelLayout.setVisibility(View.GONE);
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
                mProgressRelLayout.clearAnimation();
                mProgressRelLayout.setVisibility(View.GONE);
                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

            }
        });


    }


    private void getWebViewSettings() {



        web.setFocusable(true);
        web.setFocusableInTouchMode(true);
        web.setBackgroundColor(0X00000000);
        web.setVerticalScrollBarEnabled(false);
        web.setHorizontalScrollBarEnabled(false);
        web.setWebChromeClient(new WebChromeClient());
//        int sdk = Build.VERSION.SDK_INT;
//        if (sdk >= Build.VERSION_CODES.JELLY_BEAN) {
//            mWebView.setBackgroundColor(Color.argb(1, 0, 0, 0));
//        }
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

//		refreshBtn.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				GetWebUrlAsyncTask getWebUrl = new GetWebUrlAsyncTask(WEB_CONTENT_URL
//						+ mType, WEB_CONTENT + "/" + mType, 1, mTAB_ID);
//				getWebUrl.execute();
//			}
//		});

        web.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
               // view.loadData(mLoadData, "text/html", "UTF-8");
view.loadData(mLoadData, "text/html; charset=utf-8", "utf-8");
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                mProgressRelLayout.clearAnimation();
                mProgressRelLayout.setVisibility(View.GONE);
                if (AppUtils.checkInternet(mContext) && loadingFlag) {
                    view.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
//                    view.loadUrl(url);
                    //view.loadData(mLoadData, "text/html", "UTF-8");
                    view.loadData(mLoadData, "text/html; charset=utf-8", "utf-8");

                    loadingFlag = false;
                } else if (!AppUtils.checkInternet(mContext) && loadingFlag) {
                    view.getSettings()
                            .setCacheMode(WebSettings.LOAD_CACHE_ONLY);
//                    view.loadUrl(url);
                   // view.loadData(mLoadData, "text/html", "UTF-8");
                   // view.loadData(mLoadData, "text/html; charset=utf-8", "utf-8");
                    view.loadDataWithBaseURL("file:///android_asset/", mLoadData, "text/html; charset=utf-8", "utf-8", "about:blank");


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

        if (mLoadData.equals("")) {

            mErrorFlag = true;
        } else {
            mErrorFlag = false;
        }
        if (mLoadData != null && !mErrorFlag) {
            //web.loadData(mLoadData, "text/html", "UTF-8");
           // web.loadData(mLoadData, "text/html; charset=utf-8", "utf-8");
            web.loadDataWithBaseURL("file:///android_asset/", mLoadData, "text/html; charset=utf-8", "utf-8", "about:blank");


        } else {
            mProgressRelLayout.clearAnimation();
            mProgressRelLayout.setVisibility(View.GONE);
            AppUtils.showAlertFinish((Activity) mContext, getResources()
                            .getString(R.string.common_error_loading_page), "",
                    getResources().getString(R.string.ok), false);
        }

    }
}
