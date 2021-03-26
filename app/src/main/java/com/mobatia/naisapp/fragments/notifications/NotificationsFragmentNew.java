/**
 *
 */
package com.mobatia.naisapp.fragments.notifications;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.iid.FirebaseInstanceId;
import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.activities.notification_new.AudioPlayerViewActivityNew;
import com.mobatia.naisapp.activities.notification_new.ImageActivityNew;
import com.mobatia.naisapp.activities.notification_new.TextalertActivityNew;
import com.mobatia.naisapp.activities.notification_new.VideoWebViewActivityNew;
import com.mobatia.naisapp.activities.notifications.AudioPlayerViewActivity;
import com.mobatia.naisapp.activities.notifications.ImageActivity;
import com.mobatia.naisapp.activities.notifications.TextalertActivity;
import com.mobatia.naisapp.activities.notifications.VideoWebViewActivity;
import com.mobatia.naisapp.constants.CacheDIRConstants;
import com.mobatia.naisapp.constants.IntentPassValueConstants;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.constants.NaisTabConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.notifications.adapter.PushNotificationListAdapter;
import com.mobatia.naisapp.fragments.notifications.adapter.PushNotificationRecyclerAdapter;
import com.mobatia.naisapp.fragments.notifications.model.PushNotificationModel;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.recyclerviewmanager.ItemOffsetDecoration;
import com.mobatia.naisapp.recyclerviewmanager.OnBottomReachedListener;
import com.mobatia.naisapp.recyclerviewmanager.RecyclerItemListener;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * @author Rijo K Jose
 */

public class NotificationsFragmentNew extends Fragment implements
        NaisTabConstants, CacheDIRConstants, URLConstants,
        IntentPassValueConstants, NaisClassNameConstants, JSONConstants, StatusConstants {
    TextView mTitleTextView;

    private View mRootView;
    private Context mContext;
    private RecyclerView pushListView;
    private String mTitle;
    private String mTabId;
    private RelativeLayout relMain;
    private ImageView mBannerImage;
    ArrayList<PushNotificationModel> pushNotificationArrayList;
    ArrayList<PushNotificationModel> jsonArrayList;
    PushNotificationRecyclerAdapter mPushNotificationListAdapter;
    Intent mIntent;
    RecyclerView.LayoutManager recyclerViewLayoutManager;
    String myFormatCalender = "yyyy-MM-dd HH:mm:ss";
    static SimpleDateFormat sdfcalender;
    String scrollTo="";
    String apiID="";
    boolean isFromBottom=false;
    int notificationSize=0;
    LinearLayoutManager mUnRead;
    SwipeRefreshLayout swipeRefreshLayout;
    public NotificationsFragmentNew() {

    }

    public NotificationsFragmentNew(String title, String tabId) {
        this.mTitle = title;
        this.mTabId = tabId;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * android.support.v4.app.Fragment#onCreateOptionsMenu(android.view.Menu,
     * android.view.MenuInflater)
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_notification_list, container,
                false);
//		setHasOptionsMenu(true);
        mContext = getActivity();
        pushNotificationArrayList = new ArrayList<PushNotificationModel>();
        myFormatCalender = "yyyy-MM-dd HH:mm:ss";
        sdfcalender = new SimpleDateFormat(myFormatCalender, Locale.ENGLISH);
