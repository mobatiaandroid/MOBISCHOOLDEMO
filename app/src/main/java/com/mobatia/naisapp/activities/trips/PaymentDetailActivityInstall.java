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
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.itextpdf.text.Document;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.pdf.PdfWriter;
import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.activities.trips.adapter.PaymentDetailAdapter;
import com.mobatia.naisapp.activities.trips.database.DatabaseClient;
import com.mobatia.naisapp.activities.trips.database.Trip;
import com.mobatia.naisapp.activities.trips.model.FullPaymentListModel;
import com.mobatia.naisapp.activities.trips.model.InstallmentListModel;
import com.mobatia.naisapp.activities.web_view.DownloadPdfHtmlViewActivity;
import com.mobatia.naisapp.activities.web_view.SharePdfHtmlViewActivity;
import com.mobatia.naisapp.appcontroller.AppController;
import com.mobatia.naisapp.constants.ClickListener;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.trips.TripListModel;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.HeaderManager;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.recyclerviewmanager.DividerItemDecorationNew;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PaymentDetailActivityInstall extends Activity implements URLConstants, JSONConstants, StatusConstants {
    Context mContext;
    TextView descriptionTitle;
    TextView descriptionTxt;
    TextView closingTxt;
    TextView installmentmainTxt;
    RecyclerView mEmiRecycler;
    Bundle extras;
    int adapterSize;
    int Position;
    String amount;
    String categoryId;
    WebView paymentWeb;
    String orderId;
    String trip_id;
    public String merchantid;
    public String payment_url;
    public String merchant_name;
    static String auth_id;
    public String postUrl;
    String title;
    String instalTitle;
    String totalAmtInst;
    String titlePrint;
    HeaderManager headermanager;
    ImageView back;
    RelativeLayout relativeHeader;
    ImageView home;
    LinearLayout addToCalendar;
    LinearLayout mainLinear;
    LinearLayout printLinear;
    LinearLayout emailLinear;
    LinearLayout printLinearClick;
    LinearLayout downloadLinear;
    LinearLayout shareLinear;
    String orderIdPrint;
    private ArrayList<TripListModel> mListViewArray;
    private ArrayList<TripListModel> mListViewArraySuccess;
    private ArrayList<FullPaymentListModel> mFullPaymentListModelArray;
    private static ArrayList<FullPaymentListModel> mFullPaymentListModelArraySuccess;
    String tab_type = "";
    String stud_id = "";
    String billing_code = "";
    String tripDetails = "";
    String paidDate = "";
    String installFull = "";
    PaymentDetailAdapter adapter;
    public String postBody = "";
    Boolean isShowPaymentView = false;
    String sessionId;
    String sessionVersion;
    RotateAnimation anim;
    String descriptionWeb;
    private RelativeLayout mProgressRelLayout;
    String fullHtml;
    String installFullHtml;
    public final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/octet-stream");
    public String merchantpassword = "16496a68b8ac0fb9b6fde61274272457";
    Boolean isFullPayment;
    String payment_date = "";
    String installment_amount = "";
    String installment_id = "";
    String category_id = "";
    String payment_option = "";
    String installment = "";
    String status = "";
    String amountFull = "";
    String invoiceNote = "";
    String paidBy = "";
    String backStatus = "0";
    Boolean isInstallPage = false;
    Boolean isFullPayPage = false;
    ImageView print, download, share;
    PrintJob printJob;
    String completed_date;
    int enablePosition;
    String paidStatusInstall="0";
    String description="";
    boolean isInvoiceInstall=false;
    File pathFile;
    Uri pdfUri;
    ScrollView scroll;
    boolean isDownload=false;
    boolean isShareInstall=false;
    boolean isDoenloadInstall=false;
    private ArrayList<InstallmentListModel> mInstallmentModelArrayList;
    private static ArrayList<InstallmentListModel> mInstallmentModelArrayListSuccess;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_detail_emi_new);
        mContext = this;
        initUI();
        //"NASDUBAI" + mListViewArray.get(position).getId() + "S" + stud_id
    }
    public void initUI() {
        mListViewArray = new ArrayList<>();
        mInstallmentModelArrayList = new ArrayList<>();
        mListViewArraySuccess = new ArrayList<>();
        mFullPaymentListModelArray = new ArrayList<>();

        extras = getIntent().getExtras();
        if (extras != null) {

            mListViewArray = (ArrayList<TripListModel>) getIntent().getSerializableExtra("key");
            adapterSize = extras.getInt("adapterSize");
            AppController.Position = extras.getInt("pos");
            //orderId = extras.getString("orderId");
            System.out.println("order Id" + orderId);
            merchantid = extras.getString("merchant_id");
            merchant_name = extras.getString("merchant_name");
            auth_id = extras.getString("auth_id");
            postUrl = extras.getString("session_url");
            payment_url = extras.getString("payment_url");
            stud_id = extras.getString("stud_id");
            categoryId = extras.getString("categoryId");
            status = extras.getString("status");
            title = "test title";
            tab_type = extras.getString("tab_type");


        }
        if (!(status.equalsIgnoreCase("1"))) {
            if (AppUtils.isNetworkConnected(mContext)) {
                System.out.println("working call status");
                callStatusChangeApi(URL_GET_STATUS_CHANGE_API, trip_id);
            }
        }
        trip_id=mListViewArray.get(AppController.Position).getId();
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        headermanager = new HeaderManager(this, tab_type);
        headermanager.getHeader(relativeHeader, 4);
        back = headermanager.getLeftButton();
        mProgressRelLayout = (RelativeLayout) findViewById(R.id.progressDialog);
        download = (ImageView) findViewById(R.id.download);
        share = (ImageView) findViewById(R.id.share);
        print = (ImageView) findViewById(R.id.print);
        installmentmainTxt = (TextView) findViewById(R.id.installmentmainTxt);
        mProgressRelLayout.setVisibility(View.GONE);

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
        descriptionTitle=findViewById(R.id.descriptionTitle);
        printLinear=findViewById(R.id.printLinear);
        printLinearClick=findViewById(R.id.printLinearClick);
        emailLinear=findViewById(R.id.emailLinear);
        downloadLinear=findViewById(R.id.downloadLinear);
        shareLinear=findViewById(R.id.shareLinear);
        descriptionTxt=findViewById(R.id.description);
        mEmiRecycler=findViewById(R.id.mEmiRecycler);
        closingTxt=findViewById(R.id.closingTxt);
        paymentWeb=findViewById(R.id.paymentWeb);
        scroll=findViewById(R.id.scroll);
        mainLinear=findViewById(R.id.mainLinear);
        mainLinear.setVisibility(View.VISIBLE);
        paymentWeb.setVisibility(View.GONE);
        mEmiRecycler.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        mEmiRecycler.setLayoutManager(new LinearLayoutManager(this));
        mEmiRecycler.addItemDecoration(new DividerItemDecorationNew(getResources().getDrawable(R.drawable.linegrey)));
        mInstallmentModelArrayList=mListViewArray.get(AppController.Position).getInstallmentArrayList();
        mFullPaymentListModelArray=mListViewArray.get(AppController.Position).getFullpaymentArrayList();
        description=mListViewArray.get(AppController.Position).getDescription();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // System.out.println("backstatus"+backStatus);
                /*AppUtils.hideKeyBoard(mContext);
                finish();*/
                if (backStatus.equalsIgnoreCase("0")) {
                    finish();

                }else {
                    backStatus="0";
                    mainLinear.setVisibility(View.VISIBLE);
                    paymentWeb.setVisibility(View.GONE);
                    reload();
                }


            }
        });
        //title

        reload();


    }
    public void reload()
    {
        int installEnable=0;
            if (mListViewArray.get(AppController.Position).isEmiAvailable())
            {
                     // Emi Available
                for (int i=0;i<mInstallmentModelArrayList.size();i++)
                {
                    if (mInstallmentModelArrayList.get(i).getPaid_status().equalsIgnoreCase("1"))
                    {

                        mInstallmentModelArrayList.get(i).setEnable("0");
                        paidStatusInstall="1";
                        mInstallmentModelArrayList.get(i).setPaid(true);
                    }
                    else {
                        paidStatusInstall="2";
                        if (installEnable==0)
                        {
                            mInstallmentModelArrayList.get(i).setEnable("-1");
                            enablePosition=i;
                            mInstallmentModelArrayList.get(i).setEnablePosition(i);
                        }
                        else {
                            mInstallmentModelArrayList.get(i).setEnable("0");
                        }
                        installEnable++;

                    }

                }
                //installment size 1
                if (mListViewArray.get(AppController.Position).getInstallmentArrayList().size()==1)
                {
                    if (mListViewArray.get(AppController.Position).getPayment_status().equalsIgnoreCase("1"))
                    {
                        mEmiRecycler.setVisibility(View.VISIBLE);
                        printLinear.setVisibility(View.GONE);
                        adapter = new PaymentDetailAdapter(mContext, mListViewArray, adapterSize,AppController.Position, new ClickListener() {
                            @Override
                            public void onPositionClicked(int position,Boolean isFullPayment,Boolean isInstallment,String installment_amountpay,String amountPay,String payment_optionpay,String installment_idpay,String installmentPay,String orderPaid) {
                                mainLinear.setVisibility(View.GONE);
                                printLinear.setVisibility(View.GONE);
                                paymentWeb.setVisibility(View.VISIBLE);
                                amount=amountPay;
                                backStatus="1";
                                payment_date = AppUtils.getCurrentDateToday() ;
                                installment_amount = installment_amountpay;
                                installment = installmentPay;
                                installment_id = installment_idpay;
                                payment_option = payment_optionpay;
                                category_id =AppController.categoryId ;
                                descriptionWeb=mListViewArray.get(AppController.Position).getDescription();

                                if(isFullPayment) {
                                    orderId="NASDUBAI" + mListViewArray.get(AppController.Position).getId()+ "S" + stud_id;
                                }
                                else
                                {
                                    orderId="NASDUBAI" + mListViewArray.get(AppController.Position).getId()+ "S" + AppController.stud_id+"I"+installment_id;
                                }
                                postBody = "{\n" +
                                        "\"apiOperation\":\"CREATE_CHECKOUT_SESSION\",\n" +
                                        "\"interaction\":{\n" +
                                        "\"operation\":\"PURCHASE\"\n" +
                                        "},\n" +
                                        "\"order\":{\n" +
                                        "\"amount\":\"" + amount + "\",\n" +
                                        "\"currency\":\"AED\",\n" +
                                        "\"id\":\"" + orderId + "\"\n" +
                                        "}\n" +
                                        "}";
                                try {
                                    postRequest(postUrl, postBody, merchantpassword,orderId);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onLongClicked(int position) {

                            }
                        });
                        mEmiRecycler.setAdapter(adapter);
                        System.out.println("working paid on 1");
                        closingTxt.setText("Paid on " + AppUtils.dateConversionddmmyyyy(mListViewArray.get(AppController.Position).getLast_paid_date()));
                        for (int i=0;i<mInstallmentModelArrayList.size();i++)
                        {
                            orderIdPrint=mInstallmentModelArrayList.get(i).getOrder_id();
                            invoiceNote=mInstallmentModelArrayList.get(i).getInvoiceNote();
                            amountFull=mInstallmentModelArrayList.get(i).getPaid_amount();
                            paidBy=mInstallmentModelArrayList.get(i).getPaid_by();
                            paidDate=mInstallmentModelArrayList.get(i).getPaid_date();
                            billing_code=mListViewArray.get(AppController.Position).getBillingCode();
                            titlePrint=mListViewArray.get(AppController.Position).getTitle();
                        }
                        isInvoiceInstall=false;
                    }
                }
                //installment size not 1
                else {
                        // total payment not done

                    if (mListViewArray.get(AppController.Position).getPayment_status().equalsIgnoreCase("0")) {
                        // total payment date not over
                        if (mListViewArray.get(AppController.Position).getPayment_date_status().equalsIgnoreCase("0"))
                        {
                            for (int j = 0; j < mListViewArray.get(AppController.Position).getInstallmentArrayList().size(); j++) {
                                if (mListViewArray.get(AppController.Position).getInstallmentArrayList().get(j).getPaid_status().equalsIgnoreCase("1")) {
                                    if (mListViewArray.get(AppController.Position).getInstallmentArrayList().get(j).getInst_date_status().equalsIgnoreCase("0")) {
                                        printLinear.setVisibility(View.VISIBLE);
                                        emailLinear.setVisibility(View.GONE);
                                        mEmiRecycler.setVisibility(View.VISIBLE);
                                    } else {
                                        printLinear.setVisibility(View.VISIBLE);
                                        emailLinear.setVisibility(View.VISIBLE);
                                        closingTxt.setText("Partially Paid");
                                        mEmiRecycler.setVisibility(View.VISIBLE);

                                    }

                                }
                            }
                        }

                        //partiallyPaid
                        else {
                            if (paidStatusInstall.equalsIgnoreCase("1"))
                            {
                                printLinear.setVisibility(View.VISIBLE);
                                emailLinear.setVisibility(View.VISIBLE);
                                closingTxt.setText("Partialy Paid");
                                mEmiRecycler.setVisibility(View.VISIBLE);


                            }
                            mEmiRecycler.setVisibility(View.GONE);
                            printLinear.setVisibility(View.GONE);
                            closingTxt.setText("Closed on " + AppUtils.dateConversionddmmyyyy(mListViewArray.get(AppController.Position).getClosing_date()));

                        }

                    } else
                        {
                        mEmiRecycler.setVisibility(View.GONE);
                        printLinear.setVisibility(View.VISIBLE);
                        emailLinear.setVisibility(View.GONE);
                        System.out.println("working paid on 2");
                        closingTxt.setText("Paid on " + AppUtils.dateConversionddmmyyyy(mListViewArray.get(AppController.Position).getLast_paid_date()));

                    }
                }

            }

        if (mListViewArray.get(AppController.Position).getInstallment().equalsIgnoreCase("1"))
        {
            System.out.println("emi available");
            mListViewArray.get(AppController.Position).setEmiAvailable(true);
            if (mListViewArray.get(AppController.Position).getInstallmentArrayList().size()==1)
            {
                if (mListViewArray.get(AppController.Position).getPayment_status().equalsIgnoreCase("1"))
                {
                    mEmiRecycler.setVisibility(View.VISIBLE);
                    adapter = new PaymentDetailAdapter(mContext, mListViewArray, adapterSize,AppController.Position, new ClickListener() {
                        @Override
                        public void onPositionClicked(int position,Boolean isFullPayment,Boolean isInstallment,String installment_amountpay,String amountPay,String payment_optionpay,String installment_idpay,String installmentPay,String orderPaid) {
                            mainLinear.setVisibility(View.GONE);
                            paymentWeb.setVisibility(View.VISIBLE);
                            amount=amountPay;
                            payment_date = AppUtils.getCurrentDateToday() ;
                            installment_amount = installment_amountpay;
                            installment = installmentPay;
                            installment_id = installment_idpay;
                            payment_option = payment_optionpay;
                            category_id =AppController.categoryId ;
                            descriptionWeb=mListViewArray.get(AppController.Position).getDescription();

                            if(isFullPayment) {
                                orderId="NASDUBAI" + mListViewArray.get(AppController.Position).getId()+ "S" + stud_id;
                            }
                            else
                            {
                                orderId="NASDUBAI" + mListViewArray.get(AppController.Position).getId()+ "S" + AppController.stud_id+"I"+installment_id;
                            }
                            postBody = "{\n" +
                                    "\"apiOperation\":\"CREATE_CHECKOUT_SESSION\",\n" +
                                    "\"interaction\":{\n" +
                                    "\"operation\":\"PURCHASE\"\n" +
                                    "},\n" +
                                    "\"order\":{\n" +
                                    "\"amount\":\"" + amount + "\",\n" +
                                    "\"currency\":\"AED\",\n" +
                                    "\"id\":\"" + orderId + "\"\n" +
                                    "}\n" +
                                    "}";
                            try {
                                postRequest(postUrl, postBody, merchantpassword,orderId);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onLongClicked(int position) {

                        }
                    });
                    mEmiRecycler.setAdapter(adapter);
                    for (int i=0;i<mInstallmentModelArrayList.size();i++)
                    {
                        orderIdPrint=mInstallmentModelArrayList.get(i).getOrder_id();
                        invoiceNote=mInstallmentModelArrayList.get(i).getInvoiceNote();
                        amountFull=mInstallmentModelArrayList.get(i).getPaid_amount();
                        paidBy=mInstallmentModelArrayList.get(i).getPaid_by();
                        paidDate=mInstallmentModelArrayList.get(i).getPaid_date();
                        billing_code=mListViewArray.get(AppController.Position).getBillingCode();
                        titlePrint=mListViewArray.get(AppController.Position).getTitle();
                    }
                    isInvoiceInstall=false;
                }

            }
            else {
                isInvoiceInstall=true;
            }
        }
        else
        {
            System.out.println("emi not available");
            if (mListViewArray.get(AppController.Position).getFullpaymentArrayList().size()>0)
            {
                for (int i=0;i<mListViewArray.get(AppController.Position).getFullpaymentArrayList().size();i++) {
                    orderIdPrint =mListViewArray.get(AppController.Position).getFullpaymentArrayList().get(i).getOrder_id();
                    invoiceNote = mListViewArray.get(AppController.Position).getFullpaymentArrayList().get(i).getInvoiceNote();
                    amountFull =mListViewArray.get(AppController.Position).getFullpaymentArrayList().get(i).getPaid_amount();
                    paidBy =mListViewArray.get(AppController.Position).getFullpaymentArrayList().get(i).getPaid_by();
                    paidDate=mListViewArray.get(AppController.Position).getFullpaymentArrayList().get(i).getPaid_date();
                    billing_code = mListViewArray.get(AppController.Position).getBillingCode();
                    titlePrint=mListViewArray.get(AppController.Position).getTitle();
                    isInvoiceInstall = false;

                }
            }
            else {
              /*  orderIdPrint=mListViewArray.get(Position).getOrder_id();
                invoiceNote=mListViewArray.get(Position).getInvoiceNote();
                amountFull=mListViewArray.get(Position).getAmount();
                paidBy=mListViewArray.get(Position).getPaidby();
                paidDate=mListViewArray.get(Position).getLast_paid_date();
                billing_code=mListViewArray.get(Position).getBillingCode();*/
                isInvoiceInstall=false;
            }
        }
        if (mListViewArray.get(AppController.Position).getPayment_status().equalsIgnoreCase("0"))
        {
            mListViewArray.get(AppController.Position).setIsfullPaid(false);
        }
        else {

            mListViewArray.get(AppController.Position).setIsfullPaid(true);

        }
        if (mListViewArray.get(AppController.Position).getTitle().equalsIgnoreCase(""))
        {
            descriptionTitle.setVisibility(View.GONE);
        }
        else {
            descriptionTitle.setVisibility(View.VISIBLE);
            descriptionTitle.setText(mListViewArray.get(AppController.Position).getTitle());
        }
        if (adapterSize>1)
        {
            installmentmainTxt.setVisibility(View.VISIBLE);
        }
        else {
            installmentmainTxt.setVisibility(View.GONE);
        }
        //description

        if (mListViewArray.get(AppController.Position).getDescription().equalsIgnoreCase(""))
        {
            descriptionTxt.setVisibility(View.GONE);
        }
        else {
            descriptionTxt.setVisibility(View.VISIBLE);
            descriptionTxt.setText(mListViewArray.get(AppController.Position).getDescription());

        }

        //Installment Available

        if (mListViewArray.get(AppController.Position).isEmiAvailable())
        {

            if (mListViewArray.get(AppController.Position).getPayment_date_status().equalsIgnoreCase("0"))
            {
                if (paidStatusInstall.equalsIgnoreCase("1"))
                {
                    System.out.println("working 1!");

                }
                else {
                    //System.out.println("working 2!");
                  //  printLinear.setVisibility(View.GONE);
                }
                if (mListViewArray.get(AppController.Position).getPayment_status().equalsIgnoreCase("0"))
                {
                    closingTxt.setText("Closing on "+AppUtils.dateConversionddmmyyyy(mListViewArray.get(AppController.Position).getClosing_date()));

                }
                else {
                    System.out.println("working paid on 3");
                    printLinear.setVisibility(View.VISIBLE);
                    emailLinear.setVisibility(View.GONE);
                    closingTxt.setText("Paid on "+AppUtils.dateConversionddmmyyyy(mListViewArray.get(AppController.Position).getLast_paid_date()));

                }
                mEmiRecycler.setVisibility(View.VISIBLE);
                adapter = new PaymentDetailAdapter(mContext, mListViewArray, adapterSize,AppController.Position, new ClickListener() {
                    @Override
                    public void onPositionClicked(int position,Boolean isFullPayment,Boolean isInstallment,String installment_amountpay,String amountPay,String payment_optionpay,String installment_idpay,String installmentPay,String orderPaid) {
                        mainLinear.setVisibility(View.GONE);
                        paymentWeb.setVisibility(View.VISIBLE);
                        amount=amountPay;
                        payment_date = AppUtils.getCurrentDateToday() ;
                        installment_amount = installment_amountpay;
                        installment = installmentPay;
                        installment_id = installment_idpay;
                        payment_option = payment_optionpay;
                        category_id =AppController.categoryId ;
                        descriptionWeb=mListViewArray.get(AppController.Position).getDescription();

                        if(isFullPayment) {
                            orderId="NASDUBAI" + mListViewArray.get(AppController.Position).getId()+ "S" + stud_id;
                        }
                        else
                        {
                            orderId="NASDUBAI" + mListViewArray.get(AppController.Position).getId()+ "S" + AppController.stud_id+"I"+installment_id;
                        }
                        postBody = "{\n" +
                                "\"apiOperation\":\"CREATE_CHECKOUT_SESSION\",\n" +
                                "\"interaction\":{\n" +
                                "\"operation\":\"PURCHASE\"\n" +
                                "},\n" +
                                "\"order\":{\n" +
                                "\"amount\":\"" + amount + "\",\n" +
                                "\"currency\":\"AED\",\n" +
                                "\"id\":\"" + orderId + "\"\n" +
                                "}\n" +
                                "}";
                        try {
                            postRequest(postUrl, postBody, merchantpassword,orderId);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onLongClicked(int position) {

                    }
                });
                mEmiRecycler.setAdapter(adapter);
            }
            else {
                if (mListViewArray.get(AppController.Position).getPayment_status().equalsIgnoreCase("0"))
                {
                    if (paidStatusInstall.equalsIgnoreCase("1"))
                    {
                        System.out.println("working partial");
                        mEmiRecycler.setVisibility(View.VISIBLE);
                        closingTxt.setText("Partially Paid");
                        AppController.isaddCall=false;
                        printLinear.setVisibility(View.VISIBLE);
                        // emailLinear.setVisibility(View.VISIBLE);
                        adapter = new PaymentDetailAdapter(mContext, mListViewArray, adapterSize,AppController.Position, new ClickListener() {
                            @Override
                            public void onPositionClicked(int position,Boolean isFullPayment,Boolean isInstallment,String installment_amountpay,String amountPay,String payment_optionpay,String installment_idpay,String installmentPay,String orderPaid) {
                                mainLinear.setVisibility(View.GONE);
                                paymentWeb.setVisibility(View.VISIBLE);
                                amount=amountPay;
                                payment_date = AppUtils.getCurrentDateToday() ;
                                installment_amount = installment_amountpay;
                                installment = installmentPay;
                                installment_id = installment_idpay;
                                payment_option = payment_optionpay;
                                category_id =AppController.categoryId ;
                                descriptionWeb=mListViewArray.get(AppController.Position).getDescription();

                                if(isFullPayment) {
                                    orderId="NASDUBAI" + mListViewArray.get(AppController.Position).getId()+ "S" + AppController.stud_id;
                                }
                                else
                                {
                                    orderId="NASDUBAI" + mListViewArray.get(AppController.Position).getId()+ "S" + AppController.stud_id+"I"+installment_id;
                                }
                                postBody = "{\n" +
                                        "\"apiOperation\":\"CREATE_CHECKOUT_SESSION\",\n" +
                                        "\"interaction\":{\n" +
                                        "\"operation\":\"PURCHASE\"\n" +
                                        "},\n" +
                                        "\"order\":{\n" +
                                        "\"amount\":\"" + amount + "\",\n" +
                                        "\"currency\":\"AED\",\n" +
                                        "\"id\":\"" + orderId + "\"\n" +
                                        "}\n" +
                                        "}";
                                try {
                                    postRequest(postUrl, postBody, merchantpassword,orderId);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onLongClicked(int position) {

                            }
                        });
                        mEmiRecycler.setAdapter(adapter);
                    }
                    else
                    {
                        System.out.println("working partial else");
                        mEmiRecycler.setVisibility(View.GONE);
                        printLinear.setVisibility(View.GONE);
                        emailLinear.setVisibility(View.GONE);
                        installmentmainTxt.setVisibility(View.GONE);
                        closingTxt.setText("Closed on "+ AppUtils.dateConversionddmmyyyy(mListViewArray.get(AppController.Position).getClosing_date()));



                    }


                 /*   System.out.println("working partial else 111");
                    mEmiRecycler.setVisibility(View.GONE);
                    printLinear.setVisibility(View.VISIBLE);
                    closingTxt.setText("Closed on "+AppUtils.dateConversionddmmyyyy(mListViewArray.get(AppController.Position).getClosing_date()));*/
                }
                else if (mListViewArray.get(AppController.Position).getPayment_status().equalsIgnoreCase("1"))
                {
                    mEmiRecycler.setVisibility(View.VISIBLE);
                    printLinear.setVisibility(View.VISIBLE);
                    emailLinear.setVisibility(View.GONE);
                    System.out.println("working paid on 4");
                    closingTxt.setText("Paid on "+AppUtils.dateConversionddmmyyyy(mListViewArray.get(AppController.Position).getLast_paid_date()));
                }

            }

        }
        else
        {
            if (mListViewArray.get(AppController.Position).getFullpaymentArrayList().size()==0)
            {
                if (mListViewArray.get(AppController.Position).getPayment_date_status().equalsIgnoreCase("0"))
                {
                    mainLinear.setVisibility(View.VISIBLE);
                    paymentWeb.setVisibility(View.GONE);
                    closingTxt.setVisibility(View.VISIBLE);
                    printLinear.setVisibility(View.GONE);
                    mEmiRecycler.setVisibility(View.VISIBLE);
                    closingTxt.setText("Closing on "+AppUtils.dateConversionddmmyyyy(mListViewArray.get(AppController.Position).getClosing_date()));
                    adapter = new PaymentDetailAdapter(mContext, mListViewArray, adapterSize,AppController.Position, new ClickListener() {
                        @Override
                        public void onPositionClicked(int position,Boolean isFullPayment,Boolean isInstallment,String installment_amountpay,String amountPay,String payment_optionpay,String installment_idpay,String installmentPay,String orderPaid) {
                            System.out.println("clicked position"+position);
                            System.out.println("isFullPayment"+isFullPayment);
                            isShowPaymentView=true;
                            backStatus="1";
                            mainLinear.setVisibility(View.GONE);
                            paymentWeb.setVisibility(View.VISIBLE);
                            amount=amountPay;
                            payment_date = AppUtils.getCurrentDateToday() ;
                            installment_amount = installment_amountpay;
                            installment = installmentPay;
                            installment_id = installment_idpay;
                            payment_option = payment_optionpay;
                            category_id =AppController.categoryId ;

                            if(isFullPayment) {
                                orderId="NASDUBAI" + mListViewArray.get(AppController.Position).getId()+ "S" + AppController.stud_id;
                            }
                            else
                            {
                                orderId="NASDUBAI" + mListViewArray.get(AppController.Position).getId()+ "S" + AppController.stud_id+"I"+installment_id;
                            }
                            descriptionWeb=mListViewArray.get(AppController.Position).getDescription();
                            postBody = "{\n" +
                                    "\"apiOperation\":\"CREATE_CHECKOUT_SESSION\",\n" +
                                    "\"interaction\":{\n" +
                                    "\"operation\":\"PURCHASE\"\n" +
                                    "},\n" +
                                    "\"order\":{\n" +
                                    "\"amount\":\"" + amount + "\",\n" +
                                    "\"currency\":\"AED\",\n" +
                                    "\"id\":\"" + orderId + "\"\n" +
                                    "}\n" +
                                    "}";
                            try {
                                postRequest(postUrl, postBody, merchantpassword,orderId);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onLongClicked(int position) {

                        }
                    });
                    mEmiRecycler.setAdapter(adapter);
                    closingTxt.setText("Closing on "+ AppUtils.dateConversionddmmyyyy(mListViewArray.get(AppController.Position).getClosing_date()));

                }else
                {
                    printLinear.setVisibility(View.GONE);
                    mEmiRecycler.setVisibility(View.GONE);
                    closingTxt.setText("Closed on "+ AppUtils.dateConversionddmmyyyy(mListViewArray.get(AppController.Position).getClosing_date()));

                }
            }
            else {
                mainLinear.setVisibility(View.VISIBLE);
                paymentWeb.setVisibility(View.GONE);
                System.out.println("working ");
                closingTxt.setVisibility(View.VISIBLE);
                System.out.println("working paid on 5");
                closingTxt.setText("Paid on "+ AppUtils.dateConversionddmmyyyy(mListViewArray.get(AppController.Position).getLast_paid_date()));
                printLinear.setVisibility(View.VISIBLE);
                mEmiRecycler.setVisibility(View.VISIBLE);
                emailLinear.setVisibility(View.GONE);
                completed_date=mListViewArray.get(AppController.Position).getLast_paid_date();
                amount=mListViewArray.get(AppController.Position).getAmount();


                adapter = new PaymentDetailAdapter(mContext, mListViewArray, adapterSize,AppController.Position, new ClickListener() {
                    @Override
                    public void onPositionClicked(int position,Boolean isFullPayment,Boolean isInstallment,String installment_amountpay,String amountPay,String payment_optionpay,String installment_idpay,String installmentPay,String orderPaid) {
                        System.out.println("clicked position"+position);
                        if(isFullPayment) {
                            orderId="NASDUBAI" + mListViewArray.get(AppController.Position).getId()+ "S" + AppController.stud_id;
                        }
                        else
                        {
                            orderId="NASDUBAI" + mListViewArray.get(AppController.Position).getId()+ "S" + AppController.stud_id+"I"+installment_id;
                        }
                    }

                    @Override
                    public void onLongClicked(int position) {

                    }
                });
                mEmiRecycler.setAdapter(adapter);
                System.out.println("working paid on 6");
                closingTxt.setText("Paid on "+ AppUtils.dateConversionddmmyyyy(mListViewArray.get(AppController.Position).getFullpaymentArrayList().get(0).getPaid_date()));
            }
        }
        printLinearClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInvoiceInstall)
                {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                        paymentWeb.loadUrl("about:blank");
                        setWebViewSettingsPrint();
                        loadWebViewWithDataPrintInsatall();
                        createWebPrintJobInstall(paymentWeb);

                    } else {
                        Toast.makeText(mContext, "Print is not supported below Android KITKAT Version", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                        paymentWeb.loadUrl("about:blank");
                        setWebViewSettingsPrint();
                        loadWebViewWithDataPrint();
                        createWebPrintJob(paymentWeb);

                    } else {
                        Toast.makeText(mContext, "Print is not supported below Android KITKAT Version", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
       /* downloadLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/
        shareLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInvoiceInstall) {
                    isShareInstall=true;

                    if ((int) Build.VERSION.SDK_INT >= 23) {
                        TedPermission.with(mContext)
                                .setPermissionListener(permissionListenerStorage)
                                .setDeniedMessage("If you reject permission,you cannot use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                                .check();
                    } else {
                        sharePdfFileInstallPrint();
                    }
                }
                else {
                   isDownload=false;
                   isShareInstall=false;
                    if ((int) Build.VERSION.SDK_INT >= 23) {
                        TedPermission.with(mContext)
                                .setPermissionListener(permissionListenerStorage)
                                .setDeniedMessage("If you reject permission,you cannot use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                                .check();
                    } else {
                        sharePdfFilePrint();
                    }
                }
            }
        });
        downloadLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInvoiceInstall)
                {
                    isDownload=true;
                    isDoenloadInstall=true;
                    if ((int) Build.VERSION.SDK_INT >= 23) {
                        TedPermission.with(mContext)
                                .setPermissionListener(permissionListenerStorage)
                                .setDeniedMessage("If you reject permission,you cannot use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                                .check();
                    } else {
                        downloadFilePrintInstall();
                    }
                }
                else {
                    isDoenloadInstall=false;
                    isDownload=true;
                    if ((int) Build.VERSION.SDK_INT >= 23) {
                        TedPermission.with(mContext)
                                .setPermissionListener(permissionListenerStorage)
                                .setDeniedMessage("If you reject permission,you cannot use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                                .check();
                    } else {
                        downloadFilePrint();
                    }
                }

            }
        });
    }
    PermissionListener permissionListenerStorage = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
           if (isDownload)
            {
                if (isDoenloadInstall)
                {
                    downloadFilePrintInstall();
                }
                else {
                    downloadFilePrint();
                }

            }
           else {
               if (isShareInstall)
               {
                   sharePdfFileInstallPrint();
               }
               else {
                   sharePdfFilePrint();
               }

           }


        }

        @Override
        public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            Toast.makeText(mContext, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }


    };
    void sharePdfFilePrint() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            paymentWeb.loadUrl("about:blank");
            setWebViewSettingsPrint();
            loadWebViewWithDataPrint();
//                    createWebPrintJobShare(paymentWeb);
            pathFile= new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/" + "NAS_DUBAI_TRIP/Payments" + "_" + "NASDUBAI" + "/");
            System.out.println("Path file 5"+pathFile);

            pathFile.mkdirs();
//            if(!pathFile.exists())
//                pathFile.mkdirs();
            if (Build.VERSION.SDK_INT >= 23) {

                pdfUri = FileProvider.getUriForFile(mContext, getPackageName() + ".provider", createWebPrintJobShare(paymentWeb,pathFile));
            } else {
                System.out.println("Path file 6"+pathFile);
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
            intent.putExtra("orderId", "NASDUBAI");
            intent.putExtra("pdfUri", pdfUri.toString());
            startActivity(intent);
            paymentWeb.setVisibility(View.GONE);

        } else {
//            Toast.makeText(mContext, "Print is not supported below Android KITKAT Version", Toast.LENGTH_SHORT).show();
        }
    }
    void sharePdfFileInstallPrint() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            paymentWeb.loadUrl("about:blank");
            setWebViewSettingsPrint();
            loadWebViewWithDataPrintInsatall();
//                    createWebPrintJobShare(paymentWeb);
            pathFile= new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath() +"/" + "NAS_DUBAI_TRIP/Payments" + "_" + "NASDUBAI" + "/");
            System.out.println("Path file 7"+pathFile);
            pathFile.mkdirs();
//            if(!pathFile.exists())
//                pathFile.mkdirs();
            if (Build.VERSION.SDK_INT >= 23) {

                pdfUri = FileProvider.getUriForFile(mContext, getPackageName() + ".provider", createWebPrintJobShare(paymentWeb,pathFile));
            } else {
                System.out.println("Path file 8"+pathFile);
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
            intent.putExtra("orderId", "NASDUBAI");
            intent.putExtra("pdfUri", pdfUri.toString());
            startActivity(intent);
            paymentWeb.setVisibility(View.GONE);

        } else {
//            Toast.makeText(mContext, "Print is not supported below Android KITKAT Version", Toast.LENGTH_SHORT).show();
        }
    }
    void downloadFilePrint() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            paymentWeb.loadUrl("about:blank");
            setWebViewSettingsPrint();
            loadWebViewWithDataPrint();
//                    createWebPrintJobShare(paymentWeb);
            pathFile= new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath() +"/" + "NAS_DUBAI_TRIP/Payments" + "_" + "NASDUBAI" + "/");
            System.out.println("Path file 9"+pathFile);
            pathFile.mkdirs();
//            if(!pathFile.exists())
//                pathFile.mkdirs();
            if (Build.VERSION.SDK_INT >= 23) {

                pdfUri = FileProvider.getUriForFile(mContext, getPackageName() + ".provider", createWebPrintJobShare(paymentWeb,pathFile));
            } else {
                pdfUri = Uri.fromFile(createWebPrintJobShare(paymentWeb,pathFile));
                System.out.println("Path file 8"+pathFile);
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
    void downloadFilePrintInstall() {
        isDoenloadInstall=false;
        isDownload=false;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            paymentWeb.loadUrl("about:blank");
            setWebViewSettingsPrint();
            loadWebViewWithDataPrintInsatall();
//                    createWebPrintJobShare(paymentWeb);
            pathFile= new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/" + "NAS_DUBAI_TRIP/Payments" + "_" + "NASDUBAI" + "/");
            System.out.println("Path file 9"+pathFile);
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
    private void createWebPrintJobInstall(WebView webView) {
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
    private File createWebPrintJobShare(WebView webView, File path) {
        String jobName = "Receipt_Share_" + "NASDUBAI"+ ".pdf";
        mProgressRelLayout.clearAnimation();
        mProgressRelLayout.setVisibility(View.GONE);
        paymentWeb.setVisibility(View.VISIBLE);
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
    void postRequest(final String postUrl, final String postBody, String merchantPassword, final String orderRequest) throws IOException {

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON_MEDIA_TYPE, postBody);
        System.out.println("Postbody"+postBody);
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
                                    // closedImg.setVisibility(View.GONE);
                                    if (sessionId.equalsIgnoreCase("")) {
                                        try {
                                            postRequest(postUrl, postBody, merchantpassword,orderRequest);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    } else {

                                        setWebViewSettings();
                                        loadWebViewWithData(title, descriptionWeb,orderRequest);
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
        paymentWeb.setWebViewClient(new PaymentDetailActivityInstall.MyPrintWebViewClient());
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
            fullHtml = fullHtml.replace("###amount###", amountFull);
            fullHtml = fullHtml.replace("###order_Id###", orderIdPrint);
            fullHtml = fullHtml.replace("###ParentName###",paidBy );
            fullHtml = fullHtml.replace("###Date###", AppUtils.dateParsingTodd_MMM_yyyy(paidDate));
            fullHtml = fullHtml.replace("###paidBy###", invoiceNote);
            fullHtml = fullHtml.replace("###billing_code###", billing_code);
            fullHtml = fullHtml.replace("###trn_no###", billing_code);
           // fullHtml = fullHtml.replace("###paidBy###", "Done");
            fullHtml = fullHtml.replace("###title###", titlePrint);

            backStatus = "2";

//            Log.d("LOG3","fullHtml = "+fullHtml);
//            paymentWeb.loadDataWithBaseURL("", fullHtml, "text/html", "UTF-8", "");
            paymentWeb.loadDataWithBaseURL("file:///android_asset/images/", fullHtml, "text/html; charset=utf-8", "utf-8", "about:blank");


        }

    }
    void loadWebViewWithDataPrintInsatall() {
        fullHtml="";
        installFull="";
        installFullHtml="";
        StringBuffer sb = new StringBuffer();
        String eachLine = "";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(getAssets().open("InstallmentInvoice.html")));
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
        for (int i=0;i<mInstallmentModelArrayList.size();i++)
        {
            if (mInstallmentModelArrayList.get(i).getPaid_status().equalsIgnoreCase("1"))
            {
                orderIdPrint=mInstallmentModelArrayList.get(i).getOrder_id();
                titlePrint=mListViewArray.get(AppController.Position).getTitle();
                paidDate=mInstallmentModelArrayList.get(i).getPaid_date();
                amountFull=mInstallmentModelArrayList.get(i).getPaid_amount();
                instalTitle="Installment "+(i+1);
                installFullHtml=
                        " <tr>" +
                        "<td height=\"35\"></td>" +
                        "</tr>" +
                        "<tr>" +
                        "<td align=\"center\">" +
                        "<table width=\"100%\" class=\"table-inner\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">" +
                        "<tr>" +
                        "<td width='100' align='center' valign=\"top\" style=\"font-family: 'Open Sans', Arial, sans-serif; font-size:14px; color:#3b3b3b; line-height:26px;  font-weight: bold;\">###title###</td>" +
                        "<td width=\"87\" align=\"center\" valign=\"top\" style=\"font-family: 'Open Sans', Arial, sans-serif; font-size:14px; color:#3b3b3b; line-height:26px;  font-weight: bold;\">###orderid###</td>" +
                        "<td width=\"87\" align=\"center\" valign=\"top\" style=\"font-family: 'Open Sans', Arial, sans-serif; font-size:14px; color:#3b3b3b; line-height:26px;  font-weight: bold;\"> ###date###</td>" +
                        "<td width=\"87\" align=\"center\" valign=\"top\" style=\"font-family: 'Open Sans', Arial, sans-serif; font-size:14px; color:#3b3b3b; line-height:26px;  font-weight: bold;\">###amount###</td>" +
                        "</tr>" +
                        "</table>" +
                        "</td>" +
                        "</tr>" +
                        "<tr>" +
                        "<td height=\"5\" style=\"border-bottom:1px solid #ecf0f1;\"></td>"+"</tr>"+"<tr>"+"<td height=\"5\"></td>"+"</tr>";

                if (installFullHtml.length()>0)
                {
                    installFullHtml=installFullHtml.replace("###amount###",amountFull);
                    installFullHtml=installFullHtml.replace("###orderid###",orderIdPrint);
                    installFullHtml=installFullHtml.replace("###date###",AppUtils.dateParsingTodd_MMM_yyyy(paidDate));
                    installFullHtml=installFullHtml.replace("###title###",instalTitle);

                }
                installFull=installFull+installFullHtml;

            }



        }

        if (fullHtml.length() > 0) {
            totalAmtInst=String.valueOf(Integer.parseInt(mListViewArray.get(AppController.Position).getAmount())-Integer.parseInt(mListViewArray.get(AppController.Position).getRemaining_amount()));
            fullHtml = fullHtml.replace("###ParentName###", mListViewArray.get(AppController.Position).getParentName());
            fullHtml = fullHtml.replace("###totalamount###", totalAmtInst);
            fullHtml = fullHtml.replace("###TitleMain###", mListViewArray.get(AppController.Position).getTitle());
            fullHtml = fullHtml.replace("###INSTTALMENT###",installFull);
            fullHtml = fullHtml.replace("###billingCode###",mListViewArray.get(AppController.Position).getBillingCode());
            fullHtml = fullHtml.replace("###trn_no###",PreferenceManager.getTrnNo(mContext));
            backStatus = "2";
            paymentWeb.loadDataWithBaseURL("file:///android_asset/images/", fullHtml, "text/html; charset=utf-8", "utf-8", "about:blank");


        }

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
        paymentWeb.setWebViewClient(new PaymentDetailActivityInstall.MyWebViewClient());
        paymentWeb.setWebChromeClient(new PaymentDetailActivityInstall.MyWebChromeClient());
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
    private void goToNextView() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
//                startService(new Intent(getBaseContext(), OnClearFromRecentService.class));
                saveTask();

            }
        }, 10000);
    }
    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            //Calling a javascript function in html page
//            view.loadUrl("javascript:alert(showVersion('called by Android'))");
            mProgressRelLayout.clearAnimation();
            mProgressRelLayout.setVisibility(View.GONE);
            view.loadUrl("javascript:Checkout.showLightbox()");
          //  Log.d("WebView", "your current url when webpage finished." + url);
            if (url.startsWith("https://network.gateway.mastercard.com/checkout/receipt/")) {
                goToNextView();
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            backStatus="1";
           // Log.d("WebView", "your current url when webpage loading.." + url);
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

         //   Log.d("LogTag", message);
            result.confirm();
            return true;
        }

        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            if (consoleMessage.message().contains("completeCallback")) {
                //System.out.println("completeCallback");
                saveTask();

            } else if (consoleMessage.message().contains("cancelCallback")) {
                System.out.println("cancelCallback");
                if (isFullPayPage)
                {
                    mainLinear.setVisibility(View.VISIBLE);
                    paymentWeb.setVisibility(View.GONE);
                    closingTxt.setVisibility(View.VISIBLE);
                    printLinear.setVisibility(View.GONE);
                    mEmiRecycler.setVisibility(View.VISIBLE);
                    adapter = new PaymentDetailAdapter(mContext, mListViewArray, adapterSize,AppController.Position, new ClickListener() {
                        @Override
                        public void onPositionClicked(int position,Boolean isFullPayment,Boolean isInstallment,String installment_amountpay,String amountPay,String payment_optionpay,String installment_idpay,String installmentPay,String isPaid) {
                          //  System.out.println("clicked position"+position);
                            //System.out.println("isFullPayment"+isFullPayment);
                            isShowPaymentView=true;
                            backStatus="1";
                            mainLinear.setVisibility(View.GONE);
                            paymentWeb.setVisibility(View.VISIBLE);
                            amount=amountPay;
                            payment_date = AppUtils.getCurrentDateToday() ;
                            installment_amount = installment_amountpay;
                            installment = installmentPay;
                            installment_id = installment_idpay;
                            payment_option = payment_optionpay;
                            category_id =AppController.categoryId ;
                            descriptionWeb=mListViewArray.get(AppController.Position).getDescription();

                        }

                        @Override
                        public void onLongClicked(int position) {

                        }
                    });
                    mEmiRecycler.setAdapter(adapter);
                    closingTxt.setText("Closing on "+ AppUtils.dateConversionddmmyyyy(mListViewArray.get(AppController.Position).getClosing_date()));
                }
            } else if (consoleMessage.message().contains("errorCallback")) {
                System.out.println("errorCallback");
            }


            return super.onConsoleMessage(consoleMessage);
        }
    }
    private void saveTask() {
        final String tTripId = trip_id;
        final String tStatus = "0";
        final String tStudentId = AppController.stud_id;
        final String tPaymentDate = payment_date;
        final String tInstallmentAmount = installment_amount;
        final String tAmount = amount;
        final String tPaymentOption= payment_option;
        final String tInstallmentId= installment_id;
        final String tCategoryId= AppController.categoryId;
        final String tInstallment= installment;

        class SaveTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                //creating a task
                Trip task = new Trip();
                task.setTrip_id(tTripId);
                task.setStatus(tStatus);
                task.setStudent_id(tStudentId);
                task.setPayment_date(tPaymentDate);
                task.setAmount(tAmount);
                task.setInstallment_amount(tInstallmentAmount);
                task.setPayment_option(tPaymentOption);
                task.setInstallment_id(tInstallmentId);
                task.setCategory_id(tCategoryId);
                task.setInstallment(tInstallment);
                task.setOrder_id(orderId);
                task.setUsers_id(PreferenceManager.getUserId(mContext));
                task.setPayment_detail_id(AppController.categoryId);

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
    public void tripSubmiotApi(final String URLHEAD, final String tripId, final String studentId) {

        PaymentDetailActivityInstall.TripSubmit mTripSubmit = new PaymentDetailActivityInstall.TripSubmit(URLHEAD, tripId, studentId);
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
            Log.v("gson to json:", " " +  gson.toJson(mTripList));
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
                            paymentWeb.setVisibility(View.GONE);
//                            Trip tripWithId = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
//                                    .tripDao().getTripWithId(tripId, studentId, PreferenceManager.getUserId(mContext));
//                            DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
//                                    .tripDao().delete(tripWithId);
                            /*"data":[{"orderId":"NAS_DUBAI_2S_3189","status":"success","details":null}]}}*/

                            JSONArray mJsonDataArray = secobj.optJSONArray("data");

                            PaymentDetailActivityInstall.DeleteQueryAsyncTask mDeleteQueryAsyncTask = new PaymentDetailActivityInstall.DeleteQueryAsyncTask(tripId, studentId, mJsonDataArray);
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
                orderId=mOrderData.optString("orderId");
                if (mOrderData.optString("status").equalsIgnoreCase("success")) {
                    if (mOrderData.optString("orderId").equalsIgnoreCase(orderId))
                    {

                        JSONObject mDetail=mOrderData.optJSONObject("details");





                        //completed_dated=mDetail.optString("completed_dated");
                        // invoiceNote=mDetail.optString("invoiceNote");
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
            if (AppUtils.isNetworkConnected(mContext)) {
                getReportListAPI(URL_TRIP_LIST);
            } else {
                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

            }
            //  closingTxt.setText("Paid on " + AppUtils.dateConversionddmmyyyy(completed_dated));

        }

    }

    @Override
    protected void onResume() {
        super.onResume();

    }
    private void callStatusChangeApi(final String URL, final String id) {
        VolleyWrapper volleyWrapper = new VolleyWrapper(URL);
        String[] name = {"access_token", "users_id", "id", "type"};
        String[] value = {PreferenceManager.getAccessToken(mContext), PreferenceManager.getUserId(mContext), mListViewArray.get(AppController.Position).getId(), "payment"};
        System.out.println("Passing values"+PreferenceManager.getAccessToken(mContext)+ PreferenceManager.getUserId(mContext)+ id+"payment");
        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("status success"+successResponse);
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
    private void getReportListAPI(final String URLHEAD) {
        System.out.println("working api");
      //  mListViewArray = new ArrayList<>();
        mListViewArray.clear();
        mInstallmentModelArrayList.clear();
        mFullPaymentListModelArray.clear();
        VolleyWrapper volleyWrapper = new VolleyWrapper(URLHEAD);
        String[] name = {"access_token", "student_id", "users_id", "category_id"};
        String[] value = {PreferenceManager.getAccessToken(mContext), AppController.stud_id, PreferenceManager.getUserId(mContext), AppController.categoryId};
        System.out.println("Sending"+PreferenceManager.getAccessToken(mContext)+ AppController.stud_id+ PreferenceManager.getUserId(mContext)+ AppController.categoryId);
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
                            JSONArray dataArray = secobj.getJSONArray(JTAG_RESPONSE_DATA_ARRAY);
                            if (dataArray.length() > 0) {
                                for (int i = 0; i < dataArray.length(); i++) {
                                    JSONObject dataObject = dataArray.getJSONObject(i);
                                    //installPos=i;
                                    mListViewArray.add(getSearchValues(dataObject));
                                }
                                // mListViewArray=mListViewArraySuccess;
                               if (mListViewArray.get(AppController.Position).getInstallment().equalsIgnoreCase("1"))
                               {
                                  if (mListViewArray.get(AppController.Position).getInstallmentArrayList().size()==1)
                                  {
                                      adapterSize=1;
                                  }
                                  else {
                                      adapterSize=mListViewArray.get(AppController.Position).getInstallmentArrayList().size()+1;
                                  }
                               }
                               else {
                                   adapterSize=1;

                               }
                               paymentWeb.setVisibility(View.GONE);
                                mainLinear.setVisibility(View.VISIBLE);
                                reload();

                            } else {

                                Toast.makeText(mContext, "No Trips available", Toast.LENGTH_SHORT).show();
                            }



                        }
                    } else if (response_code.equalsIgnoreCase("500")) {

                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        getReportListAPI(URLHEAD);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        getReportListAPI(URLHEAD);

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        getReportListAPI(URLHEAD);

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
    private TripListModel getSearchValues(JSONObject Object)
            throws JSONException {
        TripListModel mTripListModel = new TripListModel();
        mTripListModel.setId(Object.getString(JTAG_ID));
        mTripListModel.setTitle(Object.getString("title"));
        mTripListModel.setInstallment(Object.getString("installment"));
        mTripListModel.setPayment_option(Object.getString("payment_option"));
        mTripListModel.setRemaining_amount(Object.getString("remaining_amount"));
        mTripListModel.setDescription(Object.getString("description"));
        mTripListModel.setTrip_date(Object.getString("trip_date"));
        mTripListModel.setAmount(Object.getString("amount"));
        mTripListModel.setTrip_date_staus(Object.getString("trip_date_staus"));
        mTripListModel.setStatus(Object.getString("status"));
        mTripListModel.setStudentName(Object.getString("studentName"));
        mTripListModel.setParentName(Object.getString("parentName"));
        mTripListModel.setPayment_status(Object.getString("payment_status"));
        mTripListModel.setClosing_date(Object.getString("closing_date"));
        mTripListModel.setLast_paid_date(Object.getString("last_paid_date"));
        mTripListModel.setPayment_date_status(Object.getString("payment_date_status"));
        mTripListModel.setBillingCode(Object.getString("isams_no"));
        JSONArray installmentArray = Object.getJSONArray(JTAG_RESPONSE_INSTALLMENT_ARRAY);
        if (installmentArray.length() > 0) {
            for (int j = 0; j < installmentArray.length(); j++) {
                JSONObject Objectsec = installmentArray.getJSONObject(j);
                InstallmentListModel nTripListModel = new InstallmentListModel();
                nTripListModel.setInstallment_id(Objectsec.getString("installment_id"));
                nTripListModel.setInvoiceNote(Objectsec.getString("invoice_note"));
                nTripListModel.setPaid_amount(Objectsec.getString("paid_amount"));
                nTripListModel.setInst_amount(Objectsec.getString("inst_amount"));
                nTripListModel.setPaid_status(Objectsec.getString("paid_status"));
                nTripListModel.setLast_payment_date(Objectsec.getString("last_payment_date"));
                nTripListModel.setPaid_by(Objectsec.getString("paid_by"));
                nTripListModel.setPayment_type(Objectsec.getString("payment_type"));
                nTripListModel.setPaid_date(Objectsec.getString("paid_date"));
                nTripListModel.setOrder_id(Objectsec.getString("order_id"));
                nTripListModel.setInst_date_status(Objectsec.getString("inst_date_status"));
                mInstallmentModelArrayList.add(nTripListModel);
                Log.e("ggggggg", String.valueOf(mInstallmentModelArrayList));
            }

        }

        JSONArray fullpaymentArray = Object.getJSONArray(JTAG_RESPONSE_FULLPAYMENT_ARRAY);
        mFullPaymentListModelArray = new ArrayList<>();
        if (fullpaymentArray.length() > 0) {
            for (int k = 0; k < fullpaymentArray.length(); k++) {
                JSONObject Objects = fullpaymentArray.getJSONObject(k);
                FullPaymentListModel oTripListModel = new FullPaymentListModel();
                oTripListModel.setInvoiceNote(Objects.getString("invoice_note"));
                oTripListModel.setLast_payment_date(Objects.getString("last_payment_date"));
                oTripListModel.setPaid_amount(Objects.getString("paid_amount"));
                oTripListModel.setPaid_by(Objects.getString("paid_by"));
                oTripListModel.setPayment_type(Objects.getString("payment_type"));
                oTripListModel.setPaid_date(Objects.getString("paid_date"));
                oTripListModel.setOrder_id(Objects.getString("order_id"));

                mFullPaymentListModelArray.add(oTripListModel);
            }

        }


        mTripListModel.setInstallmentArrayList(mInstallmentModelArrayList);
        mTripListModel.setFullpaymentArrayList(mFullPaymentListModelArray);

        return mTripListModel;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.out.println("back status"+backStatus);
        if (backStatus.equalsIgnoreCase("0")) {
            finish();

        }else {
            backStatus="0";
            mainLinear.setVisibility(View.VISIBLE);
            paymentWeb.setVisibility(View.GONE);
            reload();
        }
    }

    public  void createPDF(String html) {
        try {
            String k = "<html><body> This is my Project </body></html>";

           /* pathFile= new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/" + "NAS_DUBAI_TRIP/Payments" + "_" + "NASDUBAI" + "/");

            pathFile.mkdirs();*/
            OutputStream file = new FileOutputStream(new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/" + "NAS_DUBAI_TRIP/Payments" + "_" + "NASDUBAI" + "/Test.pdf"));
            Document document = new Document();
            PdfWriter.getInstance(document, file);
            document.open();
            HTMLWorker htmlWorker = new HTMLWorker(document);
            htmlWorker.parse(new StringReader(k));
            document.close();
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
