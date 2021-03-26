package com.mobatia.naisapp.activities.coming_up.secondary;

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
import android.widget.Toast;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.coming_up.adapter.ComingUpAdapter;
import com.mobatia.naisapp.activities.coming_up.coming_up_detail.ComingUpDetailWebViewActivity;
import com.mobatia.naisapp.activities.coming_up.model.ComingUpModel;
import com.mobatia.naisapp.activities.home.HomeListActivity;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.activities.staff_directory.StaffListActivity;
import com.mobatia.naisapp.activities.staff_directory.adapter.CustomStaffDirectoryAdapter;
import com.mobatia.naisapp.activities.staff_directory.model.StaffModel;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.HeaderManager;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.volleywrappermanager.CustomDialog;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

@SuppressLint("NewApi")
public class SecondaryActivity extends Activity implements
		 URLConstants,JSONConstants,StatusConstants,NaisClassNameConstants {

	private Context mContext;

	RelativeLayout relativeHeader;
	HeaderManager headermanager;
	ImageView back;
	ImageView home;
	ArrayList<ComingUpModel> mComingUpModelListArray = new ArrayList<ComingUpModel>();
	private ListView mComingUpListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coming_up);

		mContext = this;
		initialiseUI();
		if(AppUtils.isNetworkConnected(mContext)) {
			callStaffDirectoryListAPI(URL_SECONDARY_COMING_UP_LIST);
		}else{
			AppUtils.showDialogAlertDismiss((Activity)mContext,"Network Error",getString(R.string.no_internet),R.drawable.nonetworkicon,R.drawable.roundred);

		}

	}

	private void callStaffDirectoryListAPI(String URL) {
		VolleyWrapper volleyWrapper=new VolleyWrapper(URL);
		String[] name={JTAG_ACCESSTOKEN};
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
							if (data.length() > 0) {
								for (int i = 0; i < data.length(); i++) {
									JSONObject dataObject = data.getJSONObject(i);
									mComingUpModelListArray.add(addStaffDetails(dataObject));
								}

								ComingUpAdapter customStaffDirectoryAdapter = new ComingUpAdapter(mContext, mComingUpModelListArray);
								mComingUpListView.setAdapter(customStaffDirectoryAdapter);
							} else {
								Toast.makeText(SecondaryActivity.this, "No data found", Toast.LENGTH_SHORT).show();
							}
						}
					} 	else if (response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) ||
							response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
							response_code.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
						AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
							@Override
							public void getAccessToken() {
							}
						});
						callStaffDirectoryListAPI(URL_SECONDARY_COMING_UP_LIST);

					}else {

						AppUtils.showDialogAlertDismiss((Activity)mContext,"Alert",getString(R.string.common_error),R.drawable.exclamationicon,R.drawable.round);

					}
				} catch (Exception ex) {
					System.out.println("The Exception in edit profile is" + ex.toString());
				}

			}

			@Override
			public void responseFailure(String failureResponse) {

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
		mComingUpListView = (ListView) findViewById(R.id.mComingUpListView);
		headermanager = new HeaderManager(SecondaryActivity.this, "Coming Up");
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
				Intent in = new Intent(mContext, HomeListAppCompatActivity.class);
				in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(in);
			}
		});
		mComingUpListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String webViewComingUpDetail= "<!DOCTYPE html>\n" +
						"<html>\n" +
						"<head>\n" +
						"<style>\n" +
						"\n" +
						"@font-face {\n" +
						"font-family: SourceSansPro-Semibold;"+
						"src: url(SourceSansPro-Semibold.otf);"+

						"font-family: SourceSansPro-Regular;"+
						"src: url(SourceSansPro-Regular.otf);"+
						"}"+
						".title {"+
						"font-family: SourceSansPro-Regular;"+
						"font-size:16px;"+
						"text-align:left;"+
						"color:	#46C1D0;"+
						"}"+

						".description {"+
						"font-family: SourceSansPro-Light;"+
						"text-align:justify;"+
						"font-size:14px;"+
						"color: #000000;"+
						"text-align: left;"+
						"}"+
						"</style>\n"+"</head>"+
						"<body>"+
						"<p class='title'>"+mComingUpModelListArray.get(position).getTitle()+"</p>";
				if (!mComingUpModelListArray.get(position).getImage().equalsIgnoreCase("")) {
					webViewComingUpDetail = webViewComingUpDetail + "<center><img src='" + mComingUpModelListArray.get(position).getImage() + "'width='100%', height='auto'>";
				}
				webViewComingUpDetail=webViewComingUpDetail+"<p class='description'>"+mComingUpModelListArray.get(position).getDescription()+"</p>"+
						"</body>\n</html>";
				Intent mIntent = new Intent(mContext, ComingUpDetailWebViewActivity.class);
				mIntent.putExtra("webViewComingDetail", webViewComingUpDetail);
				mIntent.putExtra("tab_type", "Coming Up");

				startActivity(mIntent);
			}
		});
		}

	private ComingUpModel addStaffDetails(JSONObject obj) {
		ComingUpModel model = new ComingUpModel();
		try {
//			model.setStaffCategoryId(obj.optString("id"));
			model.setTitle(obj.optString(JTAG_TITLE));
			model.setDescription(obj.optString(JTAG_DESCRIPTION));
			model.setDate(obj.optString(JTAG_DATE));
			model.setImage(obj.optString(JTAG_IMAGE));
		} catch (Exception ex) {
			System.out.println("Exception is" + ex);
		}

		return model;
	}

}
