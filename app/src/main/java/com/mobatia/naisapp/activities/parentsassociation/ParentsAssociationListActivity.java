package com.mobatia.naisapp.activities.parentsassociation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.activities.parentsassociation.adapter.ParentsAssociationMainRecyclerviewAdapter;
import com.mobatia.naisapp.activities.parentsassociation.model.ParentAssociationEventItemsModel;
import com.mobatia.naisapp.activities.parentsassociation.model.ParentAssociationEventsModel;
import com.mobatia.naisapp.activities.web_view.FullscreenWebViewActivityNoHeader;
import com.mobatia.naisapp.constants.IntentPassValueConstants;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NameValueConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.HeaderManager;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by gayatri on 23/3/17.
 */
public class ParentsAssociationListActivity extends Activity implements URLConstants, StatusConstants, JSONConstants,IntentPassValueConstants, NameValueConstants {
     Context mContext=this;
    String tab_type;
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back,fbShare,infoImg,home;
    static RecyclerView mRecyclerView;
    Bundle extras;
    static ArrayList<ParentAssociationEventsModel> mListViewArray;
    String myFormatCalender = "yyyy-MM-dd";
    static SimpleDateFormat sdfcalender;
    static GetPtaItemList PtaItemList;
    static ArrayList<ParentAssociationEventsModel> mParentAssociationEventsModelsArrayList;
    static String facebookurl="";
    public interface GetPtaItemList {
        public void getPtaItemData();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parentsactivityrecyclerview);
        mContext = this;
        myFormatCalender = "yyyy-MM-dd";
        sdfcalender = new SimpleDateFormat(myFormatCalender);
        initialiseUI();
//        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "Work In Progress", R.drawable.exclamationicon, R.drawable.round);
        if(PreferenceManager.getIsFirstTimeInPA(mContext)){
            PreferenceManager.setIsFirstTimeInPA(mContext,false);
            Intent mintent = new Intent(mContext, PTAinfoActivity.class);
            mintent.putExtra(TYPE, 1);
            startActivity(mintent);
        }
        else
        {
            if (AppUtils.isNetworkConnected(mContext)) {
                callListApi();
            } else {
                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

            }
        }

    }

    private void initialiseUI() {
        extras = getIntent().getExtras();
        if (extras != null) {
            tab_type = extras.getString("tab_type");
        }
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        headermanager = new HeaderManager(ParentsAssociationListActivity.this, tab_type);
        headermanager.getHeader(relativeHeader, 3);
        back = headermanager.getLeftButton();
        fbShare=headermanager.getRightButton();
        infoImg=headermanager.getRightInfoImage();
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
        fbShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mintent = new Intent(mContext, FullscreenWebViewActivityNoHeader.class);
                mintent.putExtra("url", facebookurl);
                startActivity(mintent);
            }
        });
        infoImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mintent = new Intent(mContext, PTAinfoActivity.class);
                startActivity(mintent);
            }
        });

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

