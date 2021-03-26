package com.mobatia.naisapp.activities.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.legacy.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.annimon.stream.function.Predicate;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.home.adapter.HomeListAdapter;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.constants.NaisDBConstants;
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
import com.mobatia.naisapp.fragments.home.HomeScreenGuestUserFragment;
import com.mobatia.naisapp.fragments.home.HomeScreenRegisteredUserFragment;
import com.mobatia.naisapp.fragments.home.HomeScreenStaffUserFragment;
import com.mobatia.naisapp.fragments.ib_programme.IbProgrammeFragment;
import com.mobatia.naisapp.fragments.nae_programmes.NaeProgrammesFragment;
import com.mobatia.naisapp.fragments.notification_staff.NotificationsFragmentStaff;
import com.mobatia.naisapp.fragments.notifications.NotificationsFragment;
import com.mobatia.naisapp.fragments.notifications.NotificationsFragmentNew;
import com.mobatia.naisapp.fragments.parent_essentials.ParentEssentialsFragment;
import com.mobatia.naisapp.fragments.parentassociation.ParentAssociationsFragment;
import com.mobatia.naisapp.fragments.parents_evening.ParentsEveningFragment;
import com.mobatia.naisapp.fragments.performing_arts.PerformingArtsFragment;
import com.mobatia.naisapp.fragments.permission_slips.PermissionSlipsFragment;
import com.mobatia.naisapp.fragments.primary.PrimaryFragment;
import com.mobatia.naisapp.fragments.report.ReportFragment;
import com.mobatia.naisapp.fragments.secondary.SecondaryFragment;
import com.mobatia.naisapp.fragments.settings.SettingsFragment;
import com.mobatia.naisapp.fragments.sports.SportsMainScreenFragment;
import com.mobatia.naisapp.fragments.trips.TripsFragmentNew;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.sqliteservice.DatabaseHelper;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


