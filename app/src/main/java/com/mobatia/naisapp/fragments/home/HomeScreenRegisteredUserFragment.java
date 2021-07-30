package com.mobatia.naisapp.fragments.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AlertDialog;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.appcontroller.AppController;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.constants.NaisTabConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.about_us.AboutUsFragment;
import com.mobatia.naisapp.fragments.absence.AbsenceFragment;
import com.mobatia.naisapp.fragments.calendar.CalendarFragment;
import com.mobatia.naisapp.fragments.canteen_new.CanteenFragmentNew;
import com.mobatia.naisapp.fragments.cca.CcaFragmentMain;
import com.mobatia.naisapp.fragments.communications.CommunicationsFragment;
import com.mobatia.naisapp.fragments.contact_us.ContactUsFragment;
import com.mobatia.naisapp.fragments.early_years.EarlyYearsFragment;
import com.mobatia.naisapp.fragments.gallery.GalleryFragment;
import com.mobatia.naisapp.fragments.home.adapter.ImagePagerDrawableAdapter;
import com.mobatia.naisapp.fragments.ib_programme.IbProgrammeFragment;
import com.mobatia.naisapp.fragments.nae_programmes.NaeProgrammesFragment;
import com.mobatia.naisapp.fragments.notifications.NotificationsFragment;
import com.mobatia.naisapp.fragments.notifications.NotificationsFragmentNew;
import com.mobatia.naisapp.fragments.parent_essentials.ParentEssentialsFragment;
import com.mobatia.naisapp.fragments.parentassociation.ParentAssociationsFragment;
import com.mobatia.naisapp.fragments.parents_evening.ParentsEveningFragment;
import com.mobatia.naisapp.fragments.parents_evening.model.StudentModel;
import com.mobatia.naisapp.fragments.performing_arts.PerformingArtsFragment;
import com.mobatia.naisapp.fragments.permission_slips.PermissionSlipsFragment;
import com.mobatia.naisapp.fragments.primary.PrimaryFragment;
import com.mobatia.naisapp.fragments.report.ReportFragment;
import com.mobatia.naisapp.fragments.report.model.ReportModel;
import com.mobatia.naisapp.fragments.secondary.SecondaryFragment;
import com.mobatia.naisapp.fragments.sports.SportsMainScreenFragment;
import com.mobatia.naisapp.fragments.trips.TripsFragmentNew;
import com.mobatia.naisapp.fragments.universityguidance.UniverstyGuidanceFragment;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class   HomeScreenRegisteredUserFragment extends Fragment implements
		OnClickListener, NaisTabConstants, NaisClassNameConstants,JSONConstants,StatusConstants,URLConstants {

	private RelativeLayout mRelOne;
	private RelativeLayout mRelTwo;
	private RelativeLayout mRelThree;
	private RelativeLayout mRelFour;
	private RelativeLayout mRelFive;
	private RelativeLayout mRelSix;
	private RelativeLayout mRelSeven;
	private RelativeLayout mRelEight;
	private RelativeLayout mRelNine;

	private TextView mTxtOne;
	private TextView mTxtTwo;
	private TextView mTxtThree;
	private TextView mTxtFour;
	private TextView mTxtFive;
	private TextView mTxtSix;
	private TextView mTxtSeven;
	private TextView mTxtEight;
	private TextView mTxtNine;

	private TextView relImgOneDot;
	private TextView relImgTwoDot;
	private TextView relImgThreeDot;
	private TextView relImgFourDot;
	private TextView relImgFiveDot;
	private TextView relImgSixDot;
	private TextView relImgSevenDot;
	private TextView relImgEightDot;
	private TextView relImgNineDot;

	private ImageView mImgOne;
	private ImageView mImgTwo;
	private ImageView mImgThree;
	private ImageView mImgFour;
	private ImageView mImgFive;
	private ImageView mImgSix;
	private ImageView mImgSeven;
	private ImageView mImgEight;
	private ImageView mImgNine;

	private View mRootView;
	private Context mContext;
	private View viewTouched = null;
	private String TAB_ID;
	private String INTENT_TAB_ID;
	//	private ImageView mBannerImg;
	private String[] mSectionText;
	private boolean isDraggable;
	int currentPage = 0;
	ViewPager bannerImagePager;
	ArrayList<Integer>homaBannerImageArray;
	ArrayList<String>homeBannerUrlImageArray;
	//	LinearLayout AppController.mLinearLayouts;
	private static final int PERMISSION_CALLBACK_CONSTANT_CALENDAR = 1;
	private static final int PERMISSION_CALLBACK_CONSTANT_EXTERNAL_STORAGE = 2;
	private static final int PERMISSION_CALLBACK_CONSTANT_LOCATION = 3;
	private static final int REQUEST_PERMISSION_CALENDAR = 101;
	private static final int REQUEST_PERMISSION_EXTERNAL_STORAGE = 102;
	private static final int REQUEST_PERMISSION_LOCATION = 103;
	String[] permissionsRequiredCalendar = new String[]{Manifest.permission.READ_CALENDAR,
			Manifest.permission.WRITE_CALENDAR};
	String[] permissionsRequiredExternalStorage = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.WRITE_EXTERNAL_STORAGE};
	String[] permissionsRequiredLocation = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
			Manifest.permission.ACCESS_FINE_LOCATION};
	private SharedPreferences calendarPermissionStatus;
	private SharedPreferences externalStoragePermissionStatus;
	private SharedPreferences locationPermissionStatus;
	private boolean calendarToSettings = false;
	private boolean externalStorageToSettings = false;
	private boolean locationToSettings = false;
	String tabiDToProceed="";
	ArrayList<ReportModel> studentsModelArrayList;
	ArrayList<StudentModel> studentsModelArrayListn;
	ArrayList<String> studentList = new ArrayList<>();
	public HomeScreenRegisteredUserFragment(String title,
											DrawerLayout mDrawerLayouts, ListView listView,
											String[] mListItemArray, TypedArray mListImgArray) {
		AppController.mTitles = title;
		AppController.mDrawerLayouts = mDrawerLayouts;
		AppController.mListViews = listView;
		AppController.listitemArrays = mListItemArray;
		AppController.mListImgArrays = mListImgArray;
	}
	public HomeScreenRegisteredUserFragment(String title,
											DrawerLayout mDrawerLayouts, ListView listView, LinearLayout linearLayout,
											String[] mListItemArray, TypedArray mListImgArray) {
		AppController.mTitles = title;
		AppController.mDrawerLayouts = mDrawerLayouts;
		AppController.mLinearLayouts = linearLayout;
		AppController.mListViews = listView;
		AppController.listitemArrays = mListItemArray;
		AppController.mListImgArrays = mListImgArray;
	}
	public HomeScreenRegisteredUserFragment() {
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * android.support.v4.app.Fragment#onCreateOptionsMenu(android.view.Menu,
	 * android.view.MenuInflater)
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		mRootView = inflater.inflate(R.layout.fragment_home_screen_new, container,
				false);
		mContext = getActivity();
		AppController.isCommunicationDetailVisited=false;

		calendarPermissionStatus = getActivity().getSharedPreferences("calendarPermissionStatus", getActivity().MODE_PRIVATE);
		externalStoragePermissionStatus = getActivity().getSharedPreferences("externalStoragePermissionStatus", getActivity().MODE_PRIVATE);
		locationPermissionStatus = getActivity().getSharedPreferences("locationPermissionStatus", getActivity().MODE_PRIVATE);
