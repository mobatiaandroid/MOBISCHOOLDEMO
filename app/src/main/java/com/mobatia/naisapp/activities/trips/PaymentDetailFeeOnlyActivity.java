package com.mobatia.naisapp.activities.trips;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import androidx.core.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.activities.trips.model.FeePaymentModel;
import com.mobatia.naisapp.activities.web_view.DownloadPdfHtmlViewActivity;
import com.mobatia.naisapp.activities.web_view.SharePdfHtmlViewActivity;
import com.mobatia.naisapp.appcontroller.AppController;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.HeaderManager;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PaymentDetailFeeOnlyActivity extends Activity implements URLConstants, JSONConstants, StatusConstants {
    Context mContext;
    Bundle extras;
    HeaderManager headermanager;
    ImageView back;
    RelativeLayout relativeHeader;
    ImageView home;
    String tab_type = "";
    LinearLayout addToCalendar;
    LinearLayout totalLinear,printLinear,printLinearClick,mainLinear,downloadLinear,shareLinear;
    TextView totalAmount,descriptionTitle,description;
    Button payTotalButton;
    WebView paymentWeb;
    ImageView bannerImageViewPager,paidImg;
    String categoryId="";
    String category_name="";
    String invoice_description="";
    String studentId="";
    String amount="";
    String is_paid="";
    String backStatus="0";
    String orderId="";
    public String postBody = "";
    public String paid_amount = "";
    public String auth_id = "";
    String sessionId;
    String sessionVersion;
    public String postUrl;
    public String merchantpassword="";
    public final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/octet-stream");
    private RelativeLayout mProgressRelLayout;
    RotateAnimation anim;
    String fullHtml;
    String payment_date;
    String tripDetails = "";
    String payment_date_print = "";
    String invoice_note = "";
    String isams_no = "";
    PrintJob printJob;
    boolean isDownload=false;
    boolean isShareInstall=false;
    String parent_name;
    String formated_amount;
    String trn_no;
    String invoice_no;
    String payment_type;
    String payment_type_print;
    String studentName;
    File pathFile;
    Uri pdfUri;
    String current="";
    String vat_percentage="";
    String vat_amount="";
    Handler handler = new Handler();
    Runnable runnable;
    int delay = 10000;
    boolean isToastShown=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_fee_activity_detail);
        mContext = this;
        initUI();
    }
    public void initUI() {
        extras = getIntent().getExtras();
        if (extras != null) {
            tab_type = extras.getString("tab_type");
            categoryId = extras.getString("categoryId");
            category_name = extras.getString("category_name");
            invoice_description = extras.getString("invoice_description");
            studentId = extras.getString("studentId");
            amount = extras.getString("amount");
            is_paid = extras.getString("is_paid");
            paid_amount = extras.getString("paid_amount");
            orderId = extras.getString("order_id");
            payment_date_print = extras.getString("payment_date");
            invoice_note = extras.getString("invoice_note");
            isams_no = extras.getString("isams_no");
            parent_name = extras.getString("parent_name");
            formated_amount = extras.getString("formated_amount");
            trn_no = extras.getString("trn_no");
            invoice_no = extras.getString("invoice_no");
            payment_type = extras.getString("payment_type");
            studentName = extras.getString("studentName");
            current = extras.getString("current");
            vat_amount = extras.getString("vat_amount");
            vat_percentage = extras.getString("vat_percentage");
        }
        merchantpassword = PreferenceManager.getMerchantPassword(mContext);
        if (payment_type.equalsIgnoreCase("1"))
        {
            payment_type_print="Online";
        }
        else  if (payment_type.equalsIgnoreCase("2"))
        {
            payment_type_print="Cash";
        }else  if (payment_type.equalsIgnoreCase("3"))
        {
            payment_type_print="Cheque";
        }else  if (payment_type.equalsIgnoreCase("4"))
        {
            payment_type_print="Bank Transfer";
        }
        else if(payment_type.equalsIgnoreCase("5"))
        {
            payment_type_print="Online";
        }
        else
        {
            payment_type_print="Online";
        }
        auth_id= PreferenceManager.getAuthId(mContext);
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        headermanager = new HeaderManager(this, "Payment Details");
        headermanager.getHeader(relativeHeader, 4);
        back = headermanager.getLeftButton();
        addToCalendar = headermanager.getLinearRight();
        headermanager.setButtonLeftSelector(R.drawable.back,
                R.drawable.back);

//        headermanager.setButtonRightSelector(R.drawable.add_calendar,
//                R.drawable.add_calendar);
        addToCalendar.setVisibility(View.GONE);
        home = headermanager.getLogoButton();
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(mContext, HomeListAppCompatActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
            }
        });

        totalLinear=findViewById(R.id.totalLinear);
        totalAmount=findViewById(R.id.totalAmount);
        printLinear=findViewById(R.id.printLinear);
        printLinearClick=findViewById(R.id.printLinearClick);
        paymentWeb=findViewById(R.id.paymentWeb);
        mainLinear=findViewById(R.id.mainLinear);
        descriptionTitle=findViewById(R.id.descriptionTitle);
        description=findViewById(R.id.description);
        payTotalButton=findViewById(R.id.payTotalButton);
        downloadLinear=findViewById(R.id.downloadLinear);
        shareLinear=findViewById(R.id.shareLinear);
        paidImg=findViewById(R.id.paidImg);
        mProgressRelLayout = (RelativeLayout) findViewById(R.id.progressDialog);
        mProgressRelLayout.setVisibility(View.GONE);
        bannerImageViewPager=findViewById(R.id.bannerImageViewPager);
        descriptionTitle.setText(category_name);
        description.setText(invoice_description);
        totalAmount.setText(formated_amount);
        mainLinear.setVisibility(View.VISIBLE);
        paymentWeb.setVisibility(View.GONE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(backStatus.equalsIgnoreCase("0"))
                {
                    finish();
                }
                else if (backStatus.equalsIgnoreCase("1"))
                {
                    backStatus="0";
                    payTotalButton.setVisibility(View.VISIBLE);
                    totalLinear.setVisibility(View.VISIBLE);
                    paidImg.setVisibility(View.GONE);
                    mainLinear.setVisibility(View.VISIBLE);
                    paymentWeb.setVisibility(View.GONE);
                    mProgressRelLayout.setVisibility(View.GONE);
                    mProgressRelLayout.clearAnimation();
                    payment_date = AppUtils.getCurrentDateToday() ;
                    handler.removeCallbacks(runnable);
                }
                else if (backStatus.equalsIgnoreCase("2"))
                {
                    backStatus="0";
                    payTotalButton.setVisibility(View.GONE);
                    totalLinear.setVisibility(View.VISIBLE);
                    paidImg.setVisibility(View.VISIBLE);
                    mainLinear.setVisibility(View.VISIBLE);
                    mProgressRelLayout.setVisibility(View.GONE);
                    mProgressRelLayout.clearAnimation();
                    paymentWeb.setVisibility(View.GONE);
                    handler.removeCallbacks(runnable);
                }


            }
        });
        if (is_paid.equalsIgnoreCase("0"))
        {
            System.out.println("is paid 0 working");
            payTotalButton.setVisibility(View.VISIBLE);
            totalLinear.setVisibility(View.VISIBLE);
            paidImg.setVisibility(View.GONE);
            mainLinear.setVisibility(View.VISIBLE);
            payment_date = AppUtils.getCurrentDateToday() ;
            printLinear.setVisibility(View.GONE);
        }
        else
        {
            System.out.println("is paid 1 working");
            payTotalButton.setVisibility(View.GONE);
            totalLinear.setVisibility(View.VISIBLE);
            paidImg.setVisibility(View.VISIBLE);
            mainLinear.setVisibility(View.VISIBLE);
            printLinear.setVisibility(View.VISIBLE);

        }
        printLinearClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                        paymentWeb.loadUrl("about:blank");
                        setWebViewSettingsPrint();
                        loadWebViewWithDataPrint();
                        createWebPrintJob(paymentWeb);

                    } else {
                        Toast.makeText(mContext, "Print is not supported below Android KITKAT Version", Toast.LENGTH_SHORT).show();
                    }
            }
        });
        downloadLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                        paymentWeb.loadUrl("about:blank");
                        setWebViewSettingsPrint();
                        loadWebViewWithDataPrint();
                        createWebPrintJob(paymentWeb);

                    } else {
                        Toast.makeText(mContext, "Print is not supported below Android KITKAT Version", Toast.LENGTH_SHORT).show();
                    }


            }
        });
        shareLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                   isDownload=false;
                    isShareInstall=false;
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

        payTotalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isToastShown=false;
                mainLinear.setVisibility(View.GONE);
                printLinear.setVisibility(View.GONE);
                paymentWeb.setVisibility(View.VISIBLE);
                paymentWeb.loadUrl("about:blank");
             //   setWebViewSettings();
                mProgressRelLayout.setVisibility(View.VISIBLE);
                anim = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
                anim.setInterpolator(mContext, android.R.interpolator.linear);
                anim.setRepeatCount(Animation.INFINITE);
                anim.setDuration(1000);
                mProgressRelLayout.setAnimation(anim);
                mProgressRelLayout.startAnimation(anim);
                backStatus="1";
                orderId="NASDUBAI" + categoryId+ "S" + studentId;
                postUrl= PreferenceManager.getSessionUrl(mContext);
                postBody = "{\n" +
                        "\"apiOperation\":\"CREATE_CHECKOUT_SESSION\",\n" +
                        "\"interaction\":{\n" +
                        "\"operation\":\"PURCHASE\"\n" +
                        "},\n" +
                        "\"order\":{\n" +
                        "\"amount\":\"" + amount + "\",\n" +
                        "\"currency\":\"AED\",\n" +
                        "\"id\":\"" + invoice_no + "\"\n" +
                        "}\n" +
                        "}";

             //   Log.e("Postbody",postBody);
                try {

                    postRequest(postUrl, postBody, merchantpassword,orderId);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    PermissionListener permissionListenerStorage = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            if (isDownload)
            {
                downloadFilePrint();

            }
            else {
                sharePdfFilePrint();

            }


        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(mContext, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }


    };
    void downloadFilePrint()
    {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            paymentWeb.loadUrl("about:blank");
            setWebViewSettingsPrint();
            loadWebViewWithDataPrint();
//                    createWebPrintJobShare(paymentWeb);
            pathFile= new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath() +"/" + "NAS_DUBAI_TRIP/Payments" + "_" + "NASDUBAI" + "/");
         //   System.out.println("Path file 9"+pathFile);
            pathFile.mkdirs();
