package com.mobatia.naisapp.activities.universityguidance.guidanceessential;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.activities.notifications.VideoWebViewActivity;
import com.mobatia.naisapp.activities.pdf.PdfReaderActivity;
import com.mobatia.naisapp.activities.universityguidance.guidanceessential.adapter.GuidanceEssentialAdapter;
import com.mobatia.naisapp.activities.universityguidance.guidanceessential.adapter.GuidanceEssentialDetailAdapter;
import com.mobatia.naisapp.activities.universityguidance.guidanceessential.model.GuidanceEssentialDetailModel;
import com.mobatia.naisapp.activities.universityguidance.guidanceessential.model.GuidanceEssentialModel;
import com.mobatia.naisapp.constants.ClickInter;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.constants.ResultConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.HeaderManager;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.recyclerviewmanager.DividerItemDecoration;
import com.mobatia.naisapp.recyclerviewmanager.RecyclerItemListener;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class GuidanceEssentialDetailActivity  extends Activity implements JSONConstants, URLConstants, ResultConstants, StatusConstants, NaisClassNameConstants,ClickInter {
    private Context mContext;
    Bundle extras;
    String tab_type;
    String universityID;
    RelativeLayout relativeHeader;
    RecyclerView informationRecycler;
    ArrayList<GuidanceEssentialDetailModel> mListViewArray;

    HeaderManager headermanager;
    ImageView back;
    ImageView home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guidance_essential_detail);
        mContext = this;
        initUI();
        if (AppUtils.checkInternet(mContext)) {
            getInformationList();
        } else {
            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);

        }

    }

    private void initUI() {
        extras = getIntent().getExtras();
        if (extras != null) {
            tab_type = extras.getString("tab_type");
            universityID = extras.getString("universityID");
        }
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        informationRecycler = (RecyclerView) findViewById(R.id.informationRecycler);
        informationRecycler.setHasFixedSize(true);
        headermanager = new HeaderManager(GuidanceEssentialDetailActivity.this, tab_type);
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
        informationRecycler.setLayoutManager(llm);
//        informationRecycler.addOnItemTouchListener(new RecyclerItemListener(getApplicationContext(), informationRecycler,
//                new RecyclerItemListener.RecyclerTouchListener() {
//                    public void onClickItem(View v, int position) {
//
////                       if (mListViewArray.get(position).getFile_type().equalsIgnoreCase("PDF"))
////                       {
////                           Intent intent = new Intent(mContext, PdfReaderActivity.class);
////                           intent.putExtra("pdf_url",mListViewArray.get(position).getFile_url());
////                           startActivity(intent);
////                       }
////                       else if (mListViewArray.get(position).getFile_type().equalsIgnoreCase("Video"))
////                       {
////                          Intent mIntent = new Intent(mContext, VideoViewActivity.class);
////                           mIntent.putExtra("URL", mListViewArray.get(position).getFile_url());
////                           startActivity(mIntent);
////                       }
//                    }
//
//                    public void onLongClickItem(View v, int position) {
//                    }
//                }));
    }

    public void getInformationList() {

        try {
            mListViewArray = new ArrayList<>();
            final VolleyWrapper manager = new VolleyWrapper(URL_GET_GUIDANCE_ESSENTIALS_DETAILS);
            String[] name = new String[]{JTAG_ACCESSTOKEN,"users_id","university_id"};
            String[] value = new String[]{PreferenceManager.getAccessToken(mContext),PreferenceManager.getUserId(mContext),universityID};

            manager.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {

                @Override
                public void responseSuccess(String successResponse) {
                    String responsCode = "";
                    if (successResponse != null)
                    {
                        try {
                            JSONObject rootObject = new JSONObject(successResponse);
                            if (rootObject.optString(JTAG_RESPONSE) != null) {
                                responsCode = rootObject.optString(JTAG_RESPONSECODE);
                                if (responsCode.equals(RESPONSE_SUCCESS)) {
                                    JSONObject respObject = rootObject.getJSONObject(JTAG_RESPONSE);
                                    String statusCode = respObject.optString(JTAG_STATUSCODE);
                                    if (statusCode.equals(STATUS_SUCCESS)) {
                                        JSONArray dataArray = respObject.getJSONArray(JTAG_RESPONSE_DATA_ARRAY);
                                        if (dataArray.length() > 0) {
                                            for (int i = 0; i <dataArray.length(); i++) {
                                                JSONObject dataObject = dataArray.getJSONObject(i);
                                                mListViewArray.add(getSearchValues(dataObject));

                                            }

//                                           GuidanceEssentialDetailAdapter unReadMessageAdapter = new GuidanceEssentialDetailAdapter(mContext, mListViewArray, new ClickInter() {
//                                                @Override
//                                                public void onPositionClicked(int position) {
//                                                    Log.e("Clicked position",String.valueOf(position));
//
//
//                                                }
//
//
//                                                @Override
//                                                public void onLongClicked(int position) {
//
//                                                }
//
//                                            });
//
//                                            informationRecycler.setAdapter(unReadMessageAdapter);
//                                            unReadMessageAdapter.setClickListener(this);
                                            GuidanceEssentialDetailAdapter   mAdapter = new GuidanceEssentialDetailAdapter(mContext, mListViewArray);
                                            informationRecycler.setAdapter(mAdapter);
                                            mAdapter.setClickListener(GuidanceEssentialDetailActivity.this);

                                        } else {
                                            AppUtils.showDialogAlertDismiss((Activity)mContext,"Alert",getString(R.string.nodatafound),R.drawable.exclamationicon,R.drawable.round);

                                        }
                                    } else {
                                        AppUtils.showDialogAlertDismiss((Activity)mContext,"Alert",getString(R.string.common_error),R.drawable.exclamationicon,R.drawable.round);

                                    }
                                }
                                else if (responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) ||
                                        responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                                        responsCode.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
                                    AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                                        @Override
                                        public void getAccessToken() {
                                        }
                                    });
                                    getInformationList();

                                }else if (responsCode.equals(RESPONSE_ERROR)) {
                                    AppUtils.showDialogAlertDismiss((Activity)mContext,"Alert",getString(R.string.common_error),R.drawable.exclamationicon,R.drawable.round);

                                }
                            } else {
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
    private GuidanceEssentialDetailModel getSearchValues(JSONObject Object)
            throws JSONException {
        GuidanceEssentialDetailModel model = new GuidanceEssentialDetailModel();
        model.setDescription(Object.getString("description"));
        model.setFile_type(Object.getString("file_type"));
        model.setFile_url(Object.getString("file_url"));
        if (Object.getString("file_type").equalsIgnoreCase("Video"))
        {
            if (Object.getString("file_url").contains("https://youtu.be/"))
            {
                String main=Object.getString("file_url");
                System.out.println(main.substring(main.lastIndexOf("/") + 1));
                String imageURL="https://img.youtube.com/vi/"+main.substring(main.lastIndexOf("/") + 1)+"/hqdefault.jpg";
                model.setImage_url(imageURL);
               // Picasso.with(mContext).load(AppUtils.replace(imageURL)).placeholder(R.drawable.boy).fit().into(holder.imageTypeImg);
                Log.e("IMAGE URL",imageURL);
            }
            else {
                Bitmap bitmap;
                try {
                    bitmap  =retriveVideoFrameFromVideo(Object.getString("file_url"));
                    if (bitmap !=null){
                        DisplayMetrics displayMetrics = new DisplayMetrics();
                        ((Activity) mContext).getWindowManager()
                                .getDefaultDisplay()
                                .getMetrics(displayMetrics);
                        int height = displayMetrics.heightPixels;
                        int width = displayMetrics.widthPixels;
                        bitmap = Bitmap.createScaledBitmap(bitmap,width,500,false);
                        model.setBitmap(bitmap);
                        model.setImage_url("");
//                        holder.imageTypeImg.setImageBitmap(bitmap);
                    }
                }catch (Throwable throwable){
                    throwable.printStackTrace();
                }
            }

        }
        else
        {
            model.setImage_url("");
        }
        return model;
    }

    public static Bitmap retriveVideoFrameFromVideo(String videoPath) throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.getMessage());

        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    @Override
    public void onPositionClicked(int position) {

        Log.e("CLCIKED POS",String.valueOf(position));
    }

    @Override
    public void onLongClicked(int position) {

    }
}

