package com.mobatia.naisapp.activities.cca;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.activities.pdf.PdfReaderActivity;
import com.mobatia.naisapp.activities.sports.information.adapter.InformationRecyclerAdapter;
import com.mobatia.naisapp.activities.sports.teams.model.TeamModel;
import com.mobatia.naisapp.activities.web_view.LoadUrlWebViewActivity;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.constants.ResultConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.secondary.model.SecondaryModel;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.HeaderManager;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.recyclerviewmanager.DividerItemDecoration;
import com.mobatia.naisapp.recyclerviewmanager.RecyclerItemListener;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ExternalProviderActivity extends Activity
        implements JSONConstants,URLConstants,ResultConstants,StatusConstants,NaisClassNameConstants {
    private Context mContext;
    Bundle extras;
    String tab_type;
    RelativeLayout relativeHeader;
    LinearLayout mStudentSpinner;
    ImageView studImg;
    TextView studName;
    RecyclerView mnewsLetterListView;
    ArrayList<TeamModel> studentsModelArrayList;
    TextView textViewYear;
    static String stud_id = "";
    String stud_class = "";
    String stud_name = "";
    String stud_img = "";
    String section = "";
    private ArrayList<SecondaryModel> mListViewArray;

    HeaderManager headermanager;
    ImageView back;
    ImageView home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        mContext = this;
        initUI();
        if (AppUtils.checkInternet(mContext)) {

            getList();
        } else {
            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

        }

    }

    private void initUI() {
        extras = getIntent().getExtras();
        if (extras != null) {
            tab_type = extras.getString("tab_type");
        }
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        mStudentSpinner = (LinearLayout) findViewById(R.id.studentSpinner);
        studImg = (ImageView) findViewById(R.id.imagicon);
        studName = (TextView) findViewById(R.id.studentName);
        textViewYear = (TextView) findViewById(R.id.textViewYear);
        mnewsLetterListView = (RecyclerView) findViewById(R.id.mnewsLetterListView);
        mnewsLetterListView.setHasFixedSize(true);
        mnewsLetterListView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.list_divider_teal)));
        headermanager = new HeaderManager(ExternalProviderActivity.this, tab_type);
        headermanager.getHeader(relativeHeader, 0);
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
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mnewsLetterListView.setLayoutManager(llm);
        mnewsLetterListView.addOnItemTouchListener(new RecyclerItemListener(getApplicationContext(), mnewsLetterListView,
                new RecyclerItemListener.RecyclerTouchListener() {
                    public void onClickItem(View v, int position) {

                        if (mListViewArray.get(position).getmFile().endsWith(".pdf")) {
                            Intent intent = new Intent(mContext, PdfReaderActivity.class);
                            intent.putExtra("pdf_url", mListViewArray.get(position).getmFile());
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(mContext, LoadUrlWebViewActivity.class);
                            intent.putExtra("url", mListViewArray.get(position).getmFile());
                            intent.putExtra("tab_type", mListViewArray.get(position).getmName());
                            mContext.startActivity(intent);
                        }
                    }

                    public void onLongClickItem(View v, int position) {
                    }
                }));
    }
    public void getList() {

        try {
            mListViewArray = new ArrayList<>();
            final VolleyWrapper manager = new VolleyWrapper(URL_GET_SPORTS_EXTERNAL_PROVIDERS);
            String[] name = new String[]{JTAG_ACCESSTOKEN};
            String[] value = new String[]{PreferenceManager.getAccessToken(mContext)};


            manager.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {

                @Override
                public void responseSuccess(String successResponse) {
                    String responsCode = "";
                    if (successResponse != null) {
                        System.out.println("Response information Sports"+successResponse);

                        try {
                            JSONObject rootObject = new JSONObject(successResponse);
                            if (rootObject.optString(JTAG_RESPONSE) != null) {
                                responsCode = rootObject.optString(JTAG_RESPONSECODE);
                                if (responsCode.equals(RESPONSE_SUCCESS)) {
                                    JSONObject respObject = rootObject.getJSONObject(JTAG_RESPONSE);
                                    String statusCode = respObject.optString(JTAG_STATUSCODE);
                                    if (statusCode.equals(STATUS_SUCCESS)) {
                                        String bannerImage = respObject.optString(JTAG_BANNER_IMAGE);
                                        JSONArray dataArray = respObject.getJSONArray(JTAG_RESPONSE_DATA_ARRAY);
                                        if (dataArray.length() > 0) {
                                            for (int i = 0; i < dataArray.length(); i++) {
                                                JSONObject dataObject = dataArray.getJSONObject(i);
                                                mListViewArray.add(getSearchValues(dataObject));

                                            }

                                            mnewsLetterListView.setAdapter(new InformationRecyclerAdapter(mContext, mListViewArray));

                                        }
                                    } else {
                                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                                    }
                                } else if (responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) ||
                                        responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                                        responsCode.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
                                    AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                                        @Override
                                        public void getAccessToken() {
                                        }
                                    });
                                    getList();

                                } else if (responsCode.equals(RESPONSE_ERROR)) {
                                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                                }
                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }

                @Override
                public void responseFailure(String failureResponse) {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    private SecondaryModel getSearchValues(JSONObject Object)
            throws JSONException {
        SecondaryModel mSecondaryModel = new SecondaryModel();
        mSecondaryModel.setmId(Object.getString(JTAG_ID));
        mSecondaryModel.setmFile(Object.getString(JTAG_TAB_FILE));
        mSecondaryModel.setmName(Object.getString(JTAG_TAB_NAME));
        if (Object.has("status"))
        {
            mSecondaryModel.setStatus(Object.getString("status"));

        }
        else
        {
            mSecondaryModel.setStatus("1");

        }
        return mSecondaryModel;
    }


}