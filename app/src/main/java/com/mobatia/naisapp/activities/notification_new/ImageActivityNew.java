/**
 * 
 */
package com.mobatia.naisapp.activities.notification_new;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.constants.CacheDIRConstants;
import com.mobatia.naisapp.constants.IntentPassValueConstants;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.constants.NaisTabConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.HeaderManager;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * @author archana.s
 * 
 */
public class ImageActivityNew extends Activity implements
		NaisTabConstants, CacheDIRConstants, URLConstants,
		IntentPassValueConstants, NaisClassNameConstants, JSONConstants, StatusConstants {

	private Context mContext;
	private WebView mWebView;
	private RelativeLayout mProgressRelLayout;
	private WebSettings mwebSettings;
	private boolean loadingFlag = true;
	private String mLoadUrl = null;
	private boolean mErrorFlag = false;
	Bundle extras;
	RelativeLayout relativeHeader;
	HeaderManager headermanager;
	ImageView back;
	ImageView home;
	RotateAnimation anim;
    String PushID="";
    String id="";
    String title="";
    String message="";
    String url="";
    String date="";
    String push_from="";
    String type="";
    String status="";
	String Day="";
	String Month="";
	String Year="";
	String PushDate="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comingup_detailweb_view_layout);
		mContext = this;
		extras = getIntent().getExtras();
		if (extras != null) {
			//mLoadUrl = extras.getString("webViewComingDetail");
			PushID = extras.getString("PushID");
			Day = extras.getString("Day");
			Month = extras.getString("Month");
			Year = extras.getString("Year");
			PushDate = extras.getString("PushDate");
			title = extras.getString("title");
		}
		System.out.println("Push Id "+PushID);
//		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(mContext));
		initialiseUI();
		if (AppUtils.isNetworkConnected(mContext)) {
			callPushNotification(PushID);
		} else {
			AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
		}

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
		headermanager = new HeaderManager(ImageActivityNew.this, "Notification");
		headermanager.getHeader(relativeHeader, 0);
		back = headermanager.getLeftButton();
		headermanager.setButtonLeftSelector(R.drawable.back,
				R.drawable.back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AppUtils.hideKeyBoard(mContext);
				finish();
			}
		});
		home = headermanager.getLogoButton();
		home.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent in = new Intent(mContext, HomeListAppCompatActivity.class);
				in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(in);
			}
		});
	}

	/*******************************************************
	 * Method name : getWebViewSettings Description : get web view settings
	 * Parameters : nil Return type : void Date : Oct 31, 2014 Author : Vandana
	 * Surendranath
	 *****************************************************/
	private void getWebViewSettings() {


		mProgressRelLayout.setVisibility(View.GONE);
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
//        int sdk = Build.VERSION.SDK_INT;
//        if (sdk >= Build.VERSION_CODES.JELLY_BEAN) {
//            mWebView.setBackgroundColor(Color.argb(1, 0, 0, 0));
//        }
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
//                view.loadUrl(url);
				//view.loadData(mLoadUrl, "text/html", "UTF-8");
				//view.loadData(mLoadUrl, "text/html; charset=utf-8", "utf-8");
//				view.loadDataWithBaseURL("file:///android_asset/", mLoadUrl, "text/html; charset=utf-8", "utf-8", "about:blank");
				if( url.startsWith("http:") || url.startsWith("https:") || url.startsWith("www.") ) {
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
					startActivity(intent);
					return true;
				}
				else
				{
					System.out.println("NAS load url " + mLoadUrl);
					view.loadDataWithBaseURL("file:///android_asset/fonts/", mLoadUrl, "text/html; charset=utf-8", "utf-8", "about:blank");
					return true;

				}
			}

			public void onPageFinished(WebView view, String url)
			{
				mProgressRelLayout.clearAnimation();
				mProgressRelLayout.setVisibility(View.GONE);
				if (AppUtils.checkInternet(mContext) && loadingFlag) {
					view.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
//                    view.loadUrl(url);
					// view.loadData(mLoadUrl, "text/html", "UTF-8");
					//view.loadData(mLoadUrl, "text/html; charset=utf-8", "utf-8");
					System.out.println("NAS load url " + mLoadUrl);
					view.loadDataWithBaseURL("file:///android_asset/", mLoadUrl, "text/html; charset=utf-8", "utf-8", "about:blank");

					loadingFlag = false;
				} else if (!AppUtils.checkInternet(mContext) && loadingFlag) {
					view.getSettings()
							.setCacheMode(WebSettings.LOAD_CACHE_ONLY);
//                    view.loadUrl(url);
					//view.loadData(mLoadUrl, "text/html", "UTF-8");
					//view.loadData(mLoadUrl, "text/html; charset=utf-8", "utf-8");
					System.out.println("NAS load url " + mLoadUrl);
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
			//mWebView.loadData(mLoadUrl, "text/html", "UTF-8");
			mWebView.loadDataWithBaseURL("file:///android_asset/", mLoadUrl, "text/html; charset=utf-8", "utf-8", "about:blank");
			//mWebView.loadData(mLoadUrl, "text/html; charset=utf-8", "utf-8");
		} else {
			mProgressRelLayout.clearAnimation();
			mProgressRelLayout.setVisibility(View.GONE);
			AppUtils.showAlertFinish((Activity) mContext, getResources()
							.getString(R.string.common_error_loading_page), "",
					getResources().getString(R.string.ok), false);
		}

	}
	private void callPushNotification(String pushID)
	{
		try {

			final VolleyWrapper manager = new VolleyWrapper(URL_GET_NOTICATIONS_MESSAGE);
			String[] name = {JTAG_ACCESSTOKEN,"push_id","users_id"};
			String[] value = {PreferenceManager.getAccessToken(mContext),pushID,PreferenceManager.getUserId(mContext)};
			manager.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {

				@Override
				public void responseSuccess(String successResponse) {
					System.out.println("NofifyRes Message:" + successResponse);

					if (successResponse != null) {
						try {
							JSONObject rootObject = new JSONObject(successResponse);
							String responseCode = rootObject.getString(JTAG_RESPONSECODE);
							if (responseCode.equalsIgnoreCase(RESPONSE_SUCCESS)) {
								JSONObject responseObject = rootObject.optJSONObject(JTAG_RESPONSE);
								String statusCode = responseObject.getString(JTAG_STATUSCODE);
								if (statusCode.equalsIgnoreCase(STATUS_SUCCESS)) {
									String notification_badge=responseObject.optString("notification_badge");
									String notification_edited_badge=responseObject.optString("notification_edited_badge");
									System.out.println("notification_edited_badge"+notification_edited_badge+"notification_badge"+notification_badge);
									PreferenceManager.setNotificationBadge(mContext,notification_badge);
									PreferenceManager.setNotificationEditedBadge(mContext,notification_edited_badge);
									HomeListAppCompatActivity.mListAdapter.notifyDataSetChanged();
									JSONArray dataArray=responseObject.getJSONArray("data");
									if (dataArray.length()>0) {
										for (int i = 0; i < dataArray.length(); i++) {
											JSONObject dataObject = dataArray.getJSONObject(i);
										//	id=dataObject.optString("id");
											//title=dataObject.optString("title");
											message=dataObject.optString("message");
											url=dataObject.optString("url");
										//	date=dataObject.optString("date");
											String pushNotificationDetail = "<!DOCTYPE html>\n" +
													"<html>\n" +
													"<head>\n" +
													"<style>\n" +
													"\n" +
													"@font-face {\n" +
													"font-family: SourceSansPro-Semibold;" +
													"src: url(SourceSansPro-Semibold.ttf);" +

													"font-family: SourceSansPro-Regular;" +
													"src: url(SourceSansPro-Regular.ttf);" +
													"}" +
													".title {" +
													"font-family: SourceSansPro-Regular;" +
													"font-size:16px;" +
													"text-align:left;" +
													"color:	#46C1D0;" +
													"text-align: ####TEXT_ALIGN####;" +
													"}" +
													".description {" +
													"font-family: SourceSansPro-Semibold;" +
													"text-align:justify;" +
													"font-size:14px;" +
													"color: #000000;" +
													"text-align: ####TEXT_ALIGN####;" +
													"}" +
													"</style>\n" + "</head>" +
													"<body>" +
													"<p class='title'>" +message;
											pushNotificationDetail = pushNotificationDetail + "<p class='description'>" + Day + "-" + Month + "-" + Year+ " " + PushDate + "</p>";
											System.out.println("URL IMAGE"+url);
											if (!url.equalsIgnoreCase(""))
											{
												pushNotificationDetail = pushNotificationDetail + "<center><img src='" + url + "'width='100%', height='auto'>";
											}
											pushNotificationDetail = pushNotificationDetail +
													"</body>\n</html>";
											mLoadUrl=pushNotificationDetail;
											getWebViewSettings();
										}
									}
								//	JSONObject dataObject=responseObject.getJSONObject("data");

								} else if (statusCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
										statusCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) ||
										statusCode.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
									AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
										@Override
										public void getAccessToken() {
										}
									});
									callPushNotification(pushID);

								}
							} else if (responseCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
									responseCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) ||
									responseCode.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
								AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
									@Override
									public void getAccessToken() {
									}
								});
								callPushNotification(pushID);

							} else {
								Toast.makeText(mContext, "Some Error Occured", Toast.LENGTH_SHORT).show();

							}


						}
						catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}

				@Override
				public void responseFailure(String failureResponse) {
					// CustomStatusDialog(RESPONSE_FAILURE);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}


	}
}
