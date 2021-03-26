package com.mobatia.naisapp.activities.bus_routes;

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
import com.mobatia.naisapp.activities.bus_routes.adapter.BusRouteListRecyclerviewAdapter;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.constants.NameValueConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.sports.model.BusModel;
import com.mobatia.naisapp.manager.HeaderManager;
import com.mobatia.naisapp.recyclerviewmanager.ItemOffsetDecoration;

import java.util.ArrayList;

/**
 * Created by Rijo on 25/1/17.
 */
public class BusRoutesRecyclerViewActivity extends Activity implements URLConstants, StatusConstants, JSONConstants, NameValueConstants, NaisClassNameConstants {


    Context mContext;
    private RecyclerView recycler_view_photos;
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back;
    ImageView home;
    Intent intent;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayList<BusModel> mSportsModelArrayList;
    Bundle extras;
    String tab_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participation_recyclerview);
        mContext = this;
        initUI();
    }

    private void initUI() {
        extras = getIntent().getExtras();
        if (extras != null) {
            tab_type = extras.getString("tab_type");
            mSportsModelArrayList = (ArrayList<BusModel>) extras.getSerializable("bus_route_array");
        }
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        headermanager = new HeaderManager(BusRoutesRecyclerViewActivity.this, tab_type);
        headermanager.getHeader(relativeHeader, 1);
        back = headermanager.getLeftButton();
        home = headermanager.getLogoButton();
        headermanager.setButtonLeftSelector(R.drawable.back,
                R.drawable.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(mContext, HomeListAppCompatActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
            }
        });

        recycler_view_photos = (RecyclerView) findViewById(R.id.recycler_view_photos);
        recycler_view_photos.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        int spacing = 5; // 50px
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext, spacing);
        recycler_view_photos.addItemDecoration(itemDecoration);
        recycler_view_photos.setLayoutManager(llm);
        recycler_view_photos.setAdapter(new BusRouteListRecyclerviewAdapter(mContext, mSportsModelArrayList));
    }
}
