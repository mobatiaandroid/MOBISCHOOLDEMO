package com.mobatia.naisapp.activities.canteen_new.model;

import java.io.Serializable;
import java.util.ArrayList;

public class CartItemDateModel implements Serializable {

    String delivery_date;
    float total_amount;

    public float getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(float total_amount) {
        this.total_amount = total_amount;
    }

    ArrayList<CartItemDetailModel>mCartItemDetailArrayList;

    public String getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(String delivery_date) {
        this.delivery_date = delivery_date;
    }

    public ArrayList<CartItemDetailModel> getmCartItemDetailArrayList() {
        return mCartItemDetailArrayList;
    }

    public void setmCartItemDetailArrayList(ArrayList<CartItemDetailModel> mCartItemDetailArrayList) {
        this.mCartItemDetailArrayList = mCartItemDetailArrayList;
    }
}
