package com.mobatia.naisapp.activities.newsletters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.activities.newsletters.adapter.NewsLatterRecyclerViewAdapter;
import com.mobatia.naisapp.activities.newsletters.model.NewsLetterModel;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.ResultConstants;
import com.mobatia.naisapp.constants.StatusConstants;
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

/**
 * Created by gayatri on 23/3/17.
 */
public class NewsLettersCategoryListActivity extends Activity
        implements JSONConstants,URLConstants,ResultConstants,StatusConstants {
    Context mContext=this;
    RecyclerView mNewsLetterListView;
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back;
    ImageView home;
    Bundle extras;
    String tab_type;
    ArrayList<NewsLetterModel> newsLetterModelArrayList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newslettercategory_layout);
        initUI();
        if(AppUtils.isNetworkConnected(mContext)){
            getNewslettercategory(URL_GET_NEWSLETTER_CATEGORY);
        }else{
            AppUtils.showDialogAlertDismiss((Activity)mContext,"Network Error",getString(R.string.no_internet),R.drawable.nonetworkicon,R.drawable.roundred);

        }
    }

    private void getNewslettercategory(String urlGetNewsletterCategory) {

        VolleyWrapper volleyWrapper=new VolleyWrapper(urlGetNewsletterCategory);
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

                            if (data.length() > 0) {
                                for (int i = 0; i < data.length(); i++) {
                                    JSONObject dataObject = data.getJSONObject(i);
                                    newsLetterModelArrayList.add(addNewsLetterDetails(dataObject));
                                }

                                NewsLatterRecyclerViewAdapter newsLetterAdapter = new NewsLatterRecyclerViewAdapter(mContext, newsLetterModelArrayList);
                                mNewsLetterListView.setAdapter(newsLetterAdapter);
                            } else {
                                Toast.makeText(NewsLettersCategoryListActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else if (response_code.equalsIgnoreCase("500")) {
                        AppUtils.showDialogAlertDismiss((Activity)mContext,"Alert",getString(R.string.common_error),R.drawable.exclamationicon,R.drawable.round);

                    } else if (response_code.equalsIgnoreCase("400")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        getNewslettercategory(URL_GET_NEWSLETTER_CATEGORY);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        getNewslettercategory(URL_GET_NEWSLETTER_CATEGORY);

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        getNewslettercategory(URL_GET_NEWSLETTER_CATEGORY);

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

    private void initUI() {
        extras=getIntent().getExtras();
        if(extras!=null) {
            tab_type =extras.getString("tab_type");
        }

        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        mNewsLetterListView = (RecyclerView) findViewById(R.id.mnewsLetterListView);
        mNewsLetterListView.setHasFixedSize(true);
        mNewsLetterListView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.list_divider_teal)));
        headermanager = new HeaderManager(NewsLettersCategoryListActivity.this, tab_type);
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
        mNewsLetterListView.setLayoutManager(llm);

        mNewsLetterListView.addOnItemTouchListener(new RecyclerItemListener(getApplicationContext(), mNewsLetterListView,
                new RecyclerItemListener.RecyclerTouchListener() {
                    public void onClickItem(View v, int position) {
                        Intent intent=new Intent(NewsLettersCategoryListActivity.this,NewsLetterSubListActivity.class);
                        intent.putExtra("category_id",newsLetterModelArrayList.get(position).getNewsLetterCatId());
                        intent.putExtra("tab_type",newsLetterModelArrayList.get(position).getNewsLetterCatName());
                        startActivity(intent);
                    }

                    public void onLongClickItem(View v, int position) {
                        System.out.println("On Long Click Item interface");
                    }
                }));
    }

    private NewsLetterModel addNewsLetterDetails(JSONObject obj) {
        NewsLetterModel model = new NewsLetterModel();
        try {
            model.setNewsLetterCatId(obj.optString("id"));
            model.setNewsLetterCatName(obj.optString("name"));
        } catch (Exception ex) {
            System.out.println("Exception is" + ex);
        }

        return model;
    }
}
