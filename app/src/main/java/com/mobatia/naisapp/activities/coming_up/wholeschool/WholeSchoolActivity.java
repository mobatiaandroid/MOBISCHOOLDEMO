package com.mobatia.naisapp.activities.coming_up.wholeschool;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.coming_up.adapter.ComingUpRecyclerAdapter;
import com.mobatia.naisapp.activities.coming_up.coming_up_detail.ComingUpDetailWebViewActivity;
import com.mobatia.naisapp.activities.coming_up.model.ComingUpModel;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.appcontroller.AppController;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.HeaderManager;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.recyclerviewmanager.DividerItemDecoration;
import com.mobatia.naisapp.recyclerviewmanager.RecyclerItemListener;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

@SuppressLint("NewApi")
public class WholeSchoolActivity extends Activity implements
        URLConstants, JSONConstants, StatusConstants, NaisClassNameConstants {

    private Context mContext;

    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back;
    ImageView home;
    int eventPos = -1;
    ArrayList<ComingUpModel> mComingUpModelListArray = new ArrayList<ComingUpModel>();
    private RecyclerView recycler_view_list;
    ComingUpRecyclerAdapter customStaffDirectoryAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coming_up_recycler_view);

        mContext = this;
        initialiseUI();
        if (AppUtils.isNetworkConnected(mContext)) {
            callStaffDirectoryListAPI(URL_WHOLE_SCHOOL_COMING_UP_LIST);
        } else {
            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

        }

    }

    private void callStaffDirectoryListAPI(String URL) {
        VolleyWrapper volleyWrapper = new VolleyWrapper(URL);
        String[] name = {JTAG_ACCESSTOKEN, "users_id"};

        String[] value = {PreferenceManager.getAccessToken(mContext), PreferenceManager.getUserId(mContext)};
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
                            JSONArray data = secobj.getJSONArray("data");
                            if (data.length() > 0) {
                                mComingUpModelListArray.clear();
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject dataObject = data.getJSONObject(i);
                                    mComingUpModelListArray.add(addStaffDetails(dataObject));
                                }

                                customStaffDirectoryAdapter = new ComingUpRecyclerAdapter(mContext, mComingUpModelListArray);

                                recycler_view_list.setAdapter(customStaffDirectoryAdapter);
                            } else {
                                Toast.makeText(WholeSchoolActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else if (response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) ||
                            response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                            response_code.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
                        AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                            @Override
                            public void getAccessToken() {
                            }
                        });
                        callStaffDirectoryListAPI(URL_WHOLE_SCHOOL_COMING_UP_LIST);

                    } else {

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

    /*******************************************************
     * Method name : initialiseUI Description : initialise UI elements
     * Parameters : nil Return type : void Date : March 8, 2017 Author : RIJO K JOSE
     *****************************************************/
    @SuppressWarnings("deprecation")
    public void initialiseUI() {
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        recycler_view_list = (RecyclerView) findViewById(R.id.recycler_view_list);
        headermanager = new HeaderManager(WholeSchoolActivity.this, "Coming Up - Whole School");
        headermanager.getHeader(relativeHeader, 0);
        back = headermanager.getLeftButton();
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view_list.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.list_divider_teal)));
        recycler_view_list.setLayoutManager(llm);
        headermanager.setButtonLeftSelector(R.drawable.back,
                R.drawable.back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.hideKeyBoard(mContext);
                finish();
            }
        });
        home = headermanager.getLogoButton();
        home.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(mContext, HomeListAppCompatActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
            }
        });
        recycler_view_list.addOnItemTouchListener(new RecyclerItemListener(mContext, recycler_view_list,
                new RecyclerItemListener.RecyclerTouchListener() {
                    public void onClickItem(View v, int position) {
                        if (mComingUpModelListArray.get(position).getStatus().equalsIgnoreCase("0") || mComingUpModelListArray.get(position).getStatus().equalsIgnoreCase("2")) {

                            callStatusChangeApi(URL_GET_STATUS_CHANGE_API, mComingUpModelListArray.get(position).getId(), position, mComingUpModelListArray.get(position).getStatus());

                        } else {
                            String webViewComingUpDetail = "<!DOCTYPE html>\n" +
                                    "<html>\n" +
                                    "<head>\n" +
                                    "<style>\n" +
                                    "\n" +
                                    "@font-face {\n" +
                                    "font-family: SourceSansPro-Semibold;" +
                                    "src: url(SourceSansPro-Semibold.otf);" +

                                    "font-family: SourceSansPro-Regular;" +
                                    "src: url(SourceSansPro-Regular.otf);" +
                                    "}" +
                                    ".title {" +
                                    "font-family: SourceSansPro-Regular;" +
                                    "font-size:16px;" +
                                    "text-align:left;" +
                                    "color:	#46C1D0;" +
                                    "}" +

                                    ".description {" +
                                    "font-family: SourceSansPro-Light;" +
                                    "text-align:justify;" +
                                    "font-size:14px;" +
                                    "color: #000000;" +
                                    "text-align: left;" +
                                    "}" +
                                    "</style>\n" + "</head>" +
                                    "<body>" +
                                    "<p class='title'>" + mComingUpModelListArray.get(position).getTitle() + "</p>";
                            if (!mComingUpModelListArray.get(position).getImage().equalsIgnoreCase("")) {
                                webViewComingUpDetail = webViewComingUpDetail + "<center><img src='" + mComingUpModelListArray.get(position).getImage() + "'width='100%', height='auto'>";
                            }
                            webViewComingUpDetail = webViewComingUpDetail + "<p class='description'>" + mComingUpModelListArray.get(position).getDescription() + "</p>" +
                                    "</body>\n</html>";
                            Intent mIntent = new Intent(mContext, ComingUpDetailWebViewActivity.class);
                            mIntent.putExtra("webViewComingDetail", webViewComingUpDetail);
                            mIntent.putExtra("tab_type", "Coming Up - Whole School");

                            startActivity(mIntent);
                        }
                    }

                    public void onLongClickItem(View v, int position) {
                        System.out.println("On Long Click Item interface");
                    }
                }));

    }

    private ComingUpModel addStaffDetails(JSONObject obj) {
        ComingUpModel model = new ComingUpModel();
        try {
            model.setTitle(obj.optString(JTAG_TITLE));
            model.setDescription(obj.optString(JTAG_DESCRIPTION));
            model.setDate(obj.optString(JTAG_DATE));
            model.setImage(obj.optString(JTAG_IMAGE));
            model.setId(obj.optString("id"));
            model.setStatus(obj.optString("status"));
        } catch (Exception ex) {
            System.out.println("Exception is" + ex);
        }

        return model;
    }

    private void callStatusChangeApi(final String URL, final String id, final int eventPosition, final String status) {
        VolleyWrapper volleyWrapper = new VolleyWrapper(URL);
        String[] name = {"access_token", "users_id", "id", "type"};
        String[] value = {PreferenceManager.getAccessToken(mContext), PreferenceManager.getUserId(mContext), id, "whole_school_coming_up"};
        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("The response is status change" + successResponse);
                try {
                    JSONObject obj = new JSONObject(successResponse);
                    String response_code = obj.getString(JTAG_RESPONSECODE);
                    if (response_code.equalsIgnoreCase("200")) {
                        JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                        String status_code = secobj.getString(JTAG_STATUSCODE);
                        if (status_code.equalsIgnoreCase("303")) {

                            AppController.isCommunicationDetailVisited=true;
                            if (status.equalsIgnoreCase("0") || status.equalsIgnoreCase("2")) {

                                String webViewComingUpDetail = "<!DOCTYPE html>\n" +
                                        "<html>\n" +
                                        "<head>\n" +
                                        "<style>\n" +
                                        "\n" +
                                        "@font-face {\n" +
                                        "font-family: SourceSansPro-Semibold;" +
                                        "src: url(SourceSansPro-Semibold.otf);" +

                                        "font-family: SourceSansPro-Regular;" +
                                        "src: url(SourceSansPro-Regular.otf);" +
                                        "}" +
                                        ".title {" +
                                        "font-family: SourceSansPro-Regular;" +
                                        "font-size:16px;" +
                                        "text-align:left;" +
                                        "color:	#46C1D0;" +
                                        "}" +

                                        ".description {" +
                                        "font-family: SourceSansPro-Light;" +
                                        "text-align:justify;" +
                                        "font-size:14px;" +
                                        "color: #000000;" +
                                        "text-align: left;" +
                                        "}" +
                                        "</style>\n" + "</head>" +
                                        "<body>" +
                                        "<p class='title'>" + mComingUpModelListArray.get(eventPosition).getTitle() + "</p>";
                                if (!mComingUpModelListArray.get(eventPosition).getImage().equalsIgnoreCase("")) {
                                    webViewComingUpDetail = webViewComingUpDetail + "<center><img src='" + mComingUpModelListArray.get(eventPosition).getImage() + "'width='100%', height='auto'>";
                                }
                                webViewComingUpDetail = webViewComingUpDetail + "<p class='description'>" + mComingUpModelListArray.get(eventPosition).getDescription() + "</p>" +
                                        "</body>\n</html>";
                                Intent mIntent = new Intent(mContext, ComingUpDetailWebViewActivity.class);
                                mIntent.putExtra("webViewComingDetail", webViewComingUpDetail);
                                mIntent.putExtra("tab_type", "Coming Up - Whole School");

                                startActivity(mIntent);
                            }
                        }
                    } else if (response_code.equalsIgnoreCase("500")) {

                    } else if (response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) ||
                            response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                            response_code.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
                        AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                            @Override
                            public void getAccessToken() {

                            }
                        });
                        callStatusChangeApi(URL, id, eventPosition, status);


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

    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }

    @Override
    protected void onRestart() {
        super.onRestart();

		if(AppUtils.isNetworkConnected(mContext)) {
			callStaffDirectoryListAPI(URL_WHOLE_SCHOOL_COMING_UP_LIST);
		}else{
			AppUtils.showDialogAlertDismiss((Activity)mContext,"Network Error",getString(R.string.no_internet),R.drawable.nonetworkicon,R.drawable.roundred);

		}
    }
}
