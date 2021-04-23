package com.mobatia.naisapp.activities.universityguidance.information.model;

import com.mobatia.naisapp.fragments.primary.model.PrimaryModel;

import java.io.Serializable;
import java.util.ArrayList;

public class InformationModel implements Serializable {
    String name;
    String file;
    String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
