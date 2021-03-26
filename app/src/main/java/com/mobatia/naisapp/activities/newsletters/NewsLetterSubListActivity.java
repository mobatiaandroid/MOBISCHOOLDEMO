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
import com.mobatia.naisapp.activities.newsletters.adapter.NewsLetterRecyclerSubListAdapter;
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
public class NewsLetterSubListActivity extends Activity
        implements JSONConstants,URLConstants,ResultConstants,StatusConstants {
    Context mContext=this;
    RecyclerView mNewsLetterListView;
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back;
    ImageView home;
    Bundle extras;
    String tab_type,category_id;
    ArrayList<NewsLetterModel> newsLetterModelArrayList=new ArrayList<>();
    NewsLetterModel letterModel=new NewsLetterModel();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newslettercategory_layout);
        initUI();
        if(AppUtils.isNetworkConnected(mContext)){
            getNewslettercategory(URL_GET_NEWSLETTERS);
        }else{
            AppUtils.showDialogAlertDismiss((Activity)mContext,"Network Error",getString(R.string.no_internet),R.drawable.nonetworkicon,R.drawable.roundred);

        }
    }

    private void getNewslettercategory(String urlGetNewsletterCategory) {

        VolleyWrapper volleyWrapper=new VolleyWrapper(urlGetNewsletterCategory);
        String[] name={"access_token","category_id"};
        String[] value={PreferenceManager.getAccessToken(mContext),category_id};
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
                            JSONArray responses = secobj.getJSONArray("responses");

                            if (responses.length() > 0) {
                                for (int i = 0; i < responses.length(); i++) {
                                    JSONObject dataObject = responses.getJSONObject(i);

                                    newsLetterModelArrayList.add(addNewsLetterDetails(dataObject));
                                }

                                NewsLetterRecyclerSubListAdapter newsLetterAdapter = new NewsLetterRecyclerSubListAdapter(mContext, newsLetterModelArrayList);
                                mNewsLetterListView.setAdapter(newsLetterAdapter);
                            } else {
                                Toast.makeText(NewsLetterSubListActivity.this, "No data found", Toast.LENGTH_SHORT).show();
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
                        getNewslettercategory(URL_GET_NEWSLETTERS);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        getNewslettercategory(URL_GET_NEWSLETTERS);

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        getNewslettercategory(URL_GET_NEWSLETTERS);

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
            category_id=extras.getString("category_id");
        }

        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        mNewsLetterListView = (RecyclerView) findViewById(R.id.mnewsLetterListView);
        mNewsLetterListView.setHasFixedSize(true);
        mNewsLetterListView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.list_divider_teal)));
        headermanager = new HeaderManager(NewsLetterSubListActivity.this, tab_type);
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
                    Intent intent=new Intent(NewsLetterSubListActivity.this,NewsLetterDetailActivity.class);
                        intent.putExtra("submenuArray",newsLetterModelArrayList.get(position).getNewsLetterModelArrayList());
                        intent.putExtra("tab_type",newsLetterModelArrayList.get(position).getTitle());//newsLetterModelArrayList.get(position).getTitle());//rijo removed
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
            model.setTitle(obj.optString("title"));
            JSONArray submenuArray=obj.getJSONArray("submenu");
            ArrayList<NewsLetterModel> subMenNewsLetterModels=new ArrayList<>();
            for(int i=0;i<submenuArray.length();i++) {
                JSONObject subObj=submenuArray.getJSONObject(i);
                NewsLetterModel newsModel=new NewsLetterModel();
                newsModel.setNewLetterSubId(subObj.optString("id"));
                newsModel.setFilename(subObj.optString("filename"));
                newsModel.setSubmenu(subObj.optString("submenu"));
                subMenNewsLetterModels.add(newsModel);
            }
        model.setNewsLetterModelArrayList(subMenNewsLetterModels);
        } catch (Exception ex) {
            System.out.println("Exception is" + ex);
        }

        return model;
    }
}
