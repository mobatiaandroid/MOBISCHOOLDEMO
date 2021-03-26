/**
 * 
 */
package com.mobatia.naisapp.activities.notification_staff;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.constants.IntentPassValueConstants;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.HeaderManager;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;

import org.json.JSONObject;

/**
 * @author rahul
 * 
 */

public class TextalertActivityStaff extends Activity implements
		 OnClickListener,IntentPassValueConstants, JSONConstants, URLConstants, StatusConstants {

	TextView txtmsg;
	TextView mDateTv;
	ImageView img;
	ImageView home;
	Bundle extras;
	int position;
//	PhotosManager photosManager;
	Context context = this;
	Activity mActivity;
	RelativeLayout header;
//	HeaderManager headermanager;
	ImageView back;
	RelativeLayout relativeHeader;
	HeaderManager headermanager;
	String id="";
	String title="";
	String message="";
	String url="";
	String date="";
	String Day="";
	String Month="";
	String Year="";
	String PushDate="";
	String PushID="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.text_push);
		mActivity = this;
		extras = getIntent().getExtras();
		if (extras != null) {
			position = extras.getInt(POSITION);
			Day = extras.getString("Day");
			Month = extras.getString("Month");
			Year = extras.getString("Year");
			PushDate = extras.getString("PushDate");
			PushID = extras.getString("PushID");
		}

		initialiseUI();
		if (AppUtils.isNetworkConnected(mActivity)) {
			callPushNotification(PushID);
		} else {
			AppUtils.showDialogAlertDismiss((Activity) mActivity, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
		}
	}

	private void initialiseUI() {



		img = (ImageView) findViewById(R.id.image);
		txtmsg = (TextView) findViewById(R.id.txt);
		mDateTv = (TextView) findViewById(R.id.mDateTv);
		relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
		headermanager = new HeaderManager(mActivity, "Notification");
		headermanager.getHeader(relativeHeader, 0);
		back = headermanager.getLeftButton();
		headermanager.setButtonLeftSelector(R.drawable.back,
				R.drawable.back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		home = headermanager.getLogoButton();
		home.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent in = new Intent(mActivity, HomeListAppCompatActivity.class);
				in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(in);
			}
		});
//		photosManager = new PhotosManager(context, ALERT_COVER_PATH,
//				(int) context.getResources().getDimension(
//						R.dimen.coverimage_width), (int) context.getResources()
//						.getDimension(R.dimen.coverimage_height), 0);


	}

	private void setDetails() {
		txtmsg.setText(message);
		mDateTv.setText(Day + "-" +Month+ "-" + Year+" "+PushDate);
//		if (!(imglist.get(position).getPushDetail().toString()
//				.equalsIgnoreCase(""))) {
//			txtmsg.setText(imglist.get(position).getPushDetail());
//		} else {
//			txtmsg.setText(imglist.get(position).getPushTitle());
//		}
	}

	@Override
	public void onClick(View v) {

	}
	private void callPushNotification(String pushID)
	{
		try {

			final VolleyWrapper manager = new VolleyWrapper(URL_GET_STAFF_NOTIFICATION_MESSAGE);
			String[] name = {JTAG_ACCESSTOKEN,"push_id"};
			String[] value = {PreferenceManager.getAccessToken(mActivity),pushID};
			manager.getResponsePOST(mActivity, 11, name, value, new VolleyWrapper.ResponseListener() {

				@Override
				public void responseSuccess(String successResponse) {
					System.out.println("NofifyRes Message:" + successResponse);

					if (successResponse != null) {
						try {
							JSONObject rootObject = new JSONObject(successResponse);
							String responseCode = rootObject.getString(JTAG_RESPONSECODE);
							if (responseCode.equalsIgnoreCase(RESPONSE_SUCCESS)) {
								JSONObject responseObject = rootObject.optJSONObject(JTAG_RESPONSE);
								String statusCode = responseObject.getString(JTAG_STATUSCODE);
								if (statusCode.equalsIgnoreCase(STATUS_SUCCESS)) {
									JSONObject dataObject=responseObject.getJSONObject("data");
									id=dataObject.optString("id");
									title=dataObject.optString("title");
									message=dataObject.optString("message");
									url=dataObject.optString("url");
									date=dataObject.optString("date");
									setDetails();
								} else if (statusCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
										statusCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) ||
										statusCode.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
									AppUtils.postInitParam(mActivity, new AppUtils.GetAccessTokenInterface() {
										@Override
										public void getAccessToken() {
										}
									});
									callPushNotification(pushID);

								}
							} else if (responseCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
									responseCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) ||
									responseCode.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
								AppUtils.postInitParam(mActivity, new AppUtils.GetAccessTokenInterface() {
									@Override
									public void getAccessToken() {
									}
								});
								callPushNotification(pushID);

							} else {
								Toast.makeText(mActivity, "Some Error Occured", Toast.LENGTH_SHORT).show();

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
}
