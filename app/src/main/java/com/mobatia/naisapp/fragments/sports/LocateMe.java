package com.mobatia.naisapp.fragments.sports;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.home.HomeListActivity;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.manager.HeaderManager;

/**
 * Created by gayatri on 3/4/17.
 */
public class LocateMe extends Activity {
    WebView mapView;
    RelativeLayout relativeHeader,mProgressRelLayout;
    HeaderManager headermanager;
    ImageView back;
    ImageView home;
    Bundle extras;
    String latitude,longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_aboutus);
        extras=getIntent().getExtras();
        if(extras!=null){
            latitude=extras.getString("latitude");
            longitude=extras.getString("longitude");
        }
        mapView= (WebView) findViewById(R.id.webView);
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        mProgressRelLayout= (RelativeLayout) findViewById(R.id.progressDialog);
        mProgressRelLayout.setVisibility(View.GONE);
        //web.setWebViewClient(new myWebClient());
        mapView.getSettings().setJavaScriptEnabled(true);
        headermanager = new HeaderManager(LocateMe.this, "Location");
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
                Intent in = new Intent(LocateMe.this, HomeListAppCompatActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
            }
        });
        mapView.getSettings().setJavaScriptEnabled(true); // enable javascript

        final Activity activity = this;

        mapView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
            }
        });

        mapView .loadUrl("http://maps.google.com/?q="+latitude+","+longitude);
    }
}
