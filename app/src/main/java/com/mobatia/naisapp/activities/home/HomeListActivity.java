package com.mobatia.naisapp.activities.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.legacy.app.ActionBarDrawerToggle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.mobatia.naisapp.constants.NaisClassNameConstants;
import com.mobatia.naisapp.constants.NaisDBConstants;
import com.mobatia.naisapp.constants.NaisTabConstants;
import com.mobatia.naisapp.fragments.settings.SettingsFragment;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.R;

import com.mobatia.naisapp.activities.home.adapter.HomeListAdapter;

import com.mobatia.naisapp.sqliteservice.DatabaseHelper;

import java.io.IOException;


@SuppressLint("NewApi")
public class HomeListActivity extends AppCompatActivity implements
        OnItemClickListener, OnGestureListener, OnItemLongClickListener,
        NaisDBConstants, NaisTabConstants, OnClickListener,
        NaisClassNameConstants,OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMapLongClickListener {
    LinearLayout linearLayout;
    private ListView mHomeListView;
    private HomeListAdapter mListAdapter;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home_list);
        mContext = this;
        mActivity = this;
        extras = getIntent().getExtras();
        if (extras != null) {
            notificationRecieved = extras.getInt("Notification_Recieved", 0);
        }
        initialiseUI();
        initialSettings();

        checkDatabase();
        if (notificationRecieved==1)
        {

        }
        else
        {

        }
    }

    /*******************************************************
     * Method name : checkDatabase() Description : check if database exist and
     * open it Parameters : nil Return type : void Date : Nov 21, 2014 Author :
     * Rijo K Jose
     *****************************************************/
    private void checkDatabase() {
        DatabaseHelper dbHelper = new DatabaseHelper(HomeListActivity.this,
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
        } else {
            // guest user
            mListItemArray = mContext.getResources().getStringArray(
                    R.array.home_list_content_guest_items);
            mListImgArray = mContext.getResources().obtainTypedArray(
                    R.array.home_list_guest_icons);
        }
        mListAdapter = new HomeListAdapter(mContext, mListItemArray,
                mListImgArray, R.layout.custom_list_adapter, true);
        mHomeListView.setAdapter(mListAdapter);
        mHomeListView.setBackgroundColor(getResources().getColor(
                R.color.split_bg));
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        int width = (int) (getResources().getDisplayMetrics().widthPixels / 2.2);
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
                    System.out.println("currentFirstVisibleItem---" + currentFirstVisibleItem);
                    System.out.println("currentFirstVisibleItem---" + firstVisibleItem);
                    System.out.println("currentFirstVisibleItem---" + mHomeListView.getCount());
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
        getSupportActionBar().setIcon(
                new ColorDrawable(getResources().getColor(
                        android.R.color.transparent)));
        getSupportActionBar().setTitle(R.string.null_value);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.hamburgerbtn);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setCustomView(R.layout.custom_action_view_home);
        getSupportActionBar().show();

        mDrawerToggle.syncState();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.acton_bar_header, menu);
        FragmentManager fm = getSupportFragmentManager();
        MenuItem item = menu.findItem(R.id.action_settings);
        Fragment currentFragment = fm.findFragmentById(R.id.frame_container);
        System.out.println("nas current fragment "
                + currentFragment.getClass().toString());
        if (currentFragment
                .getClass()
                .toString()
                .equalsIgnoreCase(
                        "class com.mobatia.naisapp.fragments.settings.SettingsFragment")) {
            item.setVisible(false);
        } else {
            item.setVisible(true);
            item.getActionView().setOnClickListener(this);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action bar actions click
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mDrawerLayout.isDrawerOpen(linearLayout)) {
                    mDrawerLayout.closeDrawer(linearLayout);
                } else {
                    mDrawerLayout.openDrawer(linearLayout);
                }
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
            //displayView(position);
        }
    }

    /*******************************************************
     * Method name : displayView Description : display fragment view according
     * to position Parameters : position Return type : void Date : Oct 29, 2014
     * Author : Rijo K Jose
     *****************************************************/

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
                    .addToBackStack(mListItemArray[position]).commit();
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
                                "class com.mobatia.naisapp.fragments.home.HomeScreenGuestUserFragment")) {
                    AppUtils.showAlert((Activity) mContext, getResources()
                            .getString(R.string.exit_alert), getResources()
                            .getString(R.string.ok), getResources()
                            .getString(R.string.cancel), true);
                } else if (currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.naisapp.fragments.aboutus.AboutUsFragment")
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
                                "class com.mobatia.naisapp.fragments.notifications.NotificationsFragment")
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
                                "class com.mobatia.naisapp.fragments.parents_evening.ParentsEveningFragment")) {
                  //  displayView(0);
                    /*|| currentFragment
                        .getClass()
                        .toString()
                        .equalsIgnoreCase(
                                "class com.mobatia.naisapp.fragments.settings.SettingsFragment")*/
                } else {
                    getSupportFragmentManager().popBackStack();

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
        if (position != 0) {
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
        }
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
}
