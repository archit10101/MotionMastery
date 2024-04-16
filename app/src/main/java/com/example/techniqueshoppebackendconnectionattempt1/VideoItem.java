package com.example.techniqueshoppebackendconnectionattempt1;

public class VideoItem {
    private String name;
    private String description;
    private String thumbnailUrl;

    public VideoItem(String name, String description, String thumbnailUrl) {
        this.name = name;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
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
