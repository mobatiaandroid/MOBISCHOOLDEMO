package com.mobatia.naisapp.activities.notifications;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.constants.IntentPassValueConstants;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.notifications.model.PushNotificationModel;
import com.mobatia.naisapp.manager.HeaderManager;


import java.util.ArrayList;

public class VideoWebViewActivity extends Activity implements OnClickListener,
		IntentPassValueConstants, JSONConstants, URLConstants, StatusConstants {

	ArrayList<PushNotificationModel> videolist;
	WebView webView;
	int position;
	ProgressBar proWebView;

	ImageView back;
	ImageView home;
	RelativeLayout relativeMain;
	Activity mActivity;
	TextView textcontent;

	RelativeLayout relativeHeader;
	HeaderManager headermanager;
	@SuppressLint("NewApi")
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.videopush_web_view);
		mActivity = this;

		Bundle extra = getIntent().getExtras();
		if (extra != null) {
			position = extra.getInt(POSITION);

			videolist = (ArrayList<PushNotificationModel>) extra
					.getSerializable(PASS_ARRAY_LIST);
		}		webView = (WebView) findViewById(R.id.webView);

		proWebView = (ProgressBar) findViewById(R.id.proWebView);
		textcontent = (TextView) findViewById(R.id.txtContent);
		textcontent.setVisibility(View.INVISIBLE);

		relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
		headermanager = new HeaderManager(mActivity, "Notification");
		headermanager.getHeader(relativeHeader, 0);
		back = headermanager.getLeftButton();
		headermanager.setButtonLeftSelector(R.drawable.back,
				R.drawable.back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		home = headermanager.getLogoButton();
		home.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent in = new Intent(mActivity, HomeListAppCompatActivity.class);
				in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(in);
			}
		});
		webView.setWebViewClient(new HelloWebViewClient());
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setPluginState(PluginState.ON);
		webView.getSettings().setBuiltInZoomControls(false);
		webView.getSettings().setDisplayZoomControls(true);
//		DisplayMetrics displaymetrics = new DisplayMetrics();
//	    getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//	    int height = displaymetrics.heightPixels;
		String frameVideo = "<html>"+"<br><iframe width=\"320\" height=\"250\" src=\"";
		String url_Video=frameVideo+videolist.get(position).getPushUrl()+"\" frameborder=\"0\" allowfullscreen></iframe></body></html>";
		String urlYoutube = url_Video.replace("watch?v=", "embed/");
		System.out.println("urlYoutube:"+urlYoutube);
		webView.loadData(urlYoutube, "text/html", "utf-8");
	
//		webView.loadUrl(videolist.get(position).getUrl());
		textcontent.setText(videolist.get(position).getPushTitle());
		proWebView.setVisibility(View.VISIBLE);

	}


	private class HelloWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			return true;
		}

		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			super.onPageStarted(view, url, favicon);

		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);
			proWebView.setVisibility(View.GONE);
			textcontent.setVisibility(View.VISIBLE);
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}


}
