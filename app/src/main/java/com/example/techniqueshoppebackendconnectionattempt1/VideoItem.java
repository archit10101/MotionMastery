package com.example.techniqueshoppebackendconnectionattempt1;

public class VideoItem {
    private String name;
    private String description;
    private String thumbnailUrl;

    private int vidID;

    public VideoItem(String name, String description, String thumbnailUrl, int videoID) {
        this.name = name;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.vidID = videoID;
    }

    public int getVidID() {
        return vidID;
    }


    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
}
