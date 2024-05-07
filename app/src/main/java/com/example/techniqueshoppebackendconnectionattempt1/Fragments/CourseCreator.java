package com.example.techniqueshoppebackendconnectionattempt1.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.techniqueshoppebackendconnectionattempt1.R;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.CourseInfo;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.PresignedUrlResponse;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.RetrofitDBConnector;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.UserInfo;
import com.example.techniqueshoppebackendconnectionattempt1.RetrofitData.UserInfoSingleton;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class CourseCreator extends AppCompatActivity {

    private Context context;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PERMISSION_REQUEST_CODE = 2;

    private RetrofitDBConnector rdbc;
    private ImageView imageView;

    public interface CourseCallback {
        void onSuccess(List<CourseInfo> courses);
        void onFailure(String error);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_creator);

        context = this;
        rdbc = new RetrofitDBConnector();

        imageView = findViewById(R.id.imageCourse);
        ImageView imageClose = findViewById(R.id.image_close);
        imageClose.setOnClickListener(v ->{
            finish();
        });

        Button create = findViewById(R.id.button_create_course);
        create.setOnClickListener(v ->{
            createCourse();

        });

        Button upload = findViewById(R.id.button_upload_image);
        upload.setOnClickListener(v ->{
            Log.d("d","HERE");
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            } else {
                openImagePicker();
            }

        });
    }

    private void openImagePicker() {
        Log.d("I am here","This is openImagePicker");
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    String courseImgPath;
    private void createCourse() {
        TextInputEditText editCourseName = findViewById(R.id.edit_course_name);
        TextInputEditText editCourseDescription = findViewById(R.id.edit_course_description);

        String courseName = editCourseName.getText().toString();
        String courseDescription = editCourseDescription.getText().toString();

        if (!courseName.isEmpty() && !courseDescription.isEmpty()) {
            int authorId = UserInfoSingleton.getInstance().getDataList().get(0).getUserID();
            Log.d("id",authorId+"");
            String authorName = UserInfoSingleton.getInstance().getDataList().get(0).getUserName();
            Log.d("course name",UserInfoSingleton.getInstance().getDataList().get(0).getUserImagePath());
            CourseInfo courseInfo = new CourseInfo(0, courseName, courseDescription, authorName, authorId, UserInfoSingleton.getInstance().getDataList().get(0).getUserImagePath(), courseImgPath);

            rdbc.postNewCourse(courseInfo, new CourseCallback() {
                @Override
                public void onSuccess(List<CourseInfo> courses) {

                }

                @Override
                public void onFailure(String error) {
                    Toast.makeText(context, "Failed to create course: " + error, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(context, "Please enter all the information", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, open image picker
                openImagePicker();
            } else {
                Toast.makeText(context, "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private File file;
    private String uuid;
    private String urlPresigned;

    private Uri resultUri;

    private InputStream inputStream;
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK )
        {
            Log.d("here","here");
            Uri imageUri = data.getData();

            if (imageUri != null){
                Intent intent = new Intent(CourseCreator.this,CropperActivity.class);
                intent.putExtra("DATA",imageUri.toString());
                startActivityForResult(intent,101001);
            }


        }else if (requestCode == 101001 && resultCode == -1 && data != null) {
            // Handle cropped image result

            String result = data.getStringExtra("RESULT");
            resultUri = null;
            if (result != null) {
                resultUri = Uri.parse(result);
                Log.d("here",resultUri.toString());
            }
            imageView.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(result)
                    .placeholder(R.drawable.loading)
                    .transform(new CropCircleTransformation())
                    .into(imageView);

            inputStream = convertUriToInputStream(resultUri);
            if (inputStream != null){
                rdbc.getPresigned(new RetrofitDBConnector.UploadCallback() {
                    @Override
                    public void onUploadSuccess(PresignedUrlResponse uploadObject) {
                        uuid = uploadObject.getUuid();
                        urlPresigned = uploadObject.getUrl();
                        Log.d("uuid", uuid);
                        Log.d("urlPresigned", urlPresigned);
                        rdbc.uploadFileNew(CourseCreator.this,urlPresigned,resultUri);

                        courseImgPath = (uuid);

                    }

                    @Override
                    public void onUploadFailure(String error) {
                        Log.d("imageError", error);
                    }

                });

            }

        }
    }

    private InputStream convertUriToInputStream(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            return inputStream;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }





}