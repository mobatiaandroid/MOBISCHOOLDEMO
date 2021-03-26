package com.mobatia.naisapp.activities.pdf;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.web_view.LoadUrlWebViewActivity;
import com.mobatia.naisapp.constants.CacheDIRConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.fragments.parent_essentials.model.ParentEssentialsModel;
import com.mobatia.naisapp.manager.AppUtils;

import java.util.ArrayList;


public class PdfReaderNextActivity extends Activity implements
        CacheDIRConstants, NaisClassNameConstants {

    private Context mContext;
    private WebView mWebView;
    private RelativeLayout mProgressRelLayout;
    private WebSettings mwebSettings;
    private boolean loadingFlag = true;
    private String mLoadUrl = null,pdfUrl="";
    private boolean mErrorFlag = false;
    Bundle extras;
    ImageView backImageView,next,prev,pdfDownloadImgView;
    RotateAnimation anim;
    int pos=0;
    ArrayList<ParentEssentialsModel> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdf_with_next_view_layout);
        mContext = this;
        extras = getIntent().getExtras();
        if (extras != null) {
//            mLoadUrl = extras.getString("pdf_url");
            pos = extras.getInt("position");
            list = (ArrayList<ParentEssentialsModel>) extras.getSerializable("submenuArray");
            mLoadUrl =AppUtils.replace(list.get(pos).getFilename().replace("&", "%26"));
            pdfUrl = AppUtils.replace(list.get(pos).getFilename().replace("&", "%26"));

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

        mWebView = (WebView) findViewById(R.id.webView);
        mProgressRelLayout = (RelativeLayout) findViewById(R.id.progressDialog);
        backImageView = (ImageView) findViewById(R.id.backImageView);
        pdfDownloadImgView = (ImageView) findViewById(R.id.pdfDownloadImgView);

        prev = (ImageView) findViewById(R.id.prev);
        next = (ImageView) findViewById(R.id.next);
        if (list.size()<=1)
        {
            prev.setVisibility(View.GONE);
            next.setVisibility(View.GONE);
        }
        else
        {
            prev.setVisibility(View.VISIBLE);
            next.setVisibility(View.VISIBLE);
        }
        backImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        prev.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pos=pos-1;
                if (pos<0)
                {
                    pos=list.size()-1;
                }
                mLoadUrl = AppUtils.replace(list.get(pos).getFilename().replace("&", "%26"));
                pdfUrl = AppUtils.replace(list.get(pos).getFilename().replace("&", "%26"));
                if (mLoadUrl.endsWith(".pdf")) {
                    getWebViewSettings();
                }
                else
                {
                    Intent intent = new Intent(mContext, LoadUrlWebViewActivity.class);
                    intent.putExtra("url", list.get(pos).getFilename());
                    intent.putExtra("tab_type", list.get(pos).getSubmenu());
                    startActivity(intent);
                    finish();
                }

            }
        });
        next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pos= pos+1;
                if (pos>=list.size())
                {
                    pos=0;
                }
                mLoadUrl = AppUtils.replace(list.get(pos).getFilename().replace("&", "%26"));
                pdfUrl = AppUtils.replace(list.get(pos).getFilename().replace("&", "%26"));
                if (mLoadUrl.endsWith(".pdf"))
                {
                    getWebViewSettings();

                }
                else
                {
                    Intent intent = new Intent(mContext, LoadUrlWebViewActivity.class);
                    intent.putExtra("url", list.get(pos).getFilename());
                    intent.putExtra("tab_type", list.get(pos).getSubmenu());
                    startActivity(intent);
                    finish();
                }
            }
        });
        pdfDownloadImgView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(pdfUrl)));

                }catch (Exception e)
                {
                    e.printStackTrace();
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
        mWebView.setBackgroundColor(0Xffffffff);
        mWebView.setVerticalScrollBarEnabled(true);
        mWebView.setHorizontalScrollBarEnabled(true);
        if (Build.VERSION.SDK_INT >= 19) {
            mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        else {
            mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        mWebView.setWebChromeClient(new WebChromeClient());
        int sdk = Build.VERSION.SDK_INT;
        if (sdk >= Build.VERSION_CODES.JELLY_BEAN) {
            mWebView.setBackgroundColor(Color.argb(1, 0, 0, 0));
        }
        mwebSettings = mWebView.getSettings();
        mwebSettings.setSaveFormData(true);
        mwebSettings.setBuiltInZoomControls(false);
        mwebSettings.setSupportZoom(false);

        mwebSettings.setPluginState(PluginState.ON);
        mwebSettings.setRenderPriority(RenderPriority.HIGH);
        mwebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        mwebSettings.setDomStorageEnabled(true);
        mwebSettings.setDatabaseEnabled(true);
        mwebSettings.setDefaultTextEncodingName("utf-8");
        mwebSettings.setLoadsImagesAutomatically(true);

//        mWebView.getSettings().setAppCacheMaxSize(10 * 1024 * 1024); // 5MB
        mWebView.getSettings().setAppCachePath(
                mContext.getCacheDir().getAbsolutePath());
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings()
                .setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//        mWebView.getSettings().setLoadWithOverviewMode(true);
//        mWebView.getSettings().setUseWideViewPort(true);

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

        mWebView.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                mProgressRelLayout.clearAnimation();
                mProgressRelLayout.setVisibility(View.GONE);

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
//            mLoadUrl = "http://docs.google.com/viewerng/viewer?url=" + mLoadUrl;
//             mLoadUrl="<iframe src="+"'http://docs.google.com/gview?embedded=true&url="+AppUtils.replace(mLoadUrl)+"'width='100%' height='100%'style='border: none;'></iframe>";

        }
        if (mLoadUrl.equals("")) {

            mErrorFlag = true;
        } else {
            mErrorFlag = false;
        }
        if (mLoadUrl != null && !mErrorFlag) {
            System.out.println("NAS load url " + mLoadUrl);
            mWebView.loadUrl(mLoadUrl);
        } else {
            mProgressRelLayout.clearAnimation();
            mProgressRelLayout.setVisibility(View.GONE);
            AppUtils.showAlertFinish((Activity) mContext, getResources()
                            .getString(R.string.common_error_loading_pdf), "",
                    getResources().getString(R.string.ok), false);
        }

    }


}
