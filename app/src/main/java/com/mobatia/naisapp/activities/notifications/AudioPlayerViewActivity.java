package com.mobatia.naisapp.activities.notifications;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.home.HomeListActivity;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.constants.IntentPassValueConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.fragments.notifications.model.PushNotificationModel;
import com.mobatia.naisapp.manager.HeaderManager;

import java.util.ArrayList;

import tcking.github.com.giraffeplayer.GiraffePlayer;
import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by Rijo on 25/1/17.
 */
public class AudioPlayerViewActivity extends Activity implements IntentPassValueConstants {
    Bundle extras;
    String url;
    Context mContext;
    GiraffePlayer player;
    ArrayList<PushNotificationModel> alertlist;
    Activity activity;
    int position;
    RelativeLayout relativeHeader;
    HeaderManager headermanager;
    ImageView backImg;
    ImageView home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_player_push_activity);
        mContext = this;
        activity=this;

        extras = getIntent().getExtras();
        if (extras != null) {
            position = extras.getInt(POSITION);

            alertlist = (ArrayList<PushNotificationModel>) extras
                    .getSerializable(PASS_ARRAY_LIST);
            url=alertlist.get(position).getPushUrl();
        }

        relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
        headermanager = new HeaderManager(activity, "Notification");
        headermanager.getHeader(relativeHeader, 0);
        backImg = headermanager.getLeftButton();
        headermanager.setButtonLeftSelector(R.drawable.back,
                R.drawable.back);
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        player = new GiraffePlayer(this);
        player.play(url);
        player.onComplete(new Runnable() {
            @Override
            public void run() {
//callback when video is finish
                Toast.makeText(getApplicationContext(), "Play completed", Toast.LENGTH_SHORT).show();
            }
        }).onInfo(new GiraffePlayer.OnInfoListener() {
            @Override
            public void onInfo(int what, int extra) {
                switch (what) {
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
//do something when buffering start
                        break;
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
//do something when buffering end
                        break;
                    case IMediaPlayer.MEDIA_INFO_NETWORK_BANDWIDTH:
//download speed
//                        ((TextView) findViewById(R.id.tv_speed)).setText(Formatter.formatFileSize(getApplicationContext(), extra)+"/s");
                        break;
                    case IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
//do something when video rendering
//                        findViewById(R.id.tv_speed).setVisibility(View.GONE);
                        break;
                }
            }
        }).onError(new GiraffePlayer.OnErrorListener() {
            @Override
            public void onError(int what, int extra) {
//UtilityMethods.showAlertFinish(activity,"Can't play this video","","OK",false);
                Toast.makeText(getApplicationContext(), "Can't play this audio",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player != null) {
            player.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.onDestroy();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (player != null) {
            player.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public void onBackPressed() {
        if (player != null && player.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }
// Find your VideoView in your video_main.xml layout
       /* videoview = (VideoView) findViewById(R.id.VideoView);
        // Execute StreamVideo AsyncTask


        final CustomProgressBar pDialog = new CustomProgressBar(activity,
                R.drawable.spinner);

        if (!activity.isFinishing()) {
            if (pDialog != null) {
                pDialog.show();
            }
        }

        try {
            // Start the MediaController
            MediaController mediacontroller = new MediaController(
                    Watch_WebView_Activity.this);
            mediacontroller.setAnchorView(videoview);
            // Get the URL from String VideoURL
            Uri video = Uri.parse(url);
            videoview.setMediaController(mediacontroller);
            videoview.setVideoURI(video);

        } catch (Exception e) {
            Log.e("Alert", e.getMessage());
            e.printStackTrace();
        }

        videoview.requestFocus();
        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
//                pDialog.dismiss();
                try {
                    if (!activity.isFinishing()) {
                        if (pDialog != null) {
                            if (pDialog.isShowing()) {
                                pDialog.dismiss();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ((Activity) activity).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            CustomDialog customDialog = new CustomDialog
                                    (activity, activity.getResources().getString(R.string.error_heading), activity.getResources().getString(R.string.common_error));
                            customDialog.show();
                        }
                    });
                }
                videoview.start();
            }
        });
        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer player) {
                Log.i("VideoView", "onCompletion()");
                finish();
            }
        });*/




    public String replace(String str) {
        return str.replaceAll(" ", "%20");
    }
  /*  @Override
    public void onBackPressed() {
// TODO Auto-generated method stub
        super.onBackPressed();
//        webView.stopLoading();
//        webView.loadData("", "text/html", "utf-8");
        finish();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
//        // Checks the orientation of the screen
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            DisplayMetrics metrics = new DisplayMetrics(); getWindowManager().getDefaultDisplay().getMetrics(metrics);
//            android.widget.LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) videoview.getLayoutParams();
//            params.width =  metrics.widthPixels;
//            params.height = metrics.heightPixels;
//            params.leftMargin = 0;
//            videoview.setLayoutParams(params);
//            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
//
//        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
//            DisplayMetrics metrics = new DisplayMetrics(); getWindowManager().getDefaultDisplay().getMetrics(metrics);
//            android.widget.LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) videoview.getLayoutParams();
//            params.width =  (int) (300*metrics.density);
//            params.height = (int) (250*metrics.density);
//            params.leftMargin = 30;
//            videoview.setLayoutParams(params);
//            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
//        }
    }*/

}