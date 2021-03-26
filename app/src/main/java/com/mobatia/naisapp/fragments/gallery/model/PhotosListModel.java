package com.mobatia.naisapp.fragments.gallery.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Rijo on 25/1/17.
 */
public class PhotosListModel implements Serializable {
  String photoId,photoUrl;
  String title;
    String itemImageurl;

    public ArrayList<PhotosListModel> getmPhotosUrlArrayList() {
        return mPhotosUrlArrayList;
    }

    public void setmPhotosUrlArrayList(ArrayList<PhotosListModel> mPhotosUrlArrayList) {
        this.mPhotosUrlArrayList = mPhotosUrlArrayList;
    }

    ArrayList<PhotosListModel>mPhotosUrlArrayList;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    String description;

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getItemImageurl() {
        return itemImageurl;
    }

    public void setItemImageurl(String itemImageurl) {
        this.itemImageurl = itemImageurl;
    }
}
