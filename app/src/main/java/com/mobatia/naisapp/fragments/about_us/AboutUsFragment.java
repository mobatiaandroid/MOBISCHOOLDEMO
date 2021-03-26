/**
 *
 */
package com.mobatia.naisapp.fragments.about_us;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.staff_directory.StaffDirectoryActivity;
import com.mobatia.naisapp.activities.web_view.LoadUrlWebViewActivity;
import com.mobatia.naisapp.constants.CacheDIRConstants;
import com.mobatia.naisapp.constants.IntentPassValueConstants;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.constants.NaisTabConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.about_us.adapter.CustomAboutUsAdapter;
import com.mobatia.naisapp.fragments.about_us.model.AboutusModel;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.recyclerviewmanager.DividerItemDecoration;
import com.mobatia.naisapp.recyclerviewmanager.ItemOffsetDecoration;
import com.mobatia.naisapp.recyclerviewmanager.RecyclerItemListener;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author RIJO K JOSE
 *
 */

public class AboutUsFragment extends Fragment implements OnItemClickListener,
		NaisTabConstants,CacheDIRConstants, URLConstants,
		IntentPassValueConstants,NaisClassNameConstants,JSONConstants,StatusConstants {

	private View mRootView;
	private Context mContext;
	//private ListView mAboutUsList;
	private RecyclerView mAboutUsList;
	private String mTitle;
	private String mTabId;
	String email_nas;
	String weburlString;
	String descriptionString;
	private RelativeLayout relMain,mtitleRel;
	private ImageView mBannerImage,sendEmail;
	TextView mTitleTextView,weburl,description,descriptionTitle;
	//	private CustomAboutUsAdapter mAdapter;
	private ArrayList<AboutusModel> mAboutUsListArray;
	private ArrayList<AboutusModel> mAboutUsModelListArray;
	Dialog dialog;
	EditText text_dialog,text_content;
ScrollView scroolView;
//	ViewPager bannerImagePager;
	ImageView bannerImagePager;
	ArrayList<String>bannerUrlImageArray;

	//ArrayList<String> mAboutUsListArray = new ArrayList<String>() {
//	{
//		add("About NAS Dubai");
//		add("Our Philosophy");
//		add("Staff Directory");
//		add("Facilities");
//		add("About Nord Anglia Education");
//	}
//};
	public AboutUsFragment() {

	}

	public AboutUsFragment(String title, String tabId) {
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
		mRootView = inflater.inflate(R.layout.fragment_aboutus_list, container,
				false);
		setHasOptionsMenu(true);
		mContext = getActivity();

//		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(mContext));
		initialiseUI();
		if (AppUtils.checkInternet(mContext)) {

			getList();
		}
		else
		{
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
		mTitleTextView= (TextView) mRootView.findViewById(R.id.titleTextView);
		mtitleRel= (RelativeLayout) mRootView.findViewById(R.id.title);
		mAboutUsList = (RecyclerView) mRootView.findViewById(R.id.mAboutUsListView);
		description= (TextView) mRootView.findViewById(R.id.description);
		descriptionTitle= (TextView) mRootView.findViewById(R.id.descriptionTitle);
		weburl= (TextView) mRootView.findViewById(R.id.weburl);
		sendEmail= (ImageView) mRootView.findViewById(R.id.sendEmail);
		scroolView= (ScrollView) mRootView.findViewById(R.id.scrollView);
		//scroolView.smoothScrollTo(0,description.getTop());
 scroolView.fullScroll(View.FOCUS_DOWN);
//		mBannerImage = (ImageView) mRootView.findViewById(R.id.bannerImageView);
		relMain = (RelativeLayout) mRootView.findViewById(R.id.relMain);
		relMain.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
//		bannerImagePager= (ViewPager) mRootView.findViewById(R.id.bannerImagePager);
		bannerImagePager= (ImageView) mRootView.findViewById(R.id.bannerImagePager);
		mTitleTextView.setText(ABOUT_US);
		mAboutUsList.setHasFixedSize(true);
		LinearLayoutManager llm = new LinearLayoutManager(mContext);
		llm.setOrientation(LinearLayoutManager.VERTICAL);
		mAboutUsList.setLayoutManager(llm);
		ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext,5);
		mAboutUsList.addItemDecoration(itemDecoration);
		//mAboutUsList.setLayoutManager(recyclerViewLayoutManager);
		mAboutUsList.addItemDecoration(
                new DividerItemDecoration(mContext.getResources().getDrawable(R.drawable.list_divider)));
//		mAboutUsList.setAdapter(new CustomAboutUsAdapter(getActivity(), mAboutUsListArray));
		//mAboutUsList.setOnItemClickListener(this);
		weburl.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, LoadUrlWebViewActivity.class);
				intent.putExtra("url",weburlString);
				intent.putExtra("tab_type",ABOUT_US);
				mContext.startActivity(intent);
			}
		});
		sendEmail.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				System.out.println("click on mail--");
				if (!PreferenceManager.getUserId(mContext).equals("")) {
					dialog = new Dialog(mContext);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

					dialog.setContentView(R.layout.alert_send_email_dialog);
					dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
					dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

					Button dialogCancelButton = (Button) dialog.findViewById(R.id.cancelButton);
					Button submitButton = (Button) dialog.findViewById(R.id.submitButton);
					text_dialog = (EditText) dialog.findViewById(R.id.text_dialog);
					text_content = (EditText) dialog.findViewById(R.id.text_content);
					// text_dialog.setSelection(0);
					//text_content.setSelection(0);
					text_dialog.setOnFocusChangeListener(new View.OnFocusChangeListener() {
						@Override
						public void onFocusChange(View v, boolean hasFocus) {
							if (hasFocus) {
								text_dialog.setHint("");
								text_dialog.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
								text_dialog.setPadding(5, 5, 0, 0);
							} else {
								text_dialog.setHint("Enter your subject here...");

								text_dialog.setGravity(Gravity.CENTER);

							}
						}
					});
					text_content.setOnFocusChangeListener(new View.OnFocusChangeListener() {
						@Override
						public void onFocusChange(View v, boolean hasFocus) {
							if (hasFocus) {
								text_content.setGravity(Gravity.LEFT);
							} else {
								text_content.setGravity(Gravity.CENTER);

							}
						}
					});
					dialogCancelButton.setOnClickListener(new View.OnClickListener() {

						@Override

						public void onClick(View v) {

							dialog.dismiss();

						}

					});

					submitButton.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							System.out.println("submit btn clicked");
                           /* if (AppUtils.isNetworkConnected(mContext)) {
                                if (text_content.equals("")) {
                                    AppUtils.setErrorForEditText(text_content, mContext.getString(R.string.mandatory_field));
                                } else if (text_dialog.equals("")) {
                                    AppUtils.setErrorForEditText(text_dialog, mContext.getString(R.string.mandatory_field));

                                } else {
                                    sendEmailToStaff(URL_SEND_EMAIL_TO_STAFF);
                                }
                            } else {
                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", mContext.getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

                            }*/
							if (text_dialog.getText().equals("")) {
								AppUtils.showDialogAlertDismiss((Activity) mContext, mContext.getString(R.string.alert_heading), "Please enter your subject", R.drawable.exclamationicon, R.drawable.round);

							} else if (text_content.getText().toString().equals("")) {
								AppUtils.showDialogAlertDismiss((Activity) mContext, mContext.getString(R.string.alert_heading), "Please enter your content", R.drawable.exclamationicon, R.drawable.round);

							} else {
								if (AppUtils.isNetworkConnected(mContext)) {
									sendEmailToStaff(URL_SEND_EMAIL_TO_STAFF);

								} else {
									AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", mContext.getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

								}
							}

						}
					});

					dialog.show();
					;

				} else {
					AppUtils.showDialogAlertDismiss((Activity) mContext, mContext.getString(R.string.alert_heading), "This feature is available for registered users.", R.drawable.exclamationicon, R.drawable.round);

				}

			}
		});

		mAboutUsList.addOnItemTouchListener(new RecyclerItemListener(mContext, mAboutUsList,
				new RecyclerItemListener.RecyclerTouchListener() {
					public void onClickItem(View v, int position) {
						if(PreferenceManager.getUserId(mContext).equalsIgnoreCase(""))
						{
							if(!PreferenceManager.getStaffId(mContext).equalsIgnoreCase(""))
							{
								if(mAboutUsListArray.get(position).getTabType().equals("Facilities")){
									Intent mIntent=new Intent(getActivity(),FacilityActivity.class);
									mIntent.putExtra("array",mAboutUsListArray.get(position).getAboutusModelArrayList());
									mIntent.putExtra("desc",mAboutUsListArray.get(position).getItemDesc());
									mIntent.putExtra("title",mAboutUsListArray.get(position).getTabType());
									mIntent.putExtra("banner_image",mAboutUsListArray.get(position).getImageUrl());
									mContext.startActivity(mIntent);
									System.out.println("faci array--"+mAboutUsListArray.get(position).getAboutusModelArrayList().size());
								}else if(mAboutUsListArray.get(position).getTabType().equals("Accreditations & Examinations")){
									Intent mIntent = new Intent(mContext, AccreditationsActivity.class);
									mIntent.putExtra("array",mAboutUsListArray.get(position).getAboutusModelArrayList());
									mIntent.putExtra("desc",mAboutUsListArray.get(position).getItemDesc());
									mIntent.putExtra("title",mAboutUsListArray.get(position).getTabType());
									mIntent.putExtra("banner_image",mAboutUsListArray.get(position).getImageUrl());
									mContext.startActivity(mIntent);
								}
								else
								{
									Intent intent = new Intent(mContext, LoadUrlWebViewActivity.class);
									intent.putExtra("url",mAboutUsListArray.get(position).getUrl());
//							intent.putExtra("tab_type",mAboutUsListArray.get(position).getTabType());
									intent.putExtra("tab_type",mAboutUsListArray.get(position).getTabType());
									mContext.startActivity(intent);
								}
							}
							else {
								if (position==0)
								{
									Intent mIntent=new Intent(getActivity(),StaffDirectoryActivity.class);
									mContext.startActivity(mIntent);

								}else if(mAboutUsListArray.get(position).getTabType().equals("Facilities")){
									Intent mIntent=new Intent(getActivity(),FacilityActivity.class);
									mIntent.putExtra("array",mAboutUsListArray.get(position).getAboutusModelArrayList());
									mIntent.putExtra("desc",mAboutUsListArray.get(position).getItemDesc());
									mIntent.putExtra("title",mAboutUsListArray.get(position).getTabType());
									mIntent.putExtra("banner_image",mAboutUsListArray.get(position).getImageUrl());
									mContext.startActivity(mIntent);
									System.out.println("faci array--"+mAboutUsListArray.get(position).getAboutusModelArrayList().size());
								}else if(mAboutUsListArray.get(position).getTabType().equals("Accreditations & Examinations")){
									Intent mIntent = new Intent(mContext, AccreditationsActivity.class);
									mIntent.putExtra("array",mAboutUsListArray.get(position).getAboutusModelArrayList());
									mIntent.putExtra("desc",mAboutUsListArray.get(position).getItemDesc());
									mIntent.putExtra("title",mAboutUsListArray.get(position).getTabType());
									mIntent.putExtra("banner_image",mAboutUsListArray.get(position).getImageUrl());
									mContext.startActivity(mIntent);
								}
								else
								{
									Intent intent = new Intent(mContext, LoadUrlWebViewActivity.class);
									intent.putExtra("url",mAboutUsListArray.get(position).getUrl());
									intent.putExtra("tab_type",mAboutUsListArray.get(position).getTabType());
									mContext.startActivity(intent);
								}
							}
						}
						else {
							if (position==0)
							{
								Intent mIntent=new Intent(getActivity(),StaffDirectoryActivity.class);
								mContext.startActivity(mIntent);

							}else if(mAboutUsListArray.get(position).getTabType().equals("Facilities")){
								Intent mIntent=new Intent(getActivity(),FacilityActivity.class);
								mIntent.putExtra("array",mAboutUsListArray.get(position).getAboutusModelArrayList());
								mIntent.putExtra("desc",mAboutUsListArray.get(position).getItemDesc());
								mIntent.putExtra("title",mAboutUsListArray.get(position).getTabType());
								mIntent.putExtra("banner_image",mAboutUsListArray.get(position).getImageUrl());
								mContext.startActivity(mIntent);
								System.out.println("faci array--"+mAboutUsListArray.get(position).getAboutusModelArrayList().size());
							}else if(mAboutUsListArray.get(position).getTabType().equals("Accreditations & Examinations")){
								Intent mIntent = new Intent(mContext, AccreditationsActivity.class);
								mIntent.putExtra("array",mAboutUsListArray.get(position).getAboutusModelArrayList());
								mIntent.putExtra("desc",mAboutUsListArray.get(position).getItemDesc());
								mIntent.putExtra("title",mAboutUsListArray.get(position).getTabType());
								mIntent.putExtra("banner_image",mAboutUsListArray.get(position).getImageUrl());
								mContext.startActivity(mIntent);
							}
							else
							{
								Intent intent = new Intent(mContext, LoadUrlWebViewActivity.class);
								intent.putExtra("url",mAboutUsListArray.get(position).getUrl());
//							intent.putExtra("tab_type",mAboutUsListArray.get(position).getTabType());
								intent.putExtra("tab_type",mAboutUsListArray.get(position).getTabType());
								mContext.startActivity(intent);
							}
						}


					}

					public void onLongClickItem(View v, int position) {
					}
				}));
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

		if(PreferenceManager.getUserId(mContext).equalsIgnoreCase("")) {
			if (!PreferenceManager.getStaffId(mContext).equalsIgnoreCase("")) {
				 if(mAboutUsListArray.get(position).getTabType().equals("Facilities")){
					Intent mIntent=new Intent(getActivity(),FacilityActivity.class);
					mIntent.putExtra("array",mAboutUsListArray.get(position).getAboutusModelArrayList());
					mIntent.putExtra("desc",mAboutUsListArray.get(position).getItemDesc());
					mIntent.putExtra("title",mAboutUsListArray.get(position).getTabType());
					mIntent.putExtra("banner_image",mAboutUsListArray.get(position).getImageUrl());
					mContext.startActivity(mIntent);
					System.out.println("faci array--"+mAboutUsListArray.get(position).getAboutusModelArrayList().size());
				}else if(mAboutUsListArray.get(position).getTabType().equals("Accreditations & Examinations")){
					Intent mIntent = new Intent(mContext, AccreditationsActivity.class);
					mIntent.putExtra("array",mAboutUsListArray.get(position).getAboutusModelArrayList());
					mIntent.putExtra("desc",mAboutUsListArray.get(position).getItemDesc());
					mIntent.putExtra("title",mAboutUsListArray.get(position).getTabType());
					mIntent.putExtra("banner_image",mAboutUsListArray.get(position).getImageUrl());
					mContext.startActivity(mIntent);
				}
				else
				{
					Intent intent = new Intent(mContext, LoadUrlWebViewActivity.class);
					intent.putExtra("url",mAboutUsListArray.get(position).getUrl());
					intent.putExtra("tab_type",mAboutUsListArray.get(position).getTabType());
					mContext.startActivity(intent);
				}
			}
			else {
				if (position==0)
				{
					Intent mIntent=new Intent(getActivity(),StaffDirectoryActivity.class);
					mContext.startActivity(mIntent);

				}else if(mAboutUsListArray.get(position).getTabType().equals("Facilities")){
					Intent mIntent=new Intent(getActivity(),FacilityActivity.class);
					mIntent.putExtra("array",mAboutUsListArray.get(position).getAboutusModelArrayList());
					mIntent.putExtra("desc",mAboutUsListArray.get(position).getItemDesc());
					mIntent.putExtra("title",mAboutUsListArray.get(position).getTabType());
					mIntent.putExtra("banner_image",mAboutUsListArray.get(position).getImageUrl());
					mContext.startActivity(mIntent);
					System.out.println("faci array--"+mAboutUsListArray.get(position).getAboutusModelArrayList().size());
				}else if(mAboutUsListArray.get(position).getTabType().equals("Accreditations & Examinations")){
					Intent mIntent = new Intent(mContext, AccreditationsActivity.class);
					mIntent.putExtra("array",mAboutUsListArray.get(position).getAboutusModelArrayList());
					mIntent.putExtra("desc",mAboutUsListArray.get(position).getItemDesc());
					mIntent.putExtra("title",mAboutUsListArray.get(position).getTabType());
					mIntent.putExtra("banner_image",mAboutUsListArray.get(position).getImageUrl());
					mContext.startActivity(mIntent);
				}
				else
				{
					Intent intent = new Intent(mContext, LoadUrlWebViewActivity.class);
					intent.putExtra("url",mAboutUsListArray.get(position).getUrl());
					intent.putExtra("tab_type",mAboutUsListArray.get(position).getTabType());
					mContext.startActivity(intent);
				}
			}
		}
		else {
			if (position==0)
			{
				Intent mIntent=new Intent(getActivity(),StaffDirectoryActivity.class);
				mContext.startActivity(mIntent);

			}else if(mAboutUsListArray.get(position).getTabType().equals("Facilities")){
				Intent mIntent=new Intent(getActivity(),FacilityActivity.class);
				mIntent.putExtra("array",mAboutUsListArray.get(position).getAboutusModelArrayList());
				mIntent.putExtra("desc",mAboutUsListArray.get(position).getItemDesc());
				mIntent.putExtra("title",mAboutUsListArray.get(position).getTabType());
				mIntent.putExtra("banner_image",mAboutUsListArray.get(position).getImageUrl());
				mContext.startActivity(mIntent);
				System.out.println("faci array--"+mAboutUsListArray.get(position).getAboutusModelArrayList().size());
			}else if(mAboutUsListArray.get(position).getTabType().equals("Accreditations & Examinations")){
				Intent mIntent = new Intent(mContext, AccreditationsActivity.class);
				mIntent.putExtra("array",mAboutUsListArray.get(position).getAboutusModelArrayList());
				mIntent.putExtra("desc",mAboutUsListArray.get(position).getItemDesc());
				mIntent.putExtra("title",mAboutUsListArray.get(position).getTabType());
				mIntent.putExtra("banner_image",mAboutUsListArray.get(position).getImageUrl());
				mContext.startActivity(mIntent);
			}
			else
			{
				Intent intent = new Intent(mContext, LoadUrlWebViewActivity.class);
				intent.putExtra("url",mAboutUsListArray.get(position).getUrl());
				intent.putExtra("tab_type",mAboutUsListArray.get(position).getTabType());
				mContext.startActivity(intent);
			}
		}

