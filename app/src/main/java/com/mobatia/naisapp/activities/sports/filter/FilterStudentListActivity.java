package com.mobatia.naisapp.activities.sports.filter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.appcontroller.AppController;
import com.mobatia.naisapp.constants.IntentPassValueConstants;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.constants.ResultConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.sports.SportsActivity;
import com.mobatia.naisapp.manager.HeaderManager;

import java.util.ArrayList;

public class FilterStudentListActivity extends Activity
        implements JSONConstants,URLConstants,ResultConstants,StatusConstants,NaisClassNameConstants,IntentPassValueConstants {
    Activity activity;
    Context mContext;
    RelativeLayout header;
    HeaderManager headermanager;
    ImageView splitImage;
    TextView clearButton;
    TextView applyButton;
    TextView cancelButton;
    RecyclerView filterListView;

    GridLayoutManager recyclerViewLayoutManager;
    FilterStudentListRecyclerAdapter mFilterCarePlanRecyclerAdapter;
    ArrayList<FilterModel> unFilterModelArrayList;
    ArrayList<FilterModel>resetFilterModelArrayList;
    Bundle extras;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_careplan_layout);

        initUI();


        mFilterCarePlanRecyclerAdapter=new FilterStudentListRecyclerAdapter(mContext,AppController.filterModelArrayListFirst);
        filterListView.setAdapter(mFilterCarePlanRecyclerAdapter);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=0;i<resetFilterModelArrayList.size();i++)
                {
                    resetFilterModelArrayList.get(i).setChecked(true);
                }
                mFilterCarePlanRecyclerAdapter=new FilterStudentListRecyclerAdapter(mContext,resetFilterModelArrayList);
                filterListView.setAdapter(mFilterCarePlanRecyclerAdapter);
            }
        });
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i=0;i<AppController.filterModelArrayListFirst.size();i++)
                {
                    if (AppController.filterModelArrayListFirst.get(i).getChecked()) {
                        SportsActivity.mFilterModelList.get(i).setChecked(true);
                    }
                    else
                    {
                        SportsActivity.mFilterModelList.get(i).setChecked(false);

                    }
                }
                mFilterCarePlanRecyclerAdapter.notifyDataSetChanged();

                finish();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFilterCarePlanRecyclerAdapter=new FilterStudentListRecyclerAdapter(mContext,unFilterModelArrayList);
                filterListView.setAdapter(mFilterCarePlanRecyclerAdapter);
                finish();


            }
        });

    }

    public void initUI() {
        activity = this;
        mContext = this;

        header = findViewById(R.id.relativeHeader);
        clearButton = findViewById(R.id.clearButton);
        applyButton = findViewById(R.id.applyButton);
        cancelButton = findViewById(R.id.cancelButton);
        filterListView = findViewById(R.id.filterListView);
/*
        headermanager = new HeaderManager(activity, getResources().getString(
                R.string.filter));

        headermanager.getHeader(header, 1);
        splitImage = headermanager.getLeftButton();
        splitImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        headermanager.setButtonLeftSelector(R.drawable.newback,
                R.drawable.backpress);*/
        recyclerViewLayoutManager = new GridLayoutManager(mContext, 1);
        filterListView.setLayoutManager(recyclerViewLayoutManager);
        filterListView.setHasFixedSize(true);
        extras = getIntent().getExtras();

        AppController.filterModelArrayListFirst=new ArrayList<>();
        unFilterModelArrayList=new ArrayList<>();
        resetFilterModelArrayList=new ArrayList<>();

        if (extras != null) {
            AppController.filterModelArrayListFirst = (ArrayList<FilterModel>) extras
                    .getSerializable(FILTERS_ARRAY);
            unFilterModelArrayList = (ArrayList<FilterModel>) extras
                    .getSerializable(FILTERS_ARRAY);
            resetFilterModelArrayList = (ArrayList<FilterModel>) extras
                    .getSerializable(FILTERS_ARRAY);
        }

    }

}
