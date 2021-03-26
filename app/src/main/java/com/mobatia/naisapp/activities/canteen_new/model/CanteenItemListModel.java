package com.mobatia.naisapp.activities.canteen_new.model;

import java.io.Serializable;
import java.util.ArrayList;

public class CanteenItemListModel implements Serializable {

    String date;
    String items_count_in_category;
    ArrayList<CanteenItemListDataModel>mCanteenItemDataArrayList;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getItems_count_in_category() {
        return items_count_in_category;
    }

    public void setItems_count_in_category(String items_count_in_category) {
        this.items_count_in_category = items_count_in_category;
    }

    public ArrayList<CanteenItemListDataModel> getmCanteenItemDataArrayList() {
        return mCanteenItemDataArrayList;
    }

    public void setmCanteenItemDataArrayList(ArrayList<CanteenItemListDataModel> mCanteenItemDataArrayList) {
        this.mCanteenItemDataArrayList = mCanteenItemDataArrayList;
    }
}
