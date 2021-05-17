package com.mobatia.naisapp.activities.universityguidance.guidanceessential.model;

import android.graphics.Bitmap;

import java.io.Serializable;

public class GuidanceEssentialDetailModel  implements Serializable {
    String description;
    String file_url;
    String file_type;
    String image_url;
    Bitmap bitmap;
    String pdf_thumbnail_url;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

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

    public String getPdf_thumbnail_url() {
        return pdf_thumbnail_url;
    }

    public void setPdf_thumbnail_url(String pdf_thumbnail_url) {
        this.pdf_thumbnail_url = pdf_thumbnail_url;
    }
}
