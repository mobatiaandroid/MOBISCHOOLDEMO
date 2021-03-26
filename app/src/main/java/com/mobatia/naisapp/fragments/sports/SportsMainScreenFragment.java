package com.mobatia.naisapp.fragments.sports;

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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.sports.calendar.CalendarActivity;
import com.mobatia.naisapp.activities.sports.information.InformationActivity;
import com.mobatia.naisapp.activities.sports.teams.TeamActivity;
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

public class SportsMainScreenFragment extends Fragment implements
        NaisTabConstants,CacheDIRConstants, URLConstants,
        IntentPassValueConstants,NaisClassNameConstants,JSONConstants,StatusConstants {
    TextView mTitleTextView;

    private View mRootView;
    private Context mContext;
    //	private ListView mListView;
    private String mTitle;
    private String mTabId;
    private String description = "";
    private RelativeLayout relMain;
    TextView descriptionTV;
    public static TextView calendarDot;
    public static TextView fixtureDot;
    public static TextView informationDot;
    RelativeLayout mtitleRel;
    RelativeLayout CCAFRegisterRel;
    private ArrayList<SecondaryModel> mListViewArray;
    ImageView bannerImagePager;
    //	ViewPager bannerImagePager;
    ArrayList<String> bannerUrlImageArray;
//    private RecyclerView mListView;
    String contactEmail = "";
    EditText text_dialog;
    EditText text_content;
    ImageView sendEmail;
    RelativeLayout teamRelative;
    RelativeLayout fixtureRelative;
    RelativeLayout calendarRelative;
    RelativeLayout informationRelative;

    public SportsMainScreenFragment() {

    }

    public SportsMainScreenFragment(String title, String tabId) {
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
        mRootView = inflater.inflate(R.layout.fragment_sports_main_fragmant, container,
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
        mTitleTextView.setText(SPORTS);
        bannerImagePager = (ImageView) mRootView.findViewById(R.id.bannerImagePager);
        sendEmail = (ImageView) mRootView.findViewById(R.id.sendEmail);
//        mListView = (RecyclerView) mRootView.findViewById(R.id.mListView);
        descriptionTV = (TextView) mRootView.findViewById(R.id.descriptionTitle);
        calendarDot = (TextView) mRootView.findViewById(R.id.calendarDot);
        fixtureDot = (TextView) mRootView.findViewById(R.id.fixtureDot);
        informationDot = (TextView) mRootView.findViewById(R.id.informationDot);
        mtitleRel = (RelativeLayout) mRootView.findViewById(R.id.title);
        relMain = (RelativeLayout) mRootView.findViewById(R.id.relMain);
        teamRelative =(RelativeLayout)mRootView.findViewById(R.id.teamRelative);
        calendarRelative =(RelativeLayout)mRootView.findViewById(R.id.calendarRelative);
        fixtureRelative =(RelativeLayout)mRootView.findViewById(R.id.fixtureRelative);
        informationRelative =(RelativeLayout)mRootView.findViewById(R.id.informationRelative);
        relMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        if (AppUtils.checkInternet(mContext)) {

            getList();
        } else {
            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

        }
        teamRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, TeamActivity.class);
                intent.putExtra("tab_type", "Squad");
                startActivity(intent);
            }
        });
        informationRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CalendarActivity.class);
                intent.putExtra("tab_type", " Sports Calendar");
                startActivity(intent);

            }
        });
        fixtureRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, SportsActivity.class);
                intent.putExtra("tab_type", "Sports Fixtures");
                startActivity(intent);
            }
        });
        calendarRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, InformationActivity.class);
                intent.putExtra("tab_type", "Information");
                startActivity(intent);
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
                        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(text_dialog.getWindowToken(), 0);
                        imm.hideSoftInputFromWindow(text_content.getWindowToken(), 0);
                        dialog.dismiss();

                    }

                });

                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("submit btn clicked");
                        sendEmailToStaff(URL_SEND_EMAIL_TO_STAFF);
                    }
                });


                dialog.show();


            }
        });
    }


    public void getList() {

        try {
            mListViewArray = new ArrayList<>();
            final VolleyWrapper manager = new VolleyWrapper(URL_SPORTS_PDF);
            String[] name = new String[]{JTAG_ACCESSTOKEN,"users_id"};
            String[] value = new String[]{PreferenceManager.getAccessToken(mContext),PreferenceManager.getUserId(mContext)};


            manager.getResponseGET(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {

                @Override
                public void responseSuccess(String successResponse) {
                    System.out.println("response"+successResponse);
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
                                        String bannerImage = respObject.optString(JTAG_BANNER_IMAGE);
                                        description = respObject.optString(JTAG_DESCRIPTION);
                                        contactEmail = respObject.optString(JTAG_CONTACT_EMAIL);


                                        String sports_fixture_badge=respObject.optString("sports_badge");
                                        PreferenceManager.setSportsFixtureBadge(mContext,sports_fixture_badge);
                                        String sports_edited_fixture_badge=respObject.optString("sports_edited_badge");
                                        PreferenceManager.setSportsEditedFixtureBadge(mContext,sports_edited_fixture_badge);
                                        String sports_calendar_badge=respObject.optString("sports_calendar_badge");
                                        PreferenceManager.setSportsCalendarBadge(mContext,sports_calendar_badge);
                                        String sports_edited_calendar_badge=respObject.optString("sports_calendar_edited_badge");
                                        PreferenceManager.setSportsEditedCalendarBadge(mContext,sports_edited_calendar_badge);

                                        String sports_information_badge=respObject.optString("sports_information_badge");
                                        PreferenceManager.setSportsInformationBadge(mContext,sports_information_badge);
                                        String sports_information_edited_badge=respObject.optString("sports_information_edited_badge");
                                        PreferenceManager.setSportsEditedInformationBadge(mContext,sports_information_edited_badge);
                                        if (!bannerImage.equalsIgnoreCase("")) {
                                            Glide.with(mContext).load(AppUtils.replace(bannerImage)).centerCrop().into(bannerImagePager);

                                        } else {
                                            bannerImagePager.setBackgroundResource(R.drawable.default_banner);
//											bannerImagePager.setBackgroundResource(R.drawable.ccas_banner);

                                        }
                                        if ((PreferenceManager.getSportsFixtureBadge(mContext).equalsIgnoreCase("0"))&&PreferenceManager.getSportsEditedFixtureBadge(mContext).equalsIgnoreCase("0"))
                                        {
                                            fixtureDot.setVisibility(View.GONE);

                                        }
                                        else if ((PreferenceManager.getSportsFixtureBadge(mContext).equalsIgnoreCase("0"))&& !(PreferenceManager.getSportsEditedFixtureBadge(mContext).equalsIgnoreCase("0")))
                                        {
                                            fixtureDot.setVisibility(View.VISIBLE);
                                            fixtureDot.setText(respObject.optString("sports_edited_badge"));
                                            fixtureDot.setBackgroundResource(R.drawable.shape_circle_navy);

                                        }
                                        else if (!(PreferenceManager.getSportsFixtureBadge(mContext).equalsIgnoreCase("0"))&& (PreferenceManager.getSportsEditedFixtureBadge(mContext).equalsIgnoreCase("0")))
                                        {
                                            fixtureDot.setVisibility(View.VISIBLE);
                                            fixtureDot.setText(respObject.optString("sports_badge"));
                                            fixtureDot.setBackgroundResource(R.drawable.shape_circle_red);

                                        }
                                        else if (!(PreferenceManager.getSportsFixtureBadge(mContext).equalsIgnoreCase("0"))&& !(PreferenceManager.getSportsEditedFixtureBadge(mContext).equalsIgnoreCase("0")))
                                        {
                                            fixtureDot.setVisibility(View.VISIBLE);
                                            fixtureDot.setText(respObject.optString("sports_badge"));
                                            fixtureDot.setBackgroundResource(R.drawable.shape_circle_red);

                                        }

                                        if ((PreferenceManager.getSportsCalendarBadge(mContext).equalsIgnoreCase("0"))&&PreferenceManager.getSportsEditedCalendarBadge(mContext).equalsIgnoreCase("0"))
                                        {
                                            calendarDot.setVisibility(View.GONE);

                                        }
                                        else if ((PreferenceManager.getSportsCalendarBadge(mContext).equalsIgnoreCase("0"))&& !(PreferenceManager.getSportsEditedCalendarBadge(mContext).equalsIgnoreCase("0")))
                                        {
                                            calendarDot.setVisibility(View.VISIBLE);
                                            calendarDot.setText(respObject.optString("sports_calendar_edited_badge"));
                                            calendarDot.setBackgroundResource(R.drawable.shape_circle_navy);

                                        }
                                        else if (!(PreferenceManager.getSportsCalendarBadge(mContext).equalsIgnoreCase("0"))&& (PreferenceManager.getSportsEditedCalendarBadge(mContext).equalsIgnoreCase("0")))
                                        {
                                            calendarDot.setVisibility(View.VISIBLE);
                                            calendarDot.setText(respObject.optString("sports_calendar_badge"));
                                            calendarDot.setBackgroundResource(R.drawable.shape_circle_red);

                                        }
                                        else if (!(PreferenceManager.getSportsCalendarBadge(mContext).equalsIgnoreCase("0"))&& !(PreferenceManager.getSportsEditedCalendarBadge(mContext).equalsIgnoreCase("0")))
                                        {
                                            calendarDot.setVisibility(View.VISIBLE);
                                            calendarDot.setText(respObject.optString("sports_calendar_badge"));
                                            calendarDot.setBackgroundResource(R.drawable.shape_circle_red);

                                        }
                                        System.out.println("sports info badge"+PreferenceManager.getSportsInformationBadge(mContext));
                                        System.out.println("sports info edited badge"+PreferenceManager.getSportsEditedInformationBadge(mContext));
                                        if ((PreferenceManager.getSportsInformationBadge(mContext).equalsIgnoreCase("0"))&&PreferenceManager.getSportsEditedInformationBadge(mContext).equalsIgnoreCase("0"))
                                        {
                                            informationDot.setVisibility(View.GONE);

                                        }
                                        else if ((PreferenceManager.getSportsInformationBadge(mContext).equalsIgnoreCase("0"))&& !(PreferenceManager.getSportsEditedInformationBadge(mContext).equalsIgnoreCase("0")))
                                        {
                                            informationDot.setVisibility(View.VISIBLE);
                                            informationDot.setText(respObject.optString("sports_information_edited_badge"));
                                            informationDot.setBackgroundResource(R.drawable.shape_circle_navy);

                                        }
                                        else if (!(PreferenceManager.getSportsInformationBadge(mContext).equalsIgnoreCase("0"))&& (PreferenceManager.getSportsEditedInformationBadge(mContext).equalsIgnoreCase("0")))
                                        {
                                            informationDot.setVisibility(View.VISIBLE);
                                            informationDot.setText(respObject.optString("sports_information_badge"));
                                            informationDot.setBackgroundResource(R.drawable.shape_circle_red);

                                        }
                                        else if (!(PreferenceManager.getSportsInformationBadge(mContext).equalsIgnoreCase("0"))&& !(PreferenceManager.getSportsEditedInformationBadge(mContext).equalsIgnoreCase("0")))
                                        {
                                            informationDot.setVisibility(View.VISIBLE);
                                            informationDot.setText(respObject.optString("sports_information_badge"));
                                            informationDot.setBackgroundResource(R.drawable.shape_circle_red);

                                        }
                                        else {
                                            informationDot.setVisibility(View.GONE);

                                        }

                                        if (description.equalsIgnoreCase("") && contactEmail.equalsIgnoreCase("")) {
                                            mtitleRel.setVisibility(View.GONE);
                                        } else {
                                            mtitleRel.setVisibility(View.VISIBLE);
                                        }
                                        if (description.equalsIgnoreCase("")) {
                                            descriptionTV.setVisibility(View.GONE);
                                        } else {
                                            descriptionTV.setText(description);
                                            descriptionTV.setVisibility(View.VISIBLE);
                                            mtitleRel.setVisibility(View.VISIBLE);
                                        }
                                        if (contactEmail.equalsIgnoreCase("")) {
                                            sendEmail.setVisibility(View.GONE);
                                        } else {
                                            mtitleRel.setVisibility(View.VISIBLE);
                                            sendEmail.setVisibility(View.VISIBLE);
                                        }
//                                        CCAFRegisterRel.setVisibility(View.VISIBLE);//rijo commented
                                        JSONArray dataArray = respObject.getJSONArray(JTAG_RESPONSE_DATA_ARRAY);
                                        if (dataArray.length() > 0) {
                                            for (int i = 0; i < dataArray.length(); i++) {
                                                JSONObject dataObject = dataArray.getJSONObject(i);
                                                mListViewArray.add(getSearchValues(dataObject));

                                            }

//											mListView.setAdapter(new CustomSecondaryAdapter(getActivity(), mListViewArray));
//                                            mListView.setAdapter(new CCARecyclerAdapter(mContext, mListViewArray));

                                        }
                                    } else {
//										CustomStatusDialog(RESPONSE_FAILURE);
                                        //Toast.makeText(mContext,"Failure",Toast.LENGTH_SHORT).show();
                                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                                    }
                                } else if (responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) ||
                                        responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                                        responsCode.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
                                    AppUtils.postInitParam(getActivity(), new AppUtils.GetAccessTokenInterface() {
                                        @Override
                                        public void getAccessToken() {
                                        }
                                    });
                                    getList();

                                } else if (responsCode.equals(RESPONSE_ERROR)) {
//								CustomStatusDialog(RESPONSE_FAILURE);
                                    //Toast.makeText(mContext,"Failure", Toast.LENGTH_SHORT).show();
                                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                                }
                            } else {
//								CustomStatusDialog(RESPONSE_FAILURE);
                                //Toast.makeText(mContext,"Failure", Toast.LENGTH_SHORT).show();
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }

                @Override
                public void responseFailure(String failureResponse) {
                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);
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
        VolleyWrapper volleyWrapper = new VolleyWrapper(URL);
        String[] name = {"access_token", "email", "users_id", "title", "message"};
        String[] value = {PreferenceManager.getAccessToken(mContext), contactEmail, PreferenceManager.getUserId(mContext), text_dialog.getText().toString(), text_content.getText().toString()};//contactEmail

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

    @Override
    public void onResume() {
        super.onResume();
        getList();
    }
}






