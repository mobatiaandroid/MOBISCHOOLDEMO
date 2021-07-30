package com.mobatia.naisapp.fragments.universityguidance;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.cca.InformationCCAActivity;
import com.mobatia.naisapp.activities.universityguidance.calendar.CalendarGuidanceActivity;
import com.mobatia.naisapp.activities.universityguidance.guidanceessential.GuidanceEssentialsActivity;
import com.mobatia.naisapp.activities.universityguidance.information.InformationActivity;
import com.mobatia.naisapp.activities.universityguidance.information.adapter.GuidanceInformationRecyclerAdapter;
import com.mobatia.naisapp.constants.CacheDIRConstants;
import com.mobatia.naisapp.constants.IntentPassValueConstants;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.constants.NaisTabConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;
import org.json.JSONObject;

public class UniverstyGuidanceFragment extends Fragment implements AdapterView.OnItemClickListener,
        NaisTabConstants, CacheDIRConstants, URLConstants,
        IntentPassValueConstants, NaisClassNameConstants, JSONConstants, StatusConstants {
    TextView mTitleTextView;

    private View mRootView;
    private Context mContext;
    private String mTitle;
    private String mTabId;
    LinearLayout mtitleRel;
    ImageView bannerImagePager;
    ImageView mailImageView;
    String contactEmail = "", descriptionTxt = "";
    TextView text_content, description;

    TextView text_dialog,calDot;
    RelativeLayout essentialRelative, informationRelative, calendarRelative;

    public UniverstyGuidanceFragment() {

    }

    public UniverstyGuidanceFragment(String title, String tabId) {
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
        mRootView = inflater.inflate(R.layout.fragment_university_guidance, container,
                false);
        mContext = getActivity();
        initialiseUI();
        if (AppUtils.checkInternet(mContext)) {
            getBannerDetail(URL_GET_BANNER_GUIDANCE);
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
        mTitleTextView.setText(UNIVERSITY_GUIDANCE);

        mtitleRel = (LinearLayout) mRootView.findViewById(R.id.title);
        description = (TextView) mRootView.findViewById(R.id.description);
        calDot = (TextView) mRootView.findViewById(R.id.calDot);

        bannerImagePager = (ImageView) mRootView.findViewById(R.id.bannerImagePager);

        essentialRelative = (RelativeLayout) mRootView.findViewById(R.id.essentialRelative);
        informationRelative = (RelativeLayout) mRootView.findViewById(R.id.informationRelative);
        calendarRelative = (RelativeLayout) mRootView.findViewById(R.id.calendarRelative);
        if ((PreferenceManager.getUniversity_badge(mContext).equalsIgnoreCase("0"))&&PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase("0"))
        {
            calDot.setVisibility(View.GONE);

        }
        else if ((PreferenceManager.getUniversity_badge(mContext).equalsIgnoreCase("0"))&& !(PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase("0"))&& !(PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase("")) )
        {
            calDot.setVisibility(View.VISIBLE);
            calDot.setText(PreferenceManager.getUniversity_edit_badge(mContext));
            calDot.setBackgroundResource(R.drawable.shape_circle_navy);

        }
        else if (!(PreferenceManager.getUniversity_badge(mContext).equalsIgnoreCase("0"))&& (PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase("0")))
        {
            calDot.setVisibility(View.VISIBLE);
            calDot.setText(PreferenceManager.getUniversity_badge(mContext));
            calDot.setBackgroundResource(R.drawable.shape_circle_red);

        }
        else if (!(PreferenceManager.getUniversity_badge(mContext).equalsIgnoreCase("0"))&& !(PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase("0")))
        {
            calDot.setVisibility(View.VISIBLE);
            calDot.setText(PreferenceManager.getUniversity_badge(mContext));
            calDot.setBackgroundResource(R.drawable.shape_circle_red);

        }
        else {
            calDot.setVisibility(View.GONE);

        }
        informationRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, InformationActivity.class);
                intent.putExtra("tab_type", "Information");
                startActivity(intent);
            }
        });
        calendarRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calDot.setVisibility(View.GONE);
                Intent intent = new Intent(mContext, CalendarGuidanceActivity.class);
                intent.putExtra("tab_type", "Calendar");
                startActivity(intent);
            }
        });
        essentialRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, GuidanceEssentialsActivity.class);
                intent.putExtra("tab_type", "Guidance Essentials");
                startActivity(intent);
            }
        });

        mailImageView = (ImageView) mRootView.findViewById(R.id.mailImageView);
        mailImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PreferenceManager.getUserId(mContext).equalsIgnoreCase("")) {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "This feature is available for registered users", R.drawable.exclamationicon, R.drawable.round);
                } else {
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


    private void sendEmailToStaff(String URL) {
        VolleyWrapper volleyWrapper = new VolleyWrapper(URL);
        String[] name = {"access_token", "email", "users_id", "title", "message"};
        String[] value = {PreferenceManager.getAccessToken(mContext), contactEmail, PreferenceManager.getUserId(mContext), text_dialog.getText().toString(), text_content.getText().toString()};//contactEmail
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

                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    }
                } catch (Exception ex) {
                    System.out.println("The Exception in edit profile is" + ex.toString());
                }

            }

            @Override
            public void responseFailure(String failureResponse) {

                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

            }
        });
    }

    private void getBannerDetail(String URL) {
        VolleyWrapper volleyWrapper = new VolleyWrapper(URL);
        String[] name = {"access_token"};
        String[] value = {PreferenceManager.getAccessToken(mContext)};
        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("RESPONSE UNIVERSITY BANNER" + successResponse);
                try {
                    JSONObject obj = new JSONObject(successResponse);
                    String response_code = obj.getString(JTAG_RESPONSECODE);
                    if (response_code.equalsIgnoreCase("200")) {
                        JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                        String status_code = secobj.getString(JTAG_STATUSCODE);
                        if (status_code.equalsIgnoreCase("303")) {
                            String bannerImage = secobj.optString(JTAG_BANNER_IMAGE);
                            descriptionTxt = secobj.optString("description");
                            contactEmail = secobj.optString(JTAG_CONTACT_EMAIL);

                            if (!bannerImage.equalsIgnoreCase("")) {
                                Glide.with(mContext).load(AppUtils.replace(bannerImage)).centerCrop().into(bannerImagePager);
                            } else {
                                bannerImagePager.setBackgroundResource(R.drawable.default_banner);
                            }

                            if (descriptionTxt.equalsIgnoreCase("") && contactEmail.equalsIgnoreCase("")) {
                                mtitleRel.setVisibility(View.GONE);
                            } else {
                                mtitleRel.setVisibility(View.VISIBLE);
                            }

                            if (descriptionTxt.equalsIgnoreCase("")) {
                                description.setVisibility(View.GONE);
                            } else {
                                description.setText(descriptionTxt);
                                description.setVisibility(View.VISIBLE);
                                mtitleRel.setVisibility(View.VISIBLE);
                            }

                            if (contactEmail.equalsIgnoreCase("")) {
                                mailImageView.setVisibility(View.GONE);
                            } else {
                                mtitleRel.setVisibility(View.VISIBLE);
                                mailImageView.setVisibility(View.VISIBLE);
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
                        getBannerDetail(URL_CANTEEN_BANNER);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {

                            }
                        });
                        getBannerDetail(URL_CANTEEN_BANNER);

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {

                            }
                        });
                        getBannerDetail(URL_CANTEEN_BANNER);

                    }
                    else
                        {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);
                    }
                } catch (Exception ex) {
                    System.out.println("The Exception in edit profile is" + ex.toString());
                }

            }

            @Override
            public void responseFailure(String failureResponse)
            {
                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();


    }
}