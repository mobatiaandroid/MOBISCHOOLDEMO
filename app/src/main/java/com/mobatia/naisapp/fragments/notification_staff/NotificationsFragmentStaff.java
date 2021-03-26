/**
 *
 */
package com.mobatia.naisapp.fragments.notification_staff;

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

import com.google.firebase.iid.FirebaseInstanceId;
import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.notification_new.AudioPlayerViewActivityNew;
import com.mobatia.naisapp.activities.notification_new.ImageActivityNew;
import com.mobatia.naisapp.activities.notification_new.TextalertActivityNew;
import com.mobatia.naisapp.activities.notification_new.VideoWebViewActivityNew;
import com.mobatia.naisapp.activities.notification_staff.AudioPlayerViewActivityStaff;
import com.mobatia.naisapp.activities.notification_staff.ImageActivityStaff;
import com.mobatia.naisapp.activities.notification_staff.TextalertActivityStaff;
import com.mobatia.naisapp.activities.notification_staff.VideoWebViewActivityStaff;
import com.mobatia.naisapp.constants.CacheDIRConstants;
import com.mobatia.naisapp.constants.IntentPassValueConstants;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.constants.NaisTabConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.notifications.adapter.PushNotificationListAdapter;
import com.mobatia.naisapp.fragments.notifications.model.PushNotificationModel;
import com.mobatia.naisapp.manager.AppUtils;
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

import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * @author Rijo K Jose
 */

