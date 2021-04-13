package com.mobatia.naisapp.activities.photos.adapter;

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
import com.mobatia.naisapp.fragments.gallery.model.PhotosListModel;
import com.mobatia.naisapp.manager.AppUtils;
import com.mobatia.naisapp.manager.MyBounceInterpolator;
import com.mobatia.naisapp.recyclerviewmanager.OnBottomReachedListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Rijo on 17/1/17.
 */
public class PhotosRecyclerviewAdapter extends RecyclerView.Adapter<PhotosRecyclerviewAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<PhotosListModel> mPhotosModelArrayList;
    String photo_id="-1";
    OnBottomReachedListener onBottomReachedListener;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView photoImageView;
        TextView photoDescription;
        TextView photoTitle;
        RelativeLayout gridClickRelative;
        public MyViewHolder(View view) {
            super(view);

            photoImageView = (ImageView) view.findViewById(R.id.imgView);
            gridClickRelative = (RelativeLayout) view.findViewById(R.id.gridClickRelative);
            photoDescription = (TextView) view.findViewById(R.id.photoDescription);
            photoTitle = (TextView) view.findViewById(R.id.photoTitle);
        }
    }


    public PhotosRecyclerviewAdapter(Context mContext, ArrayList<PhotosListModel> mPhotosList) {
        this.mContext = mContext;
        this.mPhotosModelArrayList = mPhotosList;
    }
    public PhotosRecyclerviewAdapter(Context mContext, ArrayList<PhotosListModel> mPhotosList,String photo_id) {
        this.mContext = mContext;
        this.mPhotosModelArrayList = mPhotosList;
        this.photo_id = photo_id;
    }
    public void setOnBottomReachedListener(OnBottomReachedListener onBottomReachedListener){

        this.onBottomReachedListener = onBottomReachedListener;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.photos_recyclerview_adapter, parent, false);//photos_recyclerview_adapter

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        if (position == mPhotosModelArrayList.size() - 1){

            onBottomReachedListener.onBottomReached(position);

        }
//        holder.phototakenDate.setText(mPhotosModelArrayList.get(position).getMonth() + " " + mPhotosModelArrayList.get(position).getDay() + "," + mPhotosModelArrayList.get(position).getYear());
        holder.photoTitle.setText(mPhotosModelArrayList.get(position).getTitle());
        holder.photoDescription.setText(mPhotosModelArrayList.get(position).getDescription());
        if (!mPhotosModelArrayList.get(position).getPhotoUrl().equalsIgnoreCase("")) {
            Picasso.with(mContext).load(AppUtils.replace(mPhotosModelArrayList.get(position).getPhotoUrl())).fit()
                    .into(holder.photoImageView, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {

                        }
                    });
        }
        if (!photo_id.equalsIgnoreCase("-1")) {
            if (photo_id.equalsIgnoreCase(mPhotosModelArrayList.get(position).getPhotoId())) {
//                didTapButton(holder.photoImageView);
                holder.gridClickRelative.setBackgroundResource(R.color.red);

            }
        }


    }

    @Override
    public int getItemCount() {
        return mPhotosModelArrayList.size();
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
