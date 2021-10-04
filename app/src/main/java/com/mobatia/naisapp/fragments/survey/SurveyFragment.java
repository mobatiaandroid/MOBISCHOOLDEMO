package com.mobatia.naisapp.fragments.survey;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.notification_new.AudioPlayerViewActivityNew;
import com.mobatia.naisapp.activities.notification_new.ImageActivityNew;
import com.mobatia.naisapp.activities.notification_new.TextalertActivityNew;
import com.mobatia.naisapp.activities.notification_new.VideoWebViewActivityNew;
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
import com.mobatia.naisapp.fragments.notifications.adapter.PushNotificationRecyclerAdapter;
import com.mobatia.naisapp.fragments.notifications.model.PushNotificationModel;
import com.mobatia.naisapp.fragments.survey.adapter.SurveyListAdapter;
import com.mobatia.naisapp.fragments.survey.model.SurveyListModel;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.PreferenceManager;
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

public class SurveyFragment  extends Fragment implements
        NaisTabConstants, CacheDIRConstants, URLConstants,
        IntentPassValueConstants, NaisClassNameConstants, JSONConstants, StatusConstants {
    TextView mTitleTextView;

    private View mRootView;
    private Context mContext;

    private String mTitle;
    private String mTabId;
    private RelativeLayout relMain;
    Intent mIntent;
    private RecyclerView pushListView;
    LinearLayoutManager mUnRead;
    SurveyListAdapter mSurveyListAdapter;
    ArrayList<SurveyListModel>surveyListArrayList;
    ArrayList<SurveyModel> surveyArrayList;
    ArrayList<SurveyQuestionsModel> surveyQuestionArrayList;
    ArrayList<SurveyAnswersModel> surveyAnswersArrayList;

    ArrayList<AnswerSubmitModel>mAnswerList;
    String id="";
    String survey_name="";
    String image="";
    String title="";
    String description="";
    String created_at="";
    String updated_at="";
    String contact_email="";

    TextView text_content;
    TextView text_dialog;
    boolean isemoji1Selected=false;
    boolean isemoji2Selected=false;
    boolean isemoji3Selected=false;
    int survey_satisfation_status=0;
    int currentPage = 0;
    int currentPageSurvey = 0;
    private int surveySize=1;
    public SurveyFragment() {

    }

    public SurveyFragment(String title, String tabId) {
        this.mTitle = title;
        this.mTabId = tabId;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_survey, container,
                false);
//		setHasOptionsMenu(true);
        mContext = getActivity();
        initialiseUI();
        if (AppUtils.isNetworkConnected(mContext)) {
            callSurveyListApi();
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
//        mTitleTextView = (TextView) mRootView.findViewById(R.id.titleTextView);
//        mTitleTextView.setText(SURVEY);
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
                        if (AppUtils.isNetworkConnected(mContext)) {
                            callSurveyDetailApi(surveyListArrayList.get(position).getId());
                        } else
                        {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
                        }


                    }

                    public void onLongClickItem(View v, int position) {
                    }
                }));
    }




    public void callSurveyListApi() {
        try {
            surveyListArrayList=new ArrayList<>();
            final VolleyWrapper manager = new VolleyWrapper(URL_GET_USER_SURVEY_LIST);
            String[] name = new String[]{JTAG_ACCESSTOKEN,"users_id"};
            String[] value = new String[]{PreferenceManager.getAccessToken(mContext),PreferenceManager.getUserId(mContext)};
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

                                        JSONArray dataArray=respObject.getJSONArray("data");
                                        if (dataArray.length()>0)
                                        {
                                            for (int i=0;i<dataArray.length();i++)
                                            {
                                                JSONObject dataObject = dataArray.getJSONObject(i);
                                                SurveyListModel model=new SurveyListModel();
                                                model.setId(dataObject.optString("id"));
                                                model.setSurvey_name(dataObject.optString("survey_name"));
                                                surveyListArrayList.add(model);
                                            }

                                            if (surveyListArrayList.size()>0)
                                            {
                                                mSurveyListAdapter = new SurveyListAdapter(mContext, surveyListArrayList);
                                                pushListView.setAdapter(mSurveyListAdapter);
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

    public void callSurveyDetailApi(String survey_id) {
        try {
            currentPageSurvey = 0;
            surveyArrayList=new ArrayList<>();
            final VolleyWrapper manager = new VolleyWrapper(URL_GET_USER_SURVEY_DETAIL);
            String[] name = new String[]{JTAG_ACCESSTOKEN,"users_id","survey_id"};
            String[] value = new String[]{PreferenceManager.getAccessToken(mContext),PreferenceManager.getUserId(mContext),survey_id};
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

                                        JSONObject dataObject = respObject.getJSONObject("data");
                                        id=dataObject.optString("id");
                                        survey_name=dataObject.optString("survey_name");
                                        image=dataObject.optString("image");
                                        title=dataObject.optString("title");
                                        description=dataObject.optString("description");
                                        created_at=dataObject.optString("created_at");
                                        updated_at=dataObject.optString("updated_at");
                                        contact_email=dataObject.optString("contact_email");
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
                                                 mModel.setAnswer(questionsObject.optString("answer"));
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
                                                   if (questionsObject.optString("answer").equalsIgnoreCase(answerObject.optString("id")))
                                                    {
                                                     nModel.setClicked(true);

                                                    }
                                                   else
                                                       {
                                                           nModel.setClicked(false);
                                                       }

                                                   surveyAnswersArrayList.add(nModel);
                                                  }

                                                 if (surveyAnswersArrayList.size()>0)
                                                 {
                                                     boolean isPositionClicked=false;
                                                     int position=-1;
                                                     for (int m=0;m<surveyAnswersArrayList.size();m++)
                                                     {
                                                         if (surveyAnswersArrayList.get(m).isClicked())
                                                         {
                                                             isPositionClicked=true;
                                                             position=m;
                                                         }
                                                     }
                                                     if (isPositionClicked)
                                                     {
                                                         if (position==0)
                                                         {
                                                             surveyAnswersArrayList.get(0).setClicked0(true);
                                                         }
                                                         else if (position==1)
                                                         {
                                                             surveyAnswersArrayList.get(0).setClicked0(true);
                                                             surveyAnswersArrayList.get(1).setClicked0(true);
                                                         }
                                                         else if (position==2)
                                                         {
                                                             surveyAnswersArrayList.get(0).setClicked0(true);
                                                             surveyAnswersArrayList.get(1).setClicked0(true);
                                                             surveyAnswersArrayList.get(2).setClicked0(true);
                                                         }
                                                         else if (position==3)
                                                         {
                                                             surveyAnswersArrayList.get(0).setClicked0(true);
                                                             surveyAnswersArrayList.get(1).setClicked0(true);
                                                             surveyAnswersArrayList.get(2).setClicked0(true);
                                                             surveyAnswersArrayList.get(3).setClicked0(true);
                                                         }
                                                         else if (position==4)
                                                         {
                                                             surveyAnswersArrayList.get(0).setClicked0(true);
                                                             surveyAnswersArrayList.get(1).setClicked0(true);
                                                             surveyAnswersArrayList.get(2).setClicked0(true);
                                                             surveyAnswersArrayList.get(3).setClicked0(true);
                                                             surveyAnswersArrayList.get(4).setClicked0(true);
                                                         }
                                                         else
                                                         {
                                                             surveyAnswersArrayList.get(0).setClicked0(false);
                                                             surveyAnswersArrayList.get(1).setClicked0(false);
                                                             surveyAnswersArrayList.get(2).setClicked0(false);
                                                             surveyAnswersArrayList.get(3).setClicked0(false);
                                                             surveyAnswersArrayList.get(4).setClicked0(false);
                                                         }
                                                     }
                                                 }
                                                 }
                                                 mModel.setSurveyAnswersrrayList(surveyAnswersArrayList);
                                                 surveyQuestionArrayList.add(mModel);
                                              }
                                           }
                                           showSurveyWelcomeDialogue(getActivity(),surveyArrayList,false);
                                          //showSurvey(getActivity(),surveyArrayList);
                                        //showSurveyWelcomeDialogue(getActivity(),surveyArrayList,false);
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
                                    callSurveyDetailApi(survey_id);

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
    @Override
    public void onResume() {
        super.onResume();

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

                        } else
                            {


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
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_survey_thank_you);
        survey_satisfation_status=0;
        //callSurveySubmitApi(URL_SURVEY_SUBMIT,surveyId,jsonData,getActivity(),surveyArrayList,isThankyou,survey_satisfation_status,dialog);

        Button btn_Ok = (Button) dialog.findViewById(R.id.btn_Ok);
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


    private void sendEmailToStaff(String URL,String email) {
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
                        sendEmailToStaff(URL_SEND_EMAIL_TO_STAFF,email);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        sendEmailToStaff(URL_SEND_EMAIL_TO_STAFF,email);


                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        sendEmailToStaff(URL_SEND_EMAIL_TO_STAFF,email);

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
        headingTxt.setText(title);
        descriptionTxt.setText(description);
        ImageView bannerImg = (ImageView) dialog.findViewById(R.id.bannerImg);
        if (!image.equalsIgnoreCase(""))
        {
            Picasso.with(mContext).load(AppUtils.replace(image)).placeholder(R.drawable.survey).fit().into(bannerImg);
        }
        else
        {
            bannerImg.setImageResource(R.drawable.survey);
        }

        startNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (surveyQuestionArrayList.size()>0)
                {
                    showSurveyQuestionAnswerDialog(activity,surveyQuestionArrayList,survey_name,id,contact_email);
                }

            }
        });
        Button skipBtn = (Button) dialog.findViewById(R.id.skipBtn);
        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showCloseSurveyDialog(activity,dialog);
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


