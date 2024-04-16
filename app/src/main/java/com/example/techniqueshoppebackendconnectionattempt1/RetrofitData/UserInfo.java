package com.example.techniqueshoppebackendconnectionattempt1.RetrofitData;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserInfo {

    private List<MyData> dataList;

    public List<MyData> getDataList() {
        return dataList;
    }

    public static class MyData {

        @SerializedName("userID")
        private int userID;

        @SerializedName("userName")
        private String userName;

        @SerializedName("userPassword")
        private String userPassword;

        @SerializedName("userEmail")
        private String userEmail;

        @SerializedName("firstName")
        private String firstName;

        @SerializedName("lastName")
        private String lastName;

        @SerializedName("userImagePath")
        private String userImagePath;

        public MyData(int userID, String userName, String userPassword, String userEmail, String firstName, String lastName, String userImagePath) {
            this.userID = userID;
            this.userName = userName;
            this.userPassword = userPassword;
            this.userEmail = userEmail;
            this.firstName = firstName;
            this.lastName = lastName;
            this.userImagePath = userImagePath;
        }

        public void setUserImagePath(String userImagePath) {
            this.userImagePath = userImagePath;
        }

        public String getUserImagePath() {
            return userImagePath;
        }

        public int getUserID() {
            return userID;
        }

        public void setUserID(int userID) {
            this.userID = userID;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserPassword() {
            return userPassword;
        }

        public void setUserPassword(String userPassword) {
            this.userPassword = userPassword;
        }

        public String getUserEmail() {
            return userEmail;
        }

        public void setUserEmail(String userEmail) {
            this.userEmail = userEmail;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        @Override
        public String toString() {
            return "MyData{" +
                    "userID=" + userID +
                    ", userName='" + userName + '\'' +
                    ", userPassword='" + userPassword + '\'' +
                    ", userEmail='" + userEmail + '\'' +
                    ", firstName='" + firstName + '\'' +
                    ", lastName='" + lastName + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "dataList=" + dataList +
                '}';
    }
}
