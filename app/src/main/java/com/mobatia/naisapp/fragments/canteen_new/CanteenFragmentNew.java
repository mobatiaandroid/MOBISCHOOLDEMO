package com.mobatia.naisapp.fragments.canteen_new;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.gms.vision.text.Line;
import com.google.gson.Gson;
import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.canteen.CanteenFirstActivity;
import com.mobatia.naisapp.activities.canteen.CanteenFirstStaffActivity;
import com.mobatia.naisapp.activities.canteen_new.CanteenInfoActivity;
import com.mobatia.naisapp.activities.canteen_new.PreOrderActivity;
import com.mobatia.naisapp.activities.canteen_new.PreOrderStaffActivity;
import com.mobatia.naisapp.activities.parentsassociation.model.ParentAssociationEventsModel;
import com.mobatia.naisapp.activities.sports.calendar.CalendarActivity;
import com.mobatia.naisapp.activities.sports.information.InformationActivity;
import com.mobatia.naisapp.activities.sports.teams.TeamActivity;
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
import com.mobatia.naisapp.fragments.secondary.model.SecondaryModel;
import com.mobatia.naisapp.fragments.sports.SportsActivity;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CanteenFragmentNew  extends Fragment implements AdapterView.OnItemClickListener,
        NaisTabConstants, CacheDIRConstants, URLConstants,
        IntentPassValueConstants, NaisClassNameConstants, JSONConstants, StatusConstants {
    private String mTitle;
    private String mTabId;
    Context mContext;
    private View mRootView;
    Bundle extras;
    TextView text_content;
    TextView text_dialog;
    String description="";
    String contactEmail="";
    RelativeLayout signRelative;
    ImageView sendEmail;
    String  tab_type;
    TextView signUpModule;
    TextView descriptionTitle;
    ArrayList<String> bannerUrlImageArray;
    RelativeLayout mtitle;
    ImageView bannerImagePager;
    ArrayList<ParentAssociationEventsModel> parentAssociationEventsModelsArrayList=new ArrayList<>();
    LinearLayout paymentLinear,preOrderLinear,informationLinear,StaffinformationLinear,staffLinear,studeLinear,StaffpreOrderLinear;
    ArrayList<AnswerSubmitModel>mAnswerList;
    boolean isemoji1Selected=false;
    boolean isemoji2Selected=false;
    boolean isemoji3Selected=false;
    int survey_satisfation_status=0;
    int currentPage = 0;
    int currentPageSurvey = 0;
    private int surveySize=0;
    boolean isShown=false;
    int pos=-1;
    ArrayList<SurveyModel> surveyArrayList;
    ArrayList<SurveyQuestionsModel> surveyQuestionArrayList;
    ArrayList<SurveyAnswersModel> surveyAnswersArrayList;

    public CanteenFragmentNew() {

    }

    public CanteenFragmentNew(String title, String tabId) {
        this.mTitle = title;
        this.mTabId = tabId;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_canteen_new, container,
                false);
        mContext = getActivity();
        TextView mTitleTextView = (TextView) mRootView.findViewById(R.id.titleTextView);
        mTitleTextView.setText("Lunch Box");


        preOrderLinear = (LinearLayout) mRootView.findViewById(R.id.preOrderLinear);
        informationLinear = (LinearLayout) mRootView.findViewById(R.id.informationLinear);
        paymentLinear = (LinearLayout) mRootView.findViewById(R.id.paymentLinear);
        //StaffinformationLinear,staffLinear,,StaffpreOrderLinear
        StaffinformationLinear = (LinearLayout) mRootView.findViewById(R.id.StaffinformationLinear);
        staffLinear = (LinearLayout) mRootView.findViewById(R.id.staffLinear);
        studeLinear = (LinearLayout) mRootView.findViewById(R.id.studeLinear);
        StaffpreOrderLinear = (LinearLayout) mRootView.findViewById(R.id.StaffpreOrderLinear);
        mtitle = (RelativeLayout) mRootView.findViewById(R.id.title);
        bannerImagePager= (ImageView) mRootView.findViewById(R.id.bannerImagePager);
        signUpModule= (TextView) mRootView.findViewById(R.id.signUpModule);

        descriptionTitle= (TextView) mRootView.findViewById(R.id.descriptionTitle);
        sendEmail= (ImageView) mRootView.findViewById(R.id.sendEmail);
        if(AppUtils.isNetworkConnected(mContext)) {
            callStaffDirectoryListAPI(URL_CANTEEN_BANNER);
        }else{
            AppUtils.showDialogAlertDismiss((Activity)mContext,"Network Error",getString(R.string.no_internet),R.drawable.nonetworkicon,R.drawable.roundred);

        }
        if(PreferenceManager.getUserId(mContext).equalsIgnoreCase(""))
        {
            studeLinear.setVisibility(View.VISIBLE);
            staffLinear.setVisibility(View.GONE);
        }
        else
        {
            studeLinear.setVisibility(View.VISIBLE);
            staffLinear.setVisibility(View.GONE);
        }
        preOrderLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (PreferenceManager.getUserId(mContext).equals(""))
                {
                    Intent intent = new Intent(mContext, PreOrderStaffActivity.class);
                    intent.putExtra("tab_type","Pre-Order");
                    intent.putExtra("email",contactEmail);
                    startActivity(intent);

                }
                else
                {
                    Intent intent = new Intent(mContext, PreOrderActivity.class);
                    intent.putExtra("tab_type","Pre-Order");
                    intent.putExtra("email",contactEmail);
                    startActivity(intent);

                }

            }
        });
        StaffpreOrderLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (PreferenceManager.getUserId(mContext).equals(""))
                {
                    Intent intent = new Intent(mContext, PreOrderStaffActivity.class);
                    intent.putExtra("tab_type","Pre-Order");
                    intent.putExtra("email",contactEmail);
                    startActivity(intent);

                }
                else
                {
                    Intent intent = new Intent(mContext, PreOrderActivity.class);
                    intent.putExtra("tab_type","Pre-Order");
                    intent.putExtra("email",contactEmail);
                    startActivity(intent);

                }

            }
        });

        informationLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, CanteenInfoActivity.class);
                intent.putExtra("tab_type", "Information");
                startActivity(intent);

            }
        });
        StaffinformationLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, CanteenInfoActivity.class);
                intent.putExtra("tab_type", "Information");
                startActivity(intent);

            }
        });
        paymentLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (PreferenceManager.getUserId(mContext).equalsIgnoreCase(""))
                {
              //      AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "Module under construction", R.drawable.exclamationicon, R.drawable.round);
                    Intent intent = new Intent(mContext, CanteenFirstStaffActivity.class);
                    intent.putExtra("tab_type","Payment");
                    intent.putExtra("email",contactEmail);
                    intent.putExtra("isFrom","payment");
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(mContext, CanteenFirstActivity.class);
                    intent.putExtra("tab_type","Payment");
                    intent.putExtra("email",contactEmail);
                    intent.putExtra("isFrom","payment");
                    startActivity(intent);
                }



            }
        });
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
                        sendEmailToStaff(URL_SEND_EMAIL_TO_STAFF);
                    }
                });


                dialog.show();


            }
        });
        return mRootView;
    }





    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
    private void initialiseUI() {
        if (extras != null) {
        }
    }

    private void sendEmailToStaff(String URL) {
        VolleyWrapper volleyWrapper=new VolleyWrapper(URL);
        String[] name={"access_token","email","users_id","title","message"};
        String[] value={PreferenceManager.getAccessToken(mContext),contactEmail,PreferenceManager.getUserId(mContext),text_dialog.getText().toString(),text_content.getText().toString()};//contactEmail

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
    private void callStaffDirectoryListAPI(String URL)
    {
        parentAssociationEventsModelsArrayList=new ArrayList<>();
        VolleyWrapper volleyWrapper=new VolleyWrapper(URL);
        String[] name={"access_token","users_id"};
        String[] value={PreferenceManager.getAccessToken(mContext),PreferenceManager.getUserId(mContext)};
        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("The response of canteen" + successResponse);
                try {
                    JSONObject obj = new JSONObject(successResponse);
                    String response_code = obj.getString(JTAG_RESPONSECODE);
                    if (response_code.equalsIgnoreCase("200")) {
                        JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                        String status_code = secobj.getString(JTAG_STATUSCODE);
                        if (status_code.equalsIgnoreCase("303")) {
                            String bannerImage = secobj.optString(JTAG_BANNER_IMAGE);
//                            int survey=secobj.getInt("survey");
//                            Log.e("SURVEY VAL",String.valueOf(survey));
//                            if (survey==1)
//                            {
//                                if (isShown)
//                                {
//
//                                }
//                                else
//                                {
//                                    isShown=true;
//                                    callSurveyApi();
//                                }
//                            }
                            description = secobj.optString("description");
                            contactEmail = secobj.optString(JTAG_CONTACT_EMAIL);
                            System.out.println("banner img---" + bannerImage);
                            if (!bannerImage.equalsIgnoreCase("")) {
                                System.out.println("banner img--- inside if" + bannerImage +"Description"+description+"   "+"Conatct Email"+contactEmail);
                                Glide.with(mContext).load(AppUtils.replace(bannerImage)).centerCrop().into(bannerImagePager);

                            } else {
                                System.out.println("banner img--- inside else" + bannerImage);
                                bannerImagePager.setBackgroundResource(R.drawable.default_banner);

                            }

                            if (description.equalsIgnoreCase("")&&contactEmail.equalsIgnoreCase(""))
                            {
                                System.out.println("banner img---1111");
                                mtitle.setVisibility(View.GONE);
                            }
                            else
                            {
                                System.out.println("banner img---22222");
                                mtitle.setVisibility(View.VISIBLE);
                            }
                            if (description.equalsIgnoreCase(""))
                            {
                                System.out.println("banner img--- 3333");

                                descriptionTitle.setVisibility(View.GONE);
                            }else
                            {
                                System.out.println("banner img--- 4444");
                                descriptionTitle.setText(description);
                                descriptionTitle.setVisibility(View.VISIBLE);

                                mtitle.setVisibility(View.VISIBLE);
                            }
                            System.out.println("contact email"+contactEmail);
                            if (contactEmail.equalsIgnoreCase(""))
                            {
                                System.out.println("banner img--- 5555");
                                sendEmail.setVisibility(View.GONE);
                            }else
                            {
                                System.out.println("banner img--- 66666");
                                mtitle.setVisibility(View.VISIBLE);

                                sendEmail.setVisibility(View.VISIBLE);
                            }

                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        callStaffDirectoryListAPI(URL_CANTEEN_BANNER);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                                //callStaffDirectoryListAPI(URL_STAFFDIRECTORY_LIST);
                            }
                        });
                        callStaffDirectoryListAPI(URL_CANTEEN_BANNER);

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                                // callStaffDirectoryListAPI(URL_STAFFDIRECTORY_LIST);
                            }
                        });
                        callStaffDirectoryListAPI(URL_CANTEEN_BANNER);

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
				/*CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
						, getResources().getString(R.string.ok));
				dialog.show();*/
                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

            }
        });


    }