//		Fragment fragment = new BrowserWebContents(mAboutUsListArray.get(
//				position).getCategoryName(), TAB_ABOUT);
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

	public void getList() {

		try {
			mAboutUsListArray = new ArrayList<>();
			final VolleyWrapper manager = new VolleyWrapper(URL_ABOUTUS_LIST);
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
//											bannerImagePager.setBackgroundResource(R.drawable.aboutbanner);

										}
										JSONArray dataArray = respObject.getJSONArray(JTAG_RESPONSE_DATA_ARRAY);
										AboutusModel model=new AboutusModel();
										model.setDescription(respObject.optString(JTAG_DESCRIPTION));
										model.setWebUrl(respObject.optString(JTAG_WEB_LINK));
										model.setEmail(respObject.optString(JTAG_CONTACT_EMAIL));
										email_nas=respObject.optString(JTAG_CONTACT_EMAIL);
										description.setText(respObject.optString(JTAG_DESCRIPTION));
										weburl.setText(respObject.optString(JTAG_WEB_LINK));
										weburlString=respObject.optString(JTAG_WEB_LINK);
										descriptionString=respObject.optString(JTAG_DESCRIPTION);
										if (descriptionString.equalsIgnoreCase("")&&email_nas.equalsIgnoreCase(""))
										{
											mtitleRel.setVisibility(View.GONE);
										}
										else
										{
											mtitleRel.setVisibility(View.VISIBLE);
										}
										if (descriptionString.equalsIgnoreCase(""))
										{
											description.setVisibility(View.GONE);
											descriptionTitle.setVisibility(View.GONE);
										}else
										{
											description.setText(descriptionString);
											descriptionTitle.setVisibility(View.GONE);
//											descriptionTitle.setVisibility(View.VISIBLE);
											description.setVisibility(View.VISIBLE);
											mtitleRel.setVisibility(View.VISIBLE);
										}
										if (email_nas.equalsIgnoreCase(""))
										{
											sendEmail.setVisibility(View.GONE);
										}else
										{
											mtitleRel.setVisibility(View.VISIBLE);
											sendEmail.setVisibility(View.VISIBLE);
										}
										if (weburlString.equalsIgnoreCase(""))
										{
											weburl.setVisibility(View.GONE);
										}else
										{
											weburl.setVisibility(View.VISIBLE);
										}
										if(!PreferenceManager.getUserId(mContext).equalsIgnoreCase(""))
										{
											if (dataArray.length() > 0) {
												for (int i = 0; i <= dataArray.length(); i++) {
													if (i!=0) {
														JSONObject dataObject = dataArray.getJSONObject(i-1);
														mAboutUsListArray.add(getSearchValues(dataObject));
													}
													else
													{
														AboutusModel mAboutUsModel = new AboutusModel();
														mAboutUsModel.setId("0");
														mAboutUsModel.setUrl("");
														mAboutUsModel.setTabType("Staff Directory");
														mAboutUsListArray.add(mAboutUsModel);
													}
												}

												mAboutUsList.setAdapter(new CustomAboutUsAdapter(getActivity(), mAboutUsListArray));
												//setListViewHeightBasedOnChildren(mAboutUsList);


											}
											else {
												//CustomStatusDialog();
												//Toast.makeText(mContext,"Failure",Toast.LENGTH_SHORT).show();
												AppUtils.showDialogAlertDismiss((Activity)mContext,"Alert",getString(R.string.nodatafound),R.drawable.exclamationicon,R.drawable.round);

											}
										}
										else
										{
											if (!PreferenceManager.getStaffId(mContext).equalsIgnoreCase(""))
											{
												System.out.println("Staff id"+PreferenceManager.getStaffId(mContext));
												if (dataArray.length() > 0) {
													System.out.println("Staff id"+PreferenceManager.getStaffId(mContext));
													for (int i = 0; i < dataArray.length(); i++) {
//														if (i!=0) {
														System.out.println("Staff id"+PreferenceManager.getStaffId(mContext));
															JSONObject dataObject = dataArray.getJSONObject(i);
															mAboutUsListArray.add(getSearchValues(dataObject));
//														}
//														else
//														{
//															AboutusModel mAboutUsModel = new AboutusModel();
//															mAboutUsModel.setId("0");
//															mAboutUsModel.setUrl("");
//															mAboutUsModel.setTabType("Staff Directory");
//															mAboutUsListArray.add(mAboutUsModel);
//														}
													}

													mAboutUsList.setAdapter(new CustomAboutUsAdapter(getActivity(), mAboutUsListArray));
													//setListViewHeightBasedOnChildren(mAboutUsList);


												}
												else {
													//CustomStatusDialog();
													//Toast.makeText(mContext,"Failure",Toast.LENGTH_SHORT).show();
													AppUtils.showDialogAlertDismiss((Activity)mContext,"Alert",getString(R.string.nodatafound),R.drawable.exclamationicon,R.drawable.round);

												}
											}
											else {
												if (dataArray.length() > 0) {
													for (int i = 0; i <= dataArray.length(); i++) {
														if (i!=0) {
															JSONObject dataObject = dataArray.getJSONObject(i-1);
															mAboutUsListArray.add(getSearchValues(dataObject));
														}
														else
														{
															AboutusModel mAboutUsModel = new AboutusModel();
															mAboutUsModel.setId("0");
															mAboutUsModel.setUrl("");
															mAboutUsModel.setTabType("Staff Directory");
															mAboutUsListArray.add(mAboutUsModel);
														}
													}

													mAboutUsList.setAdapter(new CustomAboutUsAdapter(getActivity(), mAboutUsListArray));
													//setListViewHeightBasedOnChildren(mAboutUsList);


												}
												else {
													//CustomStatusDialog();
													//Toast.makeText(mContext,"Failure",Toast.LENGTH_SHORT).show();
													AppUtils.showDialogAlertDismiss((Activity)mContext,"Alert",getString(R.string.nodatafound),R.drawable.exclamationicon,R.drawable.round);

												}
											}
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
									//Toast.makeText(mContext,"Failure",Toast.LENGTH_SHORT).show();
									AppUtils.showDialogAlertDismiss((Activity)mContext,"Alert",getString(R.string.common_error),R.drawable.exclamationicon,R.drawable.round);

								}
							} else  {
//								CustomStatusDialog(RESPONSE_FAILURE);
								//Toast.makeText(mContext,"Failure",Toast.LENGTH_SHORT).show();
								AppUtils.showDialogAlertDismiss((Activity)mContext,"Alert",getString(R.string.common_error),R.drawable.exclamationicon,R.drawable.round);

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
	private AboutusModel getSearchValues(JSONObject Object)
			throws JSONException {
		AboutusModel mAboutUsModel = new AboutusModel();
		mAboutUsModel.setId(Object.getString(JTAG_ID));
		mAboutUsModel.setUrl(Object.getString(JTAG_URL));
		mAboutUsModel.setTabType(Object.getString(JTAG_TAB_TYPE));
		mAboutUsModel.setItemDesc(Object.getString(JTAG_DESCRIPTION));
		mAboutUsModel.setImageUrl(Object.getString(JTAG_BANNER_IMAGE));
		JSONArray itemArray=Object.getJSONArray(JTAG_ITEMS);
		if(itemArray.length()>0) {
			mAboutUsModelListArray=new ArrayList<>();

			for (int i = 0; i < itemArray.length(); i++) {
				AboutusModel model = new AboutusModel();
				model.setItemPdfUrl(itemArray.getJSONObject(i).getString(JTAG_URL));
				model.setItemTitle(itemArray.getJSONObject(i).getString(JTAG_TITLE));
				mAboutUsModelListArray.add(model);
			}
			mAboutUsModel.setAboutusModelArrayList(mAboutUsModelListArray);
		}
		return mAboutUsModel;
	}
	public static void setListViewHeightBasedOnChildren(ListView listView) {


//        ListAdapter mAdapter = listView.getAdapter();
//
//        int listviewElementsheight = 0;
//
//        for(int i =0;i<mAdapter.getCount();i++) {
//            View mView = mAdapter.getView(i, null, listView);
//
//            mView.measure(View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
//            View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED));
//            listviewElementsheight += mView.getMeasuredHeight();
//        }
//        listView.setMinimumHeight(listviewElementsheight);
//                ViewGroup.LayoutParams params = listView.getLayoutParams();
//        params.height = listviewElementsheight ;
//        listView.setLayoutParams(params);



		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
//            listItem.measure(0, 0);
			listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
		listView.requestLayout();

//        ViewGroup.LayoutParams params = listView.getLayoutParams();
//        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
//        listView.setLayoutParams(params);
	}

	private void sendEmailToStaff(String URL) {
		VolleyWrapper volleyWrapper=new VolleyWrapper(URL);
		String[] name={"access_token","email", "users_id","title","message"};
		String[] value={PreferenceManager.getAccessToken(mContext),email_nas,PreferenceManager.getUserId(mContext),text_dialog.getText().toString(),text_content.getText().toString()};

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
                            /*Toast toast = Toast.makeText(mContext,"Successfully sent email to staff", Toast.LENGTH_SHORT);
                            toast.show();*/
							dialog.dismiss();
							AppUtils.showDialogAlertDismiss((Activity)mContext,"Success","Successfully sent email to staff",R.drawable.tick,R.drawable.round);

						}else{
							Toast toast = Toast.makeText(mContext,"Email not sent", Toast.LENGTH_SHORT);
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
}
