package com.mobatia.naisapp.fragments.communications;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.activities.pdf.PdfReaderActivity;
import com.mobatia.naisapp.activities.sports.teams.model.TeamModel;
import com.mobatia.naisapp.activities.web_view.LoadUrlWebViewActivity;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.constants.ResultConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.communications.adapter.CommunicationInformationRecyclerAdapter;
import com.mobatia.naisapp.fragments.secondary.model.SecondaryModel;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.HeaderManager;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.recyclerviewmanager.DividerItemDecoration;
import com.mobatia.naisapp.recyclerviewmanager.RecyclerItemListener;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CommunicationInformationActivity extends Activity
        implements JSONConstants,URLConstants,ResultConstants,StatusConstants,NaisClassNameConstants {
    private Context mContext;
    Bundle extras;
    String tab_type;
    RelativeLayout relativeHeader;
    LinearLayout mStudentSpinner;
    ImageView studImg;
    TextView studName;
    RecyclerView mnewsLetterListView;
    ArrayList<TeamModel> studentsModelArrayList;
    TextView textViewYear;
    static String stud_id = "";
    String stud_class = "";
    String stud_name = "";
    String stud_img = "";
    String section = "";
    private ArrayList<SecondaryModel> mListViewArray;
    CommunicationInformationRecyclerAdapter customStaffDirectoryAdapter;
    HeaderManager headermanager;
    ImageView back;
    ImageView home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        mContext = this;
        initUI();
        if (AppUtils.checkInternet(mContext)) {

            getList();
        } else {
            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

        }

    }

    private void initUI() {
        extras = getIntent().getExtras();
        if (extras != null) {
            tab_type = extras.getString("tab_type");
        }
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        mStudentSpinner = (LinearLayout) findViewById(R.id.studentSpinner);
        studImg = (ImageView) findViewById(R.id.imagicon);
        studName = (TextView) findViewById(R.id.studentName);
        textViewYear = (TextView) findViewById(R.id.textViewYear);
        mnewsLetterListView = (RecyclerView) findViewById(R.id.mnewsLetterListView);
        mnewsLetterListView.setHasFixedSize(true);
        mnewsLetterListView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.list_divider_teal)));
        headermanager = new HeaderManager(CommunicationInformationActivity.this, tab_type);
        headermanager.getHeader(relativeHeader, 0);
        back = headermanager.getLeftButton();
        headermanager.setButtonLeftSelector(R.drawable.back,
                R.drawable.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.hideKeyBoard(mContext);
                finish();
            }
        });
        home = headermanager.getLogoButton();
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(mContext, HomeListAppCompatActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
            }
        });
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mnewsLetterListView.setLayoutManager(llm);
        mnewsLetterListView.addOnItemTouchListener(new RecyclerItemListener(getApplicationContext(), mnewsLetterListView,
                new RecyclerItemListener.RecyclerTouchListener() {
                    public void onClickItem(View v, int position) {


                        if (mListViewArray.get(position).getmName().endsWith(".pdf")) {
                            Intent intent = new Intent(mContext, PdfReaderActivity.class);
                            intent.putExtra("pdf_url", mListViewArray.get(position).getmFile());
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(mContext, LoadUrlWebViewActivity.class);
                            intent.putExtra("url", mListViewArray.get(position).getmName());
                            intent.putExtra("tab_type", mListViewArray.get(position).getmFile());
                            mContext.startActivity(intent);
                        }
                    }

                    public void onLongClickItem(View v, int position) {
                        System.out.println("On Long Click Item interface");
                    }
                }));
    }
    public void getList() {


            mListViewArray = new ArrayList<>();
            final VolleyWrapper manager = new VolleyWrapper(URL_COMMUNICATION_INFORMATION);
            String[] name = {JTAG_ACCESSTOKEN};
            String[] value = {PreferenceManager.getAccessToken(mContext)};
            manager.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {
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
                                String bannerImage = secobj.optString(JTAG_BANNER_IMAGE);
                                JSONArray responses = secobj.getJSONArray(JTAG_RESPONSE_DATA_ARRAY);

                                if (responses.length() > 0) {
                                    for (int i = 0; i < responses.length(); i++) {
                                        JSONObject dataObject = responses.getJSONObject(i);
                                        mListViewArray.add(getSearchValues(dataObject));

                                    }
                                    customStaffDirectoryAdapter = new CommunicationInformationRecyclerAdapter(mContext, mListViewArray);

                                    mnewsLetterListView.setAdapter(customStaffDirectoryAdapter);

//											mListView.setAdapter(new CustomSecondaryAdapter(getActivity(), mListViewArray));
                                    // mnewsLetterListView.setAdapter(new InformationRecyclerAdapter(mContext, mListViewArray));

                                } else {
                                    Toast.makeText(mContext, "No data found", Toast.LENGTH_SHORT).show();
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
                            getList();

                        } else if (response_code.equalsIgnoreCase("401")) {
                            AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                                @Override
                                public void tokenrenewed() {
                                }
                            });
                            getList();

                        } else if (response_code.equalsIgnoreCase("402")) {
                            AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                                @Override
                                public void tokenrenewed() {
                                }
                            });
                            getList();

                        } else {
						/*CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
								, getResources().getString(R.string.ok));
						dialog.show();*/
                            AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

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
                    AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

                }

            });



    }
    private SecondaryModel getSearchValues(JSONObject Object)
            throws JSONException {
        SecondaryModel mSecondaryModel = new SecondaryModel();
        mSecondaryModel.setmId(Object.getString(JTAG_ID));
        mSecondaryModel.setmFile(Object.getString(JTAG_TAB_SUBMENU_ARRAY));
        mSecondaryModel.setmName(Object.getString(JTAG_TAB_FILE_NAME));
        return mSecondaryModel;
    }


}