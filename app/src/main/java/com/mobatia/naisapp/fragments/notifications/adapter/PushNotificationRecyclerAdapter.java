package com.mobatia.naisapp.fragments.notifications.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.fragments.notifications.model.PushNotificationModel;
import com.mobatia.naisapp.fragments.secondary.model.SecondaryModel;
import com.mobatia.naisapp.recyclerviewmanager.OnBottomReachedListener;

import java.util.ArrayList;

public class PushNotificationRecyclerAdapter extends RecyclerView.Adapter<PushNotificationRecyclerAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<PushNotificationModel> pushNotificationList;
    String dept;
    OnBottomReachedListener onBottomReachedListener;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageIcon;
        TextView pdfTitle;
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


        /**
         * The progress img.
         */
        ProgressBar progressImg;
        public MyViewHolder(View view) {
            super(view);
            Img = (ImageView) view.findViewById(R.id.Img);
            status = (TextView) view.findViewById(R.id.status);
            statusLayout = (RelativeLayout) view.findViewById(R.id.statusLayout);
            title = (TextView) view.findViewById(R.id.title);


        }
    }


    public PushNotificationRecyclerAdapter(Context mContext, ArrayList<PushNotificationModel> pushNotificationList) {
        this.mContext = mContext;
        this.pushNotificationList = pushNotificationList;

    }
    public void setOnBottomReachedListener(OnBottomReachedListener onBottomReachedListener){

        this.onBottomReachedListener = onBottomReachedListener;
    }
    @Override
    public PushNotificationRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_adapter_pushlist_item, parent, false);

        return new PushNotificationRecyclerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PushNotificationRecyclerAdapter.MyViewHolder holder, int position) {
        if (position == pushNotificationList.size() - 1){

            onBottomReachedListener.onBottomReached(position);

        }
        if (pushNotificationList.get(position).getPushType().equalsIgnoreCase("video")) {

            holder.Img.setImageResource(R.drawable.alerticon_video);

        } else if (pushNotificationList.get(position).getPushType().equalsIgnoreCase("text")) {
            holder.Img.setImageResource(R.drawable.alerticon_text);

        } else if (pushNotificationList.get(position).getPushType().equalsIgnoreCase("image")) {
            holder.Img.setImageResource(R.drawable.alerticon_image);
        } else if (pushNotificationList.get(position).getPushType().equalsIgnoreCase("audio")) {
            holder.Img.setImageResource(R.drawable.alerticon_audio);

        }
        if (pushNotificationList.get(position).getStatus().equalsIgnoreCase("0")) {
            holder.statusLayout.setVisibility(View.VISIBLE);
            holder.status.setBackgroundResource(R.drawable.rectangle_red);
            holder.status.setText("New");
        } else if (pushNotificationList.get(position).getStatus().equalsIgnoreCase("1") || pushNotificationList.get(position).getStatus().equalsIgnoreCase("")) {
            holder.statusLayout.setVisibility(View.GONE);

        } else if (pushNotificationList.get(position).getStatus().equalsIgnoreCase("2")) {
            holder.statusLayout.setVisibility(View.VISIBLE);
            holder.status.setBackgroundResource(R.drawable.rectangle_orange);
            holder.status.setText("Updated");
        }

        holder.title.setText(pushNotificationList.get(position).getHeadTitle());    }


    @Override
    public int getItemCount() {
        return pushNotificationList.size();
    }

}