//    /**********************************SURVEY ******************************************/
//
//    /**********************************SURVEY API***************************************/
//    public void callSurveyApi() {
//        surveyArrayList=new ArrayList<>();
//
//
//        try {
//            final VolleyWrapper manager = new VolleyWrapper(URL_GET_USER_SURVEY);
//            String[] name = new String[]{JTAG_ACCESSTOKEN,"users_id","module"};
//            String[] value = new String[]{PreferenceManager.getAccessToken(mContext),PreferenceManager.getUserId(mContext),"6"};
//            manager.getResponsePOST(mContext, 14, name, value, new VolleyWrapper.ResponseListener() {
//
//                @Override
//                public void responseSuccess(String successResponse) {
//                    String responsCode = "";
//                    if (successResponse != null) {
//                        try {
//                            Log.e("SURVEY VALUE",successResponse);
//                            JSONObject rootObject = new JSONObject(successResponse);
//                            if (rootObject.optString(JTAG_RESPONSE) != null) {
//                                responsCode = rootObject.optString(JTAG_RESPONSECODE);
//                                if (responsCode.equals(RESPONSE_SUCCESS)) {
//                                    JSONObject respObject = rootObject.getJSONObject(JTAG_RESPONSE);
//                                    String statusCode = respObject.optString(JTAG_STATUSCODE);
//                                    if (statusCode.equals(STATUS_SUCCESS)) {
//
//                                        JSONArray dataArray=respObject.getJSONArray("data");
//                                        if (dataArray.length()>0)
//                                        {
//                                            surveySize=dataArray.length();
//                                            for (int i=0;i<dataArray.length();i++)
//                                            {
//                                                JSONObject dataObject = dataArray.getJSONObject(i);
//                                                SurveyModel model=new SurveyModel();
//                                                model.setId(dataObject.optString("id"));
//                                                model.setSurvey_name(dataObject.optString("survey_name"));
//                                                model.setImage(dataObject.optString("image"));
//                                                model.setTitle(dataObject.optString("title"));
//                                                model.setDescription(dataObject.optString("description"));
//                                                model.setCreated_at(dataObject.optString("created_at"));
//                                                model.setUpdated_at(dataObject.optString("updated_at"));
//                                                model.setContact_email(dataObject.optString("contact_email"));
//                                                surveyQuestionArrayList=new ArrayList<>();
//                                                JSONArray questionsArray=dataObject.getJSONArray("questions");
//                                                if (questionsArray.length()>0)
//                                                {
//                                                    for (int j=0;j<questionsArray.length();j++)
//                                                    {
//                                                        JSONObject questionsObject = questionsArray.getJSONObject(j);
//                                                        SurveyQuestionsModel mModel=new SurveyQuestionsModel();
//                                                        mModel.setId(questionsObject.optString("id"));
//                                                        mModel.setQuestion(questionsObject.optString("question"));
//                                                        mModel.setAnswer_type(questionsObject.optString("answer_type"));
//                                                        mModel.setAnswer("");
//                                                        surveyAnswersArrayList=new ArrayList<>();
//                                                        JSONArray answerArray=questionsObject.getJSONArray("offered_answers");
//                                                        if (answerArray.length()>0)
//                                                        {
//                                                            for (int k=0;k<answerArray.length();k++)
//                                                            {
//                                                                JSONObject answerObject = answerArray.getJSONObject(k);
//                                                                SurveyAnswersModel nModel=new SurveyAnswersModel();
//                                                                nModel.setId(answerObject.optString("id"));
//                                                                nModel.setAnswer(answerObject.optString("answer"));
//                                                                nModel.setClicked(false);
//                                                                nModel.setClicked0(false);
//                                                                surveyAnswersArrayList.add(nModel);
//                                                            }
//                                                        }
//                                                        mModel.setSurveyAnswersrrayList(surveyAnswersArrayList);
//                                                        surveyQuestionArrayList.add(mModel);
//                                                    }
//                                                }
//                                                model.setSurveyQuestionsArrayList(surveyQuestionArrayList);
//                                                surveyArrayList.add(model);
//                                            }
//                                            //showSurvey(getActivity(),surveyArrayList);
//                                            showSurveyWelcomeDialogue(getActivity(),surveyArrayList,false);
//                                        }
//
//                                    }
//                                }
//                                else if (responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) ||
//                                        responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
//                                        responsCode.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
//                                    AppUtils.postInitParam(getActivity(), new AppUtils.GetAccessTokenInterface() {
//                                        @Override
//                                        public void getAccessToken() {
//                                        }
//                                    });
//                                    callSurveyApi();
//
//                                }
//                            } else if (responsCode.equals(RESPONSE_ERROR)) {
////								CustomStatusDialog(RESPONSE_FAILURE);
//
//                            }
//                        } catch (Exception ex) {
//                            ex.printStackTrace();
//                        }
//                    }
//                }
//
//                @Override
//                public void responseFailure(String failureResponse) {
//                    // CustomStatusDialog(RESPONSE_FAILURE);
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//    }
//    /**********************************SURVEY DIALOGUES***************************************/
//    public void showSurveyWelcomeDialogue(final Activity activity, final ArrayList<SurveyModel> surveyArrayList,final Boolean isThankyou)
//    {
//        final Dialog dialog = new Dialog(activity);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        dialog.setCancelable(false);
//        dialog.setContentView(R.layout.dialog_survey_welcome_screen);
//        Button startNowBtn = (Button) dialog.findViewById(R.id.startNowBtn);
//        ImageView imgClose = (ImageView) dialog.findViewById(R.id.closeImg);
//        TextView headingTxt = (TextView) dialog.findViewById(R.id.titleTxt);
//        TextView descriptionTxt = (TextView) dialog.findViewById(R.id.descriptionTxt);
////        if (isThankyou)
////		{
////			thankyouTxt.setVisibility(View.VISIBLE);
////			thankyouTxt.setText("Thank you For Submitting your Survey");
////		}
////        else {
////			thankyouTxt.setVisibility(View.GONE);
////		}
//
//        headingTxt.setText(surveyArrayList.get(pos+1).getTitle());
//        descriptionTxt.setText(surveyArrayList.get(pos+1).getDescription());
//        ImageView bannerImg = (ImageView) dialog.findViewById(R.id.bannerImg);
//        if (surveyArrayList.get(pos+1).getImage().equalsIgnoreCase(""))
//        {
//            Picasso.with(mContext).load(AppUtils.replace(surveyArrayList.get(pos+1).getImage())).placeholder(R.drawable.survey).fit().into(bannerImg);
//        }
//        else
//        {
//            bannerImg.setImageResource(R.drawable.survey);
//        }
//        Button skipBtn = (Button) dialog.findViewById(R.id.skipBtn);
//        skipBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showCloseSurveyDialog(activity,dialog);
//
//
//            }
//        });
//
//        startNowBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                if (surveyArrayList.size()>0)
//                {
//                    pos=pos+1;
//                    if (pos<surveyArrayList.size())
//                    {
//                        showSurveyQuestionAnswerDialog(activity,surveyArrayList.get(pos).getSurveyQuestionsArrayList(),surveyArrayList.get(pos).getSurvey_name(),surveyArrayList.get(pos).getId(),surveyArrayList.get(pos).getContact_email());
//                    }
//                }
//
//            }
//        });
//        imgClose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showCloseSurveyDialog(activity,dialog);
//            }
//        });
//        dialog.show();
//
//    }
//
////    public void showSurveyQuestionAnswerDialog(final Activity activity, final ArrayList<SurveyQuestionsModel> surveyQuestionArrayList,final String surveyname,String surveyID,String contactEmail)
////    {
////        final Dialog dialog = new Dialog(activity);
////        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
////        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
////        dialog.setCancelable(false);
////        dialog.setContentView(R.layout.dialog_question_choice_survey);
////        ViewPager surveyPager = (ViewPager) dialog.findViewById(R.id.surveyPager);
////        TextView questionCount = (TextView) dialog.findViewById(R.id.questionCount);
////        TextView nxtQnt = (TextView) dialog.findViewById(R.id.nxtQnt);
////        Button skipBtn = (Button) dialog.findViewById(R.id.skipBtn);
////        ImageView emailImg = (ImageView) dialog.findViewById(R.id.emailImg);
////        AppController.mAnswerArrayList=new ArrayList<>();
////        AppController.question_id="";
////        AppController.answer_id="";
////        ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.progressBar);
////        progressBar.setMax(surveyQuestionArrayList.size());
////        progressBar.getProgressDrawable().setColorFilter(
////                mContext.getResources().getColor(R.color.rel_one), android.graphics.PorterDuff.Mode.SRC_IN);
////        if (surveyQuestionArrayList.size()>9)
////        {
////            questionCount.setText("01/"+String.valueOf(surveyQuestionArrayList.size()));
////        }
////        else {
////            questionCount.setText("01/"+"0"+String.valueOf(surveyQuestionArrayList.size()));
////        }
////
////        RelativeLayout nextQuestionBtn = (RelativeLayout) dialog.findViewById(R.id.nextQuestionBtn);
////        nextQuestionBtn.setClickable(false);
////        if (surveyQuestionArrayList.size()==1)
////        {
////            nextQuestionBtn.setAlpha(1.0f);
////            nextQuestionBtn.setClickable(true);
////            nxtQnt.setText("Submit");
////        }
////        else {
////            nextQuestionBtn.setAlpha(0.5f);
////            nextQuestionBtn.setClickable(false);
////            nxtQnt.setText("Next Question");
////        }
////        if (contactEmail.equalsIgnoreCase(""))
////        {
////            emailImg.setVisibility(View.GONE);
////        }
////        else
////        {
////            emailImg.setVisibility(View.VISIBLE);
////        }
////        emailImg.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////
////                final Dialog dialog = new Dialog(mContext);
////                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
////                dialog.setContentView(R.layout.alert_send_email_dialog);
////                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
////                Button dialogCancelButton = (Button) dialog.findViewById(R.id.cancelButton);
////                Button submitButton = (Button) dialog.findViewById(R.id.submitButton);
////                text_dialog = (EditText) dialog.findViewById(R.id.text_dialog);
////                text_content = (EditText) dialog.findViewById(R.id.text_content);
////
////
////                dialogCancelButton.setOnClickListener(new View.OnClickListener() {
////
////                    @Override
////
////                    public void onClick(View v) {
////                        //   AppUtils.hideKeyBoard(mContext);
////                        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
////                        imm.hideSoftInputFromWindow(text_dialog.getWindowToken(), 0);
////                        imm.hideSoftInputFromWindow(text_content.getWindowToken(), 0);
////                        dialog.dismiss();
////
////                    }
////
////                });
////
////                submitButton.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////                        sendEmailToStaff(URL_SEND_EMAIL_TO_STAFF,contactEmail);
////                    }
////                });
////
////
////                dialog.show();
////            }
////        });
////        RelativeLayout previousBtn = (RelativeLayout) dialog.findViewById(R.id.previousBtn);
////        if (currentPageSurvey==1)
////        {
////            previousBtn.setAlpha(0.5f);
////            previousBtn.setClickable(false);
////        }
////        else
////        {
////            previousBtn.setAlpha(1.0f);
////            previousBtn.setClickable(true);
////        }
////        nextQuestionBtn.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                if (surveyQuestionArrayList.size()>0)
////                {
////                    if (currentPageSurvey==surveyQuestionArrayList.size())
////                    {
////                        nextQuestionBtn.setAlpha(1.0f);
////                        nextQuestionBtn.setClickable(true);
////                        nxtQnt.setText("Submit");
////                        progressBar.setProgress(surveyQuestionArrayList.size());
////                        surveySize=surveySize-1;
////                        AnswerSubmitModel model=new AnswerSubmitModel();
////                        model.setAnswer_id(AppController.answer_id);
////                        model.setQuestion_id(AppController.question_id);
////                        AppController.mAnswerArrayList.add(model);
////                        if (surveySize<=0)
////                        {
////                            mAnswerList=new ArrayList<>();
////                            for (int j=0;j<surveyQuestionArrayList.size();j++)
////                            {
////                                AnswerSubmitModel mmodel=new AnswerSubmitModel();
////                                for (int k=0;k<surveyQuestionArrayList.get(j).getSurveyAnswersrrayList().size();k++)
////                                {
////                                    String answerId="";
////                                    mmodel.setQuestion_id(surveyQuestionArrayList.get(j).getId());
////                                    if (surveyQuestionArrayList.get(j).getSurveyAnswersrrayList().get(k).isClicked())
////                                    {
////                                        Log.e("CLICK ","ITS CLICKED"+" k "+String.valueOf(k));
////                                        answerId=surveyQuestionArrayList.get(j).getSurveyAnswersrrayList().get(k).getId();
////                                        Log.e("CLICK Ans ","ITS CLICKED"+" k "+String.valueOf(k));
////                                        mmodel.setAnswer_id(answerId);
////                                    }
////                                }
////                                mAnswerList.add(mmodel);
////                            }
////                            Gson gson   = new Gson();
////                            ArrayList<String> PassportArray = new ArrayList<>();
////                            for (int i=0;i<mAnswerList.size();i++)
////                            {
////                                AnswerSubmitModel nmodel=new AnswerSubmitModel();
////                                nmodel.setAnswer_id(mAnswerList.get(i).getAnswer_id());
////                                nmodel.setQuestion_id(mAnswerList.get(i).getQuestion_id());
////                                //	Log.e("Question ID",AppController.mAnswerArrayList.get(i).getQuestion_id());
////                                //Log.e("Answer ID",AppController.mAnswerArrayList.get(i).getAnswer_id());
////                                String json = gson.toJson(nmodel);
////                                PassportArray.add(i,json);
////                            }
////                            String JSON_STRING=""+PassportArray+"";
////                            Log.e("JSON",JSON_STRING);
////                            dialog.dismiss();
////                            callSurveySubmitApi(URL_SURVEY_SUBMIT,surveyID,JSON_STRING,getActivity(),surveyArrayList,false,1);
////
////                            //showSurveyThankYouDialog(getActivity(),surveyArrayList,false,JSON_STRING,surveyID);
////
////                        }
////                        else {
////
////                            mAnswerList=new ArrayList<>();
////                            for (int j=0;j<surveyQuestionArrayList.size();j++)
////                            {
////                                AnswerSubmitModel mmodel=new AnswerSubmitModel();
////                                for (int k=0;k<surveyQuestionArrayList.get(j).getSurveyAnswersrrayList().size();k++)
////                                {
////                                    String answerId="";
////                                    mmodel.setQuestion_id(surveyQuestionArrayList.get(j).getId());
////                                    if (surveyQuestionArrayList.get(j).getSurveyAnswersrrayList().get(k).isClicked())
////                                    {
////                                        Log.e("CLICK ","ITS CLICKED"+" k "+String.valueOf(k));
////                                        answerId=surveyQuestionArrayList.get(j).getSurveyAnswersrrayList().get(k).getId();
////                                        Log.e("CLICK Ans ","ITS CLICKED"+" k "+String.valueOf(k));
////                                        mmodel.setAnswer_id(answerId);
////                                    }
////                                }
////                                mAnswerList.add(mmodel);
////                            }
////
////                            Gson gson   = new Gson();
////                            ArrayList<String> PassportArray = new ArrayList<>();
////                            for (int i=0;i<mAnswerList.size();i++)
////                            {
////                                AnswerSubmitModel nmodel=new AnswerSubmitModel();
////                                nmodel.setAnswer_id(mAnswerList.get(i).getAnswer_id());
////                                nmodel.setQuestion_id(mAnswerList.get(i).getQuestion_id());
////                                String json = gson.toJson(nmodel);
////                                PassportArray.add(i,json);
////                            }
////                            String JSON_STRING=""+PassportArray+"";
////                            Log.e("JSON1",JSON_STRING);
////
////                            dialog.dismiss();
////                            callSurveySubmitApi(URL_SURVEY_SUBMIT,surveyID,JSON_STRING,getActivity(),surveyArrayList,true,1);
////
////                          //  showSurveyThankYouDialog(getActivity(),surveyArrayList,true,JSON_STRING,surveyID);
////                            //showSurveyWelcomeDialogue(getActivity(),surveyArrayList,true);
////                        }
////
////
////                    }
////
////                    else {
////                        AnswerSubmitModel model=new AnswerSubmitModel();
////                        model.setAnswer_id(AppController.answer_id);
////                        model.setQuestion_id(AppController.question_id);
////                        AppController.mAnswerArrayList.add(model);
////                        currentPageSurvey++;
////                        progressBar.setProgress(currentPageSurvey);
////                        surveyPager.setCurrentItem(currentPageSurvey-1);
////                        if (surveyQuestionArrayList.size()==currentPageSurvey)
////                        {
////                            nextQuestionBtn.setAlpha(1.0f);
////                            nxtQnt.setText("Submit");
////                            nextQuestionBtn.setClickable(true);
////                        }
////                        else {
////                            nextQuestionBtn.setAlpha(0.5f);
////                            nextQuestionBtn.setClickable(false);
////                            nxtQnt.setText("Next Question");
////                        }
////
////                        if (surveyQuestionArrayList.size()>9)
////                        {
////                            if (currentPageSurvey<9)
////                            {
////                                questionCount.setText("0"+currentPageSurvey+"/"+String.valueOf(surveyQuestionArrayList.size()));
////                            }
////                            else {
////                                questionCount.setText(currentPageSurvey+"/"+String.valueOf(surveyQuestionArrayList.size()));
////                            }
////
////                        }
////                        else {
////                            if (currentPageSurvey<9)
////                            {
////                                questionCount.setText("0"+currentPageSurvey+"/"+"0"+String.valueOf(surveyQuestionArrayList.size()));
////                            }
////                            else {
////                                questionCount.setText(currentPageSurvey+"/"+"0"+String.valueOf(surveyQuestionArrayList.size()));
////                            }
////                        }
////
////                    }
////
////                    if (currentPageSurvey==1)
////                    {
////                        previousBtn.setAlpha(0.5f);
////                        previousBtn.setClickable(false);
////                    }
////                    else
////                    {
////                        previousBtn.setAlpha(1.0f);
////                        previousBtn.setClickable(true);
////                    }
////                }
////            }
////        });
////
////        skipBtn.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                if (surveyQuestionArrayList.size()>0)
////                {
////                    if (currentPageSurvey==surveyQuestionArrayList.size())
////                    {
////                        progressBar.setProgress(currentPageSurvey);
////                        skipBtn.setClickable(false);
////                        nextQuestionBtn.setAlpha(1.0f);
////                        nextQuestionBtn.setClickable(true);
////                        nxtQnt.setText("Submit");
////                        surveySize=surveySize-1;
////                        if (surveySize<=0)
////                        {
////
////                            //dialog.dismiss();
////                        }
////                        else {
////                            skipBtn.setClickable(false);
////                            // dialog.dismiss();
////                            //showSurveyWelcomeDialogue(getActivity(),surveyArrayList,true);
////                        }
////
////
////                    }
////                    else {
////                        skipBtn.setClickable(true);
////                        AppController.answer_id="";
////                        AppController.question_id=surveyQuestionArrayList.get(currentPageSurvey-1).getId();
////                        AnswerSubmitModel model=new AnswerSubmitModel();
////                        model.setAnswer_id(AppController.answer_id);
////                        model.setQuestion_id(AppController.question_id);
////                        AppController.mAnswerArrayList.add(model);
////                        currentPageSurvey++;
////                        surveyPager.setCurrentItem(currentPageSurvey-1);
////                        if (surveyQuestionArrayList.size()==currentPageSurvey)
////                        {
////                            nextQuestionBtn.setAlpha(1.0f);
////                            nxtQnt.setText("Submit");
////                            nextQuestionBtn.setClickable(true);
////                        }
////                        else {
////                            progressBar.setProgress(currentPageSurvey);
////                            nextQuestionBtn.setAlpha(0.5f);
////                            nextQuestionBtn.setClickable(false);
////                            nxtQnt.setText("Next Question");
////
////                        }
////
////                        if (surveyQuestionArrayList.size()>9)
////                        {
////                            if (currentPageSurvey<9)
////                            {
////                                questionCount.setText("0"+currentPageSurvey+"/"+String.valueOf(surveyQuestionArrayList.size()));
////                            }
////                            else {
////                                questionCount.setText(currentPageSurvey+"/"+String.valueOf(surveyQuestionArrayList.size()));
////                            }
////
////                        }
////                        else {
////                            if (currentPageSurvey<9)
////                            {
////                                questionCount.setText("0"+currentPageSurvey+"/"+"0"+String.valueOf(surveyQuestionArrayList.size()));
////                            }
////                            else {
////                                questionCount.setText(currentPageSurvey+"/"+"0"+String.valueOf(surveyQuestionArrayList.size()));
////                            }
////                        }
////
////                    }
////                }
////            }
////        });
////
////        previousBtn.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////
////
////                if (currentPageSurvey==1)
////                {
////                    previousBtn.setAlpha(0.5f);
////                    previousBtn.setClickable(false);
////
////                }
////                else
////                {
////                    previousBtn.setAlpha(1.0f);
////                    previousBtn.setClickable(true);
////                    currentPageSurvey--;
////                    if (currentPageSurvey==1)
////                    {
////                        previousBtn.setAlpha(0.5f);
////                        previousBtn.setClickable(false);
////                        progressBar.setProgress(currentPageSurvey);
////                        surveyPager.setCurrentItem(currentPageSurvey-1);
////                    }
////                    else
////                    {
////                        previousBtn.setAlpha(1.0f);
////                        previousBtn.setClickable(true);
////                        progressBar.setProgress(currentPageSurvey-1);
////                        surveyPager.setCurrentItem(currentPageSurvey-1);
////                    }
////                    Log.e("CURRENT PAGE",String.valueOf(currentPageSurvey));
////
////
////                }
////
////                if (surveyQuestionArrayList.size()>9)
////                {
////                    if (currentPageSurvey<9)
////                    {
////                        questionCount.setText("0"+currentPageSurvey+"/"+String.valueOf(surveyQuestionArrayList.size()));
////                    }
////                    else {
////                        questionCount.setText(currentPageSurvey+"/"+String.valueOf(surveyQuestionArrayList.size()));
////                    }
////
////                }
////                else {
////                    if (currentPageSurvey<9)
////                    {
////                        questionCount.setText("0"+currentPageSurvey+"/"+"0"+String.valueOf(surveyQuestionArrayList.size()));
////                    }
////                    else {
////                        questionCount.setText(currentPageSurvey+"/"+"0"+String.valueOf(surveyQuestionArrayList.size()));
////                    }
////                }
////            }
////        });
////
////        currentPageSurvey=1;
////        surveyPager.setCurrentItem(currentPage-1);
////        progressBar.setProgress(currentPageSurvey);
////        surveyPager.setAdapter(new SurveyPagerAdapter(activity,surveyQuestionArrayList,nextQuestionBtn));
////
////        TextView surveyName = (TextView) dialog.findViewById(R.id.surveyName);
////        surveyName.setText(surveyname);
////
////        ImageView closeImg= (ImageView) dialog.findViewById(R.id.closeImg);
////        closeImg.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////
////                showCloseSurveyDialog(activity,dialog);
////            }
////        });
////
////        dialog.show();
////
////    }
//
//    public void showCloseSurveyDialog(final Activity activity, final Dialog dialogCLose)
//    {
//        final Dialog dialog = new Dialog(activity);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        dialog.setCancelable(false);
//        dialog.setContentView(R.layout.dialog_close_survey);
//        TextView text_dialog = (TextView) dialog.findViewById(R.id.text_dialog);
//        text_dialog.setText("Are you sure you want to close this survey.");
//
//        Button btn_Ok = (Button) dialog.findViewById(R.id.btn_Ok);
//        btn_Ok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //set Preference to true
//                dialogCLose.dismiss();
//                dialog.dismiss();
//
//            }
//        });
//
//        Button btn_Cancel = (Button) dialog.findViewById(R.id.btn_Cancel);
//        btn_Cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                dialog.dismiss();
//            }
//        });
//        dialog.show();
//
//    }
//
//
//    private void sendEmailToStaff(String URL,String email) {
//        VolleyWrapper volleyWrapper=new VolleyWrapper(URL);
//        String[] name={"access_token","email","users_id","title","message"};
//        String[] value={PreferenceManager.getAccessToken(mContext),email,PreferenceManager.getUserId(mContext),text_dialog.getText().toString(),text_content.getText().toString()};//contactEmail
//
//        //String[] value={PreferenceManager.getAccessToken(mContext),mStaffList.get(pos).getStaffEmail(),JTAG_USERS_ID_VALUE,text_dialog.getText().toString(),text_content.getText().toString()};
//        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
//            @Override
//            public void responseSuccess(String successResponse) {
//                System.out.println("The response is" + successResponse);
//                try {
//                    JSONObject obj = new JSONObject(successResponse);
//                    String response_code = obj.getString(JTAG_RESPONSECODE);
//                    if (response_code.equalsIgnoreCase("200")) {
//                        JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
//                        String status_code = secobj.getString(JTAG_STATUSCODE);
//                        if (status_code.equalsIgnoreCase("303")) {
//                            Toast toast = Toast.makeText(mContext, "Successfully sent email to staff", Toast.LENGTH_SHORT);
//                            toast.show();
//                        } else {
//                            Toast toast = Toast.makeText(mContext, "Email not sent", Toast.LENGTH_SHORT);
//                            toast.show();
//                        }
//                    } else if (response_code.equalsIgnoreCase("500")) {
//                    } else if (response_code.equalsIgnoreCase("400")) {
//                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
//                            @Override
//                            public void tokenrenewed() {
//                            }
//                        });
//                        sendEmailToStaff(URL_SEND_EMAIL_TO_STAFF,email);
//
//                    } else if (response_code.equalsIgnoreCase("401")) {
//                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
//                            @Override
//                            public void tokenrenewed() {
//                            }
//                        });
//                        sendEmailToStaff(URL_SEND_EMAIL_TO_STAFF,email);
//
//
//                    } else if (response_code.equalsIgnoreCase("402")) {
//                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
//                            @Override
//                            public void tokenrenewed() {
//                            }
//                        });
//                        sendEmailToStaff(URL_SEND_EMAIL_TO_STAFF,email);
//
//                    } else {
//						/*CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
//								, getResources().getString(R.string.ok));
//						dialog.show();*/
//                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);
//
//                    }
//                } catch (Exception ex)
//                {
//                    System.out.println("The Exception in edit profile is" + ex.toString());
//                }
//
//            }
//
//            @Override
//            public void responseFailure(String failureResponse) {
//				/*CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
//						, getResources().getString(R.string.ok));
//				dialog.show();*/
//                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);
//
//            }
//        });
//
//
//    }
//
//    public void showSurveyThankYouDialog(final Activity activity, final ArrayList<SurveyModel> surveyArrayList,final Boolean isThankyou)
//    {
//        final Dialog dialog = new Dialog(activity);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        dialog.setCancelable(false);
//        dialog.setContentView(R.layout.dialog_survey_thank_you);
//        survey_satisfation_status=0;
//        //callSurveySubmitApi(URL_SURVEY_SUBMIT,surveyId,jsonData,getActivity(),surveyArrayList,isThankyou,survey_satisfation_status,dialog);
//        Button btn_Ok = (Button) dialog.findViewById(R.id.btn_Ok);
//        btn_Ok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                if (isThankyou)
//                {
//                    showSurveyWelcomeDialogue(activity,surveyArrayList,true);
//
//                }
//                else
//                {
//
//                }
//                dialog.dismiss();
//            }
//        });
//
//
//        dialog.show();
//
//    }
//
//
//    private void callSurveySubmitApi(String URL,String survey_id,String answer,Activity activity,ArrayList<SurveyModel> surveyArrayList, Boolean isThankyou,int survey_satisfation_status) {
//        VolleyWrapper volleyWrapper=new VolleyWrapper(URL);
//        String[] name={"access_token","users_id","survey_id","answers","survey_satisfation_status"};
//        Log.e("STATUs",String.valueOf(survey_satisfation_status));
//        String[] value={PreferenceManager.getAccessToken(mContext),PreferenceManager.getUserId(mContext),survey_id,answer,String.valueOf(survey_satisfation_status)};//contactEmail
//        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
//            @Override
//            public void responseSuccess(String successResponse) {
//                System.out.println("The response is" + successResponse);
//                try {
//                    JSONObject obj = new JSONObject(successResponse);
//                    String response_code = obj.getString(JTAG_RESPONSECODE);
//                    if (response_code.equalsIgnoreCase("200")) {
//                        JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
//                        String status_code = secobj.getString(JTAG_STATUSCODE);
//                        if (status_code.equalsIgnoreCase("303")) {
//                            showSurveyThankYouDialog(getActivity(),surveyArrayList,isThankyou);
//
//
//                        } else {
//
//
//                        }
//                    } else if (response_code.equalsIgnoreCase("500")) {
//                    } else if (response_code.equalsIgnoreCase("400")) {
//                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
//                            @Override
//                            public void tokenrenewed() {
//                            }
//                        });
//                        callSurveySubmitApi(URL_SURVEY_SUBMIT,survey_id,answer,activity,surveyArrayList,isThankyou,survey_satisfation_status);
//
//                    } else if (response_code.equalsIgnoreCase("401")) {
//                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
//                            @Override
//                            public void tokenrenewed() {
//                            }
//                        });
//                        callSurveySubmitApi(URL_SURVEY_SUBMIT,survey_id,answer,activity,surveyArrayList,isThankyou,survey_satisfation_status);
//
//
//                    } else if (response_code.equalsIgnoreCase("402")) {
//                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
//                            @Override
//                            public void tokenrenewed() {
//                            }
//                        });
//                        callSurveySubmitApi(URL_SURVEY_SUBMIT,survey_id,answer,activity,surveyArrayList,isThankyou,survey_satisfation_status);
//
//                    } else {
//						/*CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
//								, getResources().getString(R.string.ok));
//						dialog.show();*/
//                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);
//
//                    }
//                } catch (Exception ex)
//                {
//                    System.out.println("The Exception in edit profile is" + ex.toString());
//                }
//
//            }
//
//            @Override
//            public void responseFailure(String failureResponse) {
//				/*CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
//						, getResources().getString(R.string.ok));
//				dialog.show();*/
//                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);
//
//            }
//        });
//
//
//    }
//
//
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
//        TextView currentQntTxt = (TextView) dialog.findViewById(R.id.currentQntTxt);
//        TextView surveyName = (TextView) dialog.findViewById(R.id.surveyName);
//        Button skipBtn = (Button) dialog.findViewById(R.id.skipBtn);
//        ImageView previousBtn = (ImageView) dialog.findViewById(R.id.previousBtn);
//        ImageView nextQuestionBtn = (ImageView) dialog.findViewById(R.id.nextQuestionBtn);
//        ImageView emailImg = (ImageView) dialog.findViewById(R.id.emailImg);
//        ImageView closeImg = (ImageView) dialog.findViewById(R.id.closeImg);
//        ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.progressBar);
//        progressBar.setMax(surveyQuestionArrayList.size());
//        progressBar.getProgressDrawable().setColorFilter(mContext.getResources().getColor(R.color.rel_one), android.graphics.PorterDuff.Mode.SRC_IN);
//        closeImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                showCloseSurveyDialog(activity,dialog);
//            }
//        });
//        if (surveyQuestionArrayList.size()>9)
//        {
//            currentQntTxt.setText("01");
//            questionCount.setText("/"+String.valueOf(surveyQuestionArrayList.size()));
//        }
//        else {
//            currentQntTxt.setText("01");
//            questionCount.setText("/0"+String.valueOf(surveyQuestionArrayList.size()));
//        }
//        surveyName.setText(surveyname);
//        if (contactEmail.equalsIgnoreCase(""))
//        {
//            emailImg.setVisibility(View.GONE);
//        }
//        else {
//            emailImg.setVisibility(View.VISIBLE);
//        }
//
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
//
//        currentPageSurvey=1;
//        surveyPager.setCurrentItem(currentPageSurvey-1);
//        progressBar.setProgress(currentPageSurvey);
//        surveyPager.setAdapter(new SurveyPagerAdapter(activity,surveyQuestionArrayList,nextQuestionBtn));
//        if(currentPageSurvey==surveyQuestionArrayList.size())
//        {
//            previousBtn.setVisibility(View.INVISIBLE);
//            nextQuestionBtn.setVisibility(View.INVISIBLE);
//            nxtQnt.setVisibility(View.VISIBLE);
//        }
//        else
//        {
//            if (currentPageSurvey==1)
//            {
//                previousBtn.setVisibility(View.INVISIBLE);
//                nextQuestionBtn.setVisibility(View.VISIBLE);
//                nxtQnt.setVisibility(View.INVISIBLE);
//            }
//            else {
//                previousBtn.setVisibility(View.INVISIBLE);
//                nextQuestionBtn.setVisibility(View.VISIBLE);
//                nxtQnt.setVisibility(View.INVISIBLE);
//            }
//        }
//
//        nextQuestionBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (currentPageSurvey==surveyQuestionArrayList.size())
//                {
//
//                }
//                else {
//                    currentPageSurvey++;
//                    progressBar.setProgress(currentPageSurvey);
//                    surveyPager.setCurrentItem(currentPageSurvey-1);
//                    if (currentPageSurvey==surveyQuestionArrayList.size())
//                    {
//                        nextQuestionBtn.setVisibility(View.INVISIBLE);
//                        previousBtn.setVisibility(View.VISIBLE);
//                        nxtQnt.setVisibility(View.VISIBLE);
//
//                    }else {
//                        nextQuestionBtn.setVisibility(View.VISIBLE);
//                        previousBtn.setVisibility(View.VISIBLE);
//                        nxtQnt.setVisibility(View.INVISIBLE);
//                    }
//                }
//
//                if (surveyQuestionArrayList.size()>9)
//                {
//                    if (currentPageSurvey<9)
//                    {
//                        currentQntTxt.setText("0"+currentPageSurvey);
//                        questionCount.setText("/"+String.valueOf(surveyQuestionArrayList.size()));
//                    }
//                    else {
//                        currentQntTxt.setText(currentPageSurvey);
//                        questionCount.setText("/"+String.valueOf(surveyQuestionArrayList.size()));
//                    }
//
//                }
//                else {
//                    if (currentPageSurvey<9)
//                    {
//                        currentQntTxt.setText("0"+currentPageSurvey);
//                        questionCount.setText("/"+"0"+String.valueOf(surveyQuestionArrayList.size()));
//                    }
//                    else {
//                        currentQntTxt.setText(currentPageSurvey);
//                        questionCount.setText("/"+"0"+String.valueOf(surveyQuestionArrayList.size()));
//                    }
//                }
//
//            }
//        });
//        previousBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (currentPageSurvey==1)
//                {
//                    previousBtn.setVisibility(View.INVISIBLE);
//                    nxtQnt.setVisibility(View.INVISIBLE);
//                    if (currentPageSurvey==surveyQuestionArrayList.size())
//                    {
//                        nxtQnt.setVisibility(View.VISIBLE);
//                    }
//                    else {
//                        nxtQnt.setVisibility(View.INVISIBLE);
//                    }
//                }
//                else {
//
//                    currentPageSurvey--;
//                    progressBar.setProgress(currentPageSurvey);
//                    surveyPager.setCurrentItem(currentPageSurvey-1);
//                    if (currentPageSurvey==surveyQuestionArrayList.size())
//                    {
//                        nextQuestionBtn.setVisibility(View.INVISIBLE);
//                        previousBtn.setVisibility(View.VISIBLE);
//                        nxtQnt.setVisibility(View.VISIBLE);
//
//                    }else {
//                        if (currentPageSurvey==1)
//                        {
//                            previousBtn.setVisibility(View.INVISIBLE);
//                            nextQuestionBtn.setVisibility(View.VISIBLE);
//                            nxtQnt.setVisibility(View.INVISIBLE);
//                        }
//                        else {
//                            nextQuestionBtn.setVisibility(View.VISIBLE);
//                            previousBtn.setVisibility(View.VISIBLE);
//                            nxtQnt.setVisibility(View.INVISIBLE);
//                        }
//
//                    }
//                }
//
//
//                if (surveyQuestionArrayList.size()>9)
//                {
//                    if (currentPageSurvey<9)
//                    {
//                        currentQntTxt.setText("0"+currentPageSurvey);
//                        questionCount.setText("/"+String.valueOf(surveyQuestionArrayList.size()));
//                    }
//                    else {
//                        currentQntTxt.setText(currentPageSurvey);
//                        questionCount.setText("/"+String.valueOf(surveyQuestionArrayList.size()));
//                    }
//
//                }
//                else {
//                    if (currentPageSurvey<9)
//                    {
//                        currentQntTxt.setText("0"+currentPageSurvey);
//                        questionCount.setText("/"+"0"+String.valueOf(surveyQuestionArrayList.size()));
//                    }
//                    else {
//                        currentQntTxt.setText(currentPageSurvey);
//                        questionCount.setText("/"+"0"+String.valueOf(surveyQuestionArrayList.size()));
//                    }
//                }
//
//            }
//        });
//        nxtQnt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                boolean isFound=false;
//                int pos=-1;
//                int emptyvalue=0;
//                for (int i=0;i<surveyQuestionArrayList.size();i++)
//                {
//                    if (surveyQuestionArrayList.get(i).getAnswer().equalsIgnoreCase(""))
//                    {
//                        emptyvalue=emptyvalue+1;
//                        if (!isFound)
//                        {
//                            isFound=true;
//                            pos=i;
//                        }
//                    }
//                }
//                if (isFound)
//                {
//                    mAnswerList=new ArrayList<>();
//                    for (int k=0;k<surveyQuestionArrayList.size();k++)
//                    {
//                        AnswerSubmitModel model=new AnswerSubmitModel();
//                        model.setQuestion_id(surveyQuestionArrayList.get(k).getId());
//                        model.setAnswer_id(surveyQuestionArrayList.get(k).getAnswer());
//                        mAnswerList.add(model);
//                    }
//                    Gson gson   = new Gson();
//                    ArrayList<String> PassportArray = new ArrayList<>();
//                    for (int i=0;i<mAnswerList.size();i++)
//                    {
//                        AnswerSubmitModel nmodel=new AnswerSubmitModel();
//                        nmodel.setAnswer_id(mAnswerList.get(i).getAnswer_id());
//                        nmodel.setQuestion_id(mAnswerList.get(i).getQuestion_id());
//                        String json = gson.toJson(nmodel);
//                        PassportArray.add(i,json);
//                    }
//                    String JSON_STRING=""+PassportArray+"";
//                    Log.e("JSON",JSON_STRING);
//                    if (emptyvalue==surveyQuestionArrayList.size())
//                    {
//                        boolean isEmpty=true;
//                        showSurveyContinueDialog(activity,surveyID,JSON_STRING,surveyArrayList,progressBar,surveyPager,surveyQuestionArrayList,previousBtn,nextQuestionBtn,nxtQnt,currentQntTxt,questionCount,pos,dialog,isEmpty);
//
//                    }
//                    else {
//                        boolean isEmpty=false;
//                        showSurveyContinueDialog(activity,surveyID,JSON_STRING,surveyArrayList,progressBar,surveyPager,surveyQuestionArrayList,previousBtn,nextQuestionBtn,nxtQnt,currentQntTxt,questionCount,pos,dialog,isEmpty);
//
//                    }
//
//
//                }
//                else
//                {
//                    surveySize=surveySize-1;
//                    if (surveySize<=0)
//                    {
//                        mAnswerList=new ArrayList<>();
//                        for (int k=0;k<surveyQuestionArrayList.size();k++)
//                        {
//                            AnswerSubmitModel model=new AnswerSubmitModel();
//                            model.setQuestion_id(surveyQuestionArrayList.get(k).getId());
//                            model.setAnswer_id(surveyQuestionArrayList.get(k).getAnswer());
//                            mAnswerList.add(model);
//                        }
//                        Gson gson   = new Gson();
//                        ArrayList<String> PassportArray = new ArrayList<>();
//                        for (int i=0;i<mAnswerList.size();i++)
//                        {
//                            AnswerSubmitModel nmodel=new AnswerSubmitModel();
//                            nmodel.setAnswer_id(mAnswerList.get(i).getAnswer_id());
//                            nmodel.setQuestion_id(mAnswerList.get(i).getQuestion_id());
//                            String json = gson.toJson(nmodel);
//                            PassportArray.add(i,json);
//                        }
//                        String JSON_STRING=""+PassportArray+"";
//                        Log.e("JSON",JSON_STRING);
//                        dialog.dismiss();
//                        callSurveySubmitApi(URL_SURVEY_SUBMIT,surveyID,JSON_STRING,getActivity(),surveyArrayList,false,1);
//                    }
//                    else {
//                        mAnswerList=new ArrayList<>();
//                        for (int k=0;k<surveyQuestionArrayList.size();k++)
//                        {
//                            AnswerSubmitModel model=new AnswerSubmitModel();
//                            model.setQuestion_id(surveyQuestionArrayList.get(k).getId());
//                            model.setAnswer_id(surveyQuestionArrayList.get(k).getAnswer());
//                            mAnswerList.add(model);
//                        }
//                        Gson gson   = new Gson();
//                        ArrayList<String> PassportArray = new ArrayList<>();
//                        for (int i=0;i<mAnswerList.size();i++)
//                        {
//                            AnswerSubmitModel nmodel=new AnswerSubmitModel();
//                            nmodel.setAnswer_id(mAnswerList.get(i).getAnswer_id());
//                            nmodel.setQuestion_id(mAnswerList.get(i).getQuestion_id());
//                            String json = gson.toJson(nmodel);
//                            PassportArray.add(i,json);
//                        }
//                        String JSON_STRING=""+PassportArray+"";
//                        Log.e("JSON",JSON_STRING);
//                        dialog.dismiss();
//                        callSurveySubmitApi(URL_SURVEY_SUBMIT,surveyID,JSON_STRING,getActivity(),surveyArrayList,true,1);
//                    }
//
//
//                }
//
//                Log.e("POS",String.valueOf(pos));
//
//
//            }
//
//        });
//
//        dialog.show();
//
//    }
//
//
//
//    public void showSurveyContinueDialog(final Activity activity,String surveyID,String JSON_STRING,ArrayList<SurveyModel> surveyArrayList,ProgressBar progressBar,ViewPager surveyPager,ArrayList<SurveyQuestionsModel>surveyQuestionArrayList,ImageView previousBtn,ImageView nextQuestionBtn,TextView nxtQnt,TextView currentQntTxt,TextView questionCount, int pos,Dialog nDialog,boolean isEmpty)
//    {
//        final Dialog dialog = new Dialog(activity);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        dialog.setCancelable(false);
//        dialog.setContentView(R.layout.dialog_continue_layout);
//        survey_satisfation_status=0;
//        //callSurveySubmitApi(URL_SURVEY_SUBMIT,surveyId,jsonData,getActivity(),surveyArrayList,isThankyou,survey_satisfation_status,dialog);
//        Button btn_Ok = (Button) dialog.findViewById(R.id.btn_Ok);
//        Button submit = (Button) dialog.findViewById(R.id.submit);
//        TextView thoughtsTxt = (TextView) dialog.findViewById(R.id.thoughtsTxt);
//
//        if (isEmpty)
//        {
//            submit.setClickable(false);
//            submit.setAlpha(0.5f);
//            thoughtsTxt.setText("Appreciate atleast one feedback from you.");
//        }
//        else {
//            submit.setClickable(true);
//            submit.setAlpha(1.0f);
//            thoughtsTxt.setText("There is an unanswered question on this survey. Would you like to continue?");
//        }
//        submit.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v) {
//                if (!isEmpty)
//                {
//                    nDialog.dismiss();
//                    Log.e("SURVEY SIZE",String.valueOf(surveySize));
//                    surveySize=surveySize-1;
//                    if (surveySize<=0)
//                    {
//                        Log.e("SURVEY SIZE ",String.valueOf(surveySize));
//                        callSurveySubmitApi(URL_SURVEY_SUBMIT,surveyID,JSON_STRING,getActivity(),surveyArrayList,false,1);
//                    }
//                    else {
//                        callSurveySubmitApi(URL_SURVEY_SUBMIT,surveyID,JSON_STRING,getActivity(),surveyArrayList,true,1);
//
//                    }
//                    dialog.dismiss();
//                }
//
//
//
//            }
//        });
//        btn_Ok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                currentPageSurvey=pos+1;
//                progressBar.setProgress(currentPageSurvey);
//                surveyPager.setCurrentItem(currentPageSurvey-1);
//
//                Log.e("WORKING","SURVEY COUNT"+String.valueOf(currentPageSurvey));
//                if(surveyQuestionArrayList.size()>1)
//                {
//                    if (currentPageSurvey!=surveyQuestionArrayList.size())
//                    {
//                        if(currentPageSurvey==1)
//                        {
//                            previousBtn.setVisibility(View.INVISIBLE);
//                            nextQuestionBtn.setVisibility(View.VISIBLE);
//                            nxtQnt.setVisibility(View.INVISIBLE);
//                        }
//                        else {
//                            if(currentPageSurvey==1)
//                            {
//                                previousBtn.setVisibility(View.INVISIBLE);
//                                nextQuestionBtn.setVisibility(View.VISIBLE);
//                                nxtQnt.setVisibility(View.INVISIBLE);
//                            }
//                            else {
//                                previousBtn.setVisibility(View.VISIBLE);
//                                nextQuestionBtn.setVisibility(View.VISIBLE);
//                                nxtQnt.setVisibility(View.INVISIBLE);
//                            }
//                        }
//                    }
//                    else {
//                        previousBtn.setVisibility(View.VISIBLE);
//                        nextQuestionBtn.setVisibility(View.INVISIBLE);
//                        nxtQnt.setVisibility(View.VISIBLE);
//                    }
//
//                }
//                else {
//                    if (currentPageSurvey==1)
//                    {
//                        previousBtn.setVisibility(View.INVISIBLE);
//                        nextQuestionBtn.setVisibility(View.INVISIBLE);
//                        nxtQnt.setVisibility(View.VISIBLE);
//                    }
//                }
//                if (surveyQuestionArrayList.size()>9)
//                {
//                    if (currentPageSurvey<9)
//                    {
//                        currentQntTxt.setText("0"+currentPageSurvey);
//                        questionCount.setText("/"+String.valueOf(surveyQuestionArrayList.size()));
//                    }
//                    else {
//                        currentQntTxt.setText(currentPageSurvey);
//                        questionCount.setText("/"+String.valueOf(surveyQuestionArrayList.size()));
//                    }
//
//                }
//                else {
//                    if (currentPageSurvey<9)
//                    {
//                        currentQntTxt.setText("0"+currentPageSurvey);
//                        questionCount.setText("/"+"0"+String.valueOf(surveyQuestionArrayList.size()));
//                    }
//                    else {
//                        currentQntTxt.setText(currentPageSurvey);
//                        questionCount.setText("/"+"0"+String.valueOf(surveyQuestionArrayList.size()));
//                    }
//                }
//
//                dialog.dismiss();
//            }
//        });
//        dialog.show();
//
//    }


}
