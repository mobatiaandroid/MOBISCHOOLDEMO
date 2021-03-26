package com.mobatia.naisapp.fragments.gallery.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mobatia.naisapp.R;
import com.mobatia.naisapp.fragments.gallery.model.PhotosListModel;
import com.mobatia.naisapp.manager.AppUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Rijo on 17/1/17.
 */
public class GalleryPhotosRecyclerviewAdapter extends RecyclerView.Adapter<GalleryPhotosRecyclerviewAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<PhotosListModel> mPhotosModelArrayList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView photoImageView;
        public MyViewHolder(View view) {
            super(view);

            photoImageView = (ImageView) view.findViewById(R.id.imgView);

        }
    }


    public GalleryPhotosRecyclerviewAdapter(Context mContext, ArrayList<PhotosListModel> mPhotosList) {
        this.mContext = mContext;
        this.mPhotosModelArrayList = mPhotosList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.gallery_thumbnail_recyclerview_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
//        holder.phototakenDate.setText(mPhotosModelArrayList.get(position).getMonth() + " " + mPhotosModelArrayList.get(position).getDay() + "," + mPhotosModelArrayList.get(position).getYear());
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


    }



    @Override
    public int getItemCount() {
        return mPhotosModelArrayList.size();
    }
}
