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
import com.mobatia.naisapp.fragments.cca.CcaFragmentMain;
import com.mobatia.naisapp.fragments.communications.CommunicationsFragment;
import com.mobatia.naisapp.fragments.contact_us.ContactUsFragment;
import com.mobatia.naisapp.fragments.early_years.EarlyYearsFragment;
import com.mobatia.naisapp.fragments.home.adapter.ImagePagerDrawableAdapter;
import com.mobatia.naisapp.fragments.ib_programme.IbProgrammeFragment;
import com.mobatia.naisapp.fragments.nae_programmes.NaeProgrammesFragment;
import com.mobatia.naisapp.fragments.notifications.NotificationsFragment;
import com.mobatia.naisapp.fragments.notifications.NotificationsFragmentNew;
import com.mobatia.naisapp.fragments.parent_essentials.ParentEssentialsFragment;
import com.mobatia.naisapp.fragments.performing_arts.PerformingArtsFragment;
import com.mobatia.naisapp.fragments.primary.PrimaryFragment;
import com.mobatia.naisapp.fragments.secondary.SecondaryFragment;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class HomeScreenGuestUserFragment extends Fragment implements
        OnClickListener, NaisTabConstants, NaisClassNameConstants, JSONConstants, StatusConstants, URLConstants {

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
//	private String AppController.mTitles;
//	private DrawerLayout AppController.mDrawerLayouts;
//	private ListView mListView;

    //	private String[] listitemArrays;
//	 TypedArray AppController.mListImgArrays;
    private View viewTouched = null;
    private String TAB_ID;
    private String INTENT_TAB_ID;
    //	private ImageView mBannerImg;
    private String[] mSectionText;
    private boolean isDraggable;
    int currentPage = 0;
    ViewPager bannerImagePager;
    ArrayList<Integer> homaBannerImageArray;
    ArrayList<String> homeBannerUrlImageArray;
    //	LinearLayout mLinearLayout;
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
    String tabiDToProceed = "";

    public HomeScreenGuestUserFragment(String title,
                                       DrawerLayout mDrawerLayouts, ListView listView,
                                       String[] mListItemArray, TypedArray mListImgArray) {
        AppController.mTitles = title;
        AppController.mDrawerLayouts = mDrawerLayouts;
        AppController.mListViews = listView;
        AppController.listitemArrays = mListItemArray;
        AppController.mListImgArrays = mListImgArray;
    }

    public HomeScreenGuestUserFragment(String title,
                                       DrawerLayout mDrawerLayouts, ListView listView, LinearLayout linearLayout,
                                       String[] mListItemArray, TypedArray mListImgArray) {
        AppController.mTitles = title;
        AppController.mDrawerLayouts = mDrawerLayouts;
        AppController.mLinearLayouts = linearLayout;
        AppController.mListViews = listView;
        AppController.listitemArrays = mListItemArray;
        AppController.mListImgArrays = mListImgArray;
    }

    public HomeScreenGuestUserFragment() {
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
//		setHasOptionsMenu(true);
//		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(mContext));
        calendarPermissionStatus = getActivity().getSharedPreferences("calendarPermissionStatus", getActivity().MODE_PRIVATE);
        externalStoragePermissionStatus = getActivity().getSharedPreferences("externalStoragePermissionStatus", getActivity().MODE_PRIVATE);
        locationPermissionStatus = getActivity().getSharedPreferences("locationPermissionStatus", getActivity().MODE_PRIVATE);

        initialiseUI();
        setListeners();
        setDragListenersForButtons();
        getButtonBgAndTextImages();

        return mRootView;
    }

    /********************************************************
     * Method name : getButtonBgAndTextImages Description : get button
     * background color and text images Parameters : nil Return type : void Date
     * * : Nov 7, 2014 Author : Rijo K Jose
     *****************************************************/
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void getButtonBgAndTextImages() {
        if (Integer.parseInt(PreferenceManager
                .getButtonOneGuestTextImage(mContext)) != 0) {
            mImgOne.setImageDrawable(AppController.mListImgArrays.getDrawable(Integer
                    .parseInt(PreferenceManager
                            .getButtonOneGuestTextImage(mContext))));
            String relTwoStr = "";
            if (AppController.listitemArrays[Integer
                    .parseInt(PreferenceManager
                            .getButtonOneGuestTextImage(mContext))].toString().equalsIgnoreCase(CCAS)) {
                relTwoStr = CCAS;
            } else {
                relTwoStr = AppController.listitemArrays[Integer
                        .parseInt(PreferenceManager
                                .getButtonOneGuestTextImage(mContext))].toUpperCase();

            }
            mTxtOne.setText(relTwoStr);
            mRelOne.setBackgroundColor(PreferenceManager
                    .getButtonOneGuestBg(mContext));
        }
        if (Integer.parseInt(PreferenceManager
                .getButtonTwoGuestTextImage(mContext)) != 0) {
            mImgTwo.setImageDrawable(AppController.mListImgArrays.getDrawable(Integer
                    .parseInt(PreferenceManager
                            .getButtonTwoGuestTextImage(mContext))));
            String relTwoStr = "";
            if (AppController.listitemArrays[Integer
                    .parseInt(PreferenceManager
                            .getButtonTwoGuestTextImage(mContext))].toString().equalsIgnoreCase(CCAS)) {
                relTwoStr = CCAS;
            } else {
                relTwoStr = AppController.listitemArrays[Integer
                        .parseInt(PreferenceManager
                                .getButtonTwoGuestTextImage(mContext))].toUpperCase();

            }
            mTxtTwo.setText(relTwoStr);
            mRelTwo.setBackgroundColor(PreferenceManager
                    .getButtonTwoGuestBg(mContext));
        }
        if (Integer.parseInt(PreferenceManager
                .getButtonThreeGuestTextImage(mContext)) != 0) {
            mImgThree.setImageDrawable(AppController.mListImgArrays.getDrawable(Integer
                    .parseInt(PreferenceManager
                            .getButtonThreeGuestTextImage(mContext))));
            String relTwoStr = "";
            if (AppController.listitemArrays[Integer
                    .parseInt(PreferenceManager
                            .getButtonThreeGuestTextImage(mContext))].toString().equalsIgnoreCase(CCAS)) {
                relTwoStr = CCAS;
            } else {
                relTwoStr = AppController.listitemArrays[Integer
                        .parseInt(PreferenceManager
                                .getButtonThreeGuestTextImage(mContext))].toUpperCase();

            }
            mTxtThree.setText(relTwoStr);
            mRelThree.setBackgroundColor(PreferenceManager
                    .getButtonThreeGuestBg(mContext));
        }
        if (Integer.parseInt(PreferenceManager
                .getButtonFourGuestTextImage(mContext)) != 0) {
            mImgFour.setImageDrawable(AppController.mListImgArrays.getDrawable(Integer
                    .parseInt(PreferenceManager
                            .getButtonFourGuestTextImage(mContext))));
            String relTwoStr = "";
            if (AppController.listitemArrays[Integer
                    .parseInt(PreferenceManager
                            .getButtonFourGuestTextImage(mContext))].toString().equalsIgnoreCase(CCAS)) {
                relTwoStr = CCAS;
            } else {
                relTwoStr = AppController.listitemArrays[Integer
                        .parseInt(PreferenceManager
                                .getButtonFourGuestTextImage(mContext))].toUpperCase();

            }
            mTxtFour.setText(relTwoStr);
            mRelFour.setBackgroundColor(PreferenceManager
                    .getButtonFourGuestBg(mContext));
        }
        if (Integer.parseInt(PreferenceManager
                .getButtonFiveGuestTextImage(mContext)) != 0) {
            mImgFive.setImageDrawable(AppController.mListImgArrays.getDrawable(Integer
                    .parseInt(PreferenceManager
                            .getButtonFiveGuestTextImage(mContext))));
            String relTwoStr = "";
            if (AppController.listitemArrays[Integer
                    .parseInt(PreferenceManager
                            .getButtonFiveGuestTextImage(mContext))].toString().equalsIgnoreCase(CCAS)) {
                relTwoStr = CCAS;
            } else {
                relTwoStr = AppController.listitemArrays[Integer
                        .parseInt(PreferenceManager
                                .getButtonFiveGuestTextImage(mContext))].toUpperCase();

            }
            mTxtFive.setText(relTwoStr);
            mRelFive.setBackgroundColor(PreferenceManager
                    .getButtonFiveGuestBg(mContext));
        }
        if (Integer.parseInt(PreferenceManager
                .getButtonSixGuestTextImage(mContext)) != 0) {
            mImgSix.setImageDrawable(AppController.mListImgArrays.getDrawable(Integer
                    .parseInt(PreferenceManager
                            .getButtonSixGuestTextImage(mContext))));
            String relTwoStr = "";
            if (AppController.listitemArrays[Integer
                    .parseInt(PreferenceManager
                            .getButtonSixGuestTextImage(mContext))].toString().equalsIgnoreCase(CCAS)) {
                relTwoStr = CCAS;
            } else {
                relTwoStr = AppController.listitemArrays[Integer
                        .parseInt(PreferenceManager
                                .getButtonSixGuestTextImage(mContext))].toUpperCase();

            }
            mTxtSix.setText(relTwoStr);
            mRelSix.setBackgroundColor(PreferenceManager
                    .getButtonSixGuestBg(mContext));
        }
        if (Integer.parseInt(PreferenceManager
                .getButtonSevenGuestTextImage(mContext)) != 0) {
            mImgSeven.setImageDrawable(AppController.mListImgArrays.getDrawable(Integer
                    .parseInt(PreferenceManager
                            .getButtonSevenGuestTextImage(mContext))));
            String relTwoStr = "";
            if (AppController.listitemArrays[Integer
                    .parseInt(PreferenceManager
                            .getButtonSevenGuestTextImage(mContext))].toString().equalsIgnoreCase(CCAS)) {
                relTwoStr = CCAS;
            } else {
                relTwoStr = AppController.listitemArrays[Integer
                        .parseInt(PreferenceManager
                                .getButtonSevenGuestTextImage(mContext))].toUpperCase();

            }
            mTxtSeven.setText(relTwoStr);
            mRelSeven.setBackgroundColor(PreferenceManager
                    .getButtonSevenGuestBg(mContext));
        }
        if (Integer.parseInt(PreferenceManager
                .getButtonEightGuestTextImage(mContext)) != 0) {
            mImgEight.setImageDrawable(AppController.mListImgArrays.getDrawable(Integer
                    .parseInt(PreferenceManager
                            .getButtonEightGuestTextImage(mContext))));
            String relTwoStr = "";
            if (AppController.listitemArrays[Integer
                    .parseInt(PreferenceManager
                            .getButtonEightGuestTextImage(mContext))].toString().equalsIgnoreCase(CCAS)) {
                relTwoStr = CCAS;
            } else {
                relTwoStr = AppController.listitemArrays[Integer
                        .parseInt(PreferenceManager
                                .getButtonEightGuestTextImage(mContext))].toUpperCase();

            }
            mTxtEight.setText(relTwoStr);
            mRelEight.setBackgroundColor(PreferenceManager
                    .getButtonEightGuestBg(mContext));
        }
        if (Integer.parseInt(PreferenceManager
                .getButtonNineGuestTextImage(mContext)) != 0) {
            mImgNine.setImageDrawable(AppController.mListImgArrays.getDrawable(Integer
                    .parseInt(PreferenceManager
                            .getButtonNineGuestTextImage(mContext))));
            String relTwoStr = "";
            if (AppController.listitemArrays[Integer
                    .parseInt(PreferenceManager
                            .getButtonNineGuestTextImage(mContext))].toString().equalsIgnoreCase(CCAS)) {
                relTwoStr = CCAS;
            } else {
                relTwoStr = AppController.listitemArrays[Integer
                        .parseInt(PreferenceManager
                                .getButtonNineGuestTextImage(mContext))].toUpperCase();

            }
            mTxtNine.setText(relTwoStr);
            mRelNine.setBackgroundColor(PreferenceManager
                    .getButtonNineGuestBg(mContext));
        }
    }

    /********************************************************
     * Method name : initialiseUI Description : initialise UI elements
     * Parameters : nil Return type : void Date : Oct 31, 2014 Author : Vandana
     * Surendranath
     *****************************************************/

    private void initialiseUI() {
        bannerImagePager = (ViewPager) mRootView.findViewById(R.id.bannerImagePager);

//		mBannerImg = (ImageView) mRootView.findViewById(R.id.bannerImage);
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

//		homeBannerUrlImageArray=new ArrayList<>();
//		homeBannerUrlImageArray.add("http://mobicare2.mobatia.com/nais/media/images/4.png");
//		homeBannerUrlImageArray.add("http://mobicare2.mobatia.com/nais/media/images/3.png");
//		homeBannerUrlImageArray.add("http://mobicare2.mobatia.com/nais/media/images/2.png");
//		homeBannerUrlImageArray.add("http://mobicare2.mobatia.com/nais/media/images/1.png");
//		bannerImagePager.setAdapter(new ImagePagerDrawableAdapter(homeBannerUrlImageArray,getActivity()));
        if (AppUtils.checkInternet(mContext)) {
            getBanner();
        } else {
            Toast.makeText(mContext, "Network Error", Toast.LENGTH_SHORT).show();

        }
        if (homeBannerUrlImageArray != null) {

            final Handler handler = new Handler();
            final Runnable Update = new Runnable() {
                public void run() {
                    if (currentPage == homeBannerUrlImageArray.size()) {
                        currentPage = 0;
                        bannerImagePager.setCurrentItem(currentPage,
                                false);
                    } else {

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

        /** change home list width */
        // int width = mContext.getResources().getDisplayMetrics().widthPixels /
        // 5;
        // DrawerLayout.LayoutParams params =
        // (android.support.v4.widget.DrawerLayout.LayoutParams) mListView
        // .getLayoutParams();
        // params.width = width;
        // mListView.setLayoutParams(params);
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
//				AppController.mDrawerLayouts.closeDrawer(mListView);
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
//					if (mSectionText[i]
//							.equalsIgnoreCase(AppController.listitemArrays[HomeListActivity.sPosition])) {
                        if (mSectionText[i]
                                .equalsIgnoreCase(AppController.listitemArrays[HomeListAppCompatActivity.sPosition])) {
                            isDraggable = false;
                            break;
                        }
                    }
                    if (isDraggable) {
//					getButtonDrawablesAndText(viewTouched,
//							HomeListActivity.sPosition);
                        getButtonDrawablesAndText(viewTouched,
                                HomeListAppCompatActivity.sPosition);
                    } else {
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
            TAB_ID = TAB_ABOUT_US_GUEST;//TAB_ABOUT_US;
        }
//        else if (text.equalsIgnoreCase(NOTIFICATIONS)) {
//            TAB_ID = TAB_NOTIFICATIONS_GUEST;//TAB_NOTIFICATIONS;
//        }
        else if (text.equalsIgnoreCase(COMMUNICATIONS)) {
            TAB_ID = TAB_COMMUNICATIONS_GUEST;//TAB_COMMUNICATIONS;
        }  else if (text.equalsIgnoreCase(PARENT_ESSENTIALS)) {
            TAB_ID = TAB_PARENT_ESSENTIALS_GUEST;//TAB_PARENT_ESSENTIALS;
        } else if (text.equalsIgnoreCase(EARLY_YEARS)) {
            TAB_ID = TAB_EARLYYEARS_GUEST;//TAB_EARLYYEARS;
        }  else if (text.equalsIgnoreCase(IB_PROGRAMME)) {
            TAB_ID = TAB_IB_PROGRAMME_GUEST;//TAB_IB_PROGRAMME;
        } else if (text.equalsIgnoreCase(PERFORMING_ARTS)) {
            TAB_ID = TAB_PERFORMING_ARTS_GUEST;// TAB_PERFORMING_ARTS;
        }
//        else if (text.equalsIgnoreCase(CCAS)) {
//            TAB_ID = TAB_CCAS_GUEST;//TAB_CCAS;
//        }
        else if (text.equalsIgnoreCase(NAE_PROGRAMMES)) {
            TAB_ID = TAB_NAE_PROGRAMMES_GUEST;// TAB_NAE_PROGRAMMES;
        } else if (text.equalsIgnoreCase(PRIMARY)) {
            TAB_ID = TAB_PRIMARY_GUEST;// TAB_PRIMARY;
        } else if (text.equalsIgnoreCase(SECONDARY)) {
            TAB_ID = TAB_SECONDARY_GUEST;// TAB_SECONDARY;
        } /*else if (text.equalsIgnoreCase(SOCIAL_MEDIA)) {
            TAB_ID = TAB_SOCIAL_MEDIA_GUEST;// TAB_SOCIAL_MEDIA;
        } else if (text.equalsIgnoreCase(NAS_TODAY)) {
            TAB_ID = TAB_NAS_TODAY;
        } else if (text.equalsIgnoreCase(WISSUP)) {
            TAB_ID = TAB_WISSUP;
        }*/
        else if (text.equalsIgnoreCase(CONTACT_US)) {
            TAB_ID = TAB_CONTACT_US_GUEST;
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
                String relTwoStr = "";
                if (AppController.listitemArrays[position].toString().equalsIgnoreCase(CCAS)) {
                    relTwoStr = CCAS;
                } else {
                    relTwoStr = AppController.listitemArrays[position].toUpperCase();

                }
                mTxtOne.setText(relTwoStr);
                getTabId(AppController.listitemArrays[position]);
                PreferenceManager.setButtonOneGuestTabId(mContext, TAB_ID);
                setBackgroundColorForView(AppController.listitemArrays[position], mRelOne);
                PreferenceManager.setButtonOneGuestTextImage(mContext,
                        Integer.toString(position));
            }else if (v == mRelTwo) {
                mImgTwo.setImageDrawable(AppController.mListImgArrays.getDrawable(position));
                String relTwoStr = "";
                if (AppController.listitemArrays[position].toString().equalsIgnoreCase(CCAS)) {
                    relTwoStr = CCAS;
                } else {
                    relTwoStr = AppController.listitemArrays[position].toUpperCase();

                }
                mTxtTwo.setText(relTwoStr);
                getTabId(AppController.listitemArrays[position]);
                PreferenceManager.setButtonTwoGuestTabId(mContext, TAB_ID);
                setBackgroundColorForView(AppController.listitemArrays[position], mRelTwo);
                PreferenceManager.setButtonTwoGuestTextImage(mContext,
                        Integer.toString(position));
            } else if (v == mRelThree) {
                mImgThree.setImageDrawable(AppController.mListImgArrays.getDrawable(position));
                String relTwoStr = "";
                if (AppController.listitemArrays[position].toString().equalsIgnoreCase(CCAS)) {
                    relTwoStr = CCAS;
                } else {
                    relTwoStr = AppController.listitemArrays[position].toUpperCase();

                }
                mTxtThree.setText(relTwoStr);
                getTabId(AppController.listitemArrays[position]);
                PreferenceManager
                        .setButtonThreeGuestTabId(mContext, TAB_ID);
                setBackgroundColorForView(AppController.listitemArrays[position], mRelThree);
                PreferenceManager.setButtonThreeGuestTextImage(mContext,
                        Integer.toString(position));
            } else if (v == mRelFour) {
                mImgFour.setImageDrawable(AppController.mListImgArrays.getDrawable(position));
                String relTwoStr = "";
                if (AppController.listitemArrays[position].toString().equalsIgnoreCase(CCAS)) {
                    relTwoStr = CCAS;
                } else {
                    relTwoStr = AppController.listitemArrays[position].toUpperCase();

                }
                mTxtFour.setText(relTwoStr);
                getTabId(AppController.listitemArrays[position]);
                PreferenceManager.setButtonFourGuestTabId(mContext, TAB_ID);
                setBackgroundColorForView(AppController.listitemArrays[position], mRelFour);
                PreferenceManager.setButtonFourGuestTextImage(mContext,
                        Integer.toString(position));
            } else if (v == mRelFive) {
                mImgFive.setImageDrawable(AppController.mListImgArrays.getDrawable(position));
                String relTwoStr = "";
                if (AppController.listitemArrays[position].toString().equalsIgnoreCase(CCAS)) {
                    relTwoStr = CCAS;
                } else {
                    relTwoStr = AppController.listitemArrays[position].toUpperCase();

                }
                mTxtFive.setText(relTwoStr);
//				mTxtFive.setText(AppController.listitemArrays[position].toUpperCase());
                getTabId(AppController.listitemArrays[position]);
                PreferenceManager.setButtonFiveGuestTabId(mContext, TAB_ID);
                setBackgroundColorForView(AppController.listitemArrays[position], mRelFive);
                PreferenceManager.setButtonFiveGuestTextImage(mContext,
                        Integer.toString(position).toUpperCase());
            } else if (v == mRelSix) {
                mImgSix.setImageDrawable(AppController.mListImgArrays.getDrawable(position));
                String relTwoStr = "";
                if (AppController.listitemArrays[position].toString().equalsIgnoreCase(CCAS)) {
                    relTwoStr = CCAS;
                } else {
                    relTwoStr = AppController.listitemArrays[position].toUpperCase();

                }
                mTxtSix.setText(relTwoStr);
//				mTxtSix.setText(AppController.listitemArrays[position]);
                getTabId(AppController.listitemArrays[position]);
                PreferenceManager.setButtonSixGuestTabId(mContext, TAB_ID);
                setBackgroundColorForView(AppController.listitemArrays[position], mRelSix);
                PreferenceManager.setButtonSixGuestTextImage(mContext,
                        Integer.toString(position));
            } else if (v == mRelSeven) {
                mImgSeven.setImageDrawable(AppController.mListImgArrays.getDrawable(position));
                String relTwoStr = "";
                if (AppController.listitemArrays[position].toString().equalsIgnoreCase(CCAS)) {
                    relTwoStr = CCAS;
                } else {
                    relTwoStr = AppController.listitemArrays[position].toUpperCase();

                }
                mTxtSeven.setText(relTwoStr);
//				mTxtSeven.setText(AppController.listitemArrays[position].toUpperCase());
                getTabId(AppController.listitemArrays[position]);
                PreferenceManager
                        .setButtonSevenGuestTabId(mContext, TAB_ID);
                setBackgroundColorForView(AppController.listitemArrays[position], mRelSeven);
                PreferenceManager.setButtonSevenGuestTextImage(mContext,
                        Integer.toString(position));
            } else if (v == mRelEight) {
                mImgEight.setImageDrawable(AppController.mListImgArrays.getDrawable(position));
                String relTwoStr = "";
                if (AppController.listitemArrays[position].toString().equalsIgnoreCase(CCAS)) {
                    relTwoStr = CCAS;
                } else {
                    relTwoStr = AppController.listitemArrays[position].toUpperCase();

                }
                mTxtEight.setText(relTwoStr);
//				mTxtEight.setText(AppController.listitemArrays[position].toUpperCase());
                getTabId(AppController.listitemArrays[position]);
                PreferenceManager
                        .setButtonEightGuestTabId(mContext, TAB_ID);
                setBackgroundColorForView(AppController.listitemArrays[position], mRelEight);
                PreferenceManager.setButtonEightGuestTextImage(mContext,
                        Integer.toString(position));
            } else if (v == mRelNine) {
                mImgNine.setImageDrawable(AppController.mListImgArrays.getDrawable(position));
                String relTwoStr = "";
                if (AppController.listitemArrays[position].toString().equalsIgnoreCase(CCAS)) {
                    relTwoStr = CCAS;
                } else {
                    relTwoStr = AppController.listitemArrays[position].toUpperCase();

                }
                mTxtNine.setText(relTwoStr);
//				mTxtNine.setText(AppController.listitemArrays[position].toUpperCase());
                getTabId(AppController.listitemArrays[position]);
                PreferenceManager.setButtonNineGuestTabId(mContext, TAB_ID);
                setBackgroundColorForView(AppController.listitemArrays[position], mRelNine);
                PreferenceManager.setButtonNineGuestTextImage(mContext,
                        Integer.toString(position));
            }
        }
        v = null;
        viewTouched = null;
    }

    /*******************************************************
     * Method name : setBackgroundColorForView Description : set background
     * color for view Parameters : view Return type : void Date : Nov 11, 2014
     * Author : Rijo K Jose
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
                    R.color.rel_five));
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
                PreferenceManager.setButtonOneGuestBg(mContext, color);
            } else if (v == mRelTwo) {
                PreferenceManager.setButtonTwoGuestBg(mContext, color);
            } else if (v == mRelThree) {
                PreferenceManager.setButtonThreeGuestBg(mContext, color);
            } else if (v == mRelFour) {
                PreferenceManager.setButtonFourGuestBg(mContext, color);
            } else if (v == mRelFive) {
                PreferenceManager.setButtonFiveGuestBg(mContext, color);
            } else if (v == mRelSix) {
                PreferenceManager.setButtonSixGuestBg(mContext, color);
            } else if (v == mRelSeven) {
                PreferenceManager.setButtonSevenGuestBg(mContext, color);
            } else if (v == mRelEight) {
                PreferenceManager.setButtonEightGuestBg(mContext, color);
            } else if (v == mRelNine) {
                PreferenceManager.setButtonNineGuestBg(mContext, color);
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
        tabiDToProceed = tabId;

        System.out.println("INTENT_TAB_ID:::" + INTENT_TAB_ID);
        Fragment mFragment = null;
		/*if (tabId.equalsIgnoreCase(TAB_CALENDAR)) {
			mFragment = new CalendarFragment(CALENDAR, TAB_CALENDAR);
		}
		else*/
//        if (tabId.equalsIgnoreCase(TAB_NOTIFICATIONS_GUEST)) {
//            mFragment = new NotificationsFragmentNew(NOTIFICATIONS, TAB_NOTIFICATIONS_GUEST);
//            fragmentIntent(mFragment);
//
//        }
//        else
            if (tabId.equalsIgnoreCase(TAB_COMMUNICATIONS_GUEST)) {
            mFragment = new CommunicationsFragment(COMMUNICATIONS, TAB_COMMUNICATIONS_GUEST);
            fragmentIntent(mFragment);

        } else if (tabId.equalsIgnoreCase(TAB_PARENT_ESSENTIALS_GUEST)) {
            mFragment = new ParentEssentialsFragment(PARENT_ESSENTIALS, TAB_PARENT_ESSENTIALS_GUEST);
            fragmentIntent(mFragment);

        } else if (tabId.equalsIgnoreCase(TAB_EARLYYEARS_GUEST)) {
            mFragment = new EarlyYearsFragment(EARLY_YEARS, TAB_EARLYYEARS_GUEST);
            fragmentIntent(mFragment);

        } else if (tabId.equalsIgnoreCase(TAB_PRIMARY_GUEST)) {
            mFragment = new PrimaryFragment(PRIMARY, TAB_PRIMARY_GUEST);
            fragmentIntent(mFragment);

        } else if (tabId.equalsIgnoreCase(TAB_SECONDARY_GUEST)) {
            mFragment = new SecondaryFragment(SECONDARY, TAB_SECONDARY_GUEST);
            fragmentIntent(mFragment);

        } else if (tabId.equalsIgnoreCase(TAB_IB_PROGRAMME_GUEST)) {
            mFragment = new IbProgrammeFragment(IB_PROGRAMME, TAB_IB_PROGRAMME_GUEST);
            fragmentIntent(mFragment);

        } else if (tabId.equalsIgnoreCase(TAB_PERFORMING_ARTS_GUEST)) {
//			mFragment = new SportsFragment(SPORTS, TAB_SPORTS);
            mFragment = new PerformingArtsFragment(PERFORMING_ARTS, TAB_PERFORMING_ARTS_GUEST);
            fragmentIntent(mFragment);


        }
//        else if (tabId.equalsIgnoreCase(TAB_CCAS_GUEST)) {
//            mFragment = new CcaFragmentMain(CCAS, TAB_CCAS_GUEST);
//            fragmentIntent(mFragment);
//
//        }
        else if (tabId.equalsIgnoreCase(TAB_NAE_PROGRAMMES_GUEST)) {
            mFragment = new NaeProgrammesFragment(NAE_PROGRAMMES, TAB_NAE_PROGRAMMES_GUEST);
            fragmentIntent(mFragment);

        } /*else if (tabId.equalsIgnoreCase(TAB_SOCIAL_MEDIA_GUEST)) {
            mFragment = new SocialMediaFragment(SOCIAL_MEDIA, TAB_SOCIAL_MEDIA_GUEST);
            fragmentIntent(mFragment);

        }*/
/*
		else if (tabId.equalsIgnoreCase(TAB_NAE_PROGRAMMES)) {
			mFragment = new GalleryFragment(CONTACT_US, TAB_NAE_PROGRAMMES);
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
*/
        else if (tabId.equalsIgnoreCase(TAB_ABOUT_US_GUEST)) {
            mFragment = new AboutUsFragment(ABOUT_US, TAB_ABOUT_US_GUEST);
//			mFragment = new ContactUsFragment(CONTACT_US, TAB_SOCIAL_MEDIA);
            fragmentIntent(mFragment);

        } else if (tabId.equalsIgnoreCase(TAB_CONTACT_US_GUEST)) {
            mFragment = new ContactUsFragment(CONTACT_US, TAB_CONTACT_US_GUEST);
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
                    } else if (locationPermissionStatus.getBoolean(permissionsRequiredLocation[1], false)) {
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
                    editor.putBoolean(permissionsRequiredLocation[0], true);
                    editor.commit();
                } else {
                    fragmentIntent(mFragment);

                }
            }
        }
	/*	else if (tabId.equalsIgnoreCase("12")) {
//			mFragment = new ContactUsFragment(CONTACT_US, TAB_ABOUT_US);
			mFragment = new GalleryFragment(GALLERY, "12");
			fragmentIntent(mFragment);


		}*/
        /*else if (tabId.equalsIgnoreCase(TAB_NAS_TODAY)) {
            mFragment = new NasTodayFragment(NAS_TODAY, TAB_NAS_TODAY);
            fragmentIntent(mFragment);

        }*/
//		if (mFragment != null) {
//			System.out.println("title:"+AppController.mTitles);
//
//			FragmentManager fragmentManager = getActivity()
//					.getSupportFragmentManager();
//			fragmentManager.beginTransaction()
//					.replace(R.id.frame_container, mFragment, AppController.mTitles)
//					.addToBackStack(AppController.mTitles).commit();
//		}

    }

    void fragmentIntent(Fragment mFragment) {

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
            INTENT_TAB_ID = PreferenceManager
                    .getButtonOneGuestTabId(mContext);
            checkIntent(INTENT_TAB_ID);
        } else if (v == mRelTwo) {
            INTENT_TAB_ID = PreferenceManager
                    .getButtonTwoGuestTabId(mContext);
            checkIntent(INTENT_TAB_ID);
        } else if (v == mRelThree) {
            INTENT_TAB_ID = PreferenceManager
                    .getButtonThreeGuestTabId(mContext);
            checkIntent(INTENT_TAB_ID);
        } else if (v == mRelFour) {
            INTENT_TAB_ID = PreferenceManager
                    .getButtonFourGuestTabId(mContext);
            checkIntent(INTENT_TAB_ID);
        } else if (v == mRelFive) {
            INTENT_TAB_ID = PreferenceManager
                    .getButtonFiveGuestTabId(mContext);
            checkIntent(INTENT_TAB_ID);
        } else if (v == mRelSix) {
            INTENT_TAB_ID = PreferenceManager
                    .getButtonSixGuestTabId(mContext);
            checkIntent(INTENT_TAB_ID);
        } else if (v == mRelSeven) {
            INTENT_TAB_ID = PreferenceManager
                    .getButtonSevenGuestTabId(mContext);
            checkIntent(INTENT_TAB_ID);
        } else if (v == mRelEight) {
            INTENT_TAB_ID = PreferenceManager
                    .getButtonEightGuestTabId(mContext);
            checkIntent(INTENT_TAB_ID);
        } else if (v == mRelNine) {
            INTENT_TAB_ID = PreferenceManager
                    .getButtonNineGuestTabId(mContext);
            checkIntent(INTENT_TAB_ID);
        } /*else if (v == mBannerImg) {
			INTENT_TAB_ID = TAB_NEWS;
			checkIntent(INTENT_TAB_ID);
		}*/
    }

    public void getBanner() {

        try {
            final VolleyWrapper manager = new VolleyWrapper(URL_GET_USER_BANNER);
            String[] name = new String[]{JTAG_ACCESSTOKEN};
            String[] value = new String[]{PreferenceManager.getAccessToken(mContext)};
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
//												homeBannerUrlImageArray.add(dataObject.optString(JTAG_BANNER_IMAGE));
                                                homeBannerUrlImageArray.add(dataArray.optString(i));

                                            }
                                            bannerImagePager.setAdapter(new ImagePagerDrawableAdapter(homeBannerUrlImageArray, getActivity()));


                                        } else {
                                            //CustomStatusDialog();
                                            bannerImagePager.setBackgroundResource(R.drawable.default_banner);
//											bannerImagePager.setBackgroundResource(R.drawable.banner);

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
                                } else if (responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) ||
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
                                Toast.makeText(mContext, "Failure", Toast.LENGTH_SHORT).show();

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
//        if (tabiDFromProceed.equalsIgnoreCase(TAB_NOTIFICATIONS_GUEST)) {
//            mFragment = new NotificationsFragmentNew(NOTIFICATIONS, TAB_NOTIFICATIONS_GUEST);
//
//        } else
            if (tabiDFromProceed.equalsIgnoreCase(TAB_COMMUNICATIONS_GUEST)) {
            mFragment = new CommunicationsFragment(COMMUNICATIONS, TAB_COMMUNICATIONS_GUEST);

        } else if (tabiDFromProceed.equalsIgnoreCase(TAB_PARENT_ESSENTIALS_GUEST)) {
            mFragment = new ParentEssentialsFragment(PARENT_ESSENTIALS, TAB_PARENT_ESSENTIALS_GUEST);

        } else if (tabiDFromProceed.equalsIgnoreCase(TAB_EARLYYEARS_GUEST)) {
            mFragment = new EarlyYearsFragment(EARLY_YEARS, TAB_EARLYYEARS_GUEST);

        } else if (tabiDFromProceed.equalsIgnoreCase(TAB_PRIMARY_GUEST)) {
            mFragment = new PrimaryFragment(PRIMARY, TAB_PRIMARY_GUEST);

        } else if (tabiDFromProceed.equalsIgnoreCase(TAB_SECONDARY_GUEST)) {
            mFragment = new SecondaryFragment(SECONDARY, TAB_SECONDARY_GUEST);

        } else if (tabiDFromProceed.equalsIgnoreCase(TAB_IB_PROGRAMME_GUEST)) {
            mFragment = new IbProgrammeFragment(IB_PROGRAMME, TAB_IB_PROGRAMME_GUEST);

        } else if (tabiDFromProceed.equalsIgnoreCase(TAB_PERFORMING_ARTS_GUEST)) {
//			mFragment = new SportsFragment(SPORTS, TAB_SPORTS);
            mFragment = new PerformingArtsFragment(PERFORMING_ARTS, TAB_PERFORMING_ARTS_GUEST);


        }
