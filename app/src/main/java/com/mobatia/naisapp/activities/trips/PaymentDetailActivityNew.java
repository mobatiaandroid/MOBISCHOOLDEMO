package com.mobatia.naisapp.activities.trips;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.print.PrintJob;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.animation.RotateAnimation;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.activities.trips.adapter.PaymentDetailAdapter;
import com.mobatia.naisapp.activities.trips.model.FullPaymentListModel;
import com.mobatia.naisapp.activities.trips.model.InstallmentListModel;
import com.mobatia.naisapp.appcontroller.AppController;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.trips.TripListModel;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.HeaderManager;
import com.mobatia.naisapp.recyclerviewmanager.DividerItemDecorationNew;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;

public class PaymentDetailActivityNew extends Activity implements URLConstants, JSONConstants {
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
               // callStatusChangeApi(URL_GET_STATUS_CHANGE_API, trip_id);
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
        /*if (mListViewArray.get(AppController.Position).getInstallment().equalsIgnoreCase("1")) {
            mListViewArray.get(AppController.Position).setEmiAvailable(true);
        } else {
            mListViewArray.get(AppController.Position).setEmiAvailable(false);

        }
        if (mListViewArray.get(AppController.Position).getTitle().equalsIgnoreCase("")) {
            descriptionTitle.setVisibility(View.GONE);
        } else {
            descriptionTitle.setVisibility(View.VISIBLE);
            descriptionTitle.setText(mListViewArray.get(AppController.Position).getTitle());
        }

        if (mListViewArray.get(AppController.Position).getDescription().equalsIgnoreCase("")) {
            descriptionTxt.setVisibility(View.GONE);
        } else {
            descriptionTxt.setVisibility(View.VISIBLE);
            descriptionTxt.setText(mListViewArray.get(AppController.Position).getDescription());

        }
        int installEnable = 0;
        if (mListViewArray.get(AppController.Position).isEmiAvailable())
        {
            // Emi Available
            for (int i = 0; i < mInstallmentModelArrayList.size(); i++) {
                if (mInstallmentModelArrayList.get(i).getPaid_status().equalsIgnoreCase("1")) {

                    mInstallmentModelArrayList.get(i).setEnable("0");
                    paidStatusInstall = "1";
                    mInstallmentModelArrayList.get(i).setPaid(true);
                } else {
                    if (installEnable == 0) {
                        mInstallmentModelArrayList.get(i).setEnable("-1");
                        enablePosition = i;
                        mInstallmentModelArrayList.get(i).setEnablePosition(i);
                    } else {
                        mInstallmentModelArrayList.get(i).setEnable("0");
                    }
                    installEnable++;

                }

            }
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
                                    "\"order\":{\n" +
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
            else
            {
                if (mListViewArray.get(AppController.Position).getPayment_date_status().equalsIgnoreCase("0"))
                {
                    if (mListViewArray.get(AppController.Position).getPayment_status().equalsIgnoreCase("0"))
                    {
                        for (int j=0;j<mInstallmentModelArrayList.size();j++)
                        {
                            if (mInstallmentModelArrayList.get(j).getPaid_status().equalsIgnoreCase("1"))
                            {
                                printLinear.setVisibility(View.VISIBLE);
                                emailLinear.setVisibility(View.GONE);
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
                                                "\"order\":{\n" +
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
                        }
                    }
                    else
                    {
                        printLinear.setVisibility(View.VISIBLE);
                        emailLinear.setVisibility(View.GONE);
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
                                        "\"order\":{\n" +
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
                        closingTxt.setText("Paid on " + AppUtils.dateConversionddmmyyyy(mListViewArray.get(AppController.Position).getLast_paid_date()));

                    }
                }
                else
                {
                    if (mListViewArray.get(AppController.Position).getPayment_date_status().equalsIgnoreCase("1"))
                    {
                        if (mListViewArray.get(AppController.Position).getPayment_status().equalsIgnoreCase("0"))
                        {
                            for (int j=0;j<mInstallmentModelArrayList.size();j++)
                            {
                                if (mInstallmentModelArrayList.get(j).getPaid_status().equalsIgnoreCase("1"))
                                {
                                    printLinear.setVisibility(View.VISIBLE);
                                    emailLinear.setVisibility(View.VISIBLE);
                                    mEmiRecycler.setVisibility(View.VISIBLE);
                                    closingTxt.setText("Partially Paid");
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
                                                    "\"order\":{\n" +
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
                            }
                        }
                        else
                        {
                            printLinear.setVisibility(View.VISIBLE);
                            emailLinear.setVisibility(View.GONE);
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
                                            "\"order\":{\n" +
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
                            closingTxt.setText("Paid on " + AppUtils.dateConversionddmmyyyy(mListViewArray.get(AppController.Position).getLast_paid_date()));
                        }
                    }
                }
            }

        }
        else
        {
            // full payment paid
            if (mListViewArray.get(AppController.Position).getFullpaymentArrayList().size()>0)
            {    mEmiRecycler.setVisibility(View.VISIBLE);
                 printLinear.setVisibility(View.VISIBLE);
                 emailLinear.setVisibility(View.GONE);
                closingTxt.setText("Paid on "+AppUtils.dateConversionddmmyyyy(mListViewArray.get(AppController.Position).getLast_paid_date()));
                for (int i=0;i<mListViewArray.get(AppController.Position).getFullpaymentArrayList().size();i++)
                {
                    orderIdPrint =mListViewArray.get(AppController.Position).getFullpaymentArrayList().get(i).getOrder_id();
                    invoiceNote = mListViewArray.get(AppController.Position).getFullpaymentArrayList().get(i).getInvoiceNote();
                    amountFull =mListViewArray.get(AppController.Position).getFullpaymentArrayList().get(i).getPaid_amount();
                    paidBy =mListViewArray.get(AppController.Position).getFullpaymentArrayList().get(i).getPaid_by();
                    paidDate=mListViewArray.get(AppController.Position).getFullpaymentArrayList().get(i).getPaid_date();
                    billing_code = mListViewArray.get(AppController.Position).getBillingCode();
                    titlePrint=mListViewArray.get(AppController.Position).getTitle();
                    isInvoiceInstall = false;

                }
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
                                "\"order\":{\n" +
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
            }
            else
            {
                if(mListViewArray.get(AppController.Position).getPayment_date_status().equalsIgnoreCase("0"))
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
                                    "\"order\":{\n" +
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
                    mEmiRecycler.setVisibility(View.GONE);
                    printLinear.setVisibility(View.GONE);
                    closingTxt.setText("Closed on "+ AppUtils.dateConversionddmmyyyy(mListViewArray.get(AppController.Position).getClosing_date()));

                }
            }
        }*/
    }
}