//    public void showSurveyQuestionAnswerDialog(final Activity activity, final ArrayList<SurveyQuestionsModel> surveyQuestionArrayList,final String surveyname,String surveyID,String contactEmail)
//    {
//        final Dialog dialog = new Dialog(activity);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        dialog.setCancelable(false);
//        dialog.setContentView(R.layout.dialog_question_choice_survey);
//        ViewPager surveyPager = (ViewPager) dialog.findViewById(R.id.surveyPager);
//        TextView questionCount = (TextView) dialog.findViewById(R.id.questionCount);
//        TextView nxtQnt = (TextView) dialog.findViewById(R.id.nxtQnt);
//        Button skipBtn = (Button) dialog.findViewById(R.id.skipBtn);
//        ImageView emailImg = (ImageView) dialog.findViewById(R.id.emailImg);
//
//        AppController.mAnswerArrayList=new ArrayList<>();
//        ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.progressBar);
//        progressBar.setMax(surveyQuestionArrayList.size());
//        progressBar.getProgressDrawable().setColorFilter(
//               mContext.getResources().getColor(R.color.rel_one), android.graphics.PorterDuff.Mode.SRC_IN);
//        currentPageSurvey=0;
//        AppController.question_id="";
//        AppController.answer_id="";
//
//        if (surveyQuestionArrayList.size()>9)
//        {
//            questionCount.setText("01/"+String.valueOf(surveyQuestionArrayList.size()));
//        }
//        else {
//            questionCount.setText("01/"+"0"+String.valueOf(surveyQuestionArrayList.size()));
//        }
//
//        RelativeLayout nextQuestionBtn = (RelativeLayout) dialog.findViewById(R.id.nextQuestionBtn);
//
//       // nextQuestionBtn.setClickable(false);
//        if (surveyQuestionArrayList.size()==1)
//        {
//            nextQuestionBtn.setAlpha(1.0f);
//            nextQuestionBtn.setClickable(true);
//            nxtQnt.setText("Submit");
//        }
//        else
//            {
//                if (surveyQuestionArrayList.get(currentPageSurvey).getAnswer().equalsIgnoreCase(""))
//                {
//                    AppController.answer_id="";
//                    AppController.question_id=surveyQuestionArrayList.get(currentPageSurvey).getId();
//                    nextQuestionBtn.setAlpha(0.5f);
//                    nextQuestionBtn.setClickable(false);
//                    nxtQnt.setText("Next");
//
//                }
//                else
//                {
//                    AppController.answer_id=surveyQuestionArrayList.get(currentPageSurvey).getAnswer();
//                    AppController.question_id=surveyQuestionArrayList.get(currentPageSurvey).getId();
//                    nextQuestionBtn.setAlpha(1.0f);
//                    nextQuestionBtn.setClickable(true);
//                    nxtQnt.setText("Next");
//                }
//
//        }
//        if (contactEmail.equalsIgnoreCase(""))
//        {
//            emailImg.setVisibility(View.GONE);
//        }
//        else
//        {
//            emailImg.setVisibility(View.VISIBLE);
//        }
//        emailImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                final Dialog dialog = new Dialog(mContext);
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setContentView(R.layout.alert_send_email_dialog);
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                Button dialogCancelButton = (Button) dialog.findViewById(R.id.cancelButton);
//                Button submitButton = (Button) dialog.findViewById(R.id.submitButton);
//                text_dialog = (EditText) dialog.findViewById(R.id.text_dialog);
//                text_content = (EditText) dialog.findViewById(R.id.text_content);
//
//
//                dialogCancelButton.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//
//                    public void onClick(View v) {
//                        //   AppUtils.hideKeyBoard(mContext);
//                        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
//                        imm.hideSoftInputFromWindow(text_dialog.getWindowToken(), 0);
//                        imm.hideSoftInputFromWindow(text_content.getWindowToken(), 0);
//                        dialog.dismiss();
//
//                    }
//
//                });
//
//                submitButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        sendEmailToStaff(URL_SEND_EMAIL_TO_STAFF,contactEmail);
//                    }
//                });
//
//
//                dialog.show();
//            }
//        });
//        RelativeLayout previousBtn = (RelativeLayout) dialog.findViewById(R.id.previousBtn);
//        if (currentPageSurvey==0)
//        {
//            previousBtn.setAlpha(0.5f);
//            previousBtn.setClickable(false);
//        }
//        else
//        {
//            previousBtn.setAlpha(1.0f);
//            previousBtn.setClickable(true);
//        }
//        nextQuestionBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (surveyQuestionArrayList.size()>0)
//                {
//
//                    if (currentPageSurvey==surveyQuestionArrayList.size())
//                    {
//                        nextQuestionBtn.setAlpha(1.0f);
//                        skipBtn.setAlpha(0.5f);
//                        skipBtn.setClickable(false);
//                        skipBtn.setVisibility(View.VISIBLE);
//                        nextQuestionBtn.setClickable(true);
//                        nxtQnt.setText("Submit");
//                        surveySize=surveySize-1;
//                        progressBar.setProgress(surveyQuestionArrayList.size());
//                        if (surveySize<=0)
//                        {
//                            mAnswerList=new ArrayList<>();
//                             for (int j=0;j<surveyQuestionArrayList.size();j++)
//                             {
//                                 AnswerSubmitModel mmodel=new AnswerSubmitModel();
//                                 for (int k=0;k<surveyQuestionArrayList.get(j).getSurveyAnswersrrayList().size();k++)
//                                 {
//
//                                     String answerId="";
//                                     mmodel.setQuestion_id(surveyQuestionArrayList.get(j).getId());
//                                     if (surveyQuestionArrayList.get(j).getSurveyAnswersrrayList().get(k).isClicked())
//                                     {
//                                         answerId=surveyQuestionArrayList.get(j).getSurveyAnswersrrayList().get(k).getId();
//                                         mmodel.setAnswer_id(answerId);
//                                     }
//
//
//
//                                 }
//                                 mAnswerList.add(mmodel);
//
//
//
//                             }
//                            Gson gson   = new Gson();
//                            ArrayList<String> PassportArray = new ArrayList<>();
//                            for (int i=0;i<mAnswerList.size();i++)
//                            {
//                                //Log.e("Answer ID",mAnswerList.get(i).getAnswer_id());
//                                AnswerSubmitModel nmodel=new AnswerSubmitModel();
//                                nmodel.setAnswer_id(mAnswerList.get(i).getAnswer_id());
//                                nmodel.setQuestion_id(mAnswerList.get(i).getQuestion_id());
//                                String json = gson.toJson(nmodel);
//                                PassportArray.add(i,json);
//                            }
//                            String JSON_STRING=""+PassportArray+"";
//                            Log.e("JSON",JSON_STRING);
//                            dialog.dismiss();
//                            callSurveySubmitApi(URL_SURVEY_SUBMIT,surveyID,JSON_STRING,getActivity(),surveyArrayList,false,1);
//
//                          //  showSurveyThankYouDialog(getActivity(),JSON_STRING,surveyID);
//
//                        }
//                        else {
//                            Gson gson   = new Gson();
//                            ArrayList<String> PassportArray = new ArrayList<>();
//                            Log.e("ANSWER & QUESTION",AppController.answer_id+" :: "+AppController.question_id);
//                            for (int i=0;i<AppController.mAnswerArrayList.size();i++)
//                            {
//                                AnswerSubmitModel nmodel=new AnswerSubmitModel();
//                                nmodel.setAnswer_id(AppController.mAnswerArrayList.get(i).getAnswer_id());
//                                nmodel.setQuestion_id(AppController.mAnswerArrayList.get(i).getQuestion_id());
//                                String json = gson.toJson(nmodel);
//                                PassportArray.add(i,json);
//                            }
//                            String JSON_STRING=""+PassportArray+"";
//                            Log.e("JSON1",JSON_STRING);
//
//                            dialog.dismiss();
//                            callSurveySubmitApi(URL_SURVEY_SUBMIT,surveyID,JSON_STRING,getActivity(),surveyArrayList,false,1);
//
//                            //showSurveyThankYouDialog(getActivity(),JSON_STRING,surveyID);
//                            //showSurveyWelcomeDialogue(getActivity(),surveyArrayList,true);
//                        }
//
//
//                    }
//                    else {
//                        Log.e("SURVEY hhs",AppController.answer_id+" dhdh "+AppController.question_id);
//                        AnswerSubmitModel model=new AnswerSubmitModel();
//                        model.setAnswer_id(AppController.answer_id);
//                        model.setQuestion_id(AppController.question_id);
//                        AppController.mAnswerArrayList.add(model);
//                        currentPageSurvey++;
//                        if (currentPageSurvey==surveyQuestionArrayList.size())
//                        {
//                            skipBtn.setAlpha(0.5f);
//                            skipBtn.setVisibility(View.VISIBLE);
//                        }
//                        else
//                        {
//                            skipBtn.setAlpha(1.0f);
//                            skipBtn.setVisibility(View.VISIBLE);
//                        }
//                        progressBar.setProgress(currentPageSurvey);
//                        skipBtn.setVisibility(View.VISIBLE);
//                        skipBtn.setAlpha(1.0f);
//                        if (surveyQuestionArrayList.size()==currentPageSurvey)
//                        {
//                            skipBtn.setAlpha(0.5f);
//                            skipBtn.setVisibility(View.VISIBLE);
//                            skipBtn.setClickable(false);
//                            nextQuestionBtn.setAlpha(1.0f);
//                            nxtQnt.setText("Submit");
//                            nextQuestionBtn.setClickable(true);
//                        }
//                        else {
//
//                            if (surveyQuestionArrayList.get(currentPageSurvey).getAnswer().equalsIgnoreCase(""))
//                            {
//
//                                nextQuestionBtn.setAlpha(0.5f);
//                                nextQuestionBtn.setClickable(false);
//                                nxtQnt.setText("Next");
//                            }
//                            else
//                            {
//                                nextQuestionBtn.setAlpha(1.0f);
//                                nextQuestionBtn.setClickable(true);
//                                nxtQnt.setText("Next");
//                            }
//
//                        }
//                        surveyPager.setCurrentItem(currentPageSurvey-1);
//                        if (surveyQuestionArrayList.size()>9)
//                        {
//                            if (currentPageSurvey<9)
//                            {
//                                questionCount.setText("0"+currentPageSurvey+"/"+String.valueOf(surveyQuestionArrayList.size()));
//                            }
//                            else {
//                                questionCount.setText(currentPageSurvey+"/"+String.valueOf(surveyQuestionArrayList.size()));
//                            }
//
//                        }
//                        else {
//                            if (currentPageSurvey<9)
//                            {
//                                questionCount.setText("0"+currentPageSurvey+"/"+"0"+String.valueOf(surveyQuestionArrayList.size()));
//                            }
//                            else {
//                                questionCount.setText(currentPageSurvey+"/"+"0"+String.valueOf(surveyQuestionArrayList.size()));
//                            }
//                        }
//
//                    }
//
//                    if (currentPageSurvey==0)
//                    {
//                        previousBtn.setAlpha(0.5f);
//                        previousBtn.setClickable(false);
//                    }
//                    else
//                    {
//                        previousBtn.setAlpha(1.0f);
//                        previousBtn.setClickable(true);
//                    }
//                }
//            }
//        });
//
//        skipBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (surveyQuestionArrayList.size()>0)
//                {
//                    if (currentPageSurvey==surveyQuestionArrayList.size())
//                    {
//                        progressBar.setProgress(currentPageSurvey);
//                        skipBtn.setClickable(false);
//                        nextQuestionBtn.setAlpha(1.0f);
//                        skipBtn.setAlpha(0.5f);
//                        nextQuestionBtn.setClickable(true);
//                        nxtQnt.setText("Submit");
//                        surveySize=surveySize-1;
//                        if (surveySize<=0)
//                        {
//
//                            //dialog.dismiss();
//                        }
//                        else {
//                            skipBtn.setClickable(false);
//                            // dialog.dismiss();
//                            //showSurveyWelcomeDialogue(getActivity(),surveyArrayList,true);
//                        }
//
//
//                    }
//                    else {
//                        skipBtn.setAlpha(1.0f);
//                        skipBtn.setClickable(true);
//                        skipBtn.setVisibility(View.VISIBLE);
//                        AppController.answer_id="";
//                        AppController.question_id=surveyQuestionArrayList.get(currentPageSurvey-1).getId();
//                        AnswerSubmitModel model=new AnswerSubmitModel();
//                        model.setAnswer_id(AppController.answer_id);
//                        model.setQuestion_id(AppController.question_id);
//                        AppController.mAnswerArrayList.add(model);
//                        currentPageSurvey++;
//                        if (currentPageSurvey==surveyQuestionArrayList.size())
//                        {
//                            skipBtn.setVisibility(View.VISIBLE);
//                            skipBtn.setAlpha(0.5f);
//                            skipBtn.setClickable(false);
//                        }
//                        else
//                        {
//                            skipBtn.setVisibility(View.VISIBLE);
//                            skipBtn.setAlpha(1.0f);
//                            skipBtn.setClickable(true);
//                        }
//                        progressBar.setProgress(currentPageSurvey);
//                        surveyPager.setCurrentItem(currentPageSurvey-1);
//                        if (surveyQuestionArrayList.size()==currentPageSurvey)
//                        {
//
//                            nextQuestionBtn.setAlpha(1.0f);
//                            nxtQnt.setText("Submit");
//                            nextQuestionBtn.setClickable(true);
//                        }
//                        else {
//                            if (surveyQuestionArrayList.get(currentPageSurvey-1).getAnswer().equalsIgnoreCase(""))
//                            {
//                                progressBar.setProgress(currentPageSurvey);
//                                nextQuestionBtn.setAlpha(0.5f);
//                                nextQuestionBtn.setClickable(false);
//                                nxtQnt.setText("Next");
//                            }
//                            else
//                            {
//                                progressBar.setProgress(currentPageSurvey);
//                                nextQuestionBtn.setAlpha(1.0f);
//                                nextQuestionBtn.setClickable(true);
//                                nxtQnt.setText("Next");
//                            }
//
//                        }
//
//                        if (surveyQuestionArrayList.size()>9)
//                        {
//                            if (currentPageSurvey<9)
//                            {
//                                questionCount.setText("0"+currentPageSurvey+"/"+String.valueOf(surveyQuestionArrayList.size()));
//                            }
//                            else {
//                                questionCount.setText(currentPageSurvey+"/"+String.valueOf(surveyQuestionArrayList.size()));
//                            }
//
//                        }
//                        else {
//                            if (currentPageSurvey<9)
//                            {
//                                questionCount.setText("0"+currentPageSurvey+"/"+"0"+String.valueOf(surveyQuestionArrayList.size()));
//                            }
//                            else {
//                                questionCount.setText(currentPageSurvey+"/"+"0"+String.valueOf(surveyQuestionArrayList.size()));
//                            }
//                        }
//
//                    }
//
//                    if (currentPageSurvey==0)
//                    {
//                        previousBtn.setAlpha(0.5f);
//                        previousBtn.setClickable(false);
//                    }
//                    else
//                    {
//                        previousBtn.setAlpha(1.0f);
//                        previousBtn.setClickable(true);
//                    }
//                }
//            }
//        });
//
//
//
//        previousBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                if (currentPageSurvey==1)
//                {
//                    previousBtn.setAlpha(0.5f);
//                    previousBtn.setClickable(false);
//                    skipBtn.setClickable(true);
//                    skipBtn.setAlpha(1.0f);
//                    skipBtn.setVisibility(View.VISIBLE);
//
//                }
//                else
//                {
//                    previousBtn.setAlpha(1.0f);
//                    previousBtn.setClickable(true);
//                    currentPageSurvey--;
//                    if (currentPageSurvey==1)
//                    {
//                        previousBtn.setAlpha(0.5f);
//                        previousBtn.setClickable(false);
//                        nxtQnt.setText("Next");
//                        progressBar.setProgress(currentPageSurvey);
//                        surveyPager.setCurrentItem(currentPageSurvey-1);
//                    }
//                    else
//                    {
//                        previousBtn.setAlpha(1.0f);
//                        previousBtn.setClickable(true);
//                        nxtQnt.setText("Next");
//                        progressBar.setProgress(currentPageSurvey-1);
//                        surveyPager.setCurrentItem(currentPageSurvey-1);
//                    }
//                    if (currentPageSurvey==surveyAnswersArrayList.size())
//                    {
//                        skipBtn.setClickable(false);
//                        skipBtn.setAlpha(0.5f);
//                        skipBtn.setVisibility(View.VISIBLE);
//                    }
//                    else
//                    {
//                        skipBtn.setClickable(true);
//                        skipBtn.setAlpha(1.0f);
//                        skipBtn.setVisibility(View.VISIBLE);
//                    }
//                    Log.e("CURRENT PAGE",String.valueOf(currentPageSurvey));
//
//
//                }
//
//                if (surveyQuestionArrayList.size()>9)
//                {
//                    if (currentPageSurvey<9)
//                    {
//                        questionCount.setText("0"+currentPageSurvey+"/"+String.valueOf(surveyQuestionArrayList.size()));
//                    }
//                    else {
//                        questionCount.setText(currentPageSurvey+"/"+String.valueOf(surveyQuestionArrayList.size()));
//                    }
//
//                }
//                else {
//                    if (currentPageSurvey<9)
//                    {
//                        questionCount.setText("0"+currentPageSurvey+"/"+"0"+String.valueOf(surveyQuestionArrayList.size()));
//                    }
//                    else {
//                        questionCount.setText(currentPageSurvey+"/"+"0"+String.valueOf(surveyQuestionArrayList.size()));
//                    }
//                }
//            }
//        });
//        currentPageSurvey=1;
//        surveyPager.setCurrentItem(currentPage-1);
//        Log.e("SURVVEY ::","Working CURRENT"+String.valueOf(currentPageSurvey));
//        progressBar.setProgress(currentPageSurvey);
//        surveyPager.setAdapter(new SurveyPagerAdapter(activity,surveyQuestionArrayList,nextQuestionBtn));
//
//        TextView surveyName = (TextView) dialog.findViewById(R.id.surveyName);
//        surveyName.setText(surveyname);
//
//        ImageView closeImg= (ImageView) dialog.findViewById(R.id.closeImg);
//        closeImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                showCloseSurveyDialog(activity,dialog);
//            }
//        });
//
//        dialog.show();
//
//    }

    public void showSurveyQuestionAnswerDialog(final Activity activity, final ArrayList<SurveyQuestionsModel> surveyQuestionArrayList,final String surveyname,String surveyID,String contactEmail)
    {

    }






}
