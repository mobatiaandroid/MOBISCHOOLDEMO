package com.mobatia.naisapp.activities.trips;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
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

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.gson.Gson;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.activities.trips.database.DatabaseClient;
import com.mobatia.naisapp.activities.trips.database.Trip;
import com.mobatia.naisapp.activities.web_view.SharePdfHtmlViewActivity;
import com.mobatia.naisapp.appcontroller.AppController;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.trips.TripListModel;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PaymentDetailActivity extends Activity implements URLConstants, JSONConstants, StatusConstants {
    Bundle extras;
    String amount;
    String orderId;
    String title;
    String trip_id;
    String last_payment_date;
    String completed_dated;
    String description;
    String last_payment_status;
    String trip_date_staus;
    String status = "";
    String payment_type = "";
    String invoiceNote = "";
    String parentName = "";
    String studentName = "";
    String tab_type = "";
    String payment_date = "";
    String installment_amount = "";
    String installment_id = "";
    String category_id = "";


    String trip_date;
    Context mContext;
    public String merchantid;
    public String payment_url;
    public String merchant_name;
    public String merchantpassword = "16496a68b8ac0fb9b6fde61274272457";
    public String postUrl;
    public String postBody = "";
    String auth_id;
    String sessionId;
    String sessionVersion;
    String stud_id = "";
    String fullHtml;
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back;
    LinearLayout addToCalendar;
    ImageView home;
    ImageView paidImg;
    ImageView closedImg;
    ImageView printImgView;
    //    ImageView alertImg;
    TextView titleTxt;
    TextView descriptionTxt;
    TextView amountTxt;
    TextView currencyTxt;
    TextView closingTxt;
    TextView proccedTxt;
    WebView paymentWeb;
    Button payButton;
    String backStatus = "0";
    private RelativeLayout mProgressRelLayout;
    private ArrayList<TripListModel> mListViewArray;
    RotateAnimation anim;
    public static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/octet-stream");
    String tripDetails = "";
    FloatingActionButton shareFloatActionButton;
    FloatingActionButton printFloatActionButton;
    FloatingActionsMenu rightFloatingActionMenu;
    PrintJob printJob;
    File pathFile;
       Uri pdfUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_detail);
        mContext = this;
        AppController.isVisited = false;

        extras = getIntent().getExtras();
        if (extras != null) {
            amount = extras.getString("amount");
            orderId = extras.getString("orderId");
            title = extras.getString("title");
            trip_id = extras.getString("trip_id");
            description = extras.getString("description");
            trip_date = extras.getString("trip_date");
            merchantid = extras.getString("merchant_id");
            merchant_name = extras.getString("merchant_name");
            auth_id = extras.getString("auth_id");
            postUrl = extras.getString("session_url");
            payment_url = extras.getString("payment_url");
            stud_id = extras.getString("stud_id");
            last_payment_date = extras.getString("last_payment_date");
            completed_dated = extras.getString("completed_dated");
            last_payment_status = extras.getString("last_payment_status");
            trip_date_staus = extras.getString("trip_date_staus");
            status = extras.getString("status");
            payment_type = extras.getString("payment_type");
            invoiceNote = extras.getString("invoiceNote");
            parentName = extras.getString("parentName");
            studentName = extras.getString("studentName");
            tab_type = extras.getString("tab_type");



        }
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        shareFloatActionButton = (FloatingActionButton) findViewById(R.id.shareFloatActionButton);
        printFloatActionButton = (FloatingActionButton) findViewById(R.id.printFloatActionButton);
        rightFloatingActionMenu = (FloatingActionsMenu) findViewById(R.id.right_labels);
        mProgressRelLayout = (RelativeLayout) findViewById(R.id.progressDialog);
        mProgressRelLayout.setVisibility(View.GONE);
        payButton = findViewById(R.id.payButton);
        descriptionTxt = findViewById(R.id.descriptionTxt);
        currencyTxt = findViewById(R.id.currencyTxt);
        paymentWeb = findViewById(R.id.paymentWeb);
        closedImg = findViewById(R.id.closedImg);
        printImgView = findViewById(R.id.printImgView);
        amountTxt = findViewById(R.id.amountTxt);
        proccedTxt = findViewById(R.id.proccedTxt);
        currencyTxt = findViewById(R.id.currencyTxt);
