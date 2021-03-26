package com.mobatia.naisapp.activities.parents_evening;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.tutorial.adapter.TutorialViewPagerAdapter;
import com.mobatia.naisapp.constants.IntentPassValueConstants;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Rijo on 6/4/17.
 */
public class ParentsEveninginfoActivity extends Activity implements IntentPassValueConstants, NaisClassNameConstants, JSONConstants {
    private ImageView mImgCircle[];
    private LinearLayout mLinearLayout;
    ViewPager mTutorialViewPager;
    Context mContext;
    TutorialViewPagerAdapter mTutorialViewPagerAdapter;
    ArrayList<Integer> mPhotoList = new ArrayList<>(Arrays.asList(R.drawable.tut_pe_1, R.drawable.tut_pe_2, R.drawable.tut_pe_3, R.drawable.tut_pe_4, R.drawable.tut_pe_5, R.drawable.tut_pe_6, R.drawable.tut_pe_7, R.drawable.tut_pe_8, R.drawable.tut_pe_9));
    int dataType;
    ImageView imageSkip;
    String mStaffid;
    String mStudentId;
    String mStudentName;
    String mStaffName;
    String mClass;
    String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        mContext = this;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            dataType = bundle.getInt(TYPE, 0);
//            mStaffid = bundle.getString(JTAG_STAFF_ID);
//            mStudentId = bundle.getString(JTAG_STUDENT_ID);
//            mStudentName = bundle.getString("studentName");
//            mStaffName = bundle.getString("staffName");
//            mClass = bundle.getString("studentClass");
//            selectedDate = bundle.getString("selectedDate");
        }
        initialiseViewPagerUI();
    }

    /*******************************************************
     * Method name : initialiseViewPagerUI Description : initialise View pager
     * UI elements Parameters : nil Return type : void Date : Jan 13, 2015
     * Author : Rijo K Jose
     *****************************************************/
    private void initialiseViewPagerUI() {
        mTutorialViewPager = (ViewPager) findViewById(R.id.tutorialViewPager);
        mLinearLayout = (LinearLayout) findViewById(R.id.linear);
        imageSkip = (ImageView) findViewById(R.id.imageSkip);
//        if (WissPreferenceManager.getUserName(mContext).equals("")) {
//            mPhotoList = new int[] { R.drawable.tut_1, R.drawable.tut_2,
//                    R.drawable.tut_3, R.drawable.tut_4, R.drawable.tut_5 };
//        } else {
//            mPhotoList = new int[] { R.drawable.tut_1, R.drawable.tut_2,
//                    R.drawable.tut_3, R.drawable.tut_4, R.drawable.tut_6 };
//        }
        mImgCircle = new ImageView[mPhotoList.size()];
        mTutorialViewPagerAdapter = new TutorialViewPagerAdapter(mContext, mPhotoList);
        mTutorialViewPager.setCurrentItem(0);
        mTutorialViewPager.setAdapter(mTutorialViewPagerAdapter);
        addShowCountView(0);
        imageSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        mTutorialViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
//                for (int i = 0; i < mPhotoList.size(); i++) {
//                    mImgCircle[i].setBackgroundDrawable(getResources()
//                            .getDrawable(R.drawable.blackround));
//                }
//                if (position < mPhotoList.size()) {
//                    mImgCircle[position].setBackgroundDrawable(getResources()
//                            .getDrawable(R.drawable.redround));
//                    mLinearLayout.removeAllViews();
//                    addShowCountView(position);
//                } else {
////                    mLinearLayout.removeAllViews();
////                    finish();
//                    mLinearLayout.removeAllViews();
//                    if (dataType==0)
//                    {
//                        finish();
//
//                    }
//                    else
//                    {
//                        Intent intent = new Intent(mContext, ParentsAssociationListActivity.class);
//                        intent.putExtra("tab_type", "Parents Association");
//                        mContext.startActivity(intent);
//                        finish();
//                    }
//
//                }
                for (int i = 0; i < mPhotoList.size(); i++) {
                    mImgCircle[i].setBackgroundDrawable(getResources()
                            .getDrawable(R.drawable.blackround));
                }
                if (position < mPhotoList.size()) {
                    mImgCircle[position].setBackgroundDrawable(getResources()
                            .getDrawable(R.drawable.redround));
                    mLinearLayout.removeAllViews();
                    addShowCountView(position);
                } else {
                    mLinearLayout.removeAllViews();
                    if (dataType == 0) {
                        finish();

                    } else {

//                        Intent intent = new Intent(mContext, ParentsEveningTimeSlotActivity.class);
//                        intent.putExtra(JTAG_STAFF_ID,mStaffid);
//                        intent.putExtra(JTAG_STUDENT_ID,mStudentId);
//                        intent.putExtra("studentName",mStudentName);
//                        intent.putExtra("staffName",mStaffName);
//                        intent.putExtra("studentClass", mClass);
//                        intent.putExtra("selectedDate", selectedDate);
//                        mContext.startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        mTutorialViewPager.getAdapter().notifyDataSetChanged();
    }

    /*******************************************************
     * Method name : addShowCountView Description : add show count view at
     * bottom Parameters : count Return type : void Date : Apr 17, 2015 Author :
     * Rijo K Jose
     *****************************************************/
    private void addShowCountView(int count) {
        for (int i = 0; i < mPhotoList.size(); i++) {
            mImgCircle[i] = new ImageView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    (int) getResources()
                            .getDimension(R.dimen.home_circle_width),
                    (int) getResources().getDimension(
                            R.dimen.home_circle_height));
            mImgCircle[i].setLayoutParams(layoutParams);
            if (i == count) {
                mImgCircle[i].setBackgroundResource(R.drawable.redround);
            } else {
                mImgCircle[i].setBackgroundResource(R.drawable.blackround);
            }
            mLinearLayout.addView(mImgCircle[i]);
        }
    }

}
