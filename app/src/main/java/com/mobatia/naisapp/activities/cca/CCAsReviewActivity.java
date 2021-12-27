package com.mobatia.naisapp.activities.cca;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.cca.adapter.CCAfinalReviewAdapter;
import com.mobatia.naisapp.activities.cca.model.CCADetailModel;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
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
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.HeaderManager;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CCAsReviewActivity extends Activity implements NaisTabConstants, CacheDIRConstants, URLConstants,
        IntentPassValueConstants, NaisClassNameConstants, JSONConstants, StatusConstants {
    GridLayoutManager recyclerViewLayoutManager;
    RecyclerView recycler_review;
    HeaderManager headermanager;
    RelativeLayout relativeHeader;
    ImageView back;
    Button submitBtn;
    ImageView home;
    String tab_type = "CCAs";
    Bundle extras;
    Context mContext;
    public ArrayList<CCADetailModel> mCCADetailModelArrayList;
    public ArrayList<String> mCCAItemIdArray;
    TextView textViewCCAaItem;
    String cca_details = "";
    String cca_detailsId = "[";
    CCADetailModel mCCADetailModels;
    int survey_satisfation_status=0;
    int currentPage = 0;
    int currentPageSurvey = 0;
    private int surveySize=0;
    int pos=-1;
    ArrayList<SurveyModel> surveyArrayList;
    ArrayList<SurveyQuestionsModel> surveyQuestionArrayList;
    ArrayList<SurveyAnswersModel> surveyAnswersArrayList;
    ArrayList<AnswerSubmitModel>mAnswerList;
    TextView text_content;
    TextView text_dialog;
    private String surveyEmail="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cca_review);
        mContext = this;
        extras = getIntent().getExtras();
        if (extras != null) {
            tab_type = extras.getString("tab_type");
        }
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        recycler_review = (RecyclerView) findViewById(R.id.recycler_view_cca);
        textViewCCAaItem = (TextView) findViewById(R.id.textViewCCAaItem);
        submitBtn = (Button) findViewById(R.id.submitBtn);
        headermanager = new HeaderManager(CCAsReviewActivity.this, "CCA Summary");//tab_type
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
        recycler_review.setHasFixedSize(true);
        recyclerViewLayoutManager = new GridLayoutManager(mContext, 1);
        recycler_review.setLayoutManager(recyclerViewLayoutManager);
        mCCADetailModelArrayList = new ArrayList<>();
        mCCAItemIdArray = new ArrayList<>();
        if (PreferenceManager.getStudClassForCCA(mContext).equalsIgnoreCase("")) {
            textViewCCAaItem.setText(Html.fromHtml(PreferenceManager.getCCATitle(mContext) + "<br/>" + PreferenceManager.getStudNameForCCA(mContext)));
        } else {
            textViewCCAaItem.setText(Html.fromHtml(PreferenceManager.getCCATitle(mContext) + "<br/>" + PreferenceManager.getStudNameForCCA(mContext) + "<br/>Year Group : " + PreferenceManager.getStudClassForCCA(mContext)));
        }
        for (int i = 0; i < AppController.weekList.size(); i++) {
            for (int j = 0; j < CCASelectionActivity.CCADetailModelArrayList.size(); j++) {
                if (AppController.weekList.get(i).getWeekDay().equalsIgnoreCase(CCASelectionActivity.CCADetailModelArrayList.get(j).getDay())) {
                    CCADetailModel mCCADetailModel = new CCADetailModel();
                    mCCADetailModel.setDay(CCASelectionActivity.CCADetailModelArrayList.get(j).getDay());
                    mCCADetailModel.setChoice1(CCASelectionActivity.CCADetailModelArrayList.get(j).getChoice1());
                    mCCADetailModel.setChoice2(CCASelectionActivity.CCADetailModelArrayList.get(j).getChoice2());
                    mCCADetailModel.setChoice1Id(CCASelectionActivity.CCADetailModelArrayList.get(j).getChoice1Id());
                    mCCADetailModel.setChoice2Id(CCASelectionActivity.CCADetailModelArrayList.get(j).getChoice2Id());
                    for (int k = 0; k < CCASelectionActivity.CCADetailModelArrayList.get(j).getCcaChoiceModel().size(); k++)
                        if (CCASelectionActivity.CCADetailModelArrayList.get(j).getChoice1().equalsIgnoreCase(CCASelectionActivity.CCADetailModelArrayList.get(j).getCcaChoiceModel().get(k).getCca_item_name())) {
                            if (CCASelectionActivity.CCADetailModelArrayList.get(j).getCcaChoiceModel().get(k).getCca_item_start_time() != null && CCASelectionActivity.CCADetailModelArrayList.get(j).getCcaChoiceModel().get(k).getCca_item_end_time() != null) {
                                mCCADetailModel.setCca_item_start_timechoice1(CCASelectionActivity.CCADetailModelArrayList.get(j).getCcaChoiceModel().get(k).getCca_item_start_time());
                                mCCADetailModel.setCca_item_end_timechoice1(CCASelectionActivity.CCADetailModelArrayList.get(j).getCcaChoiceModel().get(k).getCca_item_end_time());
                                mCCADetailModel.setLocation(CCASelectionActivity.CCADetailModelArrayList.get(j).getCcaChoiceModel().get(k).getVenue());
                                mCCADetailModel.setDescription(CCASelectionActivity.CCADetailModelArrayList.get(j).getCcaChoiceModel().get(k).getDescription());

                                break;
                            }
                            else {
                                mCCADetailModel.setLocation("0");
                                mCCADetailModel.setDescription("0");
                            }
                        }
                    for (int k = 0; k < CCASelectionActivity.CCADetailModelArrayList.get(j).getCcaChoiceModel2().size(); k++)
                        if (CCASelectionActivity.CCADetailModelArrayList.get(j).getChoice2().equalsIgnoreCase(CCASelectionActivity.CCADetailModelArrayList.get(j).getCcaChoiceModel2().get(k).getCca_item_name())) {
                            if (CCASelectionActivity.CCADetailModelArrayList.get(j).getCcaChoiceModel2().get(k).getCca_item_start_time() != null && CCASelectionActivity.CCADetailModelArrayList.get(j).getCcaChoiceModel2().get(k).getCca_item_end_time() != null) {
                                mCCADetailModel.setCca_item_start_timechoice2(CCASelectionActivity.CCADetailModelArrayList.get(j).getCcaChoiceModel2().get(k).getCca_item_start_time());
                                mCCADetailModel.setCca_item_end_timechoice2(CCASelectionActivity.CCADetailModelArrayList.get(j).getCcaChoiceModel2().get(k).getCca_item_end_time());
                                mCCADetailModel.setLocation2(CCASelectionActivity.CCADetailModelArrayList.get(j).getCcaChoiceModel2().get(k).getVenue());
                                mCCADetailModel.setDescription2(CCASelectionActivity.CCADetailModelArrayList.get(j).getCcaChoiceModel2().get(k).getDescription());
                                break;
                            }
                            else {
                                mCCADetailModel.setLocation2("0");
                                mCCADetailModel.setDescription2("0");
                            }
                        }
                    mCCADetailModelArrayList.add(mCCADetailModel);
                    break;
                }
            }
        }
        CCAfinalReviewAdapter mCCAsActivityAdapter = new CCAfinalReviewAdapter(mContext, mCCADetailModelArrayList);
        recycler_review.setAdapter(mCCAsActivityAdapter);
        for (int j = 0; j < mCCADetailModelArrayList.size(); j++) {

            if (mCCADetailModelArrayList.get(j).getChoice1() != null && mCCADetailModelArrayList.get(j).getChoice2() != null) {
                if (!(mCCADetailModelArrayList.get(j).getChoice1Id().equalsIgnoreCase("-541")) && !(mCCADetailModelArrayList.get(j).getChoice2Id().equalsIgnoreCase("-541"))) {

                    mCCAItemIdArray.add(mCCADetailModelArrayList.get(j).getChoice1Id());
                    mCCAItemIdArray.add(mCCADetailModelArrayList.get(j).getChoice2Id());
                } else if (!mCCADetailModelArrayList.get(j).getChoice1Id().equalsIgnoreCase("-541")) {
                    mCCAItemIdArray.add(mCCADetailModelArrayList.get(j).getChoice1Id());

                } else if (!mCCADetailModelArrayList.get(j).getChoice2Id().equalsIgnoreCase("-541")) {
                    mCCAItemIdArray.add(mCCADetailModelArrayList.get(j).getChoice2Id());

                }
            } else if (mCCADetailModelArrayList.get(j).getChoice1() != null) {
                if (!mCCADetailModelArrayList.get(j).getChoice1Id().equalsIgnoreCase("-541")) {
                    mCCAItemIdArray.add(mCCADetailModelArrayList.get(j).getChoice1Id());

                }
            } else if (mCCADetailModelArrayList.get(j).getChoice2() != null) {
                if (!mCCADetailModelArrayList.get(j).getChoice2Id().equalsIgnoreCase("-541")) {
                    mCCAItemIdArray.add(mCCADetailModelArrayList.get(j).getChoice2Id());

                }
            }


        }

        if (mCCAItemIdArray.size() == 0) {
            cca_detailsId += "]}";

        }
        for (int i = 0; i < mCCAItemIdArray.size(); i++) {

            if ((mCCAItemIdArray.size() - 1) == 0) {
                cca_detailsId += "\"" + mCCAItemIdArray.get(i).toString() + "\"]}";

            } else if (i == mCCAItemIdArray.size() - 1) {
                cca_detailsId += mCCAItemIdArray.get(i).toString() + "\"]}";
            } else if (i == 0) {
                cca_detailsId += "\"" + mCCAItemIdArray.get(i).toString() + "\",\"";

            } else {
                cca_detailsId += mCCAItemIdArray.get(i).toString() + "\",\"";

            }
        }
        cca_details = "{\"cca_days_id\":\"" + PreferenceManager.getCCAItemId(mContext) + "\",\"student_id\":\"" + PreferenceManager.getStudIdForCCA(mContext) + "\",\"users_id\":\"" + PreferenceManager.getUserId(mContext) + "\",\"cca_days_details_id\":" + cca_detailsId;

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialogReviewSubmit((Activity) mContext, "Confirm", "", R.drawable.exclamationicon, R.drawable.round);

                }
        });
    }

    private void ccaSubmitAPI() {
        VolleyWrapper volleyWrapper = new VolleyWrapper(URL_CCA_SUBMIT);
        String[] name = {"access_token", "cca_details","module"};
        String[] value = {PreferenceManager.getAccessToken(mContext), cca_details,"7"};
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

                            int survey=secobj.optInt("survey");
                            showDialogAlert((Activity) mContext, "Success", "You are able to make changes until the closing date. After the closing date selections are final", R.drawable.tickicon, R.drawable.round,survey);




                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        ccaSubmitAPI();

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        ccaSubmitAPI();

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        ccaSubmitAPI();

                    } else {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    }
                } catch (Exception ex) {
                }

            }

            @Override
            public void responseFailure(String failureResponse) {

            }
        });


    }

    public void showDialogAlert(final Activity activity, String msgHead, String msg, int ico, int bgIcon,int survey) {

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_dialogue_ok_layout);
        ImageView icon = (ImageView) dialog.findViewById(R.id.iconImageView);
        icon.setBackgroundResource(bgIcon);
        icon.setImageResource(ico);
        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        TextView textHead = (TextView) dialog.findViewById(R.id.alertHead);
        text.setText(msg);
        textHead.setText(msgHead);

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_Ok);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (survey==1)
                {
                    callSurveyApi();
                }
                else
                {
                    Intent intent = new Intent(mContext, CCA_Activity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }


            }
        });

        dialog.show();

    }

    public void showDialogReviewSubmit(final Activity activity, String msgHead, String msg, int ico, int bgIcon) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_dialogue_layout);
        ImageView icon = (ImageView) dialog.findViewById(R.id.iconImageView);
        icon.setBackgroundResource(bgIcon);
        icon.setImageResource(ico);
        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        TextView textHead = (TextView) dialog.findViewById(R.id.alertHead);
        text.setText(msg);
        textHead.setText(msgHead);

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_Ok);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (AppUtils.isNetworkConnected(mContext)) {
                    ccaSubmitAPI();
                } else {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", mContext.getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                }


            }
        });
        Button dialogButtonCancel = (Button) dialog.findViewById(R.id.btn_Cancel);
        dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    /**********************************SURVEY API***************************************/
    public void callSurveyApi() {
        surveyArrayList=new ArrayList<>();


        try {
            final VolleyWrapper manager = new VolleyWrapper(URL_GET_USER_SURVEY);
            String[] name = new String[]{JTAG_ACCESSTOKEN,"users_id","module"};
            String[] value = new String[]{PreferenceManager.getAccessToken(mContext),PreferenceManager.getUserId(mContext),"7"};
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

                                        PreferenceManager.setIsSurveyHomeVisible(mContext,true);
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
                                            showSurveyWelcomeDialogue((Activity) mContext,surveyArrayList,false);
                                        }
                                        else {
                                            showDialogAlert((Activity) mContext, "Success", "You are able to make changes until the closing date. After the closing date selections are final", R.drawable.tickicon, R.drawable.round,0);

                                        }

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
//        final Dialog dialog = new Dialog(activity,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
//        dialog.requestWindowFeature(R.style.full_screen_dialog);
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
//        else {
//			thankyouTxt.setVisibility(View.GONE);
//		}

        headingTxt.setText(surveyArrayList.get(pos+1).getTitle());
        descriptionTxt.setText(surveyArrayList.get(pos+1).getDescription());

        ImageView bannerImg = (ImageView) dialog.findViewById(R.id.bannerImg);
        if (!surveyArrayList.get(pos+1).getImage().equalsIgnoreCase(""))
        {
            Picasso.with(mContext).load(AppUtils.replace(surveyArrayList.get(pos+1).getImage())).placeholder(R.drawable.survey).fit().into(bannerImg);
        }
        else
        {
            bannerImg.setImageResource(R.drawable.survey);
        }
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
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showCloseSurveyDialog(activity,dialog);
            }
        });
        Button skipBtn = (Button) dialog.findViewById(R.id.skipBtn);
        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(mContext, CCA_Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //showCloseSurveyDialog(activity,dialog);
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
                Intent intent = new Intent(mContext, CCA_Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

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
        ImageView emailImg = (ImageView) dialog.findViewById(R.id.emailImg);
        if (surveyEmail.equalsIgnoreCase(""))
        {
            emailImg.setVisibility(View.GONE);
        }
        else
        {
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
                        sendEmailToStaff(URL_SEND_EMAIL_TO_STAFF,surveyEmail);
                    }
                });


                dialog.show();
            }
        });

        btn_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (isThankyou)
                {
                    Log.e("SURVEY SIZE","Thank");
                    showSurveyWelcomeDialogue(activity,surveyArrayList,true);

                }
                else
                {
                    Intent intent = new Intent(mContext, CCA_Activity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                dialog.dismiss();
            }
        });
        dialog.show();

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

                            showSurveyThankYouDialog((Activity) mContext,surveyArrayList,isThankyou);


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
        ImageView emailImg = (ImageView) dialog.findViewById(R.id.emailImg);
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
//        if (contactEmail.equalsIgnoreCase(""))
//		{
//			emailImg.setVisibility(View.GONE);
//		}
//        else {
//			emailImg.setVisibility(View.GONE);
//		}



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
                        callSurveySubmitApi(URL_SURVEY_SUBMIT,surveyID,JSON_STRING,(Activity) mContext,surveyArrayList,false,1);
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
                        callSurveySubmitApi(URL_SURVEY_SUBMIT,surveyID,JSON_STRING,(Activity) mContext,surveyArrayList,true,1);
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
                        callSurveySubmitApi(URL_SURVEY_SUBMIT,surveyID,JSON_STRING,(Activity) mContext,surveyArrayList,false,1);
                    }
                    else {
                        callSurveySubmitApi(URL_SURVEY_SUBMIT,surveyID,JSON_STRING,(Activity) mContext
                                ,surveyArrayList,true,1);

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

}
