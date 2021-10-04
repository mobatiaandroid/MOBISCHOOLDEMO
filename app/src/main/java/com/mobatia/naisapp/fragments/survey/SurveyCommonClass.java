package com.mobatia.naisapp.fragments.survey;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.mobatia.naisapp.R;
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
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SurveyCommonClass implements
        NaisTabConstants, CacheDIRConstants, URLConstants,
        IntentPassValueConstants, NaisClassNameConstants, JSONConstants, StatusConstants {

    static boolean isShown=false;
    static int survey_satisfation_status=0;
    static int currentPage = 0;
    static int currentPageSurvey = 0;
    private static int surveySize=0;
    static int pos=-1;
    static ArrayList<AnswerSubmitModel> mAnswerList;
    static ArrayList<SurveyModel> surveyArrayList;
    static ArrayList<SurveyQuestionsModel> surveyQuestionArrayList;
    static ArrayList<SurveyAnswersModel> surveyAnswersArrayList;
    TextView text_content;
    TextView text_dialog;
//
//    public static void callSurveyApi(String module, Context mContext, Activity activity) {
//        surveyArrayList=new ArrayList<>();
//
//
//        try {
//            final VolleyWrapper manager = new VolleyWrapper(URL_GET_USER_SURVEY);
//            String[] name = new String[]{JTAG_ACCESSTOKEN,"users_id","module"};
//            String[] value = new String[]{PreferenceManager.getAccessToken(mContext),PreferenceManager.getUserId(mContext),"1"};
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
//                                            showSurveyWelcomeDialogue(activity,surveyArrayList,false,mContext);
//                                        }
//
//                                    }
//                                }
//                                else if (responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) ||
//                                        responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
//                                        responsCode.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
//                                    AppUtils.postInitParam(activity, new AppUtils.GetAccessTokenInterface() {
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
//
//
//    public static void showSurveyWelcomeDialogue(final Activity activity, final ArrayList<SurveyModel> surveyArrayList,final Boolean isThankyou,Context mContext)
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
//        Button skipBtn = (Button) dialog.findViewById(R.id.skipBtn);
//        skipBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//
//
//            }
//        });
//        headingTxt.setText(surveyArrayList.get(pos+1).getTitle());
//        descriptionTxt.setText(surveyArrayList.get(pos+1).getDescription());
//        ImageView bannerImg = (ImageView) dialog.findViewById(R.id.bannerImg);
//        if (!surveyArrayList.get(pos+1).getImage().equalsIgnoreCase(""))
//        {
//            Picasso.with(mContext).load(AppUtils.replace(surveyArrayList.get(pos+1).getImage())).placeholder(R.drawable.wallet_survey).fit().into(bannerImg);
//        }
//        else
//        {
//            bannerImg.setImageResource(R.drawable.wallet_survey);
//        }
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
//                        showSurveyQuestionAnswerDialog(activity,surveyArrayList.get(pos).getSurveyQuestionsArrayList(),surveyArrayList.get(pos).getSurvey_name(),surveyArrayList.get(pos).getId(),surveyArrayList.get(pos).getContact_email(),mContext);
//                    }
//                }
//
//            }
//        });
//        imgClose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//        dialog.show();
//
//    }
//
//
//    public static void showSurveyQuestionAnswerDialog(final Activity activity, final ArrayList<SurveyQuestionsModel> surveyQuestionArrayList,final String surveyname,String surveyID,String contactEmail,Context mContext)
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
//        AppController.mAnswerArrayList=new ArrayList<>();
//        AppController.question_id="";
//        AppController.answer_id="";
//        ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.progressBar);
//        progressBar.setMax(surveyQuestionArrayList.size());
//        progressBar.getProgressDrawable().setColorFilter(
//                mContext.getResources().getColor(R.color.rel_one), android.graphics.PorterDuff.Mode.SRC_IN);
//        if (surveyQuestionArrayList.size()>9)
//        {
//            questionCount.setText("01/"+String.valueOf(surveyQuestionArrayList.size()));
//        }
//        else {
//            questionCount.setText("01/"+"0"+String.valueOf(surveyQuestionArrayList.size()));
//        }
//
//        RelativeLayout nextQuestionBtn = (RelativeLayout) dialog.findViewById(R.id.nextQuestionBtn);
//        nextQuestionBtn.setClickable(false);
//        if (surveyQuestionArrayList.size()==1)
//        {
//            nextQuestionBtn.setAlpha(1.0f);
//            nextQuestionBtn.setClickable(true);
//            nxtQnt.setText("Submit");
//        }
//        else {
//            nextQuestionBtn.setAlpha(0.5f);
//            nextQuestionBtn.setClickable(false);
//            nxtQnt.setText("Next Question");
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
//
//        nextQuestionBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (surveyQuestionArrayList.size()>0)
//                {
//                    if (currentPageSurvey==surveyQuestionArrayList.size())
//                    {
//                        skipBtn.setVisibility(View.INVISIBLE);
//                        progressBar.setProgress(currentPageSurvey);
//                        nextQuestionBtn.setAlpha(1.0f);
//                        nextQuestionBtn.setClickable(true);
//                        nxtQnt.setText("Submit");
//                        surveySize=surveySize-1;
//                        progressBar.setProgress(surveyQuestionArrayList.size());
//                        AnswerSubmitModel model=new AnswerSubmitModel();
//                        model.setAnswer_id(AppController.answer_id);
//                        model.setQuestion_id(AppController.question_id);
//                        AppController.mAnswerArrayList.add(model);
//                        if (surveySize<=0)
//                        {
//                            mAnswerList=new ArrayList<>();
//                            for (int j=0;j<surveyQuestionArrayList.size();j++)
//                            {
//                                AnswerSubmitModel mmodel=new AnswerSubmitModel();
//                                for (int k=0;k<surveyQuestionArrayList.get(j).getSurveyAnswersrrayList().size();k++)
//                                {
//                                    String answerId="";
//                                    mmodel.setQuestion_id(surveyQuestionArrayList.get(j).getId());
//                                    if (surveyQuestionArrayList.get(j).getSurveyAnswersrrayList().get(k).isClicked())
//                                    {
//                                        Log.e("CLICK ","ITS CLICKED"+" k "+String.valueOf(k));
//                                        answerId=surveyQuestionArrayList.get(j).getSurveyAnswersrrayList().get(k).getId();
//                                        Log.e("CLICK Ans ","ITS CLICKED"+" k "+String.valueOf(k));
//                                        mmodel.setAnswer_id(answerId);
//                                    }
//                                }
//                                mAnswerList.add(mmodel);
//                            }
//                            Gson gson   = new Gson();
//                            ArrayList<String> PassportArray = new ArrayList<>();
//                            for (int i=0;i<mAnswerList.size();i++)
//                            {
//                                AnswerSubmitModel nmodel=new AnswerSubmitModel();
//                                nmodel.setAnswer_id(mAnswerList.get(i).getAnswer_id());
//                                nmodel.setQuestion_id(mAnswerList.get(i).getQuestion_id());
//                                //	Log.e("Question ID",AppController.mAnswerArrayList.get(i).getQuestion_id());
//                                //Log.e("Answer ID",AppController.mAnswerArrayList.get(i).getAnswer_id());
//                                String json = gson.toJson(nmodel);
//                                PassportArray.add(i,json);
//                            }
//                            String JSON_STRING=""+PassportArray+"";
//                            Log.e("JSON",JSON_STRING);
//                            dialog.dismiss();
//                            callSurveySubmitApi(URL_SURVEY_SUBMIT,surveyID,JSON_STRING,getActivity(),surveyArrayList,false,1);
//
//                            // showSurveyThankYouDialog(getActivity(),surveyArrayList,false,JSON_STRING,surveyID);
//
//                        }
//                        else {
//
//                            mAnswerList=new ArrayList<>();
//                            for (int j=0;j<surveyQuestionArrayList.size();j++)
//                            {
//                                AnswerSubmitModel mmodel=new AnswerSubmitModel();
//                                for (int k=0;k<surveyQuestionArrayList.get(j).getSurveyAnswersrrayList().size();k++)
//                                {
//                                    String answerId="";
//                                    mmodel.setQuestion_id(surveyQuestionArrayList.get(j).getId());
//                                    if (surveyQuestionArrayList.get(j).getSurveyAnswersrrayList().get(k).isClicked())
//                                    {
//                                        Log.e("CLICK ","ITS CLICKED"+" k "+String.valueOf(k));
//                                        answerId=surveyQuestionArrayList.get(j).getSurveyAnswersrrayList().get(k).getId();
//                                        Log.e("CLICK Ans ","ITS CLICKED"+" k "+String.valueOf(k));
//                                        mmodel.setAnswer_id(answerId);
//                                    }
//                                }
//                                mAnswerList.add(mmodel);
//                            }
//
//                            Gson gson   = new Gson();
//                            ArrayList<String> PassportArray = new ArrayList<>();
//                            for (int i=0;i<mAnswerList.size();i++)
//                            {
//                                AnswerSubmitModel nmodel=new AnswerSubmitModel();
//                                nmodel.setAnswer_id(mAnswerList.get(i).getAnswer_id());
//                                nmodel.setQuestion_id(mAnswerList.get(i).getQuestion_id());
//                                String json = gson.toJson(nmodel);
//                                PassportArray.add(i,json);
//                            }
//                            String JSON_STRING=""+PassportArray+"";
//                            Log.e("JSON1",JSON_STRING);
//
//                            dialog.dismiss();
//                            callSurveySubmitApi(URL_SURVEY_SUBMIT,surveyID,JSON_STRING,getActivity(),surveyArrayList,true,1);
//
//                            // showSurveyThankYouDialog(getActivity(),surveyArrayList,true,JSON_STRING,surveyID);
//                            //showSurveyWelcomeDialogue(getActivity(),surveyArrayList,true);
//                        }
//
//
//                    }
//
//                    else {
//                        AnswerSubmitModel model=new AnswerSubmitModel();
//                        model.setAnswer_id(AppController.answer_id);
//                        model.setQuestion_id(AppController.question_id);
//                        AppController.mAnswerArrayList.add(model);
//                        currentPageSurvey++;
//                        skipBtn.setVisibility(View.VISIBLE);
//                        progressBar.setProgress(currentPageSurvey);
//                        surveyPager.setCurrentItem(currentPageSurvey-1);
//                        if (surveyQuestionArrayList.size()==currentPageSurvey)
//                        {
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
//                                nxtQnt.setText("Next Question");
//                            }
//                            else
//                            {
//                                progressBar.setProgress(currentPageSurvey);
//                                nextQuestionBtn.setAlpha(1.0f);
//                                nextQuestionBtn.setClickable(true);
//                                nxtQnt.setText("Next Question");
//                            }
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
//                        skipBtn.setVisibility(View.INVISIBLE);
//                        nextQuestionBtn.setAlpha(1.0f);
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
//                        skipBtn.setClickable(true);
//                        AppController.answer_id="";
//                        skipBtn.setVisibility(View.VISIBLE);
//                        AppController.question_id=surveyQuestionArrayList.get(currentPageSurvey-1).getId();
//                        AnswerSubmitModel model=new AnswerSubmitModel();
//                        model.setAnswer_id(AppController.answer_id);
//                        model.setQuestion_id(AppController.question_id);
//                        AppController.mAnswerArrayList.add(model);
//                        currentPageSurvey++;
//                        progressBar.setProgress(currentPageSurvey);
//                        surveyPager.setCurrentItem(currentPageSurvey-1);
//                        if (surveyQuestionArrayList.size()==currentPageSurvey)
//                        {
//                            nextQuestionBtn.setAlpha(1.0f);
//                            nxtQnt.setText("Submit");
//                            nextQuestionBtn.setClickable(true);
//                        }
//                        else {
//                            nextQuestionBtn.setAlpha(0.5f);
//                            nextQuestionBtn.setClickable(false);
//                            nxtQnt.setText("Next Question");
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
//                }
//            }
//        });
//        currentPageSurvey=1;
//        surveyPager.setCurrentItem(currentPage-1);
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


}
