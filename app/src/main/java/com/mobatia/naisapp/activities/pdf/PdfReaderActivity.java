package com.mobatia.naisapp.activities.pdf;

import android.app.Activity;
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
import com.mobatia.naisapp.constants.CacheDIRConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.manager.AppUtils;


public class PdfReaderActivity extends Activity implements
        CacheDIRConstants, NaisClassNameConstants {

    private Activity mContext;
    private WebView mWebView;
    private RelativeLayout mProgressRelLayout;
    private WebSettings mwebSettings;
    private boolean loadingFlag = true;
    private String mLoadUrl = null;
    private String pdfUrl = null;
    private boolean mErrorFlag = false;
    Bundle extras;
    ImageView backImageView;
    ImageView pdfDownloadImgView;
    RotateAnimation anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdf_view_layout);
        mContext = this;
        extras = getIntent().getExtras();
        if (extras != null) {
            mLoadUrl = AppUtils.replace(extras.getString("pdf_url").replace("&","%26"));
            pdfUrl = AppUtils.replace(extras.getString("pdf_url").replace("&","%26"));
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
    }

    /*******************************************************
     * Method name : getWebViewSettings Description : get web view settings
     * Parameters : nil Return type : void Date : Oct 31, 2014 Author : Vandana
     * Surendranath
     *****************************************************/
    private void getWebViewSettings() {

        backImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mProgressRelLayout.setVisibility(View.VISIBLE);
        anim = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(mContext, android.R.interpolator.linear);
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(4000);
        mProgressRelLayout.setAnimation(anim);
        mProgressRelLayout.startAnimation(anim);
        mWebView.setFocusable(true);
        mWebView.setFocusableInTouchMode(true);
        mWebView.setBackgroundColor(0Xffffff);
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
        mwebSettings.setAllowUniversalAccessFromFileURLs(true);
        mwebSettings.setAllowContentAccess(true);
        mwebSettings.setAllowFileAccessFromFileURLs(true);

        mWebView.getSettings().setAppCacheMaxSize(10 * 1024 * 1024); // 5MB
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
        if (mLoadUrl.endsWith(".pdf")) {
//            mLoadUrl = "http://docs.google.com/gview?embedded=true&url=" + mLoadUrl;
            mLoadUrl = "https://drive.google.com/viewerng/viewer?embedded=true&url=" + mLoadUrl;
//             mLoadUrl="<iframe src="+"'http://docs.google.com/gview?embedded=true&url="+AppUtils.replace(mLoadUrl)+"'width='100%' height='100%'style='border: none;'></iframe>";

        }
        if (mLoadUrl.equals("")) {

            mErrorFlag = true;
        } else {
            mErrorFlag = false;
        }
        if (mLoadUrl != null && !mErrorFlag) {
            System.out.println("BISAD load url " + mLoadUrl);
            mWebView.loadUrl(mLoadUrl);
        } else {
            mProgressRelLayout.clearAnimation();
            mProgressRelLayout.setVisibility(View.GONE);
            AppUtils.showAlertFinish((Activity) mContext, getResources()
                            .getString(R.string.common_error_loading_pdf), "",
                    getResources().getString(R.string.ok), false);
        }
        mWebView.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                System.out.println("===Page LOADING1111==="+url);

                if (mProgressRelLayout.getVisibility()==View.GONE)
                    mProgressRelLayout.setVisibility(View.VISIBLE);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                mProgressRelLayout.clearAnimation();
                mProgressRelLayout.setVisibility(View.GONE);
                System.out.println("===Page LOADING2222===");


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

//                mWebView.setDownloadListener(new DownloadListener() {
//
//                    @Override
//                    public void onDownloadStart(String url, String userAgent,
//                                                String contentDisposition, String mimetype,
//                                                long contentLength) {
//                        DownloadManager.Request request = new DownloadManager.Request(
//                                Uri.parse(url));
//
//                        request.allowScanningByMediaScanner();//http://mobicare2.mobatia.com/nais/media/images/NASDubai_logo_blk.pdf
//                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); //Notify client once download is completed!
//                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, AppUtils.replacePdf(mLoadUrl).trim());
//                        DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
//                        dm.enqueue(request);
//                        Toast.makeText(getApplicationContext(), "Downloading File", //To notify the Client that the file is being downloaded
//                                Toast.LENGTH_LONG).show();
//
//                    }
//                });

            }
        });

//		GetWebUrlAsyncTask getWebUrl = new GetWebUrlAsyncTask(WEB_CONTENT_URL
//				+ mType, WEB_CONTENT + "/" + mType, 1, mTAB_ID);
//		getWebUrl.execute();
    }

//	private class GetWebUrlAsyncTask extends AsyncTask<Void, Integer, Void> {
//		private String tabId;
//		private String url;
//		private String dirName;
//		private int isCache;
//		private InternetManager manager;
//		private WebContentApi webApi;
//
//		public GetWebUrlAsyncTask(String url, String dirName, int isCache,
//				String tabId) {
//			this.url = url;
//			this.dirName = dirName;
//			this.isCache = isCache;
//			this.tabId = tabId;
//			manager = new InternetManager(url, mContext, dirName, isCache);
//			webApi = new WebContentApi(mContext, manager, tabId);
//		}
//
//		@Override
//		protected void onPreExecute() {
//			super.onPreExecute();
//			mProgressRelLayout.setVisibility(View.VISIBLE);
//		}
//
//		/*
//		 * (non-Javadoc)
//		 *
//		 * @see android.os.AsyncTask#doInBackground(Params[])
//		 */
//		@Override
//		protected Void doInBackground(Void... params) {
//			mLoadUrl = webApi.getWebContentApiResponse();
//			if (mLoadUrl.endsWith(".pdf")) {
//				mLoadUrl = "http://docs.google.com/gview?embedded=true&url=" + mLoadUrl;
//			}
//			if (mLoadUrl.equals("")) {
//				mErrorFlag = true;
//			} else {
//				mErrorFlag = false;
//			}
//			return null;
//		}
//
//		@Override
//		protected void onPostExecute(Void arg0) {
//			super.onPostExecute(arg0);
//			if (mLoadUrl != null && !mErrorFlag) {
//				System.out.println("wiss load url " + mLoadUrl);
//				mWebView.loadUrl(mLoadUrl);
//			}
//
//		}
//	}
}
