package com.example.techniqueshoppebackendconnectionattempt1;

import java.util.List;

public class UserInfo {
    private List<MyData> dataList;

    public List<MyData> getDataList() {
        return dataList;
    }

    public static class MyData {
        private int id;
        private String userName, userPassword, userEmail, firstName, lastName;

        public MyData(int id, String userName, String userPassword, String userEmail, String firstName, String lastName) {
            this.id = id;
            this.userName = userName;
            this.userPassword = userPassword;
            this.userEmail = userEmail;
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

    }
}
