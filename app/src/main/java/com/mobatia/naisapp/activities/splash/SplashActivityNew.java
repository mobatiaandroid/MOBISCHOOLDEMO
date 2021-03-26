package com.mobatia.naisapp.activities.splash;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.core.app.ActivityCompat;
import android.util.Log;
import android.widget.VideoView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.activities.login.LoginActivity;
import com.mobatia.naisapp.activities.tutorial.TutorialActivity;
import com.mobatia.naisapp.constants.IntentPassValueConstants;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.PreferenceManager;
import com.mobatia.naisapp.service.OnClearFromRecentService;

import java.util.HashMap;
import java.util.Map;


public class SplashActivityNew extends Activity implements
        IntentPassValueConstants, JSONConstants, URLConstants {

    private Context mContext;
    //    String registrationId;
//    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_CALENDAR
            , Manifest.permission.WRITE_CALENDAR , Manifest.permission.CALL_PHONE};

    private static final int APP_API = 17;

    class C00951 implements MediaPlayer.OnCompletionListener {
        C00951() {
        }

        public void onCompletion(MediaPlayer mp) {
            SplashActivityNew.this.jump();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(1);
//        getWindow().setFlags(AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT, AccessibilityNodeInfoCompat.ACTION_NEXT_HTML_ELEMENT);
        setContentView(R.layout.splash_activity_new);
        mContext=this;
        if (isLowerDeviceApi()) {
            Log.e("Lower", "Lower Device Api");
            jump();
            return;
        }
        try {
            VideoView videoHolder = (VideoView) findViewById(R.id.videoView);
            videoHolder.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.splash));
            videoHolder.setOnCompletionListener(new C00951());
            videoHolder.start();
        } catch (Exception e) {
            jump();
        }
    }

    private boolean isLowerDeviceApi() {
        try {
            Integer i = Integer.valueOf(Build.VERSION.SDK);
//			LogUtils.logError("Device Api", BuildConfig.VERSION_NAME + i.intValue());
            if (i.intValue() <= APP_API) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    private void jump() {
        if (!isFinishing()) {
            if (AppUtils.checkInternet(mContext)) {
                AppUtils.postInitParams(mContext, new AppUtils.GetAccessTokenInterface() {
                    @Override
                    public void getAccessToken() {
                    }
                });
                if (Build.VERSION.SDK_INT < 23) {
                    //Do not need to check the permission
                    goToNextView();

                } else {

                    if (hasPermissions(mContext, permissions)) {
                        goToNextView();

                    } else {
                        ActivityCompat.requestPermissions(this, permissions, 100);
                    }
                }
            }else{
                AppUtils.showDialogAlertDismiss((Activity)mContext,"Network Error",getString(R.string.no_internet),R.drawable.nonetworkicon,R.drawable.roundred);

            }
        }
    }


	/*private  boolean checkAndRequestPermissions() {
		int permissionSendMessage = ContextCompat.checkSelfPermission(this,
				Manifest.permission.CAMERA);
		int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
		List<String> listPermissionsNeeded = new ArrayList<>();
		if (locationPermission != PackageManager.PERMISSION_GRANTED) {
			listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
		}
		if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
			listPermissionsNeeded.add(Manifest.permission.CAMERA);
		}
		if (!listPermissionsNeeded.isEmpty()) {
			ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
			return false;
		}
		return true;
	}*/

	/*private boolean checkAndRequestPermissions() {
		int result;
		List<String> listPermissionsNeeded = new ArrayList<>();
		for (String p:permissions) {
			result = ContextCompat.checkSelfPermission(mContext,p);
			if (result != PackageManager.PERMISSION_GRANTED) {
				listPermissionsNeeded.add(p);
			}
		}
		if (!listPermissionsNeeded.isEmpty()) {
			ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS );
			return false;
		}else{
			goToNextView();
		}
		return true;
	}*/

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

//                    Intent loginIntent = new Intent(mContext,
//                            HomeListActivity.class);
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
// Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);

                if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED && perms.get(Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED && perms.get(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED
                        && (grantResults.length > 0)) {
// All Permissions Granted
                    goToNextView();


                } else {

                    ActivityCompat.requestPermissions(this, permissions, 100);


                }
                return;
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


}