//        alertImg = findViewById(R.id.alertImg);
        paidImg = findViewById(R.id.paidImg);
        titleTxt = findViewById(R.id.titleTxt);
        closingTxt = findViewById(R.id.closingTxt);
        headermanager = new HeaderManager(this, tab_type);
        headermanager.getHeader(relativeHeader, 4);
        back = headermanager.getLeftButton();
        addToCalendar = headermanager.getLinearRight();
        headermanager.setButtonLeftSelector(R.drawable.back,
                R.drawable.back);
//        headermanager.setButtonRightSelector(R.drawable.add_calendar,
//                R.drawable.add_calendar);

        home = headermanager.getLogoButton();
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(mContext, HomeListAppCompatActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
            }
        });

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        String formattedDate = df.format(c);
        System.out.println("date " + formattedDate);
        amountTxt = findViewById(R.id.amountTxt);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("backstatus"+backStatus);
                      AppUtils.hideKeyBoard(mContext);
                if (backStatus.equalsIgnoreCase("0")) {
                    finish();

                } else if (backStatus.equalsIgnoreCase("1")) {
//                    proceed to pay
                    backStatus = "0";
                    closedImg.setVisibility(View.GONE);
                    paidImg.setVisibility(View.GONE);
                    amountTxt.setVisibility(View.VISIBLE);
                    amountTxt.setText(amount);
                    proccedTxt.setBackgroundResource(R.drawable.edittext_bg);
                    proccedTxt.setTextColor(getResources().getColor(R.color.white));

                    proccedTxt.setVisibility(View.VISIBLE);
                    currencyTxt.setVisibility(View.VISIBLE);
                    paymentWeb.setVisibility(View.GONE);

                } else if (backStatus.equalsIgnoreCase("2")) {
//                    paid
                    backStatus = "0";
                    closedImg.setVisibility(View.GONE);
                    paidImg.setVisibility(View.VISIBLE);
                    printImgView.setVisibility(View.INVISIBLE);
                    rightFloatingActionMenu.setVisibility(View.VISIBLE);
                    proccedTxt.setVisibility(View.GONE);
                    amountTxt.setVisibility(View.VISIBLE);
                    amountTxt.setText(amount);
                    currencyTxt.setVisibility(View.VISIBLE);
                    paymentWeb.setVisibility(View.GONE);
                    if (rightFloatingActionMenu.isExpanded()) {
                        rightFloatingActionMenu.collapse();
                        AppUtils.hideKeyBoard(mContext);
                    }
                }
                if (proccedTxt.getVisibility() == View.VISIBLE) {
                    addToCalendar.setVisibility(View.VISIBLE);
                } else {
                    addToCalendar.setVisibility(View.INVISIBLE);

                }
