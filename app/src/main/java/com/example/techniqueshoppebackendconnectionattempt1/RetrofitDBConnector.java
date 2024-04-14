package com.example.techniqueshoppebackendconnectionattempt1;

import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class RetrofitDBConnector {

    private Retrofit retrofit;

    private interface RequestUser{
        @GET("/users/{user_name}/{user_password}")
        Call<List<UserInfo.MyData>> getUser(@Path("user_name") String user_name,@Path("user_password") String user_password);
    }

    public interface AddUser {
        @POST("/users")
        Call<List<UserInfo.MyData>> postData(@Body UserInfo.MyData request);
    }

    public RetrofitDBConnector(){
        retrofit = new Retrofit.Builder()
                .baseUrl("http://techniqueshoppe-env.eba-pmmmtci6.us-east-2.elasticbeanstalk.com/users/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


    }

    public void postNewUser(UserInfo.MyData user,LoginActivity.UserCallback callback) {
        AddUser addUser = retrofit.create(AddUser.class);

        addUser.postData(user).enqueue(new Callback<List<UserInfo.MyData>>() {
            @Override
            public void onResponse(Call<List<UserInfo.MyData>> call, Response<List<UserInfo.MyData>> response) {
                if (response.isSuccessful()) {
                    List<UserInfo.MyData> userData = response.body();
                    if (userData != null && userData.size() > 0) {
                        callback.onSuccess(userData);
                    } else {
                        callback.onFailure("No data found");
                    }
                } else {
                    callback.onFailure("Failed to get data. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<UserInfo.MyData>> call, Throwable throwable) {
                callback.onFailure(throwable.getMessage());
            }
        });

    }

    public void getUserData(String userName, String userPassword, LoginActivity.UserCallback callback) {
        RequestUser requestUser = retrofit.create(RequestUser.class);

        requestUser.getUser(userName,userPassword).enqueue(new Callback<List<UserInfo.MyData>>() {
            @Override
            public void onResponse(Call<List<UserInfo.MyData>> call, Response<List<UserInfo.MyData>> response) {
                if (response.isSuccessful()) {
                    List<UserInfo.MyData> userData = response.body();
                    if (userData != null && userData.size() > 0) {
                        callback.onSuccess(userData);
                    } else {
                        callback.onFailure("No data found");
                    }
                } else {
                    callback.onFailure("Failed to get data. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<UserInfo.MyData>> call, Throwable throwable) {
                callback.onFailure(throwable.getMessage());
            }
        });
    }

}
