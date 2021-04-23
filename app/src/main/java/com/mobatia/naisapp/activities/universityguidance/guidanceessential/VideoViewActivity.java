package com.mobatia.naisapp.activities.universityguidance.guidanceessential;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
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

public class VideoViewActivity extends Activity{

	WebView webView;
	int position;
	ProgressBar proWebView;

	ImageView back;
	ImageView home;
	RelativeLayout relativeMain;
	Activity mActivity;
	TextView textcontent;
	RelativeLayout relativeHeader;
	String url;
	HeaderManager headermanager;
	@SuppressLint("NewApi")
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.video_web);
		mActivity = this;

		Bundle extra = getIntent().getExtras();
		if (extra != null) {
			url = extra.getString("URL");

		}		webView = (WebView) findViewById(R.id.webView);

		proWebView = (ProgressBar) findViewById(R.id.proWebView);
		textcontent = (TextView) findViewById(R.id.txtContent);

		relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
		headermanager = new HeaderManager(mActivity, "Guidance");
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

		final WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		settings.setPluginState(WebSettings.PluginState.ON);

		webView.setWebViewClient(new WebViewClient() {
			// autoplay when finished loading via javascript injection
			public void onPageFinished(WebView view, String url)
			{
				webView.loadUrl("javascript:(function() { document.getElementsByTagName('video')[0].play(); })()"); }
		});
		webView.setWebChromeClient(new WebChromeClient());
		proWebView.setVisibility(View.GONE);
		webView.loadUrl(url);

	}

	@Override
	protected void onPause() {
		super.onPause();
		webView.onPause();
	}

	@Override
	protected void onResume() {

		webView.loadUrl("javascript:(function() { document.getElementsByTagName('video')[0].play(); })()");
		super.onResume();
	}

}
