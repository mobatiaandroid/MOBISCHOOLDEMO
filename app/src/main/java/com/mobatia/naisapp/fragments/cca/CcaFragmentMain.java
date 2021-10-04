package com.mobatia.naisapp.fragments.cca;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.cca.CCA_Activity;
import com.mobatia.naisapp.activities.cca.ExternalProviderActivity;
import com.mobatia.naisapp.activities.cca.InformationCCAActivity;
import com.mobatia.naisapp.constants.CacheDIRConstants;
import com.mobatia.naisapp.constants.IntentPassValueConstants;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.constants.NaisTabConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.secondary.model.SecondaryModel;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CcaFragmentMain extends Fragment implements AdapterView.OnItemClickListener,
        NaisTabConstants,CacheDIRConstants, URLConstants,
        IntentPassValueConstants,NaisClassNameConstants,JSONConstants,StatusConstants {
    TextView mTitleTextView;
    TextView descriptionTV;
   public static TextView ccaDot;
    private View mRootView;
    private Context mContext;
    private String mTitle;
    private String mTabId;
    LinearLayout mtitleRel;
RelativeLayout externalCCA;
    RelativeLayout informationCCA;
    ImageView bannerImagePager;
    ImageView mailImageView;
    RelativeLayout ccaOption;
    String contactEmail="";
    private String description="";
    TextView text_content;
    TextView text_dialog;

    private ArrayList<SecondaryModel> mListViewArray;

    public CcaFragmentMain() {

    }

    public CcaFragmentMain(String title, String tabId) {
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
        mRootView = inflater.inflate(R.layout.fragment_cca_main, container,
                false);
        mContext = getActivity();
        initialiseUI();


        return mRootView;
    }

    /*******************************************************
     * Method name : initialiseUI Description : initialise UI elements
     * Parameters : nil Return type : void Date : Jan 12, 2015 Author : Vandana
     * Surendranath
     *****************************************************/
    private void initialiseUI() {
        mTitleTextView = (TextView) mRootView.findViewById(R.id.titleTextView);
        descriptionTV = (TextView) mRootView.findViewById(R.id.descriptionTitle);
        ccaDot = (TextView) mRootView.findViewById(R.id.ccaDot);
        mTitleTextView.setText(CCAS);
        mtitleRel = (LinearLayout) mRootView.findViewById(R.id.title);

        externalCCA=(RelativeLayout)mRootView.findViewById(R.id.epRelative);
        ccaOption=(RelativeLayout)mRootView.findViewById(R.id.CcaOptionRelative);
        informationCCA=(RelativeLayout)mRootView.findViewById(R.id.informationRelative);
        bannerImagePager=(ImageView)mRootView.findViewById(R.id.bannerImagePager);
        mailImageView=(ImageView)mRootView.findViewById(R.id.mailImageView);

        if (AppUtils.checkInternet(mContext)) {

            getList();
        }
        else
        {
            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

        }
        externalCCA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ExternalProviderActivity.class);
                intent.putExtra("tab_type", "External Providers");
                startActivity(intent);
            }
        });
        informationCCA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, InformationCCAActivity.class);
                intent.putExtra("tab_type", "Information");
                startActivity(intent);
            }
        });
        ccaOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(PreferenceManager.getUserId(mContext).equalsIgnoreCase("")))
                {
                    PreferenceManager.setStudIdForCCA(mContext,"");
                    Intent intent = new Intent(mContext, CCA_Activity.class);
                    intent.putExtra("tab_type", "CCA Options");
                    startActivity(intent);
                }
                else
                {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "This feature is available for Registered users only", R.drawable.exclamationicon, R.drawable.round);

                }

            }
        });
        mailImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PreferenceManager.getUserId(mContext).equalsIgnoreCase("")) {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert","This feature is available for registered users", R.drawable.exclamationicon, R.drawable.round);
                }
                else
                {
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


            }
        });


    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
    public void getList() {

        try {
            mListViewArray = new ArrayList<>();
            final VolleyWrapper manager = new VolleyWrapper(URL_CCAS_LIST);
            String[] name = new String[]{JTAG_ACCESSTOKEN,"users_id"};
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
                                    if (statusCode.equals(STATUS_SUCCESS)) {
                                        String bannerImage=respObject.optString(JTAG_BANNER_IMAGE);
                                        description = respObject.optString(JTAG_DESCRIPTION);
                                        contactEmail = respObject.optString(JTAG_CONTACT_EMAIL);
                                        PreferenceManager.setCcaOptionBadge(mContext,respObject.optString("cca_badge"));
                                        PreferenceManager.setCcaOptionEditedBadge(mContext,respObject.optString("cca_edited_badge"));
                                        if ((PreferenceManager.getCcaOptionBadge(mContext).equalsIgnoreCase("0"))&&PreferenceManager.getCcaOptionEditedBadge(mContext).equalsIgnoreCase("0"))
                                        {
                                            ccaDot.setVisibility(View.GONE);

                                        }
                                        else if ((PreferenceManager.getCcaOptionBadge(mContext).equalsIgnoreCase("0"))&& !(PreferenceManager.getCcaOptionEditedBadge(mContext).equalsIgnoreCase("0")))
                                        {
                                            ccaDot.setVisibility(View.VISIBLE);
                                            ccaDot.setText(respObject.optString("cca_edited_badge"));
                                            ccaDot.setBackgroundResource(R.drawable.shape_circle_navy);

                                        }
                                        else if (!(PreferenceManager.getCcaOptionBadge(mContext).equalsIgnoreCase("0"))&& (PreferenceManager.getCcaOptionEditedBadge(mContext).equalsIgnoreCase("0")))
                                        {
                                            ccaDot.setVisibility(View.VISIBLE);
                                            ccaDot.setText(respObject.optString("cca_badge"));
                                            ccaDot.setBackgroundResource(R.drawable.shape_circle_red);

                                        }
                                        else if (!(PreferenceManager.getCcaOptionBadge(mContext).equalsIgnoreCase("0"))&& !(PreferenceManager.getCcaOptionEditedBadge(mContext).equalsIgnoreCase("0")))
                                        {
                                            ccaDot.setVisibility(View.VISIBLE);
                                            ccaDot.setText(respObject.optString("cca_badge"));
                                            ccaDot.setBackgroundResource(R.drawable.shape_circle_red);

                                        }
                                        if (!bannerImage.equalsIgnoreCase("")) {
                                            Glide.with(mContext).load(AppUtils.replace(bannerImage)).centerCrop().into(bannerImagePager);

//											bannerUrlImageArray = new ArrayList<>();
//											bannerUrlImageArray.add(bannerImage);
//											bannerImagePager.setAdapter(new ImagePagerDrawableAdapter(bannerUrlImageArray, getActivity()));
                                        }
                                        else
                                        {
                                            bannerImagePager.setBackgroundResource(R.drawable.default_banner);
//											bannerImagePager.setBackgroundResource(R.drawable.ccas_banner);

                                        }
                                        System.out.println("contact mail"+contactEmail);
                                        if (description.equalsIgnoreCase("") && contactEmail.equalsIgnoreCase(""))
                                        {
                                            mtitleRel.setVisibility(View.GONE);
                                        }
                                        else
                                        {
                                            mtitleRel.setVisibility(View.VISIBLE);
                                        }
                                        if (description.equalsIgnoreCase(""))
                                        {
                                            descriptionTV.setVisibility(View.GONE);
                                          //  descriptionTitle.setVisibility(View.GONE);
                                        }else
                                        {
                                            descriptionTV.setText(description);
                                            descriptionTV.setVisibility(View.VISIBLE);
                                            mtitleRel.setVisibility(View.VISIBLE);
                                           // mtitleRel.setVisibility(View.VISIBLE);
                                        }
                                        if (contactEmail.equalsIgnoreCase(""))
                                        {
                                            System.out.println("contact mail1");
                                            mailImageView.setVisibility(View.GONE);
                                        }else
                                        {
                                            System.out.println("contact mail2");
                                           mtitleRel.setVisibility(View.VISIBLE);
                                            mailImageView.setVisibility(View.VISIBLE);
                                        }
                                        // CCAFRegisterRel.setVisibility(View.VISIBLE);
                                        JSONArray dataArray = respObject.getJSONArray(JTAG_RESPONSE_DATA_ARRAY);
                                        if (dataArray.length() > 0) {
                                            for (int i = 0; i <dataArray.length(); i++) {
                                                JSONObject dataObject = dataArray.getJSONObject(i);
                                                mListViewArray.add(getSearchValues(dataObject));

                                            }

//											mListView.setAdapter(new CustomSecondaryAdapter(getActivity(), mListViewArray));
                                           // mListView.setAdapter(new CCARecyclerAdapter(mContext, mListViewArray));

                                        } else {
                                            //CustomStatusDialog();
                                            //Toast.makeText(mContext,"Failure",Toast.LENGTH_SHORT).show();
                                            AppUtils.showDialogAlertDismiss((Activity)mContext,"Alert",getString(R.string.nodatafound),R.drawable.exclamationicon,R.drawable.round);

                                        }
                                    } else {
//										CustomStatusDialog(RESPONSE_FAILURE);
                                        //Toast.makeText(mContext,"Failure",Toast.LENGTH_SHORT).show();
                                        AppUtils.showDialogAlertDismiss((Activity)mContext,"Alert",getString(R.string.common_error),R.drawable.exclamationicon,R.drawable.round);

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
                                    getList();

                                }else if (responsCode.equals(RESPONSE_ERROR)) {
//								CustomStatusDialog(RESPONSE_FAILURE);
                                    //Toast.makeText(mContext,"Failure", Toast.LENGTH_SHORT).show();
                                    AppUtils.showDialogAlertDismiss((Activity)mContext,"Alert",getString(R.string.common_error),R.drawable.exclamationicon,R.drawable.round);

                                }
                            } else {
//								CustomStatusDialog(RESPONSE_FAILURE);
                                //Toast.makeText(mContext,"Failure", Toast.LENGTH_SHORT).show();
                                AppUtils.showDialogAlertDismiss((Activity)mContext,"Alert",getString(R.string.common_error),R.drawable.exclamationicon,R.drawable.round);

                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }

                @Override
                public void responseFailure(String failureResponse) {
                    AppUtils.showDialogAlertDismiss((Activity)mContext,"Alert",getString(R.string.common_error),R.drawable.exclamationicon,R.drawable.round);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    private SecondaryModel getSearchValues(JSONObject Object)
            throws JSONException {
        SecondaryModel mSecondaryModel = new SecondaryModel();
        mSecondaryModel.setmId(Object.getString(JTAG_ID));
        mSecondaryModel.setmFile(Object.getString(JTAG_TAB_FILE));
        mSecondaryModel.setmName(Object.getString(JTAG_TAB_NAME));
        return mSecondaryModel;
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
                } catch (Exception ex) {
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