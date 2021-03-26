package com.mobatia.naisapp.activities.canteen_new.model;

import java.io.Serializable;
import java.util.ArrayList;

public class MyOrderModel implements Serializable {

    String id;
    String delivery_date;
    String total_amount;
    String status;
    String cancellation_time_exceed;
    String pickup_location;
    ArrayList<MyOrderDetailModel>mMyOrderDetailArrayList;

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

    public String getCancellation_time_exceed() {
        return cancellation_time_exceed;
    }

    public void setCancellation_time_exceed(String cancellation_time_exceed) {
        this.cancellation_time_exceed = cancellation_time_exceed;
    }

    public ArrayList<MyOrderDetailModel> getmMyOrderDetailArrayList() {
        return mMyOrderDetailArrayList;
    }

    public void setmMyOrderDetailArrayList(ArrayList<MyOrderDetailModel> mMyOrderDetailArrayList) {
        this.mMyOrderDetailArrayList = mMyOrderDetailArrayList;
    }

    public String getPickup_location() {
        return pickup_location;
    }

    public void setPickup_location(String pickup_location) {
        this.pickup_location = pickup_location;
    }
}
