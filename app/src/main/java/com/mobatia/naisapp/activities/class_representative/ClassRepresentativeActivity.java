package com.mobatia.naisapp.activities.class_representative;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.class_representative.adapter.ClassRepresentativeRecyclerAdapter;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.activities.pdf.PdfReaderActivity;
import com.mobatia.naisapp.activities.term_calendar.model.TermsCalendarModel;
import com.mobatia.naisapp.activities.web_view.FullscreenWebViewActivityNoHeader;
import com.mobatia.naisapp.activities.web_view.LoadUrlWebViewActivity;
import com.mobatia.naisapp.constants.CacheDIRConstants;
import com.mobatia.naisapp.constants.IntentPassValueConstants;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.constants.NaisTabConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.HeaderManager;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.recyclerviewmanager.DividerItemDecoration;
import com.mobatia.naisapp.recyclerviewmanager.ItemOffsetDecoration;
import com.mobatia.naisapp.recyclerviewmanager.RecyclerItemListener;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by admin on 26-Mar-17.
 */
public class ClassRepresentativeActivity extends Activity implements
        NaisTabConstants,CacheDIRConstants, URLConstants,JSONConstants,StatusConstants,
        IntentPassValueConstants,NaisClassNameConstants {

    private Context mContext;
    Bundle extras;
    String tab_type;
    TextView descriptionTV;
    TextView descriptionTitle;
    RelativeLayout relativeHeader,mtitle;
    HeaderManager headermanager;
    ImageView back;
    ImageView home;
    ImageView fbImageView;
    ImageView sendEmail;
    ArrayList<TermsCalendarModel> mTermsCalendarListArray ;
    ImageView bannerImagePager;
    ArrayList<String> bannerUrlImageArray;
    private RecyclerView mChatterBoxListRecyclerView;
    String fbLink;
    String contactEmail;
    EditText text_dialog;
    EditText text_content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classrepresentative_list);
        mContext = this;
        initialiseUI();
    }

    /*******************************************************
     * Method name : initialiseUI Description : initialise UI elements
     * Parameters : nil Return type : void Date : March 8, 2017 Author : RIJO K JOSE
     *****************************************************/
    @SuppressWarnings("deprecation")
    public void initialiseUI() {
        extras=getIntent().getExtras();
        if(extras!=null) {
            tab_type=extras.getString("tab_type");
        }
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        mChatterBoxListRecyclerView = (RecyclerView) findViewById(R.id.mChatterBoxListRecyclerView);
        bannerImagePager= (ImageView) findViewById(R.id.bannerImageViewPager);
        descriptionTV= (TextView) findViewById(R.id.descriptionTV);
        descriptionTitle= (TextView) findViewById(R.id.descriptionTitle);
        sendEmail= (ImageView) findViewById(R.id.sendEmail);
        mtitle = (RelativeLayout) findViewById(R.id.title);

        headermanager = new HeaderManager(ClassRepresentativeActivity.this, tab_type);
        headermanager.getHeader(relativeHeader, 1);
        back = headermanager.getLeftButton();
        fbImageView=headermanager.getRightButton();
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
        fbImageView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent mintent = new Intent(ClassRepresentativeActivity.this, FullscreenWebViewActivityNoHeader.class);
        mintent.putExtra("url",fbLink);
        startActivity(mintent);
    }
});
        mChatterBoxListRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        int spacing = 5; // 50px
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext,spacing);
        mChatterBoxListRecyclerView.addItemDecoration(itemDecoration);
        mChatterBoxListRecyclerView.setLayoutManager(llm);
        mChatterBoxListRecyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.list_divider_teal)));
        mChatterBoxListRecyclerView.addOnItemTouchListener(new RecyclerItemListener(getApplicationContext(), mChatterBoxListRecyclerView,
                new RecyclerItemListener.RecyclerTouchListener() {
                    public void onClickItem(View v, int position) {
                        if (mTermsCalendarListArray.get(position).getmFileName().endsWith(".pdf")) {
                            Intent intent = new Intent(mContext, PdfReaderActivity.class);
                            intent.putExtra("pdf_url", mTermsCalendarListArray.get(position).getmFileName());
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(mContext, LoadUrlWebViewActivity.class);
                            intent.putExtra("url", mTermsCalendarListArray.get(position).getmFileName());
                            intent.putExtra("tab_type", tab_type);
                            startActivity(intent);
                        }

                    }

                    public void onLongClickItem(View v, int position) {
                    }
                }));
        sendEmail.setOnClickListener(new View.OnClickListener() {
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

                        dialog.dismiss();

                    }

                });

                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendEmailToStaff(URL_SEND_EMAIL_TO_STAFF);
                    }
                });


                dialog.show();


            }
        });

        if (AppUtils.checkInternet(mContext)) {

            getList();
        }
        else
        {
            AppUtils.showDialogAlertDismiss((Activity)mContext,"Network Error",getString(R.string.no_internet),R.drawable.nonetworkicon,R.drawable.roundred);

        }

    }
    public void getList() {

        try {
            mTermsCalendarListArray = new ArrayList<>();
            final VolleyWrapper manager = new VolleyWrapper(URL_CLASS_REPRESENTATIVE_LIST);
            String[] name = new String[]{JTAG_ACCESSTOKEN};
            String[] value = new String[]{PreferenceManager.getAccessToken(mContext)};


            manager.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {

                @Override
                public void responseSuccess(String successResponse) {
                    String responsCode = "";
                    if (successResponse != null) {
                        try {
                            JSONObject rootObject = new JSONObject(successResponse);
                            if (rootObject.optString(JTAG_RESPONSE) != null) {
                                responsCode = rootObject.optString(JTAG_RESPONSECODE);
                                if (responsCode.equals(RESPONSE_SUCCESS)) {
                                    JSONObject respObject = rootObject.getJSONObject(JTAG_RESPONSE);
                                    String statusCode = respObject.optString(JTAG_STATUSCODE);
                                    if (statusCode.equals(STATUS_SUCCESS)) {
                                        String bannerImage=respObject.optString(JTAG_BANNER_IMAGE);
                                         fbLink=respObject.optString(JTAG_FACEBOOK_URL);
                                        String description = respObject.optString(JTAG_DESCRIPTION);
                                         contactEmail = respObject.optString(JTAG_CONTACT_EMAIL);
                                        if(!fbLink.equals("")){
                                            headermanager.setButtonRightSelector(R.drawable.facebookshare,R.drawable.facebookshare);
                                        }
                                        else{
                                            fbImageView.setVisibility(View.GONE);
                                        }
                                        if (!bannerImage.equalsIgnoreCase("")) {
                                            Glide.with(mContext).load(AppUtils.replace(bannerImage)).centerCrop().into(bannerImagePager);

                                        }
                                        else
                                        {
                                            bannerImagePager.setBackgroundResource(R.drawable.demo);

                                        }
                                        if (description.equalsIgnoreCase("")&&contactEmail.equalsIgnoreCase(""))
                                        {
                                            mtitle.setVisibility(View.GONE);
                                        }
                                        else
                                        {
                                            mtitle.setVisibility(View.VISIBLE);
                                        }
                                        if (description.equalsIgnoreCase(""))
                                        {
                                            descriptionTV.setVisibility(View.GONE);
                                            descriptionTitle.setVisibility(View.GONE);
                                        }else
                                        {
                                            descriptionTV.setText(description);
                                            descriptionTitle.setVisibility(View.GONE);
                                            descriptionTV.setVisibility(View.VISIBLE);
                                            mtitle.setVisibility(View.VISIBLE);
                                        }
                                        if (contactEmail.equalsIgnoreCase(""))
                                        {
                                            sendEmail.setVisibility(View.GONE);
                                        }else
                                        {
                                            mtitle.setVisibility(View.VISIBLE);
                                            sendEmail.setVisibility(View.VISIBLE);
                                        }
                                        JSONArray dataArray = respObject.getJSONArray(JTAG_RESPONSE_DATA_ARRAY);
                                        if (dataArray.length() > 0) {
                                            for (int i = 0; i < dataArray.length(); i++) {
                                                JSONObject dataObject = dataArray.getJSONObject(i);
                                                mTermsCalendarListArray.add(getSearchValues(dataObject));

                                            }

                                            mChatterBoxListRecyclerView.setAdapter(new ClassRepresentativeRecyclerAdapter(mContext, mTermsCalendarListArray));

                                        } else {
                                            AppUtils.showDialogAlertDismiss((Activity)mContext,"Alert",getString(R.string.nodatafound),R.drawable.exclamationicon,R.drawable.round);

                                        }
                                    } else {
                                        AppUtils.showDialogAlertDismiss((Activity)mContext,"Alert",getString(R.string.common_error),R.drawable.exclamationicon,R.drawable.round);

                                    }
                                }
                                else if (responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) ||
                                        responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                                        responsCode.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
                                    AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                                        @Override
                                        public void getAccessToken() {
                                        }
                                    });
                                    getList();

                                }
                                else if (responsCode.equals(RESPONSE_ERROR)) {
                                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                                }
                            } else  {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }

                @Override
                public void responseFailure(String failureResponse) {
                    AppUtils.showDialogAlertDismiss((Activity)mContext,"Alert",getString(R.string.common_error),R.drawable.exclamationicon,R.drawable.round);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    private TermsCalendarModel getSearchValues(JSONObject Object)
            throws JSONException {
        TermsCalendarModel mTermsCalendarModel = new TermsCalendarModel();
        mTermsCalendarModel.setmId(Object.getString(JTAG_ID));
        mTermsCalendarModel.setmFileName(Object.getString(JTAG_TAB_FILE));
        mTermsCalendarModel.setmTitle(Object.getString(JTAG_TITLE));
        return mTermsCalendarModel;
    }


    private boolean appInstalledOrNot(String packagename) {
        try {
            ApplicationInfo info = getPackageManager().
                    getApplicationInfo(packagename, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
    private void sendEmailToStaff(String URL) {
        VolleyWrapper volleyWrapper=new VolleyWrapper(URL);
        String[] name={"access_token","email","users_id","title","message"};
        String[] value={PreferenceManager.getAccessToken(mContext),contactEmail,PreferenceManager.getUserId(mContext),text_dialog.getText().toString(),text_content.getText().toString()};//contactEmail

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
                            Toast toast = Toast.makeText(mContext,"Successfully sent email to staff", Toast.LENGTH_SHORT);
                            toast.show();
                        }else{
                            Toast toast = Toast.makeText(mContext,"Email not sent", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        sendEmailToStaff(URL_SEND_EMAIL_TO_STAFF);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        sendEmailToStaff(URL_SEND_EMAIL_TO_STAFF);

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        sendEmailToStaff(URL_SEND_EMAIL_TO_STAFF);

                    } else {

                        AppUtils.showDialogAlertDismiss((Activity)mContext,"Alert",mContext.getString(R.string.common_error),R.drawable.exclamationicon,R.drawable.round);

                    }
                } catch (Exception ex) {
                }

            }

            @Override
            public void responseFailure(String failureResponse) {

                AppUtils.showDialogAlertDismiss((Activity)mContext,"Alert",mContext.getString(R.string.common_error),R.drawable.exclamationicon,R.drawable.round);

            }
        });


    }

}