public class NotificationsFragmentStaff extends Fragment implements OnItemClickListener,
        NaisTabConstants, CacheDIRConstants, URLConstants,
        IntentPassValueConstants, NaisClassNameConstants, JSONConstants, StatusConstants {
    TextView mTitleTextView;

    private View mRootView;
    private Context mContext;
    private ListView pushListView;
    private String mTitle;
    private String mTabId;
    private RelativeLayout relMain;
    private ImageView mBannerImage;
    ArrayList<PushNotificationModel> pushNotificationArrayList;
    PushNotificationListAdapter mPushNotificationListAdapter;
    Intent mIntent;
    String myFormatCalender = "yyyy-MM-dd HH:mm:ss";
    static SimpleDateFormat sdfcalender;

    public NotificationsFragmentStaff() {

    }

    public NotificationsFragmentStaff(String title, String tabId) {
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
        myFormatCalender = "yyyy-MM-dd HH:mm:ss";
        sdfcalender = new SimpleDateFormat(myFormatCalender, Locale.ENGLISH);
//		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(mContext));
        ShortcutBadger.applyCount(mContext, 0);//badge
        initialiseUI();
        if (AppUtils.isNetworkConnected(mContext)) {
            clearBadge();
            callPushNotification();
        } else {
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
        pushListView = (ListView) mRootView.findViewById(R.id.pushListView);
//		mProgressDialog = (RelativeLayout) mRootView
//				.findViewById(R.id.progressDialog);
//		mBannerImage = (ImageView) mRootView.findViewById(R.id.bannerImage);
        relMain = (RelativeLayout) mRootView.findViewById(R.id.relMain);
        relMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        pushListView.setOnItemClickListener(this);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget
     * .AdapterView, android.view.View, int, long)
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        if(pushNotificationArrayList.get(position).getStatus().equalsIgnoreCase("0")||pushNotificationArrayList.get(position).getStatus().equalsIgnoreCase("2"))
        {
            callStatusChangeApi(URL_GET_STAFF_NOTIFICATION_STATUS_CHANGE,pushNotificationArrayList.get(position).getId(),position,pushNotificationArrayList.get(position).getStatus());

        }
        if (pushNotificationArrayList.get(position).getPushType().equalsIgnoreCase("")) {
            mIntent = new Intent(mContext, TextalertActivityStaff.class);
            mIntent.putExtra(POSITION, position);
            mIntent.putExtra("PushID", pushNotificationArrayList.get(position).getId());
            mIntent.putExtra("Day",pushNotificationArrayList.get(position).getDay());
            mIntent.putExtra("Month",pushNotificationArrayList.get(position).getMonthString());
            mIntent.putExtra("Year",pushNotificationArrayList.get(position).getYear());
            mIntent.putExtra("PushDate",pushNotificationArrayList.get(position).getPushTime());
            mContext.startActivity(mIntent);
        }
        if (pushNotificationArrayList.get(position).getPushType().equalsIgnoreCase("image")||pushNotificationArrayList.get(position).getPushType().equalsIgnoreCase("text")||pushNotificationArrayList.get(position).getPushType().equalsIgnoreCase("Text")||pushNotificationArrayList.get(position).getPushType().equalsIgnoreCase("Image")) {
            mIntent = new Intent(mContext, ImageActivityStaff.class);
            mIntent.putExtra("PushID", pushNotificationArrayList.get(position).getId());
            mIntent.putExtra("Day",pushNotificationArrayList.get(position).getDay());
            mIntent.putExtra("Month",pushNotificationArrayList.get(position).getMonthString());
            mIntent.putExtra("Year",pushNotificationArrayList.get(position).getYear());
            mIntent.putExtra("PushDate",pushNotificationArrayList.get(position).getPushTime());
            mContext.startActivity(mIntent);
        }
        if (pushNotificationArrayList.get(position).getPushType().equalsIgnoreCase("audio")) {
            mIntent = new Intent(mContext, AudioPlayerViewActivityStaff.class);
            mIntent.putExtra("PushID", pushNotificationArrayList.get(position).getId());
            mIntent.putExtra("Day",pushNotificationArrayList.get(position).getDay());
            mIntent.putExtra("Month",pushNotificationArrayList.get(position).getMonthString());
            mIntent.putExtra("Year",pushNotificationArrayList.get(position).getYear());
            mIntent.putExtra("PushDate",pushNotificationArrayList.get(position).getPushTime());
            mContext.startActivity(mIntent);
        }
        if (pushNotificationArrayList.get(position).getPushType().equalsIgnoreCase("video")) {
            mIntent = new Intent(mContext, VideoWebViewActivityStaff.class);
            mIntent.putExtra("PushID", pushNotificationArrayList.get(position).getId());
            mIntent.putExtra("Day",pushNotificationArrayList.get(position).getDay());
            mIntent.putExtra("Month",pushNotificationArrayList.get(position).getMonthString());
            mIntent.putExtra("Year",pushNotificationArrayList.get(position).getYear());
            mIntent.putExtra("PushDate",pushNotificationArrayList.get(position).getPushTime());
            mContext.startActivity(mIntent);
        }
    }

    private PushNotificationModel getSearchValues(JSONObject Object)
            throws JSONException {
        PushNotificationModel mPushNotificationModel = new PushNotificationModel();
        mPushNotificationModel.setPushType(Object.optString(JTAG_PUSH_TYPE));
        mPushNotificationModel.setPushDate(Object.optString(JTAG_DATE));
        mPushNotificationModel.setId(Object.optString("id"));
        mPushNotificationModel.setStatus(Object.optString("status"));
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
        return mPushNotificationModel;
    }

    private void callPushNotification() {
        pushNotificationArrayList = new ArrayList<PushNotificationModel>();

        try {

            final VolleyWrapper manager = new VolleyWrapper(URL_GET_STAFF_NOTIFICATION);
            String[] name = {JTAG_ACCESSTOKEN,"staff_id"};
            String[] value = {PreferenceManager.getAccessToken(mContext),PreferenceManager.getStaffId(mContext)};
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
                                        for (int i = 0; i < dataArray.length(); i++) {
                                            //String statusCode=responseArray.get(i)
                                            JSONObject dataObject = dataArray.getJSONObject(i);
                                            pushNotificationArrayList.add(getSearchValues(dataObject));
                                        }

                                        mPushNotificationListAdapter = new PushNotificationListAdapter(mContext, pushNotificationArrayList);
                                        pushListView.setAdapter(mPushNotificationListAdapter);
                                    } else {
                                        Toast.makeText(mContext, "No Notifications Found", Toast.LENGTH_SHORT).show();
                                    }
                                } else if (statusCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                                        statusCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) ||
                                        statusCode.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
                                    AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                                        @Override
                                        public void getAccessToken() {
                                        }
                                    });
                                    callPushNotification();

                                }
                            } else if (responseCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                                    responseCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) ||
                                    responseCode.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
                                AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                                    @Override
                                    public void getAccessToken() {
                                    }
                                });
                                callPushNotification();

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
        String[] name = {"access_token","staff_id","id","type"};
        String[] value = {PreferenceManager.getAccessToken(mContext),PreferenceManager.getStaffId(mContext),id,"notification"};
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

}
