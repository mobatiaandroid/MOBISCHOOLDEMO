package com.mobatia.naisapp.activities.chatter_box;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.chatter_box.adapter.ChatterboxAdapter;
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
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by admin on 26-Mar-17.
 */
public class ChatterBoxActivity extends Activity implements AdapterView.OnItemClickListener,
        NaisTabConstants,CacheDIRConstants, URLConstants,JSONConstants,StatusConstants,
        IntentPassValueConstants,NaisClassNameConstants {

    private Context mContext;
    Bundle extras;
    String tab_type;
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back;
    ImageView home;
    ImageView fbImageView;
    ArrayList<TermsCalendarModel> mTermsCalendarListArray ;
    ImageView bannerImagePager;
    ArrayList<String> bannerUrlImageArray;
    private ListView mTermsCalendarListView;
    String fbLink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatterbox_list);
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
        mTermsCalendarListView = (ListView) findViewById(R.id.mTermsCalendarListView);
        bannerImagePager= (ImageView) findViewById(R.id.bannerImageViewPager);

        headermanager = new HeaderManager(ChatterBoxActivity.this, tab_type);
        headermanager.getHeader(relativeHeader, 1);
        back = headermanager.getLeftButton();
        fbImageView=headermanager.getRightButton();
        headermanager.setButtonLeftSelector(R.drawable.back,
                R.drawable.back);
        mTermsCalendarListView.setOnItemClickListener(this);
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

        Intent mintent = new Intent(ChatterBoxActivity.this, FullscreenWebViewActivityNoHeader.class);
        mintent.putExtra("url",fbLink);
        startActivity(mintent);
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
            final VolleyWrapper manager = new VolleyWrapper(URL_CHATTER_BOX_LIST);
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
                                            bannerImagePager.setBackgroundResource(R.drawable.chatterbox_banner);

                                        }
                                        JSONArray dataArray = respObject.getJSONArray(JTAG_RESPONSE_DATA_ARRAY);
                                        if (dataArray.length() > 0) {
                                            for (int i = 0; i < dataArray.length(); i++) {
                                                JSONObject dataObject = dataArray.getJSONObject(i);
                                                mTermsCalendarListArray.add(getSearchValues(dataObject));

                                            }

                                            mTermsCalendarListView.setAdapter(new ChatterboxAdapter(mContext, mTermsCalendarListArray));

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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (mTermsCalendarListArray.get(position).getmFileName().endsWith(".pdf")) {
            Intent intent = new Intent(mContext, PdfReaderActivity.class);
            intent.putExtra("pdf_url",mTermsCalendarListArray.get(position).getmFileName());
            startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(mContext, LoadUrlWebViewActivity.class);
            intent.putExtra("url",mTermsCalendarListArray.get(position).getmFileName());
            intent.putExtra("tab_type",tab_type);
            startActivity(intent);
        }
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
}
