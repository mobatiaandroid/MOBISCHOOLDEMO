package com.mobatia.naisapp.activities.staff_directory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.activities.staff_directory.adapter.CustomStaffDirectoryAdapter;
import com.mobatia.naisapp.activities.staff_directory.model.StaffModel;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.HeaderManager;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.recyclerviewmanager.DividerItemDecoration;
import com.mobatia.naisapp.recyclerviewmanager.RecyclerItemListener;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

@SuppressLint("NewApi")
public class StaffDirectoryActivity extends Activity implements
		URLConstants,JSONConstants{

	private Context mContext;

	RelativeLayout relativeHeader;
	HeaderManager headermanager;
	ImageView back;
	ImageView home;
	ArrayList<StaffModel> mStaffDirectoryListArray = new ArrayList<StaffModel>();
	ArrayList<StaffModel> filtered = new ArrayList<StaffModel>();
	//private ListView mStaffDirectoryList;
	RecyclerView mStaffDirectoryListView;
	ArrayList<String>bannerUrlImageArray;
	//	ImageView bannerImagePager;
	LinearLayout linearSearch;
	EditText searchEditText;
	ImageView btnImgsearch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_staffdirectory_list);

		mContext = this;
		initialiseUI();
		if(AppUtils.isNetworkConnected(mContext)) {
			callStaffDirectoryListAPI(URL_STAFFDIRECTORY_LIST);
		}else{
			AppUtils.showDialogAlertDismiss((Activity)mContext,"Network Error",getString(R.string.no_internet),R.drawable.nonetworkicon,R.drawable.roundred);

		}

	}

	private void callStaffDirectoryListAPI(String URL) {
		mStaffDirectoryListArray=new ArrayList<>();
		VolleyWrapper volleyWrapper=new VolleyWrapper(URL);
		String[] name={"access_token"};
		String[] value={PreferenceManager.getAccessToken(mContext)};
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
							JSONArray data = secobj.getJSONArray("data");
//							String bannerImage=secobj.optString(JTAG_BANNER_IMAGE);
//							System.out.println("banner img---"+bannerImage);
//							if (!bannerImage.equalsIgnoreCase("")) {
//								Glide.with(mContext).load(AppUtils.replace(bannerImage)).centerCrop().placeholder(R.drawable.default_banner).error(R.drawable.default_banner).into(bannerImagePager);
//
////								bannerUrlImageArray = new ArrayList<>();
////								bannerUrlImageArray.add(bannerImage);
////								bannerImagePager.setAdapter(new ImagePagerDrawableAdapter(bannerUrlImageArray, mContext));
//							}
//							else
//							{
//								bannerImagePager.setBackgroundResource(R.drawable.staffdirectory);
//
//							}
							if (data.length() > 0) {
								for (int i = 0; i < data.length(); i++) {
									JSONObject dataObject = data.getJSONObject(i);
									mStaffDirectoryListArray.add(addStaffDetails(dataObject));
								}

								CustomStaffDirectoryAdapter customStaffDirectoryAdapter = new CustomStaffDirectoryAdapter(mContext, mStaffDirectoryListArray);
								mStaffDirectoryListView.setAdapter(customStaffDirectoryAdapter);
							} else {
								Toast.makeText(StaffDirectoryActivity.this, "No data found", Toast.LENGTH_SHORT).show();
							}
						}
					} else if (response_code.equalsIgnoreCase("500")) {
						AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

					} else if (response_code.equalsIgnoreCase("400")) {
						AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
							@Override
							public void tokenrenewed() {
							}
						});
						callStaffDirectoryListAPI(URL_STAFFDIRECTORY_LIST);

					} else if (response_code.equalsIgnoreCase("401")) {
						AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
							@Override
							public void tokenrenewed() {
							}
						});
						callStaffDirectoryListAPI(URL_STAFFDIRECTORY_LIST);

					} else if (response_code.equalsIgnoreCase("402")) {
						AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
							@Override
							public void tokenrenewed() {
							}
						});
						callStaffDirectoryListAPI(URL_STAFFDIRECTORY_LIST);

					} else {
						/*CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
								, getResources().getString(R.string.ok));
						dialog.show();*/
						AppUtils.showDialogAlertDismiss((Activity)mContext,"Alert",getString(R.string.common_error),R.drawable.exclamationicon,R.drawable.round);

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
				AppUtils.showDialogAlertDismiss((Activity)mContext,"Alert",getString(R.string.common_error),R.drawable.exclamationicon,R.drawable.round);

			}
		});


	}

	/*******************************************************
	 * Method name : initialiseUI Description : initialise UI elements
	 * Parameters : nil Return type : void Date : March 8, 2017 Author : RIJO K JOSE
	 *****************************************************/
	@SuppressWarnings("deprecation")
	public void initialiseUI() {
		relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
		linearSearch = (LinearLayout) findViewById(R.id.linearSearch);
		searchEditText = (EditText) findViewById(R.id.searchEditText);
		btnImgsearch= (ImageView) findViewById(R.id.btnImgsearch);
//		bannerImagePager= (ImageView) findViewById(R.id.bannerImageViewPager);
		mStaffDirectoryListView = (RecyclerView) findViewById(R.id.mStaffDirectoryListView);
		mStaffDirectoryListView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.list_divider)));
		mStaffDirectoryListView.setHasFixedSize(true);
		LinearLayoutManager llm = new LinearLayoutManager(this);
		llm.setOrientation(LinearLayoutManager.VERTICAL);
		mStaffDirectoryListView.setLayoutManager(llm);
		headermanager = new HeaderManager(StaffDirectoryActivity.this, "Staff Directory");
		headermanager.getHeader(relativeHeader, 0);
		back = headermanager.getLeftButton();
		headermanager.setButtonLeftSelector(R.drawable.back,
				R.drawable.back);
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
				if (AppUtils.isEditTextFocused(StaffDirectoryActivity.this)) {
					AppUtils.hideKeyBoard(mContext);
				}
				Intent in = new Intent(mContext, HomeListAppCompatActivity.class);
				in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(in);
			}
		});
		mStaffDirectoryListView.addOnItemTouchListener(new RecyclerItemListener(getApplicationContext(), mStaffDirectoryListView,
				new RecyclerItemListener.RecyclerTouchListener() {
					public void onClickItem(View v, int position) {
						if (filtered.size()>0)
						{
							Intent intent=new Intent(StaffDirectoryActivity.this,StaffListActivity.class);
							intent.putExtra("category_id",filtered.get(position).getStaffCategoryId());
							intent.putExtra("title",filtered.get(position).getStaffCategoryName());
							startActivity(intent);
						}
						else
						{
							Intent intent=new Intent(StaffDirectoryActivity.this,StaffListActivity.class);
							intent.putExtra("category_id",mStaffDirectoryListArray.get(position).getStaffCategoryId());
							intent.putExtra("title",mStaffDirectoryListArray.get(position).getStaffCategoryName());
							startActivity(intent);
						}

					}

					public void onLongClickItem(View v, int position) {
						System.out.println("On Long Click Item interface");
					}
				}));
		filtered = new ArrayList<StaffModel>();


		btnImgsearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mStaffDirectoryListArray.size() > 0) {
					filtered = new ArrayList<StaffModel>();
					for (int i = 0; i < mStaffDirectoryListArray.size(); i++) {
						if (mStaffDirectoryListArray.get(i).getStaffCategoryName()
								.toLowerCase().contains(searchEditText.getText().toString().toLowerCase())) {
							filtered.add(mStaffDirectoryListArray.get(i));
						}
					}
					CustomStaffDirectoryAdapter sportsEventListAdapters = new CustomStaffDirectoryAdapter(mContext, filtered);
					mStaffDirectoryListView.setAdapter(sportsEventListAdapters);
					if (searchEditText.getText().toString()
							.equalsIgnoreCase("")) {
						CustomStaffDirectoryAdapter sportsEventListAdapter=new CustomStaffDirectoryAdapter(mContext,mStaffDirectoryListArray);
						mStaffDirectoryListView.setAdapter(sportsEventListAdapter);
						filtered = new ArrayList<StaffModel>();

					}
				}
				AppUtils.hideKeyBoard(mContext);

			}

		});
		searchEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start,
									  int before, int count) {
				// TODO Auto-generated method stub
				if (mStaffDirectoryListArray.size() > 0) {
					filtered = new ArrayList<StaffModel>();
					for (int i = 0; i < mStaffDirectoryListArray.size(); i++) {
						if (mStaffDirectoryListArray.get(i).getStaffCategoryName()
								.toLowerCase().contains(s.toString().toLowerCase())) {
							filtered.add(mStaffDirectoryListArray.get(i));
						}
					}
					CustomStaffDirectoryAdapter sportsEventListAdapters = new CustomStaffDirectoryAdapter(mContext, filtered);
					mStaffDirectoryListView.setAdapter(sportsEventListAdapters);
					if (searchEditText.getText().toString()
							.equalsIgnoreCase("")) {
						CustomStaffDirectoryAdapter sportsEventListAdapter=new CustomStaffDirectoryAdapter(mContext,mStaffDirectoryListArray);
						mStaffDirectoryListView.setAdapter(sportsEventListAdapter);
						filtered = new ArrayList<StaffModel>();

					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start,
										  int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
	}

	private StaffModel addStaffDetails(JSONObject obj) {
		StaffModel model = new StaffModel();
		try {
			model.setStaffCategoryId(obj.optString("id"));
			model.setStaffCategoryName(obj.optString("category_name"));
		} catch (Exception ex) {
			System.out.println("Exception is" + ex);
		}

		return model;
	}

}