//        mRecyclerView.addOnItemTouchListener(new RecyclerItemListener(getApplicationContext(), mRecyclerView,
//                new RecyclerItemListener.RecyclerTouchListener() {
//                    public void onClickItem(View v, int position) {
//
//                    }
//
//                    public void onLongClickItem(View v, int position) {
//                        System.out.println("On Long Click Item interface");
//                    }
//                }));
    }

    public  void callListApi() {
        try {
            mListViewArray = new ArrayList<ParentAssociationEventsModel>();
            final VolleyWrapper manager = new VolleyWrapper(URL_GET_PARENT_ASSOCIATION_EVENT_LIST);
            String[] name = new String[]{JTAG_ACCESSTOKEN,JTAG_USERS_ID};
            String[] value = new String[]{PreferenceManager.getAccessToken(mContext),PreferenceManager.getUserId(mContext)};
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
                                     facebookurl = respObject.optString(JTAG_FACEBOOK_URL);
                                    if(!facebookurl.equals("")){
                                        headermanager.setButtonRightSelector(R.drawable.facebookshare,R.drawable.facebookshare);
                                    }
                                    else{
                                        fbShare.setVisibility(View.GONE);
                                    }
                                    if (statusCode.equals(STATUS_SUCCESS)) {
                                        JSONArray dataArray = respObject.getJSONArray(JTAG_RESPONSE_DATA_ARRAY);
                                        if (dataArray.length() > 0) {
                                            for (int i = 0; i < dataArray.length(); i++) {
                                                JSONObject eventJSONdata = dataArray.optJSONObject(i);
                                                mListViewArray.add(getParentAssociationEventsValues(eventJSONdata));
                                            }
                                            ParentsAssociationMainRecyclerviewAdapter mParentsAssociationMainRecyclerviewAdapter = new ParentsAssociationMainRecyclerviewAdapter(mContext, mListViewArray);
                                            mRecyclerView.setAdapter(mParentsAssociationMainRecyclerviewAdapter);

                                        } else {
                                            //CustomStatusDialog();
                                            Toast.makeText(mContext, "No Data Available", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
//										CustomStatusDialog(RESPONSE_FAILURE);
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
                                    callListApi();

                                }
                            } else {
//								CustomStatusDialog(RESPONSE_FAILURE);
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

    private  ParentAssociationEventsModel getParentAssociationEventsValues(JSONObject mEventJSONdata)
            throws JSONException {
        ParentAssociationEventsModel mParentAssociationEventsModel = new ParentAssociationEventsModel();
        mParentAssociationEventsModel.setEvenId(mEventJSONdata.optString("even_id"));
        mParentAssociationEventsModel.setEventName(mEventJSONdata.optString("event"));
        mParentAssociationEventsModel.setEventDate(mEventJSONdata.optString("date"));
        mParentAssociationEventsModel.setPosition(0);
        String mDate = mParentAssociationEventsModel.getEventDate();
        Date mEventDate = new Date();

        try {
            mEventDate = sdfcalender.parse(mDate);
        } catch (ParseException ex) {
            Log.e("Date", "Parsing error");
        }
        String dayOfTheWeek = (String) DateFormat.format("EEEE", mEventDate); // Thursday
        String day = (String) DateFormat.format("dd", mEventDate); // 20
        String monthString = (String) DateFormat.format("MMMM", mEventDate); // June
        String monthNumber = (String) DateFormat.format("MM", mEventDate); // 06
        String year = (String) DateFormat.format("yyyy", mEventDate); // 2013
        mParentAssociationEventsModel.setDayOfTheWeek(dayOfTheWeek);
        mParentAssociationEventsModel.setDay(day);
        mParentAssociationEventsModel.setMonthString(monthString);
        mParentAssociationEventsModel.setMonthNumber(monthNumber);
        mParentAssociationEventsModel.setYear(year);
        ArrayList<ParentAssociationEventsModel> mParentAssociationItemsArrayList = new ArrayList<>();

        JSONArray itemData = mEventJSONdata.optJSONArray("items");

        for (int i = 0; i < itemData.length(); i++) {
            JSONObject mItemJsonObject = itemData.optJSONObject(i);
            ParentAssociationEventsModel mItemModel = new ParentAssociationEventsModel();
            mItemModel.setItemName(mItemJsonObject.optString("name"));
//            mItemModel.setItemSelected(false);
//            if (i==0)
//            {
//                mItemModel.setItemSelected(true);
//            }
            JSONArray itemDataEvent = mItemJsonObject.optJSONArray("timeslots");
            ArrayList<ParentAssociationEventItemsModel> mParentAssociationEventItemsArraList = new ArrayList<>();
            for (int j = 0; j < itemDataEvent.length(); j++) {
                JSONObject mEventItemJsonObject = itemDataEvent.optJSONObject(j);
                ParentAssociationEventItemsModel mParentAssociationEventItemsModel = new ParentAssociationEventItemsModel();
                mParentAssociationEventItemsModel.setUserName(mEventItemJsonObject.optString("user_name"));
                mParentAssociationEventItemsModel.setStatus(mEventItemJsonObject.optString("status"));
                mParentAssociationEventItemsModel.setEventId(mEventItemJsonObject.optString("id"));
                mParentAssociationEventItemsModel.setStart_time(mEventItemJsonObject.optString("start_time"));
                mParentAssociationEventItemsModel.setEnd_time(mEventItemJsonObject.optString("end_time"));
                java.text.DateFormat format1 = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
                try {
                    Date dateStart = format1.parse(mEventItemJsonObject.optString("start_time"));
                    SimpleDateFormat format2 = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
                    String startTime = format2.format(dateStart);
                    mParentAssociationEventItemsModel.setFrom_time(startTime);
                    Date dateEndTime = format1.parse(mEventItemJsonObject.optString("end_time"));
                    String endTime = format2.format(dateEndTime);
                    mParentAssociationEventItemsModel.setTo_time(endTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                mParentAssociationEventItemsArraList.add(mParentAssociationEventItemsModel);
            }
            mItemModel.setEventItemStatusList(mParentAssociationEventItemsArraList);
            mParentAssociationItemsArrayList.add(mItemModel);
        }

        mParentAssociationEventsModel.setEventItemList(mParentAssociationItemsArrayList);
        return mParentAssociationEventsModel;
    }
    private static ParentAssociationEventsModel getParentAssociationEventValues(
            JSONObject mEventJSONdata, int postion)
            throws JSONException {
        ParentAssociationEventsModel mParentAssociationEventsModel = new ParentAssociationEventsModel();
        mParentAssociationEventsModel.setEvenId(mEventJSONdata.optString("even_id"));
        mParentAssociationEventsModel.setEventName(mEventJSONdata.optString("event"));
        mParentAssociationEventsModel.setEventDate(mEventJSONdata.optString("date"));
        mParentAssociationEventsModel.setPosition(postion);
        String mDate = mParentAssociationEventsModel.getEventDate();
        Date mEventDate = new Date();

        try {
            mEventDate = sdfcalender.parse(mDate);
        } catch (ParseException ex) {
            Log.e("Date", "Parsing error");
        }
        String dayOfTheWeek = (String) DateFormat.format("EEEE", mEventDate); // Thursday
        String day = (String) DateFormat.format("dd", mEventDate); // 20
        String monthString = (String) DateFormat.format("MMMM", mEventDate); // June
        String monthNumber = (String) DateFormat.format("MM", mEventDate); // 06
        String year = (String) DateFormat.format("yyyy", mEventDate); // 2013
        mParentAssociationEventsModel.setDayOfTheWeek(dayOfTheWeek);
        mParentAssociationEventsModel.setDay(day);
        mParentAssociationEventsModel.setMonthString(monthString);
        mParentAssociationEventsModel.setMonthNumber(monthNumber);
        mParentAssociationEventsModel.setYear(year);
        ArrayList<ParentAssociationEventsModel> mParentAssociationItemsArrayList = new ArrayList<>();
        JSONArray itemData = mEventJSONdata.optJSONArray("items");

        for (int i = 0; i < itemData.length(); i++) {
            JSONObject mItemJsonObject = itemData.optJSONObject(i);
            ParentAssociationEventsModel mItemModel = new ParentAssociationEventsModel();
            mItemModel.setItemName(mItemJsonObject.optString("name"));
//            mItemModel.setItemSelected(false);
//            if (i==0)
//            {
//                mItemModel.setItemSelected(true);
//            }
            JSONArray itemDataEvent = mItemJsonObject.optJSONArray("timeslots");
            ArrayList<ParentAssociationEventItemsModel> mParentAssociationEventItemsArraList = new ArrayList<>();
            for (int j = 0; j < itemDataEvent.length(); j++) {
                JSONObject mEventItemJsonObject = itemDataEvent.optJSONObject(j);
                ParentAssociationEventItemsModel mParentAssociationEventItemsModel = new ParentAssociationEventItemsModel();
                mParentAssociationEventItemsModel.setUserName(mEventItemJsonObject.optString("user_name"));
                mParentAssociationEventItemsModel.setStatus(mEventItemJsonObject.optString("status"));
                mParentAssociationEventItemsModel.setStart_time(mEventItemJsonObject.optString("start_time"));
                mParentAssociationEventItemsModel.setEnd_time(mEventItemJsonObject.optString("end_time"));
                mParentAssociationEventItemsModel.setEventId(mEventItemJsonObject.optString("id"));

                java.text.DateFormat format1 = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
                try {
                    Date dateStart = format1.parse(mEventItemJsonObject.optString("start_time"));
                    SimpleDateFormat format2 = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
                    String startTime = format2.format(dateStart);
                    mParentAssociationEventItemsModel.setFrom_time(startTime);
                    Date dateEndTime = format1.parse(mEventItemJsonObject.optString("end_time"));
                    String endTime = format2.format(dateEndTime);
                    mParentAssociationEventItemsModel.setTo_time(endTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                mParentAssociationEventItemsArraList.add(mParentAssociationEventItemsModel);
            }
            mItemModel.setEventItemStatusList(mParentAssociationEventItemsArraList);
            mParentAssociationItemsArrayList.add(mItemModel);
        }

        mParentAssociationEventsModel.setEventItemList(mParentAssociationItemsArrayList);
        return mParentAssociationEventsModel;
    }

    public static void callListApis(final Context context,ArrayList<ParentAssociationEventsModel> parentAssociationEventsModelsArrayList, GetPtaItemList mPtaItemList) {
        try {
            PtaItemList=mPtaItemList;
            mListViewArray = new ArrayList<ParentAssociationEventsModel>();
            mParentAssociationEventsModelsArrayList = parentAssociationEventsModelsArrayList;
            final VolleyWrapper manager = new VolleyWrapper(URL_GET_PARENT_ASSOCIATION_EVENT_LIST);
            String[] name = new String[]{JTAG_ACCESSTOKEN,JTAG_USERS_ID};
            String[] value = new String[]{PreferenceManager.getAccessToken(context),PreferenceManager.getUserId(context)};
            manager.getResponsePOST(context, 11, name, value, new VolleyWrapper.ResponseListener() {

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
                                    facebookurl = respObject.optString(JTAG_FACEBOOK_URL);

                                    if (statusCode.equals(STATUS_SUCCESS)) {
                                        JSONArray dataArray = respObject.getJSONArray(JTAG_RESPONSE_DATA_ARRAY);
                                        if (dataArray.length() > 0) {
                                            for (int i = 0; i < dataArray.length(); i++) {
                                                JSONObject eventJSONdata = dataArray.optJSONObject(i);
                                                mListViewArray.add(getParentAssociationEventValues(eventJSONdata,mParentAssociationEventsModelsArrayList.get(i).getPosition()));
                                            }
                                            ParentsAssociationMainRecyclerviewAdapter mParentsAssociationMainRecyclerviewAdapter = new ParentsAssociationMainRecyclerviewAdapter(context, mListViewArray);
                                            mRecyclerView.setAdapter(mParentsAssociationMainRecyclerviewAdapter);

                                        } else {
                                            //CustomStatusDialog();
                                            Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
//										CustomStatusDialog(RESPONSE_FAILURE);
                                        Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show();
                                    }
                                } else if (responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) ||
                                        responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                                        responsCode.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
                                    AppUtils.postInitParam(context, new AppUtils.GetAccessTokenInterface() {
                                        @Override
                                        public void getAccessToken() {
                                        }
                                    });
                                    callListApis(context, mParentAssociationEventsModelsArrayList ,PtaItemList);

                                }
                            } else if (responsCode.equals(RESPONSE_ERROR)) {
//								CustomStatusDialog(RESPONSE_FAILURE);
                                Toast.makeText(context, "Failure", Toast.LENGTH_SHORT).show();

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

    @Override
    protected void onRestart() {
        super.onRestart();
        if (AppUtils.isNetworkConnected(mContext)) {
            callListApi();
        } else {
            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

        }
    }
}