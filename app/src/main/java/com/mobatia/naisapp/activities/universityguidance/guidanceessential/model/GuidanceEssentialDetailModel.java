package com.mobatia.naisapp.activities.universityguidance.guidanceessential.model;

import java.io.Serializable;

public class GuidanceEssentialDetailModel  implements Serializable {
    String description;
    String file_url;
    String file_type;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public String getFile_type() {
        return file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }
}
