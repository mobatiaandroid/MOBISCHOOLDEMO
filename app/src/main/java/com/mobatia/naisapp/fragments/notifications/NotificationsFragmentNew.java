/**
 *
 */
package com.mobatia.naisapp.fragments.notifications;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
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
import com.mobatia.naisapp.appcontroller.AppController;
import com.mobatia.naisapp.constants.CacheDIRConstants;
import com.mobatia.naisapp.constants.IntentPassValueConstants;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.constants.NaisTabConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.home.adapter.SurveyPagerAdapter;
import com.mobatia.naisapp.fragments.home.module.AnswerSubmitModel;
import com.mobatia.naisapp.fragments.home.module.SurveyAnswersModel;
import com.mobatia.naisapp.fragments.home.module.SurveyModel;
import com.mobatia.naisapp.fragments.home.module.SurveyQuestionsModel;
import com.mobatia.naisapp.fragments.notifications.adapter.PushNotificationListAdapter;
import com.mobatia.naisapp.fragments.notifications.adapter.PushNotificationRecyclerAdapter;
import com.mobatia.naisapp.fragments.notifications.model.PushNotificationModel;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.recyclerviewmanager.ItemOffsetDecoration;
import com.mobatia.naisapp.recyclerviewmanager.OnBottomReachedListener;
import com.mobatia.naisapp.recyclerviewmanager.RecyclerItemListener;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;
import com.squareup.picasso.Picasso;

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
    private String surveyEmail;
    private String mTabId;
    private String mTsurveyEmailabId="";
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
    ArrayList<AnswerSubmitModel>mAnswerList;
    TextView text_content;
    TextView text_dialog;
    boolean isemoji1Selected=false;
    boolean isemoji2Selected=false;
    boolean isemoji3Selected=false;
    int survey_satisfation_status=0;
    int currentPage = 0;
    int currentPageSurvey = 0;
    private int surveySize=0;
    int pos=-1;
    ArrayList<SurveyModel> surveyArrayList;
    ArrayList<SurveyQuestionsModel> surveyQuestionArrayList;
    ArrayList<SurveyAnswersModel> surveyAnswersArrayList;
    boolean isShown=false;
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

        myFormatCalender = "yyyy-MM-dd HH:mm:ss";
        sdfcalender = new SimpleDateFormat(myFormatCalender, Locale.ENGLISH);
//		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(mContext));
        ShortcutBadger.applyCount(mContext, 0);//badge
        initialiseUI();
        if (AppUtils.isNetworkConnected(mContext))
        {

            scrollTo="";
            apiID="";
            isFromBottom=false;
            pushNotificationArrayList = new ArrayList<PushNotificationModel>();
            callPushNotification(apiID,scrollTo);
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
        else
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
                           // callStatusChangeApi(URL_GET_STATUS_CHANGE_API,pushNotificationArrayList.get(position).getId(),position,pushNotificationArrayList.get(position).getStatus());

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
                                    int survey=responseObject.getInt("survey");
                                    Log.e("SURVEY VAL",String.valueOf(survey));
                                    if (survey==1)
                                    {
                                        if (scrollto.equalsIgnoreCase("") && !isFromBottom)
                                        {
                                            if (isShown)
                                            {

                                            }
                                            else
                                            {
                                                isShown=true;
                                                callSurveyApi();
                                            }

                                        }
                                    }
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
                                                System.out.println("isFrom");
                                                pushListView.scrollToPosition(pushNotificationArrayList.size()-notificationSize-2);
                                            }
                                            else
                                            {
                                                System.out.println("isFrom");

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



    @Override
    public void onResume() {
        super.onResume();
        System.out.println("on Resume works");
//        scrollTo="";
//        apiID="";
//        isFromBottom=false;
//        pushNotificationArrayList=new ArrayList<>();
//        callPushNotification(apiID,scrollTo);
    }


    /**********************************SURVEY ******************************************/

    /**********************************SURVEY API***************************************/
    public void callSurveyApi() {
        surveyArrayList=new ArrayList<>();


        try {
            final VolleyWrapper manager = new VolleyWrapper(URL_GET_USER_SURVEY);
            String[] name = new String[]{JTAG_ACCESSTOKEN,"users_id","module"};
            String[] value = new String[]{PreferenceManager.getAccessToken(mContext),PreferenceManager.getUserId(mContext),"2"};
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
                                            showSurveyWelcomeDialogue(getActivity(),surveyArrayList,false);
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
                //showCloseSurveyDialog(activity,dialog);

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
                        callSurveySubmitApi(URL_SURVEY_SUBMIT,surveyID,JSON_STRING,getActivity(),surveyArrayList,false,1);
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
                        callSurveySubmitApi(URL_SURVEY_SUBMIT,surveyID,JSON_STRING,getActivity(),surveyArrayList,true,1);
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
                        callSurveySubmitApi(URL_SURVEY_SUBMIT,surveyID,JSON_STRING,getActivity(),surveyArrayList,false,1);
                    }
                    else {
                        callSurveySubmitApi(URL_SURVEY_SUBMIT,surveyID,JSON_STRING,getActivity(),surveyArrayList,true,1);

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

                            showSurveyThankYouDialog(getActivity(),surveyArrayList,isThankyou);

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