//            if(!pathFile.exists())
//                pathFile.mkdirs();
            if (Build.VERSION.SDK_INT >= 23) {

                pdfUri = FileProvider.getUriForFile(mContext, getPackageName() + ".provider", createWebPrintJobShare(paymentWeb,pathFile));
            } else {
                pdfUri = Uri.fromFile(createWebPrintJobShare(paymentWeb,pathFile));
              //  System.out.println("Path file 8"+pathFile);
            }

//            Intent share = new Intent();
//            share.setAction(Intent.ACTION_SEND);
//            share.setType("application/pdf");
//            Log.v("uriFile",pdfUri.toString());
//            share.putExtra(Intent.EXTRA_STREAM, pdfUri);
//            startActivity(Intent.createChooser(share, "Share"));

            Intent intent = new Intent(mContext, DownloadPdfHtmlViewActivity.class);
            intent.putExtra("url", fullHtml);
            intent.putExtra("tab_type", "Preview");
            intent.putExtra("orderId", "NASDUBAI");
            intent.putExtra("pdfUri", pdfUri.toString());
            startActivity(intent);
            paymentWeb.setVisibility(View.GONE);

        } else {
//            Toast.makeText(mContext, "Print is not supported below Android KITKAT Version", Toast.LENGTH_SHORT).show();
        }
    }
    void postRequest(final String postUrl, final String postBody, String merchantPassword, final String orderRequest) throws IOException {
       OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON_MEDIA_TYPE, postBody);
       // System.out.println("Postbody"+postBody);
        String credential = Credentials.basic("merchant."+PreferenceManager.getMerchantId(mContext), PreferenceManager.getMerchantPassword(mContext));
        final Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .header("Authorization", credential)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("postUrl::"+response);