@SuppressLint("NewApi")
public class HomeListAppCompatActivity extends AppCompatActivity implements
        OnItemClickListener, OnGestureListener, OnItemLongClickListener,
        NaisDBConstants, NaisTabConstants, OnClickListener,
        NaisClassNameConstants,OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMapLongClickListener,JSONConstants,StatusConstants,URLConstants {
    LinearLayout linearLayout;
    private ListView mHomeListView;
    public static HomeListAdapter mListAdapter;
    private Context mContext;
    private Activity mActivity;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String[] mListItemArray;
    private TypedArray mListImgArray;
    private GestureDetector mDetector;
    private Fragment mFragment = null;
    public static int sPosition;
    ImageView downarrow;
    private int preLast;
    int notificationRecieved = 0;
    Bundle extras;
    ImageView imageButton;
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
    int tabPositionProceed=0;
    public static Dialog dialogCal;
    public ImageView imageButton2;
    Fragment currentFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home_list);

        mContext = this;
        mActivity = this;

        calendarPermissionStatus = getSharedPreferences("calendarPermissionStatus", MODE_PRIVATE);
        externalStoragePermissionStatus = getSharedPreferences("externalStoragePermissionStatus", MODE_PRIVATE);
        locationPermissionStatus = getSharedPreferences("locationPermissionStatus", MODE_PRIVATE);

        extras = getIntent().getExtras();
        if (extras != null) {
            notificationRecieved = extras.getInt("Notification_Recieved", 0);
        }
        initialiseUI();
        initialSettings();
         dialogCal = new Dialog(mContext, R.style.NewDialog);
        dialogCal.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogCal.setContentView(R.layout.custom_dialog_calendar);
        dialogCal.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogCal.setCancelable(true);
        if (notificationRecieved==1)
        {
            displayView(0);
            displayView(2);

        }
        else
        {
            displayView(0);
        }
    }

    /*******************************************************
     * Method name : checkDatabase() Description : check if database exist and
     * open it Parameters : nil Return type : void Date : Nov 21, 2014 Author :
     * Rijo K Jose
     *****************************************************/
    private void checkDatabase() {
        DatabaseHelper dbHelper = new DatabaseHelper(HomeListAppCompatActivity.this,
                DBNAME);
        try {
            dbHelper.createDataBase();
        } catch (IOException ioe) {
            System.out.println("wiss create db exception " + ioe);
        }
        try {
            dbHelper.openDataBase();
            dbHelper.close();
        } catch (SQLException sqle) {
            System.out.println("wiss db exception " + sqle);
        }
    }

    /*******************************************************
     * Method name : initialiseUI() Description : initialise UI elements
     * Parameters : nil Return type : void Date : Oct 29, 2014 Author : Vandana
     * Surendranath
     *****************************************************/
    private void initialiseUI() {
        mHomeListView = (ListView) findViewById(R.id.homeList);
        downarrow = (ImageView) findViewById(R.id.downarrow);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        if (!PreferenceManager.getUserId(mContext).equals("")) {
            // registered user
            mListItemArray = mContext.getResources().getStringArray(
                    R.array.home_list_content_reg_items);
            mListImgArray = mContext.getResources().obtainTypedArray(
                    R.array.home_list_reg_icons);
        } else
            {


                if (!PreferenceManager.getStaffId(mContext).equals(""))
                {
                    //staff user
                    mListItemArray = mContext.getResources().getStringArray(
                            R.array.home_list_content_staff_items);
                    mListImgArray = mContext.getResources().obtainTypedArray(
                            R.array.home_list_staff_icons);
                }
                else
                {
                    //guest user
                    mListItemArray = mContext.getResources().getStringArray(
                            R.array.home_list_content_guest_items);
                    mListImgArray = mContext.getResources().obtainTypedArray(
                            R.array.home_list_guest_icons);
                }

        }
        mListAdapter = new HomeListAdapter(mContext, mListItemArray,
                mListImgArray, R.layout.custom_list_adapter, true);


        mHomeListView.setAdapter(mListAdapter);
        mHomeListView.setBackgroundColor(getResources().getColor(
                R.color.split_bg));
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        /** change home list width */
        int width = (int) (getResources().getDisplayMetrics().widthPixels / 2);
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) linearLayout
                .getLayoutParams();
        params.width = width;
        linearLayout.setLayoutParams(params);
        mHomeListView.setOnItemClickListener(this);
        mHomeListView.setOnItemLongClickListener(this);

        mDetector = new GestureDetector(this);
        mDrawerToggle = new ActionBarDrawerToggle((Activity) mContext,
                mDrawerLayout, R.drawable.hamburgerbtn, R.string.null_value,
                R.string.null_value) {

            public void onDrawerClosed(View view) {

                mDrawerLayout.setOnTouchListener(new OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return mDetector.onTouchEvent(event);
                    }
                });
                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                mDrawerLayout.setOnTouchListener(new OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return mDetector.onTouchEvent(event);
                    }
                });
                supportInvalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mDetector.onTouchEvent(event);
            }
        });

        mHomeListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            int mLastFirstVisibleItem = 0;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (view.getId() == mHomeListView.getId()) {
                    final int currentFirstVisibleItem = mHomeListView.getLastVisiblePosition();

                    if (currentFirstVisibleItem == totalItemCount - 1) {
                        downarrow.setVisibility(View.INVISIBLE);
                    } else {
                        downarrow.setVisibility(View.VISIBLE);

                    }

                }
            }
        });

    }

    /*******************************************************
     * Method name : initialSettings Description : initial settings in home
     * screen Parameters : nil Return type : void Date : Oct 29, 2014 Author :
     * Rijo K Jose
     *****************************************************/
    private void initialSettings() {
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_view_home);
        getSupportActionBar().setElevation(0);

        View view =getSupportActionBar().getCustomView();
        Toolbar toolbar=(Toolbar)view.getParent();
        toolbar.setContentInsetsAbsolute(0,0);

        imageButton= (ImageView)view.findViewById(R.id.action_bar_back);

        imageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                Fragment currentFragment = fm.findFragmentById(R.id.frame_container);
                System.out.println("nas current fragment "
                        + currentFragment.getClass().toString());
                if (mDrawerLayout.isDrawerOpen(linearLayout)) {
                    mDrawerLayout.closeDrawer(linearLayout);
                } else {
                    mDrawerLayout.openDrawer(linearLayout);
                }

            }
        });

        imageButton2= (ImageView)view.findViewById(R.id.action_bar_forward);
        ImageView logoClickImgView= (ImageView)view.findViewById(R.id.logoClickImgView);
        logoClickImgView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                Fragment currentFragment = fm.findFragmentById(R.id.frame_container);
                System.out.println("nas current fragment "
                        + currentFragment.getClass().toString());
                if (currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.naisapp.fragments.about_us.AboutUsFragment")
                        || currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.naisapp.fragments.absence.AbsenceFragment")
                        || currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.naisapp.fragments.report.ReportFragment")
                        || currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.naisapp.fragments.calendar.CalendarFragment")
                        || currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.naisapp.fragments.parentassociation.ParentAssociationsFragment")
                        || currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.naisapp.fragments.cca.CcaFragmentMain")
                        || currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.naisapp.fragments.communications.CommunicationsFragment")
                        || currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.naisapp.fragments.contact_us.ContactUsFragment")
                        || currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.naisapp.fragments.gallery.GalleryFragment")
                        || currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.naisapp.fragments.early_years.EarlyYearsFragment")
                        || currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.naisapp.fragments.social_media.SocialMediaFragment")
                        || currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.naisapp.fragments.ib_programme.IbProgrammeFragment")
                        || currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.naisapp.fragments.nae_programmes.NaeProgrammesFragment")

                        || currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.naisapp.fragments.notifications.NotificationsFragmentNew")
                        || currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.naisapp.fragments.parent_essentials.ParentEssentialsFragment")
                        || currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.naisapp.fragments.performing_arts.PerformingArtsFragment")
                        || currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.naisapp.fragments.primary.PrimaryFragment") || currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.naisapp.fragments.secondary.SecondaryFragment") || currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.naisapp.fragments.sports.SportsMainScreenFragment") || currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.naisapp.fragments.contact_us.ContactUsFragment") || currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.naisapp.fragments.nas_today.NasTodayFragment") || currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.naisapp.fragments.parents_evening.ParentsEveningFragment")|| currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.naisapp.fragments.permission_slips.PermissionSlipsFragment")||currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.naisapp.fragments.settings.SettingsFragment")|| currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.naisapp.fragments.trips.TripsFragmentNew")|| currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.naisapp.fragments.canteen_new.CanteenFragmentNew"))
                {
//                    onBackPressed();
                    imageButton2.setVisibility(View.VISIBLE);

                    displayView(0);

                }

                }
        });
        imageButton2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "Forward Button is clicked", Toast.LENGTH_LONG).show();
                FragmentManager fm = getSupportFragmentManager();
                Fragment currentFragment = fm.findFragmentById(R.id.frame_container);
                System.out.println("nas current fragment "
                        + currentFragment.getClass().toString());
                if (!(currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.naisapp.fragments.settings.SettingsFragment"))) {
                    mFragment = new SettingsFragment(SETTINGS, TAB_SETTINGS);
                    if (mFragment != null) {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .add(R.id.frame_container, mFragment, SETTINGS)
                                .addToBackStack(SETTINGS).commit();
                        mDrawerLayout.closeDrawer(linearLayout);
                        getSupportActionBar().setTitle(R.string.null_value);
                    }
                    imageButton2.setVisibility(View.GONE);
                }
            }
        });

        mDrawerToggle.syncState();


    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        System.out.println("Position working:");

        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget
     * .AdapterView, android.view.View, int, long)
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view,
                            final int position, long id) {
        if (PreferenceManager.getIfHomeItemClickEnabled(mContext)) {
            System.out.println("Position homelist:" + position);
            displayView(position);
        }
    }

    /*******************************************************
     * Method name : displayView Description : display fragment view according
     * to position Parameters : position Return type : void Date : Oct 29, 2014
     * Author : Rijo K Jose
     *****************************************************/
    private void displayView(int position) {
        mFragment = null;
        tabPositionProceed=position;
        imageButton2.setVisibility(View.VISIBLE);

        if (PreferenceManager.getUserId(mContext).equals(""))
        {
                switch (position) {
                    case 0:
                        // home

                        mFragment = new HomeScreenGuestUserFragment(
                                mListItemArray[position], mDrawerLayout, mHomeListView, linearLayout,
                                mListItemArray, mListImgArray);
                        replaceFragmentsSelected(position);

                        break;

//                    case 1:
//                        // Notifications
//                        mFragment = new NotificationsFragmentNew(mListItemArray[position],
//                                TAB_NOTIFICATIONS_GUEST);
//                        replaceFragmentsSelected(position);
//
//                        break;
                    case 1:
                        // communications
                        mFragment = new CommunicationsFragment(
                                mListItemArray[position], TAB_COMMUNICATIONS_GUEST);
                        replaceFragmentsSelected(position);

                        break;
                    case 2:
                        //parent essentials
                        mFragment = new ParentEssentialsFragment(mListItemArray[position],
                                TAB_PARENT_ESSENTIALS_GUEST);
                        replaceFragmentsSelected(position);

                        break;
                    case 3:
                        // early years
                        mFragment = new EarlyYearsFragment(mListItemArray[position],
                                TAB_EARLYYEARS_GUEST);
                        replaceFragmentsSelected(position);

                        break;
                    case 4:
                        // primary
                        mFragment = new PrimaryFragment(mListItemArray[position],
                                TAB_PRIMARY_GUEST);
                        replaceFragmentsSelected(position);

                        break;
                    case 5:
                        // secondary
                        mFragment = new SecondaryFragment(mListItemArray[position],
                                TAB_SECONDARY_GUEST);
                        replaceFragmentsSelected(position);

                        break;
                    case 6:
                        // IB programme
                        mFragment = new IbProgrammeFragment(mListItemArray[position],
                                TAB_IB_PROGRAMME_GUEST);
                        replaceFragmentsSelected(position);

                        break;
                    case 7:
                        // performing arts
                        mFragment = new PerformingArtsFragment(mListItemArray[position],
                                TAB_PERFORMING_ARTS_GUEST);
                        replaceFragmentsSelected(position);

                        break;
//                    case 9:
//                        // CCAs
//                        mFragment = new CcaFragmentMain(mListItemArray[position],
//                                TAB_CCAS_GUEST);
//                        replaceFragmentsSelected(position);
//
//                        break;
                    case 8:
                        // Nae programmes
                        mFragment = new NaeProgrammesFragment(mListItemArray[position],
                                TAB_NAE_PROGRAMMES_GUEST);
                        replaceFragmentsSelected(position);

                        break;


                    case 9:
                        // about us
                        mFragment = new AboutUsFragment(mListItemArray[position],
                                TAB_ABOUT_US_GUEST);
                        replaceFragmentsSelected(position);

                        break;
                    case 10:
                        // contact us
                        mFragment = new ContactUsFragment(mListItemArray[position],
                                TAB_CONTACT_US_GUEST);
                        if (Build.VERSION.SDK_INT < 23) {
                            //Do not need to check the permission
                            replaceFragmentsSelected(position);
                        } else {

                            if (ActivityCompat.checkSelfPermission(mActivity, permissionsRequiredLocation[0]) != PackageManager.PERMISSION_GRANTED
                                    || ActivityCompat.checkSelfPermission(mActivity, permissionsRequiredLocation[1]) != PackageManager.PERMISSION_GRANTED) {
                                if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permissionsRequiredLocation[0])
                                        || ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permissionsRequiredLocation[1])) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                                    builder.setTitle("Need Location Permission");
                                    builder.setMessage("This module needs location permissions.");

                                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                            ActivityCompat.requestPermissions(mActivity, permissionsRequiredLocation, PERMISSION_CALLBACK_CONSTANT_LOCATION);
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

                                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                                    builder.setTitle("Need Location Permission");
                                    builder.setMessage("This module needs location permissions.");
                                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                            locationToSettings = true;

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
                                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                                    builder.setTitle("Need Location Permission");
                                    builder.setMessage("This module needs location permissions.");
                                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                            locationToSettings = true;

                                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            Uri uri = Uri.fromParts("package", mActivity.getPackageName(), null);
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
//                        ActivityCompat.requestPermissions(mActivity, permissionsRequired, PERMISSION_CALLBACK_CONSTANT_CALENDAR);
                                    requestPermissions(permissionsRequiredLocation, PERMISSION_CALLBACK_CONSTANT_LOCATION);

                                }
                                SharedPreferences.Editor editor = locationPermissionStatus.edit();
                                editor.putBoolean(permissionsRequiredLocation[0],true);
                                editor.commit();
                            }

                            else
                            {
                                replaceFragmentsSelected(position);

                            }
                        }
                        break;
                    case 13:
                        // Trips
                        mFragment = new TripsFragmentNew(mListItemArray[position],
                                TAB_TRIPS_REG);
                        replaceFragmentsSelected(position);

                        break;

                    case 14:
                        // Canteen
                        mFragment = new CanteenFragmentNew(mListItemArray[position],
                                TAB_CANTEEN_REG);
                        replaceFragmentsSelected(position);

                        break;
                    default:
                        break;
                }


        } else {

            switch (position) {
                case 0:
                    // home
//                    mFragment = new HomeScreenRegisteredUserFragment(
//                            mListItemArray[position], mDrawerLayout, mHomeListView,
//                            mListItemArray, mListImgArray);

                    mFragment = new HomeScreenRegisteredUserFragment(
                            mListItemArray[position], mDrawerLayout, mHomeListView, linearLayout,
                            mListItemArray, mListImgArray);
                    replaceFragmentsSelected(position);

                    break;
                   /* case 1:
                    // Notifications
                    mFragment = new NasTodayFragment(mListItemArray[position],
                            TAB_NAS_TODAY_REG);
                    replaceFragmentsSelected(position);

                    break;*/

                case 1:
                    // Calendar
                    if (PreferenceManager.getStaffOnly(mContext).equalsIgnoreCase("1"))
                    {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "This feature is not available for staff", R.drawable.exclamationicon, R.drawable.round);

                    }
                    else
                    {
                        mFragment = new CalendarFragment(mListItemArray[position],
                                TAB_CALENDAR_REG);
                        if (Build.VERSION.SDK_INT < 23) {
                            //Do not need to check the permission
                            replaceFragmentsSelected(position);
                        } else {

                            if (ActivityCompat.checkSelfPermission(mActivity, permissionsRequiredCalendar[0]) != PackageManager.PERMISSION_GRANTED
                                    || ActivityCompat.checkSelfPermission(mActivity, permissionsRequiredCalendar[1]) != PackageManager.PERMISSION_GRANTED) {
                                if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permissionsRequiredCalendar[0])
                                        || ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permissionsRequiredCalendar[1])) {
                                    //Show information about why you need the permission
                                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                                    builder.setTitle("Need Calendar Permission");
                                    builder.setMessage("This module needs Calendar permissions.");
                                    builder.setCancelable(false);

                                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                            ActivityCompat.requestPermissions(mActivity, permissionsRequiredCalendar, PERMISSION_CALLBACK_CONSTANT_CALENDAR);
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
                                    System.out.println("Permission0");

                                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                                    builder.setTitle("Need Calendar Permission");
                                    builder.setMessage("This module needs Calendar permissions.");
                                    builder.setCancelable(false);

                                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                            calendarToSettings = true;

                                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            Uri uri = Uri.fromParts("package", mActivity.getPackageName(), null);
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
                                    System.out.println("Permission1");
                                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                                    builder.setTitle("Need Calendar Permission");
                                    builder.setMessage("This module needs Calendar permissions.");
                                    builder.setCancelable(false);

                                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                            calendarToSettings = true;

                                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            Uri uri = Uri.fromParts("package", mActivity.getPackageName(), null);
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
                                    System.out.println("Permission3");

                                    //just request the permission
//                        ActivityCompat.requestPermissions(mActivity, permissionsRequired, PERMISSION_CALLBACK_CONSTANT_CALENDAR);
                                    ActivityCompat.requestPermissions(mActivity,permissionsRequiredCalendar, PERMISSION_CALLBACK_CONSTANT_CALENDAR);

                                }
                                SharedPreferences.Editor editor = calendarPermissionStatus.edit();
                                editor.putBoolean(permissionsRequiredCalendar[0],true);
                                editor.commit();
                            }
                            else
                            {
                                replaceFragmentsSelected(position);

                            }
                        }
                    }


                    break;
                case 2:
                    // Notifications
                    mFragment = new NotificationsFragmentNew(mListItemArray[position],
                            TAB_NOTIFICATIONS_REG);
                    replaceFragmentsSelected(position);

                    break;
                case 3:
                    // communications
                    mFragment = new CommunicationsFragment(
                            mListItemArray[position], TAB_COMMUNICATIONS_REG);
                    replaceFragmentsSelected(position);

                    break;
                case 4:
                    // absence
                    if (PreferenceManager.getStaffOnly(mContext).equalsIgnoreCase("1"))
                    {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "This feature is not available for staff", R.drawable.exclamationicon, R.drawable.round);

                    }
                    else
                    {
                        mFragment = new AbsenceFragment(
                                mListItemArray[position], TAB_ABSENSE_REG);
                        replaceFragmentsSelected(position);
                    }


                    break;
                case 5:
                    // Trips
                    if (PreferenceManager.getStaffOnly(mContext).equalsIgnoreCase("1"))
                    {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "This feature is not available for staff", R.drawable.exclamationicon, R.drawable.round);

                    }
                    else
                    {
                        mFragment = new TripsFragmentNew(mListItemArray[position],
                                TAB_TRIPS_REG);
                        replaceFragmentsSelected(position);

                    }

                    break;
                case 6:

                    mFragment = new CanteenFragmentNew(mListItemArray[position],
                            TAB_CANTEEN_REG);
                    replaceFragmentsSelected(position);

                    break;
                case 7:
                    //parent essentials
                    mFragment = new ParentEssentialsFragment(mListItemArray[position],
                            TAB_PARENT_ESSENTIALS_REG);
                    replaceFragmentsSelected(position);

                    break;
                case 8:
                    // early years
                    mFragment = new EarlyYearsFragment(mListItemArray[position],
                            TAB_EARLYYEARS_REG);
                    replaceFragmentsSelected(position);

                    break;
                case 9:
                    // primary
                    mFragment = new PrimaryFragment(mListItemArray[position],
                            TAB_PRIMARY_REG);
                    replaceFragmentsSelected(position);

                    break;
                case 10:
                    // secondary
                    mFragment = new SecondaryFragment(mListItemArray[position],
                            TAB_SECONDARY_REG);
                    replaceFragmentsSelected(position);

                    break;
                case 11:
                    // IB programme
                    mFragment = new IbProgrammeFragment(mListItemArray[position],
                            TAB_IB_PROGRAMME_REG);
                    replaceFragmentsSelected(position);

                    break;
                case 12:
                    // Sports
                    if (PreferenceManager.getStaffOnly(mContext).equalsIgnoreCase("1"))
                    {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "This feature is not available for staff", R.drawable.exclamationicon, R.drawable.round);

                    }
                    else
                    {
                        mFragment = new SportsMainScreenFragment(mListItemArray[position],
                                TAB_SPORTS_REG);
                        replaceFragmentsSelected(position);
                    }


                    break;
                case 13:
                    // performing arts
                    mFragment = new PerformingArtsFragment(mListItemArray[position],
                            TAB_PERFORMING_ARTS_REG);
                    replaceFragmentsSelected(position);

                    break;
                case 14:
                    // CCAs
                    if (PreferenceManager.getStaffOnly(mContext).equalsIgnoreCase("1"))
                    {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "This feature is not available for staff", R.drawable.exclamationicon, R.drawable.round);

                    }
                    else
                    {
                        mFragment = new CcaFragmentMain(mListItemArray[position],
                                TAB_CCAS_REG);
                        replaceFragmentsSelected(position);
                    }


                    break;
                case 15:
                    // parents association
                    if (PreferenceManager.getStaffOnly(mContext).equalsIgnoreCase("1"))
                    {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "This feature is not available for staff", R.drawable.exclamationicon, R.drawable.round);

                    }
                    else
                    {
                        mFragment = new ParentAssociationsFragment(mListItemArray[position],
                                TAB_PARENTS_ASSOCIATION_REG);
                        replaceFragmentsSelected(position);
                    }

                    break;
                case 16:
                    // parents' evening
                    if (PreferenceManager.getStaffOnly(mContext).equalsIgnoreCase("1"))
                    {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "This feature is not available for staff", R.drawable.exclamationicon, R.drawable.round);

                    }
                    else
                    {
                        mFragment = new ParentsEveningFragment(mListItemArray[position],
                                TAB_PARENTS_MEETING_REG);
                        replaceFragmentsSelected(position);
                    }


                    break;
                case 17:
                    // Nae programmes
                    mFragment = new NaeProgrammesFragment(mListItemArray[position],
                            TAB_NAE_PROGRAMMES_REG);
                    replaceFragmentsSelected(position);

                    break;
                case 18:
                    // Report
                    if (PreferenceManager.getStaffOnly(mContext).equalsIgnoreCase("1"))
                    {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "This feature is not available for staff", R.drawable.exclamationicon, R.drawable.round);

                    }
                    else
                    {
                        mFragment = new ReportFragment(mListItemArray[position],
                                TAB_REPORT_REG);
                        replaceFragmentsSelected(position);
                    }


                    break;
                case 19:
                    // Report
                    if (PreferenceManager.getStaffOnly(mContext).equalsIgnoreCase("1"))
                    {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "This feature is not available for staff", R.drawable.exclamationicon, R.drawable.round);

                    }
                    else
                    {
                        mFragment = new PermissionSlipsFragment(mListItemArray[position],
                                TAB_PERMISSION_SLIP_REG);
                        replaceFragmentsSelected(position);
                    }


                    break;
                case 20:
                    // gallery
                    if (PreferenceManager.getStaffOnly(mContext).equalsIgnoreCase("1"))
                    {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "This feature is not available for staff", R.drawable.exclamationicon, R.drawable.round);

                    }
                    else
                    {
                        mFragment = new GalleryFragment(mListItemArray[position],
                                TAB_GALLERY_REG);
                        if (Build.VERSION.SDK_INT < 23) {
                            //Do not need to check the permission
                            replaceFragmentsSelected(position);
                        } else {

                            if (ActivityCompat.checkSelfPermission(mActivity, permissionsRequiredExternalStorage[0]) != PackageManager.PERMISSION_GRANTED
                                    || ActivityCompat.checkSelfPermission(mActivity, permissionsRequiredExternalStorage[1]) != PackageManager.PERMISSION_GRANTED) {
                                if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permissionsRequiredExternalStorage[0])
                                        || ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permissionsRequiredExternalStorage[1])) {
                                    //Show information about why you need the permission
                                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                                    builder.setTitle("Need Storage Permission");
                                    builder.setMessage("This module needs Storage permissions.");

                                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                            ActivityCompat.requestPermissions(mActivity, permissionsRequiredExternalStorage, PERMISSION_CALLBACK_CONSTANT_EXTERNAL_STORAGE);
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
                                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                                    builder.setTitle("Need Storage Permission");
                                    builder.setMessage("This module needs Storage permissions.");

                                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                            externalStorageToSettings = true;

                                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            Uri uri = Uri.fromParts("package", mActivity.getPackageName(), null);
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
                                    AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                                    builder.setTitle("Need Storage Permission");
                                    builder.setMessage("This module needs Storage permissions.");
                                    builder.setCancelable(false);

                                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                            externalStorageToSettings = true;

                                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            Uri uri = Uri.fromParts("package", mActivity.getPackageName(), null);
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
//                        ActivityCompat.requestPermissions(mActivity, permissionsRequired, PERMISSION_CALLBACK_CONSTANT_CALENDAR);
                                    ActivityCompat.requestPermissions(mActivity,permissionsRequiredExternalStorage, PERMISSION_CALLBACK_CONSTANT_EXTERNAL_STORAGE);

                                }
                                SharedPreferences.Editor editor = externalStoragePermissionStatus.edit();
                                editor.putBoolean(permissionsRequiredExternalStorage[0],true);
                                editor.commit();
                            }
                            else
                            {
                                replaceFragmentsSelected(position);

                            }
                        }

                    }

                    break;
                case 21:
                    // about us
                    mFragment = new AboutUsFragment(mListItemArray[position],
                            TAB_ABOUT_US_REG);
                    replaceFragmentsSelected(position);

                    break;
                case 22:
                    // contact us
                    mFragment = new ContactUsFragment(mListItemArray[position],
                            TAB_CONTACT_US_REG);
                    if (Build.VERSION.SDK_INT < 23) {
                        //Do not need to check the permission
                        replaceFragmentsSelected(position);
                    } else {

                        if (ActivityCompat.checkSelfPermission(mActivity, permissionsRequiredLocation[0]) != PackageManager.PERMISSION_GRANTED
                                || ActivityCompat.checkSelfPermission(mActivity, permissionsRequiredLocation[1]) != PackageManager.PERMISSION_GRANTED) {
                            if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permissionsRequiredLocation[0])
                                    || ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permissionsRequiredLocation[1])) {
                                //Show information about why you need the permission
                                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                                builder.setTitle("Need Location Permission");
                                builder.setMessage("This module needs location permissions.");

                                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        ActivityCompat.requestPermissions(mActivity, permissionsRequiredLocation, PERMISSION_CALLBACK_CONSTANT_LOCATION);
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
                                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                                builder.setTitle("Need Location Permission");
                                builder.setMessage("This module needs location permissions.");
                                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        locationToSettings = true;

                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", mActivity.getPackageName(), null);
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
                                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                                builder.setTitle("Need Location Permission");
                                builder.setMessage("This module needs location permissions.");
                                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                        locationToSettings = true;

                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", mActivity.getPackageName(), null);
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
//                        ActivityCompat.requestPermissions(mActivity, permissionsRequired, PERMISSION_CALLBACK_CONSTANT_CALENDAR);
                                ActivityCompat.requestPermissions(mActivity,permissionsRequiredLocation, PERMISSION_CALLBACK_CONSTANT_LOCATION);

                            }
                            SharedPreferences.Editor editor = locationPermissionStatus.edit();
                            editor.putBoolean(permissionsRequiredLocation[0],true);
                            editor.commit();
                        }
                        else
                        {
                            replaceFragmentsSelected(position);

                        }
                    }

                    break;


