package com.mobatia.naisapp.fragments.report.model;

import java.io.Serializable;
import java.util.ArrayList;

public class StudentInfoModel implements Serializable {
    public String getAcyear() {
        return acyear;
    }

    public void setAcyear(String acyear) {
        this.acyear = acyear;
    }

    String acyear;
    ArrayList<DataModel> mDataModel;

    public ArrayList<DataModel> getmDataModel() {
        return mDataModel;
    }

    public void setmDataModel(ArrayList<DataModel> mDataModel) {
        this.mDataModel = mDataModel;
    }
}
