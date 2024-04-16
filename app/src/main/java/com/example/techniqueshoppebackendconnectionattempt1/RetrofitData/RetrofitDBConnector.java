package com.example.techniqueshoppebackendconnectionattempt1.RetrofitData;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;

import com.example.techniqueshoppebackendconnectionattempt1.Fragments.CreateDialog;
import com.example.techniqueshoppebackendconnectionattempt1.Fragments.HomeFragment;
import com.example.techniqueshoppebackendconnectionattempt1.Fragments.SearchFragment;
import com.example.techniqueshoppebackendconnectionattempt1.LoginActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public class RetrofitDBConnector {

    private Retrofit retrofit;

    RetrofitInterface retrofitInterface;

    private interface RetrofitInterface{
        @POST("/users")
        Call<List<UserInfo.MyData>> postData(@Body UserInfo.MyData request);

        @GET("/users/{user_name}/{user_password}")
        Call<List<UserInfo.MyData>> getUser(@Path("user_name") String user_name, @Path("user_password") String user_password);

        @GET("/courses")
        Call<List<CourseInfo>> getCourses();

        @GET("/courses/courseID/{id}")
        Call<List<CourseInfo>> getCourseById(@Path("id") String courseId);

        @POST("/courses")
        Call<CourseInfo> addCourse(@Body CourseInfo courseInfo);

        @GET("/courses/tag/{tagName}")
        Call<List<CourseInfo>> getCourseByTag(@Path("tagName") String tagName);

        @GET("/courses/search/{name}")
        Call<List<CourseInfo>> searchCoursesByName(@Path("name") String name);

        @GET("/courses/author/{author}")
        Call<List<CourseInfo>> getCoursesByAuthor(@Path("author") String author);

        @POST("/enroll-course/{userID}/{courseID}")
        Call<Void> enrollCourse(
                @Path("userID") String userID,
                @Path("courseID") String courseID
        );

        @GET("/enrolled-courses/{userID}/{courseID}")
        Call<UserEnrolledCourse> getEnrolledCourse(
                @Path("userID") String userID,
                @Path("courseID") String courseID
        );

        @PUT("/users/{userId}")
        Call<UserInfo.MyData> updateUserData(@Path("userId") String userId, @Body UserInfo.MyData userData);

        @GET("/enrolled-courses/{userID}")
        Call<List<CourseInfo>> getEnrolledCourses(@Path("userID") int userID);


    }


    public RetrofitDBConnector(){
        retrofit = new Retrofit.Builder()
                .baseUrl("http://techniqueshoppe-env.eba-pmmmtci6.us-east-2.elasticbeanstalk.com/users/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);


    }

    public void postNewUser(UserInfo.MyData user, LoginActivity.UserCallback callback) {
        retrofitInterface.postData(user).enqueue(new Callback<List<UserInfo.MyData>>() {
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
        retrofitInterface.getUser(userName,userPassword).enqueue(new Callback<List<UserInfo.MyData>>() {
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

    public void getCourseByCourseID(int courseID,SearchFragment.CourseCallback callback){
        Call<List<CourseInfo>> call = retrofitInterface.getCourseById(""+courseID);

        // Execute the call asynchronously
        call.enqueue(new Callback<List<CourseInfo>>() {
            @Override
            public void onResponse(Call<List<CourseInfo>> call, Response<List<CourseInfo>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CourseInfo> courses = response.body();
                    for (CourseInfo course : courses) {
                        Log.d("Course", "ID: " + course.getCourseId() + ", Name: " + course.getCourseName());
                    }
                    callback.onSuccess(courses);
                } else {
                    Log.e("API Error", "Failed to fetch courses");
                    callback.onFailure("failed");
                }
            }

            @Override
            public void onFailure(Call<List<CourseInfo>> call, Throwable t) {
                Log.e("API Error", "Error fetching courses: " + t.getMessage());
                callback.onFailure(t.getMessage());
            }
        });
    }

    public void getCourseByTag(String tag, SearchFragment.CourseCallback callback){
        Call<List<CourseInfo>> call = retrofitInterface.getCourseByTag(tag);

        // Execute the call asynchronously
        call.enqueue(new Callback<List<CourseInfo>>() {
            @Override
            public void onResponse(Call<List<CourseInfo>> call, Response<List<CourseInfo>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CourseInfo> courses = response.body();
                    for (CourseInfo course : courses) {
                        Log.d("Course", "ID: " + course.getCourseId() + ", Name: " + course.getCourseName());
                    }
                    callback.onSuccess(courses);

                } else {
                    callback.onFailure("failure");

                    Log.e("API Error", "Failed to fetch courses");
                }
            }

            @Override
            public void onFailure(Call<List<CourseInfo>> call, Throwable t) {
                callback.onFailure(t.getMessage());

                Log.e("API Error", "Error fetching courses: " + t.getMessage());
            }
        });
    }
    public void postNewCourse(CourseInfo course, CreateDialog.CourseCallback courseCreatedSuccessfully) {
        retrofitInterface.addCourse(course).enqueue(new Callback<CourseInfo>() {
            @Override
            public void onResponse(Call<CourseInfo> call, Response<CourseInfo> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CourseInfo> newList = new ArrayList<>();

                    CourseInfo addedCourse = response.body();
                    newList.add(addedCourse);
                    Log.d("Added Course", "ID: " + addedCourse.getCourseId() + ", Name: " + addedCourse.getCourseName());
                    courseCreatedSuccessfully.onSuccess(newList );
                } else {
                    Log.e("API Error", "Failed to add course");
                    courseCreatedSuccessfully.onFailure("failed");
                }
            }

            @Override
            public void onFailure(Call<CourseInfo> call, Throwable t) {
                Log.e("API Error", "Error adding course: " + t.getMessage());
                courseCreatedSuccessfully.onFailure(t.getMessage());

            }
        });
    }

    public void getCoursesByAuthor(String author, CreateDialog.CourseCallback callback) {
        retrofitInterface.getCoursesByAuthor(author).enqueue(new Callback<List<CourseInfo>>() {
            @Override
            public void onResponse(Call<List<CourseInfo>> call, Response<List<CourseInfo>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CourseInfo> courses = response.body();
                    callback.onSuccess(courses);
                } else {
                    callback.onFailure("Failed to get courses by author");
                }
            }

            @Override
            public void onFailure(Call<List<CourseInfo>> call, Throwable t) {
                callback.onFailure(t.getMessage());
            }
        });
    }
    public void enrollCourse(String userID, String courseID, EnrollCallback callback) {
        retrofitInterface.enrollCourse(userID, courseID).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("Retrofit", "User enrolled in course successfully");
                    callback.onSuccess();
                } else {
                    Log.e("Retrofit", "Failed to enroll user in course. Code: " + response.code());
                    callback.onFailure("Failed to enroll user in course");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("Retrofit", "Error enrolling user in course", t);
                callback.onFailure("Error enrolling user in course");
            }
        });
    }

    public interface EnrollCallback {
        void onSuccess();
        void onFailure(String error);
    }
    public void getEnrolledCourse(String userID, String courseID, EnrolledCourseCallback callback) {
        retrofitInterface.getEnrolledCourse(userID, courseID).enqueue(new Callback<UserEnrolledCourse>() {
            @Override
            public void onResponse(Call<UserEnrolledCourse> call, Response<UserEnrolledCourse> response) {
                if (response.isSuccessful()) {
                    UserEnrolledCourse enrolledCourse = response.body();
                    if (enrolledCourse != null) {
                        callback.onSuccess(enrolledCourse);
                    } else {
                        callback.onFailure("No data found");
                    }
                } else {
                    callback.onFailure("Failed to get enrolled course. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<UserEnrolledCourse> call, Throwable t) {
                Log.e("Retrofit", "Error getting enrolled course", t);
                callback.onFailure("Error getting enrolled course");
            }
        });
    }

    public interface EnrolledCourseCallback {
        void onSuccess(UserEnrolledCourse enrolledCourse);
        void onFailure(String error);
    }

    public void updateUserData(String userId, UserInfo.MyData updatedData) {
        Call<UserInfo.MyData> call = retrofitInterface.updateUserData(userId, updatedData);
        call.enqueue(new Callback<UserInfo.MyData>() {
            @Override
            public void onResponse(Call<UserInfo.MyData> call, Response<UserInfo.MyData> response) {
                if (response.isSuccessful()) {
                    UserInfo.MyData updatedUserData = response.body();
                    if (updatedUserData != null) {
                        ArrayList<UserInfo.MyData> list= new ArrayList<>();
                        list.add(updatedUserData);
                        UserInfoSingleton userInfoSingleton = UserInfoSingleton.getInstance(list);
                    } else {
                        onFailure(call, new Throwable("Empty response body"));
                    }
                } else {
                    onFailure(call, new Throwable("Failed to update data. Code: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<UserInfo.MyData> call, Throwable t) {
                // Handle failure
                Log.e(TAG, "Failed to update user data", t);
            }
        });
    }

    public void getEnrolledCoursesByUserID(int userID, HomeFragment.EnrolledCoursesCallback callback) {
        Call<List<CourseInfo>> call = retrofitInterface.getEnrolledCourses(userID);
        call.enqueue(new Callback<List<CourseInfo>>() {
            @Override
            public void onResponse(Call<List<CourseInfo>> call, Response<List<CourseInfo>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CourseInfo> enrolledCourses = response.body();
                    for (CourseInfo course : enrolledCourses) {
                        Log.d("Course", "ID: " + course.getCourseId() + ", Name: " + course.getCourseName());
                    }
                    callback.onSuccess(enrolledCourses);
                } else {
                    Log.e("API Error", "Failed to fetch enrolled courses");
                    callback.onFailure("Failed to fetch enrolled courses");
                }
            }

            @Override
            public void onFailure(Call<List<CourseInfo>> call, Throwable t) {
                Log.e("API Error", "Error fetching enrolled courses: " + t.getMessage());
                callback.onFailure("Error fetching enrolled courses: " + t.getMessage());
            }
        });
    }


}