//		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(mContext));
        ShortcutBadger.applyCount(mContext, 0);//badge
        initialiseUI();
        if (AppUtils.isNetworkConnected(mContext)) {
            clearBadge();
            scrollTo="";
            apiID="";
            isFromBottom=false;
            callPushNotification(apiID,scrollTo);
            getClearBadge();
            getBadge();
        } else
            {
            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
            }
        return mRootView;
    }

    /*******************************************************
     * Method name : initialiseUI Description : initialise UI elements
     * Parameters : nil Return type : void Date : Jan 12, 2015 Author : Vandana
     * Surendranath
     *****************************************************/
    private void initialiseUI() {
        mTitleTextView = (TextView) mRootView.findViewById(R.id.titleTextView);
        mTitleTextView.setText(NOTIFICATIONS);
        pushListView = (RecyclerView) mRootView.findViewById(R.id.pushListView);
        pushListView.setHasFixedSize(true);
        mUnRead = new LinearLayoutManager(mContext);
        mUnRead.setOrientation(LinearLayoutManager.VERTICAL);
        pushListView.setLayoutManager(mUnRead);
        relMain = (RelativeLayout) mRootView.findViewById(R.id.relMain);
        relMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        pushListView.addOnItemTouchListener(new RecyclerItemListener(mContext, pushListView,
                new RecyclerItemListener.RecyclerTouchListener()
                {
                    public void onClickItem(View v, int position) {
                        if(pushNotificationArrayList.get(position).getStatus().equalsIgnoreCase("0")||pushNotificationArrayList.get(position).getStatus().equalsIgnoreCase("2"))
                        {
                            callStatusChangeApi(URL_GET_STATUS_CHANGE_API,pushNotificationArrayList.get(position).getId(),position,pushNotificationArrayList.get(position).getStatus());

                        }
                        if (pushNotificationArrayList.get(position).getPushType().equalsIgnoreCase("")) {
                            mIntent = new Intent(mContext, TextalertActivityNew.class);
                            mIntent.putExtra(POSITION, position);
                            mIntent.putExtra("PushID", pushNotificationArrayList.get(position).getId());
                            mIntent.putExtra("Day",pushNotificationArrayList.get(position).getDay());
                            mIntent.putExtra("Month",pushNotificationArrayList.get(position).getMonthString());
                            mIntent.putExtra("Year",pushNotificationArrayList.get(position).getYear());
                            mIntent.putExtra("PushDate",pushNotificationArrayList.get(position).getPushTime());
                            mIntent.putExtra("title",pushNotificationArrayList.get(position).getHeadTitle());
                            mContext.startActivity(mIntent);
                        }
                        if (pushNotificationArrayList.get(position).getPushType().equalsIgnoreCase("image")||pushNotificationArrayList.get(position).getPushType().equalsIgnoreCase("text")||pushNotificationArrayList.get(position).getPushType().equalsIgnoreCase("Text")||pushNotificationArrayList.get(position).getPushType().equalsIgnoreCase("Image")) {
                            mIntent = new Intent(mContext, ImageActivityNew.class);
                            mIntent.putExtra("PushID", pushNotificationArrayList.get(position).getId());
                            mIntent.putExtra("Day",pushNotificationArrayList.get(position).getDay());
                            mIntent.putExtra("Month",pushNotificationArrayList.get(position).getMonthString());
                            mIntent.putExtra("Year",pushNotificationArrayList.get(position).getYear());
                            mIntent.putExtra("PushDate",pushNotificationArrayList.get(position).getPushTime());
                            mIntent.putExtra("title",pushNotificationArrayList.get(position).getHeadTitle());
                            mContext.startActivity(mIntent);
                        }
                        if (pushNotificationArrayList.get(position).getPushType().equalsIgnoreCase("audio")) {
                            mIntent = new Intent(mContext, AudioPlayerViewActivityNew.class);
                            mIntent.putExtra("PushID", pushNotificationArrayList.get(position).getId());
                            mIntent.putExtra("Day",pushNotificationArrayList.get(position).getDay());
                            mIntent.putExtra("Month",pushNotificationArrayList.get(position).getMonthString());
                            mIntent.putExtra("Year",pushNotificationArrayList.get(position).getYear());
                            mIntent.putExtra("PushDate",pushNotificationArrayList.get(position).getPushTime());
                            mIntent.putExtra("title",pushNotificationArrayList.get(position).getHeadTitle());
                            mContext.startActivity(mIntent);
                        }
                        if (pushNotificationArrayList.get(position).getPushType().equalsIgnoreCase("video")) {
                            mIntent = new Intent(mContext, VideoWebViewActivityNew.class);
                            mIntent.putExtra("PushID", pushNotificationArrayList.get(position).getId());
                            mIntent.putExtra("Day",pushNotificationArrayList.get(position).getDay());
                            mIntent.putExtra("Month",pushNotificationArrayList.get(position).getMonthString());
                            mIntent.putExtra("Year",pushNotificationArrayList.get(position).getYear());
                            mIntent.putExtra("PushDate",pushNotificationArrayList.get(position).getPushTime());
                            mIntent.putExtra("title",pushNotificationArrayList.get(position).getHeadTitle());
                            mContext.startActivity(mIntent);
                        }
                    }

                    public void onLongClickItem(View v, int position) {
                    }
                }));
        swipeRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.simpleSwipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // cancel the Visual indication of a refresh
                swipeRefreshLayout.setRefreshing(false);
              //  shuffleItems();

                isFromBottom=false;
                scrollTo="";
                int listSize=pushNotificationArrayList.size();
                if (pushNotificationArrayList.size()>0)
                {
                    //apiID=pushNotificationArrayList.get(0).getId();
                    apiID="";
                }
                else {
                    apiID="";
                }

                System.out.println("Page From Value"+apiID);
                if (AppUtils.isNetworkConnected(mContext)) {
                    pushNotificationArrayList=new ArrayList<>();
                    callPushNotification(apiID,scrollTo);
                } else {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
                }
            }
        });
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget
     * .AdapterView, android.view.View, int, long)
     */



    private PushNotificationModel getSearchValues(JSONObject Object)
            throws JSONException {
        PushNotificationModel mPushNotificationModel = new PushNotificationModel();
        mPushNotificationModel.setPushType(Object.optString("alert_type"));
        mPushNotificationModel.setPushDate(Object.optString("time_Stamp"));
        mPushNotificationModel.setId(Object.optString("id"));
        mPushNotificationModel.setStatus(Object.optString("read_unread_status"));
        mPushNotificationModel.setHeadTitle(Object.optString("title"));

        String mDate = mPushNotificationModel.getPushDate();
        Date mEventDate = new Date();

        try {
            mEventDate = sdfcalender.parse(mDate);
            SimpleDateFormat format2 = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
            String startTime = format2.format(mEventDate);
            mPushNotificationModel.setPushTime(startTime);
            ;
        } catch (ParseException ex) {
            Log.e("Date", "Parsing error");
        }
        String dayOfTheWeek = (String) DateFormat.format("EEEE", mEventDate); // Thursday
        String day = (String) DateFormat.format("dd", mEventDate); // 20
        String monthString = (String) DateFormat.format("MMM", mEventDate); // June
        String monthNumber = (String) DateFormat.format("MM", mEventDate); // 06
        String year = (String) DateFormat.format("yyyy", mEventDate); // 2013
        mPushNotificationModel.setDayOfTheWeek(dayOfTheWeek);
        mPushNotificationModel.setDay(day);
        mPushNotificationModel.setMonthString(monthString);
        mPushNotificationModel.setMonthNumber(monthNumber);
        mPushNotificationModel.setYear(year);
        System.out.println("It works till end");
        return mPushNotificationModel;
    }

    private void callPushNotification(String id,String scrollto) {


        notificationSize=0;
        try {

            final VolleyWrapper manager = new VolleyWrapper(URL_GET_NOTICATIONS_LIST_V1);
            String[] name = {JTAG_ACCESSTOKEN, JTAG_DEVICE_iD, JTAG_DEVICE_tYPE, JTAG_USERS_ID,"scroll_to","page_from"};
            String[] value = {PreferenceManager.getAccessToken(mContext), FirebaseInstanceId.getInstance().getToken(), "2", PreferenceManager.getUserId(mContext),scrollto,id};
            manager.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {

                @Override
                public void responseSuccess(String successResponse) {
                    System.out.println("NofifyRes:" + successResponse);

                    if (successResponse != null) {
                        try {
                            JSONObject rootObject = new JSONObject(successResponse);
                            String responseCode = rootObject.getString(JTAG_RESPONSECODE);
                            if (responseCode.equalsIgnoreCase(RESPONSE_SUCCESS)) {
                                JSONObject responseObject = rootObject.optJSONObject(JTAG_RESPONSE);
                                String statusCode = responseObject.getString(JTAG_STATUSCODE);
                                if (statusCode.equalsIgnoreCase(STATUS_SUCCESS)) {
                                    JSONArray dataArray = responseObject.getJSONArray(JTAG_RESPONSE_DATA_ARRAY);
                                    if (dataArray.length() > 0) {
                                        notificationSize=dataArray.length();
                                        for (int i = 0; i < dataArray.length(); i++) {
                                            //String statusCode=responseArray.get(i)
                                            JSONObject dataObject = dataArray.getJSONObject(i);
                                            pushNotificationArrayList.add(getSearchValues(dataObject));
                                        }
                                        if (pushNotificationArrayList.size()>0)
                                        {
                                            mPushNotificationListAdapter = new PushNotificationRecyclerAdapter(mContext, pushNotificationArrayList);
                                            pushListView.setAdapter(mPushNotificationListAdapter);
                                            if (isFromBottom)
                                            {
                                                pushListView.scrollToPosition(pushNotificationArrayList.size()-notificationSize-2);
                                            }
                                            else
                                            {
                                                pushListView.scrollToPosition(0);
                                            }
                                            mPushNotificationListAdapter.setOnBottomReachedListener(new OnBottomReachedListener()
                                            {
                                                @Override
                                                public void onBottomReached(int position) {
                                                    System.out.println("reachedbottom");
                                                    isFromBottom=true;
                                                    scrollTo="old";
                                                    int listSize=pushNotificationArrayList.size();
                                                    apiID=pushNotificationArrayList.get(listSize-1).getId();
                                                    System.out.println("Page From Value"+apiID);
                                                    if (notificationSize==15)
                                                    {
                                                        if (AppUtils.isNetworkConnected(mContext)) {
                                                            callPushNotification(apiID,scrollTo);
                                                        } else {
                                                            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
                                                        }
                                                    }


                                                }
                                            });
                                        }
                                        else
                                        {
                                            Toast.makeText(mContext, "No Notifications Found", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                       // Toast.makeText(mContext, "No Notifications Found", Toast.LENGTH_SHORT).show();
                                    }
                                } else if (statusCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                                        statusCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) ||
                                        statusCode.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
                                    AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                                        @Override
                                        public void getAccessToken() {
                                        }
                                    });
                                    callPushNotification(id,scrollto);

                                }
                            } else if (responseCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                                    responseCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) ||
                                    responseCode.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
                                AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                                    @Override
                                    public void getAccessToken() {
                                    }
                                });
                                callPushNotification(id,scrollto);

                            } else {
                                Toast.makeText(mContext, "Some Error Occured", Toast.LENGTH_SHORT).show();

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

    private void clearBadge() {
        pushNotificationArrayList = new ArrayList<PushNotificationModel>();

        try {

            final VolleyWrapper manager = new VolleyWrapper(URL_CLEAR_BADGE);
            String[] name = {JTAG_ACCESSTOKEN, JTAG_USERS_ID};
            String[] value = {PreferenceManager.getAccessToken(mContext), PreferenceManager.getUserId(mContext)};
            manager.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {

                @Override
                public void responseSuccess(String successResponse) {

                    System.out.println("NofifyRes:" + successResponse);

                    if (successResponse != null) {
                        try {
                            JSONObject rootObject = new JSONObject(successResponse);
                            String responseCode = rootObject.getString(JTAG_RESPONSECODE);
                            if (responseCode.equalsIgnoreCase(RESPONSE_SUCCESS)) {
                                JSONObject responseObject = rootObject.optJSONObject(JTAG_RESPONSE);
                                String statusCode = responseObject.getString(JTAG_STATUSCODE);
                                if (statusCode.equalsIgnoreCase(STATUS_SUCCESS)) {

                                } else if (statusCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                                        statusCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING)) {
                                    AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                                        @Override
                                        public void getAccessToken() {
                                        }
                                    });
                                    clearBadge();

                                }
                            } else if (responseCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                                    responseCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) ||
                                    responseCode.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
                                AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                                    @Override
                                    public void getAccessToken() {
                                    }
                                });
                                clearBadge();

                            } else {
                                Toast.makeText(mContext, "Some Error Occured", Toast.LENGTH_SHORT).show();

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
    private void callStatusChangeApi(String URL, final String id, final int eventPosition, final String status) {
        VolleyWrapper volleyWrapper = new VolleyWrapper(URL);
        String[] name = {"access_token","users_id","id","type"};
        String[] value = {PreferenceManager.getAccessToken(mContext),PreferenceManager.getUserId(mContext),id,"notification"};
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
                            if(status.equalsIgnoreCase("0")||status.equalsIgnoreCase("2"))
                            {
                                pushNotificationArrayList.get(eventPosition).setStatus("1");
                                mPushNotificationListAdapter.notifyDataSetChanged();

                            }
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
    public void getBadge()
    {

        try {
            final VolleyWrapper manager = new VolleyWrapper(URL_GET_USER_BADGE_COUNT);
            String[] name = new String[]{JTAG_ACCESSTOKEN,"users_id"};
            String[] value = new String[]{PreferenceManager.getAccessToken(mContext),PreferenceManager.getUserId(mContext)};
            manager.getResponsePOST(mContext, 14, name, value, new VolleyWrapper.ResponseListener() {

                @Override
                public void responseSuccess(String successResponse) {
                    String responsCode = "";
                    if (successResponse != null) {
                        try {
                            JSONObject rootObject = new JSONObject(successResponse);
                            System.out.println("The response is badge count" + successResponse);
                            if (rootObject.optString(JTAG_RESPONSE) != null) {
                                responsCode = rootObject.optString(JTAG_RESPONSECODE);
                                if (responsCode.equals(RESPONSE_SUCCESS)) {
                                    JSONObject respObject = rootObject.getJSONObject(JTAG_RESPONSE);
                                    String statusCode = respObject.optString(JTAG_STATUSCODE);
                                    if (statusCode.equals(STATUS_SUCCESS)) {
                                        String calendar_badge=respObject.optString("calendar_badge");
                                        PreferenceManager.setCalendarBadge(mContext,calendar_badge);
                                        String notification_badge=respObject.optString("notification_badge");
                                        PreferenceManager.setNotificationBadge(mContext,notification_badge);
                                        String whole_school_coming_up_badge=respObject.optString("whole_school_coming_up_badge");
                                        PreferenceManager.setNoticeBadge(mContext,whole_school_coming_up_badge);
                                        String sports_badge=respObject.optString("sports_badge");
                                        PreferenceManager.setSportsBadge(mContext,sports_badge);
                                        String reports_badge=respObject.optString("reports_badge");
                                        PreferenceManager.setReportsBadge(mContext,reports_badge);
                                        String cca_badge=respObject.optString("cca_badge");
                                        PreferenceManager.setCcaBadge(mContext,cca_badge);
                                        String paymentitem_badge=respObject.optString("paymentitem_badge");
                                        PreferenceManager.setPaymentitem_badge(mContext,paymentitem_badge);
                                        String calendar_edited_badge=respObject.optString("calendar_edited_badge");
                                        PreferenceManager.setCalendarEditedBadge(mContext,calendar_edited_badge);
                                        String notification_edited_badge=respObject.optString("notification_edited_badge");
                                        PreferenceManager.setNotificationEditedBadge(mContext,notification_edited_badge);
                                        String whole_school_coming_up_edited_badge=respObject.optString("whole_school_coming_up_edited_badge");
                                        PreferenceManager.setNoticeEditedBadge(mContext,whole_school_coming_up_edited_badge);
                                        String report_edited_badge=respObject.optString("reports_edited_badge");
                                        PreferenceManager.setReportsEditedBadge(mContext,report_edited_badge);
                                        String cca_edited_badge=respObject.optString("cca_edited_badge");
                                        PreferenceManager.setCcaEditedBadge(mContext,cca_edited_badge);
                                        String sports_edited_badge=respObject.optString("sports_edited_badge");
                                        PreferenceManager.setSportsEditedBadge(mContext,sports_edited_badge);
                                        String paymentitem_edit_badge=respObject.optString("paymentitem_edit_badge");
                                        PreferenceManager.setPaymentitem_edit_badge(mContext,paymentitem_edit_badge);
                                        HomeListAppCompatActivity.mListAdapter.notifyDataSetChanged();

                                    }
                                }
                                else if (responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) ||
                                        responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                                        responsCode.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
                                    AppUtils.postInitParam(getActivity(), new AppUtils.GetAccessTokenInterface() {
                                        @Override
                                        public void getAccessToken() {
                                        }
                                    });
                                    getBadge();

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
    public void getClearBadge()
    {
        try {
            final VolleyWrapper manager = new VolleyWrapper(URL_GET_CLEAR_NOTIFICATION_BADGE);
            String[] name = new String[]{JTAG_ACCESSTOKEN,"users_id"};
            String[] value = new String[]{PreferenceManager.getAccessToken(mContext),PreferenceManager.getUserId(mContext)};
            manager.getResponsePOST(mContext, 14, name, value, new VolleyWrapper.ResponseListener() {

                @Override
                public void responseSuccess(String successResponse) {
                    System.out.println("The response is badge clear" + successResponse);
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
                                      if (pushNotificationArrayList.size()>0)
                                      {
                                          for (int i=0;i<pushNotificationArrayList.size();i++)
                                          {
                                              if (pushNotificationArrayList.get(i).getStatus().equalsIgnoreCase("0") || pushNotificationArrayList.get(i).getStatus().equalsIgnoreCase("2"))
                                              {
                                               pushNotificationArrayList.get(i).setStatus("1");
                                              }
                                          }
                                      }
                                    }
                                }
                                else if (responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) ||
                                        responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                                        responsCode.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
                                    AppUtils.postInitParam(getActivity(), new AppUtils.GetAccessTokenInterface() {
                                        @Override
                                        public void getAccessToken() {
                                        }
                                    });
                                    getClearBadge();

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
}
