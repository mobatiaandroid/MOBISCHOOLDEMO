package com.mobatia.naisapp.activities.trips.model;

import java.io.Serializable;

public class FeePaymentModel implements Serializable {
    String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
