/**
 *
 */
package com.mobatia.naisapp.manager;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.iid.FirebaseInstanceId;
import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.login.LoginActivity;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NameValueConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.parents_evening.model.StudentModel;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

/**
 * @author Rijo K Jose
 *
 */
public class AppUtils implements JSONConstants,URLConstants,NameValueConstants,StatusConstants {
	private static GetAccessTokenInterface getTokenIntrface;
	private static Context mContext;
	private static int count = 0;

	public static boolean isNetworkConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni == null) {
			// There are no active networks.
			return false;
		} else
			return true;
	}

	public static void setErrorForEditTextNull(final EditText edt) {
		edt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				edt.setError(null);

			}

		});
	}


	public static void setErrorForEditText(EditText edt, String msg) {
		edt.setError(msg);
	}
	public static String replace(String str) {
		return str.replaceAll(" ", "%20");
	}
	public static String replaceam(String str) {
		return str.replaceAll("am", " ");
	}
	public static String replacepm(String str) {
		return str.replaceAll("pm", " ");
	}
	public static String replaceAM(String str) {
		return str.replaceAll("AM", " ");
	}
	public static String replacePM(String str) {
		return str.replaceAll("PM", " ");
	}
	public static String replaceamdot(String str) {
		return str.replaceAll("a.m.", " ");
	}
	public static String replacepmdot(String str) {
		return str.replaceAll("p.m.", " ");
	}
	public static String replaceAMDot(String str) {
		return str.replaceAll("A.M.", " ");
	}
	public static String replacePMDot(String str) {
		return str.replaceAll("P.M.", " ");
	}

	public static String replaceAmdot(String str) {
		return str.replaceAll("a.m.", "am");
	}
	public static String replacePmdot(String str) {
		return str.replaceAll("p.m.", "pm");
	}
	public static String replaceYoutube(String str) {
		return str.replaceAll("https://www.youtube.com/embed/", "");
	}
	public static String replacePdf(String str) {
		return str.replaceAll("http://mobicare2.mobatia.com/nais/media/images/", "");
	}
	public static boolean isValidEmail(CharSequence target) {
		if (target == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		}
	}


	public static boolean validateLong(String value) {
		try {
			Long.parseLong(value);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	//get the current version number and name
	public static String getVersionInfo(Context mContext) {
		String versionName = "";
		int versionCode = -1;
		try {
			PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
			versionName = packageInfo.versionName;
			versionCode = packageInfo.versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
return versionName;
//		TextView textViewVersionInfo = (TextView) findViewById(R.id.textview_version_info);
//		textViewVersionInfo.setText(String.format("Version name = %s \nVersion code = %d", versionName, versionCode));
	}
	public static final boolean isValidPhoneNumber(Context context, CharSequence target) {
		return android.util.Patterns.PHONE.matcher(target).matches();
	}

	public static void deviceRegistration(final Context mContext) {

		try {

			final VolleyWrapper manager = new VolleyWrapper(URL_DEVICE_REGISTRATION);
			String[] name = {JTAG_ACCESSTOKEN,JTAG_DEVICE_ID,JTAG_DEVICE_TYPE};
			String[] value = {PreferenceManager.getAccessToken(mContext),FirebaseInstanceId.getInstance().getToken(), "2"};
			manager.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {

				@Override
				public void responseSuccess(String successResponse) {
					if (successResponse != null) {
						try {
							JSONObject rootObject = new JSONObject(successResponse);
							String responseCode = rootObject.getString(JTAG_RESPONSECODE);
							if (responseCode.equalsIgnoreCase(RESPONSE_SUCCESS)) {
								JSONObject responseObject = rootObject.optJSONObject(JTAG_RESPONSE);
								String statusCode = responseObject.getString(JTAG_STATUSCODE);
								if (statusCode.equalsIgnoreCase(STATUS_SUCCESS)) {

								}else if (statusCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
										statusCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING)||
										statusCode.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
									AppUtils.postInitParam(mContext, new GetAccessTokenInterface() {
										@Override
										public void getAccessToken() {
										}
									});
									deviceRegistration(mContext);

								}
							} else if (responseCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
									responseCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING)||
									responseCode.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
								AppUtils.postInitParam(mContext, new GetAccessTokenInterface() {
									@Override
									public void getAccessToken() {
									}
								});
								deviceRegistration(mContext);

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
	public static void postInitParams(final Context mContext, GetAccessTokenInterface getTokenInterface) {
		getTokenIntrface = getTokenInterface;

		final VolleyWrapper manager = new VolleyWrapper(POST_APITOKENURL);
		String name[] = {NAME_GRANT_TYPE,NAME_CLIENT_ID,NAME_CLIENT_SECRET,NAME_USERNAME,NAME_PASSWORD};
		String values[] = {VALUE_GRANT_TYPE,VALUE_CLIENT_ID,VALUE_CLIENT_SECRET,VALUE_USERNAME,VALUE_PASSWORD};

		manager.getResponsePOST(mContext, 11, name, values, new VolleyWrapper.ResponseListener() {

			@Override
			public void responseSuccess(String successResponse) {
				if (successResponse != null) {
					try {
						JSONObject rootObject = new JSONObject(successResponse);

						if (rootObject != null) {
							String acccessToken=rootObject.optString(JTAG_ACCESSTOKEN);
							PreferenceManager.setAccessToken(mContext, acccessToken);
							deviceRegistration(mContext);
//							if (PreferenceManager.getUserId(mContext).equals("")) {
//
//								deviceRegistration(mContext);//changed on 22-09-2017
//							}
						} else {
							// CustomStatusDialog(RESPONSE_CODE_NULL);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public void responseFailure(String failureResponse) {
			}
		});
	}

	public static OkHttpClient createAuthenticatedClient(final String username,
														 final String password) {
		// build client with authentication information.
		OkHttpClient httpClient = new OkHttpClient.Builder().authenticator(new Authenticator() {
			public Request authenticate(Route route, Response response) throws IOException {
				String credential = Credentials.basic(username, password);
				return response.request().newBuilder().header("Authorization", credential).build();
			}
		}).build();
		return httpClient;
	}
	public static void postInitParam(final Context mContext, GetAccessTokenInterface getTokenInterface) {
		getTokenIntrface = getTokenInterface;

		final VolleyWrapper manager = new VolleyWrapper(POST_APITOKENURL);
		String name[] = {NAME_GRANT_TYPE,NAME_CLIENT_ID,NAME_CLIENT_SECRET,NAME_USERNAME,NAME_PASSWORD};
		String values[] = {VALUE_GRANT_TYPE,VALUE_CLIENT_ID,VALUE_CLIENT_SECRET,VALUE_USERNAME,VALUE_PASSWORD};

		manager.getResponsePOST(mContext, 11, name, values, new VolleyWrapper.ResponseListener() {

			@Override
			public void responseSuccess(String successResponse) {
				if (successResponse != null) {
					try {
						JSONObject rootObject = new JSONObject(successResponse);

						if (rootObject != null) {
							String acccessToken=rootObject.optString(JTAG_ACCESSTOKEN);
							PreferenceManager.setAccessToken(mContext, acccessToken);
						} else {
							// CustomStatusDialog(RESPONSE_CODE_NULL);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public void responseFailure(String failureResponse) {
			}
		});
	}

	public interface GetAccessTokenInterface {
		public void getAccessToken();
	}


	public static String htmlparsing(String text) {
		String encodedString;
		encodedString = text.replaceAll("&lt;", "<");
		encodedString = encodedString.replaceAll("&gt;", ">");
		encodedString = encodedString.replaceAll("&amp;", "");
		encodedString = encodedString.replaceAll("amp;", "");
		return encodedString;
	}


	public static String htmlFile(Context context, String content) {
		String APP_DIR = "." + context.getString(R.string.app_name) + "/";
		String HTMLFILE = "file://APP_DIR/" + content;
		return HTMLFILE;
	}

	/**
	 * ****************************************************
	 * Method name : hideKeyboardOnTouchOutside Description : hide keyboard on
	 * touch outside edit text Parameters : view Return type : void Date : Feb
	 * 16, 2015 Author : Rijo K Jose
	 * ***************************************************
	 */
	public static void hideKeyboardOnTouchOutside(View view,
												  final Context context, final EditText edtText) {
		if (!(view instanceof EditText))
			view.setOnTouchListener(new View.OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
					hideKeyBoard(context);
					return false;
				}
			});
	}

	public static int convertDpIntoPixels(Context context, int padding_in_dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
		return padding_in_px;
	}


	/**
	 * ****************************************************
	 * Method name : getButtonDrawableByScreenCathegory Description : get button
	 * bakgrounds Parameters : context, normal_id, pressed_id Return type :
	 * drawable Date : Feb 18, 2014 Author : Rijo K Jose
	 * ***************************************************
	 */
	public static Drawable getButtonDrawableByScreenCathegory(Context con,
															  int normalStateResID, int pressedStateResID) {
		Drawable state_normal = con.getResources()
				.getDrawable(normalStateResID).mutate();
		Drawable state_pressed = con.getResources()
				.getDrawable(pressedStateResID).mutate();
		StateListDrawable drawable = new StateListDrawable();

		drawable.addState(new int[]{android.R.attr.state_pressed},
				state_pressed);
		drawable.addState(new int[]{android.R.attr.state_enabled},
				state_normal);
		return drawable;
	}


	/**
	 * ****************************************************
	 * Method name : editTextValidationAlert Description : show edit text
	 * validation alerts Parameters : edtText, message, context Return type :
	 * void Date : Feb 20, 2014 Author : Rijo K Jose
	 * ***************************************************
	 */
	public static void editTextValidationAlert(EditText edtText,
											   String message, Context context) {
		edtText.setError(message);
	}

	/**
	 * ****************************************************
	 * Method name : setEdtTextTextChangelistener Description : edit text text
	 * change listener Parameters : edtTxt, context Return type : void Date :
	 * Feb 20, 2015 Author : Rijo K Jose
	 * ***************************************************
	 */
	public static void setEdtTextTextChangelistener(final EditText edtTxt,
													Context context) {
		edtTxt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
									  int count) {
				edtTxt.setError(null);

			}


			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				edtTxt.setError(null);

			}
		});
	}

	public static void txtViewValidationAlert(TextView edtText, String message,
											  Context context) {
		edtText.setError(message);
	}

	public static void setTxtViewChangelistener(final TextView edtTxt,
												Context context) {
		edtTxt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
									  int count) {
				edtTxt.setError(null);

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				edtTxt.setError(null);

			}
		});
	}


	/**
	 * ****************************************************
	 * Method name : toFile Description : write bitmap to file Parameters : bm,
	 * fileName Return type : String Date : Jan 14, 2015 Author :Rijo
	 * ***************************************************
	 */
	public static String toFile(Bitmap bm, String fileName) {
		String filePath;
		filePath = "/sdcard/" + fileName;
		File f = new File(filePath);
		try {
			f.createNewFile();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
			byte[] bitmapdata = bos.toByteArray();
			FileOutputStream fos = null;

			fos = new FileOutputStream(f);
			fos.write(bitmapdata);
			fos.close();
			filePath = f.getAbsolutePath();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		bm = null;
		return filePath;
	}


	/**
	 * Sets the error for text view.
	 *
	 * @param edt the edt
	 * @param msg the msg
	 */
	public static void setErrorForTextView(TextView edt, String msg) {
		edt.setError(msg);
	}

	/**
	 * Text watcher for edit text.
	 *
	 * @param edt the edt
	 * @param msg the msg
	 */
	public static void textWatcherForEditText(final EditText edt,
											  final String msg) {
		edt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
									  int count) {
				if (s.length() == 0 || s.equals("")) {
					setErrorForEditText(edt, msg);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {
				if (s.equals("")) {
					setErrorForEditText(edt, msg);
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s != null && s.length() > 0 && edt.getError() != null) {
					edt.setError(null);
				} else if (s.length() == 0 || s.equals("")) {
					setErrorForEditText(edt, msg);
				}
			}
		});
	}

	/**
	 * Text watcher for text view.
	 *
	 * @param edt the edt
	 * @param msg the msg
	 */
	public static void textWatcherForTextView(final TextView edt,
											  final String msg) {
		edt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
									  int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s != null && s.length() > 0 && edt.getError() != null) {
					edt.setError(null);
				} else if (s.length() == 0) {
					setErrorForTextView(edt, msg);
				}
			}
		});
	}

	public static void hideKeyBoard(Context context) {
		InputMethodManager imm = (InputMethodManager) context

				.getSystemService(Context.INPUT_METHOD_SERVICE);

		if (imm.isAcceptingText()) {

			imm.hideSoftInputFromWindow(((Activity) context).getCurrentFocus()
					.getWindowToken(), 0);

		}
	}


	public static  boolean isEditTextFocused(Activity context) {


			InputMethodManager inputManager = (InputMethodManager) context.getSystemService(
					Context.INPUT_METHOD_SERVICE);
			View focusedView = context.getCurrentFocus();
    /*
     * If no view is focused, an NPE will be thrown
     *
     * Maxim Dmitriev
     */
			if (focusedView != null) {
				inputManager.hideSoftInputFromWindow(focusedView.getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
				return true;

			}
		else {
				return false;

			}


		/*InputMethodManager imm = (InputMethodManager) context

				.getSystemService(Context.INPUT_METHOD_SERVICE);

		if (imm.isAcceptingText()) {

			return true;


		}
		else
			return false;*/


	}
	/**
	 * Date comprison.
	 *
	 * @param inputFirstDate  the input first date
	 * @param inputSecondDate the input second date
	 * @return true, if successful
	 */
	public static boolean dateComprison(Date inputFirstDate,
										Date inputSecondDate) {

		if (inputFirstDate.before(inputSecondDate)) {
			return true;
		} else {
			return false;
		}

	}

	public static boolean dateComprisonToday(Date inputDate) {
		if (inputDate.after(getCurrentDate()))
		// || inputDate.equals(getCurrentDate()))
		{
			return true;
		} else {
			return false;
		}

	}

	/**
	 * Gets the current date.
	 *
	 * @return the current date
	 */
	public static Date getCurrentDate() {
		Calendar c = Calendar.getInstance();
		int mYear = c.get(Calendar.YEAR);
		int mMonth = c.get(Calendar.MONTH);
		int mDay = c.get(Calendar.DAY_OF_MONTH);
		Date today = new Date(mYear, mMonth, mDay);
		return today;
	}


	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public static int[] getWindowDimens(Context context) {
		int dimen[] = new int[2];
		Point size = new Point();
		WindowManager w = ((Activity) context).getWindowManager();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			w.getDefaultDisplay().getSize(size);
			dimen[0] = size.x;
			dimen[1] = size.y;
		} else {
			Display d = w.getDefaultDisplay();
			dimen[0] = d.getWidth();
			dimen[1] = d.getHeight();
		}
		return dimen;
	}




	public static class DecimalDigitsInputFilter implements InputFilter {

		Pattern mPattern;

		public DecimalDigitsInputFilter(int digitsBeforeZero, int digitsAfterZero) {
			mPattern = Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) + "})?)||(\\.)?");
		}

		@Override
		public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
			Matcher matcher = mPattern.matcher(dest);
			if (!matcher.matches())
				return "";
			return null;
		}

	}

	public static boolean isAppAvailable(Context ctx, Intent intent) {
		final PackageManager mgr = ctx.getPackageManager();
		List<ResolveInfo> list = mgr.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}

	public static String durationInSecondsToString(int sec) {
		int hours = sec / 3600;
		int minutes = (sec / 60) - (hours * 60);
		int seconds = sec - (hours * 3600) - (minutes * 60);
		String formatted = String.format("%d:%02d:%02d", hours, minutes,
				seconds);
		return formatted;
	}



	public static int GCD(int a, int b) {
		// TODO Auto-generated method stub
		return (b == 0 ? a : GCD(b, a % b));
	}

	public static void getToken(Context context, GetTokenSuccess tokenobj) {
		mContext = context;
		AppUtils apputils = new AppUtils();
		Securitycode accesstoken = apputils.new Securitycode(context, tokenobj);
		accesstoken.getToken();
	}

	private class Securitycode {
		private Context mContext;
		private GetTokenSuccess getTokenObj;

		public Securitycode(Context context, GetTokenSuccess getTokenObj) {
			this.mContext = context;
			this.getTokenObj = getTokenObj;
		}

		public void getToken()
		{

			final VolleyWrapper manager = new VolleyWrapper(POST_APITOKENURL);
			String name[] = {NAME_GRANT_TYPE,NAME_CLIENT_ID,NAME_CLIENT_SECRET,NAME_USERNAME,NAME_PASSWORD};
			String values[] = {VALUE_GRANT_TYPE,VALUE_CLIENT_ID,VALUE_CLIENT_SECRET,VALUE_USERNAME,VALUE_PASSWORD};

			manager.getResponsePOST(mContext, 11, name, values, new VolleyWrapper.ResponseListener() {

				@Override
				public void responseSuccess(String successResponse) {
					if (successResponse != null) {
						try {
							JSONObject rootObject = new JSONObject(successResponse);

							if (rootObject != null) {
								String acccessToken=rootObject.optString(JTAG_ACCESSTOKEN);
								PreferenceManager.setAccessToken(mContext,acccessToken);
							} else {
								// CustomStatusDialog(RESPONSE_CODE_NULL);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}

				@Override
				public void responseFailure(String failureResponse)
				{
				}
			});

		}
	}

	public interface GetTokenSuccess {
		void tokenrenewed();
	}

	public static boolean checkInternet(Context context) {
		ConnectivityManager connec = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = connec.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		} else {
			return false;
		}
	}

	public static void alertGps(final Activity context, String msg) {
		AlertDialog.Builder alert = new AlertDialog.Builder(context);
		alert.setIcon(R.drawable.ic_launcher);
		alert.setTitle(context.getResources().getString(R.string.app_name));
		alert.setMessage(msg);
		alert.setCancelable(false);
		alert.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(
								Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						context.startActivity(intent);
					}
				});

		alert.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});

		alert.show();
	}

	public static void showAlert(final Activity activity, String message,
								 String okBtnTitle, String cancelBtnTitle, boolean okBtnVisibility) {
		// custom dialog
		final Dialog dialog = new Dialog(activity, R.style.NewDialog);
		dialog.setContentView(R.layout.custom_alert_dialog);
		dialog.setCancelable(false);

		// set the custom dialog components - text, image, button
		TextView text = (TextView) dialog.findViewById(R.id.text);
		text.setText(message);
		int sdk = Build.VERSION.SDK_INT;

		Button dialogCancelButton = (Button) dialog
				.findViewById(R.id.dialogButtonCancel);
		dialogCancelButton.setText(cancelBtnTitle);
//		if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
//			dialogCancelButton.setBackgroundDrawable(AppUtils
//					.getButtonDrawableByScreenCathegory(activity,
//							R.color.split_bg, R.color.list_selector));
//		} else {
//			dialogCancelButton.setBackground(AppUtils
//					.getButtonDrawableByScreenCathegory(activity,
//							R.color.split_bg, R.color.list_selector));
//		}
		// if button is clicked, close the custom dialog
		dialogCancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		Button dialogOkButton = (Button) dialog
				.findViewById(R.id.dialogButtonOK);
		dialogOkButton.setVisibility(View.GONE);
		dialogOkButton.setText(okBtnTitle);
		if (okBtnVisibility) {
			dialogOkButton.setVisibility(View.VISIBLE);
//			if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
//				dialogOkButton.setBackgroundDrawable(AppUtils
//						.getButtonDrawableByScreenCathegory(activity,
//								R.color.split_bg, R.color.list_selector));
//			} else {
//				dialogOkButton.setBackground(AppUtils
//						.getButtonDrawableByScreenCathegory(activity,
//								R.color.split_bg, R.color.list_selector));
//			}
			// if button is clicked, close the custom dialog
			dialogOkButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					activity.finish();
				}
			});
		}

		dialog.show();
	}
	public static void showAlertFinish(final Activity activity, String message,
								 String okBtnTitle, String cancelBtnTitle, boolean okBtnVisibility) {
		// custom dialog
		final Dialog dialog = new Dialog(activity, R.style.NewDialog);
		dialog.setContentView(R.layout.custom_alert_dialog);
		dialog.setCancelable(false);

		// set the custom dialog components - text, image, button
		TextView text = (TextView) dialog.findViewById(R.id.text);
		text.setText(message);
		int sdk = Build.VERSION.SDK_INT;

		Button dialogCancelButton = (Button) dialog
				.findViewById(R.id.dialogButtonCancel);
		dialogCancelButton.setText(cancelBtnTitle);
//		if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
//			dialogCancelButton.setBackgroundDrawable(AppUtils
//					.getButtonDrawableByScreenCathegory(activity,
//							R.color.split_bg, R.color.list_selector));
//		} else {
//			dialogCancelButton.setBackground(AppUtils
//					.getButtonDrawableByScreenCathegory(activity,
//							R.color.split_bg, R.color.list_selector));
//		}
		// if button is clicked, close the custom dialog
		dialogCancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				activity.finish();

			}
		});

		Button dialogOkButton = (Button) dialog
				.findViewById(R.id.dialogButtonOK);
		dialogOkButton.setVisibility(View.GONE);
		dialogOkButton.setText(okBtnTitle);
		if (okBtnVisibility) {
			dialogOkButton.setVisibility(View.VISIBLE);
//			if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
//				dialogOkButton.setBackgroundDrawable(AppUtils
//						.getButtonDrawableByScreenCathegory(activity,
//								R.color.split_bg, R.color.list_selector));
//			} else {
//				dialogOkButton.setBackground(AppUtils
//						.getButtonDrawableByScreenCathegory(activity,
//								R.color.split_bg, R.color.list_selector));
//			}
			// if button is clicked, close the custom dialog
			dialogOkButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					activity.finish();
				}
			});
		}

		dialog.show();
	}

	public static void hideKeyboard(Context context, EditText edtText) {
		if (edtText != null) {
			InputMethodManager imm = (InputMethodManager) context
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(edtText.getWindowToken(), 0);
		}
	}
	public static void showDialogAlertFinish(final Activity activity, String msgHead, String msg, int ico,int bgIcon) {
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
				activity.finish();
			}
		});
