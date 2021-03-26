/**
 * 
 */
package com.mobatia.naisapp.activities.notifications;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.home.HomeListAppCompatActivity;
import com.mobatia.naisapp.constants.IntentPassValueConstants;
import com.mobatia.naisapp.constants.JSONConstants;
import com.mobatia.naisapp.constants.StatusConstants;
import com.mobatia.naisapp.constants.URLConstants;
import com.mobatia.naisapp.fragments.notifications.model.PushNotificationModel;
import com.mobatia.naisapp.manager.HeaderManager;

import java.util.ArrayList;

/**
 * @author rahul
 * 
 */

public class TextalertActivity extends Activity implements
		 OnClickListener,IntentPassValueConstants, JSONConstants, URLConstants, StatusConstants {

	TextView txtmsg;
	TextView mDateTv;
	ImageView img;
	ImageView home;
	Bundle extras;
	ArrayList<PushNotificationModel> imglist;
	int position;
//	PhotosManager photosManager;
	Context context = this;
	Activity mActivity;
	RelativeLayout header;
//	HeaderManager headermanager;
	ImageView back;
	RelativeLayout relativeHeader;
	HeaderManager headermanager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.text_push);
		mActivity = this;
		extras = getIntent().getExtras();
		if (extras != null) {
			position = extras.getInt(POSITION);

			imglist = (ArrayList<PushNotificationModel>) extras
					.getSerializable(PASS_ARRAY_LIST);

		}

		initialiseUI();

	}

	private void initialiseUI() {



		img = (ImageView) findViewById(R.id.image);
		txtmsg = (TextView) findViewById(R.id.txt);
		mDateTv = (TextView) findViewById(R.id.mDateTv);
		relativeHeader = (RelativeLayout) findViewById(R.id.relativeHeader);
		headermanager = new HeaderManager(mActivity, "Notification");
		headermanager.getHeader(relativeHeader, 0);
		back = headermanager.getLeftButton();
		headermanager.setButtonLeftSelector(R.drawable.back,
				R.drawable.back);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		home = headermanager.getLogoButton();
		home.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent in = new Intent(mActivity, HomeListAppCompatActivity.class);
				in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(in);
			}
		});
//		photosManager = new PhotosManager(context, ALERT_COVER_PATH,
//				(int) context.getResources().getDimension(
//						R.dimen.coverimage_width), (int) context.getResources()
//						.getDimension(R.dimen.coverimage_height), 0);
		setDetails();

	}

	private void setDetails() {
		txtmsg.setText(imglist.get(position).getPushTitle());
		mDateTv.setText(imglist.get(position).getDay() + "-" + imglist.get(position).getMonthString() + "-" + imglist.get(position).getYear()+" "+imglist.get(position).getPushTime());
//		if (!(imglist.get(position).getPushDetail().toString()
//				.equalsIgnoreCase(""))) {
//			txtmsg.setText(imglist.get(position).getPushDetail());
//		} else {
//			txtmsg.setText(imglist.get(position).getPushTitle());
//		}
	}

	@Override
	public void onClick(View v) {

	}

}
