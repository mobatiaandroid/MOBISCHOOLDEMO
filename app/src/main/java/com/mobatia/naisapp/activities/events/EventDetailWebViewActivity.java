package com.mobatia.naisapp.activities.events;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.home.HomeListActivity;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.activities.pdf.PdfReaderActivity;
import com.mobatia.naisapp.constants.CacheDIRConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.HeaderManager;

/**
 * Created by gayatri on 20/3/17.
 */
public class EventDetailWebViewActivity extends Activity implements
        CacheDIRConstants, NaisClassNameConstants {

    private Context mContext;
    private WebView mWebView;
    private RelativeLayout mProgressRelLayout;
    private WebSettings mwebSettings;
    private boolean loadingFlag = true;
    private String mLoadUrl = null;
    private String message="";
    private String link="";
    String pdf;
    String tab_type;
    private boolean mErrorFlag = false;
    Bundle extras;
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back,share,home,infoImg;
    RotateAnimation anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comingup_detailweb_view_layout);
        mContext = this;
        extras = getIntent().getExtras();
        if (extras != null) {
            mLoadUrl = extras.getString("webViewComingDetail");
            link=extras.getString("link");
            message=extras.getString("message");
            pdf=extras.getString("pdf");
            tab_type=extras.getString("tab_type");
        }
//		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(mContext));
        initialiseUI();
        getWebViewSettings();
    }


    /*******************************************************
     * Method name : initialiseUI Description : initialise UI elements
     * Parameters : nil Return type : void Date : Oct 30, 2014 Author : Vandana
     * Surendranath
     *****************************************************/
    private void initialiseUI() {
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        mWebView = (WebView) findViewById(R.id.webView);
        mProgressRelLayout = (RelativeLayout) findViewById(R.id.progressDialog);
        headermanager = new HeaderManager(EventDetailWebViewActivity.this, tab_type);
        if (pdf.equalsIgnoreCase(""))
        {
            headermanager.getHeader(relativeHeader, 0);

        }
        else {
            headermanager.getHeader(relativeHeader, 3);
            infoImg=headermanager.getRightInfoImage();
            infoImg.setImageResource(R.drawable.pdfdownloadbutton);
            infoImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!pdf.equals("")) {
                        Intent intent = new Intent(mContext, PdfReaderActivity.class);
                        intent.putExtra("pdf_url", pdf);
                        startActivity(intent);
                    }
                }
            });
        }
        back = headermanager.getLeftButton();
        share=headermanager.getRightButton();

        headermanager.setButtonRightSelector(R.drawable.shareicon, R.drawable.shareicon);
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


        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(link.equalsIgnoreCase("")&&message.equalsIgnoreCase("")){
                    share.setVisibility(View.GONE);
                }else if(link.equalsIgnoreCase("")&&!message.equalsIgnoreCase("")){
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                    share.putExtra(Intent.EXTRA_TEXT, message);
                    startActivity(Intent.createChooser(share, "Share"));
                }else if(!link.equalsIgnoreCase("")){
                    /*Intent intent = new Intent(Intent.ACTION_SEND);
                    Uri uri = Uri.parse(link);
                    intent.setType("image*//*");
                    intent.putExtra(Intent.EXTRA_STREAM, String.valueOf(uri));
                    startActivity(Intent.createChooser(intent, "Share"));*/
                    Intent i = new Intent(Intent.ACTION_SEND); i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_TEXT, link);
                    startActivity(Intent.createChooser(i, "Share"));
                }else{
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    Uri uri = Uri.parse(link);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_STREAM, String.valueOf(uri));
                    intent.putExtra(Intent.EXTRA_TEXT, message);
                    startActivity(Intent.createChooser(intent, "Share"));
                }
            }
        });
    }

    /*******************************************************
     * Method name : getWebViewSettings Description : get web view settings
     * Parameters : nil Return type : void Date : Oct 31, 2014 Author : Vandana
     * Surendranath
     *****************************************************/
    private void getWebViewSettings() {


        mProgressRelLayout.setVisibility(View.VISIBLE);
        anim = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(mContext, android.R.interpolator.linear);
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(1000);
        mProgressRelLayout.setAnimation(anim);
        mProgressRelLayout.startAnimation(anim);
        mWebView.setFocusable(true);
        mWebView.setFocusableInTouchMode(true);
        mWebView.setBackgroundColor(0X00000000);
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setWebChromeClient(new WebChromeClient());

        mwebSettings = mWebView.getSettings();
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

        mWebView.getSettings().setAppCacheMaxSize(10 * 1024 * 1024); // 5MB
        mWebView.getSettings().setAppCachePath(
                mContext.getCacheDir().getAbsolutePath());
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings()
                .setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        mWebView.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadDataWithBaseURL("file:///android_asset/", mLoadUrl, "text/html; charset=utf-8", "utf-8", "about:blank");

                return true;
            }

            public void onPageFinished(WebView view, String url) {
                mProgressRelLayout.clearAnimation();
                mProgressRelLayout.setVisibility(View.GONE);
                if (AppUtils.checkInternet(mContext) && loadingFlag) {
                    view.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                    view.loadDataWithBaseURL("file:///android_asset/", mLoadUrl, "text/html; charset=utf-8", "utf-8", "about:blank");

                    loadingFlag = false;
                } else if (!AppUtils.checkInternet(mContext) && loadingFlag) {
                    view.getSettings()
                            .setCacheMode(WebSettings.LOAD_CACHE_ONLY);

                    view.loadDataWithBaseURL("file:///android_asset/", mLoadUrl, "text/html; charset=utf-8", "utf-8", "about:blank");

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

        if (mLoadUrl.equals("")) {

            mErrorFlag = true;
        } else {
            mErrorFlag = false;
        }
        if (mLoadUrl != null && !mErrorFlag) {
            System.out.println("NAS load url " + mLoadUrl);
            mWebView.loadDataWithBaseURL("file:///android_asset/", mLoadUrl, "text/html; charset=utf-8", "utf-8", "about:blank");

        } else {
            mProgressRelLayout.clearAnimation();
            mProgressRelLayout.setVisibility(View.GONE);
            AppUtils.showAlertFinish((Activity) mContext, getResources()
                            .getString(R.string.common_error_loading_page), "",
                    getResources().getString(R.string.ok), false);
        }

    }

}
