package com.mobatia.naisapp.activities.participants;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.activities.participants.adapter.ParticipantRecyclerviewAdapter;
import com.mobatia.naisapp.activities.photos.adapter.PhotosRecyclerviewAdapter;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.constants.NameValueConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.sports.model.SportsModel;
import com.mobatia.naisapp.manager.HeaderManager;
import com.mobatia.naisapp.recyclerviewmanager.ItemOffsetDecoration;

import java.util.ArrayList;

/**
 * Created by Rijo on 25/1/17.
 */
public class ParticipantRecyclerViewActivity extends Activity implements URLConstants, StatusConstants, JSONConstants, NameValueConstants,NaisClassNameConstants {
    Context mContext;
    private RecyclerView recycler_view_photos;
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back;
    ImageView home;
    Intent intent;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayList<SportsModel> mSportsModelArrayList;
    PhotosRecyclerviewAdapter mPhotosRecyclerviewAdapter;
//    RecyclerView.LayoutManager recyclerViewLayoutManager;
  Bundle extras;
    String tab_type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participation_recyclerview);
        mContext = this;
        initUI();
//        if (AppUtils.isNetworkConnected(mContext)) {
//            photosListApiCall();
//        } else {
//            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
//        }
    }

    private void initUI() {
        extras=getIntent().getExtras();
        if(extras!=null){
            tab_type=extras.getString("tab_type");
            mSportsModelArrayList= (ArrayList<SportsModel>) extras.getSerializable("participant_sports_array");
        }
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        headermanager = new HeaderManager(ParticipantRecyclerViewActivity.this,tab_type);
        headermanager.getHeader(relativeHeader, 1);
        back = headermanager.getLeftButton();
        headermanager.setButtonLeftSelector(R.drawable.back,
                R.drawable.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        recycler_view_photos = (RecyclerView) findViewById(R.id.recycler_view_photos);
//        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
//        mSwipeRefreshLayout.setRefreshing(false);
        recycler_view_photos.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        int spacing = 5; // 50px
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext,spacing);
        recycler_view_photos.addItemDecoration(itemDecoration);
        recycler_view_photos.setLayoutManager(llm);
//        recycler_view_photos.setLayoutManager(recyclerViewLayoutManager);
        recycler_view_photos.setAdapter(new ParticipantRecyclerviewAdapter(mContext,mSportsModelArrayList));
//        recycler_view_photos.addOnItemTouchListener(new RecyclerItemListener(mContext, recycler_view_photos,
//                new RecyclerItemListener.RecyclerTouchListener() {
//                    public void onClickItem(View v, int position) {
//                        System.out.println("sizeee::"+ mSportsModelArrayList.get(position).getSportsModelParticipantsArrayList().size());
////                        Intent intent = new Intent(mContext, PhotosViewPagerActivity.class);
////                        intent.putExtra("photo_array", mSportsModelArrayList.get(position).getSportsModelParticipantsArrayList());
////                        intent.putExtra("pos", position);
////                        startActivity(intent);
//                    }
//
//                    public void onLongClickItem(View v, int position) {
//                    }
//                }));



    }

    void refreshItems() {
        // Load items
        // ...
        // Load complete
        onItemsLoadComplete();
    }


    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...

        // Stop refresh animation
        mSwipeRefreshLayout.setRefreshing(false);
    }



    String getmonth(String month) {
        String mMonth = "";
        switch (month) {
            case "0":
                mMonth = "January";
                break;

            case "1":
                mMonth = "February";
                break;
            case "2":
                mMonth = "March";
                break;
            case "3":
                mMonth = "April";
                break;
            case "4":
                mMonth = "May";
                break;
            case "5":
                mMonth = "June";
                break;
            case "6":
                mMonth = "July";
                break;
            case "7":
                mMonth = "August";
                break;
            case "8":
                mMonth = "September";
                break;
            case "9":
                mMonth = "October";
                break;
            case "10":
                mMonth = "November";
                break;
            case "11":
                mMonth = "December";
                break;


        }
        return mMonth;
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
//                                            Toast.makeText(ParticipantRecyclerViewActivity.this, "Status Failure:" + rootObject.getString("Status"), Toast.LENGTH_SHORT).show();
//
//                                        }
//
//                                    } else {
//                                        Toast.makeText(ParticipantRecyclerViewActivity.this, "NETWORK ERROR", Toast.LENGTH_SHORT).show();
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
}
