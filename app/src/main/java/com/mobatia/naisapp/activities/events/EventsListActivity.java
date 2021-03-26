package com.mobatia.naisapp.activities.events;

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
import com.mobatia.naisapp.activities.events.adapter.EventslistAdapter;
import com.mobatia.naisapp.activities.events.model.EventsModel;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.activities.videos.VideosPlayerViewActivity;
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
public class EventsListActivity extends Activity implements JSONConstants,ResultConstants,StatusConstants,URLConstants
{
    Context mContext=this;
    RecyclerView mNewsLetterListView;
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back;
    ImageView home;
    Bundle extras;
    String tab_type,category_id;
    ArrayList<EventsModel> newsLetterModelArrayList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newslettercategory_layout);
        initUI();
        if(AppUtils.isNetworkConnected(mContext)){
            getEventsListApi(URL_GET_COMMUNICATIONS_EVENTS);
        }else{
            AppUtils.showDialogAlertDismiss((Activity)mContext,"Network Error",getString(R.string.no_internet),R.drawable.nonetworkicon,R.drawable.roundred);

        }
    }

    private void getEventsListApi(String urlGetNewsletters) {
        VolleyWrapper volleyWrapper=new VolleyWrapper(urlGetNewsletters);
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

                                    newsLetterModelArrayList.add(addEventsDetails(dataObject));
                                }

                                EventslistAdapter newsLetterAdapter = new EventslistAdapter(mContext, newsLetterModelArrayList);
                                mNewsLetterListView.setAdapter(newsLetterAdapter);
                            } else {
                                Toast.makeText(EventsListActivity.this, "No data found", Toast.LENGTH_SHORT).show();
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
                        getEventsListApi(URL_GET_COMMUNICATIONS_EVENTS);

                    } else if (response_code.equalsIgnoreCase("401")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        getEventsListApi(URL_GET_COMMUNICATIONS_EVENTS);

                    } else if (response_code.equalsIgnoreCase("402")) {
                        AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
                            @Override
                            public void tokenrenewed() {
                            }
                        });
                        getEventsListApi(URL_GET_COMMUNICATIONS_EVENTS);

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

                AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

            }
        });

    }

    private void initUI() {
        extras=getIntent().getExtras();
        if(extras!=null) {
            tab_type=extras.getString("tab_type");
        }
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        mNewsLetterListView = (RecyclerView) findViewById(R.id.mnewsLetterListView);
        mNewsLetterListView.setHasFixedSize(true);
        mNewsLetterListView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.list_divider)));
        headermanager = new HeaderManager(EventsListActivity.this, tab_type);
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
                        if (newsLetterModelArrayList.get(position).getType().equalsIgnoreCase("Video"))
                        {
                            Intent intent = new Intent(mContext, VideosPlayerViewActivity.class);
                            intent.putExtra("video_url", newsLetterModelArrayList.get(position).getUrl());
                            startActivity(intent);
                        }
                        else  if (newsLetterModelArrayList.get(position).getType().equalsIgnoreCase("Image"))
                        {
String evenFromToDate="";
                            if (newsLetterModelArrayList.get(position).getStart_date().equalsIgnoreCase(newsLetterModelArrayList.get(position).getEnd_date()))
                            {
                                evenFromToDate =AppUtils.dateParsingTodd_MMM_yyyy(newsLetterModelArrayList.get(position).getStart_date());
                            }
                            else {
                                evenFromToDate=AppUtils.dateParsingTodd_MMM_yyyy(newsLetterModelArrayList.get(position).getStart_date()) + " to " + AppUtils.dateParsingTodd_MMM_yyyy(newsLetterModelArrayList.get(position).getEnd_date());
                            }
                            String webViewComingUpDetail= "<!DOCTYPE html>\n" +
                                    "<html>\n" +
                                    "<head>\n" +
                                    "<style>\n" +
                                    "\n" +
                                    "@font-face {\n" +
                                    "font-family: SourceSansPro-Semibold;"+
                                    "src: url(SourceSansPro-Semibold.ttf);"+

                                    "font-family: SourceSansPro-Regular;"+
                                    "src: url(SourceSansPro-Regular.ttf);"+
                                    "}"+
                                    ".title {"+
                                    "font-family: SourceSansPro-Regular;"+
                                    "font-size:16px;"+
                                    "text-align:left;"+
                                    "color:	#46C1D0;"+
                                    "text-align: ####TEXT_ALIGN####;"+
                                    "}"+

                                    ".description {"+
                                    "font-family: SourceSansPro-Light;"+
                                    "text-align:justify;"+
                                    "font-size:14px;"+
                                    "color: #000000;"+
                                    "text-align: ####TEXT_ALIGN####;"+
                                    "}"+
                                    "</style>\n"+"</head>"+
                                    "<body>"+
                                    "<p class='title'>"+newsLetterModelArrayList.get(position).getTitle()+"</p> <p>"+evenFromToDate+"</p>";
                            if (!newsLetterModelArrayList.get(position).getUrl().equalsIgnoreCase("")) {
                                webViewComingUpDetail = webViewComingUpDetail + "<center><img src='" + newsLetterModelArrayList.get(position).getUrl() + "'width='100%', height='auto'>";
                            }
                            webViewComingUpDetail=webViewComingUpDetail+"<p class='description'>"+newsLetterModelArrayList.get(position).getMessage()+"</p>"+
                                    "</body>\n</html>";
                            Intent mIntent = new Intent(mContext, EventDetailWebViewActivity.class);
                            mIntent.putExtra("webViewComingDetail", webViewComingUpDetail);
                            mIntent.putExtra("message",newsLetterModelArrayList.get(position).getTitle());
                            mIntent.putExtra("link",newsLetterModelArrayList.get(position).getLink());
                            mIntent.putExtra("pdf",newsLetterModelArrayList.get(position).getPdf());
                            mIntent.putExtra("tab_type", tab_type);

                            startActivity(mIntent);
                        }
                        else  if (newsLetterModelArrayList.get(position).getType().equalsIgnoreCase("Text"))
                        {
                            String evenFromToDate="";
                            if (newsLetterModelArrayList.get(position).getStart_date().equalsIgnoreCase(newsLetterModelArrayList.get(position).getEnd_date()))
                            {
                                evenFromToDate =AppUtils.dateParsingTodd_MMM_yyyy(newsLetterModelArrayList.get(position).getStart_date());
                            }
                            else {
                                evenFromToDate=AppUtils.dateParsingTodd_MMM_yyyy(newsLetterModelArrayList.get(position).getStart_date()) + " to " + AppUtils.dateParsingTodd_MMM_yyyy(newsLetterModelArrayList.get(position).getEnd_date());
                            }
                            String webViewComingUpDetail= "<!DOCTYPE html>\n" +
                                    "<html>\n" +
                                    "<head>\n" +
                                    "<style>\n" +
                                    "\n" +
                                    "@font-face {\n" +
                                    "font-family: SourceSansPro-Semibold;"+
                                    "src: url(SourceSansPro-Semibold.ttf);"+

                                    "font-family: SourceSansPro-Regular;"+
                                    "src: url(SourceSansPro-Regular.ttf);"+
                                    "}"+
                                    ".title {"+
                                    "font-family: SourceSansPro-Regular;"+
                                    "font-size:16px;"+
                                    "text-align:left;"+
                                    "color:	#46C1D0;"+
                                    "text-align: ####TEXT_ALIGN####;"+
                                    "}"+

                                    ".description {"+
                                    "font-family: SourceSansPro-Light;"+
                                    "text-align:justify;"+
                                    "font-size:14px;"+
                                    "color: #000000;"+
                                    "text-align: ####TEXT_ALIGN####;"+
                                    "}"+
                                    "</style>\n"+"</head>"+
                                    "<body>"+
                                    "<p class='title'>"+newsLetterModelArrayList.get(position).getTitle()+"</p> <p>"+evenFromToDate+"</p>";
                            if (!newsLetterModelArrayList.get(position).getUrl().equalsIgnoreCase("")) {
                                webViewComingUpDetail = webViewComingUpDetail + "<center><img src='" + newsLetterModelArrayList.get(position).getUrl() + "'width='100%', height='auto'>";
                            }
                            webViewComingUpDetail=webViewComingUpDetail+"<p class='description'>"+newsLetterModelArrayList.get(position).getMessage()+"</p>"+
                                    "</body>\n</html>";
                            Intent mIntent = new Intent(mContext, EventDetailWebViewActivity.class);
                            mIntent.putExtra("webViewComingDetail", webViewComingUpDetail);
                            mIntent.putExtra("message",newsLetterModelArrayList.get(position).getTitle());
                            mIntent.putExtra("link",newsLetterModelArrayList.get(position).getLink());
                            mIntent.putExtra("pdf",newsLetterModelArrayList.get(position).getPdf());
                            mIntent.putExtra("tab_type", tab_type);

                            startActivity(mIntent);
                        }


                    }

                    public void onLongClickItem(View v, int position) {
                        System.out.println("On Long Click Item interface");
                    }
                }));
    }

    private EventsModel addEventsDetails(JSONObject obj) {
        EventsModel model = new EventsModel();
        try {
            model.setId(obj.optString(JTAG_ID));
            model.setTitle(obj.optString(JTAG_TITLE));
            model.setStart_date(obj.optString(JTAG_EVENT_STARTDATE));
            model.setEnd_date(obj.optString(JTAG_EVENT_ENDDATE));
            model.setMessage(obj.optString(JTAG_EVENT_MESSAGE));
            model.setUrl(obj.optString(JTAG_URL));
            model.setType(obj.optString(JTAG_TYPE));
            model.setLink(obj.optString(JTAG_EVENT_LINK));
            model.setPdf(obj.optString("pdf"));

        } catch (Exception ex) {
            System.out.println("Exception is" + ex);
        }

        return model;
    }
}
