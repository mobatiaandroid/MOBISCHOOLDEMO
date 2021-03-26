package com.mobatia.naisapp.activities.trips;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.activities.parentsassociation.model.ParentAssociationEventsModel;
import com.mobatia.naisapp.activities.pdf.PdfReaderActivity;
import com.mobatia.naisapp.activities.trips.adapter.PaymentInformationAdapter;
import com.mobatia.naisapp.activities.web_view.LoadUrlWebViewActivity;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.constants.ResultConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.HeaderManager;
import com.mobatia.naisapp.recyclerviewmanager.DividerItemDecoration;
import com.mobatia.naisapp.recyclerviewmanager.RecyclerItemListener;

import java.util.ArrayList;

public class PaymentInformationActivity extends Activity
        implements JSONConstants, URLConstants, ResultConstants, StatusConstants, NaisClassNameConstants {
    private Context mContext;
    Bundle extras;
    String tab_type;
    RelativeLayout relativeHeader;
    LinearLayout mStudentSpinner;
    ImageView studImg;
    TextView studName;
    RecyclerView mnewsLetterListView;
    TextView textViewYear;
    private ArrayList<ParentAssociationEventsModel> mListViewArray;

    HeaderManager headermanager;
    ImageView back;
    ImageView home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        mContext = this;
        initUI();
        //  PaymentRecyclerAdapter adapter=new PaymentRecyclerAdapter(mContext,parentAssociationEventsModelsArrayList);
        //  mRecyclerView.setAdapter(adapter);

    }

    private void initUI() {
        extras = getIntent().getExtras();
        if (extras != null) {
            mListViewArray = (ArrayList<ParentAssociationEventsModel>) getIntent().getSerializableExtra("informationArrayList");
        }
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        mStudentSpinner = (LinearLayout) findViewById(R.id.studentSpinner);
        studImg = (ImageView) findViewById(R.id.imagicon);
        studName = (TextView) findViewById(R.id.studentName);
        textViewYear = (TextView) findViewById(R.id.textViewYear);
        mnewsLetterListView = (RecyclerView) findViewById(R.id.mnewsLetterListView);
        mnewsLetterListView.setHasFixedSize(true);
        mnewsLetterListView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.list_divider_teal)));
        headermanager = new HeaderManager(PaymentInformationActivity.this, "Information");
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
        home.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(mContext, HomeListAppCompatActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
            }
        });
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mnewsLetterListView.setLayoutManager(llm);
        PaymentInformationAdapter adapter=new PaymentInformationAdapter(mContext,mListViewArray);
        mnewsLetterListView.setAdapter(adapter);
        mnewsLetterListView.addOnItemTouchListener(new RecyclerItemListener(getApplicationContext(), mnewsLetterListView,
                new RecyclerItemListener.RecyclerTouchListener() {
                    public void onClickItem(View v, int position) {

                        if (mListViewArray.get(position).getPdfUrl().endsWith(".pdf")) {
                            Intent intent = new Intent(mContext, PdfReaderActivity.class);
                            intent.putExtra("pdf_url", mListViewArray.get(position).getPdfUrl());
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(mContext, LoadUrlWebViewActivity.class);
                            intent.putExtra("url", mListViewArray.get(position).getPdfUrl());
                            intent.putExtra("tab_type", mListViewArray.get(position).getPdfTitle());
                            mContext.startActivity(intent);
                        }
                    }

                    public void onLongClickItem(View v, int position) {
                    }
                }));
    }



}

