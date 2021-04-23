package com.mobatia.naisapp.fragments.secondary.model;

import com.mobatia.naisapp.fragments.primary.model.PrimaryModel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by RIJO K JOSE on 25/1/17.
 */
public class SecondaryModel implements Serializable{

    public ArrayList<PrimaryModel> getmPrimaryModel() {
        return mPrimaryModel;
    }

    public void setmPrimaryModel(ArrayList<PrimaryModel> mPrimaryModel) {
        this.mPrimaryModel = mPrimaryModel;
    }


    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmFile() {
        return mFile;
    }

    public void setmFile(String mFile) {
        this.mFile = mFile;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmDescription() {
        return mDescription;
    }

    public void setmDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    String mName;String mFile,mTitle,mDescription;
    String mId;
    ArrayList<PrimaryModel>mPrimaryModel;
    String title;
    String description;
    String createdon;
    String status;
    String id;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedon() {
        return createdon;
    }

    public void setCreatedon(String createdon) {
        this.createdon = createdon;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