//                    proceed to pay

            }
        });

        descriptionTxt.setText(description);
        titleTxt.setText(title);
       /* if(!completed_dated.equalsIgnoreCase(""))
        {
            closingTxt.setText("Paid on " + AppUtils.dateConversionddmmyyyy(completed_dated));
        }
        else if (completed_dated.equalsIgnoreCase("") && last_payment_status.equalsIgnoreCase("0"))
        {
            closingTxt.setText("Closing on " + AppUtils.dateConversionddmmyyyy(last_payment_date));
        }
        else if (completed_dated.equalsIgnoreCase("") && last_payment_status.equalsIgnoreCase("1"))
        {
            closingTxt.setText("Closed on " + AppUtils.dateConversionddmmyyyy(last_payment_date));
        }*/
         /*if (last_payment_date.equalsIgnoreCase("")) {
            closingTxt.setText("Closing on ");

        } else {
            closingTxt.setText("Closing on " + AppUtils.dateConversionddmmyyyy(last_payment_date));

        }*/
        if (completed_dated.equalsIgnoreCase("") && last_payment_status.equalsIgnoreCase("0") && trip_date_staus.equalsIgnoreCase("0")) {
            Log.v("processing :", "1");
            //proceed to pay
            closedImg.setVisibility(View.GONE);
            paidImg.setVisibility(View.GONE);
            printImgView.setVisibility(View.GONE);
            rightFloatingActionMenu.setVisibility(View.GONE);

            amountTxt.setVisibility(View.VISIBLE);
            amountTxt.setText(amount);
            proccedTxt.setVisibility(View.VISIBLE);
            closingTxt.setText("Closing on " + AppUtils.dateConversionddmmyyyy(last_payment_date));
        } else if (completed_dated.equalsIgnoreCase("") && last_payment_status.equalsIgnoreCase("0") && trip_date_staus.equalsIgnoreCase("1")) {
            Log.v("processing :", "2");

            //closed
            closedImg.setVisibility(View.VISIBLE);
            paidImg.setVisibility(View.GONE);
            printImgView.setVisibility(View.GONE);
            rightFloatingActionMenu.setVisibility(View.GONE);
            closingTxt.setText("Closed on " + AppUtils.dateConversionddmmyyyy(last_payment_date));
            amountTxt.setVisibility(View.GONE);
            currencyTxt.setVisibility(View.GONE);
            proccedTxt.setVisibility(View.GONE);
        } else if (completed_dated.equalsIgnoreCase("") && last_payment_status.equalsIgnoreCase("1") && trip_date_staus.equalsIgnoreCase("0")) {
            Log.v("processing :", "3");

            //payment date over
            closedImg.setVisibility(View.VISIBLE);
            paidImg.setVisibility(View.GONE);
            printImgView.setVisibility(View.GONE);
            rightFloatingActionMenu.setVisibility(View.GONE);
            closingTxt.setText("Closed on " + AppUtils.dateConversionddmmyyyy(last_payment_date));
            amountTxt.setVisibility(View.GONE);
            currencyTxt.setVisibility(View.GONE);
            proccedTxt.setVisibility(View.GONE);
        } else if (completed_dated.equalsIgnoreCase("") && last_payment_status.equalsIgnoreCase("1") && trip_date_staus.equalsIgnoreCase("1")) {
            Log.v("processing :", "4");

            //closed
            closedImg.setVisibility(View.VISIBLE);
            paidImg.setVisibility(View.GONE);
            printImgView.setVisibility(View.GONE);
            rightFloatingActionMenu.setVisibility(View.GONE);
            closingTxt.setText("Closed on " + AppUtils.dateConversionddmmyyyy(last_payment_date));
            amountTxt.setVisibility(View.GONE);
            currencyTxt.setVisibility(View.GONE);
            proccedTxt.setVisibility(View.GONE);
        } else if (!(completed_dated.equalsIgnoreCase("")) && last_payment_status.equalsIgnoreCase("0") && trip_date_staus.equalsIgnoreCase("0")) {
            Log.v("processing :", "5");

            //paid
            closedImg.setVisibility(View.GONE);
            paidImg.setVisibility(View.VISIBLE);
            printImgView.setVisibility(View.INVISIBLE);
            rightFloatingActionMenu.setVisibility(View.VISIBLE);

            proccedTxt.setVisibility(View.GONE);
            amountTxt.setVisibility(View.VISIBLE);
            amountTxt.setText(amount);
            closingTxt.setText("Paid on " + AppUtils.dateConversionddmmyyyy(completed_dated));
            currencyTxt.setVisibility(View.VISIBLE);
        } else if (!(completed_dated.equalsIgnoreCase("")) && last_payment_status.equalsIgnoreCase("0") && trip_date_staus.equalsIgnoreCase("1")) {
            Log.v("processing :", "6");

            //paid
            closedImg.setVisibility(View.GONE);
            paidImg.setVisibility(View.VISIBLE);
            printImgView.setVisibility(View.INVISIBLE);
            rightFloatingActionMenu.setVisibility(View.VISIBLE);
            closingTxt.setText("Paid on " + AppUtils.dateConversionddmmyyyy(completed_dated));
            proccedTxt.setVisibility(View.GONE);
            amountTxt.setVisibility(View.VISIBLE);
            amountTxt.setText(amount);
            currencyTxt.setVisibility(View.VISIBLE);
        } else if (!(completed_dated.equalsIgnoreCase("")) && last_payment_status.equalsIgnoreCase("1") && trip_date_staus.equalsIgnoreCase("0")) {
            Log.v("processing :", "7");

            //paid
            closedImg.setVisibility(View.GONE);
            paidImg.setVisibility(View.VISIBLE);
            printImgView.setVisibility(View.INVISIBLE);
            rightFloatingActionMenu.setVisibility(View.VISIBLE);
            closingTxt.setText("Paid on " + AppUtils.dateConversionddmmyyyy(completed_dated));
            proccedTxt.setVisibility(View.GONE);
            amountTxt.setVisibility(View.VISIBLE);
            amountTxt.setText(amount);
            currencyTxt.setVisibility(View.VISIBLE);
        } else if (!(completed_dated.equalsIgnoreCase("")) && last_payment_status.equalsIgnoreCase("1") && trip_date_staus.equalsIgnoreCase("1")) {
            Log.v("processing :", "8");

            //paid
            paidImg.setVisibility(View.VISIBLE);
            closedImg.setVisibility(View.GONE);
            printImgView.setVisibility(View.INVISIBLE);
            rightFloatingActionMenu.setVisibility(View.VISIBLE);
            closingTxt.setText("Paid on " + AppUtils.dateConversionddmmyyyy(completed_dated));
            proccedTxt.setVisibility(View.GONE);
            amountTxt.setVisibility(View.VISIBLE);
            amountTxt.setText(amount);
            currencyTxt.setVisibility(View.VISIBLE);
        }

        if (proccedTxt.getVisibility() == View.VISIBLE) {
            addToCalendar.setVisibility(View.VISIBLE);
        } else {
            addToCalendar.setVisibility(View.INVISIBLE);

        }
        postBody = "{\n" +
                "\"apiOperation\":\"CREATE_CHECKOUT_SESSION\",\n" +
                "\"order\":{\n" +
                "\"currency\":\"AED\",\n" +
                "\"id\":\"" + orderId + "\"\n" +
                "}\n" +
                "}";

        proccedTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backStatus="1";
                if (AppUtils.isNetworkConnected(mContext)) {
                    if (completed_dated.equalsIgnoreCase("")) {
                        System.out.println("clicked ");
                        proccedTxt.setTextColor(getResources().getColor(R.color.grey));
                        proccedTxt.setBackgroundColor(getResources().getColor(R.color.white));
                        paymentWeb.setVisibility(View.VISIBLE);
                        if (paymentWeb.getVisibility() == View.VISIBLE) {
                            addToCalendar.setVisibility(View.INVISIBLE);
                        } else {
                            addToCalendar.setVisibility(View.VISIBLE);

                        }
                        try {
                            postRequest(postUrl, postBody, merchantpassword);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }


                }
                else
                {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                }
            }
        });


        printImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
