package com.mobatia.naisapp.activities.photos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.activities.photos.adapter.PhotosRecyclerviewAdapter;
import com.mobatia.naisapp.activities.photos.adapter.PhotosViewRecyclerviewAdapter;
import com.mobatia.naisapp.appcontroller.AppController;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.constants.NameValueConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.gallery.model.PhotosListModel;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.HeaderManager;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.recyclerviewmanager.ItemOffsetDecoration;
import com.mobatia.naisapp.recyclerviewmanager.OnBottomReachedListener;
import com.mobatia.naisapp.recyclerviewmanager.RecyclerItemListener;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Rijo on 25/1/17.
 */
public class PhotosViewRecyclerViewActivity extends Activity implements URLConstants, StatusConstants, JSONConstants, NameValueConstants,NaisClassNameConstants {
    Context mContext;
    private RecyclerView recycler_view_photos;
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back;
    ImageView home;
    Intent intent;
    String albumID="";
    SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayList<PhotosListModel> mPhotosModelArrayList;
    PhotosViewRecyclerviewAdapter mPhotosRecyclerviewAdapter;
    RecyclerView.LayoutManager recyclerViewLayoutManager;
    Bundle extras;
    int pos=0;
    String scrollTo="";
    String apiID="";
    boolean isFromBottom=false;
    int notificationSize=0;
//    String photo_id="-1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photos_recyclerview_activity);
        mContext = this;
        initUI();

        if (AppUtils.isNetworkConnected(mContext)) {
            scrollTo="";
            apiID="";
            isFromBottom=false;
            photosListApiCall(apiID,scrollTo);
        } else {
            AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
        }
    }

    private void initUI() {
        extras=getIntent().getExtras();
        if(extras!=null){
            mPhotosModelArrayList=new ArrayList<>();
            albumID=extras.getString("albumID");
//            mPhotosModelArrayList= (ArrayList<PhotosListModel>) extras.getSerializable("photo_array");
         //   mPhotosModelArrayList= AppController.mPhotosModelArrayListGallery;
            pos= extras.getInt("pos");
        }
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        headermanager = new HeaderManager(PhotosViewRecyclerViewActivity.this, "Photos");
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
        /*extras=getIntent().getExtras();
        if(extras!=null){
            photo_id=extras.getString("photo_id");
        }*/
        recycler_view_photos = (RecyclerView) findViewById(R.id.recycler_view_photos);
//        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
//        mSwipeRefreshLayout.setRefreshing(false);
        recycler_view_photos.setHasFixedSize(true);
        recyclerViewLayoutManager = new GridLayoutManager(mContext, 3);
        int spacing = 10; // 50px
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext,spacing);
        recycler_view_photos.addItemDecoration(itemDecoration);