//                case 22:
//                    // Report
//                    mFragment = new TripsFragmentNew(mListItemArray[position],
//                            TAB_TRIPS_REG);
//                    replaceFragmentsSelected(position);
//
//                    break;
//                case 22:
//                    // Canteen
//                    mFragment = new CanteenFragmentNew(mListItemArray[position],
//                            TAB_CANTEEN_REG);
//                    replaceFragmentsSelected(position);
//
//                    break;
             /*   case 21:
                    // about us
                    mFragment = new ReportFragment(mListItemArray[position],
                            TAB_REPORT_REG);
                    replaceFragmentsSelected(position);

                    break;*/
                default:
                    break;
            }

        }
//        replaceFragmentsSelected(position);
    }

    /*******************************************************
     * Method name : replaceFragmentsSelected Description : replace fragments
     * Parameters : position Return type : void Date : Nov 4, 2014 Author :
     * Rijo K Jose
     *****************************************************/
    private void replaceFragmentsSelected(final int position) {
        if (mFragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_container, mFragment,
                            mListItemArray[position])
                    .addToBackStack(mListItemArray[position]).commitAllowingStateLoss();
//                    .addToBackStack(mListItemArray[position]).commit();
            // update selected item and title, then close the drawer
            mHomeListView.setItemChecked(position, true);
            mHomeListView.setSelection(position);
            mDrawerLayout.closeDrawer(linearLayout);
            getSupportActionBar().setTitle(R.string.null_value);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v7.app.ActionBarActivity#onBackPressed()
     */
    @Override
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(linearLayout);
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                FragmentManager fm = getSupportFragmentManager();
                Fragment currentFragment = fm
                        .findFragmentById(R.id.frame_container);
                System.out.println("nas current fragment "
                        + currentFragment.getClass().toString());
                if (currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.naisapp.fragments.home.HomeScreenRegisteredUserFragment")
                        || currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.naisapp.fragments.home.HomeScreenGuestUserFragment") || currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.naisapp.fragments.home.HomeScreenStaffUserFragment")) {
                    AppUtils.showAlert((Activity) mContext, getResources()
                            .getString(R.string.exit_alert), getResources()
                            .getString(R.string.ok), getResources()
                            .getString(R.string.cancel), true);
                } else if (currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.naisapp.fragments.aboutus.AboutUsFragment")
                        ||currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.naisapp.fragments.absence.AbsenceFragment")
                        || currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.naisapp.fragments.calendar.CalendarFragment")
                        || currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.naisapp.fragments.cca.CcaFragmentMain")
                        || currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.naisapp.fragments.parentassociation.ParentAssociationsFragment")
                        || currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.naisapp.fragments.communications.CommunicationsFragment")
                        || currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.naisapp.fragments.contact_us.ContactUsFragment")
                        || currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.naisapp.fragments.gallery.GalleryFragment")
                        || currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.naisapp.fragments.early_years.EarlyYearsFragment")
                        || currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.naisapp.fragments.social_media.SocialMediaFragment")
                        || currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.naisapp.fragments.ib_programme.IbProgrammeFragment")
                        || currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.naisapp.fragments.nae_programmes.NaeProgrammesFragment")

                        || currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.naisapp.fragments.notifications.NotificationsFragmentNew")
                        || currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.naisapp.fragments.parent_essentials.ParentEssentialsFragment")
                        || currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.naisapp.fragments.performing_arts.PerformingArtsFragment")
                        || currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.naisapp.fragments.primary.PrimaryFragment") || currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.naisapp.fragments.secondary.SecondaryFragment") || currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.naisapp.fragments.sports.SportsMainScreenFragment") || currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.naisapp.fragments.contact_us.ContactUsFragment") || currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.naisapp.fragments.nas_today.NasTodayFragment") || currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.naisapp.fragments.permission_slips.PermissionSlipsFragment")|| currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.naisapp.fragments.parents_evening.ParentsEveningFragment")|| currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.naisapp.fragments.trips.TripsFragmentNew")|| currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.naisapp.fragments.canteen_new.CanteenFragmentNew")) {
