package com.mobatia.naisapp.fragments.about_us;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.activities.pdf.PdfReaderActivity;
import com.mobatia.naisapp.activities.web_view.LoadUrlWebViewActivity;
import com.mobatia.naisapp.constants.CacheDIRConstants;
import com.mobatia.naisapp.constants.IntentPassValueConstants;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.constants.NaisTabConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.about_us.adapter.AccreditationsRecyclerViewAdapter;
import com.mobatia.naisapp.fragments.about_us.model.AboutusModel;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.HeaderManager;

import java.util.ArrayList;

/**
 * Created by gayatri on 11/5/17.
 */
public class AccreditationsActivity  extends Activity implements AdapterView.OnItemClickListener,
        NaisTabConstants,CacheDIRConstants, URLConstants,JSONConstants,StatusConstants,
        IntentPassValueConstants,NaisClassNameConstants {
    private Context mContext;
    Bundle extras;
    String tab_type,bannner_img;
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView back;
    ImageView home;
    ArrayList<AboutusModel> mAboutUsListArray ;
    ImageView bannerImagePager;
//    ViewPager bannerImagePager;
    ArrayList<String> bannerUrlImageArray=new ArrayList<>();
    private ListView mTermsCalendarListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatterbox_list);
//		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this,
//				LoginActivity.class));
        mContext = this;
        initialiseUI();    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



    if (mAboutUsListArray.get(position).getItemPdfUrl().endsWith(".pdf"))
        {
            Intent intent = new Intent(mContext, PdfReaderActivity.class);
            intent.putExtra("pdf_url",mAboutUsListArray.get(position).getItemPdfUrl());
            intent.putExtra("tab_type", "Accreditiations & Examinations");

            startActivity(intent);
        }
        else {
        Intent intent = new Intent(mContext, LoadUrlWebViewActivity.class);
        intent.putExtra("url", mAboutUsListArray.get(position).getItemPdfUrl());
        intent.putExtra("tab_type", "Accreditiations & Examinations");
        startActivity(intent);
    }


    }

    public void initialiseUI() {
        extras=getIntent().getExtras();
        if(extras!=null) {
            mAboutUsListArray=(ArrayList<AboutusModel>) extras
                    .getSerializable("array");
            bannner_img=extras.getString("banner_image");
            System.out.println("Image url--"+bannner_img);

            //desc=extras.getString("desc");
            //title=extras.getString("title");
            if(!bannner_img.equals("")) {
                bannerUrlImageArray.add(bannner_img);
            }
            }
        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        mTermsCalendarListView = (ListView) findViewById(R.id.mTermsCalendarListView);
        bannerImagePager= (ImageView) findViewById(R.id.bannerImageViewPager);
//        bannerImagePager= (ViewPager) findViewById(R.id.bannerImageViewPager);
        bannerImagePager.setVisibility(View.GONE);

        headermanager = new HeaderManager(AccreditationsActivity.this, "Accreditations & Examinations");
        headermanager.getHeader(relativeHeader, 1);
        back = headermanager.getLeftButton();
        headermanager.setButtonLeftSelector(R.drawable.back,
                R.drawable.back);
        mTermsCalendarListView.setOnItemClickListener(this);
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
//        bannerImagePager.setAdapter(new ImagePagerDrawableAdapter(bannerUrlImageArray, mContext));
        Glide.with(mContext).load(AppUtils.replace(bannner_img)).centerCrop().into(bannerImagePager);

        AccreditationsRecyclerViewAdapter recyclerViewAdapter=new AccreditationsRecyclerViewAdapter(mContext,mAboutUsListArray);
        mTermsCalendarListView.setAdapter(recyclerViewAdapter);
    }
}