//                System.out.println("orderId::"+orderId);

                if (response.isSuccessful()) {

                    try {
                        JSONObject obj = new JSONObject(response.body().string());
                        String result = obj.getString("result");
                        if (result.equalsIgnoreCase("SUCCESS")) {
                            final JSONObject sessionObj = obj.getJSONObject("session");
                            sessionId = sessionObj.getString("id");
                            sessionVersion = sessionObj.getString("version");
//                            System.out.println("session Id " + sessionId);
//                            System.out.println("session version" + sessionVersion);


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() { // Stuff that updates the UI } });
                                    // closedImg.setVisibility(View.GONE);
                                    if (sessionId.equalsIgnoreCase("")) {
                                        try {
                                            postRequest(postUrl, postBody, merchantpassword,orderRequest);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    } else {

                                        setWebViewSettings();
                                        loadWebViewWithData(category_name, invoice_description,orderRequest);
                                       // Log.d("LOG3", fullHtml);
                                    }


                                }
                            });
                        }
                        System.out.println("result" + result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }
    void sharePdfFilePrint() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT)
        {
            paymentWeb.loadUrl("about:blank");
            setWebViewSettingsPrint();
            loadWebViewWithDataPrint();
//                    createWebPrintJobShare(paymentWeb);
            pathFile= new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/" + "NAS_DUBAI_TRIP/Payments" + "_" + "NASDUBAI" + "/");
        //    System.out.println("Path file 5"+pathFile);

            pathFile.mkdirs();
//            if(!pathFile.exists())
//                pathFile.mkdirs();
            if (Build.VERSION.SDK_INT >= 23) {
             //   System.out.println("web view data"+fullHtml);

                pdfUri = FileProvider.getUriForFile(mContext, getPackageName() + ".provider", createWebPrintJobShare(paymentWeb,pathFile));
            } else {
              //  System.out.println("Path file 6"+pathFile);
                pdfUri = Uri.fromFile(createWebPrintJobShare(paymentWeb,pathFile));
            }

            Intent intent = new Intent(mContext, SharePdfHtmlViewActivity.class);
            intent.putExtra("url", fullHtml);
            intent.putExtra("tab_type", "Preview");
            intent.putExtra("orderId", "NASDUBAI");
            intent.putExtra("pdfUri", pdfUri.toString());
            startActivity(intent);
            paymentWeb.setVisibility(View.GONE);

        }
        else {
//            Toast.makeText(mContext, "Print is not supported below Android KITKAT Version", Toast.LENGTH_SHORT).show();
        }
    }
    private File createWebPrintJobShare(WebView webView, File path) {
        String jobName = "Receipt_Share_" + "NASDUBAI"+ ".pdf";
        mProgressRelLayout.clearAnimation();
        mProgressRelLayout.setVisibility(View.GONE);
        paymentWeb.setVisibility(View.VISIBLE);
        //Log.d("CREATEJOB", fullHtml);


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
            paymentWeb.setVisibility(View.GONE);

        }
        return null;

    }
    private void setWebViewSettings() {
        mProgressRelLayout.setVisibility(View.VISIBLE);
        anim = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(mContext, android.R.interpolator.linear);
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(1000);
        mProgressRelLayout.setAnimation(anim);
        mProgressRelLayout.startAnimation(anim);
        paymentWeb.getSettings().setJavaScriptEnabled(true);
        backStatus="1";
        paymentWeb.clearCache(true);
        paymentWeb.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        paymentWeb.getSettings().setSupportMultipleWindows(true);
        paymentWeb.setWebViewClient(new PaymentDetailFeeOnlyActivity.MyWebViewClient());
        paymentWeb.setWebChromeClient(new PaymentDetailFeeOnlyActivity.MyWebChromeClient());
    }
    void loadWebViewWithData(String title, String description,String orderRequest) {
        StringBuffer sb = new StringBuffer();
        String eachLine = "";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(getAssets().open("paymentnew.html")));
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
            fullHtml = fullHtml.replace("####amount####", amount);
            fullHtml = fullHtml.replace("######orderId#####", orderRequest);
            fullHtml = fullHtml.replace("##merchantId##", PreferenceManager.getMerchantId(mContext));
            fullHtml = fullHtml.replace("######session_id#####", sessionId);
            fullHtml = fullHtml.replace("###payment_url###", PreferenceManager.getPaymentUrl(mContext));
            fullHtml = fullHtml.replace("###merchant_name###", PreferenceManager.getMerchantName(mContext));
            fullHtml = fullHtml.replace("#####title#####", invoice_description);
            backStatus = "1";
         //   System.out.println("merchant name"+PreferenceManager.getMerchantName(mContext));