//                    imageButton.setImageResource(R.drawable.hamburgerbtn);
                    if ( currentFragment
                            .getClass()
                            .toString()
                            .equalsIgnoreCase(
                                    "class com.mobatia.naisapp.fragments.calendar.CalendarFragment")) {
                        if (dialogCal.isShowing()) {

                        } else {
                            displayView(0);

                        }
                    }
                    else {
                        displayView(0);

                    }

                } else {
                    System.out.println("working *** * *  8");
                    imageButton2.setVisibility(View.VISIBLE);
                    getSupportFragmentManager().popBackStack();
//                    getSupportFragmentManager().popBackStackImmediate();

                }
            }
            // Default action on back pressed
            else {
                AppUtils.showAlert((Activity) mContext, getResources()
                                .getString(R.string.exit_alert), getResources()
                                .getString(R.string.ok),
                        getResources().getString(R.string.cancel), true);
            }

        }
    }

    /*
     * (non-Javadoc)
     *
     * @see android.view.GestureDetector.OnGestureListener#onDown(android.view.
     * MotionEvent)
     */
    @Override
    public boolean onDown(MotionEvent e) {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * android.view.GestureDetector.OnGestureListener#onShowPress(android.view
     * .MotionEvent)
     */
    @Override
    public void onShowPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * android.view.GestureDetector.OnGestureListener#onSingleTapUp(android.
     * view.MotionEvent)
     */
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * android.view.GestureDetector.OnGestureListener#onScroll(android.view.
     * MotionEvent, android.view.MotionEvent, float, float)
     */
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * android.view.GestureDetector.OnGestureListener#onLongPress(android.view
     * .MotionEvent)
     */
    @Override
    public void onLongPress(MotionEvent e) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     *
     * @see android.view.GestureDetector.OnGestureListener#onFling(android.view.
     * MotionEvent, android.view.MotionEvent, float, float)
     */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        /** change home list width */
        // if (e2 != null && e1 != null) {
        // if ((e2.getX() - e1.getX()) > 0
        // && mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
        // int width = (int) (getResources().getDisplayMetrics().widthPixels /
        // 2.5);
        // DrawerLayout.LayoutParams params =
        // (android.support.v4.widget.DrawerLayout.LayoutParams) mHomeListView
        // .getLayoutParams();
        // params.width = width;
        // mHomeListView.setLayoutParams(params);
        // }
        // }
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * android.widget.AdapterView.OnItemLongClickListener#onItemLongClick(android
     * .widget.AdapterView, android.view.View, int, long)
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view,
                                   int position, long id) {
        PreferenceManager.setIfHomeItemClickEnabled(mContext, true);
        sPosition = position;
        view.setBackgroundColor(Color.parseColor("#47C2D1"));
        DragShadowBuilder shadowBuilder = new DragShadowBuilder(view);
        ClipData data = ClipData.newPlainText("", "");
        view.startDrag(data, shadowBuilder, view, 0);
        view.setVisibility(View.VISIBLE);
        mDrawerLayout.closeDrawer(linearLayout);
       /* if (position != 0) {
            // drag list view item
            PreferenceManager.setIfHomeItemClickEnabled(mContext, true);
            sPosition = position;
            view.setBackgroundColor(Color.parseColor("#47C2D1"));
            DragShadowBuilder shadowBuilder = new DragShadowBuilder(view);
            ClipData data = ClipData.newPlainText("", "");
            view.startDrag(data, shadowBuilder, view, 0);
            view.setVisibility(View.VISIBLE);
            mDrawerLayout.closeDrawer(linearLayout);

        } else {
            // if home in list view is selected
            AppUtils.showAlert((Activity) mContext,
                    getResources().getString(R.string.drag_impossible), "",
                    getResources().getString(R.string.ok), false);
            PreferenceManager.setIfHomeItemClickEnabled(mContext, true);
        }*/
        return false;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
        // settings
        mFragment = new SettingsFragment(SETTINGS, TAB_SETTINGS);
        if (mFragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, mFragment, SETTINGS)
                    .addToBackStack(SETTINGS).commit();
            mDrawerLayout.closeDrawer(linearLayout);
            getSupportActionBar().setTitle(R.string.null_value);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }


    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }
    private void proceedAfterPermission(int position) {
        imageButton2.setVisibility(View.VISIBLE);
        System.out.println("workingPerm:"+position);
        if (PreferenceManager.getUserId(mContext).equals(""))
        {
                switch (position) {
                    case 0:
                        // home
//                    mFragment = new HomeScreenGuestUserFragment(
//                            mListItemArray[position], mDrawerLayout, mHomeListView,
//                            mListItemArray, mListImgArray);
                        mFragment = new HomeScreenGuestUserFragment(
                                mListItemArray[position], mDrawerLayout, mHomeListView, linearLayout,
                                mListItemArray, mListImgArray);
                        break;
                /*case 1:
                    // NAS TODAY
                    mFragment = new NasTodayFragment(mListItemArray[position],
                            TAB_NAS_TODAY);
                    break;*/
//                    case 1:
//                        // Notifications
//                        mFragment = new NotificationsFragmentNew(mListItemArray[position],
//                                TAB_NOTIFICATIONS_GUEST);
//                        break;
                    case 1:
                        // communications
                        mFragment = new CommunicationsFragment(
                                mListItemArray[position], TAB_COMMUNICATIONS_GUEST);
                        break;
                    case 2:
                        //parent essentials
                        mFragment = new ParentEssentialsFragment(mListItemArray[position],
                                TAB_PARENT_ESSENTIALS_GUEST);
                        break;
                    case 3:
                        // early years
                        mFragment = new EarlyYearsFragment(mListItemArray[position],
                                TAB_EARLYYEARS_GUEST);
                        break;
                    case 4:
                        // primary
                        mFragment = new PrimaryFragment(mListItemArray[position],
                                TAB_PRIMARY_GUEST);
                        break;
                    case 5:
                        // secondary
                        mFragment = new SecondaryFragment(mListItemArray[position],
                                TAB_SECONDARY_GUEST);
                        break;
                    case 6:
                        // IB programme
                        mFragment = new IbProgrammeFragment(mListItemArray[position],
                                TAB_IB_PROGRAMME_GUEST);
                        break;
                    case 7:
                        // performing arts
                        mFragment = new PerformingArtsFragment(mListItemArray[position],
                                TAB_PERFORMING_ARTS_GUEST);
                        break;
//                    case 9:
//                        // CCAs
//                        mFragment = new CcaFragmentMain(mListItemArray[position],
//                                TAB_CCAS_GUEST);
//                        break;
                    case 8:
                        // Nae programmes
                        mFragment = new NaeProgrammesFragment(mListItemArray[position],
                                TAB_NAE_PROGRAMMES_GUEST);
                        break;
                    case 9:
                        // about us
                        mFragment = new AboutUsFragment(mListItemArray[position],
                                TAB_ABOUT_US_GUEST);
                        break;
                    case 10:
                        // contact us
                        mFragment = new ContactUsFragment(mListItemArray[position],
                                TAB_CONTACT_US_GUEST);
                        break;

                    default:
                        break;
                }


        } else {

            switch (position) {
                case 0:
                    // home
                    mFragment = new HomeScreenRegisteredUserFragment(
                            mListItemArray[position], mDrawerLayout, mHomeListView, linearLayout,
                            mListItemArray, mListImgArray);
                    break;
                case 1:
                    // Calendar
                    if (PreferenceManager.getStaffOnly(mContext).equalsIgnoreCase("1"))
                    {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "This feature is not available for staff", R.drawable.exclamationicon, R.drawable.round);

                    }
                    else
                    {
                        mFragment = new CalendarFragment(mListItemArray[position],
                                TAB_CALENDAR_REG);
                    }

                    break;
                case 2:
                    // Notifications
                    mFragment = new NotificationsFragmentNew(mListItemArray[position],
                            TAB_NOTIFICATIONS_REG);
                    break;
                case 3:
                    // communications
                    mFragment = new CommunicationsFragment(
                            mListItemArray[position], TAB_COMMUNICATIONS_REG);
                    break;
                case 4:
                    // communications
                    if (PreferenceManager.getStaffOnly(mContext).equalsIgnoreCase("1"))
                    {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "This feature is not available for staff", R.drawable.exclamationicon, R.drawable.round);

                    }
                    else
                    {
                        mFragment = new AbsenceFragment(
                                mListItemArray[position], TAB_ABSENSE_REG);
                    }

                    break;
                case 5:
                    // parents' evening
                    if (PreferenceManager.getStaffOnly(mContext).equalsIgnoreCase("1"))
                    {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "This feature is not available for staff", R.drawable.exclamationicon, R.drawable.round);

                    }
                    else
                    {
                        mFragment = new TripsFragmentNew(mListItemArray[position],
                                TAB_TRIPS_REG);
                    }


                    break;
                case 6:

                    mFragment = new CanteenFragmentNew(mListItemArray[position],
                            TAB_CANTEEN_REG);
                    //   replaceFragmentsSelected(position);

                    break;
                case 7:
                    //parent essentials
                    mFragment = new ParentEssentialsFragment(mListItemArray[position],
                            TAB_PARENT_ESSENTIALS_REG);
                    break;
                case 8:
                    // early years
                    mFragment = new EarlyYearsFragment(mListItemArray[position],
                            TAB_EARLYYEARS_REG);
                    break;
                case 9:
                    // primary
                    mFragment = new PrimaryFragment(mListItemArray[position],
                            TAB_PRIMARY_REG);
                    break;
                case 10:
                    // secondary
                    mFragment = new SecondaryFragment(mListItemArray[position],
                            TAB_SECONDARY_REG);
                    break;
                case 11:
                    // IB programme
                    mFragment = new IbProgrammeFragment(mListItemArray[position],
                            TAB_IB_PROGRAMME_REG);
                    break;
                case 12:
                    // Sports
                    if (PreferenceManager.getStaffOnly(mContext).equalsIgnoreCase("1"))
                    {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "This feature is not available for staff", R.drawable.exclamationicon, R.drawable.round);

                    }
                    else
                    {
                        mFragment = new SportsMainScreenFragment(mListItemArray[position],
                                TAB_SPORTS_REG);
                    }

                    break;
                case 13:
                    // performing arts
                    mFragment = new PerformingArtsFragment(mListItemArray[position],
                            TAB_PERFORMING_ARTS_REG);
                    break;
                case 14:
                    // CCAs
                    if (PreferenceManager.getStaffOnly(mContext).equalsIgnoreCase("1"))
                    {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "This feature is not available for staff", R.drawable.exclamationicon, R.drawable.round);

                    }
                    else
                    {
                        mFragment = new CcaFragmentMain(mListItemArray[position],
                                TAB_CCAS_REG);
                    }

                    break;
                case 15:
                    // parents association
                    if (PreferenceManager.getStaffOnly(mContext).equalsIgnoreCase("1"))
                    {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "This feature is not available for staff", R.drawable.exclamationicon, R.drawable.round);

                    }
                    else
                    {
                        mFragment = new ParentAssociationsFragment(mListItemArray[position],
                                TAB_PARENTS_ASSOCIATION_REG);
                    }

                    break;
                case 16:
                    // parents' evening
                    if (PreferenceManager.getStaffOnly(mContext).equalsIgnoreCase("1"))
                    {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "This feature is not available for staff", R.drawable.exclamationicon, R.drawable.round);

                    }
                    else
                    {
                        mFragment = new ParentsEveningFragment(mListItemArray[position],
                                TAB_PARENTS_MEETING_REG);
                    }


                    break;

                case 17:
                    // Nae programmes
                    mFragment = new NaeProgrammesFragment(mListItemArray[position],
                            TAB_NAE_PROGRAMMES_REG);
                    break;
                case 18:
                    // social media
                    if (PreferenceManager.getStaffOnly(mContext).equalsIgnoreCase("1"))
                    {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "This feature is not available for staff", R.drawable.exclamationicon, R.drawable.round);

                    }
                    else
                    {
                        mFragment = new ReportFragment(mListItemArray[position],
                                TAB_REPORT_REG);
                        replaceFragmentsSelected(position);
                    }


                    break;
                case 19:
                    // social media
                    if (PreferenceManager.getStaffOnly(mContext).equalsIgnoreCase("1"))
                    {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "This feature is not available for staff", R.drawable.exclamationicon, R.drawable.round);

                    }
                    else
                    {
                        mFragment = new PermissionSlipsFragment(mListItemArray[position],
                                TAB_PERMISSION_SLIP_REG);
                        replaceFragmentsSelected(position);
                    }


                    break;
                case 20:
                    // gallery
                    if (PreferenceManager.getStaffOnly(mContext).equalsIgnoreCase("1"))
                    {
                        AppUtils.showDialogAlertDismiss((Activity) mContext, "Alert", "This feature is not available for staff", R.drawable.exclamationicon, R.drawable.round);

                    }
                    else
                    {
                        mFragment = new GalleryFragment(mListItemArray[position],
                                TAB_GALLERY_REG);
                    }

                    break;
                case 21:
                    // about us
                    mFragment = new AboutUsFragment(mListItemArray[position],
                            TAB_ABOUT_US_REG);
                    break;
                case 22:
                    // contact us
                    mFragment = new ContactUsFragment(mListItemArray[position],
                            TAB_CONTACT_US_REG);
                    break;

