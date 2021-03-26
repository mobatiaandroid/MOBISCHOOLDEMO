package com.mobatia.naisapp.activities.login;

import android.annotation.SuppressLint;
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
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.home.HomeListActivity;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.exception.ExceptionHandler;
import com.mobatia.naisapp.fragments.absence.LeaveRequestSubmissionActivity;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

@SuppressLint("NewApi")
public class LoginActivity extends Activity implements OnTouchListener,
		OnClickListener, URLConstants,JSONConstants {

	private Context mContext;
	private EditText mUserNameEdtTxt;
	private EditText mPasswordEdtTxt;
	private Button mNeedpasswordBtn;
	private Button mGuestUserButton;
	private Button mLoginBtn;
	private Button mSignUpBtn;
	private EditText mMailEdtText;
	private Button mHelpButton;
	private Button staffBtn;
	private RelativeLayout loginMain;
	private RelativeLayout mPrgrsRel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
//		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this,
//				LoginActivity.class));
		mContext = this;
		PreferenceManager.setStaffId(mContext,"");
		initialiseUI();
		/*AppUtils.hideKeyboardOnTouchOutside(loginMain, mContext,
				mUserNameEdtTxt);*/
		setListeners();
	}

	/*******************************************************
	 * Method name : initialiseUI Description : initialise UI elements
	 * Parameters : nil Return type : void Date : March 8, 2017 Author : RIJO K JOSE
	 *****************************************************/
	@SuppressWarnings("deprecation")
	public void initialiseUI() {
		loginMain = (RelativeLayout) findViewById(R.id.loginMain);
		mPrgrsRel = (RelativeLayout) findViewById(R.id.progressDialog);
		mUserNameEdtTxt = (EditText) findViewById(R.id.userEditText);
		mUserNameEdtTxt.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
										  KeyEvent event) {
				mUserNameEdtTxt.setFocusable(false);
				mUserNameEdtTxt.setFocusableInTouchMode(false);
				return false;
			}
		});
		mPasswordEdtTxt = (EditText) findViewById(R.id.passwordEditText);
		mPasswordEdtTxt.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
										  KeyEvent event) {
				mPasswordEdtTxt.setFocusable(false);
				mPasswordEdtTxt.setFocusableInTouchMode(false);
				return false;
			}
		});
		mHelpButton = (Button) findViewById(R.id.helpButton);
		mNeedpasswordBtn = (Button) findViewById(R.id.forgotPasswordButton);
		mGuestUserButton = (Button) findViewById(R.id.guestButton);
		mLoginBtn = (Button) findViewById(R.id.loginBtn);
		mSignUpBtn = (Button) findViewById(R.id.signUpButton);
		staffBtn = (Button) findViewById(R.id.staffBtn);
		int sdk = android.os.Build.VERSION.SDK_INT;
		if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
			mNeedpasswordBtn.setBackgroundDrawable(AppUtils
					.getButtonDrawableByScreenCathegory(mContext,
							R.drawable.forgotpassword,
							R.drawable.forgotpasswordpress));
			mGuestUserButton.setBackgroundDrawable(AppUtils
					.getButtonDrawableByScreenCathegory(mContext,
							R.drawable.guest, R.drawable.guestpress));
			staffBtn.setBackgroundDrawable(AppUtils
					.getButtonDrawableByScreenCathegory(mContext,
							R.drawable.staff, R.drawable.staffpress));
			mLoginBtn.setBackgroundDrawable(AppUtils
					.getButtonDrawableByScreenCathegory(mContext,
							R.drawable.login, R.drawable.loginpress));
			mSignUpBtn.setBackgroundDrawable(AppUtils
					.getButtonDrawableByScreenCathegory(mContext,
							R.drawable.signup_new, R.drawable.signuppress_new));
			mHelpButton.setBackgroundDrawable(AppUtils
					.getButtonDrawableByScreenCathegory(mContext,
							R.drawable.help, R.drawable.helppress));
		} else {
			mNeedpasswordBtn.setBackground(AppUtils
					.getButtonDrawableByScreenCathegory(mContext,
							R.drawable.forgotpassword,
							R.drawable.forgotpasswordpress));
			mGuestUserButton.setBackground(AppUtils
					.getButtonDrawableByScreenCathegory(mContext,
							R.drawable.guest, R.drawable.guestpress));
			staffBtn.setBackground(AppUtils
					.getButtonDrawableByScreenCathegory(mContext,
							R.drawable.staff, R.drawable.staffpress));
			mLoginBtn.setBackground(AppUtils
					.getButtonDrawableByScreenCathegory(mContext,
							R.drawable.login, R.drawable.loginpress));
			mSignUpBtn.setBackground(AppUtils
					.getButtonDrawableByScreenCathegory(mContext,
							R.drawable.signup_new, R.drawable.signuppress_new));
			mHelpButton.setBackground(AppUtils
					.getButtonDrawableByScreenCathegory(mContext,
							R.drawable.help, R.drawable.helppress));
		}
	}

	/*******************************************************
	 * Method name : setListeners Description : set listeners for UI elements
	 * Parameters : nil Return type : void Date :  March 8, 2017 Author : RIJO K JOSE
	 *****************************************************/
	public void setListeners() {
		mUserNameEdtTxt.setOnTouchListener(this);
		mPasswordEdtTxt.setOnTouchListener(this);
		mNeedpasswordBtn.setOnClickListener(this);
		mGuestUserButton.setOnClickListener(this);
		mLoginBtn.setOnClickListener(this);
		mSignUpBtn.setOnClickListener(this);
		mHelpButton.setOnClickListener(this);
		staffBtn.setOnClickListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnTouchListener#onTouch(android.view.View,
	 * android.view.MotionEvent)
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (v == mUserNameEdtTxt) {
				mUserNameEdtTxt.setFocusable(true);
				mUserNameEdtTxt.setFocusableInTouchMode(true);
			} else if (v == mPasswordEdtTxt) {
				mPasswordEdtTxt.setFocusable(true);
				mPasswordEdtTxt.setFocusableInTouchMode(true);
			} else if (v == mMailEdtText) {
				mMailEdtText.setFocusable(true);
				mMailEdtText.setFocusableInTouchMode(true);
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		if (v == mLoginBtn) {
			// login button
			AppUtils.hideKeyboard(mContext, mPasswordEdtTxt);
			// guest user button
			/*PreferenceManager.setUserName(mContext, "Rijo");
			Intent homeIntent = new Intent(mContext, HomeListActivity.class);
			startActivity(homeIntent);*/

			if(mUserNameEdtTxt.getText().toString().trim().equalsIgnoreCase("")){
				//AppUtils.setErrorForEditText(mUserNameEdtTxt,getString(R.string.mandatory_field));
				AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.enter_email), R.drawable.exclamationicon, R.drawable.round);

			}else if(!AppUtils.isValidEmail(mUserNameEdtTxt.getText().toString())){
				//AppUtils.setErrorForEditText(mUserNameEdtTxt,getString(R.string.mandatory_field));
				AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.enter_valid_email), R.drawable.exclamationicon, R.drawable.round);

			}else if(mPasswordEdtTxt.getText().toString().equalsIgnoreCase("")){
				//AppUtils.setErrorForEditText(mPasswordEdtTxt,getString(R.string.mandatory_field));
				AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.enter_password), R.drawable.exclamationicon, R.drawable.round);

			}else{
				//call api
				LoginApiCall(URL_LOGIN);
			}

		} else if (v == mGuestUserButton) {
			// guest user button
			AppUtils.hideKeyboard(mContext, mPasswordEdtTxt);
			PreferenceManager.setUserId(mContext, "");
			Intent homeIntent = new Intent(mContext, HomeListAppCompatActivity.class);
			startActivity(homeIntent);
			
		}
		else if (v == mSignUpBtn) {
			// sign up
			AppUtils.hideKeyboard(mContext, mPasswordEdtTxt);
			// Intent signUpIntent = new Intent(mContext, SignUpActivity.class);
			// startActivity(signUpIntent);
			if (AppUtils.checkInternet(mContext)) {
				showSignUpAlertDialog();
			} else {
				AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

			}
		}
		else if (v == mNeedpasswordBtn) {
			// need password button
			AppUtils.hideKeyboard(mContext, mPasswordEdtTxt);
			if (AppUtils.checkInternet(mContext)) {
				//showForgotpasswordAlertDialog();
				forgotPasswordApiCall();
			} else {
				AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

			}
		}
		else if (v == mHelpButton)
		{
			Intent emailIntent = new Intent(
					Intent.ACTION_SEND_MULTIPLE);
			String[] deliveryAddress = {"communications@nasdubai.ae"};
			emailIntent.putExtra(Intent.EXTRA_EMAIL, deliveryAddress);
			emailIntent.setType("text/plain");
			emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

			PackageManager pm = v.getContext().getPackageManager();
			List<ResolveInfo> activityList = pm.queryIntentActivities(
					emailIntent, 0);
			System.out.println("packge size" + activityList.size());
			for (final ResolveInfo app : activityList) {
				System.out.println("packge name" + app.activityInfo.name);
				if ((app.activityInfo.name).contains("com.google.android.gm"))
				{
					final ActivityInfo activity = app.activityInfo;
					final ComponentName name = new ComponentName(
							activity.applicationInfo.packageName, activity.name);
					emailIntent.addCategory(Intent.CATEGORY_LAUNCHER);
					emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
							| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
					emailIntent.setComponent(name);
					v.getContext().startActivity(emailIntent);
					break;
				}
			}
		}
		else if (v==staffBtn)
		{
			AppUtils.hideKeyboard(mContext, mPasswordEdtTxt);
			PreferenceManager.setUserId(mContext, "");
			Intent loginIntent = new Intent(mContext,
					StaffLoginActivity.class);
			startActivity(loginIntent);
			finish();
		}
	}

	/*******************************************************
	 * Method name : doValidation Description : do validation of edit texts
	 * Parameters : nil Return type : boolean Date :  March 8, 2017 Author : RIJO K JOSE
	 *****************************************************/
	private boolean doValidation() {
		if (mUserNameEdtTxt.getText().toString().trim().equalsIgnoreCase("")) {
			AppUtils.editTextValidationAlert(mUserNameEdtTxt, getResources()
					.getString(R.string.mandatory_field), mContext);
			AppUtils.setEdtTextTextChangelistener(mUserNameEdtTxt, mContext);
			return false;
		} else if (!AppUtils
				.isValidEmail(mUserNameEdtTxt.getText().toString())) {
			AppUtils.editTextValidationAlert(mUserNameEdtTxt, getResources()
					.getString(R.string.invalid_email), mContext);
			AppUtils.setEdtTextTextChangelistener(mUserNameEdtTxt, mContext);
			return false;
		} else if (mPasswordEdtTxt.getText().toString().equalsIgnoreCase("")) {
			AppUtils.editTextValidationAlert(mPasswordEdtTxt, getResources()
					.getString(R.string.mandatory_field), mContext);
			AppUtils.setEdtTextTextChangelistener(mPasswordEdtTxt, mContext);
			return false;
		} else {
			return true;
		}
	}

	/*******************************************************
	 * Method name : loginApiCall Description : login api call Parameters : nil
	 * Return type : void Date : Dec 11, 2014 Author : Rijo K Jose
	 *****************************************************/
	private void loginApiCall() {
//		LoginAsyncTask loginAsynTask = new LoginAsyncTask(URL_USER_LOGIN);
//		loginAsynTask.execute();
	}


	/*******************************************************
	 * Method name : showForgotpasswordAlertDialog Description : show forgot
	 * password alert Parameters : nil Return type : void Date :  March 8, 2017 Author : RIJO K JOSE
	 *****************************************************/
	/*private void showForgotpasswordAlertDialog() {
		// custom dialog
		final Dialog dialog = new Dialog(mContext, R.style.NewDialog);
		dialog.setContentView(R.layout.dialog_forgot_password);
		dialog.setCancelable(true);

		// set the custom dialog components - edit text, button
		int sdk = android.os.Build.VERSION.SDK_INT;
		mMailEdtText = (EditText) dialog.findViewById(R.id.text_dialog);
		mMailEdtText.setOnTouchListener(this);

		Button dialogSubmitButton = (Button) dialog
				.findViewById(R.id.submitButton);
		if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
			dialogSubmitButton.setBackgroundDrawable(AppUtils
					.getButtonDrawableByScreenCathegory(mContext,
							R.color.split_bg, R.color.list_selector));
		} else {
			dialogSubmitButton.setBackground(AppUtils
					.getButtonDrawableByScreenCathegory(mContext,
							R.color.split_bg, R.color.list_selector));
		}
		// if button is clicked, close the custom dialog
		dialogSubmitButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AppUtils.hideKeyboard(mContext, mMailEdtText);
				if (!mMailEdtText.getText().toString().equals("")) {
					if (AppUtils.isValidEmail(mMailEdtText.getText()
							.toString())) {
						// forgot password api call
						forgotPasswordApiCall(mMailEdtText.getText().toString());
						dialog.dismiss();
					} else {
						AppUtils.editTextValidationAlert(mMailEdtText,
								getResources()
										.getString(R.string.invalid_email),
								mContext);
						AppUtils.setEdtTextTextChangelistener(mMailEdtText,
								mContext);
					}
				} else {
					AppUtils.editTextValidationAlert(mMailEdtText,
							getResources().getString(R.string.mandatory_field),
							mContext);
					AppUtils.setEdtTextTextChangelistener(mMailEdtText,
							mContext);
				}
			}
		});
		dialog.show();
	}
*/
	/*******************************************************
	 * Method name : forgotPasswordApiCall Description : forgot password api
	 * call Parameters : emailId Return type : void Date : March 8, 2017 Author : RIJO K JOSE
	 *****************************************************/
	private void forgotPasswordApiCall() {
		final Dialog dialog = new Dialog(mContext, R.style.NewDialog);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_forgot_password);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.setCancelable(true);
		// set the custom dialog components - edit text, button
		int sdk = android.os.Build.VERSION.SDK_INT;
		mMailEdtText = (EditText) dialog.findViewById(R.id.text_dialog);
		mMailEdtText.setOnTouchListener(this);
		TextView alertHead= (TextView) dialog.findViewById(R.id.alertHead);
