package com.example.techniqueshoppebackendconnectionattempt1.RetrofitData;

import com.google.gson.annotations.SerializedName;

public class PresignedUrlResponse {

    @SerializedName("uuid")
    private String uuid;

    @SerializedName("url")
    private String url;

    public String getUuid() {
        return uuid;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUrl() {
        return url;
    }
}
