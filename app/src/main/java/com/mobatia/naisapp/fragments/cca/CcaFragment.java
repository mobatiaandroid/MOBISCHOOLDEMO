/**
 * 
 */
package com.mobatia.naisapp.fragments.cca;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.cca.CCA_Activity;
import com.mobatia.naisapp.activities.pdf.PdfReaderActivity;
import com.mobatia.naisapp.activities.web_view.LoadUrlWebViewActivity;
import com.mobatia.naisapp.constants.CacheDIRConstants;
import com.mobatia.naisapp.constants.IntentPassValueConstants;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.constants.NaisTabConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.cca.adapter.CCARecyclerAdapter;
import com.mobatia.naisapp.fragments.secondary.model.SecondaryModel;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.recyclerviewmanager.ItemOffsetDecoration;
import com.mobatia.naisapp.recyclerviewmanager.RecyclerItemListener;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author Rijo K Jose
 * 
 */

public class CcaFragment extends Fragment implements OnItemClickListener,
		NaisTabConstants,CacheDIRConstants, URLConstants,
		IntentPassValueConstants,NaisClassNameConstants,JSONConstants,StatusConstants {
	TextView mTitleTextView;

	private View mRootView;
	private Context mContext;
//	private ListView mListView;
	private String mTitle;
	private String mTabId;
	private String description="";
	private RelativeLayout relMain;
	TextView descriptionTV;
	TextView descriptionTitle;
	RelativeLayout mtitleRel;
	RelativeLayout CCAFRegisterRel;
	private ArrayList<SecondaryModel> mListViewArray;
	ImageView bannerImagePager;
//	ViewPager bannerImagePager;
	ArrayList<String> bannerUrlImageArray;
	private RecyclerView mListView;
	String contactEmail="";
	EditText text_dialog;
	EditText text_content;
	ImageView sendEmail;
	public CcaFragment() {

	}

	public CcaFragment(String title, String tabId) {
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
		mRootView = inflater.inflate(R.layout.fragment_ccas_list, container,
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
		mTitleTextView.setText(CCAS);
//		mListView = (ListView) mRootView.findViewById(R.id.mListView);
		bannerImagePager= (ImageView) mRootView.findViewById(R.id.bannerImagePager);
//		bannerImagePager= (ViewPager) mRootView.findViewById(R.id.bannerImagePager);
		mListView = (RecyclerView) mRootView.findViewById(R.id.mListView);
		descriptionTV= (TextView) mRootView.findViewById(R.id.descriptionTV);
		descriptionTitle= (TextView) mRootView.findViewById(R.id.descriptionTitle);
		sendEmail= (ImageView) mRootView.findViewById(R.id.sendEmail);
		mtitleRel = (RelativeLayout) mRootView.findViewById(R.id.title);
		CCAFRegisterRel = (RelativeLayout) mRootView.findViewById(R.id.CCAFRegisterRel);
		relMain = (RelativeLayout) mRootView.findViewById(R.id.relMain);
		relMain.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		CCAFRegisterRel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (PreferenceManager.getUserId(mContext).equalsIgnoreCase("")) {
					AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert","This feature is available for registered users", R.drawable.exclamationicon, R.drawable.round);
				}
				else
				{
					Intent intent = new Intent(mContext, CCA_Activity.class);
					intent.putExtra("tab_type", CCAS);
					mContext.startActivity(intent);
				}
//				AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.module_under_construction), R.drawable.exclamationicon, R.drawable.round);

			}
		});
//		mListView.setOnItemClickListener(this);
		if (AppUtils.checkInternet(mContext)) {

			getList();
		}
		else
		{
			AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

		}
		mListView.setHasFixedSize(true);