//		setHasOptionsMenu(true);
//		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(mContext));


		initialiseUI();

		setListeners();
		setDragListenersForButtons();
		getButtonBgAndTextImages();
		HomeListAppCompatActivity.mListAdapter.notifyDataSetChanged();
		return mRootView;
	}

	/********************************************************
	 * Method name : getButtonBgAndTextImages Description : get button
	 * background color and text images Parameters : nil Return type : void Date
	 * * : Nov 7, 2014 Author : Rijo K Jose
	 *****************************************************/
	private void getButtonBgAndTextImages() {

		if (Integer.parseInt(PreferenceManager
				.getButtonOneTextImage(mContext)) != 0) {
			mImgOne.setImageDrawable(AppController.mListImgArrays.getDrawable(Integer
					.parseInt(PreferenceManager
							.getButtonOneTextImage(mContext))));
			String relTwoStr="";
			if (AppController.listitemArrays[Integer
					.parseInt(PreferenceManager
							.getButtonOneTextImage(mContext))].toString().equalsIgnoreCase(CCAS))
			{
				relTwoStr=CCAS;
			}
			else
			{
				relTwoStr=AppController.listitemArrays[Integer
						.parseInt(PreferenceManager
								.getButtonOneTextImage(mContext))].toUpperCase();

			}
			mTxtOne.setText(relTwoStr);
//			mTxtTwo.setText(AppController.listitemArrays[Integer
//					.parseInt(PreferenceManager
//							.getButtonTwoTextImage(mContext))].toUpperCase());
			mRelOne.setBackgroundColor(PreferenceManager
					.getButtonOneBg(mContext));
			if (PreferenceManager.getButtonOneTabId(mContext).equalsIgnoreCase(TAB_CALENDAR_REG)) {
				if(!(PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					System.out.println("relImgDot 1");
					System.out.println("relImgDot 1 badge "+PreferenceManager.getCalendarBadge(mContext));

					relImgOneDot.setVisibility(View.VISIBLE);
					relImgOneDot.setText(PreferenceManager.getCalendarBadge(mContext));
					relImgOneDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else if((PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					System.out.println("relImgDot 2");

					relImgOneDot.setVisibility(View.VISIBLE);
					relImgOneDot.setText(PreferenceManager.getCalendarEditedBadge(mContext));
					relImgOneDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					System.out.println("relImgDot 3");

					relImgOneDot.setVisibility(View.VISIBLE);

					relImgOneDot.setText(PreferenceManager.getCalendarBadge(mContext));
					relImgOneDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {

					System.out.println("relImgDot");
					relImgOneDot.setVisibility(View.GONE);
				}


			} else if (PreferenceManager.getButtonOneTabId(mContext).equalsIgnoreCase(TAB_NOTIFICATIONS_REG)) {
				if(!(PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgOneDot.setVisibility(View.VISIBLE);
					relImgOneDot.setText(PreferenceManager.getNotificationBadge(mContext));
					relImgOneDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgOneDot.setVisibility(View.VISIBLE);
					relImgOneDot.setText(PreferenceManager.getNotificationEditedBadge(mContext));
					relImgOneDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgOneDot.setVisibility(View.VISIBLE);
					relImgOneDot.setText(PreferenceManager.getNotificationBadge(mContext));
					relImgOneDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgOneDot.setVisibility(View.GONE);

				}

			} else if (PreferenceManager.getButtonOneTabId(mContext).equalsIgnoreCase(TAB_REPORT_REG)) {
				if(!(PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgOneDot.setVisibility(View.VISIBLE);
					relImgOneDot.setText(PreferenceManager.getReportsBadge(mContext));
					relImgOneDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgOneDot.setVisibility(View.VISIBLE);
					relImgOneDot.setText(PreferenceManager.getReportsEditedBadge(mContext));
					relImgOneDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgOneDot.setVisibility(View.VISIBLE);
					relImgOneDot.setText(PreferenceManager.getReportsBadge(mContext));
					relImgOneDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else if((PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgOneDot.setVisibility(View.GONE);

				}
				else {
					relImgOneDot.setVisibility(View.GONE);

				}

			} else if (PreferenceManager.getButtonOneTabId(mContext).equalsIgnoreCase(TAB_CCAS_REG)) {
				if(!(PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgOneDot.setVisibility(View.VISIBLE);
					relImgOneDot.setText(PreferenceManager.getCcaBadge(mContext));
					relImgOneDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgOneDot.setVisibility(View.VISIBLE);
					relImgOneDot.setText(PreferenceManager.getCcaEditedBadge(mContext));
					relImgOneDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgOneDot.setVisibility(View.VISIBLE);
					relImgOneDot.setText(PreferenceManager.getCcaBadge(mContext));
					relImgOneDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgOneDot.setVisibility(View.GONE);

				}

			}
			else if (PreferenceManager.getButtonOneTabId(mContext).equalsIgnoreCase(TAB_SPORTS_REG)) {
				if(!(PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgOneDot.setVisibility(View.VISIBLE);
					relImgOneDot.setText(PreferenceManager.getSportsBadge(mContext));
					relImgOneDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgOneDot.setVisibility(View.VISIBLE);
					relImgOneDot.setText(PreferenceManager.getSportsEditedBadge(mContext));
					relImgOneDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgOneDot.setVisibility(View.VISIBLE);
					relImgOneDot.setText(PreferenceManager.getSportsBadge(mContext));
					relImgOneDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgOneDot.setVisibility(View.GONE);

				}

			}else if (PreferenceManager.getButtonOneTabId(mContext).equalsIgnoreCase(TAB_UNIVERSITY_GUIDANCE_REG)) {
				if(!(PreferenceManager.getUniversity_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase("0"))))
				{
					relImgOneDot.setVisibility(View.VISIBLE);
					relImgOneDot.setText(PreferenceManager.getUniversity_badge(mContext));
					relImgOneDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getUniversity_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase("0"))) &&  (!(PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase(""))))
				{
					relImgOneDot.setVisibility(View.VISIBLE);
					relImgOneDot.setText(PreferenceManager.getUniversity_edit_badge(mContext));
					relImgOneDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getUniversity_badge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase("0"))))
				{
					relImgOneDot.setVisibility(View.VISIBLE);
					relImgOneDot.setText(PreferenceManager.getUniversity_badge(mContext));
					relImgOneDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgOneDot.setVisibility(View.GONE);

				}

			}
			else if (PreferenceManager.getButtonOneTabId(mContext).equalsIgnoreCase(TAB_COMMUNICATIONS_REG)) {
				if(!(PreferenceManager.getNoticeBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNoticeEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgOneDot.setVisibility(View.VISIBLE);
					relImgOneDot.setText(PreferenceManager.getNoticeBadge(mContext));
					relImgOneDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getNoticeBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNoticeEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgOneDot.setVisibility(View.VISIBLE);
					relImgOneDot.setText(PreferenceManager.getNoticeEditedBadge(mContext));
					relImgOneDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getNoticeBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getNoticeEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgOneDot.setVisibility(View.VISIBLE);
					relImgOneDot.setText(PreferenceManager.getNoticeBadge(mContext));
					relImgOneDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgOneDot.setVisibility(View.GONE);

				}

			}
			else if (PreferenceManager.getButtonOneTabId(mContext).equalsIgnoreCase(TAB_TRIPS_REG)) {
				if(!(PreferenceManager.getPaymentitem_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getPaymentitem_edit_badge(mContext).equalsIgnoreCase("0"))))
				{
					System.out.println("relImgDot 1");
					System.out.println("relImgDot 1 badge "+PreferenceManager.getPaymentitem_badge(mContext));

					relImgOneDot.setVisibility(View.VISIBLE);
					relImgOneDot.setText(PreferenceManager.getPaymentitem_badge(mContext));
					relImgOneDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else if((PreferenceManager.getPaymentitem_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getPaymentitem_edit_badge(mContext).equalsIgnoreCase("0"))))
				{
					System.out.println("relImgDot 2");

					relImgOneDot.setVisibility(View.VISIBLE);

					relImgOneDot.setText(PreferenceManager.getPaymentitem_edit_badge(mContext));
					relImgOneDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getPaymentitem_badge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getPaymentitem_edit_badge(mContext).equalsIgnoreCase("0"))))
				{
					System.out.println("relImgDot 3");

					relImgOneDot.setVisibility(View.VISIBLE);

					relImgOneDot.setText(PreferenceManager.getPaymentitem_badge(mContext));
					relImgOneDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {

					System.out.println("relImgDot");
					relImgOneDot.setVisibility(View.GONE);
				}


			}
			else
			{
				relImgOneDot.setVisibility(View.GONE);
			}
		}
		if (Integer.parseInt(PreferenceManager
				.getButtonTwoTextImage(mContext)) != 0) {
			mImgTwo.setImageDrawable(AppController.mListImgArrays.getDrawable(Integer
					.parseInt(PreferenceManager
							.getButtonTwoTextImage(mContext))));
			String relTwoStr="";
			if (AppController.listitemArrays[Integer
					.parseInt(PreferenceManager
							.getButtonTwoTextImage(mContext))].toString().equalsIgnoreCase(CCAS))
			{
				relTwoStr=CCAS;
			}
			else
			{
				relTwoStr=AppController.listitemArrays[Integer
						.parseInt(PreferenceManager
								.getButtonTwoTextImage(mContext))].toUpperCase();

			}
			mTxtTwo.setText(relTwoStr);
//			mTxtTwo.setText(AppController.listitemArrays[Integer
//					.parseInt(PreferenceManager
//							.getButtonTwoTextImage(mContext))].toUpperCase());
			mRelTwo.setBackgroundColor(PreferenceManager
					.getButtonTwoBg(mContext));
			if (PreferenceManager.getButtonTwoTabId(mContext).equalsIgnoreCase(TAB_CALENDAR_REG)) {
				if(!(PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgTwoDot.setVisibility(View.VISIBLE);
					relImgTwoDot.setText(PreferenceManager.getCalendarBadge(mContext));
					relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else if((PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgTwoDot.setVisibility(View.VISIBLE);

					relImgTwoDot.setText(PreferenceManager.getCalendarEditedBadge(mContext));
					relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgTwoDot.setVisibility(View.VISIBLE);

					relImgTwoDot.setText(PreferenceManager.getCalendarBadge(mContext));
					relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgTwoDot.setVisibility(View.GONE);
				}


			} else if (PreferenceManager.getButtonTwoTabId(mContext).equalsIgnoreCase(TAB_NOTIFICATIONS_REG)) {
				if(!(PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgTwoDot.setVisibility(View.VISIBLE);
					relImgTwoDot.setText(PreferenceManager.getNotificationBadge(mContext));
					relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgTwoDot.setVisibility(View.VISIBLE);
					relImgTwoDot.setText(PreferenceManager.getNotificationEditedBadge(mContext));
					relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgTwoDot.setVisibility(View.VISIBLE);
					relImgTwoDot.setText(PreferenceManager.getNotificationBadge(mContext));
					relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgTwoDot.setVisibility(View.GONE);

				}
			} else if (PreferenceManager.getButtonTwoTabId(mContext).equalsIgnoreCase(TAB_REPORT_REG)) {
				if(!(PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgTwoDot.setVisibility(View.VISIBLE);
					relImgTwoDot.setText(PreferenceManager.getReportsBadge(mContext));
					relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgTwoDot.setVisibility(View.VISIBLE);
					relImgTwoDot.setText(PreferenceManager.getReportsEditedBadge(mContext));
					relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgTwoDot.setVisibility(View.VISIBLE);
					relImgTwoDot.setText(PreferenceManager.getReportsBadge(mContext));
					relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else if((PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgOneDot.setVisibility(View.GONE);

				}
				else {
					relImgTwoDot.setVisibility(View.GONE);

				}
			}  else if (PreferenceManager.getButtonTwoTabId(mContext).equalsIgnoreCase(TAB_TRIPS_REG)) {
				if(!(PreferenceManager.getPaymentitem_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getPaymentitem_edit_badge(mContext).equalsIgnoreCase("0"))))
				{
					relImgTwoDot.setVisibility(View.VISIBLE);
					relImgTwoDot.setText(PreferenceManager.getPaymentitem_badge(mContext));
					relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getPaymentitem_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getPaymentitem_edit_badge(mContext).equalsIgnoreCase("0"))))
				{
					relImgTwoDot.setVisibility(View.VISIBLE);
					relImgTwoDot.setText(PreferenceManager.getPaymentitem_edit_badge(mContext));
					relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getPaymentitem_badge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getPaymentitem_edit_badge(mContext).equalsIgnoreCase("0"))))
				{
					relImgTwoDot.setVisibility(View.VISIBLE);
					relImgTwoDot.setText(PreferenceManager.getPaymentitem_badge(mContext));
					relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgTwoDot.setVisibility(View.GONE);

				}
			}
			else if (PreferenceManager.getButtonTwoTabId(mContext).equalsIgnoreCase(TAB_CCAS_REG)) {
				if(!(PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgTwoDot.setVisibility(View.VISIBLE);
					relImgTwoDot.setText(PreferenceManager.getCcaBadge(mContext));
					relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgTwoDot.setVisibility(View.VISIBLE);
					relImgTwoDot.setText(PreferenceManager.getCcaEditedBadge(mContext));
					relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgTwoDot.setVisibility(View.VISIBLE);
					relImgTwoDot.setText(PreferenceManager.getCcaBadge(mContext));
					relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgTwoDot.setVisibility(View.GONE);

				}
			}
			else if (PreferenceManager.getButtonTwoTabId(mContext).equalsIgnoreCase(TAB_SPORTS_REG))
			 {
				if(!(PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgTwoDot.setVisibility(View.VISIBLE);
					relImgTwoDot.setText(PreferenceManager.getSportsBadge(mContext));
					relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgTwoDot.setVisibility(View.VISIBLE);
					relImgTwoDot.setText(PreferenceManager.getSportsEditedBadge(mContext));
					relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgTwoDot.setVisibility(View.VISIBLE);
					relImgTwoDot.setText(PreferenceManager.getSportsBadge(mContext));
					relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgTwoDot.setVisibility(View.GONE);

				}
			}
			else if (PreferenceManager.getButtonTwoTabId(mContext).equalsIgnoreCase(TAB_UNIVERSITY_GUIDANCE_REG))
			 {
				if(!(PreferenceManager.getUniversity_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase("0"))))
				{
					relImgTwoDot.setVisibility(View.VISIBLE);
					relImgTwoDot.setText(PreferenceManager.getUniversity_badge(mContext));
					relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getUniversity_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase("0"))) && (!(PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase(""))))
				{
					relImgTwoDot.setVisibility(View.VISIBLE);
					relImgTwoDot.setText(PreferenceManager.getUniversity_edit_badge(mContext));
					relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getUniversity_badge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase("0"))))
				{
					relImgTwoDot.setVisibility(View.VISIBLE);
					relImgTwoDot.setText(PreferenceManager.getUniversity_badge(mContext));
					relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgTwoDot.setVisibility(View.GONE);

				}
			}
			else if (PreferenceManager.getButtonTwoTabId(mContext).equalsIgnoreCase(TAB_COMMUNICATIONS_REG)) {
				if(!(PreferenceManager.getNoticeBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNoticeEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgTwoDot.setVisibility(View.VISIBLE);
					relImgTwoDot.setText(PreferenceManager.getNoticeBadge(mContext));
					relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getNoticeBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNoticeEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgTwoDot.setVisibility(View.VISIBLE);
					relImgTwoDot.setText(PreferenceManager.getNoticeEditedBadge(mContext));
					relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getNoticeBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getNoticeEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgTwoDot.setVisibility(View.VISIBLE);
					relImgTwoDot.setText(PreferenceManager.getNoticeBadge(mContext));
					relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgTwoDot.setVisibility(View.GONE);

				}

			}
			else
			{
				relImgTwoDot.setVisibility(View.GONE);
			}
		}
		if (Integer.parseInt(PreferenceManager
				.getButtonThreeTextImage(mContext)) != 0) {
			mImgThree.setImageDrawable(AppController.mListImgArrays.getDrawable(Integer
					.parseInt(PreferenceManager
							.getButtonThreeTextImage(mContext))));
			String relTwoStr="";
			if (AppController.listitemArrays[Integer
					.parseInt(PreferenceManager
							.getButtonThreeTextImage(mContext))].toString().equalsIgnoreCase(CCAS))
			{
				relTwoStr=CCAS;
			}
			else
			{
				relTwoStr=AppController.listitemArrays[Integer
						.parseInt(PreferenceManager
								.getButtonThreeTextImage(mContext))].toUpperCase();

			}
			mTxtThree.setText(relTwoStr);
//			mTxtThree.setText(AppController.listitemArrays[Integer
//					.parseInt(PreferenceManager
//							.getButtonThreeTextImage(mContext))].toUpperCase());
			mRelThree.setBackgroundColor(PreferenceManager
					.getButtonThreeBg(mContext));
			if (PreferenceManager.getButtonThreeTabId(mContext).equalsIgnoreCase(TAB_CALENDAR_REG)) {
				if(!(PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgThreeDot.setVisibility(View.VISIBLE);
					relImgThreeDot.setText(PreferenceManager.getCalendarBadge(mContext));
					relImgThreeDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else if((PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgThreeDot.setVisibility(View.VISIBLE);

					relImgThreeDot.setText(PreferenceManager.getCalendarEditedBadge(mContext));
					relImgThreeDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgThreeDot.setVisibility(View.VISIBLE);

					relImgThreeDot.setText(PreferenceManager.getCalendarBadge(mContext));
					relImgThreeDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgThreeDot.setVisibility(View.GONE);
				}

			} else if (PreferenceManager.getButtonThreeTabId(mContext).equalsIgnoreCase(TAB_NOTIFICATIONS_REG)) {
				if(!(PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgThreeDot.setVisibility(View.VISIBLE);
					relImgThreeDot.setText(PreferenceManager.getNotificationBadge(mContext));
					relImgThreeDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgThreeDot.setVisibility(View.VISIBLE);
					relImgThreeDot.setText(PreferenceManager.getNotificationEditedBadge(mContext));
					relImgThreeDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgThreeDot.setVisibility(View.VISIBLE);
					relImgThreeDot.setText(PreferenceManager.getNotificationBadge(mContext));
					relImgThreeDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgThreeDot.setVisibility(View.GONE);

				}
			} else if (PreferenceManager.getButtonThreeTabId(mContext).equalsIgnoreCase(TAB_REPORT_REG)) {
				if(!(PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgThreeDot.setVisibility(View.VISIBLE);
					relImgThreeDot.setText(PreferenceManager.getReportsBadge(mContext));
					relImgThreeDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgThreeDot.setVisibility(View.VISIBLE);
					relImgThreeDot.setText(PreferenceManager.getReportsEditedBadge(mContext));
					relImgThreeDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgThreeDot.setVisibility(View.VISIBLE);
					relImgThreeDot.setText(PreferenceManager.getReportsBadge(mContext));
					relImgThreeDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else if((PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgThreeDot.setVisibility(View.GONE);

				}
				else {
					relImgThreeDot.setVisibility(View.GONE);

				}
			}
			else if (PreferenceManager.getButtonThreeTabId(mContext).equalsIgnoreCase(TAB_TRIPS_REG)) {
				if(!(PreferenceManager.getPaymentitem_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getPaymentitem_edit_badge(mContext).equalsIgnoreCase("0"))))
				{
					relImgThreeDot.setVisibility(View.VISIBLE);
					relImgThreeDot.setText(PreferenceManager.getPaymentitem_badge(mContext));
					relImgThreeDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getPaymentitem_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getPaymentitem_edit_badge(mContext).equalsIgnoreCase("0"))))
				{
					relImgThreeDot.setVisibility(View.VISIBLE);
					relImgThreeDot.setText(PreferenceManager.getPaymentitem_edit_badge(mContext));
					relImgThreeDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getPaymentitem_badge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getPaymentitem_edit_badge(mContext).equalsIgnoreCase("0"))))
				{
					relImgThreeDot.setVisibility(View.VISIBLE);
					relImgThreeDot.setText(PreferenceManager.getPaymentitem_badge(mContext));
					relImgThreeDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgThreeDot.setVisibility(View.GONE);

				}
			}
			else if (PreferenceManager.getButtonThreeTabId(mContext).equalsIgnoreCase(TAB_CCAS_REG)) {
				if(!(PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgThreeDot.setVisibility(View.VISIBLE);
					relImgThreeDot.setText(PreferenceManager.getCcaBadge(mContext));
					relImgThreeDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgThreeDot.setVisibility(View.VISIBLE);
					relImgThreeDot.setText(PreferenceManager.getCcaEditedBadge(mContext));
					relImgThreeDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgThreeDot.setVisibility(View.VISIBLE);
					relImgThreeDot.setText(PreferenceManager.getCcaBadge(mContext));
					relImgThreeDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgThreeDot.setVisibility(View.GONE);

				}
			}
			else if (PreferenceManager.getButtonThreeTabId(mContext).equalsIgnoreCase(TAB_SPORTS_REG)) {
				if(!(PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgThreeDot.setVisibility(View.VISIBLE);
					relImgThreeDot.setText(PreferenceManager.getSportsBadge(mContext));
					relImgThreeDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgThreeDot.setVisibility(View.VISIBLE);
					relImgThreeDot.setText(PreferenceManager.getSportsEditedBadge(mContext));
					relImgThreeDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgThreeDot.setVisibility(View.VISIBLE);
					relImgThreeDot.setText(PreferenceManager.getSportsBadge(mContext));
					relImgThreeDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgThreeDot.setVisibility(View.GONE);

				}
			}
			else if (PreferenceManager.getButtonThreeTabId(mContext).equalsIgnoreCase(TAB_UNIVERSITY_GUIDANCE_REG)) {
				if(!(PreferenceManager.getUniversity_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase("0"))))
				{
					relImgThreeDot.setVisibility(View.VISIBLE);
					relImgThreeDot.setText(PreferenceManager.getUniversity_badge(mContext));
					relImgThreeDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getUniversity_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase("0"))) && (!(PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase(""))))
				{
					relImgThreeDot.setVisibility(View.VISIBLE);
					relImgThreeDot.setText(PreferenceManager.getUniversity_edit_badge(mContext));
					relImgThreeDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getUniversity_badge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase("0"))))
				{
					relImgThreeDot.setVisibility(View.VISIBLE);
					relImgThreeDot.setText(PreferenceManager.getUniversity_badge(mContext));
					relImgThreeDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgThreeDot.setVisibility(View.GONE);

				}
			}
			else if (PreferenceManager.getButtonThreeTabId(mContext).equalsIgnoreCase(TAB_COMMUNICATIONS_REG)) {
				if(!(PreferenceManager.getNoticeBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNoticeEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgThreeDot.setVisibility(View.VISIBLE);
					relImgThreeDot.setText(PreferenceManager.getNoticeBadge(mContext));
					relImgThreeDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getNoticeBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNoticeEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgThreeDot.setVisibility(View.VISIBLE);
					relImgThreeDot.setText(PreferenceManager.getNoticeEditedBadge(mContext));
					relImgThreeDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getNoticeBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getNoticeEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgThreeDot.setVisibility(View.VISIBLE);
					relImgThreeDot.setText(PreferenceManager.getNoticeBadge(mContext));
					relImgThreeDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgThreeDot.setVisibility(View.GONE);

				}

			}
			else
			{
				relImgThreeDot.setVisibility(View.GONE);
			}
		}
		if (Integer.parseInt(PreferenceManager
				.getButtonFourTextImage(mContext)) != 0) {
			mImgFour.setImageDrawable(AppController.mListImgArrays.getDrawable(Integer
					.parseInt(PreferenceManager
							.getButtonFourTextImage(mContext))));
			String relTwoStr="";
			if (AppController.listitemArrays[Integer
					.parseInt(PreferenceManager
							.getButtonFourTextImage(mContext))].toString().equalsIgnoreCase(CCAS))
			{
				relTwoStr=CCAS;
			}
			else
			{
				relTwoStr=AppController.listitemArrays[Integer
						.parseInt(PreferenceManager
								.getButtonFourTextImage(mContext))].toUpperCase();

			}
			mTxtFour.setText(relTwoStr);
//			mTxtFour.setText(AppController.listitemArrays[Integer
//					.parseInt(PreferenceManager
//							.getButtonFourTextImage(mContext))].toUpperCase());
			mRelFour.setBackgroundColor(PreferenceManager
					.getButtonFourBg(mContext));
			if (PreferenceManager.getButtonFourTabId(mContext).equalsIgnoreCase(TAB_CALENDAR_REG)) {
				if(!(PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgFourDot.setVisibility(View.VISIBLE);
					relImgFourDot.setText(PreferenceManager.getCalendarBadge(mContext));
					relImgFourDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else if((PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgFourDot.setVisibility(View.VISIBLE);

					relImgFourDot.setText(PreferenceManager.getCalendarEditedBadge(mContext));
					relImgFourDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgFourDot.setVisibility(View.VISIBLE);

					relImgFourDot.setText(PreferenceManager.getCalendarBadge(mContext));
					relImgFourDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgFourDot.setVisibility(View.GONE);
				}

			} else if (PreferenceManager.getButtonFourTabId(mContext).equalsIgnoreCase(TAB_NOTIFICATIONS_REG)) {
				if(!(PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgFourDot.setVisibility(View.VISIBLE);
					relImgFourDot.setText(PreferenceManager.getNotificationBadge(mContext));
					relImgFourDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgFourDot.setVisibility(View.VISIBLE);
					relImgFourDot.setText(PreferenceManager.getNotificationEditedBadge(mContext));
					relImgFourDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgFourDot.setVisibility(View.VISIBLE);
					relImgFourDot.setText(PreferenceManager.getNotificationBadge(mContext));
					relImgFourDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgFourDot.setVisibility(View.GONE);

				}
			}
			else if (PreferenceManager.getButtonFourTabId(mContext).equalsIgnoreCase(TAB_TRIPS_REG)) {
				if(!(PreferenceManager.getPaymentitem_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getPaymentitem_edit_badge(mContext).equalsIgnoreCase("0"))))
				{
					relImgFourDot.setVisibility(View.VISIBLE);
					relImgFourDot.setText(PreferenceManager.getPaymentitem_badge(mContext));
					relImgFourDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getPaymentitem_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getPaymentitem_edit_badge(mContext).equalsIgnoreCase("0"))))
				{
					relImgFourDot.setVisibility(View.VISIBLE);
					relImgFourDot.setText(PreferenceManager.getPaymentitem_edit_badge(mContext));
					relImgFourDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getPaymentitem_badge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getPaymentitem_edit_badge(mContext).equalsIgnoreCase("0"))))
				{
					relImgFourDot.setVisibility(View.VISIBLE);
					relImgFourDot.setText(PreferenceManager.getPaymentitem_badge(mContext));
					relImgFourDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgFourDot.setVisibility(View.GONE);

				}
			} else if (PreferenceManager.getButtonFourTabId(mContext).equalsIgnoreCase(TAB_REPORT_REG)) {
				if(!(PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgFourDot.setVisibility(View.VISIBLE);
					relImgFourDot.setText(PreferenceManager.getReportsBadge(mContext));
					relImgFourDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgFourDot.setVisibility(View.VISIBLE);
					relImgFourDot.setText(PreferenceManager.getReportsEditedBadge(mContext));
					relImgFourDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgFourDot.setVisibility(View.VISIBLE);
					relImgFourDot.setText(PreferenceManager.getReportsBadge(mContext));
					relImgFourDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else if((PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgFourDot.setVisibility(View.GONE);

				}
				else {
					relImgFourDot.setVisibility(View.GONE);

				}
			} else if (PreferenceManager.getButtonFourTabId(mContext).equalsIgnoreCase(TAB_CCAS_REG)) {
				if(!(PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgFourDot.setVisibility(View.VISIBLE);
					relImgFourDot.setText(PreferenceManager.getCcaBadge(mContext));
					relImgFourDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgFourDot.setVisibility(View.VISIBLE);
					relImgFourDot.setText(PreferenceManager.getCcaEditedBadge(mContext));
					relImgFourDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgFourDot.setVisibility(View.VISIBLE);
					relImgFourDot.setText(PreferenceManager.getCcaBadge(mContext));
					relImgFourDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgFourDot.setVisibility(View.GONE);

				}
			}

			else if (PreferenceManager.getButtonFourTabId(mContext).equalsIgnoreCase(TAB_SPORTS_REG)) {
				if(!(PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgFourDot.setVisibility(View.VISIBLE);
					relImgFourDot.setText(PreferenceManager.getSportsBadge(mContext));
					relImgFourDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgFourDot.setVisibility(View.VISIBLE);
					relImgFourDot.setText(PreferenceManager.getSportsEditedBadge(mContext));
					relImgFourDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgFourDot.setVisibility(View.VISIBLE);
					relImgFourDot.setText(PreferenceManager.getSportsBadge(mContext));
					relImgFourDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgFourDot.setVisibility(View.GONE);

				}
			}

			else if (PreferenceManager.getButtonFourTabId(mContext).equalsIgnoreCase(TAB_UNIVERSITY_GUIDANCE_REG)) {
				if(!(PreferenceManager.getUniversity_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase("0"))))
				{
					relImgFourDot.setVisibility(View.VISIBLE);
					relImgFourDot.setText(PreferenceManager.getUniversity_badge(mContext));
					relImgFourDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getUniversity_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase("0"))) && (!(PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase(""))))
				{
					relImgFourDot.setVisibility(View.VISIBLE);
					relImgFourDot.setText(PreferenceManager.getUniversity_edit_badge(mContext));
					relImgFourDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getUniversity_badge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase("0"))))
				{
					relImgFourDot.setVisibility(View.VISIBLE);
					relImgFourDot.setText(PreferenceManager.getUniversity_badge(mContext));
					relImgFourDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgFourDot.setVisibility(View.GONE);

				}
			}
			else if (PreferenceManager.getButtonFourTabId(mContext).equalsIgnoreCase(TAB_COMMUNICATIONS_REG)) {
				if(!(PreferenceManager.getNoticeBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNoticeEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgFourDot.setVisibility(View.VISIBLE);
					relImgFourDot.setText(PreferenceManager.getNoticeBadge(mContext));
					relImgFourDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getNoticeBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNoticeEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgFourDot.setVisibility(View.VISIBLE);
					relImgFourDot.setText(PreferenceManager.getNoticeEditedBadge(mContext));
					relImgFourDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getNoticeBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getNoticeEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgFourDot.setVisibility(View.VISIBLE);
					relImgFourDot.setText(PreferenceManager.getNoticeBadge(mContext));
					relImgFourDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgFourDot.setVisibility(View.GONE);

				}

			}

		}
		if (Integer.parseInt(PreferenceManager
				.getButtonFiveTextImage(mContext)) != 0) {
			mImgFive.setImageDrawable(AppController.mListImgArrays.getDrawable(Integer
					.parseInt(PreferenceManager
							.getButtonFiveTextImage(mContext))));
			String relTwoStr="";
			if (AppController.listitemArrays[Integer
					.parseInt(PreferenceManager
							.getButtonFiveTextImage(mContext))].toString().equalsIgnoreCase(CCAS))
			{
				relTwoStr=CCAS;
			}
			else
			{
				relTwoStr=AppController.listitemArrays[Integer
						.parseInt(PreferenceManager
								.getButtonFiveTextImage(mContext))].toUpperCase();

			}
			mTxtFive.setText(relTwoStr);
//			mTxtFive.setText(AppController.listitemArrays[Integer
//					.parseInt(PreferenceManager
//							.getButtonFiveTextImage(mContext))].toUpperCase());
			mRelFive.setBackgroundColor(PreferenceManager
					.getButtonFiveBg(mContext));
			if (PreferenceManager.getButtonFiveTabId(mContext).equalsIgnoreCase(TAB_CALENDAR_REG)) {
				if(!(PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgFiveDot.setVisibility(View.VISIBLE);
					relImgFiveDot.setText(PreferenceManager.getCalendarBadge(mContext));
					relImgFiveDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else if((PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgFiveDot.setVisibility(View.VISIBLE);

					relImgFiveDot.setText(PreferenceManager.getCalendarEditedBadge(mContext));
					relImgFiveDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgFiveDot.setVisibility(View.VISIBLE);

					relImgFiveDot.setText(PreferenceManager.getCalendarBadge(mContext));
					relImgFiveDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgFiveDot.setVisibility(View.GONE);
				}

			} else if (PreferenceManager.getButtonFiveTabId(mContext).equalsIgnoreCase(TAB_NOTIFICATIONS_REG)) {
				if(!(PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgFiveDot.setVisibility(View.VISIBLE);
					relImgFiveDot.setText(PreferenceManager.getNotificationBadge(mContext));
					relImgFiveDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgFiveDot.setVisibility(View.VISIBLE);
					relImgFiveDot.setText(PreferenceManager.getNotificationEditedBadge(mContext));
					relImgFiveDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgFiveDot.setVisibility(View.VISIBLE);
					relImgFiveDot.setText(PreferenceManager.getNotificationBadge(mContext));
					relImgFiveDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgFiveDot.setVisibility(View.GONE);

				}
			} else if (PreferenceManager.getButtonFiveTabId(mContext).equalsIgnoreCase(TAB_REPORT_REG)) {
				if(!(PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgFiveDot.setVisibility(View.VISIBLE);
					relImgFiveDot.setText(PreferenceManager.getReportsBadge(mContext));
					relImgFiveDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgFiveDot.setVisibility(View.VISIBLE);
					relImgFiveDot.setText(PreferenceManager.getReportsEditedBadge(mContext));
					relImgFiveDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgFiveDot.setVisibility(View.VISIBLE);
					relImgFiveDot.setText(PreferenceManager.getReportsBadge(mContext));
					relImgFiveDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgFiveDot.setVisibility(View.GONE);

				}
			}else if (PreferenceManager.getButtonFiveTabId(mContext).equalsIgnoreCase(TAB_TRIPS_REG)) {
				if(!(PreferenceManager.getPaymentitem_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getPaymentitem_edit_badge(mContext).equalsIgnoreCase("0"))))
				{
					relImgFiveDot.setVisibility(View.VISIBLE);
					relImgFiveDot.setText(PreferenceManager.getPaymentitem_badge(mContext));
					relImgFiveDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getPaymentitem_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getPaymentitem_edit_badge(mContext).equalsIgnoreCase("0"))))
				{
					relImgFiveDot.setVisibility(View.VISIBLE);
					relImgFiveDot.setText(PreferenceManager.getPaymentitem_edit_badge(mContext));
					relImgFiveDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getPaymentitem_badge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getPaymentitem_edit_badge(mContext).equalsIgnoreCase("0"))))
				{
					relImgFiveDot.setVisibility(View.VISIBLE);
					relImgFiveDot.setText(PreferenceManager.getPaymentitem_badge(mContext));
					relImgFiveDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgFiveDot.setVisibility(View.GONE);

				}
			}
			else if (PreferenceManager.getButtonFiveTabId(mContext).equalsIgnoreCase(TAB_CCAS_REG)) {
				if(!(PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgFiveDot.setVisibility(View.VISIBLE);
					relImgFiveDot.setText(PreferenceManager.getCcaBadge(mContext));
					relImgFiveDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgFiveDot.setVisibility(View.VISIBLE);
					relImgFiveDot.setText(PreferenceManager.getCcaEditedBadge(mContext));
					relImgFiveDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgFiveDot.setVisibility(View.VISIBLE);
					relImgFiveDot.setText(PreferenceManager.getCcaBadge(mContext));
					relImgFiveDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgFiveDot.setVisibility(View.GONE);

				}
			}
			else if (PreferenceManager.getButtonFiveTabId(mContext).equalsIgnoreCase(TAB_SPORTS_REG)) {
				if(!(PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgFiveDot.setVisibility(View.VISIBLE);
					relImgFiveDot.setText(PreferenceManager.getSportsBadge(mContext));
					relImgFiveDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgFiveDot.setVisibility(View.VISIBLE);
					relImgFiveDot.setText(PreferenceManager.getSportsEditedBadge(mContext));
					relImgFiveDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgFiveDot.setVisibility(View.VISIBLE);
					relImgFiveDot.setText(PreferenceManager.getSportsBadge(mContext));
					relImgFiveDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgFiveDot.setVisibility(View.GONE);

				}
			}else if (PreferenceManager.getButtonFiveTabId(mContext).equalsIgnoreCase(TAB_UNIVERSITY_GUIDANCE_REG)) {
				if(!(PreferenceManager.getUniversity_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase("0"))))
				{
					relImgFiveDot.setVisibility(View.VISIBLE);
					relImgFiveDot.setText(PreferenceManager.getUniversity_badge(mContext));
					relImgFiveDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getUniversity_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase("0"))) && (!(PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase(""))))
				{
					relImgFiveDot.setVisibility(View.VISIBLE);
					relImgFiveDot.setText(PreferenceManager.getUniversity_edit_badge(mContext));
					relImgFiveDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getUniversity_badge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase("0"))))
				{
					relImgFiveDot.setVisibility(View.VISIBLE);
					relImgFiveDot.setText(PreferenceManager.getUniversity_badge(mContext));
					relImgFiveDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgFiveDot.setVisibility(View.GONE);

				}
			}
			else if (PreferenceManager.getButtonFiveTabId(mContext).equalsIgnoreCase(TAB_COMMUNICATIONS_REG)) {
				if(!(PreferenceManager.getNoticeBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNoticeEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgFiveDot.setVisibility(View.VISIBLE);
					relImgFiveDot.setText(PreferenceManager.getNoticeBadge(mContext));
					relImgFiveDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getNoticeBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNoticeEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgFiveDot.setVisibility(View.VISIBLE);
					relImgFiveDot.setText(PreferenceManager.getNoticeEditedBadge(mContext));
					relImgFiveDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getNoticeBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getNoticeEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgFiveDot.setVisibility(View.VISIBLE);
					relImgFiveDot.setText(PreferenceManager.getNoticeBadge(mContext));
					relImgFiveDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgFiveDot.setVisibility(View.GONE);

				}

			}
			else
			{
				relImgFiveDot.setVisibility(View.GONE);
			}
		}
		if (Integer.parseInt(PreferenceManager
				.getButtonSixTextImage(mContext)) != 0) {
			mImgSix.setImageDrawable(AppController.mListImgArrays.getDrawable(Integer
					.parseInt(PreferenceManager
							.getButtonSixTextImage(mContext))));
			String relTwoStr="";
			if (AppController.listitemArrays[Integer
					.parseInt(PreferenceManager
							.getButtonSixTextImage(mContext))].toString().equalsIgnoreCase(CCAS))
			{
				relTwoStr=CCAS;
			}
			else
			{
				relTwoStr=AppController.listitemArrays[Integer
						.parseInt(PreferenceManager
								.getButtonSixTextImage(mContext))].toUpperCase();

			}
			mTxtSix.setText(relTwoStr);
//			mTxtSix.setText(AppController.listitemArrays[Integer
//					.parseInt(PreferenceManager
//							.getButtonSixTextImage(mContext))].toUpperCase());
			mRelSix.setBackgroundColor(PreferenceManager
					.getButtonSixBg(mContext));
			if (PreferenceManager.getButtonSixTabId(mContext).equalsIgnoreCase(TAB_CALENDAR_REG)) {
				if(!(PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgSixDot.setVisibility(View.VISIBLE);
					relImgSixDot.setText(PreferenceManager.getCalendarBadge(mContext));
					relImgSixDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else if((PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgSixDot.setVisibility(View.VISIBLE);

					relImgSixDot.setText(PreferenceManager.getCalendarEditedBadge(mContext));
					relImgSixDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgSixDot.setVisibility(View.VISIBLE);

					relImgSixDot.setText(PreferenceManager.getCalendarBadge(mContext));
					relImgSixDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgSixDot.setVisibility(View.GONE);
				}

			} else if (PreferenceManager.getButtonSixTabId(mContext).equalsIgnoreCase(TAB_NOTIFICATIONS_REG)) {
				if(!(PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgSixDot.setVisibility(View.VISIBLE);
					relImgSixDot.setText(PreferenceManager.getNotificationBadge(mContext));
					relImgSixDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgSixDot.setVisibility(View.VISIBLE);
					relImgSixDot.setText(PreferenceManager.getNotificationEditedBadge(mContext));
					relImgSixDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgSixDot.setVisibility(View.VISIBLE);
					relImgSixDot.setText(PreferenceManager.getNotificationBadge(mContext));
					relImgSixDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgSixDot.setVisibility(View.GONE);

				}
			} else if (PreferenceManager.getButtonSixTabId(mContext).equalsIgnoreCase(TAB_REPORT_REG)) {
				if(!(PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgSixDot.setVisibility(View.VISIBLE);
					relImgSixDot.setText(PreferenceManager.getReportsBadge(mContext));
					relImgSixDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgSixDot.setVisibility(View.VISIBLE);
					relImgSixDot.setText(PreferenceManager.getReportsEditedBadge(mContext));
					relImgSixDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgSixDot.setVisibility(View.VISIBLE);
					relImgSixDot.setText(PreferenceManager.getReportsBadge(mContext));
					relImgSixDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgSixDot.setVisibility(View.GONE);

				}
			} else if (PreferenceManager.getButtonSixTabId(mContext).equalsIgnoreCase(TAB_TRIPS_REG)) {
				if(!(PreferenceManager.getPaymentitem_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getPaymentitem_edit_badge(mContext).equalsIgnoreCase("0"))))
				{
					relImgSixDot.setVisibility(View.VISIBLE);
					relImgSixDot.setText(PreferenceManager.getPaymentitem_badge(mContext));
					relImgSixDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getPaymentitem_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getPaymentitem_edit_badge(mContext).equalsIgnoreCase("0"))))
				{
					relImgSixDot.setVisibility(View.VISIBLE);
					relImgSixDot.setText(PreferenceManager.getPaymentitem_edit_badge(mContext));
					relImgSixDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getPaymentitem_badge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getPaymentitem_edit_badge(mContext).equalsIgnoreCase("0"))))
				{
					relImgSixDot.setVisibility(View.VISIBLE);
					relImgSixDot.setText(PreferenceManager.getPaymentitem_badge(mContext));
					relImgSixDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgSixDot.setVisibility(View.GONE);

				}
			}
			else if (PreferenceManager.getButtonSixTabId(mContext).equalsIgnoreCase(TAB_CCAS_REG)) {
				if(!(PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgSixDot.setVisibility(View.VISIBLE);
					relImgSixDot.setText(PreferenceManager.getCcaBadge(mContext));
					relImgSixDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgSixDot.setVisibility(View.VISIBLE);
					relImgSixDot.setText(PreferenceManager.getCcaEditedBadge(mContext));
					relImgSixDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgSixDot.setVisibility(View.VISIBLE);
					relImgSixDot.setText(PreferenceManager.getCcaBadge(mContext));
					relImgSixDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgSixDot.setVisibility(View.GONE);

				}
			}
			else if (PreferenceManager.getButtonSixTabId(mContext).equalsIgnoreCase(TAB_SPORTS_REG)) {
				if(!(PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgSixDot.setVisibility(View.VISIBLE);
					relImgSixDot.setText(PreferenceManager.getSportsBadge(mContext));
					relImgSixDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgSixDot.setVisibility(View.VISIBLE);
					relImgSixDot.setText(PreferenceManager.getSportsEditedBadge(mContext));
					relImgSixDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgSixDot.setVisibility(View.VISIBLE);
					relImgSixDot.setText(PreferenceManager.getSportsBadge(mContext));
					relImgSixDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgSixDot.setVisibility(View.GONE);

				}
			}else if (PreferenceManager.getButtonSixTabId(mContext).equalsIgnoreCase(TAB_UNIVERSITY_GUIDANCE_REG)) {
				if(!(PreferenceManager.getUniversity_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase("0"))))
				{
					relImgSixDot.setVisibility(View.VISIBLE);
					relImgSixDot.setText(PreferenceManager.getUniversity_badge(mContext));
					relImgSixDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getUniversity_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase("0"))) && (!(PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase(""))))
				{
					relImgSixDot.setVisibility(View.VISIBLE);
					relImgSixDot.setText(PreferenceManager.getUniversity_edit_badge(mContext));
					relImgSixDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getUniversity_badge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase("0"))))
				{
					relImgSixDot.setVisibility(View.VISIBLE);
					relImgSixDot.setText(PreferenceManager.getUniversity_badge(mContext));
					relImgSixDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgSixDot.setVisibility(View.GONE);

				}
			}
			else if (PreferenceManager.getButtonSixTabId(mContext).equalsIgnoreCase(TAB_COMMUNICATIONS_REG)) {
				if(!(PreferenceManager.getNoticeBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNoticeEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgSixDot.setVisibility(View.VISIBLE);
					relImgSixDot.setText(PreferenceManager.getNoticeBadge(mContext));
					relImgSixDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getNoticeBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNoticeEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgSixDot.setVisibility(View.VISIBLE);
					relImgSixDot.setText(PreferenceManager.getNoticeEditedBadge(mContext));
					relImgSixDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getNoticeBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getNoticeEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgSixDot.setVisibility(View.VISIBLE);
					relImgSixDot.setText(PreferenceManager.getNoticeBadge(mContext));
					relImgSixDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgSixDot.setVisibility(View.GONE);

				}

			}
			else
			{
				relImgSixDot.setVisibility(View.GONE);
			}
		}
		if (Integer.parseInt(PreferenceManager
				.getButtonSevenTextImage(mContext)) != 0) {
			mImgSeven.setImageDrawable(AppController.mListImgArrays.getDrawable(Integer
					.parseInt(PreferenceManager
							.getButtonSevenTextImage(mContext))));
			String relTwoStr="";
			if (AppController.listitemArrays[Integer
					.parseInt(PreferenceManager
							.getButtonSevenTextImage(mContext))].toString().equalsIgnoreCase(CCAS))
			{
				relTwoStr=CCAS;
			}
			else
			{
				relTwoStr=AppController.listitemArrays[Integer
						.parseInt(PreferenceManager
								.getButtonSevenTextImage(mContext))].toUpperCase();

			}
			mTxtSeven.setText(relTwoStr);
			System.out.println("Rel seven text"+mTxtSeven.getText());
//			mTxtSeven.setText(AppController.listitemArrays[Integer
//					.parseInt(PreferenceManager
//							.getButtonSevenTextImage(mContext))].toUpperCase());
			mRelSeven.setBackgroundColor(PreferenceManager
					.getButtonSevenBg(mContext));
			if (PreferenceManager.getButtonSevenTabId(mContext).equalsIgnoreCase(TAB_CALENDAR_REG)) {
				if(!(PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgSevenDot.setVisibility(View.VISIBLE);
					relImgSevenDot.setText(PreferenceManager.getCalendarBadge(mContext));
					relImgSevenDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else if((PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgSevenDot.setVisibility(View.VISIBLE);

					relImgSevenDot.setText(PreferenceManager.getCalendarEditedBadge(mContext));
					relImgSevenDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgSevenDot.setVisibility(View.VISIBLE);

					relImgSevenDot.setText(PreferenceManager.getCalendarBadge(mContext));
					relImgSevenDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgSevenDot.setVisibility(View.GONE);
				}

			} else if (PreferenceManager.getButtonSevenTabId(mContext).equalsIgnoreCase(TAB_NOTIFICATIONS_REG)) {
				if(!(PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgSevenDot.setVisibility(View.VISIBLE);
					relImgSevenDot.setText(PreferenceManager.getNotificationBadge(mContext));
					relImgSevenDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgSevenDot.setVisibility(View.VISIBLE);
					relImgSevenDot.setText(PreferenceManager.getNotificationEditedBadge(mContext));
					relImgSevenDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgSevenDot.setVisibility(View.VISIBLE);
					relImgSevenDot.setText(PreferenceManager.getNotificationBadge(mContext));
					relImgSevenDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgSevenDot.setVisibility(View.GONE);

				}
			}
			else if (PreferenceManager.getButtonSevenTabId(mContext).equalsIgnoreCase(TAB_TRIPS_REG)) {
				if(!(PreferenceManager.getPaymentitem_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getPaymentitem_edit_badge(mContext).equalsIgnoreCase("0"))))
				{
					relImgSevenDot.setVisibility(View.VISIBLE);
					relImgSevenDot.setText(PreferenceManager.getPaymentitem_badge(mContext));
					relImgSevenDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getPaymentitem_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getPaymentitem_edit_badge(mContext).equalsIgnoreCase("0"))))
				{
					relImgSevenDot.setVisibility(View.VISIBLE);
					relImgSevenDot.setText(PreferenceManager.getPaymentitem_edit_badge(mContext));
					relImgSevenDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getPaymentitem_badge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getPaymentitem_edit_badge(mContext).equalsIgnoreCase("0"))))
				{
					relImgSevenDot.setVisibility(View.VISIBLE);
					relImgSevenDot.setText(PreferenceManager.getPaymentitem_badge(mContext));
					relImgSevenDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgSevenDot.setVisibility(View.GONE);

				}
			}
			else if (PreferenceManager.getButtonSevenTabId(mContext).equalsIgnoreCase(TAB_REPORT_REG)) {
				if(!(PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgSevenDot.setVisibility(View.VISIBLE);
					relImgSevenDot.setText(PreferenceManager.getReportsBadge(mContext));
					relImgSevenDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgSevenDot.setVisibility(View.VISIBLE);
					relImgSevenDot.setText(PreferenceManager.getReportsEditedBadge(mContext));
					relImgSevenDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgSevenDot.setVisibility(View.VISIBLE);
					relImgSevenDot.setText(PreferenceManager.getReportsBadge(mContext));
					relImgSevenDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgSevenDot.setVisibility(View.GONE);

				}
			} else if (PreferenceManager.getButtonSevenTabId(mContext).equalsIgnoreCase(TAB_CCAS_REG)) {
				if(!(PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgSevenDot.setVisibility(View.VISIBLE);
					relImgSevenDot.setText(PreferenceManager.getCcaBadge(mContext));
					relImgSevenDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgSevenDot.setVisibility(View.VISIBLE);
					relImgSevenDot.setText(PreferenceManager.getCcaEditedBadge(mContext));
					relImgSevenDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgSevenDot.setVisibility(View.VISIBLE);
					relImgSevenDot.setText(PreferenceManager.getCcaBadge(mContext));
					relImgSevenDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgSevenDot.setVisibility(View.GONE);

				}
			}
			else if (PreferenceManager.getButtonSevenTabId(mContext).equalsIgnoreCase(TAB_SPORTS_REG)) {
				if(!(PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgSevenDot.setVisibility(View.VISIBLE);
					relImgSevenDot.setText(PreferenceManager.getSportsBadge(mContext));
					relImgSevenDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgSevenDot.setVisibility(View.VISIBLE);
					relImgSevenDot.setText(PreferenceManager.getSportsEditedBadge(mContext));
					relImgSevenDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgSevenDot.setVisibility(View.VISIBLE);
					relImgSevenDot.setText(PreferenceManager.getSportsBadge(mContext));
					relImgSevenDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgSevenDot.setVisibility(View.GONE);

				}
			}else if (PreferenceManager.getButtonSevenTabId(mContext).equalsIgnoreCase(TAB_UNIVERSITY_GUIDANCE_REG)) {
				if(!(PreferenceManager.getUniversity_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase("0"))))
				{
					relImgSevenDot.setVisibility(View.VISIBLE);
					relImgSevenDot.setText(PreferenceManager.getUniversity_badge(mContext));
					relImgSevenDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getUniversity_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase("0"))) && (!(PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase(""))))
				{
					relImgSevenDot.setVisibility(View.VISIBLE);
					relImgSevenDot.setText(PreferenceManager.getUniversity_edit_badge(mContext));
					relImgSevenDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getUniversity_badge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase("0"))))
				{
					relImgSevenDot.setVisibility(View.VISIBLE);
					relImgSevenDot.setText(PreferenceManager.getUniversity_badge(mContext));
					relImgSevenDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgSevenDot.setVisibility(View.GONE);

				}
			}
			else if (PreferenceManager.getButtonSevenTabId(mContext).equalsIgnoreCase(TAB_COMMUNICATIONS_REG)) {
				if(!(PreferenceManager.getNoticeBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNoticeEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgSevenDot.setVisibility(View.VISIBLE);
					relImgSevenDot.setText(PreferenceManager.getNoticeBadge(mContext));
					relImgSevenDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getNoticeBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNoticeEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgSevenDot.setVisibility(View.VISIBLE);
					relImgSevenDot.setText(PreferenceManager.getNoticeEditedBadge(mContext));
					relImgSevenDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getNoticeBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getNoticeEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgSevenDot.setVisibility(View.VISIBLE);
					relImgSevenDot.setText(PreferenceManager.getNoticeBadge(mContext));
					relImgSevenDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgSevenDot.setVisibility(View.GONE);

				}

			}
			else
			{
				relImgSevenDot.setVisibility(View.GONE);
			}
		}
		if (Integer.parseInt(PreferenceManager
				.getButtonEightTextImage(mContext)) != 0) {
			mImgEight.setImageDrawable(AppController.mListImgArrays.getDrawable(Integer
					.parseInt(PreferenceManager
							.getButtonEightTextImage(mContext))));
			String relTwoStr="";
			if (AppController.listitemArrays[Integer
					.parseInt(PreferenceManager
							.getButtonEightTextImage(mContext))].toString().equalsIgnoreCase(CCAS))
			{
				relTwoStr=CCAS;
			}
			else
			{
				relTwoStr=AppController.listitemArrays[Integer
						.parseInt(PreferenceManager
								.getButtonEightTextImage(mContext))].toUpperCase();

			}
			mTxtEight.setText(relTwoStr);
//			mTxtEight.setText(AppController.listitemArrays[Integer
//					.parseInt(PreferenceManager
//							.getButtonEightTextImage(mContext))].toUpperCase());
			mRelEight.setBackgroundColor(PreferenceManager
					.getButtonEightBg(mContext));
			if (PreferenceManager.getButtonEightTabId(mContext).equalsIgnoreCase(TAB_CALENDAR_REG)) {
				if(!(PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgEightDot.setVisibility(View.VISIBLE);
					relImgEightDot.setText(PreferenceManager.getCalendarBadge(mContext));
					relImgEightDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else if((PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgEightDot.setVisibility(View.VISIBLE);

					relImgEightDot.setText(PreferenceManager.getCalendarEditedBadge(mContext));
					relImgEightDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgEightDot.setVisibility(View.VISIBLE);

					relImgEightDot.setText(PreferenceManager.getCalendarBadge(mContext));
					relImgEightDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgEightDot.setVisibility(View.GONE);
				}

			} else if (PreferenceManager.getButtonEightTabId(mContext).equalsIgnoreCase(TAB_NOTIFICATIONS_REG)) {
				if(!(PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgEightDot.setVisibility(View.VISIBLE);
					relImgEightDot.setText(PreferenceManager.getNotificationBadge(mContext));
					relImgEightDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgEightDot.setVisibility(View.VISIBLE);
					relImgEightDot.setText(PreferenceManager.getNotificationEditedBadge(mContext));
					relImgEightDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgEightDot.setVisibility(View.VISIBLE);
					relImgEightDot.setText(PreferenceManager.getNotificationBadge(mContext));
					relImgEightDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgEightDot.setVisibility(View.GONE);

				}
			}else if (PreferenceManager.getButtonEightTabId(mContext).equalsIgnoreCase(TAB_TRIPS_REG)) {
				if(!(PreferenceManager.getPaymentitem_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getPaymentitem_edit_badge(mContext).equalsIgnoreCase("0"))))
				{
					relImgEightDot.setVisibility(View.VISIBLE);
					relImgEightDot.setText(PreferenceManager.getPaymentitem_badge(mContext));
					relImgEightDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getPaymentitem_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getPaymentitem_edit_badge(mContext).equalsIgnoreCase("0"))))
				{
					relImgEightDot.setVisibility(View.VISIBLE);
					relImgEightDot.setText(PreferenceManager.getPaymentitem_edit_badge(mContext));
					relImgEightDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getPaymentitem_badge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getPaymentitem_edit_badge(mContext).equalsIgnoreCase("0"))))
				{
					relImgEightDot.setVisibility(View.VISIBLE);
					relImgEightDot.setText(PreferenceManager.getPaymentitem_badge(mContext));
					relImgEightDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgEightDot.setVisibility(View.GONE);

				}
			}
			else if (PreferenceManager.getButtonEightTabId(mContext).equalsIgnoreCase(TAB_REPORT_REG)) {
				if(!(PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgEightDot.setVisibility(View.VISIBLE);
					relImgEightDot.setText(PreferenceManager.getReportsBadge(mContext));
					relImgEightDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgEightDot.setVisibility(View.VISIBLE);
					relImgEightDot.setText(PreferenceManager.getReportsEditedBadge(mContext));
					relImgEightDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgEightDot.setVisibility(View.VISIBLE);
					relImgEightDot.setText(PreferenceManager.getReportsBadge(mContext));
					relImgEightDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else if((PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgEightDot.setVisibility(View.GONE);

				}
				else {
					relImgEightDot.setVisibility(View.GONE);

				}
			} else if (PreferenceManager.getButtonEightTabId(mContext).equalsIgnoreCase(TAB_CCAS_REG)) {
				if(!(PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgEightDot.setVisibility(View.VISIBLE);
					relImgEightDot.setText(PreferenceManager.getCcaBadge(mContext));
					relImgEightDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgEightDot.setVisibility(View.VISIBLE);
					relImgEightDot.setText(PreferenceManager.getCcaEditedBadge(mContext));
					relImgEightDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgEightDot.setVisibility(View.VISIBLE);
					relImgEightDot.setText(PreferenceManager.getCcaBadge(mContext));
					relImgEightDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgEightDot.setVisibility(View.GONE);

				}
			}
			else if (PreferenceManager.getButtonEightTabId(mContext).equalsIgnoreCase(TAB_SPORTS_REG)) {
				if(!(PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgEightDot.setVisibility(View.VISIBLE);
					relImgEightDot.setText(PreferenceManager.getSportsBadge(mContext));
					relImgEightDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgEightDot.setVisibility(View.VISIBLE);
					relImgEightDot.setText(PreferenceManager.getSportsEditedBadge(mContext));
					relImgEightDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgEightDot.setVisibility(View.VISIBLE);
					relImgEightDot.setText(PreferenceManager.getSportsBadge(mContext));
					relImgEightDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgEightDot.setVisibility(View.GONE);

				}
			}else if (PreferenceManager.getButtonEightTabId(mContext).equalsIgnoreCase(TAB_UNIVERSITY_GUIDANCE_REG)) {
				if(!(PreferenceManager.getUniversity_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase("0"))))
				{
					relImgEightDot.setVisibility(View.VISIBLE);
					relImgEightDot.setText(PreferenceManager.getUniversity_badge(mContext));
					relImgEightDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getUniversity_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase("0"))) && (!(PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase(""))))
				{
					relImgEightDot.setVisibility(View.VISIBLE);
					relImgEightDot.setText(PreferenceManager.getUniversity_edit_badge(mContext));
					relImgEightDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getUniversity_badge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase("0"))))
				{
					relImgEightDot.setVisibility(View.VISIBLE);
					relImgEightDot.setText(PreferenceManager.getUniversity_badge(mContext));
					relImgEightDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgEightDot.setVisibility(View.GONE);

				}
			}
			else if (PreferenceManager.getButtonEightTabId(mContext).equalsIgnoreCase(TAB_COMMUNICATIONS_REG)) {
				if(!(PreferenceManager.getNoticeBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNoticeEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgEightDot.setVisibility(View.VISIBLE);
					relImgEightDot.setText(PreferenceManager.getNoticeBadge(mContext));
					relImgEightDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getNoticeBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNoticeEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgEightDot.setVisibility(View.VISIBLE);
					relImgEightDot.setText(PreferenceManager.getNoticeEditedBadge(mContext));
					relImgEightDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getNoticeBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getNoticeEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgEightDot.setVisibility(View.VISIBLE);
					relImgEightDot.setText(PreferenceManager.getNoticeBadge(mContext));
					relImgEightDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgEightDot.setVisibility(View.GONE);

				}

			}
			else
			{
				relImgEightDot.setVisibility(View.GONE);
			}
		}
		if (Integer.parseInt(PreferenceManager
				.getButtonNineTextImage(mContext)) != 0) {
			mImgNine.setImageDrawable(AppController.mListImgArrays.getDrawable(Integer
					.parseInt(PreferenceManager
							.getButtonNineTextImage(mContext))));
			String relTwoStr="";
			if (AppController.listitemArrays[Integer
					.parseInt(PreferenceManager
							.getButtonNineTextImage(mContext))].toString().equalsIgnoreCase(CCAS))
			{
				relTwoStr=CCAS;
			}
			else
			{
				relTwoStr=AppController.listitemArrays[Integer
						.parseInt(PreferenceManager
								.getButtonNineTextImage(mContext))].toUpperCase();

			}
			mTxtNine.setText(relTwoStr);
//			mTxtNine.setText(AppController.listitemArrays[Integer
//					.parseInt(PreferenceManager
//							.getButtonNineTextImage(mContext))].toUpperCase());
			mRelNine.setBackgroundColor(PreferenceManager
					.getButtonNineBg(mContext));
			if (PreferenceManager.getButtonNineTabId(mContext).equalsIgnoreCase(TAB_CALENDAR_REG)) {
				if(!(PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgNineDot.setVisibility(View.VISIBLE);
					relImgNineDot.setText(PreferenceManager.getCalendarBadge(mContext));
					relImgNineDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else if((PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgNineDot.setVisibility(View.VISIBLE);

					relImgNineDot.setText(PreferenceManager.getCalendarEditedBadge(mContext));
					relImgNineDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgNineDot.setVisibility(View.VISIBLE);

					relImgNineDot.setText(PreferenceManager.getCalendarBadge(mContext));
					relImgNineDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgNineDot.setVisibility(View.GONE);
				}

			} else if (PreferenceManager.getButtonNineTabId(mContext).equalsIgnoreCase(TAB_NOTIFICATIONS_REG)) {
				if(!(PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgNineDot.setVisibility(View.VISIBLE);
					relImgNineDot.setText(PreferenceManager.getNotificationBadge(mContext));
					relImgNineDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgNineDot.setVisibility(View.VISIBLE);
					relImgNineDot.setText(PreferenceManager.getNotificationEditedBadge(mContext));
					relImgNineDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgNineDot.setVisibility(View.VISIBLE);
					relImgNineDot.setText(PreferenceManager.getNotificationBadge(mContext));
					relImgNineDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgNineDot.setVisibility(View.GONE);

				}
			}
			else if (PreferenceManager.getButtonNineTabId(mContext).equalsIgnoreCase(TAB_TRIPS_REG)) {
				if(!(PreferenceManager.getPaymentitem_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getPaymentitem_edit_badge(mContext).equalsIgnoreCase("0"))))
				{
					relImgNineDot.setVisibility(View.VISIBLE);
					relImgNineDot.setText(PreferenceManager.getPaymentitem_badge(mContext));
					relImgNineDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getPaymentitem_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getPaymentitem_edit_badge(mContext).equalsIgnoreCase("0"))))
				{
					relImgNineDot.setVisibility(View.VISIBLE);
					relImgNineDot.setText(PreferenceManager.getPaymentitem_edit_badge(mContext));
					relImgNineDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getPaymentitem_badge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getPaymentitem_edit_badge(mContext).equalsIgnoreCase("0"))))
				{
					relImgNineDot.setVisibility(View.VISIBLE);
					relImgNineDot.setText(PreferenceManager.getPaymentitem_badge(mContext));
					relImgNineDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgNineDot.setVisibility(View.GONE);

				}
			}
			else if (PreferenceManager.getButtonNineTabId(mContext).equalsIgnoreCase(TAB_REPORT_REG)) {
				if(!(PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgNineDot.setVisibility(View.VISIBLE);
					relImgNineDot.setText(PreferenceManager.getReportsBadge(mContext));
					relImgNineDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgNineDot.setVisibility(View.VISIBLE);
					relImgNineDot.setText(PreferenceManager.getReportsEditedBadge(mContext));
					relImgNineDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgNineDot.setVisibility(View.VISIBLE);
					relImgNineDot.setText(PreferenceManager.getReportsBadge(mContext));
					relImgNineDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgNineDot.setVisibility(View.GONE);

				}
			} else if (PreferenceManager.getButtonNineTabId(mContext).equalsIgnoreCase(TAB_CCAS_REG)) {
				if(!(PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgNineDot.setVisibility(View.VISIBLE);
					relImgNineDot.setText(PreferenceManager.getCcaBadge(mContext));
					relImgNineDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgNineDot.setVisibility(View.VISIBLE);
					relImgNineDot.setText(PreferenceManager.getCcaEditedBadge(mContext));
					relImgNineDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgNineDot.setVisibility(View.VISIBLE);
					relImgNineDot.setText(PreferenceManager.getCcaBadge(mContext));
					relImgNineDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgNineDot.setVisibility(View.GONE);

				}
			}
			else if (PreferenceManager.getButtonNineTabId(mContext).equalsIgnoreCase(TAB_SPORTS_REG)) {
				if(!(PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgNineDot.setVisibility(View.VISIBLE);
					relImgNineDot.setText(PreferenceManager.getSportsBadge(mContext));
					relImgNineDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgNineDot.setVisibility(View.VISIBLE);
					relImgNineDot.setText(PreferenceManager.getSportsEditedBadge(mContext));
					relImgNineDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgNineDot.setVisibility(View.VISIBLE);
					relImgNineDot.setText(PreferenceManager.getSportsBadge(mContext));
					relImgNineDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgNineDot.setVisibility(View.GONE);

				}
			}else if (PreferenceManager.getButtonNineTabId(mContext).equalsIgnoreCase(TAB_UNIVERSITY_GUIDANCE_REG)) {
				if(!(PreferenceManager.getUniversity_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase("0"))))
				{
					relImgNineDot.setVisibility(View.VISIBLE);
					relImgNineDot.setText(PreferenceManager.getUniversity_badge(mContext));
					relImgNineDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getUniversity_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase("0"))))
				{
					relImgNineDot.setVisibility(View.VISIBLE);
					relImgNineDot.setText(PreferenceManager.getUniversity_edit_badge(mContext));
					relImgNineDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getUniversity_badge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase("0"))))
				{
					relImgNineDot.setVisibility(View.VISIBLE);
					relImgNineDot.setText(PreferenceManager.getUniversity_badge(mContext));
					relImgNineDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgNineDot.setVisibility(View.GONE);

				}
			}
			else if (PreferenceManager.getButtonNineTabId(mContext).equalsIgnoreCase(TAB_COMMUNICATIONS_REG)) {
				if(!(PreferenceManager.getNoticeBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNoticeEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgNineDot.setVisibility(View.VISIBLE);
					relImgNineDot.setText(PreferenceManager.getNoticeBadge(mContext));
					relImgNineDot.setBackgroundResource(R.drawable.shape_circle_red);
				}
				else if((PreferenceManager.getNoticeBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNoticeEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgNineDot.setVisibility(View.VISIBLE);
					relImgNineDot.setText(PreferenceManager.getNoticeEditedBadge(mContext));
					relImgNineDot.setBackgroundResource(R.drawable.shape_circle_navy);

				}
				else if(!(PreferenceManager.getNoticeBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getNoticeEditedBadge(mContext).equalsIgnoreCase("0"))))
				{
					relImgNineDot.setVisibility(View.VISIBLE);
					relImgNineDot.setText(PreferenceManager.getNoticeBadge(mContext));
					relImgNineDot.setBackgroundResource(R.drawable.shape_circle_red);

				}
				else {
					relImgNineDot.setVisibility(View.GONE);

				}

			}
			else
			{
				relImgNineDot.setVisibility(View.GONE);
			}
		}
	}

	/********************************************************
	 * Method name : initialiseUI Description : initialise UI elements
	 * Parameters : nil Return type : void Date : Oct 31, 2014 Author : Vandana
	 * Surendranath
	 *****************************************************/

	private void initialiseUI() {
//		mBannerImg = (ImageView) mRootView.findViewById(R.id.bannerImage);
		bannerImagePager = (ViewPager) mRootView.findViewById(R.id.bannerImagePager);
		mRelOne = (RelativeLayout) mRootView.findViewById(R.id.relOne);
		mRelTwo = (RelativeLayout) mRootView.findViewById(R.id.relTwo);
		mRelThree = (RelativeLayout) mRootView.findViewById(R.id.relThree);
		mRelFour = (RelativeLayout) mRootView.findViewById(R.id.relFour);
		mRelFive = (RelativeLayout) mRootView.findViewById(R.id.relFive);
		mRelSix = (RelativeLayout) mRootView.findViewById(R.id.relSix);
		mRelSeven = (RelativeLayout) mRootView.findViewById(R.id.relSeven);
		mRelEight = (RelativeLayout) mRootView.findViewById(R.id.relEight);
		mRelNine = (RelativeLayout) mRootView.findViewById(R.id.relNine);

		mTxtOne = (TextView) mRootView.findViewById(R.id.relTxtOne);
		mImgOne = (ImageView) mRootView.findViewById(R.id.relImgOne);
		mTxtTwo = (TextView) mRootView.findViewById(R.id.relTxtTwo);
		mImgTwo = (ImageView) mRootView.findViewById(R.id.relImgTwo);
		mTxtThree = (TextView) mRootView.findViewById(R.id.relTxtThree);
		mImgThree = (ImageView) mRootView.findViewById(R.id.relImgThree);
		mTxtFour = (TextView) mRootView.findViewById(R.id.relTxtFour);
		mImgFour = (ImageView) mRootView.findViewById(R.id.relImgFour);
		mTxtFive = (TextView) mRootView.findViewById(R.id.relTxtFive);
		mImgFive = (ImageView) mRootView.findViewById(R.id.relImgFive);
		mTxtSix = (TextView) mRootView.findViewById(R.id.relTxtSix);
		mImgSix = (ImageView) mRootView.findViewById(R.id.relImgSix);
		mTxtSeven = (TextView) mRootView.findViewById(R.id.relTxtSeven);
		mImgSeven = (ImageView) mRootView.findViewById(R.id.relImgSeven);
		mTxtEight = (TextView) mRootView.findViewById(R.id.relTxtEight);
		mImgEight = (ImageView) mRootView.findViewById(R.id.relImgEight);
		mTxtNine = (TextView) mRootView.findViewById(R.id.relTxtNine);
		mImgNine = (ImageView) mRootView.findViewById(R.id.relImgNine);

		/**************Badge Setting aparna 05Dec2018**************/

		relImgOneDot = (TextView) mRootView.findViewById(R.id.relImgOneDot);
		relImgTwoDot = (TextView) mRootView.findViewById(R.id.relImgTwoDot);
		relImgThreeDot = (TextView) mRootView.findViewById(R.id.relImgThreeDot);
		relImgFourDot = (TextView) mRootView.findViewById(R.id.relImgFourDot);
		relImgFiveDot = (TextView) mRootView.findViewById(R.id.relImgFiveDot);
		relImgSixDot = (TextView) mRootView.findViewById(R.id.relImgSixDot);
		relImgSevenDot = (TextView) mRootView.findViewById(R.id.relImgSevenDot);
		relImgEightDot = (TextView) mRootView.findViewById(R.id.relImgEightDot);
		relImgNineDot = (TextView) mRootView.findViewById(R.id.relImgNineDot);

		if (AppUtils.checkInternet(mContext)) {
			getStudentsListAPI(URL_GET_STUDENT_LIST);
			getBanner();
			getBadge();
		}
		else
		{
			Toast.makeText(mContext,"Network Error",Toast.LENGTH_SHORT).show();

		}
//		homeBannerUrlImageArray=new ArrayList<>();
//		homeBannerUrlImageArray.add("http://mobicare2.mobatia.com/nais/media/images/4.png");
//		homeBannerUrlImageArray.add("http://mobicare2.mobatia.com/nais/media/images/3.png");
//		homeBannerUrlImageArray.add("http://mobicare2.mobatia.com/nais/media/images/2.png");
//		homeBannerUrlImageArray.add("http://mobicare2.mobatia.com/nais/media/images/1.png");
//		bannerImagePager.setAdapter(new ImagePagerDrawableAdapter(homeBannerUrlImageArray,getActivity()));

		if (PreferenceManager.getArrayList("home_banner",mContext) != null) {

			final Handler handler = new Handler();
			final Runnable Update = new Runnable() {
				public void run() {
					if (currentPage == PreferenceManager.getArrayList("home_banner",mContext).size()) {
						currentPage = 0;
						bannerImagePager.setCurrentItem(currentPage,
								false);
					}
					else {
						bannerImagePager
								.setCurrentItem(currentPage++, true);
					}

				}
			};
			final Timer swipeTimer = new Timer();
			swipeTimer.schedule(new TimerTask() {
				@Override
				public void run() {
					handler.post(Update);
				}
			}, 100, 3000);
		}

		mSectionText = new String[9];
		if (homeBannerUrlImageArray.size() > 0) {
			bannerImagePager.setAdapter(new ImagePagerDrawableAdapter(homeBannerUrlImageArray, getActivity()));


		} else {
			//CustomStatusDialog();
//											bannerImagePager.setBackgroundResource(R.drawable.banner);
			bannerImagePager.setBackgroundResource(R.drawable.default_banner);
//											Toast.makeText(mContext, "Failure", Toast.LENGTH_SHORT).show();
		}
		/** change home list width */
		// int width = mContext.getResources().getDisplayMetrics().widthPixels /
		// 5;
		// DrawerLayout.LayoutParams params =
		// (android.support.v4.widget.DrawerLayout.LayoutParams) AppController.mListViews
		// .getLayoutParams();
		// params.width = width;
		// AppController.mListViews.setLayoutParams(params);
	}

	/*******************************************************
	 * Method name : setListeners Description : set listeners for UI elements
	 * Parameters : nil Return type : void Date : Oct 31, 2014 Author : Vandana
	 * Surendranath
	 *****************************************************/

	private void setListeners() {
//		mBannerImg.setOnClickListener(this);
		mRelOne.setOnClickListener(this);
		mRelTwo.setOnClickListener(this);
		mRelThree.setOnClickListener(this);
		mRelFour.setOnClickListener(this);
		mRelFive.setOnClickListener(this);
		mRelSix.setOnClickListener(this);
		mRelSeven.setOnClickListener(this);
		mRelEight.setOnClickListener(this);
		mRelNine.setOnClickListener(this);
	}

	@SuppressLint("NewApi")
	private class DropListener implements OnDragListener {
		/*
		 * (non-Javadoc)
		 *
		 * @see android.view.View.OnDragListener#onDrag(android.view.View,
		 * android.view.DragEvent)
		 */
		@Override
		public boolean onDrag(View view, DragEvent event) {
			PreferenceManager.setIfHomeItemClickEnabled(mContext, true);
			switch (event.getAction()) {
				case DragEvent.ACTION_DRAG_STARTED:
					break;
				case DragEvent.ACTION_DRAG_ENTERED:
					break;
				case DragEvent.ACTION_DRAG_EXITED:
					break;
				case DragEvent.ACTION_DROP:
//				AppController.mDrawerLayouts.closeDrawer(AppController.mListViews);
					AppController.mDrawerLayouts.closeDrawer(AppController.mLinearLayouts);
					int arr[] = new int[2];
					view.getLocationInWindow(arr);
					int x = arr[0];
					int y = arr[1];
					getButtonViewTouched(x, y);
					mSectionText[0] = mTxtOne.getText().toString().toUpperCase();
					mSectionText[1] = mTxtTwo.getText().toString().toUpperCase();
					mSectionText[2] = mTxtThree.getText().toString().toUpperCase();
					mSectionText[3] = mTxtFour.getText().toString().toUpperCase();
					mSectionText[4] = mTxtFive.getText().toString().toUpperCase();
					mSectionText[5] = mTxtSix.getText().toString().toUpperCase();
					mSectionText[6] = mTxtSeven.getText().toString().toUpperCase();
					mSectionText[7] = mTxtEight.getText().toString().toUpperCase();
					mSectionText[8] = mTxtNine.getText().toString().toUpperCase();
					for (int i = 0; i < mSectionText.length; i++) {
						isDraggable = true;
						if (mSectionText[i]
								.equalsIgnoreCase(AppController.listitemArrays[HomeListAppCompatActivity.sPosition])) {
							isDraggable = false;
							break;
						}
					}
					if (isDraggable) {
						getButtonDrawablesAndText(viewTouched,
								HomeListAppCompatActivity.sPosition);
					}
					else {
						AppUtils.showAlert((Activity) mContext, mContext
										.getResources().getString(R.string.drag_duplicate),
								"",
								mContext.getResources().getString(R.string.ok),
								false);
					}
					break;
				case DragEvent.ACTION_DRAG_ENDED:
					break;
				default:
					break;
			}
			return true;

		}
	}

	/*******************************************************
	 * Method name : getButtonViewTouched Description : get which button to set
	 * dragged image Parameters : centerX, centerY Return type : void Date : Nov
	 * 12, 2014 Author : Rijo K Jose
	 *****************************************************/
	private void getButtonViewTouched(float centerX, float centerY) {
		// button one
		int arr1[] = new int[2];
		mRelOne.getLocationInWindow(arr1);
		int x1 = arr1[0];
		int x2 = x1 + mRelOne.getWidth();
		int y1 = arr1[1];
		int y2 = y1 + mRelOne.getHeight();

		// button two
		int arr2[] = new int[2];
		mRelTwo.getLocationInWindow(arr2);
		int x3 = arr2[0];
		int x4 = x3 + mRelTwo.getWidth();
		int y3 = arr2[1];
		int y4 = y3 + mRelTwo.getHeight();

		// button three
		int arr3[] = new int[2];
		mRelThree.getLocationInWindow(arr3);
		int x5 = arr3[0];
		int x6 = x5 + mRelThree.getWidth();
		int y5 = arr3[1];
		int y6 = y5 + mRelFour.getHeight();

		// button four
		int arr4[] = new int[2];
		mRelFour.getLocationInWindow(arr4);
		int x7 = arr4[0];
		int x8 = x7 + mRelFour.getWidth();
		int y7 = arr4[1];
		int y8 = y7 + mRelFour.getHeight();

		// button five
		int arr5[] = new int[2];
		mRelFive.getLocationInWindow(arr5);
		int x9 = arr5[0];
		int x10 = x9 + mRelFive.getWidth();
		int y9 = arr5[1];
		int y10 = y9 + mRelFive.getHeight();

		// button six
		int arr6[] = new int[2];
		mRelSix.getLocationInWindow(arr6);
		int x11 = arr6[0];
		int x12 = x11 + mRelSix.getWidth();
		int y11 = arr6[1];
		int y12 = y11 + mRelSix.getHeight();

		// button seven
		int arr7[] = new int[2];
		mRelSeven.getLocationInWindow(arr7);
		int x13 = arr7[0];
		int x14 = x13 + mRelSeven.getWidth();
		int y13 = arr7[1];
		int y14 = y13 + mRelSeven.getHeight();

		// button eight
		int arr8[] = new int[2];
		mRelEight.getLocationInWindow(arr8);
		int x15 = arr8[0];
		int x16 = x15 + mRelEight.getWidth();
		int y15 = arr8[1];
		int y16 = y15 + mRelEight.getHeight();

		// button nine
		int arr9[] = new int[2];
		mRelNine.getLocationInWindow(arr9);
		int x17 = arr9[0];
		int x18 = x17 + mRelNine.getWidth();
		int y17 = arr9[1];
		int y18 = y17 + mRelNine.getHeight();

		if (x1 <= centerX && centerX <= x2 && y1 <= centerY && centerY <= y2) {
			viewTouched = mRelOne;
		} else if (x3 <= centerX && centerX <= x4 && y3 <= centerY
				&& centerY <= y4) {
			viewTouched = mRelTwo;
		} else if (x5 <= centerX && centerX <= x6 && y5 <= centerY
				&& centerY <= y6) {
			viewTouched = mRelThree;
		} else if (x7 <= centerX && centerX <= x8 && y7 <= centerY
				&& centerY <= y8) {
			viewTouched = mRelFour;
		} else if (x9 <= centerX && centerX <= x10 && y9 <= centerY
				&& centerY <= y10) {
			viewTouched = mRelFive;
		} else if (x11 <= centerX && centerX <= x12 && y11 <= centerY
				&& centerY <= y12) {
			viewTouched = mRelSix;
		} else if (x13 <= centerX && centerX <= x14 && y13 <= centerY
				&& centerY <= y14) {
			viewTouched = mRelSeven;
		} else if (x15 <= centerX && centerX <= x16 && y15 <= centerY
				&& centerY <= y16) {
			viewTouched = mRelEight;
		} else if (x17 <= centerX && centerX <= x18 && y17 <= centerY
				&& centerY <= y18) {
			viewTouched = mRelNine;
		} else {
			viewTouched = null;
		}
	}

	/*******************************************************
	 * Method name : getTabId Description : get tab id for home button click
	 * Parameters : text Return type : void Date : Nov 26, 2014 Author : Vandana
	 * Surendranath
	 *****************************************************/
	private void getTabId(String text) {
		System.out.println("text wiss " + text);
		if (text.equalsIgnoreCase(ABOUT_US)) {
			TAB_ID = TAB_ABOUT_US_REG;
		} else if (text.equalsIgnoreCase(NOTIFICATIONS)) {
			TAB_ID = TAB_NOTIFICATIONS_REG;
		} else if (text.equalsIgnoreCase(COMMUNICATIONS)) {
			TAB_ID = TAB_COMMUNICATIONS_REG;
		} else if (text.equalsIgnoreCase(CALENDAR)) {
			TAB_ID = TAB_CALENDAR_REG;
		} else if (text.equalsIgnoreCase(CONTACT_US)) {
			TAB_ID = TAB_CONTACT_US_REG;
		} else if (text.equalsIgnoreCase(PARENT_ESSENTIALS)) {
			TAB_ID = TAB_PARENT_ESSENTIALS_REG;
		} else if (text.equalsIgnoreCase(EARLY_YEARS)) {
			TAB_ID = TAB_EARLYYEARS_REG;
		} else if (text.equalsIgnoreCase(SPORTS)) {
			TAB_ID = TAB_SPORTS_REG;
		} else if (text.equalsIgnoreCase(IB_PROGRAMME)) {
			TAB_ID = TAB_IB_PROGRAMME_REG;
		} else if (text.equalsIgnoreCase(PERFORMING_ARTS)) {
			TAB_ID = TAB_PERFORMING_ARTS_REG;
		} else if (text.equalsIgnoreCase(GALLERY)) {
			TAB_ID = TAB_GALLERY_REG;
		} else if (text.equalsIgnoreCase(CCAS)) {
			TAB_ID = TAB_CCAS_REG;
		} else if (text.equalsIgnoreCase(NAE_PROGRAMMES)) {
			TAB_ID = TAB_NAE_PROGRAMMES_REG;
		} else if (text.equalsIgnoreCase(PRIMARY)) {
			TAB_ID = TAB_PRIMARY_REG;
		}
		else if (text.equalsIgnoreCase(SECONDARY)) {
			TAB_ID = TAB_SECONDARY_REG;
		}
		else if (text.equalsIgnoreCase(UNIVERSITY_GUIDANCE)) {
			TAB_ID = TAB_UNIVERSITY_GUIDANCE_REG;
		}
		/*else if (text.equalsIgnoreCase(SOCIAL_MEDIA)) {
			TAB_ID = TAB_SOCIAL_MEDIA_REG;
		} else if (text.equalsIgnoreCase(WISSUP)) {
			TAB_ID = TAB_WISSUP;
		}*/
		/*else if (text.equalsIgnoreCase(NAS_TODAY)) {
			TAB_ID = TAB_NAS_TODAY_REG;
		}*/
		else if (text.equalsIgnoreCase(PARENT_EVENING)) {
			TAB_ID = TAB_PARENTS_MEETING_REG;
		}	else if (text.equalsIgnoreCase(ABSENCE)) {
			TAB_ID = TAB_ABSENSE_REG;
		}	else if (text.equalsIgnoreCase(PARENTS_ASSOCIATION)) {
			TAB_ID = TAB_PARENTS_ASSOCIATION_REG;
		}else if(text.equalsIgnoreCase("Reports"))
		{
			TAB_ID = TAB_REPORT_REG;

		}else if(text.equalsIgnoreCase("Permission Forms"))
		{
			TAB_ID = TAB_PERMISSION_SLIP_REG;

		}
		else if(text.equalsIgnoreCase(TRIPS))
		{
			TAB_ID = TAB_TRIPS_REG;

		}
		else if(text.equalsIgnoreCase(CANTEEN))
		{
			TAB_ID = TAB_CANTEEN_REG;

		}

	}

	/*******************************************************
	 * Method name : getButtonDrawablesAndText Description : get button image,
	 * text and background Parameters : view Return type : void Date : Nov 6,
	 * 2014 Author : Rijo K Jose
	 *****************************************************/

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	private void getButtonDrawablesAndText(View v, int position) {
		if (position != 0) {
			if (v == mRelOne) {
				mImgOne.setImageDrawable(AppController.mListImgArrays.getDrawable(position));
				String relTwoStr="";
				if (AppController.listitemArrays[position].toString().equalsIgnoreCase(CCAS))
				{
					relTwoStr=CCAS;
				}
				else
				{
					relTwoStr=AppController.listitemArrays[position].toUpperCase();

				}
				mTxtOne.setText(relTwoStr);
				getTabId(AppController.listitemArrays[position]);
				PreferenceManager.setButtonOneTabId(mContext, TAB_ID);
				setBackgroundColorForView(AppController.listitemArrays[position], mRelOne);
				PreferenceManager.setButtonOneTextImage(mContext,
						Integer.toString(position));
                if (PreferenceManager.getButtonOneTabId(mContext).equalsIgnoreCase(TAB_CALENDAR_REG)) {
                    if(!(PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgOneDot.setVisibility(View.VISIBLE);
                        relImgOneDot.setText(PreferenceManager.getCalendarBadge(mContext));
                        relImgOneDot.setBackgroundResource(R.drawable.shape_circle_red);

                    }
                    else if((PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgOneDot.setVisibility(View.VISIBLE);

                        relImgOneDot.setText(PreferenceManager.getCalendarEditedBadge(mContext));
                        relImgOneDot.setBackgroundResource(R.drawable.shape_circle_navy);

                    }
                    else if(!(PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgOneDot.setVisibility(View.VISIBLE);

                        relImgOneDot.setText(PreferenceManager.getCalendarBadge(mContext));
                        relImgOneDot.setBackgroundResource(R.drawable.shape_circle_red);

                    }
                    else {
                        relImgOneDot.setVisibility(View.GONE);
                    }


                } else if (PreferenceManager.getButtonOneTabId(mContext).equalsIgnoreCase(TAB_NOTIFICATIONS_REG)) {
                    if(!(PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgOneDot.setVisibility(View.VISIBLE);
                        relImgOneDot.setText(PreferenceManager.getNotificationBadge(mContext));
                        relImgOneDot.setBackgroundResource(R.drawable.shape_circle_red);
                    }
                    else if((PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgOneDot.setVisibility(View.VISIBLE);
                        relImgOneDot.setText(PreferenceManager.getNotificationEditedBadge(mContext));
                        relImgOneDot.setBackgroundResource(R.drawable.shape_circle_navy);

                    }
                    else if(!(PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgOneDot.setVisibility(View.VISIBLE);
                        relImgOneDot.setText(PreferenceManager.getNotificationBadge(mContext));
                        relImgOneDot.setBackgroundResource(R.drawable.shape_circle_red);

                    }
                    else {
                        relImgOneDot.setVisibility(View.GONE);

                    }

                } else if (PreferenceManager.getButtonOneTabId(mContext).equalsIgnoreCase(TAB_TRIPS_REG)) {
					if(!(PreferenceManager.getPaymentitem_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getPaymentitem_edit_badge(mContext).equalsIgnoreCase("0"))))
					{
						relImgOneDot.setVisibility(View.VISIBLE);
						relImgOneDot.setText(PreferenceManager.getPaymentitem_badge(mContext));
						relImgOneDot.setBackgroundResource(R.drawable.shape_circle_red);
					}
					else if((PreferenceManager.getPaymentitem_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getPaymentitem_edit_badge(mContext).equalsIgnoreCase("0"))))
					{
						relImgOneDot.setVisibility(View.VISIBLE);
						relImgOneDot.setText(PreferenceManager.getPaymentitem_edit_badge(mContext));
						relImgOneDot.setBackgroundResource(R.drawable.shape_circle_navy);

					}
					else if(!(PreferenceManager.getPaymentitem_badge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getPaymentitem_edit_badge(mContext).equalsIgnoreCase("0"))))
					{
						relImgOneDot.setVisibility(View.VISIBLE);
						relImgOneDot.setText(PreferenceManager.getPaymentitem_badge(mContext));
						relImgOneDot.setBackgroundResource(R.drawable.shape_circle_red);

					}
					else {
						relImgOneDot.setVisibility(View.GONE);

					}

				}
                else if (PreferenceManager.getButtonOneTabId(mContext).equalsIgnoreCase(TAB_REPORT_REG)) {
                    if(!(PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgOneDot.setVisibility(View.VISIBLE);
                        relImgOneDot.setText(PreferenceManager.getReportsBadge(mContext));
                        relImgOneDot.setBackgroundResource(R.drawable.shape_circle_red);
                    }
                    else if((PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgOneDot.setVisibility(View.VISIBLE);
                        relImgOneDot.setText(PreferenceManager.getReportsEditedBadge(mContext));
                        relImgOneDot.setBackgroundResource(R.drawable.shape_circle_navy);

                    }
                    else if(!(PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgOneDot.setVisibility(View.VISIBLE);
                        relImgOneDot.setText(PreferenceManager.getReportsBadge(mContext));
                        relImgOneDot.setBackgroundResource(R.drawable.shape_circle_red);

                    }
                    else {
                        relImgOneDot.setVisibility(View.GONE);

                    }

                } else if (PreferenceManager.getButtonOneTabId(mContext).equalsIgnoreCase(TAB_CCAS_REG)) {
                    if(!(PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgOneDot.setVisibility(View.VISIBLE);
                        relImgOneDot.setText(PreferenceManager.getCcaBadge(mContext));
                        relImgOneDot.setBackgroundResource(R.drawable.shape_circle_red);
                    }
                    else if((PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgOneDot.setVisibility(View.VISIBLE);
                        relImgOneDot.setText(PreferenceManager.getCcaEditedBadge(mContext));
                        relImgOneDot.setBackgroundResource(R.drawable.shape_circle_navy);

                    }
                    else if(!(PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgOneDot.setVisibility(View.VISIBLE);
                        relImgOneDot.setText(PreferenceManager.getCcaBadge(mContext));
                        relImgOneDot.setBackgroundResource(R.drawable.shape_circle_red);

                    }
                    else {
                        relImgOneDot.setVisibility(View.GONE);

                    }

                }
                else if (PreferenceManager.getButtonOneTabId(mContext).equalsIgnoreCase(TAB_SPORTS_REG)) {
                    if(!(PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgOneDot.setVisibility(View.VISIBLE);
                        relImgOneDot.setText(PreferenceManager.getSportsBadge(mContext));
                        relImgOneDot.setBackgroundResource(R.drawable.shape_circle_red);
                    }
                    else if((PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgOneDot.setVisibility(View.VISIBLE);
                        relImgOneDot.setText(PreferenceManager.getSportsEditedBadge(mContext));
                        relImgOneDot.setBackgroundResource(R.drawable.shape_circle_navy);

                    }
                    else if(!(PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgOneDot.setVisibility(View.VISIBLE);
                        relImgOneDot.setText(PreferenceManager.getSportsBadge(mContext));
                        relImgOneDot.setBackgroundResource(R.drawable.shape_circle_red);

                    }
                    else {
                        relImgOneDot.setVisibility(View.GONE);

                    }

                }

				else if (PreferenceManager.getButtonOneTabId(mContext).equalsIgnoreCase(TAB_UNIVERSITY_GUIDANCE_REG)) {
					if(!(PreferenceManager.getUniversity_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase("0"))))
					{
						relImgOneDot.setVisibility(View.VISIBLE);
						relImgOneDot.setText(PreferenceManager.getUniversity_badge(mContext));
						relImgOneDot.setBackgroundResource(R.drawable.shape_circle_red);
					}
					else if((PreferenceManager.getUniversity_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase("0"))) && (!(PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase(""))))
					{
						relImgOneDot.setVisibility(View.VISIBLE);
						relImgOneDot.setText(PreferenceManager.getUniversity_edit_badge(mContext));
						relImgOneDot.setBackgroundResource(R.drawable.shape_circle_navy);

					}
					else if(!(PreferenceManager.getUniversity_badge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase("0"))))
					{
						relImgOneDot.setVisibility(View.VISIBLE);
						relImgOneDot.setText(PreferenceManager.getUniversity_badge(mContext));
						relImgOneDot.setBackgroundResource(R.drawable.shape_circle_red);

					}
					else {
						relImgOneDot.setVisibility(View.GONE);

					}

				}
				else if (PreferenceManager.getButtonOneTabId(mContext).equalsIgnoreCase(TAB_COMMUNICATIONS_REG)) {
					if(!(PreferenceManager.getNoticeBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNoticeEditedBadge(mContext).equalsIgnoreCase("0"))))
					{
						relImgOneDot.setVisibility(View.VISIBLE);
						relImgOneDot.setText(PreferenceManager.getNoticeBadge(mContext));
						relImgOneDot.setBackgroundResource(R.drawable.shape_circle_red);
					}
					else if((PreferenceManager.getNoticeBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNoticeEditedBadge(mContext).equalsIgnoreCase("0"))))
					{
						relImgOneDot.setVisibility(View.VISIBLE);
						relImgOneDot.setText(PreferenceManager.getNoticeEditedBadge(mContext));
						relImgOneDot.setBackgroundResource(R.drawable.shape_circle_navy);

					}
					else if(!(PreferenceManager.getNoticeBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getNoticeEditedBadge(mContext).equalsIgnoreCase("0"))))
					{
						relImgOneDot.setVisibility(View.VISIBLE);
						relImgOneDot.setText(PreferenceManager.getNoticeBadge(mContext));
						relImgOneDot.setBackgroundResource(R.drawable.shape_circle_red);

					}
					else {
						relImgOneDot.setVisibility(View.GONE);

					}

				}
                else
                {
                    relImgOneDot.setVisibility(View.GONE);
                }
			} else if (v == mRelTwo) {
				mImgTwo.setImageDrawable(AppController.mListImgArrays.getDrawable(position));
				String relTwoStr="";
				if (AppController.listitemArrays[position].toString().equalsIgnoreCase(CCAS))
				{
					relTwoStr=CCAS;
				}
				else
				{
					relTwoStr=AppController.listitemArrays[position].toUpperCase();

				}
				mTxtTwo.setText(relTwoStr);
				getTabId(AppController.listitemArrays[position]);
				PreferenceManager.setButtonTwoTabId(mContext, TAB_ID);
				setBackgroundColorForView(AppController.listitemArrays[position], mRelTwo);
				PreferenceManager.setButtonTwoTextImage(mContext,
						Integer.toString(position));
                if (PreferenceManager.getButtonTwoTabId(mContext).equalsIgnoreCase(TAB_CALENDAR_REG)) {
                    if(!(PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgTwoDot.setVisibility(View.VISIBLE);
                        relImgTwoDot.setText(PreferenceManager.getCalendarBadge(mContext));
                        relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_red);

                    }
                    else if((PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgTwoDot.setVisibility(View.VISIBLE);

                        relImgTwoDot.setText(PreferenceManager.getCalendarEditedBadge(mContext));
                        relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_navy);

                    }
                    else if(!(PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgTwoDot.setVisibility(View.VISIBLE);

                        relImgTwoDot.setText(PreferenceManager.getCalendarBadge(mContext));
                        relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_red);

                    }
                    else {
                        relImgTwoDot.setVisibility(View.GONE);
                    }


                } else if (PreferenceManager.getButtonTwoTabId(mContext).equalsIgnoreCase(TAB_NOTIFICATIONS_REG)) {
                    if(!(PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgTwoDot.setVisibility(View.VISIBLE);
                        relImgTwoDot.setText(PreferenceManager.getNotificationBadge(mContext));
                        relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_red);
                    }
                    else if((PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgTwoDot.setVisibility(View.VISIBLE);
                        relImgTwoDot.setText(PreferenceManager.getNotificationEditedBadge(mContext));
                        relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_navy);

                    }
                    else if(!(PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgTwoDot.setVisibility(View.VISIBLE);
                        relImgTwoDot.setText(PreferenceManager.getNotificationBadge(mContext));
                        relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_red);

                    }
                    else {
                        relImgTwoDot.setVisibility(View.GONE);

                    }
                } else if (PreferenceManager.getButtonTwoTabId(mContext).equalsIgnoreCase(TAB_TRIPS_REG)) {
					if(!(PreferenceManager.getPaymentitem_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getPaymentitem_edit_badge(mContext).equalsIgnoreCase("0"))))
					{
						relImgTwoDot.setVisibility(View.VISIBLE);
						relImgTwoDot.setText(PreferenceManager.getPaymentitem_badge(mContext));
						relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_red);
					}
					else if((PreferenceManager.getPaymentitem_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getPaymentitem_edit_badge(mContext).equalsIgnoreCase("0"))))
					{
						relImgTwoDot.setVisibility(View.VISIBLE);
						relImgTwoDot.setText(PreferenceManager.getPaymentitem_edit_badge(mContext));
						relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_navy);

					}
					else if(!(PreferenceManager.getPaymentitem_badge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getPaymentitem_edit_badge(mContext).equalsIgnoreCase("0"))))
					{
						relImgTwoDot.setVisibility(View.VISIBLE);
						relImgTwoDot.setText(PreferenceManager.getPaymentitem_badge(mContext));
						relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_red);

					}
					else {
						relImgTwoDot.setVisibility(View.GONE);

					}
				}
				else if (PreferenceManager.getButtonTwoTabId(mContext).equalsIgnoreCase(TAB_REPORT_REG)) {
                    if(!(PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgTwoDot.setVisibility(View.VISIBLE);
                        relImgTwoDot.setText(PreferenceManager.getReportsBadge(mContext));
                        relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_red);
                    }
                    else if((PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgTwoDot.setVisibility(View.VISIBLE);
                        relImgTwoDot.setText(PreferenceManager.getReportsEditedBadge(mContext));
                        relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_navy);

                    }
                    else if(!(PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgTwoDot.setVisibility(View.VISIBLE);
                        relImgTwoDot.setText(PreferenceManager.getReportsBadge(mContext));
                        relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_red);

                    }
                    else {
                        relImgTwoDot.setVisibility(View.GONE);

                    }
                } else if (PreferenceManager.getButtonTwoTabId(mContext).equalsIgnoreCase(TAB_CCAS_REG)) {
                    if(!(PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgTwoDot.setVisibility(View.VISIBLE);
                        relImgTwoDot.setText(PreferenceManager.getCcaBadge(mContext));
                        relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_red);
                    }
                    else if((PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgTwoDot.setVisibility(View.VISIBLE);
                        relImgTwoDot.setText(PreferenceManager.getCcaEditedBadge(mContext));
                        relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_navy);

                    }
                    else if(!(PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgTwoDot.setVisibility(View.VISIBLE);
                        relImgTwoDot.setText(PreferenceManager.getCcaBadge(mContext));
                        relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_red);

                    }
                    else {
                        relImgTwoDot.setVisibility(View.GONE);

                    }
                } else if (PreferenceManager.getButtonTwoTabId(mContext).equalsIgnoreCase(TAB_SPORTS_REG)) {
                    if(!(PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgTwoDot.setVisibility(View.VISIBLE);
                        relImgTwoDot.setText(PreferenceManager.getSportsBadge(mContext));
                        relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_red);
                    }
                    else if((PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgTwoDot.setVisibility(View.VISIBLE);
                        relImgTwoDot.setText(PreferenceManager.getSportsEditedBadge(mContext));
                        relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_navy);

                    }
                    else if(!(PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgTwoDot.setVisibility(View.VISIBLE);
                        relImgTwoDot.setText(PreferenceManager.getSportsBadge(mContext));
                        relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_red);

                    }
                    else {
                        relImgTwoDot.setVisibility(View.GONE);

                    }
                }
                else
                {
                    relImgTwoDot.setVisibility(View.GONE);
                }if (PreferenceManager.getButtonTwoTabId(mContext).equalsIgnoreCase(TAB_CALENDAR_REG)) {
                    if(!(PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgTwoDot.setVisibility(View.VISIBLE);
                        relImgTwoDot.setText(PreferenceManager.getCalendarBadge(mContext));
                        relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_red);

                    }
                    else if((PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgTwoDot.setVisibility(View.VISIBLE);

                        relImgTwoDot.setText(PreferenceManager.getCalendarEditedBadge(mContext));
                        relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_navy);

                    }
                    else if(!(PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgTwoDot.setVisibility(View.VISIBLE);

                        relImgTwoDot.setText(PreferenceManager.getCalendarBadge(mContext));
                        relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_red);

                    }
                    else {
                        relImgTwoDot.setVisibility(View.GONE);
                    }


                } else if (PreferenceManager.getButtonTwoTabId(mContext).equalsIgnoreCase(TAB_NOTIFICATIONS_REG)) {
                    if(!(PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgTwoDot.setVisibility(View.VISIBLE);
                        relImgTwoDot.setText(PreferenceManager.getNotificationBadge(mContext));
                        relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_red);
                    }
                    else if((PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgTwoDot.setVisibility(View.VISIBLE);
                        relImgTwoDot.setText(PreferenceManager.getNotificationEditedBadge(mContext));
                        relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_navy);

                    }
                    else if(!(PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgTwoDot.setVisibility(View.VISIBLE);
                        relImgTwoDot.setText(PreferenceManager.getNotificationBadge(mContext));
                        relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_red);

                    }
                    else {
                        relImgTwoDot.setVisibility(View.GONE);

                    }
                } else if (PreferenceManager.getButtonTwoTabId(mContext).equalsIgnoreCase(TAB_REPORT_REG)) {
                    if(!(PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgTwoDot.setVisibility(View.VISIBLE);
                        relImgTwoDot.setText(PreferenceManager.getReportsBadge(mContext));
                        relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_red);
                    }
                    else if((PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgTwoDot.setVisibility(View.VISIBLE);
                        relImgTwoDot.setText(PreferenceManager.getReportsEditedBadge(mContext));
                        relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_navy);

                    }
                    else if(!(PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgTwoDot.setVisibility(View.VISIBLE);
                        relImgTwoDot.setText(PreferenceManager.getReportsBadge(mContext));
                        relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_red);

                    }
                    else {
                        relImgTwoDot.setVisibility(View.GONE);

                    }
                } else if (PreferenceManager.getButtonTwoTabId(mContext).equalsIgnoreCase(TAB_CCAS_REG)) {
                    if(!(PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgTwoDot.setVisibility(View.VISIBLE);
                        relImgTwoDot.setText(PreferenceManager.getCcaBadge(mContext));
                        relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_red);
                    }
                    else if((PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgTwoDot.setVisibility(View.VISIBLE);
                        relImgTwoDot.setText(PreferenceManager.getCcaEditedBadge(mContext));
                        relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_navy);

                    }
                    else if(!(PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgTwoDot.setVisibility(View.VISIBLE);
                        relImgTwoDot.setText(PreferenceManager.getCcaBadge(mContext));
                        relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_red);

                    }
                    else {
                        relImgTwoDot.setVisibility(View.GONE);

                    }
                } else if (PreferenceManager.getButtonTwoTabId(mContext).equalsIgnoreCase(TAB_SPORTS_REG)) {
                    if(!(PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgTwoDot.setVisibility(View.VISIBLE);
                        relImgTwoDot.setText(PreferenceManager.getSportsBadge(mContext));
                        relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_red);
                    }
                    else if((PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgTwoDot.setVisibility(View.VISIBLE);
                        relImgTwoDot.setText(PreferenceManager.getSportsEditedBadge(mContext));
                        relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_navy);

                    }
                    else if(!(PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgTwoDot.setVisibility(View.VISIBLE);
                        relImgTwoDot.setText(PreferenceManager.getSportsBadge(mContext));
                        relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_red);

                    }
                    else {
                        relImgTwoDot.setVisibility(View.GONE);

                    }
                }

				else if (PreferenceManager.getButtonTwoTabId(mContext).equalsIgnoreCase(TAB_UNIVERSITY_GUIDANCE_REG))
				{
					if(!(PreferenceManager.getUniversity_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase("0"))))
					{
						relImgTwoDot.setVisibility(View.VISIBLE);
						relImgTwoDot.setText(PreferenceManager.getUniversity_badge(mContext));
						relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_red);
					}
					else if((PreferenceManager.getUniversity_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase("0"))) && (!(PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase(""))))
					{
						relImgTwoDot.setVisibility(View.VISIBLE);
						relImgTwoDot.setText(PreferenceManager.getUniversity_edit_badge(mContext));
						relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_navy);

					}
					else if(!(PreferenceManager.getUniversity_badge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase("0"))))
					{
						relImgTwoDot.setVisibility(View.VISIBLE);
						relImgTwoDot.setText(PreferenceManager.getUniversity_badge(mContext));
						relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_red);

					}
					else {
						relImgTwoDot.setVisibility(View.GONE);

					}
				}
				else if (PreferenceManager.getButtonTwoTabId(mContext).equalsIgnoreCase(TAB_COMMUNICATIONS_REG)) {
					if(!(PreferenceManager.getNoticeBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNoticeEditedBadge(mContext).equalsIgnoreCase("0"))))
					{
						relImgTwoDot.setVisibility(View.VISIBLE);
						relImgTwoDot.setText(PreferenceManager.getNoticeBadge(mContext));
						relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_red);
					}
					else if((PreferenceManager.getNoticeBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNoticeEditedBadge(mContext).equalsIgnoreCase("0"))))
					{
						relImgTwoDot.setVisibility(View.VISIBLE);
						relImgTwoDot.setText(PreferenceManager.getNoticeEditedBadge(mContext));
						relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_navy);

					}
					else if(!(PreferenceManager.getNoticeBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getNoticeEditedBadge(mContext).equalsIgnoreCase("0"))))
					{
						relImgTwoDot.setVisibility(View.VISIBLE);
						relImgTwoDot.setText(PreferenceManager.getNoticeBadge(mContext));
						relImgTwoDot.setBackgroundResource(R.drawable.shape_circle_red);

					}
					else {
						relImgTwoDot.setVisibility(View.GONE);

					}

				}
                else
                {
                    relImgTwoDot.setVisibility(View.GONE);
                }
			} else if (v == mRelThree) {
				mImgThree.setImageDrawable(AppController.mListImgArrays.getDrawable(position));
				String relTwoStr="";
				if (AppController.listitemArrays[position].toString().equalsIgnoreCase(CCAS))
				{
					relTwoStr=CCAS;
				}
				else
				{
					relTwoStr=AppController.listitemArrays[position].toUpperCase();

				}
				mTxtThree.setText(relTwoStr);
				getTabId(AppController.listitemArrays[position]);
				PreferenceManager.setButtonThreeTabId(mContext, TAB_ID);
				setBackgroundColorForView(AppController.listitemArrays[position], mRelThree);
				PreferenceManager.setButtonThreeTextImage(mContext,
						Integer.toString(position));
                if (PreferenceManager.getButtonThreeTabId(mContext).equalsIgnoreCase(TAB_CALENDAR_REG)) {
                    if(!(PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgThreeDot.setVisibility(View.VISIBLE);
                        relImgThreeDot.setText(PreferenceManager.getCalendarBadge(mContext));
                        relImgThreeDot.setBackgroundResource(R.drawable.shape_circle_red);

                    }
                    else if((PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgThreeDot.setVisibility(View.VISIBLE);

                        relImgThreeDot.setText(PreferenceManager.getCalendarEditedBadge(mContext));
                        relImgThreeDot.setBackgroundResource(R.drawable.shape_circle_navy);

                    }
                    else if(!(PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgThreeDot.setVisibility(View.VISIBLE);

                        relImgThreeDot.setText(PreferenceManager.getCalendarBadge(mContext));
                        relImgThreeDot.setBackgroundResource(R.drawable.shape_circle_red);

                    }
                    else {
                        relImgThreeDot.setVisibility(View.GONE);
                    }

                } else if (PreferenceManager.getButtonThreeTabId(mContext).equalsIgnoreCase(TAB_NOTIFICATIONS_REG)) {
                    if(!(PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgThreeDot.setVisibility(View.VISIBLE);
                        relImgThreeDot.setText(PreferenceManager.getNotificationBadge(mContext));
                        relImgThreeDot.setBackgroundResource(R.drawable.shape_circle_red);
                    }
                    else if((PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgThreeDot.setVisibility(View.VISIBLE);
                        relImgThreeDot.setText(PreferenceManager.getNotificationEditedBadge(mContext));
                        relImgThreeDot.setBackgroundResource(R.drawable.shape_circle_navy);

                    }
                    else if(!(PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgThreeDot.setVisibility(View.VISIBLE);
                        relImgThreeDot.setText(PreferenceManager.getNotificationBadge(mContext));
                        relImgThreeDot.setBackgroundResource(R.drawable.shape_circle_red);

                    }
                    else {
                        relImgThreeDot.setVisibility(View.GONE);

                    }
                } else if (PreferenceManager.getButtonThreeTabId(mContext).equalsIgnoreCase(TAB_TRIPS_REG)) {
					if(!(PreferenceManager.getPaymentitem_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getPaymentitem_edit_badge(mContext).equalsIgnoreCase("0"))))
					{
						relImgThreeDot.setVisibility(View.VISIBLE);
						relImgThreeDot.setText(PreferenceManager.getPaymentitem_badge(mContext));
						relImgThreeDot.setBackgroundResource(R.drawable.shape_circle_red);
					}
					else if((PreferenceManager.getPaymentitem_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getPaymentitem_edit_badge(mContext).equalsIgnoreCase("0"))))
					{
						relImgThreeDot.setVisibility(View.VISIBLE);
						relImgThreeDot.setText(PreferenceManager.getPaymentitem_edit_badge(mContext));
						relImgThreeDot.setBackgroundResource(R.drawable.shape_circle_navy);

					}
					else if(!(PreferenceManager.getPaymentitem_badge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getPaymentitem_edit_badge(mContext).equalsIgnoreCase("0"))))
					{
						relImgThreeDot.setVisibility(View.VISIBLE);
						relImgThreeDot.setText(PreferenceManager.getPaymentitem_badge(mContext));
						relImgThreeDot.setBackgroundResource(R.drawable.shape_circle_red);

					}
					else {
						relImgThreeDot.setVisibility(View.GONE);

					}
				}
                else if (PreferenceManager.getButtonThreeTabId(mContext).equalsIgnoreCase(TAB_REPORT_REG)) {
                    if(!(PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgThreeDot.setVisibility(View.VISIBLE);
                        relImgThreeDot.setText(PreferenceManager.getReportsBadge(mContext));
                        relImgThreeDot.setBackgroundResource(R.drawable.shape_circle_red);
                    }
                    else if((PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgThreeDot.setVisibility(View.VISIBLE);
                        relImgThreeDot.setText(PreferenceManager.getReportsEditedBadge(mContext));
                        relImgThreeDot.setBackgroundResource(R.drawable.shape_circle_navy);

                    }
                    else if(!(PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgThreeDot.setVisibility(View.VISIBLE);
                        relImgThreeDot.setText(PreferenceManager.getReportsBadge(mContext));
                        relImgThreeDot.setBackgroundResource(R.drawable.shape_circle_red);

                    }
                    else {
                        relImgThreeDot.setVisibility(View.GONE);

                    }
                } else if (PreferenceManager.getButtonThreeTabId(mContext).equalsIgnoreCase(TAB_CCAS_REG)) {
                    if(!(PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgThreeDot.setVisibility(View.VISIBLE);
                        relImgThreeDot.setText(PreferenceManager.getCcaBadge(mContext));
                        relImgThreeDot.setBackgroundResource(R.drawable.shape_circle_red);
                    }
                    else if((PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgThreeDot.setVisibility(View.VISIBLE);
                        relImgThreeDot.setText(PreferenceManager.getCcaEditedBadge(mContext));
                        relImgThreeDot.setBackgroundResource(R.drawable.shape_circle_navy);

                    }
                    else if(!(PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgThreeDot.setVisibility(View.VISIBLE);
                        relImgThreeDot.setText(PreferenceManager.getCcaBadge(mContext));
                        relImgThreeDot.setBackgroundResource(R.drawable.shape_circle_red);

                    }
                    else {
                        relImgThreeDot.setVisibility(View.GONE);

                    }
                } else if (PreferenceManager.getButtonThreeTabId(mContext).equalsIgnoreCase(TAB_SPORTS_REG)) {
                    if(!(PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgThreeDot.setVisibility(View.VISIBLE);
                        relImgThreeDot.setText(PreferenceManager.getSportsBadge(mContext));
                        relImgThreeDot.setBackgroundResource(R.drawable.shape_circle_red);
                    }
                    else if((PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgThreeDot.setVisibility(View.VISIBLE);
                        relImgThreeDot.setText(PreferenceManager.getSportsEditedBadge(mContext));
                        relImgThreeDot.setBackgroundResource(R.drawable.shape_circle_navy);

                    }
                    else if(!(PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgThreeDot.setVisibility(View.VISIBLE);
                        relImgThreeDot.setText(PreferenceManager.getSportsBadge(mContext));
                        relImgThreeDot.setBackgroundResource(R.drawable.shape_circle_red);

                    }
                    else {
                        relImgThreeDot.setVisibility(View.GONE);

                    }
                }

				else if (PreferenceManager.getButtonThreeTabId(mContext).equalsIgnoreCase(TAB_UNIVERSITY_GUIDANCE_REG)) {
					if(!(PreferenceManager.getUniversity_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase("0"))))
					{
						relImgThreeDot.setVisibility(View.VISIBLE);
						relImgThreeDot.setText(PreferenceManager.getUniversity_badge(mContext));
						relImgThreeDot.setBackgroundResource(R.drawable.shape_circle_red);
					}
					else if((PreferenceManager.getUniversity_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase("0"))) && (!(PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase(""))))
					{
						relImgThreeDot.setVisibility(View.VISIBLE);
						relImgThreeDot.setText(PreferenceManager.getUniversity_edit_badge(mContext));
						relImgThreeDot.setBackgroundResource(R.drawable.shape_circle_navy);

					}
					else if(!(PreferenceManager.getUniversity_badge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase("0"))))
					{
						relImgThreeDot.setVisibility(View.VISIBLE);
						relImgThreeDot.setText(PreferenceManager.getUniversity_badge(mContext));
						relImgThreeDot.setBackgroundResource(R.drawable.shape_circle_red);

					}
					else {
						relImgThreeDot.setVisibility(View.GONE);

					}
				}
				else if (PreferenceManager.getButtonThreeTabId(mContext).equalsIgnoreCase(TAB_COMMUNICATIONS_REG)) {
					if(!(PreferenceManager.getNoticeBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNoticeEditedBadge(mContext).equalsIgnoreCase("0"))))
					{
						relImgThreeDot.setVisibility(View.VISIBLE);
						relImgThreeDot.setText(PreferenceManager.getNoticeBadge(mContext));
						relImgThreeDot.setBackgroundResource(R.drawable.shape_circle_red);
					}
					else if((PreferenceManager.getNoticeBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNoticeEditedBadge(mContext).equalsIgnoreCase("0"))))
					{
						relImgThreeDot.setVisibility(View.VISIBLE);
						relImgThreeDot.setText(PreferenceManager.getNoticeEditedBadge(mContext));
						relImgThreeDot.setBackgroundResource(R.drawable.shape_circle_navy);

					}
					else if(!(PreferenceManager.getNoticeBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getNoticeEditedBadge(mContext).equalsIgnoreCase("0"))))
					{
						relImgThreeDot.setVisibility(View.VISIBLE);
						relImgThreeDot.setText(PreferenceManager.getNoticeBadge(mContext));
						relImgThreeDot.setBackgroundResource(R.drawable.shape_circle_red);

					}
					else {
						relImgThreeDot.setVisibility(View.GONE);

					}

				}
                else
                {
                    relImgThreeDot.setVisibility(View.GONE);
                }
			} else if (v == mRelFour) {
				mImgFour.setImageDrawable(AppController.mListImgArrays.getDrawable(position));
				String relTwoStr="";
				if (AppController.listitemArrays[position].toString().equalsIgnoreCase(CCAS))
				{
					relTwoStr=CCAS;
				}
				else
				{
					relTwoStr=AppController.listitemArrays[position].toUpperCase();

				}
				mTxtFour.setText(relTwoStr);
				getTabId(AppController.listitemArrays[position]);
				PreferenceManager.setButtonFourTabId(mContext, TAB_ID);
				setBackgroundColorForView(AppController.listitemArrays[position], mRelFour);
				PreferenceManager.setButtonFourTextImage(mContext,
						Integer.toString(position));
                if (PreferenceManager.getButtonFourTabId(mContext).equalsIgnoreCase(TAB_CALENDAR_REG)) {
                    if(!(PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgFourDot.setVisibility(View.VISIBLE);
                        relImgFourDot.setText(PreferenceManager.getCalendarBadge(mContext));
                        relImgFourDot.setBackgroundResource(R.drawable.shape_circle_red);

                    }
                    else if((PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgFourDot.setVisibility(View.VISIBLE);

                        relImgFourDot.setText(PreferenceManager.getCalendarEditedBadge(mContext));
                        relImgFourDot.setBackgroundResource(R.drawable.shape_circle_navy);

                    }
                    else if(!(PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgFourDot.setVisibility(View.VISIBLE);

                        relImgFourDot.setText(PreferenceManager.getCalendarBadge(mContext));
                        relImgFourDot.setBackgroundResource(R.drawable.shape_circle_red);

                    }
                    else {
                        relImgFourDot.setVisibility(View.GONE);
                    }

                } else if (PreferenceManager.getButtonFourTabId(mContext).equalsIgnoreCase(TAB_NOTIFICATIONS_REG)) {
                    if(!(PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgFourDot.setVisibility(View.VISIBLE);
                        relImgFourDot.setText(PreferenceManager.getNotificationBadge(mContext));
                        relImgFourDot.setBackgroundResource(R.drawable.shape_circle_red);
                    }
                    else if((PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgFourDot.setVisibility(View.VISIBLE);
                        relImgFourDot.setText(PreferenceManager.getNotificationEditedBadge(mContext));
                        relImgFourDot.setBackgroundResource(R.drawable.shape_circle_navy);

                    }
                    else if(!(PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgFourDot.setVisibility(View.VISIBLE);
                        relImgFourDot.setText(PreferenceManager.getNotificationBadge(mContext));
                        relImgFourDot.setBackgroundResource(R.drawable.shape_circle_red);

                    }
                    else {
                        relImgFourDot.setVisibility(View.GONE);

                    }
                }
				else if (PreferenceManager.getButtonFourTabId(mContext).equalsIgnoreCase(TAB_TRIPS_REG)) {
					if(!(PreferenceManager.getPaymentitem_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getPaymentitem_edit_badge(mContext).equalsIgnoreCase("0"))))
					{
						relImgFourDot.setVisibility(View.VISIBLE);
						relImgFourDot.setText(PreferenceManager.getPaymentitem_badge(mContext));
						relImgFourDot.setBackgroundResource(R.drawable.shape_circle_red);
					}
					else if((PreferenceManager.getPaymentitem_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getPaymentitem_edit_badge(mContext).equalsIgnoreCase("0"))))
					{
						relImgFourDot.setVisibility(View.VISIBLE);
						relImgFourDot.setText(PreferenceManager.getPaymentitem_edit_badge(mContext));
						relImgFourDot.setBackgroundResource(R.drawable.shape_circle_navy);

					}
					else if(!(PreferenceManager.getPaymentitem_badge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getPaymentitem_edit_badge(mContext).equalsIgnoreCase("0"))))
					{
						relImgFourDot.setVisibility(View.VISIBLE);
						relImgFourDot.setText(PreferenceManager.getPaymentitem_badge(mContext));
						relImgFourDot.setBackgroundResource(R.drawable.shape_circle_red);

					}
					else {
						relImgFourDot.setVisibility(View.GONE);

					}
				}
                else if (PreferenceManager.getButtonFourTabId(mContext).equalsIgnoreCase(TAB_REPORT_REG)) {
                    if(!(PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgFourDot.setVisibility(View.VISIBLE);
                        relImgFourDot.setText(PreferenceManager.getReportsBadge(mContext));
                        relImgFourDot.setBackgroundResource(R.drawable.shape_circle_red);
                    }
                    else if((PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgFourDot.setVisibility(View.VISIBLE);
                        relImgFourDot.setText(PreferenceManager.getReportsEditedBadge(mContext));
                        relImgFourDot.setBackgroundResource(R.drawable.shape_circle_navy);

                    }
                    else if(!(PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgFourDot.setVisibility(View.VISIBLE);
                        relImgFourDot.setText(PreferenceManager.getReportsBadge(mContext));
                        relImgFourDot.setBackgroundResource(R.drawable.shape_circle_red);

                    }
                    else {
                        relImgFourDot.setVisibility(View.GONE);

                    }
                } else if (PreferenceManager.getButtonFourTabId(mContext).equalsIgnoreCase(TAB_CCAS_REG)) {
                    if(!(PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgFourDot.setVisibility(View.VISIBLE);
                        relImgFourDot.setText(PreferenceManager.getCcaBadge(mContext));
                        relImgFourDot.setBackgroundResource(R.drawable.shape_circle_red);
                    }
                    else if((PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgFourDot.setVisibility(View.VISIBLE);
                        relImgFourDot.setText(PreferenceManager.getCcaEditedBadge(mContext));
                        relImgFourDot.setBackgroundResource(R.drawable.shape_circle_navy);

                    }
                    else if(!(PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgFourDot.setVisibility(View.VISIBLE);
                        relImgFourDot.setText(PreferenceManager.getCcaBadge(mContext));
                        relImgFourDot.setBackgroundResource(R.drawable.shape_circle_red);

                    }
                    else {
                        relImgFourDot.setVisibility(View.GONE);

                    }
                } else if (PreferenceManager.getButtonFourTabId(mContext).equalsIgnoreCase(TAB_SPORTS_REG)) {
					if (!(PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0")) && (!(PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0")))) {
						relImgFourDot.setVisibility(View.VISIBLE);
						relImgFourDot.setText(PreferenceManager.getSportsBadge(mContext));
						relImgFourDot.setBackgroundResource(R.drawable.shape_circle_red);
					} else if ((PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0")) && (!(PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0")))) {
						relImgFourDot.setVisibility(View.VISIBLE);
						relImgFourDot.setText(PreferenceManager.getSportsEditedBadge(mContext));
						relImgFourDot.setBackgroundResource(R.drawable.shape_circle_navy);

					} else if (!(PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0")) && ((PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0")))) {
						relImgFourDot.setVisibility(View.VISIBLE);
						relImgFourDot.setText(PreferenceManager.getSportsBadge(mContext));
						relImgFourDot.setBackgroundResource(R.drawable.shape_circle_red);

					}
				}
				else if (PreferenceManager.getButtonFourTabId(mContext).equalsIgnoreCase(TAB_UNIVERSITY_GUIDANCE_REG)) {
					if(!(PreferenceManager.getUniversity_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase("0"))))
					{
						relImgFourDot.setVisibility(View.VISIBLE);
						relImgFourDot.setText(PreferenceManager.getUniversity_badge(mContext));
						relImgFourDot.setBackgroundResource(R.drawable.shape_circle_red);
					}
					else if((PreferenceManager.getUniversity_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase("0"))) && (!(PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase(""))))
					{
						relImgFourDot.setVisibility(View.VISIBLE);
						relImgFourDot.setText(PreferenceManager.getUniversity_edit_badge(mContext));
						relImgFourDot.setBackgroundResource(R.drawable.shape_circle_navy);

					}
					else if(!(PreferenceManager.getUniversity_badge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase("0"))))
					{
						relImgFourDot.setVisibility(View.VISIBLE);
						relImgFourDot.setText(PreferenceManager.getUniversity_badge(mContext));
						relImgFourDot.setBackgroundResource(R.drawable.shape_circle_red);

					}
					else {
						relImgFourDot.setVisibility(View.GONE);

					}
				}
					else if (PreferenceManager.getButtonFourTabId(mContext).equalsIgnoreCase(TAB_COMMUNICATIONS_REG)) {
						if(!(PreferenceManager.getNoticeBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNoticeEditedBadge(mContext).equalsIgnoreCase("0"))))
						{
							relImgFourDot.setVisibility(View.VISIBLE);
							relImgFourDot.setText(PreferenceManager.getNoticeBadge(mContext));
							relImgFourDot.setBackgroundResource(R.drawable.shape_circle_red);
						}
						else if((PreferenceManager.getNoticeBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNoticeEditedBadge(mContext).equalsIgnoreCase("0"))))
						{
							relImgFourDot.setVisibility(View.VISIBLE);
							relImgFourDot.setText(PreferenceManager.getNoticeEditedBadge(mContext));
							relImgFourDot.setBackgroundResource(R.drawable.shape_circle_navy);

						}
						else if(!(PreferenceManager.getNoticeBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getNoticeEditedBadge(mContext).equalsIgnoreCase("0"))))
						{
							relImgFourDot.setVisibility(View.VISIBLE);
							relImgFourDot.setText(PreferenceManager.getNoticeBadge(mContext));
							relImgFourDot.setBackgroundResource(R.drawable.shape_circle_red);

						}
						else {
							relImgFourDot.setVisibility(View.GONE);

						}

					}


                else
                {
                    relImgFourDot.setVisibility(View.GONE);
                }
			} else if (v == mRelFive) {
				mImgFive.setImageDrawable(AppController.mListImgArrays.getDrawable(position));
				String relTwoStr="";
				if (AppController.listitemArrays[position].toString().equalsIgnoreCase(CCAS))
				{
					relTwoStr=CCAS;
				}
				else
				{
					relTwoStr=AppController.listitemArrays[position].toUpperCase();

				}
				mTxtFive.setText(relTwoStr);
				getTabId(AppController.listitemArrays[position]);
				PreferenceManager.setButtonFiveTabId(mContext, TAB_ID);
				setBackgroundColorForView(AppController.listitemArrays[position], mRelFive);
				PreferenceManager.setButtonFiveTextImage(mContext,
						Integer.toString(position));
				if (PreferenceManager.getButtonFiveTabId(mContext).equalsIgnoreCase(TAB_CALENDAR_REG)) {
					if(!(PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0"))))
					{
						relImgFiveDot.setVisibility(View.VISIBLE);
						relImgFiveDot.setText(PreferenceManager.getCalendarBadge(mContext));
						relImgFiveDot.setBackgroundResource(R.drawable.shape_circle_red);

					}
					else if((PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0"))))
					{
						relImgFiveDot.setVisibility(View.VISIBLE);

						relImgFiveDot.setText(PreferenceManager.getCalendarEditedBadge(mContext));
						relImgFiveDot.setBackgroundResource(R.drawable.shape_circle_navy);

					}
					else if(!(PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0"))))
					{
						relImgFiveDot.setVisibility(View.VISIBLE);

						relImgFiveDot.setText(PreferenceManager.getCalendarBadge(mContext));
						relImgFiveDot.setBackgroundResource(R.drawable.shape_circle_red);

					}
					else {
						relImgFiveDot.setVisibility(View.GONE);
					}

				} else if (PreferenceManager.getButtonFiveTabId(mContext).equalsIgnoreCase(TAB_NOTIFICATIONS_REG)) {
					if(!(PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0"))))
					{
						relImgFiveDot.setVisibility(View.VISIBLE);
						relImgFiveDot.setText(PreferenceManager.getNotificationBadge(mContext));
						relImgFiveDot.setBackgroundResource(R.drawable.shape_circle_red);
					}
					else if((PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0"))))
					{
						relImgFiveDot.setVisibility(View.VISIBLE);
						relImgFiveDot.setText(PreferenceManager.getNotificationEditedBadge(mContext));
						relImgFiveDot.setBackgroundResource(R.drawable.shape_circle_navy);

					}
					else if(!(PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0"))))
					{
						relImgFiveDot.setVisibility(View.VISIBLE);
						relImgFiveDot.setText(PreferenceManager.getNotificationBadge(mContext));
						relImgFiveDot.setBackgroundResource(R.drawable.shape_circle_red);

					}
					else {
						relImgFiveDot.setVisibility(View.GONE);

					}
				} else if (PreferenceManager.getButtonFiveTabId(mContext).equalsIgnoreCase(TAB_REPORT_REG)) {
					if(!(PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
					{
						relImgFiveDot.setVisibility(View.VISIBLE);
						relImgFiveDot.setText(PreferenceManager.getReportsBadge(mContext));
						relImgFiveDot.setBackgroundResource(R.drawable.shape_circle_red);
					}
					else if((PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
					{
						relImgFiveDot.setVisibility(View.VISIBLE);
						relImgFiveDot.setText(PreferenceManager.getReportsEditedBadge(mContext));
						relImgFiveDot.setBackgroundResource(R.drawable.shape_circle_navy);

					}
					else if(!(PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
					{
						relImgFiveDot.setVisibility(View.VISIBLE);
						relImgFiveDot.setText(PreferenceManager.getReportsBadge(mContext));
						relImgFiveDot.setBackgroundResource(R.drawable.shape_circle_red);

					}
					else {
						relImgFiveDot.setVisibility(View.GONE);

					}
				}
				else if (PreferenceManager.getButtonFiveTabId(mContext).equalsIgnoreCase(TAB_TRIPS_REG)) {
					if(!(PreferenceManager.getPaymentitem_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getPaymentitem_edit_badge(mContext).equalsIgnoreCase("0"))))
					{
						relImgFiveDot.setVisibility(View.VISIBLE);
						relImgFiveDot.setText(PreferenceManager.getPaymentitem_badge(mContext));
						relImgFiveDot.setBackgroundResource(R.drawable.shape_circle_red);
					}
					else if((PreferenceManager.getPaymentitem_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getPaymentitem_edit_badge(mContext).equalsIgnoreCase("0"))))
					{
						relImgFiveDot.setVisibility(View.VISIBLE);
						relImgFiveDot.setText(PreferenceManager.getPaymentitem_edit_badge(mContext));
						relImgFiveDot.setBackgroundResource(R.drawable.shape_circle_navy);

					}
					else if(!(PreferenceManager.getPaymentitem_badge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getPaymentitem_edit_badge(mContext).equalsIgnoreCase("0"))))
					{
						relImgFiveDot.setVisibility(View.VISIBLE);
						relImgFiveDot.setText(PreferenceManager.getPaymentitem_badge(mContext));
						relImgFiveDot.setBackgroundResource(R.drawable.shape_circle_red);

					}
					else {
						relImgFiveDot.setVisibility(View.GONE);

					}
				}
				else if (PreferenceManager.getButtonFiveTabId(mContext).equalsIgnoreCase(TAB_CCAS_REG)) {
					if(!(PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0"))))
					{
						relImgFiveDot.setVisibility(View.VISIBLE);
						relImgFiveDot.setText(PreferenceManager.getCcaBadge(mContext));
						relImgFiveDot.setBackgroundResource(R.drawable.shape_circle_red);
					}
					else if((PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0"))))
					{
						relImgFiveDot.setVisibility(View.VISIBLE);
						relImgFiveDot.setText(PreferenceManager.getCcaEditedBadge(mContext));
						relImgFiveDot.setBackgroundResource(R.drawable.shape_circle_navy);

					}
					else if(!(PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0"))))
					{
						relImgFiveDot.setVisibility(View.VISIBLE);
						relImgFiveDot.setText(PreferenceManager.getCcaBadge(mContext));
						relImgFiveDot.setBackgroundResource(R.drawable.shape_circle_red);

					}
					else {
						relImgFiveDot.setVisibility(View.GONE);

					}
				} else if (PreferenceManager.getButtonFiveTabId(mContext).equalsIgnoreCase(TAB_SPORTS_REG)) {
					if(!(PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0"))))
					{
						relImgFiveDot.setVisibility(View.VISIBLE);
						relImgFiveDot.setText(PreferenceManager.getSportsBadge(mContext));
						relImgFiveDot.setBackgroundResource(R.drawable.shape_circle_red);
					}
					else if((PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0"))))
					{
						relImgFiveDot.setVisibility(View.VISIBLE);
						relImgFiveDot.setText(PreferenceManager.getSportsEditedBadge(mContext));
						relImgFiveDot.setBackgroundResource(R.drawable.shape_circle_navy);

					}
					else if(!(PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0"))))
					{
						relImgFiveDot.setVisibility(View.VISIBLE);
						relImgFiveDot.setText(PreferenceManager.getSportsBadge(mContext));
						relImgFiveDot.setBackgroundResource(R.drawable.shape_circle_red);

					}
					else {
						relImgFiveDot.setVisibility(View.GONE);

					}
				}

				else if (PreferenceManager.getButtonFiveTabId(mContext).equalsIgnoreCase(TAB_UNIVERSITY_GUIDANCE_REG)) {
					if(!(PreferenceManager.getUniversity_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase("0"))))
					{
						relImgFiveDot.setVisibility(View.VISIBLE);
						relImgFiveDot.setText(PreferenceManager.getUniversity_badge(mContext));
						relImgFiveDot.setBackgroundResource(R.drawable.shape_circle_red);
					}
					else if((PreferenceManager.getUniversity_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase("0"))) && (!(PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase(""))))
					{
						relImgFiveDot.setVisibility(View.VISIBLE);
						relImgFiveDot.setText(PreferenceManager.getUniversity_edit_badge(mContext));
						relImgFiveDot.setBackgroundResource(R.drawable.shape_circle_navy);

					}
					else if(!(PreferenceManager.getUniversity_badge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase("0"))))
					{
						relImgFiveDot.setVisibility(View.VISIBLE);
						relImgFiveDot.setText(PreferenceManager.getUniversity_badge(mContext));
						relImgFiveDot.setBackgroundResource(R.drawable.shape_circle_red);

					}
					else {
						relImgFiveDot.setVisibility(View.GONE);

					}
				}
				else if (PreferenceManager.getButtonFiveTabId(mContext).equalsIgnoreCase(TAB_COMMUNICATIONS_REG)) {
					if(!(PreferenceManager.getNoticeBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNoticeEditedBadge(mContext).equalsIgnoreCase("0"))))
					{
						relImgFiveDot.setVisibility(View.VISIBLE);
						relImgFiveDot.setText(PreferenceManager.getNoticeBadge(mContext));
						relImgFiveDot.setBackgroundResource(R.drawable.shape_circle_red);
					}
					else if((PreferenceManager.getNoticeBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNoticeEditedBadge(mContext).equalsIgnoreCase("0"))))
					{
						relImgFiveDot.setVisibility(View.VISIBLE);
						relImgFiveDot.setText(PreferenceManager.getNoticeEditedBadge(mContext));
						relImgFiveDot.setBackgroundResource(R.drawable.shape_circle_navy);

					}
					else if(!(PreferenceManager.getNoticeBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getNoticeEditedBadge(mContext).equalsIgnoreCase("0"))))
					{
						relImgFiveDot.setVisibility(View.VISIBLE);
						relImgFiveDot.setText(PreferenceManager.getNoticeBadge(mContext));
						relImgFiveDot.setBackgroundResource(R.drawable.shape_circle_red);

					}
					else {
						relImgFiveDot.setVisibility(View.GONE);

					}

				}
				else
				{
					relImgFiveDot.setVisibility(View.GONE);
				}
			} else if (v == mRelSix) {
				mImgSix.setImageDrawable(AppController.mListImgArrays.getDrawable(position));
				String relTwoStr="";
				if (AppController.listitemArrays[position].toString().equalsIgnoreCase(CCAS))
				{
					relTwoStr=CCAS;
				}
				else
				{
					relTwoStr=AppController.listitemArrays[position].toUpperCase();

				}
				mTxtSix.setText(relTwoStr);
				getTabId(AppController.listitemArrays[position]);
				PreferenceManager.setButtonSixTabId(mContext, TAB_ID);
				setBackgroundColorForView(AppController.listitemArrays[position], mRelSix);
				PreferenceManager.setButtonSixTextImage(mContext,
						Integer.toString(position));
                if (PreferenceManager.getButtonSixTabId(mContext).equalsIgnoreCase(TAB_CALENDAR_REG)) {
                    if(!(PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgSixDot.setVisibility(View.VISIBLE);
                        relImgSixDot.setText(PreferenceManager.getCalendarBadge(mContext));
                        relImgSixDot.setBackgroundResource(R.drawable.shape_circle_red);

                    }
                    else if((PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgSixDot.setVisibility(View.VISIBLE);

                        relImgSixDot.setText(PreferenceManager.getCalendarEditedBadge(mContext));
                        relImgSixDot.setBackgroundResource(R.drawable.shape_circle_navy);

                    }
                    else if(!(PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgSixDot.setVisibility(View.VISIBLE);

                        relImgSixDot.setText(PreferenceManager.getCalendarBadge(mContext));
                        relImgSixDot.setBackgroundResource(R.drawable.shape_circle_red);

                    }
                    else {
                        relImgSixDot.setVisibility(View.GONE);
                    }

                } else if (PreferenceManager.getButtonSixTabId(mContext).equalsIgnoreCase(TAB_NOTIFICATIONS_REG)) {
                    if(!(PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgSixDot.setVisibility(View.VISIBLE);
                        relImgSixDot.setText(PreferenceManager.getNotificationBadge(mContext));
                        relImgSixDot.setBackgroundResource(R.drawable.shape_circle_red);
                    }
                    else if((PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgSixDot.setVisibility(View.VISIBLE);
                        relImgSixDot.setText(PreferenceManager.getNotificationEditedBadge(mContext));
                        relImgSixDot.setBackgroundResource(R.drawable.shape_circle_navy);

                    }
                    else if(!(PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgSixDot.setVisibility(View.VISIBLE);
                        relImgSixDot.setText(PreferenceManager.getNotificationBadge(mContext));
                        relImgSixDot.setBackgroundResource(R.drawable.shape_circle_red);

                    }
                    else {
                        relImgSixDot.setVisibility(View.GONE);

                    }
                } else if (PreferenceManager.getButtonSixTabId(mContext).equalsIgnoreCase(TAB_REPORT_REG)) {
                    if(!(PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgSixDot.setVisibility(View.VISIBLE);
                        relImgSixDot.setText(PreferenceManager.getReportsBadge(mContext));
                        relImgSixDot.setBackgroundResource(R.drawable.shape_circle_red);
                    }
                    else if((PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgSixDot.setVisibility(View.VISIBLE);
                        relImgSixDot.setText(PreferenceManager.getReportsEditedBadge(mContext));
                        relImgSixDot.setBackgroundResource(R.drawable.shape_circle_navy);

                    }
                    else if(!(PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgSixDot.setVisibility(View.VISIBLE);
                        relImgSixDot.setText(PreferenceManager.getReportsBadge(mContext));
                        relImgSixDot.setBackgroundResource(R.drawable.shape_circle_red);

                    }
                    else {
                        relImgSixDot.setVisibility(View.GONE);

                    }
                }
				else if (PreferenceManager.getButtonSixTabId(mContext).equalsIgnoreCase(TAB_TRIPS_REG)) {
					if(!(PreferenceManager.getPaymentitem_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getPaymentitem_edit_badge(mContext).equalsIgnoreCase("0"))))
					{
						relImgSixDot.setVisibility(View.VISIBLE);
						relImgSixDot.setText(PreferenceManager.getPaymentitem_badge(mContext));
						relImgSixDot.setBackgroundResource(R.drawable.shape_circle_red);
					}
					else if((PreferenceManager.getPaymentitem_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getPaymentitem_edit_badge(mContext).equalsIgnoreCase("0"))))
					{
						relImgSixDot.setVisibility(View.VISIBLE);
						relImgSixDot.setText(PreferenceManager.getPaymentitem_edit_badge(mContext));
						relImgSixDot.setBackgroundResource(R.drawable.shape_circle_navy);

					}
					else if(!(PreferenceManager.getPaymentitem_badge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getPaymentitem_edit_badge(mContext).equalsIgnoreCase("0"))))
					{
						relImgSixDot.setVisibility(View.VISIBLE);
						relImgSixDot.setText(PreferenceManager.getPaymentitem_badge(mContext));
						relImgSixDot.setBackgroundResource(R.drawable.shape_circle_red);

					}
					else {
						relImgSixDot.setVisibility(View.GONE);

					}
				}
				else if (PreferenceManager.getButtonSixTabId(mContext).equalsIgnoreCase(TAB_CCAS_REG)) {
                    if(!(PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgSixDot.setVisibility(View.VISIBLE);
                        relImgSixDot.setText(PreferenceManager.getCcaBadge(mContext));
                        relImgSixDot.setBackgroundResource(R.drawable.shape_circle_red);
                    }
                    else if((PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgSixDot.setVisibility(View.VISIBLE);
                        relImgSixDot.setText(PreferenceManager.getCcaEditedBadge(mContext));
                        relImgSixDot.setBackgroundResource(R.drawable.shape_circle_navy);

                    }
                    else if(!(PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgSixDot.setVisibility(View.VISIBLE);
                        relImgSixDot.setText(PreferenceManager.getCcaBadge(mContext));
                        relImgSixDot.setBackgroundResource(R.drawable.shape_circle_red);

                    }
                    else {
                        relImgSixDot.setVisibility(View.GONE);

                    }
                } else if (PreferenceManager.getButtonSixTabId(mContext).equalsIgnoreCase(TAB_SPORTS_REG)) {
                    if(!(PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgSixDot.setVisibility(View.VISIBLE);
                        relImgSixDot.setText(PreferenceManager.getSportsBadge(mContext));
                        relImgSixDot.setBackgroundResource(R.drawable.shape_circle_red);
                    }
                    else if((PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgSixDot.setVisibility(View.VISIBLE);
                        relImgSixDot.setText(PreferenceManager.getSportsEditedBadge(mContext));
                        relImgSixDot.setBackgroundResource(R.drawable.shape_circle_navy);

                    }
                    else if(!(PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgSixDot.setVisibility(View.VISIBLE);
                        relImgSixDot.setText(PreferenceManager.getSportsBadge(mContext));
                        relImgSixDot.setBackgroundResource(R.drawable.shape_circle_red);

                    }
                    else {
                        relImgSixDot.setVisibility(View.GONE);

                    }
                }

				else if (PreferenceManager.getButtonSixTabId(mContext).equalsIgnoreCase(TAB_UNIVERSITY_GUIDANCE_REG)) {
					if(!(PreferenceManager.getUniversity_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase("0"))))
					{
						relImgSixDot.setVisibility(View.VISIBLE);
						relImgSixDot.setText(PreferenceManager.getUniversity_badge(mContext));
						relImgSixDot.setBackgroundResource(R.drawable.shape_circle_red);
					}
					else if((PreferenceManager.getUniversity_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase("0"))) && (!(PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase(""))))
					{
						relImgSixDot.setVisibility(View.VISIBLE);
						relImgSixDot.setText(PreferenceManager.getUniversity_edit_badge(mContext));
						relImgSixDot.setBackgroundResource(R.drawable.shape_circle_navy);

					}
					else if(!(PreferenceManager.getUniversity_badge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase("0"))))
					{
						relImgSixDot.setVisibility(View.VISIBLE);
						relImgSixDot.setText(PreferenceManager.getUniversity_badge(mContext));
						relImgSixDot.setBackgroundResource(R.drawable.shape_circle_red);

					}
					else {
						relImgSixDot.setVisibility(View.GONE);

					}
				}
				else if (PreferenceManager.getButtonSixTabId(mContext).equalsIgnoreCase(TAB_COMMUNICATIONS_REG)) {
					if(!(PreferenceManager.getNoticeBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNoticeEditedBadge(mContext).equalsIgnoreCase("0"))))
					{
						relImgSixDot.setVisibility(View.VISIBLE);
						relImgSixDot.setText(PreferenceManager.getNoticeBadge(mContext));
						relImgSixDot.setBackgroundResource(R.drawable.shape_circle_red);
					}
					else if((PreferenceManager.getNoticeBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNoticeEditedBadge(mContext).equalsIgnoreCase("0"))))
					{
						relImgSixDot.setVisibility(View.VISIBLE);
						relImgSixDot.setText(PreferenceManager.getNoticeEditedBadge(mContext));
						relImgSixDot.setBackgroundResource(R.drawable.shape_circle_navy);

					}
					else if(!(PreferenceManager.getNoticeBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getNoticeEditedBadge(mContext).equalsIgnoreCase("0"))))
					{
						relImgSixDot.setVisibility(View.VISIBLE);
						relImgSixDot.setText(PreferenceManager.getNoticeBadge(mContext));
						relImgSixDot.setBackgroundResource(R.drawable.shape_circle_red);

					}
					else {
						relImgSixDot.setVisibility(View.GONE);

					}

				}
                else
                {
                    relImgSixDot.setVisibility(View.GONE);
                }
			} else if (v == mRelSeven) {
				mImgSeven.setImageDrawable(AppController.mListImgArrays.getDrawable(position));
				String relTwoStr="";
				if (AppController.listitemArrays[position].toString().equalsIgnoreCase(CCAS))
				{
					relTwoStr=CCAS;
				}
				else
				{
					relTwoStr=AppController.listitemArrays[position].toUpperCase();

				}
				mTxtSeven.setText(relTwoStr);
				getTabId(AppController.listitemArrays[position]);
				PreferenceManager.setButtonSevenTabId(mContext, TAB_ID);
				setBackgroundColorForView(AppController.listitemArrays[position], mRelSeven);
				PreferenceManager.setButtonSevenTextImage(mContext,
						Integer.toString(position));
                if (PreferenceManager.getButtonSevenTabId(mContext).equalsIgnoreCase(TAB_CALENDAR_REG)) {
                    if(!(PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgSevenDot.setVisibility(View.VISIBLE);
                        relImgSevenDot.setText(PreferenceManager.getCalendarBadge(mContext));
                        relImgSevenDot.setBackgroundResource(R.drawable.shape_circle_red);

                    }
                    else if((PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgSevenDot.setVisibility(View.VISIBLE);

                        relImgSevenDot.setText(PreferenceManager.getCalendarEditedBadge(mContext));
                        relImgSevenDot.setBackgroundResource(R.drawable.shape_circle_navy);

                    }
                    else if(!(PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgSevenDot.setVisibility(View.VISIBLE);

                        relImgSevenDot.setText(PreferenceManager.getCalendarBadge(mContext));
                        relImgSevenDot.setBackgroundResource(R.drawable.shape_circle_red);

                    }
                    else {
                        relImgSevenDot.setVisibility(View.GONE);
                    }

                } else if (PreferenceManager.getButtonSevenTabId(mContext).equalsIgnoreCase(TAB_NOTIFICATIONS_REG)) {
                    if(!(PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgSevenDot.setVisibility(View.VISIBLE);
                        relImgSevenDot.setText(PreferenceManager.getNotificationBadge(mContext));
                        relImgSevenDot.setBackgroundResource(R.drawable.shape_circle_red);
                    }
                    else if((PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgSevenDot.setVisibility(View.VISIBLE);
                        relImgSevenDot.setText(PreferenceManager.getNotificationEditedBadge(mContext));
                        relImgSevenDot.setBackgroundResource(R.drawable.shape_circle_navy);

                    }
                    else if(!(PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgSevenDot.setVisibility(View.VISIBLE);
                        relImgSevenDot.setText(PreferenceManager.getNotificationBadge(mContext));
                        relImgSevenDot.setBackgroundResource(R.drawable.shape_circle_red);

                    }
                    else {
                        relImgSevenDot.setVisibility(View.GONE);

                    }
                } else if (PreferenceManager.getButtonSevenTabId(mContext).equalsIgnoreCase(TAB_REPORT_REG)) {
                    if(!(PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgSevenDot.setVisibility(View.VISIBLE);
                        relImgSevenDot.setText(PreferenceManager.getReportsBadge(mContext));
                        relImgSevenDot.setBackgroundResource(R.drawable.shape_circle_red);
                    }
                    else if((PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgSevenDot.setVisibility(View.VISIBLE);
                        relImgSevenDot.setText(PreferenceManager.getReportsEditedBadge(mContext));
                        relImgSevenDot.setBackgroundResource(R.drawable.shape_circle_navy);

                    }
                    else if(!(PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgSevenDot.setVisibility(View.VISIBLE);
                        relImgSevenDot.setText(PreferenceManager.getReportsBadge(mContext));
                        relImgSevenDot.setBackgroundResource(R.drawable.shape_circle_red);

                    }
                    else {
                        relImgSevenDot.setVisibility(View.GONE);

                    }
                }else if (PreferenceManager.getButtonSevenTabId(mContext).equalsIgnoreCase(TAB_TRIPS_REG)) {
					if(!(PreferenceManager.getPaymentitem_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getPaymentitem_edit_badge(mContext).equalsIgnoreCase("0"))))
					{
						relImgSevenDot.setVisibility(View.VISIBLE);
						relImgSevenDot.setText(PreferenceManager.getPaymentitem_badge(mContext));
						relImgSevenDot.setBackgroundResource(R.drawable.shape_circle_red);
					}
					else if((PreferenceManager.getPaymentitem_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getPaymentitem_edit_badge(mContext).equalsIgnoreCase("0"))))
					{
						relImgSevenDot.setVisibility(View.VISIBLE);
						relImgSevenDot.setText(PreferenceManager.getPaymentitem_edit_badge(mContext));
						relImgSevenDot.setBackgroundResource(R.drawable.shape_circle_navy);

					}
					else if(!(PreferenceManager.getPaymentitem_badge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getPaymentitem_edit_badge(mContext).equalsIgnoreCase("0"))))
					{
						relImgSevenDot.setVisibility(View.VISIBLE);
						relImgSevenDot.setText(PreferenceManager.getPaymentitem_badge(mContext));
						relImgSevenDot.setBackgroundResource(R.drawable.shape_circle_red);

					}
					else {
						relImgSevenDot.setVisibility(View.GONE);

					}
				}
				else if (PreferenceManager.getButtonSevenTabId(mContext).equalsIgnoreCase(TAB_CCAS_REG)) {
                    if(!(PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgSevenDot.setVisibility(View.VISIBLE);
                        relImgSevenDot.setText(PreferenceManager.getCcaBadge(mContext));
                        relImgSevenDot.setBackgroundResource(R.drawable.shape_circle_red);
                    }
                    else if((PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgSevenDot.setVisibility(View.VISIBLE);
                        relImgSevenDot.setText(PreferenceManager.getCcaEditedBadge(mContext));
                        relImgSevenDot.setBackgroundResource(R.drawable.shape_circle_navy);

                    }
                    else if(!(PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgSevenDot.setVisibility(View.VISIBLE);
                        relImgSevenDot.setText(PreferenceManager.getCcaBadge(mContext));
                        relImgSevenDot.setBackgroundResource(R.drawable.shape_circle_red);

                    }
                    else {
                        relImgSevenDot.setVisibility(View.GONE);

                    }
                } else if (PreferenceManager.getButtonSevenTabId(mContext).equalsIgnoreCase(TAB_SPORTS_REG)) {
                    if(!(PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgSevenDot.setVisibility(View.VISIBLE);
                        relImgSevenDot.setText(PreferenceManager.getSportsBadge(mContext));
                        relImgSevenDot.setBackgroundResource(R.drawable.shape_circle_red);
                    }
                    else if((PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgSevenDot.setVisibility(View.VISIBLE);
                        relImgSevenDot.setText(PreferenceManager.getSportsEditedBadge(mContext));
                        relImgSevenDot.setBackgroundResource(R.drawable.shape_circle_navy);

                    }
                    else if(!(PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgSevenDot.setVisibility(View.VISIBLE);
                        relImgSevenDot.setText(PreferenceManager.getSportsBadge(mContext));
                        relImgSevenDot.setBackgroundResource(R.drawable.shape_circle_red);

                    }
                    else {
                        relImgSevenDot.setVisibility(View.GONE);

                    }
                }

				else if (PreferenceManager.getButtonSevenTabId(mContext).equalsIgnoreCase(TAB_UNIVERSITY_GUIDANCE_REG)) {
					if(!(PreferenceManager.getUniversity_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase("0"))))
					{
						relImgSevenDot.setVisibility(View.VISIBLE);
						relImgSevenDot.setText(PreferenceManager.getUniversity_badge(mContext));
						relImgSevenDot.setBackgroundResource(R.drawable.shape_circle_red);
					}
					else if((PreferenceManager.getUniversity_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase("0"))) && (!(PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase(""))))
					{
						relImgSevenDot.setVisibility(View.VISIBLE);
						relImgSevenDot.setText(PreferenceManager.getUniversity_edit_badge(mContext));
						relImgSevenDot.setBackgroundResource(R.drawable.shape_circle_navy);

					}
					else if(!(PreferenceManager.getUniversity_badge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase("0"))))
					{
						relImgSevenDot.setVisibility(View.VISIBLE);
						relImgSevenDot.setText(PreferenceManager.getUniversity_badge(mContext));
						relImgSevenDot.setBackgroundResource(R.drawable.shape_circle_red);

					}
					else {
						relImgSevenDot.setVisibility(View.GONE);

					}
				}
				else if (PreferenceManager.getButtonSevenTabId(mContext).equalsIgnoreCase(TAB_COMMUNICATIONS_REG)) {
					if(!(PreferenceManager.getNoticeBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNoticeEditedBadge(mContext).equalsIgnoreCase("0"))))
					{
						relImgSevenDot.setVisibility(View.VISIBLE);
						relImgSevenDot.setText(PreferenceManager.getNoticeBadge(mContext));
						relImgSevenDot.setBackgroundResource(R.drawable.shape_circle_red);
					}
					else if((PreferenceManager.getNoticeBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNoticeEditedBadge(mContext).equalsIgnoreCase("0"))))
					{
						relImgSevenDot.setVisibility(View.VISIBLE);
						relImgSevenDot.setText(PreferenceManager.getNoticeEditedBadge(mContext));
						relImgSevenDot.setBackgroundResource(R.drawable.shape_circle_navy);

					}
					else if(!(PreferenceManager.getNoticeBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getNoticeEditedBadge(mContext).equalsIgnoreCase("0"))))
					{
						relImgSevenDot.setVisibility(View.VISIBLE);
						relImgSevenDot.setText(PreferenceManager.getNoticeBadge(mContext));
						relImgSevenDot.setBackgroundResource(R.drawable.shape_circle_red);

					}
					else {
						relImgSevenDot.setVisibility(View.GONE);

					}

				}
                else
                {
                    relImgSevenDot.setVisibility(View.GONE);
                }
			} else if (v == mRelEight) {
				mImgEight.setImageDrawable(AppController.mListImgArrays.getDrawable(position));
				String relTwoStr="";
				if (AppController.listitemArrays[position].toString().equalsIgnoreCase(CCAS))
				{
					relTwoStr=CCAS;
				}
				else
				{
					relTwoStr=AppController.listitemArrays[position].toUpperCase();

				}
				mTxtEight.setText(relTwoStr);
				getTabId(AppController.listitemArrays[position]);
				PreferenceManager.setButtonEightTabId(mContext, TAB_ID);
				setBackgroundColorForView(AppController.listitemArrays[position], mRelEight);
				PreferenceManager.setButtonEightTextImage(mContext,
						Integer.toString(position));
                if (PreferenceManager.getButtonEightTabId(mContext).equalsIgnoreCase(TAB_CALENDAR_REG)) {
                    if(!(PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgEightDot.setVisibility(View.VISIBLE);
                        relImgEightDot.setText(PreferenceManager.getCalendarBadge(mContext));
                        relImgEightDot.setBackgroundResource(R.drawable.shape_circle_red);

                    }
                    else if((PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgEightDot.setVisibility(View.VISIBLE);

                        relImgEightDot.setText(PreferenceManager.getCalendarEditedBadge(mContext));
                        relImgEightDot.setBackgroundResource(R.drawable.shape_circle_navy);

                    }
                    else if(!(PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgEightDot.setVisibility(View.VISIBLE);

                        relImgEightDot.setText(PreferenceManager.getCalendarBadge(mContext));
                        relImgEightDot.setBackgroundResource(R.drawable.shape_circle_red);

                    }
                    else {
                        relImgEightDot.setVisibility(View.GONE);
                    }

                } else if (PreferenceManager.getButtonEightTabId(mContext).equalsIgnoreCase(TAB_NOTIFICATIONS_REG)) {
                    if(!(PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgEightDot.setVisibility(View.VISIBLE);
                        relImgEightDot.setText(PreferenceManager.getNotificationBadge(mContext));
                        relImgEightDot.setBackgroundResource(R.drawable.shape_circle_red);
                    }
                    else if((PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgEightDot.setVisibility(View.VISIBLE);
                        relImgEightDot.setText(PreferenceManager.getNotificationEditedBadge(mContext));
                        relImgEightDot.setBackgroundResource(R.drawable.shape_circle_navy);

                    }
                    else if(!(PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgEightDot.setVisibility(View.VISIBLE);
                        relImgEightDot.setText(PreferenceManager.getNotificationBadge(mContext));
                        relImgEightDot.setBackgroundResource(R.drawable.shape_circle_red);

                    }
                    else {
                        relImgEightDot.setVisibility(View.GONE);

                    }
                } else if (PreferenceManager.getButtonEightTabId(mContext).equalsIgnoreCase(TAB_REPORT_REG)) {
                    if(!(PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgEightDot.setVisibility(View.VISIBLE);
                        relImgEightDot.setText(PreferenceManager.getReportsBadge(mContext));
                        relImgEightDot.setBackgroundResource(R.drawable.shape_circle_red);
                    }
                    else if((PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgEightDot.setVisibility(View.VISIBLE);
                        relImgEightDot.setText(PreferenceManager.getReportsEditedBadge(mContext));
                        relImgEightDot.setBackgroundResource(R.drawable.shape_circle_navy);

                    }
                    else if(!(PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgEightDot.setVisibility(View.VISIBLE);
                        relImgEightDot.setText(PreferenceManager.getReportsBadge(mContext));
                        relImgEightDot.setBackgroundResource(R.drawable.shape_circle_red);

                    }
                    else {
                        relImgEightDot.setVisibility(View.GONE);

                    }
                } else if (PreferenceManager.getButtonEightTabId(mContext).equalsIgnoreCase(TAB_TRIPS_REG)) {
					if(!(PreferenceManager.getPaymentitem_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getPaymentitem_edit_badge(mContext).equalsIgnoreCase("0"))))
					{
						relImgEightDot.setVisibility(View.VISIBLE);
						relImgEightDot.setText(PreferenceManager.getPaymentitem_badge(mContext));
						relImgEightDot.setBackgroundResource(R.drawable.shape_circle_red);
					}
					else if((PreferenceManager.getPaymentitem_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getPaymentitem_edit_badge(mContext).equalsIgnoreCase("0"))))
					{
						relImgEightDot.setVisibility(View.VISIBLE);
						relImgEightDot.setText(PreferenceManager.getPaymentitem_edit_badge(mContext));
						relImgEightDot.setBackgroundResource(R.drawable.shape_circle_navy);

					}
					else if(!(PreferenceManager.getPaymentitem_badge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getPaymentitem_edit_badge(mContext).equalsIgnoreCase("0"))))
					{
						relImgEightDot.setVisibility(View.VISIBLE);
						relImgEightDot.setText(PreferenceManager.getPaymentitem_badge(mContext));
						relImgEightDot.setBackgroundResource(R.drawable.shape_circle_red);

					}
					else {
						relImgEightDot.setVisibility(View.GONE);

					}
				}
                else if (PreferenceManager.getButtonEightTabId(mContext).equalsIgnoreCase(TAB_CCAS_REG)) {
                    if(!(PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgEightDot.setVisibility(View.VISIBLE);
                        relImgEightDot.setText(PreferenceManager.getCcaBadge(mContext));
                        relImgEightDot.setBackgroundResource(R.drawable.shape_circle_red);
                    }
                    else if((PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgEightDot.setVisibility(View.VISIBLE);
                        relImgEightDot.setText(PreferenceManager.getCcaEditedBadge(mContext));
                        relImgEightDot.setBackgroundResource(R.drawable.shape_circle_navy);

                    }
                    else if(!(PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgEightDot.setVisibility(View.VISIBLE);
                        relImgEightDot.setText(PreferenceManager.getCcaBadge(mContext));
                        relImgEightDot.setBackgroundResource(R.drawable.shape_circle_red);

                    }
                    else {
                        relImgEightDot.setVisibility(View.GONE);

                    }
                } else if (PreferenceManager.getButtonEightTabId(mContext).equalsIgnoreCase(TAB_SPORTS_REG)) {
                    if(!(PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgEightDot.setVisibility(View.VISIBLE);
                        relImgEightDot.setText(PreferenceManager.getSportsBadge(mContext));
                        relImgEightDot.setBackgroundResource(R.drawable.shape_circle_red);
                    }
                    else if((PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgEightDot.setVisibility(View.VISIBLE);
                        relImgEightDot.setText(PreferenceManager.getSportsEditedBadge(mContext));
                        relImgEightDot.setBackgroundResource(R.drawable.shape_circle_navy);

                    }
                    else if(!(PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgEightDot.setVisibility(View.VISIBLE);
                        relImgEightDot.setText(PreferenceManager.getSportsBadge(mContext));
                        relImgEightDot.setBackgroundResource(R.drawable.shape_circle_red);

                    }
                    else {
                        relImgEightDot.setVisibility(View.GONE);

                    }
                }

				else if (PreferenceManager.getButtonEightTabId(mContext).equalsIgnoreCase(TAB_UNIVERSITY_GUIDANCE_REG)) {
					if(!(PreferenceManager.getUniversity_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase("0"))))
					{
						relImgEightDot.setVisibility(View.VISIBLE);
						relImgEightDot.setText(PreferenceManager.getUniversity_badge(mContext));
						relImgEightDot.setBackgroundResource(R.drawable.shape_circle_red);
					}
					else if((PreferenceManager.getUniversity_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase("0"))) && (!(PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase(""))))
					{
						relImgEightDot.setVisibility(View.VISIBLE);
						relImgEightDot.setText(PreferenceManager.getUniversity_edit_badge(mContext));
						relImgEightDot.setBackgroundResource(R.drawable.shape_circle_navy);

					}
					else if(!(PreferenceManager.getUniversity_badge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase("0"))))
					{
						relImgEightDot.setVisibility(View.VISIBLE);
						relImgEightDot.setText(PreferenceManager.getUniversity_badge(mContext));
						relImgEightDot.setBackgroundResource(R.drawable.shape_circle_red);

					}
					else {
						relImgEightDot.setVisibility(View.GONE);

					}
				}
				else if (PreferenceManager.getButtonEightTabId(mContext).equalsIgnoreCase(TAB_COMMUNICATIONS_REG)) {
					if(!(PreferenceManager.getNoticeBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNoticeEditedBadge(mContext).equalsIgnoreCase("0"))))
					{
						relImgEightDot.setVisibility(View.VISIBLE);
						relImgEightDot.setText(PreferenceManager.getNoticeBadge(mContext));
						relImgEightDot.setBackgroundResource(R.drawable.shape_circle_red);
					}
					else if((PreferenceManager.getNoticeBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNoticeEditedBadge(mContext).equalsIgnoreCase("0"))))
					{
						relImgEightDot.setVisibility(View.VISIBLE);
						relImgEightDot.setText(PreferenceManager.getNoticeEditedBadge(mContext));
						relImgEightDot.setBackgroundResource(R.drawable.shape_circle_navy);

					}
					else if(!(PreferenceManager.getNoticeBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getNoticeEditedBadge(mContext).equalsIgnoreCase("0"))))
					{
						relImgEightDot.setVisibility(View.VISIBLE);
						relImgEightDot.setText(PreferenceManager.getNoticeBadge(mContext));
						relImgEightDot.setBackgroundResource(R.drawable.shape_circle_red);

					}
					else {
						relImgEightDot.setVisibility(View.GONE);

					}

				}
                else
                {
                    relImgEightDot.setVisibility(View.GONE);
                }
			} else if (v == mRelNine) {
				mImgNine.setImageDrawable(AppController.mListImgArrays.getDrawable(position));
				String relTwoStr="";
				if (AppController.listitemArrays[position].toString().equalsIgnoreCase(CCAS))
				{
					relTwoStr=CCAS;
				}
				else
				{
					relTwoStr=AppController.listitemArrays[position].toUpperCase();

				}
				mTxtNine.setText(relTwoStr);
				getTabId(AppController.listitemArrays[position]);
				PreferenceManager.setButtonNineTabId(mContext, TAB_ID);
				setBackgroundColorForView(AppController.listitemArrays[position], mRelNine);
				PreferenceManager.setButtonNineTextImage(mContext,
						Integer.toString(position));
                if (PreferenceManager.getButtonNineTabId(mContext).equalsIgnoreCase(TAB_CALENDAR_REG)) {
                    if(!(PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgNineDot.setVisibility(View.VISIBLE);
                        relImgNineDot.setText(PreferenceManager.getCalendarBadge(mContext));
                        relImgNineDot.setBackgroundResource(R.drawable.shape_circle_red);

                    }
                    else if((PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgNineDot.setVisibility(View.VISIBLE);

                        relImgNineDot.setText(PreferenceManager.getCalendarEditedBadge(mContext));
                        relImgNineDot.setBackgroundResource(R.drawable.shape_circle_navy);

                    }
                    else if(!(PreferenceManager.getCalendarBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getCalendarEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgNineDot.setVisibility(View.VISIBLE);

                        relImgNineDot.setText(PreferenceManager.getCalendarBadge(mContext));
                        relImgNineDot.setBackgroundResource(R.drawable.shape_circle_red);

                    }
                    else {
                        relImgNineDot.setVisibility(View.GONE);
                    }

                } else if (PreferenceManager.getButtonNineTabId(mContext).equalsIgnoreCase(TAB_NOTIFICATIONS_REG)) {
                    if(!(PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgNineDot.setVisibility(View.VISIBLE);
                        relImgNineDot.setText(PreferenceManager.getNotificationBadge(mContext));
                        relImgNineDot.setBackgroundResource(R.drawable.shape_circle_red);
                    }
                    else if((PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgNineDot.setVisibility(View.VISIBLE);
                        relImgNineDot.setText(PreferenceManager.getNotificationEditedBadge(mContext));
                        relImgNineDot.setBackgroundResource(R.drawable.shape_circle_navy);

                    }
                    else if(!(PreferenceManager.getNotificationBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getNotificationEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgNineDot.setVisibility(View.VISIBLE);
                        relImgNineDot.setText(PreferenceManager.getNotificationBadge(mContext));
                        relImgNineDot.setBackgroundResource(R.drawable.shape_circle_red);

                    }
                    else {
                        relImgNineDot.setVisibility(View.GONE);

                    }
                } else if (PreferenceManager.getButtonNineTabId(mContext).equalsIgnoreCase(TAB_REPORT_REG)) {
                    if(!(PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgNineDot.setVisibility(View.VISIBLE);
                        relImgNineDot.setText(PreferenceManager.getReportsBadge(mContext));
                        relImgNineDot.setBackgroundResource(R.drawable.shape_circle_red);
                    }
                    else if((PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgNineDot.setVisibility(View.VISIBLE);
                        relImgNineDot.setText(PreferenceManager.getReportsEditedBadge(mContext));
                        relImgNineDot.setBackgroundResource(R.drawable.shape_circle_navy);

                    }
                    else if(!(PreferenceManager.getReportsBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getReportsEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgNineDot.setVisibility(View.VISIBLE);
                        relImgNineDot.setText(PreferenceManager.getReportsBadge(mContext));
                        relImgNineDot.setBackgroundResource(R.drawable.shape_circle_red);

                    }
                    else {
                        relImgNineDot.setVisibility(View.GONE);

                    }
                }else if (PreferenceManager.getButtonNineTabId(mContext).equalsIgnoreCase(TAB_TRIPS_REG)) {
					if(!(PreferenceManager.getPaymentitem_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getPaymentitem_edit_badge(mContext).equalsIgnoreCase("0"))))
					{
						relImgNineDot.setVisibility(View.VISIBLE);
						relImgNineDot.setText(PreferenceManager.getPaymentitem_badge(mContext));
						relImgNineDot.setBackgroundResource(R.drawable.shape_circle_red);
					}
					else if((PreferenceManager.getPaymentitem_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getPaymentitem_edit_badge(mContext).equalsIgnoreCase("0"))))
					{
						relImgNineDot.setVisibility(View.VISIBLE);
						relImgNineDot.setText(PreferenceManager.getPaymentitem_edit_badge(mContext));
						relImgNineDot.setBackgroundResource(R.drawable.shape_circle_navy);

					}
					else if(!(PreferenceManager.getPaymentitem_badge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getPaymentitem_edit_badge(mContext).equalsIgnoreCase("0"))))
					{
						relImgNineDot.setVisibility(View.VISIBLE);
						relImgNineDot.setText(PreferenceManager.getPaymentitem_badge(mContext));
						relImgNineDot.setBackgroundResource(R.drawable.shape_circle_red);

					}
					else {
						relImgNineDot.setVisibility(View.GONE);

					}
				}
                else if (PreferenceManager.getButtonNineTabId(mContext).equalsIgnoreCase(TAB_CCAS_REG)) {
                    if(!(PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgNineDot.setVisibility(View.VISIBLE);
                        relImgNineDot.setText(PreferenceManager.getCcaBadge(mContext));
                        relImgNineDot.setBackgroundResource(R.drawable.shape_circle_red);
                    }
                    else if((PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgNineDot.setVisibility(View.VISIBLE);
                        relImgNineDot.setText(PreferenceManager.getCcaEditedBadge(mContext));
                        relImgNineDot.setBackgroundResource(R.drawable.shape_circle_navy);

                    }
                    else if(!(PreferenceManager.getCcaBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getCcaEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgNineDot.setVisibility(View.VISIBLE);
                        relImgNineDot.setText(PreferenceManager.getCcaBadge(mContext));
                        relImgNineDot.setBackgroundResource(R.drawable.shape_circle_red);

                    }
                    else {
                        relImgNineDot.setVisibility(View.GONE);

                    }
                } else if (PreferenceManager.getButtonNineTabId(mContext).equalsIgnoreCase(TAB_SPORTS_REG)) {
                    if(!(PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgNineDot.setVisibility(View.VISIBLE);
                        relImgNineDot.setText(PreferenceManager.getSportsBadge(mContext));
                        relImgNineDot.setBackgroundResource(R.drawable.shape_circle_red);
                    }
                    else if((PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgNineDot.setVisibility(View.VISIBLE);
                        relImgNineDot.setText(PreferenceManager.getSportsEditedBadge(mContext));
                        relImgNineDot.setBackgroundResource(R.drawable.shape_circle_navy);

                    }
                    else if(!(PreferenceManager.getSportsBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getSportsEditedBadge(mContext).equalsIgnoreCase("0"))))
                    {
                        relImgNineDot.setVisibility(View.VISIBLE);
                        relImgNineDot.setText(PreferenceManager.getSportsBadge(mContext));
                        relImgNineDot.setBackgroundResource(R.drawable.shape_circle_red);

                    }
                    else {
                        relImgNineDot.setVisibility(View.GONE);

                    }
                }

				else if (PreferenceManager.getButtonNineTabId(mContext).equalsIgnoreCase(TAB_UNIVERSITY_GUIDANCE_REG)) {
					if(!(PreferenceManager.getUniversity_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase("0"))))
					{
						relImgNineDot.setVisibility(View.VISIBLE);
						relImgNineDot.setText(PreferenceManager.getUniversity_badge(mContext));
						relImgNineDot.setBackgroundResource(R.drawable.shape_circle_red);
					}
					else if((PreferenceManager.getUniversity_badge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase("0"))) && (!(PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase(""))))
					{
						relImgNineDot.setVisibility(View.VISIBLE);
						relImgNineDot.setText(PreferenceManager.getUniversity_edit_badge(mContext));
						relImgNineDot.setBackgroundResource(R.drawable.shape_circle_navy);

					}
					else if(!(PreferenceManager.getUniversity_badge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getUniversity_edit_badge(mContext).equalsIgnoreCase("0"))))
					{
						relImgNineDot.setVisibility(View.VISIBLE);
						relImgNineDot.setText(PreferenceManager.getUniversity_badge(mContext));
						relImgNineDot.setBackgroundResource(R.drawable.shape_circle_red);

					}
					else {
						relImgNineDot.setVisibility(View.GONE);

					}
				}
				else if (PreferenceManager.getButtonNineTabId(mContext).equalsIgnoreCase(TAB_COMMUNICATIONS_REG)) {
					if(!(PreferenceManager.getNoticeBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNoticeEditedBadge(mContext).equalsIgnoreCase("0"))))
					{
						relImgNineDot.setVisibility(View.VISIBLE);
						relImgNineDot.setText(PreferenceManager.getNoticeBadge(mContext));
						relImgNineDot.setBackgroundResource(R.drawable.shape_circle_red);
					}
					else if((PreferenceManager.getNoticeBadge(mContext).equalsIgnoreCase("0"))&&(!(PreferenceManager.getNoticeEditedBadge(mContext).equalsIgnoreCase("0"))))
					{
						relImgNineDot.setVisibility(View.VISIBLE);
						relImgNineDot.setText(PreferenceManager.getNoticeEditedBadge(mContext));
						relImgNineDot.setBackgroundResource(R.drawable.shape_circle_navy);

					}
					else if(!(PreferenceManager.getNoticeBadge(mContext).equalsIgnoreCase("0"))&&((PreferenceManager.getNoticeEditedBadge(mContext).equalsIgnoreCase("0"))))
					{
						relImgNineDot.setVisibility(View.VISIBLE);
						relImgNineDot.setText(PreferenceManager.getNoticeBadge(mContext));
						relImgNineDot.setBackgroundResource(R.drawable.shape_circle_red);

					}
					else {
						relImgNineDot.setVisibility(View.GONE);

					}

				}
                else
                {
                    relImgNineDot.setVisibility(View.GONE);
                }
			}
		}
		v = null;
		viewTouched = null;
	}

	/*******************************************************
	 * Method name : setBackgroundColorForView Description : setbackground color
	 * for view Parameters : view Return type : void Date : Nov 11, 2014 Author
	 * : Rijo K Jose
	 *****************************************************/
	private void setBackgroundColorForView(String buttonText, View v) {
		if (v == mRelOne) {
			v.setBackgroundColor(mContext.getResources().getColor(
					R.color.transparent));
			saveButtonBgColor(v,
					mContext.getResources().getColor(R.color.transparent));
			return;
		} else if (v == mRelTwo) {
			v.setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
			saveButtonBgColor(v, mContext.getResources().getColor(R.color.transparent));
			return;
		} else if (v == mRelThree) {
			v.setBackgroundColor(mContext.getResources().getColor(
					R.color.transparent));
			saveButtonBgColor(v,
					mContext.getResources().getColor(R.color.transparent));
			return;
		} else if (v == mRelFour) {
			v.setBackgroundColor(mContext.getResources().getColor(
					R.color.transparent));
			saveButtonBgColor(v,
					mContext.getResources().getColor(R.color.transparent));
			return;
		} else if (v == mRelFive) {
			v.setBackgroundColor(mContext.getResources().getColor(
					R.color.transparent));
			saveButtonBgColor(v,
					mContext.getResources().getColor(R.color.transparent));
			return;
		} else if (v == mRelSix) {
			v.setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
			saveButtonBgColor(v, mContext.getResources().getColor(R.color.transparent));
			return;
		} else if (v == mRelSeven) {
			v.setBackgroundColor(mContext.getResources().getColor(
					R.color.transparent));
			saveButtonBgColor(v,
					mContext.getResources().getColor(R.color.transparent));
			return;
		} else if (v == mRelEight) {
			v.setBackgroundColor(mContext.getResources().getColor(
					R.color.transparent));
			saveButtonBgColor(v,
					mContext.getResources().getColor(R.color.transparent));
			return;
		} else if (v == mRelNine) {
			v.setBackgroundColor(mContext.getResources()
					.getColor(R.color.transparent));
			saveButtonBgColor(v, mContext.getResources()
					.getColor(R.color.transparent));
			return;
		}
	}

	/*******************************************************
	 * Method name : saveButtonBgColor Description : save button bg color to
	 * preference Parameters : view, color Return type : void Date : Nov 7, 2014
	 * Author : Rijo K Jose
	 *****************************************************/

	private void saveButtonBgColor(View v, int color) {
		if (v != null) {
			if (v == mRelOne) {
				PreferenceManager.setButtonOneBg(mContext, color);
			} else if (v == mRelTwo) {
				PreferenceManager.setButtonTwoBg(mContext, color);
			} else if (v == mRelThree) {
				PreferenceManager.setButtonThreeBg(mContext, color);
			} else if (v == mRelFour) {
				PreferenceManager.setButtonFourBg(mContext, color);
			} else if (v == mRelFive) {
				PreferenceManager.setButtonFiveBg(mContext, color);
			} else if (v == mRelSix) {
				PreferenceManager.setButtonSixBg(mContext, color);
			} else if (v == mRelSeven) {
				PreferenceManager.setButtonSevenBg(mContext, color);
			} else if (v == mRelEight) {
				PreferenceManager.setButtonEightBg(mContext, color);
			} else if (v == mRelNine) {
				PreferenceManager.setButtonNineBg(mContext, color);
			}
			v = null;
		}
	}

	/*******************************************************
	 * Method name : setDragListenersForButtons Description : set drag listeners
	 * for home screen buttons Parameters : nil Return type : void Date : Nov
	 * 12, 2014 Author : Rijo K Jose
	 *****************************************************/
	@SuppressLint("NewApi")
	private void setDragListenersForButtons() {
		mRelOne.setOnDragListener(new DropListener());
		mRelTwo.setOnDragListener(new DropListener());
		mRelThree.setOnDragListener(new DropListener());
		mRelFour.setOnDragListener(new DropListener());
		mRelFive.setOnDragListener(new DropListener());
		mRelSix.setOnDragListener(new DropListener());
		mRelSeven.setOnDragListener(new DropListener());
		mRelEight.setOnDragListener(new DropListener());
		mRelNine.setOnDragListener(new DropListener());
	}

	/*******************************************************
	 * Method name : checkIntent Description : check intent for fragment
	 * transaction Parameters : tabId Return type : void Date : Nov 26, 2014
	 * Author : Rijo K Jose
	 *****************************************************/
	private void checkIntent(String tabId) {
		tabiDToProceed=tabId;
		System.out.println("tabId::" + tabId);
		Fragment mFragment = null;
		if (tabId.equalsIgnoreCase(TAB_CALENDAR_REG)) {
			if (PreferenceManager.getStaffOnly(mContext).equalsIgnoreCase("1"))
			{
				AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "This feature is not available for staff", R.drawable.exclamationicon, R.drawable.round);

			}
			else {
				mFragment = new CalendarFragment(CALENDAR, TAB_CALENDAR_REG);
				if (Build.VERSION.SDK_INT < 23) {
					//Do not need to check the permission
					fragmentIntent(mFragment);
				} else {

					if (ActivityCompat.checkSelfPermission(getActivity(), permissionsRequiredCalendar[0]) != PackageManager.PERMISSION_GRANTED
							|| ActivityCompat.checkSelfPermission(getActivity(), permissionsRequiredCalendar[1]) != PackageManager.PERMISSION_GRANTED) {
						if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissionsRequiredCalendar[0])
								|| ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissionsRequiredCalendar[1])) {
							//Show information about why you need the permission
							AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
							builder.setTitle("Need Calendar Permission");
							builder.setMessage("This module needs Calendar permissions.");
							builder.setCancelable(false);

							builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.cancel();
									ActivityCompat.requestPermissions(getActivity(), permissionsRequiredCalendar, PERMISSION_CALLBACK_CONSTANT_CALENDAR);
								}
							});
							builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.cancel();

								}
							});
							builder.show();
						} else if (calendarPermissionStatus.getBoolean(permissionsRequiredCalendar[0], false)) {
							//Previously Permission Request was cancelled with 'Dont Ask Again',
							// Redirect to Settings after showing information about why you need the permission
							AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
							builder.setTitle("Need Calendar Permission");
							builder.setMessage("This module needs Calendar permissions.");
							builder.setCancelable(false);

							builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.cancel();
									calendarToSettings = true;

									Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
									Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
									intent.setData(uri);
									startActivityForResult(intent, REQUEST_PERMISSION_CALENDAR);
									Toast.makeText(mContext, "Go to settings and grant access to calendar", Toast.LENGTH_LONG).show();
								}
							});
							builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.cancel();
									calendarToSettings = false;

								}
							});
							builder.show();
						}else if (calendarPermissionStatus.getBoolean(permissionsRequiredCalendar[1], false)) {
							//Previously Permission Request was cancelled with 'Dont Ask Again',
							// Redirect to Settings after showing information about why you need the permission
							AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
							builder.setTitle("Need Calendar Permission");
							builder.setMessage("This module needs Calendar permissions.");
							builder.setCancelable(false);

							builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.cancel();
									calendarToSettings = true;

									Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
									Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
									intent.setData(uri);
									startActivityForResult(intent, REQUEST_PERMISSION_CALENDAR);
									Toast.makeText(mContext, "Go to settings and grant access to calendar", Toast.LENGTH_LONG).show();
								}
							});
							builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.cancel();
									calendarToSettings = false;

								}
							});
							builder.show();
						} else {

							//just request the permission
//                        ActivityCompat.requestPermissions(getActivity(), permissionsRequired, PERMISSION_CALLBACK_CONSTANT_CALENDAR);
							requestPermissions(permissionsRequiredCalendar, PERMISSION_CALLBACK_CONSTANT_CALENDAR);

						}
						SharedPreferences.Editor editor = calendarPermissionStatus.edit();
						editor.putBoolean(permissionsRequiredCalendar[0],true);
						editor.commit();
					}
					else
					{
						fragmentIntent(mFragment);

					}
				}
			}
		}
		else if (tabId.equalsIgnoreCase(TAB_NOTIFICATIONS_REG)) {
			if (PreferenceManager.getStaffOnly(mContext).equalsIgnoreCase("1"))
			{
				//AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "This feature is not available for staff", R.drawable.exclamationicon, R.drawable.round);
				mFragment = new NotificationsFragmentNew(NOTIFICATIONS, TAB_NOTIFICATIONS_REG);
				fragmentIntent(mFragment);
			}
			else {
				mFragment = new NotificationsFragmentNew(NOTIFICATIONS, TAB_NOTIFICATIONS_REG);
				fragmentIntent(mFragment);
			}


		}
		else if (tabId.equalsIgnoreCase(TAB_COMMUNICATIONS_REG)) {

				mFragment = new CommunicationsFragment(COMMUNICATIONS, TAB_COMMUNICATIONS_REG);
				fragmentIntent(mFragment);



		}
		else if (tabId.equalsIgnoreCase(TAB_PARENT_ESSENTIALS_REG)) {

				mFragment = new ParentEssentialsFragment(PARENT_ESSENTIALS, TAB_PARENT_ESSENTIALS_REG);
				fragmentIntent(mFragment);

		}
		else if (tabId.equalsIgnoreCase(TAB_EARLYYEARS_REG)) {

				mFragment = new EarlyYearsFragment(EARLY_YEARS, TAB_EARLYYEARS_REG);
				fragmentIntent(mFragment);

		}
		else if (tabId.equalsIgnoreCase(TAB_PRIMARY_REG)) {
			mFragment = new PrimaryFragment(PRIMARY, TAB_PRIMARY_REG);
			fragmentIntent(mFragment);

		}
		else if (tabId.equalsIgnoreCase(TAB_SECONDARY_REG)) {
			mFragment = new SecondaryFragment(SECONDARY, TAB_SECONDARY_REG);
			fragmentIntent(mFragment);

		}
		else if (tabId.equalsIgnoreCase(TAB_IB_PROGRAMME_REG)) {
			mFragment = new IbProgrammeFragment(IB_PROGRAMME, TAB_IB_PROGRAMME_REG);
			fragmentIntent(mFragment);

		}
		else if (tabId.equalsIgnoreCase(TAB_UNIVERSITY_GUIDANCE_REG)) {
			mFragment = new UniverstyGuidanceFragment(UNIVERSITY_GUIDANCE, TAB_UNIVERSITY_GUIDANCE_REG);
			fragmentIntent(mFragment);

		}
		else if (tabId.equalsIgnoreCase(TAB_SPORTS_REG)) {
			if (PreferenceManager.getStaffOnly(mContext).equalsIgnoreCase("1"))
			{
				AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "This feature is not available for staff", R.drawable.exclamationicon, R.drawable.round);

			}
			else {
				mFragment = new SportsMainScreenFragment(SPORTS, TAB_SPORTS_REG);
				fragmentIntent(mFragment);
			}


		}
		else if (tabId.equalsIgnoreCase(TAB_PERFORMING_ARTS_REG)) {
			mFragment = new PerformingArtsFragment(PERFORMING_ARTS, TAB_PERFORMING_ARTS_REG);
			fragmentIntent(mFragment);

		}
		else if (tabId.equalsIgnoreCase(TAB_CCAS_REG)) {
			if (PreferenceManager.getStaffOnly(mContext).equalsIgnoreCase("1"))
			{
				AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "This feature is not available for staff", R.drawable.exclamationicon, R.drawable.round);

			}
			else {
				mFragment = new CcaFragmentMain(CCAS, TAB_CCAS_REG);
				fragmentIntent(mFragment);
			}

		}
		else if (tabId.equalsIgnoreCase(TAB_NAE_PROGRAMMES_REG)) {
			mFragment = new NaeProgrammesFragment(NAE_PROGRAMMES, TAB_NAE_PROGRAMMES_REG);
			fragmentIntent(mFragment);

		}
		/*else if (tabId.equalsIgnoreCase(TAB_SOCIAL_MEDIA_REG)) {
			mFragment = new SocialMediaFragment(SOCIAL_MEDIA, TAB_SOCIAL_MEDIA_REG);
			fragmentIntent(mFragment);

		}*/
		else if (tabId.equalsIgnoreCase(TAB_GALLERY_REG)) {
			if (PreferenceManager.getStaffOnly(mContext).equalsIgnoreCase("1"))
			{
				AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "This feature is not available for staff", R.drawable.exclamationicon, R.drawable.round);

			}
			else {
				mFragment = new GalleryFragment(GALLERY, TAB_GALLERY_REG);
				if (Build.VERSION.SDK_INT < 23) {
					//Do not need to check the permission
					fragmentIntent(mFragment);
				} else {

					if (ActivityCompat.checkSelfPermission(getActivity(), permissionsRequiredExternalStorage[0]) != PackageManager.PERMISSION_GRANTED
							|| ActivityCompat.checkSelfPermission(getActivity(), permissionsRequiredExternalStorage[1]) != PackageManager.PERMISSION_GRANTED) {
						if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissionsRequiredExternalStorage[0])
								|| ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissionsRequiredExternalStorage[1])) {
							//Show information about why you need the permission
							AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
							builder.setTitle("Need Storage Permission");
							builder.setMessage("This module needs Storage permissions.");

							builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.cancel();
									ActivityCompat.requestPermissions(getActivity(), permissionsRequiredExternalStorage, PERMISSION_CALLBACK_CONSTANT_EXTERNAL_STORAGE);
								}
							});
							builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.cancel();

								}
							});
							builder.show();
						} else if (externalStoragePermissionStatus.getBoolean(permissionsRequiredExternalStorage[0], false)) {
							//Previously Permission Request was cancelled with 'Dont Ask Again',
							// Redirect to Settings after showing information about why you need the permission
							AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
							builder.setTitle("Need Storage Permission");
							builder.setMessage("This module needs Storage permissions.");

							builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.cancel();
									externalStorageToSettings = true;

									Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
									Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
									intent.setData(uri);
									startActivityForResult(intent, REQUEST_PERMISSION_EXTERNAL_STORAGE);
									Toast.makeText(mContext, "Go to settings and grant access to storage", Toast.LENGTH_LONG).show();
								}
							});
							builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.cancel();
									externalStorageToSettings = false;

								}
							});
							builder.show();
						}else if (externalStoragePermissionStatus.getBoolean(permissionsRequiredExternalStorage[1], false)) {
							//Previously Permission Request was cancelled with 'Dont Ask Again',
							// Redirect to Settings after showing information about why you need the permission
							AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
							builder.setTitle("Need Storage Permission");
							builder.setMessage("This module needs Storage permissions.");
							builder.setCancelable(false);

							builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.cancel();
									externalStorageToSettings = true;

									Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
									Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
									intent.setData(uri);
									startActivityForResult(intent, REQUEST_PERMISSION_EXTERNAL_STORAGE);
									Toast.makeText(mContext, "Go to settings and grant access to storage", Toast.LENGTH_LONG).show();
								}
							});
							builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.cancel();
									externalStorageToSettings = false;

								}
							});
							builder.show();
						} else {

							//just request the permission
//                        ActivityCompat.requestPermissions(getActivity(), permissionsRequired, PERMISSION_CALLBACK_CONSTANT_CALENDAR);
							requestPermissions(permissionsRequiredExternalStorage, PERMISSION_CALLBACK_CONSTANT_EXTERNAL_STORAGE);

						}
						SharedPreferences.Editor editor = externalStoragePermissionStatus.edit();
						editor.putBoolean(permissionsRequiredExternalStorage[0],true);
						editor.commit();
					}
					else
					{
						fragmentIntent(mFragment);

					}
				}
			}

		}
		else if (tabId.equalsIgnoreCase(TAB_ABOUT_US_REG)) {
			mFragment = new AboutUsFragment(ABOUT_US, TAB_ABOUT_US_REG);
			fragmentIntent(mFragment);

		}
		else if (tabId.equalsIgnoreCase(TAB_CONTACT_US_REG)) {
			mFragment = new ContactUsFragment(CONTACT_US, TAB_CONTACT_US_REG);
			if (Build.VERSION.SDK_INT < 23) {
				//Do not need to check the permission
				fragmentIntent(mFragment);
			} else {

				if (ActivityCompat.checkSelfPermission(getActivity(), permissionsRequiredLocation[0]) != PackageManager.PERMISSION_GRANTED
						|| ActivityCompat.checkSelfPermission(getActivity(), permissionsRequiredLocation[1]) != PackageManager.PERMISSION_GRANTED) {
					if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissionsRequiredLocation[0])
							|| ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissionsRequiredLocation[1])) {
						//Show information about why you need the permission
						AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
						builder.setTitle("Need Location Permission");
						builder.setMessage("This module needs location permissions.");

						builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
								ActivityCompat.requestPermissions(getActivity(), permissionsRequiredLocation, PERMISSION_CALLBACK_CONSTANT_LOCATION);
							}
						});
						builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();

							}
						});
						builder.show();
					} else if (locationPermissionStatus.getBoolean(permissionsRequiredLocation[0], false)) {
						//Previously Permission Request was cancelled with 'Dont Ask Again',
						// Redirect to Settings after showing information about why you need the permission
						AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
						builder.setTitle("Need Location Permission");
						builder.setMessage("This module needs location permissions.");
						builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
								locationToSettings = true;

								Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
								Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
								intent.setData(uri);
								startActivityForResult(intent, REQUEST_PERMISSION_LOCATION);
								Toast.makeText(mContext, "Go to settings and grant access to location", Toast.LENGTH_LONG).show();
							}
						});
						builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
								locationToSettings = false;

							}
						});
						builder.show();
					}else if (locationPermissionStatus.getBoolean(permissionsRequiredLocation[1], false)) {
						//Previously Permission Request was cancelled with 'Dont Ask Again',
						// Redirect to Settings after showing information about why you need the permission
						AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
						builder.setTitle("Need Location Permission");
						builder.setMessage("This module needs location permissions.");
						builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
								locationToSettings = true;

								Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
								Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
								intent.setData(uri);
								startActivityForResult(intent, REQUEST_PERMISSION_LOCATION);
								Toast.makeText(mContext, "Go to settings and grant access to location", Toast.LENGTH_LONG).show();
							}
						});
						builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
								locationToSettings = false;

							}
						});
						builder.show();
					} else {

						//just request the permission
//                        ActivityCompat.requestPermissions(getActivity(), permissionsRequired, PERMISSION_CALLBACK_CONSTANT_CALENDAR);
						requestPermissions(permissionsRequiredLocation, PERMISSION_CALLBACK_CONSTANT_LOCATION);

					}
					SharedPreferences.Editor editor = locationPermissionStatus.edit();
					editor.putBoolean(permissionsRequiredLocation[0],true);
					editor.commit();
				}
				else
				{
					fragmentIntent(mFragment);

				}
			}

		}
		else if (tabId.equalsIgnoreCase(TAB_REPORT_REG)) {
			if (PreferenceManager.getStaffOnly(mContext).equalsIgnoreCase("1"))
			{
				AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "This feature is not available for staff", R.drawable.exclamationicon, R.drawable.round);

			}
			else {
				mFragment = new ReportFragment("Reports", TAB_REPORT_REG);
				fragmentIntent(mFragment);
			}


		}
		else if (tabId.equalsIgnoreCase(TAB_PERMISSION_SLIP_REG)) {
			if (PreferenceManager.getStaffOnly(mContext).equalsIgnoreCase("1"))
			{
				AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "This feature is not available for staff", R.drawable.exclamationicon, R.drawable.round);

			}
			else {
				mFragment = new PermissionSlipsFragment("Permission Forms", TAB_PERMISSION_SLIP_REG);
				fragmentIntent(mFragment);
			}


		}

	/*	else if (tabId.equalsIgnoreCase(TAB_NAS_TODAY_REG)) {
			mFragment = new NasTodayFragment(NAS_TODAY, TAB_NAS_TODAY_REG);
			fragmentIntent(mFragment);

		}*/
		else if (tabId.equalsIgnoreCase(TAB_PARENTS_MEETING_REG)) {
			if (PreferenceManager.getStaffOnly(mContext).equalsIgnoreCase("1"))
			{
				AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "This feature is not available for staff", R.drawable.exclamationicon, R.drawable.round);

			}
			else {
				mFragment = new ParentsEveningFragment(PARENT_EVENING, TAB_PARENTS_MEETING_REG);
				fragmentIntent(mFragment);
			}


		}else if (tabId.equalsIgnoreCase(TAB_ABSENSE_REG)) {
			if (PreferenceManager.getStaffOnly(mContext).equalsIgnoreCase("1"))
			{
				AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "This feature is not available for staff", R.drawable.exclamationicon, R.drawable.round);

			}
			else {
				mFragment = new AbsenceFragment(ABSENCE, TAB_ABSENSE_REG);
				fragmentIntent(mFragment);
			}


		}else if (tabId.equalsIgnoreCase(TAB_PARENTS_ASSOCIATION_REG)) {
			if (PreferenceManager.getStaffOnly(mContext).equalsIgnoreCase("1"))
			{
				AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "This feature is not available for staff", R.drawable.exclamationicon, R.drawable.round);

			}
			else {
				mFragment = new ParentAssociationsFragment(PARENTS_ASSOCIATION, TAB_PARENTS_ASSOCIATION_REG);
				fragmentIntent(mFragment);
			}


		}
		else if (tabId.equalsIgnoreCase(TAB_TRIPS_REG)) {
			if (PreferenceManager.getStaffOnly(mContext).equalsIgnoreCase("1"))
			{
				AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "This feature is not available for staff", R.drawable.exclamationicon, R.drawable.round);

			}
			else {
				mFragment = new TripsFragmentNew(TRIPS, TAB_TRIPS_REG);
				fragmentIntent(mFragment);
			}


		}
		else if (tabId.equalsIgnoreCase(TAB_CANTEEN_REG)) {
			mFragment = new CanteenFragmentNew(CANTEEN, TAB_CANTEEN_REG);
			fragmentIntent(mFragment);

		}
