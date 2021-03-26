package com.mobatia.naisapp.activities.videos.model;

import java.io.Serializable;

/**
 * Created by Rijo on 25/1/17.
 */
public class VideosListModel implements Serializable {
    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String utl) {
        this.url = utl;
    }

    public String getVideo_type() {
        return video_type;
    }

    public void setVideo_type(String video_type) {
        this.video_type = video_type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getVideo_length() {
        return video_length;
    }

    public void setVideo_length(String video_length) {
        this.video_length = video_length;
    }

    String videoId,title,url,video_type,description,image_url,video_length;

//    "id": "2",
//            "title": "Christmas Fayre 2015",
//            "url": "http://img.nordangliaeducation.com/resources/asia/_filecache/58d/7ce/39352-mp4-christmas-medley.mp4",
//            "video_type": "Youtube",
//            "description": "Christmas Fayre 2015",
//            "image_url": "http://mobicare2.mobatia.com/nais/media/images/39351-maintainratio-w800-h0-of-1-000000-christmas-medley.png",
//            "video_length": ""
}
