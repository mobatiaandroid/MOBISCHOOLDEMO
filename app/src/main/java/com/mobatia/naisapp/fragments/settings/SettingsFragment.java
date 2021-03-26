/**
 *
 */
package com.mobatia.naisapp.fragments.settings;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.login.LoginActivity;
import com.mobatia.naisapp.activities.terms_of_service.TermsOfServiceActivity;
import com.mobatia.naisapp.activities.tutorial.TutorialActivity;
import com.mobatia.naisapp.constants.CacheDIRConstants;
import com.mobatia.naisapp.constants.IntentPassValueConstants;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.constants.NaisTabConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.settings.adapter.CustomSettingsAdapter;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rijo K Jose
 */

public class SettingsFragment extends Fragment implements OnItemClickListener,
        NaisTabConstants, CacheDIRConstants, URLConstants,
        IntentPassValueConstants, NaisClassNameConstants, JSONConstants {

    private View mRootView;
    private Context mContext;
    private ListView mSettingsList;
    private String mTitle;
    private String mTabId;
    private RelativeLayout relMain;
    private ImageView mBannerImage;
    TextView mTitleTextView;
    EditText text_currentpswd, newpassword, confirmpassword;
    boolean isRegUser;
    Dialog  dialog ;
    ArrayList<String> mSettingsListArray = new ArrayList<String>() {
        {
            add("Change App Settings");
            add("Terms of Service");
            add("Email Us");
            add("Tutorials");
            add("Logout");
        }
    };
    ArrayList<String> mSettingsListArrayRegistered = new ArrayList<String>() {
        {
            add("Change App Settings");
            add("Terms of Service");
            add("Email Us");
            add("Tutorials");
            add("Change Password");
            add("Logout");
        }
    };

    public SettingsFragment() {

    }

    public SettingsFragment(String title, String tabId) {
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
        mRootView = inflater.inflate(R.layout.fragment_settings_list, container,
                false);
//        setHasOptionsMenu(true);
        mContext = getActivity();
        dialog = new Dialog(mContext, R.style.NewDialog);
//		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(mContext));
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
        mSettingsList = (ListView) mRootView.findViewById(R.id.mSettingsListView);
        relMain = (RelativeLayout) mRootView.findViewById(R.id.relMain);
        relMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mTitleTextView.setText(SETTINGS);
        if (PreferenceManager.getUserId(mContext).equals(""))
        {
            if (PreferenceManager.getStaffId(mContext).equals(""))
            {
                isRegUser = false;
                mSettingsList.setAdapter(new CustomSettingsAdapter(getActivity(), mSettingsListArray));
            }
            else
            {
                isRegUser = true;
                mSettingsList.setAdapter(new CustomSettingsAdapter(getActivity(), mSettingsListArrayRegistered));
            }


        } else {


            isRegUser = true;
            mSettingsList.setAdapter(new CustomSettingsAdapter(getActivity(), mSettingsListArrayRegistered));

        }
        mSettingsList.setOnItemClickListener(this);

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
        if (isRegUser) {
            switch (position) {

                case 0://go to app settings
                    PreferenceManager.setGoToSettings(mContext, "1");

                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                    intent.setData(uri);
                    mContext.startActivity(intent);
//                    Toast.makeText(mContext, "If you change app permission in settings then app will restart. ", Toast.LENGTH_LONG).show();
//                    Intent in = new Intent(mContext, HomeListAppCompatActivity.class);
//                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    mContext.startActivity(in);
                    break;
                case 1://terms of service
                    Intent mIntent = new Intent(mContext, TermsOfServiceActivity.class);
                    mIntent.putExtra("tab_type", mSettingsListArray.get(position).toString());
                    mContext.startActivity(mIntent);
                    break;
                case 2://email us
                    String to = "communications@nasdubai.ae";
//                String subject=editTextSubject.getText().toString();
//                String message=editTextMessage.getText().toString();
                    Intent email = new Intent(Intent.ACTION_SEND);
                    email.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
//                email.putExtra(Intent.EXTRA_SUBJECT, subject);
//                email.putExtra(Intent.EXTRA_TEXT, message);

                    //need this to prompts email client only
                    email.setType("message/rfc822");

                    startActivity(Intent.createChooser(email, "Choose an Email client :"));
                    break;

                case 3://tutorial
                    Intent mintent = new Intent(mContext, TutorialActivity.class);
                    mintent.putExtra(TYPE, 0);

                    mContext.startActivity(mintent);
                    break;

                case 4://change password
                    if(AppUtils.isNetworkConnected(mContext)){
                        showChangePasswordAlert();
                    }else{
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                    }
                    break;
                case 5:
                    if(AppUtils.isNetworkConnected(mContext)){
                        AppUtils.showDialogAlertLogout(getActivity(), "Confirm", "Do you want to logout?", R.drawable.questionmark_icon, R.drawable.round);
                    }else{
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                    }
                    break;
                default:
                    break;

            }
        }
        else {
            switch (position) {

                case 0://go to app settings
                    PreferenceManager.setGoToSettings(mContext, "1");
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                    intent.setData(uri);
                    mContext.startActivity(intent);

//                    Toast.makeText(mContext, "If you change the permissions from settings, to apply the changes, app will restart.", Toast.LENGTH_LONG).show();

                    break;
                case 1://terms of service
                    Intent mIntent = new Intent(mContext, TermsOfServiceActivity.class);
                    mIntent.putExtra("tab_type", mSettingsListArray.get(position).toString());
                    mContext.startActivity(mIntent);
                    break;
                case 2://email us
                    Intent emailIntent = new Intent(
                            Intent.ACTION_SEND_MULTIPLE);
                    String[] deliveryAddress = {"communications@nasdubai.ae"};
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, deliveryAddress);
                    emailIntent.setType("text/plain");
                    emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    PackageManager pm = mContext.getPackageManager();
                    List<ResolveInfo> activityList = pm.queryIntentActivities(
                            emailIntent, 0);
                    System.out.println("packge size" + activityList.size());
                    for (final ResolveInfo app : activityList) {
                        System.out.println("packge name" + app.activityInfo.name);
                        if ((app.activityInfo.name).contains("com.google.android.gm")) {
                            final ActivityInfo activity = app.activityInfo;
                            final ComponentName name = new ComponentName(
                                    activity.applicationInfo.packageName, activity.name);
                            emailIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                            emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                            emailIntent.setComponent(name);
                            mContext.startActivity(emailIntent);
                            break;
                        }
                    }
                    break;

                case 3://tutorial
                    Intent mintent = new Intent(mContext, TutorialActivity.class);
                    mintent.putExtra(TYPE, 0);

                    mContext.startActivity(mintent);
                    break;

                case 4:
                    AppUtils.showDialogAlertLogout(getActivity(), "Confirm?", "Do you want to logout?", R.drawable.questionmark_icon, R.drawable.round);
                    break;
                default:
                    break;

            }
        }
//		Fragment fragment = new BrowserWebContents(mAboutUsListArray.get(
//				position).getCategoryName(), TAB_ABOUT);//
//		Bundle bundle = new Bundle();
//		bundle.putSerializable(URL_LINK, mAboutUsListArray.get(position)
//				.getCategoryUrl());
//		bundle.putSerializable(MESSAGE, mAboutUsListArray.get(position)
//				.getCategoryName());
//		bundle.putInt(POSITION, position);
//		fragment.setArguments(bundle);
//		if (fragment != null) {
//			FragmentManager fragmentManager = getActivity()
//					.getSupportFragmentManager();
//			fragmentManager.beginTransaction()
//					.add(R.id.frame_container, fragment, mTitle)
//					.addToBackStack(mTitle).commit();
//		}
    }

    private void showChangePasswordAlert() {
        dialog = new Dialog(mContext, R.style.NewDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_changepassword);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        // set the custom dialog components - edit text, button
        int sdk = android.os.Build.VERSION.SDK_INT;
        text_currentpswd = (EditText) dialog.findViewById(R.id.text_currentpassword);
        newpassword = (EditText) dialog.findViewById(R.id.text_currentnewpassword);
        confirmpassword = (EditText) dialog.findViewById(R.id.text_confirmpassword);

        Button dialogSubmitButton = (Button) dialog
                .findViewById(R.id.btn_changepassword);

        // if button is clicked, close the custom dialog
        dialogSubmitButton.setOnClickListener(new View.OnClickListener() {
                                                  @Override
                                                  public void onClick(View v) {
                                                      AppUtils.hideKeyboard(mContext, newpassword);
                                                      AppUtils.hideKeyboard(mContext, text_currentpswd);
                                                      AppUtils.hideKeyboard(mContext, confirmpassword);
                                                      if (text_currentpswd.getText().toString().trim().length() == 0) {
                                                          AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.enter_current_password), R.drawable.infoicon, R.drawable.round);

                                                      } else if (newpassword.getText().toString().trim().length() == 0) {
                                                          AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.enter_new_password), R.drawable.infoicon, R.drawable.round);

                                                          //newpassword.setError(mContext.getResources().getString(R.string.mandatory_field));
                                                      } else if (confirmpassword.getText().toString().trim().length() == 0) {
                                                          AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.enter_confirm_password), R.drawable.infoicon, R.drawable.round);

                                                          //confirmpassword.setError(mContext.getResources().getString(R.string.mandatory_field));
                                                      } else if (!newpassword.getText().toString().trim().equals(confirmpassword.getText().toString().trim())) {
                                                          //confirmpassword.setError(mContext.getResources().getString(R.string.password_mismatch));
                                                          AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.password_mismatch), R.drawable.infoicon, R.drawable.round);

                                                      } else {
                                                          if (!PreferenceManager.getUserId(mContext).equals(""))
                                                          {
                                                              if (AppUtils.isNetworkConnected(mContext)) {
                                                                  callChangePasswordAPI(URL_CHANGEPSAAWORD);
                                                              } else {
                                                                  AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                                                              }
                                                          }
                                                          else
                                                          {
                                                              if (AppUtils.isNetworkConnected(mContext)) {
                                                                  callChangePasswordStaffAPI(URL_STAFF_CHANGE_PASSWORD);
                                                              } else {
                                                                  AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                                                              }
                                                          }


                                                      }
                                                  }
                                              }

        );

        Button dialogCancel = (Button) dialog.findViewById(R.id.btn_cancel);
        dialogCancel.setOnClickListener(new View.OnClickListener()

                                        {
                                            @Override
                                            public void onClick(View v) {
                                                AppUtils.hideKeyboard(mContext, newpassword);
                                                AppUtils.hideKeyboard(mContext, text_currentpswd);
                                                AppUtils.hideKeyboard(mContext, confirmpassword);

                                                dialog.dismiss();
                                            }
                                        }

        );
        dialog.show();
    }
    private void callChangePasswordAPI(String URL) {
        VolleyWrapper volleyWrapper = new VolleyWrapper(URL);
        String[] name = {"access_token", "users_id", "current_password", "new_password", "email", "deviceid", "devicetype"};
        String[] value = {PreferenceManager.getAccessToken(mContext), PreferenceManager.getUserId(mContext),
                text_currentpswd.getText().toString(),
                newpassword.getText().toString(), PreferenceManager.getUserEmail(mContext),
                FirebaseInstanceId.getInstance().getToken(), "2"};

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
                            /*JSONArray responseArray = secobj.getJSONArray(JTAG_RESPONSE_ARRAY);
							for (int i = 0; i < responseArray.length(); i++) {
								JSONObject respObj = responseArray.getJSONObject(i);
								PreferenceManager.setUserId(mContext, respObj.optString(JTAG_USERS_ID));
							}*/
                            dialog.dismiss();

                            showDialogSignUpAlert((Activity) mContext, "Success", getString(R.string.succ_pswd), R.drawable.tick, R.drawable.round,mContext);

                        } else if (status_code.equalsIgnoreCase("301")) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.missing_parameter), R.drawable.infoicon, R.drawable.round);

                        } else if (status_code.equalsIgnoreCase("304")) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.email_exists), R.drawable.infoicon, R.drawable.round);

                        } else if (status_code.equalsIgnoreCase("305")) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.incrct_usernamepswd), R.drawable.infoicon, R.drawable.round);

                        } else if (status_code.equals("306")) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.invalid_email), R.drawable.infoicon, R.drawable.round);

                        } else {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.common_error), R.drawable.infoicon, R.drawable.round);

                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                        AppUtils.showDialogAlertDismiss((Activity)mContext,"Alert",getString(R.string.common_error),R.drawable.exclamationicon,R.drawable.round);

                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {

                            }
                        });
                        callChangePasswordAPI(URL_CHANGEPSAAWORD);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        callChangePasswordAPI(URL_CHANGEPSAAWORD);

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        callChangePasswordAPI(URL_CHANGEPSAAWORD);

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
    private void callChangePasswordStaffAPI(String URL) {
        VolleyWrapper volleyWrapper = new VolleyWrapper(URL);
        String[] name = {"access_token", "staff_id", "current_password", "new_password", "email", "deviceid", "devicetype"};
        String[] value = {PreferenceManager.getAccessToken(mContext), PreferenceManager.getStaffId(mContext),
                text_currentpswd.getText().toString(),
                newpassword.getText().toString(), PreferenceManager.getUserEmail(mContext),
                FirebaseInstanceId.getInstance().getToken(), "2"};

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
                            /*JSONArray responseArray = secobj.getJSONArray(JTAG_RESPONSE_ARRAY);
							for (int i = 0; i < responseArray.length(); i++) {
								JSONObject respObj = responseArray.getJSONObject(i);
								PreferenceManager.setUserId(mContext, respObj.optString(JTAG_USERS_ID));
							}*/
                            dialog.dismiss();

                            showDialogSignUpAlert((Activity) mContext, "Success", getString(R.string.succ_pswd), R.drawable.tick, R.drawable.round,mContext);

                        } else if (status_code.equalsIgnoreCase("301")) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.missing_parameter), R.drawable.infoicon, R.drawable.round);

                        } else if (status_code.equalsIgnoreCase("304")) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.email_exists), R.drawable.infoicon, R.drawable.round);

                        } else if (status_code.equalsIgnoreCase("305")) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.incrct_usernamepswd), R.drawable.infoicon, R.drawable.round);

                        } else if (status_code.equals("306")) {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.invalid_email), R.drawable.infoicon, R.drawable.round);

                        } else {
                            AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.common_error), R.drawable.infoicon, R.drawable.round);

                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                        AppUtils.showDialogAlertDismiss((Activity)mContext,"Alert",getString(R.string.common_error),R.drawable.exclamationicon,R.drawable.round);

                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {

                            }
                        });
                        callChangePasswordStaffAPI(URL_STAFF_CHANGE_PASSWORD);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        callChangePasswordStaffAPI(URL_STAFF_CHANGE_PASSWORD);

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        callChangePasswordStaffAPI(URL_CHANGEPSAAWORD);

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



    public  void showDialogSignUpAlert(final Activity activity, String msgHead, String msg, int ico, int bgIcon, final Context con) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
                PreferenceManager.setUserId(activity, "");
                PreferenceManager.setStaffId(activity, "");
                dialog.dismiss();
                Intent mIntent = new Intent(activity, LoginActivity.class);
                mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                activity.startActivity(mIntent);
            }
        });

        dialog.show();

    }

}
