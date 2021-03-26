package com.mobatia.naisapp.activities.term_calendar.model;

import java.io.Serializable;

/**
 * Created by RIJO K JOSE on 25/1/17.
 */
public class TermsCalendarModel implements Serializable{
    String mId;

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }



    public String getmFileName() {
        return mFileName;
    }

    public void setmFileName(String mFileName) {
        this.mFileName = mFileName;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }



    String mFileName,mTitle;


}
