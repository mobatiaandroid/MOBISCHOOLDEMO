package com.mobatia.naisapp.fragments.primary.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by RIJO K JOSE on 25/1/17.
 */
public class PrimaryModel implements Serializable{


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

    String mId;
    ArrayList<PrimaryModel>mPrimaryModel;
    String mName;
    String mFile;
    String mTitle;
    String mDescription;


}
