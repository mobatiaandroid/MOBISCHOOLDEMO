package com.mobatia.naisapp.fragments.home;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.fragments.home.adapter.SurveyPagerAdapter;
import com.mobatia.naisapp.fragments.home.module.SurveyModel;
import com.mobatia.naisapp.fragments.home.module.SurveyQuestionsModel;

import java.util.ArrayList;

public class DummyClass {
//    public void showSurvey(final Activity activity, final ArrayList<SurveyModel> surveyArrayList)
//    {
//        final Dialog dialog = new Dialog(activity);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        dialog.setCancelable(false);
//        dialog.setContentView(R.layout.dialogue_survey);
//        Button startNowBtn = (Button) dialog.findViewById(R.id.startNowBtn);
//        ImageView imgClose = (ImageView) dialog.findViewById(R.id.imgClose);
//        TextView headingTxt = (TextView) dialog.findViewById(R.id.headingTxt);
//        TextView descriptionTxt = (TextView) dialog.findViewById(R.id.descriptionTxt);
//        headingTxt.setText("You Have New Survey Available");
//        descriptionTxt.setText("Hi, We are conducting a survey, and your response would be appreciated");
//        startNowBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
////				if (surveyArrayList.size()>1)
////				{
////					showSurveyListDialog(activity,surveyArrayList);
////				}
////				else {
////					showChoiceSurvey(activity,surveyArrayList.get(0).getSurveyQuestionsArrayList());
////				}
//
//                if (surveySize>0)
//                {
//                    pos=pos+1;
//                    showChoiceSurvey(activity,surveyArrayList.get(pos).getSurveyQuestionsArrayList(),surveyArrayList.get(pos).getSurvey_name());
//                }
//
//
//            }
//        });
//        imgClose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//        dialog.show();
//
//    }
//
//    public void showChoiceSurvey(final Activity activity, final ArrayList<SurveyQuestionsModel>surveyQuestionArrayList, final String surveyname)
//    {
//        final Dialog dialog = new Dialog(activity);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        dialog.setCancelable(false);
//        dialog.setContentView(R.layout.dialog_question_choice_survey);
//        ViewPager surveyPager = (ViewPager) dialog.findViewById(R.id.surveyPager);
//        Button nextQuestionBtn = (Button) dialog.findViewById(R.id.nextQuestionBtn);
//        TextView surveyName = (TextView) dialog.findViewById(R.id.surveyName);
////		surveyPager.setPageTransformer(true, new HingeAnimation());
//        currentPageSurvey=1;
//        surveyPager.setCurrentItem(currentPage-1);
//        surveyName.setText(surveyname);
//        surveyPager.setAdapter(new SurveyPagerAdapter(activity,surveyQuestionArrayList));
//        nextQuestionBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (surveyQuestionArrayList.size()>0)
//                {
//                    if (currentPageSurvey==surveyQuestionArrayList.size())
//                    {
//
//                        nextQuestionBtn.setText("Submit");
//                        surveySize=surveySize-1;
//                        showSurvey(activity,surveyArrayList);
//                        //callSubmit APi()
//
//                    }
//                    else {
//                        currentPageSurvey++;
//                        surveyPager.setCurrentItem(currentPageSurvey-1);
//                        nextQuestionBtn.setText("Next Question");
//
//                    }
//                }
//            }
//        });
//        dialog.show();
//
//    }

//	public void showSurveyListDialog(final Activity activity,final ArrayList<SurveyModel>surveyArrayList)
//	{
//		final Dialog dialog = new Dialog(activity);
//		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//		dialog.setCancelable(false);
//		dialog.setContentView(R.layout.dialog_question_survey);
//		ImageView imgClose = (ImageView) dialog.findViewById(R.id.imgClose);
//		RecyclerView surveyRecycler = (RecyclerView) dialog.findViewById(R.id.surveyRecycler);
//		LinearLayoutManager llm= new LinearLayoutManager(activity);
//		surveyRecycler.setLayoutManager(llm);
//		surveyRecycler.setHasFixedSize(true);
//		SurveyListAdapter mPushNotificationListAdapter = new SurveyListAdapter(mContext, surveyArrayList);
//		surveyRecycler.setAdapter(mPushNotificationListAdapter);
//		imgClose.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				dialog.dismiss();
//			}
//		});
//		surveyRecycler.addOnItemTouchListener(new RecyclerItemListener(mContext, surveyRecycler,
//				new RecyclerItemListener.RecyclerTouchListener()
//				{
//					public void onClickItem(View v, int position) {
//
//						Log.e("SURVEY SIZE LIST",String.valueOf(surveyArrayList.get(position).getSurveyQuestionsArrayList().size()));
//						showChoiceSurvey(activity,surveyArrayList.get(position).getSurveyQuestionsArrayList());
//					}
//
//					public void onLongClickItem(View v, int position) {
//					}
//				}));
//		dialog.show();
//
//	}

//public class HingeAnimation implements ViewPager.PageTransformer
//{
//
//
//	@Override
//	public void transformPage(@NonNull View page, float position) {
//
//
//		page.setTranslationY(-position* page.getWidth());
//		page.setPivotX(0);
//		page.setPivotY(0);
//		if (position<-1)
//		{
//			page.setAlpha(0);
//		}
//		else if(position<=0)
//		{
//			page.setRotation(90*Math.abs(position));
//			page.setAlpha(1-Math.abs(position));
//		}
//		else if (position<=1)
//		{
//			page.setRotation(0);
//			page.setAlpha(1);
//		}
//		else {
//			page.setAlpha(0);
//		}
//	}
//}


