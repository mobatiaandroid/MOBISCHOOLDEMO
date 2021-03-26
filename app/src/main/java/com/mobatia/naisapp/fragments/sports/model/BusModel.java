package com.mobatia.naisapp.fragments.sports.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by gayatri on 31/3/17.
 */
public class BusModel implements Serializable{
    String route_name;
    String time;
    String bus_no;
    String bus_name;
    String contact_person;
    String contact_no;
    String latitude;

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    String longitude;
/* "bus_no": "2365",
          "bus_name": "pombi",
          "contact_person": "sanal",
          "contact_no": "65326598",
          "bus_routes": [
            {
              "route_name": "Daimond views 3",
              "time": "11:00:00",
              "latitude": "25.065961 ",
              "longitude": "55.213033"*/
    public ArrayList<BusModel> getmBusRoute() {
        return mBusRoute;
    }

    public void setmBusRoute(ArrayList<BusModel> mBusRoute) {
        this.mBusRoute = mBusRoute;
    }

    ArrayList<BusModel>mBusRoute;
    public String getRoute_name() {
        return route_name;
    }

    public void setRoute_name(String route_name) {
        this.route_name = route_name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBus_no() {
        return bus_no;
    }

    public void setBus_no(String bus_no) {
        this.bus_no = bus_no;
    }

    public String getBus_name() {
        return bus_name;
    }

    public void setBus_name(String bus_name) {
        this.bus_name = bus_name;
    }

    public String getContact_person() {
        return contact_person;
    }

    public void setContact_person(String contact_person) {
        this.contact_person = contact_person;
    }

    public String getContact_no() {
        return contact_no;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }
}
