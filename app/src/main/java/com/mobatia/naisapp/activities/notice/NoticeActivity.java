package com.mobatia.naisapp.activities.notice;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mobatia.naisapp.R;

import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.activities.notice.adapter.NoticeAdapter;
import com.mobatia.naisapp.activities.notice.model.NoticeModel;
import com.mobatia.naisapp.activities.pdf.PdfReaderActivity;
import com.mobatia.naisapp.activities.web_view.LoadUrlWebViewActivity;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

@SuppressLint("NewApi")
public class NoticeActivity extends Activity implements
		 URLConstants,JSONConstants,StatusConstants,NaisClassNameConstants {

	private Context mContext;

	RelativeLayout relativeHeader;
	HeaderManager headermanager;
	ImageView back;
	ImageView home;
	ArrayList<NoticeModel> mComingUpModelListArray = new ArrayList<NoticeModel>();
	private ListView mComingUpListView;
Bundle extras;
	String tab_type;
	String myFormatCalender = "yyyy-MM-dd";
	SimpleDateFormat sdfcalender;
    NoticeAdapter customStaffDirectoryAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coming_up);

		mContext = this;
		myFormatCalender = "yyyy-MM-dd";
		sdfcalender = new SimpleDateFormat(myFormatCalender, Locale.ENGLISH);
		initialiseUI();
		if(AppUtils.isNetworkConnected(mContext)) {
			callStaffDirectoryListAPI(URL_GET_NOTICE_LIST);
		}else{
			AppUtils.showDialogAlertDismiss((Activity)mContext,"Network Error",getString(R.string.no_internet),R.drawable.nonetworkicon,R.drawable.roundred);

		}

	}

	private void callStaffDirectoryListAPI(String URL) {
		VolleyWrapper volleyWrapper=new VolleyWrapper(URL);
		String[] name={JTAG_ACCESSTOKEN,"users_id"};
		String[] value={PreferenceManager.getAccessToken(mContext),PreferenceManager.getUserId(mContext)};
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

								customStaffDirectoryAdapter = new NoticeAdapter(mContext, mComingUpModelListArray);
								mComingUpListView.setAdapter(customStaffDirectoryAdapter);
							} else {
								Toast.makeText(NoticeActivity.this, "No data found", Toast.LENGTH_SHORT).show();
							}
						}
					} else if (response_code.equalsIgnoreCase("500")) {
						AppUtils.showDialogAlertDismiss((Activity)mContext,"Alert",getString(R.string.common_error),R.drawable.exclamationicon,R.drawable.round);

					}	else if (response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) ||
							response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
							response_code.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
						AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
							@Override
							public void getAccessToken() {
							}
						});
						callStaffDirectoryListAPI(URL_GET_NOTICE_LIST);

					}else {
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
		extras = getIntent().getExtras();
		if (extras != null) {
			tab_type=extras.getString("tab_type");
		}
		relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
		mComingUpListView = (ListView) findViewById(R.id.mComingUpListView);
		headermanager = new HeaderManager(NoticeActivity.this, tab_type);
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
			    if(mComingUpModelListArray.get(position).getStatus().equalsIgnoreCase("0")||mComingUpModelListArray.get(position).getStatus().equalsIgnoreCase("2"))
                {
                    callStatusChangeApi(URL_GET_STATUS_CHANGE_API,mComingUpModelListArray.get(position).getId(),position,mComingUpModelListArray.get(position).getStatus());

                }

				if (mComingUpModelListArray.get(position).getFilename().endsWith(".pdf")) {
					Intent intent = new Intent(mContext, PdfReaderActivity.class);
					intent.putExtra("pdf_url", mComingUpModelListArray.get(position).getFilename());
					startActivity(intent);
				}
				else
				{
					Intent intent = new Intent(mContext, LoadUrlWebViewActivity.class);
					intent.putExtra("url",mComingUpModelListArray.get(position).getFilename());
					intent.putExtra("tab_type",tab_type);
					mContext.startActivity(intent);
				}
			}
		});
		}

	private NoticeModel addStaffDetails(JSONObject obj) {
		NoticeModel model = new NoticeModel();
		try {
			model.setId(obj.optString(JTAG_ID));
			model.setTitle(obj.optString(JTAG_TITLE));
			model.setType(obj.optString(JTAG_TYPE));
			model.setStatus(obj.optString("status"));
//			model.setCreated_on(obj.optString(JTAG_CREATED_ON));
			model.setStart_date(obj.optString(JTAG_CREATED_ON));
			model.setEnd_date(obj.optString(JTAG_CREATED_ON));
			model.setFilename(obj.optString(JTAG_TAB_FILE_NAME));
			Date mNoticeDate = new Date();
			Date mNoticeEndDate = new Date();
//			String mDate=obj.optString(JTAG_CREATED_ON);
			String mDate=obj.optString(JTAG_EVENT_STARTDATE);
			String mDateEnd=obj.optString(JTAG_EVENT_ENDDATE);
			/* "start_date": "2017-05-18",
        "end_date"*/
			try {
				mNoticeDate = sdfcalender.parse(mDate);
				mNoticeEndDate = sdfcalender.parse(mDateEnd);
			} catch (ParseException ex) {
				Log.e("Date", "Parsing error");
			}
//			String dayOfTheWeek = (String) DateFormat.format("EEEE", mNoticeDate); // Thursday
			String day          = (String) DateFormat.format("dd",   mNoticeDate); // 20
			String monthString  = (String) DateFormat.format("MMM",  mNoticeDate); // Jun
//			String monthNumber  = (String) DateFormat.format("MM",   mNoticeDate); // 06
			String year         = (String) DateFormat.format("yyyy", mNoticeDate); // 2013
			String dayEnd          = (String) DateFormat.format("dd",   mNoticeEndDate); // 20
			String monthStringEnd  = (String) DateFormat.format("MMM",  mNoticeEndDate); // Jun
			String yearEnd         = (String) DateFormat.format("yyyy", mNoticeEndDate); // 2013

			model.setDay(day);
			model.setMonth(monthString);
			model.setYear(year);
			model.setDayEnd(dayEnd);
			model.setMonthEnd(monthStringEnd);
			model.setYearEnd(yearEnd);

		} catch (Exception ex) {
			System.out.println("Exception is" + ex);
		}

		return model;
	}
    private void callStatusChangeApi(final String URL, final String id, final int position, final String status) {
        VolleyWrapper volleyWrapper = new VolleyWrapper(URL);
        String[] name = {"access_token","users_id","id","type"};
        String[] value = {PreferenceManager.getAccessToken(mContext),PreferenceManager.getUserId(mContext),id,"notice"};
        volleyWrapper.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
            @Override
            public void responseSuccess(String successResponse) {
                System.out.println("The response is status change" + successResponse);
                try {
                    JSONObject obj = new JSONObject(successResponse);
                    String response_code = obj.getString(JTAG_RESPONSECODE);
                    if (response_code.equalsIgnoreCase("200")) {
                        JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
                        String status_code = secobj.getString(JTAG_STATUSCODE);
                        if (status_code.equalsIgnoreCase("303")) {
                             mComingUpModelListArray.get(position).setStatus("1");
                            customStaffDirectoryAdapter.notifyDataSetChanged();

                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                        // AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                    } else if (response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) ||
                            response_code.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                            response_code.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
                        AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                            @Override
                            public void getAccessToken() {

                            }

                        });
                        callStatusChangeApi(URL,id,position,status);

                    } else {

                    }
                } catch (Exception ex) {
                    System.out.println("The Exception in edit profile is" + ex.toString());
                }

            }

            @Override
            public void responseFailure(String failureResponse) {
                //AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

            }
        });


    }

}
