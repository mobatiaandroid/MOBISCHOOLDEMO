package com.mobatia.naisapp.activities.canteen_new.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ConfirmedDataModel implements Serializable {

    String id;
    String delivery_date;
    String total_amount;
    String status;
    int cancellation_time_exceed;
    ArrayList<ConfirmedDetailModel>mConfirmedDetailArrayList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(String delivery_date) {
        this.delivery_date = delivery_date;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<ConfirmedDetailModel> getmConfirmedDetailArrayList() {
        return mConfirmedDetailArrayList;
    }

    public void setmConfirmedDetailArrayList(ArrayList<ConfirmedDetailModel> mConfirmedDetailArrayList) {
        this.mConfirmedDetailArrayList = mConfirmedDetailArrayList;
    }

    public int getCancellation_time_exceed() {
        return cancellation_time_exceed;
    }

    public void setCancellation_time_exceed(int cancellation_time_exceed) {
        this.cancellation_time_exceed = cancellation_time_exceed;
    }
}