//                    paymentWeb.loadUrl("about:blank");
//                    setWebViewSettingsPrint();
//                    loadWebViewWithDataPrint("","");
//                    createWebPrintJob(paymentWeb);
//                }else {
//                    Toast.makeText(mContext, "Print is not supported below Android KITKAT Version", Toast.LENGTH_SHORT).show();
//                }


            }
        });
        printFloatActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        shareFloatActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                createPDF(loadWebViewWithDataShare("",""), orderId+".pdf", (ContextWrapper) mContext);

                if ((int) Build.VERSION.SDK_INT >= 23) {
                    TedPermission.with(mContext)
                            .setPermissionListener(permissionListenerStorage)
                            .setDeniedMessage("If you reject permission,you cannot use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                            .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                            .check();
                } else {
                    sharePdfFile();
                }
            }
        });
        addToCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backStatus="1";
                long startTime = 0;

                try {
                    Date dateStart = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(last_payment_date);
                    startTime = dateStart.getTime();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(Intent.ACTION_EDIT);
                intent.setType("vnd.android.cursor.item/event");
                intent.putExtra("beginTime", startTime);
                intent.putExtra("allDay", true);
                intent.putExtra("title", title);
                startActivity(intent);
            }
        });
        if (!(status.equalsIgnoreCase("1"))) {
            if (AppUtils.isNetworkConnected(mContext)) {
                callStatusChangeApi(URL_GET_STATUS_CHANGE_API, trip_id);
            }
        }

    }
    PermissionListener permissionListenerStorage = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            sharePdfFile();

        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(mContext, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }


    };
    void sharePdfFile() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            paymentWeb.loadUrl("about:blank");
            setWebViewSettingsPrint();
            loadWebViewWithDataPrint();
//                    createWebPrintJobShare(paymentWeb);
             pathFile= new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/" + "NAS_DUBAI_TRIP/Payments" + "_" + orderId + "/");

            pathFile.mkdirs();
//            if(!pathFile.exists())
//                pathFile.mkdirs();
            if (Build.VERSION.SDK_INT >= 23) {

                pdfUri = FileProvider.getUriForFile(mContext, getPackageName() + ".provider", createWebPrintJobShare(paymentWeb,pathFile));
            } else {
                pdfUri = Uri.fromFile(createWebPrintJobShare(paymentWeb,pathFile));
            }

