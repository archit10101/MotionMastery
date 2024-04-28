package com.example.techniqueshoppebackendconnectionattempt1.RetrofitData;

import com.google.gson.annotations.SerializedName;

public class VideoTutorial {
    @SerializedName("videoID")
    private int videoID;
    @SerializedName("course_id")
    private int courseId;
    @SerializedName("video_name")
    private String videoName;
    @SerializedName("video_description")
    private String videoDescription;
    @SerializedName("video_img_path")
    private String videoImgPath;
    @SerializedName("video_path")
    private String videoPath;
    @SerializedName("video_sequence")
    private int videoSequence;

    public VideoTutorial(int videoID,int courseId, String videoName, String videoDescription, String videoImgPath, String videoPath, int videoSequence) {
        this.videoID = videoID;
        this.courseId = courseId;
        this.videoName = videoName;
        this.videoDescription = videoDescription;
        this.videoImgPath = videoImgPath;
        this.videoPath = videoPath;
        this.videoSequence = videoSequence;
    }

    public int getVideoID() {
        return videoID;
    }

    public void setVideoID(int videoID) {
        this.videoID = videoID;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getVideoDescription() {
        return videoDescription;
    }

    public void setVideoDescription(String videoDescription) {
        this.videoDescription = videoDescription;
    }

    public String getVideoImgPath() {
        return videoImgPath;
    }

    public void setVideoImgPath(String videoImgPath) {
        this.videoImgPath = videoImgPath;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public int getVideoSequence() {
        return videoSequence;
    }

    public void setVideoSequence(int videoSequence) {
        this.videoSequence = videoSequence;
    }
}
