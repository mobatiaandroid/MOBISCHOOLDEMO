package com.mobatia.naisapp.activities.photos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;

import android.view.View;
import android.widget.ImageView;


import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.photos.adapter.ImagePagerAdapter;
import com.mobatia.naisapp.activities.photos.adapter.PhotosRecyclerviewAdapter;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.constants.NameValueConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.gallery.model.PhotosListModel;

import java.util.ArrayList;

/**
 * Created by Rijo on 25/1/17.
 */
public class PhotosViewPagerActivity extends Activity implements URLConstants, StatusConstants, JSONConstants, NameValueConstants,NaisClassNameConstants {
    Context mContext;
//    RelativeLayout relativeHeader;
//    HeaderManager headermanager;
    ImageView back;
//    ImageView home;
    Intent intent;
    ArrayList<PhotosListModel> mPhotosModelArrayList;
    PhotosRecyclerviewAdapter mPhotosRecyclerviewAdapter;
  Bundle extras;
    ViewPager bannerImageViewPager;
    int pos=0;
    //RelativeLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_viewpager);
        mContext = this;
        initUI();

    }

    private void initUI() {
//        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        back = (ImageView) findViewById(R.id.back);
        bannerImageViewPager = (ViewPager) findViewById(R.id.bannerImageViewPager);
        extras=getIntent().getExtras();
        if(extras!=null){
            mPhotosModelArrayList= (ArrayList<PhotosListModel>) extras.getSerializable("photo_array");
            pos= extras.getInt("pos");
        }



     /*   headermanager = new HeaderManager(PhotosViewPagerActivity.this, (pos+1)+" Of "+mPhotosModelArrayList.size());
        headermanager.getHeader(relativeHeader, 1);
        back = headermanager.getLeftButton();
        headermanager.setButtonLeftSelector(R.drawable.back,
                R.drawable.back);*/
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
  /*      home = headermanager.getLogoButton();
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(mContext, HomeListAppCompatActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
            }
        });*/
        bannerImageViewPager.setAdapter(new ImagePagerAdapter(mContext, mPhotosModelArrayList,"portrait"));
        bannerImageViewPager.setCurrentItem(pos);
        bannerImageViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
//                headermanager.setTitle((position + 1) + " Of " + mPhotosModelArrayList.size());
                pos=position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }






//    private void photosListApiCall() {
//        try {
//            mPhotosModelArrayList = new ArrayList<PhotosListModel>();
//            String[] name = {NAME_ACCESS_TOKEN};
//            String[] values = {PreferenceManager.getAccessToken(mContext)};
//            final VolleyWrapper manager = new VolleyWrapper(URL_GET_PHOTOS_LIST);
//            manager.getResponsePOST(mContext, 11, name, values,
//                    new VolleyWrapper.ResponseListener() {
//
//                        @Override
//                        public void responseSuccess(String successResponse) {
//                            if (successResponse != null) {
//                                try {
//                                    System.out.println("successResponse::" + successResponse);
//                                    JSONObject rootObject = new JSONObject(successResponse);
//                                    String responseCode = rootObject.getString(JTAG_RESPONSECODE);
//                                    if (responseCode.equalsIgnoreCase(RESPONSE_SUCCESS)) {
//                                        JSONObject responseObject = rootObject.optJSONObject(JTAG_RESPONSE);
//                                        String statusCode = responseObject.getString(JTAG_STATUSCODE);
//                                        if (statusCode.equalsIgnoreCase(STATUS_SUCCESS)) {
//
//                                            JSONArray data = responseObject.optJSONArray(JTAG_RESPONSE_IMAGES_ARRAY);
//                                            for (int i = 0; i < data.length(); i++) {
//                                                JSONObject imageDetail = data.optJSONObject(i);
//                                                PhotosListModel mPhotosModel = new PhotosListModel();
//                                                mPhotosModel.setPhotoId(imageDetail.optString(JTAG_ID));
//                                                mPhotosModel.setPhotoUrl(imageDetail.optString(JTAG_IMAGE));
//                                                mPhotosModelArrayList.add(mPhotosModel);
//                                            }
//                                            mPhotosRecyclerviewAdapter = new PhotosRecyclerviewAdapter(mContext, mPhotosModelArrayList,photo_id);
//                                            recycler_view_photos.setAdapter(mPhotosRecyclerviewAdapter);
//                                        } else if (statusCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
//                                                statusCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING)) {
//                                            AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
//                                                @Override
//                                                public void getAccessToken() {
//                                                    photosListApiCall();
//                                                }
//                                            });
//                                        } else {
//                                            Toast.makeText(PhotosViewPagerActivity.this, "Status Failure:" + rootObject.getString("Status"), Toast.LENGTH_SHORT).show();
//
//                                        }
//
//                                    } else {
//                                        Toast.makeText(PhotosViewPagerActivity.this, "NETWORK ERROR", Toast.LENGTH_SHORT).show();
//                                    }
//
//                                } catch (Exception ex) {
//                                    ex.printStackTrace();
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void responseFailure(String failureResponse) {
//                            //CustomStatusDialog(RESPONSE_FAILURE);
//                        }
//
//                    });
//        } catch (Exception e) {
//            // CustomStatusDialog(RESPONSE_FAILURE);
//            e.printStackTrace();
//        }
//    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            relativeHeader.setVisibility(View.GONE);
            back.setVisibility(View.GONE);
            bannerImageViewPager.setAdapter(new ImagePagerAdapter(mContext, mPhotosModelArrayList, "landscape"));
            bannerImageViewPager.setCurrentItem(pos);


            //Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            //Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
//            relativeHeader.setVisibility(View.VISIBLE);
            back.setVisibility(View.GONE);
            bannerImageViewPager.setAdapter(new ImagePagerAdapter(mContext, mPhotosModelArrayList, "portrait"));
            bannerImageViewPager.setCurrentItem(pos);


        }
    }



}
