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

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.activities.newsletters.adapter.NewsDetailListAdapter;
import com.mobatia.naisapp.activities.newsletters.model.NewsLetterModel;
import com.mobatia.naisapp.activities.pdf.PdfReaderActivity;
import com.mobatia.naisapp.activities.web_view.LoadUrlWebViewActivity;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.ResultConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.HeaderManager;
import com.mobatia.naisapp.recyclerviewmanager.DividerItemDecoration;
import com.mobatia.naisapp.recyclerviewmanager.RecyclerItemListener;

import java.util.ArrayList;

/**
 * Created by gayatri on 23/3/17.
 */
public class NewsLetterDetailActivity extends Activity
        implements JSONConstants,URLConstants,ResultConstants,StatusConstants {
    Bundle extras;
    ArrayList<NewsLetterModel> list;
    String tab_type;
    Context mContext=this;
    RecyclerView mNewsLetterListView;
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back;
    ImageView home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newslettercategory_layout);
        initUI();
        setListAdapter();

    }

    private void setListAdapter() {
        NewsDetailListAdapter newsDetailListAdapter=new NewsDetailListAdapter(mContext,list);
                mNewsLetterListView.setAdapter(newsDetailListAdapter);
    }

    private void initUI() {
        extras=getIntent().getExtras();
        if(extras!=null) {
            list = (ArrayList<NewsLetterModel>) extras.getSerializable("submenuArray");
            tab_type=extras.getString("tab_type");
        }
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        mNewsLetterListView = (RecyclerView) findViewById(R.id.mnewsLetterListView);
        mNewsLetterListView.setHasFixedSize(true);
        mNewsLetterListView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.list_divider_teal)));
        headermanager = new HeaderManager(NewsLetterDetailActivity.this, tab_type);//"Newsletters");//rijo cmmented
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
                        if(list.get(position).getFilename().endsWith(".pdf")){
                            Intent intent = new Intent(mContext, PdfReaderActivity.class);
                            intent.putExtra("pdf_url", list.get(position).getFilename());
                            startActivity(intent);
                        }else {
                            Intent intent = new Intent(mContext, LoadUrlWebViewActivity.class);
                            intent.putExtra("url",list.get(position).getFilename());
                            intent.putExtra("tab_type","Newsletters");
                            //intent.putExtra("tab_type",list.get(position).getSubmenu());
                            startActivity(intent);
                        }
                    }

                    public void onLongClickItem(View v, int position) {
                        System.out.println("On Long Click Item interface");
                    }
                }));
    }
}
