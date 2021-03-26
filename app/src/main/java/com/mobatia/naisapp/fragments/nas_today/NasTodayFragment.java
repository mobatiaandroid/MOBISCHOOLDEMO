/**
 *
 */
package com.mobatia.naisapp.fragments.nas_today;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mobatia.naisapp.R;
import com.mobatia.naisapp.constants.CacheDIRConstants;
import com.mobatia.naisapp.constants.IntentPassValueConstants;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.constants.NaisTabConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.nas_today.adapter.NasTodayAdapter;
import com.mobatia.naisapp.fragments.nas_today.model.NasTodayModel;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * @author Rijo K Jose
 *
 */

public class NasTodayFragment extends Fragment implements
		NaisTabConstants,CacheDIRConstants, URLConstants,
		IntentPassValueConstants,NaisClassNameConstants,JSONConstants,StatusConstants {
	TextView mTitleTextView;

	private View mRootView;
	private Context mContext;
	private ListView mNasTodayListView;
	private String mTitle;
	private String mTabId;
	private RelativeLayout relMain;
	private ImageView mBannerImage;
	private ArrayList<NasTodayModel> mListViewArray;
	ImageView bannerImagePager;
//	ViewPager bannerImagePager;
	ArrayList<String> bannerUrlImageArray;
	String myFormatCalender = "yyyy-MM-dd";
	SimpleDateFormat sdfcalender;
	public NasTodayFragment() {

	}

	public NasTodayFragment(String title, String tabId) {
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
		mRootView = inflater.inflate(R.layout.fragment_nastoday_list, container,
				false);
//		setHasOptionsMenu(true);
		mContext = getActivity();
		myFormatCalender = "yyyy-MM-dd hh:mm:ss";
		sdfcalender = new SimpleDateFormat(myFormatCalender, Locale.ENGLISH);
//		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(mContext));
		initialiseUI();
//		GetAboutUsListAsyncTask aboutUsTask = new GetAboutUsListAsyncTask(
//				URL_ABOUTUS_LIST, ABOUT_US_DIR, 1, mTabId);
//		aboutUsTask.execute();
		return mRootView;
	}

	/*******************************************************
	 * Method name : initialiseUI Description : initialise UI elements
	 * Parameters : nil Return type : void Date : Jan 12, 2015 Author : Vandana
	 * Surendranath
	 *****************************************************/
	private void initialiseUI() {
		mTitleTextView= (TextView) mRootView.findViewById(R.id.titleTextView);
		mNasTodayListView= (ListView) mRootView.findViewById(R.id.mNasTodayListView);
		mTitleTextView.setText(NAS_TODAY);
//		bannerImagePager= (ViewPager) mRootView.findViewById(R.id.bannerImageViewPager);
		bannerImagePager= (ImageView) mRootView.findViewById(R.id.bannerImageViewPager);
		relMain = (RelativeLayout) mRootView.findViewById(R.id.relMain);
		relMain.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		if (AppUtils.checkInternet(mContext)) {

			getList();
		}
		else
		{
			AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

		}
	}


	public void getList() {

		try {
			mListViewArray = new ArrayList<>();
			final VolleyWrapper manager = new VolleyWrapper(URL_NAS_TODAY_LIST);
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
											Glide.with(mContext).load(AppUtils.replace(bannerImage)).centerCrop().into(bannerImagePager);

//											bannerUrlImageArray = new ArrayList<>();
//											bannerUrlImageArray.add(bannerImage);
//											bannerImagePager.setAdapter(new ImagePagerDrawableAdapter(bannerUrlImageArray, getActivity()));
										}
										else
										{
											bannerImagePager.setBackgroundResource(R.drawable.nastodaybanner);

										}
										JSONArray dataArray = respObject.getJSONArray(JTAG_RESPONSE_DATA_ARRAY);
										if (dataArray.length() > 0) {
											for (int i = 0; i <dataArray.length(); i++) {
												JSONObject dataObject = dataArray.getJSONObject(i);
												mListViewArray.add(getSearchValues(dataObject));

											}

											mNasTodayListView.setAdapter(new NasTodayAdapter(getActivity(), mListViewArray));

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
									AppUtils.postInitParam(getActivity(), new AppUtils.GetAccessTokenInterface() {
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
							} else  {
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
	private NasTodayModel getSearchValues(JSONObject Object)
			throws JSONException {
		NasTodayModel model = new NasTodayModel();
		model.setId(Object.getString(JTAG_ID));
		model.setImage(Object.getString(JTAG_IMAGE));
		model.setTitle(Object.getString(JTAG_TITLE));
		model.setDescription(Object.getString(JTAG_DESCRIPTION));
		model.setDate(Object.getString(JTAG_DATE));
		model.setPdf(Object.getString(JTAG_PDF));
		Date mNoticeDate = new Date();
		String mDate=Object.optString(JTAG_DATE);
		try {
			mNoticeDate = sdfcalender.parse(mDate);
		} catch (ParseException ex) {
			Log.e("Date", "Parsing error");
		}
		String mDateTime = Object.getString(JTAG_DATE);
		Date mTime = new Date();

		try {
			mTime = sdfcalender.parse(mDateTime);
			SimpleDateFormat format2 = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
			String startTime = format2.format(mTime);
			model.setTime(startTime);
			;
		} catch (ParseException ex) {
			Log.e("Date", "Parsing error");
		}
//			String dayOfTheWeek = (String) DateFormat.format("EEEE", mNoticeDate); // Thursday
		String day          = (String) DateFormat.format("dd", mNoticeDate); // 20
		String monthString  = (String) DateFormat.format("MMM",  mNoticeDate); // Jun
//			String monthNumber  = (String) DateFormat.format("MM",   mNoticeDate); // 06
		String year         = (String) DateFormat.format("yyyy", mNoticeDate); // 2013
		model.setDay(day);
		model.setMonth(monthString.toUpperCase());
		model.setYear(year);
		return model;
	}
	/*public static Bitmap getBitmapFromURL(String src) {
		Bitmap myBitmap = null;
		try {

			URL url = new URL(src);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.connect();

			//Decryption
			try {
				Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
				SecretKeySpec keySpec = new SecretKeySpec("01234567890abcde".getBytes(), "AES");
				IvParameterSpec ivSpec = new IvParameterSpec("fedcba9876543210".getBytes());
				cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

				InputStream input = connection.getInputStream();
				CipherInputStream cis = new CipherInputStream(input, cipher);


				myBitmap = BitmapFactory.decodeStream(cis);

			}
			catch(Exception e){
				e.fillInStackTrace();
				Log.v("Alert","Errorchence : "+e);
			}

			return myBitmap;


		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

}*/

}
