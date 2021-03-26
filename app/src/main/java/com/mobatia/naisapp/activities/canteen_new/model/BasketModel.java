package com.mobatia.naisapp.activities.canteen_new.model;

import java.io.Serializable;
import java.util.ArrayList;

public class BasketModel implements Serializable {
    String delivery_date;
    int total_amount;
    ArrayList<BasketDetailModel>mBasketDetailArrayList;

    public String getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(String delivery_date) {
        this.delivery_date = delivery_date;
    }

    public int getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(int total_amount) {
        this.total_amount = total_amount;
    }

    public ArrayList<BasketDetailModel> getmBasketDetailArrayList() {
        return mBasketDetailArrayList;
    }

    public void setmBasketDetailArrayList(ArrayList<BasketDetailModel> mBasketDetailArrayList) {
        this.mBasketDetailArrayList = mBasketDetailArrayList;
    }
}
