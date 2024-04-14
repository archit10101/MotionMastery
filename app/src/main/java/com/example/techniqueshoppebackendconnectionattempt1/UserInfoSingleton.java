package com.example.techniqueshoppebackendconnectionattempt1;

import java.util.List;

public class UserInfoSingleton {

    private static UserInfoSingleton instance;
    private List<UserInfo.MyData> dataList;

    private UserInfoSingleton(List<UserInfo.MyData> dataList) {
        this.dataList = (dataList);

    }

    public static synchronized UserInfoSingleton getInstance(List<UserInfo.MyData> dataList) {
        instance = new UserInfoSingleton(dataList);
        return instance;
    }
    public static synchronized UserInfoSingleton getInstance() {

        return instance;
    }

    public List<UserInfo.MyData> getDataList() {
        return dataList;
    }


    // You can add more methods as needed
}
