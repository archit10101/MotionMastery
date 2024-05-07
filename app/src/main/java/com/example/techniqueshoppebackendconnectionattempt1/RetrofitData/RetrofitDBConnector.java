package com.example.techniqueshoppebackendconnectionattempt1.RetrofitData;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.techniqueshoppebackendconnectionattempt1.Fragments.CourseCreator;
import com.example.techniqueshoppebackendconnectionattempt1.Fragments.HomeFragment;
import com.example.techniqueshoppebackendconnectionattempt1.Fragments.SearchFragment;
import com.example.techniqueshoppebackendconnectionattempt1.LoginActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Url;

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

//        @Multipart
//        @POST("/upload") // The endpoint in your Node.js server
//        Call<ResponseBody> uploadFile(@Part MultipartBody.Part file);

        @GET("/download/{key}")
        Call<ResponseBody> downloadFile(@Path("key") String key);

        @GET("/videos/course_id/{courseID}")
        Call<List<VideoTutorial>> getVideoTutorialsByCourseId(@Path("courseID") int courseID);

        @POST("/videos")
        Call<VideoTutorial> createVideoTutorial(@Body VideoTutorial videoTutorial);

        @Headers("Content-Type: application/octet-stream")
        @PUT
        Call<ResponseBody> uploadWithUrlBig(@Url String url, @Part MultipartBody.Part file);

        @Headers("Content-Type: application/octet-stream")
        @PUT
        Call<ResponseBody> uploadWithUrlSmall(@Url String url, @Body RequestBody body);

        @GET("/upload")
        Call<PresignedUrlResponse> getPresignedUpload();

        @GET("/videos/{videoID}")
        Call<Video> getVideoByID(@Path("videoID") int videoID);

        @POST("/create-demo")
        Call<Void> createDemo(@Body DemoInfo demoInfo);

        @GET("/demos/{videoID}")
        Call<List<DemoInfo>> getDemosByVideoID(@Path("videoID") String videoID);
        @PUT
        Call<ResponseBody> uploadFile(@Url String url, @Body RequestBody body);



    }


    public void getVidbyVidID(int vidID, VideoSingleCallback callback) {
        retrofitInterface.getVideoByID(vidID).enqueue(new Callback<Video>() {
            @Override
            public void onResponse(Call<Video> call, Response<Video> response) {
                if (response.isSuccessful()) {
                    Video userData = response.body();
                    if (userData != null) {
                        callback.onSuccess(userData);
                    } else {
                        callback.onFailure("No data found");
                    }
                } else {
                    callback.onFailure("Failed to get data. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Video> call, Throwable throwable) {
                callback.onFailure(throwable.getMessage());
            }
        });
    }

    public interface VideoSingleCallback {
        void onSuccess(Video vid);
        void onFailure(String error);
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
    public void postNewCourse(CourseInfo course, CourseCreator.CourseCallback courseCreatedSuccessfully) {
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

    public void getCoursesByAuthor(String author, CourseCreator.CourseCallback callback) {
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

    public void getCoursesBySearch(String name, CourseCreator.CourseCallback callback) {
        retrofitInterface.searchCoursesByName(name).enqueue(new Callback<List<CourseInfo>>() {
            @Override
            public void onResponse(Call<List<CourseInfo>> call, Response<List<CourseInfo>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CourseInfo> courses = response.body();
                    callback.onSuccess(courses);
                } else {
                    callback.onFailure("Failed to get courses by search");
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

//    public void uploadImage(File file, UploadCallback callback) {
//        if (file.exists()) {
//            byte[] fileBytes = convertFileToByteArray(file);
//
//            // Create a RequestBody instance from the byte array
//            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), fileBytes);
//
//            // MultipartBody.Part is used to send the actual file name along with the request
//            MultipartBody.Part body = MultipartBody.Part.createFormData("file", "image.jpg", requestFile);
//
//            // Call the Retrofit API to upload the file
//            Call<ResponseBody> call = retrofitInterface.uploadFile(body);
//            call.enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                    if (response.isSuccessful() && response.body() != null) {
//                        try {
//                            String uuid = response.body().string();
//                            // Call the callback with the UUID
//                            callback.onUploadSuccess(uuid);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                            callback.onUploadFailure();
//                        }
//                    } else {
//                        // Handle upload error
//                        callback.onUploadFailure();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    // Handle failure
//                    callback.onUploadFailure();
//                }
//            });
//        } else {
//            // File does not exist
//            callback.onUploadFailure();
//        }
//    }
public void getPresigned(UploadCallback callback) {
    Call<PresignedUrlResponse> call =  retrofitInterface.getPresignedUpload();
    call.enqueue(new Callback<PresignedUrlResponse>() {
        @Override
        public void onResponse(@NonNull Call<PresignedUrlResponse> call, @NonNull Response<PresignedUrlResponse> response) {
            if (response.isSuccessful() && response.body() != null) {
                PresignedUrlResponse pur = response.body();
                try {
                    callback.onUploadSuccess(pur);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                callback.onUploadFailure("reponse success: "+ response.isSuccessful()+ "  responsebodynull: "+ response.body().toString());
            }
        }

        @Override
        public void onFailure(Call<PresignedUrlResponse> call, Throwable t) {
            Log.e("Retrofit", "Failed to download file", t);
            callback.onUploadFailure("Failed to download file");
        }
    });
}



    // Callback interface for handling upload result
    public interface UploadCallback {
        void onUploadSuccess(PresignedUrlResponse uploadObject) throws IOException;
        void onUploadFailure(String error);
    }

    public void downloadFile(String key, DownloadCallback callback) {
        Call<ResponseBody> call =  retrofitInterface.downloadFile(key);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String fileContent = null;
                    try {
                        fileContent = response.body().string();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Log.d("here",response.body().toString());
                    callback.onSuccess(fileContent);
                } else {
                    callback.onFailure("failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Retrofit", "Failed to download file", t);
                callback.onFailure("Failed to download file");
            }
        });
    }

    public interface VideoCallback {
        void onSuccess(List<VideoTutorial> fileContent);
        void onFailure(String error);
    }

    public interface DownloadCallback {
        void onSuccess(String fileContent);
        void onFailure(String error);
    }

    private static byte[] convertFileToByteArray(File file) {
        FileInputStream fis = null;
        byte[] byteArray = null;
        try {
            fis = new FileInputStream(file);
            byteArray = new byte[(int) file.length()];
            fis.read(byteArray);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return byteArray;
    }

    public void getVideoTutorialsByCourseId(int courseId, VideoCallback callback) {
        retrofitInterface.getVideoTutorialsByCourseId(courseId).enqueue(new Callback<List<VideoTutorial>>() {
            @Override
            public void onResponse(Call<List<VideoTutorial>> call, Response<List<VideoTutorial>> response) {
                if (response.isSuccessful()) {
                    List<VideoTutorial> videoTutorials = response.body();
                    if (videoTutorials != null && videoTutorials.size() > 0) {
                        callback.onSuccess(videoTutorials);
                    } else {
                        callback.onFailure("No video tutorials found");
                    }
                } else {
                    callback.onFailure("Failed to get video tutorials. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<VideoTutorial>> call, Throwable throwable) {
                callback.onFailure(throwable.getMessage());
            }
        });
    }

    public void createVideoTutorial(VideoTutorial videoTutorial, VideoCallback callback) {
        retrofitInterface.createVideoTutorial(videoTutorial).enqueue(new Callback<VideoTutorial>() {
            @Override
            public void onResponse(Call<VideoTutorial> call, Response<VideoTutorial> response) {
                if (response.isSuccessful()) {
                    VideoTutorial createdVideoTutorial = response.body();
                    if (createdVideoTutorial != null) {
                        ArrayList<VideoTutorial> videoTutorialArrayList = new ArrayList<>();
                        videoTutorialArrayList.add(createdVideoTutorial);
                        callback.onSuccess(videoTutorialArrayList);
                    } else {
                        callback.onFailure("Failed to create video tutorial");
                    }
                } else {
                    callback.onFailure("Failed to create video tutorial. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<VideoTutorial> call, Throwable throwable) {
                callback.onFailure(throwable.getMessage());
            }
        });
    }

    public void uploadWithURI(File file, String preSignedUrl, String contentType) throws IOException {
//        long fileSize = file.length();
//        if (file.available() > 100 * 1024 * 1024) {
//            uploadMultipart(file, preSignedUrl);
//        } else {
//        }
        uploadSingle(file, preSignedUrl, contentType);

    }

//    private void uploadMultipart(InputStream file, String preSignedUrl) {
//        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
//        Call<ResponseBody> call = retrofitInterface.uploadWithUrlBig(preSignedUrl, body);
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                Log.d("uploadMultipart","upload multipart successful");
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Log.d("uploadMultipart","upload multipart failed");
//            }
//        });
//    }

    private void uploadSingle(File file, String preSignedUrl, String contentType) {
        // Create a request body for the entire file
        RequestBody requestBody = RequestBody.create(MediaType.parse(contentType), file);

        // Execute the upload request using the pre-signed URL
        Call<ResponseBody> call = retrofitInterface.uploadWithUrlSmall(preSignedUrl, requestBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("uploadNotMultipart","upload successful");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("uploadNotMultipart","upload failed: "+t);
            }
        });
    }
    private byte[] readStream(InputStream inputStream) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024];
        try {
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toByteArray();
    }
    public void postNewDemo(DemoInfo demo, DemoCallback demoCreatedSuccessfully) {
        retrofitInterface.createDemo(demo).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("Retrofit", "Demo created successfully");
                    demoCreatedSuccessfully.onSuccess();
                } else {
                    Log.e("Retrofit", "Failed to create demo");
                    demoCreatedSuccessfully.onFailure("Failed to create demo");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("Retrofit", "Error creating demo: " + t.getMessage());
                demoCreatedSuccessfully.onFailure(t.getMessage());
            }
        });
    }

    public void getDemosByVideoID(String videoID, DemoCallback callback){
        Call<List<DemoInfo>> call = retrofitInterface.getDemosByVideoID(videoID);

        call.enqueue(new Callback<List<DemoInfo>>() {
            @Override
            public void onResponse(Call<List<DemoInfo>> call, Response<List<DemoInfo>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<DemoInfo> demos = response.body();
                    for (DemoInfo demo : demos) {
                        Log.d("Retrofit", "DemoID: " + demo.getDemoId() + ", VideoID: " + demo.getVideoId());
                    }
                    callback.onSuccess(demos);
                } else {
                    callback.onFailure("Failed to fetch demos");
                    Log.e("Retrofit", "Failed to fetch demos");
                }
            }

            @Override
            public void onFailure(Call<List<DemoInfo>> call, Throwable t) {
                callback.onFailure("Error fetching demos: " + t.getMessage());
                Log.e("Retrofit", "Error fetching demos: " + t.getMessage());
            }
        });
    }

    public interface DemoCallback {
        void onSuccess(List<DemoInfo> demos);
        void onSuccess();

        void onFailure(String errorMessage);
    }



//    public void uploadFileNew(Context context, String presignedUrl, Uri fileUri, ImageView imageView) {
//        File file = new File(fileUri.getPath());
//        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
//
//        imageView.setImageBitmap(bitmap);
//
//        String mimeType = getMimeType(context, fileUri);
//        Log.d("mime",mimeType);
//        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
//
//        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
//
//
//        Call<ResponseBody> call = retrofitInterface.uploadFile(presignedUrl, filePart);
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                // Handle successful upload
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                // Handle upload failure
//            }
//        });
//    }
    public void uploadFileNew(Context context, String presignedUrl, Uri fileUri) {
        File file = new File(Objects.requireNonNull(fileUri.getPath()));



        // Get the MIME type of the file
        String mimeType = getMimeType(context, fileUri);
        Log.d("mime", mimeType);


        // Read the file data into a RequestBody
        RequestBody requestBody = RequestBody.create(MediaType.parse(mimeType), file);

        // Make the upload request using the pre-signed URL
        Call<ResponseBody> call = retrofitInterface.uploadFile(presignedUrl, requestBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("upload","success");
                Toast.makeText(context,"upload successfull",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Handle upload failure
            }
        });
    }

    public void uploadFileNew(Context context, String presignedUrl, File file) {


        // Read the file data into a RequestBody
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);

        // Make the upload request using the pre-signed URL
        Call<ResponseBody> call = retrofitInterface.uploadFile(presignedUrl, requestBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("upload","success: "+presignedUrl);
                Toast.makeText(context,"upload successfull",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("upload","fail");
                Toast.makeText(context,"upload failure",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getMimeType(Context context,Uri uri) {
        String mimeType;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = context.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase());
        }
        return mimeType;
    }
}