//            Intent share = new Intent();
//            share.setAction(Intent.ACTION_SEND);
//            share.setType("application/pdf");
//            Log.v("uriFile",pdfUri.toString());
//            share.putExtra(Intent.EXTRA_STREAM, pdfUri);
//            startActivity(Intent.createChooser(share, "Share"));

            Intent intent = new Intent(mContext, SharePdfHtmlViewActivity.class);
            intent.putExtra("url", fullHtml);
            intent.putExtra("tab_type", "Preview");
            intent.putExtra("orderId", orderId);
            intent.putExtra("pdfUri", pdfUri.toString());
            startActivity(intent);
            paymentWeb.setVisibility(View.GONE);

        } else {
//            Toast.makeText(mContext, "Print is not supported below Android KITKAT Version", Toast.LENGTH_SHORT).show();
        }
    }



    //    @SuppressLint("SetJavaScriptEnabled")
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
        paymentWeb.setWebViewClient(new MyWebViewClient());
        paymentWeb.setWebChromeClient(new MyWebChromeClient());
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
        paymentWeb.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        paymentWeb.getSettings().setSupportMultipleWindows(true);
        paymentWeb.setWebViewClient(new MyPrintWebViewClient());
//        paymentWeb.setWebChromeClient(new MyWebChromeClient());
    }

    void loadWebViewWithDataPrint() {
        StringBuffer sb = new StringBuffer();
        String eachLine = "";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(getAssets().open("payreciept.html")));
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
            fullHtml = fullHtml.replace("###ParentName###", parentName);
            fullHtml = fullHtml.replace("###Date###", AppUtils.dateParsingTodd_MMM_yyyy(completed_dated));
            fullHtml = fullHtml.replace("###paidBy###", invoiceNote);
            fullHtml = fullHtml.replace("###title###", title);
            fullHtml = fullHtml.replace("###payment_type###", payment_type);
            backStatus = "2";

