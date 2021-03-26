package com.mobatia.naisapp.appcontroller;

import android.content.res.TypedArray;
import androidx.multidex.MultiDexApplication;
import androidx.drawerlayout.widget.DrawerLayout;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.mobatia.naisapp.activities.cca.model.WeekListModel;
import com.mobatia.naisapp.activities.sports.filter.FilterModel;
import com.mobatia.naisapp.fragments.gallery.model.PhotosListModel;

import java.util.ArrayList;


public class AppController extends MultiDexApplication {

    public static final String TAG = AppController.class
            .getSimpleName();
    public static final boolean isWebViewVisible=false;
    public static boolean isaddCall=true;
    public static int Position;
    public static int basketPosition;
    public static int myHistoryPosition;
  public static String stud_id;
  public static String categoryId;
    private RequestQueue mRequestQueue;

    private static AppController mInstance;
    public static ArrayList<String> eventIdList=new ArrayList<>();
    public static boolean isProviderEnabled=false;
    public static boolean isVisited=false;
    public static boolean isAPISuccess=false;
    public static ArrayList<WeekListModel> weekList;
    public static ArrayList<Integer> weekListWithData;
    public static ArrayList<PhotosListModel> mPhotosModelArrayListGallery;
    public static TypedArray mListImgArrays;
    public static String mTitles;
    public static DrawerLayout mDrawerLayouts;
    public static LinearLayout mLinearLayouts;
    public static ListView mListViews;
    public static String[] listitemArrays;
    public  static ArrayList<FilterModel> filterModelArrayListFirst;
public static boolean isCommunicationDetailVisited=false;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }
    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
}}