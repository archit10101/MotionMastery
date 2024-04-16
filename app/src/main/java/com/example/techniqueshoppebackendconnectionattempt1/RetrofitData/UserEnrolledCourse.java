package com.example.techniqueshoppebackendconnectionattempt1.RetrofitData;

import com.google.gson.annotations.SerializedName;

public class UserEnrolledCourse {
    @SerializedName("id")
    private int id;

    @SerializedName("userID")
    private String userID;

    @SerializedName("courseID")
    private String courseID;

    @SerializedName("date_time")
    private String dateTime;

    public UserEnrolledCourse(int id, String userID, String courseID, String dateTime) {
        this.id = id;
        this.userID = userID;
        this.courseID = courseID;
        this.dateTime = dateTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "UserEnrolledCourse{" +
                "id=" + id +
                ", userID='" + userID + '\'' +
                ", courseID='" + courseID + '\'' +
                ", dateTime='" + dateTime + '\'' +
                '}';
    }
}