//            Log.d("LOG3","fullHtml = "+fullHtml);
//            paymentWeb.loadDataWithBaseURL("", fullHtml, "text/html", "UTF-8", "");
            paymentWeb.loadDataWithBaseURL("file:///android_asset/", fullHtml, "text/html; charset=utf-8", "utf-8", "about:blank");


        }

    }

    void loadWebViewWithData(String title, String description) {
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
            fullHtml = fullHtml.replace("######orderId#####", orderId);
            fullHtml = fullHtml.replace("##merchantId##", merchantid);
            fullHtml = fullHtml.replace("######session_id#####", sessionId);
            fullHtml = fullHtml.replace("###payment_url###", payment_url);
            fullHtml = fullHtml.replace("###merchant_name###", merchant_name);
           fullHtml = fullHtml.replace("#####title#####", title);
            backStatus = "1";

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
            mProgressRelLayout.clearAnimation();
            mProgressRelLayout.setVisibility(View.GONE);
            view.loadUrl("javascript:Checkout.showLightbox()");
            Log.d("WebView", "your current url when webpage finished." + url);
            if (url.startsWith("https://network.gateway.mastercard.com/checkout/receipt/")) {
                goToNextView();
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

            Log.d("WebView", "print webpage loading.." + url);

        }
    }

    /*https://network.gateway.mastercard.com/checkout/receipt/SESSION0002030093378I80793691G9?resultIndicator=3c9926449c9f42cf&sessionVersion=f7b077bf08*/
    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {

            Log.d("LogTag", message);
            result.confirm();
            return true;
        }

        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            if (consoleMessage.message().contains("completeCallback")) {
                System.out.println("completeCallback");
                saveTask();

            } else if (consoleMessage.message().contains("cancelCallback")) {
                System.out.println("cancelCallback");
                closedImg.setVisibility(View.GONE);
                paidImg.setVisibility(View.GONE);
                printImgView.setVisibility(View.GONE);
                rightFloatingActionMenu.setVisibility(View.GONE);

                paymentWeb.setVisibility(View.GONE);
                amountTxt.setVisibility(View.VISIBLE);
                amountTxt.setText(amount);
                proccedTxt.setBackgroundResource(R.drawable.edittext_bg);
                proccedTxt.setTextColor(getResources().getColor(R.color.white));

                proccedTxt.setVisibility(View.VISIBLE);
                if (proccedTxt.getVisibility() == View.VISIBLE) {
                    addToCalendar.setVisibility(View.VISIBLE);
                } else {
                    addToCalendar.setVisibility(View.INVISIBLE);

                }
            } else if (consoleMessage.message().contains("errorCallback")) {
                System.out.println("errorCallback");
            }


            return super.onConsoleMessage(consoleMessage);
        }
    }


    void postRequest(final String postUrl, final String postBody, String merchantPassword) throws IOException {

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON_MEDIA_TYPE, postBody);
        final Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .addHeader("authorization", auth_id)
                .addHeader("postman-token", "0bc67956-9357-f12e-65d9-1da59f8c74d5")
                .addHeader("cache-control", "no-cache")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                System.out.println("postUrl::"+response);
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
                                    closedImg.setVisibility(View.GONE);
                                    if (sessionId.equalsIgnoreCase("")) {
                                        try {
                                            postRequest(postUrl, postBody, merchantpassword);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    } else {

                                        setWebViewSettings();
                                        loadWebViewWithData(title, description);
                                        Log.d("LOG3", fullHtml);
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

    public void tripSubmiotApi(final String URLHEAD, final String tripId, final String studentId) {

        TripSubmit mTripSubmit = new TripSubmit(URLHEAD, tripId, studentId);
        mTripSubmit.execute();


    }

    class TripSubmit extends AsyncTask<Void, Void, Void> {
        List<Trip> mTripList;
        String url;
        String tripId;
        String studentId;

        TripSubmit(String urlHead, String mTripId, String mStudentId) {
            this.url = urlHead;
            this.tripId = mTripId;
            this.studentId = mStudentId;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mTripList = DatabaseClient.getInstance(mContext).getAppDatabase()
                    .tripDao().getTripUnsyncWithId("0", studentId, PreferenceManager.getUserId(mContext));//trip with status zero
            Gson gson = new Gson();
//                gson.toJson(mTripList);
//                Log.v("gson to json:", " " +  gson.toJson(mTripList));
            tripDetails = "{\"details\":" + gson.toJson(mTripList) + "}";

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (mTripList.size() > 0) {
                tripSync(url, tripId, studentId);
            }

        }


    }

    void tripSync(final String URLHEAD, final String tripId, final String studentId) {
        VolleyWrapper volleyWrapper = new VolleyWrapper(URLHEAD);
        String[] name = {"access_token", "data"};
        String[] value = {PreferenceManager.getAccessToken(mContext), tripDetails};
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
                            closedImg.setVisibility(View.GONE);
                            paymentWeb.setVisibility(View.GONE);
                            paidImg.setVisibility(View.VISIBLE);
                            printImgView.setVisibility(View.INVISIBLE);
                            rightFloatingActionMenu.setVisibility(View.VISIBLE);
                            proccedTxt.setVisibility(View.GONE);
                            amountTxt.setVisibility(View.VISIBLE);
                            amountTxt.setText(amount);
                            currencyTxt.setVisibility(View.VISIBLE);
//                            Trip tripWithId = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
//                                    .tripDao().getTripWithId(tripId, studentId, PreferenceManager.getUserId(mContext));
//                            DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
//                                    .tripDao().delete(tripWithId);
                            /*"data":[{"orderId":"NAS_DUBAI_2S_3189","status":"success","details":null}]}}*/

                            JSONArray mJsonDataArray = secobj.optJSONArray("data");

                            DeleteQueryAsyncTask mDeleteQueryAsyncTask = new DeleteQueryAsyncTask(tripId, studentId, mJsonDataArray);
                            mDeleteQueryAsyncTask.execute();
                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                        // AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        tripSync(URLHEAD, tripId, studentId);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {

                            }
                        });
                        tripSync(URLHEAD, tripId, studentId);

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        tripSync(URLHEAD, tripId, studentId);

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

   public class DeleteQueryAsyncTask extends AsyncTask<Void, Void, Void> {

        String tripId = "";
        String studentId = "";
        JSONArray orderDataArray;

        DeleteQueryAsyncTask(String mTripId, String mStudentId, JSONArray mOrderDataArray) {
            this.tripId = mTripId;
            this.studentId = mStudentId;
            this.orderDataArray = mOrderDataArray;
        }

        @Override
        protected Void doInBackground(Void... voids) {
//            Trip tripWithId = DatabaseClient.getInstance(mContext).getAppDatabase()
//                    .tripDao().getTripWithId(tripId, studentId, PreferenceManager.getUserId(mContext));
            for (int i = 0; i < orderDataArray.length(); i++) {
                JSONObject mOrderData = orderDataArray.optJSONObject(i);

                if (mOrderData.optString("status").equalsIgnoreCase("success")) {
                    if (mOrderData.optString("orderId").equalsIgnoreCase(orderId))
                    {
                        JSONObject mDetail=mOrderData.optJSONObject("details");
                        completed_dated=mDetail.optString("completed_dated");
                        invoiceNote=mDetail.optString("invoiceNote");
                    }
                    DatabaseClient.getInstance(mContext).getAppDatabase()
                            .tripDao().deleteTripWithOrderId(mOrderData.optString("orderId"), PreferenceManager.getUserId(mContext));
                }
            }

//            DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
//                    .tripDao().delete(tripWithId);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            closingTxt.setText("Paid on " + AppUtils.dateConversionddmmyyyy(completed_dated));

        }

    }

    @Override
    public void onBackPressed() {
        if (backStatus.equalsIgnoreCase("0")) {
            finish();

        } else if (backStatus.equalsIgnoreCase("1")) {
            backStatus = "0";
            closedImg.setVisibility(View.GONE);
            paidImg.setVisibility(View.GONE);
            printImgView.setVisibility(View.GONE);
            rightFloatingActionMenu.setVisibility(View.GONE);

            amountTxt.setVisibility(View.VISIBLE);
            amountTxt.setText(amount);
            proccedTxt.setBackgroundResource(R.drawable.edittext_bg);
            proccedTxt.setTextColor(getResources().getColor(R.color.white));

            proccedTxt.setVisibility(View.VISIBLE);
            paymentWeb.setVisibility(View.GONE);

        } else if (backStatus.equalsIgnoreCase("2")) {
            backStatus = "0";
            closedImg.setVisibility(View.GONE);
            paidImg.setVisibility(View.VISIBLE);
            printImgView.setVisibility(View.INVISIBLE);
            rightFloatingActionMenu.setVisibility(View.VISIBLE);

            proccedTxt.setVisibility(View.GONE);
            amountTxt.setVisibility(View.VISIBLE);
            amountTxt.setText(amount);
            currencyTxt.setVisibility(View.VISIBLE);
            paymentWeb.setVisibility(View.GONE);
            if (rightFloatingActionMenu.isExpanded()) {
                rightFloatingActionMenu.collapse();
                AppUtils.hideKeyBoard(mContext);
            }
        }
        if (proccedTxt.getVisibility() == View.VISIBLE) {
            addToCalendar.setVisibility(View.VISIBLE);
        } else {
            addToCalendar.setVisibility(View.INVISIBLE);

        }
    }

    private void saveTask() {
        final String tTripId = trip_id;
        final String tStatus = "0";
        final String tStudentId = stud_id;

        class SaveTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                //creating a task
                Trip task = new Trip();
                task.setTrip_id(tTripId);
                task.setStatus(tStatus);
                task.setStudent_id(tStudentId);
                task.setOrder_id(orderId);
                task.setUsers_id(PreferenceManager.getUserId(mContext));

                //adding to database
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .tripDao()
                        .insert(task);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

//                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
                backStatus = "2";
                if (AppUtils.isNetworkConnected(mContext)) {
                    tripSubmiotApi(URL_TRIP_SUBMISSION, tTripId, tStudentId);
                } else {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                }

            }

        }

        SaveTask st = new SaveTask();
        st.execute();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        AppUtils.hideKeyBoard(mContext);
        if (AppController.isVisited) {
//            paid
            AppController.isVisited = false;
            closedImg.setVisibility(View.GONE);
            paidImg.setVisibility(View.VISIBLE);
            proccedTxt.setVisibility(View.GONE);
            amountTxt.setVisibility(View.VISIBLE);
            amountTxt.setText(amount);
            currencyTxt.setVisibility(View.VISIBLE);
            if (rightFloatingActionMenu.isExpanded()) {
                rightFloatingActionMenu.collapse();
                AppUtils.hideKeyBoard(mContext);
            }
        }
    }

    private void goToNextView() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