//            Log.d("LOG3","fullHtml = "+fullHtml);
            paymentWeb.loadDataWithBaseURL("", fullHtml, "text/html", "UTF-8", "");
//            paymentWeb.loadUrl("javascript:Checkout.showLightbox();");

        }

    }
    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            //Calling a javascript function in html page
//            view.loadUrl("javascript:alert(showVersion('called by Android'))");


            view.loadUrl("javascript:Checkout.showLightbox()");


             Log.d("WebView", "your current url when webpage finished." + url);

            /*Live URL PAYMENT GATEWAY FOR GETTING RECEIPT*/
            /*https://network.gateway.mastercard.com/checkout/receipt/*/


            if (url.startsWith("https://network.gateway.mastercard.com/checkout/receipt/"))
            {
                mProgressRelLayout.setVisibility(View.GONE);
                mProgressRelLayout.clearAnimation();
                String tripDetailsAPI="{\n" +
                        "\"details\":[\n" +
                        "{\n" +
                        "\"amount\":\"" + amount + "\",\n" +
                        "\"order_id\":\"" + orderId + "\",\n" +
                        "\"payment_detail_id\":\"" + categoryId + "\",\n" +
                        "\"users_id\":\"" + PreferenceManager.getUserId(mContext) + "\",\n" +
                        "\"payment_date\":\"" + AppUtils.getCurrentDateToday() + "\",\n" +
                        "\"type\":\"" +"1"+ "\",\n" +
                        "\"student_id\":\"" + studentId + "\"\n" +
                        "}\n" +
                        "]\n" +
                        "}";
             //   Log.e("TripdetailsApi::::",tripDetailsAPI);
                tripSync(URL_FEE_PAYMENT_SUBMISSION, categoryId, studentId,tripDetailsAPI);
                goToNextView();
            }
            else
                {
                if (!isToastShown)
                {
                    isToastShown=true;
                    handler = new Handler();

                    final Runnable r = new Runnable() {
                        public void run() {
                            mProgressRelLayout.setVisibility(View.GONE);
                            mProgressRelLayout.clearAnimation();
                            Toast toast = Toast.makeText(mContext, "Your payment request is being processed.Please wait !!!!", Toast.LENGTH_LONG);
                            toast.show();
                            //  tv.append("Hello World");
                            //  handler.postDelayed(this, 15000);
                        }
                    };

                    handler.postDelayed(r, 6000);
                }


            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            backStatus="1";
             Log.d("WebView", "your current url when webpage loading.." + url);
//            if (url.startsWith("https://network.gateway.mastercard.com/checkout/receipt/"))
//            {
//                saveTask();
//            }
        }
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

    /*https://network.gateway.mastercard.com/checkout/receipt/SESSION0002030093378I80793691G9?resultIndicator=3c9926449c9f42cf&sessionVersion=f7b077bf08*/
    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {

             //  Log.d("LogTag", url);
            result.confirm();
            return true;
        }

        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            if (consoleMessage.message().contains("completeCallback"))
            {
             //   System.out.println("completeCallback");
                backStatus = "2";
                String tripDetailsAPI="{\n" +
                        "\"details\":[\n" +
                        "{\n" +
                        "\"amount\":\"" + amount + "\",\n" +
                        "\"order_id\":\"" + orderId + "\",\n" +
                        "\"payment_detail_id\":\"" + categoryId + "\",\n" +
                        "\"users_id\":\"" + PreferenceManager.getUserId(mContext) + "\",\n" +
                        "\"payment_date\":\"" + AppUtils.getCurrentDateToday() + "\",\n" +
                        "\"student_id\":\"" + studentId + "\"\n" +
                        "}\n" +
                        "]\n" +
                        "}";
         //       Log.e("TripdetailsApi::::",tripDetailsAPI);
//                tripSync(URL_FEE_PAYMENT_SUBMISSION, categoryId, studentId,tripDetailsAPI);
                //saveTask();

            }
            else if (consoleMessage.message().contains("cancelCallback")) {
             //   System.out.println("cancelCallback");
                payTotalButton.setVisibility(View.VISIBLE);
                totalLinear.setVisibility(View.VISIBLE);
                paidImg.setVisibility(View.GONE);
                mainLinear.setVisibility(View.VISIBLE);
                payment_date = AppUtils.getCurrentDateToday() ;

            } else if (consoleMessage.message().contains("errorCallback")) {
               // System.out.println("errorCallback");
                payTotalButton.setVisibility(View.VISIBLE);
                totalLinear.setVisibility(View.VISIBLE);
                paidImg.setVisibility(View.GONE);
                mainLinear.setVisibility(View.VISIBLE);
                payment_date = AppUtils.getCurrentDateToday() ;
            }


            return super.onConsoleMessage(consoleMessage);
        }
    }
    private void goToNextView() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
