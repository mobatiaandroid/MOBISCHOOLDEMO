package com.mobatia.naisapp.activities.videos.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.activities.videos.model.VideosListModel;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.MyBounceInterpolator;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Rijo on 17/1/17.
 */
public class VideosRecyclerviewAdapter extends RecyclerView.Adapter<VideosRecyclerviewAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<VideosListModel> mVideosModelArrayList;
    String video_id="-1";

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView photoImageView;
        TextView videoDescription;
        TextView videoTitle;
        RelativeLayout gridClickRelative;
        public MyViewHolder(View view) {
            super(view);

            photoImageView = (ImageView) view.findViewById(R.id.imgView);
            gridClickRelative = (RelativeLayout) view.findViewById(R.id.gridClickRelative);
            videoDescription = (TextView) view.findViewById(R.id.videoDescription);
            videoTitle = (TextView) view.findViewById(R.id.videoTitle);

        }
    }


    public VideosRecyclerviewAdapter(Context mContext, ArrayList<VideosListModel> mPhotosList) {
        this.mContext = mContext;
        this.mVideosModelArrayList = mPhotosList;
    }
    public VideosRecyclerviewAdapter(Context mContext, ArrayList<VideosListModel> mPhotosList,String video_id) {
        this.mContext = mContext;
        this.mVideosModelArrayList = mPhotosList;
        this.video_id = video_id;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.videos_recyclerview_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.videoTitle.setText(mVideosModelArrayList.get(position).getTitle());
        holder.videoDescription.setText(mVideosModelArrayList.get(position).getDescription());
        if (!mVideosModelArrayList.get(position).getImage_url().equalsIgnoreCase("")) {
            Picasso.with(mContext).load(AppUtils.replace(mVideosModelArrayList.get(position).getImage_url())).fit()
                    .into(holder.photoImageView, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                        }
                    });
        }

        if (!video_id.equalsIgnoreCase("-1")) {
            if (video_id.equalsIgnoreCase(mVideosModelArrayList.get(position).getVideoId())) {
//                didTapButton(holder.photoImageView);
                holder.gridClickRelative.setBackgroundResource(R.color.red);
            }
        }
    }



    @Override
    public int getItemCount() {
        return mVideosModelArrayList.size();
    }
    public void didTapButton(View view) {
//        ImageView button = (ImageView)view.findViewById(R.id.imgView);
//        final Animation myAnim = AnimationUtils.loadAnimation(mContext, R.anim.bounce);
//        button.startAnimation(myAnim);
        final Animation myAnim = AnimationUtils.loadAnimation(mContext, R.anim.bounce);

        // Use bounce interpolator with amplitude 0.2 and frequency 20
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.6, 20);
        myAnim.setInterpolator(interpolator);

        view.startAnimation(myAnim);
    }
    @Override
    public void onViewDetachedFromWindow(MyViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.photoImageView.clearAnimation();
    }
}