//		else if (tabId.equalsIgnoreCase(TAB_CANTEEN_REG)) {
//			mFragment = new CanteenFragmentNew(CANTEEN, TAB_CANTEEN_REG);
//			fragmentIntent(mFragment);
//
//		}

//		if (mFragment != null) {
//
//			System.out.println("title:"+AppController.mTitles);
//			FragmentManager fragmentManager = getActivity()
//					.getSupportFragmentManager();
//			fragmentManager.beginTransaction()
//					.add(R.id.frame_container, mFragment, AppController.mTitles)
//					.addToBackStack(AppController.mTitles).commit();
//
//		}

	}
	void fragmentIntent(Fragment mFragment)
	{

		if (mFragment != null) {

			System.out.println("title:" + AppController.mTitles);
			FragmentManager fragmentManager = getActivity()
					.getSupportFragmentManager();
			fragmentManager.beginTransaction()
					.add(R.id.frame_container, mFragment, AppController.mTitles)
					.addToBackStack(AppController.mTitles).commitAllowingStateLoss();//commit

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		if (v == mRelOne) {
			INTENT_TAB_ID = PreferenceManager.getButtonOneTabId(mContext);
			checkIntent(INTENT_TAB_ID);
		} else if (v == mRelTwo) {
			INTENT_TAB_ID = PreferenceManager.getButtonTwoTabId(mContext);
			checkIntent(INTENT_TAB_ID);
		} else if (v == mRelThree) {
			INTENT_TAB_ID = PreferenceManager.getButtonThreeTabId(mContext);
			checkIntent(INTENT_TAB_ID);
		} else if (v == mRelFour) {
			INTENT_TAB_ID = PreferenceManager.getButtonFourTabId(mContext);
			checkIntent(INTENT_TAB_ID);
		} else if (v == mRelFive) {
			INTENT_TAB_ID = PreferenceManager.getButtonFiveTabId(mContext);
			checkIntent(INTENT_TAB_ID);
		} else if (v == mRelSix) {
			INTENT_TAB_ID = PreferenceManager.getButtonSixTabId(mContext);
			checkIntent(INTENT_TAB_ID);
		} else if (v == mRelSeven) {
			INTENT_TAB_ID = PreferenceManager.getButtonSevenTabId(mContext);
			checkIntent(INTENT_TAB_ID);
		} else if (v == mRelEight) {
			INTENT_TAB_ID = PreferenceManager.getButtonEightTabId(mContext);
			checkIntent(INTENT_TAB_ID);
		} else if (v == mRelNine) {
			INTENT_TAB_ID = PreferenceManager.getButtonNineTabId(mContext);
			checkIntent(INTENT_TAB_ID);
		} /*else if (v == mBannerImg) {
			INTENT_TAB_ID = TAB_NEWS;
			checkIntent(INTENT_TAB_ID);
		}*/
	}
	public void getBanner() {

		try {
			final VolleyWrapper manager = new VolleyWrapper(URL_GET_USER_BANNER);
			String[] name = new String[]{JTAG_ACCESSTOKEN,"users_id"};
			String[] value = new String[]{PreferenceManager.getAccessToken(mContext),PreferenceManager.getUserId(mContext)};
			homeBannerUrlImageArray = new ArrayList<>();


			manager.getResponsePOST(mContext, 11, name, value, new VolleyWrapper.ResponseListener() {

				@Override
				public void responseSuccess(String successResponse) {
					String responsCode = "";
					if (successResponse != null) {
						try {
							JSONObject rootObject = new JSONObject(successResponse);
							if (rootObject.optString(JTAG_RESPONSE) != null) {
								responsCode = rootObject.optString(JTAG_RESPONSECODE);
								if (responsCode.equals(RESPONSE_SUCCESS)) {
									JSONObject respObject = rootObject.getJSONObject(JTAG_RESPONSE);
									String statusCode = respObject.optString(JTAG_STATUSCODE);
									if (statusCode.equals(STATUS_SUCCESS)) {

										JSONArray dataArray = respObject.getJSONArray(JTAG_RESPONSE_DATA_ARRAY);
										if (dataArray.length() > 0) {
											for (int i = 0; i < dataArray.length(); i++) {
//												JSONObject dataObject = dataArray.getJSONObject(i);
												homeBannerUrlImageArray.add(dataArray.optString(i));
												PreferenceManager.saveArrayList(homeBannerUrlImageArray,"home_banner",mContext);
											}
											bannerImagePager.setAdapter(new ImagePagerDrawableAdapter(PreferenceManager.getArrayList("home_banner",mContext), getActivity()));


										} else {
											//CustomStatusDialog();
//											bannerImagePager.setBackgroundResource(R.drawable.banner);
											bannerImagePager.setBackgroundResource(R.drawable.default_banner);
//											Toast.makeText(mContext, "Failure", Toast.LENGTH_SHORT).show();
										}
										String android_app_version = respObject.optString(JTAG_ANDROID_APP_VERSION);
										PreferenceManager.setVersionFromApi(mContext,android_app_version);
										String versionFromPreference = PreferenceManager.getVersionFromApi(mContext).replace(".","");
										int versionNumberAsInteger = Integer.parseInt(versionFromPreference);
										String replaceVersion = AppUtils.getVersionInfo(mContext).replace(".","");
										int replaceCurrentVersion=Integer.parseInt(replaceVersion);
										System.out.println("versionNumberAsInteger"+versionNumberAsInteger);
										System.out.println("replaceCurrentVersion"+replaceCurrentVersion);
										if (!(PreferenceManager.getVersionFromApi(mContext).equalsIgnoreCase(""))) {
											if(versionNumberAsInteger >replaceCurrentVersion) {

												AppUtils.showDialogAlertUpdate(mContext);
											}

										}

									}
								}
								else if (responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) ||
										responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
										responsCode.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
									AppUtils.postInitParam(getActivity(), new AppUtils.GetAccessTokenInterface() {
										@Override
										public void getAccessToken() {
										}
									});
									getBanner();

								}
							} else if (responsCode.equals(RESPONSE_ERROR)) {
//								CustomStatusDialog(RESPONSE_FAILURE);

							}
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}

				@Override
				public void responseFailure(String failureResponse) {
					// CustomStatusDialog(RESPONSE_FAILURE);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}


	}
	public void getBadge() {

		try {
			final VolleyWrapper manager = new VolleyWrapper(URL_GET_USER_BADGE_COUNT);
			String[] name = new String[]{JTAG_ACCESSTOKEN,"users_id"};
			String[] value = new String[]{PreferenceManager.getAccessToken(mContext),PreferenceManager.getUserId(mContext)};
			manager.getResponsePOST(mContext, 14, name, value, new VolleyWrapper.ResponseListener() {

				@Override
				public void responseSuccess(String successResponse) {
					String responsCode = "";
					if (successResponse != null) {
						try {
							JSONObject rootObject = new JSONObject(successResponse);
							if (rootObject.optString(JTAG_RESPONSE) != null) {
								responsCode = rootObject.optString(JTAG_RESPONSECODE);
								if (responsCode.equals(RESPONSE_SUCCESS)) {
									JSONObject respObject = rootObject.getJSONObject(JTAG_RESPONSE);
									String statusCode = respObject.optString(JTAG_STATUSCODE);
									if (statusCode.equals(STATUS_SUCCESS)) {
										String calendar_badge=respObject.optString("calendar_badge");
										PreferenceManager.setCalendarBadge(mContext,calendar_badge);
										String notification_badge=respObject.optString("notification_badge");
										PreferenceManager.setNotificationBadge(mContext,notification_badge);
										String whole_school_coming_up_badge=respObject.optString("whole_school_coming_up_badge");
										PreferenceManager.setNoticeBadge(mContext,whole_school_coming_up_badge);
										String sports_badge=respObject.optString("sports_badge");
										PreferenceManager.setSportsBadge(mContext,sports_badge);
										String reports_badge=respObject.optString("reports_badge");
										PreferenceManager.setReportsBadge(mContext,reports_badge);
										String cca_badge=respObject.optString("cca_badge");
										PreferenceManager.setCcaBadge(mContext,cca_badge);
										String paymentitem_badge=respObject.optString("paymentitem_badge");
										PreferenceManager.setPaymentitem_badge(mContext,paymentitem_badge);
                                        String calendar_edited_badge=respObject.optString("calendar_edited_badge");
                                        PreferenceManager.setCalendarEditedBadge(mContext,calendar_edited_badge);
                                        String notification_edited_badge=respObject.optString("notification_edited_badge");
                                        PreferenceManager.setNotificationEditedBadge(mContext,notification_edited_badge);
                                        String whole_school_coming_up_edited_badge=respObject.optString("whole_school_coming_up_edited_badge");
                                        PreferenceManager.setNoticeEditedBadge(mContext,whole_school_coming_up_edited_badge);
                                        String report_edited_badge=respObject.optString("reports_edited_badge");
                                        PreferenceManager.setReportsEditedBadge(mContext,report_edited_badge);
                                        String cca_edited_badge=respObject.optString("cca_edited_badge");
                                        PreferenceManager.setCcaEditedBadge(mContext,cca_edited_badge);
                                        String sports_edited_badge=respObject.optString("sports_edited_badge");
                                        PreferenceManager.setSportsEditedBadge(mContext,sports_edited_badge);
										String paymentitem_edit_badge=respObject.optString("paymentitem_edit_badge");
										PreferenceManager.setPaymentitem_edit_badge(mContext,paymentitem_edit_badge);

										String university_badge=respObject.optString("guidance_calendar_badge");
										PreferenceManager.setUniversity_badge(mContext,university_badge);
										String university_edit_badge=respObject.optString("guidance_calendar_edited_badge");
										PreferenceManager.setUniversity_edit_badge(mContext,university_edit_badge);
										getButtonBgAndTextImages();
										HomeListAppCompatActivity.mListAdapter.notifyDataSetChanged();


									}
								}
								else if (responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) ||
										responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
										responsCode.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
									AppUtils.postInitParam(getActivity(), new AppUtils.GetAccessTokenInterface() {
										@Override
										public void getAccessToken() {
										}
									});
									getBadge();

								}
							} else if (responsCode.equals(RESPONSE_ERROR)) {
//								CustomStatusDialog(RESPONSE_FAILURE);

							}
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}

				@Override
				public void responseFailure(String failureResponse) {
					// CustomStatusDialog(RESPONSE_FAILURE);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}


	}


	private void proceedAfterPermission(String tabiDFromProceed) {

		Fragment mFragment = null;
		if (tabiDFromProceed.equalsIgnoreCase(TAB_CALENDAR_REG))
		{
			if (PreferenceManager.getStaffOnly(mContext).equalsIgnoreCase("1"))
			{
				AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "This feature is not available for staff", R.drawable.exclamationicon, R.drawable.round);

			}
			else {
				mFragment = new CalendarFragment(CALENDAR, TAB_CALENDAR_REG);
			}


		}
		else if (tabiDFromProceed.equalsIgnoreCase(TAB_GALLERY_REG))
		{
			if (PreferenceManager.getStaffOnly(mContext).equalsIgnoreCase("1"))
			{
				AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "This feature is not available for staff", R.drawable.exclamationicon, R.drawable.round);

			}
			else {
				mFragment = new GalleryFragment(GALLERY, TAB_GALLERY_REG);
			}


		}else if (tabiDFromProceed.equalsIgnoreCase(TAB_CONTACT_US_REG))
		{
			mFragment = new ContactUsFragment(CONTACT_US, TAB_CONTACT_US_REG);

		} else if (tabiDFromProceed.equalsIgnoreCase(TAB_NOTIFICATIONS_REG)) {
			mFragment = new NotificationsFragmentNew(NOTIFICATIONS, TAB_NOTIFICATIONS_REG);

		}
		else if (tabiDFromProceed.equalsIgnoreCase(TAB_COMMUNICATIONS_REG)) {
			mFragment = new CommunicationsFragment(COMMUNICATIONS, TAB_COMMUNICATIONS_REG);

		}
		else if (tabiDFromProceed.equalsIgnoreCase(TAB_PARENT_ESSENTIALS_REG)) {
			mFragment = new ParentEssentialsFragment(PARENT_ESSENTIALS, TAB_PARENT_ESSENTIALS_REG);

		}
		else if (tabiDFromProceed.equalsIgnoreCase(TAB_EARLYYEARS_REG)) {
			mFragment = new EarlyYearsFragment(EARLY_YEARS, TAB_EARLYYEARS_REG);

		}
		else if (tabiDFromProceed.equalsIgnoreCase(TAB_PRIMARY_REG)) {
			mFragment = new PrimaryFragment(PRIMARY, TAB_PRIMARY_REG);

		}
		else if (tabiDFromProceed.equalsIgnoreCase(TAB_SECONDARY_REG)) {
			mFragment = new SecondaryFragment(SECONDARY, TAB_SECONDARY_REG);

		}
		else if (tabiDFromProceed.equalsIgnoreCase(TAB_IB_PROGRAMME_REG)) {
			mFragment = new IbProgrammeFragment(IB_PROGRAMME, TAB_IB_PROGRAMME_REG);

		}
		else if (tabiDFromProceed.equalsIgnoreCase(TAB_UNIVERSITY_GUIDANCE_REG)) {
			mFragment = new UniverstyGuidanceFragment(UNIVERSITY_GUIDANCE, TAB_UNIVERSITY_GUIDANCE_REG);

		}
		else if (tabiDFromProceed.equalsIgnoreCase(TAB_SPORTS_REG)) {
			if (PreferenceManager.getStaffOnly(mContext).equalsIgnoreCase("1"))
			{
				AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "This feature is not available for staff", R.drawable.exclamationicon, R.drawable.round);

			}
			else {
				mFragment = new SportsMainScreenFragment(SPORTS, TAB_SPORTS_REG);
			}


		}
		else if (tabiDFromProceed.equalsIgnoreCase(TAB_PERFORMING_ARTS_REG)) {
			mFragment = new PerformingArtsFragment(PERFORMING_ARTS, TAB_PERFORMING_ARTS_REG);

		}
		else if (tabiDFromProceed.equalsIgnoreCase(TAB_CCAS_REG)) {
			if (PreferenceManager.getStaffOnly(mContext).equalsIgnoreCase("1"))
			{
				AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "This feature is not available for staff", R.drawable.exclamationicon, R.drawable.round);

			}
			else {
				mFragment = new CcaFragmentMain(CCAS, TAB_CCAS_REG);
			}


		}
		else if (tabiDFromProceed.equalsIgnoreCase(TAB_NAE_PROGRAMMES_REG)) {
			mFragment = new NaeProgrammesFragment(NAE_PROGRAMMES, TAB_NAE_PROGRAMMES_REG);

		}
	/*	else if (tabiDFromProceed.equalsIgnoreCase(TAB_SOCIAL_MEDIA_REG)) {
			mFragment = new SocialMediaFragment(SOCIAL_MEDIA, TAB_SOCIAL_MEDIA_REG);

		}*/
	/*	else if (tabiDFromProceed.equalsIgnoreCase(TAB_NAS_TODAY_REG)) {
			mFragment = new NasTodayFragment(NAS_TODAY, TAB_NAS_TODAY_REG);

		}*/
		else if (tabiDFromProceed.equalsIgnoreCase(TAB_PARENTS_MEETING_REG)) {
			if (PreferenceManager.getStaffOnly(mContext).equalsIgnoreCase("1"))
			{
				AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "This feature is not available for staff", R.drawable.exclamationicon, R.drawable.round);

			}
			else {
				mFragment = new ParentsEveningFragment(PARENT_EVENING, TAB_PARENTS_MEETING_REG);
			}


		}
		else if (tabiDFromProceed.equalsIgnoreCase(TAB_ABOUT_US_REG)) {
			mFragment = new AboutUsFragment(ABOUT_US, TAB_ABOUT_US_REG);

		}else if (tabiDFromProceed.equalsIgnoreCase(TAB_ABSENSE_REG)) {
			if (PreferenceManager.getStaffOnly(mContext).equalsIgnoreCase("1"))
			{
				AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "This feature is not available for staff", R.drawable.exclamationicon, R.drawable.round);

			}
			else {
				mFragment = new AbsenceFragment(ABSENCE, TAB_ABSENSE_REG);
			}

		}else if (tabiDFromProceed.equalsIgnoreCase(TAB_PARENTS_ASSOCIATION_REG)) {
			if (PreferenceManager.getStaffOnly(mContext).equalsIgnoreCase("1"))
			{
				AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "This feature is not available for staff", R.drawable.exclamationicon, R.drawable.round);

			}
			else {
				mFragment = new ParentAssociationsFragment(PARENTS_ASSOCIATION, TAB_PARENTS_ASSOCIATION_REG);
			}

		}else if(tabiDFromProceed.equalsIgnoreCase(TAB_REPORT_REG))
		{
			if (PreferenceManager.getStaffOnly(mContext).equalsIgnoreCase("1"))
			{
				AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "This feature is not available for staff", R.drawable.exclamationicon, R.drawable.round);

			}
			else {
				mFragment = new ReportFragment("Reports", TAB_REPORT_REG);
			}


		}
		else if(tabiDFromProceed.equalsIgnoreCase(TAB_PERMISSION_SLIP_REG))
		{
			if (PreferenceManager.getStaffOnly(mContext).equalsIgnoreCase("1"))
			{
				AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "This feature is not available for staff", R.drawable.exclamationicon, R.drawable.round);

			}
			else {
				mFragment = new PermissionSlipsFragment(PERMISSSION_SLIPS, TAB_PERMISSION_SLIP_REG);
			}


		}
		else if(tabiDFromProceed.equalsIgnoreCase(TAB_TRIPS_REG))
		{
			if (PreferenceManager.getStaffOnly(mContext).equalsIgnoreCase("1"))
			{
				AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "This feature is not available for staff", R.drawable.exclamationicon, R.drawable.round);

			}
			else {
				mFragment = new TripsFragmentNew(TRIPS, TAB_TRIPS_REG);
			}


		}
		else if(tabiDFromProceed.equalsIgnoreCase(TAB_CANTEEN_REG))
		{
			mFragment = new CanteenFragmentNew(CANTEEN, TAB_CANTEEN_REG);

		}


//		else if(tabiDFromProceed.equalsIgnoreCase(TAB_CANTEEN_REG))
//		{
//			mFragment = new CanteenFragmentNew(CANTEEN, TAB_CANTEEN_REG);
//
//		}
		fragmentIntent(mFragment);


	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if(requestCode == PERMISSION_CALLBACK_CONSTANT_CALENDAR){
			//check if all permissions are granted
			boolean allgranted = false;
			for(int i=0;i<grantResults.length;i++){
				if(grantResults[i]== PackageManager.PERMISSION_GRANTED){
					allgranted = true;
				} else {
					allgranted = false;
					break;
				}
			}

			if(allgranted){
				proceedAfterPermission(tabiDToProceed);
			} else if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_CALENDAR)){
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle("Need Calendar Permissions");
				builder.setMessage("This module needs calendar permissions.");
				builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						calendarToSettings = false;

						requestPermissions(permissionsRequiredCalendar, PERMISSION_CALLBACK_CONSTANT_CALENDAR);
					}
				});
				builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						calendarToSettings = false;

						dialog.cancel();
					}
				});
				builder.show();
			} else if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.WRITE_CALENDAR)){
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle("Need Calendar Permissions");
				builder.setMessage("This module needs calendar permissions.");
				builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						calendarToSettings = false;

						requestPermissions(permissionsRequiredCalendar, PERMISSION_CALLBACK_CONSTANT_CALENDAR);
					}
				});
				builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						calendarToSettings = false;

						dialog.cancel();
					}
				});
				builder.show();
			} else {
//                Toast.makeText(getActivity(),"Unable to get Permission",Toast.LENGTH_LONG).show();
				calendarToSettings = true;

				Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
				Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
				intent.setData(uri);
				startActivityForResult(intent, REQUEST_PERMISSION_CALENDAR);
				Toast.makeText(mContext, "Go to settings and grant access to calendar", Toast.LENGTH_LONG).show();

			}
		}else if(requestCode == PERMISSION_CALLBACK_CONSTANT_EXTERNAL_STORAGE){
			//check if all permissions are granted
			boolean allgranted = false;
			for(int i=0;i<grantResults.length;i++){
				if(grantResults[i]== PackageManager.PERMISSION_GRANTED){
					allgranted = true;
				} else {
					allgranted = false;
					break;
				}
			}

			if(allgranted){
				proceedAfterPermission(tabiDToProceed);
			} else if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)){
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle("Need Storage Permissions");
				builder.setMessage("This module needs storage permissions.");
				builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						externalStorageToSettings = false;

						requestPermissions(permissionsRequiredExternalStorage, PERMISSION_CALLBACK_CONSTANT_EXTERNAL_STORAGE);
					}
				});
				builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						externalStorageToSettings = false;

						dialog.cancel();
					}
				});
				builder.show();
			} else if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.WRITE_CALENDAR)){
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle("Need Storage Permissions");
				builder.setMessage("This module needs storage permissions.");
				builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						externalStorageToSettings = false;

						requestPermissions(permissionsRequiredExternalStorage, PERMISSION_CALLBACK_CONSTANT_EXTERNAL_STORAGE);
					}
				});
				builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						externalStorageToSettings = false;

						dialog.cancel();
					}
				});
				builder.show();
			} else {
//                Toast.makeText(getActivity(),"Unable to get Permission",Toast.LENGTH_LONG).show();
				externalStorageToSettings = true;

				Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
				Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
				intent.setData(uri);
				startActivityForResult(intent, REQUEST_PERMISSION_EXTERNAL_STORAGE);
				Toast.makeText(mContext, "Go to settings and grant access to storage", Toast.LENGTH_LONG).show();

			}
		}else if(requestCode == PERMISSION_CALLBACK_CONSTANT_LOCATION){
			//check if all permissions are granted
			boolean allgranted = false;
			for(int i=0;i<grantResults.length;i++){
				if(grantResults[i]== PackageManager.PERMISSION_GRANTED){
					allgranted = true;
				} else {
					allgranted = false;
					break;
				}
			}

			if(allgranted){
				proceedAfterPermission(tabiDToProceed);
			} else if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)){
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle("Need Location Permissions");
				builder.setMessage("This module needs location permissions.");
				builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						locationToSettings = false;

						requestPermissions(permissionsRequiredLocation, PERMISSION_CALLBACK_CONSTANT_LOCATION);
					}
				});
				builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						locationToSettings = false;

						dialog.cancel();
					}
				});
				builder.show();
			} else if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.ACCESS_COARSE_LOCATION)){
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle("Need Location Permissions");
				builder.setMessage("This module needs location permissions.");
				builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
						locationToSettings = false;

						requestPermissions(permissionsRequiredLocation, PERMISSION_CALLBACK_CONSTANT_LOCATION);
					}
				});
				builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						locationToSettings = false;

						dialog.cancel();
					}
				});
				builder.show();
			} else {
//                Toast.makeText(getActivity(),"Unable to get Permission",Toast.LENGTH_LONG).show();
				locationToSettings = true;

				Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
				Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
				intent.setData(uri);
				startActivityForResult(intent, PERMISSION_CALLBACK_CONSTANT_LOCATION);
				Toast.makeText(mContext, "Go to settings and grant access to location", Toast.LENGTH_LONG).show();

			}
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_PERMISSION_CALENDAR) {
			if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
				//Got Permission
				proceedAfterPermission(tabiDToProceed);
			}/*else  if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_DENIED || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_DENIED) {
				//DENIAL
				requestPermissions(permissionsRequiredCalendar, PERMISSION_CALLBACK_CONSTANT_CALENDAR);
			}*/
		}else	if (requestCode == REQUEST_PERMISSION_EXTERNAL_STORAGE) {
			if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
				//Got Permission
				proceedAfterPermission(tabiDToProceed);
			}/*else  if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_DENIED || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_DENIED) {
				//DENIAL
				requestPermissions(permissionsRequiredCalendar, PERMISSION_CALLBACK_CONSTANT_CALENDAR);
			}*/
		}else	if (requestCode == REQUEST_PERMISSION_LOCATION) {
			if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
				//Got Permission
				proceedAfterPermission(tabiDToProceed);
			}/*else  if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
				//DENIAL
				requestPermissions(permissionsRequiredLocation, PERMISSION_CALLBACK_CONSTANT_LOCATION);

			}*/
		}
	}

	private void getStudentsListAPI(final String URLHEAD) {
		VolleyWrapper volleyWrapper = new VolleyWrapper(URLHEAD);
		String[] name = {"access_token", "users_id"};
		String[] value = {PreferenceManager.getAccessToken(mContext), PreferenceManager.getUserId(mContext)};
		volleyWrapper.getResponsePOST(mContext, 14, name, value, new VolleyWrapper.ResponseListener() {
			@Override
			public void responseSuccess(String successResponse) {
				System.out.println("The response is student list" + successResponse);
				try {
					studentsModelArrayList = new ArrayList<>();
					JSONObject obj = new JSONObject(successResponse);
					String response_code = obj.getString(JTAG_RESPONSECODE);
					if (response_code.equalsIgnoreCase("200")) {
						JSONObject secobj = obj.getJSONObject(JTAG_RESPONSE);
						String status_code = secobj.getString(JTAG_STATUSCODE);
						if (status_code.equalsIgnoreCase("303")) {
							JSONArray data = secobj.getJSONArray("data");
							studentList.clear();

							if (data.length() > 0)
							{
								for (int i = 0; i < data.length(); i++) {
									JSONObject dataObject = data.getJSONObject(i);
									studentsModelArrayList.add(addStudentDetails(dataObject));
									studentsModelArrayListn.add(addStudentDetailsn(dataObject));
								}

								if (studentsModelArrayList.size()>0)
								{
									PreferenceManager.setStudentArrayList(studentsModelArrayListn,mContext);
								}

							}
							if (studentsModelArrayList.size()==1)
							{
								if (studentsModelArrayList.get(0).getmSection().equalsIgnoreCase("Staff"))
								{
									PreferenceManager.setStaffOnly(mContext,"1");
								}
							}
							else
								{
								PreferenceManager.setStaffOnly(mContext,"0");
							}
						}
					} else if (response_code.equalsIgnoreCase("500")) {
						//AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

					} else if (response_code.equalsIgnoreCase("400")) {
						AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
							@Override
							public void tokenrenewed() {
							}
						});
						getStudentsListAPI(URLHEAD);

					} else if (response_code.equalsIgnoreCase("401")) {
						AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
							@Override
							public void tokenrenewed() {
							}
						});
						getStudentsListAPI(URLHEAD);

					} else if (response_code.equalsIgnoreCase("402")) {
						AppUtils.getToken(mContext, new AppUtils.GetTokenSuccess() {
							@Override
							public void tokenrenewed() {
							}
						});
						getStudentsListAPI(URLHEAD);

					} else {
                        /*CustomDialog dialog = new CustomDialog(mContext, getResources().getString(R.string.common_error)
								, getResources().getString(R.string.ok));
						dialog.show();*/
						//AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", getString(R.string.common_error), R.drawable.exclamationicon, R.drawable.round);

					}
				} catch (Exception ex) {
					System.out.println("The Exception in edit profile is" + ex.toString());
				}

			}

			@Override
			public void responseFailure(String failureResponse) {


			}
		});


	}
	private ReportModel addStudentDetails(JSONObject dataObject) {
		ReportModel studentModel = new ReportModel();
		studentModel.setmId(dataObject.optString(JTAG_ID));
		studentModel.setmName(dataObject.optString(JTAG_TAB_NAME));
		studentModel.setmClass(dataObject.optString(JTAG_TAB_CLASS));
		studentModel.setmSection(dataObject.optString(JTAG_TAB_SECTION));
		studentModel.setmHouse(dataObject.optString("house"));
		studentModel.setmPhoto(dataObject.optString("photo"));

		return studentModel;
	}
	private StudentModel addStudentDetailsn(JSONObject dataObject) {
		StudentModel studentModel = new StudentModel();
		studentModel.setmId(dataObject.optString(JTAG_ID));
		studentModel.setmName(dataObject.optString(JTAG_TAB_NAME));
		studentModel.setmClass(dataObject.optString(JTAG_TAB_CLASS));
		studentModel.setmSection(dataObject.optString(JTAG_TAB_SECTION));
		studentModel.setmHouse(dataObject.optString("house"));
		studentModel.setmPhoto(dataObject.optString("photo"));

		return studentModel;
	}



}

