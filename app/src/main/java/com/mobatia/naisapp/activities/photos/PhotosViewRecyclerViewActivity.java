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

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.activities.photos.adapter.PhotosViewRecyclerviewAdapter;
import com.mobatia.naisapp.appcontroller.AppController;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.constants.NameValueConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.gallery.model.PhotosListModel;
import com.mobatia.naisapp.manager.HeaderManager;
import com.mobatia.naisapp.recyclerviewmanager.ItemOffsetDecoration;
import com.mobatia.naisapp.recyclerviewmanager.RecyclerItemListener;

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
    SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayList<PhotosListModel> mPhotosModelArrayList;
    PhotosViewRecyclerviewAdapter mPhotosRecyclerviewAdapter;
    RecyclerView.LayoutManager recyclerViewLayoutManager;
  Bundle extras;
    int pos=0;
//    String photo_id="-1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photos_recyclerview_activity);
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
            mPhotosModelArrayList=new ArrayList<>();
//            mPhotosModelArrayList= (ArrayList<PhotosListModel>) extras.getSerializable("photo_array");
            mPhotosModelArrayList= AppController.mPhotosModelArrayListGallery;
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
        mPhotosRecyclerviewAdapter = new PhotosViewRecyclerviewAdapter(mContext,  mPhotosModelArrayList.get(pos).getmPhotosUrlArrayList());
        recycler_view_photos.setAdapter(mPhotosRecyclerviewAdapter);
        recycler_view_photos.addOnItemTouchListener(new RecyclerItemListener(mContext, recycler_view_photos,
                new RecyclerItemListener.RecyclerTouchListener() {
                    public void onClickItem(View v, int position) {
                        Intent intent = new Intent(mContext, PhotosViewPagerActivity.class);
                        intent.putExtra("photo_array", mPhotosModelArrayList.get(pos).getmPhotosUrlArrayList());
                        intent.putExtra("pos", position);
                        startActivity(intent);
                    }

                    public void onLongClickItem(View v, int position) {
                    }
                }));

//        LinearLayoutManager llm = new LinearLayoutManager(this);
//        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
//        recycler_view_photos.setLayoutManager(llm);

//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                photosListApiCall();
//                // Refresh items
//                refreshItems();
//            }
//        });


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










}
