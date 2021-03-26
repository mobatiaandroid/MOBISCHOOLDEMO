package com.mobatia.naisapp.constants;

/**
 * Created by RIJO K JOSE  on 2/12/16.
 */
public interface StatusConstants {

    String RESPONSE_SUCCESS = "200";
    String RESPONSE_ERROR = "500";
    String RESPONSE_ACCESSTOKEN_MISSING = "400";
    String RESPONSE_ACCESSTOKEN_EXPIRED = "401";
    String RESPONSE_INVALID_TOKEN = "402";

    String STATUS_MISSING_PARAMETER = "301";
    String STATUS_EMAIL_EXISTS = "304";
    String STATUS_SUCCESS = "303";
    String STATUS_DUPLICATE_ENTRY = "308";
    String STATUS_NOT_APPROVED = "309";
    String STATUS_BOOKED_BY_USER = "310";
    String STATUS_CANCEL = "311";
    String STATUS_ALREADY_REQUEST = "313";
    String STATUS_SLOT_NOT_FOUND = "315";
    String STATUS_BOOKING_DATE_EXPIRED = "316";

    String STATUS_STUDENT_EXISTS = "209";
    String STATUS_INCORRECT_PSWD = "305";
    String STATUS_INVALID_EMAIL = "306";
}