//
		Button dialogSubmitButton = (Button) dialog
				.findViewById(R.id.btn_signup);

		// if button is clicked, close the custom dialog
		dialogSubmitButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AppUtils.hideKeyboard(mContext, mMailEdtText);
				if (!mMailEdtText.getText().toString().trim().equalsIgnoreCase("")) {
					if (AppUtils.isValidEmail(mMailEdtText.getText()
							.toString())) {
						// sign up api call
						if(AppUtils.isNetworkConnected(mContext)) {
							sendForGotpassWord(URL_FORGOTPASSWORD);
						}else{
							AppUtils.showDialogAlertDismiss((Activity)mContext,"Network Error",getString(R.string.no_internet),R.drawable.nonetworkicon,R.drawable.roundred);

						}
						dialog.dismiss();
					} else {
						/*AppUtils.editTextValidationAlert(mMailEdtText,
								getResources()
										.getString(R.string.invalid_email),
								mContext);
						AppUtils.setEdtTextTextChangelistener(mMailEdtText,
								mContext);*/
						AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.invalid_email), R.drawable.exclamationicon, R.drawable.round);

					}
				} else {
					/*AppUtils.editTextValidationAlert(mMailEdtText,
							getResources().getString(R.string.mandatory_field),
							mContext);
					AppUtils.setEdtTextTextChangelistener(mMailEdtText,
							mContext);*/
					AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.enter_email), R.drawable.exclamationicon, R.drawable.round);

				}
			}
		});

		Button dialogMayBelaterutton= (Button) dialog.findViewById(R.id.btn_maybelater);
		dialogMayBelaterutton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AppUtils.hideKeyboard(mContext, mMailEdtText);

				dialog.dismiss();
			}
		});
		dialog.show();
	}

	/*******************************************************
	 * Method name : showAlreadyLoginAlert Description : show alert - user is
	 * already logged in another device Return Type : void Parameters type :
	 * activity, message, okBtnTitle, cancelBtnTitle, Date :  March 8, 2017 Author : RIJO K JOSE
	 *****************************************************/
	@SuppressLint("NewApi")
	private void showAlreadyLoginAlert(final Activity activity, String message,
			String okBtnTitle, String cancelBtnTitle, final String userName,
			final String password, final RelativeLayout progresslayout) {
		// custom dialog
		final Dialog dialog = new Dialog(activity, R.style.NewDialog);
		dialog.setContentView(R.layout.custom_alert_dialog);
		dialog.setCancelable(false);

		// set the custom dialog components - text, image, button
		TextView text = (TextView) dialog.findViewById(R.id.text);
		text.setText(message);
		int sdk = android.os.Build.VERSION.SDK_INT;

		Button dialogCancelButton = (Button) dialog
				.findViewById(R.id.dialogButtonCancel);
		dialogCancelButton.setText(cancelBtnTitle);
		if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
			dialogCancelButton.setBackgroundDrawable(AppUtils
					.getButtonDrawableByScreenCathegory(activity,
							R.color.split_bg, R.color.list_selector));
		} else {
			dialogCancelButton.setBackground(AppUtils
					.getButtonDrawableByScreenCathegory(activity,
							R.color.split_bg, R.color.list_selector));
		}
		// if button is clicked, close the custom dialog
		dialogCancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		Button dialogOkButton = (Button) dialog
				.findViewById(R.id.dialogButtonOK);
		dialogOkButton.setText(okBtnTitle);
		dialogOkButton.setVisibility(View.VISIBLE);
		if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
			dialogOkButton.setBackgroundDrawable(AppUtils
					.getButtonDrawableByScreenCathegory(activity,
							R.color.split_bg, R.color.list_selector));
		} else {
			dialogOkButton.setBackground(AppUtils
					.getButtonDrawableByScreenCathegory(activity,
							R.color.split_bg, R.color.list_selector));
		}
		// if button is clicked, close the custom dialog
		dialogOkButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				new LoginActivity().confirmLoginApiCall(activity, userName,
						password, progresslayout);
			}
		});
		dialog.show();
	}

	/*******************************************************
	 * Method name : confirmLoginApiCall Description : confirm login api call in
	 * new device Parameters : context, userName, password, progressLayout
	 * Return type : void Date :  March 8, 2017 Author : RIJO K JOSE
	 *****************************************************/
	private void confirmLoginApiCall(Context context, String userName,
			String password, RelativeLayout progressLayout) {
//		SecondDeviceLoginConfirmAsyncTask loginConfirmAsynTask = new SecondDeviceLoginConfirmAsyncTask(
//				URL_USER_LOGIN_ANOTHER_DEVICE_CONFIRM, context, userName,
//				password, progressLayout);
//		loginConfirmAsynTask.execute();
	}



	/*******************************************************
	 * Method name : showSignUpAlertDialog Description : show signup alert
	 * dialog Parameters : nil Return type : void Date : Mar 11, 2015 Author :
	 * Rijo K Jose
	 *****************************************************/
	private void showSignUpAlertDialog() {
		// custom dialog
		final Dialog dialog = new Dialog(mContext, R.style.NewDialog);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_layout_signup);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.setCancelable(true);
		// set the custom dialog components - edit text, button
		int sdk = android.os.Build.VERSION.SDK_INT;
		mMailEdtText = (EditText) dialog.findViewById(R.id.text_dialog);
		mMailEdtText.setOnTouchListener(this);
		TextView alertHead= (TextView) dialog.findViewById(R.id.alertHead);
