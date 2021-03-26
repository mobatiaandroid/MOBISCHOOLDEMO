package com.mobatia.naisapp.fragments.report.model;

import java.io.Serializable;

/**
 * Created by krishnaraj on 20/07/18.
 */

public class DataModel implements Serializable {
    String id;
    String academic_year;
    String class_id;
    String reporting_cycle;
    String file;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAcademic_year() {
        return academic_year;
    }

    public void setAcademic_year(String academic_year) {
        this.academic_year = academic_year;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getReporting_cycle() {
        return reporting_cycle;
    }

    public void setReporting_cycle(String reporting_cycle) {
        this.reporting_cycle = reporting_cycle;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getStud_id() {
        return stud_id;
    }

    public void setStud_id(String stud_id) {
        this.stud_id = stud_id;
    }

    public String getCreated_on() {
        return created_on;
    }

    public void setCreated_on(String created_on) {
        this.created_on = created_on;
    }

    public String getUpdated_on() {
        return updated_on;
    }

    public void setUpdated_on(String updated_on) {
        this.updated_on = updated_on;
    }

    String stud_id;
    String created_on;
    String updated_on;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    String status;
}
