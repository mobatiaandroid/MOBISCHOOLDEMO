package com.mobatia.naisapp.activities.payment_history;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.activities.trips.PdfPrint;
import com.mobatia.naisapp.activities.web_view.SharePdfHtmlViewActivity;
import com.mobatia.naisapp.constants.CacheDIRConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.HeaderManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class PaymentPrintActivity extends Activity implements
        CacheDIRConstants, NaisClassNameConstants {

    private Context mContext;
    private WebView mWebView;
    private WebView paymentWebDummy;
    private RelativeLayout mProgressRelLayout;
    private WebSettings mwebSettings;
    private String mLoadUrl = null;
    Bundle extras;
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back;
    ImageView home;
    LinearLayout addToCalendar;
    String tab_type = "";
    String orderId = "";
    String pdfUriFrom = "";
    File pathFile;
    public Uri pdfUri;
    PDFView pdfView;
    String fullHtml;
    String amount="";
    String title="";
    String invoice="";
    String paidby="";
    String paidDate="";
    String billingCode="";
    String tr_no="";
    String payment_type="";
    LinearLayout emailLinear;
    LinearLayout printLinearClick;
    LinearLayout downloadLinear;
    LinearLayout shareLinear;
    RotateAnimation anim;
    PrintJob printJob;
    boolean BackPage=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview_activity);
        mContext = this;
        extras = getIntent().getExtras();
        if (extras != null) {
            tab_type = extras.getString("tab_type");
            orderId = extras.getString("orderId");
            amount = extras.getString("amount");
            title = extras.getString("title");
            invoice = extras.getString("invoice");
            paidby = extras.getString("paidby");
            paidDate = extras.getString("paidDate");
            billingCode = extras.getString("billingCode");
            tr_no = extras.getString("tr_no");
            payment_type = extras.getString("payment_type");
        }
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
        mWebView = (WebView) findViewById(R.id.paymentWeb);
        paymentWebDummy = (WebView) findViewById(R.id.paymentWebDummy);
        mWebView.setVisibility(View.VISIBLE);
        paymentWebDummy.setVisibility(View.GONE);
        mProgressRelLayout = (RelativeLayout) findViewById(R.id.progressDialog);
        mProgressRelLayout.setVisibility(View.GONE);

        headermanager = new HeaderManager(PaymentPrintActivity.this, "Preview");
        headermanager.getHeader(relativeHeader, 0);

        back = headermanager.getLeftButton();
        emailLinear=findViewById(R.id.emailLinear);
        printLinearClick=findViewById(R.id.printLinearClick);
        downloadLinear=findViewById(R.id.downloadLinear);
        shareLinear=findViewById(R.id.shareLinear);
        home = headermanager.getLogoButton();
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(mContext, HomeListAppCompatActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
            }
        });
        headermanager.setButtonLeftSelector(R.drawable.back,
                R.drawable.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        printLinearClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                     BackPage=false;
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                      //  mWebView.loadUrl("about:blank");
                        paymentWebDummy.loadUrl("about:blank");
                        setWebViewSettingsPrint();
                        loadWebViewWithDataPrint();
                        createWebPrintJob(paymentWebDummy);

                    } else {
                        Toast.makeText(mContext, "Print is not supported below Android KITKAT Version", Toast.LENGTH_SHORT).show();
                    }


            }
        });
        downloadLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                        paymentWebDummy.loadUrl("about:blank");
                        setWebViewSettingsPrint();
                        loadWebViewWithDataPrint();
                        createWebPrintJob(paymentWebDummy);

                    } else {
                        Toast.makeText(mContext, "Print is not supported below Android KITKAT Version", Toast.LENGTH_SHORT).show();
                    }


            }
        });
        shareLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                    if ((int) Build.VERSION.SDK_INT >= 23) {
                        System.out.println("share function sharePdfFilePrint permission");
                        TedPermission.with(mContext)
                                .setPermissionListener(permissionListenerStorage)
                                .setDeniedMessage("If you reject permission,you cannot use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                                .check();
                    } else {
                        System.out.println("share function sharePdfFilePrint");
                        sharePdfFilePrint();
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

        loadWebViewWithDataPrint();
    }

    void loadWebViewWithDataPrint() {
        StringBuffer sb = new StringBuffer();
        String eachLine = "";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(getAssets().open("paycanteenreciept.html")));
            sb = new StringBuffer();
            eachLine = br.readLine();
            while (eachLine != null) {
                sb.append(eachLine);
                sb.append("\n");
                eachLine = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        fullHtml = sb.toString();

        if (fullHtml.length() > 0) {
            fullHtml = fullHtml.replace("###amount###", amount);
            fullHtml = fullHtml.replace("###order_Id###", orderId);
            fullHtml = fullHtml.replace("###ParentName###",paidby);
            fullHtml = fullHtml.replace("###Date###", AppUtils.dateParsingTodd_MMM_yyyy(paidDate));
            fullHtml = fullHtml.replace("###paidBy###", invoice);
            fullHtml = fullHtml.replace("###billing_code###",billingCode );
            fullHtml = fullHtml.replace("###trn_no###",tr_no );
            fullHtml = fullHtml.replace("###payment_type###",payment_type );
            // fullHtml = fullHtml.replace("###paidBy###", "Done");
            fullHtml = fullHtml.replace("###title###", title);
            paymentWebDummy.loadDataWithBaseURL("file:///android_asset/images/", fullHtml, "text/html; charset=utf-8", "utf-8", "about:blank");
            mWebView.loadDataWithBaseURL("file:///android_asset/images/", fullHtml, "text/html; charset=utf-8", "utf-8", "about:blank");


        }

    }
    private void setWebViewSettingsPrint() {
        mProgressRelLayout.setVisibility(View.VISIBLE);
        anim = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(mContext, android.R.interpolator.linear);
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(1000);
        mProgressRelLayout.setAnimation(anim);
        mProgressRelLayout.startAnimation(anim);
        paymentWebDummy.getSettings().setJavaScriptEnabled(true);
        paymentWebDummy.clearCache(true);
        paymentWebDummy.getSettings().setDomStorageEnabled(true);
        paymentWebDummy.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        paymentWebDummy.getSettings().setSupportMultipleWindows(true);
        paymentWebDummy.setWebViewClient(new PaymentPrintActivity.MyPrintWebViewClient());
//        paymentWeb.setWebChromeClient(new MyWebChromeClient());
    }
    private class MyPrintWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            //Calling a javascript function in html page

//            view.loadUrl(url);

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

            //   Log.d("WebView", "print webpage loading.." + url);

        }
    }
    private void createWebPrintJob(WebView webView) {
        mProgressRelLayout.clearAnimation();
        mProgressRelLayout.setVisibility(View.GONE);
        paymentWebDummy.setVisibility(View.GONE);
        PrintManager printManager = (PrintManager) this.getSystemService(Context.PRINT_SERVICE);
        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter();
        String jobName = getString(R.string.app_name) + "_Pay" + "NASDUBAI_CANTEEN";
        PrintAttributes.Builder builder = new PrintAttributes.Builder();
        builder.setMediaSize(PrintAttributes.MediaSize.ISO_A4);
        if (printManager != null) {
            printJob = printManager.print(jobName, printAdapter, builder.build());
        }
        if (printJob.isCompleted()) {
//            Toast.makeText(getApplicationContext(), R.string.print_complete, Toast.LENGTH_LONG).show();
        } else if (printJob.isFailed()) {
            Toast.makeText(getApplicationContext(), "Print failed", Toast.LENGTH_SHORT).show();
        }

    }
    PermissionListener permissionListenerStorage = new PermissionListener() {
        @Override
        public void onPermissionGranted() {

            sharePdfFilePrint();
            }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(mContext, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }


    };
    void sharePdfFilePrint() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT)
        {
            paymentWebDummy.loadUrl("about:blank");
            setWebViewSettingsPrint();
            loadWebViewWithDataPrint();
//                    createWebPrintJobShare(paymentWeb);
            pathFile= new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/" + "NAS_DUBAI_CANTEEN/Payments" + "_" + "NASDUBAI" + "/");
            System.out.println("Path file 5"+pathFile);

            pathFile.mkdirs();
//            if(!pathFile.exists())
//                pathFile.mkdirs();
            if (Build.VERSION.SDK_INT >= 23) {
                System.out.println("web view data"+fullHtml);

                pdfUri = FileProvider.getUriForFile(mContext, getPackageName() + ".provider", createWebPrintJobShare(paymentWebDummy,pathFile));
            } else {
                System.out.println("Path file 6"+pathFile);
                pdfUri = Uri.fromFile(createWebPrintJobShare(paymentWebDummy,pathFile));
            }

            Intent intent = new Intent(mContext, SharePdfHtmlViewActivity.class);
            intent.putExtra("url", fullHtml);
            intent.putExtra("tab_type", "Preview");
            intent.putExtra("orderId", orderId);
            intent.putExtra("pdfUri", pdfUri.toString());
            startActivity(intent);
            paymentWebDummy.setVisibility(View.GONE);

        }
        else {
//            Toast.makeText(mContext, "Print is not supported below Android KITKAT Version", Toast.LENGTH_SHORT).show();
        }
    }
    private File createWebPrintJobShare(WebView webView, File path) {
        String jobName = orderId+ ".pdf";
        mProgressRelLayout.clearAnimation();
        mProgressRelLayout.setVisibility(View.GONE);
        paymentWebDummy.setVisibility(View.VISIBLE);
        try {
            PrintDocumentAdapter printAdapter;
            PrintAttributes attributes = new PrintAttributes.Builder()
                    .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                    .setResolution(new PrintAttributes.Resolution("pdf", "pdf", 600, 600))
                    .setMinMargins(PrintAttributes.Margins.NO_MARGINS).build();

            PdfPrint pdfPrint = new PdfPrint(attributes, mContext);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                printAdapter = webView.createPrintDocumentAdapter(jobName);
                //Log.v("working", "1");

            } else {
                printAdapter = webView.createPrintDocumentAdapter();
                // Log.v("working", "2");

            }

            pdfPrint.printNew(printAdapter, path, jobName, mContext.getCacheDir().getPath());
            //Log.v("pathfile", path.getAbsolutePath() + "/"  + jobName);
            return new File(path.getAbsolutePath() + "/"  + jobName);
        } catch (Exception e) {
            e.printStackTrace();
            paymentWebDummy.setVisibility(View.GONE);

        }
        return null;

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (BackPage)
        {
            finish();
        }
        else
        {
            mWebView.setVisibility(View.VISIBLE);
            mWebView.loadDataWithBaseURL("file:///android_asset/images/", fullHtml, "text/html; charset=utf-8", "utf-8", "about:blank");
            BackPage=true;
        }
    }

}