//		Button dialogButtonCancel = (Button) dialog.findViewById(R.id.btn_Cancel);
//		dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				dialog.dismiss();
//			}
//		});
		dialog.show();

	}
	public static void showDialogAlertSingleBtnFinish(final Activity activity, String msgHead, String msg, int ico,int bgIcon) {
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
				activity.finish();
			}
		});
		Button dialogButtonCancel = (Button) dialog.findViewById(R.id.btn_Cancel);
		dialogButtonCancel.setVisibility(View.GONE);
		dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();

	}
	public static void showDialogAlertDismiss(final Activity activity, String msgHead, String msg, int ico,int bgIcon)
	{
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
			}
		});
//		Button dialogButtonCancel = (Button) dialog.findViewById(R.id.btn_Cancel);
//		dialogButtonCancel.setVisibility(View.GONE);
//		dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				dialog.dismiss();
//			}
//		});
		dialog.show();

	}

	public static void showDialogAlertDismissFinish(final Activity activity, String msgHead, String msg, int ico,int bgIcon) {
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
				activity.finish();
			}
		});
//		Button dialogButtonCancel = (Button) dialog.findViewById(R.id.btn_Cancel);
//		dialogButtonCancel.setVisibility(View.GONE);
//		dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				dialog.dismiss();
//			}
//		});
		dialog.show();

	}
	/**To convert date from yyyy-MM-dd to dd-MMM-yyyy**/
	public static String dateParsingToDdMmmYyyy(String date) {

		String strCurrentDate = "";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
		Date newDate = null;
		try {
			newDate = format.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		format = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.ENGLISH);
		strCurrentDate = format.format(newDate);
		return strCurrentDate;
	}
	public static String dateParsingToDdMmYyyy(String date) {

		String strCurrentDate = "";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		Date newDate = null;
		try {
			newDate = format.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
		strCurrentDate = format.format(newDate);
		return strCurrentDate;
	}
	public static void showDialogAlertLogout(final Activity activity, String msgHead, String msg, int ico,int bgIcon)
	{
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
				if (PreferenceManager.getUserId(activity).equalsIgnoreCase(""))
				{
					if (PreferenceManager.getStaffId(activity).equalsIgnoreCase(""))
					{
						PreferenceManager.setUserId(activity, "");
						dialog.dismiss();
						Intent mIntent = new Intent(activity, LoginActivity.class);
						mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
						activity.startActivity(mIntent);
					}
					else
					{
						callLogoutStaffApi(activity,dialog);
					}

				}
				else
				{
					callLogoutApi(activity,dialog);
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
	private static void callLogoutApi(final Activity mActivity,final Dialog dialog)
	{
		VolleyWrapper volleyWrapper = new VolleyWrapper(URL_LOGOUT);
		String[] name = {"access_token", "users_id",JTAG_DEVICE_iD,JTAG_DEVICE_tYPE};
		String[] value = {PreferenceManager.getAccessToken(mActivity), PreferenceManager.getUserId(mActivity),FirebaseInstanceId.getInstance().getToken(), "2"};

		//String[] value={PreferenceManager.getAccessToken(mContext),mStaffList.get(pos).getStaffEmail(),JTAG_USERS_ID_VALUE,text_dialog.getText().toString(),text_content.getText().toString()};
		volleyWrapper.getResponsePOST(mActivity, 11, name, value, new VolleyWrapper.ResponseListener() {
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
							dialog.dismiss();
							PreferenceManager.setUserId(mActivity, "");
							ArrayList<StudentModel>mArray=new ArrayList<>();
							PreferenceManager.setStudentArrayList(mArray,mContext);
							Intent mIntent = new Intent(mActivity, LoginActivity.class);
							mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
							mActivity.startActivity(mIntent);

						}
					} else if (response_code.equalsIgnoreCase("500")) {
						dialog.dismiss();
						AppUtils.showDialogAlertDismiss((Activity)mActivity,"Alert",mActivity.getString(R.string.common_error),R.drawable.exclamationicon,R.drawable.round);

					} else if (response_code.equalsIgnoreCase("400")) {
						AppUtils.getToken(mActivity, new GetTokenSuccess() {
							@Override
							public void tokenrenewed() {

							}
						});
						callLogoutApi(mActivity, dialog);

					} else if (response_code.equalsIgnoreCase("401")) {
						AppUtils.getToken(mActivity, new GetTokenSuccess() {
							@Override
							public void tokenrenewed() {
							}
						});
						callLogoutApi(mActivity, dialog);

					} else if (response_code.equalsIgnoreCase("402")) {
						AppUtils.getToken(mActivity, new GetTokenSuccess() {
							@Override
							public void tokenrenewed() {
							}
						});
						callLogoutApi(mActivity, dialog);

					} else {
						dialog.dismiss();
						AppUtils.showDialogAlertDismiss(mActivity, "Alert", mActivity.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

					}
				} catch (Exception ex) {
					System.out.println("The Exception in edit profile is" + ex.toString());
				}

			}

			@Override
			public void responseFailure(String failureResponse) {
				dialog.dismiss();
				AppUtils.showDialogAlertDismiss(mActivity, "Alert", mActivity.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

			}
		});


	}
   private static void callLogoutStaffApi(final Activity mActivity,final Dialog dialog)
	{
		VolleyWrapper volleyWrapper = new VolleyWrapper(URL_GET_STAFF_LOGOUT);
		String[] name = {"access_token", "staff_id",JTAG_DEVICE_iD,JTAG_DEVICE_tYPE};
		String[] value = {PreferenceManager.getAccessToken(mActivity), PreferenceManager.getStaffId(mActivity),FirebaseInstanceId.getInstance().getToken(), "2"};

		//String[] value={PreferenceManager.getAccessToken(mContext),mStaffList.get(pos).getStaffEmail(),JTAG_USERS_ID_VALUE,text_dialog.getText().toString(),text_content.getText().toString()};
		volleyWrapper.getResponsePOST(mActivity, 11, name, value, new VolleyWrapper.ResponseListener() {
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
							dialog.dismiss();
							PreferenceManager.setUserId(mActivity, "");
							PreferenceManager.setStaffId(mActivity,"");
							Intent mIntent = new Intent(mActivity, LoginActivity.class);
							mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
							mActivity.startActivity(mIntent);

						}
					} else if (response_code.equalsIgnoreCase("500")) {
						dialog.dismiss();
						AppUtils.showDialogAlertDismiss((Activity)mActivity,"Alert",mActivity.getString(R.string.common_error),R.drawable.exclamationicon,R.drawable.round);

					} else if (response_code.equalsIgnoreCase("400")) {
						AppUtils.getToken(mActivity, new GetTokenSuccess() {
							@Override
							public void tokenrenewed() {

							}
						});
						callLogoutApi(mActivity, dialog);

					} else if (response_code.equalsIgnoreCase("401")) {
						AppUtils.getToken(mActivity, new GetTokenSuccess() {
							@Override
							public void tokenrenewed() {
							}
						});
						callLogoutApi(mActivity, dialog);

					} else if (response_code.equalsIgnoreCase("402")) {
						AppUtils.getToken(mActivity, new GetTokenSuccess() {
							@Override
							public void tokenrenewed() {
							}
						});
						callLogoutApi(mActivity, dialog);

					} else {
						dialog.dismiss();
						AppUtils.showDialogAlertDismiss(mActivity, "Alert", mActivity.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

					}
				} catch (Exception ex) {
					System.out.println("The Exception in edit profile is" + ex.toString());
				}

			}

			@Override
			public void responseFailure(String failureResponse) {
				dialog.dismiss();
				AppUtils.showDialogAlertDismiss(mActivity, "Alert", mActivity.getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

			}
		});


	}

	public static String dateParsingToyyyyMMdd(String date) {

		String strCurrentDate = "";
		SimpleDateFormat format = new SimpleDateFormat("EEEE dd MMMM yyyy HH:mm:ss", Locale.ENGLISH);
		Date newDate = null;
		try {
			newDate = format.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		strCurrentDate = format.format(newDate);
		return strCurrentDate;
	}
	/* Intent callGPSSettingIntent = new Intent(
                                            android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    activity.startActivity(callGPSSettingIntent);*/

	public static void showDialogAlertGPS(final Activity activity, String msgHead, String msg, int ico,int bgIcon) {
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
				Intent callGPSSettingIntent = new Intent(
						Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				activity.startActivity(callGPSSettingIntent);
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

	public static String dateParsingTodd_MMM_yyyy(String date) {

		String strCurrentDate = "";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		Date newDate = null;
		try {
			newDate = format.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		format = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
		strCurrentDate = format.format(newDate);
		return strCurrentDate;
	}
	public static String dateConversionY(String inputDate){
		String mDate="";

		try {
			Date date;
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
			date = formatter.parse(inputDate);
			//Subtracting 6 hours from selected time
			long time = date.getTime();

			//SimpleDateFormat formatterFullDate = new SimpleDateFormat("dd MMMM yyyy");
			SimpleDateFormat formatterFullDate = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);

			mDate = formatterFullDate.format(time);

		} catch (Exception e) {
			Log.d("Exception", "" + e);
		}
		return mDate;
	}
	public static String dateConversionYY(String inputDate){
		String mDate="";

		try {
			Date date;
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
			date = formatter.parse(inputDate);
			//Subtracting 6 hours from selected time
			long time = date.getTime();

			//SimpleDateFormat formatterFullDate = new SimpleDateFormat("dd MMMM yyyy");
			SimpleDateFormat formatterFullDate = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);

			mDate = formatterFullDate.format(time);

		} catch (Exception e) {
			Log.d("Exception", "" + e);
		}
		return mDate;
	}
	public static String dateConversionYToD(String inputDate){
		String mDate="";

		try {
			Date date;
			DateFormat formatter = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
			date = formatter.parse(inputDate);
			//Subtracting 6 hours from selected time
			long time = date.getTime();

			//SimpleDateFormat formatterFullDate = new SimpleDateFormat("dd MMMM yyyy");
			SimpleDateFormat formatterFullDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

			mDate = formatterFullDate.format(time);

		} catch (Exception e) {
			Log.d("Exception", "" + e);
		}
		return mDate;
	}
	public static void showNotifyAlertDismiss(final Context activity, String msgHead, String msg, int ico,int bgIcon) {
		final Dialog dialog = new Dialog(activity);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

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
			}
		});