//                startService(new Intent(getBaseContext(), OnClearFromRecentService.class));
           //     saveTask();
                showDialogAlertPayment((Activity) mContext, "Alert", "Your payment has been successfully processed", R.drawable.exclamationicon, R.drawable.round);

            }
        }, 500);
    }
    void tripSync(final String URLHEAD, final String tripId, final String studentId,final String tripDetailsAPI) {
        VolleyWrapper volleyWrapper = new VolleyWrapper(URLHEAD);
        String[] name = {"access_token", "data"};
        String[] value = {PreferenceManager.getAccessToken(mContext), tripDetailsAPI};
     //   System.out.println("tripDetailsAPI :::"+tripDetailsAPI);
        Log.e("tripDetailsAPI :::",tripDetailsAPI);
        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("The response is student list progress" + successResponse);
                try {

                    JSONObject obj = new JSONObject(successResponse);
                    String response_code = obj.getString(JTAG_RESPONSECODE);
                    if (response_code.equalsIgnoreCase("200")) {
                        JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                        String status_code = secobj.getString(JTAG_STATUSCODE);
                        if (status_code.equalsIgnoreCase("303")) {

                            AppController.isAPISuccess = true;
                            AppController.isVisited = true;
                            JSONArray mJsonDataArray = secobj.optJSONArray("data");
                            if (mJsonDataArray.length() > 0) {
                                for (int i = 0; i < mJsonDataArray.length(); i++)
                                {
                                    FeePaymentModel mModel=new FeePaymentModel();
                                    JSONObject dataObject = mJsonDataArray.getJSONObject(i);
                                    if (dataObject.optString("status").equalsIgnoreCase("success"))
                                    {
//                                        JSONObject detailobj=dataObject.getJSONObject("details");
//                                        paid_amount=detailobj.optString("paid_amount");
//                                        orderId=detailobj.optString("order_id");
//                                        parent_name=detailobj.optString("parentName");
//                                        payment_date_print=detailobj.optString("payment_date");
//                                        invoice_note=detailobj.optString("invoice_note");
//                                        invoice_no=detailobj.optString("invoice_no");
//                                        isams_no=detailobj.optString("billing_code");
//                                        trn_no=detailobj.optString("trn_no");
//                                        payment_type=detailobj.optString("payment_type");
//                                        studentName=detailobj.optString("studentName");
//                                        formated_amount=detailobj.optString("formated_amount");
//                                        if (payment_type.equalsIgnoreCase("1"))
//                                        {
//                                            payment_type_print="Online";
//                                        }
//                                        else  if (payment_type.equalsIgnoreCase("2"))
//                                        {
//                                            payment_type_print="Cash";
//                                        }else  if (payment_type.equalsIgnoreCase("3"))
//                                        {
//                                            payment_type_print="Cheque";
//                                        }else  if (payment_type.equalsIgnoreCase("4"))
//                                        {
//                                            payment_type_print="Bank Transfer";
//                                        }
                                        payment_type_print="Online";
//                                        payTotalButton.setVisibility(View.GONE);
//                                        totalLinear.setVisibility(View.VISIBLE);
//                                        printLinear.setVisibility(View.VISIBLE);
//                                        paidImg.setVisibility(View.VISIBLE);
//                                        mainLinear.setVisibility(View.VISIBLE);
//                                        paymentWeb.setVisibility(View.GONE);


                                    }

                                }

                                }

                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                        // AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        tripSync(URLHEAD, tripId, studentId,tripDetailsAPI);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {

                            }
                        });
                        tripSync(URLHEAD, tripId, studentId,tripDetailsAPI);

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        tripSync(URLHEAD, tripId, studentId,tripDetailsAPI);

                    } else {


                    }
                } catch (Exception ex) {
                    System.out.println("The Exception in edit profile is" + ex.toString());
                }

            }

            @Override
            public void responseFailure(String failureResponse) {

            }
        });
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
        paymentWeb.getSettings().setJavaScriptEnabled(true);
        paymentWeb.clearCache(true);
        paymentWeb.getSettings().setDomStorageEnabled(true);
        paymentWeb.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        paymentWeb.getSettings().setSupportMultipleWindows(true);
        paymentWeb.setWebViewClient(new PaymentDetailFeeOnlyActivity.MyPrintWebViewClient());