//                startService(new Intent(getBaseContext(), OnClearFromRecentService.class));
                saveTask();

            }
        }, 10000);
    }

    private void callStatusChangeApi(final String URL, final String id) {
        VolleyWrapper volleyWrapper = new VolleyWrapper(URL);
        String[] name = {"access_token", "users_id", "id", "type"};
        String[] value = {PreferenceManager.getAccessToken(mContext), PreferenceManager.getUserId(mContext), id, "payment"};
        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                try {
                    JSONObject obj = new JSONObject(successResponse);
                    String response_code = obj.getString(JTAG_RESPONSECODE);
                    if (response_code.equalsIgnoreCase("200")) {
                        JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                        String status_code = secobj.getString(JTAG_STATUSCODE);
                        if (status_code.equalsIgnoreCase("303")) {


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
                        callStatusChangeApi(URL, id);

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

    private File createWebPrintJobShare(WebView webView,File path) {
        String jobName = "Receipt_Share_" + orderId+ ".pdf";
        mProgressRelLayout.clearAnimation();
        mProgressRelLayout.setVisibility(View.GONE);
        paymentWeb.setVisibility(View.VISIBLE);
        try {
//            File path = new File(Environment.getExternalStorageDirectory()
//                    .getAbsolutePath() + "/" + "NAS_DUBAI/TripPayments" + "_" + orderId + "/");
//
//            path.mkdirs();
            PrintDocumentAdapter printAdapter;
//            String jobName = getString(R.string.app_name) + " Document";
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
//            File file = new File(path.getAbsolutePath() + "/" + jobName);
//            file.mkdirs();
            Log.v("pathfile", path.getAbsolutePath() + "/"  + jobName);
            return new File(path.getAbsolutePath() + "/"  + jobName);
        } catch (Exception e) {
            e.printStackTrace();
            paymentWeb.setVisibility(View.GONE);

        }
        return null;

    }

    private void createWebPrintJob(WebView webView) {
        mProgressRelLayout.clearAnimation();
        mProgressRelLayout.setVisibility(View.GONE);
        paymentWeb.setVisibility(View.GONE);
//        PrintManager printManager = (PrintManager) this
//                .getSystemService(Context.PRINT_SERVICE);
//
//        PrintDocumentAdapter printAdapter =
//                webView.createPrintDocumentAdapter();
//
//        String jobName = getString(R.string.app_name) + " Print Test";
//
//        if (printManager != null) {
//            printManager.print(jobName, printAdapter,
//                    new PrintAttributes.Builder().build());
//        }

        PrintManager printManager = (PrintManager) this.getSystemService(Context.PRINT_SERVICE);
        PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter();
        String jobName = getString(R.string.app_name) + "_Pay" + orderId;
        PrintAttributes.Builder builder = new PrintAttributes.Builder();
        builder.setMediaSize(PrintAttributes.MediaSize.ISO_A4);
        if (printManager != null) {
            printJob = printManager.print(jobName, printAdapter, builder.build());
//            printJob =  printManager.print(jobName, printAdapter,
//                    new PrintAttributes.Builder().build());
        }
        if (printJob.isCompleted()) {
//            Toast.makeText(getApplicationContext(), R.string.print_complete, Toast.LENGTH_LONG).show();
        } else if (printJob.isFailed()) {
            Toast.makeText(getApplicationContext(), "Print failed", Toast.LENGTH_SHORT).show();
        }

//        String jobName = getString(R.string.app_name) + "_Pay"+orderId;
//        PrintAttributes attributes = new PrintAttributes.Builder()
//                .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
//                .setResolution(new PrintAttributes.Resolution("pdf", "pdf", 600, 600))
//                .setMinMargins(PrintAttributes.Margins.NO_MARGINS).build();
//        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS+ "/NAS_PAYMENT/");
//        PdfPrint pdfPrint = new PdfPrint(attributes);
//        pdfPrint.print(webView.createPrintDocumentAdapter(jobName), path, "output_" + System.currentTimeMillis() + ".pdf");

    }

//    public boolean createPDF(String rawHTML, String fileName, ContextWrapper context) {
//        final String APPLICATION_PACKAGE_NAME = context.getBaseContext().getPackageName();
//        File path = new File(Environment.getExternalStorageDirectory(), APPLICATION_PACKAGE_NAME);
//        if (!path.exists()) {
//            path.mkdir();
//        }
//        File file = new File(path, fileName);
//
//        try {
//
//            Document document = new Document();
//            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
//            document.open();
//
//            // Подготавливаем HTML
//            String htmlText = Jsoup.clean(rawHTML, Whitelist.relaxed());
//            InputStream inputStream = new ByteArrayInputStream(htmlText.getBytes());
//
//            // Печатаем документ PDF
//            XMLWorkerHelper.getInstance().parseXHtml(writer, document,
//                    inputStream, null, Charset.defaultCharset(), new PdfSaveFont());
//
//            document.close();
//            return true;
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            return false;
//        } catch (DocumentException e) {
//            e.printStackTrace();
//            return false;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }
//
//
//    }
}