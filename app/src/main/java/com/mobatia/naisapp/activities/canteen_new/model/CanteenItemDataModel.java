package com.mobatia.naisapp.activities.canteen_new.model;

import java.io.Serializable;
import java.util.ArrayList;

public class CanteenItemDataModel implements Serializable {
    String date;
    String status;
    int items_count_in_date;
    ArrayList<CanteenItemDetailModel>mCanteenItemdetailArrayList;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getItems_count_in_date() {
        return items_count_in_date;
    }

    public void setItems_count_in_date(int items_count_in_date) {
        this.items_count_in_date = items_count_in_date;
    }

    public ArrayList<CanteenItemDetailModel> getmCanteenItemdetailArrayList() {
        return mCanteenItemdetailArrayList;
    }

    public void setmCanteenItemdetailArrayList(ArrayList<CanteenItemDetailModel> mCanteenItemdetailArrayList) {
        this.mCanteenItemdetailArrayList = mCanteenItemdetailArrayList;
    }
}