    /*<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="325dp"

    >
    <RelativeLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        >
    <RelativeLayout
        android:id="@+id/bannerRelative"
        android:layout_width="match_parent"
        android:layout_height="150dp"
        android:background="@drawable/curved_top_rec_survey"
       >
<androidx.constraintlayout.widget.ConstraintLayout
    android:layout_width="match_parent"
    android:layout_height="match_parent">


    <ImageView
        android:id="@+id/bannerImg"
        android:layout_width="0dp"
        android:layout_height="0dp"
        android:layout_marginStart="16dp"
        android:layout_marginTop="16dp"
        android:layout_marginEnd="16dp"
        android:layout_marginBottom="16dp"
        android:src="@drawable/survey"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <ImageView
        android:id="@+id/closeImg"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:src="@drawable/close"
        android:visibility="gone"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintTop_toTopOf="parent" />
</androidx.constraintlayout.widget.ConstraintLayout>



    </RelativeLayout>
    <RelativeLayout
        android:id="@+id/descriptionRelative"
        android:layout_width="match_parent"
        android:layout_below="@+id/bannerRelative"
        android:background="@drawable/dialoguebottomcurve"
        android:layout_height="175dp">
        <com.mobatia.naisapp.manager.CustomFontBoldWithoutColor
            android:id="@+id/titleTxt"
            android:layout_height="wrap_content"
            android:layout_width="match_parent"
            android:textSize="18sp"
            android:textColor="@color/black"
            android:layout_marginLeft="5dp"
            android:layout_marginRight="5dp"
            android:gravity="center"
            android:layout_marginTop="10dp"/>

        <com.mobatia.naisapp.manager.CustomFontSansProTextWhiteWithoutColor
            android:id="@+id/descriptionTxt"
            android:layout_height="wrap_content"
            android:layout_width="match_parent"
            android:textSize="16sp"
            android:layout_marginLeft="5dp"
            android:layout_marginRight="5dp"
            android:layout_below="@+id/titleTxt"
            android:textColor="@color/black"
            android:gravity="center"
            android:layout_marginTop="10dp"/>
        <RelativeLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:gravity="center"
            android:layout_alignParentBottom="true">
         <LinearLayout
             android:layout_height="50dp"
             android:layout_width="match_parent"
             android:orientation="horizontal"
             android:layout_marginBottom="10dp"
             android:weightSum="7">
             <com.mobatia.naisapp.manager.CustomButtonFontSansButton
                 android:id="@+id/skipBtn"
                 android:layout_width="0dp"
                 android:layout_height="match_parent"
                 android:layout_weight="3"
                 android:layout_marginLeft="5dp"
                 android:text="Not Now"
                 android:textAllCaps="false"
                 android:clickable="true"
                 android:background="@drawable/button_grey_white_curve"/>
             <TextView
                 android:layout_width="0dp"
                 android:layout_height="match_parent"
                 android:layout_weight="1"/>
             <com.mobatia.naisapp.manager.CustomButtonFontSansButton
                 android:id="@+id/startNowBtn"
                 android:layout_width="0dp"
                 android:layout_height="match_parent"
                 android:layout_weight="3"
                 android:layout_marginLeft="5dp"
                 android:text="Start Now"
                 android:textAllCaps="false"
                 android:layout_marginRight="5dp"
                 android:background="@drawable/button_blue"/>
         </LinearLayout>
        </RelativeLayout>

    </RelativeLayout>
</RelativeLayout>
</RelativeLayout>


    <!--startNowBtn   skipBtn  descriptionTxt  titleTxt  bannerImg   closeImg -->*/
}
