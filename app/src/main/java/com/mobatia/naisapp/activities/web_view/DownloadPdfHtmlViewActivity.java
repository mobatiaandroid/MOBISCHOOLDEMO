package com.mobatia.naisapp.activities.web_view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import androidx.core.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
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
public class DownloadPdfHtmlViewActivity extends Activity implements
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
    //    RotateAnimation anim;
    String tab_type = "";
    String orderId = "";
    String pdfUriFrom = "";
    File pathFile;
   public Uri pdfUri;
    PDFView pdfView;
    ImageView pdfDownloadImgView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_pdf_web_view_layout_download);
        mContext = this;
        extras = getIntent().getExtras();
        if (extras != null) {
            mLoadUrl = extras.getString("url");
            tab_type = extras.getString("tab_type");
            orderId = extras.getString("orderId");
            pdfUriFrom = extras.getString("pdfUri");

        }
//		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(mContext));
        initialiseUI();
        getWebViewSettings();
        pdfUri = Uri.parse(pdfUriFrom);

        pathFile = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/" + "NAS_DUBAI_TRIP/Payments" + "_" + orderId + "/");

        pathFile.mkdirs();
        if (Build.VERSION.SDK_INT >= 23) {

            pdfUri = FileProvider.getUriForFile(mContext, getPackageName() + ".provider", createWebPrintJobShare(mWebView, pathFile));
        } else {
            pdfUri = Uri.fromFile(createWebPrintJobShare(mWebView, pathFile));
        }
//                .pages(0, 2, 1, 3, 3, 3) // all pages are displayed by default
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms

//        or
//        pdfView.fromFile(File)
//        or
//        pdfView.fromBytes(byte[])
//        or
//        pdfView.fromStream(InputStream) // stream is written to bytearray - native code cannot use Java Streams
//        or
//        pdfView.fromSource(DocumentSource)
//        or
//        pdfView.fromAsset(String)
//                .pages(0, 2, 1, 3, 3, 3) // all pages are displayed by default
                pdfView.fromUri(pdfUri)
                        .enableSwipe(true) // allows to block changing pages using swipe
                        .swipeHorizontal(false)
                        .enableDoubletap(true)
                        .defaultPage(0)
                        .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                        .password(null)
                        .scrollHandle(null)
                        .enableAntialiasing(true)
                        .spacing(0)
                        .load();
//                        .pageSnap(true)
//                        .autoSpacing(false)
//                        .pageFitPolicy(FitPolicy.BOTH)
//                        // snap pages to screen boundaries
//                        .pageFling(false) // make a fling change only a single page like ViewPager
//                        .nightMode(false) // toggle night mode
//                        .load();
//                        .onLoad(new OnLoadCompleteListener() {
//                            @Override
//                            public void loadComplete(int nbPages) {
//                                mProgressRelLayout.clearAnimation();
//                                mProgressRelLayout.setVisibility(View.GONE);
//                            }
//                        });

            }
        }, 5000);

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
        mProgressRelLayout = (RelativeLayout) findViewById(R.id.progressDialog);
        mProgressRelLayout.setVisibility(View.GONE);

        headermanager = new HeaderManager(DownloadPdfHtmlViewActivity.this, tab_type);
//        headermanager.getHeader(relativeHeader, 0);
        headermanager.getHeader(relativeHeader, 1);
        shareImg = headermanager.getRightButton();
        headermanager.setButtonRightSelector(R.drawable.shareicon,
                R.drawable.shareicon);
        shareImg.setVisibility(View.GONE);
        shareImg.setOnClickListener(new View.OnClickListener() {
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
        pdfDownloadImgView = (ImageView) findViewById(R.id.pdfDownloadImgView);
        pdfDownloadImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    startActivity(new Intent(Intent.ACTION_VIEW, pdfUri));

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
    }

    /*******************************************************
     * Method name : getWebViewSettings Description : get web view settings
     * Parameters : nil Return type : void Date : Oct 31, 2014 Author : Vandana
     * Surendranath
     *****************************************************/
    private void getWebViewSettings() {
//        mProgressRelLayout.setVisibility(View.GONE);
//        Animation anim = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
//                Animation.RELATIVE_TO_SELF, 0.5f);
//        anim.setInterpolator(mContext, android.R.interpolator.linear);
//        anim.setRepeatCount(Animation.INFINITE);
//        anim.setDuration(3000);
//        mProgressRelLayout.setAnimation(anim);
//        mProgressRelLayout.startAnimation(anim);
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
        mWebView.loadDataWithBaseURL("file:///android_asset/", mLoadUrl, "text/html; charset=utf-8", "utf-8", "about:blank");


    }

    void sharePdfFile() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            pathFile = new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/" + "NAS_DUBAI_TRIP/Payments" + "_" + orderId + "/");
            if (!pathFile.exists())
                pathFile.mkdirs();
//            pathFile.mkdirs();
            if (Build.VERSION.SDK_INT >= 23) {

                pdfUri = FileProvider.getUriForFile(mContext, getPackageName() + ".provider", createWebPrintJobShare(mWebView, pathFile));
            } else {
                pdfUri = Uri.fromFile(createWebPrintJobShare(mWebView, pathFile));
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
        String jobName = "Receipt_Share_" + orderId + ".pdf";

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


//    void  share()
//    {
//        Uri pdfUri;
//           pathFile= new File(Environment.getExternalStorageDirectory()
//                .getAbsolutePath() + "/" + "NAS_DUBAI/TripPayments" + "_" + orderId + "/");
//
//        pathFile.mkdirs();
//        if (Build.VERSION.SDK_INT >= 23) {
//
//            pdfUri = FileProvider.getUriForFile(mContext, getPackageName() + ".provider", createWebPrintJobShare(mWebView,pathFile));
//        } else {
//            pdfUri = Uri.fromFile(createWebPrintJobShare(mWebView,pathFile));
//        }
//    }

}
