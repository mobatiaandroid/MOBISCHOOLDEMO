package com.mobatia.naisapp.fragments.trips;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.parentsassociation.model.ParentAssociationEventsModel;
import com.mobatia.naisapp.activities.trips.PaymentCategoryListNew;
import com.mobatia.naisapp.activities.trips.PaymentInformationActivity;
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
import com.mobatia.naisapp.recyclerviewmanager.ItemOffsetDecoration;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TripsFragmentNew extends Fragment implements AdapterView.OnItemClickListener,
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
    ImageView back,home,imageViewSlotInfo,sendEmail;
    //RecyclerView mRecyclerView;
    String  tab_type;
    TextView signUpModule;
    TextView descriptionTV;
    TextView descriptionTitle;
    ArrayList<String> bannerUrlImageArray;
    RelativeLayout mtitle,informationRelative;
    ImageView bannerImagePager;
    boolean isShown=false;
    boolean isemoji2Selected=false;
    boolean isemoji3Selected=false;
    int survey_satisfation_status=0;
    int currentPage = 0;
    int currentPageSurvey = 0;
    private int surveySize=0;
    int pos=-1;
    ArrayList<AnswerSubmitModel>mAnswerList;
    ArrayList<SurveyModel> surveyArrayList;
    ArrayList<SurveyQuestionsModel> surveyQuestionArrayList;
    ArrayList<SurveyAnswersModel> surveyAnswersArrayList;
    ArrayList<ParentAssociationEventsModel> parentAssociationEventsModelsArrayList=new ArrayList<>();
    public TripsFragmentNew() {

    }

    public TripsFragmentNew(String title, String tabId) {
        this.mTitle = title;
        this.mTabId = tabId;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_payment_new, container,
                false);
        mContext = getActivity();
        TextView mTitleTextView = (TextView) mRootView.findViewById(R.id.titleTextView);
        mTitleTextView.setText("Payments");


        signRelative = (RelativeLayout) mRootView.findViewById(R.id.paymentRelative);
        mtitle = (RelativeLayout) mRootView.findViewById(R.id.title);
        informationRelative = (RelativeLayout) mRootView.findViewById(R.id.informationRelative);
        bannerImagePager= (ImageView) mRootView.findViewById(R.id.bannerImageViewPager);
       // mRecyclerView= (RecyclerView) mRootView.findViewById(R.id.mStaffDirectoryListView);
        signUpModule= (TextView) mRootView.findViewById(R.id.signUpModule);
        descriptionTV= (TextView) mRootView.findViewById(R.id.descriptionTV);
        descriptionTitle= (TextView) mRootView.findViewById(R.id.descriptionTitle);
        sendEmail= (ImageView) mRootView.findViewById(R.id.sendEmail);
       // mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        int spacing = 5; // 50px
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext,spacing);
       // mRecyclerView.addItemDecoration(itemDecoration);
       // mRecyclerView.setLayoutManager(llm);
       // mRecyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.list_divider_teal)));
        if(AppUtils.isNetworkConnected(mContext)) {
            callStaffDirectoryListAPI(URL_PAYMENT_BANNER);
        }else{
            AppUtils.showDialogAlertDismiss((Activity)mContext,"Network Error",getString(R.string.no_internet),R.drawable.nonetworkicon,R.drawable.roundred);

        }
        signRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, PaymentCategoryListNew.class);
                intent.putExtra("tab_type","Categories");
                intent.putExtra("email",contactEmail);
                startActivity(intent);

            }
        });
        informationRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, PaymentInformationActivity.class);
                intent.putExtra("informationArrayList",parentAssociationEventsModelsArrayList);
                startActivity(intent);

            }
        });
        /*mRecyclerView.addOnItemTouchListener(new RecyclerItemListener(mContext, mRecyclerView,
                new RecyclerItemListener.RecyclerTouchListener() {
                    public void onClickItem(View v, int position) {
                        if (parentAssociationEventsModelsArrayList.size() > 0)
                        {
                            if (parentAssociationEventsModelsArrayList.get(position).getPdfUrl().endsWith(".pdf")) {
                                Intent intent = new Intent(mContext, PdfReaderActivity.class);
                                intent.putExtra("pdf_url", parentAssociationEventsModelsArrayList.get(position).getPdfUrl());
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(mContext, LoadUrlWebViewActivity.class);
                                intent.putExtra("url", parentAssociationEventsModelsArrayList.get(position).getPdfUrl());
                                intent.putExtra("tab_type", tab_type);
                                startActivity(intent);
                            }

                        }
                    }

                    public void onLongClickItem(View v, int position) {
                        System.out.println("On Long Click Item interface");
                    }
                }));*/
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
    private void callStaffDirectoryListAPI(String URL) {
        parentAssociationEventsModelsArrayList=new ArrayList<>();
        VolleyWrapper volleyWrapper=new VolleyWrapper(URL);
        String[] name={"access_token","users_id"};
        String[] value={PreferenceManager.getAccessToken(mContext),PreferenceManager.getUserId(mContext)};
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
                            description = secobj.optString(JTAG_DESCRIPTION);
                            contactEmail = secobj.optString(JTAG_CONTACT_EMAIL);
                            System.out.println("banner img---" + bannerImage);
                            JSONObject canteenPayment=secobj.getJSONObject("payment_gateway_credentials");
                            PreferenceManager.setMerchantId(mContext,canteenPayment.optString("merchant_id"));
                            PreferenceManager.setAuthId(mContext,canteenPayment.optString("auth_id"));
                            PreferenceManager.setSessionUrl(mContext,canteenPayment.optString("session_url"));
                            PreferenceManager.setPaymentUrl(mContext,canteenPayment.optString("payment_url"));
                            PreferenceManager.setMerchantName(mContext,canteenPayment.optString("merchant_name"));
                            PreferenceManager.setMerchantPassword(mContext,canteenPayment.optString("password"));
                            System.out.println("values in api"+canteenPayment.optString("merchant_id"));
                            if (!bannerImage.equalsIgnoreCase("")) {
//                                bannerUrlImageArray = new ArrayList<>();
//                                bannerUrlImageArray.add(bannerImage);
//                                bannerImagePager.setAdapter(new ImagePagerDrawableAdapter(bannerUrlImageArray, mContext));
                                Glide.with(mContext).load(AppUtils.replace(bannerImage)).centerCrop().into(bannerImagePager);

                            } else {
                                bannerImagePager.setBackgroundResource(R.drawable.pabanner);

                            }
                            if (data.length() > 0) {
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject dataObject = data.getJSONObject(i);
                                    ParentAssociationEventsModel parentAssociationEventsModel=new ParentAssociationEventsModel();
                                    parentAssociationEventsModel.setPdfId(dataObject.optString("id"));
                                    parentAssociationEventsModel.setPdfTitle(dataObject.optString("title"));
                                    parentAssociationEventsModel.setPdfUrl(dataObject.optString("file"));
                                    parentAssociationEventsModelsArrayList.add(parentAssociationEventsModel);
                                }

                              //  PaymentRecyclerAdapter adapter=new PaymentRecyclerAdapter(mContext,parentAssociationEventsModelsArrayList);
                              //  mRecyclerView.setAdapter(adapter);

                            } else {
                                //Toast.makeText(mContext, "No data found", Toast.LENGTH_SHORT).show();
                            }
                            if (description.equalsIgnoreCase("")&&contactEmail.equalsIgnoreCase(""))
                            {
                                mtitle.setVisibility(View.GONE);
                            }
                            else
                            {
                                mtitle.setVisibility(View.VISIBLE);
                            }
                            if (description.equalsIgnoreCase(""))
                            {
                                descriptionTV.setVisibility(View.GONE);
                                descriptionTitle.setVisibility(View.GONE);
                            }else
                            {
                                descriptionTV.setText(description);
                                descriptionTitle.setVisibility(View.GONE);
                                descriptionTV.setVisibility(View.VISIBLE);
                                mtitle.setVisibility(View.VISIBLE);
                            }
                            System.out.println("contact email"+contactEmail);
                            if (contactEmail.equalsIgnoreCase(""))
                            {
                                sendEmail.setVisibility(View.GONE);
                            }else
                            {
                                mtitle.setVisibility(View.VISIBLE);

                                sendEmail.setVisibility(View.VISIBLE);
                            }


                        }
                    }
                    else if (response_code.equalsIgnoreCase("500"))
                    {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    }
                    else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        callStaffDirectoryListAPI(URL_PAYMENT_BANNER);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                                //callStaffDirectoryListAPI(URL_STAFFDIRECTORY_LIST);
                            }
                        });
                        callStaffDirectoryListAPI(URL_PAYMENT_BANNER);

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                                // callStaffDirectoryListAPI(URL_STAFFDIRECTORY_LIST);
                            }
                        });
                        callStaffDirectoryListAPI(URL_PAYMENT_BANNER);

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
    class PaymentRecyclerAdapter extends RecyclerView.Adapter<PaymentRecyclerAdapter.MyViewHolder> {

        private Context mContext;
        private ArrayList<ParentAssociationEventsModel> mnNewsLetterModelArrayList;
        String dept;


        public class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView imageIcon;
            TextView pdfTitle;
            public MyViewHolder(View view) {
                super(view);
                imageIcon = (ImageView) view.findViewById(R.id.imageIcon);
                pdfTitle = (TextView) view.findViewById(R.id.pdfTitle);



            }
        }


        public PaymentRecyclerAdapter(Context mContext, ArrayList<ParentAssociationEventsModel> mnNewsLetterModelArrayList) {
            this.mContext = mContext;
            this.mnNewsLetterModelArrayList = mnNewsLetterModelArrayList;

        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_pdf_adapter_row_new, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
//        holder.submenu.setText(mnNewsLetterModelArrayList.get(position).getSubmenu());
            holder.pdfTitle.setText(mnNewsLetterModelArrayList.get(position).getPdfTitle());
            holder.imageIcon.setVisibility(View.GONE);


        }


        @Override
        public int getItemCount() {
            return mnNewsLetterModelArrayList.size();
        }

    }

//
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
//            String[] value = new String[]{PreferenceManager.getAccessToken(mContext),PreferenceManager.getUserId(mContext),"5"};
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
//        Button skipBtn = (Button) dialog.findViewById(R.id.skipBtn);
//        skipBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showCloseSurveyDialog(activity,dialog);
//
//
//            }
//        });
//        headingTxt.setText(surveyArrayList.get(pos+1).getTitle());
//        descriptionTxt.setText(surveyArrayList.get(pos+1).getDescription());
//        ImageView bannerImg = (ImageView) dialog.findViewById(R.id.bannerImg);
//        if (!surveyArrayList.get(pos+1).getImage().equalsIgnoreCase(""))
//        {
//            Picasso.with(mContext).load(AppUtils.replace(surveyArrayList.get(pos+1).getImage())).placeholder(R.drawable.survey).fit().into(bannerImg);
//        }
//        else
//        {
//            bannerImg.setImageResource(R.drawable.survey);
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
//
//
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
//
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
//
//                            showSurveyThankYouDialog(getActivity(),surveyArrayList,isThankyou);
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

}
