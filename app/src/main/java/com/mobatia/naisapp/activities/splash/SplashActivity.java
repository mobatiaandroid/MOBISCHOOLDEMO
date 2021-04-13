package com.mobatia.naisapp.activities.splash;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;

import android.view.View;
import android.widget.Toast;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.activities.login.LoginActivity;
import com.mobatia.naisapp.activities.tutorial.TutorialActivity;
import com.mobatia.naisapp.constants.IntentPassValueConstants;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.parents_evening.model.StudentModel;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.service.OnClearFromRecentService;
import com.mobatia.naisapp.volleywrappermanager.VolleyWrapper;
import com.squareup.picasso.Picasso;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SplashActivity extends Activity implements
        IntentPassValueConstants, JSONConstants, URLConstants {
    ArrayList<StudentModel> studentsModelArrayList = new ArrayList<>();
    ArrayList<String> studentList = new ArrayList<>();
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = this;
        if (AppUtils.checkInternet(mContext)) {
            AppUtils.postInitParams(mContext, new AppUtils.GetAccessTokenInterface() {
                @Override
                public void getAccessToken()
                {

                }
            });
            goToNextView();
        }else{
            AppUtils.showDialogAlertDismiss((Activity)mContext,"Network Error",getString(R.string.no_internet),R.drawable.nonetworkicon,R.drawable.roundred);

        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    /*******************************************************
     * Method name : goToNextView() Description : go to next view after a delay
     * Parameters : nil Return type : void Date : March 8, 2017 Author : RIJO K JOSE
     *****************************************************/
    private void goToNextView() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                startService(new Intent(getBaseContext(), OnClearFromRecentService.class));
                if (PreferenceManager.getIsFirstLaunch(mContext)&& PreferenceManager.getUserId(mContext).equals("") && PreferenceManager.getStaffId(mContext).equals("")) {
                    Intent tutorialIntent = new Intent(mContext,
                            TutorialActivity.class);
                    tutorialIntent.putExtra(TYPE, 1);
                    startActivity(tutorialIntent);
                    PreferenceManager.setIsFirstLaunch(mContext, false);
                    finish();
                }
                else  if(PreferenceManager.getUserId(mContext).equals("")) {
                    System.out.println("Staff id logout"+PreferenceManager.getStaffId(mContext));
                    if (PreferenceManager.getStaffId(mContext).equals(""))
                    {
                        Intent loginIntent = new Intent(mContext,
                                LoginActivity.class);
                        startActivity(loginIntent);
                        finish();
                    }
                    else
                    {
                        Intent loginIntent = new Intent(mContext,
                                HomeListAppCompatActivity.class);
                        startActivity(loginIntent);
                        finish();
                    }
                }else{
                    Intent loginIntent = new Intent(mContext,
                            HomeListAppCompatActivity.class);
                    startActivity(loginIntent);
                    finish();
                }

            }
        }, 5000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
// TODO Auto-generated method stub
//super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case 100: {
                Map<String, Integer> perms = new HashMap<String, Integer>();
// Initial
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_CALENDAR, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_CALENDAR, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CALL_PHONE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
// Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                for (int i = 0, len = permissions.length; i < len; i++) {
                    String permission = permissions[i];
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        boolean showRationale = false;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            showRationale = shouldShowRequestPermissionRationale( permission );
                        }
                        if (! showRationale) {
                            // user denied flagging NEVER ASK AGAIN
                            // you can either enable some fall back,
                            // disable features of your app
                            // or open another dialog explaining
                            // again the permission and directing to
                            // the app setting
                            Toast.makeText(mContext, "Unable to continue without granting permissions", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.fromParts("package", mContext.getPackageName(), null));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                }
                if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED && perms.get(Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED && perms.get(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED
                        &&perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED&& (grantResults.length > 0)) {
                 // All Permissions Granted
                    goToNextView();
                }
                else {

                    ActivityCompat.requestPermissions(this, permissions, 100);


                }
                return;
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    private StudentModel addStudentDetails(JSONObject dataObject) {
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