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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.constants.CacheDIRConstants;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;

import org.json.JSONObject;

import static com.mobatia.naisapp.constants.StatusConstants.RESPONSE_ACCESSTOKEN_EXPIRED;
import static com.mobatia.naisapp.constants.StatusConstants.RESPONSE_ACCESSTOKEN_MISSING;
import static com.mobatia.naisapp.constants.StatusConstants.RESPONSE_INVALID_TOKEN;


public class PdfReaderPermissionActivity extends Activity implements
        CacheDIRConstants, NaisClassNameConstants,URLConstants,JSONConstants {

    private Activity mContext;
    private WebView mWebView;
    private RelativeLayout mProgressRelLayout;
    private LinearLayout acceptRelative;
    private LinearLayout statusLinear;
    private ImageView acceptImg;
    private TextView statusTxt;
    private Button accept;
    private Button notAccept;
    private WebSettings mwebSettings;
    private boolean loadingFlag = true;
    private String mLoadUrl = null;
    private String pdfUrl = null;
    private String status;
    private boolean mErrorFlag = false;
    Bundle extras;
    ImageView backImageView;
    ImageView pdfDownloadImgView;
    RotateAnimation anim;
    String statusValue;
    String pdfId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdf_view_permission_layout);
        mContext = this;
        extras = getIntent().getExtras();
        if (extras != null) {
            status=extras.getString("status");
            pdfId=extras.getString("pdf_id");
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
        acceptRelative = (LinearLayout) findViewById(R.id.acceptRelative);
        statusLinear = (LinearLayout) findViewById(R.id.statusLinear);
        backImageView = (ImageView) findViewById(R.id.backImageView);
        accept = (Button) findViewById(R.id.accept);
        notAccept = (Button) findViewById(R.id.notAccept);
        pdfDownloadImgView = (ImageView) findViewById(R.id.pdfDownloadImgView);
        statusTxt = (TextView) findViewById(R.id.statusTxt);
        acceptImg = (ImageView) findViewById(R.id.acceptImg);
//        if (status.equalsIgnoreCase("0"))
////        {
////            acceptRelative.setVisibility(View.VISIBLE);
////        }
////        else {
////            acceptRelative.setVisibility(View.GONE);
////        }
        if (status.equalsIgnoreCase("0"))
        {
            acceptRelative.setVisibility(View.VISIBLE);
        }
        else if (status.equalsIgnoreCase("1")){
            acceptRelative.setVisibility(View.GONE);
            statusLinear.setVisibility(View.VISIBLE);
            acceptImg.setImageResource(R.drawable.approve);
            statusTxt.setText("Accepted");
        }
        else {
            acceptRelative.setVisibility(View.GONE);
            statusLinear.setVisibility(View.VISIBLE);
            acceptImg.setImageResource(R.drawable.closed);
            statusTxt.setText("Not Accepted");

        }

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
        notAccept.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                statusValue="2";
                permissionStatusChange(URL_GET_PERMISSION_SLIPS_STATUS_CHANGE,statusValue,pdfId);
                }
        });
        accept.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                statusValue="1";
                permissionStatusChange(URL_GET_PERMISSION_SLIPS_STATUS_CHANGE,statusValue,pdfId);



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
private void permissionStatusChange(String URL,final String status,String pdfId) {
    VolleyWrapper volleyWrapper = new VolleyWrapper(URL);
    String[] name = {"access_token","status","id","users_id"};
    String[] value = {PreferenceManager.getAccessToken(mContext),status,pdfId,PreferenceManager.getUserId(mContext)};
    volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
        @Override
        public void responseSuccess(String successResponse) {
            System.out.println("The response is status change" + successResponse);
            try {
                JSONObject obj = new JSONObject(successResponse);
                String response_code = obj.getString(JTAG_RESPONSECODE);
                if (response_code.equalsIgnoreCase("200")) {
                    JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                    String status_code = secobj.getString(JTAG_STATUSCODE);
                    if (status_code.equalsIgnoreCase("303")) {
                        if (status.equalsIgnoreCase("0"))
                        {
                            statusLinear.setVisibility(View.GONE);
                            acceptRelative.setVisibility(View.VISIBLE);
                        }
                        else if (status.equalsIgnoreCase("1")){
                            acceptRelative.setVisibility(View.GONE);
                            statusLinear.setVisibility(View.VISIBLE);
                            acceptImg.setImageResource(R.drawable.approve);
                            statusTxt.setText("Accepted");
                        }
                        else {
                            acceptRelative.setVisibility(View.GONE);
                            statusLinear.setVisibility(View.VISIBLE);
                            acceptImg.setImageResource(R.drawable.closed);
                            statusTxt.setText("Not Accepted");

                        }
                    }
                } else if (response_code.equalsIgnoreCase("500")) {
                    // AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                } else if (response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) ||
                        response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                        response_code.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
                    AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                        @Override
                        public void getAccessToken() {

                        }
                    });

                } else {

                }
            } catch (Exception ex) {
                System.out.println("The Exception in edit profile is" + ex.toString());
            }

        }

        @Override
        public void responseFailure(String failureResponse) {
            //AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

        }
    });


}
}
