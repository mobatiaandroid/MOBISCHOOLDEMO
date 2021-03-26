/**
 *
 */
package com.mobatia.naisapp.fragments.notifications.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.mobatia.naisapp.R;
import com.mobatia.naisapp.fragments.notifications.model.PushNotificationModel;

import java.util.ArrayList;

// TODO: Auto-generated Javadoc

/**
 * The Class AlertAdapter.
 *
 * @author rahul
 */
public class PushNotificationListAdapter extends BaseAdapter {

    /**
     * The context.
     */
    private Context context;

    /**
     * The inflater.
     */
    private LayoutInflater inflater;

    /**
     * The view.
     */
    private View view;

    private RelativeLayout statusLayout;
    private TextView status;

    /**
     * The profile name.
     */
    private TextView profileName;

    /**
     * The title.
     */
    private TextView title;


    /**
     * The news date.
     */
    private TextView newsDate;

    /**
     * The Img.
     */
    private ImageView Img;

    /**
     * The alert list.
     */
    private ArrayList<PushNotificationModel> pushNotificationList;

    /**
     * The progress img.
     */
    ProgressBar progressImg;


    public PushNotificationListAdapter(Context context, ArrayList<PushNotificationModel> mPushNotificationList) {
        this.context = context;
        this.pushNotificationList = mPushNotificationList;

    }

    /*
     * (non-Javadoc)
     *
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public int getCount() {

        return pushNotificationList.size();
    }

    /*
     * (non-Javadoc)
     *
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public Object getItem(int position) {

        return pushNotificationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.custom_adapter_pushlist_item, null);
        Img = (ImageView) view.findViewById(R.id.Img);
        status = (TextView) view.findViewById(R.id.status);
        statusLayout = (RelativeLayout) view.findViewById(R.id.statusLayout);

        title = (TextView) view.findViewById(R.id.title);


        if (pushNotificationList.get(position).getPushType().equalsIgnoreCase("video")) {

            Img.setImageResource(R.drawable.alerticon_video);

        } else if (pushNotificationList.get(position).getPushType().equalsIgnoreCase("text")) {
            Img.setImageResource(R.drawable.alerticon_text);

        } else if (pushNotificationList.get(position).getPushType().equalsIgnoreCase("image")) {
            Img.setImageResource(R.drawable.alerticon_image);
        } else if (pushNotificationList.get(position).getPushType().equalsIgnoreCase("audio")) {
            Img.setImageResource(R.drawable.alerticon_audio);

        }
        if (pushNotificationList.get(position).getStatus().equalsIgnoreCase("0")) {
            statusLayout.setVisibility(View.VISIBLE);
            status.setBackgroundResource(R.drawable.rectangle_red);
            status.setText("New");
        } else if (pushNotificationList.get(position).getStatus().equalsIgnoreCase("1") || pushNotificationList.get(position).getStatus().equalsIgnoreCase("")) {
            statusLayout.setVisibility(View.GONE);

        } else if (pushNotificationList.get(position).getStatus().equalsIgnoreCase("2")) {
            statusLayout.setVisibility(View.VISIBLE);
            status.setBackgroundResource(R.drawable.rectangle_orange);
            status.setText("Updated");
        }

        title.setText(pushNotificationList.get(position).getHeadTitle());

        return view;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}