//        paymentWeb.setWebChromeClient(new MyWebChromeClient());
    }
    void loadWebViewWithDataPrint() {
        StringBuffer sb = new StringBuffer();
        String eachLine = "";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(getAssets().open("receipfee.html")));
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
            fullHtml = fullHtml.replace("###amount###", current);
//            fullHtml = fullHtml.replace("###order_Id###", orderId);
            fullHtml = fullHtml.replace("###ParentName###",studentName);
            fullHtml = fullHtml.replace("###Date###", AppUtils.dateParsingTodd_MMM_yyyy(AppUtils.getCurrentDateToday()));
            fullHtml = fullHtml.replace("###paidBy###", invoice_note);
            fullHtml = fullHtml.replace("###billing_code###", isams_no);
            // fullHtml = fullHtml.replace("###paidBy###", "Done");
            fullHtml = fullHtml.replace("###title###", invoice_description);
            fullHtml = fullHtml.replace("###trn_no###", trn_no);
            fullHtml = fullHtml.replace("###order_Id###", invoice_no);
            fullHtml = fullHtml.replace("###payment_type###", payment_type_print);
            fullHtml = fullHtml.replace("###percentageAmount###", vat_amount);
            fullHtml = fullHtml.replace("###full-amount###", formated_amount);
            fullHtml = fullHtml.replace("###percent###", "("+vat_percentage+"%)");

            backStatus = "2";

