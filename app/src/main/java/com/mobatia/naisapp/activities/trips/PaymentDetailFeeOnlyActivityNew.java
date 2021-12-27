package com.mobatia.naisapp.activities.trips;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
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
import com.mobatia.naisapp.fragments.canteen.CanteenActviity;
import com.mobatia.naisapp.fragments.home.adapter.SurveyPagerAdapter;
import com.mobatia.naisapp.fragments.home.module.AnswerSubmitModel;
import com.mobatia.naisapp.fragments.home.module.SurveyAnswersModel;
import com.mobatia.naisapp.fragments.home.module.SurveyModel;
import com.mobatia.naisapp.fragments.home.module.SurveyQuestionsModel;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.HeaderManager;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import payment.sdk.android.PaymentClient;
import payment.sdk.android.cardpayment.CardPaymentData;
import payment.sdk.android.cardpayment.CardPaymentRequest;

public class PaymentDetailFeeOnlyActivityNew extends Activity implements URLConstants, JSONConstants, StatusConstants {
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
    String merchantOrderReference="";
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
    Activity mActivity = this;
    String PaymentToken = "",OrderRef = "",PayUrl = "", AuthUrl = "";
    int survey_satisfation_status=0;
    int currentPage = 0;
    int currentPageSurvey = 0;
    private int surveySize=0;
    int pos=-1;
    ArrayList<SurveyModel> surveyArrayList;
    ArrayList<SurveyQuestionsModel> surveyQuestionArrayList;
    ArrayList<SurveyAnswersModel> surveyAnswersArrayList;
    TextView text_content;
    TextView text_dialog;
    ArrayList<AnswerSubmitModel>mAnswerList;
    String surveyEmail = "";
    boolean isShown=false;

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
              finish();
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
                getPaymentToken();


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
            pathFile= new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath() +"/" + "NAS_DUBAI_TRIP/Payments" + "_" + "NASDUBAI" + "/");
            pathFile.mkdirs();
            if (Build.VERSION.SDK_INT >= 23) {

                pdfUri = FileProvider.getUriForFile(mContext, getPackageName() + ".provider", createWebPrintJobShare(paymentWeb,pathFile));
            } else {
                pdfUri = Uri.fromFile(createWebPrintJobShare(paymentWeb,pathFile));
            }

            Intent intent = new Intent(mContext, DownloadPdfHtmlViewActivity.class);
            intent.putExtra("url", fullHtml);
            intent.putExtra("tab_type", "Preview");
            intent.putExtra("orderId", "NASDUBAI");
            intent.putExtra("pdfUri", pdfUri.toString());
            startActivity(intent);
            paymentWeb.setVisibility(View.GONE);

        } else {
        }
    }

    void sharePdfFilePrint() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT)
        {
            paymentWeb.loadUrl("about:blank");
            setWebViewSettingsPrint();
            loadWebViewWithDataPrint();
            pathFile= new File(Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/" + "NAS_DUBAI_TRIP/Payments" + "_" + "NASDUBAI" + "/");

            pathFile.mkdirs();
            if (Build.VERSION.SDK_INT >= 23) {

                pdfUri = FileProvider.getUriForFile(mContext, getPackageName() + ".provider", createWebPrintJobShare(paymentWeb,pathFile));
            } else {
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


        try {
            PrintDocumentAdapter printAdapter;
            PrintAttributes attributes = new PrintAttributes.Builder()
                    .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                    .setResolution(new PrintAttributes.Resolution("pdf", "pdf", 600, 600))
                    .setMinMargins(PrintAttributes.Margins.NO_MARGINS).build();

            PdfPrint pdfPrint = new PdfPrint(attributes, mContext);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                printAdapter = webView.createPrintDocumentAdapter(jobName);

            } else {
                printAdapter = webView.createPrintDocumentAdapter();

            }

            pdfPrint.printNew(printAdapter, path, jobName, mContext.getCacheDir().getPath());
            return new File(path.getAbsolutePath() + "/"  + jobName);
        } catch (Exception e) {
            e.printStackTrace();
            paymentWeb.setVisibility(View.GONE);

        }
        return null;

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
                                        int survey=dataObject.getInt("survey");
                                        if (survey==1)
                                        {
                                            callSurveyApi();
                                        }
                                        payment_type_print="Online";
                                        payTotalButton.setVisibility(View.GONE);
                                        totalLinear.setVisibility(View.VISIBLE);
                                        paidImg.setVisibility(View.VISIBLE);
                                        mainLinear.setVisibility(View.VISIBLE);
                                        printLinear.setVisibility(View.VISIBLE);


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
        paymentWeb.setWebViewClient(new PaymentDetailFeeOnlyActivityNew.MyPrintWebViewClient());
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

        dialog.show();

    }

    private void getPaymentToken()
    {
        VolleyWrapper volleyWrapper = new VolleyWrapper(URLConstants.URL_PAYMENT_TOKEN_FOR_FEE);
//        String[] name = {"access_token","users_id"};
//        String[] value = {PreferenceManager.getAccessToken(mContext), PreferenceManager.getUserId(mContext)};
        String[] name = {};
        String[] value = {};
        System.out.println("Access token payment"+PreferenceManager.getAccessToken(mContext));
        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("The response accesstoken" + successResponse);
                try {
                    JSONObject obj = new JSONObject(successResponse);
                    String response_code = obj.getString(JTAG_RESPONSECODE);
                    if (response_code.equalsIgnoreCase("200")) {
                        JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                        String status_code = secobj.getString(JTAG_STATUSCODE);
                        if (status_code.equalsIgnoreCase("303")) {
                            PaymentToken = secobj.getString("access_token");
                            Log.d("PAYMM",PaymentToken);
                            System.out.println("AMOUNT IN PAYMENT"+amount);
                            double amountDouble=Double.parseDouble(amount)*100;
                            int amuntInt=(int)amountDouble;
                            String strDouble=String.valueOf(amuntInt);
                           // int amountInt=Integer.parseInt(amount)*100;
                           // String str = String.valueOf(amountInt);
                            int arrayLength = 0;
                            char[] array = strDouble.toCharArray();
                            arrayLength = array.length;
                            int firstNonZeroAt = 0;
                            for(int i=0; i<array.length; i++) {
                                if(!String.valueOf(array[i]).equalsIgnoreCase("0")) {
                                    firstNonZeroAt = i;
                                    break;
                                }
                            }
                            System.out.println("first non zero digit at : " +firstNonZeroAt);
                            char [] newArray = Arrays.copyOfRange(array, firstNonZeroAt,arrayLength);
                            String resultString = new String(newArray);
                            System.out.println("amount removed zero"+resultString);
                            long unixTime = System.currentTimeMillis() / 1000L;
                            orderId="NASDUBAI" + categoryId+ "S" + studentId;
                            System.out.println("Unix Time:::"+unixTime+"Order ID"+strDouble);
                            CallForPayment(strDouble);
                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        getPaymentToken();

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        getPaymentToken();

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        getPaymentToken();

                    } else {
                        /*CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
								, getResources().getString(R.string.ok));
						dialog.show();*/
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    }
                } catch (Exception ex) {
                    System.out.println("The Exception in edit profile is" + ex.toString());
                }

            }

            @Override
            public void responseFailure(String failureResponse) {
                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

            }
        });
    }


    private void CallForPayment(String amount)
    {
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        merchantOrderReference=invoice_no+"-"+ts;
       // merchantOrderReference="MUSICACAT2-01-1610606134";
        VolleyWrapper volleyWrapper = new VolleyWrapper(URLConstants.URL_CREATE_PAYMENT_FEE);
        String[] name = {"access_token", "amount","merchantOrderReference","firstName","lastName","address1","city","countryCode","emailAddress"};
        String[] value = {PaymentToken, amount,merchantOrderReference,PreferenceManager.getCanteenStudentName(mContext),"","NAS DUBAI","DUBAI","UAE",PreferenceManager.getUserEmail(mContext)};
        System.out.println("payment Access_token"+PaymentToken);
        System.out.println("payment amount"+amount);
        System.out.println("payment merchantOrderReference"+merchantOrderReference);
        System.out.println("payment firstName"+PreferenceManager.getCanteenStudentName(mContext));
        System.out.println("payment emailAddress"+PreferenceManager.getUserEmail(mContext));
        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("The response is" + successResponse);
                try {
                    JSONObject obj = new JSONObject(successResponse);
                    String response_code = obj.getString(JTAG_RESPONSECODE);
                    if (response_code.equalsIgnoreCase("200")) {
                        JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                        String status_code = secobj.getString(JTAG_STATUSCODE);
                        if (status_code.equalsIgnoreCase("303"))
                        {

                            OrderRef = secobj.getString("order_reference");
                            PayUrl = secobj.getString("order_paypage_url");
                            AuthUrl = secobj.getString("authorization");
                            String Code = PayUrl.split("=")[1];
                            Log.d("PAYMM",OrderRef);
                            Log.d("PAYMM",PayUrl);
                            Log.d("PAYMM",Code);
                            Log.d("PAYMM",AuthUrl);

//                            String AuthURL = "https://api-gateway.sandbox.ngenius-payments.com/transactions/paymentAuthorization";

                            CardPaymentRequest request = new CardPaymentRequest(AuthUrl,Code);

                            PaymentClient paymentClient = new PaymentClient(mActivity);
                            paymentClient.launchCardPayment(request,0);

                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        CallForPayment(amount);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        CallForPayment(amount);

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        CallForPayment(amount);

                    } else {
                        /*CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
								, getResources().getString(R.string.ok));
						dialog.show();*/
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    }
                } catch (Exception ex) {
                    System.out.println("The Exception in edit profile is" + ex.toString());
                }

            }

            @Override
            public void responseFailure(String failureResponse) {
                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("request_code", String.valueOf(requestCode));
        Log.d("resultt_code", String.valueOf(resultCode));
        if (data == null) {

            Toast.makeText(mActivity, "transaction cancelled", Toast.LENGTH_SHORT).show();
        } else {
            if (requestCode == 0) {

//            Log.d("response",data.getStringExtra("jsonResponse"));
//            String jsonObject=data.getStringExtra("jsonResponse");
//            Log.v("jsonResponse",jsonObject);

                CardPaymentData cardPaymentData = CardPaymentData.getFromIntent(data);
                Log.d("PAYMM",String.valueOf(cardPaymentData.getCode()));
                Log.d("PAYMM",String.valueOf(cardPaymentData.getReason()));

                if (cardPaymentData.getCode() == 2) {
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
                    payment_type_print="Online";
                    payTotalButton.setVisibility(View.GONE);
                    totalLinear.setVisibility(View.VISIBLE);
                    paidImg.setVisibility(View.VISIBLE);
                    mainLinear.setVisibility(View.VISIBLE);
                    printLinear.setVisibility(View.VISIBLE);
                    tripSync(URL_FEE_PAYMENT_SUBMISSION, categoryId, studentId,tripDetailsAPI);


//                Log.d("reason",cardPaymentData.getReason());

                } else {
                    Toast.makeText(mContext, "Transaction failed", Toast.LENGTH_SHORT).show();

                }

            }

        }
    }


    /**********************************SURVEY ******************************************/

    /**********************************SURVEY API***************************************/
    public void callSurveyApi() {
        surveyArrayList=new ArrayList<>();


        try {
            final VolleyWrapper manager = new VolleyWrapper(URL_GET_USER_SURVEY);
            String[] name = new String[]{JTAG_ACCESSTOKEN,"users_id","module"};
            String[] value = new String[]{PreferenceManager.getAccessToken(mContext),PreferenceManager.getUserId(mContext),"5"};
            manager.getResponsePOST(mContext, 14, name, value, new VolleyWrapper.ResponseListener() {

                @Override
                public void responseSuccess(String successResponse) {
                    String responsCode = "";
                    if (successResponse != null) {
                        try {
                            Log.e("SURVEY VALUE",successResponse);
                            JSONObject rootObject = new JSONObject(successResponse);
                            if (rootObject.optString(JTAG_RESPONSE) != null) {
                                responsCode = rootObject.optString(JTAG_RESPONSECODE);
                                if (responsCode.equals(RESPONSE_SUCCESS)) {
                                    JSONObject respObject = rootObject.getJSONObject(JTAG_RESPONSE);
                                    String statusCode = respObject.optString(JTAG_STATUSCODE);
                                    if (statusCode.equals(STATUS_SUCCESS)) {
                                        surveyEmail=respObject.optString("contact_email");
                                        JSONArray dataArray=respObject.getJSONArray("data");
                                        if (dataArray.length()>0)
                                        {
                                            surveySize=dataArray.length();
                                            for (int i=0;i<dataArray.length();i++)
                                            {
                                                JSONObject dataObject = dataArray.getJSONObject(i);
                                                SurveyModel model=new SurveyModel();
                                                model.setId(dataObject.optString("id"));
                                                model.setSurvey_name(dataObject.optString("survey_name"));
                                                model.setImage(dataObject.optString("image"));
                                                model.setTitle(dataObject.optString("title"));
                                                model.setDescription(dataObject.optString("description"));
                                                model.setCreated_at(dataObject.optString("created_at"));
                                                model.setUpdated_at(dataObject.optString("updated_at"));
                                                model.setContact_email(dataObject.optString("contact_email"));
                                                surveyQuestionArrayList=new ArrayList<>();
                                                JSONArray questionsArray=dataObject.getJSONArray("questions");
                                                if (questionsArray.length()>0)
                                                {
                                                    for (int j=0;j<questionsArray.length();j++)
                                                    {
                                                        JSONObject questionsObject = questionsArray.getJSONObject(j);
                                                        SurveyQuestionsModel mModel=new SurveyQuestionsModel();
                                                        mModel.setId(questionsObject.optString("id"));
                                                        mModel.setQuestion(questionsObject.optString("question"));
                                                        mModel.setAnswer_type(questionsObject.optString("answer_type"));
                                                        mModel.setAnswer("");
                                                        surveyAnswersArrayList=new ArrayList<>();
                                                        JSONArray answerArray=questionsObject.getJSONArray("offered_answers");
                                                        if (answerArray.length()>0)
                                                        {
                                                            for (int k=0;k<answerArray.length();k++)
                                                            {
                                                                JSONObject answerObject = answerArray.getJSONObject(k);
                                                                SurveyAnswersModel nModel=new SurveyAnswersModel();
                                                                nModel.setId(answerObject.optString("id"));
                                                                nModel.setAnswer(answerObject.optString("answer"));
                                                                nModel.setLabel(answerObject.optString("label"));
                                                                nModel.setClicked(false);
                                                                nModel.setClicked0(false);
                                                                surveyAnswersArrayList.add(nModel);
                                                            }
                                                        }
                                                        mModel.setSurveyAnswersrrayList(surveyAnswersArrayList);
                                                        surveyQuestionArrayList.add(mModel);
                                                    }
                                                }
                                                model.setSurveyQuestionsArrayList(surveyQuestionArrayList);
                                                surveyArrayList.add(model);
                                            }
                                            //showSurvey(getActivity(),surveyArrayList);
                                            showSurveyWelcomeDialogue((Activity)mContext ,surveyArrayList,false);
                                        }

                                    }
                                }
                                else if (responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) ||
                                        responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                                        responsCode.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
                                    AppUtils.postInitParam((Activity)mContext, new AppUtils.GetAccessTokenInterface() {
                                        @Override
                                        public void getAccessToken() {
                                        }
                                    });
                                    callSurveyApi();

                                }
                            } else if (responsCode.equals(RESPONSE_ERROR)) {
//								CustomStatusDialog(RESPONSE_FAILURE);

                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }

                @Override
                public void responseFailure(String failureResponse) {
                    // CustomStatusDialog(RESPONSE_FAILURE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    /**********************************SURVEY DIALOGUES***************************************/
    public void showSurveyWelcomeDialogue(final Activity activity, final ArrayList<SurveyModel> surveyArrayList,final Boolean isThankyou)
    {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_survey_welcome_screen);
        Button startNowBtn = (Button) dialog.findViewById(R.id.startNowBtn);
        ImageView imgClose = (ImageView) dialog.findViewById(R.id.closeImg);
        TextView headingTxt = (TextView) dialog.findViewById(R.id.titleTxt);
        TextView descriptionTxt = (TextView) dialog.findViewById(R.id.descriptionTxt);
//        if (isThankyou)
//		{
//			thankyouTxt.setVisibility(View.VISIBLE);
//			thankyouTxt.setText("Thank you For Submitting your Survey");
//		}
//        else
//        {
//			thankyouTxt.setVisibility(View.GONE);
//		}
        ImageView bannerImg = (ImageView) dialog.findViewById(R.id.bannerImg);
        if (!surveyArrayList.get(pos+1).getImage().equalsIgnoreCase(""))
        {
            Picasso.with(mContext).load(AppUtils.replace(surveyArrayList.get(pos+1).getImage())).placeholder(R.drawable.survey).fit().into(bannerImg);
        }
        else
        {
            bannerImg.setImageResource(R.drawable.survey);
        }
        headingTxt.setText(surveyArrayList.get(pos+1).getTitle());
        descriptionTxt.setText(surveyArrayList.get(pos+1).getDescription());


        startNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (surveyArrayList.size()>0)
                {
                    pos=pos+1;
                    if (pos<surveyArrayList.size())
                    {
                        showSurveyQuestionAnswerDialog(activity,surveyArrayList.get(pos).getSurveyQuestionsArrayList(),surveyArrayList.get(pos).getSurvey_name(),surveyArrayList.get(pos).getId(),surveyArrayList.get(pos).getContact_email());
                    }
                }

            }
        });
        Button skipBtn = (Button) dialog.findViewById(R.id.skipBtn);
        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // showCloseSurveyDialog(activity,dialog);
                 dialog.dismiss();

            }
        });
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCloseSurveyDialog(activity,dialog);
            }
        });
        dialog.show();

    }

    public void showSurveyQuestionAnswerDialog(final Activity activity, final ArrayList<SurveyQuestionsModel> surveyQuestionArrayList,final String surveyname,String surveyID,String contactEmail)
    {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_question_choice_survey);
        ViewPager surveyPager = (ViewPager) dialog.findViewById(R.id.surveyPager);
        TextView questionCount = (TextView) dialog.findViewById(R.id.questionCount);
        TextView nxtQnt = (TextView) dialog.findViewById(R.id.nxtQnt);
        TextView currentQntTxt = (TextView) dialog.findViewById(R.id.currentQntTxt);
        TextView surveyName = (TextView) dialog.findViewById(R.id.surveyName);
        Button skipBtn = (Button) dialog.findViewById(R.id.skipBtn);
        ImageView previousBtn = (ImageView) dialog.findViewById(R.id.previousBtn);
        ImageView nextQuestionBtn = (ImageView) dialog.findViewById(R.id.nextQuestionBtn);

        ImageView closeImg = (ImageView) dialog.findViewById(R.id.closeImg);
        ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.progressBar);
        progressBar.setMax(surveyQuestionArrayList.size());
        progressBar.getProgressDrawable().setColorFilter(mContext.getResources().getColor(R.color.rel_one), android.graphics.PorterDuff.Mode.SRC_IN);
        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showCloseSurveyDialog(activity,dialog);
            }
        });
        if (surveyQuestionArrayList.size()>9)
        {
            currentQntTxt.setText("01");
            questionCount.setText("/"+String.valueOf(surveyQuestionArrayList.size()));
        }
        else {
            currentQntTxt.setText("01");
            questionCount.setText("/0"+String.valueOf(surveyQuestionArrayList.size()));
        }
        surveyName.setText(surveyname);

        currentPageSurvey=1;
        surveyPager.setCurrentItem(currentPageSurvey-1);
        progressBar.setProgress(currentPageSurvey);
        surveyPager.setAdapter(new SurveyPagerAdapter(activity,surveyQuestionArrayList,nextQuestionBtn));
        if(currentPageSurvey==surveyQuestionArrayList.size())
        {
            previousBtn.setVisibility(View.INVISIBLE);
            nextQuestionBtn.setVisibility(View.INVISIBLE);
            nxtQnt.setVisibility(View.VISIBLE);
        }
        else
        {
            if (currentPageSurvey==1)
            {
                previousBtn.setVisibility(View.INVISIBLE);
                nextQuestionBtn.setVisibility(View.VISIBLE);
                nxtQnt.setVisibility(View.INVISIBLE);
            }
            else {
                previousBtn.setVisibility(View.INVISIBLE);
                nextQuestionBtn.setVisibility(View.VISIBLE);
                nxtQnt.setVisibility(View.INVISIBLE);
            }
        }

        nextQuestionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (currentPageSurvey==surveyQuestionArrayList.size())
                {

                }
                else {
                    currentPageSurvey++;
                    progressBar.setProgress(currentPageSurvey);
                    surveyPager.setCurrentItem(currentPageSurvey-1);
                    if (currentPageSurvey==surveyQuestionArrayList.size())
                    {
                        nextQuestionBtn.setVisibility(View.INVISIBLE);
                        previousBtn.setVisibility(View.VISIBLE);
                        nxtQnt.setVisibility(View.VISIBLE);

                    }else {
                        nextQuestionBtn.setVisibility(View.VISIBLE);
                        previousBtn.setVisibility(View.VISIBLE);
                        nxtQnt.setVisibility(View.INVISIBLE);
                    }
                }

                if (surveyQuestionArrayList.size()>9)
                {
                    if (currentPageSurvey<9)
                    {
                        currentQntTxt.setText("0"+currentPageSurvey);
                        questionCount.setText("/"+String.valueOf(surveyQuestionArrayList.size()));
                    }
                    else {
                        currentQntTxt.setText(currentPageSurvey);
                        questionCount.setText("/"+String.valueOf(surveyQuestionArrayList.size()));
                    }

                }
                else {
                    if (currentPageSurvey<9)
                    {
                        currentQntTxt.setText("0"+currentPageSurvey);
                        questionCount.setText("/"+"0"+String.valueOf(surveyQuestionArrayList.size()));
                    }
                    else {
                        currentQntTxt.setText(currentPageSurvey);
                        questionCount.setText("/"+"0"+String.valueOf(surveyQuestionArrayList.size()));
                    }
                }

            }
        });
        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPageSurvey==1)
                {
                    previousBtn.setVisibility(View.INVISIBLE);
                    nxtQnt.setVisibility(View.INVISIBLE);
                    if (currentPageSurvey==surveyQuestionArrayList.size())
                    {
                        nxtQnt.setVisibility(View.VISIBLE);
                    }
                    else {
                        nxtQnt.setVisibility(View.INVISIBLE);
                    }
                }
                else {

                    currentPageSurvey--;
                    progressBar.setProgress(currentPageSurvey);
                    surveyPager.setCurrentItem(currentPageSurvey-1);
                    if (currentPageSurvey==surveyQuestionArrayList.size())
                    {
                        nextQuestionBtn.setVisibility(View.INVISIBLE);
                        previousBtn.setVisibility(View.VISIBLE);
                        nxtQnt.setVisibility(View.VISIBLE);

                    }else {
                        if (currentPageSurvey==1)
                        {
                            previousBtn.setVisibility(View.INVISIBLE);
                            nextQuestionBtn.setVisibility(View.VISIBLE);
                            nxtQnt.setVisibility(View.INVISIBLE);
                        }
                        else {
                            nextQuestionBtn.setVisibility(View.VISIBLE);
                            previousBtn.setVisibility(View.VISIBLE);
                            nxtQnt.setVisibility(View.INVISIBLE);
                        }

                    }
                }


                if (surveyQuestionArrayList.size()>9)
                {
                    if (currentPageSurvey<9)
                    {
                        currentQntTxt.setText("0"+currentPageSurvey);
                        questionCount.setText("/"+String.valueOf(surveyQuestionArrayList.size()));
                    }
                    else {
                        currentQntTxt.setText(currentPageSurvey);
                        questionCount.setText("/"+String.valueOf(surveyQuestionArrayList.size()));
                    }

                }
                else {
                    if (currentPageSurvey<9)
                    {
                        currentQntTxt.setText("0"+currentPageSurvey);
                        questionCount.setText("/"+"0"+String.valueOf(surveyQuestionArrayList.size()));
                    }
                    else {
                        currentQntTxt.setText(currentPageSurvey);
                        questionCount.setText("/"+"0"+String.valueOf(surveyQuestionArrayList.size()));
                    }
                }

            }
        });
        nxtQnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isFound=false;
                int pos=-1;
                int emptyvalue=0;
                for (int i=0;i<surveyQuestionArrayList.size();i++)
                {
                    if (surveyQuestionArrayList.get(i).getAnswer().equalsIgnoreCase(""))
                    {
                        emptyvalue=emptyvalue+1;
                        if (!isFound)
                        {
                            isFound=true;
                            pos=i;
                        }
                    }
                }
                if (isFound)
                {
                    mAnswerList=new ArrayList<>();
                    for (int k=0;k<surveyQuestionArrayList.size();k++)
                    {
                        AnswerSubmitModel model=new AnswerSubmitModel();
                        model.setQuestion_id(surveyQuestionArrayList.get(k).getId());
                        model.setAnswer_id(surveyQuestionArrayList.get(k).getAnswer());
                        mAnswerList.add(model);
                    }
                    Gson gson   = new Gson();
                    ArrayList<String> PassportArray = new ArrayList<>();
                    for (int i=0;i<mAnswerList.size();i++)
                    {
                        AnswerSubmitModel nmodel=new AnswerSubmitModel();
                        nmodel.setAnswer_id(mAnswerList.get(i).getAnswer_id());
                        nmodel.setQuestion_id(mAnswerList.get(i).getQuestion_id());
                        String json = gson.toJson(nmodel);
                        PassportArray.add(i,json);
                    }
                    String JSON_STRING=""+PassportArray+"";
                    Log.e("JSON",JSON_STRING);
                    if (emptyvalue==surveyQuestionArrayList.size())
                    {
                        boolean isEmpty=true;
                        showSurveyContinueDialog(activity,surveyID,JSON_STRING,surveyArrayList,progressBar,surveyPager,surveyQuestionArrayList,previousBtn,nextQuestionBtn,nxtQnt,currentQntTxt,questionCount,pos,dialog,isEmpty);

                    }
                    else {
                        boolean isEmpty=false;
                        showSurveyContinueDialog(activity,surveyID,JSON_STRING,surveyArrayList,progressBar,surveyPager,surveyQuestionArrayList,previousBtn,nextQuestionBtn,nxtQnt,currentQntTxt,questionCount,pos,dialog,isEmpty);

                    }


                }
                else
                {
                    surveySize=surveySize-1;
                    if (surveySize<=0)
                    {
                        mAnswerList=new ArrayList<>();
                        for (int k=0;k<surveyQuestionArrayList.size();k++)
                        {
                            AnswerSubmitModel model=new AnswerSubmitModel();
                            model.setQuestion_id(surveyQuestionArrayList.get(k).getId());
                            model.setAnswer_id(surveyQuestionArrayList.get(k).getAnswer());
                            mAnswerList.add(model);
                        }
                        Gson gson   = new Gson();
                        ArrayList<String> PassportArray = new ArrayList<>();
                        for (int i=0;i<mAnswerList.size();i++)
                        {
                            AnswerSubmitModel nmodel=new AnswerSubmitModel();
                            nmodel.setAnswer_id(mAnswerList.get(i).getAnswer_id());
                            nmodel.setQuestion_id(mAnswerList.get(i).getQuestion_id());
                            String json = gson.toJson(nmodel);
                            PassportArray.add(i,json);
                        }
                        String JSON_STRING=""+PassportArray+"";
                        Log.e("JSON",JSON_STRING);
                        dialog.dismiss();
                        callSurveySubmitApi(URL_SURVEY_SUBMIT,surveyID,JSON_STRING,(Activity)mContext,surveyArrayList,false,1);
                    }
                    else {
                        mAnswerList=new ArrayList<>();
                        for (int k=0;k<surveyQuestionArrayList.size();k++)
                        {
                            AnswerSubmitModel model=new AnswerSubmitModel();
                            model.setQuestion_id(surveyQuestionArrayList.get(k).getId());
                            model.setAnswer_id(surveyQuestionArrayList.get(k).getAnswer());
                            mAnswerList.add(model);
                        }
                        Gson gson   = new Gson();
                        ArrayList<String> PassportArray = new ArrayList<>();
                        for (int i=0;i<mAnswerList.size();i++)
                        {
                            AnswerSubmitModel nmodel=new AnswerSubmitModel();
                            nmodel.setAnswer_id(mAnswerList.get(i).getAnswer_id());
                            nmodel.setQuestion_id(mAnswerList.get(i).getQuestion_id());
                            String json = gson.toJson(nmodel);
                            PassportArray.add(i,json);
                        }
                        String JSON_STRING=""+PassportArray+"";
                        Log.e("JSON",JSON_STRING);
                        dialog.dismiss();
                        callSurveySubmitApi(URL_SURVEY_SUBMIT,surveyID,JSON_STRING,(Activity)mContext,surveyArrayList,true,1);
                    }


                }

                Log.e("POS",String.valueOf(pos));


            }

        });

        dialog.show();

    }



    public void showSurveyContinueDialog(final Activity activity,String surveyID,String JSON_STRING,ArrayList<SurveyModel> surveyArrayList,ProgressBar progressBar,ViewPager surveyPager,ArrayList<SurveyQuestionsModel>surveyQuestionArrayList,ImageView previousBtn,ImageView nextQuestionBtn,TextView nxtQnt,TextView currentQntTxt,TextView questionCount, int pos,Dialog nDialog,boolean isEmpty)
    {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_continue_layout);
        survey_satisfation_status=0;
        //callSurveySubmitApi(URL_SURVEY_SUBMIT,surveyId,jsonData,getActivity(),surveyArrayList,isThankyou,survey_satisfation_status,dialog);
        Button btn_Ok = (Button) dialog.findViewById(R.id.btn_Ok);
        Button submit = (Button) dialog.findViewById(R.id.submit);
        TextView thoughtsTxt = (TextView) dialog.findViewById(R.id.thoughtsTxt);

        if (isEmpty)
        {
            submit.setClickable(false);
            submit.setAlpha(0.5f);
            thoughtsTxt.setText("Appreciate atleast one feedback from you.");
        }
        else {
            submit.setClickable(true);
            submit.setAlpha(1.0f);
            thoughtsTxt.setText("There is an unanswered question on this survey. Would you like to continue?");
        }
        submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if (!isEmpty)
                {
                    nDialog.dismiss();
                    Log.e("SURVEY SIZE",String.valueOf(surveySize));
                    surveySize=surveySize-1;
                    if (surveySize<=0)
                    {
                        Log.e("SURVEY SIZE ",String.valueOf(surveySize));
                        callSurveySubmitApi(URL_SURVEY_SUBMIT,surveyID,JSON_STRING,(Activity)mContext,surveyArrayList,false,1);
                    }
                    else {
                        callSurveySubmitApi(URL_SURVEY_SUBMIT,surveyID,JSON_STRING,(Activity)mContext,surveyArrayList,true,1);

                    }
                    dialog.dismiss();
                }



            }
        });
        btn_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentPageSurvey=pos+1;
                progressBar.setProgress(currentPageSurvey);
                surveyPager.setCurrentItem(currentPageSurvey-1);

                Log.e("WORKING","SURVEY COUNT"+String.valueOf(currentPageSurvey));
                if(surveyQuestionArrayList.size()>1)
                {
                    if (currentPageSurvey!=surveyQuestionArrayList.size())
                    {
                        if(currentPageSurvey==1)
                        {
                            previousBtn.setVisibility(View.INVISIBLE);
                            nextQuestionBtn.setVisibility(View.VISIBLE);
                            nxtQnt.setVisibility(View.INVISIBLE);
                        }
                        else {
                            if(currentPageSurvey==1)
                            {
                                previousBtn.setVisibility(View.INVISIBLE);
                                nextQuestionBtn.setVisibility(View.VISIBLE);
                                nxtQnt.setVisibility(View.INVISIBLE);
                            }
                            else {
                                previousBtn.setVisibility(View.VISIBLE);
                                nextQuestionBtn.setVisibility(View.VISIBLE);
                                nxtQnt.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                    else {
                        previousBtn.setVisibility(View.VISIBLE);
                        nextQuestionBtn.setVisibility(View.INVISIBLE);
                        nxtQnt.setVisibility(View.VISIBLE);
                    }

                }
                else {
                    if (currentPageSurvey==1)
                    {
                        previousBtn.setVisibility(View.INVISIBLE);
                        nextQuestionBtn.setVisibility(View.INVISIBLE);
                        nxtQnt.setVisibility(View.VISIBLE);
                    }
                }
                if (surveyQuestionArrayList.size()>9)
                {
                    if (currentPageSurvey<9)
                    {
                        currentQntTxt.setText("0"+currentPageSurvey);
                        questionCount.setText("/"+String.valueOf(surveyQuestionArrayList.size()));
                    }
                    else {
                        currentQntTxt.setText(currentPageSurvey);
                        questionCount.setText("/"+String.valueOf(surveyQuestionArrayList.size()));
                    }

                }
                else {
                    if (currentPageSurvey<9)
                    {
                        currentQntTxt.setText("0"+currentPageSurvey);
                        questionCount.setText("/"+"0"+String.valueOf(surveyQuestionArrayList.size()));
                    }
                    else {
                        currentQntTxt.setText(currentPageSurvey);
                        questionCount.setText("/"+"0"+String.valueOf(surveyQuestionArrayList.size()));
                    }
                }

                dialog.dismiss();
            }
        });
        dialog.show();

    }


    public void showCloseSurveyDialog(final Activity activity, final Dialog dialogCLose)
    {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_close_survey);
        TextView text_dialog = (TextView) dialog.findViewById(R.id.text_dialog);
        text_dialog.setText("Are you sure you want to close this survey.");

        Button btn_Ok = (Button) dialog.findViewById(R.id.btn_Ok);
        btn_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set Preference to true
                dialogCLose.dismiss();
                dialog.dismiss();

            }
        });

        Button btn_Cancel = (Button) dialog.findViewById(R.id.btn_Cancel);
        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        dialog.show();

    }


    private void sendEmailToStaff(String URL,String email,Dialog dialogThank,Dialog dialog) {
        VolleyWrapper volleyWrapper=new VolleyWrapper(URL);
        String[] name={"access_token","email","users_id","title","message"};
        String[] value={PreferenceManager.getAccessToken(mContext),email,PreferenceManager.getUserId(mContext),text_dialog.getText().toString(),text_content.getText().toString()};//contactEmail

        //String[] value={PreferenceManager.getAccessToken(mContext),mStaffList.get(pos).getStaffEmail(),JTAG_USERS_ID_VALUE,text_dialog.getText().toString(),text_content.getText().toString()};
        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("The response is" + successResponse);
                try {
                    JSONObject obj = new JSONObject(successResponse);
                    String response_code = obj.getString(JTAG_RESPONSECODE);
                    if (response_code.equalsIgnoreCase("200")) {
                        JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                        String status_code = secobj.getString(JTAG_STATUSCODE);
                        if (status_code.equalsIgnoreCase("303")) {
                            Toast toast = Toast.makeText(mContext, "Successfully sent email to staff", Toast.LENGTH_SHORT);
                            toast.show();
                            dialogThank.dismiss();
                            dialog.dismiss();
                        } else {
                            Toast toast = Toast.makeText(mContext, "Email not sent", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        sendEmailToStaff(URL_SEND_EMAIL_TO_STAFF,email,dialogThank,dialog);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        sendEmailToStaff(URL_SEND_EMAIL_TO_STAFF,email,dialogThank,dialog);


                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        sendEmailToStaff(URL_SEND_EMAIL_TO_STAFF,email,dialogThank,dialog);

                    } else {
						/*CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
								, getResources().getString(R.string.ok));
						dialog.show();*/
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    }
                } catch (Exception ex)
                {
                    System.out.println("The Exception in edit profile is" + ex.toString());
                }

            }

            @Override
            public void responseFailure(String failureResponse) {
				/*CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
						, getResources().getString(R.string.ok));
				dialog.show();*/
                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

            }
        });


    }


    public void showSurveyThankYouDialog(final Activity activity, final ArrayList<SurveyModel> surveyArrayList,final Boolean isThankyou)
    {
        final Dialog dialogThank = new Dialog(activity);
        dialogThank.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogThank.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogThank.setCancelable(false);
        dialogThank.setContentView(R.layout.dialog_survey_thank_you);
        survey_satisfation_status=0;
        //callSurveySubmitApi(URL_SURVEY_SUBMIT,surveyId,jsonData,getActivity(),surveyArrayList,isThankyou,survey_satisfation_status,dialog);

        ImageView emailImg = (ImageView) dialogThank.findViewById(R.id.emailImg);
        if (surveyEmail.equalsIgnoreCase(""))
        {
            emailImg.setVisibility(View.GONE);
        }
        else {
            emailImg.setVisibility(View.VISIBLE);
        }

        emailImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(mContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.alert_send_email_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Button dialogCancelButton = (Button) dialog.findViewById(R.id.cancelButton);
                Button submitButton = (Button) dialog.findViewById(R.id.submitButton);
                text_dialog = (EditText) dialog.findViewById(R.id.text_dialog);
                text_content = (EditText) dialog.findViewById(R.id.text_content);


                dialogCancelButton.setOnClickListener(new View.OnClickListener() {

                    @Override

                    public void onClick(View v) {
                        //   AppUtils.hideKeyBoard(mContext);
                        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(text_dialog.getWindowToken(), 0);
                        imm.hideSoftInputFromWindow(text_content.getWindowToken(), 0);
                        dialog.dismiss();

                    }

                });

                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendEmailToStaff(URL_SEND_EMAIL_TO_STAFF,surveyEmail,dialogThank,dialog);
                    }
                });


                dialog.show();
            }
        });


        Button btn_Ok = (Button) dialogThank.findViewById(R.id.btn_Ok);
        btn_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (isThankyou)
                {
                    showSurveyWelcomeDialogue(activity,surveyArrayList,true);

                }
                else
                {

                }
                dialogThank.dismiss();
            }
        });
        dialogThank.show();

    }

    private void callSurveySubmitApi(String URL,String survey_id,String answer,Activity activity,ArrayList<SurveyModel> surveyArrayList, Boolean isThankyou,int survey_satisfation_status) {
        VolleyWrapper volleyWrapper=new VolleyWrapper(URL);
        String[] name={"access_token","users_id","survey_id","answers","survey_satisfation_status"};
        Log.e("STATUs",String.valueOf(survey_satisfation_status));
        String[] value={PreferenceManager.getAccessToken(mContext),PreferenceManager.getUserId(mContext),survey_id,answer,String.valueOf(survey_satisfation_status)};//contactEmail
        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("The response is" + successResponse);
                try {
                    JSONObject obj = new JSONObject(successResponse);
                    String response_code = obj.getString(JTAG_RESPONSECODE);
                    if (response_code.equalsIgnoreCase("200")) {
                        JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                        String status_code = secobj.getString(JTAG_STATUSCODE);
                        if (status_code.equalsIgnoreCase("303")) {

                            showSurveyThankYouDialog((Activity)mContext,surveyArrayList,isThankyou);

                        } else {


                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        callSurveySubmitApi(URL_SURVEY_SUBMIT,survey_id,answer,activity,surveyArrayList,isThankyou,survey_satisfation_status);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        callSurveySubmitApi(URL_SURVEY_SUBMIT,survey_id,answer,activity,surveyArrayList,isThankyou,survey_satisfation_status);


                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        callSurveySubmitApi(URL_SURVEY_SUBMIT,survey_id,answer,activity,surveyArrayList,isThankyou,survey_satisfation_status);

                    } else {
						/*CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
								, getResources().getString(R.string.ok));
						dialog.show();*/
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    }
                } catch (Exception ex)
                {
                    System.out.println("The Exception in edit profile is" + ex.toString());
                }

            }

            @Override
            public void responseFailure(String failureResponse) {
				/*CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
						, getResources().getString(R.string.ok));
				dialog.show();*/
                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

            }
        });


    }

}
/*   String tripDetailsAPI="{\n" +
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
                        "}";*/