//
		Button dialogSubmitButton = (Button) dialog
				.findViewById(R.id.btn_signup);

		// if button is clicked, close the custom dialog
		dialogSubmitButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AppUtils.hideKeyboard(mContext, mMailEdtText);
				if (!mMailEdtText.getText().toString().trim().equalsIgnoreCase("")) {
					if (AppUtils.isValidEmail(mMailEdtText.getText()
							.toString())) {
						// sign up api call
						if(AppUtils.isNetworkConnected(mContext)) {
							sendSignUpRequest(URL_PARENT_SIGNUP);
						}else{
							AppUtils.showDialogAlertDismiss((Activity)mContext,"Network Error",getString(R.string.no_internet),R.drawable.nonetworkicon,R.drawable.roundred);

						}
						dialog.dismiss();
					} else {
						/*AppUtils.editTextValidationAlert(mMailEdtText,
								getResources()
										.getString(R.string.invalid_email),
								mContext);
						AppUtils.setEdtTextTextChangelistener(mMailEdtText,
								mContext);*/
						AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.enter_valid_email), R.drawable.exclamationicon, R.drawable.round);

					}
				} else {
					/*AppUtils.editTextValidationAlert(mMailEdtText,
							getResources().getString(R.string.mandatory_field),
							mContext);
					AppUtils.setEdtTextTextChangelistener(mMailEdtText,
							mContext);*/
					AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.alert_heading), getString(R.string.enter_email), R.drawable.exclamationicon, R.drawable.round);

				}
			}
		});

		Button dialogMayBelaterutton= (Button) dialog.findViewById(R.id.btn_maybelater);
		dialogMayBelaterutton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AppUtils.hideKeyboard(mContext, mMailEdtText);
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	private void sendSignUpRequest(String URL) {
		VolleyWrapper volleyWrapper=new VolleyWrapper(URL);
		String[] name={"access_token","email","deviceid","devicetype"};
		String[] value={PreferenceManager.getAccessToken(mContext),mMailEdtText.getText().toString(),FirebaseInstanceId.getInstance().getToken(),"2"};

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
							AppUtils.showDialogAlertDismiss((Activity) mContext, "Success", getString(R.string.signup_success_alert), R.drawable.tick, R.drawable.round);

						}else if(status_code.equalsIgnoreCase("301")){
							AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading),getString(R.string.missing_parameter), R.drawable.infoicon, R.drawable.round);

						}else if(status_code.equalsIgnoreCase("304")){
							AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading),getString(R.string.email_exists), R.drawable.infoicon, R.drawable.round);

						}else if(status_code.equalsIgnoreCase("306")){
							AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading),getString(R.string.invalid_email), R.drawable.infoicon, R.drawable.round);

						}
					} else if (response_code.equalsIgnoreCase("500")) {
						AppUtils.showDialogAlertDismiss((Activity)mContext,"Alert",mContext.getString(R.string.common_error),R.drawable.exclamationicon,R.drawable.round);

					} else if (response_code.equalsIgnoreCase("400")) {
						AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
							@Override
							public void tokenrenewed() {
							}
						});
						sendSignUpRequest(URL_PARENT_SIGNUP);

					} else if (response_code.equalsIgnoreCase("401")) {
						AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
							@Override
							public void tokenrenewed() {
							}
						});
						sendSignUpRequest(URL_PARENT_SIGNUP);

					} else if (response_code.equalsIgnoreCase("402")) {
						AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
							@Override
							public void tokenrenewed() {
							}
						});
						sendSignUpRequest(URL_PARENT_SIGNUP);

					} else {
						/*CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
								, getResources().getString(R.string.ok));
						dialog.show();*/
						AppUtils.showDialogAlertDismiss((Activity)mContext,"Alert",mContext.getString(R.string.common_error),R.drawable.exclamationicon,R.drawable.round);

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
				AppUtils.showDialogAlertDismiss((Activity)mContext,"Alert",mContext.getString(R.string.common_error),R.drawable.exclamationicon,R.drawable.round);

			}
		});


	}

	private void LoginApiCall(String URL) {
		VolleyWrapper volleyWrapper=new VolleyWrapper(URL);
		String[] name={"access_token","email","password","deviceid","devicetype"};
		String[] value={PreferenceManager.getAccessToken(mContext),mUserNameEdtTxt.getText().toString(),mPasswordEdtTxt.getText().toString(), FirebaseInstanceId.getInstance().getToken(),"2"};

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
							//for(int i=0;i<responseArray.length();i++){
								JSONObject respObj= secobj.getJSONObject(JTAG_RESPONSE_ARRAY);
								PreferenceManager.setUserId(mContext, respObj.optString(JTAG_USER_ID));
								PreferenceManager.setUserType(mContext,"1");
							PreferenceManager.setUserEmail(mContext,mUserNameEdtTxt.getText().toString());
							System.out.println("user id---"+PreferenceManager.getUserId(mContext));
							//}
							showDialogSignUpAlert((Activity) mContext, "Success", getString(R.string.login_success_alert), R.drawable.tick, R.drawable.round);

						}else if(status_code.equalsIgnoreCase("301")){
							AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading),getString(R.string.missing_parameter), R.drawable.infoicon, R.drawable.round);

						}else if(status_code.equalsIgnoreCase("304")){
							AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading),getString(R.string.email_exists), R.drawable.infoicon, R.drawable.round);

						}else if(status_code.equalsIgnoreCase("305")){
							AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading),getString(R.string.incrct_usernamepswd), R.drawable.infoicon, R.drawable.round);

						}
						else if(status_code.equalsIgnoreCase("306")){
							AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading),getString(R.string.invalid_email), R.drawable.infoicon, R.drawable.round);

						}else{
							AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading),getString(R.string.common_error), R.drawable.infoicon, R.drawable.round);

						}
					} else if (response_code.equalsIgnoreCase("500")) {
					} else if (response_code.equalsIgnoreCase("400")) {
						AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
							@Override
							public void tokenrenewed() {
							}
						});
						LoginApiCall(URL_LOGIN);

					} else if (response_code.equalsIgnoreCase("401")) {
						AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
							@Override
							public void tokenrenewed() {
														}
						});
						LoginApiCall(URL_LOGIN);
					} else if (response_code.equalsIgnoreCase("402")) {
						AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
							@Override
							public void tokenrenewed() {
														}
						});
						LoginApiCall(URL_LOGIN);
					} else {
						/*CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
								, getResources().getString(R.string.ok));
						dialog.show();*/
						AppUtils.showDialogAlertDismiss((Activity)mContext,"Alert", mContext.getString(R.string.common_error),R.drawable.exclamationicon,R.drawable.round);

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
				AppUtils.showDialogAlertDismiss((Activity)mContext,"Alert",mContext.getString(R.string.common_error),R.drawable.exclamationicon,R.drawable.round);

			}
		});


	}

	public  void showDialogSignUpAlert(final Activity activity, String msgHead, String msg, int ico,int bgIcon) {
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
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
//				Intent homeIntent = new Intent(mContext, HomeListActivity.class);
				Intent homeIntent = new Intent(mContext, HomeListAppCompatActivity.class);
				startActivity(homeIntent);
				finish();
			}
		});

		dialog.show();

	}

	private void sendForGotpassWord(String URL) {
		VolleyWrapper volleyWrapper=new VolleyWrapper(URL);
		String[] name={"access_token","email","deviceid","devicetype"};
		String[] value={PreferenceManager.getAccessToken(mContext),mMailEdtText.getText().toString(),FirebaseInstanceId.getInstance().getToken(),"2"};

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
							AppUtils.showDialogAlertDismiss((Activity) mContext, "Success", getString(R.string.frgot_success_alert), R.drawable.tick, R.drawable.round);

						} else if (status_code.equalsIgnoreCase("301")) {
							AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.missing_parameter), R.drawable.infoicon, R.drawable.round);

						} else if (status_code.equalsIgnoreCase("304")) {
							AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.email_exists), R.drawable.infoicon, R.drawable.round);

						} else if (status_code.equalsIgnoreCase("305")) {
							AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.incrct_usernamepswd), R.drawable.infoicon, R.drawable.round);

						} else if (status_code.equalsIgnoreCase("306")) {
							AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.invalid_email), R.drawable.infoicon, R.drawable.round);

						} else {
							AppUtils.showDialogAlertDismiss((Activity) mContext, getString(R.string.error_heading), getString(R.string.common_error), R.drawable.infoicon, R.drawable.round);

						}
					} else if (response_code.equalsIgnoreCase("500")) {
						AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", mContext.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

					} else if (response_code.equalsIgnoreCase("400")) {
						AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
							@Override
							public void tokenrenewed() {
							}
						});
						sendForGotpassWord(URL_FORGOTPASSWORD);

					} else if (response_code.equalsIgnoreCase("401")) {
						AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
							@Override
							public void tokenrenewed() {
							}
						});
						sendForGotpassWord(URL_FORGOTPASSWORD);

					} else if (response_code.equalsIgnoreCase("402")) {
						AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
							@Override
							public void tokenrenewed() {
							}
						});
						sendForGotpassWord(URL_FORGOTPASSWORD);

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