//            Log.d("LOG3","fullHtml = "+fullHtml);
//            paymentWeb.loadDataWithBaseURL("", fullHtml, "text/html", "UTF-8", "");
            paymentWeb.loadDataWithBaseURL("file:///android_asset/images/", fullHtml, "text/html; charset=utf-8", "utf-8", "about:blank");
            //Log.e("LOADPRINT", fullHtml);


        }

    }
    private void createWebPrintJob(WebView webView) {
        mProgressRelLayout.clearAnimation();
        mProgressRelLayout.setVisibility(View.GONE);
        paymentWeb.setVisibility(View.GONE);
        PrintManager printManager = (PrintManager) this.getSystemService(Context.PRINT_SERVICE);
        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter();
        String jobName = getString(R.string.app_name) + "_Pay" + "NASDUBAI";
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
     //   System.out.println("Back status"+backStatus);
        if(backStatus.equalsIgnoreCase("0"))
        {
            finish();
        }
        else if (backStatus.equalsIgnoreCase("1"))
        {
            backStatus="0";
            payTotalButton.setVisibility(View.VISIBLE);
            totalLinear.setVisibility(View.VISIBLE);
            paidImg.setVisibility(View.GONE);
            mainLinear.setVisibility(View.VISIBLE);
            payment_date = AppUtils.getCurrentDateToday() ;
            handler.removeCallbacks(runnable);
            mProgressRelLayout.setVisibility(View.GONE);
            mProgressRelLayout.clearAnimation();
        }
        else if (backStatus.equalsIgnoreCase("2"))
        {
            backStatus="0";
            payTotalButton.setVisibility(View.GONE);
            totalLinear.setVisibility(View.VISIBLE);
            paidImg.setVisibility(View.VISIBLE);
            mainLinear.setVisibility(View.VISIBLE);
            paymentWeb.setVisibility(View.GONE);
            handler.removeCallbacks(runnable);
            mProgressRelLayout.setVisibility(View.GONE);
            mProgressRelLayout.clearAnimation();
        }
    }
    public void showDialogAlertPayment(final Activity activity, String msgHead, String msg, int ico,int bgIcon) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_dialogue_ok_layout);
        ImageView icon = (ImageView) dialog.findViewById(R.id.iconImageView);
        icon.setBackgroundResource(bgIcon);
        icon.setImageResource(ico);
        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        TextView textHead = (TextView) dialog.findViewById(R.id.alertHead);
        text.setText(msg);
        textHead.setText(msgHead);

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_Ok);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                payTotalButton.setVisibility(View.GONE);
                totalLinear.setVisibility(View.VISIBLE);
                printLinear.setVisibility(View.VISIBLE);
                paidImg.setVisibility(View.VISIBLE);
                mainLinear.setVisibility(View.VISIBLE);
                paymentWeb.setVisibility(View.GONE);

            }
        });
//		Button dialogButtonCancel = (Button) dialog.findViewById(R.id.btn_Cancel);
//		dialogButtonCancel.setVisibility(View.GONE);
//		dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				dialog.dismiss();
//			}
//		});
        dialog.show();

    }
}
