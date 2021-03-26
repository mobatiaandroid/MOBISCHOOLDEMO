package com.mobatia.naisapp.activities.parentsassociation.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by gayatri on 23/3/17.
 */
public class ParentAssociationEventItemsModel implements Serializable {
  public String getEnd_time() {
    return end_time;
  }

  public void setEnd_time(String end_time) {
    this.end_time = end_time;
  }

  public String getStart_time() {
    return start_time;
  }

  public void setStart_time(String start_time) {
    this.start_time = start_time;
  }

  String start_time;
  String from_time;
  String to_time;
  String end_time;

  public String getEventId() {
    return eventId;
  }

  public void setEventId(String eventId) {
    this.eventId = eventId;
  }

  String eventId;

  public String getFrom_time() {
    return from_time;
  }

  public void setFrom_time(String from_time) {
    this.from_time = from_time;
  }

  public String getTo_time() {
    return to_time;
  }

  public void setTo_time(String to_time) {
    this.to_time = to_time;
  }

  String status;
  String userName;




  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }



}