//        mNewsLetterListView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.list_divider)));
		GridLayoutManager recyclerViewLayout= new GridLayoutManager(mContext, 4);
		int spacing = 5; // 50px
		ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext,spacing);
		mListView.addItemDecoration(itemDecoration);
		mListView.setLayoutManager(recyclerViewLayout);

		mListView.addOnItemTouchListener(new RecyclerItemListener(mContext, mListView,
				new RecyclerItemListener.RecyclerTouchListener() {
					public void onClickItem(View v, int position) {
						if (mListViewArray.get(position).getmFile().endsWith(".pdf")) {
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
						}

					}

					public void onLongClickItem(View v, int position) {
						System.out.println("On Long Click Item interface");
					}
				}));
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
		PreferenceManager.setStudIdForCCA(mContext, "");
		PreferenceManager.setCCAStudentIdPosition(mContext, "0");
		PreferenceManager.setStudNameForCCA(mContext, "");
		PreferenceManager.setStudClassForCCA(mContext, "");
		PreferenceManager.setCCATitle(mContext, "");
		PreferenceManager.setCCAItemId(mContext, "");
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
		if (mListViewArray.get(position).getmFile().endsWith(".pdf")) {
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
		}

	}

	public void getList() {

		try {
			mListViewArray = new ArrayList<>();
			final VolleyWrapper manager = new VolleyWrapper(URL_CCAS_LIST);
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
										 description = respObject.optString(JTAG_DESCRIPTION);
										contactEmail = respObject.optString(JTAG_CONTACT_EMAIL);
										if (!bannerImage.equalsIgnoreCase("")) {
											Glide.with(mContext).load(AppUtils.replace(bannerImage)).centerCrop().into(bannerImagePager);

//											bannerUrlImageArray = new ArrayList<>();
//											bannerUrlImageArray.add(bannerImage);
//											bannerImagePager.setAdapter(new ImagePagerDrawableAdapter(bannerUrlImageArray, getActivity()));
										}
										else
										{
											bannerImagePager.setBackgroundResource(R.drawable.default_banner);
//											bannerImagePager.setBackgroundResource(R.drawable.ccas_banner);

										}
										if (description.equalsIgnoreCase("")&&contactEmail.equalsIgnoreCase(""))
										{
											mtitleRel.setVisibility(View.GONE);
										}
										else
										{
											mtitleRel.setVisibility(View.VISIBLE);
										}
										if (description.equalsIgnoreCase(""))
										{
											descriptionTV.setVisibility(View.GONE);
											descriptionTitle.setVisibility(View.GONE);
										}else
										{
											descriptionTV.setText(description);
											descriptionTitle.setVisibility(View.GONE);
//											descriptionTitle.setVisibility(View.VISIBLE);
											descriptionTV.setVisibility(View.VISIBLE);
											mtitleRel.setVisibility(View.VISIBLE);
										}
										if (contactEmail.equalsIgnoreCase(""))
										{
											sendEmail.setVisibility(View.GONE);
										}else
										{
											mtitleRel.setVisibility(View.VISIBLE);
											sendEmail.setVisibility(View.VISIBLE);
										}
										CCAFRegisterRel.setVisibility(View.VISIBLE);
										JSONArray dataArray = respObject.getJSONArray(JTAG_RESPONSE_DATA_ARRAY);
										if (dataArray.length() > 0) {
											for (int i = 0; i <dataArray.length(); i++) {
												JSONObject dataObject = dataArray.getJSONObject(i);
												mListViewArray.add(getSearchValues(dataObject));

											}

//											mListView.setAdapter(new CustomSecondaryAdapter(getActivity(), mListViewArray));
											mListView.setAdapter(new CCARecyclerAdapter(mContext, mListViewArray));

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
		mSecondaryModel.setmFile(Object.getString(JTAG_TAB_FILE));
		mSecondaryModel.setmName(Object.getString(JTAG_TAB_NAME));
		return mSecondaryModel;
	}
	private void sendEmailToStaff(String URL) {
		VolleyWrapper volleyWrapper=new VolleyWrapper(URL);
		String[] name={"access_token","email","users_id","title","message"};
		String[] value={PreferenceManager.getAccessToken(mContext),contactEmail,PreferenceManager.getUserId(mContext),text_dialog.getText().toString(),text_content.getText().toString()};//contactEmail

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
		PreferenceManager.setStudIdForCCA(mContext, "");
		PreferenceManager.setCCAStudentIdPosition(mContext, "0");
		PreferenceManager.setStudNameForCCA(mContext, "");
		PreferenceManager.setStudClassForCCA(mContext, "");
		PreferenceManager.setCCATitle(mContext, "");
		PreferenceManager.setCCAItemId(mContext, "");
	}
}
