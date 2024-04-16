package com.example.techniqueshoppebackendconnectionattempt1.RetrofitData;

import com.google.gson.annotations.SerializedName;

public class CourseInfo {
    @SerializedName("courseID")
    private int courseId;

    @SerializedName("courseName")
    private String courseName;

    @SerializedName("courseDescription")
    private String courseDescription;

    @SerializedName("courseAuthorName")
    private String courseAuthorName;

    @SerializedName("courseAuthorID")
    private int courseAuthorId;

    @SerializedName("courseAuthorImgPath")
    private String courseAuthorImgPath;

    @SerializedName("courseImgPath")
    private String courseImgPath;

    // Constructor
    public CourseInfo(int courseId, String courseName, String courseDescription,
                      String courseAuthorName, int courseAuthorId,
                      String courseAuthorImgPath, String courseImgPath) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseDescription = courseDescription;
        this.courseAuthorName = courseAuthorName;
        this.courseAuthorId = courseAuthorId;
        this.courseAuthorImgPath = courseAuthorImgPath;
        this.courseImgPath = courseImgPath;
    }

    // Getters and Setters
    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public String getCourseAuthorName() {
        return courseAuthorName;
    }

    public void setCourseAuthorName(String courseAuthorName) {
        this.courseAuthorName = courseAuthorName;
    }

    public int getCourseAuthorId() {
        return courseAuthorId;
    }

    public void setCourseAuthorId(int courseAuthorId) {
        this.courseAuthorId = courseAuthorId;
    }

    public String getCourseAuthorImgPath() {
        return courseAuthorImgPath;
    }

    public void setCourseAuthorImgPath(String courseAuthorImgPath) {
        this.courseAuthorImgPath = courseAuthorImgPath;
    }

    public String getCourseImgPath() {
        return courseImgPath;
    }

    public void setCourseImgPath(String courseImgPath) {
        this.courseImgPath = courseImgPath;
    }

    @Override
    public String toString() {
        return "CourseInfo{" +
                "courseId=" + courseId +
                ", courseName='" + courseName + '\'' +
                ", courseDescription='" + courseDescription + '\'' +
                ", courseAuthorName='" + courseAuthorName + '\'' +
                ", courseAuthorId=" + courseAuthorId +
                ", courseAuthorImgPath='" + courseAuthorImgPath + '\'' +
                ", courseImgPath='" + courseImgPath + '\'' +
                '}';
    }
}
