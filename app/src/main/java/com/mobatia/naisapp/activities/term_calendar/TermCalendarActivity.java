package com.mobatia.naisapp.activities.term_calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.activities.pdf.PdfReaderActivity;
import com.mobatia.naisapp.activities.term_calendar.adapter.CustomTermsCalendarAdapter;
import com.mobatia.naisapp.activities.term_calendar.model.TermsCalendarModel;
import com.mobatia.naisapp.constants.CacheDIRConstants;
import com.mobatia.naisapp.constants.IntentPassValueConstants;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.constants.NaisTabConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.HeaderManager;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

@SuppressLint("NewApi")
public class TermCalendarActivity extends Activity  implements AdapterView.OnItemClickListener,
		NaisTabConstants,CacheDIRConstants, URLConstants,JSONConstants,StatusConstants,
		IntentPassValueConstants,NaisClassNameConstants {

	private Context mContext;

	RelativeLayout relativeHeader;
	HeaderManager headermanager;
	ImageView back;
	ImageView home;
	ArrayList<TermsCalendarModel> mTermsCalendarListArray ;
	ImageView bannerImagePager;
//	ViewPager bannerImagePager;
	ArrayList<String> bannerUrlImageArray;
	// {
//		{
//			add("Student Services");
//			add("Leadership Team");
//			add("Administrative Staff");
//			add("NIS Wide Heads Of Department");
//			add("Primary");
//			add("Secondary");
//		}
//	};
	private ListView mTermsCalendarListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_termscalendar_list);
//		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this,
//				LoginActivity.class));
		mContext = this;
		initialiseUI();
	}

	/*******************************************************
	 * Method name : initialiseUI Description : initialise UI elements
	 * Parameters : nil Return type : void Date : March 8, 2017 Author : RIJO K JOSE
	 *****************************************************/
	@SuppressWarnings("deprecation")
	public void initialiseUI() {
		relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
		mTermsCalendarListView = (ListView) findViewById(R.id.mTermsCalendarListView);
		bannerImagePager= (ImageView) findViewById(R.id.bannerImageViewPager);
//		bannerImagePager= (ViewPager) findViewById(R.id.bannerImageViewPager);

		headermanager = new HeaderManager(TermCalendarActivity.this, "Term Calendar");
		headermanager.getHeader(relativeHeader, 0);
		back = headermanager.getLeftButton();
		headermanager.setButtonLeftSelector(R.drawable.back,
				R.drawable.back);
		mTermsCalendarListView.setOnItemClickListener(this);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AppUtils.hideKeyBoard(mContext);
				finish();
			}
		});
		home = headermanager.getLogoButton();
		home.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent in = new Intent(mContext, HomeListAppCompatActivity.class);
				in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(in);
			}
		});
		if (AppUtils.checkInternet(mContext)) {

			getList();
		}
		else
		{
			AppUtils.showDialogAlertDismiss((Activity)mContext,"Network Error",getString(R.string.no_internet),R.drawable.nonetworkicon,R.drawable.roundred);

		}
		}
	public void getList() {

		try {
			mTermsCalendarListArray = new ArrayList<>();
			final VolleyWrapper manager = new VolleyWrapper(URL_TERM_CALENDAR_LIST);
			String[] name = new String[]{JTAG_ACCESSTOKEN};
			String[] value = new String[]{PreferenceManager.getAccessToken(mContext)};


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
										if (!bannerImage.equalsIgnoreCase("")) {
//											bannerUrlImageArray = new ArrayList<>();
//											bannerUrlImageArray.add(bannerImage);
//											bannerImagePager.setAdapter(new ImagePagerDrawableAdapter(bannerUrlImageArray,mContext));
											Glide.with(mContext).load(AppUtils.replace(bannerImage)).centerCrop().into(bannerImagePager);

										}
										else
										{
											bannerImagePager.setBackgroundResource(R.drawable.term_calendar_banner);

										}
										JSONArray dataArray = respObject.getJSONArray(JTAG_RESPONSE_DATA_ARRAY);
										if (dataArray.length() > 0) {
											for (int i = 0; i < dataArray.length(); i++) {
													JSONObject dataObject = dataArray.getJSONObject(i);
													mTermsCalendarListArray.add(getSearchValues(dataObject));

											}

											mTermsCalendarListView.setAdapter(new CustomTermsCalendarAdapter(mContext, mTermsCalendarListArray));

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
									AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

								}
							} else {
//								CustomStatusDialog(RESPONSE_FAILURE);
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
	private TermsCalendarModel getSearchValues(JSONObject Object)
			throws JSONException {
		TermsCalendarModel mTermsCalendarModel = new TermsCalendarModel();
		mTermsCalendarModel.setmId(Object.getString(JTAG_ID));
		mTermsCalendarModel.setmFileName(Object.getString(JTAG_TAB_FILE_NAME));
		mTermsCalendarModel.setmTitle(Object.getString(JTAG_TITLE));
		return mTermsCalendarModel;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent(mContext, PdfReaderActivity.class);
		intent.putExtra("pdf_url",mTermsCalendarListArray.get(position).getmFileName());
		startActivity(intent);
	}
}