//                case 22:
//                    // parents' evening
//
//                    mFragment = new CanteenFragmentNew(mListItemArray[position],
//                            TAB_CANTEEN_REG);
//
//                    break;



                default:
                    break;
            }

        }
        replaceFragmentsSelected(position);
//         replaceFragmentsSelected(position);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
                proceedAfterPermission(tabPositionProceed);
            } else if(ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.READ_CALENDAR)){
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
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
            } else if(ActivityCompat.shouldShowRequestPermissionRationale(mActivity,Manifest.permission.WRITE_CALENDAR)){
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
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
            } else  {
//                Toast.makeText(mActivity,"Unable to get Permission",Toast.LENGTH_LONG).show();
                calendarToSettings = true;
                System.out.println("Permission4");

                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", mActivity.getPackageName(), null);
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
                proceedAfterPermission(tabPositionProceed);
            } else if(ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE)){
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setTitle("Need Storage Permissions");
                builder.setMessage("This module needs storage permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        externalStorageToSettings = false;

                        ActivityCompat.requestPermissions(mActivity, permissionsRequiredExternalStorage, PERMISSION_CALLBACK_CONSTANT_EXTERNAL_STORAGE);
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
            } else if(ActivityCompat.shouldShowRequestPermissionRationale(mActivity,Manifest.permission.WRITE_CALENDAR)){
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setTitle("Need Storage Permissions");
                builder.setMessage("This module needs storage permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        externalStorageToSettings = false;

                        ActivityCompat.requestPermissions(mActivity, permissionsRequiredExternalStorage, PERMISSION_CALLBACK_CONSTANT_EXTERNAL_STORAGE);
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
                externalStorageToSettings = true;

                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", mActivity.getPackageName(), null);
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
                proceedAfterPermission(tabPositionProceed);
            } else if(ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.ACCESS_FINE_LOCATION)){
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setTitle("Need Location Permissions");
                builder.setMessage("This module needs location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        locationToSettings = false;

                        ActivityCompat.requestPermissions(mActivity, permissionsRequiredLocation, PERMISSION_CALLBACK_CONSTANT_LOCATION);
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
            } else if(ActivityCompat.shouldShowRequestPermissionRationale(mActivity,Manifest.permission.ACCESS_COARSE_LOCATION)){
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setTitle("Need Location Permissions");
                builder.setMessage("This module needs location permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        locationToSettings = false;

                        ActivityCompat.requestPermissions(mActivity, permissionsRequiredLocation, PERMISSION_CALLBACK_CONSTANT_LOCATION);
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
                locationToSettings = true;

                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", mActivity.getPackageName(), null);
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
            if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission(tabPositionProceed);
            }
        }else	if (requestCode == REQUEST_PERMISSION_EXTERNAL_STORAGE) {
            if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission(tabPositionProceed);
            }
        }else	if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission(tabPositionProceed);
            }
        }
    }



    @Override
    protected void onRestart() {
        super.onRestart();
        if (AppUtils.checkInternet(mContext)) {
            getBanner();
            if(PreferenceManager.getUserId(mContext).equalsIgnoreCase(""))
            {
                if (!PreferenceManager.getStaffId(mContext).equalsIgnoreCase(""))
                {
                    getBadge();
                }
                else
                {

                }
            }
            else
            {

            }
        }



    }


    public void getBanner() {

        try {
            final VolleyWrapper manager = new VolleyWrapper(URL_GET_USER_BANNER);
            String[] name = new String[]{JTAG_ACCESSTOKEN};
            String[] value = new String[]{PreferenceManager.getAccessToken(mContext)};

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
                                        String android_app_version = respObject.optString(JTAG_ANDROID_APP_VERSION);
                                        PreferenceManager.setVersionFromApi(mContext,android_app_version);
                                        String versionFromPreference = PreferenceManager.getVersionFromApi(mContext).replace(".","");
                                        int versionNumberAsInteger = Integer.parseInt(versionFromPreference);
                                        String replaceVersion = AppUtils.getVersionInfo(mContext).replace(".","");
                                        int replaceCurrentVersion=Integer.parseInt(replaceVersion);
                                        if (!(PreferenceManager.getVersionFromApi(mContext).equalsIgnoreCase(""))) {
                                            if(versionNumberAsInteger >replaceCurrentVersion) {

                                                AppUtils.showDialogAlertUpdate(mContext);
                                            }

                                        }
                                    }
                                } else if (responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) ||
                                        responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                                        responsCode.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
                                    AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                                        @Override
                                        public void getAccessToken() {
                                        }
                                    });
                                    getBanner();

                                }
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }

                @Override
                public void responseFailure(String failureResponse) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    public void getBadge()
    {

        try {
            final VolleyWrapper manager = new VolleyWrapper(URL_GET_STAFF_BADGE_COUNT);
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
                                        PreferenceManager.setStaffNotificationBadge(mContext,respObject.optString("notification_badge"));
                                        PreferenceManager.setStaffNotificationEditedBadge(mContext,respObject.optString("notification_edited_badge"));
                                    }
                                } else if (responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_MISSING) ||
                                        responsCode.equalsIgnoreCase(RESPONSE_ACCESSTOKEN_EXPIRED) ||
                                        responsCode.equalsIgnoreCase(RESPONSE_INVALID_TOKEN)) {
                                    AppUtils.postInitParam(mContext, new AppUtils.GetAccessTokenInterface() {
                                        @Override
                                        public void getAccessToken() {
                                        }
                                    });
                                    getBadge();

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
}