//		Button dialogButtonCancel = (Button) dialog.findViewById(R.id.btn_Cancel);
//		dialogButtonCancel.setVisibility(View.GONE);
//		dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				dialog.dismiss();
//			}
//		});
		dialog.show();

	}
	public static String getCurrentDateToday() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		Calendar calendar = Calendar.getInstance();
		String today = dateFormat.format(calendar.getTime());
		return today;
	}

	public static String convertTimeToAMPM(String date) {
		String strCurrentDate = "";
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
		Date newDate = null;
		try {
			newDate = format.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		format = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
		strCurrentDate = format.format(newDate);
		return strCurrentDate;
	}
	public static void showDialogAlertUpdate(final Context mContext) {
		final Dialog dialog = new Dialog(mContext);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialog.setCancelable(false);
		dialog.setContentView(R.layout.dialog_update_version);
		Button btnUpdate = (Button) dialog.findViewById(R.id.btnUpdate);
		btnUpdate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();

				final String appPackageName = mContext.getPackageName(); // getPackageName() from Context or Activity object
				try {
					mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
				} catch (android.content.ActivityNotFoundException anfe) {
					mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
				}
			}
		});
		dialog.show();

	}
	public static String dateConversionddmmyyyy(String inputDate){
		String mDate="";

		try {
			Date date;
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
			date = formatter.parse(inputDate);
			//Subtracting 6 hours from selected time
			long time = date.getTime();

			//SimpleDateFormat formatterFullDate = new SimpleDateFormat("dd MMMM yyyy");
			SimpleDateFormat formatterFullDate = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

			mDate = formatterFullDate.format(time);

		} catch (Exception e) {
			Log.d("Exception", "" + e);
		}
		return mDate;
	}
	public static String dateParsingToEEEEyyyyMMdd(String date) {

		String strCurrentDate = "";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		Date newDate = null;
		try {
			newDate = format.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		format = new SimpleDateFormat("EEEE dd MMMM yyyy", Locale.ENGLISH);
		strCurrentDate = format.format(newDate);
		return strCurrentDate;
	}
	public static String dateConversionddmmyyyynew(String inputDate){
		String mDate="";

		try {
			Date date;
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
			date = formatter.parse(inputDate);
			//Subtracting 6 hours from selected time
			long time = date.getTime();

			//SimpleDateFormat formatterFullDate = new SimpleDateFormat("dd MMMM yyyy");
			SimpleDateFormat formatterFullDate = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);

			mDate = formatterFullDate.format(time);

		} catch (Exception e) {
			Log.d("Exception", "" + e);
		}
		return mDate;
	}
	public static String dateParsingTodd_MMM_yyyy_am(String date) {

		String strCurrentDate = "";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
		Date newDate = null;
		try {
			newDate = format.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		format = new SimpleDateFormat("dd-MMM-yyyy hh:mm a", Locale.ENGLISH);
		strCurrentDate = format.format(newDate);
		return strCurrentDate;
	}

	public static String dateParsingToyymmdd(String date) {

		String strCurrentDate = "";
		SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy", Locale.ENGLISH);
		Date newDate = null;
		try {
			newDate = format.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		strCurrentDate = format.format(newDate);
		return strCurrentDate;
	}
	public static String dateParsingTocurrent(String date) {

		String strCurrentDate = "";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
		Date newDate = null;
		try {
			newDate = format.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		strCurrentDate = format.format(newDate);
		return strCurrentDate;
	}
	public static String dateParsingTomm(String date) {

		String strCurrentDate = "";
		SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy", Locale.ENGLISH);
		Date newDate = null;
		try {
			newDate = format.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		format = new SimpleDateFormat("MMM", Locale.ENGLISH);
		strCurrentDate = format.format(newDate);
		return strCurrentDate;
	}
	public static String dateParsingToEEE(String date) {

		String strCurrentDate = "";
		SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy", Locale.ENGLISH);
		Date newDate = null;
		try {
			newDate = format.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		format = new SimpleDateFormat("EEE", Locale.ENGLISH);
		strCurrentDate = format.format(newDate);
		return strCurrentDate;
	}
	public static String dateParsingToyyy(String date) {

		String strCurrentDate = "";
		SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy", Locale.ENGLISH);
		Date newDate = null;
		try {
			newDate = format.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		format = new SimpleDateFormat("yyyy", Locale.ENGLISH);
		strCurrentDate = format.format(newDate);
		return strCurrentDate;
	}
	public static String dateParsingTodd(String date) {

		String strCurrentDate = "";
		SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy", Locale.ENGLISH);
		Date newDate = null;
		try {
			newDate = format.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		format = new SimpleDateFormat("dd", Locale.ENGLISH);
		strCurrentDate = format.format(newDate);
		return strCurrentDate;
	}
}