package com.mobatia.naisapp.activities.canteen_new.model;

import java.io.Serializable;
import java.util.ArrayList;

public class CanteenItemDetailModel implements Serializable {

    String id;
    String category_name;
    String category_image;
    int items_count_in_category;

    ArrayList<CanteenDetailModel>mCanteenDetailItemArrayList;

    public int getItems_count_in_category() {
        return items_count_in_category;
    }

    public void setItems_count_in_category(int items_count_in_category) {
        this.items_count_in_category = items_count_in_category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_image() {
        return category_image;
    }

    public void setCategory_image(String category_image) {
        this.category_image = category_image;
    }

    public ArrayList<CanteenDetailModel> getmCanteenDetailItemArrayList() {
        return mCanteenDetailItemArrayList;
    }

    public void setmCanteenDetailItemArrayList(ArrayList<CanteenDetailModel> mCanteenDetailItemArrayList) {
        this.mCanteenDetailItemArrayList = mCanteenDetailItemArrayList;
    }
}