//            else if (tabiDFromProceed.equalsIgnoreCase(TAB_CCAS_GUEST)) {
//            mFragment = new CcaFragmentMain(CCAS, TAB_CCAS_GUEST);
//
//        }
            else if (tabiDFromProceed.equalsIgnoreCase(TAB_NAE_PROGRAMMES_GUEST)) {
            mFragment = new NaeProgrammesFragment(NAE_PROGRAMMES, TAB_NAE_PROGRAMMES_GUEST);

        } /*else if (tabiDFromProceed.equalsIgnoreCase(TAB_SOCIAL_MEDIA_GUEST)) {
            mFragment = new SocialMediaFragment(SOCIAL_MEDIA, TAB_SOCIAL_MEDIA_GUEST);

        } */ else if (tabiDFromProceed.equalsIgnoreCase(TAB_ABOUT_US_GUEST)) {
            mFragment = new AboutUsFragment(ABOUT_US, TAB_ABOUT_US_GUEST);
//			mFragment = new ContactUsFragment(CONTACT_US, TAB_SOCIAL_MEDIA);

        }	/*else if (tabiDFromProceed.equalsIgnoreCase("12")) {
//			mFragment = new ContactUsFragment(CONTACT_US, TAB_ABOUT_US);
			mFragment = new GalleryFragment(GALLERY, "12");


		}*//* else if (tabiDFromProceed.equalsIgnoreCase(TAB_NAS_TODAY)) {
            mFragment = new NasTodayFragment(NAS_TODAY, TAB_NAS_TODAY);

        } */else if (tabiDFromProceed.equalsIgnoreCase(TAB_CONTACT_US_GUEST)) {
            mFragment = new ContactUsFragment(CONTACT_US, TAB_CONTACT_US_GUEST);
        }

        fragmentIntent(mFragment);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CALLBACK_CONSTANT_CALENDAR) {
            //check if all permissions are granted
            boolean allgranted = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if (allgranted) {
                proceedAfterPermission(tabiDToProceed);
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_CALENDAR)) {
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
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_CALENDAR)) {
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
        } else if (requestCode == PERMISSION_CALLBACK_CONSTANT_EXTERNAL_STORAGE) {
            //check if all permissions are granted
            boolean allgranted = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if (allgranted) {
                proceedAfterPermission(tabiDToProceed);
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
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
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_CALENDAR)) {
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
        } else if (requestCode == PERMISSION_CALLBACK_CONSTANT_LOCATION) {
            //check if all permissions are granted
            boolean allgranted = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if (allgranted) {
                proceedAfterPermission(tabiDToProceed);
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
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
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)) {
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
//		super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_CALENDAR) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission(tabiDToProceed);
            }/*else  if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_DENIED || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_DENIED) {
				//DENIAL
				requestPermissions(permissionsRequiredCalendar, PERMISSION_CALLBACK_CONSTANT_CALENDAR);
			}*/
        } else if (requestCode == REQUEST_PERMISSION_EXTERNAL_STORAGE) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission(tabiDToProceed);
            }/*else  if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_DENIED || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_DENIED) {
				//DENIAL
				requestPermissions(permissionsRequiredCalendar, PERMISSION_CALLBACK_CONSTANT_CALENDAR);
			}*/
        } else if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission(tabiDToProceed);
            }/*else  if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
				//DENIAL
				requestPermissions(permissionsRequiredLocation, PERMISSION_CALLBACK_CONSTANT_LOCATION);

			}*/
        }
    }

}
