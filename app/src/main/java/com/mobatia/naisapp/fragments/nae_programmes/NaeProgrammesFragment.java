/**
 * 
 */
package com.mobatia.naisapp.fragments.nae_programmes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.news.NewsActivity;
import com.mobatia.naisapp.constants.CacheDIRConstants;
import com.mobatia.naisapp.constants.IntentPassValueConstants;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.constants.NaisTabConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.primary.model.PrimaryModel;
import com.mobatia.naisapp.fragments.secondary.adapter.CustomSecondaryAdapter;
import com.mobatia.naisapp.fragments.secondary.model.SecondaryModel;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author Rijo K Jose
 * 
 */

public class NaeProgrammesFragment extends Fragment implements OnItemClickListener,
		NaisTabConstants,CacheDIRConstants, URLConstants,
		IntentPassValueConstants,NaisClassNameConstants,JSONConstants,StatusConstants {
	TextView mTitleTextView;

	private View mRootView;
	private Context mContext;
	private ListView mListView;
	private String mTitle;
	private String mTabId;
	private RelativeLayout relMain;
	private ArrayList<SecondaryModel> mListViewArray;
	ImageView bannerImagePager;
//	ViewPager bannerImagePager;
	ArrayList<String> bannerUrlImageArray;
	private ArrayList<PrimaryModel> mNewsModelArrayList;

	public NaeProgrammesFragment() {

	}

	public NaeProgrammesFragment(String title, String tabId) {
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
		mRootView = inflater.inflate(R.layout.fragment_naeprogrammes_list, container,
				false);
//		setHasOptionsMenu(true);
		mContext = getActivity();
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
		mTitleTextView.setText(NAE_PROGRAMMES);
		mListView = (ListView) mRootView.findViewById(R.id.mListView);
//		bannerImagePager= (ViewPager) mRootView.findViewById(R.id.bannerImagePager);
		bannerImagePager= (ImageView) mRootView.findViewById(R.id.bannerImagePager);
		relMain = (RelativeLayout) mRootView.findViewById(R.id.relMain);
		relMain.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		mListView.setOnItemClickListener(this);
		if (AppUtils.checkInternet(mContext)) {

			getList();
		}
		else
		{
			AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

		}
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
		/*if (mListViewArray.get(position).getmFile().endsWith(".pdf")) {
			Intent intent = new Intent(mContext, PdfReaderActivity.class);
			intent.putExtra("pdf_url", mListViewArray.get(position).getmFile());
			startActivity(intent);
		}
		else
		{
			Intent intent = new Intent(mContext, LoadUrlWebViewActivity.class);
			intent.putExtra("url",mListViewArray.get(position).getmFile());
			intent.putExtra("tab_type",mListViewArray.get(position).getmName());
			mContext.startActivity(intent);
		}*/
		Intent intent = new Intent(mContext, NewsActivity.class);
		intent.putExtra("pdf_urls", mListViewArray.get(position).getmPrimaryModel());
		intent.putExtra("tab_type", mListViewArray.get(position).getmName());
		startActivity(intent);
	}

	public void getList() {

		try {
			mListViewArray = new ArrayList<>();
			final VolleyWrapper manager = new VolleyWrapper(URL_NAE_PROGRAMMES_LIST);
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
											bannerImagePager.setBackgroundResource(R.drawable.default_banner);
//											bannerImagePager.setBackgroundResource(R.drawable.nae_programmes_banner);

										}
										JSONArray dataArray = respObject.getJSONArray(JTAG_RESPONSE_DATA_ARRAY);
										if (dataArray.length() > 0) {
											for (int i = 0; i <dataArray.length(); i++) {
												JSONObject dataObject = dataArray.getJSONObject(i);
												mListViewArray.add(getSearchValues(dataObject));

											}

											mListView.setAdapter(new CustomSecondaryAdapter(getActivity(), mListViewArray));

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

								} else if (responsCode.equals(RESPONSE_ERROR)) {
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
//		mSecondaryModel.setmFile(Object.getString(JTAG_TAB_FILE));
		mSecondaryModel.setmName(Object.getString(JTAG_TAB_NAME));
		if (Object.get(JTAG_TAB_FILE) instanceof JSONArray)
		{
			JSONArray fileArray = Object.getJSONArray(JTAG_TAB_FILE);
			mNewsModelArrayList=new ArrayList<>();
			for (int i=0;i<fileArray.length();i++)
			{
				JSONObject newsObject=fileArray.optJSONObject(i);
				PrimaryModel mNewsModel=new PrimaryModel();
				mNewsModel.setmId(newsObject.optString(JTAG_ID));
				mNewsModel.setmFile(newsObject.optString(JTAG_TAB_FILE));
				mNewsModel.setmTitle(newsObject.optString(JTAG_TITLE));
				mNewsModelArrayList.add(mNewsModel);
			}
			mSecondaryModel.setmPrimaryModel(mNewsModelArrayList);

		}
		else
		{
			mSecondaryModel.setmFile(Object.getString(JTAG_TAB_FILE));
		}
		return mSecondaryModel;
	}


}