//        recycler_view_photos.setLayoutManager(recyclerViewLayoutManager);
//        recycler_view_photos.addItemDecoration(
//                new DividerItemDecoration(mContext.getResources().getDrawable(R.drawable.list_divider)));
        recycler_view_photos.setLayoutManager(recyclerViewLayoutManager);
        recycler_view_photos.addOnItemTouchListener(new RecyclerItemListener(mContext, recycler_view_photos,
                new RecyclerItemListener.RecyclerTouchListener() {
                    public void onClickItem(View v, int position) {
                        Intent intent = new Intent(mContext, PhotosViewPagerActivity.class);
                        intent.putExtra("photo_array", mPhotosModelArrayList);
                        intent.putExtra("pos", position);
                        startActivity(intent);
                    }

                    public void onLongClickItem(View v, int position) {
                    }
                }));




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



    private void photosListApiCall(String id,String scrollto) {
        try {
            notificationSize=0;
            String[] name = {NAME_ACCESS_TOKEN,"page_from","scroll_to","album_id"};
            String[] values = {PreferenceManager.getAccessToken(mContext),id,scrollto,albumID};
            final VolleyWrapper manager = new VolleyWrapper(URL_GET_PHOTOS_LIST_DETAIL);
            manager.getResponsePOST(mContext, 11, name, values,
                    new VolleyWrapper.ResponseListener() {

                        @Override
                        public void responseSuccess(String successResponse) {
                            if (successResponse != null) {
                                try {
                                    System.out.println("successResponse::" + successResponse);
                                    JSONObject rootObject = new JSONObject(successResponse);
                                    String responseCode = rootObject.getString(JTAG_RESPONSECODE);
                                    if (responseCode.equalsIgnoreCase(RESPONSE_SUCCESS)) {
                                        JSONObject responseObject = rootObject.optJSONObject(JTAG_RESPONSE);
                                        String statusCode = responseObject.getString(JTAG_STATUSCODE);
                                        if (statusCode.equalsIgnoreCase(STATUS_SUCCESS)) {

                                            JSONArray data = responseObject.optJSONArray("images");
                                            notificationSize=data.length();
                                            for (int i = 0; i < data.length(); i++) {
                                                JSONObject imageDetail = data.optJSONObject(i);
                                                PhotosListModel mPhotosModel = new PhotosListModel();
                                                mPhotosModel.setPhotoId(imageDetail.optString(JTAG_ID));
                                                mPhotosModel.setPhotoUrl(imageDetail.optString(JTAG_IMAGE));
                                                mPhotosModel.setTitle(imageDetail.optString(JTAG_TITLE));
                                                mPhotosModel.setDescription(imageDetail.optString(JTAG_DESCRIPTION));
                                                mPhotosModelArrayList.add(mPhotosModel);
                                            }
                                            if (mPhotosModelArrayList.size()>0)
                                            {
                                                mPhotosRecyclerviewAdapter = new PhotosViewRecyclerviewAdapter(mContext,mPhotosModelArrayList);
                                                recycler_view_photos.setAdapter(mPhotosRecyclerviewAdapter);
                                                if (isFromBottom)
                                                {
                                                    recycler_view_photos.scrollToPosition(mPhotosModelArrayList.size()-notificationSize-2);
                                                }
                                                else
                                                {
                                                    recycler_view_photos.scrollToPosition(0);
                                                }


                                                mPhotosRecyclerviewAdapter.setOnBottomReachedListener(new OnBottomReachedListener()
                                                {
                                                    @Override
                                                    public void onBottomReached(int position) {
                                                        System.out.println("reachedbottom");
                                                        isFromBottom=true;
                                                        scrollTo="old";
                                                        int listSize=mPhotosModelArrayList.size();
                                                        apiID=mPhotosModelArrayList.get(listSize-1).getPhotoId();
                                                        System.out.println("Page From Value"+apiID);
                                                        if (notificationSize==15)
                                                        {
                                                            if (AppUtils.isNetworkConnected(mContext)) {
                                                                photosListApiCall(apiID,scrollTo);
                                                            } else {
                                                                AppUtils.showDialogAlertDismiss((Activity) mContext, "Network Error", getString(R.string.no_internet), R.drawable.nonetworkicon, R.drawable.roundred);
                                                            }
                                                        }


                                                    }
                                                });
                                            }
                                            else {
                                                Toast.makeText(mContext,"No photos available in this album",Toast.LENGTH_SHORT).show();
                                            }

                                        } else if (statusCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                                                statusCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING)) {
                                            AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                                                @Override
                                                public void getAccessToken() {
                                                }
                                            });
                                            photosListApiCall(id,scrollto);

                                        }

                                    }else if (responseCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) ||
                                            responseCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                                            responseCode.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
                                        AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                                            @Override
                                            public void getAccessToken() {
                                            }
                                        });
                                        photosListApiCall(id,scrollto);

                                    } else {
                                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);
                                    }

                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void responseFailure(String failureResponse) {
                            //CustomStatusDialog(RESPONSE_FAILURE);
                        }

                    });
        } catch (Exception e) {
            // CustomStatusDialog(RESPONSE_FAILURE);
            e.printStackTrace();
        }
    }






}
