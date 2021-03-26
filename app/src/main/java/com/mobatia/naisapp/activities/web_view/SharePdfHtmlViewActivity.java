package com.mobatia.naisapp.activities.web_view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import androidx.core.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.activities.trips.PdfPrint;
import com.mobatia.naisapp.constants.CacheDIRConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.HeaderManager;

import java.io.File;

/**
 * Created by gayatri on 20/3/17.
 */
public class SharePdfHtmlViewActivity extends Activity implements
        CacheDIRConstants, NaisClassNameConstants {

    private Context mContext;
    private WebView mWebView;
    private RelativeLayout mProgressRelLayout;
    private WebSettings mwebSettings;
    private String mLoadUrl = null;
    Bundle extras;
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back;
    ImageView home;
    ImageView shareImg;
    LinearLayout addToCalendar;
    //    RotateAnimation anim;
    String tab_type = "";
    String orderId = "";
    String pdfUriFrom = "";
    File pathFile;
   public Uri pdfUri;
    PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_pdf_web_view_layout);
        mContext = this;
        extras = getIntent().getExtras();
        if (extras != null) {
            mLoadUrl = extras.getString("url");
            tab_type = extras.getString("tab_type");
            orderId = extras.getString("orderId");
            pdfUriFrom = extras.getString("pdfUri");
            /*Log.d("CREATEJOB", "=======================================");
            int maxLogSize = 1000;
            for(int i = 0; i <= mLoadUrl.length() / maxLogSize; i++) {
                int start = i * maxLogSize;
                int end = (i+1) * maxLogSize;
                end = end > mLoadUrl.length() ? mLoadUrl.length() : end;
                Log.v("CREATEJOB", mLoadUrl.substring(start, end));
            }
            Log.d("CREATEJOB", "=======================================");*/
        }
//		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(mContext));
        initialiseUI();
        getWebViewSettings();
       // pdfUri = Uri.parse(pdfUriFrom);

        pathFile = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/" + "NAS_DUBAI_TRIP/Payments" + "_" + "NASDUBAI" + "/");
         System.out.println("Path file 1"+pathFile);
        pathFile.mkdirs();
        if (Build.VERSION.SDK_INT >= 23) {

            pdfUri = FileProvider.getUriForFile(mContext, getPackageName() + ".provider", createWebPrintJobShare(mWebView, pathFile));
        } else {
            pdfUri = Uri.fromFile(createWebPrintJobShare(mWebView, pathFile));
            System.out.println("Path file 2"+pathFile);
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
        pdfView = findViewById(R.id.pdfView);
        pdfView.setVisibility(View.GONE);
        mWebView.setVisibility(View.VISIBLE);
        mProgressRelLayout = (RelativeLayout) findViewById(R.id.progressDialog);
        mProgressRelLayout.setVisibility(View.GONE);

        headermanager = new HeaderManager(SharePdfHtmlViewActivity.this, tab_type);
//        headermanager.getHeader(relativeHeader, 0);
        headermanager.getHeader(relativeHeader, 4);
        back = headermanager.getLeftButton();
     //   shareImg = headermanager.getRightButton();
        addToCalendar = headermanager.getLinearRight();
        headermanager.setLinearRight(addToCalendar);
        addToCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharePdfFile();
            }
        });
        back = headermanager.getLeftButton();
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
    }

    /*******************************************************
     * Method name : getWebViewSettings Description : get web view settings
     * Parameters : nil Return type : void Date : Oct 31, 2014 Author : Vandana
     * Surendranath
     *****************************************************/
    private void getWebViewSettings() {
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
        mwebSettings.setLoadsImagesAutomatically(true);
        mwebSettings.setUseWideViewPort(true);
        mWebView.setInitialScale(1);
        mwebSettings.setLoadWithOverviewMode(true);
        mWebView.getSettings().setAppCacheMaxSize(10 * 1024 * 1024); // 5MB
        mWebView.getSettings().setAppCachePath(
                mContext.getCacheDir().getAbsolutePath());
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings()
                .setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);


        //mWebView.loadData(mLoadUrl, "text/html", "UTF-8");
        mWebView.loadDataWithBaseURL("file:///android_asset/images/", mLoadUrl, "text/html; charset=utf-8", "utf-8", "about:blank");


    }

    void sharePdfFile() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            pathFile = new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/" + "NAS_DUBAI_TRIP/Payments" + "_" + "NASDUBAI" + "/");
            System.out.println("Path file 3"+pathFile);
            if (!pathFile.exists())
                pathFile.mkdirs();
//            pathFile.mkdirs();
            if (Build.VERSION.SDK_INT >= 23) {

                pdfUri = FileProvider.getUriForFile(mContext, getPackageName() + ".provider", createWebPrintJobShare(mWebView, pathFile));
            } else {
                pdfUri = Uri.fromFile(createWebPrintJobShare(mWebView, pathFile));
                System.out.println("Path file 4"+pathFile);
            }
            Intent share = new Intent();
            share.setAction(Intent.ACTION_SEND);
            share.setType("application/pdf");
            share.putExtra(Intent.EXTRA_STREAM, pdfUri);
            startActivity(Intent.createChooser(share, "Share"));

        } else {
            Toast.makeText(mContext, "Failed to share the file.", Toast.LENGTH_SHORT).show();
        }
    }

    private File createWebPrintJobShare(WebView webView, File path) {
        String jobName = orderId + ".pdf";

        try {
            PrintDocumentAdapter printAdapter;

            PrintAttributes attributes = new PrintAttributes.Builder()
                    .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                    .setResolution(new PrintAttributes.Resolution("pdf", "pdf", 600, 600))
                    .setMinMargins(PrintAttributes.Margins.NO_MARGINS).build();

            PdfPrint pdfPrint = new PdfPrint(attributes, mContext);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                printAdapter = webView.createPrintDocumentAdapter(jobName);
                Log.v("working", "1");

            } else {
                printAdapter = webView.createPrintDocumentAdapter();
                Log.v("working", "2");

            }

            pdfPrint.printNew(printAdapter, path, jobName, mContext.getCacheDir().getPath());

            return new File(path.getAbsolutePath() + "/" + jobName);
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;

    }


}
